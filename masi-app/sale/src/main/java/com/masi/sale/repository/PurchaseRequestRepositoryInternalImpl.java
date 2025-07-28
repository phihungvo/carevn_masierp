package com.masi.sale.repository;

import com.masi.sale.domain.PurchaseRequest;
import com.masi.sale.repository.rowmapper.PurchaseRequestRowMapper;
import com.masi.sale.service.dto.PurchaseRequestQueryDTO;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;

import java.time.format.DateTimeFormatter;
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
 * Spring Data R2DBC custom repository implementation for the PurchaseRequest entity.
 */
@SuppressWarnings("unused")
class PurchaseRequestRepositoryInternalImpl
    extends SimpleR2dbcRepository<PurchaseRequest, UUID>
    implements PurchaseRequestRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final PurchaseRequestRowMapper purchaserequestMapper;

    private static final Table entityTable = Table.aliased("purchase_request", EntityManager.ENTITY_ALIAS);

    public PurchaseRequestRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        PurchaseRequestRowMapper purchaserequestMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(PurchaseRequest.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.purchaserequestMapper = purchaserequestMapper;
    }

    private Pageable getDefaultSort(Pageable pageable) {
        Sort defaultSort = Sort.by(Sort.Direction.DESC, "create_date");
        if (Objects.isNull(pageable)) {
            pageable = PageRequest.of(0, Integer.MAX_VALUE, defaultSort);
        }
        if (pageable.getSort().equals(Sort.unsorted())) {
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), defaultSort);
        }
        return pageable;
    }

    @Override
    public Flux<PurchaseRequest> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<PurchaseRequest> createQuery(Pageable pageable, Condition whereClause) {
        return createQuery(pageable, whereClause, null);
    }

    RowsFetchSpec<PurchaseRequest> createQuery(Pageable pageable, Condition whereClause, Map<String, Object> parameters) {
        List<Expression> columns = PurchaseRequestSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        SelectFromAndJoin selectFrom = Select.builder().select(columns).from(entityTable);
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, PurchaseRequest.class, pageable, whereClause);
        DatabaseClient.GenericExecuteSpec spec = db.sql(select);
        if (parameters != null && !parameters.isEmpty()) {
            spec = spec.bindValues(parameters);
        }
        return spec.map(this::process);
    }


    @Override
    public Flux<PurchaseRequest> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<PurchaseRequest> findById(UUID id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(StringUtils.wrap(id.toString(), "'")));
        return createQuery(null, whereClause).one();
    }

    private Tuple2<Condition, Map<String, Object>> buildWhereClause(PurchaseRequestQueryDTO query) {
        Condition whereClause = Conditions.isEqual(entityTable.column("is_deleted"), Conditions.just("false"));
        Map<String, Object> parameters = new HashMap<>();

        if (query.getSearchString() != null) {
            String search = StringUtils.stripAccents(query.getSearchString());
            String productNameAlias = String.format("%s.%s", EntityManager.ENTITY_ALIAS, "product_name");
            String supplierAlias = String.format("%s.%s", EntityManager.ENTITY_ALIAS, "supplier");
            String noteAlias = String.format("%s.%s", EntityManager.ENTITY_ALIAS, "note");
            String productNameClause = String.format("unaccent(%s) iLIKE :searchString", productNameAlias);
            String supplierClause = String.format("unaccent(%s) iLIKE :searchString", supplierAlias);
            String noteClause = String.format("unaccent(%s) iLIKE :searchString", noteAlias);
            parameters.put("searchString", "%" + search + "%");
            whereClause = whereClause.and(Conditions.just(String.format("( %s OR %s OR %s )", productNameClause, supplierClause, noteClause)));
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        if (query.getCreatedFrom() != null) {
            whereClause = whereClause.and(Conditions.isGreaterOrEqualTo(entityTable.column("create_date"), Conditions.just("'"+formatter.format(query.getCreatedFrom().atStartOfDay())+"'")));
        }
        if (query.getCreatedTo() != null) {
            whereClause = whereClause.and(Conditions.isLessOrEqualTo(entityTable.column("create_date"), Conditions.just("'"+formatter.format(query.getCreatedTo().atTime(23, 59, 59))+"'")));
        }
        if (query.getStatuses() != null && !query.getStatuses().isEmpty()) {
            String inClause = "(" + query.getStatuses().stream().map(status -> String.format("'%s'", status.toString())).collect(Collectors.joining(",")) + ")";
            whereClause = whereClause.and(Conditions.just(String.format(" %s IN %s ", entityTable.column("request_status"), inClause)));
        }
        return Tuples.of(whereClause, parameters);
    }

    @Override
    public Flux<PurchaseRequest> findAllByQuery(PurchaseRequestQueryDTO queryDTO, Pageable pageable) {
        Tuple2<Condition, Map<String, Object>> whereClause = buildWhereClause(queryDTO);
        pageable = getDefaultSort(pageable);
        return createQuery(pageable, whereClause.getT1(), whereClause.getT2()).all();
    }

    @Override
    public Mono<Long> countAllByQuery(PurchaseRequestQueryDTO queryDTO) {
        Tuple2<Condition, Map<String, Object>> whereClause = buildWhereClause(queryDTO);
        return createQuery(null, whereClause.getT1(), whereClause.getT2()).all().count();
    }

    private PurchaseRequest process(Row row, RowMetadata metadata) {
        PurchaseRequest entity = purchaserequestMapper.apply(row, "e");
        return entity;
    }

    @Override
    public <S extends PurchaseRequest> Mono<S> save(S entity) {
        return super.save(entity);
    }


}
