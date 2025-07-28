package com.carevn.masi.repository;

import com.carevn.masi.domain.Company;
import com.carevn.masi.repository.rowmapper.CompanyRowMapper;
import com.carevn.masi.service.dto.CompanyQuery;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
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
 * Spring Data R2DBC custom repository implementation for the Company entity.
 */
@SuppressWarnings("unused")
class CompanyRepositoryInternalImpl extends SimpleR2dbcRepository<Company, UUID> implements CompanyRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final CompanyRowMapper companyMapper;

    private static final Table entityTable = Table.aliased("company", EntityManager.ENTITY_ALIAS);

    public CompanyRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        CompanyRowMapper companyMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(Company.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.companyMapper = companyMapper;
    }

    @Override
    public Flux<Company> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<Company> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = CompanySqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        SelectFromAndJoin selectFrom = Select.builder().select(columns).from(entityTable);
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, Company.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<Company> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<Company> findById(UUID id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(StringUtils.wrap(id.toString(), "'")));
        return createQuery(null, whereClause).one();
    }

    @Override
    public Flux<Company> findAllByQuery(Pageable pageable, CompanyQuery query) {
        Condition whereClause = buildCondition(query);
        return createQuery(pageable, whereClause).all();
    }

    @Override
    public Mono<Long> countByQuery(CompanyQuery query) {
        return createQuery(null, buildCondition(query)).all().count();
    }

    private static Condition buildCondition(CompanyQuery query) {
        Condition whereClause = Conditions.isEqual(entityTable.column("is_deleted"), Conditions.just("false"));
        if (query.getParentId() != null) {
            whereClause = whereClause.and(Conditions.isEqual(entityTable.column("parent_id"), Conditions.just(StringUtils.wrap(query.getParentId().toString(), "'"))));
        }
        if (query.getSearch() != null) {
            var columnSearch = new String[]{"name", "code", "description", "normalized_name"};

            List<Condition> conditions = new ArrayList<>();
            for (String column : columnSearch) {
                Condition itemCondition = Conditions.like(entityTable.column(column), Conditions.just(StringUtils.wrap("%" + query.getSearch() + "%" , "'")));
                conditions.add(itemCondition);
            }
            Condition orCondition = conditions.stream().reduce(Condition::or).orElse(Conditions.just("1=1"));
            whereClause = whereClause.and(Conditions.nest(orCondition));
        }
        if (query.getIsParentIdSpecified() != null) {
            if (query.getIsParentIdSpecified()) {
                whereClause = whereClause.and(entityTable.column("parent_id").isNotNull());
            }
            else {
                whereClause = whereClause.and(Conditions.isNull(entityTable.column("parent_id")));
            }
        }
        return whereClause;
    }

    private Company process(Row row, RowMetadata metadata) {
        Company entity = companyMapper.apply(row, "e");
        return entity;
    }

    @Override
    public <S extends Company> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
