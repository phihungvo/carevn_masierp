package com.masi.logistics.repository;

import com.masi.logistics.domain.PaymentRequest;
import com.masi.logistics.domain.criteria.PaymentRequestCriteria;
import com.masi.logistics.repository.rowmapper.ColumnConverter;
import com.masi.logistics.repository.rowmapper.PaymentRequestRowMapper;
import com.masi.logistics.repository.rowmapper.SuppliersRowMapper;
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
 * Spring Data R2DBC custom repository implementation for the PaymentRequest entity.
 */
@SuppressWarnings("unused")
class PaymentRequestRepositoryInternalImpl extends SimpleR2dbcRepository<PaymentRequest, UUID> implements PaymentRequestRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;
    private final SuppliersRowMapper suppliersMapper;
    private final PaymentRequestRowMapper paymentrequestMapper;
    private final ColumnConverter columnConverter;

    private static final Table entityTable = Table.aliased("payment_request", EntityManager.ENTITY_ALIAS);
    private static final Table suppliersTable = Table.aliased("suppliers", "suppliers");


    public PaymentRequestRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        PaymentRequestRowMapper paymentrequestMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter, SuppliersRowMapper suppliersMapper,
        ColumnConverter columnConverter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(PaymentRequest.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.paymentrequestMapper = paymentrequestMapper;
        this.suppliersMapper = suppliersMapper;
        this.columnConverter = columnConverter;
    }

    @Override
    public Flux<PaymentRequest> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<PaymentRequest> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = PaymentRequestSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(SuppliersSqlHelper.getColumns(suppliersTable, "suppliers"));
        SelectBuilder.SelectFromAndJoinCondition selectFrom = Select.builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(suppliersTable)
            .on(Column.create("supplier_id", entityTable))
            .equals(Column.create("id", suppliersTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, PaymentRequest.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    RowsFetchSpec<PaymentRequest> createQueryCustom(Pageable pageable, Condition whereClause, HashMap<String, Object> parameters) {
        List<Expression> columns = PaymentRequestSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(SuppliersSqlHelper.getColumns(suppliersTable, "suppliers"));
        SelectBuilder.SelectFromAndJoinCondition selectFrom = Select.builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(suppliersTable)
            .on(Column.create("supplier_id", entityTable))
            .equals(Column.create("id", suppliersTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, PaymentRequest.class, pageable, whereClause);
        var prepared = db.sql(select);
        if (parameters != null) {
            prepared = prepared.bindValues(parameters);
        }
        return prepared.map(this::process);
    }

    @Override
    public Flux<PaymentRequest> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<PaymentRequest> findById(UUID id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(StringUtils.wrap(id.toString(), "'")));
        return createQuery(null, whereClause).one();
    }

    private PaymentRequest process(Row row, RowMetadata metadata) {
        PaymentRequest entity = paymentrequestMapper.apply(row, "e");
        entity.setSuppliers(suppliersMapper.apply(row, "suppliers"));
        return entity;
    }

    @Override
    public <S extends PaymentRequest> Mono<S> save(S entity) {
        return super.save(entity);
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
    public Flux<PaymentRequest> findByCriteria(PaymentRequestCriteria paymentRequestCriteria, Pageable page) {
        page = getDefaultSort(page);
        var tuple = buildConditions(paymentRequestCriteria);
        return createQueryCustom(page, tuple.getT1(),tuple.getT2() ).all();
    }

    @Override
    public Mono<Long> countByCriteria(PaymentRequestCriteria criteria) {
        return findByCriteria(criteria, null)
            .collectList()
            .map(collectedList -> collectedList != null ? (long) collectedList.size() : (long) 0);
    }

    private Tuple2<Condition, HashMap<String, Object>> buildConditions(PaymentRequestCriteria criteria) {
        ConditionBuilder builder = new ConditionBuilder(this.columnConverter);
        HashMap<String, Object> parameters = new HashMap<>();
        List<Condition> allConditions = new ArrayList<Condition>();
        if (criteria != null) {
            if (criteria.getId() != null) {
                builder.buildFilterConditionForField(criteria.getId(), entityTable.column("id"));
            }
            if (criteria.getCode() != null) {
                builder.buildFilterConditionForField(criteria.getCode(), entityTable.column("code"));
            }

            if (criteria.getEmployeeId() != null) {
                builder.buildFilterConditionForField(criteria.getEmployeeId(), entityTable.column("employee_id"));
            }
            if (criteria.getOrder() != null) {
                builder.buildFilterConditionForField(criteria.getOrder(), entityTable.column("jhi_order"));
            }
            if (criteria.getCreatedBy() != null) {
                builder.buildFilterConditionForField(criteria.getCreatedBy(), entityTable.column("created_by"));
            }
            if (criteria.getCreatedDate() != null) {
                builder.buildFilterConditionForField(criteria.getCreatedDate(), entityTable.column("created_date"));
            }
            if (criteria.getType() != null) {
                builder.buildFilterConditionForField(criteria.getType(), entityTable.column("type"));
            }
            if (criteria.getDepartmentId() != null) {
                builder.buildFilterConditionForField(criteria.getDepartmentId(), entityTable.column("department_id"));
            }
            if (criteria.getCompany() != null) {
                builder.buildFilterConditionForField(criteria.getCompany(), entityTable.column("company"));
            }
            // check content value empty:
            var contentFilter = criteria.getContent();
            if (contentFilter != null && contentFilter.getEquals() != null && !contentFilter.getEquals().isEmpty()) {
                builder.buildFilterConditionForField(criteria.getContent(), entityTable.column("content"));
            }
            //
            if (criteria.getAttachments() != null) {
                builder.buildFilterConditionForField(criteria.getAttachments(), entityTable.column("attachments"));
            }
            if (criteria.getTotalAmount() != null) {
                builder.buildFilterConditionForField(criteria.getTotalAmount(), entityTable.column("total_amount"));
            }
            if (criteria.getPaidAmount() != null) {
                builder.buildFilterConditionForField(criteria.getPaidAmount(), entityTable.column("paid_amount"));
            }
            if (criteria.getRemainingAmount() != null) {
                builder.buildFilterConditionForField(criteria.getRemainingAmount(), entityTable.column("remaining_amount"));
            }
            if (criteria.getPaymentDate() != null) {
                builder.buildFilterConditionForField(criteria.getPaymentDate(), entityTable.column("payment_date"));
            }
            if (criteria.getNote() != null) {
                builder.buildFilterConditionForField(criteria.getNote(), entityTable.column("note"));
            }
            if (criteria.getStatus() != null) {
                builder.buildFilterConditionForField(criteria.getStatus(), entityTable.column("status"));
            }
            if (criteria.getSupplierId() != null) {
                builder.buildFilterConditionForField(criteria.getSupplierId(), entityTable.column("supplier_id"));
            }

            // add conditions to the builder
            if (criteria.getSearch() != null){
                String[] columns = {"code", "content"};
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
                    Condition inCondition = Conditions.in(entityTable.column("employee_id"), Conditions.just(inClause));
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
