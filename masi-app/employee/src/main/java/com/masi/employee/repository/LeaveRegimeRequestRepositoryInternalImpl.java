package com.masi.employee.repository;

import com.carevn.masi.constants.AuthoritiesConstants;
import com.masi.employee.domain.Employee;
import com.masi.employee.domain.LeaveRegimeRequest;
import com.masi.employee.domain.ProcessLeaveRegimeRequest;
import com.masi.employee.domain.enumeration.LeaveRegimeRequestStatus;
import com.masi.employee.domain.enumeration.LeaveType;
import com.masi.employee.repository.rowmapper.ColumnConverter;
import com.masi.employee.repository.rowmapper.EmployeeRowMapper;
import com.masi.employee.repository.rowmapper.LeaveRegimeRequestRowMapper;
import com.masi.employee.repository.rowmapper.ProcessLeaveRegimeRequestRowMapper;
import com.masi.employee.service.dto.LeaveRegimeRequestFilterDTO;
import com.masi.employee.service.dto.LeaveRegimeRequestGetListDTO;

import com.masi.employee.web.rest.errors.BadRequestAlertException;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import tech.jhipster.service.ConditionBuilder;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.UUIDFilter;
import tech.jhipster.service.filter.ZonedDateTimeFilter;

/**
 * Spring Data R2DBC custom repository implementation for the LeaveRegimeRequest
 * entity.
 */
@SuppressWarnings("unused")
class LeaveRegimeRequestRepositoryInternalImpl
    extends SimpleR2dbcRepository<LeaveRegimeRequest, Long>
    implements LeaveRegimeRequestRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;
    private final EmployeeRowMapper employeeMapper;

    private final LeaveRegimeRequestRowMapper leaveregimerequestMapper;
    private final ProcessLeaveRegimeRequestRowMapper processLeaveregimerequestMapper;
    private final ColumnConverter columnConverter;

    private static final Table entityTable = Table.aliased("leave_regime_request", EntityManager.ENTITY_ALIAS);
    private static final Table substituteTable = Table.aliased("employee", "substitute");
    private static final Table employeeTable = Table.aliased("employee", "employee");
    private static final Table processTable = Table.aliased("process_leave_regime_request", "p");
    private static final Table profileTable = Table.aliased("employee_profile", "profile");
    private static final Table workspaceTable = Table.aliased("workspace", "workspace");
    private Map<UUID, LeaveRegimeRequest> groups = new HashMap<>();

    @SuppressWarnings({"unchecked", "rawtypes"})
    public LeaveRegimeRequestRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        LeaveRegimeRequestRowMapper leaveregimerequestMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter, EmployeeRowMapper employeeMapper,
        ColumnConverter columnConverter, ProcessLeaveRegimeRequestRowMapper processLeaveregimerequestMapper) {
        super(
            new MappingRelationalEntityInformation(
                converter.getMappingContext().getRequiredPersistentEntity(LeaveRegimeRequest.class)),
            entityOperations,
            converter);
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.leaveregimerequestMapper = leaveregimerequestMapper;
        this.employeeMapper = employeeMapper;
        this.processLeaveregimerequestMapper = processLeaveregimerequestMapper;
        this.columnConverter = columnConverter;
    }

    @Override
    public Flux<LeaveRegimeRequest> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<LeaveRegimeRequest> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = LeaveRegimeRequestSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(EmployeeSqlHelper.getColumns(substituteTable, "substitute"));
        columns.addAll(EmployeeSqlHelper.getColumns(employeeTable, "employee"));
        columns.addAll(EmployeeProfileSqlHelper.getColumns(profileTable, "profile"));
        columns.addAll(WorkspaceSqlHelper.getColumns(workspaceTable, "workspace"));
        // columns.addAll(ProcessLeaveRegimeRequestSqlHelper.getColumns(processTable,
        // "p"));
        var selectFrom = Select.builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(substituteTable)
            .on(Column.create("substitute_id", entityTable))
            .equals(Column.create("id", substituteTable))
            .leftOuterJoin(employeeTable)
            .on(Column.create("employee_id", entityTable))
            .equals(Column.create("id", employeeTable))
            .leftOuterJoin(profileTable)
            .on(Column.create("id", employeeTable))
            .equals(Column.create("id", profileTable))
            .leftOuterJoin(workspaceTable)
            .on(Column.create("workspace_id", profileTable))
            .equals(Column.create("id", workspaceTable));

        // .leftOuterJoin(processTable)
        // .on(Column.create("id", entityTable))
        // .equals(Column.create("leave_regime_request_id", processTable));

        // we do not support Criteria here for now as of
        // https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, LeaveRegimeRequest.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<LeaveRegimeRequest> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<LeaveRegimeRequest> findById(UUID id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    private LeaveRegimeRequest process(Row row, RowMetadata metadata) {
        UUID requestId = row.get("e_id", UUID.class);
        LeaveRegimeRequest entity = groups.getOrDefault(requestId,
            leaveregimerequestMapper.apply(row, EntityManager.ENTITY_ALIAS));
        entity.setEmployee(employeeMapper.apply(row, "employee"));
        entity.setSubstitute(employeeMapper.apply(row, "substitute"));
        // if (row.get("p_id", UUID.class) != null) {
        // entity.getProcessLeaveRegimeRequests().add(processLeaveregimerequestMapper.apply(row,
        // "p"));
        // }

        // groups.put(requestId, entity);
        return entity;
    }

    @Override
    public <S extends LeaveRegimeRequest> Mono<S> save(S entity) {
        return super.save(entity);
    }

    @Override
    public Flux<LeaveRegimeRequest> findAllByFilter(Pageable pageable,
                                                    LeaveRegimeRequestGetListDTO leaveRegimeRequestGetListDTO, String role) {
        var conditionFilter = createConditionFilterDTO(leaveRegimeRequestGetListDTO, role);
        return createQuery(pageable, createCondition(conditionFilter)).all();
    }

    @Override
    public Mono<Long> countByFilter(LeaveRegimeRequestGetListDTO leaveRegimeRequestGetListDTO, String role) {
        var conditionFilter = createConditionFilterDTO(leaveRegimeRequestGetListDTO, role);
        return createQuery(null, createCondition(conditionFilter)).all().count();
    }

    private LeaveRegimeRequestFilterDTO createConditionFilterDTO(LeaveRegimeRequestGetListDTO dto, String auth) {
        var filter = new LeaveRegimeRequestFilterDTO();
        filter.isDeleted().setEquals(false);
//        filter.setWorkspaceType(dto.getWorkspaceType());
        if (dto.getStartDate() != null) {
            // ho chi minh timezone
            filter.startLeaveDate().setGreaterThanOrEqual(dto.getStartDate().atStartOfDay().atZone(ZoneId.of("Asia/Ho_Chi_Minh")));
        }
        if (dto.getEndDate() != null) {
            filter.endLeaveDate().setLessThanOrEqual(dto.getEndDate().atTime(23, 50).atZone(ZoneId.of("Asia/Ho_Chi_Minh")));
        }
        if (dto.getCompanyId() != null) {
            filter.companyId().setEquals(dto.getCompanyId());
        }
        if (dto.getStatus() != null && !dto.getStatus().isEmpty()) {
            filter.status().setIn(dto.getStatus());
        }
        if (dto.getLeaveType() != null && !dto.getLeaveType().isEmpty()) {
            filter.leaveType().setIn(dto.getLeaveType());
        }

        if (auth.contains(AuthoritiesConstants.DIRECTOR)) {
            return filter;
        } else {
            if (auth.contains(AuthoritiesConstants.STAFF)) {
                filter.employeeId().setEquals(dto.getEmployeeId());
            } else {
                if (auth.contains(AuthoritiesConstants.DEPARTMENT_MANAGER)) {
                    filter.department().setEquals(dto.getDepartment());
                }
            }
        }

        return filter;
    }

    private Condition createCondition(LeaveRegimeRequestFilterDTO filter) {

        ConditionBuilder builder = new ConditionBuilder(columnConverter);
        if (filter.getStatus() != null) {
            builder.buildFilterConditionForField(filter.getStatus(), entityTable.column("status"));
        }
        if (filter.getLeaveType() != null) {
            builder.buildFilterConditionForField(filter.getLeaveType(), entityTable.column("leave_type"));
        }
        if (filter.getStartLeaveDate() != null) {
            builder.buildFilterConditionForField(filter.getStartLeaveDate(),
                entityTable.column("last_work_date"));
        }
        if (filter.getEndLeaveDate() != null) {
            builder.buildFilterConditionForField(filter.getEndLeaveDate(),
                entityTable.column("last_work_date"));
        }

        if (filter.getIsDeleted() != null) {
            builder.buildFilterConditionForField(filter.getIsDeleted(), entityTable.column("is_deleted"));
        }

        if (filter.getCompanyId() != null) {
            builder.buildFilterConditionForField(filter.getCompanyId(), entityTable.column("company_id"));
        }

        var condition = builder.buildConditions();
//        if (StringUtils.isNotBlank(filter.getWorkspaceType().toString())) {
//            condition = condition.and(Conditions.isEqual(employeeTable.column("workspace_type"), Conditions.just("'" + filter.getWorkspaceType().toString() + "'")));
//        }
        return condition;
    }

    @Override
    public Mono<LeaveRegimeRequest> findNotDeleteById(UUID id) {
        // TODO Auto-generated method stub
        var conditionBuilder = new ConditionBuilder(columnConverter);
        conditionBuilder.buildFilterConditionForField(new UUIDFilter().setEquals(id), entityTable.column("id"));
        conditionBuilder.buildFilterConditionForField(new BooleanFilter().setEquals(false),
            entityTable.column("is_deleted"));
        var condition = conditionBuilder.buildConditions();
        var query = createQuery(null, condition).first().doOnError(RuntimeException::new);
        // handle when query return null
        return query.switchIfEmpty(
            Mono.error(new BadRequestAlertException("Entity not found", "LeaveRegimeRequest", "idnotfound")));
    }
}
