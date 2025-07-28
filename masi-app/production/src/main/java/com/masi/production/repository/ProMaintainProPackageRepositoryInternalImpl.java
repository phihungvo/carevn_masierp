package com.masi.production.repository;

import com.masi.production.domain.ProMaintainProPackage;
import com.masi.production.repository.rowmapper.ProMaintainProPackageRowMapper;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
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

import java.util.List;
import java.util.UUID;

/**
 * Spring Data R2DBC custom repository implementation for the ProMaintainProPackage entity.
 */
@SuppressWarnings("unused")
class ProMaintainProPackageRepositoryInternalImpl
    extends SimpleR2dbcRepository<ProMaintainProPackage, UUID>
    implements ProMaintainProPackageRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final ProMaintainProPackageRowMapper promaintainpropackageMapper;

    private static final Table entityTable = Table.aliased("pro_maintain_pro_package", EntityManager.ENTITY_ALIAS);

    public ProMaintainProPackageRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        ProMaintainProPackageRowMapper promaintainpropackageMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(ProMaintainProPackage.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.promaintainpropackageMapper = promaintainpropackageMapper;
    }

    @Override
    public Flux<ProMaintainProPackage> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<ProMaintainProPackage> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = ProMaintainProPackageSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        SelectFromAndJoin selectFrom = Select.builder().select(columns).from(entityTable);
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, ProMaintainProPackage.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<ProMaintainProPackage> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<ProMaintainProPackage> findById(UUID id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(StringUtils.wrap(id.toString(), "'")));
        return createQuery(null, whereClause).one();
    }

    private ProMaintainProPackage process(Row row, RowMetadata metadata) {
        ProMaintainProPackage entity = promaintainpropackageMapper.apply(row, "e");
        return entity;
    }

    @Override
    public <S extends ProMaintainProPackage> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
