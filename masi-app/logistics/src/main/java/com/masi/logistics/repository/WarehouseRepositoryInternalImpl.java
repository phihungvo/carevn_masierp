package com.masi.logistics.repository;

import com.masi.logistics.domain.Warehouse;
import com.masi.logistics.domain.criteria.WarehouseCriteria;
import com.masi.logistics.domain.enumeration.WarehouseTypePage;
import com.masi.logistics.repository.rowmapper.ColumnConverter;
import com.masi.logistics.repository.rowmapper.WarehouseRowMapper;
import com.masi.logistics.repository.rowmapper.WarehouseTypeRowMapper;
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
import tech.jhipster.service.filter.BooleanFilter;

/**
 * Spring Data R2DBC custom repository implementation for the Warehouse entity.
 */
@SuppressWarnings("unused")
class WarehouseRepositoryInternalImpl extends SimpleR2dbcRepository<Warehouse, UUID> implements WarehouseRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final WarehouseTypeRowMapper warehousetypeMapper;
    private final WarehouseRowMapper warehouseMapper;
    private final ColumnConverter columnConverter;

    private static final Table entityTable = Table.aliased("warehouse", EntityManager.ENTITY_ALIAS);
    private static final Table warehouseTypeTable = Table.aliased("warehouse_type", "warehouseType");

    public WarehouseRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        WarehouseTypeRowMapper warehousetypeMapper,
        WarehouseRowMapper warehouseMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter,
        ColumnConverter columnConverter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(Warehouse.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.warehousetypeMapper = warehousetypeMapper;
        this.warehouseMapper = warehouseMapper;
        this.columnConverter = columnConverter;
    }

    @Override
    public Flux<Warehouse> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<Warehouse> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = WarehouseSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(WarehouseTypeSqlHelper.getColumns(warehouseTypeTable, "warehouseType"));
        SelectFromAndJoinCondition selectFrom = Select.builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(warehouseTypeTable)
            .on(Column.create("warehouse_type_id", entityTable))
            .equals(Column.create("id", warehouseTypeTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, Warehouse.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<Warehouse> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<Warehouse> findById(UUID id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(StringUtils.wrap(id.toString(), "'")));
        return createQuery(null, whereClause).one();
    }

    private Warehouse process(Row row, RowMetadata metadata) {
        Warehouse entity = warehouseMapper.apply(row, "e");
        entity.setWarehouseType(warehousetypeMapper.apply(row, "warehouseType"));
        return entity;
    }

    @Override
    public <S extends Warehouse> Mono<S> save(S entity) {
        return super.save(entity);
    }

    @Override
    public Flux<Warehouse> findByCriteria(WarehouseCriteria warehouseCriteria, Pageable page) {
        return createQuery(page, buildConditions(warehouseCriteria)).all();
    }

    @Override
    public Mono<Long> countByCriteria(WarehouseCriteria criteria) {
        return findByCriteria(criteria, null)
            .collectList()
            .map(collectedList -> collectedList != null ? (long) collectedList.size() : (long) 0);
    }

    private Condition buildConditions(WarehouseCriteria criteria) {
        ConditionBuilder builder = new ConditionBuilder(this.columnConverter);
        builder.buildFilterConditionForField(new BooleanFilter().setEquals(true), entityTable.column("active"));
        List<Condition> allConditions = new ArrayList<>();
        if (criteria != null) {
            if (criteria.getId() != null) {
                builder.buildFilterConditionForField(criteria.getId(), entityTable.column("id"));
            }
            if (criteria.getCode() != null) {
                builder.buildFilterConditionForField(criteria.getCode(), entityTable.column("code"));
            }
            if (criteria.getName() != null) {
                builder.buildFilterConditionForField(criteria.getName(), entityTable.column("name"));
            }
            if (criteria.getAddress() != null) {
                builder.buildFilterConditionForField(criteria.getAddress(), entityTable.column("address"));
            }
            if (criteria.getWarehouseTypePage() != null) {
                builder.buildFilterConditionForField(criteria.getWarehouseTypePage(), entityTable.column("warehouse_type_page"));
            }
            if (criteria.getActive() != null) {
                builder.buildFilterConditionForField(criteria.getActive(), entityTable.column("active"));
            }
            if (criteria.getCreateAt() != null) {
                builder.buildFilterConditionForField(criteria.getCreateAt(), entityTable.column("create_at"));
            }
            if (criteria.getCreateBy() != null) {
                builder.buildFilterConditionForField(criteria.getCreateBy(), entityTable.column("create_by"));
            }
            if (criteria.getUpdateAt() != null) {
                builder.buildFilterConditionForField(criteria.getUpdateAt(), entityTable.column("update_at"));
            }
            if (criteria.getUpdateBy() != null) {
                builder.buildFilterConditionForField(criteria.getUpdateBy(), entityTable.column("update_by"));
            }
            if (criteria.getDeleteAt() != null) {
                builder.buildFilterConditionForField(criteria.getDeleteAt(), entityTable.column("delete_at"));
            }
            if (criteria.getDeleteBy() != null) {
                builder.buildFilterConditionForField(criteria.getDeleteBy(), entityTable.column("delete_by"));
            }
            if (criteria.getCompany() != null) {
                builder.buildFilterConditionForField(criteria.getCompany(), entityTable.column("company"));
            }
            if (criteria.getWarehouseTypeId() != null) {
                builder.buildFilterConditionForField(criteria.getWarehouseTypeId(), warehouseTypeTable.column("id"));
            }
            if (criteria.getIsNotGetWareHouseUniform() != null){
                if (!criteria.getIsNotGetWareHouseUniform()){
                    allConditions.add(entityTable.column("warehouse_type_page").isEqualTo(Conditions.just(StringUtils.wrap(WarehouseTypePage.UNIFORM_WAREHOUSE.name(), "'"))));
                } else {
                    allConditions.add(entityTable.column("warehouse_type_page").isNotEqualTo(Conditions.just(StringUtils.wrap(WarehouseTypePage.UNIFORM_WAREHOUSE.name(), "'"))));
                }
            } else {
                allConditions.add(entityTable.column("warehouse_type_page").isNotEqualTo(Conditions.just(StringUtils.wrap(WarehouseTypePage.UNIFORM_WAREHOUSE.name(), "'"))));
            }

        }
        allConditions.add(builder.buildConditions());
        return allConditions.stream().reduce(Condition::and).orElse(null);
    }
}
