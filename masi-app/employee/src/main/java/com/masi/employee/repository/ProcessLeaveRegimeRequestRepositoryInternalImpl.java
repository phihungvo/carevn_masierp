package com.masi.employee.repository;

import com.masi.employee.domain.ProcessLeaveRegimeRequest;
import com.masi.employee.repository.rowmapper.ColumnConverter;
import com.masi.employee.repository.rowmapper.EmployeeRowMapper;
import com.masi.employee.repository.rowmapper.ProcessLeaveRegimeRequestRowMapper;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
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
import org.springframework.data.relational.core.sql.SelectBuilder.SelectFromAndJoin;
import org.springframework.data.relational.core.sql.SelectBuilder.SelectFromAndJoinCondition;
import org.springframework.data.relational.core.sql.Table;
import org.springframework.data.relational.repository.support.MappingRelationalEntityInformation;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.r2dbc.core.RowsFetchSpec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.service.ConditionBuilder;

/**
 * Spring Data R2DBC custom repository implementation for the
 * ProcessLeaveRegimeRequest entity.
 */
@SuppressWarnings("unused")
class ProcessLeaveRegimeRequestRepositoryInternalImpl
        extends SimpleR2dbcRepository<ProcessLeaveRegimeRequest, UUID>
        implements ProcessLeaveRegimeRequestRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;
    private final ColumnConverter columnConverter;

    private final ProcessLeaveRegimeRequestRowMapper processleaveregimerequestMapper;
    private final EmployeeRowMapper employeeRowMapper;

    private static final Table entityTable = Table.aliased("process_leave_regime_request", EntityManager.ENTITY_ALIAS);
    private static final Table employeeTable = Table.aliased("employee", "employee");
    private static final Table approverTable = Table.aliased("employee", "a");

    public ProcessLeaveRegimeRequestRepositoryInternalImpl(
            R2dbcEntityTemplate template,
            EntityManager entityManager,
            ProcessLeaveRegimeRequestRowMapper processleaveregimerequestMapper,
            R2dbcEntityOperations entityOperations,
            R2dbcConverter converter, ColumnConverter columnConverter, EmployeeRowMapper employeeRowMapper) {
        super(
                new MappingRelationalEntityInformation(
                        converter.getMappingContext().getRequiredPersistentEntity(ProcessLeaveRegimeRequest.class)),
                entityOperations,
                converter);
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.columnConverter = columnConverter;
        this.processleaveregimerequestMapper = processleaveregimerequestMapper;
        this.employeeRowMapper = employeeRowMapper;
    }

    @Override
    public Flux<ProcessLeaveRegimeRequest> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<ProcessLeaveRegimeRequest> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = ProcessLeaveRegimeRequestSqlHelper.getColumns(entityTable,
                EntityManager.ENTITY_ALIAS);
        columns.addAll(EmployeeSqlHelper.getColumns(employeeTable, "employee"));
        columns.addAll(EmployeeSqlHelper.getColumns(approverTable, "a"));
        SelectFromAndJoinCondition selectFrom = Select.builder().select(columns).from(entityTable)
                .leftOuterJoin(employeeTable).on(Column.create("employee_id", entityTable))
                .equals(Column.create("id", employeeTable))
                .leftOuterJoin(
                        approverTable)
                .on(Column.create("approver_id", entityTable))
                .equals(Column.create("id", approverTable));

        // we do not support Criteria here for now as of
        // https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, ProcessLeaveRegimeRequest.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<ProcessLeaveRegimeRequest> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<ProcessLeaveRegimeRequest> findById(UUID id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"),
                Conditions.just(StringUtils.wrap(id.toString(), "'")));
        return createQuery(null, whereClause).one();
    }

    private ProcessLeaveRegimeRequest process(Row row, RowMetadata metadata) {
        ProcessLeaveRegimeRequest entity = processleaveregimerequestMapper.apply(row, "e");
        entity.setEmployee(employeeRowMapper.apply(row, "employee"));
        entity.setApprover(employeeRowMapper.apply(row, "a"));

        return entity;
    }

    @Override
    public <S extends ProcessLeaveRegimeRequest> Mono<S> save(S entity) {
        return super.save(entity);
    }

    @Override
    public Flux<ProcessLeaveRegimeRequest> findByLeaveRegimeRequestIdAndIsDeletedIsFalseQuery(
            UUID leaveRegimeRequestId) {
        // TODO Auto-generated method stub
        Condition whereClause = Conditions.isEqual(entityTable.column("is_deleted"), Conditions.just("false"));
        whereClause = whereClause.and(Conditions.isEqual(entityTable.column("leave_regime_request_id"),
                Conditions.just(StringUtils.wrap(leaveRegimeRequestId.toString(), "'"))));
        return createQuery(null, whereClause).all();

    }
}
