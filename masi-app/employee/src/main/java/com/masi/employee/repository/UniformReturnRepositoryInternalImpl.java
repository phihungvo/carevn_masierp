package com.masi.employee.repository;

import com.masi.employee.domain.Uniform;
import com.masi.employee.domain.UniformReturn;
import com.masi.employee.repository.rowmapper.UniformFormDetailRowMapper;
import com.masi.employee.repository.rowmapper.UniformReturnRowMapper;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
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
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

/**
 * Spring Data R2DBC custom repository implementation for the UniformReturn entity.
 */
@SuppressWarnings("unused")
class UniformReturnRepositoryInternalImpl extends SimpleR2dbcRepository<UniformReturn, UUID> implements UniformReturnRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final UniformReturnRowMapper uniformreturnMapper;

    private final UniformFormDetailRowMapper uniformformdetailMapper;

    private static final Table entityTable = Table.aliased("uniform_return", EntityManager.ENTITY_ALIAS);

    private static final Table detailTable = Table.aliased("uniform_form_detail", "d");


    public UniformReturnRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        UniformReturnRowMapper uniformreturnMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter, UniformFormDetailRowMapper uniformformdetailMapper
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(UniformReturn.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.uniformreturnMapper = uniformreturnMapper;
        this.uniformformdetailMapper = uniformformdetailMapper;
    }

    @Override
    public Flux<UniformReturn> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<UniformReturn> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = UniformReturnSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        SelectFromAndJoin selectFrom = Select.builder().select(columns).from(entityTable);
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, UniformReturn.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<UniformReturn> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<UniformReturn> findById(UUID id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(StringUtils.wrap(id.toString(), "'")));
        return createQuery(null, whereClause).one();
    }

    @Override
    public Flux<UniformReturn> findByUniformReleaseId(UUID uniformReleaseId, String company, List<Uniform> uniforms) {
        var sql = "SELECT "+  UniformFormDetailSqlHelper.getColumns(detailTable, "d")
            + ", "
            + UniformReturnSqlHelper.getColumns(entityTable, "e")
            + " FROM uniform_return e";
        sql+= " JOIN uniform_form_detail d ON e.id = d.uniform_return_id";
        sql+= " WHERE e.uniform_release_id = :uniformReleaseId AND e.company = :company";
        sql = sql.replace("[", "").replace("]", "");

        var params = new HashMap<String, Object>();
        params.put("uniformReleaseId", uniformReleaseId);
        params.put("company", company);
        return db.sql(sql)
            .bindValues(params)
            .map(((row, rowMetadata) -> Tuples.of(
                uniformreturnMapper.apply(row, "e"),
                uniformformdetailMapper.apply(row, "d")))).all().groupBy(x -> x.getT1().getId())
            .flatMap(g -> g.collectList().map(l -> {
                UniformReturn entity = l.get(0).getT1();
                entity.setUniformFormDetails(new HashSet<>());
                l.forEach(detailItem -> {
                    entity.getUniformFormDetails().add(detailItem.getT2());
                    uniforms.stream().filter(u -> u.getId() != null && u.getId().equals(detailItem.getT2().getUniformId())).findFirst().ifPresent(uniform -> detailItem.getT2().setUniform(uniform));
                });
                return entity;
            }));

    }

    private UniformReturn process(Row row, RowMetadata metadata) {
        UniformReturn entity = uniformreturnMapper.apply(row, "e");
        return entity;
    }

    @Override
    public <S extends UniformReturn> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
