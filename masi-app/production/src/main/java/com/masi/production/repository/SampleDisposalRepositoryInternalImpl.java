package com.masi.production.repository;

import com.masi.production.domain.SampleDisposal;
import com.masi.production.repository.rowmapper.SampleDisposalRowMapper;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
 * Spring Data R2DBC custom repository implementation for the SampleDisposal entity.
 */
@SuppressWarnings("unused")
class SampleDisposalRepositoryInternalImpl extends SimpleR2dbcRepository<SampleDisposal, UUID> implements SampleDisposalRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final SampleDisposalRowMapper sampledisposalMapper;

    private static final Table entityTable = Table.aliased("sample_disposal", EntityManager.ENTITY_ALIAS);

    public SampleDisposalRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        SampleDisposalRowMapper sampledisposalMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(SampleDisposal.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.sampledisposalMapper = sampledisposalMapper;
    }

    @Override
    public Flux<SampleDisposal> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<SampleDisposal> createQuery(Pageable pageable, Condition whereClause) {
        if (Objects.isNull(pageable) || pageable.getSort().equals(Sort.unsorted())) {
            pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.DESC, "created_at"));
        }
        List<Expression> columns = SampleDisposalSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        SelectFromAndJoin selectFrom = Select.builder().select(columns).from(entityTable);
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, SampleDisposal.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<SampleDisposal> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<SampleDisposal> findById(UUID id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(StringUtils.wrap(id.toString(), "'")));
        return createQuery(null, whereClause).one();
    }

    private SampleDisposal process(Row row, RowMetadata metadata) {
        SampleDisposal entity = sampledisposalMapper.apply(row, "e");
        return entity;
    }

    @Override
    public <S extends SampleDisposal> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
