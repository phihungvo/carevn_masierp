package com.masi.logistics.repository;

import com.masi.logistics.domain.IncomingInvoice;
import com.masi.logistics.repository.rowmapper.*;
import com.masi.logistics.service.dto.IncomingInvoiceQuery;
import com.masi.logistics.service.mapper.SupplierContractMapper;
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

/**
 * Spring Data R2DBC custom repository implementation for the IncomingInvoice entity.
 */
@SuppressWarnings("unused")
class IncomingInvoiceRepositoryInternalImpl
    extends SimpleR2dbcRepository<IncomingInvoice, UUID>
    implements IncomingInvoiceRepositoryInternal {

    private final DatabaseClient db;
    private final EntityManager entityManager;

    private final IncomingInvoiceRowMapper incominginvoiceMapper;
    private final PaymentRequestRowMapper paymentRequestMapper;
    private final CurrencyRowMapper currencyMapper;
    private final SuppliersRowMapper supplierMapper;
    private final SupplierContractRowMapper supplierContractMapper;

    private static final Table entityTable = Table.aliased("incoming_invoice", EntityManager.ENTITY_ALIAS);
    private static final Table supplierTable = Table.aliased("suppliers", "supplier");
    private static final Table currencyTable = Table.aliased("currency", "currency");
    private static final Table paymentRequestTable = Table.aliased("payment_request", "paymentRequest");
    private static final Table reimbursementsTable = Table.aliased("payment_request", "reimbursements");
    private static final Table suplierContractTable = Table.aliased("supplier_contract", "supplierContract");

    public IncomingInvoiceRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        IncomingInvoiceRowMapper incominginvoiceMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter, PaymentRequestRowMapper paymentRequestMapper, CurrencyRowMapper currencyMapper, SuppliersRowMapper supplierMapper, SupplierContractRowMapper supplierContractMapper
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(IncomingInvoice.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.entityManager = entityManager;
        this.incominginvoiceMapper = incominginvoiceMapper;
        this.paymentRequestMapper = paymentRequestMapper;
        this.currencyMapper = currencyMapper;
        this.supplierMapper = supplierMapper;
        this.supplierContractMapper = supplierContractMapper;
    }

    @Override
    public Flux<IncomingInvoice> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<IncomingInvoice> createQuery(Pageable pageable, Condition whereClause) {
        return this.createQuery(pageable, whereClause, null);
    }

    RowsFetchSpec<IncomingInvoice> createQuery(Pageable pageable, Condition whereClause, Map<String, Object> parameters) {
        List<Expression> columns = IncomingInvoiceSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(SuppliersSqlHelper.getColumns(supplierTable, "supplier"));
        columns.addAll(CurrencySqlHelper.getColumns(currencyTable, "currency"));
        columns.addAll(PaymentRequestSqlHelper.getColumns(paymentRequestTable, "paymentRequest"));
        columns.addAll(PaymentRequestSqlHelper.getColumns(reimbursementsTable, "reimbursements"));
        columns.addAll(SupplierContractSqlHelper.getColumns(suplierContractTable, "supplierContract"));
        SelectBuilder.SelectFromAndJoinCondition selectFrom = Select.builder().select(columns).from(entityTable)
            .leftOuterJoin(supplierTable).on(Column.create("supplier_id", entityTable)).equals(Column.create("id", supplierTable))
            .leftOuterJoin(currencyTable).on(Column.create("currency_id", entityTable)).equals(Column.create("id", currencyTable))
            .leftOuterJoin(paymentRequestTable).on(Column.create("document_id", entityTable)).equals(Column.create("id", paymentRequestTable))
            .leftOuterJoin(reimbursementsTable).on(Column.create("reimbursement_id", entityTable)).equals(Column.create("id", reimbursementsTable))
            .leftOuterJoin(suplierContractTable).on(Column.create("supplier_contract_id", entityTable)).equals(Column.create("id", suplierContractTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, IncomingInvoice.class, pageable, whereClause);
        var prepared = db.sql(select);
        if (parameters != null) {
            prepared = prepared.bindValues(parameters);
        }
        return prepared.map(this::process);
    }

    @Override
    public Flux<IncomingInvoice> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<IncomingInvoice> findById(UUID id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(StringUtils.wrap(id.toString(), "'")));
        return createQuery(null, whereClause).one();
    }

    public Tuple2<Map<String, Object>, Condition> buildQuery(IncomingInvoiceQuery query) {
        Map<String, Object> parameters = new HashMap<>();
        Condition whereClause = Conditions.just("e.deleted_at is null");
        if (StringUtils.isNotBlank(query.getCompanyId())) {
            whereClause = whereClause.and(Conditions.isEqual(entityTable.column("company"), Conditions.just(StringUtils.wrap(query.getCompanyId(), "'"))));
        }
        if (StringUtils.isNotBlank(query.getDepartmentId())) {
            whereClause = whereClause.and(Conditions.isEqual(entityTable.column("department_id"), Conditions.just(StringUtils.wrap(query.getDepartmentId(), "'"))));
        }
        if (StringUtils.isNotBlank(query.getDepartment())) {
            whereClause = whereClause.and(Conditions.isEqual(entityTable.column("department"), Conditions.just(StringUtils.wrap(query.getDepartment(), "'"))));
        }
        if (query.getContractIds() != null && !query.getContractIds().isEmpty()) {
            String inClause = query.getContractIds().stream().map(id -> String.format("'%s'", id.toString())).reduce((a, b) -> a + "," + b).orElseThrow();
            whereClause = whereClause.and(Conditions.in(entityTable.column("contract_id"), Conditions.just(inClause)));
        }
        if (StringUtils.isNotBlank(query.getInvoiceType())) {
            whereClause = whereClause.and(Conditions.isEqual(entityTable.column("invoice_type"), Conditions.just(StringUtils.wrap(query.getInvoiceType(), "'"))));
        }
        if (query.getEmployeeId() != null) {
            whereClause = whereClause.and(Conditions.isEqual(entityTable.column("employee_id"), Conditions.just(StringUtils.wrap(query.getEmployeeId(), "'"))));
        }
        if (StringUtils.isNotBlank(query.getCreatedBy())) {
            whereClause = whereClause.and(Conditions.isEqual(entityTable.column("created_by"), Conditions.just(StringUtils.wrap(query.getCreatedBy(), "'"))));
        }

        if (query.getStartDate() != null) {
            whereClause = whereClause.and(Conditions.isGreaterOrEqualTo(entityTable.column("created_at"), Conditions.just(StringUtils.wrap(query.getStartDate().toString(), "'"))));
        }

        if (query.getEndDate() != null) {
            whereClause = whereClause.and(Conditions.isLessOrEqualTo(entityTable.column("created_at"), Conditions.just(StringUtils.wrap(query.getEndDate().toString(), "'"))));
        }

        if (query.getSupplierId() != null){
            whereClause = whereClause.and(Conditions.isEqual(entityTable.column("supplier_id"), Conditions.just(StringUtils.wrap(query.getSupplierId().toString(), "'"))));
        }

        if (StringUtils.isNotBlank(query.getSearch())) {
            // Các cột cần tìm kiếm
            String[] columns = {"invoice_no"};
            String[] aliases = Arrays.stream(columns)
                .map(column -> EntityManager.ENTITY_ALIAS + "." + column)
                .toArray(String[]::new);

            // Tạo điều kiện tìm kiếm cho invoice_no
            StringBuilder search = new StringBuilder();
            search.append('(');
            Arrays.stream(aliases).forEach(alias -> {
                search.append(String.format("unaccent(%s) ilike unaccent(:search) or ", alias));
            });
            if (query.getEmployeeIds() != null && !query.getEmployeeIds().isEmpty()) {
                String inClause = query.getEmployeeIds().stream()
                    .map(id -> String.format("'%s'", id.toString()))
                    .reduce((a, b) -> a + "," + b)
                    .orElseThrow();
                search.append(String.format("e.created_by in (%s) or", inClause));

            }
            int searchLength = search.length();
            search.delete(searchLength - 3, searchLength);
            
            search.append(')');
            var invoiceCondition = Conditions.just(search.toString());

            // Kết hợp với điều kiện WHERE hiện tại bằng AND
            whereClause = whereClause.and(invoiceCondition);

            // Thêm tham số tìm kiếm
            parameters.put("search", "%" + query.getSearch().trim() + "%");
        }


        // filter by supplier
        if (StringUtils.isNotBlank(query.getSearchSupplier())){
            String[] columns = {"name", "code"};
            String[] aliases = Arrays.stream(columns).map(column -> "supplier." + column).toArray(String[]::new);
            StringBuilder search = new StringBuilder();
            search.append('(');
            Arrays.stream(aliases).forEach(alias -> {
                search.append(String.format("unaccent(%s) ilike unaccent(:searchSupplier) or ", alias));
            });
            int searchLength = search.length();
            search.delete(searchLength - 3, searchLength);
            search.append(')');
            whereClause = whereClause.and(Conditions.just(search.toString()));
            parameters.put("searchSupplier", "%" + query.getSearchSupplier().trim() + "%");
        }

        if(query.getCurrencyId() != null){
            whereClause = whereClause.and(Conditions.isEqual(entityTable.column("currency_id"), Conditions.just(StringUtils.wrap(query.getCurrencyId().toString(), "'"))));
        }

        if (query.getSupplierContractId() != null){
            whereClause = whereClause.and(Conditions.isEqual(entityTable.column("supplier_contract_id"), Conditions.just(StringUtils.wrap(query.getSupplierContractId().toString(), "'"))));
        }

        return Tuples.of(parameters, whereClause);
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
    public Flux<IncomingInvoice> findAllBy(IncomingInvoiceQuery query, Pageable pageable) {
        var tuple = buildQuery(query);
        pageable = getDefaultSort(pageable);
        return createQuery(pageable, tuple.getT2(), tuple.getT1()).all();
    }

    @Override
    public Mono<Long> countAllBy(IncomingInvoiceQuery query) {
        var tuple = buildQuery(query);
        return createQuery(null, tuple.getT2(), tuple.getT1()).all().count();
    }

    private IncomingInvoice process(Row row, RowMetadata metadata) {
        IncomingInvoice entity = incominginvoiceMapper.apply(row, "e");
        entity.setSuppliers(supplierMapper.apply(row, "supplier"));
        entity.setCurrency(currencyMapper.apply(row, "currency"));
        entity.setPaymentRequest(paymentRequestMapper.apply(row, "paymentRequest"));
        entity.setReimbursement(paymentRequestMapper.apply(row, "reimbursements"));
        entity.setSupplierContract(supplierContractMapper.apply(row, "supplierContract"));
        return entity;
    }

    @Override
    public <S extends IncomingInvoice> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
