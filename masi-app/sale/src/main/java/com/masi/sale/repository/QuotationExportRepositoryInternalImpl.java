package com.masi.sale.repository;

import com.masi.sale.domain.QuotationExport;
import com.masi.sale.repository.rowmapper.QuotationExportRowMapper;
import com.masi.sale.repository.rowmapper.QuotationRowMapper;
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
 * Spring Data R2DBC custom repository implementation for the QuotationExport entity.
 */
@SuppressWarnings("unused")
class QuotationExportRepositoryInternalImpl
    extends SimpleR2dbcRepository<QuotationExport, UUID>
    implements QuotationExportRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final QuotationRowMapper quotationMapper;
    private final QuotationExportRowMapper quotationexportMapper;

    private static final Table entityTable = Table.aliased("quotation_export", EntityManager.ENTITY_ALIAS);
    private static final Table quotationTable = Table.aliased("quotation", "quotation");

    public QuotationExportRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        QuotationRowMapper quotationMapper,
        QuotationExportRowMapper quotationexportMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(QuotationExport.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.quotationMapper = quotationMapper;
        this.quotationexportMapper = quotationexportMapper;
    }

    @Override
    public Flux<QuotationExport> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<QuotationExport> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = QuotationExportSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(QuotationSqlHelper.getColumns(quotationTable, "quotation"));
        SelectFromAndJoinCondition selectFrom = Select.builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(quotationTable)
            .on(Column.create("quotation_id", entityTable))
            .equals(Column.create("id", quotationTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, QuotationExport.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<QuotationExport> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<QuotationExport> findById(UUID id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(StringUtils.wrap(id.toString(), "'")));
        return createQuery(null, whereClause).one();
    }

    private QuotationExport process(Row row, RowMetadata metadata) {
        QuotationExport entity = quotationexportMapper.apply(row, "e");
        entity.setQuotation(quotationMapper.apply(row, "quotation"));
        return entity;
    }

    @Override
    public <S extends QuotationExport> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
