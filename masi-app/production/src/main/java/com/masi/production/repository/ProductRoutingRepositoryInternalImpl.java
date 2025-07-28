package com.masi.production.repository;

import com.masi.production.domain.ProductRouting;
import com.masi.production.repository.rowmapper.*;
import com.masi.production.service.dto.ProductRoutingQuery;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;

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
 * Spring Data R2DBC custom repository implementation for the ProductRouting entity.
 */
@SuppressWarnings("unused")
class ProductRoutingRepositoryInternalImpl extends SimpleR2dbcRepository<ProductRouting, UUID> implements ProductRoutingRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final FactoryRowMapper factoryMapper;
    private final StorageRowMapper storageMapper;
    private final ProductRoutingRowMapper productroutingMapper;
    private final ProductPackageRowMapper productpackageMapper;
    private final ProductMaintainRowMapper productmaintainMapper;

    private static final Table entityTable = Table.aliased("product_routing", EntityManager.ENTITY_ALIAS);
    private static final Table factoryTable = Table.aliased("factory", "factory");
    private static final Table productPackageTable = Table.aliased("product_package", "productPackage");
    private static final Table productMaintainTable = Table.aliased("product_maintain", "productMaintain");

    public ProductRoutingRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        FactoryRowMapper factoryMapper,
        StorageRowMapper storageMapper,
        ProductRoutingRowMapper productroutingMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter, ProductPackageRowMapper productpackageMapper, ProductMaintainRowMapper productmaintainMapper
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(ProductRouting.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.factoryMapper = factoryMapper;
        this.storageMapper = storageMapper;
        this.productroutingMapper = productroutingMapper;
        this.productpackageMapper = productpackageMapper;
        this.productmaintainMapper = productmaintainMapper;
    }

    @Override
    public Flux<ProductRouting> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    @Override
    public Flux<ProductRouting> findAll() {
        return findAllBy(null);
    }


    RowsFetchSpec<ProductRouting> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = ProductRoutingSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(FactorySqlHelper.getColumns(factoryTable, "factory"));
        columns.addAll(ProductMaintainSqlHelper.getColumns(productMaintainTable, "productMaintain"));
        columns.addAll(ProductPackageSqlHelper.getColumns(productPackageTable, "productPackage"));
        SelectFromAndJoinCondition selectFrom = Select.builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(factoryTable)
            .on(Column.create("factory_id", entityTable))
            .equals(Column.create("id", factoryTable))
            .leftOuterJoin(productMaintainTable)
            .on(Column.create("product_maintain_id", entityTable))
            .equals(Column.create("id", productMaintainTable))
            .leftOuterJoin(productPackageTable)
            .on(Column.create("product_package_id", productMaintainTable))
            .equals(Column.create("id", productPackageTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, ProductRouting.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }


    @Override
    public Mono<Long> countByFilter(ProductRoutingQuery query) {
        Condition condition = buildConditionFromRequestObject(query);
        return createQuery(null, condition).all().count().doOnError(throwable -> new RuntimeException(throwable));
    }

    private Condition buildConditionFromRequestObject(ProductRoutingQuery filter) {
        Condition whereClause = Conditions.isEqual(entityTable.column("is_active"), Conditions.just("true"));

        if (Objects.nonNull(filter)) {
            if (StringUtils.isNotBlank(filter.getFactoryId())) {
                whereClause = whereClause.and(
                    Conditions.isEqual(entityTable.column("factory_id"), Conditions.just("'"+filter.getFactoryId()+"'"))
                );
            }
            if (StringUtils.isNotBlank(filter.getStorageId())) {
                whereClause = whereClause.and(
                    Conditions.isEqual(entityTable.column("storage_id"), Conditions.just("'"+filter.getStorageId()+"'"))
                );
            }

            if (StringUtils.isNotBlank(filter.getSearch())) {
                whereClause = whereClause.and(
                    Conditions.just("unaccent("+ EntityManager.ENTITY_ALIAS+".name) iLIKE unaccent('%"+filter.getSearch().replace("'","''")+"%')")
                );
            }

            if (Objects.nonNull(filter.getCompany())) {
                whereClause = whereClause.and(Conditions.isEqual(entityTable.column("company"), Conditions.just(StringUtils.wrap(filter.getCompany(), "'"))));
            }
//            if (Objects.nonNull(ro.getDepartment())) {
//                whereClause = whereClause.and(Conditions.isEqual(entityTable.column("department"), Conditions.just(StringUtils.wrap(RO.getDepartment(), "'"))));
//            }
        }

        return whereClause;
    }

    @Override
    public Flux<ProductRouting> findAllByFilter(ProductRoutingQuery query, Pageable pageable) {
         Sort defaultSort = Sort.by(Sort.Direction.DESC, "created_at");
        if (Objects.isNull(pageable) || pageable.getSort().equals(Sort.unsorted())) {
            int pageNumber = pageable != null ? pageable.getPageNumber() : 0;
            int pageSize = pageable != null ? pageable.getPageSize() : Integer.MAX_VALUE;
            pageable = PageRequest.of(pageNumber, pageSize, defaultSort);
        }
        return createQuery(pageable, buildConditionFromRequestObject(query)).all();
    }

    @Override
    public Mono<ProductRouting> findById(UUID id, String company) {
        Condition whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(StringUtils.wrap(id.toString(), "'")));
        whereClause = whereClause.and(Conditions.isEqual(entityTable.column("is_active"), Conditions.just("true")));
        whereClause = whereClause.and(Conditions.isEqual(entityTable.column("is_deleted"), Conditions.just("false")));
        return createQuery(null, whereClause).one();
    }

    private ProductRouting process(Row row, RowMetadata metadata) {
        ProductRouting entity = productroutingMapper.apply(row, "e");
        entity.setFactory(factoryMapper.apply(row, "factory"));
        entity.setProductMaintain(productmaintainMapper.apply(row, "productMaintain"));
        entity.getProductMaintain().setProductPackage(productpackageMapper.apply(row, "productPackage"));
        return entity;
    }

    @Override
    public <S extends ProductRouting> Mono<S> save(S entity) {
        return super.save(entity);
    }

//    @Override
    public Mono<ProductRouting> findOne(UUID id) {
        Condition whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(StringUtils.wrap(id.toString(), "'")));
        whereClause=   whereClause.and(Conditions.isEqual(entityTable.column("is_active"), Conditions.just("true")));
        return createQuery(null, whereClause).one();
    }
}
