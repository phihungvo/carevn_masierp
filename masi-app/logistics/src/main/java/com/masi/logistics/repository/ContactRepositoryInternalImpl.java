package com.masi.logistics.repository;

import com.masi.logistics.domain.Contact;
import com.masi.logistics.domain.criteria.ContactCriteria;
import com.masi.logistics.repository.rowmapper.ColumnConverter;
import com.masi.logistics.repository.rowmapper.ContactRowMapper;
import com.masi.logistics.repository.rowmapper.ContactTypeRowMapper;
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
 * Spring Data R2DBC custom repository implementation for the Contact entity.
 */
@SuppressWarnings("unused")
class ContactRepositoryInternalImpl extends SimpleR2dbcRepository<Contact, UUID> implements ContactRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final ContactTypeRowMapper contacttypeMapper;
    private final ContactRowMapper contactMapper;
    private final ColumnConverter columnConverter;

    private static final Table entityTable = Table.aliased("contact", EntityManager.ENTITY_ALIAS);
    private static final Table contactTypeTable = Table.aliased("contact_type", "contactType");

    public ContactRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        ContactTypeRowMapper contacttypeMapper,
        ContactRowMapper contactMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter,
        ColumnConverter columnConverter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(Contact.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.contacttypeMapper = contacttypeMapper;
        this.contactMapper = contactMapper;
        this.columnConverter = columnConverter;
    }

    @Override
    public Flux<Contact> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<Contact> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = ContactSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(ContactTypeSqlHelper.getColumns(contactTypeTable, "contactType"));
        SelectFromAndJoinCondition selectFrom = Select.builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(contactTypeTable)
            .on(Column.create("contact_type_id", entityTable))
            .equals(Column.create("id", contactTypeTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, Contact.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<Contact> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<Contact> findById(UUID id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(StringUtils.wrap(id.toString(), "'")));
        return createQuery(null, whereClause).one();
    }

    private Contact process(Row row, RowMetadata metadata) {
        Contact entity = contactMapper.apply(row, "e");
        entity.setContactType(contacttypeMapper.apply(row, "contactType"));
        return entity;
    }

    @Override
    public <S extends Contact> Mono<S> save(S entity) {
        return super.save(entity);
    }

    public Flux<Contact> findAllBySupplierId(UUID supplierId, String company) {
        Condition whereClause = Conditions.isEqual(entityTable.column("supplier_id"), Conditions.just(StringUtils.wrap(supplierId.toString(), "'")));
        whereClause = whereClause.and(Conditions.isNull(entityTable.column("deleted_at")));
        whereClause = whereClause.and(Conditions.isNull(entityTable.column("deleted_by")));
        whereClause = whereClause.and(Conditions.isEqual(entityTable.column("is_active"), Conditions.just("True")));
        whereClause = whereClause.and(Conditions.isEqual(entityTable.column("company"), Conditions.just(StringUtils.wrap(company, "'"))));
//        whereClause = whereClause.and((Conditions.isEqual(contactTypeTable.column("is_active"), Conditions.just("True"))));

        return createQuery(null, whereClause).all();
    }

    @Override
    public Flux<Contact> findByCriteria(ContactCriteria contactCriteria, Pageable page) {
        return createQuery(page, buildConditions(contactCriteria)).all();
    }

    @Override
    public Mono<Long> countByCriteria(ContactCriteria criteria) {
        return findByCriteria(criteria, null)
            .collectList()
            .map(collectedList -> collectedList != null ? (long) collectedList.size() : (long) 0);
    }

    private Condition buildConditions(ContactCriteria criteria) {
        ConditionBuilder builder = new ConditionBuilder(this.columnConverter);
        List<Condition> allConditions = new ArrayList<Condition>();
        if (criteria != null) {
            if (criteria.getId() != null) {
                builder.buildFilterConditionForField(criteria.getId(), entityTable.column("id"));
            }
            if (criteria.getSupplierId() != null) {
                builder.buildFilterConditionForField(criteria.getSupplierId(), entityTable.column("supplier_id"));
            }
            if (criteria.getName() != null) {
                builder.buildFilterConditionForField(criteria.getName(), entityTable.column("name"));
            }
            if (criteria.getEmail() != null) {
                builder.buildFilterConditionForField(criteria.getEmail(), entityTable.column("email"));
            }
            if (criteria.getPhone() != null) {
                builder.buildFilterConditionForField(criteria.getPhone(), entityTable.column("phone"));
            }
            if (criteria.getBirthDate() != null) {
                builder.buildFilterConditionForField(criteria.getBirthDate(), entityTable.column("birth_date"));
            }
            if (criteria.getPosition() != null) {
                builder.buildFilterConditionForField(criteria.getPosition(), entityTable.column("position"));
            }
            if (criteria.getContactInfo() != null) {
                builder.buildFilterConditionForField(criteria.getContactInfo(), entityTable.column("contact_info"));
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
            if (criteria.getContactTypeId() != null) {
                builder.buildFilterConditionForField(criteria.getContactTypeId(), contactTypeTable.column("id"));
            }
        }
        return builder.buildConditions();
    }
}
