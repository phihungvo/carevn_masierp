package com.masi.logistics.repository;

import com.masi.logistics.domain.InventoriesDetail;
import com.masi.logistics.domain.criteria.InventoriesDetailCriteria;
import com.masi.logistics.repository.rowmapper.ColumnConverter;
import com.masi.logistics.repository.rowmapper.InventoriesDetailRowMapper;
import com.masi.logistics.repository.rowmapper.ItemRowMapper;
import com.masi.logistics.repository.rowmapper.UomRowMapper;
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
import tech.jhipster.service.ConditionBuilder;

/**
 * Spring Data R2DBC custom repository implementation for the InventoriesDetail entity.
 */
@SuppressWarnings("unused")
class InventoriesDetailRepositoryInternalImpl
    extends SimpleR2dbcRepository<InventoriesDetail, UUID>
    implements InventoriesDetailRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final InventoriesDetailRowMapper inventoriesdetailMapper;
    private final ItemRowMapper itemMapper;
    private final UomRowMapper uomMapper;
    private final ColumnConverter columnConverter;

    private static final Table entityTable = Table.aliased("inventories_detail", EntityManager.ENTITY_ALIAS);
    private static final Table itemTable = Table.aliased("item", "item");
    private static final Table uomTable = Table.aliased("uom", "uom");

    public InventoriesDetailRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        InventoriesDetailRowMapper inventoriesdetailMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter, ItemRowMapper itemMapper, UomRowMapper uomMapper,
        ColumnConverter columnConverter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(InventoriesDetail.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.inventoriesdetailMapper = inventoriesdetailMapper;
        this.itemMapper = itemMapper;
        this.uomMapper = uomMapper;
        this.columnConverter = columnConverter;
    }

    @Override
    public Flux<InventoriesDetail> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<InventoriesDetail> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = InventoriesDetailSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(ItemSqlHelper.getColumns(itemTable, "item"));
        columns.addAll(UomSqlHelper.getColumns(uomTable, "uom"));
        SelectBuilder.SelectFromAndJoinCondition selectFrom = Select.builder().select(columns).from(entityTable)
            .leftOuterJoin(itemTable)
            .on(entityTable.column("item_id"))
            .equals(itemTable.column("id"))
            .leftOuterJoin(uomTable)
            .on(itemTable.column("uom_id"))
            .equals(uomTable.column("id"));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, InventoriesDetail.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<InventoriesDetail> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<InventoriesDetail> findById(UUID id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(StringUtils.wrap(id.toString(), "'")));
        return createQuery(null, whereClause).one();
    }

    private InventoriesDetail process(Row row, RowMetadata metadata) {
        InventoriesDetail entity = inventoriesdetailMapper.apply(row, "e");
        entity.setItem(itemMapper.apply(row, "item"));
        entity.getItem().setUom(uomMapper.apply(row, "uom"));
        return entity;
    }

    @Override
    public <S extends InventoriesDetail> Mono<S> save(S entity) {
        return super.save(entity);
    }

    @Override
    public Flux<InventoriesDetail> findByCriteria(InventoriesDetailCriteria inventoriesDetailCriteria, Pageable page) {
        page = getDefaultSort(page);
        return createQuery(page, buildConditions(inventoriesDetailCriteria)).all();
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
    public Mono<Long> countByCriteria(InventoriesDetailCriteria criteria) {
        return findByCriteria(criteria, null)
            .collectList()
            .map(collectedList -> collectedList != null ? (long) collectedList.size() : (long) 0);
    }

    private Condition buildConditions(InventoriesDetailCriteria criteria) {
        ConditionBuilder builder = new ConditionBuilder(this.columnConverter);
        Condition subgroup1 = null;

        if (criteria != null) {
            if (criteria.getId() != null) {
                builder.buildFilterConditionForField(criteria.getId(), entityTable.column("id"));
            }
            if ((criteria.getCode() != null && !criteria.getCode().getContains().toString().equals(""))) {
                Condition group1 = Conditions.like(entityTable.column("code"), SQL.literalOf("%" + criteria.getCode().getContains() + "%"));
                subgroup1 = Conditions.nest(group1);
                builder.buildConditions().and(subgroup1);
            }
            if (criteria.getItemId() != null) {
                builder.buildFilterConditionForField(criteria.getItemId(), entityTable.column("item_id"));
            }
            if (criteria.getInventoriesId() != null) {
                builder.buildFilterConditionForField(criteria.getInventoriesId(), entityTable.column("inventories_id"));
            }
            if (criteria.getQuantity() != null) {
                builder.buildFilterConditionForField(criteria.getQuantity(), entityTable.column("quantity"));
            }
            if (criteria.getPrice() != null) {
                builder.buildFilterConditionForField(criteria.getPrice(), entityTable.column("price"));
            }
            if (criteria.getTotalPrice() != null) {
                builder.buildFilterConditionForField(criteria.getTotalPrice(), entityTable.column("total_price"));
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
