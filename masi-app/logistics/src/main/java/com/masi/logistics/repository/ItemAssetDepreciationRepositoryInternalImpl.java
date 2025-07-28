package com.masi.logistics.repository;

import com.masi.logistics.domain.ItemAssetDepreciation;
import com.masi.logistics.domain.criteria.ItemAssetDepreciationCriteria;
import com.masi.logistics.repository.rowmapper.ColumnConverter;
import com.masi.logistics.repository.rowmapper.ItemAssetDepreciationRowMapper;
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
import tech.jhipster.service.filter.BooleanFilter;

/**
 * Spring Data R2DBC custom repository implementation for the ItemAssetDepreciation entity.
 */
@SuppressWarnings("unused")
class ItemAssetDepreciationRepositoryInternalImpl
    extends SimpleR2dbcRepository<ItemAssetDepreciation, UUID>
    implements ItemAssetDepreciationRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final ItemAssetDepreciationRowMapper itemassetdepreciationMapper;
    private final ColumnConverter columnConverter;

    private static final Table entityTable = Table.aliased("item_asset_depreciation", EntityManager.ENTITY_ALIAS);

    public ItemAssetDepreciationRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        ItemAssetDepreciationRowMapper itemassetdepreciationMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter,
        ColumnConverter columnConverter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(ItemAssetDepreciation.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.itemassetdepreciationMapper = itemassetdepreciationMapper;
        this.columnConverter = columnConverter;
    }

    @Override
    public Flux<ItemAssetDepreciation> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<ItemAssetDepreciation> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = ItemAssetDepreciationSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        SelectFromAndJoin selectFrom = Select.builder().select(columns).from(entityTable);
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, ItemAssetDepreciation.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<ItemAssetDepreciation> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<ItemAssetDepreciation> findById(UUID id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(StringUtils.wrap(id.toString(), "'")));
        return createQuery(null, whereClause).one();
    }

    private ItemAssetDepreciation process(Row row, RowMetadata metadata) {
        ItemAssetDepreciation entity = itemassetdepreciationMapper.apply(row, "e");
        return entity;
    }

    @Override
    public <S extends ItemAssetDepreciation> Mono<S> save(S entity) {
        return super.save(entity);
    }

    @Override
    public Flux<ItemAssetDepreciation> findByCriteria(ItemAssetDepreciationCriteria itemAssetDepreciationCriteria, Pageable page) {
        page = getDefaultSort(page);
        return createQuery(page, buildConditions(itemAssetDepreciationCriteria)).all();
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
    public Mono<Long> countByCriteria(ItemAssetDepreciationCriteria criteria) {
        return findByCriteria(criteria, null)
            .collectList()
            .map(collectedList -> collectedList != null ? (long) collectedList.size() : (long) 0);
    }

    private Condition buildConditions(ItemAssetDepreciationCriteria criteria) {
        ConditionBuilder builder = new ConditionBuilder(this.columnConverter);
        List<Condition> allConditions = new ArrayList<Condition>();
        builder.buildFilterConditionForField(new BooleanFilter().setEquals(false), entityTable.column("is_deleted"));
        Condition subgroup1 = null;
        if (criteria != null) {
            if (criteria.getId() != null) {
                builder.buildFilterConditionForField(criteria.getId(), entityTable.column("id"));
            }
            if (criteria.getCode() != null) {
                Condition group1 = Conditions.like(
                        Functions.lower(entityTable.column("code")),
                        SQL.literalOf("%" + criteria.getCode().getContains().toLowerCase() + "%")
                );
                Condition group2 = Conditions.like(
                        Functions.lower(entityTable.column("note")),
                        SQL.literalOf("%" + criteria.getCode().getContains().toLowerCase() + "%")
                );
            }
            if (criteria.getAttribute() != null) {
                builder.buildFilterConditionForField(criteria.getAttribute(), entityTable.column("attribute"));
            }
            if (criteria.getName() != null) {
                builder.buildFilterConditionForField(criteria.getName(), entityTable.column("name"));
            }
            if (criteria.getStatus() != null) {
                builder.buildFilterConditionForField(criteria.getStatus(), entityTable.column("status"));
            }
            if (criteria.getDepreciationDate() != null) {
                builder.buildFilterConditionForField(criteria.getDepreciationDate(), entityTable.column("depreciation_date"));
            }
            if (criteria.getAccountingDate() != null) {
                builder.buildFilterConditionForField(criteria.getAccountingDate(), entityTable.column("accounting_date"));
            }
            if (criteria.getEmployeeId() != null) {
                builder.buildFilterConditionForField(criteria.getEmployeeId(), entityTable.column("employee_id"));
            }
            if (criteria.getDescription() != null) {
                builder.buildFilterConditionForField(criteria.getDescription(), entityTable.column("description"));
            }
            if (criteria.getTypePageDepreciation() != null) {
                builder.buildFilterConditionForField(criteria.getTypePageDepreciation(), entityTable.column("type_page_depreciation"));
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
