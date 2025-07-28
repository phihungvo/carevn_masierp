package com.masi.employee.repository;

import com.masi.employee.domain.EmployeeProfile;
import com.masi.employee.domain.Workspace;
import com.masi.employee.repository.rowmapper.ColumnConverter;
import com.masi.employee.repository.rowmapper.EmployeeProfileRowMapper;
import com.masi.employee.repository.rowmapper.EmployeeRowMapper;
import com.masi.employee.repository.rowmapper.WorkspaceRowMapper;
import com.masi.employee.service.dto.EmployeeProfileQuery;
import com.masi.employee.service.dto.EmployeeProfileXlsx;
import com.masi.employee.service.mapper.WorkspaceMapper;
import com.masi.employee.service.reports.HumanResourceChangeReport;
import io.r2dbc.spi.Parameters;
import io.r2dbc.spi.R2dbcType;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.ap.internal.model.common.Type;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.r2dbc.convert.R2dbcConverter;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.support.SimpleR2dbcRepository;
import org.springframework.data.relational.core.sql.*;
import org.springframework.data.relational.core.sql.SelectBuilder.SelectFromAndJoin;
import org.springframework.data.relational.domain.SqlSort;
import org.springframework.data.relational.repository.support.MappingRelationalEntityInformation;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.r2dbc.core.RowsFetchSpec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

/**
 * Spring Data R2DBC custom repository implementation for the EmployeeProfile entity.
 */
@Slf4j
@SuppressWarnings("unused")
class EmployeeProfileRepositoryInternalImpl
        extends SimpleR2dbcRepository<EmployeeProfile, Long>
        implements EmployeeProfileRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;
    private final ColumnConverter converter;

    private final EmployeeProfileRowMapper employeeprofileMapper;
    private final WorkspaceRowMapper workspaceMapper;
    private final EmployeeRowMapper employeeRowMapper;

    private static final Table entityTable = Table.aliased("employee_profile", EntityManager.ENTITY_ALIAS);
    private static final Table workspaceTable = Table.aliased("workspace", "workspace");
    private static final Table employee = Table.aliased("employee", "employee");

    public EmployeeProfileRepositoryInternalImpl(
            R2dbcEntityTemplate template,
            EntityManager entityManager,
            EmployeeProfileRowMapper employeeprofileMapper,
            R2dbcEntityOperations entityOperations,
            R2dbcConverter converter, ColumnConverter converter1, WorkspaceRowMapper workspaceMapper, EmployeeRowMapper employeeRowMapper
    ) {
        super(
                new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(EmployeeProfile.class)),
                entityOperations,
                converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.employeeprofileMapper = employeeprofileMapper;
        this.converter = converter1;
        this.workspaceMapper = workspaceMapper;
        this.employeeRowMapper = employeeRowMapper;
    }

    @Override
    public Flux<EmployeeProfile> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<EmployeeProfile> createQuery(Pageable pageable, Condition whereClause) {
        return this.createQuery(pageable, whereClause, null);
    }

    RowsFetchSpec<EmployeeProfile> createQuery(Pageable pageable, Condition whereClause, Map<String, Object> parameters) {
        List<Expression> columns = EmployeeProfileSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(WorkspaceSqlHelper.getColumns(workspaceTable, "workspace"));
        columns.addAll(EmployeeSqlHelper.getColumns(employee, "employee"));
        SelectBuilder.SelectFromAndJoinCondition selectFrom = Select.builder().select(columns).from(entityTable)
                .leftOuterJoin(workspaceTable)
                .on(Column.create("workspace_id", entityTable))
                .equals(Column.create("id", workspaceTable))
                .leftOuterJoin(employee)
                .on(Column.create("referrer_id", entityTable))
                .equals(Column.create("id", employee));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, EmployeeProfile.class, pageable, whereClause);
        DatabaseClient.GenericExecuteSpec spec = db.sql(select);
        if (Objects.nonNull(parameters) && !parameters.isEmpty()) {
            spec = spec.bindValues(parameters);
        }
        return spec.map(this::process);
    }

    RowsFetchSpec<EmployeeProfile> createQuery(Pageable pageable, Condition whereClause, Map<String, Object> parameters, boolean withCustomSort) {
        if (!withCustomSort) {
            return createQuery(pageable, whereClause, parameters);
        }
        List<Expression> columns = EmployeeProfileSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(WorkspaceSqlHelper.getColumns(workspaceTable, "workspace"));
        columns.addAll(EmployeeSqlHelper.getColumns(employee, "employee"));
        SelectBuilder.SelectFromAndJoinCondition selectFrom = Select.builder().select(columns).from(entityTable)
                .leftOuterJoin(workspaceTable)
                .on(Column.create("workspace_id", entityTable))
                .equals(Column.create("id", workspaceTable))
                .leftOuterJoin(employee)
                .on(Column.create("referrer_id", entityTable))
                .equals(Column.create("id", employee));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, EmployeeProfile.class, null, whereClause);
        select += " ORDER BY " +
                "CASE " +
                "    WHEN e.employee_code ~ '^[FM0-9]+$' THEN REGEXP_REPLACE(e.employee_code, '(F|M)', '', 'g')::int " +
                "    ELSE NULL " +
                "END DESC, " +
                "CASE " +
                "    WHEN e.employee_code ~ '^[FM0-9]+$' THEN 0 " +
                "    ELSE 1 " +
                "END ASC, " +
                "e.employee_code ASC " +
                "LIMIT " + pageable.getPageSize() + " OFFSET " + pageable.getOffset();
        DatabaseClient.GenericExecuteSpec spec = db.sql(select);
        if (Objects.nonNull(parameters) && !parameters.isEmpty()) {
            spec = spec.bindValues(parameters);
        }
        return spec.map(this::process);
    }


    @Override
    public Flux<EmployeeProfile> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<EmployeeProfile> findFirstById(UUID id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(StringUtils.wrap(id.toString(), "'")));
        var pageable = PageRequest.of(0, 1);
        return createQuery(pageable, whereClause).one();
    }

    private Tuple2<Condition, Map<String, Object>> createWhereClause(EmployeeProfileQuery query) {
        Condition whereClause = Conditions.isEqual(entityTable.column("is_deleted"), Conditions.just("false"));
        Map<String, Object> parameters = new HashMap<>();
        var isActive = query.getIsActive();
        if (query.getIsActive() != null) {
            whereClause = whereClause.and(entityTable.column("is_active").isEqualTo(Conditions.just(isActive.toString())));
        }
        if (Objects.nonNull(query.getWorkspaceIds()) && !query.getWorkspaceIds().isEmpty()) {
            String inClause = query.getWorkspaceIds().stream().map(id -> String.format("'%s'", id.toString())).reduce((a, b) -> a + "," + b).orElseThrow();
            whereClause = whereClause.and(entityTable.column("workspace_id").in(Conditions.just(inClause)));
        }
        if (Objects.nonNull(query.getEmployeeStatuses()) && !query.getEmployeeStatuses().isEmpty()) {
            String inClause = query.getEmployeeStatuses().stream().map(id -> String.format("'%s'", id.toString())).reduce((a, b) -> a + "," + b).orElseThrow();
            whereClause = whereClause.and(entityTable.column("status").in(Conditions.just(inClause)));
        }
        if (StringUtils.isNotBlank(query.getDepartment())) {
            whereClause = whereClause.and(Conditions.just(String.format("unaccent(%s) ilike unaccent(:department)", entityTable.column("department"))));
            parameters.put("department", "%" + query.getDepartment().trim() + "%");
        }
        if (StringUtils.isNotBlank(query.getCompany())) {
            whereClause = whereClause.and(Conditions.just(String.format("unaccent(%s) ilike unaccent(:company)", entityTable.column("company"))));
            parameters.put("company", "%" + query.getCompany().trim() + "%");
        }
        if (query.getWorkspaceTypes() != null && !query.getWorkspaceTypes().isEmpty()) {
            String inClause = query.getWorkspaceTypes().stream().map(type -> String.format("'%s'", type.toString())).reduce((a, b) -> a + "," + b).orElseThrow();
            whereClause = whereClause.and(workspaceTable.column("workspace_type").in(Conditions.just(inClause)));
        }
        if (CollectionUtils.isNotEmpty(query.getWorkspaceNNames())) {
            String inClause = query.getWorkspaceNNames().stream().map(name -> String.format("'%s'", name)).reduce((a, b) -> a + "," + b).orElseThrow();
            whereClause = whereClause.and(workspaceTable.column("normalized_name").in(Conditions.just(inClause)));
        }
        if (query.getSearch() != null) {
            String[] columns = {"employee_code", "full_name", "citizen_id", "phone"};
            String[] aliases = Arrays.stream(columns).map(column -> EntityManager.ENTITY_ALIAS + "." + column).toArray(String[]::new);
            StringBuilder search = new StringBuilder();
            search.append('(');
            Arrays.stream(aliases).forEach(alias -> {
                search.append(String.format("unaccent(%s) ilike unaccent(:search) or ", alias));
            });
            int searchLength = search.length();
            search.delete(searchLength - 3, searchLength);
            search.append(')');
            whereClause = whereClause.and(Conditions.just(search.toString()));
            parameters.put("search", "%" + query.getSearch().trim() + "%");
        }
        if (query.getHasAccount() != null) {
            var hasAccount = query.getHasAccount();
            if (hasAccount) {
                whereClause = whereClause.and(Conditions.isNotEqual(entityTable.column("account_status"), Conditions.just(StringUtils.wrap("NOT_HAVING_ACCOUNT", "'"))));
            } else {
                whereClause = whereClause.and(Conditions.isEqual(entityTable.column("account_status"), Conditions.just(StringUtils.wrap("NOT_HAVING_ACCOUNT", "'"))));
            }
        }
        return Tuples.of(whereClause, parameters);
    }

    private Pageable getDefaultSort(Pageable pageable) {


        Sort defaultSort = SqlSort.unsafe(Sort.Direction.DESC, "created_at");
        if (Objects.isNull(pageable)) {
            pageable = PageRequest.of(0, Integer.MAX_VALUE, defaultSort);
        }
        if (pageable.getSort().equals(Sort.unsorted())) {
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), defaultSort);
        }
        return pageable;
    }

    @Override
    public Flux<EmployeeProfile> findAllByQuery(EmployeeProfileQuery query, Pageable pageable) {
        pageable = getDefaultSort(pageable);
        Tuple2<Condition, Map<String, Object>> whereClause = createWhereClause(query);
        return createQuery(pageable, whereClause.getT1(), whereClause.getT2(), true).all();
    }

    @Override
    public Mono<Long> countByQuery(EmployeeProfileQuery query) {
        Tuple2<Condition, Map<String, Object>> whereClause = createWhereClause(query);
        return createQuery(null, whereClause.getT1(), whereClause.getT2()).all().count();
    }

    @Override
    public Mono<EmployeeProfile> findByIdAndNotDelete(UUID id) {
        //  @Query("SELECT * FROM employee_profile entity WHERE entity.id = :id and entity.is_deleted = false")
//
        Condition whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(String.format("'%s'", id.toString()))).and(Conditions.isEqual(entityTable.column("is_deleted"), Conditions.just("false")));
        return createQuery(null, whereClause).one();
    }

    private Tuple2<String, Map<String, Object>> buildHumanResourceChangeReportQuery(HumanResourceChangeReport.Query query) {
        String sql = """
                    SELECT sum(temp.total_join) as total_join, sum(temp.total_leave) as total_leave,temp.month from\s
                        (
                                SELECT to_char(cl.leave_date, 'YYYY-MM') as month, count(*) as total_leave, 0 as total_join  from confirm_leave cl join employee_profile ep on cl.id = ep.id
                                                                                                                             where cl.leave_date >= :startDate and cl.leave_date <= :endDate
                                                                                                                              $departmentClause
                                                                                                                             and ep.company = :company
                                                                                                                             GROUP BY(to_char(cl.leave_date, 'YYYY-MM'))
                                UNION ALL
                                SELECT to_char(ep.start_work_date, 'YYYY-MM') as month, 0 as total_leave,count(*) as total_join  from employee_profile ep

                                                                                                                                where ep.start_work_date >= :startDate and ep.start_work_date <= :endDate
                                                                                                                                 and ep.company = :company
                                                                                                                                  $departmentClause
                                                                                                                                 GROUP BY(to_char(ep.start_work_date, 'YYYY-MM'))

                                ) as temp GROUP BY temp.month ORDER BY temp.month DESC limit :limit offset :skip
                """;
        if (Objects.nonNull(query.getDepartmentIds()) && !query.getDepartmentIds().isEmpty()) {
            String inClause = query.getDepartmentIds().stream().map(id -> String.format("'%s'", id.toString())).reduce((a, b) -> a + "," + b).orElseThrow();
            sql = sql.replace("$departmentClause", " and ep.workspace_id in (" + inClause + ")");
        } else {
            sql = sql.replace("$departmentClause", "");
        }
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("startDate", query.getFromDate());
        parameters.put("endDate", query.getToDate());
        parameters.put("company", query.getCompany());
        parameters.put("limit", query.getPageable().getPageSize());
        parameters.put("skip", query.getPageable().getOffset());
        return Tuples.of(sql, parameters);
    }

    @Override
    public Flux<HumanResourceChangeReport> getHumanResourceChangeReport(HumanResourceChangeReport.Query query) {
        Tuple2<String, Map<String, Object>> tuple = buildHumanResourceChangeReportQuery(query);
        return db.sql(tuple.getT1())
                .bindValues(tuple.getT2())
                .map((row, metadata) -> {
                    HumanResourceChangeReport report = new HumanResourceChangeReport();
                    report.setTotalJoin(converter.fromRow(row, "total_join", Integer.class));
                    report.setTotalLeave(converter.fromRow(row, "total_leave", Integer.class));
                    report.setMonth(row.get("month", String.class));
                    return report;
                })
                .all();
    }

    @Override
    public Mono<Long> countHumanResourceChangeReport(HumanResourceChangeReport.Query query) {
        Tuple2<String, Map<String, Object>> tuple = buildHumanResourceChangeReportQuery(query);
        var map = tuple.getT2();
        map.put("limit", Integer.MAX_VALUE);
        map.put("skip", 0);
        return db.sql(tuple.getT1())
                .bindValues(tuple.getT2())
                .map((row, metadata) -> 1L)
                .all()
                .count();
    }

    @Override
    public Mono<Void> callInsertEmployeeProfile(EmployeeProfileXlsx employeeProfile) {
        return db.sql("""
                        SELECT insert_employee(
                            :employeeCode,
                            :fullName,
                            :gender,
                            :workspaceName,
                            :citizenId,
                            :citizenIssueDate,
                            :citizenIssuePlace,
                            :residenceAddress,
                            :temporaryAddress,
                            :birthday,
                            :phone,
                            :taxCode,
                            :startWorkDate,
                            :role,
                            :position,
                            :bankCode,
                            :bankNumber,
                            :contractType,
                            :contractTerm,
                            :contractNumber,
                            :contractDate,
                            :contractEndDate,
                            :level,
                            :parkingCard,
                            :insuranceCard,
                            :referrerCode,
                            :referrerDate,
                            :email,
                            :note,
                            :status,
                            :company,
                            :probationDateFrom,
                            :probationDateTo,
                            :officialWorkType,
                            :officialWorkTypeDuration,
                            :insurancePaymentLevel
                        )
                        """)
                .bind("employeeCode", employeeProfile.getEmployeeCode())
                .bind("fullName", employeeProfile.getFullName())
                .bind("gender", employeeProfile.getGender().toString())
                .bind("workspaceName", employeeProfile.getWorkspaceName())
                .bind("citizenId", employeeProfile.getCitizenId())
                .bind("citizenIssueDate", Parameters.in(R2dbcType.DATE, employeeProfile.getCitizenIssueDate()))
                .bind("citizenIssuePlace", Parameters.in(R2dbcType.VARCHAR, employeeProfile.getCitizenIssuePlace()))
                .bind("residenceAddress", Parameters.in(R2dbcType.VARCHAR, employeeProfile.getResidenceAddress()))
                .bind("temporaryAddress", Parameters.in(R2dbcType.VARCHAR, employeeProfile.getTemporaryAddress()))
                .bind("birthday", Parameters.in(R2dbcType.DATE, employeeProfile.getBirthday()))
                .bind("phone", Parameters.in(R2dbcType.VARCHAR, employeeProfile.getPhone()))
                .bind("taxCode", employeeProfile.getTaxCode())
                .bind("startWorkDate", Parameters.in(R2dbcType.DATE, employeeProfile.getStartWorkDate()))
                .bind("role", Parameters.in(R2dbcType.VARCHAR, employeeProfile.getRole()))
                .bind("position", Parameters.in(R2dbcType.VARCHAR, employeeProfile.getPosition()))
                .bind("bankCode", Parameters.in(R2dbcType.VARCHAR, employeeProfile.getBankCode()))
                .bind("bankNumber", Parameters.in(R2dbcType.VARCHAR, employeeProfile.getBankNumber()))
                .bind("contractType", Parameters.in(R2dbcType.VARCHAR, employeeProfile.getContractType()))
                .bind("contractTerm", Parameters.in(R2dbcType.VARCHAR, employeeProfile.getContractTerm()))
                .bind("contractNumber", Parameters.in(R2dbcType.VARCHAR, employeeProfile.getContractNumber()))
                .bind("contractDate", Parameters.in(R2dbcType.DATE, employeeProfile.getContractDate()))
                .bind("contractEndDate", Parameters.in(R2dbcType.DATE, employeeProfile.getContractEndDate()))
                .bind("level", Parameters.in(R2dbcType.VARCHAR, employeeProfile.getLevel()))
                .bind("parkingCard", Parameters.in(R2dbcType.VARCHAR, employeeProfile.getParkingCard()))
                .bind("insuranceCard", Parameters.in(R2dbcType.VARCHAR, employeeProfile.getInsuranceCard()))
                .bind("referrerCode", Parameters.in(R2dbcType.VARCHAR, employeeProfile.getReferrerCode()))
                .bind("referrerDate", Parameters.in(R2dbcType.DATE, employeeProfile.getReferrerDate()))
                .bind("email", Parameters.in(R2dbcType.VARCHAR, employeeProfile.getEmail()))
                .bind("note", Parameters.in(R2dbcType.VARCHAR, employeeProfile.getNote()))
                .bind("status", Parameters.in(R2dbcType.VARCHAR, employeeProfile.getStatus()))
                .bind("company", Parameters.in(R2dbcType.VARCHAR, employeeProfile.getCompany()))
                .bind("probationDateFrom", Parameters.in(R2dbcType.DATE, employeeProfile.getProbationDateFrom()))
                .bind("probationDateTo", Parameters.in(R2dbcType.DATE, employeeProfile.getProbationDateTo()))
                .bind("officialWorkType", Parameters.in(R2dbcType.VARCHAR, employeeProfile.getOfficialWorkType()))
                .bind("officialWorkTypeDuration", Parameters.in(R2dbcType.VARCHAR, employeeProfile.getOfficialWorkTypeDuration()))
                .bind("insurancePaymentLevel", Parameters.in(R2dbcType.VARCHAR, employeeProfile.getInsurancePaymentLevel()))
                .fetch()
                .rowsUpdated()
                .doOnError(throwable -> {
                    throw new RuntimeException(throwable);
                })
                .then();
    }

    private EmployeeProfile process(Row row, RowMetadata metadata) {
        EmployeeProfile entity = employeeprofileMapper.apply(row, "e");
        entity.setWorkspace(workspaceMapper.apply(row, "workspace"));
        entity.setReferrer(employeeRowMapper.apply(row, "employee"));
        if (entity.getWorkspace() == null) {
            entity.setWorkspace(new Workspace());
        }
        return entity;
    }

    @Override
    public <S extends EmployeeProfile> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
