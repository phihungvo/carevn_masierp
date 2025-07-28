package com.masi.utility.repository;

import com.masi.utility.domain.FileAttachment;
import com.masi.utility.domain.criteria.FileAttachmentCriteria;
import com.masi.utility.repository.rowmapper.ColumnConverter;
import com.masi.utility.repository.rowmapper.FileAttachmentRowMapper;
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
 * Spring Data R2DBC custom repository implementation for the FileAttachment entity.
 */
@SuppressWarnings("unused")
class FileAttachmentRepositoryInternalImpl extends SimpleR2dbcRepository<FileAttachment, UUID> implements FileAttachmentRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final FileAttachmentRowMapper fileattachmentMapper;
    private final ColumnConverter columnConverter;

    private static final Table entityTable = Table.aliased("file_attachment", EntityManager.ENTITY_ALIAS);

    public FileAttachmentRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        FileAttachmentRowMapper fileattachmentMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter,
        ColumnConverter columnConverter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(FileAttachment.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.fileattachmentMapper = fileattachmentMapper;
        this.columnConverter = columnConverter;
    }

    @Override
    public Flux<FileAttachment> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<FileAttachment> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = FileAttachmentSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        SelectFromAndJoin selectFrom = Select.builder().select(columns).from(entityTable);
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, FileAttachment.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<FileAttachment> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<FileAttachment> findById(UUID id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(StringUtils.wrap(id.toString(), "'")));
        return createQuery(null, whereClause).one();
    }

    private FileAttachment process(Row row, RowMetadata metadata) {
        FileAttachment entity = fileattachmentMapper.apply(row, "e");
        return entity;
    }

    @Override
    public <S extends FileAttachment> Mono<S> save(S entity) {
        return super.save(entity);
    }

    @Override
    public Flux<FileAttachment> findByCriteria(FileAttachmentCriteria fileAttachmentCriteria, Pageable page) {
        return createQuery(page, buildConditions(fileAttachmentCriteria)).all();
    }

    @Override
    public Mono<Long> countByCriteria(FileAttachmentCriteria criteria) {
        return findByCriteria(criteria, null)
            .collectList()
            .map(collectedList -> collectedList != null ? (long) collectedList.size() : (long) 0);
    }

    private Condition buildConditions(FileAttachmentCriteria criteria) {
        ConditionBuilder builder = new ConditionBuilder(this.columnConverter);
        List<Condition> allConditions = new ArrayList<Condition>();
        if (criteria != null) {
            if (criteria.getId() != null) {
                builder.buildFilterConditionForField(criteria.getId(), entityTable.column("id"));
            }
            if (criteria.getName() != null) {
                builder.buildFilterConditionForField(criteria.getName(), entityTable.column("name"));
            }
            if (criteria.getPath() != null) {
                builder.buildFilterConditionForField(criteria.getPath(), entityTable.column("path"));
            }
            if (criteria.getFileSize() != null) {
                builder.buildFilterConditionForField(criteria.getFileSize(), entityTable.column("file_size"));
            }
            if (criteria.getMimeType() != null) {
                builder.buildFilterConditionForField(criteria.getMimeType(), entityTable.column("mime_type"));
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
        }
        return builder.buildConditions();
    }
}
