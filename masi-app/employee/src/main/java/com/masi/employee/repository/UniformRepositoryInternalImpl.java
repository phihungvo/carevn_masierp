package com.masi.employee.repository;

import com.masi.employee.domain.RecruitmentRequest;
import com.masi.employee.domain.Uniform;
import com.masi.employee.repository.rowmapper.UniformRowMapper;
import com.masi.employee.service.dto.RecruitmentRequestRO;
import com.masi.employee.service.dto.UniformQuery;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;

import java.util.*;

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
 * Spring Data R2DBC custom repository implementation for the Uniform entity.
 */
@SuppressWarnings("unused")
class UniformRepositoryInternalImpl extends SimpleR2dbcRepository<Uniform, UUID> implements UniformRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final UniformRowMapper uniformMapper;

    private static final Table entityTable = Table.aliased("uniform", EntityManager.ENTITY_ALIAS);

    public UniformRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        UniformRowMapper uniformMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(Uniform.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.uniformMapper = uniformMapper;
    }

    @Override
    public Mono<Long> countByFilter(UniformQuery ro) {
        Tuple2<Condition, Map<String, Object>> whereTuple = buildWhereClause(ro);
        return createQuery(null, whereTuple.getT1(), whereTuple.getT2()).all().count();
    }


    @Override
    public Flux<Uniform> findAllByFilter( Pageable pageable, UniformQuery moRO) {
        pageable = getDefaultSortIfUnsorted(pageable);
        Tuple2<Condition, Map<String, Object>> whereTuple = buildWhereClause(moRO);
        return createQuery(pageable, whereTuple.getT1(), whereTuple.getT2()).all();
    }

    private Tuple2<Condition, Map<String, Object>> buildWhereClause(UniformQuery ro) {
        Condition whereClause = Conditions.just("1= 1");
        Map<String, Object> params = new HashMap<>();
        if (Objects.nonNull(ro)) {
            if (StringUtils.isNotBlank(ro.getSearch())) {
                String column = EntityManager.ENTITY_ALIAS + ".contract_name";
                String inCondition = "unaccent(" + column + ") iLIKE unaccent(:searchString)";
                whereClause = whereClause.and(Conditions.just(inCondition));
                params.put("searchString", "%" + ro.getSearch() + "%");
            }
        }
        if (StringUtils.isNotBlank(ro.getCompany())) {
            whereClause = whereClause.and(Conditions.isEqual(entityTable.column("company"), Conditions.just(StringUtils.wrap(ro.getCompany(), "'"))));
        }
        if (StringUtils.isNotBlank(ro.getStatus()))
        {
            whereClause = whereClause.and(Conditions.isEqual(entityTable.column("status"), Conditions.just(StringUtils.wrap(ro.getStatus(), "'"))));
        }
        if (Objects.nonNull(ro.getStartDate()))
        {
            whereClause = whereClause.and(Conditions.isGreaterOrEqualTo(entityTable.column("create_at"), Conditions.just(ro.getStartDate().toString())));
        }
        if (Objects.nonNull(ro.getEndDate()))
        {
            whereClause = whereClause.and(Conditions.isLessOrEqualTo(entityTable.column("create_at"), Conditions.just(ro.getEndDate().toString())));
        }
        return Tuples.of(whereClause, params);
    }


    private Pageable getDefaultSortIfUnsorted(Pageable pageable) {
        Sort defaultSort = Sort.by(Sort.Direction.DESC, "create_by");
        if (Objects.isNull(pageable)) {
            pageable = PageRequest.of(0, Integer.MAX_VALUE, defaultSort);
        }
        if (pageable.getSort().equals(Sort.unsorted())) {
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), defaultSort);
        }
        return pageable;
    }


    RowsFetchSpec<Uniform> createQuery(Pageable pageable, Condition whereClause) {
        return this.createQuery(pageable, whereClause, null);
    }

    RowsFetchSpec<Uniform> createQuery(Pageable pageable, Condition whereClause, Map<String, Object> params) {
        List<Expression> columns = UniformSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        SelectFromAndJoin selectFrom = Select.builder()
                .select(columns)
                .from(entityTable);
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, Uniform.class, pageable, whereClause);
        DatabaseClient.GenericExecuteSpec query = db.sql(select);
        // bind params
        if (params != null && !params.isEmpty()) {
            query = query.bindValues(params);
        }
        return query.map(this::process);
    }


    @Override
    public Mono<Uniform> findById(UUID id) {
        Condition whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(StringUtils.wrap(id.toString(), "'")));
        whereClause = whereClause.and(Conditions.isNull(entityTable.column("delete_at")));

        return createQuery(null, whereClause).one();
    }

    private Uniform process(Row row, RowMetadata metadata) {
        Uniform entity = uniformMapper.apply(row, "e");
        return entity;
    }

    @Override
    public <S extends Uniform> Mono<S> save(S entity) {
        return super.save(entity);
    }


}
