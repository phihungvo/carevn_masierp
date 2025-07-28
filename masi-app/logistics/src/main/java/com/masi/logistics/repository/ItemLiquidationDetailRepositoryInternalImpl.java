package com.masi.logistics.repository;

import com.masi.logistics.domain.InventoriesStorage;
import com.masi.logistics.domain.Item;
import com.masi.logistics.domain.ItemCategory;
import com.masi.logistics.domain.ItemLiquidationDetail;
import com.masi.logistics.domain.criteria.ItemLiquidationDetailCriteria;
import com.masi.logistics.repository.rowmapper.*;
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

/**
 * Spring Data R2DBC custom repository implementation for the ItemLiquidationDetail entity.
 */
@SuppressWarnings("unused")
class ItemLiquidationDetailRepositoryInternalImpl
    extends SimpleR2dbcRepository<ItemLiquidationDetail, UUID>
    implements ItemLiquidationDetailRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final ItemLiquidationDetailRowMapper itemliquidationdetailMapper;
    private final ColumnConverter columnConverter;
    private final SuppliersRowMapper suppliersMapper;
    private final InventoriesStorageRowMapper inventoriesstorageMapper;
    private final ItemRowMapper itemMapper;

    private static final Table entityTable = Table.aliased("item_liquidation_detail", EntityManager.ENTITY_ALIAS);

    private static final Table inventoriesStorageTable = Table.aliased("inventories_storage", "inventories_storage");
    private static final Table itemTable = Table.aliased("item", "item"); //invoice_id
    private static final Table supplierTable = Table.aliased("suppliers", "suppliers");

    public ItemLiquidationDetailRepositoryInternalImpl(
            R2dbcEntityTemplate template,
            EntityManager entityManager,
            ItemLiquidationDetailRowMapper itemliquidationdetailMapper,
            R2dbcEntityOperations entityOperations,
            R2dbcConverter converter,
            ColumnConverter columnConverter, SuppliersRowMapper suppliersMapper, InventoriesStorageRowMapper inventoriesstorageMapper, ItemRowMapper itemMapper
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(ItemLiquidationDetail.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.itemliquidationdetailMapper = itemliquidationdetailMapper;
        this.columnConverter = columnConverter;
        this.suppliersMapper = suppliersMapper;
        this.inventoriesstorageMapper = inventoriesstorageMapper;
        this.itemMapper = itemMapper;
    }

    @Override
    public Flux<ItemLiquidationDetail> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<ItemLiquidationDetail> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = ItemLiquidationDetailSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(InventoriesStorageSqlHelper.getColumns(inventoriesStorageTable, "inventories_storage"));
        columns.addAll(ItemSqlHelper.getColumns(itemTable, "item"));
        columns.addAll(SuppliersSqlHelper.getColumns(supplierTable, "suppliers"));

        SelectBuilder.SelectFromAndJoinCondition selectFrom = Select.builder()
                .select(columns)
                .from(entityTable)

                .leftOuterJoin(inventoriesStorageTable)
                .on(Column.create("inventories_storage_id", entityTable))
                .equals(Column.create("id", inventoriesStorageTable))

                .leftOuterJoin(itemTable)
                .on(Column.create("item_id", inventoriesStorageTable))
                .equals(Column.create("id", itemTable))

                .leftOuterJoin(supplierTable)
                .on(Column.create("supplier_id", itemTable))
                .equals(Column.create("id", supplierTable))
                ;
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, ItemLiquidationDetail.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<ItemLiquidationDetail> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<ItemLiquidationDetail> findById(UUID id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(StringUtils.wrap(id.toString(), "'")));
        return createQuery(null, whereClause).one();
    }

    private ItemLiquidationDetail process(Row row, RowMetadata metadata) {
        ItemLiquidationDetail entity = itemliquidationdetailMapper.apply(row, "e");

        if (inventoriesStorageTable != null) {
            InventoriesStorage inventoriesStorage = inventoriesstorageMapper.apply(row, "inventories_storage");

            Item item = itemMapper.apply(row, "item");
            item.setSupplier(suppliersMapper.apply(row, "suppliers"));
            inventoriesStorage.setItem(item);
            entity.setInventoriesStorage(inventoriesStorage);
        }


        return entity;
    }

    @Override
    public <S extends ItemLiquidationDetail> Mono<S> save(S entity) {
        return super.save(entity);
    }

    @Override
    public Flux<ItemLiquidationDetail> findByCriteria(ItemLiquidationDetailCriteria itemLiquidationDetailCriteria, Pageable page) {
        return createQuery(page, buildConditions(itemLiquidationDetailCriteria)).all();
    }

    @Override
    public Mono<Long> countByCriteria(ItemLiquidationDetailCriteria criteria) {
        return findByCriteria(criteria, null)
            .collectList()
            .map(collectedList -> collectedList != null ? (long) collectedList.size() : (long) 0);
    }

    private Condition buildConditions(ItemLiquidationDetailCriteria criteria) {
        ConditionBuilder builder = new ConditionBuilder(this.columnConverter);
        List<Condition> allConditions = new ArrayList<Condition>();
        if (criteria != null) {
            if (criteria.getId() != null) {
                builder.buildFilterConditionForField(criteria.getId(), entityTable.column("id"));
            }
            if (criteria.getCode() != null) {
                builder.buildFilterConditionForField(criteria.getCode(), entityTable.column("code"));
            }
            if (criteria.getAttribute() != null) {
                builder.buildFilterConditionForField(criteria.getAttribute(), entityTable.column("attribute"));
            }
            if (criteria.getName() != null) {
                builder.buildFilterConditionForField(criteria.getName(), entityTable.column("name"));
            }
            if (criteria.getLiquidationDate() != null) {
                builder.buildFilterConditionForField(criteria.getLiquidationDate(), entityTable.column("liquidation_date"));
            }
            if (criteria.getDescription() != null) {
                builder.buildFilterConditionForField(criteria.getDescription(), entityTable.column("description"));
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
        return builder.buildConditions();
    }
}
