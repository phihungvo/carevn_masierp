package com.masi.logistics.repository;

import com.masi.logistics.domain.TransactionIn;
import com.masi.logistics.domain.criteria.TransactionInCriteria;
import com.masi.logistics.repository.rowmapper.ColumnConverter;
import com.masi.logistics.repository.rowmapper.TransactionInRowMapper;
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
import org.springframework.data.relational.core.sql.Comparison;
import org.springframework.data.relational.core.sql.Condition;
import org.springframework.data.relational.core.sql.Conditions;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Select;
import org.springframework.data.relational.core.sql.SelectBuilder.SelectFromAndJoin;
import org.springframework.data.relational.core.sql.Table;
import org.springframework.data.relational.repository.support.MappingRelationalEntityInformation;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.r2dbc.core.RowsFetchSpec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.service.ConditionBuilder;

/**
 * Spring Data R2DBC custom repository implementation for the TransactionIn entity.
 */
@SuppressWarnings("unused")
class TransactionInRepositoryInternalImpl extends SimpleR2dbcRepository<TransactionIn, UUID> implements TransactionInRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final TransactionInRowMapper transactioninMapper;
    private final ColumnConverter columnConverter;

    private static final Table entityTable = Table.aliased("transaction_in", EntityManager.ENTITY_ALIAS);

    public TransactionInRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        TransactionInRowMapper transactioninMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter,
        ColumnConverter columnConverter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(TransactionIn.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.transactioninMapper = transactioninMapper;
        this.columnConverter = columnConverter;
    }

    @Override
    public Flux<TransactionIn> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<TransactionIn> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = TransactionInSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        SelectFromAndJoin selectFrom = Select.builder().select(columns).from(entityTable);
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, TransactionIn.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<TransactionIn> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<TransactionIn> findById(UUID id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(StringUtils.wrap(id.toString(), "'")));
        return createQuery(null, whereClause).one();
    }

    private TransactionIn process(Row row, RowMetadata metadata) {
        TransactionIn entity = transactioninMapper.apply(row, "e");
        return entity;
    }

    @Override
    public <S extends TransactionIn> Mono<S> save(S entity) {
        return super.save(entity);
    }

    @Override
    public Flux<TransactionIn> findByCriteria(TransactionInCriteria transactionInCriteria, Pageable page) {
        return createQuery(page, buildConditions(transactionInCriteria)).all();
    }

    @Override
    public Mono<Long> countByCriteria(TransactionInCriteria criteria) {
        return findByCriteria(criteria, null)
            .collectList()
            .map(collectedList -> collectedList != null ? (long) collectedList.size() : (long) 0);
    }

    private Condition buildConditions(TransactionInCriteria criteria) {
        ConditionBuilder builder = new ConditionBuilder(this.columnConverter);
        List<Condition> allConditions = new ArrayList<Condition>();
        if (criteria != null) {
            if (criteria.getId() != null) {
                builder.buildFilterConditionForField(criteria.getId(), entityTable.column("id"));
            }
            if (criteria.getCode() != null) {
                builder.buildFilterConditionForField(criteria.getCode(), entityTable.column("code"));
            }
            if (criteria.getUnitPrice() != null) {
                builder.buildFilterConditionForField(criteria.getUnitPrice(), entityTable.column("unit_price"));
            }
            if (criteria.getQuantity() != null) {
                builder.buildFilterConditionForField(criteria.getQuantity(), entityTable.column("quantity"));
            }
            if (criteria.getNotes() != null) {
                builder.buildFilterConditionForField(criteria.getNotes(), entityTable.column("notes"));
            }
            if (criteria.getTransactionId() != null) {
                builder.buildFilterConditionForField(criteria.getTransactionId(), entityTable.column("transaction_id"));
            }
            if (criteria.getTransactionCode() != null) {
                builder.buildFilterConditionForField(criteria.getTransactionCode(), entityTable.column("transaction_code"));
            }
            if (criteria.getExpiredDate() != null) {
                builder.buildFilterConditionForField(criteria.getExpiredDate(), entityTable.column("expired_date"));
            }
            if (criteria.getManufactureDate() != null) {
                builder.buildFilterConditionForField(criteria.getManufactureDate(), entityTable.column("manufacture_date"));
            }
            if (criteria.getItemId() != null) {
                builder.buildFilterConditionForField(criteria.getItemId(), entityTable.column("item_id"));
            }
            if (criteria.getItemCode() != null) {
                builder.buildFilterConditionForField(criteria.getItemCode(), entityTable.column("item_code"));
            }
            if (criteria.getWarehouseId() != null) {
                builder.buildFilterConditionForField(criteria.getWarehouseId(), entityTable.column("warehouse_id"));
            }
            if (criteria.getWarehouseCode() != null) {
                builder.buildFilterConditionForField(criteria.getWarehouseCode(), entityTable.column("warehouse_code"));
            }
            if (criteria.getSupplierId() != null) {
                builder.buildFilterConditionForField(criteria.getSupplierId(), entityTable.column("supplier_id"));
            }
            if (criteria.getSupplierCode() != null) {
                builder.buildFilterConditionForField(criteria.getSupplierCode(), entityTable.column("supplier_code"));
            }
            if (criteria.getCustomerId() != null) {
                builder.buildFilterConditionForField(criteria.getCustomerId(), entityTable.column("customer_id"));
            }
            if (criteria.getCustomerCode() != null) {
                builder.buildFilterConditionForField(criteria.getCustomerCode(), entityTable.column("customer_code"));
            }
            if (criteria.getOrderId() != null) {
                builder.buildFilterConditionForField(criteria.getOrderId(), entityTable.column("order_id"));
            }
            if (criteria.getOrderCode() != null) {
                builder.buildFilterConditionForField(criteria.getOrderCode(), entityTable.column("order_code"));
            }
            if (criteria.getManufactureId() != null) {
                builder.buildFilterConditionForField(criteria.getManufactureId(), entityTable.column("manufacture_id"));
            }
            if (criteria.getManufactureCode() != null) {
                builder.buildFilterConditionForField(criteria.getManufactureCode(), entityTable.column("manufacture_code"));
            }
            if (criteria.getPackingId() != null) {
                builder.buildFilterConditionForField(criteria.getPackingId(), entityTable.column("packing_id"));
            }
            if (criteria.getPackingCode() != null) {
                builder.buildFilterConditionForField(criteria.getPackingCode(), entityTable.column("packing_code"));
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
            if (criteria.getDepartment() != null) {
                builder.buildFilterConditionForField(criteria.getDepartment(), entityTable.column("department"));
            }
        }
        return builder.buildConditions();
    }
}
