package com.masi.employee.repository;

import com.masi.employee.domain.Uniform;
import com.masi.employee.domain.UniformFormDetail;
import com.masi.employee.domain.UniformRelease;
import com.masi.employee.repository.rowmapper.EmployeeRowMapper;
import com.masi.employee.repository.rowmapper.UniformFormDetailRowMapper;
import com.masi.employee.repository.rowmapper.UniformReleaseRowMapper;
import com.masi.employee.service.dto.UniformReleaseGetListDTO;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;

import java.util.*;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
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
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

/**
 * Spring Data R2DBC custom repository implementation for the UniformRelease
 * entity.
 */
@SuppressWarnings("unused")
class UniformReleaseRepositoryInternalImpl extends SimpleR2dbcRepository<UniformRelease, UUID>
    implements UniformReleaseRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;
    private final R2dbcConverter r2dbcConverter;

    private UniformRepository uniformRepository;

    @Autowired
    public void setUniformRepository(UniformRepository uniformRepository) {
        this.uniformRepository = uniformRepository;
    }

    private final UniformReleaseRowMapper uniformreleaseMapper;
    private final UniformFormDetailRowMapper uniformFormDetailRowMapper;
    private final EmployeeRowMapper employeeRowMapper;

    private static final Table entityTable = Table.aliased("uniform_release", EntityManager.ENTITY_ALIAS);
    private static final Table detailTable = Table.aliased("uniform_form_detail", "d");
    private static final Table employeeTable = Table.aliased("employee", "employee");

    public UniformReleaseRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        UniformReleaseRowMapper uniformreleaseMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter, UniformFormDetailRowMapper uniformFormDetailRowMapper,
        @Lazy UniformRepository uniformRepository, EmployeeRowMapper employeeRowMapper) {
        super(
            new MappingRelationalEntityInformation(
                converter.getMappingContext().getRequiredPersistentEntity(UniformRelease.class)),
            entityOperations,
            converter);
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.uniformreleaseMapper = uniformreleaseMapper;
        this.r2dbcConverter = converter;
        this.uniformFormDetailRowMapper = uniformFormDetailRowMapper;
        this.uniformRepository = uniformRepository;
        this.employeeRowMapper = employeeRowMapper;
    }

    @Override
    public Flux<UniformRelease> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<UniformRelease> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = UniformReleaseSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        SelectFromAndJoin selectFrom = Select.builder().select(columns).from(entityTable);
        // we do not support Criteria here for now as of
        // https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, UniformRelease.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    RowsFetchSpec<UniformRelease> createQuery(Pageable pageable, Condition whereClause,
                                              HashMap<String, Object> params) {
        List<Expression> columns = UniformReleaseSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        SelectFromAndJoin selectFrom = Select.builder().select(columns).from(entityTable);
        // we do not support Criteria here for now as of
        // https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, UniformRelease.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    private Pageable getDefaultSort(Pageable pageable) {
        Sort defaultSort = Sort.by(Sort.Direction.DESC, "create_at");
        if (Objects.isNull(pageable)) {
            pageable = PageRequest.of(0, Integer.MAX_VALUE, defaultSort);
        }
        if (pageable.getSort().equals(Sort.unsorted())) {
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), defaultSort);
        }
        return pageable;
    }

    @Override
    public Flux<UniformRelease> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<UniformRelease> findById(UUID id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"),
            Conditions.just(StringUtils.wrap(id.toString(), "'")));
        return createQuery(null, whereClause).one();
    }

    private UniformRelease process(Row row, RowMetadata metadata) {
        UniformRelease entity = uniformreleaseMapper.apply(row, "e");
        return entity;
    }

    @Override
    public <S extends UniformRelease> Mono<S> save(S entity) {
        return super.save(entity);
    }

    private Tuple2<String, Map<String, Object>> CreateSQLQuery(Pageable pageable, UniformReleaseGetListDTO uniformReleaseGetListDTO,
                                                               List<Uniform> listUniform) {


        var sqlString = "SELECT " + UniformFormDetailSqlHelper.getColumns(detailTable, "d") + ", "
            + UniformReleaseSqlHelper.getColumns(entityTable, "e") + ", "
            + EmployeeSqlHelper.getColumns(employeeTable, "employee")
            + " FROM uniform_release e LEFT JOIN uniform_form_detail d on d.uniform_release_id = e.id"
            + " LEFT JOIN employee employee on employee.id = e.employee_id AND employee.is_active = true"
            + " LEFT JOIN uniform un on un.id = d.uniform_id"
            + " WHERE "
            + " e.delete_at IS NULL AND e.delete_by IS NULL AND d.delete_at IS NULL AND d.delete_by IS NULL AND e.company = :company AND d.company = :company";

        if (listUniform != null && !listUniform.isEmpty()) {
            String uniformIds = listUniform.stream()
                .map(uniform -> "'" + uniform.getId().toString() + "'")
                .collect(Collectors.joining(", "));
            sqlString += " AND d.uniform_id IN (" + uniformIds + ")";
        }

        if (uniformReleaseGetListDTO.getType() != null) {
            String status = uniformReleaseGetListDTO.getType().stream()
                .map(s -> "'" + s.toString() + "'")
                .collect(Collectors.joining(", "));
            sqlString += " AND e.type IN (" + status + ")";
        }

        var params = new HashMap<String, Object>();
        params.put("company", uniformReleaseGetListDTO.getCompany());
        if (uniformReleaseGetListDTO.getStartDate() != null) {
            sqlString += " AND DATE(e.date) >= DATE(:startDate)";
            params.put("startDate", uniformReleaseGetListDTO.getStartDate());
        }

        if (uniformReleaseGetListDTO.getEndDate() != null) {
            sqlString += " AND DATE(e.date) <= DATE(:endDate)";

            params.put("endDate", uniformReleaseGetListDTO.getEndDate());
        }
        if (uniformReleaseGetListDTO.getEmployeeIds() != null && !uniformReleaseGetListDTO.getEmployeeIds().isEmpty()) {
            String employeeIds = uniformReleaseGetListDTO.getEmployeeIds().stream()
                .map(employeeId -> "'" + employeeId.toString() + "'")
                .collect(Collectors.joining(", "));
            sqlString += " AND e.employee_id IN (" + employeeIds + ")";
        }

        if (StringUtils.isNotBlank(uniformReleaseGetListDTO.getSearch()) && uniformReleaseGetListDTO.getSearch() != null) {
            var search = sanitizeInput(uniformReleaseGetListDTO.getSearch());
            sqlString += " ";
            String sb = "AND (" +
                "  e.code LIKE '%' || unaccent(:search) || '%' " +
                "  OR unaccent(CONCAT(employee.last_name, ' ', employee.first_name)) ILIKE '%' || unaccent(:search) || '%' " +
                "  OR unaccent(un.name) ILIKE '%' || unaccent(:search) || '%'" +
                ") ";
            sqlString += sb;
            sqlString += " ORDER BY e.create_at DESC";
            params.put("search", search);
        }
        sqlString = sqlString.replace("[", "").replace("]", "");
        return Tuples.of(sqlString, params);
    }

    @Override
    public Flux<UniformRelease> findAllWithQuery(Pageable pageable, UniformReleaseGetListDTO uniformReleaseGetListDTO,
                                                 List<Uniform> listUniform) {
        var params = CreateSQLQuery(pageable, uniformReleaseGetListDTO, listUniform);
        long page = pageable.getPageNumber();
        long size = pageable.getPageSize();
//        String property = pageable.getSort().stream().map(Sort.Order::getProperty).findFirst().orElse("createAt");
//        String direction = String.valueOf(
//            pageable.getSort().stream().map(Sort.Order::getDirection).findFirst().orElse(Sort.Direction.DESC));
        return db
            .sql(params.getT1())
            .bindValues(params.getT2())
            .map((row, metadata) -> Tuples.of(uniformreleaseMapper.apply(row, "e"),
                uniformFormDetailRowMapper.apply(row, "d"), employeeRowMapper.apply(row, "employee")))
            .all()
            .groupBy(t -> t.getT1().getId())
            .flatMap(l -> l.collectList().flatMap(ll -> {
                return uniformRepository
                    .getUniformByCompanyAndDeleteAtIsNull(uniformReleaseGetListDTO.getCompany()).collectList()
                    .flatMap(uniforms -> {
                        UniformRelease uniformRelease = ll.get(0).getT1();
                        uniformRelease.setEmployee(ll.get(0).getT3());
                        var listDetails = ll.stream().map(detail -> {
                            UniformFormDetail uniformFormDetail = detail.getT2();
                            if (listUniform != null) {
                                var uniform = listUniform.stream()
                                    .filter(u -> u.getId() != null && u.getId().equals(uniformFormDetail.getUniformId()))
                                    .findFirst()
                                    .orElse(null);
                                uniformFormDetail.setUniform(uniform);
                                return uniformFormDetail;

                            } else {
                                var uniform = uniforms.stream()
                                    .filter(u -> u.getId().equals(uniformFormDetail.getUniformId()))
                                    .findFirst()
                                    .orElse(null);

                                uniformFormDetail.setUniform(uniform);
                                return uniformFormDetail;
                            }
                        }).toList();
                        uniformRelease.setUniformFormDetails(new HashSet<>(listDetails));
                        return Mono.just(uniformRelease);
                    });
            }))
            .sort((o1, o2) -> o2.getCreateAt().compareTo(o1.getCreateAt()))
            .skip(page * size)
            .take(size);
    }

    private String sanitizeInput(String input) {
        // Loại bỏ tất cả các ký tự không phải là chữ cái, số, hoặc khoảng trắng
        return input.replaceAll("[^\\p{L}\\p{N}\\s]", "").trim();
    }

    @Override
    public Mono<Long> countAllByQuery(UniformReleaseGetListDTO uniformReleaseGetListDTO,
                                      List<Uniform> listUniform) {

        var params = CreateSQLQuery(null, uniformReleaseGetListDTO, listUniform);

        return db
            .sql(params.getT1())
            .bindValues(params.getT2())
            .map((row, metadata) -> Tuples.of(uniformreleaseMapper.apply(row, "e"),
                uniformFormDetailRowMapper.apply(row, "d")))
            .all().groupBy(Tuple2::getT1).count();
    }
}
