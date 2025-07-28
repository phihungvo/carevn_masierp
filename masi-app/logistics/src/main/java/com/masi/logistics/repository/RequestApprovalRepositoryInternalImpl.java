package com.masi.logistics.repository;

import com.masi.logistics.domain.RequestApproval;
import com.masi.logistics.domain.criteria.RequestApprovalCriteria;
import com.masi.logistics.repository.rowmapper.ColumnConverter;
import com.masi.logistics.repository.rowmapper.RequestApprovalRowMapper;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Spring Data R2DBC custom repository implementation for the RequestApproval entity.
 */
@SuppressWarnings("unused")
class RequestApprovalRepositoryInternalImpl
    extends SimpleR2dbcRepository<RequestApproval, UUID>
    implements RequestApprovalRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final RequestApprovalRowMapper requestapprovalMapper;
    private final ColumnConverter columnConverter;

    private static final Table entityTable = Table.aliased("request_approval", EntityManager.ENTITY_ALIAS);

    public RequestApprovalRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        RequestApprovalRowMapper requestapprovalMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter,
        ColumnConverter columnConverter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(RequestApproval.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.requestapprovalMapper = requestapprovalMapper;
        this.columnConverter = columnConverter;
    }

    @Override
    public Flux<RequestApproval> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<RequestApproval> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = RequestApprovalSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        SelectFromAndJoin selectFrom = Select.builder().select(columns).from(entityTable);
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, RequestApproval.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<RequestApproval> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<RequestApproval> findById(UUID id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(StringUtils.wrap(id.toString(), "'")));
        return createQuery(null, whereClause).one();
    }

    private RequestApproval process(Row row, RowMetadata metadata) {
        RequestApproval entity = requestapprovalMapper.apply(row, "e");
        return entity;
    }

    @Override
    public <S extends RequestApproval> Mono<S> save(S entity) {
        return super.save(entity);
    }

    @Override
    public Flux<RequestApproval> findByCriteria(RequestApprovalCriteria requestApprovalCriteria, Pageable page) {
        return createQuery(page, buildConditions(requestApprovalCriteria)).all();
    }

    @Override
    public Mono<Long> countByCriteria(RequestApprovalCriteria criteria) {
        return findByCriteria(criteria, null)
            .collectList()
            .map(collectedList -> collectedList != null ? (long) collectedList.size() : (long) 0);
    }

    private Condition buildConditions(RequestApprovalCriteria criteria) {
        ConditionBuilder builder = new ConditionBuilder(this.columnConverter);
        List<Condition> allConditions = new ArrayList<Condition>();
        if (criteria != null) {
            if (criteria.getId() != null) {
                builder.buildFilterConditionForField(criteria.getId(), entityTable.column("id"));
            }
            if (criteria.getIndex() != null) {
                builder.buildFilterConditionForField(criteria.getIndex(), entityTable.column("index"));
            }
            if (criteria.getDocumentId() != null) {
                builder.buildFilterConditionForField(criteria.getDocumentId(), entityTable.column("document_id"));
            }
            if (criteria.getEmployeeId() != null) {
                builder.buildFilterConditionForField(criteria.getEmployeeId(), entityTable.column("employee_id"));
            }
            if (criteria.getResult() != null) {
                builder.buildFilterConditionForField(criteria.getResult(), entityTable.column("result"));
            }
            if (criteria.getApprovedSign() != null) {
                builder.buildFilterConditionForField(criteria.getApprovedSign(), entityTable.column("approved_sign"));
            }
            if (criteria.getApprovedSignName() != null) {
                builder.buildFilterConditionForField(criteria.getApprovedSignName(), entityTable.column("approved_sign_name"));
            }
            if (criteria.getRejectNote() != null) {
                builder.buildFilterConditionForField(criteria.getRejectNote(), entityTable.column("reject_note"));
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
