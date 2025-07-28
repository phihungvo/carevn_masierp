package com.masi.logistics.repository;

import com.masi.logistics.domain.WarehouseType;
import com.masi.logistics.domain.criteria.WarehouseTypeCriteria;
import com.masi.logistics.repository.rowmapper.ColumnConverter;
import com.masi.logistics.repository.rowmapper.WarehouseTypeRowMapper;
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
 * Spring Data R2DBC custom repository implementation for the WarehouseType entity.
 */
@SuppressWarnings("unused")
class WarehouseTypeRepositoryInternalImpl extends SimpleR2dbcRepository<WarehouseType, UUID> implements WarehouseTypeRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final WarehouseTypeRowMapper warehousetypeMapper;
    private final ColumnConverter columnConverter;

    private static final Table entityTable = Table.aliased("warehouse_type", EntityManager.ENTITY_ALIAS);

    public WarehouseTypeRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        WarehouseTypeRowMapper warehousetypeMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter,
        ColumnConverter columnConverter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(WarehouseType.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.warehousetypeMapper = warehousetypeMapper;
        this.columnConverter = columnConverter;
    }

    @Override
    public Flux<WarehouseType> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<WarehouseType> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = WarehouseTypeSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        SelectFromAndJoin selectFrom = Select.builder().select(columns).from(entityTable);
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, WarehouseType.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<WarehouseType> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<WarehouseType> findById(UUID id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(StringUtils.wrap(id.toString(), "'")));
        return createQuery(null, whereClause).one();
    }

    private WarehouseType process(Row row, RowMetadata metadata) {
        WarehouseType entity = warehousetypeMapper.apply(row, "e");
        return entity;
    }

    @Override
    public <S extends WarehouseType> Mono<S> save(S entity) {
        return super.save(entity);
    }

    @Override
    public Flux<WarehouseType> findByCriteria(WarehouseTypeCriteria warehouseTypeCriteria, Pageable page) {
        return createQuery(page, buildConditions(warehouseTypeCriteria)).all();
    }

    @Override
    public Mono<Long> countByCriteria(WarehouseTypeCriteria criteria) {
        return findByCriteria(criteria, null)
            .collectList()
            .map(collectedList -> collectedList != null ? (long) collectedList.size() : (long) 0);
    }

    private Condition buildConditions(WarehouseTypeCriteria criteria) {
        ConditionBuilder builder = new ConditionBuilder(this.columnConverter);
        List<Condition> allConditions = new ArrayList<Condition>();
        if (criteria != null) {
            if (criteria.getId() != null) {
                builder.buildFilterConditionForField(criteria.getId(), entityTable.column("id"));
            }
            if (criteria.getCode() != null) {
                builder.buildFilterConditionForField(criteria.getCode(), entityTable.column("code"));
            }
            if (criteria.getName() != null) {
                builder.buildFilterConditionForField(criteria.getName(), entityTable.column("name"));
            }
            if (criteria.getDescription() != null) {
                builder.buildFilterConditionForField(criteria.getDescription(), entityTable.column("description"));
            }
            if (criteria.getActive() != null) {
                builder.buildFilterConditionForField(criteria.getActive(), entityTable.column("active"));
            }
            if (criteria.getCreateAt() != null) {
                builder.buildFilterConditionForField(criteria.getCreateAt(), entityTable.column("create_at"));
            }
            if (criteria.getCreateBy() != null) {
                builder.buildFilterConditionForField(criteria.getCreateBy(), entityTable.column("create_by"));
            }
            if (criteria.getUpdateAt() != null) {
                builder.buildFilterConditionForField(criteria.getUpdateAt(), entityTable.column("update_at"));
            }
            if (criteria.getUpdateBy() != null) {
                builder.buildFilterConditionForField(criteria.getUpdateBy(), entityTable.column("update_by"));
            }
            if (criteria.getDeleteAt() != null) {
                builder.buildFilterConditionForField(criteria.getDeleteAt(), entityTable.column("delete_at"));
            }
            if (criteria.getDeleteBy() != null) {
                builder.buildFilterConditionForField(criteria.getDeleteBy(), entityTable.column("delete_by"));
            }
            if (criteria.getCompany() != null) {
                builder.buildFilterConditionForField(criteria.getCompany(), entityTable.column("company"));
            }
        }
        return builder.buildConditions();
    }
}
