package com.masi.utility.repository;

import com.masi.utility.domain.HolidayConfig;
import com.masi.utility.domain.criteria.HolidayConfigCriteria;
import com.masi.utility.repository.rowmapper.ColumnConverter;
import com.masi.utility.repository.rowmapper.HolidayConfigRowMapper;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;

import java.time.format.DateTimeFormatter;
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
 * Spring Data R2DBC custom repository implementation for the HolidayConfig entity.
 */
@SuppressWarnings("unused")
class HolidayConfigRepositoryInternalImpl extends SimpleR2dbcRepository<HolidayConfig, UUID> implements HolidayConfigRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final HolidayConfigRowMapper holidayconfigMapper;
    private final ColumnConverter columnConverter;

    private static final Table entityTable = Table.aliased("holiday_config", EntityManager.ENTITY_ALIAS);

    public HolidayConfigRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        HolidayConfigRowMapper holidayconfigMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter,
        ColumnConverter columnConverter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(HolidayConfig.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.holidayconfigMapper = holidayconfigMapper;
        this.columnConverter = columnConverter;
    }

    @Override
    public Flux<HolidayConfig> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<HolidayConfig> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = HolidayConfigSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        SelectFromAndJoin selectFrom = Select.builder().select(columns).from(entityTable);
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, HolidayConfig.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<HolidayConfig> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<HolidayConfig> findById(UUID id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(StringUtils.wrap(id.toString(), "'")));
        return createQuery(null, whereClause).one();
    }

    private HolidayConfig process(Row row, RowMetadata metadata) {
        HolidayConfig entity = holidayconfigMapper.apply(row, "e");
        return entity;
    }

    @Override
    public <S extends HolidayConfig> Mono<S> save(S entity) {
        return super.save(entity);
    }

    @Override
    public Flux<HolidayConfig> findByCriteria(HolidayConfigCriteria holidayConfigCriteria, Pageable page) {
        return createQuery(page, buildConditions(holidayConfigCriteria)).all();
    }

    @Override
    public Mono<Long> countByCriteria(HolidayConfigCriteria criteria) {
        return findByCriteria(criteria, null)
            .collectList()
            .map(collectedList -> collectedList != null ? (long) collectedList.size() : (long) 0);
    }

    private Condition buildConditions(HolidayConfigCriteria criteria) {
        ConditionBuilder builder = new ConditionBuilder(this.columnConverter);
        List<Condition> allConditions = new ArrayList<Condition>();
        if (criteria != null) {
            if (criteria.getId() != null) {
                builder.buildFilterConditionForField(criteria.getId(), entityTable.column("id"));
            }
            if (criteria.getName() != null) {
                builder.buildFilterConditionForField(criteria.getName(), entityTable.column("name"));
            }
            if (criteria.getType() != null) {
                builder.buildFilterConditionForField(criteria.getType(), entityTable.column("type"));
            }
            if (criteria.getCalenderType() != null) {
                builder.buildFilterConditionForField(criteria.getCalenderType(), entityTable.column("calender_type"));
            }
            if (criteria.getDate() != null) {
                builder.buildFilterConditionForField(criteria.getDate(), entityTable.column("date"));
            }

            if (criteria.getDescription() != null) {
                builder.buildFilterConditionForField(criteria.getDescription(), entityTable.column("description"));
            }
            if (criteria.getCreatedAt() != null) {
                builder.buildFilterConditionForField(criteria.getCreatedAt(), entityTable.column("created_at"));
            }
            if (criteria.getUpdatedAt() != null) {
                builder.buildFilterConditionForField(criteria.getUpdatedAt(), entityTable.column("updated_at"));
            }
        }
        var condition = builder.buildConditions();
        if (criteria != null) {
            if (criteria.getFixedDate() != null) {
                var dateFormatted = criteria.getFixedDate().format(DateTimeFormatter.ofPattern("MM-dd"));
                var column = entityTable.column("date").toString();
                var expression = "to_char(" + column + ", 'MM-dd') = '" + dateFormatted + "'";
                condition = condition.and(Conditions.just(expression));
            }
            if (criteria.getFixedFromDate() != null && criteria.getFixedToDate() != null) {
                var from = criteria.getFixedFromDate().format(DateTimeFormatter.ofPattern("MM-dd"));
                var to = criteria.getFixedToDate().format(DateTimeFormatter.ofPattern("MM-dd"));
                var column = entityTable.column("date").toString();
                var expression = "to_char(" + column + ", 'MM-dd') >= '" + from + "' and to_char(" + column + ", 'MM-dd') <= '" + to + "'";
                condition = condition.and(Conditions.just(expression));
            }
        }
        return condition;
    }
}
