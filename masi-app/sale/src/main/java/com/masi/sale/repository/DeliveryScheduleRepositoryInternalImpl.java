package com.masi.sale.repository;

import com.masi.sale.domain.DeliverySchedule;
import com.masi.sale.domain.criteria.DeliveryScheduleCriteria;
import com.masi.sale.repository.rowmapper.ColumnConverter;
import com.masi.sale.repository.rowmapper.DeliveryScheduleRowMapper;
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
 * Spring Data R2DBC custom repository implementation for the DeliverySchedule entity.
 */
@SuppressWarnings("unused")
class DeliveryScheduleRepositoryInternalImpl
    extends SimpleR2dbcRepository<DeliverySchedule, UUID>
    implements DeliveryScheduleRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final DeliveryScheduleRowMapper deliveryscheduleMapper;
    private final ColumnConverter columnConverter;

    private static final Table entityTable = Table.aliased("delivery_schedule", EntityManager.ENTITY_ALIAS);

    public DeliveryScheduleRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        DeliveryScheduleRowMapper deliveryscheduleMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter,
        ColumnConverter columnConverter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(DeliverySchedule.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.deliveryscheduleMapper = deliveryscheduleMapper;
        this.columnConverter = columnConverter;
    }

    @Override
    public Flux<DeliverySchedule> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<DeliverySchedule> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = DeliveryScheduleSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        SelectFromAndJoin selectFrom = Select.builder().select(columns).from(entityTable);
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, DeliverySchedule.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<DeliverySchedule> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<DeliverySchedule> findById(UUID id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(StringUtils.wrap(id.toString(), "'")));
        return createQuery(null, whereClause).one();
    }

    private DeliverySchedule process(Row row, RowMetadata metadata) {
        DeliverySchedule entity = deliveryscheduleMapper.apply(row, "e");
        return entity;
    }

    @Override
    public <S extends DeliverySchedule> Mono<S> save(S entity) {
        return super.save(entity);
    }

//    @Override
    public Flux<DeliverySchedule> findByCriteria(DeliveryScheduleCriteria deliveryScheduleCriteria, Pageable page) {
        return createQuery(page, buildConditions(deliveryScheduleCriteria)).all();
    }

//    @Override
    public Mono<Long> countByCriteria(DeliveryScheduleCriteria criteria) {
        return findByCriteria(criteria, null)
            .collectList()
            .map(collectedList -> collectedList != null ? (long) collectedList.size() : (long) 0);
    }

    private Condition buildConditions(DeliveryScheduleCriteria criteria) {
        ConditionBuilder builder = new ConditionBuilder(this.columnConverter);
        List<Condition> allConditions = new ArrayList<Condition>();
        if (criteria != null) {
            if (criteria.getId() != null) {
                builder.buildFilterConditionForField(criteria.getId(), entityTable.column("id"));
            }
            if (criteria.getCode() != null) {
                builder.buildFilterConditionForField(criteria.getCode(), entityTable.column("code"));
            }
            if (criteria.getDeliveryDate() != null) {
                builder.buildFilterConditionForField(criteria.getDeliveryDate(), entityTable.column("delivery_date"));
            }
            if (criteria.getExpectedReceiveDate() != null) {
                builder.buildFilterConditionForField(criteria.getExpectedReceiveDate(), entityTable.column("expected_receive_date"));
            }
            if (criteria.getContractId() != null) {
                builder.buildFilterConditionForField(criteria.getContractId(), entityTable.column("contract_id"));
            }
            if (criteria.getOrderId() != null) {
                builder.buildFilterConditionForField(criteria.getOrderId(), entityTable.column("order_id"));
            }
            if (criteria.getContent() != null) {
                builder.buildFilterConditionForField(criteria.getContent(), entityTable.column("content"));
            }
            if (criteria.getQuantity() != null) {
                builder.buildFilterConditionForField(criteria.getQuantity(), entityTable.column("quantity"));
            }
            if (criteria.getUnitId() != null) {
                builder.buildFilterConditionForField(criteria.getUnitId(), entityTable.column("unit_id"));
            }
            if (criteria.getPrice() != null) {
                builder.buildFilterConditionForField(criteria.getPrice(), entityTable.column("price"));
            }
            if (criteria.getTotal() != null) {
                builder.buildFilterConditionForField(criteria.getTotal(), entityTable.column("total"));
            }
            if (criteria.getPaymentMethod() != null) {
                builder.buildFilterConditionForField(criteria.getPaymentMethod(), entityTable.column("payment_method"));
            }
            if (criteria.getReceiverName() != null) {
                builder.buildFilterConditionForField(criteria.getReceiverName(), entityTable.column("receiver_name"));
            }
            if (criteria.getDeliveryLocation() != null) {
                builder.buildFilterConditionForField(criteria.getDeliveryLocation(), entityTable.column("delivery_location"));
            }
            if (criteria.getNote() != null) {
                builder.buildFilterConditionForField(criteria.getNote(), entityTable.column("note"));
            }
            if (criteria.getType() != null) {
                builder.buildFilterConditionForField(criteria.getType(), entityTable.column("type"));
            }
            if (criteria.getAttachment() != null) {
                builder.buildFilterConditionForField(criteria.getAttachment(), entityTable.column("attachment"));
            }
            if (criteria.getAttribute() != null) {
                builder.buildFilterConditionForField(criteria.getAttribute(), entityTable.column("attribute"));
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
