package com.masi.sale.repository;

import com.masi.sale.domain.ContractMaterial;
import com.masi.sale.repository.rowmapper.ContractMaterialRowMapper;
import com.masi.sale.repository.rowmapper.MaterialRowMapper;
import com.masi.sale.service.mapper.MaterialMapper;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
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
 * Spring Data R2DBC custom repository implementation for the ContractMaterial entity.
 */
@SuppressWarnings("unused")
class ContractMaterialRepositoryInternalImpl
    extends SimpleR2dbcRepository<ContractMaterial, UUID>
    implements ContractMaterialRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final ContractMaterialRowMapper contractproductMapper;
    private final MaterialRowMapper materialMapper;

    private static final Table entityTable = Table.aliased("contract_material", EntityManager.ENTITY_ALIAS);
    private static final Table materialTable = Table.aliased("material", "material");

    public ContractMaterialRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        ContractMaterialRowMapper contractproductMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter, MaterialRowMapper materialMapper
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(ContractMaterial.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.contractproductMapper = contractproductMapper;
        this.materialMapper = materialMapper;
    }

    @Override
    public Flux<ContractMaterial> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<ContractMaterial> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = ContractMaterialSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(MaterialSqlHelper.getColumns(materialTable, "material"));
        SelectBuilder.SelectFromAndJoinCondition selectFrom = Select.builder().select(columns).from(entityTable)
            .leftOuterJoin(materialTable).on(Column.create("id_material", entityTable))
            .equals(Column.create("id", materialTable));

        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, ContractMaterial.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<ContractMaterial> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<ContractMaterial> findById(UUID id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(StringUtils.wrap(id.toString(), "'")));
        return createQuery(null, whereClause).one();
    }

    @Override
    public Flux<ContractMaterial> findAllByIds(Collection<UUID> ids) {
        String in = ids.stream().map(id -> StringUtils.wrap(id.toString(), "'")).reduce((acc, val) -> acc + "," + val).orElse("");
        var whereClause = Conditions.in(entityTable.column("id"), Conditions.just(in));
        return createQuery(null, whereClause).all();
    }

    private ContractMaterial process(Row row, RowMetadata metadata) {
        ContractMaterial entity = contractproductMapper.apply(row, "e");
        entity.setMaterial(materialMapper.apply(row, "material"));
        return entity;
    }

    @Override
    public <S extends ContractMaterial> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
