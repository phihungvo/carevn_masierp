package com.masi.logistics.repository;

import com.masi.logistics.domain.SuppliesItem;
import com.masi.logistics.domain.criteria.SuppliesItemCriteria;
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
import org.springframework.data.relational.repository.support.MappingRelationalEntityInformation;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.r2dbc.core.RowsFetchSpec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.service.ConditionBuilder;

/**
 * Spring Data R2DBC custom repository implementation for the SuppliesItem entity.
 */
@SuppressWarnings("unused")
class SuppliesItemRepositoryInternalImpl extends SimpleR2dbcRepository<SuppliesItem, UUID> implements SuppliesItemRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final SuppliesItemRowMapper suppliesitemMapper;
    private final SuppliersRowMapper supplierRowMapper;
    private final ItemRowMapper itemRowMapper;
    private final UomRowMapper uomRowMapper;
    private final ItemCategoryRowMapper itemCategoryRowMapper;
    private final ColumnConverter columnConverter;

    private static final Table entityTable = Table.aliased("supplies_item", EntityManager.ENTITY_ALIAS);
    private static final Table suppliesRequestTable = Table.aliased("supplies_request", "suppliesRequest");
    private static final Table itemTable = Table.aliased("item", "item");
    private static final Table uomTable = Table.aliased("uom", "uom");
    private static final Table itemCategoryTable = Table.aliased("item_category", "itemCategory");

    private static final Table suppliersTable = Table.aliased("suppliers", "suppliers");
    private static final Table suppliersItemTable = Table.aliased("suppliers", "suppliersItem");

    public SuppliesItemRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        SuppliesItemRowMapper suppliesitemMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter, SuppliersRowMapper supplierRowMapper, ItemRowMapper itemRowMapper, UomRowMapper uomRowMapper, ItemCategoryRowMapper itemCategoryRowMapper,
        ColumnConverter columnConverter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(SuppliesItem.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.suppliesitemMapper = suppliesitemMapper;
        this.supplierRowMapper = supplierRowMapper;
        this.itemRowMapper = itemRowMapper;
        this.uomRowMapper = uomRowMapper;
        this.itemCategoryRowMapper = itemCategoryRowMapper;
        this.columnConverter = columnConverter;
    }

    @Override
    public Flux<SuppliesItem> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<SuppliesItem> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = SuppliesItemSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(ItemSqlHelper.getColumns(itemTable, "item"));
        columns.addAll(SuppliersSqlHelper.getColumns(suppliersTable, "suppliers"));
        columns.addAll(UomSqlHelper.getColumns(uomTable, "uom"));
        columns.addAll(ItemCategorySqlHelper.getColumns(itemCategoryTable, "itemCategory"));
        columns.addAll(SuppliersSqlHelper.getColumns(suppliersItemTable, "suppliersItem"));
        SelectBuilder.SelectFromAndJoinCondition selectFrom = Select.builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(itemTable)
            .on(Column.create("id_item", entityTable))
            .equals(Column.create("id", itemTable))
            .leftOuterJoin(suppliersTable)
            .on(Column.create("supplies_id", entityTable))
            .equals(Column.create("id", suppliersTable))
            .leftOuterJoin(uomTable)
            .on(Column.create("uom_id", itemTable))
            .equals(Column.create("id", uomTable))
            .leftOuterJoin(itemCategoryTable)
            .on(Column.create("item_category_id", itemTable))
            .equals(Column.create("id", itemCategoryTable))
            .leftOuterJoin(suppliersItemTable)
            .on(Column.create("supplier_id", itemTable))
            .equals(Column.create("id", suppliersItemTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, SuppliesItem.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<SuppliesItem> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<SuppliesItem> findById(UUID id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(StringUtils.wrap(id.toString(), "'")));
        return createQuery(null, whereClause).one();
    }

    private SuppliesItem process(Row row, RowMetadata metadata) {
        SuppliesItem entity = suppliesitemMapper.apply(row, "e");
        entity.setSuppliers(supplierRowMapper.apply(row, "suppliers"));
        entity.setItem(itemRowMapper.apply(row, "item"));
        entity.getItem().setUom(uomRowMapper.apply(row, "uom"));
        entity.getItem().setItemCategory(itemCategoryRowMapper.apply(row, "itemCategory"));
        entity.getItem().setSupplier(supplierRowMapper.apply(row, "suppliersItem"));
        return entity;
    }

    @Override
    public <S extends SuppliesItem> Mono<S> save(S entity) {
        return super.save(entity);
    }

    @Override
    public Flux<SuppliesItem> findByCriteria(SuppliesItemCriteria suppliesItemCriteria, Pageable page) {
        return createQuery(page, buildConditions(suppliesItemCriteria)).all();
    }

    @Override
    public Flux<SuppliesItem> findAllByIdSuppliesRequestAndCompanyAndIsDeletedIsFalse(UUID idSuppliesRequest, String company) {
        var whereClause = buildCondition(idSuppliesRequest, company);
        return createQuery(null, whereClause).all();
    }

    public Condition buildCondition(UUID idSuppliesRequest, String company) {
        Condition whereClause = Conditions.isEqual(entityTable.column("id_supplies_request"), Conditions.just(StringUtils.wrap(idSuppliesRequest.toString(), "'")));
        whereClause = whereClause.and(Conditions.isEqual(entityTable.column("company"), Conditions.just(StringUtils.wrap(company, "'"))));
        whereClause = whereClause.and(Conditions.isEqual(entityTable.column("is_deleted"), Conditions.just("false")));
        return whereClause;
    }

    @Override
    public Mono<Long> countByCriteria(SuppliesItemCriteria criteria) {
        return findByCriteria(criteria, null)
            .collectList()
            .map(collectedList -> collectedList != null ? (long) collectedList.size() : (long) 0);
    }

    private Condition buildConditions(SuppliesItemCriteria criteria) {
        ConditionBuilder builder = new ConditionBuilder(this.columnConverter);
        List<Condition> allConditions = new ArrayList<Condition>();
        if (criteria != null) {
            if (criteria.getId() != null) {
                builder.buildFilterConditionForField(criteria.getId(), entityTable.column("id"));
            }
            if (criteria.getIdSuppliesRequest() != null) {
                builder.buildFilterConditionForField(criteria.getIdSuppliesRequest(), entityTable.column("id_supplies_request"));
            }
            if (criteria.getIdItem() != null) {
                builder.buildFilterConditionForField(criteria.getIdItem(), entityTable.column("id_item"));
            }
            if (criteria.getIdUom() != null) {
                builder.buildFilterConditionForField(criteria.getIdUom(), entityTable.column("id_uom"));
            }
            if (criteria.getSuppliesId() != null) {
                builder.buildFilterConditionForField(criteria.getSuppliesId(), entityTable.column("supplies_id"));
            }
            if (criteria.getQuantity() != null) {
                builder.buildFilterConditionForField(criteria.getQuantity(), entityTable.column("quantity"));
            }
            if (criteria.getPrice() != null) {
                builder.buildFilterConditionForField(criteria.getPrice(), entityTable.column("price"));
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
