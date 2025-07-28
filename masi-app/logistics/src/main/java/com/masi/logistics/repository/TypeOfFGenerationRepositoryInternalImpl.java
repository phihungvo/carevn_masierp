package com.masi.logistics.repository;

import com.masi.logistics.domain.TypeOfFGeneration;
import com.masi.logistics.domain.criteria.TypeOfFGenerationCriteria;
import com.masi.logistics.repository.rowmapper.ColumnConverter;
import com.masi.logistics.repository.rowmapper.TypeOfFGenerationRowMapper;
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
 * Spring Data R2DBC custom repository implementation for the TypeOfFGeneration entity.
 */
@SuppressWarnings("unused")
class TypeOfFGenerationRepositoryInternalImpl
    extends SimpleR2dbcRepository<TypeOfFGeneration, UUID>
    implements TypeOfFGenerationRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final TypeOfFGenerationRowMapper typeoffgenerationMapper;
    private final ColumnConverter columnConverter;

    private static final Table entityTable = Table.aliased("type_of_f_generation", EntityManager.ENTITY_ALIAS);

    public TypeOfFGenerationRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        TypeOfFGenerationRowMapper typeoffgenerationMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter,
        ColumnConverter columnConverter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(TypeOfFGeneration.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.typeoffgenerationMapper = typeoffgenerationMapper;
        this.columnConverter = columnConverter;
    }

    @Override
    public Flux<TypeOfFGeneration> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<TypeOfFGeneration> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = TypeOfFGenerationSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        SelectFromAndJoin selectFrom = Select.builder().select(columns).from(entityTable);
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, TypeOfFGeneration.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<TypeOfFGeneration> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<TypeOfFGeneration> findById(UUID id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(StringUtils.wrap(id.toString(), "'")));
        return createQuery(null, whereClause).one();
    }

    private TypeOfFGeneration process(Row row, RowMetadata metadata) {
        TypeOfFGeneration entity = typeoffgenerationMapper.apply(row, "e");
        return entity;
    }

    @Override
    public <S extends TypeOfFGeneration> Mono<S> save(S entity) {
        return super.save(entity);
    }

    @Override
    public Flux<TypeOfFGeneration> findByCriteria(TypeOfFGenerationCriteria typeOfFGenerationCriteria, Pageable page) {
        return createQuery(page, buildConditions(typeOfFGenerationCriteria)).all();
    }

    @Override
    public Mono<Long> countByCriteria(TypeOfFGenerationCriteria criteria) {
        return findByCriteria(criteria, null)
            .collectList()
            .map(collectedList -> collectedList != null ? (long) collectedList.size() : (long) 0);
    }

    private Condition buildConditions(TypeOfFGenerationCriteria criteria) {
        ConditionBuilder builder = new ConditionBuilder(this.columnConverter);
        List<Condition> allConditions = new ArrayList<Condition>();
        if (criteria != null) {
            if (criteria.getId() != null) {
                builder.buildFilterConditionForField(criteria.getId(), entityTable.column("id"));
            }
            if (criteria.getCode() != null) {
                builder.buildFilterConditionForField(criteria.getCode(), entityTable.column("code"));
            }
            if (criteria.getAttribute() != null) {
                builder.buildFilterConditionForField(criteria.getAttribute(), entityTable.column("attribute"));
            }
            if (criteria.getName() != null) {
                builder.buildFilterConditionForField(criteria.getName(), entityTable.column("name"));
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
