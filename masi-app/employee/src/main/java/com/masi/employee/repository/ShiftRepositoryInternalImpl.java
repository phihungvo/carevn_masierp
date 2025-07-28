package com.masi.employee.repository;

import com.masi.employee.domain.Shift;
import com.masi.employee.domain.criteria.ShiftCriteria;
import com.masi.employee.repository.rowmapper.ColumnConverter;
import com.masi.employee.repository.rowmapper.ShiftRowMapper;
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
 * Spring Data R2DBC custom repository implementation for the Shift entity.
 */
@SuppressWarnings("unused")
class ShiftRepositoryInternalImpl extends SimpleR2dbcRepository<Shift, UUID> implements ShiftRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final ShiftRowMapper shiftMapper;
    private final ColumnConverter columnConverter;

    private static final Table entityTable = Table.aliased("shift", EntityManager.ENTITY_ALIAS);

    public ShiftRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        ShiftRowMapper shiftMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter,
        ColumnConverter columnConverter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(Shift.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.shiftMapper = shiftMapper;
        this.columnConverter = columnConverter;
    }

    @Override
    public Flux<Shift> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<Shift> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = ShiftSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        SelectFromAndJoin selectFrom = Select.builder().select(columns).from(entityTable);
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, Shift.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<Shift> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<Shift> findById(UUID id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(StringUtils.wrap(id.toString(), "'")));
        return createQuery(null, whereClause).one();
    }

    private Shift process(Row row, RowMetadata metadata) {
        Shift entity = shiftMapper.apply(row, "e");
        return entity;
    }

    @Override
    public <S extends Shift> Mono<S> save(S entity) {
        return super.save(entity);
    }

    @Override
    public Flux<Shift> findByCriteria(ShiftCriteria shiftCriteria, Pageable page) {
        return createQuery(page, buildConditions(shiftCriteria)).all();
    }

    @Override
    public Mono<Long> countByCriteria(ShiftCriteria criteria) {
        return findByCriteria(criteria, null)
            .collectList()
            .map(collectedList -> collectedList != null ? (long) collectedList.size() : (long) 0);
    }

    private Condition buildConditions(ShiftCriteria criteria) {
        ConditionBuilder builder = new ConditionBuilder(this.columnConverter);
        List<Condition> allConditions = new ArrayList<Condition>();
        if (criteria != null) {
            if (criteria.getId() != null) {
                builder.buildFilterConditionForField(criteria.getId(), entityTable.column("id"));
            }
            if (criteria.getIdStandardWorkScheduleConfig() != null) {
                builder.buildFilterConditionForField(
                    criteria.getIdStandardWorkScheduleConfig(),
                    entityTable.column("id_standard_work_schedule_config")
                );
            }
            if (criteria.getShiftName() != null) {
                builder.buildFilterConditionForField(criteria.getShiftName(), entityTable.column("shift_name"));
            }
            if (criteria.getDurationHours() != null) {
                builder.buildFilterConditionForField(criteria.getDurationHours(), entityTable.column("duration_hours"));
            }
            if (criteria.getHourStartTime() != null) {
                builder.buildFilterConditionForField(criteria.getHourStartTime(), entityTable.column("hour_start_time"));
            }
            if (criteria.getMinuteStartTime() != null) {
                builder.buildFilterConditionForField(criteria.getMinuteStartTime(), entityTable.column("minute_start_time"));
            }
            if (criteria.getSecondStartTime() != null) {
                builder.buildFilterConditionForField(criteria.getSecondStartTime(), entityTable.column("second_start_time"));
            }
            if (criteria.getHourEndTime() != null) {
                builder.buildFilterConditionForField(criteria.getHourEndTime(), entityTable.column("hour_end_time"));
            }
            if (criteria.getMinuteEndTime() != null) {
                builder.buildFilterConditionForField(criteria.getMinuteEndTime(), entityTable.column("minute_end_time"));
            }
            if (criteria.getSecondEndTime() != null) {
                builder.buildFilterConditionForField(criteria.getSecondEndTime(), entityTable.column("second_end_time"));
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
        return builder.buildConditions();
    }
}
