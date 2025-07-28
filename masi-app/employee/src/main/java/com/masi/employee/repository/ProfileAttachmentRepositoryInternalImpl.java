package com.masi.employee.repository;

import com.masi.employee.domain.ProfileAttachment;
import com.masi.employee.repository.rowmapper.ProfileAttachmentRowMapper;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
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

/**
 * Spring Data R2DBC custom repository implementation for the ProfileAttachment entity.
 */
@SuppressWarnings("unused")
class ProfileAttachmentRepositoryInternalImpl
    extends SimpleR2dbcRepository<ProfileAttachment, UUID>
    implements ProfileAttachmentRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final ProfileAttachmentRowMapper profileattachmentMapper;

    private static final Table entityTable = Table.aliased("profile_attachment", EntityManager.ENTITY_ALIAS);

    public ProfileAttachmentRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        ProfileAttachmentRowMapper profileattachmentMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(ProfileAttachment.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.profileattachmentMapper = profileattachmentMapper;
    }

    @Override
    public Flux<ProfileAttachment> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<ProfileAttachment> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = ProfileAttachmentSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        SelectFromAndJoin selectFrom = Select.builder().select(columns).from(entityTable);
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, ProfileAttachment.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<ProfileAttachment> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<ProfileAttachment> findById(UUID id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(StringUtils.wrap(id.toString(), "'")));
        return createQuery(null, whereClause).one();
    }

    private ProfileAttachment process(Row row, RowMetadata metadata) {
        ProfileAttachment entity = profileattachmentMapper.apply(row, "e");
        return entity;
    }

    @Override
    public <S extends ProfileAttachment> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
