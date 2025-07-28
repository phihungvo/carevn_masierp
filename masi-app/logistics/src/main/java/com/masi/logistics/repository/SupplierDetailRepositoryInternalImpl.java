package com.masi.logistics.repository;

import com.masi.logistics.domain.SupplierDetail;
import com.masi.logistics.domain.criteria.SupplierDetailCriteria;
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
 * Spring Data R2DBC custom repository implementation for the SupplierDetail entity.
 */
@SuppressWarnings("unused")
class SupplierDetailRepositoryInternalImpl extends SimpleR2dbcRepository<SupplierDetail, UUID> implements SupplierDetailRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final SupplierDetailRowMapper supplierdetailMapper;
    private final ItemRowMapper itemMapper;
    private final UomRowMapper uomMapper;
    private final ItemCategoryRowMapper itemCategoryMapper;

    private final ColumnConverter columnConverter;

    private static final Table entityTable = Table.aliased("supplier_detail", EntityManager.ENTITY_ALIAS);
    private static final Table itemTable = Table.aliased("item", "item");
    private static final Table uomTable = Table.aliased("uom", "uom");
    private static final Table itemCategoryTable = Table.aliased("item_category", "itemCategory");

    public SupplierDetailRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        SupplierDetailRowMapper supplierdetailMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter, ItemRowMapper itemMapper, UomRowMapper uomMapper, ItemCategoryRowMapper itemCategoryMapper,
        ColumnConverter columnConverter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(SupplierDetail.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.supplierdetailMapper = supplierdetailMapper;
        this.itemMapper = itemMapper;
        this.uomMapper = uomMapper;
        this.itemCategoryMapper = itemCategoryMapper;
        this.columnConverter = columnConverter;
    }

    @Override
    public Flux<SupplierDetail> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<SupplierDetail> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = SupplierDetailSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(ItemSqlHelper.getColumns(itemTable, "item"));
        columns.addAll(UomSqlHelper.getColumns(uomTable, "uom"));
        columns.addAll(ItemCategorySqlHelper.getColumns(itemCategoryTable, "itemCategory"));
        SelectBuilder.SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(itemTable)
            .on(entityTable.column("item_id"))
            .equals(itemTable.column("id"))
            .leftOuterJoin(uomTable)
            .on(itemTable.column("uom_id"))
            .equals(uomTable.column("id"))
            .leftOuterJoin(itemCategoryTable)
            .on(itemTable.column("item_category_id"))
            .equals(itemCategoryTable.column("id"));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, SupplierDetail.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<SupplierDetail> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<SupplierDetail> findById(UUID id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(StringUtils.wrap(id.toString(), "'")));
        return createQuery(null, whereClause).one();
    }

    private SupplierDetail process(Row row, RowMetadata metadata) {
        SupplierDetail entity = supplierdetailMapper.apply(row, "e");
        entity.setItem(itemMapper.apply(row, "item"));
        entity.getItem().setUom(uomMapper.apply(row, "uom"));
        entity.getItem().setItemCategory(itemCategoryMapper.apply(row, "itemCategory"));

        return entity;
    }

    @Override
    public <S extends SupplierDetail> Mono<S> save(S entity) {
        return super.save(entity);
    }

    @Override
    public Flux<SupplierDetail> findByCriteria(SupplierDetailCriteria supplierDetailCriteria, Pageable page) {
        return createQuery(page, buildConditions(supplierDetailCriteria)).all();
    }

    @Override
    public Mono<Long> countByCriteria(SupplierDetailCriteria criteria) {
        return findByCriteria(criteria, null)
            .collectList()
            .map(collectedList -> collectedList != null ? (long) collectedList.size() : (long) 0);
    }

    @Override
    public Flux<SupplierDetail> findAllBySupplierIdAndCompany(UUID supplierId, String company) {
        Condition whereClause = getCondition(supplierId, company);
        return createQuery(null, whereClause).all();
    }

    private Condition getCondition(UUID supplierId, String company) {
        Condition whereClause = Conditions.isNull(entityTable.column("delete_at")).and(Conditions.isNull(entityTable.column("delete_by")));
        whereClause = whereClause.and(Conditions.isEqual(uomTable.column("company"), Conditions.just(StringUtils.wrap(company, "'"))));
        whereClause = whereClause.and(Conditions.isEqual(itemCategoryTable.column("company"), Conditions.just(StringUtils.wrap(company, "'"))));
        whereClause = whereClause.and(Conditions.isNull(uomTable.column("delete_at")).and(Conditions.isNull(uomTable.column("delete_by"))));
        whereClause = whereClause.and(Conditions.isNull(itemCategoryTable.column("deleted_at")).and(Conditions.isNull(itemCategoryTable.column("deleted_by"))));
        whereClause = whereClause.and(Conditions.isNull(itemTable.column("deleted_at")).and(Conditions.isNull(itemTable.column("deleted_by"))));
        if (supplierId != null) {
            whereClause = Conditions.isEqual(entityTable.column("supplier_id"), Conditions.just(StringUtils.wrap(supplierId.toString(), "'")))
                .and(Conditions.isEqual(entityTable.column("company"), Conditions.just(StringUtils.wrap(company, "'"))));
        }
        return whereClause;
    }

    @Override
    public Mono<Long> countBySupplierIdAndCompany(UUID supplierId, String company) {
        var whereClause = getCondition(supplierId, company);
        return createQuery(null, whereClause).all().count();
    }

    private Condition buildConditions(SupplierDetailCriteria criteria) {
        ConditionBuilder builder = new ConditionBuilder(this.columnConverter);
        List<Condition> allConditions = new ArrayList<Condition>();
        if (criteria != null) {
            if (criteria.getId() != null) {
                builder.buildFilterConditionForField(criteria.getId(), entityTable.column("id"));
            }
            if (criteria.getSupplierId() != null) {
                builder.buildFilterConditionForField(criteria.getSupplierId(), entityTable.column("supplier_id"));
            }
            if (criteria.getItemId() != null) {
                builder.buildFilterConditionForField(criteria.getItemId(), entityTable.column("item_id"));
            }
            if (criteria.getBasePrice() != null) {
                builder.buildFilterConditionForField(criteria.getBasePrice(), entityTable.column("base_price"));
            }
            if (criteria.getNotes() != null) {
                builder.buildFilterConditionForField(criteria.getNotes(), entityTable.column("notes"));
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
