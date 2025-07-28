package com.masi.sale.repository;

import com.masi.sale.domain.PurchaseDelivery;
import com.masi.sale.repository.rowmapper.PurchaseDeliveryRowMapper;
import com.masi.sale.repository.rowmapper.PurchaseRequestRowMapper;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import java.util.List;
import java.util.UUID;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.convert.R2dbcConverter;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.repository.support.SimpleR2dbcRepository;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Comparison;
import org.springframework.data.relational.core.sql.Condition;
import org.springframework.data.relational.core.sql.Conditions;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Select;
import org.springframework.data.relational.core.sql.SelectBuilder.SelectFromAndJoinCondition;
import org.springframework.data.relational.core.sql.Table;
import org.springframework.data.relational.repository.support.MappingRelationalEntityInformation;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.r2dbc.core.RowsFetchSpec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC custom repository implementation for the PurchaseDelivery entity.
 */
@SuppressWarnings("unused")
class PurchaseDeliveryRepositoryInternalImpl
    extends SimpleR2dbcRepository<PurchaseDelivery, UUID>
    implements PurchaseDeliveryRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final PurchaseRequestRowMapper purchaserequestMapper;
    private final PurchaseDeliveryRowMapper purchasedeliveryMapper;

    private static final Table entityTable = Table.aliased("purchase_delivery", EntityManager.ENTITY_ALIAS);
    private static final Table purchaseRequestTable = Table.aliased("purchase_request", "purchaseRequest");

    public PurchaseDeliveryRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        PurchaseRequestRowMapper purchaserequestMapper,
        PurchaseDeliveryRowMapper purchasedeliveryMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(PurchaseDelivery.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.purchaserequestMapper = purchaserequestMapper;
        this.purchasedeliveryMapper = purchasedeliveryMapper;
    }

    @Override
    public Flux<PurchaseDelivery> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<PurchaseDelivery> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = PurchaseDeliverySqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(PurchaseRequestSqlHelper.getColumns(purchaseRequestTable, "purchaseRequest"));
        SelectFromAndJoinCondition selectFrom = Select.builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(purchaseRequestTable)
            .on(Column.create("purchase_request_id", entityTable))
            .equals(Column.create("id", purchaseRequestTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, PurchaseDelivery.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<PurchaseDelivery> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<PurchaseDelivery> findById(UUID id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(StringUtils.wrap(id.toString(), "'")));
        return createQuery(null, whereClause).one();
    }

    private PurchaseDelivery process(Row row, RowMetadata metadata) {
        PurchaseDelivery entity = purchasedeliveryMapper.apply(row, "e");
        entity.setPurchaseRequest(purchaserequestMapper.apply(row, "purchaseRequest"));
        return entity;
    }

    @Override
    public <S extends PurchaseDelivery> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
