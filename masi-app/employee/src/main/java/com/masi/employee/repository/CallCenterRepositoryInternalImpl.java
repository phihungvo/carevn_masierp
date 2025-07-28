package com.masi.employee.repository;

import com.masi.employee.domain.CallCenter;
import com.masi.employee.domain.criteria.CallCenterCriteria;
import com.masi.employee.repository.rowmapper.CallCenterRowMapper;
import com.masi.employee.repository.rowmapper.ColumnConverter;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
import reactor.util.function.Tuples;
import tech.jhipster.service.ConditionBuilder;
import tech.jhipster.service.filter.BooleanFilter;

/**
 * Spring Data R2DBC custom repository implementation for the CallCenter entity.
 */
@SuppressWarnings("unused")
class CallCenterRepositoryInternalImpl extends SimpleR2dbcRepository<CallCenter, UUID> implements CallCenterRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final CallCenterRowMapper callcenterMapper;
    private final ColumnConverter columnConverter;

    private static final Table entityTable = Table.aliased("call_center", EntityManager.ENTITY_ALIAS);

    public CallCenterRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        CallCenterRowMapper callcenterMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter,
        ColumnConverter columnConverter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(CallCenter.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.callcenterMapper = callcenterMapper;
        this.columnConverter = columnConverter;
    }

    @Override
    public Flux<CallCenter> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<CallCenter> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = CallCenterSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        SelectFromAndJoin selectFrom = Select.builder().select(columns).from(entityTable);
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, CallCenter.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<CallCenter> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<CallCenter> findById(UUID id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(StringUtils.wrap(id.toString(), "'")));
        return createQuery(null, whereClause).one();
    }

    private CallCenter process(Row row, RowMetadata metadata) {
        CallCenter entity = callcenterMapper.apply(row, "e");
        return entity;
    }

    @Override
    public <S extends CallCenter> Mono<S> save(S entity) {
        return super.save(entity);
    }

    @Override
    public Flux<CallCenter> findByCriteria(CallCenterCriteria callCenterCriteria, Pageable page) {
        page = getDefaultSort(page);
        return createQuery(page, buildConditions(callCenterCriteria)).all();
    }

    private Pageable getDefaultSort(Pageable pageable) {
        Sort defaultSort = Sort.by(Sort.Direction.DESC, "created_at");
        if (Objects.isNull(pageable)) {
            pageable = PageRequest.of(0, Integer.MAX_VALUE, defaultSort);
        }
        if (pageable.getSort().equals(Sort.unsorted())) {
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), defaultSort);
        }
        return pageable;
    }

    @Override
    public Mono<Long> countByCriteria(CallCenterCriteria criteria) {
        return findByCriteria(criteria, null)
            .collectList()
            .map(collectedList -> collectedList != null ? (long) collectedList.size() : (long) 0);
    }

    private Condition buildConditions(CallCenterCriteria criteria) {
        ConditionBuilder builder = new ConditionBuilder(this.columnConverter);
        builder.buildFilterConditionForField(new BooleanFilter().setEquals(false), entityTable.column("is_deleted"));
        Condition subgroup1 = null;

        if (criteria != null) {
            if (criteria.getId() != null) {
                builder.buildFilterConditionForField(criteria.getId(), entityTable.column("id"));
            }

            if ((criteria.getCode() != null && !criteria.getCode().getContains().isEmpty())) {
                Condition group1 = Conditions.like(
                        Functions.lower(entityTable.column("problem_content")),
                        SQL.literalOf("%" + criteria.getCode().getContains().toLowerCase() + "%")
                );
                Condition group2 = Conditions.like(
                        Functions.lower(entityTable.column("resolution_content")),
                        SQL.literalOf("%" + criteria.getCode().getContains().toLowerCase() + "%")
                );
                Condition group3 = Conditions.like(
                        Functions.lower(entityTable.column("response_content")),
                        SQL.literalOf("%" + criteria.getCode().getContains().toLowerCase() + "%")
                );
                Condition group4 = Conditions.like(
                        Functions.lower(entityTable.column("code")),
                        SQL.literalOf("%" + criteria.getCode().getContains().toLowerCase() + "%")
                );
                Condition group5 = Conditions.like(
                        Functions.lower(entityTable.column("phone_of_caller")),
                        SQL.literalOf("%" + criteria.getCode().getContains().toLowerCase() + "%")
                );
                Condition group6 = Conditions.like(
                        Functions.lower(entityTable.column("phone_of_name")),
                        SQL.literalOf("%" + criteria.getCode().getContains().toLowerCase() + "%")
                );

                subgroup1 = Conditions.nest(group1.or(group2).or(group3).or(group4).or(group5).or(group6));
                builder.buildConditions().and(subgroup1);
            }

            if (criteria.getTypePageCs() != null) {
                builder.buildFilterConditionForField(criteria.getTypePageCs(), entityTable.column("type_page_cs"));
            }

            if (criteria.getReceptionDate() != null) {
                builder.buildFilterConditionForField(criteria.getReceptionDate(), entityTable.column("reception_date"));
            }
            if (criteria.getGroupCS() != null) {
                builder.buildFilterConditionForField(criteria.getGroupCS(), entityTable.column("group_cs"));
            }
            if (criteria.getPhoneOfCaller() != null) {
                builder.buildFilterConditionForField(criteria.getPhoneOfCaller(), entityTable.column("phone_of_caller"));
            }
            if (criteria.getPhoneOfName() != null) {
                builder.buildFilterConditionForField(criteria.getPhoneOfName(), entityTable.column("phone_of_name"));
            }
            if (criteria.getStatus() != null) {
                builder.buildFilterConditionForField(criteria.getStatus(), entityTable.column("status"));
            }
            if (criteria.getTypeCS() != null) {
                builder.buildFilterConditionForField(criteria.getTypeCS(), entityTable.column("type_cs"));
            }
            if (criteria.getCustomerId() != null) {
                builder.buildFilterConditionForField(criteria.getCustomerId(), entityTable.column("customer_id"));
            }
            if (criteria.getEmployeeCreatedId() != null) {
                builder.buildFilterConditionForField(criteria.getEmployeeCreatedId(), entityTable.column("employee_created_id"));
            }
            if (criteria.getAttribute() != null) {
                builder.buildFilterConditionForField(criteria.getAttribute(), entityTable.column("attribute"));
            }
            if (criteria.getEmployeeAssignId() != null) {
                builder.buildFilterConditionForField(criteria.getEmployeeAssignId(), entityTable.column("employee_assign_id"));
            }
            if (criteria.getEmployeeCloseId() != null) {
                builder.buildFilterConditionForField(criteria.getEmployeeCloseId(), entityTable.column("employee_close_id"));
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
        if(subgroup1 == null)
            return builder.buildConditions();
        return builder.buildConditions().and(subgroup1);
    }
}
