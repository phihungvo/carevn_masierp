package com.masi.logistics.repository;

import com.masi.logistics.domain.Factories;
import com.masi.logistics.domain.criteria.FactoriesCriteria;
import com.masi.logistics.repository.rowmapper.ColumnConverter;
import com.masi.logistics.repository.rowmapper.FactoriesRowMapper;
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
 * Spring Data R2DBC custom repository implementation for the Factories entity.
 */
@SuppressWarnings("unused")
class FactoriesRepositoryInternalImpl extends SimpleR2dbcRepository<Factories, UUID> implements FactoriesRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final FactoriesRowMapper factoriesMapper;
    private final ColumnConverter columnConverter;

    private static final Table entityTable = Table.aliased("factories", EntityManager.ENTITY_ALIAS);

    public FactoriesRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        FactoriesRowMapper factoriesMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter,
        ColumnConverter columnConverter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(Factories.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.factoriesMapper = factoriesMapper;
        this.columnConverter = columnConverter;
    }

    @Override
    public Flux<Factories> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<Factories> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = FactoriesSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        SelectFromAndJoin selectFrom = Select.builder().select(columns).from(entityTable);
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, Factories.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<Factories> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<Factories> findById(UUID id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(StringUtils.wrap(id.toString(), "'")));
        return createQuery(null, whereClause).one();
    }

    private Factories process(Row row, RowMetadata metadata) {
        Factories entity = factoriesMapper.apply(row, "e");
        return entity;
    }

    @Override
    public <S extends Factories> Mono<S> save(S entity) {
        return super.save(entity);
    }

    @Override
    public Flux<Factories> findByCriteria(FactoriesCriteria factoriesCriteria, Pageable page) {
        page = getDefaultSort(page);
        return createQuery(page, buildConditions(factoriesCriteria)).all();
    }

    private Pageable getDefaultSort(Pageable pageable) {
        Sort defaultSort = Sort.by(Sort.Direction.DESC, "created_date");
        if (Objects.isNull(pageable)) {
            pageable = PageRequest.of(0, Integer.MAX_VALUE, defaultSort);
        }
        if (pageable.getSort().equals(Sort.unsorted())) {
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), defaultSort);
        }
        return pageable;
    }

    @Override
    public Mono<Long> countByCriteria(FactoriesCriteria criteria) {
        return findByCriteria(criteria, null)
            .collectList()
            .map(collectedList -> collectedList != null ? (long) collectedList.size() : (long) 0);
    }

    private Condition buildConditions(FactoriesCriteria criteria) {
        ConditionBuilder builder = new ConditionBuilder(this.columnConverter);
        builder.buildFilterConditionForField(new BooleanFilter().setEquals(false), entityTable.column("is_deleted"));
        Condition subgroup1 = null;

        if (criteria != null) {
            if (criteria.getId() != null) {
                builder.buildFilterConditionForField(criteria.getId(), entityTable.column("id"));
            }
//            if (criteria.getCode() != null) {
//                builder.buildFilterConditionForField(criteria.getCode(), entityTable.column("code"));
//            }
//            if (criteria.getName() != null) {
//                builder.buildFilterConditionForField(criteria.getName(), entityTable.column("name"));
//            }
            if ((criteria.getCode() != null && !criteria.getCode().getContains().toString().equals(""))) {
                Condition group1 = Conditions.like(
                        Functions.lower(entityTable.column("code")),
                        SQL.literalOf("%" + criteria.getCode().getContains().toLowerCase() + "%")
                );
                Condition group2 = Conditions.like(
                        Functions.lower(entityTable.column("name")),
                        SQL.literalOf("%" + criteria.getCode().getContains().toLowerCase() + "%")
                );
                Condition group3 = Conditions.like(
                        Functions.lower(entityTable.column("address")),
                        SQL.literalOf("%" + criteria.getCode().getContains().toLowerCase() + "%")
                );
                subgroup1 = Conditions.nest(group1.or(group2).or(group3));
                builder.buildConditions().and(subgroup1);
            }
            if (criteria.getTypeFactory() != null) {
                builder.buildFilterConditionForField(criteria.getTypeFactory(), entityTable.column("type_factory"));
            }
            if (criteria.getDepartmentId() != null) {
                builder.buildFilterConditionForField(criteria.getDepartmentId(), entityTable.column("department_id"));
            }
            if (criteria.getDescription() != null) {
                builder.buildFilterConditionForField(criteria.getDescription(), entityTable.column("description"));
            }
            if (criteria.getCanDelete() != null) {
                builder.buildFilterConditionForField(criteria.getCanDelete(), entityTable.column("can_delete"));
            }
            if (criteria.getNormalizedName() != null) {
                builder.buildFilterConditionForField(criteria.getNormalizedName(), entityTable.column("normalized_name"));
            }
//            if (criteria.getAddress() != null) {
//                builder.buildFilterConditionForField(criteria.getAddress(), entityTable.column("address"));
//            }
            if (criteria.getEmployeeOwnerId() != null) {
                builder.buildFilterConditionForField(criteria.getEmployeeOwnerId(), entityTable.column("employee_owner_id"));
            }
            if (criteria.getAttribute() != null) {
                builder.buildFilterConditionForField(criteria.getAttribute(), entityTable.column("attribute"));
            }
            if (criteria.getIsActive() != null) {
                builder.buildFilterConditionForField(criteria.getIsActive(), entityTable.column("is_active"));
            }
            if (criteria.getCompany() != null) {
                builder.buildFilterConditionForField(criteria.getCompany(), entityTable.column("company"));
            }
            if (criteria.getDepartment() != null) {
                builder.buildFilterConditionForField(criteria.getDepartment(), entityTable.column("department"));
            }
            if (criteria.getIsDeleted() != null) {
                builder.buildFilterConditionForField(criteria.getIsDeleted(), entityTable.column("is_deleted"));
            }
            if (criteria.getCreatedBy() != null) {
                builder.buildFilterConditionForField(criteria.getCreatedBy(), entityTable.column("created_by"));
            }
            if (criteria.getCreatedDate() != null) {
                builder.buildFilterConditionForField(criteria.getCreatedDate(), entityTable.column("created_date"));
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
        }
        if(subgroup1 == null)
            return builder.buildConditions();
        return builder.buildConditions().and(subgroup1);
    }
}
