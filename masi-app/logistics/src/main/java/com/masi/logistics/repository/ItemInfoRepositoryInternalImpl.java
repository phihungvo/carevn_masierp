package com.masi.logistics.repository;

import com.masi.logistics.domain.ItemInfo;
import com.masi.logistics.domain.criteria.ItemInfoCriteria;
import com.masi.logistics.repository.rowmapper.ColumnConverter;
import com.masi.logistics.repository.rowmapper.ItemInfoRowMapper;
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
 * Spring Data R2DBC custom repository implementation for the ItemInfo entity.
 */
@SuppressWarnings("unused")
class ItemInfoRepositoryInternalImpl extends SimpleR2dbcRepository<ItemInfo, UUID> implements ItemInfoRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final ItemInfoRowMapper iteminfoMapper;
    private final ColumnConverter columnConverter;

    private static final Table entityTable = Table.aliased("item_info", EntityManager.ENTITY_ALIAS);

    public ItemInfoRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        ItemInfoRowMapper iteminfoMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter,
        ColumnConverter columnConverter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(ItemInfo.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.iteminfoMapper = iteminfoMapper;
        this.columnConverter = columnConverter;
    }

    @Override
    public Flux<ItemInfo> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<ItemInfo> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = ItemInfoSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        SelectFromAndJoin selectFrom = Select.builder().select(columns).from(entityTable);
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, ItemInfo.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<ItemInfo> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<ItemInfo> findById(UUID id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(StringUtils.wrap(id.toString(), "'")));
        return createQuery(null, whereClause).one();
    }

    private ItemInfo process(Row row, RowMetadata metadata) {
        ItemInfo entity = iteminfoMapper.apply(row, "e");
        return entity;
    }

    @Override
    public <S extends ItemInfo> Mono<S> save(S entity) {
        return super.save(entity);
    }

    @Override
    public Flux<ItemInfo> findByCriteria(ItemInfoCriteria itemInfoCriteria, Pageable page) {
        return createQuery(page, buildConditions(itemInfoCriteria)).all();
    }

    @Override
    public Mono<Long> countByCriteria(ItemInfoCriteria criteria) {
        return findByCriteria(criteria, null)
            .collectList()
            .map(collectedList -> collectedList != null ? (long) collectedList.size() : (long) 0);
    }

    private Condition buildConditions(ItemInfoCriteria criteria) {
        ConditionBuilder builder = new ConditionBuilder(this.columnConverter);
        List<Condition> allConditions = new ArrayList<Condition>();
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
            if (criteria.getRegistrationNumber() != null) {
                builder.buildFilterConditionForField(criteria.getRegistrationNumber(), entityTable.column("registration_number"));
            }
            if (criteria.getRegistrationDate() != null) {
                builder.buildFilterConditionForField(criteria.getRegistrationDate(), entityTable.column("registration_date"));
            }
            if (criteria.getHandoverNumber() != null) {
                builder.buildFilterConditionForField(criteria.getHandoverNumber(), entityTable.column("handover_number"));
            }
            if (criteria.getHandoverDate() != null) {
                builder.buildFilterConditionForField(criteria.getHandoverDate(), entityTable.column("handover_date"));
            }
            if (criteria.getHandoverBy() != null) {
                builder.buildFilterConditionForField(criteria.getHandoverBy(), entityTable.column("handover_by"));
            }
            if (criteria.getUserId() != null) {
                builder.buildFilterConditionForField(criteria.getUserId(), entityTable.column("user_id"));
            }
            if (criteria.getUserPosition() != null) {
                builder.buildFilterConditionForField(criteria.getUserPosition(), entityTable.column("user_position"));
            }
            if (criteria.getSeriesNumber() != null) {
                builder.buildFilterConditionForField(criteria.getSeriesNumber(), entityTable.column("series_number"));
            }
            if (criteria.getUsageDate() != null) {
                builder.buildFilterConditionForField(criteria.getUsageDate(), entityTable.column("usage_date"));
            }
            if (criteria.getInvoiceNumber() != null) {
                builder.buildFilterConditionForField(criteria.getInvoiceNumber(), entityTable.column("invoice_number"));
            }
            if (criteria.getInvoiceDate() != null) {
                builder.buildFilterConditionForField(criteria.getInvoiceDate(), entityTable.column("invoice_date"));
            }
            if (criteria.getStatus() != null) {
                builder.buildFilterConditionForField(criteria.getStatus(), entityTable.column("status"));
            }
            if (criteria.getLiquidationDate() != null) {
                builder.buildFilterConditionForField(criteria.getLiquidationDate(), entityTable.column("liquidation_date"));
            }
            if (criteria.getUnit() != null) {
                builder.buildFilterConditionForField(criteria.getUnit(), entityTable.column("unit"));
            }
            if (criteria.getYearOfUse() != null) {
                builder.buildFilterConditionForField(criteria.getYearOfUse(), entityTable.column("year_of_use"));
            }
            if (criteria.getMonthOfUse() != null) {
                builder.buildFilterConditionForField(criteria.getMonthOfUse(), entityTable.column("month_of_use"));
            }
            if (criteria.getWarrantyPeriod() != null) {
                builder.buildFilterConditionForField(criteria.getWarrantyPeriod(), entityTable.column("warranty_period"));
            }
            if (criteria.getManufacturer() != null) {
                builder.buildFilterConditionForField(criteria.getManufacturer(), entityTable.column("manufacturer"));
            }
            if (criteria.getIsMadeIn() != null) {
                builder.buildFilterConditionForField(criteria.getIsMadeIn(), entityTable.column("is_made_in"));
            }
            if (criteria.getSpecs() != null) {
                builder.buildFilterConditionForField(criteria.getSpecs(), entityTable.column("specs"));
            }
            if (criteria.getRemovalDate() != null) {
                builder.buildFilterConditionForField(criteria.getRemovalDate(), entityTable.column("removal_date"));
            }
            if (criteria.getReasonForRemoval() != null) {
                builder.buildFilterConditionForField(criteria.getReasonForRemoval(), entityTable.column("reason_for_removal"));
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
        return builder.buildConditions();
    }
}
