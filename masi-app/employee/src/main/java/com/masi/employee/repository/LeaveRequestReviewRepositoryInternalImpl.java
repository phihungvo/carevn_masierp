package com.masi.employee.repository;

import com.masi.employee.domain.LeaveRequestReview;
import com.masi.employee.domain.enumeration.ReviewStatus;
import com.masi.employee.repository.rowmapper.EmployeeRowMapper;
import com.masi.employee.repository.rowmapper.LeaveRequestReviewRowMapper;
import com.masi.employee.repository.rowmapper.LeaveRequestRowMapper;
import com.masi.employee.service.dto.LeaveRequestRequestObjectBase;
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
 * Spring Data R2DBC custom repository implementation for the LeaveRequestReview entity.
 */
@SuppressWarnings("unused")
class LeaveRequestReviewRepositoryInternalImpl
    extends SimpleR2dbcRepository<LeaveRequestReview, UUID>
    implements LeaveRequestReviewRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final LeaveRequestRowMapper leaverequestMapper;
    private final LeaveRequestReviewRowMapper leaverequestreviewMapper;
    private final EmployeeRowMapper employeeMapper;

    private static final Table entityTable = Table.aliased("leave_request_review", EntityManager.ENTITY_ALIAS);
    private static final Table leaveRequestTable = Table.aliased("leave_request", "leaveRequest");
    private static final Table employeeTable = Table.aliased("employee", "employee");


    public LeaveRequestReviewRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        LeaveRequestRowMapper leaverequestMapper,
        LeaveRequestReviewRowMapper leaverequestreviewMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter, EmployeeRowMapper employeeMapper
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(LeaveRequestReview.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.leaverequestMapper = leaverequestMapper;
        this.leaverequestreviewMapper = leaverequestreviewMapper;
        this.employeeMapper = employeeMapper;
    }

    @Override
    public Flux<LeaveRequestReview> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    @Override
    public Flux<LeaveRequestReview> findAllBy(LeaveRequestRequestObjectBase reviewRO, Pageable pageable) {
        if (Objects.nonNull(reviewRO)) {
            return createQuery(pageable, buildWhereClause(reviewRO)).all();
        } else {
            return findAllBy(pageable);
        }
    }

    RowsFetchSpec<LeaveRequestReview> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = LeaveRequestReviewSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(LeaveRequestSqlHelper.getColumns(leaveRequestTable, "leaveRequest"));
        // select thêm các cột của bảng employee
        columns.addAll(EmployeeSqlHelper.getColumns(employeeTable, "employee"));
        SelectFromAndJoinCondition selectFrom = Select.builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(leaveRequestTable)
            .on(Column.create("leave_request_id", entityTable))
            .equals(Column.create("id", leaveRequestTable))
            // join thêm bảng employee
            .leftOuterJoin(employeeTable)
            .on(Column.create("reviewer_id", entityTable))
            .equals(Column.create("id", employeeTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, LeaveRequestReview.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<LeaveRequestReview> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<LeaveRequestReview> findById(UUID id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(StringUtils.wrap(id.toString(), "'")));
        return createQuery(null, whereClause).one();
    }

    @Override
    public Flux<LeaveRequestReview> findAllByLeaveRequestId(UUID leaveRequestId) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("leave_request_id"), Conditions.just(StringUtils.wrap(leaveRequestId.toString(), "'")));
        return createQuery(null, whereClause).all();

    }

    private LeaveRequestReview process(Row row, RowMetadata metadata) {
        LeaveRequestReview entity = leaverequestreviewMapper.apply(row, "e");
        entity.setLeaveRequest(leaverequestMapper.apply(row, "leaveRequest"));
        // set thêm reviewer
        entity.setReviewer(employeeMapper.apply(row, "employee"));
        return entity;
    }

    private Condition buildWhereClause(LeaveRequestRequestObjectBase reviewRO) {
        Condition whereClause = Conditions.isEqual(entityTable.column("is_active"), Conditions.just("true"));

        if (reviewRO.getId() != null) {
            whereClause = whereClause.and(Conditions.isEqual(entityTable.column("id"), Conditions.just(StringUtils.wrap(reviewRO.getId().toString(), "'"))));
        }
        if (reviewRO.getLeaveRequestId() != null) {
            whereClause = whereClause.and(Conditions.isEqual(entityTable.column("leave_request_id"), Conditions.just(StringUtils.wrap(reviewRO.getLeaveRequestId().toString(), "'"))));
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

    private String buildStatusInClause(List<String> statuses) {
        List<String> statusList = statuses.stream()
            .map(status -> "'" + status + "'")
            .collect(Collectors.toList());
        return String.join(",", statusList);
    }

    @Override
    public <S extends LeaveRequestReview> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
