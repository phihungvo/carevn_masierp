package com.masi.employee.repository;

import com.masi.employee.domain.UniformFormDetail;
import com.masi.employee.repository.rowmapper.UniformFormDetailRowMapper;
import com.masi.employee.repository.rowmapper.UniformOrderRowMapper;
import com.masi.employee.repository.rowmapper.UniformReleaseRowMapper;
import com.masi.employee.repository.rowmapper.UniformReturnRowMapper;
import com.masi.employee.repository.rowmapper.UniformRowMapper;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
 * Spring Data R2DBC custom repository implementation for the UniformFormDetail
 * entity.
 */
@SuppressWarnings("unused")
class UniformFormDetailRepositoryInternalImpl
        extends SimpleR2dbcRepository<UniformFormDetail, UUID>
        implements UniformFormDetailRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final UniformRowMapper uniformMapper;
    private final UniformReleaseRowMapper uniformreleaseMapper;
    private final UniformOrderRowMapper uniformorderMapper;
    private final UniformReturnRowMapper uniformreturnMapper;
    private final UniformFormDetailRowMapper uniformformdetailMapper;

    private static final Table entityTable = Table.aliased("uniform_form_detail", EntityManager.ENTITY_ALIAS);
    private static final Table uniformTable = Table.aliased("uniform", "uniform");
    private static final Table uniformReleaseTable = Table.aliased("uniform_release", "uniformRelease");
    private static final Table uniformOrderTable = Table.aliased("uniform_order", "uniformOrder");
    private static final Table uniformReturnTable = Table.aliased("uniform_return", "uniformReturn");

    public UniformFormDetailRepositoryInternalImpl(
            R2dbcEntityTemplate template,
            EntityManager entityManager,
            UniformRowMapper uniformMapper,
            UniformReleaseRowMapper uniformreleaseMapper,
            UniformOrderRowMapper uniformorderMapper,
            UniformReturnRowMapper uniformreturnMapper,
            UniformFormDetailRowMapper uniformformdetailMapper,
            R2dbcEntityOperations entityOperations,
            R2dbcConverter converter) {
        super(
                new MappingRelationalEntityInformation(
                        converter.getMappingContext().getRequiredPersistentEntity(UniformFormDetail.class)),
                entityOperations,
                converter);
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.uniformMapper = uniformMapper;
        this.uniformreleaseMapper = uniformreleaseMapper;
        this.uniformorderMapper = uniformorderMapper;
        this.uniformreturnMapper = uniformreturnMapper;
        this.uniformformdetailMapper = uniformformdetailMapper;
    }

    @Override
    public Flux<UniformFormDetail> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<UniformFormDetail> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = UniformFormDetailSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(UniformSqlHelper.getColumns(uniformTable, "uniform"));
        columns.addAll(UniformReleaseSqlHelper.getColumns(uniformReleaseTable, "uniformRelease"));
        columns.addAll(UniformOrderSqlHelper.getColumns(uniformOrderTable, "uniformOrder"));
        columns.addAll(UniformReturnSqlHelper.getColumns(uniformReturnTable, "uniformReturn"));
        SelectFromAndJoinCondition selectFrom = Select.builder()
                .select(columns)
                .from(entityTable)
                .leftOuterJoin(uniformTable)
                .on(Column.create("uniform_id", entityTable))
                .equals(Column.create("id", uniformTable))
                .leftOuterJoin(uniformReleaseTable)
                .on(Column.create("uniform_release_id", entityTable))
                .equals(Column.create("id", uniformReleaseTable))
                .leftOuterJoin(uniformOrderTable)
                .on(Column.create("uniform_order_id", entityTable))
                .equals(Column.create("id", uniformOrderTable))
                .leftOuterJoin(uniformReturnTable)
                .on(Column.create("uniform_return_id", entityTable))
                .equals(Column.create("id", uniformReturnTable));
        // we do not support Criteria here for now as of
        // https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, UniformFormDetail.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<UniformFormDetail> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<UniformFormDetail> findById(UUID id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"),
                Conditions.just(StringUtils.wrap(id.toString(), "'")));
        return createQuery(null, whereClause).one();
    }

    @Override
    public Flux<UniformFormDetail> findAllByFieldNotNullAndBetweenAndCompany(String field, LocalDate fromDate, LocalDate toDate, String company) {
        String fieldAlias = EntityManager.ENTITY_ALIAS + "." + field;
        String notNullCondition = fieldAlias + " IS NOT NULL";
        Condition condition = Conditions.just(notNullCondition);
        DateTimeFormatter sqlDateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        if (fromDate != null) {
            condition = condition.and(Conditions.isGreaterOrEqualTo(entityTable.column("create_at"), Conditions.just(StringUtils.wrap(fromDate.atStartOfDay().format(sqlDateFormatter), "'"))));
        }
        if (toDate != null) {
            condition = condition.and(Conditions.isLessOrEqualTo(entityTable.column("create_at"), Conditions.just(StringUtils.wrap(toDate.atTime(23, 59).format(sqlDateFormatter), "'"))));
        }
        if (company != null) {
            condition = condition.and(Conditions.isEqual(entityTable.column("company"), Conditions.just(StringUtils.wrap(company, "'"))));
        }
        return createQuery(null, condition).all();
    }

    private UniformFormDetail process(Row row, RowMetadata metadata) {
        UniformFormDetail entity = uniformformdetailMapper.apply(row, "e");
        entity.setUniform(uniformMapper.apply(row, "uniform"));
        entity.setUniformRelease(uniformreleaseMapper.apply(row, "uniformRelease"));
        entity.setUniformOrder(uniformorderMapper.apply(row, "uniformOrder"));
        entity.setUniformReturn(uniformreturnMapper.apply(row, "uniformReturn"));
        return entity;
    }

    @Override
    public <S extends UniformFormDetail> Mono<S> save(S entity) {
        return super.save(entity);
    }

    @Override
    public Flux<UniformFormDetail> findAllByUniformOrderId(UUID id) {
        // TODO Auto-generated method stub
        Condition whereClause = Conditions.isNull(entityTable.column("delete_at"));
        whereClause = whereClause.and(Conditions.isEqual(entityTable.column("uniform_order_id"),
                Conditions.just(StringUtils.wrap(id.toString(), "'"))))
                .and(Conditions.isNull(entityTable.column("delete_by")));
        return createQuery(null, whereClause).all();
    }

    public Flux<UniformFormDetail> findAllByUniformReleaseIdAndCompany(UUID id, String company) {
        // TODO Auto-generated method stub
        Condition whereClause = Conditions.isNull(entityTable.column("delete_at"));
        whereClause = whereClause.and(Conditions.isEqual(entityTable.column("uniform_release_id"),
                Conditions.just(StringUtils.wrap(id.toString(), "'"))))
                .and(Conditions.isNull(entityTable.column("delete_by")));
        whereClause = whereClause.and(Conditions.isEqual(entityTable.column("company"),
                Conditions.just(StringUtils.wrap(company, "'"))));
        return createQuery(null, whereClause).all();
    }

    /**
     * @param uniformOrderStockId UUID
     * @param company            String
     * @return Flux<UniformFormDetail>
     */
    @Override
    public Flux<UniformFormDetail> findAllByUniformOrderStock(UUID uniformOrderStockId, String company) {
        // TODO Auto-generated method stub
        Condition whereClause = Conditions.isNull(entityTable.column("delete_at"));
        whereClause = whereClause.and(Conditions.isEqual(entityTable.column("uniform_order_stock_id"),
                Conditions.just(StringUtils.wrap(uniformOrderStockId.toString(), "'"))))
                .and(Conditions.isNull(entityTable.column("delete_by")));
        whereClause = whereClause.and(Conditions.isEqual(entityTable.column("company"),
                Conditions.just(StringUtils.wrap(company, "'"))));
        return createQuery(null, whereClause).all();
    }
}
