package com.masi.logistics.repository;

import com.masi.logistics.domain.IncomingInvoice;
import com.masi.logistics.domain.PaymentDetail;
import com.masi.logistics.domain.criteria.PaymentDetailCriteria;
import com.masi.logistics.repository.rowmapper.ColumnConverter;
import com.masi.logistics.repository.rowmapper.IncomingInvoiceRowMapper;
import com.masi.logistics.repository.rowmapper.PaymentDetailRowMapper;
import com.masi.logistics.repository.rowmapper.PaymentRequestRowMapper;
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
import org.springframework.data.relational.core.sql.SelectBuilder.SelectFromAndJoinCondition;
import org.springframework.data.relational.repository.support.MappingRelationalEntityInformation;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.r2dbc.core.RowsFetchSpec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.service.ConditionBuilder;

/**
 * Spring Data R2DBC custom repository implementation for the PaymentDetail entity.
 */
@SuppressWarnings("unused")
class PaymentDetailRepositoryInternalImpl extends SimpleR2dbcRepository<PaymentDetail, UUID> implements PaymentDetailRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final PaymentDetailRowMapper paymentdetailMapper;
    private final IncomingInvoiceRowMapper incomingInvoiceRowMapper;
    private final ColumnConverter columnConverter;

    private static final Table entityTable = Table.aliased("payment_detail", EntityManager.ENTITY_ALIAS);
    private static final Table paymentRequestTable = Table.aliased("payment_request", "paymentRequest");
    private static final Table incomingInvoiceTable = Table.aliased("incoming_invoice", "incomingInvoice");

    public PaymentDetailRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        PaymentDetailRowMapper paymentdetailMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter, IncomingInvoiceRowMapper incomingInvoiceRowMapper,
        ColumnConverter columnConverter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(PaymentDetail.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.paymentdetailMapper = paymentdetailMapper;
        this.incomingInvoiceRowMapper = incomingInvoiceRowMapper;
        this.columnConverter = columnConverter;
    }

    @Override
    public Flux<PaymentDetail> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<PaymentDetail> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = PaymentDetailSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(IncomingInvoiceSqlHelper.getColumns(incomingInvoiceTable, "incomingInvoice"));
        SelectBuilder.SelectFromAndJoinCondition selectFrom = Select.builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(incomingInvoiceTable)
            .on(entityTable.column("invoice_id"))
            .equals(incomingInvoiceTable.column("id"));
        String select = entityManager.createSelect(selectFrom, PaymentDetail.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<PaymentDetail> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<PaymentDetail> findById(UUID id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(StringUtils.wrap(id.toString(), "'")));
        return createQuery(null, whereClause).one();
    }

    private PaymentDetail process(Row row, RowMetadata metadata) {
        PaymentDetail entity = paymentdetailMapper.apply(row, "e");
        entity.setIncomingInvoice(incomingInvoiceRowMapper.apply(row, "incomingInvoice"));
        return entity;
    }

    @Override
    public <S extends PaymentDetail> Mono<S> save(S entity) {
        return super.save(entity);
    }

    @Override
    public Flux<PaymentDetail> findByCriteria(PaymentDetailCriteria paymentDetailCriteria, Pageable page) {
        return createQuery(page, buildConditions(paymentDetailCriteria)).all();
    }

    @Override
    public Flux<PaymentDetail> findByPaymentRequestId(UUID id, Pageable pageable) {
        return createQuery(pageable, Conditions.isEqual(entityTable.column("payment_request_id"), Conditions.just(StringUtils.wrap(id.toString(), "'")))).all();
    }

    @Override
    public Mono<Long> countByCriteria(PaymentDetailCriteria criteria) {
        return findByCriteria(criteria, null)
            .collectList()
            .map(collectedList -> collectedList != null ? (long) collectedList.size() : (long) 0);
    }

    @Override
    public Mono<Long> countByRequestId(String requestId) {
        return db.sql("SELECT COUNT(*) FROM payment_detail WHERE payment_request_id = :id")
            .bind("id", requestId)
            .map(row -> row.get(0, Long.class))
            .one();
    }

    private Condition buildConditions(PaymentDetailCriteria criteria) {
        ConditionBuilder builder = new ConditionBuilder(this.columnConverter);
        List<Condition> allConditions = new ArrayList<Condition>();
        if (criteria != null) {
            if (criteria.getId() != null) {
                builder.buildFilterConditionForField(criteria.getId(), entityTable.column("id"));
            }
            if (criteria.getPaymentRequestId() != null) {
                builder.buildFilterConditionForField(criteria.getPaymentRequestId(), entityTable.column("payment_request_id"));
            }
            if (criteria.getInvoiceId() != null) {
                builder.buildFilterConditionForField(criteria.getInvoiceId(), entityTable.column("invoice_id"));
            }
            if (criteria.getDeletedBy() != null) {
                builder.buildFilterConditionForField(criteria.getDeletedBy(), entityTable.column("deleted_by"));
            }

            if(criteria.getDeletedAt() != null){
                builder.buildFilterConditionForField(criteria.getDeletedAt(), entityTable.column("deleted_at"));
            }

        }
        return builder.buildConditions();
    }
}
