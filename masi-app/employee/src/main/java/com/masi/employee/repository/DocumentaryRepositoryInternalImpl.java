package com.masi.employee.repository;

import com.masi.employee.domain.Documentary;
import com.masi.employee.domain.RecruitmentRequest;
import com.masi.employee.repository.rowmapper.DocumentaryRowMapper;
import com.masi.employee.service.dto.DocumentaryRO;
import com.masi.employee.service.dto.RecruitmentRequestRO;
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
 * Spring Data R2DBC custom repository implementation for the Documentary entity.
 */
@SuppressWarnings("unused")
class DocumentaryRepositoryInternalImpl extends SimpleR2dbcRepository<Documentary, UUID> implements DocumentaryRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final DocumentaryRowMapper documentaryMapper;

    private static final Table entityTable = Table.aliased("documentary", EntityManager.ENTITY_ALIAS);

    public DocumentaryRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        DocumentaryRowMapper documentaryMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(Documentary.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.documentaryMapper = documentaryMapper;
    }

    @Override
    public Flux<Documentary> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<Documentary> createQuery(Pageable pageable, Condition whereClause) {
        return this.createQuery(pageable, whereClause, null);
    }

    RowsFetchSpec<Documentary> createQuery(Pageable pageable, Condition whereClause, Map<String, Object> params) {
        List<Expression> columns = DocumentarySqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        SelectFromAndJoin selectFrom = Select.builder().select(columns).from(entityTable);
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, Documentary.class, pageable, whereClause);
        DatabaseClient.GenericExecuteSpec query = db.sql(select);
        if (params != null && !params.isEmpty()) {
            query = query.bindValues(params);
        }
        return query.map(this::process);
    }


    @Override
    public Flux<Documentary> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<Documentary> findById(UUID id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(StringUtils.wrap(id.toString(), "'")));
        return createQuery(null, whereClause).one();
    }

    @Override
    public Mono<Long> countByFilter(DocumentaryRO ro) {
        Tuple2<Condition, Map<String, Object>> whereTuple = buildWhereClause(ro);
        return createQuery(null, whereTuple.getT1(), whereTuple.getT2())
            .all().count();
    }

    @Override
    public Flux<Documentary> findAllByFilter(DocumentaryRO ro, Pageable pageable) {
        Sort defaultSort = Sort.by(Sort.Direction.DESC, "created_at");
        if (Objects.isNull(pageable) || pageable.getSort().equals(Sort.unsorted())) {
            int pageNumber = pageable != null ? pageable.getPageNumber() : 0;
            int pageSize = pageable != null ? pageable.getPageSize() : Integer.MAX_VALUE;
            pageable = PageRequest.of(pageNumber, pageSize, defaultSort);
        }
        Tuple2<Condition, Map<String, Object>> whereTuple = buildWhereClause(ro);
        return createQuery(pageable, whereTuple.getT1(), whereTuple.getT2()).all();
    }

    private Documentary process(Row row, RowMetadata metadata) {
        Documentary entity = documentaryMapper.apply(row, "e");
        return entity;
    }

    @Override
    public <S extends Documentary> Mono<S> save(S entity) {
        return super.save(entity);
    }


    private Tuple2<Condition, Map<String, Object>> buildWhereClause(DocumentaryRO RO) {
        Condition whereClause = Conditions.isEqual(entityTable.column("is_deleted"), Conditions.just("false"));
        Map<String, Object> params = new HashMap<>();

        if (Objects.nonNull(RO)) {
            if (Objects.nonNull(RO.getDocumentDateFrom())) {
                whereClause = whereClause.and(Conditions.isGreaterOrEqualTo(entityTable.column("date_start"),Conditions.just("'"+RO.getDocumentDateFrom().toString()+"'")));
            }
            if (Objects.nonNull(RO.getDocumentDateTo())) {
                whereClause = whereClause.and(Conditions.isLessOrEqualTo(entityTable.column("date_start"),Conditions.just("'"+RO.getDocumentDateTo().toString()+"'")));
            }
            if (Objects.nonNull(RO.getDocumentaryType())) {
                whereClause = whereClause.and(Conditions.isEqual(entityTable.column("type"), Conditions.just(StringUtils.wrap(RO.getDocumentaryType().toString(), "'"))));
            }
            if (Objects.nonNull(RO.getDocumentaryGroup())) {
                whereClause = whereClause.and(Conditions.isEqual(entityTable.column("documentary_group"), Conditions.just(StringUtils.wrap(RO.getDocumentaryGroup().toString(), "'"))));
            }
            if (Objects.nonNull(RO.getCompanyId())) {
                whereClause = whereClause.and(Conditions.isEqual(entityTable.column("company"), Conditions.just(StringUtils.wrap(RO.getCompanyId(), "'"))));
            }
            if (Objects.nonNull(RO.getDepartment())) {
                whereClause = whereClause.and(Conditions.isEqual(entityTable.column("department"), Conditions.just(StringUtils.wrap(RO.getDepartment(), "'"))));
            }

            if (StringUtils.isNotBlank(RO.getSearch())) {
                List<String> columns = List.of(
                    EntityManager.ENTITY_ALIAS + ".content"
                );
                Condition searchCondition = null;
                StringBuilder inCondition = new StringBuilder("(");
                columns.forEach(column -> {
                    inCondition.append(String.format("unaccent(%s) iLIKE unaccent(:searchString)", column));
                    inCondition.append(" OR ");
                });
                int length = inCondition.length();
                inCondition.delete(length - 4, length);
                inCondition.append(")");
                searchCondition = Conditions.just(inCondition.toString());
                whereClause = whereClause.and(searchCondition);
                params.put("searchString", "%" + RO.getSearch() + "%");
            }
        }

        return Tuples.of(whereClause, params);
    }
}
