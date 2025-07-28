package com.masi.logistics.repository;

import com.masi.logistics.domain.InventoriesCheckDetail;
import com.masi.logistics.domain.criteria.InventoriesCheckDetailCriteria;
import com.masi.logistics.repository.rowmapper.ColumnConverter;
import com.masi.logistics.repository.rowmapper.InventoriesCheckDetailRowMapper;
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
import org.springframework.data.relational.core.sql.Comparison;
import org.springframework.data.relational.core.sql.Condition;
import org.springframework.data.relational.core.sql.Conditions;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Select;
import org.springframework.data.relational.core.sql.SelectBuilder.SelectFromAndJoin;
import org.springframework.data.relational.core.sql.Table;
import org.springframework.data.relational.repository.support.MappingRelationalEntityInformation;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.r2dbc.core.RowsFetchSpec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.service.ConditionBuilder;

/**
 * Spring Data R2DBC custom repository implementation for the InventoriesCheckDetail entity.
 */
@SuppressWarnings("unused")
class InventoriesCheckDetailRepositoryInternalImpl
    extends SimpleR2dbcRepository<InventoriesCheckDetail, UUID>
    implements InventoriesCheckDetailRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final InventoriesCheckDetailRowMapper inventoriescheckdetailMapper;
    private final ColumnConverter columnConverter;

    private static final Table entityTable = Table.aliased("inventories_check_detail", EntityManager.ENTITY_ALIAS);

    public InventoriesCheckDetailRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        InventoriesCheckDetailRowMapper inventoriescheckdetailMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter,
        ColumnConverter columnConverter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(InventoriesCheckDetail.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.inventoriescheckdetailMapper = inventoriescheckdetailMapper;
        this.columnConverter = columnConverter;
    }

    @Override
    public Flux<InventoriesCheckDetail> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<InventoriesCheckDetail> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = InventoriesCheckDetailSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        SelectFromAndJoin selectFrom = Select.builder().select(columns).from(entityTable);
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, InventoriesCheckDetail.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<InventoriesCheckDetail> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<InventoriesCheckDetail> findById(UUID id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(StringUtils.wrap(id.toString(), "'")));
        return createQuery(null, whereClause).one();
    }

    private InventoriesCheckDetail process(Row row, RowMetadata metadata) {
        InventoriesCheckDetail entity = inventoriescheckdetailMapper.apply(row, "e");
        return entity;
    }

    @Override
    public <S extends InventoriesCheckDetail> Mono<S> save(S entity) {
        return super.save(entity);
    }

    @Override
    public Flux<InventoriesCheckDetail> findByCriteria(InventoriesCheckDetailCriteria inventoriesCheckDetailCriteria, Pageable page) {
        return createQuery(page, buildConditions(inventoriesCheckDetailCriteria)).all();
    }

    @Override
    public Mono<Long> countByCriteria(InventoriesCheckDetailCriteria criteria) {
        return findByCriteria(criteria, null)
            .collectList()
            .map(collectedList -> collectedList != null ? (long) collectedList.size() : (long) 0);
    }

    private Condition buildConditions(InventoriesCheckDetailCriteria criteria) {
        ConditionBuilder builder = new ConditionBuilder(this.columnConverter);
        List<Condition> allConditions = new ArrayList<Condition>();
        if (criteria != null) {
            if (criteria.getId() != null) {
                builder.buildFilterConditionForField(criteria.getId(), entityTable.column("id"));
            }
            if (criteria.getInventoriesCheckId() != null) {
                builder.buildFilterConditionForField(criteria.getInventoriesCheckId(), entityTable.column("inventories_check_id"));
            }
            if (criteria.getCode() != null) {
                builder.buildFilterConditionForField(criteria.getCode(), entityTable.column("code"));
            }
            if (criteria.getItemId() != null) {
                builder.buildFilterConditionForField(criteria.getItemId(), entityTable.column("item_id"));
            }
            if (criteria.getSystemQuantity() != null) {
                builder.buildFilterConditionForField(criteria.getSystemQuantity(), entityTable.column("system_quantity"));
            }
            if (criteria.getActualQuantity() != null) {
                builder.buildFilterConditionForField(criteria.getActualQuantity(), entityTable.column("actual_quantity"));
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
        return builder.buildConditions();
    }
}
