package com.masi.production.repository;

import com.masi.production.domain.ProductPackage;
import com.masi.production.domain.enumeration.StatusEntity;
import com.masi.production.repository.rowmapper.ManufactureOrderRowMapper;
import com.masi.production.repository.rowmapper.ProductPackageRowMapper;
import com.masi.production.repository.rowmapper.WorkOrderRowMapper;
import com.masi.production.service.dto.ProductPackageQuery;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

/**
 * Spring Data R2DBC custom repository implementation for the ProductPackage entity.
 */
@SuppressWarnings("unused")
class ProductPackageRepositoryInternalImpl extends SimpleR2dbcRepository<ProductPackage, UUID> implements ProductPackageRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final WorkOrderRowMapper workorderMapper;
    private final ProductPackageRowMapper productpackageMapper;
    private final ManufactureOrderRowMapper manufactureOrderRowMapper;

    private static final Table entityTable = Table.aliased("product_package", EntityManager.ENTITY_ALIAS);
    private static final Table workOrderTable = Table.aliased("work_order", "workOrder");
    private static final Table sampleDisposalTable = Table.aliased("sample_disposal", "sampleDisposal");
    private static final Table qualityCheckSampleTable = Table.aliased("quality_check_sample", "qualityCheckSample");
    private static final Table manufactureOrderTable = Table.aliased("manufacture_order", "manufactureOrder");
    public ProductPackageRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        WorkOrderRowMapper workorderMapper,
        ProductPackageRowMapper productpackageMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter, ManufactureOrderRowMapper manufactureOrderRowMapper
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(ProductPackage.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.workorderMapper = workorderMapper;
        this.productpackageMapper = productpackageMapper;
        this.manufactureOrderRowMapper = manufactureOrderRowMapper;
    }

    @Override
    public Flux<ProductPackage> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<ProductPackage> createQuery(Pageable pageable, Condition whereClause) {
        return createQuery(pageable, whereClause, null);
    }

    RowsFetchSpec<ProductPackage> createQuery(Pageable pageable, Condition whereClause, Map<String, Object> parameters) {
        List<Expression> columns = ProductPackageSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(WorkOrderSqlHelper.getColumns(workOrderTable, "workOrder"));
        columns.addAll(QualityCheckSampleSqlHelper.getColumns(qualityCheckSampleTable, "qualityCheckSample"));
        columns.addAll(ManufactureOrderSqlHelper.getColumns(manufactureOrderTable, "manufactureOrder"));
        SelectFromAndJoinCondition selectFrom = Select.builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(workOrderTable)
            .on(Column.create("work_order_id", entityTable))
            .equals(Column.create("id", workOrderTable))
            .leftOuterJoin(qualityCheckSampleTable)
            .on(Column.create("package_id", qualityCheckSampleTable))
            .equals(Column.create("id", entityTable))
            .leftOuterJoin(manufactureOrderTable)
            .on(Column.create("manufacture_order_id", entityTable))
            .equals(Column.create("id", manufactureOrderTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, ProductPackage.class, pageable, whereClause);
        if (parameters != null && !parameters.isEmpty()) {
            return db.sql(select).bindValues(parameters).map(this::process);
        }
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<ProductPackage> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<ProductPackage> findById(UUID id) {
        Condition whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(StringUtils.wrap(id.toString(), "'")));
        whereClause = whereClause.and(Conditions.isEqual(entityTable.column("is_deleted"), Conditions.just("false")));
        return createQuery(null, whereClause).one();
    }

    private ProductPackage process(Row row, RowMetadata metadata) {
        ProductPackage entity = productpackageMapper.apply(row, "e");
        entity.setWorkOrder(workorderMapper.apply(row, "workOrder"));
        entity.setManufactureOrder(manufactureOrderRowMapper.apply(row, "manufactureOrder"));
        return entity;
    }

    @Override
    public <S extends ProductPackage> Mono<S> save(S entity) {
        return super.save(entity);
    }

    private Tuple2<Condition, Map<String, Object>> createWhereClauses(ProductPackageQuery query) {
        Condition whereClause = Conditions.isEqual(entityTable.column("is_deleted"), Conditions.just("false"));
        Map<String, Object> parameters = new HashMap<>();

        if (query.getId() != null){
            whereClause = whereClause.and(Conditions.isEqual(entityTable.column("id"), Conditions.just(StringUtils.wrap(query.getId().toString(), "'"))));
        }
        if (!Strings.isBlank(query.getSearch())) {
            String columnAlias = EntityManager.ENTITY_ALIAS + "." + "package_code";
            String columnAlias2 = EntityManager.ENTITY_ALIAS + "." + "note";
            // tìm theo mã sản phẩm thì không cần gỡ dấu
            StringBuilder inBuilder = new StringBuilder();
            inBuilder.append("(");
            String inCondition = String.format(" unaccent(%s)", columnAlias) + "  iLIKE unaccent(:search) or";
            inBuilder.append(inCondition);
            String inCondition2 = String.format(" unaccent(%s)", columnAlias2) + "  iLIKE unaccent(:search)";
            inBuilder.append(inCondition2);
            inBuilder.append(" )");
            whereClause = whereClause.and(Conditions.just(inBuilder.toString()));
            parameters.put("search", "%" + query.getSearch() + "%");

        }
        if (query.getWorkOrderIds() != null && !query.getWorkOrderIds().isEmpty()) {
            AtomicInteger index = new AtomicInteger(0);
            String inValues = query.getWorkOrderIds().stream().map((uid) -> {
                parameters.put("workOrderId" + index.get(), uid);
                index.getAndIncrement();
                return ":workOrderId" + (index.get() - 1);
            }).reduce((s1, s2) -> s1 + ", " + s2).orElseThrow();
            whereClause = whereClause.and(Conditions.in(entityTable.column("work_order_id"), Conditions.just(inValues)));
        }
        if (Objects.nonNull(query.getCompany())) {
            whereClause = whereClause.and(Conditions.isEqual(entityTable.column("company"), Conditions.just(StringUtils.wrap(query.getCompany(), "'"))));
        }
//        if (Objects.nonNull(query.getMoIds())) {
//            whereClause = whereClause.and(Conditions.isEqual(entityTable.column("manufacture_order_id"), Conditions.just(StringUtils.wrap(query.getMoIds().toString(), "'"))));
//        }

        if (CollectionUtils.isNotEmpty(query.getManufactureOrderIds())) {
            String inClause = query.getManufactureOrderIds().stream().map(e -> "'" + e + "'").reduce((a, b) -> a + "," + b).orElseThrow();
            whereClause = whereClause.and(Conditions.in(entityTable.column("manufacture_order_id"), Conditions.just(inClause)));
        }

        if (CollectionUtils.isNotEmpty(query.getStatuses())){
            String inClause = query.getStatuses().stream().map(e -> "'" + e + "'").reduce((a, b) -> a + "," + b).orElseThrow();
            whereClause = whereClause.and(Conditions.in(entityTable.column("status"), Conditions.just(inClause)));
        }

        if (Objects.nonNull(query.getIsHasQC())) {
            if (query.getIsHasQC()) {
                Condition qcCondition = qualityCheckSampleTable.column("id").isNotNull();
                whereClause = whereClause.and(Conditions.nest(qcCondition.or(Conditions.just("false"))));
            } else {
                whereClause = whereClause.and(qualityCheckSampleTable.column("id").isNull());
            }
        }

        if (Objects.nonNull(query.getIsProductMaintainSpecified())) {
            Table productMaintainTable = Table.aliased("product_maintain", "pm");
            Table moTable = Table.aliased("manufacture_order", "mo");
            if (query.getIsProductMaintainSpecified()) {
                // Chọn các product_package có liên kết với product_maintain
                whereClause = whereClause.and(
                    entityTable.column("id").in(
                        Select.builder()
                            .select(productMaintainTable.column("product_package_id"))
                            .from(productMaintainTable)
                            .build()
                    )
                );
            } else {
                // Chọn các product_package không có liên kết với product_maintain và manufacture_order ở trạng thái
                whereClause = whereClause.and(
                    entityTable.column("id").notIn(
                        Select.builder()
                            .select(productMaintainTable.column("product_package_id"))
                            .from(productMaintainTable)
                            .build()
                    )
                );
                whereClause = whereClause.and(manufactureOrderTable.column("status").isEqualTo(Conditions.just(StringUtils.wrap(StatusEntity.PACKED_COMPLETED.name(), "'"))));

            }
        }


//            if (Objects.nonNull(ro.getDepartment())) {
//                whereClause = whereClause.and(Conditions.isEqual(entityTable.column("department"), Conditions.just(StringUtils.wrap(RO.getDepartment(), "'"))));
//            }
        return Tuples.of(whereClause, parameters);
    }

    @Override
    public Flux<ProductPackage> findAllByQuery(ProductPackageQuery query, Pageable pageable) {
        Sort defaultSort = Sort.by(Sort.Direction.DESC, "created_at");
        if (Objects.isNull(pageable)) {
            pageable = PageRequest.of(0, Integer.MAX_VALUE, defaultSort);
        }
        if (pageable.getSort().equals(Sort.unsorted())) {
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), defaultSort);
        }
        Tuple2<Condition, Map<String, Object>> whereClauses = createWhereClauses(query);
        return createQuery(pageable, whereClauses.getT1(), whereClauses.getT2()).all();
    }

    @Override
    public Mono<Long> countByQuery(ProductPackageQuery query) {
        return createQuery(null, createWhereClauses(query).getT1(), createWhereClauses(query).getT2()).all().count();
    }
}
