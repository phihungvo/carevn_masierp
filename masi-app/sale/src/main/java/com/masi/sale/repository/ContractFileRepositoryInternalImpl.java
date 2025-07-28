package com.masi.sale.repository;

import com.masi.sale.domain.ContractFile;
import com.masi.sale.repository.rowmapper.ContractFileRowMapper;
import com.masi.sale.repository.rowmapper.ContractRowMapper;
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
 * Spring Data R2DBC custom repository implementation for the ContractFile entity.
 */
@SuppressWarnings("unused")
class ContractFileRepositoryInternalImpl extends SimpleR2dbcRepository<ContractFile, UUID> implements ContractFileRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final ContractRowMapper contractMapper;
    private final ContractFileRowMapper contractfileMapper;

    private static final Table entityTable = Table.aliased("contract_file", EntityManager.ENTITY_ALIAS);
    private static final Table contractTable = Table.aliased("contract", "contract");

    public ContractFileRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        ContractRowMapper contractMapper,
        ContractFileRowMapper contractfileMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(ContractFile.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.contractMapper = contractMapper;
        this.contractfileMapper = contractfileMapper;
    }

    @Override
    public Flux<ContractFile> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<ContractFile> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = ContractFileSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(ContractSqlHelper.getColumns(contractTable, "contract"));
        SelectFromAndJoinCondition selectFrom = Select.builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(contractTable)
            .on(Column.create("contract_id", entityTable))
            .equals(Column.create("id", contractTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, ContractFile.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<ContractFile> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<ContractFile> findById(UUID id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(StringUtils.wrap(id.toString(), "'")));
        return createQuery(null, whereClause).one();
    }

    private ContractFile process(Row row, RowMetadata metadata) {
        ContractFile entity = contractfileMapper.apply(row, "e");
        return entity;
    }

    @Override
    public <S extends ContractFile> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
