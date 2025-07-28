package com.masi.employee.repository;

import com.masi.employee.domain.Workspace;
import com.masi.employee.repository.rowmapper.WorkspaceRowMapper;
import com.masi.employee.service.dto.WorkspaceRO;
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
 * Spring Data R2DBC custom repository implementation for the Workspace entity.
 */
@SuppressWarnings("unused")
class WorkspaceRepositoryInternalImpl extends SimpleR2dbcRepository<Workspace, UUID> implements WorkspaceRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final WorkspaceRowMapper workspaceMapper;

    private static final Table entityTable = Table.aliased("workspace", EntityManager.ENTITY_ALIAS);

    public WorkspaceRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        WorkspaceRowMapper workspaceMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(Workspace.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.workspaceMapper = workspaceMapper;
    }

    @Override
    public Flux<Workspace> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<Workspace> createQuery(Pageable pageable, Condition whereClause, Map<String, Object> params) {
        List<Expression> columns = WorkspaceSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        SelectFromAndJoin selectFrom = Select.builder().select(columns).from(entityTable);
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, Workspace.class, pageable, whereClause);
        DatabaseClient.GenericExecuteSpec spec = db.sql(select);
        if (params != null && !params.isEmpty()) {
            spec = spec.bindValues(params);
        }
        return spec.map(this::process);
    }

    RowsFetchSpec<Workspace> createQuery(Pageable pageable, Condition whereClause) {
        return createQuery(pageable, whereClause, null);
    }


    @Override
    public Flux<Workspace> findAll() {
        return findAllBy(null);
    }

    @Override
    public Flux<Workspace> findAllActiveBy(Pageable pageable, WorkspaceRO ro) {
        pageable = getDefaultSortIfUnsorted(pageable);
        Tuple2<Condition, Map<String, Object>> condition = buildConditionFromRO(ro);
        return createQuery(pageable, condition.getT1(), condition.getT2()).all();
    }

    @Override
    public Mono<Long> countAllActiveBy(WorkspaceRO ro) {
        Tuple2<Condition, Map<String, Object>> condition = buildConditionFromRO(ro);
        return createQuery(null, condition.getT1(), condition.getT2()).all().count();
    }

    private Workspace process(Row row, RowMetadata metadata) {
        Workspace entity = workspaceMapper.apply(row, "e");
        return entity;
    }

    private Pageable getDefaultSortIfUnsorted(Pageable pageable) {
        Sort defaultSort = Sort.by(Sort.Direction.DESC, "created_at");
        if (Objects.isNull(pageable)) {
            pageable = PageRequest.of(0, Integer.MAX_VALUE, defaultSort);
        }

        if (pageable.getSort().equals(Sort.unsorted())) {
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), defaultSort);
        }
        return pageable;
    }

    @Override
    public <S extends Workspace> Mono<S> save(S entity) {
        return super.save(entity);
    }

    private String wrap(String value) {
        return StringUtils.wrap(value, "'");
    }

    private Tuple2<Condition, Map<String, Object>> buildConditionFromRO(WorkspaceRO ro) {
        Condition whereClause = Conditions.isEqual(entityTable.column("is_active"), Conditions.just(wrap(((Boolean) true).toString())));
        Map<String, Object> params = new HashMap<>();
        if (StringUtils.isNotBlank(ro.getSearchString())) {
            String columnAlias = EntityManager.ENTITY_ALIAS + "." + "name";
            String likeCondition1 = String.format("unaccent(%s) iLIKE unaccent(:searchString)", columnAlias);
            columnAlias = EntityManager.ENTITY_ALIAS + "." + "description";
            String likeCondition2 = String.format("unaccent(%s) iLIKE unaccent(:searchString)", columnAlias);
            String likeCondition = String.format("(%s OR %s)", likeCondition1, likeCondition2);

            whereClause = whereClause.and(Conditions.just(likeCondition));
            params.put("searchString", "%" + ro.getSearchString().trim() + "%");

        }
        if (StringUtils.isNotBlank(ro.getCompany())) {
            whereClause = whereClause.and(Conditions.isEqual(entityTable.column("company"), Conditions.just(wrap(ro.getCompany()))));
        }
        return Tuples.of(whereClause, params);
    }
}
