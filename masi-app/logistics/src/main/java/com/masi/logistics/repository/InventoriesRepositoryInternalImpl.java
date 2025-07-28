package com.masi.logistics.repository;

import com.masi.logistics.domain.Inventories;
import com.masi.logistics.domain.InventoriesType;
import com.masi.logistics.domain.criteria.InventoriesCriteria;
import com.masi.logistics.repository.rowmapper.*;
import com.masi.logistics.repository.rowmapper.ContactRowMapper;

import com.masi.logistics.service.mapper.IncomingInvoiceMapper;
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
import tech.jhipster.service.filter.BooleanFilter;

/**
 * Spring Data R2DBC custom repository implementation for the Inventories entity.
 */
@SuppressWarnings("unused")
class InventoriesRepositoryInternalImpl extends SimpleR2dbcRepository<Inventories, UUID> implements InventoriesRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;
    private final InventoriesTypeRowMapper inventoriesTypeRowMapper;
    private final SuppliersRowMapper suppliersMapper;
    private final IncomingInvoiceRowMapper invoiceMapper;
    private final WarehouseRowMapper warehouseMapper;

    private final InventoriesRowMapper inventoriesMapper;
    private final ColumnConverter columnConverter;
    private final SupplierContractRowMapper supplierContractRowMapper;

    private static final Table entityTable = Table.aliased("inventories", EntityManager.ENTITY_ALIAS);
    private static final Table inventoriesTypeTable = Table.aliased("inventories_type", "inventories_type"); // inventories_type_id
    private static final Table suppliersCustomerTable = Table.aliased("suppliers", "suppliers_customer");
    private static final Table supplierscustomerRecipientTable = Table.aliased("suppliers", "suppliers_customer_recipient");
    private static final Table invoiceTable = Table.aliased("incoming_invoice", "incoming_invoice"); //invoice_id

    private static final Table incomingWarehouseTable = Table.aliased("warehouse", "incoming_warehouse");
    private static final Table outgoingWarehouseTable = Table.aliased("warehouse", "outgoing_warehouse");
    private static final Table supplierContractTable = Table.aliased("supplier_contract", "supplier_contract");


//    private static final Table orderIdTable = Table.aliased("order_id", "order_id");

    public InventoriesRepositoryInternalImpl(
            R2dbcEntityTemplate template,
            EntityManager entityManager,
            InventoriesRowMapper inventoriesMapper,
            R2dbcEntityOperations entityOperations,
            R2dbcConverter converter, InventoriesTypeRowMapper inventoriesTypeRowMapper, SuppliersRowMapper suppliersMapper, IncomingInvoiceRowMapper invoiceMapper, WarehouseRowMapper warehouseMapper,
            ColumnConverter columnConverter, SupplierContractRowMapper supplierContractRowMapper
    ) {
        super(
                new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(Inventories.class)),
                entityOperations,
                converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.inventoriesMapper = inventoriesMapper;
        this.inventoriesTypeRowMapper = inventoriesTypeRowMapper;
        this.suppliersMapper = suppliersMapper;
        this.invoiceMapper = invoiceMapper;
        this.warehouseMapper = warehouseMapper;
        this.columnConverter = columnConverter;
        this.supplierContractRowMapper = supplierContractRowMapper;
    }

    @Override
    public Flux<Inventories> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<Inventories> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = InventoriesSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);

        // Add columns from related tables
        columns.addAll(InventoriesTypeSqlHelper.getColumns(inventoriesTypeTable, "inventories_type"));
        columns.addAll(SuppliersSqlHelper.getColumns(suppliersCustomerTable, "suppliers_customer"));
        columns.addAll(SuppliersSqlHelper.getColumns(supplierscustomerRecipientTable, "suppliers_customer_recipient"));
        columns.addAll(IncomingInvoiceSqlHelper.getColumns(invoiceTable, "incoming_invoice"));
        columns.addAll(WarehouseSqlHelper.getColumns(incomingWarehouseTable, "incoming_warehouse"));
        columns.addAll(WarehouseSqlHelper.getColumns(outgoingWarehouseTable, "outgoing_warehouse"));
        columns.addAll(SupplierContractSqlHelper.getColumns(supplierContractTable, "supplier_contract"));
        // Build the SELECT query with JOINs
        SelectBuilder.SelectFromAndJoinCondition selectFrom = Select.builder()
                .select(columns)
                .from(entityTable)
                .leftOuterJoin(inventoriesTypeTable)
                .on(Column.create("inventories_type_id", entityTable))
                .equals(Column.create("id", inventoriesTypeTable))
                .leftOuterJoin(suppliersCustomerTable)
                .on(Column.create("customer_id", entityTable))
                .equals(Column.create("id", suppliersCustomerTable))
                .leftOuterJoin(supplierscustomerRecipientTable)
                .on(Column.create("customer_recipient_id", entityTable))
                .equals(Column.create("id", supplierscustomerRecipientTable))
                .leftOuterJoin(invoiceTable)
                .on(Column.create("invoice_id", entityTable))
                .equals(Column.create("id", invoiceTable))
                .leftOuterJoin(incomingWarehouseTable)
                .on(Column.create("incoming_warehouse_id", entityTable))
                .equals(Column.create("id", incomingWarehouseTable))
                .leftOuterJoin(outgoingWarehouseTable)
                .on(Column.create("outgoing_warehouse_id", entityTable))
                .equals(Column.create("id", outgoingWarehouseTable))
                .leftOuterJoin(supplierContractTable)
                .on(Column.create("purchase_contract_id", entityTable))
                .equals(Column.create("id", supplierContractTable));

        // Create the SELECT SQL statement
        String select = entityManager.createSelect(selectFrom, Inventories.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    RowsFetchSpec<Inventories> createQueryCustom(Pageable pageable, Condition whereClause, HashMap<String, Object> parameters) {
        List<Expression> columns = InventoriesSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);

        // Add columns from related tables
        columns.addAll(InventoriesTypeSqlHelper.getColumns(inventoriesTypeTable, "inventories_type"));
        columns.addAll(SuppliersSqlHelper.getColumns(suppliersCustomerTable, "suppliers_customer"));
        columns.addAll(SuppliersSqlHelper.getColumns(supplierscustomerRecipientTable, "suppliers_customer_recipient"));
        columns.addAll(IncomingInvoiceSqlHelper.getColumns(invoiceTable, "incoming_invoice"));
        columns.addAll(WarehouseSqlHelper.getColumns(incomingWarehouseTable, "incoming_warehouse"));
        columns.addAll(WarehouseSqlHelper.getColumns(outgoingWarehouseTable, "outgoing_warehouse"));
        columns.addAll(SupplierContractSqlHelper.getColumns(supplierContractTable, "supplier_contract"));
        // Build the SELECT query with JOINs
        SelectBuilder.SelectFromAndJoinCondition selectFrom = Select.builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(inventoriesTypeTable)
            .on(Column.create("inventories_type_id", entityTable))
            .equals(Column.create("id", inventoriesTypeTable))
            .leftOuterJoin(suppliersCustomerTable)
            .on(Column.create("customer_id", entityTable))
            .equals(Column.create("id", suppliersCustomerTable))
            .leftOuterJoin(supplierscustomerRecipientTable)
            .on(Column.create("customer_recipient_id", entityTable))
            .equals(Column.create("id", supplierscustomerRecipientTable))
            .leftOuterJoin(invoiceTable)
            .on(Column.create("invoice_id", entityTable))
            .equals(Column.create("id", invoiceTable))
            .leftOuterJoin(incomingWarehouseTable)
            .on(Column.create("incoming_warehouse_id", entityTable))
            .equals(Column.create("id", incomingWarehouseTable))
            .leftOuterJoin(outgoingWarehouseTable)
            .on(Column.create("outgoing_warehouse_id", entityTable))
            .equals(Column.create("id", outgoingWarehouseTable))
            .leftOuterJoin(supplierContractTable)
            .on(Column.create("purchase_contract_id", entityTable))
            .equals(Column.create("id", supplierContractTable));

        // Create the SELECT SQL statement
        String select = entityManager.createSelect(selectFrom, Inventories.class, pageable, whereClause);
        var prepared = db.sql(select);
        if (parameters != null) {
            prepared = prepared.bindValues(parameters);
        }
        return prepared.map(this::process);
    }

    @Override
    public Flux<Inventories> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<Inventories> findById(UUID id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(StringUtils.wrap(id.toString(), "'")));
        return createQuery(null, whereClause).one();
    }

    private Inventories process(Row row, RowMetadata metadata) {
        Inventories entity = inventoriesMapper.apply(row, "e");
        entity.setInventoriesType(inventoriesTypeRowMapper.apply(row, "inventories_type"));

        // Ánh xạ dữ liệu từ bảng suppliers_customer
        if (suppliersCustomerTable != null) {
            entity.setCustomer(suppliersMapper.apply(row, "suppliers_customer"));
        }

        // Ánh xạ dữ liệu từ bảng suppliers_customer_recipient
        if (supplierscustomerRecipientTable != null) {
            entity.setCustomerRecipient(suppliersMapper.apply(row, "suppliers_customer_recipient"));
        }

        // Ánh xạ dữ liệu từ bảng incoming_invoice
        if (invoiceTable != null) {
            entity.setInvoice(invoiceMapper.apply(row, "incoming_invoice"));
        }

        // Ánh xạ dữ liệu từ bảng incoming_warehouse
        if (incomingWarehouseTable != null) {
            entity.setIncomingWarehouse(warehouseMapper.apply(row, "incoming_warehouse"));
        }

        // Ánh xạ dữ liệu từ bảng outgoing_warehouse
        if (outgoingWarehouseTable != null) {
            entity.setOutgoingWarehouse(warehouseMapper.apply(row, "outgoing_warehouse"));
        }

        // Ánh xạ dữ liệu từ bảng outgoing_warehouse
        if (supplierContractTable != null) {
            entity.setPurchaseContract(supplierContractRowMapper.apply(row, "supplier_contract"));
        }

        return entity;
    }


    @Override
    public <S extends Inventories> Mono<S> save(S entity) {
        return super.save(entity);
    }

    @Override
    public Flux<Inventories> findByCriteria(InventoriesCriteria inventoriesCriteria, Pageable page) {
        page = getDefaultSort(page);
        var tuples = buildConditions(inventoriesCriteria);
        return createQueryCustom(page, tuples.getT1(), tuples.getT2()).all();
    }

    @Override
    public Mono<Long> countByCriteria(InventoriesCriteria criteria) {
        return findByCriteria(criteria, null)
                .collectList()
                .map(collectedList -> collectedList != null ? (long) collectedList.size() : (long) 0);
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

    private Tuple2<Condition, HashMap<String, Object>> buildConditions(InventoriesCriteria criteria) {
        ConditionBuilder builder = new ConditionBuilder(this.columnConverter);
        builder.buildFilterConditionForField(new BooleanFilter().setEquals(false), entityTable.column("is_deleted"));
        Condition subgroup1 = null;
        List<Condition> allConditions = new ArrayList<Condition>();
        HashMap<String, Object> parameters = new HashMap<>();
        if (criteria != null) {
            if (criteria.getId() != null) {
                builder.buildFilterConditionForField(criteria.getId(), entityTable.column("id"));
            }
            if ((criteria.getCode() != null && !criteria.getCode().getContains().toString().equals(""))) {
                Condition group1 = Conditions.like(entityTable.column("code"), SQL.literalOf("%" + criteria.getCode().getContains() + "%"));
                subgroup1 = Conditions.nest(group1);
                builder.buildConditions().and(subgroup1);
            }

            if (criteria.getStatus() != null) {
                builder.buildFilterConditionForField(criteria.getStatus(), entityTable.column("status"));
            }

            if (criteria.getCompany() != null) {
                builder.buildFilterConditionForField(criteria.getCompany(), entityTable.column("company"));
            }

            if (criteria.getInventoriesTypeId() != null) {
                builder.buildFilterConditionForField(criteria.getInventoriesTypeId(), entityTable.column("inventories_type_id"));
            }
            if (criteria.getCustomerId() != null) {
                builder.buildFilterConditionForField(criteria.getCustomerId(), entityTable.column("customer_id"));
            }
            if (criteria.getIncomingWarehouseId() != null) {
                builder.buildFilterConditionForField(criteria.getIncomingWarehouseId(), entityTable.column("incoming_warehouse_id"));
            }
            if (criteria.getIsInvoice() != null) {
                builder.buildFilterConditionForField(criteria.getIsInvoice(), entityTable.column("is_invoice"));
            }

            if (criteria.getWarehouseGroupType() != null) {
                builder.buildFilterConditionForField(criteria.getWarehouseGroupType(), entityTable.column("warehouse_type"));
            }
            if (criteria.getCreatedAt() != null) {
                builder.buildFilterConditionForField(criteria.getCreatedAt(), entityTable.column("date_create"));
            }

            if (criteria.getIsDeleted() != null) {
                builder.buildFilterConditionForField(criteria.getIsDeleted(), entityTable.column("is_deleted"));
            }

            if (criteria.getInvoiceId() != null) {
                builder.buildFilterConditionForField(criteria.getInvoiceId(), entityTable.column("invoice_id"));
            }

            // add conditions to the builder
            if (criteria.getSearch() != null){
                String[] columns = {"code"};
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
        if (subgroup1 != null)
            rs.and(subgroup1);
        return Tuples.of(rs, parameters);
    }
}
