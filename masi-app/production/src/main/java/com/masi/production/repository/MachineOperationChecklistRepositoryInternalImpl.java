package com.masi.production.repository;

import com.masi.production.domain.MachineOperationChecklist;
import com.masi.production.repository.rowmapper.MachineOperationChecklistRowMapper;
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
 * Spring Data R2DBC custom repository implementation for the MachineOperationChecklist entity.
 */
@SuppressWarnings("unused")
class MachineOperationChecklistRepositoryInternalImpl
    extends SimpleR2dbcRepository<MachineOperationChecklist, UUID>
    implements MachineOperationChecklistRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final WorkItemRowMapper workitemMapper;
    private final MachineOperationChecklistRowMapper machineoperationchecklistMapper;

    private static final Table entityTable = Table.aliased("machine_operation_checklist", EntityManager.ENTITY_ALIAS);
    private static final Table workItemTable = Table.aliased("work_item", "workItem");

    public MachineOperationChecklistRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        WorkItemRowMapper workitemMapper,
        MachineOperationChecklistRowMapper machineoperationchecklistMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(
                converter.getMappingContext().getRequiredPersistentEntity(MachineOperationChecklist.class)
            ),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.workitemMapper = workitemMapper;
        this.machineoperationchecklistMapper = machineoperationchecklistMapper;
    }

    @Override
    public Flux<MachineOperationChecklist> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<MachineOperationChecklist> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = MachineOperationChecklistSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(WorkItemSqlHelper.getColumns(workItemTable, "workItem"));
        SelectFromAndJoinCondition selectFrom = Select.builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(workItemTable)
            .on(Column.create("work_item_id", entityTable))
            .equals(Column.create("id", workItemTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, MachineOperationChecklist.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<MachineOperationChecklist> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<MachineOperationChecklist> findById(UUID id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(StringUtils.wrap(id.toString(), "'")));
        return createQuery(null, whereClause).one();
    }

    private MachineOperationChecklist process(Row row, RowMetadata metadata) {
        MachineOperationChecklist entity = machineoperationchecklistMapper.apply(row, "e");
        entity.setWorkItem(workitemMapper.apply(row, "workItem"));
        return entity;
    }

    @Override
    public <S extends MachineOperationChecklist> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
