package com.masi.logistics.repository;

import com.masi.logistics.domain.ItemAssetTransfer;
import com.masi.logistics.domain.ItemCategory;
import com.masi.logistics.domain.criteria.ItemAssetTransferCriteria;
import com.masi.logistics.repository.rowmapper.*;
import com.masi.logistics.service.mapper.TransactionTypeMapper;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;

import java.util.*;

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
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;
import tech.jhipster.service.ConditionBuilder;

/**
 * Spring Data R2DBC custom repository implementation for the ItemAssetTransfer entity.
 */
@SuppressWarnings("unused")
class ItemAssetTransferRepositoryInternalImpl
        extends SimpleR2dbcRepository<ItemAssetTransfer, UUID>
        implements ItemAssetTransferRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final ItemAssetTransferRowMapper itemassettransferMapper;
    private final TransactionTypeRowMapper transactionTypeMapper;
    private final ItemCategoryRowMapper itemCategoryMapper;
    private final InventoriesStorageRowMapper inventoriesStorageMapper;
    private final ColumnConverter columnConverter;

    private static final Table entityTable = Table.aliased("item_asset_transfer", EntityManager.ENTITY_ALIAS);
    private static final Table transactionTypeTable = Table.aliased("transaction_type", "transactionType");
    private static final Table itemCategoryTable = Table.aliased("item_category", "itemCategory");
    private static final Table inventoriesStorageTable = Table.aliased("inventories_storage", "inventoriesStorage");

    public ItemAssetTransferRepositoryInternalImpl(
            R2dbcEntityTemplate template,
            EntityManager entityManager,
            ItemAssetTransferRowMapper itemassettransferMapper,
            R2dbcEntityOperations entityOperations,
            R2dbcConverter converter,
            TransactionTypeRowMapper transactionTypeMapper,
            ItemCategoryRowMapper itemCategoryMapper,
            InventoriesStorageRowMapper inventoriesStorageMapper,
            ColumnConverter columnConverter
    ) {
        super(
                new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(ItemAssetTransfer.class)),
                entityOperations,
                converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.itemassettransferMapper = itemassettransferMapper;
        this.transactionTypeMapper = transactionTypeMapper;
        this.itemCategoryMapper = itemCategoryMapper;
        this.inventoriesStorageMapper = inventoriesStorageMapper;
        this.columnConverter = columnConverter;
    }

    @Override
    public Flux<ItemAssetTransfer> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<ItemAssetTransfer> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = ItemAssetTransferSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);

        SelectFromAndJoin selectFrom = Select.builder().select(columns).from(entityTable);
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, ItemAssetTransfer.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    RowsFetchSpec<ItemAssetTransfer> createQueryCustom(Pageable pageable, Condition whereClause, Map<String, Object> parameters) {
        List<Expression> columns = ItemAssetTransferSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(TransactionTypeSqlHelper.getColumns(transactionTypeTable, "transactionType"));
        columns.addAll(ItemCategorySqlHelper.getColumns(itemCategoryTable, "itemCategory"));
        columns.addAll(InventoriesStorageSqlHelper.getColumns(inventoriesStorageTable, "inventoriesStorage"));
        SelectBuilder.SelectFromAndJoinCondition selectFrom = Select.builder()
                .select(columns)

                .from(entityTable)
                .leftOuterJoin(transactionTypeTable)
                .on(Column.create("transaction_type_id", entityTable))
                .equals(Column.create("id", transactionTypeTable))

                .leftOuterJoin(itemCategoryTable)
                .on(Column.create("item_category_id", entityTable))
                .equals(Column.create("id", itemCategoryTable))

                .leftOuterJoin(inventoriesStorageTable)
                .on(Column.create("inventories_storage_id", entityTable))
                .equals(Column.create("id", inventoriesStorageTable))
                ;
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, ItemAssetTransfer.class, pageable, whereClause);
        var prepared = db.sql(select);
        if (parameters != null) {
            prepared = prepared.bindValues(parameters);
        }
        return prepared.map(this::process);
    }

    @Override
    public Flux<ItemAssetTransfer> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<ItemAssetTransfer> findById(UUID id) {
        Condition whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(StringUtils.wrap(id.toString(), "'")));
        whereClause = whereClause.and(Conditions.isEqual(entityTable.column("is_deleted"), Conditions.just("false")));

        return createQuery(null, whereClause).one();
    }

    @Override
    public Mono<ItemAssetTransfer> findByIdAndCompany(UUID id, String company) {
        Condition whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(StringUtils.wrap(id.toString(), "'")));
        whereClause = whereClause.and(Conditions.isEqual(entityTable.column("is_deleted"), Conditions.just("false")));
        whereClause = whereClause.and(Conditions.isEqual(entityTable.column("company"), Conditions.just(StringUtils.wrap(company, "'"))));

        return createQueryCustom(null, whereClause, null).one();
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

    private ItemAssetTransfer process(Row row, RowMetadata metadata) {
        ItemAssetTransfer entity = itemassettransferMapper.apply(row, "e");
        entity.setTransactionType(transactionTypeMapper.apply(row, "transactionType"));
        entity.setItemCategory(itemCategoryMapper.apply(row, "itemCategory"));
        entity.setInventoriesStorage(inventoriesStorageMapper.apply(row, "inventoriesStorage"));
        return entity;
    }

    @Override
    public <S extends ItemAssetTransfer> Mono<S> save(S entity) {
        return super.save(entity);
    }

    @Override
    public Flux<ItemAssetTransfer> findByCriteria(ItemAssetTransferCriteria itemAssetTransferCriteria, Pageable page) {
        page = getDefaultSort(page);
        var tuple = buildConditions(itemAssetTransferCriteria);
        return createQueryCustom(page, tuple.getT1(), tuple.getT2()).all();
    }

    @Override
    public Mono<Long> countByCriteria(ItemAssetTransferCriteria criteria) {
        return findByCriteria(criteria, null)
                .collectList()
                .map(collectedList -> collectedList != null ? (long) collectedList.size() : (long) 0);
    }

    private Tuple2<Condition, Map<String, Object>> buildConditions(ItemAssetTransferCriteria criteria) {
        ConditionBuilder builder = new ConditionBuilder(this.columnConverter);
        List<Condition> allConditions = new ArrayList<Condition>();
        Map<String, Object> parameters = new HashMap<>();
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
            if (criteria.getStatus() != null) {
                builder.buildFilterConditionForField(criteria.getStatus(), entityTable.column("status"));
            }
            if (criteria.getInventoriesStorageId() != null) {
                builder.buildFilterConditionForField(criteria.getInventoriesStorageId(), entityTable.column("inventories_storage_id"));
            }
            if (criteria.getTransactionTypeId() != null) {
                builder.buildFilterConditionForField(criteria.getTransactionTypeId(), entityTable.column("transaction_type_id"));
            }
            if (criteria.getItemCategoryId() != null) {
                builder.buildFilterConditionForField(criteria.getItemCategoryId(), entityTable.column("item_category_id"));
            }
            if (criteria.getTransferDate() != null) {
                builder.buildFilterConditionForField(criteria.getTransferDate(), entityTable.column("transfer_date"));
            }
            if (criteria.getFromUnit() != null) {
                builder.buildFilterConditionForField(criteria.getFromUnit(), entityTable.column("from_unit"));
            }
            if (criteria.getFromDepartmentId() != null) {
                builder.buildFilterConditionForField(criteria.getFromDepartmentId(), entityTable.column("from_department_id"));
            }
            if (criteria.getToDepartmentId() != null) {
                builder.buildFilterConditionForField(criteria.getToDepartmentId(), entityTable.column("to_department_id"));
            }
            if (criteria.getFromPersonId() != null) {
                builder.buildFilterConditionForField(criteria.getFromPersonId(), entityTable.column("from_person_id"));
            }
            if (criteria.getToPersonId() != null) {
                builder.buildFilterConditionForField(criteria.getToPersonId(), entityTable.column("to_person_id"));
            }
            if (criteria.getFromAddress() != null) {
                builder.buildFilterConditionForField(criteria.getFromAddress(), entityTable.column("from_address"));
            }
            if (criteria.getToAddress() != null) {
                builder.buildFilterConditionForField(criteria.getToAddress(), entityTable.column("to_address"));
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

            if (criteria.getSearch() != null && !criteria.getSearch().isEmpty()) {
                String[] columns = {"code", "description"};
                String[] aliases = Arrays.stream(columns).map(column -> "e." + column).toArray(String[]::new);
                StringBuilder search = new StringBuilder();
                search.append('(');
                Arrays.stream(aliases).forEach(alias -> {
                    search.append(String.format("unaccent(%s) ilike unaccent(:search) or ", alias));
                });
                search.append("unaccent(transactionType.name) ilike unaccent(:search) or ");
                int searchLength = search.length();
                search.delete(searchLength - 3, searchLength);
                search.append(')');
                Condition searchCondition = Conditions.just(search.toString());
                allConditions.add(searchCondition);
                parameters.put("search", "%" + criteria.getSearch() + "%");
            }
        }
        var builderConditions = builder.buildConditions();
        if (builderConditions != null) {
            allConditions.add(builderConditions);
        }
        var defaultConditions = Conditions.just("1 = 1");
        var rs = allConditions.stream().reduce(Condition::and).orElse(defaultConditions);

        return Tuples.of(rs, parameters);
    }
}
