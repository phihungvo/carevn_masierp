package com.masi.utility.repository;

import com.masi.utility.domain.StandardWorkScheduleConfig;
import com.masi.utility.domain.criteria.StandardWorkScheduleConfigCriteria;
import com.masi.utility.repository.rowmapper.ColumnConverter;
import com.masi.utility.repository.rowmapper.StandardWorkScheduleConfigRowMapper;
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
 * Spring Data R2DBC custom repository implementation for the StandardWorkScheduleConfig entity.
 */
@SuppressWarnings("unused")
class StandardWorkScheduleConfigRepositoryInternalImpl
    extends SimpleR2dbcRepository<StandardWorkScheduleConfig, UUID>
    implements StandardWorkScheduleConfigRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final StandardWorkScheduleConfigRowMapper standardworkscheduleconfigMapper;
    private final ColumnConverter columnConverter;

    private static final Table entityTable = Table.aliased("standard_work_schedule_config", EntityManager.ENTITY_ALIAS);

    public StandardWorkScheduleConfigRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        StandardWorkScheduleConfigRowMapper standardworkscheduleconfigMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter,
        ColumnConverter columnConverter
    ) {
        super(
            new MappingRelationalEntityInformation(
                converter.getMappingContext().getRequiredPersistentEntity(StandardWorkScheduleConfig.class)
            ),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.standardworkscheduleconfigMapper = standardworkscheduleconfigMapper;
        this.columnConverter = columnConverter;
    }

    @Override
    public Flux<StandardWorkScheduleConfig> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<StandardWorkScheduleConfig> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = StandardWorkScheduleConfigSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        SelectFromAndJoin selectFrom = Select.builder().select(columns).from(entityTable);
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, StandardWorkScheduleConfig.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<StandardWorkScheduleConfig> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<StandardWorkScheduleConfig> findById(UUID id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(StringUtils.wrap(id.toString(), "'")));
        return createQuery(null, whereClause).one();
    }

    private StandardWorkScheduleConfig process(Row row, RowMetadata metadata) {
        StandardWorkScheduleConfig entity = standardworkscheduleconfigMapper.apply(row, "e");
        return entity;
    }

    @Override
    public <S extends StandardWorkScheduleConfig> Mono<S> save(S entity) {
        return super.save(entity);
    }

    @Override
    public Flux<StandardWorkScheduleConfig> findByCriteria(
        StandardWorkScheduleConfigCriteria standardWorkScheduleConfigCriteria,
        Pageable page
    ) {
        return createQuery(page, buildConditions(standardWorkScheduleConfigCriteria)).all();
    }

    @Override
    public Mono<Long> countByCriteria(StandardWorkScheduleConfigCriteria criteria) {
        return findByCriteria(criteria, null)
            .collectList()
            .map(collectedList -> collectedList != null ? (long) collectedList.size() : (long) 0);
    }

    private Condition buildConditions(StandardWorkScheduleConfigCriteria criteria) {
        ConditionBuilder builder = new ConditionBuilder(this.columnConverter);
        List<Condition> allConditions = new ArrayList<Condition>();
        if (criteria != null) {
            if (criteria.getId() != null) {
                builder.buildFilterConditionForField(criteria.getId(), entityTable.column("id"));
            }
            if (criteria.getName() != null) {
                builder.buildFilterConditionForField(criteria.getName(), entityTable.column("name"));
            }
            if (criteria.getDayOfWeek() != null) {
                builder.buildFilterConditionForField(criteria.getDayOfWeek(), entityTable.column("day_of_week"));
            }
            if (criteria.getNumberOfShifts() != null) {
                builder.buildFilterConditionForField(criteria.getNumberOfShifts(), entityTable.column("number_of_shifts"));
            }
            if (criteria.getWorkHours() != null) {
                builder.buildFilterConditionForField(criteria.getWorkHours(), entityTable.column("work_hours"));
            }
            if (criteria.getDepartment() != null) {
                builder.buildFilterConditionForField(criteria.getDepartment(), entityTable.column("department"));
            }
            if (criteria.getCompany() != null) {
                builder.buildFilterConditionForField(criteria.getCompany(), entityTable.column("company"));
            }
            if (criteria.getDepartmentType() != null) {
                builder.buildFilterConditionForField(criteria.getDepartmentType(), entityTable.column("department_type"));
            }
            if (criteria.getUpdatedAt() != null) {
                builder.buildFilterConditionForField(criteria.getUpdatedAt(), entityTable.column("updated_at"));
            }
            if (criteria.getUpdatedBy() != null) {
                builder.buildFilterConditionForField(criteria.getUpdatedBy(), entityTable.column("updated_by"));
            }
        }
        return builder.buildConditions();
    }
}
