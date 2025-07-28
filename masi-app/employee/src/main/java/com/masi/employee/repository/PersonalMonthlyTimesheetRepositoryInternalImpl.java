package com.masi.employee.repository;

import com.masi.employee.domain.EmployeeProfile;
import com.masi.employee.domain.PersonalMonthlyTimesheet;
import com.masi.employee.domain.enumeration.TimeKeepingType;
import com.masi.employee.repository.rowmapper.EmployeeProfileRowMapper;
import com.masi.employee.repository.rowmapper.EmployeeRowMapper;
import com.masi.employee.repository.rowmapper.MonthlyTimeSheetReviewRowMapper;
import com.masi.employee.repository.rowmapper.PersonalMonthlyTimesheetRowMapper;
import com.masi.employee.service.dto.PersonalMonthlyTimesheetQuery;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

/**
 * Spring Data R2DBC custom repository implementation for the
 * PersonalMonthlyTimesheet entity.
 */
@SuppressWarnings("unused")
class PersonalMonthlyTimesheetRepositoryInternalImpl
    extends SimpleR2dbcRepository<PersonalMonthlyTimesheet, UUID>
    implements PersonalMonthlyTimesheetRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;
    private final EmployeeRowMapper employeeMapper;

    private final MonthlyTimeSheetReviewRowMapper monthlytimesheetreviewMapper;
    private final PersonalMonthlyTimesheetRowMapper personalmonthlytimesheetMapper;
    private final EmployeeProfileRowMapper employeeProfileMapper;

    private static final Table entityTable = Table.aliased("personal_monthly_timesheet", EntityManager.ENTITY_ALIAS);
    private static final Table reviewTable = Table.aliased("monthly_time_sheet_review", "review");
    private static final Table employeeTable = Table.aliased("employee", "employee");
    private static final Table workspaceTable = Table.aliased("workspace", "ws");
    private static final Table employeeProfileTable = Table.aliased("employee_profile", "employee_employeeProfile");

    public PersonalMonthlyTimesheetRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        EmployeeRowMapper employeeMapper,
        MonthlyTimeSheetReviewRowMapper monthlytimesheetreviewMapper,
        PersonalMonthlyTimesheetRowMapper personalmonthlytimesheetMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter, EmployeeProfileRowMapper employeeProfileMapper) {
        super(
            new MappingRelationalEntityInformation(
                converter.getMappingContext().getRequiredPersistentEntity(PersonalMonthlyTimesheet.class)),
            entityOperations,
            converter);
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.monthlytimesheetreviewMapper = monthlytimesheetreviewMapper;
        this.employeeMapper = employeeMapper;
        this.personalmonthlytimesheetMapper = personalmonthlytimesheetMapper;
        this.employeeProfileMapper = employeeProfileMapper;
    }

    @Override
    public Flux<PersonalMonthlyTimesheet> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<PersonalMonthlyTimesheet> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = PersonalMonthlyTimesheetSqlHelper.getColumns(entityTable,
            EntityManager.ENTITY_ALIAS);
        columns.addAll(EmployeeSqlHelper.getColumns(employeeTable, "employee"));
        columns.addAll(MonthlyTimeSheetReviewSqlHelper.getColumns(reviewTable, "review"));
        columns.addAll(WorkspaceSqlHelper.getColumns(workspaceTable, "ws"));
        columns.add(Column.aliased("workspace_type", workspaceTable, "e" + "_workspace_type"));
        columns.addAll(EmployeeProfileSqlHelper.getColumns(employeeProfileTable, "employee_employeeProfile"));
        SelectFromAndJoinCondition selectFrom = Select.builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(reviewTable).on(Column.create("review_id", entityTable)).equals(Column.create("id", reviewTable))
            .leftOuterJoin(employeeTable).on(Column.create("employee_id", entityTable)).equals(Column.create("id", employeeTable))
            .leftOuterJoin(employeeProfileTable).on(Column.create("id", employeeTable)).equals(Column.create("id", employeeProfileTable))
            .leftOuterJoin(workspaceTable).on(Column.create("workspace_id", employeeProfileTable)).equals(Column.create("id", workspaceTable));
        ;

        // we do not support Criteria here for now as of
        // https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, PersonalMonthlyTimesheet.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<PersonalMonthlyTimesheet> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<PersonalMonthlyTimesheet> findById(UUID id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"),
            Conditions.just(StringUtils.wrap(id.toString(), "'")));
        return createQuery(null, whereClause).one();
    }


    private PersonalMonthlyTimesheet process(Row row, RowMetadata metadata) {
        PersonalMonthlyTimesheet entity = personalmonthlytimesheetMapper.apply(row, "e");
        entity.setReview(monthlytimesheetreviewMapper.apply(row, "review"));
        entity.setEmployee(employeeMapper.apply(row, "employee"));
        entity.getEmployee().setEmployeeProfile(employeeProfileMapper.apply(row, "employee_employeeProfile"));
        return entity;
    }

    @Override
    public <S extends PersonalMonthlyTimesheet> Mono<S> save(S entity) {
        return super.save(entity);
    }

    @Override
    public Mono<PersonalMonthlyTimesheet> findByEmployeeIdAndMonthAndType(UUID employeeId, LocalDate month, TimeKeepingType type) {
        Condition whereClause = Conditions.isEqual(entityTable.column("employee_id"),
            Conditions.just(StringUtils.wrap(employeeId.toString(), "'")));
        String monthString = DateTimeFormatter.ISO_LOCAL_DATE.format(month.withDayOfMonth(1));
        whereClause = whereClause.and(
            Conditions.isEqual(entityTable.column("month"), Conditions.just(StringUtils.wrap(monthString, "'"))));
        whereClause = whereClause.and(
            Conditions.isEqual(entityTable.column("type"), Conditions.just(StringUtils.wrap(type.toString(), "'"))));
        // get first element

        return createQuery(null, whereClause).one();
    }

    private Pageable getDefaultSort(Pageable pageable) {
        Sort defaultSort = Sort.by(Sort.Order.asc("workspace_type")).and(Sort.by(Sort.Order.asc(employeeTable.column("first_name").toString())));
        if (Objects.isNull(pageable)) {
            pageable = PageRequest.of(0, Integer.MAX_VALUE, defaultSort);
        }
        if (pageable.getSort().equals(Sort.unsorted())) {
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), defaultSort);
        }
        return pageable;
    }

    @Override
    public Flux<PersonalMonthlyTimesheet> findAllByQuery(Pageable pageable, PersonalMonthlyTimesheetQuery query) {
        // TODO Auto-generated method stub
        pageable = getDefaultSort(pageable);
        return createQuery(pageable, buildQuery(query)).all();
    }

    Condition buildQuery(PersonalMonthlyTimesheetQuery query) {
        Condition where = Conditions.just(" 1 = 1 ");
        if (query.getMonth() != null) {
            String monthString = DateTimeFormatter.ISO_LOCAL_DATE.format(query.getMonth().withDayOfMonth(1));
            where = where.and(Conditions.isEqual(entityTable.column("month"), Conditions.just(StringUtils.wrap(monthString, "'"))));
        }
        if (query.getType() == null) {
            query.setType(TimeKeepingType.HOUR);
        }
        if (query.getWorkspaceType() != null) {
            where = where.and(Conditions.isEqual(workspaceTable.column("workspace_type"), Conditions.just(StringUtils.wrap(query.getWorkspaceType().toString(), "'"))));
        }
        if (StringUtils.isNotBlank(query.getCompany())) {
            where = where.and(Conditions.isEqual(employeeProfileTable.column("company"), Conditions.just(StringUtils.wrap(query.getCompany(), "'"))));
        }
        if (query.getType() != null)
            where = where.and(Conditions.isEqual(entityTable.column("type"), Conditions.just(StringUtils.wrap(query.getType().toString(), "'"))));
        return where;
    }

    @Override
    public Mono<Long> countAllByQuery(PersonalMonthlyTimesheetQuery query) {
        return createQuery(null, buildQuery(query)).all().count();
    }
}
