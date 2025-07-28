package com.masi.production.repository;

import com.masi.production.domain.WorkCenter;
import com.masi.production.domain.enumeration.WorkCenterStatusEnum;
import com.masi.production.repository.rowmapper.WorkCenterRowMapper;
import com.masi.production.service.dto.QuanlityCheckSampleRO;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import java.util.List;
import java.util.Objects;
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
 * Spring Data R2DBC custom repository implementation for the WorkCenter entity.
 */
@SuppressWarnings("unused")
class WorkCenterRepositoryInternalImpl extends SimpleR2dbcRepository<WorkCenter, UUID>
        implements WorkCenterRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final WorkCenterRowMapper workcenterMapper;

    private static final Table entityTable = Table.aliased("work_center", EntityManager.ENTITY_ALIAS);

    public WorkCenterRepositoryInternalImpl(
            R2dbcEntityTemplate template,
            EntityManager entityManager,
            WorkCenterRowMapper workcenterMapper,
            R2dbcEntityOperations entityOperations,
            R2dbcConverter converter) {
        super(
                new MappingRelationalEntityInformation(
                        converter.getMappingContext().getRequiredPersistentEntity(WorkCenter.class)),
                entityOperations,
                converter);
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.workcenterMapper = workcenterMapper;
    }

    @Override
    public Flux<WorkCenter> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<WorkCenter> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = WorkCenterSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        SelectFromAndJoin selectFrom = Select.builder().select(columns).from(entityTable);
        // we do not support Criteria here for now as of
        // https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, WorkCenter.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<WorkCenter> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<WorkCenter> findById(UUID id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"),
                Conditions.just(StringUtils.wrap(id.toString(), "'")));
        return createQuery(null, whereClause).one();
    }

    private WorkCenter process(Row row, RowMetadata metadata) {
        WorkCenter entity = workcenterMapper.apply(row, "e");
        return entity;
    }

    @Override
    public <S extends WorkCenter> Mono<S> save(S entity) {
        return super.save(entity);
    }

    private Condition buildConditionFromRequestObject(String search, List<WorkCenterStatusEnum> status,
            UUID companyId,String company,String department) {
        Condition whereClause = Conditions.isEqual(entityTable.column("is_deleted"), Conditions.just("false"));
        if (StringUtils.isNotBlank(search)) {
            String columnNameAlias = EntityManager.ENTITY_ALIAS + "." + "name";
            String inCondition = String.format("unaccent(%s) iLIKE unaccent('%%%s%%')", columnNameAlias,
                    search.trim().replace("'", "''"));
            whereClause = whereClause.and(Conditions.just(inCondition));
        }

        if (Objects.nonNull(status) && !status.isEmpty()) {
            var inClause = status.stream().map(s -> "'" + s.toString() + "'").reduce((a, b) -> a + "," + b)
                    .orElseThrow();
            whereClause = whereClause.and(
                    Conditions.in(entityTable.column("status"), Conditions.just(inClause)));
        }

        if (Objects.nonNull(companyId)) {
            whereClause = whereClause
                    .and(Conditions.isEqual(entityTable.column("company_id"), Conditions.just("'" + companyId + "'")));
        }
        if (Objects.nonNull(company)) {
            whereClause = whereClause.and(Conditions.isEqual(entityTable.column("company"), Conditions.just(StringUtils.wrap(company, "'"))));
        }
//            if (Objects.nonNull(ro.getDepartment())) {
//                whereClause = whereClause.and(Conditions.isEqual(entityTable.column("department"), Conditions.just(StringUtils.wrap(RO.getDepartment(), "'"))));
//            }

        return whereClause;
    }

    @Override
    public Flux<WorkCenter> GetAllWithFilter(Pageable pageable, String search, List<WorkCenterStatusEnum> status,
            UUID companyId,String company,String department) {
        var condition = buildConditionFromRequestObject(search, status, companyId,company,department);
        return createQuery(pageable, condition).all();
    }

    @Override
    public Mono<Long> CountWithFilter(String search, List<WorkCenterStatusEnum> status, UUID companyId,String company,String department) {
        var condition = buildConditionFromRequestObject(search, status, companyId,company,department);
        return createQuery(null, condition).all().count().doOnError(throwable -> new RuntimeException(throwable));
    }
}
