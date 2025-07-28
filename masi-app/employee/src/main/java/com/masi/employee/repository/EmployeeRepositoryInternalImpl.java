package com.masi.employee.repository;

import com.masi.employee.domain.Employee;
import com.masi.employee.domain.enumeration.TimeKeepingType;
import com.masi.employee.domain.enumeration.WorkspaceType;
import com.masi.employee.repository.rowmapper.EmployeeProfileRowMapper;
import com.masi.employee.repository.rowmapper.EmployeeRowMapper;
import com.masi.employee.repository.rowmapper.WorkspaceRowMapper;

import com.masi.employee.service.mapper.EmployeeProfileMapper;
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
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.support.SimpleR2dbcRepository;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Comparison;
import org.springframework.data.relational.core.sql.Condition;
import org.springframework.data.relational.core.sql.Conditions;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Select;
import org.springframework.data.relational.core.sql.SelectBuilder.SelectFromAndJoin;
import org.springframework.data.relational.core.sql.SelectBuilder.SelectFromAndJoinCondition;
import org.springframework.data.relational.core.sql.Table;
import org.springframework.data.relational.repository.support.MappingRelationalEntityInformation;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.r2dbc.core.RowsFetchSpec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC custom repository implementation for the Employee entity.
 */
@SuppressWarnings("unused")
class EmployeeRepositoryInternalImpl extends SimpleR2dbcRepository<Employee, UUID>
    implements EmployeeRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final EmployeeRowMapper employeeMapper;
    private final WorkspaceRowMapper workspaceMapper;
    private final EmployeeProfileRowMapper employeeProfileMapper;
    private static final Table entityTable = Table.aliased("employee", EntityManager.ENTITY_ALIAS);
    private static final Table workspaceTable = Table.aliased("workspace", "ws");
    private static final Table employeeProfileTable = Table.aliased("employee_profile", "ep");

    public EmployeeRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        EmployeeRowMapper employeeMapper,
        WorkspaceRowMapper workspaceMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter, EmployeeProfileRowMapper employeeProfileMapper) {
        super(
            new MappingRelationalEntityInformation(
                converter.getMappingContext().getRequiredPersistentEntity(Employee.class)),
            entityOperations,
            converter);
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.employeeMapper = employeeMapper;
        this.workspaceMapper = workspaceMapper;
        this.employeeProfileMapper = employeeProfileMapper;
    }

    private Pageable getDefaultSort(Pageable pageable) {
        Sort defaultSort = Sort.by(Sort.Order.asc("workspace_type")).and(Sort.by(Sort.Order.asc("first_name")));
        if (Objects.isNull(pageable)) {
            pageable = PageRequest.of(0, Integer.MAX_VALUE, defaultSort);
        }
        if (pageable.getSort().equals(Sort.unsorted())) {
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), defaultSort);
        }
        return pageable;
    }

    @Override
    public Flux<Employee> findAllActiveBy(WorkspaceType workspaceType, Pageable pageable) {
        pageable = getDefaultSort(pageable);
        Condition isActiveCondition = Conditions.isEqual(Column.create("is_active", entityTable),
            Conditions.just(StringUtils.wrap("true", "'")));
        if (workspaceType != null) {
            isActiveCondition = isActiveCondition.and(Conditions.isEqual(Column.create("workspace_type", workspaceTable),
                Conditions.just(StringUtils.wrap(workspaceType.toString(), "'"))));
        }
        return createQuery(pageable, isActiveCondition).all();
    }

    @Override
    public Flux<Employee> findAllActiveBy(WorkspaceType workspaceType, Pageable pageable, TimeKeepingType timeKeepingType) {
        pageable = getDefaultSort(pageable);
        Condition isActiveCondition = Conditions.isEqual(Column.create("is_active", entityTable),
            Conditions.just(StringUtils.wrap("true", "'")));
        if (workspaceType != null) {
            isActiveCondition = isActiveCondition.and(Conditions.isEqual(Column.create("workspace_type", workspaceTable),
                Conditions.just(StringUtils.wrap(workspaceType.toString(), "'"))));
        }
        var notQueryTimeKeepingTypes = List.of(TimeKeepingType.HOUR, TimeKeepingType.OVERTIME);
        if (timeKeepingType != null && !notQueryTimeKeepingTypes.contains(timeKeepingType)) {
            // timekeepingtype === workspace.normalized_name
            isActiveCondition = isActiveCondition.and(Conditions.isEqual(Column.create("normalized_name", workspaceTable),
                Conditions.just(StringUtils.wrap(timeKeepingType.toString(), "'"))));
        }
        return createQuery(pageable, isActiveCondition).all();
    }

    @Override
    public Flux<Employee> findAllActiveBy(WorkspaceType workspaceType, Pageable pageable, TimeKeepingType timeKeepingType, String companyId) {
        Condition isActiveCondition = Conditions.isEqual(Column.create("is_active", entityTable),
            Conditions.just(StringUtils.wrap("true", "'")));
        if (workspaceType != null) {
            isActiveCondition = isActiveCondition.and(Conditions.isEqual(Column.create("workspace_type", workspaceTable),
                Conditions.just(StringUtils.wrap(workspaceType.toString(), "'"))));
        }
        var notQueryTimeKeepingTypes = List.of(TimeKeepingType.HOUR, TimeKeepingType.OVERTIME);
        if (timeKeepingType != null && !notQueryTimeKeepingTypes.contains(timeKeepingType)) {
            // timekeepingtype === workspace.normalized_name
            isActiveCondition = isActiveCondition.and(Conditions.isEqual(Column.create("normalized_name", workspaceTable),
                Conditions.just(StringUtils.wrap(timeKeepingType.toString(), "'"))));
        }
        if (StringUtils.isNotBlank(companyId)) {
            isActiveCondition = isActiveCondition.and(Conditions.isEqual(Column.create("company", employeeProfileTable),
                Conditions.just(StringUtils.wrap(companyId, "'"))));
        }
        return createQuery(pageable, isActiveCondition).all();
    }

    RowsFetchSpec<Employee> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = EmployeeSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(WorkspaceSqlHelper.getColumns(workspaceTable, "ws"));
        columns.add(Column.aliased("workspace_type", workspaceTable, "e" + "_workspace_type"));
        columns.addAll(EmployeeProfileSqlHelper.getColumns(employeeProfileTable, "ep"));
        SelectFromAndJoinCondition selectFrom = Select.builder()
            .select(columns)

            .from(entityTable)

            .leftOuterJoin(employeeProfileTable)
            .on(Column.create("id", entityTable))
            .equals(Column.create("id", employeeProfileTable))
            .leftOuterJoin(workspaceTable)
            .on(Column.create("workspace_id", employeeProfileTable))
            .equals(Column.create("id", workspaceTable));
        // we do not support Criteria here for now as of
        // https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, Employee.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<Employee> findAllActive() {
        return findAllActiveBy(null, null);
    }

    @Override
    public Mono<Employee> findById(UUID id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"),
            Conditions.just(StringUtils.wrap(id.toString(), "'")));
        return createQuery(null, whereClause).one();
    }

    private Employee process(Row row, RowMetadata metadata) {
        Employee entity = employeeMapper.apply(row, "e");
        entity.setWorkspace(workspaceMapper.apply(row, "ws"));
        entity.setEmployeeProfile(employeeProfileMapper.apply(row, "ep"));
        return entity;
    }

    @Override
    public <S extends Employee> Mono<S> save(S entity) {
        return super.save(entity);
    }

    @Override
    public Mono<Long> countAllActive() {
        Condition whereClause = Conditions.isEqual(Column.create("is_active", entityTable),
            Conditions.just(StringUtils.wrap("true", "'")));
        return createQuery(null, whereClause).all().count();
    }

    @Override
    public Mono<Long> countAllByWorkspaceType(WorkspaceType workspaceType) {
        if (workspaceType == null) {
            return countAllActive();
        } else {
            Condition whereClause = Conditions.isEqual(Column.create("workspace_type", workspaceTable),
                Conditions.just(StringUtils.wrap(workspaceType.toString(), "'")));
            return createQuery(null, whereClause).all().count();
        }
    }

    @Override
    public Mono<Long> countAllByWorkspaceType(WorkspaceType workspaceType, TimeKeepingType timeKeepingType) {
        Condition isActiveCondition = Conditions.isEqual(Column.create("is_active", entityTable),
            Conditions.just(StringUtils.wrap("true", "'")));
        if (workspaceType != null) {
            isActiveCondition = isActiveCondition.and(Conditions.isEqual(Column.create("workspace_type", workspaceTable),
                Conditions.just(StringUtils.wrap(workspaceType.toString(), "'"))));
        }
        var notQueryTimeKeepingTypes = List.of(TimeKeepingType.HOUR, TimeKeepingType.OVERTIME);
        if (timeKeepingType != null && !notQueryTimeKeepingTypes.contains(timeKeepingType)) {
            // timekeepingtype === workspace.normalized_name
            isActiveCondition = isActiveCondition.and(Conditions.isEqual(Column.create("normalized_name", workspaceTable),
                Conditions.just(StringUtils.wrap(timeKeepingType.toString(), "'"))));
        }
        return createQuery(null, isActiveCondition).all().count();
    }

    @Override
    public Flux<Employee> findAllActive(String company) {
//        @Query("SELECT e.* FROM employee  e left join  employee_profile  ep on e.id = ep.id WHERE e.is_active = true and ep.status!='RESIGNED' and ep.company = :company")
        Condition isActiveCondition = Conditions.isEqual(Column.create("is_active", entityTable),
            Conditions.just(StringUtils.wrap("true", "'")));
        if (StringUtils.isNotBlank(company)) {
            isActiveCondition = isActiveCondition.and(Conditions.isEqual(Column.create("company", employeeProfileTable),
                Conditions.just(StringUtils.wrap(company, "'"))));
        }
        // status!='RESIGNED'
        isActiveCondition = isActiveCondition.and(Conditions.isNotEqual(Column.create("status", employeeProfileTable),
            Conditions.just(StringUtils.wrap("RESIGNED", "'"))));
        return createQuery(null, isActiveCondition).all();
    }


}
