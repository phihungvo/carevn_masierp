package com.masi.employee.repository;

import com.masi.employee.domain.TimeKeepingRecord;
import com.masi.employee.repository.rowmapper.EmployeeProfileRowMapper;
import com.masi.employee.repository.rowmapper.EmployeeRowMapper;
import com.masi.employee.repository.rowmapper.TimeKeepingRecordRowMapper;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;

import java.lang.module.FindException;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
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
 * Spring Data R2DBC custom repository implementation for the TimeKeepingRecord entity.
 */
@SuppressWarnings("unused")
class TimeKeepingRecordRepositoryInternalImpl
    extends SimpleR2dbcRepository<TimeKeepingRecord, UUID>
    implements TimeKeepingRecordRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final EmployeeRowMapper employeeMapper;
    private final TimeKeepingRecordRowMapper timekeepingrecordMapper;
    private final EmployeeProfileRowMapper employeeProfileMapper;

    private static final Table entityTable = Table.aliased("time_keeping_record", EntityManager.ENTITY_ALIAS);
    private static final Table employeeTable = Table.aliased("employee", "employee");
    private static final Table profileTable = Table.aliased("employee_profile", "profile");

    public TimeKeepingRecordRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        EmployeeRowMapper employeeMapper,
        TimeKeepingRecordRowMapper timekeepingrecordMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter, EmployeeProfileRowMapper employeeProfileMapper
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(TimeKeepingRecord.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.employeeMapper = employeeMapper;
        this.timekeepingrecordMapper = timekeepingrecordMapper;
        this.employeeProfileMapper = employeeProfileMapper;
    }

    private String wrap(String value) {
        return StringUtils.wrap(value, "'");
    }

    @Override
    public Flux<TimeKeepingRecord> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    @Override
    public Flux<TimeKeepingRecord> findAllByRangeDate(LocalDate startDate, LocalDate endDate, Pageable pageable) {
        String column = EntityManager.ENTITY_ALIAS + ".check_in";
        String inCondition = "DATE(" + column + ") BETWEEN " + wrap(startDate.toString()) + " AND " + wrap(endDate.toString());
        ;
        Condition whereClause = Conditions.just(inCondition);
        return createQuery(pageable, whereClause).all();
    }

    RowsFetchSpec<TimeKeepingRecord> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = TimeKeepingRecordSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(EmployeeSqlHelper.getColumns(employeeTable, "employee"));
        columns.addAll(EmployeeProfileSqlHelper.getColumns(profileTable, "profile"));
        SelectFromAndJoinCondition selectFrom = Select.builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(employeeTable)
            .on(Column.create("employee_id", entityTable))
            .equals(Column.create("id", employeeTable))
            .join(profileTable)
            .on(Column.create("id", employeeTable))
            .equals(Column.create("id", profileTable));
        ;
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, TimeKeepingRecord.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<TimeKeepingRecord> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<TimeKeepingRecord> findById(UUID id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(StringUtils.wrap(id.toString(), "'")));
        return createQuery(null, whereClause).one();
    }

    private TimeKeepingRecord process(Row row, RowMetadata metadata) {
        TimeKeepingRecord entity = timekeepingrecordMapper.apply(row, "e");
        entity.setEmployee(employeeMapper.apply(row, "employee"));
        entity.getEmployee().setEmployeeProfile(employeeProfileMapper.apply(row, "profile"));
        return entity;
    }

    @Override
    public <S extends TimeKeepingRecord> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
