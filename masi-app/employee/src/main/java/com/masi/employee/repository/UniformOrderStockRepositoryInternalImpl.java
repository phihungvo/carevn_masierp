package com.masi.employee.repository;

import com.masi.employee.domain.Uniform;
import com.masi.employee.domain.UniformFormDetail;
import com.masi.employee.domain.UniformOrder;
import com.masi.employee.domain.UniformOrderStock;
import com.masi.employee.repository.rowmapper.EmployeeRowMapper;
import com.masi.employee.repository.rowmapper.UniformFormDetailRowMapper;
import com.masi.employee.repository.rowmapper.UniformOrderRowMapper;
import com.masi.employee.repository.rowmapper.UniformOrderStockRowMapper;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;

import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

/**
 * Spring Data R2DBC custom repository implementation for the UniformOrderStock entity.
 */
@SuppressWarnings("unused")
class UniformOrderStockRepositoryInternalImpl
    extends SimpleR2dbcRepository<UniformOrderStock, UUID>
    implements UniformOrderStockRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final UniformOrderStockRowMapper uniformorderstockMapper;
    private final UniformFormDetailRowMapper uniformformdetailMapper;
    private final UniformOrderRowMapper uniformOrderMapper;
    private final EmployeeRowMapper employeeRowMapper;

    private static final Table entityTable = Table.aliased("uniform_order_stock", EntityManager.ENTITY_ALIAS);
    private static final Table uniformOrderTable = Table.aliased("uniform_order", "uniformOrder");
    private static final Table detailTable = Table.aliased("uniform_form_detail", "d");
    private static final Table employeeTable = Table.aliased("employee", "employee");

    public UniformOrderStockRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        UniformOrderStockRowMapper uniformorderstockMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter, UniformFormDetailRowMapper uniformformdetailMapper, UniformOrderRowMapper uniformOrderMapper, EmployeeRowMapper employeeRowMapper
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(UniformOrderStock.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.uniformorderstockMapper = uniformorderstockMapper;
        this.uniformformdetailMapper = uniformformdetailMapper;
        this.uniformOrderMapper = uniformOrderMapper;
        this.employeeRowMapper = employeeRowMapper;
    }

    @Override
    public Flux<UniformOrderStock> findAllBy(Pageable pageable, ZonedDateTime startDate, ZonedDateTime endDate, String company) {
        pageable = getDefaultSort(pageable);
        Condition whereClause = Conditions.isEqual(entityTable.column("company"), Conditions.just(StringUtils.wrap(company, "'")));
        whereClause = whereClause.and(Conditions.isNull(entityTable.column("delete_at")));
        if (startDate != null) {
            whereClause = whereClause.and(Conditions.isGreaterOrEqualTo(entityTable.column("create_at"), Conditions.just(startDate.toString())));
        }
        if (endDate != null) {
            whereClause = whereClause.and(Conditions.isLessOrEqualTo(entityTable.column("create_at"), Conditions.just(endDate.toString())));
        }

        return createQuery(pageable, whereClause).all();
    }

    @Override
    public Flux<UniformOrderStock> findAllBy(Pageable pageable){
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<UniformOrderStock> createQuery(Pageable pageable, Condition whereClause) {
        try {
            List<Expression> columns = UniformOrderStockSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
            columns.addAll(UniformOrderSqlHelper.getColumns(uniformOrderTable, "uniformOrder"));
            SelectBuilder.SelectFromAndJoinCondition selectFrom = Select.builder()
                .select(columns)
                .from(entityTable)
                .leftOuterJoin(uniformOrderTable)
                .on(Column.create("uniform_order_id", entityTable))
                .equals(Column.create("id", uniformOrderTable));
            // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
            String select = entityManager.createSelect(selectFrom, UniformOrderStock.class, pageable, whereClause);
            var sql = db.sql(select);
            return sql.map((row, metadata) -> {
                System.out.println("output: " + row);
                System.out.println("metadata: " + metadata);
                return process(row, metadata);
            });
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
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
    public Flux<UniformOrderStock> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<UniformOrderStock> findById(UUID id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(StringUtils.wrap(id.toString(), "'")));
        return createQuery(null, whereClause).one();
    }


    private UniformOrderStock process(Row row, RowMetadata metadata) {
        try {
            UniformOrderStock entity = uniformorderstockMapper.apply(row, "e");
            entity.setUniformOrder(uniformOrderMapper.apply(row, "uniformOrder"));
            return entity;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <S extends UniformOrderStock> Mono<S> save(S entity) {
        return super.save(entity);
    }

    @Override
    public Flux<UniformOrderStock> createQueryCondition(UUID uniformOrderId, String company, List<Uniform> uniforms) {
        var sql = "SELECT "+  UniformFormDetailSqlHelper.getColumns(detailTable, "d")
            + ", "
            + UniformOrderStockSqlHelper.getColumns(entityTable, "e")
            + " FROM uniform_order_stock e "
            + " LEFT JOIN uniform_form_detail d ON d.uniform_order_stock_id = e.id "
            + " WHERE e.company = :company AND e.delete_at IS NULL AND e.delete_by IS NULL";

        if (uniformOrderId != null) {
            sql += " AND e.uniform_order_id = :uniformOrderId";
        }

        sql += " ORDER BY e.create_at DESC";

        sql = sql.replace("[", "").replace("]", "");

        var param = new HashMap<String, Object>();
        param.put("uniformOrderId", uniformOrderId);
        param.put("company", company);
        return db.sql(sql)
            .bindValues(param)
            .map((row, metadata) -> Tuples.of(
                uniformorderstockMapper.apply(row, "e"),
                uniformformdetailMapper.apply(row, "d")
            ))
            .all()
            .groupBy(Tuple2::getT1)
            .flatMap(group -> group.collectList().flatMap(list -> {
                UniformOrderStock entity = list.get(0).getT1();
                var listDetail = list.stream().map(Tuple2::getT2).collect(Collectors.toSet());
                listDetail.forEach(det -> {
                    var u = uniforms.stream().filter(uniform -> {
                        if (uniform.getId() != null) {
                            return uniform.getId().equals(det.getUniformId());
                        }
                        return false;
                    }).findFirst().orElse(null);
                    det.setUniform(u);
                });
                entity.setUniformFormDetail(new HashSet<>(listDetail));
                return Mono.just(entity);
            }));
    }
}
