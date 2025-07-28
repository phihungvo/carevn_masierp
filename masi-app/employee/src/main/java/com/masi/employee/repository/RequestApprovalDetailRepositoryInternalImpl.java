package com.masi.employee.repository;

import com.masi.employee.domain.RequestApprovalDetail;
import com.masi.employee.domain.criteria.RequestApprovalDetailCriteria;
import com.masi.employee.repository.rowmapper.ColumnConverter;
import com.masi.employee.repository.rowmapper.RequestApprovalDetailRowMapper;
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
 * Spring Data R2DBC custom repository implementation for the RequestApprovalDetail entity.
 */
@SuppressWarnings("unused")
class RequestApprovalDetailRepositoryInternalImpl
    extends SimpleR2dbcRepository<RequestApprovalDetail, UUID>
    implements RequestApprovalDetailRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final RequestApprovalDetailRowMapper requestapprovaldetailMapper;
    private final ColumnConverter columnConverter;

    private static final Table entityTable = Table.aliased("request_approval_detail", EntityManager.ENTITY_ALIAS);

    public RequestApprovalDetailRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        RequestApprovalDetailRowMapper requestapprovaldetailMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter,
        ColumnConverter columnConverter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(RequestApprovalDetail.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.requestapprovaldetailMapper = requestapprovaldetailMapper;
        this.columnConverter = columnConverter;
    }

    @Override
    public Flux<RequestApprovalDetail> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<RequestApprovalDetail> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = RequestApprovalDetailSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        SelectFromAndJoin selectFrom = Select.builder().select(columns).from(entityTable);
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, RequestApprovalDetail.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<RequestApprovalDetail> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<RequestApprovalDetail> findById(UUID id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(StringUtils.wrap(id.toString(), "'")));
        return createQuery(null, whereClause).one();
    }

    private RequestApprovalDetail process(Row row, RowMetadata metadata) {
        RequestApprovalDetail entity = requestapprovaldetailMapper.apply(row, "e");
        return entity;
    }

    @Override
    public <S extends RequestApprovalDetail> Mono<S> save(S entity) {
        return super.save(entity);
    }

    @Override
    public Flux<RequestApprovalDetail> findByCriteria(RequestApprovalDetailCriteria requestApprovalDetailCriteria, Pageable page) {
        return createQuery(page, buildConditions(requestApprovalDetailCriteria)).all();
    }

    @Override
    public Mono<Long> countByCriteria(RequestApprovalDetailCriteria criteria) {
        return findByCriteria(criteria, null)
            .collectList()
            .map(collectedList -> collectedList != null ? (long) collectedList.size() : (long) 0);
    }

    private Condition buildConditions(RequestApprovalDetailCriteria criteria) {
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
            if (criteria.getIsApproved() != null) {
                builder.buildFilterConditionForField(criteria.getIsApproved(), entityTable.column("is_approved"));
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
            if (criteria.getEntityName() != null) {
                builder.buildFilterConditionForField(criteria.getEntityName(), entityTable.column("entity_name"));
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
