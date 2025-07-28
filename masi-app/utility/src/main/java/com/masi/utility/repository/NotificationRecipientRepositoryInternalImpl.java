package com.masi.utility.repository;

import com.masi.utility.domain.NotificationRecipient;
import com.masi.utility.domain.criteria.NotificationRecipientCriteria;
import com.masi.utility.repository.rowmapper.ColumnConverter;
import com.masi.utility.repository.rowmapper.NotificationRecipientRowMapper;
import com.masi.utility.repository.rowmapper.NotificationRowMapper;
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
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Comparison;
import org.springframework.data.relational.core.sql.Condition;
import org.springframework.data.relational.core.sql.Conditions;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Select;
import org.springframework.data.relational.core.sql.SelectBuilder.SelectFromAndJoinCondition;
import org.springframework.data.relational.core.sql.Table;
import org.springframework.data.relational.repository.support.MappingRelationalEntityInformation;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.r2dbc.core.RowsFetchSpec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.service.ConditionBuilder;

/**
 * Spring Data R2DBC custom repository implementation for the NotificationRecipient entity.
 */
@SuppressWarnings("unused")
class NotificationRecipientRepositoryInternalImpl
    extends SimpleR2dbcRepository<NotificationRecipient, UUID>
    implements NotificationRecipientRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final NotificationRowMapper notificationMapper;
    private final NotificationRecipientRowMapper notificationrecipientMapper;
    private final ColumnConverter columnConverter;

    private static final Table entityTable = Table.aliased("notification_recipient", EntityManager.ENTITY_ALIAS);
    private static final Table notificationTable = Table.aliased("notification", "notification");

    public NotificationRecipientRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        NotificationRowMapper notificationMapper,
        NotificationRecipientRowMapper notificationrecipientMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter,
        ColumnConverter columnConverter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(NotificationRecipient.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.notificationMapper = notificationMapper;
        this.notificationrecipientMapper = notificationrecipientMapper;
        this.columnConverter = columnConverter;
    }

    @Override
    public Flux<NotificationRecipient> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<NotificationRecipient> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = NotificationRecipientSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(NotificationSqlHelper.getColumns(notificationTable, "notification"));
        columns.add(Column.aliased("created_at", notificationTable, "e_sort_field"));
        Sort defaultSort = Sort.by(Sort.Order.desc("sort_field"));
         if (Objects.isNull(pageable)) {
            pageable = PageRequest.of(0, Integer.MAX_VALUE, defaultSort);
        }
        if (pageable.getSort().equals(Sort.unsorted())) {
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), defaultSort);
        }
        SelectFromAndJoinCondition selectFrom = Select.builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(notificationTable)
            .on(Column.create("notification_id", entityTable))
            .equals(Column.create("id", notificationTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, NotificationRecipient.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<NotificationRecipient> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<NotificationRecipient> findById(UUID id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(StringUtils.wrap(id.toString(), "'")));
        return createQuery(null, whereClause).one();
    }

    private NotificationRecipient process(Row row, RowMetadata metadata) {
        NotificationRecipient entity = notificationrecipientMapper.apply(row, "e");
        entity.setNotification(notificationMapper.apply(row, "notification"));
        return entity;
    }

    @Override
    public <S extends NotificationRecipient> Mono<S> save(S entity) {
        return super.save(entity);
    }


    @Override
    public Flux<NotificationRecipient> findByCriteria(NotificationRecipientCriteria notificationRecipientCriteria, Pageable page) {
        return createQuery(page, buildConditions(notificationRecipientCriteria)).all();
    }

    @Override
    public Mono<Long> countByCriteria(NotificationRecipientCriteria criteria) {
        return findByCriteria(criteria, null)
            .collectList()
            .map(collectedList -> collectedList != null ? (long) collectedList.size() : (long) 0);
    }

    private Condition buildConditions(NotificationRecipientCriteria criteria) {
        ConditionBuilder builder = new ConditionBuilder(this.columnConverter);
        List<Condition> allConditions = new ArrayList<Condition>();
        if (criteria != null) {
            if (criteria.getId() != null) {
                builder.buildFilterConditionForField(criteria.getId(), entityTable.column("id"));
            }
            if (criteria.getNotificationId() != null) {
                builder.buildFilterConditionForField(criteria.getNotificationId(), entityTable.column("notification_id"));
            }
            if (criteria.getRecipientId() != null) {
                builder.buildFilterConditionForField(criteria.getRecipientId(), entityTable.column("recipient_id"));
            }
            if (criteria.getRead() != null) {
                builder.buildFilterConditionForField(criteria.getRead(), entityTable.column("read"));
            }
            if (criteria.getReadAt() != null) {
                builder.buildFilterConditionForField(criteria.getReadAt(), entityTable.column("read_at"));
            }
            if (criteria.getNotificationId() != null) {
                builder.buildFilterConditionForField(criteria.getNotificationId(), notificationTable.column("id"));
            }
        }
        return builder.buildConditions();
    }
}
