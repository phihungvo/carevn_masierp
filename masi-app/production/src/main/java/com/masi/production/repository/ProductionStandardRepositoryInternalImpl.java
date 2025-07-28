package com.masi.production.repository;

import com.masi.production.domain.ProductionStandard;
import com.masi.production.repository.rowmapper.ColumnConverter;
import com.masi.production.repository.rowmapper.ProductionStandardRowMapper;
import com.masi.production.service.dto.ProductionStandardRO;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.r2dbc.convert.R2dbcConverter;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.repository.support.SimpleR2dbcRepository;
import org.springframework.data.relational.core.sql.Comparison;
import org.springframework.data.relational.core.sql.Condition;
import org.springframework.data.relational.core.sql.Conditions;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Select;
import org.springframework.data.relational.core.sql.SelectBuilder.SelectFromAndJoin;
import org.springframework.data.relational.core.sql.Table;
import org.springframework.data.relational.repository.support.MappingRelationalEntityInformation;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.r2dbc.core.RowsFetchSpec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC custom repository implementation for the ProductionStandard entity.
 */
@SuppressWarnings("unused")
class ProductionStandardRepositoryInternalImpl
    extends SimpleR2dbcRepository<ProductionStandard, UUID>
    implements ProductionStandardRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final ProductionStandardRowMapper productionstandardMapper;

    private static final Table entityTable = Table.aliased("production_standard", EntityManager.ENTITY_ALIAS);

    public ProductionStandardRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        ProductionStandardRowMapper productionstandardMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(ProductionStandard.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.productionstandardMapper = productionstandardMapper;
    }

    @Override
    public Flux<ProductionStandard> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<ProductionStandard> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = ProductionStandardSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        SelectFromAndJoin selectFrom = Select.builder().select(columns).from(entityTable);
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, ProductionStandard.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<ProductionStandard> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<ProductionStandard> findById(UUID id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(StringUtils.wrap(id.toString(), "'")));
        return createQuery(null, whereClause).one();
    }

    @Override
    public Mono<Long> countAllByFilter(ProductionStandardRO ro) {
        return createQuery(null, buildConditionFromRequestObject(ro)).all().count();
    }

    @Override
    public Flux<ProductionStandard> findAllByFilter(ProductionStandardRO ro, Pageable pageable) {
        Sort defaultSort = Sort.by(Sort.Direction.DESC, "created_at");
        if (Objects.isNull(pageable)) {
            pageable = PageRequest.of(0, Integer.MAX_VALUE, defaultSort);
        }
        if (pageable.getSort().equals(Sort.unsorted())) {
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), defaultSort);
        }
        return createQuery(pageable, buildConditionFromRequestObject(ro)).all();
    }

    private ProductionStandard process(Row row, RowMetadata metadata) {
        ProductionStandard entity = productionstandardMapper.apply(row, "e");
        return entity;
    }

    @Override
    public <S extends ProductionStandard> Mono<S> save(S entity) {
        return super.save(entity);
    }

    private Condition buildConditionFromRequestObject(ProductionStandardRO ro) {
        Condition whereClause = Conditions.isEqual(entityTable.column("is_deleted"), Conditions.just("false"));

        if (Objects.nonNull(ro)) {
            if (Objects.nonNull(ro.getName())) {
                String columnAlias = EntityManager.ENTITY_ALIAS + ".name";
                String inCondition = "unaccent(" + columnAlias + ") iLIKE unaccent('%" + ro.getName() + "%')";
                whereClause = whereClause.and(Conditions.just(inCondition));
            }
            if (StringUtils.isNotBlank(ro.getSearchString())) {
                String columnAlias = EntityManager.ENTITY_ALIAS + ".name";
                String inCondition = "unaccent(" + columnAlias + ") iLIKE unaccent('%" + ro.getSearchString() + "%')";
                whereClause = whereClause.and(Conditions.just(inCondition));
            }
            if (CollectionUtils.isNotEmpty(ro.getStatuses())) {
                var inCondition=ro.getStatuses().stream().map(status -> StringUtils.wrap(status, "'")).reduce((a, b) -> a + ", " + b).orElse("");
                whereClause = whereClause.and(Conditions.in(entityTable.column("status"), Conditions.just(inCondition)));
            }

            if (Objects.nonNull(ro.getStartDate())) {
                whereClause = whereClause.and(Conditions.isGreaterOrEqualTo(entityTable.column("due_date"), Conditions.just(StringUtils.wrap(String.valueOf(ro.getStartDate()), "'"))));
            }
            if (Objects.nonNull(ro.getEndDate())) {
                whereClause = whereClause.and(Conditions.isLessOrEqualTo(entityTable.column("due_date"), Conditions.just(StringUtils.wrap(String.valueOf(ro.getEndDate()), "'"))));
            }
            if (Objects.nonNull(ro.getCompany())) {
                whereClause = whereClause.and(Conditions.isEqual(entityTable.column("company"), Conditions.just(StringUtils.wrap(ro.getCompany(), "'"))));
            }
//            if (Objects.nonNull(ro.getDepartment())) {
//                whereClause = whereClause.and(Conditions.isEqual(entityTable.column("department"), Conditions.just(StringUtils.wrap(RO.getDepartment(), "'"))));
//            }
        }
        return whereClause;
    }
}
