package com.masi.employee.repository;

import com.masi.employee.domain.MonthlyTimeSheetReview;
import com.masi.employee.domain.enumeration.TimeKeepingType;
import com.masi.employee.domain.enumeration.WorkspaceType;
import com.masi.employee.repository.rowmapper.MonthlyTimeSheetReviewRowMapper;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;

import java.time.LocalDate;
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
 * Spring Data R2DBC custom repository implementation for the MonthlyTimeSheetReview entity.
 */
@SuppressWarnings("unused")
class MonthlyTimeSheetReviewRepositoryInternalImpl
    extends SimpleR2dbcRepository<MonthlyTimeSheetReview, UUID>
    implements MonthlyTimeSheetReviewRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final MonthlyTimeSheetReviewRowMapper monthlytimesheetreviewMapper;

    private static final Table entityTable = Table.aliased("monthly_time_sheet_review", EntityManager.ENTITY_ALIAS);

    public MonthlyTimeSheetReviewRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        MonthlyTimeSheetReviewRowMapper monthlytimesheetreviewMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(MonthlyTimeSheetReview.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.monthlytimesheetreviewMapper = monthlytimesheetreviewMapper;
    }

    @Override
    public Flux<MonthlyTimeSheetReview> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<MonthlyTimeSheetReview> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = MonthlyTimeSheetReviewSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        SelectFromAndJoin selectFrom = Select.builder().select(columns).from(entityTable);
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, MonthlyTimeSheetReview.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<MonthlyTimeSheetReview> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<MonthlyTimeSheetReview> findById(UUID id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(StringUtils.wrap(id.toString(), "'")));
        return createQuery(null, whereClause).one();
    }

    private MonthlyTimeSheetReview process(Row row, RowMetadata metadata) {
        MonthlyTimeSheetReview entity = monthlytimesheetreviewMapper.apply(row, "e");
        return entity;
    }

    @Override
    public <S extends MonthlyTimeSheetReview> Mono<S> save(S entity) {
        return super.save(entity);
    }

    @Override
    public Mono<MonthlyTimeSheetReview> findByMonth(LocalDate month, WorkspaceType type, TimeKeepingType timeKeepingType) {
        String subQuery = String.format("""
                SELECT pmt.review_id
                from personal_monthly_timesheet as pmt
                         join employee_profile as ep
                              on ep.id = pmt.employee_id
                         join workspace as ws
                              on ws.id = ep.workspace_id
                where pmt.month = '%s'
                  and ('%s' is not null or pmt.type = '%s')
                  and ('%s' is not null or ws.workspace_type = '%s')
                  and review_id IS NOT NULL
                LIMIT 1
                """,
            month, timeKeepingType, timeKeepingType, type, type);
        String columnAlias = EntityManager.ENTITY_ALIAS + "." + "id";
        String whereClause = columnAlias + " IN (" + subQuery + ")";
        return createQuery(null, Conditions.just(whereClause)).one();
    }
}
