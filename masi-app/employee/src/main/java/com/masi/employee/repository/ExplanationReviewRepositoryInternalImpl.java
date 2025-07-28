package com.masi.employee.repository;

import com.masi.employee.domain.ExplanationReview;
import com.masi.employee.domain.enumeration.ReviewStatus;
import com.masi.employee.repository.rowmapper.EmployeeRowMapper;
import com.masi.employee.repository.rowmapper.ExplanationReviewRowMapper;
import com.masi.employee.repository.rowmapper.TimeKeepingExplanationRowMapper;
import com.masi.employee.service.dto.ExplanationRequestObjectBase;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

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
 * Spring Data R2DBC custom repository implementation for the ExplanationReview entity.
 */
@SuppressWarnings("unused")
class ExplanationReviewRepositoryInternalImpl
    extends SimpleR2dbcRepository<ExplanationReview, UUID>
    implements ExplanationReviewRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final EmployeeRowMapper employeeMapper;
    private final TimeKeepingExplanationRowMapper timekeepingexplanationMapper;
    private final ExplanationReviewRowMapper explanationreviewMapper;

    private static final Table entityTable = Table.aliased("explanation_review", EntityManager.ENTITY_ALIAS);
    private static final Table reviewerTable = Table.aliased("employee", "reviewer");
    private static final Table explanationTable = Table.aliased("time_keeping_explanation", "explanation");

    public ExplanationReviewRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        EmployeeRowMapper employeeMapper,
        TimeKeepingExplanationRowMapper timekeepingexplanationMapper,
        ExplanationReviewRowMapper explanationreviewMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(ExplanationReview.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.employeeMapper = employeeMapper;
        this.timekeepingexplanationMapper = timekeepingexplanationMapper;
        this.explanationreviewMapper = explanationreviewMapper;
    }

    @Override
    public Flux<ExplanationReview> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    @Override
    public Flux<ExplanationReview> findAllBy(ExplanationRequestObjectBase reviewRO, Pageable pageable) {
        if (Objects.nonNull(reviewRO)) {
            return createQuery(pageable, buildWhereClause(reviewRO)).all();
        } else {
            return findAllBy(pageable);
        }
    }

    RowsFetchSpec<ExplanationReview> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = ExplanationReviewSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(EmployeeSqlHelper.getColumns(reviewerTable, "reviewer"));
        columns.addAll(TimeKeepingExplanationSqlHelper.getColumns(explanationTable, "explanation"));
        SelectFromAndJoinCondition selectFrom = Select.builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(reviewerTable)
            .on(Column.create("reviewer_id", entityTable))
            .equals(Column.create("id", reviewerTable))
            .leftOuterJoin(explanationTable)
            .on(Column.create("explanation_id", entityTable))
            .equals(Column.create("id", explanationTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, ExplanationReview.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<ExplanationReview> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<ExplanationReview> findById(UUID id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(StringUtils.wrap(id.toString(), "'")));
        return createQuery(null, whereClause).one();
    }

    @Override
    public Flux<ExplanationReview> findAllByExplanationId(UUID id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("explanation_id"), Conditions.just(StringUtils.wrap(id.toString(), "'")));
        return createQuery(null, whereClause).all();
    }

    private ExplanationReview process(Row row, RowMetadata metadata) {
        ExplanationReview entity = explanationreviewMapper.apply(row, "e");
        entity.setReviewer(employeeMapper.apply(row, "reviewer"));
        entity.setExplanation(timekeepingexplanationMapper.apply(row, "explanation"));
        return entity;
    }

    private String buildStatusInClause(List<String> statuses) {
        List<String> statusList = statuses.stream()
            .map(status -> "'" + status + "'")
            .collect(Collectors.toList());
        return String.join(",", statusList);
    }

    private Condition buildWhereClause(ExplanationRequestObjectBase reviewRO) {
        Condition whereClause = Conditions.isEqual(entityTable.column("is_active"), Conditions.just("true"));

        if (reviewRO.getId() != null) {
            whereClause = whereClause.and(Conditions.isEqual(entityTable.column("id"), Conditions.just(StringUtils.wrap(reviewRO.getId().toString(), "'"))));
        }
        if (reviewRO.getExplanationId() != null) {
            whereClause = whereClause.and(Conditions.isEqual(entityTable.column("explanation_id"), Conditions.just(StringUtils.wrap(reviewRO.getExplanationId().toString(), "'"))));
        }
        if (reviewRO.getReviewerId() != null) {
            whereClause = whereClause.and(Conditions.isEqual(entityTable.column("reviewer_id"), Conditions.just(StringUtils.wrap(reviewRO.getReviewerId().toString(), "'"))));
        }
        if (reviewRO.getStatuses() != null && !reviewRO.getStatuses().isEmpty()) {

            String inCondition = "status IN (" + buildStatusInClause(reviewRO.getStatuses()) + ")";
            whereClause = whereClause.and(Conditions.isEqual(entityTable.column(inCondition), Conditions.just("true")));
        }

        return whereClause;
    }

    @Override
    public <S extends ExplanationReview> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
