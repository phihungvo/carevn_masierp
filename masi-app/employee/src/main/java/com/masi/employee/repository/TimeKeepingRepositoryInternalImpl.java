package com.masi.employee.repository;

import com.masi.employee.domain.TimeKeeping;
import com.masi.employee.domain.enumeration.TimeKeepingType;
import com.masi.employee.domain.enumeration.WorkspaceType;
import com.masi.employee.repository.rowmapper.EmployeeProfileRowMapper;
import com.masi.employee.repository.rowmapper.EmployeeRowMapper;
import com.masi.employee.repository.rowmapper.TimeKeepingRowMapper;
import com.masi.employee.service.dto.TimekeepingQuery;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;

import java.time.LocalDate;
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
import org.springframework.data.relational.core.sql.SelectBuilder.SelectFromAndJoinCondition;
import org.springframework.data.relational.core.sql.Table;
import org.springframework.data.relational.repository.support.MappingRelationalEntityInformation;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.r2dbc.core.RowsFetchSpec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC custom repository implementation for the TimeKeeping entity.
 */
@SuppressWarnings("unused")
class TimeKeepingRepositoryInternalImpl extends SimpleR2dbcRepository<TimeKeeping, UUID> implements TimeKeepingRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final EmployeeRowMapper employeeMapper;
    private final TimeKeepingRowMapper timekeepingMapper;

    private static final Table entityTable = Table.aliased("time_keeping", EntityManager.ENTITY_ALIAS);
    private static final Table employeeTable = Table.aliased("employee", "employee");
    private static final Table profileTable = Table.aliased("employee_profile", "profile");
    private static final Table workspaceTable = Table.aliased("workspace", "workspace");
    private final EmployeeProfileRowMapper employeeProfileMapper;

    public TimeKeepingRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        EmployeeRowMapper employeeMapper,
        TimeKeepingRowMapper timekeepingMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter, EmployeeProfileRowMapper employeeProfileMapper
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(TimeKeeping.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.employeeMapper = employeeMapper;
        this.timekeepingMapper = timekeepingMapper;
        this.employeeProfileMapper = employeeProfileMapper;
    }

    @Override
    public Flux<TimeKeeping> findAllBy(Pageable pageable) {
        pageable = getDefaultSort(pageable);
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<TimeKeeping> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = TimeKeepingSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(EmployeeSqlHelper.getColumns(employeeTable, "employee"));
        columns.addAll(EmployeeProfileSqlHelper.getColumns(profileTable, "profile"));
        columns.addAll(WorkspaceSqlHelper.getColumns(workspaceTable, "workspace"));
        SelectFromAndJoinCondition selectFrom = Select.builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(employeeTable)
            .on(Column.create("employee_id", entityTable))
            .equals(Column.create("id", employeeTable))
            .leftOuterJoin(profileTable)
            .on(Column.create("id", employeeTable))
            .equals(Column.create("id", profileTable))
            .leftOuterJoin(workspaceTable)
            .on(Column.create("workspace_id", profileTable))
            .equals(Column.create("id", workspaceTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, TimeKeeping.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<TimeKeeping> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<TimeKeeping> findById(UUID id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(wrap(id.toString())));
        return createQuery(null, whereClause).one();
    }

    @Override
    public Flux<TimeKeeping> findAllByDateBetween(TimekeepingQuery query) {
        var pageable = getDefaultSort(query.getPageable());
        Condition whereClause = Conditions.between(entityTable.column("date"), Conditions.just(wrap(query.getStartDate().toString())), Conditions.just(wrap(query.getEndDate().toString())));
        if (Objects.nonNull(query.getType())) {
            whereClause = whereClause.and(Conditions.isEqual(entityTable.column("type"), Conditions.just(String.format("'%s'", query.getType()))));
        }
        if (Objects.nonNull(query.getWorkSpaceTypes())) {
            whereClause = whereClause.and(Conditions.isEqual(workspaceTable.column("workspace_type"), Conditions.just(String.format("'%s'", query.getWorkSpaceTypes()))));
        }
        if (Objects.nonNull(query.getCompanyId())) {
            whereClause = whereClause.and(Conditions.isEqual(profileTable.column("company"), Conditions.just(wrap(query.getCompanyId()))));
        }
        return createQuery(pageable, whereClause).all();
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
    public Flux<TimeKeeping> findAllByDate(LocalDate date, Pageable pageable) {
        pageable = getDefaultSort(pageable);
        Comparison whereClause = Conditions.isEqual(entityTable.column("date"), Conditions.just(wrap(date.toString())));
        return createQuery(pageable, whereClause).all();
    }

    private TimeKeeping process(Row row, RowMetadata metadata) {
        TimeKeeping entity = timekeepingMapper.apply(row, "e");
        entity.setEmployee(employeeMapper.apply(row, "employee"));
        entity.getEmployee().setEmployeeProfile(employeeProfileMapper.apply(row, "profile"));
        return entity;
    }

    @Override
    public <S extends TimeKeeping> Mono<S> save(S entity) {
        return super.save(entity);
    }

    private String wrap(String value) {
        return StringUtils.wrap(value, "'");
    }

    @Override
    public Mono<Long> countByDateBetween(TimekeepingQuery query) {
        Condition whereClause = Conditions.between(entityTable.column("date"), Conditions.just(wrap(query.getStartDate().toString())), Conditions.just(wrap(query.getEndDate().toString())));
        if (Objects.nonNull(query.getType())) {
            whereClause = whereClause.and(Conditions.isEqual(entityTable.column("type"), Conditions.just(String.format("'%s'", query.getType()))));
        }
        if (Objects.nonNull(query.getWorkSpaceTypes())) {
            whereClause = whereClause.and(Conditions.isEqual(workspaceTable.column("workspace_type"), Conditions.just(String.format("'%s'", query.getWorkSpaceTypes()))));
        }
        if (Objects.nonNull(query.getCompanyId())) {
            whereClause = whereClause.and(Conditions.isEqual(profileTable.column("company"), Conditions.just(wrap(query.getCompanyId()))));
        }
        return createQuery(null, whereClause).all().count();
    }

    @Override
    public Mono<Long> countByDate(LocalDate date) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("date"), Conditions.just(wrap(date.toString())));
        return createQuery(null, whereClause).all().count();
    }

    @Override
    public Flux<TimeKeeping> findAllByPersonalMonthlyTimesheetId(UUID id) {
        Condition whereClause = Conditions.isEqual(entityTable.column("personal_monthly_timesheet_id"), Conditions.just(wrap(id.toString())));
        Pageable pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.DESC, "created_at"));
        return createQuery(pageable, whereClause).all();
    }

    @Override
    public Flux<TimeKeeping> findAllWhereNotHaveRecord(LocalDate startDate, LocalDate endDate) {
        //     @Query("SELECT * FROM time_keeping entity WHERE NOT EXISTS(SELECT id FROM time_keeping_record WHERE time_keeping_record.check_in::date = entity.\"date\") AND entity.\"date\" BETWEEN :startDate AND :endDate")
        String columnDateAlias = String.format("%s.\"date\"", EntityManager.ENTITY_ALIAS);
        String columnEmployeeIdAlias = String.format("%s.\"employee_id\"", EntityManager.ENTITY_ALIAS);
        // kiểm trả xem nhân viên có checkin trong ngày đó chưa
        String subQuery = String.format("Not EXISTS(SELECT id FROM time_keeping_record WHERE time_keeping_record.check_in::date = %s AND time_keeping_record.employee_id = %s)", columnDateAlias, columnEmployeeIdAlias);
        Condition whereClause = Conditions.just(subQuery);
        whereClause = whereClause.and(Conditions.between(entityTable.column("date"), Conditions.just(wrap(startDate.toString())), Conditions.just(wrap(endDate.toString()))));
        return createQuery(null, whereClause).all();
    }


}
