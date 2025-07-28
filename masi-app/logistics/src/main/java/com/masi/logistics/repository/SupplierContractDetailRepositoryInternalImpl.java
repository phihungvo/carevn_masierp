package com.masi.logistics.repository;

import com.masi.logistics.domain.SupplierContractDetail;
import com.masi.logistics.domain.criteria.SupplierContractDetailCriteria;
import com.masi.logistics.repository.rowmapper.ColumnConverter;
import com.masi.logistics.repository.rowmapper.ItemRowMapper;
import com.masi.logistics.repository.rowmapper.SupplierContractDetailRowMapper;
import com.masi.logistics.repository.rowmapper.SupplierContractRowMapper;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
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
import tech.jhipster.service.ConditionBuilder;

/**
 * Spring Data R2DBC custom repository implementation for the SupplierContractDetail entity.
 */
@SuppressWarnings("unused")
class SupplierContractDetailRepositoryInternalImpl
    extends SimpleR2dbcRepository<SupplierContractDetail, UUID>
    implements SupplierContractDetailRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final SupplierContractRowMapper suppliercontractMapper;
    private final SupplierContractDetailRowMapper suppliercontractdetailMapper;
    private final ItemRowMapper itemMapper;
    private final ColumnConverter columnConverter;

    private static final Table entityTable = Table.aliased("supplier_contract_detail", EntityManager.ENTITY_ALIAS);
    private static final Table supplierContractTable = Table.aliased("supplier_contract", "supplierContract");
    private static final Table itemTable = Table.aliased("item", "item");

    public SupplierContractDetailRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        SupplierContractRowMapper suppliercontractMapper,
        SupplierContractDetailRowMapper suppliercontractdetailMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter, ItemRowMapper itemMapper,
        ColumnConverter columnConverter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(SupplierContractDetail.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.suppliercontractMapper = suppliercontractMapper;
        this.suppliercontractdetailMapper = suppliercontractdetailMapper;
        this.itemMapper = itemMapper;
        this.columnConverter = columnConverter;
    }

    @Override
    public Flux<SupplierContractDetail> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<SupplierContractDetail> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = SupplierContractDetailSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(SupplierContractSqlHelper.getColumns(supplierContractTable, "supplierContract"));
        columns.addAll(ItemSqlHelper.getColumns(itemTable, "item"));
        SelectFromAndJoinCondition selectFrom = Select.builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(supplierContractTable)
            .on(Column.create("supplier_contract_id", entityTable))
            .equals(Column.create("id", supplierContractTable))
            .leftOuterJoin(itemTable)
            .on(Column.create("supply_item_id", entityTable))
            .equals(Column.create("id", itemTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, SupplierContractDetail.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<SupplierContractDetail> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<SupplierContractDetail> findById(UUID id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(StringUtils.wrap(id.toString(), "'")));
        return createQuery(null, whereClause).one();
    }

    private SupplierContractDetail process(Row row, RowMetadata metadata) {
        SupplierContractDetail entity = suppliercontractdetailMapper.apply(row, "e");
        entity.setSupplierContract(suppliercontractMapper.apply(row, "supplierContract"));
        entity.setItem(itemMapper.apply(row, "item"));
        return entity;
    }

    @Override
    public <S extends SupplierContractDetail> Mono<S> save(S entity) {
        return super.save(entity);
    }

    @Override
    public Flux<SupplierContractDetail> findByCriteria(SupplierContractDetailCriteria supplierContractDetailCriteria, Pageable page) {
        return createQuery(page, buildConditions(supplierContractDetailCriteria)).all();
    }

    @Override
    public Flux<SupplierContractDetail> findByListSupplierContractId(List<UUID> uuids, String company, Pageable pageable) {
        if (uuids == null || uuids.isEmpty()) {
            return Flux.empty();
        }
        Condition whereClause = Conditions.isNull(entityTable.column("deleted_by"));
        whereClause = whereClause.and(Conditions.isEqual(entityTable.column("company"), Conditions.just(StringUtils.wrap(company, "'"))));
        var inClause = uuids.stream().map(id -> String.format("'%s'", id.toString())).reduce((a, b) -> a + "," + b).orElseThrow();
        whereClause = whereClause.and(Conditions.in(entityTable.column("supplier_contract_id"), Conditions.just(inClause)));
        whereClause = whereClause.and(Conditions.isEqual(entityTable.column("is_deleted"), Conditions.just("false")));
        return createQuery(pageable, whereClause).all();
    }

    @Override
    public Mono<Long> countByCriteria(SupplierContractDetailCriteria criteria) {
        return findByCriteria(criteria, null)
            .collectList()
            .map(collectedList -> collectedList != null ? (long) collectedList.size() : (long) 0);
    }

    private Condition buildConditions(SupplierContractDetailCriteria criteria) {
        ConditionBuilder builder = new ConditionBuilder(this.columnConverter);
        List<Condition> allConditions = new ArrayList<Condition>();
        if (criteria != null) {
            if (criteria.getId() != null) {
                builder.buildFilterConditionForField(criteria.getId(), entityTable.column("id"));
            }
            if (criteria.getContractId() != null) {
                builder.buildFilterConditionForField(criteria.getContractId(), entityTable.column("contract_id"));
            }
            if (criteria.getSupplyItemId() != null) {
                builder.buildFilterConditionForField(criteria.getSupplyItemId(), entityTable.column("supply_item_id"));
            }
            if (criteria.getUnitId() != null) {
                builder.buildFilterConditionForField(criteria.getUnitId(), entityTable.column("unit_id"));
            }
            if (criteria.getQuantity() != null) {
                builder.buildFilterConditionForField(criteria.getQuantity(), entityTable.column("quantity"));
            }
            if (criteria.getNote() != null) {
                builder.buildFilterConditionForField(criteria.getNote(), entityTable.column("note"));
            }
            if (criteria.getSupplierContractId() != null) {
                builder.buildFilterConditionForField(criteria.getSupplierContractId(), supplierContractTable.column("id"));
            }
            if (criteria.getIsDeleted() != null) {
                builder.buildFilterConditionForField(criteria.getIsDeleted(), entityTable.column("is_deleted"));
            }
        }
        return builder.buildConditions();
    }
}
