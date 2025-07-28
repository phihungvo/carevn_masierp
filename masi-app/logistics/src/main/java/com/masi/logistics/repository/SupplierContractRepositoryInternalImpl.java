package com.masi.logistics.repository;

import com.masi.logistics.domain.SupplierContract;
import com.masi.logistics.domain.criteria.SupplierContractCriteria;
import com.masi.logistics.repository.rowmapper.ColumnConverter;
import com.masi.logistics.repository.rowmapper.SupplierContractRowMapper;
import com.masi.logistics.repository.rowmapper.SuppliersRowMapper;
import com.masi.logistics.repository.rowmapper.SuppliesRequestRowMapper;
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
 * Spring Data R2DBC custom repository implementation for the SupplierContract entity.
 */
@SuppressWarnings("unused")
class SupplierContractRepositoryInternalImpl
    extends SimpleR2dbcRepository<SupplierContract, UUID>
    implements SupplierContractRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final SupplierContractRowMapper suppliercontractMapper;
    private final SuppliersRowMapper suppliersMapper;
    private final SuppliesRequestRowMapper suppliesRequestMapper;
    private final ColumnConverter columnConverter;

    private static final Table entityTable = Table.aliased("supplier_contract", EntityManager.ENTITY_ALIAS);
    private static final Table supplierTable = Table.aliased("suppliers", "supplier");
    private static final Table suppliesRequestTable = Table.aliased("supplies_request", "suppliesRequest");

    public SupplierContractRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        SupplierContractRowMapper suppliercontractMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter, SuppliersRowMapper suppliersMapper, SuppliesRequestRowMapper suppliesRequestMapper,
        ColumnConverter columnConverter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(SupplierContract.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.suppliercontractMapper = suppliercontractMapper;
        this.suppliersMapper = suppliersMapper;
        this.suppliesRequestMapper = suppliesRequestMapper;
        this.columnConverter = columnConverter;
    }

    @Override
    public Flux<SupplierContract> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<SupplierContract> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = SupplierContractSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(SuppliersSqlHelper.getColumns(supplierTable, "supplier"));
        columns.addAll(SuppliesRequestSqlHelper.getColumns(suppliesRequestTable, "suppliesRequest"));
        SelectBuilder.SelectFromAndJoinCondition selectFrom = Select.builder().select(columns).from(entityTable)
            .leftOuterJoin(supplierTable).on(Column.create("supplier_id", entityTable)).equals(Column.create("id", supplierTable))
            .leftOuterJoin(suppliesRequestTable).on(Column.create("supplies_request_id", entityTable)).equals(Column.create("id", suppliesRequestTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, SupplierContract.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    RowsFetchSpec<SupplierContract> createQueryCustom(Pageable pageable, Condition whereClause, Map<String, Object> parameters) {
        List<Expression> columns = SupplierContractSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(SuppliersSqlHelper.getColumns(supplierTable, "supplier"));
        columns.addAll(SuppliesRequestSqlHelper.getColumns(suppliesRequestTable, "suppliesRequest"));
        SelectBuilder.SelectFromAndJoinCondition selectFrom = Select.builder().select(columns).from(entityTable)
            .leftOuterJoin(supplierTable).on(Column.create("supplier_id", entityTable)).equals(Column.create("id", supplierTable))
            .leftOuterJoin(suppliesRequestTable).on(Column.create("supplies_request_id", entityTable)).equals(Column.create("id", suppliesRequestTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, SupplierContract.class, pageable, whereClause);
        var prepared = db.sql(select);
        if (parameters != null) {
            prepared = prepared.bindValues(parameters);
        }
        return prepared.map(this::process);
    }

    @Override
    public Flux<SupplierContract> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<SupplierContract> findById(UUID id, String company) {
        Condition whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(StringUtils.wrap(id.toString(), "'")));
        whereClause = whereClause.and(Conditions.isNull(entityTable.column("deleted_at")));
        whereClause = whereClause.and(Conditions.isNull(entityTable.column("deleted_by")));
        whereClause = whereClause.and(Conditions.isEqual(entityTable.column("is_deleted"), Conditions.just("false")));
        whereClause = whereClause.and(Conditions.isEqual(entityTable.column("company"), Conditions.just(StringUtils.wrap(company, "'"))));
        return createQuery(null, whereClause).one();
    }

    private SupplierContract process(Row row, RowMetadata metadata) {
        SupplierContract entity = suppliercontractMapper.apply(row, "e");
        entity.setSupplier(suppliersMapper.apply(row, "supplier"));
        entity.setSuppliesRequest(suppliesRequestMapper.apply(row, "suppliesRequest"));
        return entity;
    }

    @Override
    public <S extends SupplierContract> Mono<S> save(S entity) {
        return super.save(entity);
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
    public Flux<SupplierContract> findByCriteria(SupplierContractCriteria supplierContractCriteria, Pageable page) {

        page = getDefaultSort(page);
        var tuples = buildConditions(supplierContractCriteria);
        return createQueryCustom(page, tuples.getT1(), tuples.getT2()).all();
    }

    @Override
    public Mono<Long> countByCriteria(SupplierContractCriteria criteria) {
        return findByCriteria(criteria, null)
            .collectList()
            .map(collectedList -> collectedList != null ? (long) collectedList.size() : (long) 0);
    }

    private Tuple2<Condition, Map<String, Object>> buildConditions(SupplierContractCriteria criteria) {
        ConditionBuilder builder = new ConditionBuilder(this.columnConverter);
        Map<String, Object> parameters = new HashMap<>();
        List<Condition> allConditions = new ArrayList<Condition>();
        if (criteria != null) {
            if (criteria.getId() != null) {
                builder.buildFilterConditionForField(criteria.getId(), entityTable.column("id"));
            }
            if (criteria.getContractCode() != null) {
                builder.buildFilterConditionForField(criteria.getContractCode(), entityTable.column("contract_code"));
            }
            if (criteria.getContractName() != null) {
                builder.buildFilterConditionForField(criteria.getContractName(), entityTable.column("contract_name"));
            }
            if (criteria.getSupplierId() != null) {
                builder.buildFilterConditionForField(criteria.getSupplierId(), entityTable.column("supplier_id"));
            }
            if (criteria.getContractDate() != null) {
                builder.buildFilterConditionForField(criteria.getContractDate(), entityTable.column("contract_date"));
            }
            if (criteria.getEndDate() != null) {
                builder.buildFilterConditionForField(criteria.getEndDate(), entityTable.column("end_date"));
            }


            if (criteria.getNote() != null) {
                builder.buildFilterConditionForField(criteria.getNote(), entityTable.column("note"));
            }
            if (criteria.getAttachments() != null) {
                builder.buildFilterConditionForField(criteria.getAttachments(), entityTable.column("attachments"));
            }
            if (criteria.getStatus() != null) {
                builder.buildFilterConditionForField(criteria.getStatus(), entityTable.column("status"));
            }
            if (criteria.getCompany() != null) {
                builder.buildFilterConditionForField(criteria.getCompany(), entityTable.column("company"));
            }
            if (criteria.getDepartment() != null) {
                builder.buildFilterConditionForField(criteria.getDepartment(), entityTable.column("department"));
            }
            if (criteria.getCreatedBy() != null) {
                builder.buildFilterConditionForField(criteria.getCreatedBy(), entityTable.column("created_by"));
            }
            if (criteria.getCreatedAt() != null) {
                builder.buildFilterConditionForField(criteria.getCreatedAt(), entityTable.column("created_at"));
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

            if (criteria.getSuppliesRequestId() != null) {
                builder.buildFilterConditionForField(criteria.getSuppliesRequestId(), entityTable.column("supplies_request_id"));
            }

            if (criteria.getSearch() != null && !criteria.getSearch().isEmpty()) {
                String[] columns = {"supplier_full_name", "contract_code", "note"};
                String[] aliases = Arrays.stream(columns).map(column -> "e." + column).toArray(String[]::new);
                StringBuilder search = new StringBuilder();
                search.append('(');
                Arrays.stream(aliases).forEach(alias -> {
                    search.append(String.format("unaccent(%s) ilike unaccent(:search) or ", alias));
                });
                search.append("unaccent(supplier.name) ilike unaccent(:search) or");
                int searchLength = search.length();
                search.delete(searchLength - 3, searchLength);
                search.append(')');
                Condition searchCondition = Conditions.just(search.toString());
                allConditions.add(searchCondition);
                parameters.put("search", "%" + criteria.getSearch() + "%");
            }
            if (criteria.getStartDate() != null) {
                builder.buildFilterConditionForField(criteria.getStartDate(), entityTable.column("start_date"));
            }
        }

        var c = builder.buildConditions();
        if (c != null) {
            allConditions.add(c);
        }
        Condition defaultCondition = Conditions.just("1=1"); // Default condition that is always true
        var rs = allConditions.stream().reduce(Condition::and).orElse(defaultCondition);

        return Tuples.of(rs, parameters);
    }
}
