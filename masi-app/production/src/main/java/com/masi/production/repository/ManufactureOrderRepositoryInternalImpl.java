package com.masi.production.repository;

import com.masi.production.domain.ManufactureOrder;
import com.masi.production.domain.enumeration.MoStatus;
import com.masi.production.domain.enumeration.StatusEntity;
import com.masi.production.repository.rowmapper.*;
import com.masi.production.service.dto.ManufactureOrderRO;
import com.masi.production.service.dto.ManufactureWorkOrdersRO;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;

import java.util.*;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.r2dbc.convert.R2dbcConverter;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.repository.support.SimpleR2dbcRepository;
import org.springframework.data.relational.core.sql.*;
import org.springframework.data.relational.core.sql.SelectBuilder.SelectFromAndJoin;
import org.springframework.data.relational.repository.support.MappingRelationalEntityInformation;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.r2dbc.core.RowsFetchSpec;
import org.springframework.util.CollectionUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC custom repository implementation for the ManufactureOrder entity.
 */
@SuppressWarnings("unused")
class ManufactureOrderRepositoryInternalImpl
    extends SimpleR2dbcRepository<ManufactureOrder, UUID>
    implements ManufactureOrderRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;
    private final ColumnConverter columnConverter;

    private final ManufactureOrderRowMapper manufactureOrderRowMapper;
    private final QualityCheckSampleRowMapper qualityCheckSampleRowMapper;
    private final ProductPackageRowMapper productPackageRowMapper;
    private final ProductMaintainRowMapper productMaintainRowMapper;
    private final ProductRoutingRowMapper productRoutingRowMapper;
    private final SampleDisposalRowMapper sampleDisposalRowMapper;

    private static final Table entityTable = Table.aliased("manufacture_order", EntityManager.ENTITY_ALIAS);

    private static final Table qualityCheckSampleTable = Table.aliased("quality_check_sample", "quality_check_sample");
    private static final Table productPackageTable = Table.aliased("product_package", "product_package");
    private static final Table productMaintainTable = Table.aliased("product_maintain", "product_maintain");
    private static final Table productRoutingTable = Table.aliased("product_routing", "production_routing");
    private static final Table sampleDisposalTable = Table.aliased("sample_disposal", "sample_disposal");


    public ManufactureOrderRepositoryInternalImpl(
            R2dbcEntityTemplate template,
            EntityManager entityManager,
            ManufactureOrderRowMapper manufactureOrderRowMapper,
            R2dbcEntityOperations entityOperations,
            R2dbcConverter converter,
            ColumnConverter columnConverter,
            QualityCheckSampleRowMapper qualityCheckSampleRowMapper,
            ProductPackageRowMapper productPackageRowMapper,
            ProductMaintainRowMapper productMaintainRowMapper,
            ProductRoutingRowMapper productRoutingRowMapper,
            SampleDisposalRowMapper sampleDisposalRowMapper
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(ManufactureOrder.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.manufactureOrderRowMapper = manufactureOrderRowMapper;
        this.columnConverter = columnConverter;
        this.qualityCheckSampleRowMapper = qualityCheckSampleRowMapper;
        this.productPackageRowMapper = productPackageRowMapper;
        this.productMaintainRowMapper = productMaintainRowMapper;
        this.productRoutingRowMapper = productRoutingRowMapper;
        this.sampleDisposalRowMapper = sampleDisposalRowMapper;
    }

    @Override
    public Flux<ManufactureOrder> findAllBy(Pageable pageable) {
        if (Objects.isNull(pageable) || pageable.getSort().equals(Sort.unsorted())) {
            pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.DESC, "created_at"));
        }
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<ManufactureOrder> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = ManufactureOrderSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);

        columns.addAll(QualityCheckSampleSqlHelper.getColumns(qualityCheckSampleTable, "quality_check_sample"));
        columns.addAll(ProductPackageSqlHelper.getColumns(productPackageTable, "product_package"));
        columns.addAll(ProductMaintainSqlHelper.getColumns(productMaintainTable, "product_maintain"));
        columns.addAll(ProductRoutingSqlHelper.getColumns(productRoutingTable, "product_routing"));
        columns.addAll(SampleDisposalSqlHelper.getColumns(sampleDisposalTable, "sample_disposal"));

        SelectBuilder.SelectFromAndJoinCondition selectFrom = Select.builder()
                .select(columns)
                .from(entityTable)


                .leftOuterJoin(productPackageTable)
                .on(Column.create("product_package_id", entityTable))
                .equals(Column.create("id", productPackageTable))

                .leftOuterJoin(qualityCheckSampleTable)
                .on(Column.create("id", productPackageTable))
                .equals(Column.create("package_id", qualityCheckSampleTable))

                .leftOuterJoin(productMaintainTable)
                .on(Column.create("product_maintain_id", entityTable))
                .equals(Column.create("id", productMaintainTable))

                .leftOuterJoin(productRoutingTable)
                .on(Column.create("production_routing_id", entityTable))
                .equals(Column.create("id", productRoutingTable))

                .join(sampleDisposalTable, Join.JoinType.LEFT_OUTER_JOIN)
                .on(Column.create("disposal_id", qualityCheckSampleTable))
                .equals(Column.create("id", sampleDisposalTable))

                ;
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, ManufactureOrder.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<ManufactureOrder> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<ManufactureOrder> findById(UUID id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(StringUtils.wrap(id.toString(), "'")));
        return createQuery(null, whereClause).one();
    }

    @Override
    public Mono<ManufactureOrder> findByIdManufacture(UUID id) {
        Condition whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(StringUtils.wrap(id.toString(), "'")));
        whereClause = whereClause.and(Conditions.isEqual(entityTable.column("is_active"), Conditions.just("true")));

        return createQuery(null, whereClause).first().defaultIfEmpty(new ManufactureOrder());
    }

    @Override
    public Flux<ManufactureOrder> findAllByFilter(ManufactureOrderRO moRO, Pageable pageable) {
        pageable = getDefaultSort(pageable);
        return createQuery(pageable, buildWhereClause(moRO)).all();
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
    public Flux<ManufactureOrder> findAllWithWorkOrdersByFilter(ManufactureWorkOrdersRO moRO, Pageable pageable) {
        Sort defaultSort = Sort.by(Sort.Direction.DESC, "created_at");
        if (Objects.isNull(pageable)) {
            int pageNumber = 0;
            int pageSize = Integer.MAX_VALUE;
            pageable = PageRequest.of(pageNumber, pageSize, defaultSort);
        } else if (pageable.getSort().equals(Sort.unsorted())) {
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), defaultSort);
        }
        return createQuery(pageable, buildConditionWithWorkOrder(moRO)).all();
    }

    @Override
    public Mono<Long> countAllByFilter(ManufactureOrderRO moRO) {
        Condition condition = buildWhereClause(moRO);
        return createQuery(null, condition).all().count().doOnError(RuntimeException::new);
    }

    private Condition buildWhereClause(ManufactureOrderRO moRO) {
        Condition whereClause = Conditions.isEqual(entityTable.column("is_active"), Conditions.just("true"));
//        whereClause = whereClause.and(Conditions.isEqual(qualityCheckSampleTable.column("is_active"), Conditions.just("true")));
//        whereClause = whereClause.and(Conditions.isEqual(productMaintainTable.column("is_deleted"), Conditions.just("false")));
//        whereClause = whereClause.and(Conditions.isEqual(productPackageTable.column("is_deleted"), Conditions.just("false")));
//        whereClause = whereClause.and(Conditions.isEqual(productRoutingTable.column("is_deleted"), Conditions.just("false")));

        if (Objects.nonNull(moRO)) {
            //append where clause with date between if RO fromDate and toDate are not null

            if (Objects.nonNull(moRO.getTypePage())) {
                whereClause = whereClause.and(Conditions.isEqual(entityTable.column("manufacture_order_type"), Conditions.just(StringUtils.wrap(moRO.getTypePage(), "'"))));
            }

            if (Objects.nonNull(moRO.getFromDate()) && Objects.nonNull(moRO.getToDate())) {
                whereClause = whereClause.and(Conditions.between(entityTable.column("to_date"),
                    Conditions.just(StringUtils.wrap(moRO.getFromDate().toString(), "'")),
                    Conditions.just(StringUtils.wrap(moRO.getToDate().toString(), "'"))));
            }
            //append where clause with status in if RO status is not null
            if (!CollectionUtils.isEmpty(moRO.getStatuses())) {
                String inCondition = "status IN (" + buildStatusInClause(moRO.getStatuses()) + ")";
                whereClause = whereClause.and(Conditions.isEqual(entityTable.column(inCondition), Conditions.just("true")));
            }
            //append where clause with search string if column 'name' contain search string
            if (StringUtils.isNotBlank(moRO.getSearchString())) {
                String column = EntityManager.ENTITY_ALIAS + ".name";
                String inCondition = "unaccent(" + column + ") iLIKE unaccent('%" + moRO.getSearchString() + "%')";
                whereClause = whereClause.and(Conditions.just(inCondition));
            }
            if (Objects.nonNull(moRO.getCompany())) {
                whereClause = whereClause.and(Conditions.isEqual(entityTable.column("company"), Conditions.just(StringUtils.wrap(moRO.getCompany(), "'"))));
            }
            if (Objects.nonNull(moRO.getManufactureOrderType())) {
                whereClause = whereClause.and(Conditions.isEqual(entityTable.column("manufacture_order_type"), Conditions.just(StringUtils.wrap(moRO.getManufactureOrderType().toString(), "'"))));
            }
            if (Objects.nonNull(moRO.getIsProductMaintainSpecified())){
                if (moRO.getIsProductMaintainSpecified()) {
                    whereClause = whereClause.and(entityTable.column("product_maintain_id").isNotNull());
                } else {
                    whereClause = whereClause.and(Conditions.isNull(entityTable.column("product_maintain_id")));
                }
            }
            if (Objects.nonNull(moRO.getIsProductPackageSpecified())){
                if (moRO.getIsProductPackageSpecified()) {
                    whereClause = whereClause.and(entityTable.column("product_package_id").isNotNull());
                } else {
                    whereClause = whereClause.and(Conditions.isNull(entityTable.column("product_package_id")));
                }
            }
            if (Objects.nonNull(moRO.getIsProductRoutingSpecified())){
                if (moRO.getIsProductRoutingSpecified()) {
                    whereClause = whereClause.and(entityTable.column("production_routing_id").isNotNull());
                } else {
                    whereClause = whereClause.and(Conditions.isNull(entityTable.column("production_routing_id")));
                }
            }

//            if (Objects.nonNull(moRO.getManufactureOrderType())) {
//                whereClause = whereClause.and(Conditions.isEqual(entityTable.column("manufacture_order_type"), Conditions.just(StringUtils.wrap(moRO.getManufactureOrderType().toString(), "'"))));
//            }
//            if (Objects.nonNull(ro.getDepartment())) {
//                whereClause = whereClause.and(Conditions.isEqual(entityTable.column("department"), Conditions.just(StringUtils.wrap(RO.getDepartment(), "'"))));
//            }
        }

        return whereClause;
    }

    private ManufactureOrder process(Row row, RowMetadata metadata) {
        ManufactureOrder entity = manufactureOrderRowMapper.apply(row, "e");

        entity.setQualityCheckSample(qualityCheckSampleRowMapper.apply(row, "quality_check_sample"));
        entity.setProductPackage(productPackageRowMapper.apply(row, "product_package"));
        entity.setProductMaintain(productMaintainRowMapper.apply(row, "product_maintain"));
        entity.setProductionRouting(productRoutingRowMapper.apply(row, "product_routing"));
//        System.out.println("sampleDisposalRowMapper: " + sampleDisposalRowMapper.apply(row, "sample_disposal"));
        entity.getQualityCheckSample().setDisposal(sampleDisposalRowMapper.apply(row, "sample_disposal"));
        return entity;
    }

    @Override
    public <S extends ManufactureOrder> Mono<S> save(S entity) {
        return super.save(entity);
    }

    private String buildStatusInClause(List<StatusEntity> statuses) {
        List<String> statusList = statuses.stream()
            .map(status -> "'" + status.name() + "'")
            .collect(Collectors.toList());
        return String.join(",", statusList);
    }

    private Condition buildConditionWithWorkOrder(ManufactureWorkOrdersRO moRO) {
        Condition whereClause = Conditions.isEqual(entityTable.column("is_active"), Conditions.just("true"));

        if (Objects.nonNull(moRO)) {
            //append where clause with search string if column 'name' contain search string
            if (StringUtils.isNotBlank(moRO.getSearchString())) {
                String column = EntityManager.ENTITY_ALIAS + ".name";
                String inCondition = "unaccent(" + column + ") iLIKE unaccent('%" + moRO.getSearchString() + "%')";
                whereClause = whereClause.and(Conditions.just(inCondition));
            }
            if (moRO.getMoStatuses() != null && !moRO.getMoStatuses().isEmpty()) {
                String inCondition = moRO.getMoStatuses().stream().map(status -> "'" + status.name() + "'").collect(Collectors.joining(", "));
                whereClause = whereClause.and(Conditions.in(entityTable.column("status"), Conditions.just(inCondition)));
            }
            if(StringUtils.isNotBlank(moRO.getManufactureOrderType().toString())) {
                whereClause = whereClause.and(Conditions.isEqual(entityTable.column("manufacture_order_type"), Conditions.just(StringUtils.wrap(moRO.getManufactureOrderType().toString(), "'"))));
            }
        }

        return whereClause;
    }
}
