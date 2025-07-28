package com.masi.logistics.repository;

import com.masi.logistics.domain.UomGroupDetails;
import com.masi.logistics.repository.rowmapper.UomGroupDetailsRowMapper;
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
 * Spring Data R2DBC custom repository implementation for the UomGroupDetails
 * entity.
 */
@SuppressWarnings("unused")
class UomGroupDetailsRepositoryInternalImpl
        extends SimpleR2dbcRepository<UomGroupDetails, UUID>
        implements UomGroupDetailsRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final UomRowMapper uomMapper;
    private final UomGroupRowMapper uomgroupMapper;
    private final UomGroupDetailsRowMapper uomgroupdetailsMapper;

    private static final Table entityTable = Table.aliased("uom_group_details", EntityManager.ENTITY_ALIAS);
    private static final Table baseUomTable = Table.aliased("uom", "baseUom");
    private static final Table altUomTable = Table.aliased("uom", "altUom");
    private static final Table uomGroupTable = Table.aliased("uom_group", "uomGroup");

    public UomGroupDetailsRepositoryInternalImpl(
            R2dbcEntityTemplate template,
            EntityManager entityManager,
            UomRowMapper uomMapper,
            UomGroupRowMapper uomgroupMapper,
            UomGroupDetailsRowMapper uomgroupdetailsMapper,
            R2dbcEntityOperations entityOperations,
            R2dbcConverter converter) {
        super(
                new MappingRelationalEntityInformation(
                        converter.getMappingContext().getRequiredPersistentEntity(UomGroupDetails.class)),
                entityOperations,
                converter);
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.uomMapper = uomMapper;
        this.uomgroupMapper = uomgroupMapper;
        this.uomgroupdetailsMapper = uomgroupdetailsMapper;
    }

    @Override
    public Flux<UomGroupDetails> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<UomGroupDetails> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = UomGroupDetailsSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(UomSqlHelper.getColumns(baseUomTable, "baseUom"));
        columns.addAll(UomSqlHelper.getColumns(altUomTable, "altUom"));
        columns.addAll(UomGroupSqlHelper.getColumns(uomGroupTable, "uomGroup"));
        SelectFromAndJoinCondition selectFrom = Select.builder()
                .select(columns)
                .from(entityTable)
                .leftOuterJoin(baseUomTable)
                .on(Column.create("base_uom_id", entityTable))
                .equals(Column.create("id", baseUomTable))
                .leftOuterJoin(altUomTable)
                .on(Column.create("alt_uom_id", entityTable))
                .equals(Column.create("id", altUomTable))
                .leftOuterJoin(uomGroupTable)
                .on(Column.create("uom_group_id", entityTable))
                .equals(Column.create("id", uomGroupTable));
        // we do not support Criteria here for now as of
        // https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, UomGroupDetails.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<UomGroupDetails> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<UomGroupDetails> findById(UUID id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"),
                Conditions.just(StringUtils.wrap(id.toString(), "'")));
        return createQuery(null, whereClause).one();
    }

    private UomGroupDetails process(Row row, RowMetadata metadata) {
        UomGroupDetails entity = uomgroupdetailsMapper.apply(row, "e");
        entity.setBaseUom(uomMapper.apply(row, "baseUom"));
        entity.setAltUom(uomMapper.apply(row, "altUom"));
        entity.setUomGroup(uomgroupMapper.apply(row, "uomGroup"));
        return entity;
    }

    @Override
    public <S extends UomGroupDetails> Mono<S> save(S entity) {
        return super.save(entity);
    }

    // function get uom group details by uom group id and active is true and delete
    // is null and company is current user company
    @Override
    public Flux<UomGroupDetails> findAllByUomGroupId(
            UUID uomGroupId, String company) {
        Condition whereClause = Conditions.isEqual(entityTable.column("uom_group_id"),
                Conditions.just(StringUtils.wrap(uomGroupId.toString(), "'")));
        whereClause = whereClause
                .and(Conditions.isEqual(entityTable.column("active"), Conditions.just(StringUtils.wrap("true", "'"))));
        whereClause = whereClause.and(Conditions.isNull(entityTable.column("delete_at")));
        whereClause = whereClause.and(
                Conditions.isEqual(entityTable.column("company"), Conditions.just(StringUtils.wrap(company, "'"))));
        whereClause = whereClause.and(Conditions.isNull(entityTable.column("delete_by")));
        whereClause = whereClause.and(Conditions.isNull(entityTable.column("delete_at")));
        return createQuery(null, whereClause).all();
    }
}
