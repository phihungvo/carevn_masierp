package com.masi.employee.repository;

import com.masi.employee.domain.ConfirmLeave;
import com.masi.employee.domain.ConfirmLeaveAttachment;
import com.masi.employee.repository.rowmapper.ConfirmLeaveAttachmentRowMapper;
import com.masi.employee.repository.rowmapper.ConfirmLeaveRowMapper;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.convert.R2dbcConverter;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.repository.support.SimpleR2dbcRepository;
import org.springframework.data.relational.core.sql.*;
import org.springframework.data.relational.core.sql.SelectBuilder.SelectFromAndJoin;
import org.springframework.data.relational.repository.support.MappingRelationalEntityInformation;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.r2dbc.core.RowsFetchSpec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuples;

/**
 * Spring Data R2DBC custom repository implementation for the ConfirmLeave entity.
 */
@SuppressWarnings("unused")
class ConfirmLeaveRepositoryInternalImpl extends SimpleR2dbcRepository<ConfirmLeave, UUID> implements ConfirmLeaveRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;
    private final ConfirmLeaveRowMapper confirmleaveMapper;
    private final ConfirmLeaveAttachmentRowMapper confirmLeaveAttachmentRowMapper;

    private static final Table entityTable = Table.aliased("confirm_leave", EntityManager.ENTITY_ALIAS);
    private static final Table fileAttachmentTable = Table.aliased("confirm_leave_attachment", "file_attachment");

    public ConfirmLeaveRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        ConfirmLeaveRowMapper confirmleaveMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter, ConfirmLeaveAttachmentRowMapper confirmLeaveAttachmentRowMapper
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(ConfirmLeave.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.confirmleaveMapper = confirmleaveMapper;
        this.confirmLeaveAttachmentRowMapper = confirmLeaveAttachmentRowMapper;
    }

    @Override
    public Flux<ConfirmLeave> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<ConfirmLeave> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = ConfirmLeaveSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        SelectBuilder.SelectFromAndJoin selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable);

        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, ConfirmLeave.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<ConfirmLeave> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<ConfirmLeave> findById(UUID id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(StringUtils.wrap(id.toString(), "'")));
        return createQuery(null, whereClause).one();
    }

    @Override
    public Mono<ConfirmLeave> findWithDetailById(UUID id) {
        List<Expression> columns = ConfirmLeaveSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(ConfirmLeaveAttachmentSqlHelper.getColumns(fileAttachmentTable, "file_attachment"));
        SelectBuilder.SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(fileAttachmentTable)
            .on(Column.create("id", entityTable))
            .equals(Column.create("confirm_leave_id", fileAttachmentTable));

        Condition whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(StringUtils.wrap(id.toString(), "'")));
        String select = entityManager.createSelect(selectFrom, ConfirmLeave.class, null, whereClause);
        ConcurrentHashMap<String, ConfirmLeave> confirmLeaveMap = new ConcurrentHashMap<>();
        return db.sql(select).map((row, metadata) -> {
            var confirmLeave = confirmleaveMapper.apply(row, "e");
            ConfirmLeaveAttachment confirmAttachment = confirmLeaveAttachmentRowMapper.apply(row, "file_attachment");
            return Tuples.of(confirmLeave, confirmAttachment);
        }).all().map(tuple -> {
            ConfirmLeave confirmLeave = tuple.getT1();
            assert confirmLeave.getId() != null;
            confirmLeaveMap.putIfAbsent(confirmLeave.getId().toString(), confirmLeave);
            ConfirmLeaveAttachment confirmAttachment = tuple.getT2();
            ConfirmLeave existingConfirmLeave = confirmLeaveMap.get(confirmLeave.getId().toString());
            if (existingConfirmLeave == null) {
                confirmLeaveMap.put(confirmLeave.getId().toString(), confirmLeave);
                existingConfirmLeave = confirmLeave;
            }
            existingConfirmLeave.addConfirmLeaveAttachment(confirmAttachment);
            return existingConfirmLeave;
        }).collectList().flatMap(confirmLeaves -> {
            if (confirmLeaves.isEmpty()) {
                return Mono.empty();
            }
            return Mono.just(confirmLeaves.get(0));
        });
    }

    private ConfirmLeave process(Row row, RowMetadata metadata) {
        ConfirmLeave entity = confirmleaveMapper.apply(row, "e");
        return entity;
    }

    @Override
    public <S extends ConfirmLeave> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
