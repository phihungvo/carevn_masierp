package com.masi.employee.repository;

import com.masi.employee.domain.InterviewSchedule;
import com.masi.employee.repository.rowmapper.EmployeeRowMapper;
import com.masi.employee.repository.rowmapper.InterviewScheduleRowMapper;
import com.masi.employee.repository.rowmapper.RecruitmentRequestRowMapper;
import com.masi.employee.service.dto.InterviewScheduleQuery;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;

import java.util.*;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.r2dbc.convert.R2dbcConverter;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.repository.support.SimpleR2dbcRepository;
import org.springframework.data.relational.core.sql.*;
import org.springframework.data.relational.core.sql.SelectBuilder.SelectFromAndJoinCondition;
import org.springframework.data.relational.repository.support.MappingRelationalEntityInformation;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.r2dbc.core.RowsFetchSpec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

/**
 * Spring Data R2DBC custom repository implementation for the InterviewSchedule entity.
 */
@SuppressWarnings("unused")
class InterviewScheduleRepositoryInternalImpl
    extends SimpleR2dbcRepository<InterviewSchedule, UUID>
    implements InterviewScheduleRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final RecruitmentRequestRowMapper recruitmentrequestMapper;
    private final InterviewScheduleRowMapper interviewscheduleMapper;
    private final EmployeeRowMapper employeeMapper;
    private static final Table entityTable = Table.aliased("interview_schedule", EntityManager.ENTITY_ALIAS);
    private static final Table recruitmentRequestTable = Table.aliased("recruitment_request", "recruitmentRequest");
    private static final Table employeeTable = Table.aliased("employee", "em");

    public InterviewScheduleRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        RecruitmentRequestRowMapper recruitmentrequestMapper,
        InterviewScheduleRowMapper interviewscheduleMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter, EmployeeRowMapper employeeMapper
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(InterviewSchedule.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.recruitmentrequestMapper = recruitmentrequestMapper;
        this.interviewscheduleMapper = interviewscheduleMapper;
        this.employeeMapper = employeeMapper;
    }

    @Override
    public Flux<InterviewSchedule> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<InterviewSchedule> createQuery(Pageable pageable, Condition whereClause) {
        return this.createQuery(pageable, whereClause, null);

    }

    RowsFetchSpec<InterviewSchedule> createQuery(Pageable pageable, Condition whereClause, Map<String, Object> params) {
        List<Expression> columns = InterviewScheduleSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(RecruitmentRequestSqlHelper.getColumns(recruitmentRequestTable, "recruitmentRequest"));
        columns.addAll(EmployeeSqlHelper.getColumns(employeeTable, "em"));
        SelectBuilder.SelectFromAndJoinCondition selectFrom = Select.builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(recruitmentRequestTable)
            .on(Column.create("recruitment_request_id", entityTable))
            .equals(Column.create("id", recruitmentRequestTable))
            .leftOuterJoin(employeeTable)
            .on(Column.create("interviewer_id", entityTable))
            .equals(Column.create("id", employeeTable));


        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, InterviewSchedule.class, pageable, whereClause);
        var query = db.sql(select);
        if (params != null && !params.isEmpty()) {
            query = query.bindValues(params);
        }
        return query.map(this::process);
    }

    @Override
    public Flux<InterviewSchedule> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<InterviewSchedule> findById(UUID id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(StringUtils.wrap(id.toString(), "'")));
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
    public Flux<InterviewSchedule> findByQuery(InterviewScheduleQuery query, Pageable pageable) {
        pageable = getDefaultSort(pageable);
        var conditionParams = buildConditionFromRO(query);
        return createQuery(pageable, conditionParams.getT1(), conditionParams.getT2()).all();
    }

    @Override
    public Mono<Long> countByQuery(InterviewScheduleQuery query) {
        var conditionParams = buildConditionFromRO(query);
        return createQuery(null, conditionParams.getT1(), conditionParams.getT2()).all().count();
    }

    private Tuple2<Condition, Map<String, Object>> buildConditionFromRO(InterviewScheduleQuery ro) {
        Condition condition = Conditions.isEqual(entityTable.column("is_deleted"), Conditions.just("false"));
        condition = condition.and(Conditions.isEqual(recruitmentRequestTable.column("is_deleted"), Conditions.just("false")));
        Map<String, Object> params = new HashMap<>();
        if (StringUtils.isNotBlank(ro.getSearch())) {
            var columnAlias = EntityManager.ENTITY_ALIAS + ".candidate_name";
            String likeClause = String.format("unaccent(%s) ILIKE unaccent(:search)", columnAlias);
            condition = condition.and(Conditions.just(likeClause));
            params.put("search", "%" + ro.getSearch() + "%");
        }
        if (ro.getInterviewResult() != null && !ro.getInterviewResult().isEmpty()) {
            String inClause = "(" + ro.getInterviewResult().stream().map(e -> "'" + e + "'").reduce((a, b) -> a + "," + b).orElseThrow() + ")";
            condition = condition.and(Conditions.in(entityTable.column("interview_result"), Conditions.just(inClause)));
        }
        if (ro.getProcess() != null && !ro.getProcess().isEmpty()) {
            String inClause = "(" + ro.getProcess().stream().map(e -> "'" + e + "'").reduce((a, b) -> a + "," + b).orElseThrow() + ")";
            condition = condition.and(Conditions.in(entityTable.column("process"), Conditions.just(inClause)));
        }
        if (ro.getRecruitmentId() != null) {
            condition = condition.and(Conditions.isEqual(entityTable.column("recruitment_request_id"), Conditions.just("'" +ro.getRecruitmentId()+ "'")));
        }
        if (ro.getCompany() != null) {
            condition = condition.and(Conditions.isEqual(recruitmentRequestTable.column("company"), Conditions.just("'" + ro.getCompany() + "'")));
        }
        return Tuples.of(condition, params);

    }

    private InterviewSchedule process(Row row, RowMetadata metadata) {
        InterviewSchedule entity = interviewscheduleMapper.apply(row, "e");
        entity.setRecruitmentRequest(recruitmentrequestMapper.apply(row, "recruitmentRequest"));
        entity.setInterviewer(employeeMapper.apply(row, "em"));
        return entity;
    }

    @Override
    public <S extends InterviewSchedule> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
