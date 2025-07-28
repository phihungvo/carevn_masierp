package com.masi.employee.repository;

import com.masi.employee.domain.TimeKeepingExplanation;
import com.masi.employee.repository.rowmapper.ColumnConverter;
import com.masi.employee.repository.rowmapper.EmployeeRowMapper;
import com.masi.employee.repository.rowmapper.TimeKeepingExplanationRowMapper;
import com.masi.employee.service.dto.ExplanationRO;
import com.masi.employee.service.dto.TimeKeepingExplanationFilter;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.r2dbc.convert.R2dbcConverter;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.repository.support.SimpleR2dbcRepository;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Comparison;
import org.springframework.data.relational.core.sql.Condition;
import org.springframework.data.relational.core.sql.Conditions;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Select;
import org.springframework.data.relational.core.sql.SelectBuilder.SelectFromAndJoinCondition;
import org.springframework.data.relational.core.sql.Table;
import org.springframework.data.relational.repository.support.MappingRelationalEntityInformation;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.r2dbc.core.RowsFetchSpec;
import org.springframework.util.CollectionUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.service.ConditionBuilder;

/**
 * Spring Data R2DBC custom repository implementation for the TimeKeepingExplanation entity.
 */
@SuppressWarnings("unused")
class TimeKeepingExplanationRepositoryInternalImpl
    extends SimpleR2dbcRepository<TimeKeepingExplanation, UUID>
    implements TimeKeepingExplanationRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final EmployeeRowMapper employeeMapper;
    private final TimeKeepingExplanationRowMapper timekeepingexplanationMapper;


    private final ColumnConverter columnConverter;

    private static final Table entityTable = Table.aliased("time_keeping_explanation", EntityManager.ENTITY_ALIAS);
    private static final Table employeeTable = Table.aliased("employee", "employee");
    private static final Table profileTable = Table.aliased("employee_profile", "profile");

    public TimeKeepingExplanationRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        EmployeeRowMapper employeeMapper,
        TimeKeepingExplanationRowMapper timekeepingexplanationMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter,
        ColumnConverter columnConverter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(TimeKeepingExplanation.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.employeeMapper = employeeMapper;
        this.timekeepingexplanationMapper = timekeepingexplanationMapper;
        this.columnConverter = columnConverter;
    }

    @Override
    public Flux<TimeKeepingExplanation> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    private Pageable getDefaultSortIfUnsorted(Pageable pageable) {
        Sort defaultSort = Sort.by(Sort.Direction.DESC, "created_at");
        if (Objects.isNull(pageable)) {
            pageable = PageRequest.of(0, Integer.MAX_VALUE, defaultSort);
        }
        if (pageable.getSort().equals(Sort.unsorted())) {
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), defaultSort);
        }
        return pageable;
    }

    @Override
    public Flux<TimeKeepingExplanation> findAllByIsActive(Boolean isActive, Pageable pageable) {
        pageable = getDefaultSortIfUnsorted(pageable);
        Condition where = isActive == null ? null : Conditions.isEqual(entityTable.column("is_active"), Conditions.just(StringUtils.wrap(isActive.toString(), "'")));
        return createQuery(pageable, where).all();
    }

    RowsFetchSpec<TimeKeepingExplanation> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = TimeKeepingExplanationSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(EmployeeSqlHelper.getColumns(employeeTable, "employee"));
        columns.addAll(EmployeeProfileSqlHelper.getColumns(profileTable, "profile"));
        SelectFromAndJoinCondition selectFrom = Select.builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(employeeTable)
            .on(Column.create("employee_id", entityTable))
            .equals(Column.create("id", employeeTable))
            .leftOuterJoin(profileTable)
            .on(Column.create("id", employeeTable))
            .equals(Column.create("id", profileTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, TimeKeepingExplanation.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<TimeKeepingExplanation> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<TimeKeepingExplanation> findById(UUID id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(StringUtils.wrap(id.toString(), "'")));
        return createQuery(null, whereClause).one();
    }

    @Override
    public Mono<TimeKeepingExplanation> findByIdAndIsActive(UUID id, Boolean isActive) {
        Condition matchId = Conditions.isEqual(entityTable.column("id"), Conditions.just(StringUtils.wrap(id.toString(), "'")));
        Condition matchIsActive = Conditions.isEqual(entityTable.column("is_active"), Conditions.just(StringUtils.wrap(BooleanUtils.toStringTrueFalse(isActive), "'")));
        return createQuery(null, matchId.and(matchIsActive)).one();
    }

    private TimeKeepingExplanation process(Row row, RowMetadata metadata) {
        TimeKeepingExplanation entity = timekeepingexplanationMapper.apply(row, "e");
        entity.setEmployee(employeeMapper.apply(row, "employee"));
        return entity;
    }

    @Override
    public <S extends TimeKeepingExplanation> Mono<S> save(S entity) {
        return super.save(entity);
    }

    @Override
    public Mono<Long> countAllByIsActive(Boolean isActive) {
        Condition where = isActive == null ? null : Conditions.isEqual(entityTable.column("is_active"), Conditions.just(StringUtils.wrap(isActive.toString(), "'")));
        return createQuery(null, where).all().count();
    }

    @Override
    public Mono<Long> countAllByFilter(ExplanationRO ro) {
        return createQuery(null, buildConditionFromRequestObject(ro))
            .all()
            .count()
            .doOnError(throwable -> new RuntimeException(throwable));
    }

    @Override
    public Flux<TimeKeepingExplanation> findAllByFilter(Pageable pageable, ExplanationRO filter) {
        pageable = getDefaultSortIfUnsorted(pageable);
        return createQuery(pageable, buildConditionFromRequestObject(filter)).all();
    }

    private Condition buildConditionFromRequestObject(ExplanationRO ro) {
        Condition whereClause = Conditions.isEqual(entityTable.column("is_active"), Conditions.just("true"));

        if (Objects.nonNull(ro)) {
            //append where clause with date between if RO fromDate and toDate are not null
            if (Objects.nonNull(ro.getFromDate()) && Objects.nonNull(ro.getToDate())) {
                whereClause = whereClause.and(Conditions.between(entityTable.column("created_at"),
                    Conditions.just(StringUtils.wrap(ro.getFromDate().toString(), "'")),
                    Conditions.just(StringUtils.wrap(ro.getToDate().toString(), "'"))));
            }
            //append where clause with isAbnormal if RO isAbnormal is not null

            if (Objects.nonNull(ro.getCompany())) {
                whereClause = whereClause.and(Conditions.isEqual(entityTable.column("company"), Conditions.just(StringUtils.wrap(ro.getCompany(), "'"))));
            }

            if (Objects.nonNull(ro.getAbnormal())) {
                whereClause = whereClause.and(Conditions.isEqual(entityTable.column("is_abnormal"), Conditions.just(StringUtils.wrap(ro.getAbnormal().toString(), "'"))));
            }
            //append where clause with isLocked if RO isLocked is not null
            if (Objects.nonNull(ro.getLocked())) {
                whereClause = whereClause.and(Conditions.isEqual(entityTable.column("is_locked"), Conditions.just(StringUtils.wrap(ro.getLocked().toString(), "'"))));
            }
            //append where clause with search string if column 'name' contain search string
            if (StringUtils.isNotBlank(ro.getSearchString())) {
                String columnAlias = EntityManager.ENTITY_ALIAS + ".explanation";
                String inCondition = "(unaccent(" + columnAlias + ") iLIKE unaccent('%" + ro.getSearchString() + "%')";
                String fullNameAlias = profileTable.column("full_name").toString();
                inCondition += " OR unaccent(" + fullNameAlias + ") iLIKE unaccent('%" + ro.getSearchString() + "%'))";
                whereClause = whereClause.and(Conditions.just(inCondition));
            }
            if (ro.getEmployeeIds() != null && !ro.getEmployeeIds().isEmpty()) {
                var inCondition = ro.getEmployeeIds().stream().map(id -> "'" + id + "'").collect(Collectors.joining(","));
                whereClause = whereClause.and(Conditions.in(employeeTable.column("id"), Conditions.just(inCondition)));
            }
            if (ro.getStartFrom() != null && ro.getStartTo() != null) {
                var zonedStartFrom = ro.getStartFrom().atStartOfDay();
                var zonedStartTo = ro.getStartTo().atTime(23, 59, 59);
                whereClause = whereClause.and(Conditions.between(entityTable.column("created_at"),
                    Conditions.just(StringUtils.wrap(zonedStartFrom.toString(), "'")),
                    Conditions.just(StringUtils.wrap(zonedStartTo.toString(), "'"))));
            }
            if (ro.getWorkspaceIds() != null && !ro.getWorkspaceIds().isEmpty()) {
                var inCondition = ro.getWorkspaceIds().stream().map(id -> "'" + id + "'").collect(Collectors.joining(","));
                whereClause = whereClause.and(Conditions.in(profileTable.column("workspace_id"), Conditions.just(inCondition)));
            }
            //append where clause with type if RO type is not null
            if (Objects.nonNull(ro.getTypes())) {
                String inCondition = "reason IN (" + buildStringContainsClause(ro.getTypes()) + ")";
                whereClause = whereClause.and(Conditions.isEqual(entityTable.column(inCondition), Conditions.just("true")));
            }
            if (ro.getStatuses() != null && !ro.getStatuses().isEmpty()) {
                String inCondition = "status IN (" + buildStringContainsClause(ro.getStatuses()) + ")";
                whereClause = whereClause.and(Conditions.isEqual(entityTable.column(inCondition), Conditions.just("true")));
            }
        }

        return whereClause;
    }

    private String buildStringContainsClause(List<String> statuses) {
        List<String> statusList = statuses.stream()
            .map(status -> "'" + status + "'")
            .collect(Collectors.toList());
        return String.join(",", statusList);
    }
}
