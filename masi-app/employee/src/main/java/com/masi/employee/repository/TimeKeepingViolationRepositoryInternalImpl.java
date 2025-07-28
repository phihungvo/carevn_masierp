package com.masi.employee.repository;

import com.masi.employee.domain.TimeKeepingViolation;
import com.masi.employee.repository.rowmapper.*;
import com.masi.employee.service.dto.TimeKeepingViolationFilter;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import lombok.extern.slf4j.Slf4j;
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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.service.ConditionBuilder;

/**
 * Spring Data R2DBC custom repository implementation for the TimeKeepingViolation entity.
 */
@SuppressWarnings("unused")
@Slf4j
class TimeKeepingViolationRepositoryInternalImpl
    extends SimpleR2dbcRepository<TimeKeepingViolation, UUID>
    implements TimeKeepingViolationRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;
    private final ColumnConverter columnConverter;

    private final TimeKeepingRowMapper timekeepingMapper;
    private final EmployeeRowMapper employeeMapper;
    private final TimeKeepingExplanationRowMapper timekeepingexplanationMapper;
    private final TimeKeepingViolationRowMapper timekeepingviolationMapper;
    private final EmployeeProfileRowMapper employeeProfileRowMapper;

    private static final Table entityTable = Table.aliased("time_keeping_violation", EntityManager.ENTITY_ALIAS);
    private static final Table timeKeepingTable = Table.aliased("time_keeping", "timeKeeping");
    private static final Table employeeTable = Table.aliased("employee", "employee");
    private static final Table explanationTable = Table.aliased("time_keeping_explanation", "explanation");
    private static final Table employeeProfileTable = Table.aliased("employee_profile", "employee_profile");

    public TimeKeepingViolationRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        TimeKeepingRowMapper timekeepingMapper,
        EmployeeRowMapper employeeMapper,
        TimeKeepingExplanationRowMapper timekeepingexplanationMapper,
        TimeKeepingViolationRowMapper timekeepingviolationMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter,
        ColumnConverter columnConverter, EmployeeProfileRowMapper employeeProfileRowMapper
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(TimeKeepingViolation.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.timekeepingMapper = timekeepingMapper;
        this.employeeMapper = employeeMapper;
        this.timekeepingexplanationMapper = timekeepingexplanationMapper;
        this.timekeepingviolationMapper = timekeepingviolationMapper;
        this.columnConverter = columnConverter;
        this.employeeProfileRowMapper = employeeProfileRowMapper;
    }

    @Override
    public Flux<TimeKeepingViolation> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    private Pageable getDefaultSort(Pageable pageable) {
        Sort defaultSort = Sort.by(Sort.Direction.DESC, "created_at");
        if (Objects.isNull(pageable)) {
            pageable = PageRequest.of(0, Integer.MAX_VALUE, defaultSort);
        }
        if (pageable.getSort().equals(Sort.unsorted())) {
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), defaultSort);
        }
        return pageable;
    }

    RowsFetchSpec<TimeKeepingViolation> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = TimeKeepingViolationSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(TimeKeepingSqlHelper.getColumns(timeKeepingTable, "timeKeeping"));
        columns.addAll(EmployeeSqlHelper.getColumns(employeeTable, "employee"));
        columns.addAll(TimeKeepingExplanationSqlHelper.getColumns(explanationTable, "explanation"));
        columns.addAll(EmployeeProfileSqlHelper.getColumns(employeeProfileTable, "employee_profile"));
        SelectFromAndJoinCondition selectFrom = Select.builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(timeKeepingTable)
            .on(Column.create("time_keeping_id", entityTable))
            .equals(Column.create("id", timeKeepingTable))
            .leftOuterJoin(employeeTable)
            .on(Column.create("employee_id", entityTable))
            .equals(Column.create("id", employeeTable))
            .leftOuterJoin(employeeProfileTable)
            .on(Column.create("employee_id", entityTable))
            .equals(Column.create("id", employeeProfileTable))
            .leftOuterJoin(explanationTable)
            .on(Column.create("explanation_id", entityTable))
            .equals(Column.create("id", explanationTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, TimeKeepingViolation.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<TimeKeepingViolation> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<TimeKeepingViolation> findById(UUID id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(StringUtils.wrap(id.toString(), "'")));
        return createQuery(null, whereClause).one();
    }

    @Override
    public Flux<TimeKeepingViolation> findAllByIsActive(Boolean isActive, Pageable pageable) {
        pageable = getDefaultSort(pageable);
        Comparison whereClause = Conditions.isEqual(entityTable.column("is_active"), Conditions.just(StringUtils.wrap(isActive.toString(), "'")));
        return createQuery(pageable, whereClause).all();
    }

    @Override
    public Mono<TimeKeepingViolation> findByIdAndIsActive(UUID id, Boolean isActive) {
        Condition matchId = Conditions.isEqual(entityTable.column("id"), Conditions.just(StringUtils.wrap(id.toString(), "'")));
        Condition matchIsActive = Conditions.isEqual(entityTable.column("is_active"), Conditions.just(StringUtils.wrap(isActive.toString(), "'")));
        matchId.and(matchIsActive);
        return createQuery(null, matchId).one();
    }

    private TimeKeepingViolation process(Row row, RowMetadata metadata) {
        TimeKeepingViolation entity = timekeepingviolationMapper.apply(row, "e");
        entity.setTimeKeeping(timekeepingMapper.apply(row, "timeKeeping"));
        entity.setEmployee(employeeMapper.apply(row, "employee"));
        entity.setExplanation(timekeepingexplanationMapper.apply(row, "explanation"));
        if (entity.getEmployee() != null) {
            entity.getEmployee().setEmployeeProfile(employeeProfileRowMapper.apply(row, "employee_profile"));
        }
        return entity;
    }

    @Override
    public <S extends TimeKeepingViolation> Mono<S> save(S entity) {
        return super.save(entity);
    }

    @Override
    public Mono<Long> countAllByIsActive(Boolean isActive) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("is_active"), Conditions.just(StringUtils.wrap(isActive.toString(), "'")));
        return createQuery(null, whereClause).all().count();
    }

    @Override
    public Mono<Long> countAllByFilter(TimeKeepingViolationFilter filter) {
        return createQuery(null, buildConditionFromFilter(filter))
            .all()
            .count()
            .doOnError(RuntimeException::new);
    }

    @Override
    public Flux<TimeKeepingViolation> findAllByExplanationId(UUID explanationId, Pageable pageable) {
        Condition condition = Conditions.isEqual(entityTable.column("explanation_id"), Conditions.just(StringUtils.wrap(explanationId.toString(), "'")));
        return createQuery(pageable, condition).all();
    }

    @Override
    public Flux<TimeKeepingViolation> findAllByFilter(Pageable pageable, TimeKeepingViolationFilter filter) {
        pageable = getDefaultSort(pageable);
        return createQuery(pageable, buildConditionFromFilter(filter)).all();
    }

    private Condition buildConditionFromFilter(TimeKeepingViolationFilter filter) {
        ConditionBuilder builder = new ConditionBuilder(this.columnConverter);
        if (filter != null) {
            builder.buildFilterConditionForField(filter.getIsActive(), entityTable.column("is_active"));
            if (filter.getFromDate() != null) {
                builder.buildFilterConditionForField(filter.getFromDate(), timeKeepingTable.column("date"));
            }
            if (filter.getToDate() != null) {
                builder.buildFilterConditionForField(filter.getToDate(), timeKeepingTable.column("date"));
            }
            if (filter.getType() != null) {
                builder.buildFilterConditionForField(filter.getType(), entityTable.column("type"));
            }
            if (filter.getEmployeeIds() != null) {
                builder.buildFilterConditionForField(filter.getEmployeeIds(), entityTable.column("employee_id"));
            }
            if (filter.getWorkspaceIds() != null) {
                builder.buildFilterConditionForField(filter.getWorkspaceIds(), employeeTable.column("workspace_id"));
            }
            if (filter.getCompany() != null) {
                builder.buildFilterConditionForField(filter.getCompany(), employeeProfileTable.column("company"));
            }
            if (filter.getExplanationId() != null) {
                builder.buildFilterConditionForField(filter.getExplanationId(), entityTable.column("explanation_id"));
            }
        }
        return builder.buildConditions();
    }
}
