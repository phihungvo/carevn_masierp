package com.masi.production.repository;

import com.masi.production.domain.WorkItem;
import com.masi.production.domain.WorkItem;
import com.masi.production.domain.WorkOrder;
import com.masi.production.domain.enumeration.WoStatus;
import com.masi.production.repository.rowmapper.ManufactureOrderRowMapper;
import com.masi.production.repository.rowmapper.WorkItemRowMapper;
import com.masi.production.repository.rowmapper.WorkOrderRowMapper;
import com.masi.production.service.dto.ManufactureWorkOrdersRO;
import com.masi.production.service.dto.WorkOrderRO;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Collectors;

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
import org.springframework.util.CollectionUtils;
import org.springframework.util.CollectionUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC custom repository implementation for the WorkOrder entity.
 */
@SuppressWarnings("unused")
class WorkOrderRepositoryInternalImpl extends SimpleR2dbcRepository<WorkOrder, UUID> implements WorkOrderRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final WorkItemRowMapper workItemMapper;
    private final ManufactureOrderRowMapper manufactureOrderMapper;
    private final WorkOrderRowMapper workOrderMapper;

    private static final Table entityTable = Table.aliased("work_order", EntityManager.ENTITY_ALIAS);
    private static final Table workItemTable = Table.aliased("work_item", "workItem");
    private static final Table manufactureOrderTable = Table.aliased("manufacture_order", "manufactureOrder");

    public WorkOrderRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        WorkItemRowMapper workItemMapper,
        ManufactureOrderRowMapper manufactureOrderMapper,
        WorkOrderRowMapper workOrderMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(WorkOrder.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.workItemMapper = workItemMapper;
        this.manufactureOrderMapper = manufactureOrderMapper;
        this.workOrderMapper = workOrderMapper;
    }

    @Override
    public Flux<WorkOrder> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<WorkOrder> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = WorkOrderSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(WorkItemSqlHelper.getColumns(workItemTable, "workItem"));
        columns.addAll(ManufactureOrderSqlHelper.getColumns(manufactureOrderTable, "manufactureOrder"));
        SelectFromAndJoinCondition selectFrom = Select.builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(workItemTable)
            .on(Column.create("work_item_id", entityTable))
            .equals(Column.create("id", workItemTable))
            .leftOuterJoin(manufactureOrderTable)
            .on(Column.create("manufacture_order_id", entityTable))
            .equals(Column.create("id", manufactureOrderTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, WorkOrder.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<WorkOrder> findAll() {
        return findAllBy(null);
    }

    public Pageable createDefaultSort(Pageable pageable) {
        Sort defaultSort = Sort.by(Sort.Direction.DESC, "created_at");
        if (Objects.isNull(pageable)) {
            pageable = PageRequest.of(0, Integer.MAX_VALUE, defaultSort);
        }
        if (pageable.getSort().equals(Sort.unsorted())) {
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), defaultSort);
        }
        return pageable;
    }

    @Override
    public Flux<WorkOrder> findAllByIsActive(Pageable pageable, Boolean isActive,String company, String department) {
        pageable = createDefaultSort(pageable);
        Condition matchIsActive = Conditions.isEqual(entityTable.column("is_active"), Conditions.just(StringUtils.wrap(isActive.toString(), "'")));
        Condition matchMoIsActive = Conditions.isEqual(manufactureOrderTable.column("is_active"), Conditions.just(StringUtils.wrap(isActive.toString(), "'")));
        Condition companyIsActive = Conditions.isEqual(entityTable.column("company"), Conditions.just(StringUtils.wrap(company, "'")));

        return createQuery(pageable, matchIsActive.and(matchMoIsActive)).all();
    }

    @Override
    public Mono<WorkOrder> findById(UUID id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(StringUtils.wrap(id.toString(), "'")));
        return createQuery(null, whereClause).one();
    }

    @Override
    public Mono<WorkOrder> findByIdAndIsActive(UUID id, Boolean isActive) {
        Condition matchId = Conditions.isEqual(entityTable.column("id"), Conditions.just(StringUtils.wrap(id.toString(), "'")));
        Condition matchIsActive = Conditions.isEqual(entityTable.column("is_active"), Conditions.just(StringUtils.wrap(isActive.toString(), "'")));
        return createQuery(null, matchId.and(matchIsActive)).one();
    }

    @Override
    public Mono<WorkOrder> findByIdAndFilter(WorkOrderRO ro, UUID id, Boolean isActive) {
        return createQuery(null, buildConditionFromRequestObject(id, ro)).one();
    }


    @Override
    public Flux<WorkOrder> findAllByManufactureOrderIdAndIsActive(UUID manufactureOrderId, Boolean isActive) {
        Sort defaultSort = Sort.by(Sort.Direction.ASC, "from_date");
        Pageable pageable = PageRequest.of(0, Integer.MAX_VALUE, defaultSort);
        Condition matchManufactureOrderId = Conditions.isEqual(entityTable.column("manufacture_order_id"), Conditions.just(StringUtils.wrap(manufactureOrderId.toString(), "'")));
        Condition matchIsActive = Conditions.isEqual(entityTable.column("is_active"), Conditions.just(StringUtils.wrap(isActive.toString(), "'")));
        return createQuery(pageable, matchManufactureOrderId.and(matchIsActive)).all();
    }

    @Override
    public Flux<WorkOrder> findAllByMoIdAndStatuses(ManufactureWorkOrdersRO moRo, UUID manufactureOrderId, Boolean isActive) {
        Sort defaultSort = Sort.by(Sort.Direction.ASC, "from_date");
        Pageable pageable = PageRequest.of(0, Integer.MAX_VALUE, defaultSort);
        return createQuery(pageable, buildConditionGetAll(moRo, manufactureOrderId)).all();
    }



    @Override
    public Mono<Long> countIsActive(Boolean isActive) {
        Condition matchIsActive = Conditions.isEqual(entityTable.column("is_active"), Conditions.just(StringUtils.wrap(isActive.toString(), "'")));
        Condition matchMoIsActive = Conditions.isEqual(manufactureOrderTable.column("is_active"), Conditions.just(StringUtils.wrap(isActive.toString(), "'")));
        return createQuery(null, matchIsActive.and(matchMoIsActive)).all().count();
    }

    private WorkOrder process(Row row, RowMetadata metadata) {
        WorkOrder entity = workOrderMapper.apply(row, "e");
        entity.setWorkItem(workItemMapper.apply(row, "workItem"));
        entity.setManufactureOrder(manufactureOrderMapper.apply(row, "manufactureOrder"));
        return entity;
    }

    @Override
    public <S extends WorkOrder> Mono<S> save(S entity) {
        return super.save(entity);
    }

    private Condition buildConditionFromRequestObject(UUID id, WorkOrderRO ro) {
        Condition whereClause = Conditions.isEqual(entityTable.column("is_active"), Conditions.just("true"));
        whereClause = whereClause.and(Conditions.isEqual(entityTable.column("id"), Conditions.just(StringUtils.wrap(id.toString(), "'"))));

        return whereClause;
    }

    private Condition buildConditionGetAll(ManufactureWorkOrdersRO moRO, UUID manufactureOrderId) {
        Condition whereClause = Conditions.isEqual(entityTable.column("is_active"), Conditions.just("true"));
        whereClause = whereClause.and(Conditions.isEqual(entityTable.column("manufacture_order_id"), Conditions.just(StringUtils.wrap(manufactureOrderId.toString(), "'"))));
        if (!CollectionUtils.isEmpty(moRO.getStatuses())) {
            String inCondition = "status IN (" + buildStatusInClause(moRO.getStatuses()) + ")";
            whereClause = whereClause.and(Conditions.isEqual(entityTable.column(inCondition), Conditions.just("true")));
        }
        if (moRO.getFromDate() != null && moRO.getToDate() != null) {
            whereClause = whereClause.and(Conditions.between(entityTable.column("from_date"),
                Conditions.just(StringUtils.wrap(moRO.getFromDate().toString(), "'")),
                Conditions.just(StringUtils.wrap(moRO.getToDate().toString(), "'"))));
        }
        return whereClause;
    }

    private String buildStatusInClause(List<WoStatus> statuses) {
        List<String> statusList = statuses.stream()
            .map(status -> "'" + status.name() + "'")
            .collect(Collectors.toList());
        return String.join(",", statusList);
    }
}
