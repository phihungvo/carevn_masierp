package com.masi.logistics.repository;

import com.masi.logistics.domain.Suppliers;
import com.masi.logistics.repository.rowmapper.SupplierGroupRowMapper;
import com.masi.logistics.repository.rowmapper.SupplierTypeRowMapper;
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
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Comparison;
import org.springframework.data.relational.core.sql.Condition;
import org.springframework.data.relational.core.sql.Conditions;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Select;
import org.springframework.data.relational.core.sql.SelectBuilder.SelectFromAndJoinCondition;
import org.springframework.data.relational.core.sql.Table;
import org.springframework.data.relational.repository.support.MappingRelationalEntityInformation;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.r2dbc.core.RowsFetchSpec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

/**
 * Spring Data R2DBC custom repository implementation for the Suppliers entity.
 */
@SuppressWarnings("unused")
class SuppliersRepositoryInternalImpl extends SimpleR2dbcRepository<Suppliers, UUID> implements SuppliersRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final SupplierGroupRowMapper suppliergroupMapper;
    private final SuppliersRowMapper suppliersMapper;
    private final SupplierTypeRowMapper supplierTypeMapper;

    private static final Table entityTable = Table.aliased("suppliers", EntityManager.ENTITY_ALIAS);
    private static final Table supplierGroupTable = Table.aliased("supplier_group", "supplierGroup");
    private static final Table supplierTypeTable = Table.aliased("supplier_type", "supplierType");

    public SuppliersRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        SupplierGroupRowMapper suppliergroupMapper,
        SuppliersRowMapper suppliersMapper,
        SupplierTypeRowMapper supplierTypeMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(Suppliers.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.suppliergroupMapper = suppliergroupMapper;
        this.suppliersMapper = suppliersMapper;
        this.supplierTypeMapper = supplierTypeMapper;
    }

    @Override
    public Flux<Suppliers> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    private Pageable getDefaultSort(Pageable pageable) {
        Sort defaultSort = Sort.by(Sort.Direction.DESC, "create_at");
        if (Objects.isNull(pageable)) {
            pageable = PageRequest.of(0, Integer.MAX_VALUE, defaultSort);
        }
        if (pageable.getSort().equals(Sort.unsorted())) {
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), defaultSort);
        }
        return pageable;
    }

    @Override
    public Flux<Suppliers> findAllBy(Pageable pageable, String search, Boolean status, String company) {
        var whereClause = createSearchCondition(search, status, company);
        return createQueryCustom(pageable, whereClause.getT1(), whereClause.getT2()).all();
    }

    private Tuple2<Condition, HashMap<String, Object>> createSearchCondition(String search, Boolean status, String company) {
        Condition whereClause = Conditions.isNull(entityTable.column("delete_at"));
        whereClause = whereClause.and(Conditions.isNull(entityTable.column("delete_by")));
        whereClause = whereClause.and(Conditions.isEqual(entityTable.column("company"), Conditions.just(StringUtils.wrap(company, "'"))));
        var params = new HashMap<String, Object>();
        if (search != null && !search.isEmpty()) {
            var nameColumn = EntityManager.ENTITY_ALIAS + "." + "name";
            var codeColumn = EntityManager.ENTITY_ALIAS + "." + "code";
            String inCondition = "(unaccent(" + nameColumn + ") iLIKE unaccent(:search) OR unaccent(" + codeColumn + ") iLIKE unaccent(:search))";
            whereClause = whereClause.and(Conditions.just(inCondition));
            params.put("search", "%" + search.trim() + "%");
        }

        if (status != null) {
            whereClause = whereClause.and(Conditions.isEqual(entityTable.column("is_active"), Conditions.just(String.valueOf(status))));
        }
        return Tuples.of(whereClause, params);
    }

    @Override
    public Mono<Long> countByCriteria(String search, Boolean status, String company) {
        var whereClause = createSearchCondition(search, status, company);
        return createQueryCustom(null, whereClause.getT1(), whereClause.getT2()).all().count();
    }


    RowsFetchSpec<Suppliers> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = SuppliersSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(SupplierGroupSqlHelper.getColumns(supplierGroupTable, "supplierGroup"));
        columns.addAll(SupplierTypeSqlHelper.getColumns(supplierTypeTable, "supplierType"));
        SelectFromAndJoinCondition selectFrom = Select.builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(supplierGroupTable)
            .on(Column.create("supplier_group_id", entityTable))
            .equals(Column.create("id", supplierGroupTable))
            .leftOuterJoin(supplierTypeTable)
            .on(Column.create("supplier_type_id", entityTable))
            .equals(Column.create("id", supplierTypeTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, Suppliers.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    RowsFetchSpec<Suppliers> createQueryCustom(Pageable pageable, Condition whereClause, Map<String, Object> params) {
        List<Expression> columns = SuppliersSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(SupplierGroupSqlHelper.getColumns(supplierGroupTable, "supplierGroup"));
        columns.addAll(SupplierTypeSqlHelper.getColumns(supplierTypeTable, "supplierType"));
        SelectFromAndJoinCondition selectFrom = Select.builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(supplierGroupTable)
            .on(Column.create("supplier_group_id", entityTable))
            .equals(Column.create("id", supplierGroupTable))
            .leftOuterJoin(supplierTypeTable)
            .on(Column.create("supplier_type_id", entityTable))
            .equals(Column.create("id", supplierTypeTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, Suppliers.class, pageable, whereClause);
        var query = db.sql(select);
        if (params != null) {
            query = query.bindValues(params);
        }
        return query.map(this::process);
    }

    @Override
    public Flux<Suppliers> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<Suppliers> findById(UUID id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(StringUtils.wrap(id.toString(), "'")));
        return createQuery(null, whereClause).one();
    }

    private Suppliers process(Row row, RowMetadata metadata) {
        Suppliers entity = suppliersMapper.apply(row, "e");
        entity.setSupplierGroup(suppliergroupMapper.apply(row, "supplierGroup"));
        entity.setSupplierType(supplierTypeMapper.apply(row, "supplierType"));
        return entity;
    }

    @Override
    public <S extends Suppliers> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
