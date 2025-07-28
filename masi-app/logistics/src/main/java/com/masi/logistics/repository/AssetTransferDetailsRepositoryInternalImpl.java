package com.masi.logistics.repository;

import com.masi.logistics.domain.AssetTransferDetails;
import com.masi.logistics.domain.Item;
import com.masi.logistics.domain.ItemAssetTransfer;
import com.masi.logistics.domain.criteria.AssetTransferDetailsCriteria;
import com.masi.logistics.repository.rowmapper.*;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.formula.functions.T;
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
 * Spring Data R2DBC custom repository implementation for the AssetTransferDetails entity.
 */
@SuppressWarnings("unused")
class AssetTransferDetailsRepositoryInternalImpl
    extends SimpleR2dbcRepository<AssetTransferDetails, UUID>
    implements AssetTransferDetailsRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final AssetTransferDetailsRowMapper assettransferdetailsMapper;
    private final InventoriesStorageRowMapper inventoriesstorageMapper;
    private final ItemRowMapper itemMapper;
    private final ItemCategoryRowMapper itemCategoryMapper;
    private final SuppliersRowMapper suppliersMapper;
    private final ItemAssetTransferRowMapper itemAssetTransferMapper;
    private final UomRowMapper uomMapper;

    private final ColumnConverter columnConverter;

    private static final Table entityTable = Table.aliased("asset_transfer_details", EntityManager.ENTITY_ALIAS);
    private static final Table itemTable = Table.aliased("item", "item");
    private static final Table uomTable = Table.aliased("uom", "uom");
    private static final Table itemCategoryTable = Table.aliased("item_category", "itemCategory");
    private static final Table supplierTable = Table.aliased("suppliers", "suppliers");
    private static final Table inventoriesStorageTable = Table.aliased("inventories_storage", "inventoriesStorage");
    private static final Table itemAssetTransferTable = Table.aliased("item_asset_transfer", "itemAssetTransfer");
    public AssetTransferDetailsRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        AssetTransferDetailsRowMapper assettransferdetailsMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter, InventoriesStorageRowMapper inventoriesstorageMapper, ItemRowMapper itemMapper, ItemCategoryRowMapper itemCategoryMapper, SuppliersRowMapper suppliersMapper, ItemAssetTransferRowMapper itemAssetTransferMapper, UomRowMapper uomMapper,
        ColumnConverter columnConverter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(AssetTransferDetails.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.assettransferdetailsMapper = assettransferdetailsMapper;
        this.inventoriesstorageMapper = inventoriesstorageMapper;
        this.itemMapper = itemMapper;
        this.itemCategoryMapper = itemCategoryMapper;
        this.suppliersMapper = suppliersMapper;
        this.itemAssetTransferMapper = itemAssetTransferMapper;
        this.uomMapper = uomMapper;
        this.columnConverter = columnConverter;
    }

    @Override
    public Flux<AssetTransferDetails> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<AssetTransferDetails> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = AssetTransferDetailsSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(ItemSqlHelper.getColumns(itemTable, "item"));
        columns.addAll(UomSqlHelper.getColumns(uomTable, "uom"));
        columns.addAll(ItemCategorySqlHelper.getColumns(itemCategoryTable, "itemCategory"));
        columns.addAll(SuppliersSqlHelper.getColumns(supplierTable, "suppliers"));
        columns.addAll(InventoriesStorageSqlHelper.getColumns(inventoriesStorageTable, "inventories_storage"));
        columns.addAll(ItemAssetTransferSqlHelper.getColumns(itemAssetTransferTable, "itemAssetTransfer"));

        SelectBuilder.SelectFromAndJoinCondition selectFrom = Select.builder()
                .select(columns)
                .from(entityTable)

                .leftOuterJoin(inventoriesStorageTable)
                .on(Column.create("inventories_storage_id", entityTable))
                .equals(Column.create("id", inventoriesStorageTable))

                .leftOuterJoin(itemTable)
                .on(Column.create("item_id", inventoriesStorageTable))
                .equals(Column.create("id", itemTable))

                .leftOuterJoin(uomTable)
                .on(Column.create("uom_id", itemTable))
                .equals(Column.create("id", uomTable))

                .leftOuterJoin(itemCategoryTable)
                .on(Column.create("item_category_id", itemTable))
                .equals(Column.create("id", itemCategoryTable))

                .leftOuterJoin(supplierTable)
                .on(Column.create("supplier_id", itemTable))
                .equals(Column.create("id", supplierTable))

                .leftOuterJoin(itemAssetTransferTable)
                .on(Column.create("item_asset_transfer_id", entityTable))
                .equals(Column.create("id", itemAssetTransferTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, AssetTransferDetails.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<AssetTransferDetails> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<AssetTransferDetails> findById(UUID id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(StringUtils.wrap(id.toString(), "'")));
        return createQuery(null, whereClause).one();
    }

    private AssetTransferDetails process(Row row, RowMetadata metadata) {
        AssetTransferDetails entity = assettransferdetailsMapper.apply(row, "e");
        entity.setInventoriesStorage(inventoriesstorageMapper.apply(row, "inventories_storage"));

        Item item = itemMapper.apply(row, "item");
        item.setSupplier(suppliersMapper.apply(row, "suppliers"));
        entity.getInventoriesStorage().setItem(item);


        entity.getInventoriesStorage().getItem().setUom(uomMapper.apply(row, "uom"));
        entity.getInventoriesStorage().getItem().setItemCategory(itemCategoryMapper.apply(row, "itemCategory"));
        entity.setItemAssetTransfer(itemAssetTransferMapper.apply(row, "itemAssetTransfer"));
        return entity;
    }

    @Override
    public <S extends AssetTransferDetails> Mono<S> save(S entity) {
        return super.save(entity);
    }

    @Override
    public Flux<AssetTransferDetails> findByCriteria(AssetTransferDetailsCriteria assetTransferDetailsCriteria, Pageable page) {
        return createQuery(page, buildConditions(assetTransferDetailsCriteria)).all();
    }

    @Override
    public Mono<Long> countByCriteria(AssetTransferDetailsCriteria criteria) {
        return findByCriteria(criteria, null)
            .collectList()
            .map(collectedList -> collectedList != null ? (long) collectedList.size() : (long) 0);
    }

    private Condition buildConditions(AssetTransferDetailsCriteria criteria) {
        ConditionBuilder builder = new ConditionBuilder(this.columnConverter);
        List<Condition> allConditions = new ArrayList<Condition>();
        if (criteria != null) {
            if (criteria.getId() != null) {
                builder.buildFilterConditionForField(criteria.getId(), entityTable.column("id"));
            }
            if (criteria.getCode() != null) {
                builder.buildFilterConditionForField(criteria.getCode(), entityTable.column("code"));
            }
//            if (criteria.getAttribute() != null) {
//                builder.buildFilterConditionForField(criteria.getAttribute(), entityTable.column("attribute"));
//            }
            if (criteria.getName() != null) {
                builder.buildFilterConditionForField(criteria.getName(), entityTable.column("name"));
            }
            if (criteria.getStatus() != null) {
                builder.buildFilterConditionForField(criteria.getStatus(), entityTable.column("status"));
            }
            if (criteria.getInventoriesStorageId() != null) {
                builder.buildFilterConditionForField(criteria.getInventoriesStorageId(), entityTable.column("inventories_storage_id"));
            }
            if (criteria.getQuantity() != null) {
                builder.buildFilterConditionForField(criteria.getQuantity(), entityTable.column("quantity"));
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
            if (criteria.getItemAssetTransferId() != null){
                builder.buildFilterConditionForField(criteria.getItemAssetTransferId(), entityTable.column("item_asset_transfer_id"));
            }
        }
        return builder.buildConditions();
    }
}
