package com.masi.logistics.repository;

import com.masi.logistics.domain.UomGroup;
import com.masi.logistics.repository.rowmapper.UomGroupRowMapper;
import com.masi.logistics.repository.rowmapper.UomRowMapper;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
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
 * Spring Data R2DBC custom repository implementation for the UomGroup entity.
 */
@SuppressWarnings("unused")
class UomGroupRepositoryInternalImpl extends SimpleR2dbcRepository<UomGroup, UUID> implements UomGroupRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final UomRowMapper uomMapper;
    private final UomGroupRowMapper uomgroupMapper;

    private static final Table entityTable = Table.aliased("uom_group", EntityManager.ENTITY_ALIAS);
    private static final Table baseUomTable = Table.aliased("uom", "baseUom");

    public UomGroupRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        UomRowMapper uomMapper,
        UomGroupRowMapper uomgroupMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(UomGroup.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.uomMapper = uomMapper;
        this.uomgroupMapper = uomgroupMapper;
    }

    @Override
    public Flux<UomGroup> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<UomGroup> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = UomGroupSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(UomSqlHelper.getColumns(baseUomTable, "baseUom"));
        SelectFromAndJoinCondition selectFrom = Select.builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(baseUomTable)
            .on(Column.create("base_uom_id", entityTable))
            .equals(Column.create("id", baseUomTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, UomGroup.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<UomGroup> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<UomGroup> findById(UUID id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(StringUtils.wrap(id.toString(), "'")));
        return createQuery(null, whereClause).one();
    }

    private UomGroup process(Row row, RowMetadata metadata) {
        UomGroup entity = uomgroupMapper.apply(row, "e");
        entity.setBaseUom(uomMapper.apply(row, "baseUom"));
        return entity;
    }

    @Override
    public <S extends UomGroup> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
