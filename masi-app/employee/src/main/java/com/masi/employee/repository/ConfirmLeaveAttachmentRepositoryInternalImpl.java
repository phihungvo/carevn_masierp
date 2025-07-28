package com.masi.employee.repository;

import com.masi.employee.domain.ConfirmLeaveAttachment;
import com.masi.employee.repository.rowmapper.ConfirmLeaveAttachmentRowMapper;
import com.masi.employee.repository.rowmapper.ConfirmLeaveRowMapper;
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

/**
 * Spring Data R2DBC custom repository implementation for the ConfirmLeaveAttachment entity.
 */
@SuppressWarnings("unused")
class ConfirmLeaveAttachmentRepositoryInternalImpl
    extends SimpleR2dbcRepository<ConfirmLeaveAttachment, UUID>
    implements ConfirmLeaveAttachmentRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final ConfirmLeaveRowMapper confirmleaveMapper;
    private final ConfirmLeaveAttachmentRowMapper confirmleaveattachmentMapper;

    private static final Table entityTable = Table.aliased("confirm_leave_attachment", EntityManager.ENTITY_ALIAS);
    private static final Table confirmLeaveTable = Table.aliased("confirm_leave", "confirmLeave");

    public ConfirmLeaveAttachmentRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        ConfirmLeaveRowMapper confirmleaveMapper,
        ConfirmLeaveAttachmentRowMapper confirmleaveattachmentMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(ConfirmLeaveAttachment.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.confirmleaveMapper = confirmleaveMapper;
        this.confirmleaveattachmentMapper = confirmleaveattachmentMapper;
    }

    @Override
    public Flux<ConfirmLeaveAttachment> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<ConfirmLeaveAttachment> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = ConfirmLeaveAttachmentSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(ConfirmLeaveSqlHelper.getColumns(confirmLeaveTable, "confirmLeave"));
        SelectFromAndJoinCondition selectFrom = Select.builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(confirmLeaveTable)
            .on(Column.create("confirm_leave_id", entityTable))
            .equals(Column.create("id", confirmLeaveTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, ConfirmLeaveAttachment.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<ConfirmLeaveAttachment> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<ConfirmLeaveAttachment> findById(UUID id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(StringUtils.wrap(id.toString(), "'")));
        return createQuery(null, whereClause).one();
    }

    private ConfirmLeaveAttachment process(Row row, RowMetadata metadata) {
        ConfirmLeaveAttachment entity = confirmleaveattachmentMapper.apply(row, "e");
        entity.setConfirmLeave(confirmleaveMapper.apply(row, "confirmLeave"));
        return entity;
    }

    @Override
    public <S extends ConfirmLeaveAttachment> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
