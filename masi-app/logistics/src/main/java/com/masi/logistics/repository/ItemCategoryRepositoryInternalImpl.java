package com.masi.logistics.repository;

import com.masi.logistics.domain.ItemCategory;
import com.masi.logistics.domain.criteria.ItemCategoryCriteria;
import com.masi.logistics.repository.rowmapper.ColumnConverter;
import com.masi.logistics.repository.rowmapper.ItemCategoryRowMapper;
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
import tech.jhipster.service.ConditionBuilder;

/**
 * Spring Data R2DBC custom repository implementation for the ItemCategory entity.
 */
@SuppressWarnings("unused")
class ItemCategoryRepositoryInternalImpl extends SimpleR2dbcRepository<ItemCategory, UUID> implements ItemCategoryRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final WarehouseTypeRowMapper warehousetypeMapper;
    private final ItemCategoryRowMapper itemcategoryMapper;
    private final ColumnConverter columnConverter;

    private static final Table entityTable = Table.aliased("item_category", EntityManager.ENTITY_ALIAS);
    private static final Table warehouseTypeTable = Table.aliased("warehouse_type", "warehouseType");

    public ItemCategoryRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        WarehouseTypeRowMapper warehousetypeMapper,
        ItemCategoryRowMapper itemcategoryMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter,
        ColumnConverter columnConverter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(ItemCategory.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.warehousetypeMapper = warehousetypeMapper;
        this.itemcategoryMapper = itemcategoryMapper;
        this.columnConverter = columnConverter;
    }

    @Override
    public Flux<ItemCategory> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<ItemCategory> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = ItemCategorySqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(WarehouseTypeSqlHelper.getColumns(warehouseTypeTable, "warehouseType"));
        SelectFromAndJoinCondition selectFrom = Select.builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(warehouseTypeTable)
            .on(Column.create("warehouse_type_id", entityTable))
            .equals(Column.create("id", warehouseTypeTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, ItemCategory.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<ItemCategory> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<ItemCategory> findById(UUID id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(StringUtils.wrap(id.toString(), "'")));
        return createQuery(null, whereClause).one();
    }

    private ItemCategory process(Row row, RowMetadata metadata) {
        ItemCategory entity = itemcategoryMapper.apply(row, "e");
        entity.setWarehouseType(warehousetypeMapper.apply(row, "warehouseType"));
        return entity;
    }

    @Override
    public <S extends ItemCategory> Mono<S> save(S entity) {
        return super.save(entity);
    }

    @Override
    public Flux<ItemCategory> findByCriteria(ItemCategoryCriteria itemCategoryCriteria, Pageable page) {
        return createQuery(page, buildConditions(itemCategoryCriteria)).all();
    }

    @Override
    public Mono<Long> countByCriteria(ItemCategoryCriteria criteria) {
        return findByCriteria(criteria, null)
            .collectList()
            .map(collectedList -> collectedList != null ? (long) collectedList.size() : (long) 0);
    }

    private Condition buildConditions(ItemCategoryCriteria criteria) {
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
            if (criteria.getWarehouseTypeId() != null) {
                builder.buildFilterConditionForField(criteria.getWarehouseTypeId(), warehouseTypeTable.column("id"));
            }
            if (criteria.getItemTypeCategory() != null) {
                builder.buildFilterConditionForField(criteria.getItemTypeCategory(), entityTable.column("type_item_category"));
            }
        }
        return builder.buildConditions();
    }
}
