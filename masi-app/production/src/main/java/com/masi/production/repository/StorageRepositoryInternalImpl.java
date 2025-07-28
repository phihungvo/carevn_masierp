package com.masi.production.repository;

import com.masi.production.domain.Storage;
import com.masi.production.repository.rowmapper.StorageRowMapper;
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
 * Spring Data R2DBC custom repository implementation for the Storage entity.
 */
@SuppressWarnings("unused")
class StorageRepositoryInternalImpl extends SimpleR2dbcRepository<Storage, UUID> implements StorageRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final StorageRowMapper storageMapper;

    private static final Table entityTable = Table.aliased("storage", EntityManager.ENTITY_ALIAS);

    public StorageRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        StorageRowMapper storageMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(Storage.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.storageMapper = storageMapper;
    }

    @Override
    public Flux<Storage> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<Storage> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = StorageSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        SelectFromAndJoin selectFrom = Select.builder().select(columns).from(entityTable);
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, Storage.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<Storage> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<Storage> findById(UUID id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(StringUtils.wrap(id.toString(), "'")));
        return createQuery(null, whereClause).one();
    }

    private Storage process(Row row, RowMetadata metadata) {
        Storage entity = storageMapper.apply(row, "e");
        return entity;
    }

    @Override
    public <S extends Storage> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
