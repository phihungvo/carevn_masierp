package com.masi.logistics.repository;

import com.masi.logistics.domain.RelatedCosts;
import com.masi.logistics.domain.criteria.RelatedCostsCriteria;
import com.masi.logistics.repository.rowmapper.ColumnConverter;
import com.masi.logistics.repository.rowmapper.IncomingInvoiceRowMapper;
import com.masi.logistics.repository.rowmapper.RelatedCostsRowMapper;
import com.masi.logistics.repository.rowmapper.SuppliersRowMapper;
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
 * Spring Data R2DBC custom repository implementation for the RelatedCosts entity.
 */
@SuppressWarnings("unused")
class RelatedCostsRepositoryInternalImpl extends SimpleR2dbcRepository<RelatedCosts, UUID> implements RelatedCostsRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final RelatedCostsRowMapper relatedcostsMapper;
    private final IncomingInvoiceRowMapper invoiceTableMapper;
    private final SuppliersRowMapper suppliersMapper;
    private final ColumnConverter columnConverter;

    private static final Table entityTable = Table.aliased("related_costs", EntityManager.ENTITY_ALIAS);
    private static final Table invoiceTable = Table.aliased("incoming_invoice", "invoice");
    private static final Table suppliersTable = Table.aliased("suppliers", "suppliers");
    public RelatedCostsRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        RelatedCostsRowMapper relatedcostsMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter, IncomingInvoiceRowMapper invoiceTableMapper, SuppliersRowMapper suppliersMapper,
        ColumnConverter columnConverter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(RelatedCosts.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.relatedcostsMapper = relatedcostsMapper;
        this.invoiceTableMapper = invoiceTableMapper;
        this.suppliersMapper = suppliersMapper;
        this.columnConverter = columnConverter;
    }

    @Override
    public Flux<RelatedCosts> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<RelatedCosts> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = RelatedCostsSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(IncomingInvoiceSqlHelper.getColumns(invoiceTable, "invoice"));
        columns.addAll(SuppliersSqlHelper.getColumns(suppliersTable, "suppliers"));
        SelectBuilder.SelectFromAndJoinCondition selectFrom = Select.builder().select(columns).from(entityTable)
            .leftOuterJoin(invoiceTable)
            .on(Column.create("invoice_id", entityTable))
            .equals(Column.create("id", invoiceTable))
            .leftOuterJoin(suppliersTable)
            .on(Column.create("supplier_id", invoiceTable))
            .equals(Column.create("id", suppliersTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, RelatedCosts.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<RelatedCosts> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<RelatedCosts> findById(UUID id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(StringUtils.wrap(id.toString(), "'")));
        return createQuery(null, whereClause).one();
    }

    private RelatedCosts process(Row row, RowMetadata metadata) {
        RelatedCosts entity = relatedcostsMapper.apply(row, "e");
        entity.setInvoice(invoiceTableMapper.apply(row, "invoice"));
        entity.getInvoice().setSuppliers(suppliersMapper.apply(row, "suppliers"));
        return entity;
    }

    @Override
    public <S extends RelatedCosts> Mono<S> save(S entity) {
        return super.save(entity);
    }

    @Override
    public Flux<RelatedCosts> findByCriteria(RelatedCostsCriteria relatedCostsCriteria, Pageable page) {
        return createQuery(page, buildConditions(relatedCostsCriteria)).all();
    }

    @Override
    public Mono<Long> countByCriteria(RelatedCostsCriteria criteria) {
        return findByCriteria(criteria, null)
            .collectList()
            .map(collectedList -> collectedList != null ? (long) collectedList.size() : (long) 0);
    }

    private Condition buildConditions(RelatedCostsCriteria criteria) {
        ConditionBuilder builder = new ConditionBuilder(this.columnConverter);
        List<Condition> allConditions = new ArrayList<Condition>();
        if (criteria != null) {
            if (criteria.getId() != null) {
                builder.buildFilterConditionForField(criteria.getId(), entityTable.column("id"));
            }
            if (criteria.getInvoiceId() != null) {
                builder.buildFilterConditionForField(criteria.getInvoiceId(), entityTable.column("invoice_id"));
            }
            if (criteria.getPaymentMethodId() != null) {
                builder.buildFilterConditionForField(criteria.getPaymentMethodId(), entityTable.column("payment_method_id"));
            }
            if (criteria.getPaymentMethodCode() != null) {
                builder.buildFilterConditionForField(criteria.getPaymentMethodCode(), entityTable.column("payment_method_code"));
            }
            if (criteria.getPaymentMethodName() != null) {
                builder.buildFilterConditionForField(criteria.getPaymentMethodName(), entityTable.column("payment_method_name"));
            }
            if (criteria.getVatId() != null) {
                builder.buildFilterConditionForField(criteria.getVatId(), entityTable.column("vat_id"));
            }
            if (criteria.getVat() != null) {
                builder.buildFilterConditionForField(criteria.getVat(), entityTable.column("vat"));
            }
            if (criteria.getVatAmount() != null) {
                builder.buildFilterConditionForField(criteria.getVatAmount(), entityTable.column("vat_amount"));
            }
            if (criteria.getTotalAmount() != null) {
                builder.buildFilterConditionForField(criteria.getTotalAmount(), entityTable.column("total_amount"));
            }
            if (criteria.getDebtDays() != null) {
                builder.buildFilterConditionForField(criteria.getDebtDays(), entityTable.column("debt_days"));
            }
            if (criteria.getNote() != null) {
                builder.buildFilterConditionForField(criteria.getNote(), entityTable.column("note"));
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
