package com.masi.employee.repository;

import com.carevn.masi.constants.AuthoritiesConstants;
import com.masi.employee.domain.Uniform;
import com.masi.employee.domain.UniformFormDetail;
import com.masi.employee.domain.UniformOrder;
import com.masi.employee.repository.rowmapper.ColumnConverter;
import com.masi.employee.repository.rowmapper.UniformOrderRowMapper;
import com.masi.employee.repository.rowmapper.UniformRowMapper;
import com.masi.employee.service.dto.InterviewScheduleQuery;
import com.masi.employee.service.dto.UniformOrderGetListDTO;
import com.masi.employee.service.dto.UniformOrderGetListFilterDTO;
import com.masi.employee.service.dto.UniformOrderGetListFilterDTO.UniformOrderStatusFilter;

import io.r2dbc.spi.ColumnMetadata;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
import org.springframework.data.relational.core.sql.SelectBuilder.SelectFromAndJoin;
import org.springframework.data.relational.core.sql.SelectBuilder.SelectFromAndJoinCondition;
import org.springframework.data.relational.core.sql.Table;
import org.springframework.data.relational.repository.support.MappingRelationalEntityInformation;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.r2dbc.core.RowsFetchSpec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;
import tech.jhipster.service.ConditionBuilder;

/**
 * Spring Data R2DBC custom repository implementation for the UniformOrder
 * entity.
 */
@SuppressWarnings("unused")
class UniformOrderRepositoryInternalImpl extends SimpleR2dbcRepository<UniformOrder, UUID>
        implements UniformOrderRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final UniformOrderRowMapper uniformorderMapper;
    private final ColumnConverter columnConverter;

    private static final Table entityTable = Table.aliased("uniform_order", EntityManager.ENTITY_ALIAS);
    private static final Table uniformOrderFormDetailTable = Table.aliased("uniform_form_detail", "d");
    private static final Table uniformTable = Table.aliased("uniform", "u");

    public UniformOrderRepositoryInternalImpl(
            R2dbcEntityTemplate template,
            EntityManager entityManager,
            UniformRowMapper uniformMapper,
            UniformOrderRowMapper uniformorderMapper,
            R2dbcEntityOperations entityOperations,
            R2dbcConverter converter, ColumnConverter columnConverter) {
        super(
                new MappingRelationalEntityInformation(
                        converter.getMappingContext().getRequiredPersistentEntity(UniformOrder.class)),
                entityOperations,
                converter);
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.uniformorderMapper = uniformorderMapper;
        this.columnConverter = columnConverter;
    }

    @Override
    public Flux<UniformOrder> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<UniformOrder> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = UniformOrderSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        SelectFromAndJoin selectFrom = Select.builder().select(columns).from(entityTable);
        // we do not support Criteria here for now as of
        // https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, UniformOrder.class, pageable, whereClause);

        return db.sql(select).map(this::process);
    }

    RowsFetchSpec<UniformOrder> createQueryCustom(Pageable pageable, Condition whereClause,
            Map<String, Object> params) {
        List<Expression> columns = UniformOrderSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        SelectFromAndJoin selectFrom = Select.builder().select(columns).from(entityTable);
        // we do not support Criteria here for now as of
        // https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, UniformOrder.class, pageable, whereClause);
        var query = db.sql(select);
        if (params != null && !params.isEmpty()) {
            query = query.bindValues(params);
        }
        return query.map(this::process);
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
    public Flux<UniformOrder> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<UniformOrder> findById(UUID id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"),
                Conditions.just(StringUtils.wrap(id.toString(), "'")));
        return createQuery(null, whereClause).one();
    }

    private UniformOrder process(Row row, RowMetadata metadata) {
        UniformOrder entity = uniformorderMapper.apply(row, "e");
        return entity;
    }

    @Override
    public <S extends UniformOrder> Mono<S> save(S entity) {
        return super.save(entity);
    }

    private Tuple2<Condition, Map<String, Object>> buildConditionFromRO(UniformOrderGetListDTO ro) {
        Condition condition = Conditions.isNull(entityTable.column("delete_at"));
        condition = condition.and(Conditions.isNull(entityTable.column("delete_by")));
        Map<String, Object> params = new HashMap<>();
        if (StringUtils.isNotBlank(ro.getName())) {
            var columnNameAlias = EntityManager.ENTITY_ALIAS + ".name";
            var columnCodeAlias = EntityManager.ENTITY_ALIAS + ".code";
            String likeClause = String.format("(unaccent(%s) ILIKE unaccent(:search) OR unaccent(%s) ILIKE unaccent(:search))", columnNameAlias, columnCodeAlias);
            condition = condition.and(Conditions.just(likeClause));
            params.put("search", "%" + ro.getName() + "%");
        }
        if (ro.getStatus() != null && !ro.getStatus().isEmpty()) {
            String inClause = ro.getStatus().stream().map(e -> "'" + e + "'")
                    .reduce((a, b) -> a + "," + b).orElseThrow();
            condition = condition.and(Conditions.in(entityTable.column("status"), Conditions.just(inClause)));
        }

        if (ro.getCompany() != null) {
            condition = condition.and(Conditions.isEqual(entityTable.column("company"),
                    Conditions.just("'" + ro.getCompany() + "'")));
        }

        if (ro.getStartDate() != null) {
            condition = condition
                    .and(Conditions.just("CAST(e.DATE as DATE) >= CAST (:startDate as DATE)"));
            params.put("startDate", ro.getStartDate());
        }

        if (ro.getEndDate() != null) {
            condition = condition
                    .and(Conditions.just("CAST(e.DATE as DATE) <= CAST (:endDate as DATE)"));
            params.put("endDate", ro.getEndDate());
        }

        return Tuples.of(condition, params);
    }

    @Override
    public Flux<UniformOrder> findAllByQuery(Pageable pageable, UniformOrderGetListDTO uniformOrderGetListDTO) {
        // TODO Auto-generated method stub
        pageable = getDefaultSort(pageable);
        var conditionParams = buildConditionFromRO(uniformOrderGetListDTO);

        return createQueryCustom(pageable, conditionParams.getT1(), conditionParams.getT2()).all();
    }

    @Override
    public Mono<Long> countAllByQuery(UniformOrderGetListDTO uniformOrderGetListDTO) {
        // TODO Auto-generated method stub
        var conditionParams = buildConditionFromRO(uniformOrderGetListDTO);
        return createQueryCustom(null, conditionParams.getT1(), conditionParams.getT2()).all().count();
    }



}
