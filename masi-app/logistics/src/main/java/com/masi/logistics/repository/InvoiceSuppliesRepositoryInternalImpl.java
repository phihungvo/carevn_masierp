package com.masi.logistics.repository;

import com.masi.logistics.domain.InvoiceSupplies;
import com.masi.logistics.domain.ItemCategory;
import com.masi.logistics.domain.criteria.InvoiceSuppliesCriteria;
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
 * Spring Data R2DBC custom repository implementation for the InvoiceSupplies entity.
 */
@SuppressWarnings("unused")
class InvoiceSuppliesRepositoryInternalImpl
    extends SimpleR2dbcRepository<InvoiceSupplies, UUID>
    implements InvoiceSuppliesRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final InvoiceSuppliesRowMapper invoicesuppliesMapper;
    private final ItemRowMapper itemMapper;
    private final UomRowMapper uomMapper;
    private final ItemCategoryRowMapper itemCategoryMapper;
    private final ColumnConverter columnConverter;

    private static final Table entityTable = Table.aliased("invoice_supplies", EntityManager.ENTITY_ALIAS);
    private static final Table itemTable = Table.aliased("item", "item");
    private static final Table uomTable = Table.aliased("uom", "uom");
    private static final Table itemCategoryTable = Table.aliased("item_category", "itemCategory");

    public InvoiceSuppliesRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        InvoiceSuppliesRowMapper invoicesuppliesMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter, ItemRowMapper itemMapper, UomRowMapper uomMapper, ItemCategoryRowMapper itemCategoryMapper,
        ColumnConverter columnConverter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(InvoiceSupplies.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.invoicesuppliesMapper = invoicesuppliesMapper;
        this.itemMapper = itemMapper;
        this.uomMapper = uomMapper;
        this.itemCategoryMapper = itemCategoryMapper;
        this.columnConverter = columnConverter;
    }

    @Override
    public Flux<InvoiceSupplies> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<InvoiceSupplies> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = InvoiceSuppliesSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(ItemSqlHelper.getColumns(itemTable, "item"));
        columns.addAll(UomSqlHelper.getColumns(uomTable, "uom"));
        columns.addAll(ItemCategorySqlHelper.getColumns(itemCategoryTable, "itemCategory"));
        SelectBuilder.SelectFromAndJoinCondition selectFrom = Select.builder().select(columns).from(entityTable)
            .leftOuterJoin(itemTable)
            .on(Column.create("item_id", entityTable))
            .equals(Column.create("id", itemTable))
            .leftOuterJoin(uomTable)
            .on(Column.create("uom_id", itemTable))
            .equals(Column.create("id", uomTable))
            .leftOuterJoin(itemCategoryTable)
            .on(Column.create("item_category_id", itemTable))
            .equals(Column.create("id", itemCategoryTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, InvoiceSupplies.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<InvoiceSupplies> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<InvoiceSupplies> findById(UUID id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(StringUtils.wrap(id.toString(), "'")));
        return createQuery(null, whereClause).one();
    }

    private InvoiceSupplies process(Row row, RowMetadata metadata) {
        InvoiceSupplies entity = invoicesuppliesMapper.apply(row, "e");
        entity.setItem(itemMapper.apply(row, "item"));
        entity.getItem().setItemCategory(itemCategoryMapper.apply(row, "itemCategory"));
        entity.getItem().setUom(uomMapper.apply(row, "uom"));
        return entity;
    }

    @Override
    public <S extends InvoiceSupplies> Mono<S> save(S entity) {
        return super.save(entity);
    }

    @Override
    public Flux<InvoiceSupplies> findByCriteria(InvoiceSuppliesCriteria invoiceSuppliesCriteria, Pageable page) {
        return createQuery(page, buildConditions(invoiceSuppliesCriteria)).all();
    }

    @Override
    public Mono<Long> countByCriteria(InvoiceSuppliesCriteria criteria) {
        return findByCriteria(criteria, null)
            .collectList()
            .map(collectedList -> collectedList != null ? (long) collectedList.size() : (long) 0);
    }

    private Condition buildConditions(InvoiceSuppliesCriteria criteria) {
        ConditionBuilder builder = new ConditionBuilder(this.columnConverter);
        List<Condition> allConditions = new ArrayList<Condition>();
        if (criteria != null) {
            if (criteria.getId() != null) {
                builder.buildFilterConditionForField(criteria.getId(), entityTable.column("id"));
            }
            if (criteria.getItemId() != null) {
                builder.buildFilterConditionForField(criteria.getItemId(), entityTable.column("item_id"));
            }
            if (criteria.getDetail1() != null) {
                builder.buildFilterConditionForField(criteria.getDetail1(), entityTable.column("detail_1"));
            }
            if (criteria.getDetail2() != null) {
                builder.buildFilterConditionForField(criteria.getDetail2(), entityTable.column("detail_2"));
            }
            if (criteria.getInvoiceId() != null) {
                builder.buildFilterConditionForField(criteria.getInvoiceId(), entityTable.column("invoice_id"));
            }
            if (criteria.getSupplyId() != null) {
                builder.buildFilterConditionForField(criteria.getSupplyId(), entityTable.column("supply_id"));
            }
            if (criteria.getQuantity() != null) {
                builder.buildFilterConditionForField(criteria.getQuantity(), entityTable.column("quantity"));
            }
            if (criteria.getPrice() != null) {
                builder.buildFilterConditionForField(criteria.getPrice(), entityTable.column("price"));
            }
            if (criteria.getTotal() != null) {
                builder.buildFilterConditionForField(criteria.getTotal(), entityTable.column("total"));
            }
            if (criteria.getTax() != null) {
                builder.buildFilterConditionForField(criteria.getTax(), entityTable.column("tax"));
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
