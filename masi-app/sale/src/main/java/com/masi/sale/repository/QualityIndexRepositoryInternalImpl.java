package com.masi.sale.repository;

import com.masi.sale.domain.QualityIndex;
import com.masi.sale.repository.rowmapper.QualityIndexRowMapper;
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
 * Spring Data R2DBC custom repository implementation for the QualityIndex entity.
 */
@SuppressWarnings("unused")
class QualityIndexRepositoryInternalImpl extends SimpleR2dbcRepository<QualityIndex, UUID> implements QualityIndexRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final QualityIndexRowMapper qualityindexMapper;

    private static final Table entityTable = Table.aliased("quality_index", EntityManager.ENTITY_ALIAS);

    public QualityIndexRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        QualityIndexRowMapper qualityindexMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(QualityIndex.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.qualityindexMapper = qualityindexMapper;
    }

    @Override
    public Flux<QualityIndex> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<QualityIndex> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = QualityIndexSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        SelectFromAndJoin selectFrom = Select.builder().select(columns).from(entityTable);
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, QualityIndex.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<QualityIndex> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<QualityIndex> findById(UUID id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(StringUtils.wrap(id.toString(), "'")));
        return createQuery(null, whereClause).one();
    }

    private QualityIndex process(Row row, RowMetadata metadata) {
        QualityIndex entity = qualityindexMapper.apply(row, "e");
        return entity;
    }

    @Override
    public <S extends QualityIndex> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
