package com.masi.utility.repository;

import com.masi.utility.domain.Notification;
import com.masi.utility.domain.criteria.NotificationCriteria;
import com.masi.utility.repository.rowmapper.ColumnConverter;
import com.masi.utility.repository.rowmapper.NotificationRowMapper;
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
 * Spring Data R2DBC custom repository implementation for the Notification entity.
 */
@SuppressWarnings("unused")
class NotificationRepositoryInternalImpl extends SimpleR2dbcRepository<Notification, UUID> implements NotificationRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final NotificationRowMapper notificationMapper;
    private final ColumnConverter columnConverter;

    private static final Table entityTable = Table.aliased("notification", EntityManager.ENTITY_ALIAS);

    public NotificationRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        NotificationRowMapper notificationMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter,
        ColumnConverter columnConverter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(Notification.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.notificationMapper = notificationMapper;
        this.columnConverter = columnConverter;
    }

    @Override
    public Flux<Notification> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<Notification> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = NotificationSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        SelectFromAndJoin selectFrom = Select.builder().select(columns).from(entityTable);
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, Notification.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<Notification> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<Notification> findById(UUID id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(StringUtils.wrap(id.toString(), "'")));
        return createQuery(null, whereClause).one();
    }

    private Notification process(Row row, RowMetadata metadata) {
        Notification entity = notificationMapper.apply(row, "e");
        return entity;
    }

    @Override
    public <S extends Notification> Mono<S> save(S entity) {
        return super.save(entity);
    }

    @Override
    public Flux<Notification> findByCriteria(NotificationCriteria notificationCriteria, Pageable page) {
        return createQuery(page, buildConditions(notificationCriteria)).all();
    }

    @Override
    public Mono<Long> countByCriteria(NotificationCriteria criteria) {
        return findByCriteria(criteria, null)
            .collectList()
            .map(collectedList -> collectedList != null ? (long) collectedList.size() : (long) 0);
    }

    private Condition buildConditions(NotificationCriteria criteria) {
        ConditionBuilder builder = new ConditionBuilder(this.columnConverter);
        List<Condition> allConditions = new ArrayList<Condition>();
        if (criteria != null) {
            if (criteria.getId() != null) {
                builder.buildFilterConditionForField(criteria.getId(), entityTable.column("id"));
            }
            if (criteria.getContent() != null) {
                builder.buildFilterConditionForField(criteria.getContent(), entityTable.column("content"));
            }
            if (criteria.getTitle() != null) {
                builder.buildFilterConditionForField(criteria.getTitle(), entityTable.column("title"));
            }
            if (criteria.getEntityName() != null) {
                builder.buildFilterConditionForField(criteria.getEntityName(), entityTable.column("entity_name"));
            }
            if (criteria.getEntityId() != null) {
                builder.buildFilterConditionForField(criteria.getEntityId(), entityTable.column("entity_id"));
            }
            if (criteria.getEntityType() != null) {
                builder.buildFilterConditionForField(criteria.getEntityType(), entityTable.column("entity_type"));
            }
            if (criteria.getCreatedAt() != null) {
                builder.buildFilterConditionForField(criteria.getCreatedAt(), entityTable.column("created_at"));
            }
            if (criteria.getCreatedBy() != null) {
                builder.buildFilterConditionForField(criteria.getCreatedBy(), entityTable.column("created_by"));
            }
        }
        return builder.buildConditions();
    }
}
