package com.masi.employee.repository;

import com.masi.employee.domain.EmployeeChangeLog;
import com.masi.employee.repository.rowmapper.EmployeeChangeLogRowMapper;
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
import org.springframework.data.relational.core.sql.Comparison;
import org.springframework.data.relational.core.sql.Condition;
import org.springframework.data.relational.core.sql.Conditions;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Select;
import org.springframework.data.relational.core.sql.SelectBuilder.SelectFromAndJoin;
import org.springframework.data.relational.core.sql.Table;
import org.springframework.data.relational.repository.support.MappingRelationalEntityInformation;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.r2dbc.core.RowsFetchSpec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC custom repository implementation for the EmployeeChangeLog entity.
 */
@SuppressWarnings("unused")
class EmployeeChangeLogRepositoryInternalImpl
    extends SimpleR2dbcRepository<EmployeeChangeLog, UUID>
    implements EmployeeChangeLogRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final EmployeeChangeLogRowMapper employeechangelogMapper;

    private static final Table entityTable = Table.aliased("employee_change_log", EntityManager.ENTITY_ALIAS);

    public EmployeeChangeLogRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        EmployeeChangeLogRowMapper employeechangelogMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(EmployeeChangeLog.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.employeechangelogMapper = employeechangelogMapper;
    }

    @Override
    public Flux<EmployeeChangeLog> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<EmployeeChangeLog> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = EmployeeChangeLogSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        SelectFromAndJoin selectFrom = Select.builder().select(columns).from(entityTable);
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, EmployeeChangeLog.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<EmployeeChangeLog> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<EmployeeChangeLog> findById(UUID id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(StringUtils.wrap(id.toString(), "'")));
        return createQuery(null, whereClause).one();
    }

    @Override
    public Flux<EmployeeChangeLog> findAllByEmployeeIdOrderByChangeDateDesc(UUID employeeId, Pageable pageable) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("employee_id"), Conditions.just(StringUtils.wrap(employeeId.toString(), "'")));
        Sort defaultSort = Sort.by(Sort.Order.desc("change_date"));
        if (Objects.isNull(pageable)) {
            pageable = PageRequest.of(0, Integer.MAX_VALUE, defaultSort);
        }
        if (pageable.getSort().equals(Sort.unsorted())) {
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), defaultSort);
        }
        return createQuery(pageable, whereClause).all();
    }


    @Override
    public Mono<Long> countAllByEmployeeIdOrderByChangeDateDesc(UUID employeeId) {
        return db.sql("SELECT count(e.id) FROM employee_change_log e WHERE e.employee_id = :employeeId")
            .bind("employeeId", employeeId)
            .map((row, metadata) -> row.get(0, Long.class))
            .one();
    }


    private EmployeeChangeLog process(Row row, RowMetadata metadata) {
        EmployeeChangeLog entity = employeechangelogMapper.apply(row, "e");
        return entity;
    }

    @Override
    public <S extends EmployeeChangeLog> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
