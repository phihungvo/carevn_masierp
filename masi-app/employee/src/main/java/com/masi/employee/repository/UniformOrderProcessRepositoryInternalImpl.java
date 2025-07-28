package com.masi.employee.repository;

import com.masi.employee.domain.UniformOrderProcess;
import com.masi.employee.repository.rowmapper.EmployeeRowMapper;
import com.masi.employee.repository.rowmapper.UniformOrderProcessRowMapper;
import com.masi.employee.repository.rowmapper.UniformOrderRowMapper;
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
import org.springframework.data.relational.core.sql.SelectBuilder.SelectFromAndJoinCondition;
import org.springframework.data.relational.core.sql.Table;
import org.springframework.data.relational.repository.support.MappingRelationalEntityInformation;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.r2dbc.core.RowsFetchSpec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC custom repository implementation for the
 * UniformOrderProcess entity.
 */
@SuppressWarnings("unused")
class UniformOrderProcessRepositoryInternalImpl
        extends SimpleR2dbcRepository<UniformOrderProcess, UUID>
        implements UniformOrderProcessRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final UniformOrderRowMapper uniformorderMapper;
    private final UniformOrderProcessRowMapper uniformorderprocessMapper;
    private final EmployeeRowMapper employeeMapper;

    private static final Table entityTable = Table.aliased("uniform_order_process", EntityManager.ENTITY_ALIAS);
    private static final Table uniformOrderTable = Table.aliased("uniform_order", "uniformOrder");
    private static final Table employeeTable = Table.aliased("employee", "employee");

    public UniformOrderProcessRepositoryInternalImpl(
            R2dbcEntityTemplate template,
            EntityManager entityManager,
            UniformOrderRowMapper uniformorderMapper,
            UniformOrderProcessRowMapper uniformorderprocessMapper,
            R2dbcEntityOperations entityOperations,
            R2dbcConverter converter, EmployeeRowMapper employeeMapper) {
        super(
                new MappingRelationalEntityInformation(
                        converter.getMappingContext().getRequiredPersistentEntity(UniformOrderProcess.class)),
                entityOperations,
                converter);
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.uniformorderMapper = uniformorderMapper;
        this.uniformorderprocessMapper = uniformorderprocessMapper;
        this.employeeMapper = employeeMapper;
    }

    @Override
    public Flux<UniformOrderProcess> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<UniformOrderProcess> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = UniformOrderProcessSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(UniformOrderSqlHelper.getColumns(uniformOrderTable, "uniformOrder"));
        columns.addAll(EmployeeSqlHelper.getColumns(employeeTable, "employee"));
        SelectFromAndJoinCondition selectFrom = Select.builder()
                .select(columns)
                .from(entityTable)
                .leftOuterJoin(uniformOrderTable)
                .on(Column.create("uniform_order_id", entityTable))
                .equals(Column.create("id", uniformOrderTable))
                .leftOuterJoin(employeeTable)
                .on(Column.create("approver_id", entityTable))
                .equals(Column.create("id", employeeTable));
        // we do not support Criteria here for now as of
        // https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, UniformOrderProcess.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<UniformOrderProcess> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<UniformOrderProcess> findById(UUID id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"),
                Conditions.just(StringUtils.wrap(id.toString(), "'")));
        return createQuery(null, whereClause).one();
    }

    private UniformOrderProcess process(Row row, RowMetadata metadata) {
        UniformOrderProcess entity = uniformorderprocessMapper.apply(row, "e");
        entity.setUniformOrder(uniformorderMapper.apply(row, "uniformOrder"));
        entity.setApprover(employeeMapper.apply(row, "employee"));

        return entity;
    }

    @Override
    public <S extends UniformOrderProcess> Mono<S> save(S entity) {
        return super.save(entity);
    }

    @Override
    public Flux<UniformOrderProcess> findAllByOrderId(UUID id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("uniform_order_id"),
                Conditions.just(StringUtils.wrap(id.toString(), "'")));
        return createQuery(null, whereClause).all();
    }
}
