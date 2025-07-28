package com.masi.production.repository;

import com.masi.production.domain.MetalDetectionChecklist;
import com.masi.production.repository.rowmapper.MetalDetectionChecklistRowMapper;
import com.masi.production.repository.rowmapper.WorkItemRowMapper;
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
 * Spring Data R2DBC custom repository implementation for the MetalDetectionChecklist entity.
 */
@SuppressWarnings("unused")
class MetalDetectionChecklistRepositoryInternalImpl
    extends SimpleR2dbcRepository<MetalDetectionChecklist, UUID>
    implements MetalDetectionChecklistRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final WorkItemRowMapper workitemMapper;
    private final MetalDetectionChecklistRowMapper metaldetectionchecklistMapper;

    private static final Table entityTable = Table.aliased("metal_detection_checklist", EntityManager.ENTITY_ALIAS);
    private static final Table workItemTable = Table.aliased("work_item", "workItem");

    public MetalDetectionChecklistRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        WorkItemRowMapper workitemMapper,
        MetalDetectionChecklistRowMapper metaldetectionchecklistMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(
                converter.getMappingContext().getRequiredPersistentEntity(MetalDetectionChecklist.class)
            ),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.workitemMapper = workitemMapper;
        this.metaldetectionchecklistMapper = metaldetectionchecklistMapper;
    }

    @Override
    public Flux<MetalDetectionChecklist> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<MetalDetectionChecklist> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = MetalDetectionChecklistSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(WorkItemSqlHelper.getColumns(workItemTable, "workItem"));
        SelectFromAndJoinCondition selectFrom = Select.builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(workItemTable)
            .on(Column.create("work_item_id", entityTable))
            .equals(Column.create("id", workItemTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, MetalDetectionChecklist.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<MetalDetectionChecklist> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<MetalDetectionChecklist> findById(UUID id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(StringUtils.wrap(id.toString(), "'")));
        return createQuery(null, whereClause).one();
    }

    private MetalDetectionChecklist process(Row row, RowMetadata metadata) {
        MetalDetectionChecklist entity = metaldetectionchecklistMapper.apply(row, "e");
        entity.setWorkItem(workitemMapper.apply(row, "workItem"));
        return entity;
    }

    @Override
    public <S extends MetalDetectionChecklist> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
