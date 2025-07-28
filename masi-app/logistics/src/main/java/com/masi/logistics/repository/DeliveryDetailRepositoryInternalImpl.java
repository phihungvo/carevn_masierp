package com.masi.logistics.repository;

import com.masi.logistics.domain.DeliveryDetail;
import com.masi.logistics.domain.criteria.DeliveryDetailCriteria;
import com.masi.logistics.repository.rowmapper.*;
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
import org.springframework.data.relational.core.sql.*;
import org.springframework.data.relational.repository.support.MappingRelationalEntityInformation;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.r2dbc.core.RowsFetchSpec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.service.ConditionBuilder;

/**
 * Spring Data R2DBC custom repository implementation for the DeliveryDetail entity.
 */
@SuppressWarnings("unused")
class DeliveryDetailRepositoryInternalImpl extends SimpleR2dbcRepository<DeliveryDetail, UUID> implements DeliveryDetailRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;
    private final DeliveryScheduleRowMapper deliveryMapper;
    private final ItemRowMapper itemMapper;
    private final UomRowMapper uomMapper;
    private final SuppliersRowMapper supplierMapper;

    private final DeliveryDetailRowMapper deliverydetailMapper;
    private final ColumnConverter columnConverter;

    private static final Table entityTable = Table.aliased("delivery_detail", EntityManager.ENTITY_ALIAS);
    private static final Table deliveryTable = Table.aliased("delivery_schedule", "delivery");
    private static final Table itemTable = Table.aliased("item", "item");
    private static final Table uomTable = Table.aliased("uom", "uom");
    private static final Table suppliersTable = Table.aliased("suppliers", "suppliers");


    public DeliveryDetailRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        DeliveryDetailRowMapper deliverydetailMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter, DeliveryScheduleRowMapper deliveryMapper, ItemRowMapper itemMapper, UomRowMapper uomMapper, SuppliersRowMapper supplierMapper,
        ColumnConverter columnConverter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(DeliveryDetail.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.deliverydetailMapper = deliverydetailMapper;
        this.deliveryMapper = deliveryMapper;
        this.itemMapper = itemMapper;
        this.uomMapper = uomMapper;
        this.supplierMapper = supplierMapper;
        this.columnConverter = columnConverter;
    }

    @Override
    public Flux<DeliveryDetail> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<DeliveryDetail> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = DeliveryDetailSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(DeliveryScheduleSqlHelper.getColumns(deliveryTable, "delivery"));
        columns.addAll(ItemSqlHelper.getColumns(itemTable, "item"));
        columns.addAll(UomSqlHelper.getColumns(uomTable, "uom"));
        columns.addAll(SuppliersSqlHelper.getColumns(suppliersTable, "suppliers"));
        SelectBuilder.SelectFromAndJoinCondition selectFrom = Select.builder().select(columns).from(entityTable)
            .leftOuterJoin(deliveryTable)
            .on(entityTable.column("delivery_id"))
            .equals(deliveryTable.column("id"))

            .leftOuterJoin(itemTable)
            .on(entityTable.column("contract_material_id"))
            .equals(itemTable.column("id"))

            .leftOuterJoin(uomTable)
            .on(itemTable.column("uom_id"))
            .equals(uomTable.column("id"))

            .leftOuterJoin(suppliersTable)
            .on(itemTable.column("supplier_id"))
            .equals(suppliersTable.column("id"));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, DeliveryDetail.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<DeliveryDetail> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<DeliveryDetail> findById(UUID id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(StringUtils.wrap(id.toString(), "'")));
        return createQuery(null, whereClause).one();
    }

    @Override
    public Mono<DeliveryDetail> findByIdAndCompany(UUID id, String company) {
        Condition whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(StringUtils.wrap(id.toString(), "'")));
        whereClause = whereClause.and(Conditions.isEqual(entityTable.column("company"), Conditions.just(StringUtils.wrap(company, "'"))));
        whereClause = whereClause.and(Conditions.isNull(entityTable.column("deleted_at")));
        whereClause = whereClause.and(Conditions.isNull(entityTable.column("deleted_by")));
        return createQuery(null, whereClause).one();
    }

    private DeliveryDetail process(Row row, RowMetadata metadata) {
        DeliveryDetail entity = deliverydetailMapper.apply(row, "e");
        entity.setDeliverySchedule(deliveryMapper.apply(row, "delivery"));
        entity.setContractMaterial(itemMapper.apply(row, "item"));
        entity.getContractMaterial().setUom(uomMapper.apply(row, "uom"));
        entity.getContractMaterial().setSupplier(supplierMapper.apply(row, "suppliers"));
        return entity;
    }

    @Override
    public <S extends DeliveryDetail> Mono<S> save(S entity) {
        return super.save(entity);
    }

    private Pageable getDefaultSort(Pageable pageable) {
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
    public Flux<DeliveryDetail> findByCriteria(DeliveryDetailCriteria deliveryDetailCriteria, Pageable page) {
        page = getDefaultSort(page);
        return createQuery(page, buildConditions(deliveryDetailCriteria)).all();
    }

    @Override
    public Mono<Long> countByCriteria(DeliveryDetailCriteria criteria) {
        return findByCriteria(criteria, null)
            .collectList()
            .map(collectedList -> collectedList != null ? (long) collectedList.size() : (long) 0);
    }

    private Condition buildConditions(DeliveryDetailCriteria criteria) {
        ConditionBuilder builder = new ConditionBuilder(this.columnConverter);
        List<Condition> allConditions = new ArrayList<Condition>();
        if (criteria != null) {
            if (criteria.getId() != null) {
                builder.buildFilterConditionForField(criteria.getId(), entityTable.column("id"));
            }
            if (criteria.getDeliveryId() != null) {
                builder.buildFilterConditionForField(criteria.getDeliveryId(), entityTable.column("delivery_id"));
            }
            if (criteria.getContractMaterialId() != null) {
                builder.buildFilterConditionForField(criteria.getContractMaterialId(), entityTable.column("contract_material_id"));
            }
            if (criteria.getQuantity() != null) {
                builder.buildFilterConditionForField(criteria.getQuantity(), entityTable.column("quantity"));
            }
            if (criteria.getUomId() != null) {
                builder.buildFilterConditionForField(criteria.getUomId(), entityTable.column("uom_id"));
            }
            if (criteria.getPrice() != null) {
                builder.buildFilterConditionForField(criteria.getPrice(), entityTable.column("price"));
            }
            if (criteria.getCreatedAt() != null) {
                builder.buildFilterConditionForField(criteria.getCreatedAt(), entityTable.column("created_at"));
            }
            if (criteria.getUpdatedAt() != null) {
                builder.buildFilterConditionForField(criteria.getUpdatedAt(), entityTable.column("updated_at"));
            }
            if (criteria.getCreatedBy() != null) {
                builder.buildFilterConditionForField(criteria.getCreatedBy(), entityTable.column("created_by"));
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
            if (criteria.getDeliveryDate() != null){
                builder.buildFilterConditionForField(criteria.getDeliveryDate(), entityTable.column("delivery_date"));
            }

            if (criteria.getSupplierContractId() != null) {
                builder.buildFilterConditionForField(criteria.getSupplierContractId(), entityTable.column("supplier_contract_id"));
            }
            if (criteria.getCompany() != null) {
                builder.buildFilterConditionForField(criteria.getCompany(), entityTable.column("company"));
            }
        }
        return builder.buildConditions();
    }
}
