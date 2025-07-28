package com.masi.employee.repository;

import com.masi.employee.domain.LeaveRequest;
import com.masi.employee.repository.rowmapper.ColumnConverter;
import com.masi.employee.repository.rowmapper.EmployeeRowMapper;
import com.masi.employee.repository.rowmapper.LeaveRequestRowMapper;
import com.masi.employee.service.dto.LeaveRequestFilter;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

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
 * Spring Data R2DBC custom repository implementation for the LeaveRequest entity.
 */
@SuppressWarnings("unused")
class LeaveRequestRepositoryInternalImpl extends SimpleR2dbcRepository<LeaveRequest, UUID> implements LeaveRequestRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final EmployeeRowMapper employeeMapper;
    private final LeaveRequestRowMapper leaverequestMapper;
    private final ColumnConverter columnConverter;

    private static final Table entityTable = Table.aliased("leave_request", EntityManager.ENTITY_ALIAS);
    private static final Table employeeTable = Table.aliased("employee", "employee");
    private static final Table substituteTable = Table.aliased("employee", "substitute");
    private static final Table employeeProfileTable = Table.aliased("employee_profile", "ep");
    private static final Table workspaceTable = Table.aliased("workspace", "ws");

    public LeaveRequestRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        EmployeeRowMapper employeeMapper,
        LeaveRequestRowMapper leaverequestMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter,
        ColumnConverter columnConverter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(LeaveRequest.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.employeeMapper = employeeMapper;
        this.leaverequestMapper = leaverequestMapper;
        this.columnConverter = columnConverter;
    }

    @Override
    public Flux<LeaveRequest> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<LeaveRequest> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = LeaveRequestSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(EmployeeSqlHelper.getColumns(employeeTable, "employee"));
        columns.addAll(EmployeeSqlHelper.getColumns(substituteTable, "substitute"));
        SelectFromAndJoinCondition selectFrom = Select.builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(employeeTable)
            .on(Column.create("employee_id", entityTable))
            .equals(Column.create("id", employeeTable))
            .leftOuterJoin(substituteTable)
            .on(Column.create("substitute_id", entityTable))
            .equals(Column.create("id", substituteTable))
            .leftOuterJoin(employeeProfileTable)
            .on(Column.create("id", employeeTable))
            .equals(Column.create("id", employeeProfileTable))
            .leftOuterJoin(workspaceTable)
            .on(Column.create("workspace_id", employeeTable))
            .equals(Column.create("id", workspaceTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, LeaveRequest.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<LeaveRequest> findAll() {
        return findAllBy(null);
    }

    @Override
    public Flux<LeaveRequest> findAllByIsActive(Boolean isActive, Pageable pageable) {
        pageable = getDefaultSort(pageable);
        Comparison whereClause = Conditions.isEqual(entityTable.column("is_active"), Conditions.just(StringUtils.wrap(isActive.toString(), "'")));
        return createQuery(pageable, whereClause).all();
    }

    @Override
    public Mono<LeaveRequest> findByIdAndIsActive(UUID id, Boolean isActive) {
        // Create condition for id

        Condition idCondition = Conditions.isEqual(Column.create("id", entityTable), Conditions.just(StringUtils.wrap(id.toString(), "'")));

        // Initialize the where clause with id condition
        Condition whereClause = idCondition;

        // Add isActive condition if not null
        if (isActive != null) {
            Condition isActiveCondition = Conditions.isEqual(Column.create("is_active", entityTable), Conditions.just(StringUtils.wrap(isActive.toString(), "'")));
            // Combine conditions manually
            whereClause = idCondition.and(isActiveCondition);  // Assuming `and` is available on Condition objects or similar
        }

        // Use the createQuery method to execute the query with the where clause
        return createQuery(null, whereClause).one();
    }

    private LeaveRequest process(Row row, RowMetadata metadata) {
        LeaveRequest entity = leaverequestMapper.apply(row, "e");
        entity.setEmployee(employeeMapper.apply(row, "employee"));
        entity.setSubstitute(employeeMapper.apply(row, "substitute"));
        return entity;
    }

    @Override
    public <S extends LeaveRequest> Mono<S> save(S entity) {
        return super.save(entity);
    }

    @Override
    public Mono<Long> countAllByIsActive(Boolean isActive) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("is_active"), Conditions.just(StringUtils.wrap(isActive.toString(), "'")));
        return createQuery(null, whereClause).all().count();
    }

    @Override
    public Mono<Long> countAllByFilter(LeaveRequestFilter filter) {
        return createQuery(null, buildConditionFromFilter(filter))
            .all()
            .count()
            .doOnError(RuntimeException::new);
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

    @Override
    public Flux<LeaveRequest> findAllByFilter(Pageable pageable, LeaveRequestFilter filter) {
        pageable = getDefaultSort(pageable);
        return createQuery(pageable, buildConditionFromFilter(filter)).all();
    }

    private Condition buildConditionFromFilter(LeaveRequestFilter filter) {
        ConditionBuilder builder = new ConditionBuilder(this.columnConverter);
        if (filter != null) {
            builder.buildFilterConditionForField(filter.getIsActive(), entityTable.column("is_active"));
            if (filter.getType() != null) {
                builder.buildFilterConditionForField(filter.getType(), entityTable.column("leave_request_type"));
            }
            if (filter.getStatus() != null) {
                builder.buildFilterConditionForField(filter.getStatus(), entityTable.column("status"));
            }
            if (filter.getWorkspaceIds() != null) {
                builder.buildFilterConditionForField(filter.getWorkspaceIds(), employeeProfileTable.column("workspace_id"));
            }
            if (filter.getEmployeeIds() != null) {
                builder.buildFilterConditionForField(filter.getEmployeeIds(), entityTable.column("employee_id"));
            }

            var condition = builder.buildConditions();
            if (filter.getWorkspaceType() != null) {
                condition = condition.and(Conditions.isEqual(workspaceTable.column("workspace_type"), Conditions.just(StringUtils.wrap(filter.getWorkspaceType().toString(), "'"))));
            }
            return condition;
        }
        return builder.buildConditions();
    }

}
