package com.masi.logistics.repository;

import com.masi.logistics.domain.InventoriesCheck;
import com.masi.logistics.domain.criteria.InventoriesCheckCriteria;
import com.masi.logistics.repository.rowmapper.ColumnConverter;
import com.masi.logistics.repository.rowmapper.InventoriesCheckRowMapper;
import com.masi.logistics.repository.rowmapper.WarehouseRowMapper;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

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
import tech.jhipster.service.ConditionBuilder;
import tech.jhipster.service.filter.BooleanFilter;

/**
 * Spring Data R2DBC custom repository implementation for the InventoriesCheck entity.
 */
@SuppressWarnings("unused")
class InventoriesCheckRepositoryInternalImpl
        extends SimpleR2dbcRepository<InventoriesCheck, UUID>
        implements InventoriesCheckRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;
    private final WarehouseRowMapper warehouseMapper;

    private final InventoriesCheckRowMapper inventoriescheckMapper;
    private final ColumnConverter columnConverter;

    private static final Table entityTable = Table.aliased("inventories_check", EntityManager.ENTITY_ALIAS);
    private static final Table wareHouseTable = Table.aliased("warehouse", "warehouse");

    public InventoriesCheckRepositoryInternalImpl(
            R2dbcEntityTemplate template,
            EntityManager entityManager,
            InventoriesCheckRowMapper inventoriescheckMapper,
            R2dbcEntityOperations entityOperations,
            R2dbcConverter converter, WarehouseRowMapper warehouseMapper,
            ColumnConverter columnConverter
    ) {
        super(
                new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(InventoriesCheck.class)),
                entityOperations,
                converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.inventoriescheckMapper = inventoriescheckMapper;
        this.warehouseMapper = warehouseMapper;
        this.columnConverter = columnConverter;
    }

    @Override
    public Flux<InventoriesCheck> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<InventoriesCheck> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = InventoriesCheckSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(WarehouseSqlHelper.getColumns(wareHouseTable, "warehouse"));


        SelectBuilder.SelectFromAndJoinCondition selectFrom = Select.builder()
                .select(columns)
                .from(entityTable)
                .leftOuterJoin(wareHouseTable)
                .on(Column.create("warehouse_id", entityTable))
                .equals(Column.create("id", wareHouseTable));

        String select = entityManager.createSelect(selectFrom, InventoriesCheck.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<InventoriesCheck> findAll() {
        return findAllBy(null);
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
    public Mono<InventoriesCheck> findById(UUID id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(StringUtils.wrap(id.toString(), "'")));
        return createQuery(null, whereClause).one();
    }

    private InventoriesCheck process(Row row, RowMetadata metadata) {
        InventoriesCheck entity = inventoriescheckMapper.apply(row, "e");
        entity.setWarehouse(warehouseMapper.apply(row, "warehouse"));
        return entity;
    }

    @Override
    public <S extends InventoriesCheck> Mono<S> save(S entity) {
        return super.save(entity);
    }

    @Override
    public Flux<InventoriesCheck> findByCriteria(InventoriesCheckCriteria inventoriesCheckCriteria, Pageable page) {
        page = getDefaultSort(page);
        return createQuery(page, buildConditions(inventoriesCheckCriteria)).all();
    }

    @Override
    public Mono<Long> countByCriteria(InventoriesCheckCriteria criteria) {
        return findByCriteria(criteria, null)
                .collectList()
                .map(collectedList -> collectedList != null ? (long) collectedList.size() : (long) 0);
    }

    private Condition buildConditions(InventoriesCheckCriteria criteria) {
        ConditionBuilder builder = new ConditionBuilder(this.columnConverter);
        List<Condition> allConditions = new ArrayList<Condition>();
        builder.buildFilterConditionForField(new BooleanFilter().setEquals(false), entityTable.column("is_deleted"));
        Condition subgroup1 = null;
        if (criteria != null) {
            if (criteria.getId() != null) {
                builder.buildFilterConditionForField(criteria.getId(), entityTable.column("id"));
            }

            if ((criteria.getCode() != null && !criteria.getCode().getContains().toString().equals(""))) {
                Condition group1 = Conditions.like(
                        Functions.lower(entityTable.column("code")),
                        SQL.literalOf("%" + criteria.getCode().getContains().toLowerCase() + "%")
                );
                Condition group2 = Conditions.like(
                        Functions.lower(entityTable.column("note")),
                        SQL.literalOf("%" + criteria.getCode().getContains().toLowerCase() + "%")
                );
                if (criteria.getEmployeeIds() != null) {
                    Object[] employeeIdsArray = criteria.getEmployeeIds().toArray();
                    String sqlInCondition = criteria.getEmployeeIds().stream()
                            .map(id -> "" + id)
                            .collect(Collectors.joining());
                    Condition group3 = entityTable.column("created_by").in(SQL.literalOf(sqlInCondition));

                    subgroup1 = Conditions.nest(group1.or(group2).or(group3));
                    builder.buildConditions().and(subgroup1);
                } else {
                    subgroup1 = Conditions.nest(group1.or(group2));
                    builder.buildConditions().and(subgroup1);
                }
            }

            if (criteria.getCheckDate() != null) {
                builder.buildFilterConditionForField(criteria.getCheckDate(), entityTable.column("check_date"));
            }
            if (criteria.getWarehouse() != null) {
                builder.buildFilterConditionForField(criteria.getWarehouse(), entityTable.column("warehouse"));
            }
            if (criteria.getApprover1() != null) {
                builder.buildFilterConditionForField(criteria.getApprover1(), entityTable.column("approver_1"));
            }
            if (criteria.getApprover2() != null) {
                builder.buildFilterConditionForField(criteria.getApprover2(), entityTable.column("approver_2"));
            }
            if (criteria.getApprover3() != null) {
                builder.buildFilterConditionForField(criteria.getApprover3(), entityTable.column("approver_3"));
            }
            if (criteria.getStatus() != null) {
                builder.buildFilterConditionForField(criteria.getStatus(), entityTable.column("status"));
            }
            if (criteria.getAttribute() != null) {
                builder.buildFilterConditionForField(criteria.getAttribute(), entityTable.column("attribute"));
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
        if (subgroup1 == null)
            return builder.buildConditions();
        return builder.buildConditions().and(subgroup1);
    }
}
