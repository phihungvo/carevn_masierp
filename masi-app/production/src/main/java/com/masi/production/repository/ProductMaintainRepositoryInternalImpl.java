package com.masi.production.repository;

import com.masi.production.domain.ProductMaintain;
import com.masi.production.domain.ProductPackage;
import com.masi.production.domain.ProductRouting;
import com.masi.production.domain.enumeration.StatusEntity;
import com.masi.production.repository.rowmapper.ManufactureOrderRowMapper;
import com.masi.production.repository.rowmapper.ProductMaintainRowMapper;
import com.masi.production.repository.rowmapper.ProductPackageRowMapper;
import com.masi.production.service.dto.ProductMaintainQuery;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
import org.springframework.data.relational.core.sql.SelectBuilder.SelectFromAndJoin;
import org.springframework.data.relational.repository.support.MappingRelationalEntityInformation;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.r2dbc.core.RowsFetchSpec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC custom repository implementation for the ProductMaintain entity.
 */
@SuppressWarnings("unused")
class ProductMaintainRepositoryInternalImpl extends SimpleR2dbcRepository<ProductMaintain, UUID> implements ProductMaintainRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final ProductMaintainRowMapper productmaintainMapper;
    private final ProductPackageRowMapper productPackageMapper;
    private final ManufactureOrderRowMapper manufactureOrderMapper;

    private static final Table entityTable = Table.aliased("product_maintain", EntityManager.ENTITY_ALIAS);
    private static final Table productPackageTable = Table.aliased("product_package", "productPackage");
    private static final Table manufactureOrderTable = Table.aliased("manufacture_order", "manufactureOrder");

    public ProductMaintainRepositoryInternalImpl(R2dbcEntityTemplate template, EntityManager entityManager, ProductMaintainRowMapper productmaintainMapper, R2dbcEntityOperations entityOperations, R2dbcConverter converter, ProductPackageRowMapper productPackageMapper, ManufactureOrderRowMapper manufactureOrderMapper) {
        super(new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(ProductMaintain.class)), entityOperations, converter);
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.productmaintainMapper = productmaintainMapper;
        this.productPackageMapper = productPackageMapper;
        this.manufactureOrderMapper = manufactureOrderMapper;
    }

    @Override
    public Flux<ProductMaintain> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<ProductMaintain> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = ProductMaintainSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(ProductPackageSqlHelper.getColumns(productPackageTable, "productPackage"));
        columns.addAll(ManufactureOrderSqlHelper.getColumns(manufactureOrderTable, "manufactureOrder"));
        SelectBuilder.SelectFromAndJoinCondition selectFrom = Select.builder().select(columns).from(entityTable)
            .leftOuterJoin(productPackageTable).on(Column.create("product_package_id", entityTable))
            .equals(Column.create("id", productPackageTable))
            .leftOuterJoin(manufactureOrderTable)
            .on(Column.create("manufacture_order_id", productPackageTable))
            .equals(Column.create("id", manufactureOrderTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, ProductMaintain.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<ProductMaintain> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<ProductMaintain> findByIdAndIsDeletedIsFalse(UUID id) {
        Condition whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(StringUtils.wrap(id.toString(), "'")));
        whereClause = whereClause.and(Conditions.isEqual(entityTable.column("is_deleted"), Conditions.just("false")));
        return createQuery(null, whereClause).one();
    }


    private ProductMaintain process(Row row, RowMetadata metadata) {
        ProductMaintain entity = productmaintainMapper.apply(row, "e");
        entity.setProductPackage(productPackageMapper.apply(row, "productPackage"));
        entity.getProductPackage().setManufactureOrder(manufactureOrderMapper.apply(row, "manufactureOrder"));
        return entity;
    }

    @Override
    public <S extends ProductMaintain> Mono<S> save(S entity) {
        return super.save(entity);
    }

    @Override
    public Mono<Long> countByFilter(ProductMaintainQuery query) {
        Condition condition = buildConditionFromRequestObject(query);
        return createQuery(null, condition).all().count().doOnError(throwable -> new RuntimeException(throwable));
    }

    @Override
    public Flux<ProductMaintain> findAllByFilter(ProductMaintainQuery query, Pageable pageable) {
        Sort defaultSort = Sort.by(Sort.Direction.DESC, "created_at");
        if (Objects.isNull(pageable) || pageable.getSort().equals(Sort.unsorted())) {
            int pageNumber = pageable != null ? pageable.getPageNumber() : 0;
            int pageSize = pageable != null ? pageable.getPageSize() : Integer.MAX_VALUE;
            pageable = PageRequest.of(pageNumber, pageSize, defaultSort);
        }
        return createQuery(pageable, buildConditionFromRequestObject(query)).all();
    }

    private Condition buildConditionFromRequestObject(ProductMaintainQuery filter) {
        Condition whereClause = Conditions.isEqual(entityTable.column("is_deleted"), Conditions.just("false"));

        if (Objects.nonNull(filter)) {
            if (StringUtils.isNotBlank(filter.getSearch())) {
                String searchValue = StringUtils.stripAccents(filter.getSearch().toLowerCase().trim());
                searchValue = searchValue.replaceAll("'", "''");

                String condition = "( unaccent(" + EntityManager.ENTITY_ALIAS + ".product_batch_name) ILIKE unaccent('%" + searchValue + "%') OR " + "unaccent(" + EntityManager.ENTITY_ALIAS + ".product_batch_code) ILIKE unaccent('%" + searchValue + "%'))";
                whereClause = whereClause.and(Conditions.just(condition));
            }

            if (Objects.nonNull(filter.getManufactureStartDate())) {
                LocalDate startDate = filter.getManufactureStartDate();
                LocalDate endDate = Objects.requireNonNullElse(filter.getManufactureEndDate(), startDate);

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                whereClause = whereClause.and(Conditions.between(entityTable.column("manufacture_date"), Conditions.just("'" + startDate.format(formatter) + "'"), Conditions.just("'" + endDate.format(formatter) + "'")));
            }

            if (Objects.nonNull(filter.getExpiredStartDate())) {
                LocalDate startDate = filter.getExpiredStartDate();
                LocalDate endDate = Objects.requireNonNullElse(filter.getExpiredEndDate(), startDate);

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                whereClause = whereClause.and(Conditions.between(entityTable.column("expired_date"), Conditions.just("'" + startDate.format(formatter) + "'"), Conditions.just("'" + endDate.format(formatter) + "'")));
            }
            if (Objects.nonNull(filter.getCompany())) {
                whereClause = whereClause.and(Conditions.isEqual(entityTable.column("company"), Conditions.just(StringUtils.wrap(filter.getCompany(), "'"))));
            }

//            if (Objects.nonNull(ro.getDepartment())) {
//                whereClause = whereClause.and(Conditions.isEqual(entityTable.column("department"), Conditions.just(StringUtils.wrap(RO.getDepartment(), "'"))));
//            }
            if (Objects.nonNull(filter.getIsProductRoutingSpecified())) {
                Table productRouting = Table.aliased("product_routing", "pr");
                if (filter.getIsProductRoutingSpecified()) {
                    // Chọn các product_maintain có liên kết với product_routing
                    whereClause = whereClause.and(
                        entityTable.column("id").in(
                            Select.builder()
                                .select(productRouting.column("product_maintain_id"))
                                .from(productRouting)
                                .build()
                        )
                    );
                } else {
                    // Chọn các product_maintain không có liên kết với product_routing
                    whereClause = whereClause.and(
                        entityTable.column("id").notIn(
                            Select.builder()
                                .select(productRouting.column("product_maintain_id"))
                                .from(productRouting)
                                .build()
                        )
                    );
                    whereClause = whereClause.and(manufactureOrderTable.column("status").isEqualTo(Conditions.just(StringUtils.wrap(StatusEntity.SHIPPED.name(), "'"))));
                }
            }
        }

        return whereClause;
    }
}
