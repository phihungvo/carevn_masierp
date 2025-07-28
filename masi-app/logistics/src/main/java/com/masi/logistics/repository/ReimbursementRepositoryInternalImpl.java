package com.masi.logistics.repository;

import com.masi.logistics.domain.Reimbursement;
import com.masi.logistics.domain.criteria.ReimbursementCriteria;
import com.masi.logistics.repository.rowmapper.ColumnConverter;
import com.masi.logistics.repository.rowmapper.PaymentRequestRowMapper;
import com.masi.logistics.repository.rowmapper.ReimbursementRowMapper;
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
 * Spring Data R2DBC custom repository implementation for the Reimbursement entity.
 */
@SuppressWarnings("unused")
class ReimbursementRepositoryInternalImpl extends SimpleR2dbcRepository<Reimbursement, UUID> implements ReimbursementRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final ReimbursementRowMapper reimbursementMapper;
    private final PaymentRequestRowMapper advanceMapper;
    private final ColumnConverter columnConverter;

    private static final Table entityTable = Table.aliased("reimbursement", EntityManager.ENTITY_ALIAS);
    private static final Table advanceTable = Table.aliased("payment_request", "advance");
    public ReimbursementRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        ReimbursementRowMapper reimbursementMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter, PaymentRequestRowMapper advanceMapper,
        ColumnConverter columnConverter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(Reimbursement.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.reimbursementMapper = reimbursementMapper;
        this.advanceMapper = advanceMapper;
        this.columnConverter = columnConverter;
    }

    @Override
    public Flux<Reimbursement> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<Reimbursement> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = ReimbursementSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(PaymentRequestSqlHelper.getColumns(advanceTable, "advance"));
        SelectBuilder.SelectFromAndJoinCondition selectFrom = Select.builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(advanceTable)
            .on(entityTable.column("advance_id"))
            .equals(advanceTable.column("id"));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, Reimbursement.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<Reimbursement> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<Reimbursement> findById(UUID id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(StringUtils.wrap(id.toString(), "'")));
        return createQuery(null, whereClause).one();
    }

    private Reimbursement process(Row row, RowMetadata metadata) {
        Reimbursement entity = reimbursementMapper.apply(row, "e");
        entity.setAdvancement(advanceMapper.apply(row, "advance"));
        return entity;
    }

    @Override
    public <S extends Reimbursement> Mono<S> save(S entity) {
        return super.save(entity);
    }

    @Override
    public Flux<Reimbursement> findByCriteria(ReimbursementCriteria reimbursementCriteria, Pageable page) {
        return createQuery(page, buildConditions(reimbursementCriteria)).all();
    }

    @Override
    public Mono<Long> countByCriteria(ReimbursementCriteria criteria) {
        return findByCriteria(criteria, null)
            .collectList()
            .map(collectedList -> collectedList != null ? (long) collectedList.size() : (long) 0);
    }

    private Condition buildConditions(ReimbursementCriteria criteria) {
        ConditionBuilder builder = new ConditionBuilder(this.columnConverter);
        List<Condition> allConditions = new ArrayList<Condition>();
        if (criteria != null) {
            if (criteria.getId() != null) {
                builder.buildFilterConditionForField(criteria.getId(), entityTable.column("id"));
            }
            if (criteria.getReimbursementId() != null) {
                builder.buildFilterConditionForField(criteria.getReimbursementId(), entityTable.column("reimbursement_id"));
            }
            if (criteria.getAdvanceId() != null) {
                builder.buildFilterConditionForField(criteria.getAdvanceId(), entityTable.column("advance_id"));
            }
            if (criteria.getInvoiceId() != null) {
                builder.buildFilterConditionForField(criteria.getInvoiceId(), entityTable.column("invoice_id"));
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
