package com.masi.logistics.repository;

import com.masi.logistics.domain.Item;
import com.masi.logistics.domain.ItemAssetDepreciationDetail;
import com.masi.logistics.domain.ItemCategory;
import com.masi.logistics.domain.criteria.ItemAssetDepreciationDetailCriteria;
import com.masi.logistics.repository.rowmapper.ColumnConverter;
import com.masi.logistics.repository.rowmapper.InventoriesStorageRowMapper;
import com.masi.logistics.repository.rowmapper.ItemAssetDepreciationDetailRowMapper;
import com.masi.logistics.repository.rowmapper.ItemRowMapper;
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

/**
 * Spring Data R2DBC custom repository implementation for the ItemAssetDepreciationDetail entity.
 */
@SuppressWarnings("unused")
class ItemAssetDepreciationDetailRepositoryInternalImpl
        extends SimpleR2dbcRepository<ItemAssetDepreciationDetail, UUID>
        implements ItemAssetDepreciationDetailRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final ItemAssetDepreciationDetailRowMapper itemassetdepreciationdetailMapper;
    private final ColumnConverter columnConverter;
    private final InventoriesStorageRowMapper inventoriesstorageMapper;
    private final ItemRowMapper itemMapper;

    private static final Table entityTable = Table.aliased("item_asset_depreciation_detail", EntityManager.ENTITY_ALIAS);
    private static final Table inventoriesStorageTable = Table.aliased("inventories_storage", "inventoriesStorage");
    private static final Table itemTable = Table.aliased("item", "item");

    public ItemAssetDepreciationDetailRepositoryInternalImpl(
            R2dbcEntityTemplate template,
            EntityManager entityManager,
            ItemAssetDepreciationDetailRowMapper itemassetdepreciationdetailMapper,
            R2dbcEntityOperations entityOperations,
            R2dbcConverter converter,
            ColumnConverter columnConverter, InventoriesStorageRowMapper inventoriesstorageMapper, ItemRowMapper itemMapper
    ) {
        super(
                new MappingRelationalEntityInformation(
                        converter.getMappingContext().getRequiredPersistentEntity(ItemAssetDepreciationDetail.class)
                ),
                entityOperations,
                converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.itemassetdepreciationdetailMapper = itemassetdepreciationdetailMapper;
        this.columnConverter = columnConverter;
        this.inventoriesstorageMapper = inventoriesstorageMapper;
        this.itemMapper = itemMapper;
    }

    @Override
    public Flux<ItemAssetDepreciationDetail> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<ItemAssetDepreciationDetail> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = ItemAssetDepreciationDetailSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(InventoriesStorageSqlHelper.getColumns(inventoriesStorageTable, "inventories_storage"));
        columns.addAll(ItemSqlHelper.getColumns(itemTable, "item"));

        SelectBuilder.SelectFromAndJoinCondition selectFrom = Select.builder()
                .select(columns)
                .from(entityTable)

                .leftOuterJoin(inventoriesStorageTable)
                .on(Column.create("inventories_storage_id", entityTable))
                .equals(Column.create("id", inventoriesStorageTable))

                .leftOuterJoin(itemTable)
                .on(Column.create("item_id", inventoriesStorageTable))
                .equals(Column.create("id", itemTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, ItemAssetDepreciationDetail.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<ItemAssetDepreciationDetail> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<ItemAssetDepreciationDetail> findById(UUID id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(StringUtils.wrap(id.toString(), "'")));
        return createQuery(null, whereClause).one();
    }

    private ItemAssetDepreciationDetail process(Row row, RowMetadata metadata) {
        ItemAssetDepreciationDetail entity = itemassetdepreciationdetailMapper.apply(row, "e");
        entity.setInventoriesStorage(inventoriesstorageMapper.apply(row, "inventories_storage"));
        if (itemTable != null) {
            Item item = itemMapper.apply(row, "item");
            entity.getInventoriesStorage().setItem(itemMapper.apply(row, "item"));
        }
        return entity;
    }

    @Override
    public <S extends ItemAssetDepreciationDetail> Mono<S> save(S entity) {
        return super.save(entity);
    }

    @Override
    public Flux<ItemAssetDepreciationDetail> findByCriteria(
            ItemAssetDepreciationDetailCriteria itemAssetDepreciationDetailCriteria,
            Pageable page
    ) {
        page = getDefaultSort(page);
        return createQuery(page, buildConditions(itemAssetDepreciationDetailCriteria)).all();
    }

    @Override
    public Mono<Long> countByCriteria(ItemAssetDepreciationDetailCriteria criteria) {
        return findByCriteria(criteria, null)
                .collectList()
                .map(collectedList -> collectedList != null ? (long) collectedList.size() : (long) 0);
    }

    private Pageable getDefaultSort(Pageable pageable) {
        Sort defaultSort = Sort.by(Sort.Direction.ASC, "created_at");
        if (Objects.isNull(pageable)) {
            pageable = PageRequest.of(0, Integer.MAX_VALUE, defaultSort);
        }
        if (pageable.getSort().equals(Sort.unsorted())) {
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), defaultSort);
        }
        return pageable;
    }

    private Condition buildConditions(ItemAssetDepreciationDetailCriteria criteria) {
        ConditionBuilder builder = new ConditionBuilder(this.columnConverter);
        List<Condition> allConditions = new ArrayList<Condition>();
        if (criteria != null) {
            if (criteria.getId() != null) {
                builder.buildFilterConditionForField(criteria.getId(), entityTable.column("item_asset_depreciation_id"));
            }
            if (criteria.getCode() != null) {
                builder.buildFilterConditionForField(criteria.getCode(), entityTable.column("code"));
            }
            if (criteria.getAttribute() != null) {
                builder.buildFilterConditionForField(criteria.getAttribute(), entityTable.column("attribute"));
            }
            if (criteria.getInventoriesStorageId() != null) {
                builder.buildFilterConditionForField(criteria.getInventoriesStorageId(), entityTable.column("inventories_storage_id"));
            }
            if (criteria.getCostInformation() != null) {
                builder.buildFilterConditionForField(criteria.getCostInformation(), entityTable.column("cost_information"));
            }
            if (criteria.getAmortizedCostInformation() != null) {
                builder.buildFilterConditionForField(
                        criteria.getAmortizedCostInformation(),
                        entityTable.column("amortized_cost_information")
                );
            }
            if (criteria.getAmortizationAmount() != null) {
                builder.buildFilterConditionForField(criteria.getAmortizationAmount(), entityTable.column("amortization_amount"));
            }
            if (criteria.getAmortizationRate() != null) {
                builder.buildFilterConditionForField(criteria.getAmortizationRate(), entityTable.column("amortization_rate"));
            }
            if (criteria.getAccumulatedAmortizationAmount() != null) {
                builder.buildFilterConditionForField(
                        criteria.getAccumulatedAmortizationAmount(),
                        entityTable.column("accumulated_amortization_amount")
                );
            }
            if (criteria.getRecipe() != null) {
                builder.buildFilterConditionForField(criteria.getRecipe(), entityTable.column("recipe"));
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
