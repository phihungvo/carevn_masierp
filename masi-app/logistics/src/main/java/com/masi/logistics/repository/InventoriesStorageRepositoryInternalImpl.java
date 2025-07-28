package com.masi.logistics.repository;

import com.masi.logistics.domain.*;
import com.masi.logistics.domain.criteria.InventoriesStorageCriteria;
import com.masi.logistics.repository.rowmapper.*;
import com.masi.logistics.service.mapper.InventoriesDetailMapper;
import com.masi.logistics.service.mapper.ItemMapper;
import io.r2dbc.postgresql.codec.Json;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;

import java.util.ArrayList;
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
import org.springframework.data.relational.core.sql.*;
import org.springframework.data.relational.core.sql.SelectBuilder.SelectFromAndJoin;
import org.springframework.data.relational.repository.support.MappingRelationalEntityInformation;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.r2dbc.core.RowsFetchSpec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.service.ConditionBuilder;
import tech.jhipster.service.filter.BooleanFilter;

import static io.r2dbc.postgresql.codec.PostgresqlObjectId.*;

/**
 * Spring Data R2DBC custom repository implementation for the InventoriesStorage entity.
 */
@SuppressWarnings("unused")
class InventoriesStorageRepositoryInternalImpl
        extends SimpleR2dbcRepository<InventoriesStorage, UUID>
        implements InventoriesStorageRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final InventoriesStorageRowMapper inventoriesstorageMapper;
    private final ColumnConverter columnConverter;
    private final InventoriesDetailRowMapper inventoriesDetailMapper;
    private final ItemRowMapper itemMapper;
    private final ItemCategoryRowMapper itemCategoryMapper;
    private final WarehouseRowMapper warehouseMapper;
    private final SuppliersRowMapper suppliersMapper;
    private final UomRowMapper uomMapper;

    private static final Table entityTable = Table.aliased("inventories_storage", EntityManager.ENTITY_ALIAS);
    private static final Table itemTable = Table.aliased("item", "item"); //invoice_id
    private static final Table inventoriesDetailTable = Table.aliased("inventories_detail", "inventories_detail"); //invoice_id
    private static final Table itemCategoryTable = Table.aliased("item_category", "item_category"); //invoice_id
    private static final Table warehouseTable = Table.aliased("warehouse", "warehouse");
    private static final Table supplierTable = Table.aliased("suppliers", "suppliers");
    private static final Table entitySupplierTable = Table.aliased("suppliers", "entitySuppliers");

    private static final Table uomTable = Table.aliased("uom", "uom");
    private static final Table itemInfoTable = Table.aliased("item_info", "item_info");
    public InventoriesStorageRepositoryInternalImpl(
            R2dbcEntityTemplate template,
            EntityManager entityManager,
            InventoriesStorageRowMapper inventoriesstorageMapper,
            R2dbcEntityOperations entityOperations,
            R2dbcConverter converter,
            ColumnConverter columnConverter, InventoriesDetailRowMapper inventoriesDetailMapper, ItemRowMapper itemMapper, ItemCategoryRowMapper itemCategoryMapper, WarehouseRowMapper warehouseMapper, SuppliersRowMapper suppliersMapper, UomRowMapper uomMapper
    ) {
        super(
                new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(InventoriesStorage.class)),
                entityOperations,
                converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.inventoriesstorageMapper = inventoriesstorageMapper;
        this.columnConverter = columnConverter;
        this.inventoriesDetailMapper = inventoriesDetailMapper;
        this.itemMapper = itemMapper;
        this.itemCategoryMapper = itemCategoryMapper;
        this.warehouseMapper = warehouseMapper;
        this.suppliersMapper = suppliersMapper;
        this.uomMapper = uomMapper;
    }

    @Override
    public Flux<InventoriesStorage> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<InventoriesStorage> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = InventoriesStorageSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(ItemSqlHelper.getColumns(itemTable, "item"));
        columns.addAll(InventoriesDetailSqlHelper.getColumns(inventoriesDetailTable, "inventories_detail"));
        columns.addAll(ItemCategorySqlHelper.getColumns(itemCategoryTable, "item_category"));
        columns.addAll(WarehouseSqlHelper.getColumns(warehouseTable, "warehouse"));
        columns.addAll(SuppliersSqlHelper.getColumns(supplierTable, "suppliers"));
        columns.addAll(UomSqlHelper.getColumns(uomTable, "uom"));
        columns.addAll(SuppliersSqlHelper.getColumns(supplierTable, "entitySuppliers"));
        columns.addAll(ItemInfoSqlHelper.getColumns(itemInfoTable, "itemInfo"));

        SelectBuilder.SelectFromAndJoinCondition selectFrom = Select.builder()
                .select(columns)
                .from(entityTable)
                .leftOuterJoin(itemTable)
                .on(Column.create("item_id", entityTable))
                .equals(Column.create("id", itemTable))
                .leftOuterJoin(inventoriesDetailTable)
                .on(Column.create("inventories_detail_id", entityTable))
                .equals(Column.create("id", inventoriesDetailTable))
                .leftOuterJoin(itemCategoryTable)
                .on(Column.create("item_category_id", itemTable))
                .equals(Column.create("id", itemCategoryTable))
                .leftOuterJoin(warehouseTable)
                .on(Column.create("warehouse_id", entityTable))
                .equals(Column.create("id", warehouseTable))
                .leftOuterJoin(supplierTable)
                .on(Column.create("supplier_id", itemTable))
                .equals(Column.create("id", supplierTable))
                .leftOuterJoin(uomTable)
                .on(Column.create("uom_id", itemTable))
                .equals(Column.create("id", uomTable))
                .leftOuterJoin(entitySupplierTable)
                .on(Column.create("supplier_id", entityTable))
                .equals(Column.create("id", entitySupplierTable))
                .leftOuterJoin(itemInfoTable)
                .on(Column.create("id", entityTable))
                .equals(Column.create("inventory_storage_id", itemInfoTable));
        ;
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, InventoriesStorage.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<InventoriesStorage> findAll() {
        return findAllBy(null);
    }

//    @Override
//    public Flux<InventoriesStorage> saveAll(List<InventoriesStorage> storageList) {
//        return null;
//    }

    @Override
    public Mono<InventoriesStorage> findById(UUID id) {
        Condition whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(StringUtils.wrap(id.toString(), "'")))
                .and(itemInfoTable.column("is_deleted").isEqualTo(SQL.literalOf(false)));
        return createQuery(null, whereClause).one();
    }

    private InventoriesStorage process(Row row, RowMetadata metadata) {
        InventoriesStorage entity = inventoriesstorageMapper.apply(row, "e");
        entity.setSupplier(suppliersMapper.apply(row, "entitySuppliers"));
        if (itemTable != null) {
            Item item = itemMapper.apply(row, "item");
            ItemCategory category = itemCategoryMapper.apply(row, "item_category");
            item.setItemCategory(category);
            item.setSupplier(suppliersMapper.apply(row, "suppliers"));
            item.setUom(uomMapper.apply(row, "uom"));
            entity.setItem(item);
        }

        if (inventoriesDetailTable != null) {
            entity.setInventoriesDetail(inventoriesDetailMapper.apply(row, "inventories_detail"));
        }

        if (warehouseTable != null) {
            entity.setWarehouse(warehouseMapper.apply(row, "warehouse"));
        }

        return entity;
    }

    @Override
    public <S extends InventoriesStorage> Mono<S> save(S entity) {
        return super.save(entity);
    }

    @Override
    public Flux<InventoriesStorage> findByCriteria(InventoriesStorageCriteria inventoriesStorageCriteria, Pageable page) {
        page = getDefaultSort(page);
        return createQuery(page, buildConditions(inventoriesStorageCriteria)).all();
    }

    @Override
    public Mono<Long> countByCriteria(InventoriesStorageCriteria criteria) {
        return findByCriteria(criteria, null)
                .collectList()
                .map(collectedList -> collectedList != null ? (long) collectedList.size() : (long) 0);
    }

    private Pageable getDefaultSort(Pageable pageable) {
        Sort defaultSort = Sort.by(Sort.Direction.DESC, "created_at");
        if (Objects.isNull(pageable)) {
            pageable = PageRequest.of(0, Integer.MAX_VALUE, defaultSort);
        }
        if (pageable.getSort().equals(Sort.unsorted())) {
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), defaultSort);
        }
        return pageable;
    }

    private Condition buildConditions(InventoriesStorageCriteria criteria) {
        ConditionBuilder builder = new ConditionBuilder(this.columnConverter);
        builder.buildFilterConditionForField(new BooleanFilter().setEquals(false), entityTable.column("is_deleted"));
        List<Condition> allConditions = new ArrayList<>();
        allConditions.add(itemInfoTable.column("is_deleted").isEqualTo(SQL.literalOf(false)));

        if (criteria != null) {


            if (criteria.getStatusDevice() != null) {
                Condition statusCondition = Conditions.just("e.attribute->'general'->>'status' = '" + criteria.getStatusDevice() + "'");
                allConditions.add(statusCondition);
            }


            if (criteria.getId() != null) {
                builder.buildFilterConditionForField(criteria.getId(), entityTable.column("id"));
            }
            if (criteria.getStatus() != null) {
                builder.buildFilterConditionForField(criteria.getStatus(), entityTable.column("status"));
            }

            if (criteria.getCheckFishMeal() != null && criteria.getCheckFishMeal()) {
                Condition group1 = itemTable.column("percent_protein").isNotNull();
                Condition group2 = itemTable.column("percent_protein").isGreater(SQL.literalOf(0));
                Condition subgroup1 = Conditions.nest(group1.and(group2));
                allConditions.add(subgroup1);
            } else {
                Condition group1 = itemTable.column("percent_protein").isNull();
                Condition group2 = itemTable.column("percent_protein").isEqualTo(SQL.literalOf(0));
                Condition subgroup1 = Conditions.nest(group1.or(group2));
                allConditions.add(subgroup1);
            }


            if (criteria.getCheckDepreciation() != null && criteria.getCheckDepreciation()) {
                Condition group1 = Conditions.isNotEqual(
                        entityTable.column("status"),
                        SQL.literalOf("LIQUIDATION")
                );

                Condition group2 = Conditions.isGreaterOrEqualTo(
                        entityTable.column("price"),
                        entityTable.column("remaining_price")
                );
                Condition group3 = Conditions.isGreater(
                        entityTable.column("remaining_price"),
                        SQL.literalOf(0)
                );
                Condition subgroup1 = Conditions.nest(group1.and(group2.and(group3)));
                allConditions.add(subgroup1);
            }

            if (criteria.getCode() != null && !criteria.getCode().getContains().isEmpty()) {
                Condition group1 = Conditions.like(entityTable.column("code"), SQL.literalOf("%" + criteria.getCode().getContains().trim() + "%"));
                Condition group2 = Conditions.like(itemTable.column("code"), SQL.literalOf("%" + criteria.getCode().getContains().trim() + "%"));
                Condition group3 = Conditions.like(itemTable.column("name"), SQL.literalOf("%" + criteria.getCode().getContains().trim() + "%"));
                Condition subgroup1 = Conditions.nest(group1.or(group2).or(group3));
                allConditions.add(subgroup1);
            }
            if (criteria.getItemId() != null) {
                builder.buildFilterConditionForField(criteria.getItemId(), entityTable.column("item_id"));
            }
            if (criteria.getInventoriesDetailId() != null) {
                builder.buildFilterConditionForField(criteria.getInventoriesDetailId(), entityTable.column("inventories_detail_id"));
            }
            if (criteria.getImportDate() != null) {
                builder.buildFilterConditionForField(criteria.getImportDate(), entityTable.column("import_date"));
            }
            if (criteria.getExportDate() != null) {
                builder.buildFilterConditionForField(criteria.getExportDate(), entityTable.column("export_date"));
            }
            if (criteria.getDepreciation() != null) {
                builder.buildFilterConditionForField(criteria.getDepreciation(), entityTable.column("depreciation"));
            }
            if (criteria.getExpiryDate() != null) {
                builder.buildFilterConditionForField(criteria.getExpiryDate(), entityTable.column("expiry_date"));
            }
            if (criteria.getAttribute() != null) {
                builder.buildFilterConditionForField(criteria.getAttribute(), entityTable.column("attribute"));
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
                Condition group1 = Conditions.isNull(entityTable.column("department"));
                Condition group2 = Conditions.isEqual(entityTable.column("department"), SQL.literalOf(criteria.getDepartment().getContains()));
                Condition subgroup1 = Conditions.nest(group1.or(group2));
                allConditions.add(subgroup1);
            }
            if (criteria.getSupplierId() != null) {
                builder.buildFilterConditionForField(criteria.getSupplierId(), itemTable.column("supplier_id"));
            }
            if (criteria.getUomId() != null) {
                builder.buildFilterConditionForField(criteria.getUomId(), itemTable.column("uom_id"));
            }
            if (criteria.getItemCategoryId() != null) {
                builder.buildFilterConditionForField(criteria.getItemCategoryId(), itemTable.column("item_category_id"));
            }

            if (criteria.getUserPosition() != null){
                builder.buildFilterConditionForField(criteria.getUserPosition(), itemInfoTable.column("user_position"));
            }
        }
        allConditions.add(builder.buildConditions());
        return allConditions.stream().reduce(Condition::and).orElse(null);
    }
}
