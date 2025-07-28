package com.masi.employee.repository;

import com.masi.employee.domain.Uniform;
import com.masi.employee.domain.UniformOrder;
import com.masi.employee.domain.UniformStock;
import com.masi.employee.repository.rowmapper.UniformRowMapper;
import com.masi.employee.repository.rowmapper.UniformStockRowMapper;
import com.masi.employee.service.dto.UniformOrderGetListDTO;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
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
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

/**
 * Spring Data R2DBC custom repository implementation for the UniformStock
 * entity.
 */
@SuppressWarnings("unused")
class UniformStockRepositoryInternalImpl extends SimpleR2dbcRepository<UniformStock, UUID>
        implements UniformStockRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final UniformRowMapper uniformMapper;
    private final UniformStockRowMapper uniformstockMapper;

    private static final Table entityTable = Table.aliased("uniform_stock", EntityManager.ENTITY_ALIAS);
    private static final Table uniformTable = Table.aliased("uniform", "uniform");

    public UniformStockRepositoryInternalImpl(
            R2dbcEntityTemplate template,
            EntityManager entityManager,
            UniformRowMapper uniformMapper,
            UniformStockRowMapper uniformstockMapper,
            R2dbcEntityOperations entityOperations,
            R2dbcConverter converter) {
        super(
                new MappingRelationalEntityInformation(
                        converter.getMappingContext().getRequiredPersistentEntity(UniformStock.class)),
                entityOperations,
                converter);
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.uniformMapper = uniformMapper;
        this.uniformstockMapper = uniformstockMapper;
    }

    @Override
    public Flux<UniformStock> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<UniformStock> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = UniformStockSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(UniformSqlHelper.getColumns(uniformTable, "uniform"));
        SelectFromAndJoinCondition selectFrom = Select.builder()
                .select(columns)
                .from(entityTable)
                .leftOuterJoin(uniformTable)
                .on(Column.create("uniform_id", entityTable))
                .equals(Column.create("id", uniformTable));
        // we do not support Criteria here for now as of
        // https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, UniformStock.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    RowsFetchSpec<UniformStock> createQueryCustom(Pageable pageable, Condition whereClause,
            Map<String, Object> params) {
        List<Expression> columns = UniformStockSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(UniformSqlHelper.getColumns(uniformTable, "uniform"));
        SelectFromAndJoinCondition selectFrom = Select.builder()
                .select(columns)
                .from(entityTable)
                .leftOuterJoin(uniformTable)
                .on(Column.create("uniform_id", entityTable))
                .equals(Column.create("id", uniformTable));
        // we do not support Criteria here for now as of
        // https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, UniformStock.class, pageable, whereClause);
        var query = db.sql(select);
        if (params != null && !params.isEmpty()) {
            query = query.bindValues(params);
        }
        return query.map(this::process);
    }

    @Override
    public Flux<UniformStock> findAll() {
        return findAllBy(null);
    }

    @Override
    public Flux<UniformStock> findAllByCompany(String company) {
        Condition whereClause = Conditions.isEqual(entityTable.column("company"), Conditions.just(StringUtils.wrap(company, "'")));
        return createQuery(null, whereClause).all();

    }

    @Override
    public Mono<UniformStock> findById(UUID id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"),
                Conditions.just(StringUtils.wrap(id.toString(), "'")));
        return createQuery(null, whereClause).one();
    }

    private UniformStock process(Row row, RowMetadata metadata) {
        UniformStock entity = uniformstockMapper.apply(row, "e");
        entity.setUniform(uniformMapper.apply(row, "uniform"));
        return entity;
    }

    @Override
    public <S extends UniformStock> Mono<S> save(S entity) {
        return super.save(entity);
    }

    private Pageable getDefaultSort(Pageable pageable) {
        Sort defaultSort = Sort.by(Sort.Direction.DESC, "create_at");
        if (Objects.isNull(pageable)) {
            pageable = PageRequest.of(0, Integer.MAX_VALUE, defaultSort);
        }
        if (pageable.getSort().equals(Sort.unsorted())) {
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), defaultSort);
        }
        return pageable;
    }

    private Tuple2<Condition, Map<String, Object>> buildConditionFromRO(String company, String status) {
        Condition condition = Conditions.isNull(entityTable.column("delete_at"));
        condition = condition.and(Conditions.isNull(entityTable.column("delete_by")));
        if (status != null) {
            condition = condition.and(Conditions.isEqual(uniformTable.column("status"),
                    Conditions.just("'" + status + "'")));
        }
        Map<String, Object> params = new HashMap<>();
        // if (StringUtils.isNotBlank(ro.getName())) {
        // var columnAlias = EntityManager.ENTITY_ALIAS + ".name";
        // String likeClause = String.format("unaccent(%s) ILIKE unaccent(:search)",
        // columnAlias);
        // condition = condition.and(Conditions.just(likeClause));
        // params.put("search", "%" + ro.getName() + "%");
        // }
        // if (ro.getStatus() != null && !ro.getStatus().isEmpty()) {
        // String inClause = ro.getStatus().stream().map(e -> "'" + e + "'")
        // .reduce((a, b) -> a + "," + b).orElseThrow();
        // condition = condition.and(Conditions.in(entityTable.column("status"),
        // Conditions.just(inClause)));
        // }

        if (company != null) {
            condition = condition.and(Conditions.isEqual(entityTable.column("company"),
                    Conditions.just("'" + company + "'")));
        }

        // if (ro.getStartDate() != null) {
        // condition = condition
        // .and(Conditions.isGreaterOrEqualTo(entityTable.column("date"),
        // Conditions.just(":startDate")));
        // params.put("startDate", ro.getStartDate());
        // }

        // if (ro.getEndDate() != null) {
        // condition = condition
        // .and(Conditions.isLessOrEqualTo(entityTable.column("date"),
        // Conditions.just(":endDate")));
        // params.put("endDate", ro.getEndDate());
        // }

        return Tuples.of(condition, params);
    }

    @Override
    public Flux<UniformStock> findAllByQuery(Pageable pageable, String company, String status) {
        // TODO Auto-generated method stub
        pageable = getDefaultSort(pageable);
        var conditionParams = buildConditionFromRO(company, status);

        return createQueryCustom(pageable, conditionParams.getT1(), conditionParams.getT2()).all();
    }

    @Override
    public Mono<Long> countAllByQuery(String company, String status) {
        // TODO Auto-generated method stub
        var conditionParams = buildConditionFromRO(company, status);
        return createQueryCustom(null, conditionParams.getT1(), conditionParams.getT2()).all().count();
    }

}
