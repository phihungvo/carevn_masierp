package com.masi.logistics.repository;

import com.masi.logistics.domain.InventoriesType;
import com.masi.logistics.domain.criteria.InventoriesTypeCriteria;
import com.masi.logistics.repository.rowmapper.ColumnConverter;
import com.masi.logistics.repository.rowmapper.InventoriesTypeRowMapper;
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
import org.springframework.data.relational.core.sql.*;
import org.springframework.data.relational.core.sql.SelectBuilder.SelectFromAndJoin;
import org.springframework.data.relational.repository.support.MappingRelationalEntityInformation;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.r2dbc.core.RowsFetchSpec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.service.ConditionBuilder;
import tech.jhipster.service.filter.BooleanFilter;

/**
 * Spring Data R2DBC custom repository implementation for the InventoriesType entity.
 */
@SuppressWarnings("unused")
class InventoriesTypeRepositoryInternalImpl
    extends SimpleR2dbcRepository<InventoriesType, UUID>
    implements InventoriesTypeRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final InventoriesTypeRowMapper inventoriestypeMapper;
    private final ColumnConverter columnConverter;

    private static final Table entityTable = Table.aliased("inventories_type", EntityManager.ENTITY_ALIAS);

    public InventoriesTypeRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        InventoriesTypeRowMapper inventoriestypeMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter,
        ColumnConverter columnConverter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(InventoriesType.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.inventoriestypeMapper = inventoriestypeMapper;
        this.columnConverter = columnConverter;
    }

    @Override
    public Flux<InventoriesType> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<InventoriesType> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = InventoriesTypeSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        SelectFromAndJoin selectFrom = Select.builder().select(columns).from(entityTable);
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, InventoriesType.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<InventoriesType> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<InventoriesType> findById(UUID id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(StringUtils.wrap(id.toString(), "'")));
        return createQuery(null, whereClause).one();
    }

    private InventoriesType process(Row row, RowMetadata metadata) {
        InventoriesType entity = inventoriestypeMapper.apply(row, "e");
        return entity;
    }

    @Override
    public <S extends InventoriesType> Mono<S> save(S entity) {
        return super.save(entity);
    }

    @Override
    public Flux<InventoriesType> findByCriteria(InventoriesTypeCriteria inventoriesTypeCriteria, Pageable page) {
        return createQuery(page, buildConditions(inventoriesTypeCriteria)).all();
    }

    @Override
    public Mono<Long> countByCriteria(InventoriesTypeCriteria criteria) {
        return findByCriteria(criteria, null)
            .collectList()
            .map(collectedList -> collectedList != null ? (long) collectedList.size() : (long) 0);
    }

    private Condition buildConditions(InventoriesTypeCriteria criteria) {
        ConditionBuilder builder = new ConditionBuilder(this.columnConverter);
        builder.buildFilterConditionForField(new BooleanFilter().setEquals(false), entityTable.column("is_deleted"));
        Condition subgroup1 = null;
        if (criteria != null) {
            if (criteria.getId() != null) {
                builder.buildFilterConditionForField(criteria.getId(), entityTable.column("id"));
            }
            if ((criteria.getCode() != null && !criteria.getCode().getContains().toString().equals("")) || (criteria.getName() != null && !criteria.getName().getContains().toString().equals(""))) {
                Condition group1 = Conditions.like(entityTable.column("code"), SQL.literalOf("%" + criteria.getCode().getContains() + "%"));
                Condition group2 =  Conditions.like(entityTable.column("name"), SQL.literalOf("%" + criteria.getName().getContains() + "%"));
                subgroup1 = Conditions.nest(group1.or(group2));
                builder.buildConditions().and(subgroup1);
            }
            if (criteria.getDescription() != null) {
                builder.buildFilterConditionForField(criteria.getDescription(), entityTable.column("description"));
            }
            if (criteria.getIsActive() != null) {
                builder.buildFilterConditionForField(criteria.getIsActive(), entityTable.column("is_active"));
            }
            if (criteria.getIsDeleted() != null) {
                builder.buildFilterConditionForField(criteria.getIsDeleted(), entityTable.column("is_deleted"));
            }
            if (criteria.getCreatedAt() != null) {
                builder.buildFilterConditionForField(criteria.getCreatedAt(), entityTable.column("created_at"));
            }
            if (criteria.getCreatedBy() != null) {
                builder.buildFilterConditionForField(criteria.getCreatedBy(), entityTable.column("created_by"));
            }
            if (criteria.getUpdatedAt() != null) {
                builder.buildFilterConditionForField(criteria.getUpdatedAt(), entityTable.column("updated_at"));
            }
            if (criteria.getUpdatedBy() != null) {
                builder.buildFilterConditionForField(criteria.getUpdatedBy(), entityTable.column("updated_by"));
            }
            if (criteria.getDeletedAt() != null) {
                builder.buildFilterConditionForField(criteria.getDeletedAt(), entityTable.column("deleted_at"));
            }
            if (criteria.getDeletedBy() != null) {
                builder.buildFilterConditionForField(criteria.getDeletedBy(), entityTable.column("deleted_by"));
            }
            if (criteria.getCompany() != null) {
                builder.buildFilterConditionForField(criteria.getCompany(), entityTable.column("company"));
            }
            if (criteria.getDepartment() != null) {
                builder.buildFilterConditionForField(criteria.getDepartment(), entityTable.column("department"));
            }
        }
        if(subgroup1 == null)
            return builder.buildConditions();
        return builder.buildConditions().and(subgroup1);
    }
}
