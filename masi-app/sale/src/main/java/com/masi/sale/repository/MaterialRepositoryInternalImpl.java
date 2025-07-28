package com.masi.sale.repository;

import com.masi.sale.domain.Material;
import com.masi.sale.repository.rowmapper.ContractMaterialRowMapper;
import com.masi.sale.repository.rowmapper.MaterialRowMapper;
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
 * Spring Data R2DBC custom repository implementation for the Material entity.
 */
@SuppressWarnings("unused")
class MaterialRepositoryInternalImpl extends SimpleR2dbcRepository<Material, UUID> implements MaterialRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final ContractMaterialRowMapper contractproductMapper;
    private final MaterialRowMapper productMapper;

    private static final Table entityTable = Table.aliased("material", EntityManager.ENTITY_ALIAS);
    private static final Table contractProductTable = Table.aliased("contract_material", "contractMaterial");

    public MaterialRepositoryInternalImpl(
            R2dbcEntityTemplate template,
            EntityManager entityManager,
            ContractMaterialRowMapper contractproductMapper,
            MaterialRowMapper productMapper,
            R2dbcEntityOperations entityOperations,
            R2dbcConverter converter
    ) {
        super(
                new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(Material.class)),
                entityOperations,
                converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.contractproductMapper = contractproductMapper;
        this.productMapper = productMapper;
    }

    @Override
    public Flux<Material> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<Material> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = MaterialSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(ContractMaterialSqlHelper.getColumns(contractProductTable, "contractProduct"));
        SelectFromAndJoinCondition selectFrom = Select.builder()
                .select(columns)
                .from(entityTable)
                .leftOuterJoin(contractProductTable)
                .on(Column.create("contract_product_id", entityTable))
                .equals(Column.create("id", contractProductTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, Material.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<Material> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<Material> findById(UUID id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(StringUtils.wrap(id.toString(), "'")));
        return createQuery(null, whereClause).one();
    }


    private Material process(Row row, RowMetadata metadata) {
        Material entity = productMapper.apply(row, "e");
        return entity;
    }

    @Override
    public <S extends Material> Mono<S> save(S entity) {
        return super.save(entity);
    }

}
