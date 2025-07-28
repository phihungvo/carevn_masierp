package com.masi.production.repository;

import com.masi.production.domain.QualityCheckSample;
import com.masi.production.domain.enumeration.QcSampleStatus;
import com.masi.production.repository.rowmapper.ColumnConverter;
import com.masi.production.repository.rowmapper.ManufactureOrderRowMapper;
import com.masi.production.repository.rowmapper.QualityCheckSampleRowMapper;
import com.masi.production.repository.rowmapper.SampleDisposalRowMapper;
import com.masi.production.service.dto.QuanlityCheckSampleRO;
import com.masi.production.service.dto.QuantityCheckFilter;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;

import java.util.List;
import java.util.Objects;
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
import tech.jhipster.service.ConditionBuilder;
import tech.jhipster.service.filter.Filter;

/**
 * Spring Data R2DBC custom repository implementation for the QualityCheckSample entity.
 */
@SuppressWarnings("unused")
class QualityCheckSampleRepositoryInternalImpl
    extends SimpleR2dbcRepository<QualityCheckSample, UUID>
    implements QualityCheckSampleRepositoryInternal {

    private final ColumnConverter columnConverter;

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final SampleDisposalRowMapper sampledisposalMapper;
    private final QualityCheckSampleRowMapper qualitychecksampleMapper;
    private final ManufactureOrderRowMapper manufactureOrderRowMapper;

    private static final Table entityTable = Table.aliased("quality_check_sample", EntityManager.ENTITY_ALIAS);
    private static final Table disposalTable = Table.aliased("sample_disposal", "disposal");
    private static final Table manufactureTable = Table.aliased("manufacture_order", "manufacture_order");

    public QualityCheckSampleRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        SampleDisposalRowMapper sampledisposalMapper,
        QualityCheckSampleRowMapper qualitychecksampleMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter,
        ColumnConverter columnConverter, ManufactureOrderRowMapper manufactureOrderRowMapper
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(QualityCheckSample.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.sampledisposalMapper = sampledisposalMapper;
        this.qualitychecksampleMapper = qualitychecksampleMapper;
        this.columnConverter = columnConverter;
        this.manufactureOrderRowMapper = manufactureOrderRowMapper;
    }

    @Override
    public Flux<QualityCheckSample> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<QualityCheckSample> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = QualityCheckSampleSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(SampleDisposalSqlHelper.getColumns(disposalTable, "disposal"));
        columns.addAll(ManufactureOrderSqlHelper.getColumns(manufactureTable, "manufacture_order"));
        SelectFromAndJoinCondition selectFrom = Select.builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(disposalTable)
            .on(Column.create("disposal_id", entityTable))
            .equals(Column.create("id", disposalTable))
            .leftOuterJoin(manufactureTable)
            .on(Column.create("manufacture_order_id", entityTable))
            .equals(Column.create("id", manufactureTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, QualityCheckSample.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<QualityCheckSample> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<QualityCheckSample> findById(UUID id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(StringUtils.wrap(id.toString(), "'")));
        return createQuery(null, whereClause).one();
    }

    private QualityCheckSample process(Row row, RowMetadata metadata) {
        QualityCheckSample entity = qualitychecksampleMapper.apply(row, "e");
        entity.setDisposal(sampledisposalMapper.apply(row, "disposal"));
        entity.setManufactureOrder(manufactureOrderRowMapper.apply(row, "manufacture_order"));
        return entity;
    }

    @Override
    public <S extends QualityCheckSample> Mono<S> save(S entity) {
        return super.save(entity);
    }

    @Override
    public Flux<QualityCheckSample> findAllByIsActiveIsTrueAndStatus(Pageable pageable, QcSampleStatus status) {
        ConditionBuilder builder = new ConditionBuilder(this.columnConverter);
        if (status != null) {
            Filter<QcSampleStatus> filter = new Filter<>();
            filter.setEquals(status);
            builder.buildFilterConditionForField(filter, entityTable.column("status"));
        }
        Filter<Boolean> isActiveFilter = new Filter<Boolean>();
        isActiveFilter.setEquals(true);
        builder.buildFilterConditionForField(isActiveFilter, entityTable.column("is_active"));
        Condition condition = builder.buildConditions();
        return createQuery(pageable, condition).all();
    }

    @Override
    public Flux<QualityCheckSample> findAllByFilter(Pageable pageable, QuanlityCheckSampleRO ro) {
        return createQuery(pageable, buildConditionFromRequestObject(ro)).all();
    }

    private Condition createCondition(QuantityCheckFilter filter) {
        ConditionBuilder builder = new ConditionBuilder(columnConverter);
        if (filter.getStatus() != null) {
            builder.buildFilterConditionForField(filter.getStatus(), entityTable.column("status"));
        }
        if (filter.getIsActive() != null) {
            builder.buildFilterConditionForField(filter.getIsActive(), entityTable.column("is_active"));
        }
        var condition = builder.buildConditions();
        if (filter.getSearch() != null && filter.getSearch().getContains() != null) {
            String columnAlias = manufactureTable.column("name").toString();
            var search = "%" + filter.getSearch().getContains().replaceAll("'", "''") + "%";
            String likeCondition1 = String.format("unaccent(%s) iLIKE unaccent(%s)", columnAlias, search);
            columnAlias = entityTable.column("sample_no").toString();
            String likeCondition2 = String.format("unaccent(%s) iLIKE unaccent(%s)", columnAlias, search);
            String combinedCondition = String.format("(%s OR %s)", likeCondition1, likeCondition2);
            condition = condition.and(Conditions.just(combinedCondition));
        }
        return condition;
    }

    @Override
    public Mono<Long> countByFilter(QuanlityCheckSampleRO ro) {
        Condition condition = buildConditionFromRequestObject(ro);
        return createQuery(null, condition).all().count().doOnError(throwable -> new RuntimeException(throwable));
    }


    private Condition buildConditionFromRequestObject(QuanlityCheckSampleRO ro) {
        Condition whereClause = Conditions.isEqual(entityTable.column("is_active"), Conditions.just("true"));

        if (Objects.nonNull(ro)) {
            if (Objects.nonNull(ro.getStatus())) {
                whereClause = whereClause.and(Conditions.isEqual(entityTable.column("status"), Conditions.just(StringUtils.wrap(ro.getStatus().toString(), "'"))));
            }
            if (Objects.nonNull(ro.getSearch())) {
                ro.setSearch(ro.getSearch().trim().replaceAll("'", "''"));
                String column = EntityManager.ENTITY_ALIAS + ".sample_no";
                String inCondition = "unaccent(" + column + ") iLIKE unaccent('%" + ro.getSearch() + "%')";
                column = manufactureTable.column("name").toString();
                String inCondition2 = "unaccent(" + column + ") iLIKE unaccent('%" + ro.getSearch() + "%')";
                String combinedCondition = "(" + inCondition + " OR " + inCondition2 + ")";
                whereClause = whereClause.and(Conditions.just(combinedCondition));
            }
            if (Objects.nonNull(ro.getCompany())) {
                whereClause = whereClause.and(Conditions.isEqual(entityTable.column("company"), Conditions.just(StringUtils.wrap(ro.getCompany(), "'"))));
            }
            if (Objects.nonNull(ro.getIsExistItem())) {
                if (!ro.getIsExistItem()) {
                    whereClause = whereClause.and(Conditions.isNull(entityTable.column("item_id")));
                } else {
                    Condition group1 = entityTable.column("item_id").isNotNull();
                    whereClause = whereClause.and(group1);
                }
            }

//            if (Objects.nonNull(ro.getDepartment())) {
//                whereClause = whereClause.and(Conditions.isEqual(entityTable.column("department"), Conditions.just(StringUtils.wrap(RO.getDepartment(), "'"))));
//            }
        }
        return whereClause;
    }
}
