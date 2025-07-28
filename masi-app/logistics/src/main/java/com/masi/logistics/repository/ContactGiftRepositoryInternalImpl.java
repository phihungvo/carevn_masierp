package com.masi.logistics.repository;

import com.masi.logistics.domain.ContactGift;
import com.masi.logistics.domain.criteria.ContactGiftCriteria;
import com.masi.logistics.repository.rowmapper.ColumnConverter;
import com.masi.logistics.repository.rowmapper.ContactGiftRowMapper;
import com.masi.logistics.repository.rowmapper.ContactRowMapper;
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
 * Spring Data R2DBC custom repository implementation for the ContactGift entity.
 */
@SuppressWarnings("unused")
class ContactGiftRepositoryInternalImpl extends SimpleR2dbcRepository<ContactGift, UUID> implements ContactGiftRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final ContactRowMapper contactMapper;
    private final ContactGiftRowMapper contactgiftMapper;
    private final ColumnConverter columnConverter;

    private static final Table entityTable = Table.aliased("contact_gift", EntityManager.ENTITY_ALIAS);
    private static final Table contactTable = Table.aliased("contact", "contact");

    public ContactGiftRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        ContactRowMapper contactMapper,
        ContactGiftRowMapper contactgiftMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter,
        ColumnConverter columnConverter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(ContactGift.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.contactMapper = contactMapper;
        this.contactgiftMapper = contactgiftMapper;
        this.columnConverter = columnConverter;
    }

    @Override
    public Flux<ContactGift> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<ContactGift> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = ContactGiftSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(ContactSqlHelper.getColumns(contactTable, "contact"));
        SelectFromAndJoinCondition selectFrom = Select.builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(contactTable)
            .on(Column.create("contact_id", entityTable))
            .equals(Column.create("id", contactTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, ContactGift.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<ContactGift> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<ContactGift> findById(UUID id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(StringUtils.wrap(id.toString(), "'")));
        return createQuery(null, whereClause).one();
    }

    private ContactGift process(Row row, RowMetadata metadata) {
        ContactGift entity = contactgiftMapper.apply(row, "e");
        entity.setContact(contactMapper.apply(row, "contact"));
        return entity;
    }

    @Override
    public <S extends ContactGift> Mono<S> save(S entity) {
        return super.save(entity);
    }

    @Override
    public Flux<ContactGift> findByCriteria(ContactGiftCriteria contactGiftCriteria, Pageable page) {
        return createQuery(page, buildConditions(contactGiftCriteria)).all();
    }

    @Override
    public Mono<Long> countByCriteria(ContactGiftCriteria criteria) {
        return findByCriteria(criteria, null)
            .collectList()
            .map(collectedList -> collectedList != null ? (long) collectedList.size() : (long) 0);
    }

    private Condition buildConditions(ContactGiftCriteria criteria) {
        ConditionBuilder builder = new ConditionBuilder(this.columnConverter);
        List<Condition> allConditions = new ArrayList<Condition>();
        if (criteria != null) {
            if (criteria.getId() != null) {
                builder.buildFilterConditionForField(criteria.getId(), entityTable.column("id"));
            }
            if (criteria.getName() != null) {
                builder.buildFilterConditionForField(criteria.getName(), entityTable.column("name"));
            }
            if (criteria.getDescription() != null) {
                builder.buildFilterConditionForField(criteria.getDescription(), entityTable.column("description"));
            }
            if (criteria.getGiftName() != null) {
                builder.buildFilterConditionForField(criteria.getGiftName(), entityTable.column("gift_name"));
            }
            if (criteria.getValue() != null) {
                builder.buildFilterConditionForField(criteria.getValue(), entityTable.column("value"));
            }
            if (criteria.getIsGiving() != null) {
                builder.buildFilterConditionForField(criteria.getIsGiving(), entityTable.column("is_giving"));
            }
            if (criteria.getStatus() != null) {
                builder.buildFilterConditionForField(criteria.getStatus(), entityTable.column("status"));
            }
            if (criteria.getExpectedDate() != null) {
                builder.buildFilterConditionForField(criteria.getExpectedDate(), entityTable.column("expected_date"));
            }
            if (criteria.getDateOfGiving() != null) {
                builder.buildFilterConditionForField(criteria.getDateOfGiving(), entityTable.column("date_of_giving"));
            }
            if (criteria.getCompany() != null) {
                builder.buildFilterConditionForField(criteria.getCompany(), entityTable.column("company"));
            }
            if (criteria.getDepartment() != null) {
                builder.buildFilterConditionForField(criteria.getDepartment(), entityTable.column("department"));
            }
            if (criteria.getCreatedBy() != null) {
                builder.buildFilterConditionForField(criteria.getCreatedBy(), entityTable.column("created_by"));
            }
            if (criteria.getCreatedAt() != null) {
                builder.buildFilterConditionForField(criteria.getCreatedAt(), entityTable.column("created_at"));
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
            if (criteria.getContactId() != null) {
                builder.buildFilterConditionForField(criteria.getContactId(), contactTable.column("id"));
            }
        }
        return builder.buildConditions();
    }
}
