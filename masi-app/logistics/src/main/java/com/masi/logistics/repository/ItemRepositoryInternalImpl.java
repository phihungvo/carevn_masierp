package com.masi.logistics.repository;

import com.masi.logistics.domain.Item;
import com.masi.logistics.domain.criteria.ItemCriteria;
import com.masi.logistics.repository.rowmapper.*;
import com.masi.logistics.service.mapper.ItemTypeMapper;
import com.masi.logistics.service.mapper.SuppliersMapper;
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
import org.springframework.data.relational.core.sql.SelectBuilder.SelectFromAndJoinCondition;
import org.springframework.data.relational.repository.support.MappingRelationalEntityInformation;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.r2dbc.core.RowsFetchSpec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;
import tech.jhipster.service.ConditionBuilder;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Spring Data R2DBC custom repository implementation for the Item entity.
 */
@SuppressWarnings("unused")
class ItemRepositoryInternalImpl extends SimpleR2dbcRepository<Item, UUID> implements ItemRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final ItemCategoryRowMapper itemcategoryMapper;
    private final ItemRowMapper itemMapper;
    private final UomRowMapper uomMapper;
    private final SuppliersRowMapper supplierMapper;
    private final ColumnConverter columnConverter;
    private final ItemTypeRowMapper itemTypeMapper;


    private static final Table entityTable = Table.aliased("item", EntityManager.ENTITY_ALIAS);
    private static final Table itemCategoryTable = Table.aliased("item_category", "itemCategory");
    private static final Table itemTypeTable = Table.aliased("item_type", "itemTypes");

    private static final Table revenueGroupTable = Table.aliased("item_category", "itemCategoryRevenueGroup");
    private static final Table uomTable = Table.aliased("uom", "uom");
    private static final Table supplierTable = Table.aliased("suppliers", "suppliers");

    public ItemRepositoryInternalImpl(
            R2dbcEntityTemplate template,
            EntityManager entityManager,
            ItemCategoryRowMapper itemcategoryMapper,
            ItemRowMapper itemMapper,
            R2dbcEntityOperations entityOperations,
            R2dbcConverter converter, UomRowMapper uomMapper, SuppliersRowMapper supplierMapper,
            ColumnConverter columnConverter, ItemTypeRowMapper itemTypeMapper
    ) {
        super(
                new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(Item.class)),
                entityOperations,
                converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.itemcategoryMapper = itemcategoryMapper;
        this.itemMapper = itemMapper;
        this.uomMapper = uomMapper;
        this.supplierMapper = supplierMapper;
        this.columnConverter = columnConverter;
        this.itemTypeMapper = itemTypeMapper;
    }

    @Override
    public Flux<Item> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<Item> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = ItemSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(ItemCategorySqlHelper.getColumns(itemCategoryTable, "itemCategory"));
        columns.addAll(ItemCategorySqlHelper.getColumns(revenueGroupTable, "revenueGroupTable"));
        columns.addAll(UomSqlHelper.getColumns(uomTable, "uom"));
        columns.addAll(SuppliersSqlHelper.getColumns(supplierTable, "suppliers"));
        columns.addAll(ItemTypeSqlHelper.getColumns(itemTypeTable, "itemTypes"));

        SelectFromAndJoinCondition selectFrom = Select.builder()
                .select(columns)
                .from(entityTable)
                .leftOuterJoin(itemCategoryTable)
                .on(Column.create("item_category_id", entityTable))
                .equals(Column.create("id", itemCategoryTable))
                .leftOuterJoin(itemTypeTable)
                .on(Column.create("item_type_id", entityTable))
                .equals(Column.create("id", itemTypeTable))
                .leftOuterJoin(revenueGroupTable)
                .on(Column.create("revenue_group", entityTable))
                .equals(Column.create("id", revenueGroupTable))
                .leftOuterJoin(uomTable)
                .on(Column.create("uom_id", entityTable))
                .equals(Column.create("id", uomTable))
                .leftOuterJoin(supplierTable)
                .on(Column.create("supplier_id", entityTable))
                .equals(Column.create("id", supplierTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, Item.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    RowsFetchSpec<Item> createQueryCustom(Pageable pageable, Condition whereClause, Map<String, Object> parameters) {
        List<Expression> columns = ItemSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(ItemCategorySqlHelper.getColumns(itemCategoryTable, "itemCategory"));
        columns.addAll(ItemTypeSqlHelper.getColumns(itemTypeTable, "itemTypes"));
        columns.addAll(ItemCategorySqlHelper.getColumns(revenueGroupTable, "revenueGroupTable"));
        columns.addAll(UomSqlHelper.getColumns(uomTable, "uom"));
        columns.addAll(SuppliersSqlHelper.getColumns(supplierTable, "suppliers"));
        SelectFromAndJoinCondition selectFrom = Select.builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(itemCategoryTable)
            .on(Column.create("item_category_id", entityTable))
            .equals(Column.create("id", itemCategoryTable))
            .leftOuterJoin(itemTypeTable)
            .on(Column.create("item_type_id", entityTable))
            .equals(Column.create("id", itemTypeTable))
            .leftOuterJoin(revenueGroupTable)
            .on(Column.create("revenue_group", entityTable))
            .equals(Column.create("id", revenueGroupTable))
            .leftOuterJoin(uomTable)
            .on(Column.create("uom_id", entityTable))
            .equals(Column.create("id", uomTable))
            .leftOuterJoin(supplierTable)
            .on(Column.create("supplier_id", entityTable))
            .equals(Column.create("id", supplierTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, Item.class, pageable, whereClause);
        var prepared = db.sql(select);
        if (parameters != null) {
            prepared = prepared.bindValues(parameters);
        }
        return prepared.map(this::process);
    }

    @Override
    public Flux<Item> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<Item> findById(UUID id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(StringUtils.wrap(id.toString(), "'")));
        return createQuery(null, whereClause).one();
    }

    @Override
    public Mono<Item> findById(UUID id, String company) {
        Condition whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(StringUtils.wrap(id.toString(), "'")));
        whereClause = whereClause.and(entityTable.column("company").isEqualTo(Conditions.just(StringUtils.wrap(company, "'"))));
        whereClause = whereClause.and(entityTable.column("deleted_at").isNull());
        whereClause = whereClause.and(entityTable.column("deleted_by").isNull());
        return createQueryCustom(null, whereClause, null).one();
    }


    private Item process(Row row, RowMetadata metadata) {
        Item entity = itemMapper.apply(row, "e");
        entity.setItemCategory(itemcategoryMapper.apply(row, "itemCategory"));
        entity.setUom(uomMapper.apply(row, "uom"));
        entity.setSupplier(supplierMapper.apply(row, "suppliers"));
        entity.setRevenueGroup(itemcategoryMapper.apply(row, "revenueGroupTable"));
        entity.setItemTypes(itemTypeMapper.apply(row, "itemTypes"));

        return entity;
    }

    @Override
    public <S extends Item> Mono<S> save(S entity) {
        return super.save(entity);
    }

    // default sort by created_at desc
    private Pageable getDefaultSort(Pageable pageable) {
        Sort defaultSort = Sort.by(Sort.Direction.DESC, "created_date").and(Sort.by(Sort.Direction.DESC, "code"));
        if (Objects.isNull(pageable)) {
            pageable = PageRequest.of(0, Integer.MAX_VALUE, defaultSort);
        }
        if (pageable.getSort().equals(Sort.unsorted())) {
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), defaultSort);
        }
        return pageable;
    }

    @Override
    public Flux<Item> findByCriteria(ItemCriteria itemCriteria, Pageable page) {
        page = getDefaultSort(page);
        var tuple = buildConditions(itemCriteria);
        return createQueryCustom(page, tuple.getT1(), tuple.getT2()).all();
    }

    @Override
    public Mono<Long> countByCriteria(ItemCriteria criteria) {
        return findByCriteria(criteria, null)
                .collectList()
                .map(collectedList -> collectedList != null ? (long) collectedList.size() : (long) 0);
    }

    private Tuple2<Condition, HashMap<String, Object>> buildConditions(ItemCriteria criteria) {
        ConditionBuilder builder = new ConditionBuilder(this.columnConverter);
        List<Condition> allConditions = new ArrayList<Condition>();
        Condition whereClause = Conditions.isNull(entityTable.column("deleted_at"));
        HashMap<String, Object> params = new HashMap<>();
        builder.buildFilterConditionForField(new BooleanFilter().setEquals(false), entityTable.column("is_deleted"));
        Condition subgroup1 = null;
        if (criteria != null) {
            if (criteria.getId() != null) {
                builder.buildFilterConditionForField(criteria.getId(), entityTable.column("id"));
            }
            // start check search contain name or code
            String codeContains = (criteria.getCode() != null) ? criteria.getCode().getContains() : null;
            String nameContains = (criteria.getName() != null) ? criteria.getName().getContains() : null;

            Condition group1 = (codeContains != null && !codeContains.isEmpty())
                ? Conditions.like(entityTable.column("code"), SQL.literalOf("%" + codeContains + "%"))
                : null;

            Condition group2 = (nameContains != null && !nameContains.isEmpty())
                ? Conditions.like(entityTable.column("name"), SQL.literalOf("%" + nameContains + "%"))
                : null;

            if (group1 != null || group2 != null) {
                subgroup1 = (group1 != null && group2 != null)
                    ? Conditions.nest(group1.or(group2))
                    : (group1 != null ? group1 : group2);

                allConditions.add(subgroup1);
            }

            // end check search contain name or code

//            if (criteria.getName() != null && !criteria.getName().getContains().toString().equals("")) {
//                var a = criteria.getName();
//                builder.buildFilterConditionForField(criteria.getName(), entityTable.column("name"));
//            }

            if (criteria.getIsFindPerProtein() != null && criteria.getIsFindPerProtein()) {
                Condition group3 = entityTable.column("percent_protein").isNotNull();
                Condition group4 = entityTable.column("percent_protein").isGreater(SQL.literalOf(0));
                Condition subgroup2 = Conditions.nest(group3.and(group4));
                allConditions.add(subgroup2);            }

            if (criteria.getStatus() != null) {
                Condition status = Conditions.isEqual(entityTable.column("is_active"), Conditions.just(criteria.getStatus().toString()));
                allConditions.add(status);
            }

            if (criteria.getUomId() != null) {
                builder.buildFilterConditionForField(criteria.getUomId(), entityTable.column("uom_id"));
            }
            if (criteria.getAttribute() != null) {
                builder.buildFilterConditionForField(criteria.getAttribute(), entityTable.column("attribute"));
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
            if (criteria.getItemCategoryId() != null) {
                builder.buildFilterConditionForField(criteria.getItemCategoryId(), itemCategoryTable.column("id"));
            }
            if (criteria.getIsActive() != null) {
                builder.buildFilterConditionForField(criteria.getIsActive(), entityTable.column("is_active"));
            }

            if (criteria.getSearch() != null) {
                var nameColumn = EntityManager.ENTITY_ALIAS + "." + "name";
                var codeColumn = EntityManager.ENTITY_ALIAS + "." + "code";
                var itemCategoryNameColumn = "itemCategory" + "." + "name";
                String inCondition = "(unaccent(" + nameColumn + ") iLIKE unaccent(:search) OR unaccent(" + codeColumn + ") iLIKE unaccent(:search) OR unaccent(" + itemCategoryNameColumn + ") iLIKE unaccent(:search)) ";
                whereClause = whereClause.and(Conditions.just(inCondition));
                params.put("search", "%" + criteria.getSearch().trim() + "%");
                allConditions.add(whereClause);
            }

            if (criteria.getItemType() != null) {
                String itemTypeColumn = EntityManager.ENTITY_ALIAS + ".item_type"; // Xác định cột item_type
                String inCondition = "(unaccent(" + itemTypeColumn + ") ILIKE unaccent(:itemType))";
                whereClause = whereClause.and(Conditions.just(inCondition));
                params.put("itemType", "%" + criteria.getItemType().getContains() + "%");
                allConditions.add(whereClause);
            }

        }
        Condition defaultCondition = Conditions.just("1=1"); // Default condition that is always true
        var conditionsBuilder = builder.buildConditions();
        allConditions.add(conditionsBuilder);
        var rs = allConditions.stream().reduce(Condition::and).orElse(defaultCondition);
        if (subgroup1 != null)
            rs.and(subgroup1);
        return Tuples.of(rs, params);
    }
}
