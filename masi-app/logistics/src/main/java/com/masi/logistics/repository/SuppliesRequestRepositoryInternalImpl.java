package com.masi.logistics.repository;

import com.carevn.masi.utils.Utilities;
import com.masi.logistics.domain.SuppliesRequest;
import com.masi.logistics.domain.SuppliesRequestType;
import com.masi.logistics.domain.criteria.SuppliesRequestCriteria;
import com.masi.logistics.repository.rowmapper.ColumnConverter;
import com.masi.logistics.repository.rowmapper.SuppliersRowMapper;
import com.masi.logistics.repository.rowmapper.SuppliesRequestRowMapper;
import com.masi.logistics.repository.rowmapper.SuppliesRequestTypeRowMapper;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;

import java.util.*;

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
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;
import tech.jhipster.service.ConditionBuilder;

/**
 * Spring Data R2DBC custom repository implementation for the SuppliesRequest entity.
 */
@SuppressWarnings("unused")
class SuppliesRequestRepositoryInternalImpl
    extends SimpleR2dbcRepository<SuppliesRequest, UUID>
    implements SuppliesRequestRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final SuppliesRequestRowMapper suppliesrequestMapper;
    private final SuppliesRequestTypeRowMapper suppliesrequesttypeMapper;
    private final SuppliersRowMapper supplierRowMapper;
    private final ColumnConverter columnConverter;
    private static final Table suppliersTable = Table.aliased("suppliers", "supplier");
    private static final Table entityTable = Table.aliased("supplies_request", EntityManager.ENTITY_ALIAS);
    private static final Table requestTypeTable = Table.aliased("supplies_request_type", "requestType");

    public SuppliesRequestRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        SuppliesRequestRowMapper suppliesrequestMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter,
        SuppliesRequestTypeRowMapper suppliesrequesttypeMapper,
        ColumnConverter columnConverter,
        SuppliersRowMapper supplierRowMapper
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(SuppliesRequest.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.suppliesrequestMapper = suppliesrequestMapper;
        this.suppliesrequesttypeMapper = suppliesrequesttypeMapper;
        this.supplierRowMapper = supplierRowMapper;
        this.columnConverter = columnConverter;
    }

    @Override
    public Flux<SuppliesRequest> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<SuppliesRequest> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = SuppliesRequestSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(SuppliesRequestTypeSqlHelper.getColumns(requestTypeTable, "requestType"));
        columns.addAll(SuppliersSqlHelper.getColumns(suppliersTable, "supplier"));
        SelectBuilder.SelectFromAndJoinCondition selectFrom = Select.builder().select(columns).from(entityTable)
            .leftOuterJoin(requestTypeTable)
            .on(Column.create("request_type_id", entityTable))
            .equals(Column.create("id", requestTypeTable))
            .leftOuterJoin(suppliersTable)
            .on(Column.create("supplier_id", entityTable))
            .equals(Column.create("id", suppliersTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, SuppliesRequest.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    RowsFetchSpec<SuppliesRequest> createQueryCustom(Pageable pageable, Condition whereClause, HashMap<String, Object> parameters) {
        List<Expression> columns = SuppliesRequestSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(SuppliesRequestTypeSqlHelper.getColumns(requestTypeTable, "requestType"));
        columns.addAll(SuppliersSqlHelper.getColumns(suppliersTable, "supplier"));
        SelectBuilder.SelectFromAndJoinCondition selectFrom = Select.builder().select(columns).from(entityTable)
            .leftOuterJoin(requestTypeTable)
            .on(Column.create("request_type_id", entityTable))
            .equals(Column.create("id", requestTypeTable))
            .leftOuterJoin(suppliersTable)
            .on(Column.create("supplier_id", entityTable))
            .equals(Column.create("id", suppliersTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, SuppliesRequest.class, pageable, whereClause);
        var prepared = db.sql(select);
        if (parameters != null) {
            prepared = prepared.bindValues(parameters);
        }
        return prepared.map(this::process);
    }

    @Override
    public Flux<SuppliesRequest> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<SuppliesRequest> findById(UUID id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(StringUtils.wrap(id.toString(), "'")));
        return createQuery(null, whereClause).one();
    }

    private SuppliesRequest process(Row row, RowMetadata metadata) {
        SuppliesRequest entity = suppliesrequestMapper.apply(row, "e");
        entity.setRequestType(suppliesrequesttypeMapper.apply(row, "requestType"));
        entity.setSupplier(supplierRowMapper.apply(row, "supplier"));
        return entity;
    }

    @Override
    public <S extends SuppliesRequest> Mono<S> save(S entity) {
        return super.save(entity);
    }


    @Override
    public Flux<SuppliesRequest> findByCriteria(SuppliesRequestCriteria suppliesRequestCriteria, Pageable page) {
        page = getDefaultSort(page);
        var tuple = buildConditions(suppliesRequestCriteria);
        return createQueryCustom(page,tuple.getT1(), tuple.getT2() ).all();
    }

    private Pageable getDefaultSort(Pageable pageable) {
        Sort defaultSort = Sort.by(Sort.Direction.DESC, "created_date");
        if (Objects.isNull(pageable)) {
            pageable = PageRequest.of(0, Integer.MAX_VALUE, defaultSort);
        }
        if (pageable.getSort().equals(Sort.unsorted())) {
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), defaultSort);
        }
        return pageable;
    }

    @Override
    public Mono<Long> countByCriteria(SuppliesRequestCriteria criteria) {
        return findByCriteria(criteria, null)
            .collectList()
            .map(collectedList -> collectedList != null ? (long) collectedList.size() : (long) 0);
    }

    private Tuple2<Condition, HashMap<String, Object>> buildConditions(SuppliesRequestCriteria criteria) {
        ConditionBuilder builder = new ConditionBuilder(this.columnConverter);
        List<Condition> allConditions = new ArrayList<Condition>();
        HashMap<String, Object> parameters = new HashMap<>();

        if (criteria != null) {
            if (criteria.getId() != null) {
                builder.buildFilterConditionForField(criteria.getId(), entityTable.column("id"));
            }
            if (criteria.getCode() != null) {
                builder.buildFilterConditionForField(criteria.getCode(), entityTable.column("code"));
            }
            if (criteria.getRequestNumber() != null) {
                builder.buildFilterConditionForField(criteria.getRequestNumber(), entityTable.column("request_number"));
            }
            if (criteria.getRequestDate() != null) {
                builder.buildFilterConditionForField(criteria.getRequestDate(), entityTable.column("request_date"));
            }
            if (criteria.getRequestByEmployeeId() != null) {
                builder.buildFilterConditionForField(criteria.getRequestByEmployeeId(), entityTable.column("request_by_employee_id"));
            }
            if (criteria.getDepartmentId() != null) {
                builder.buildFilterConditionForField(criteria.getDepartmentId(), entityTable.column("department_id"));
            }
            if (criteria.getRequestStatus() != null) {
                builder.buildFilterConditionForField(criteria.getRequestStatus(), entityTable.column("request_status"));
            }
            if (criteria.getTotalAmount() != null) {
                builder.buildFilterConditionForField(criteria.getTotalAmount(), entityTable.column("total_amount"));
            }
            if (criteria.getNote() != null) {
                builder.buildFilterConditionForField(criteria.getNote(), entityTable.column("note"));
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

            if (criteria.getRequestTypeId() != null) {
                builder.buildFilterConditionForField(criteria.getRequestTypeId(), entityTable.column("request_type_id"));
            }

            if (criteria.getRequestTypeId() != null) {
                builder.buildFilterConditionForField(criteria.getRequestTypeId(), entityTable.column("request_type_id"));
            }

            // add conditions to the builder
            if (criteria.getSearch() != null){
                String[] columns = {"request_number", "note"};
                String[] aliases = Arrays.stream(columns).map(column -> EntityManager.ENTITY_ALIAS + "." + column).toArray(String[]::new);
                StringBuilder search = new StringBuilder();
                search.append('(');
                Arrays.stream(aliases).forEach(alias -> {
                    search.append(String.format("unaccent(%s) ilike unaccent(:search) or ", alias));
                });
                int searchLength = search.length();
                search.delete(searchLength - 3, searchLength);
                search.append(')');
                Condition whereClause = Conditions.just(search.toString());
                parameters.put("search", "%" + criteria.getSearch().trim() + "%");
                if (criteria.getEmployeeIds() != null && !criteria.getEmployeeIds().isEmpty()) {
                    String inClause = criteria.getEmployeeIds().stream().map(id -> String.format("'%s'", id.toString())).reduce((a, b) -> a + "," + b).orElseThrow();
                    Condition inCondition = Conditions.in(entityTable.column("created_by"), Conditions.just(inClause));
                    whereClause = whereClause.or(inCondition);
                }
                allConditions.add(whereClause);
            }
            var c = builder.buildConditions();
            if (c != null) {
                allConditions.add(c);
            }
        }
        Condition defaultCondition = Conditions.just("1=1"); // Default condition that is always true
        var rs = allConditions.stream().reduce(Condition::and).orElse(defaultCondition);
        return Tuples.of(rs, parameters);
    }
}
