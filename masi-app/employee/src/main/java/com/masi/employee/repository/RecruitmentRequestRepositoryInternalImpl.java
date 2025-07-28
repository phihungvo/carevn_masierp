package com.masi.employee.repository;

import com.masi.employee.domain.InterviewSchedule;
import com.masi.employee.domain.RecruitmentRequest;
import com.masi.employee.repository.rowmapper.EmployeeRowMapper;
import com.masi.employee.repository.rowmapper.InterviewScheduleRowMapper;
import com.masi.employee.repository.rowmapper.RecruitmentRequestRowMapper;
import com.masi.employee.repository.rowmapper.WorkspaceRowMapper;
import com.masi.employee.service.dto.RecruitmentRequestRO;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;

import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.r2dbc.convert.R2dbcConverter;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.repository.support.SimpleR2dbcRepository;
import org.springframework.data.relational.core.sql.*;
import org.springframework.data.relational.core.sql.SelectBuilder.SelectFromAndJoin;
import org.springframework.data.relational.core.sql.SelectBuilder.SelectFromAndJoinCondition;
import org.springframework.data.relational.repository.support.MappingRelationalEntityInformation;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.r2dbc.core.RowsFetchSpec;
import org.springframework.util.CollectionUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

/**
 * Spring Data R2DBC custom repository implementation for the RecruitmentRequest entity.
 */
@SuppressWarnings("unused")
class RecruitmentRequestRepositoryInternalImpl
    extends SimpleR2dbcRepository<RecruitmentRequest, UUID>
    implements RecruitmentRequestRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final RecruitmentRequestRowMapper recruitmentrequestMapper;
    private final InterviewScheduleRowMapper interviewscheleMapper;
    private final EmployeeRowMapper employeeMapper;
    private final WorkspaceRowMapper workspaceMapper;
    private static final Table entityTable = Table.aliased("recruitment_request", EntityManager.ENTITY_ALIAS);
    private static final Table imTable = Table.aliased("interview_schedule", "interviewSchedule");
    private static final Table workspaceTable = Table.aliased("workspace", "workspace");
    private static final Table employeeTable = Table.aliased("employee", "employee");

    public RecruitmentRequestRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        RecruitmentRequestRowMapper recruitmentrequestMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter, InterviewScheduleRowMapper interviewscheleMapper, EmployeeRowMapper employeeMapper,
        WorkspaceRowMapper workspaceMapper
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(RecruitmentRequest.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.recruitmentrequestMapper = recruitmentrequestMapper;
        this.interviewscheleMapper = interviewscheleMapper;
        this.employeeMapper = employeeMapper;
        this.workspaceMapper = workspaceMapper;
    }

    @Override
    public Flux<RecruitmentRequest> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<RecruitmentRequest> createQuery(Pageable pageable, Condition whereClause) {
        return this.createQuery(pageable, whereClause, null);
    }

    RowsFetchSpec<RecruitmentRequest> createQuery(Pageable pageable, Condition whereClause, Map<String, Object> params) {
        List<Expression> columns = RecruitmentRequestSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(WorkspaceSqlHelper.getColumns(workspaceTable, "workspace"));
        columns.addAll(EmployeeSqlHelper.getColumns(employeeTable, "employee"));
        SelectFromAndJoinCondition selectFrom = Select.builder()
            .select(columns)
            .from(entityTable)

            .leftOuterJoin(workspaceTable)
            .on(entityTable.column("department_id")
                .isEqualTo(workspaceTable.column("id")))

            .leftOuterJoin(employeeTable)
            .on(entityTable.column("replace_for_id")
                .isEqualTo(employeeTable.column("id")));


        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, RecruitmentRequest.class, pageable, whereClause);
        DatabaseClient.GenericExecuteSpec query = db.sql(select);
        if (params != null && !params.isEmpty()) {
            query = query.bindValues(params);
        }
        return query.map(this::process);
    }

    @Override
    public Flux<RecruitmentRequest> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<RecruitmentRequest> findByIdAndNotDelete(UUID id) {
        Condition whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(StringUtils.wrap(id.toString(), "'")));
        whereClause = whereClause.and(Conditions.isEqual(entityTable.column("is_deleted"), Conditions.just("false")));
        return createQuery(null, whereClause).one();
    }

    private Pageable getDefaultSort(Pageable pageable) {
        Sort defaultSort = Sort.by(Sort.Direction.DESC, "created_date");
        if (Objects.isNull(pageable)) {
            pageable = PageRequest.of(0, Integer.MAX_VALUE, defaultSort);
        }
        if (pageable.getSort().equals(Sort.unsorted())) {
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), defaultSort);
        }
        return pageable;
    }

    @Override
    public Flux<RecruitmentRequest> findAllByFilter(RecruitmentRequestRO moRO, Pageable pageable) {
        pageable = getDefaultSort(pageable);
        Tuple2<Condition, Map<String, Object>> whereTuple = buildWhereClause(moRO);
        return createQuery(pageable, whereTuple.getT1(), whereTuple.getT2()).all();
    }

    @Override
    public Mono<Long> countAllByFilter(RecruitmentRequestRO ro) {
        Tuple2<Condition, Map<String, Object>> whereTuple = buildWhereClause(ro);
        return createQuery(null, whereTuple.getT1(), whereTuple.getT2())
            .all().count();
    }


    private RecruitmentRequest mergeRecruitmentRequests(RecruitmentRequest existing, RecruitmentRequest replacement) {
        Set<InterviewSchedule> uniqueInterviewSchedules = new HashSet<>(existing.getInterviewSchedules());
        uniqueInterviewSchedules.addAll(replacement.getInterviewSchedules());
        existing.setInterviewSchedules(uniqueInterviewSchedules);
        return existing;
    }


    private RecruitmentRequest process(Row row, RowMetadata metadata) {
        RecruitmentRequest entity = recruitmentrequestMapper.apply(row, "e");
        entity.setDepartment(workspaceMapper.apply(row, "workspace"));
        entity.setReplaceFor(employeeMapper.apply(row, "employee"));
        return entity;
    }

    @Override
    public <S extends RecruitmentRequest> Mono<S> save(S entity) {
        return super.save(entity);
    }

    private Tuple2<Condition, Map<String, Object>> buildWhereClause(RecruitmentRequestRO RO) {
        Condition whereClause = Conditions.isEqual(entityTable.column("is_deleted"), Conditions.just("false"));
//        whereClause = whereClause.and(Conditions.isEqual(imTable.column("is_deleted"),Conditions.just("false")));


        Map<String, Object> params = new HashMap<>();

        if (Objects.nonNull(RO)) {
            if (Objects.nonNull(RO.getDepartmentId())) {
                whereClause = whereClause.and(Conditions.isEqual(entityTable.column("department_id"), Conditions.just(StringUtils.wrap(RO.getDepartmentId().toString(), "'"))));
            }
            if (Objects.nonNull(RO.getCompany())) {
                whereClause = whereClause.and(Conditions.isEqual(entityTable.column("company"), Conditions.just(StringUtils.wrap(RO.getCompany(), "'"))));
            }

            if (Objects.nonNull(RO.getPosition()) && !RO.getPosition().isEmpty()) {
                String inClause = RO.getPosition().stream().map(e -> "'" + e + "'")
                    .reduce((a, b) -> a + "," + b).orElseThrow();
                whereClause = whereClause.and(Conditions.in(entityTable.column("position"), Conditions.just(inClause)));
            }

            if (Objects.nonNull(RO.getStatus()) && !RO.getStatus().isEmpty()) {
                String inClause = RO.getStatus().stream().map(e -> "'" + e + "'")
                    .reduce((a, b) -> a + "," + b).orElseThrow();
                whereClause = whereClause.and(Conditions.in(entityTable.column("status"), Conditions.just(inClause)));
            }
            if (StringUtils.isNotBlank(RO.getSearch())) {
                List<String> columns = Arrays.asList(
                    "workspace.name",
                    EntityManager.ENTITY_ALIAS + ".job_title"
                );
                Condition searchCondition;
                StringBuilder inCondition = new StringBuilder("(");
                columns.forEach(column -> {
                    inCondition.append(String.format("unaccent(%s) iLIKE unaccent(:searchString)", column));
                    inCondition.append(" OR ");
                });
                int length = inCondition.length();
                inCondition.delete(length - 4, length);
                inCondition.append(")");
                searchCondition = Conditions.just(inCondition.toString());
                whereClause = whereClause.and(searchCondition);

                params.put("searchString", "%" + RO.getSearch() + "%");
            }
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            if (RO.getFromDate() != null) {
                whereClause = whereClause.and(Conditions.isGreaterOrEqualTo(entityTable.column("created_date"), Conditions.just(StringUtils.wrap(RO.getFromDate().format(formatter), "'"))));
            }
            if (RO.getToDate() != null) {
                whereClause = whereClause.and(Conditions.isLessOrEqualTo(entityTable.column("created_date"), Conditions.just(StringUtils.wrap(RO.getToDate().format(formatter), "'"))));
            }
            if (StringUtils.isNotBlank(RO.getCompany())) {
                whereClause = whereClause.and(Conditions.isEqual(entityTable.column("company"), Conditions.just(StringUtils.wrap(RO.getCompany(), "'"))));
            }
        }

        return Tuples.of(whereClause, params);
    }


}
