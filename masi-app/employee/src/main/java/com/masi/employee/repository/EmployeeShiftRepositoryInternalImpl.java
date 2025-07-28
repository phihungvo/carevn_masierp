package com.masi.employee.repository;

import com.masi.employee.domain.EmployeeShift;
import com.masi.employee.domain.criteria.EmployeeShiftCriteria;
import com.masi.employee.repository.rowmapper.ColumnConverter;
import com.masi.employee.repository.rowmapper.EmployeeShiftRowMapper;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
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
import tech.jhipster.service.ConditionBuilder;

/**
 * Spring Data R2DBC custom repository implementation for the EmployeeShift entity.
 */
@SuppressWarnings("unused")
class EmployeeShiftRepositoryInternalImpl extends SimpleR2dbcRepository<EmployeeShift, UUID> implements EmployeeShiftRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final EmployeeShiftRowMapper employeeshiftMapper;
    private final ColumnConverter columnConverter;

    private static final Table entityTable = Table.aliased("employee_shift", EntityManager.ENTITY_ALIAS);

    public EmployeeShiftRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        EmployeeShiftRowMapper employeeshiftMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter,
        ColumnConverter columnConverter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(EmployeeShift.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.employeeshiftMapper = employeeshiftMapper;
        this.columnConverter = columnConverter;
    }

    @Override
    public Flux<EmployeeShift> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<EmployeeShift> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = EmployeeShiftSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        SelectFromAndJoin selectFrom = Select.builder().select(columns).from(entityTable);
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, EmployeeShift.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<EmployeeShift> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<EmployeeShift> findById(UUID id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(StringUtils.wrap(id.toString(), "'")));
        return createQuery(null, whereClause).one();
    }

    private EmployeeShift process(Row row, RowMetadata metadata) {
        EmployeeShift entity = employeeshiftMapper.apply(row, "e");
        return entity;
    }

    @Override
    public <S extends EmployeeShift> Mono<S> save(S entity) {
        return super.save(entity);
    }

    @Override
    public Flux<EmployeeShift> findByCriteria(EmployeeShiftCriteria employeeShiftCriteria, Pageable page) {
        return createQuery(page, buildConditions(employeeShiftCriteria)).all();
    }

    @Override
    public Mono<Long> countByCriteria(EmployeeShiftCriteria criteria) {
        return findByCriteria(criteria, null)
            .collectList()
            .map(collectedList -> collectedList != null ? (long) collectedList.size() : (long) 0);
    }

    private Condition buildConditions(EmployeeShiftCriteria criteria) {
        ConditionBuilder builder = new ConditionBuilder(this.columnConverter);
        List<Condition> allConditions = new ArrayList<Condition>();
        if (criteria != null) {
            if (criteria.getId() != null) {
                builder.buildFilterConditionForField(criteria.getId(), entityTable.column("id"));
            }
            if (criteria.getEmployeeId() != null) {
                builder.buildFilterConditionForField(criteria.getEmployeeId(), entityTable.column("employee_id"));
            }
            if (criteria.getShiftId() != null) {
                builder.buildFilterConditionForField(criteria.getShiftId(), entityTable.column("shift_id"));
            }
            if (criteria.getCompany() != null) {
                builder.buildFilterConditionForField(criteria.getCompany(), entityTable.column("company"));
            }
            if (criteria.getDepartment() != null) {
                builder.buildFilterConditionForField(criteria.getDepartment(), entityTable.column("department"));
            }
            if (criteria.getIsDeleted() != null) {
                builder.buildFilterConditionForField(criteria.getIsDeleted(), entityTable.column("is_deleted"));
            }
            if (criteria.getCreatedBy() != null) {
                builder.buildFilterConditionForField(criteria.getCreatedBy(), entityTable.column("created_by"));
            }
            if (criteria.getCreatedDate() != null) {
                builder.buildFilterConditionForField(criteria.getCreatedDate(), entityTable.column("created_date"));
            }
            if (criteria.getUpdatedBy() != null) {
                builder.buildFilterConditionForField(criteria.getUpdatedBy(), entityTable.column("updated_by"));
            }
            if (criteria.getUpdatedAt() != null) {
                builder.buildFilterConditionForField(criteria.getUpdatedAt(), entityTable.column("updated_at"));
            }
            if (criteria.getDeletedBy() != null) {
                builder.buildFilterConditionForField(criteria.getDeletedBy(), entityTable.column("deleted_by"));
            }
            if (criteria.getDeletedAt() != null) {
                builder.buildFilterConditionForField(criteria.getDeletedAt(), entityTable.column("deleted_at"));
            }
        }
        return builder.buildConditions();
    }
}
