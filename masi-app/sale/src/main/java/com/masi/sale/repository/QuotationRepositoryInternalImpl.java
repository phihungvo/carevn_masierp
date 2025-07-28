package com.masi.sale.repository;

import com.carevn.masi.constants.AuthoritiesConstants;
import com.masi.sale.domain.Quotation;
import com.masi.sale.repository.rowmapper.CustomerRowMapper;
import com.masi.sale.repository.rowmapper.QuotationRowMapper;
import com.masi.sale.service.dto.QuotationGetListDTO;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
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
import reactor.util.function.*;

/**
 * Spring Data R2DBC custom repository implementation for the Quotation entity.
 */
@SuppressWarnings("unused")
class QuotationRepositoryInternalImpl extends SimpleR2dbcRepository<Quotation, UUID>
        implements QuotationRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final QuotationRowMapper quotationMapper;
    private final CustomerRowMapper customerMapper;

    private static final Table entityTable = Table.aliased("quotation", EntityManager.ENTITY_ALIAS);
    private static final Table customerTable = Table.aliased("customer", "customer");

    public QuotationRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        QuotationRowMapper quotationMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter, CustomerRowMapper customerMapper) {
        super(
                new MappingRelationalEntityInformation(
                        converter.getMappingContext().getRequiredPersistentEntity(Quotation.class)),
                entityOperations,
                converter);
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.quotationMapper = quotationMapper;
        this.customerMapper = customerMapper;
    }

    @Override
    public Flux<Quotation> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<Quotation> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = QuotationSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        var selectFrom = Select.builder().select(columns).from(entityTable);
        // we do not support Criteria here for now as of
        // https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, Quotation.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    RowsFetchSpec<Quotation> createQueryCustom(Pageable pageable, Condition whereClause, Map<String, Object> params) {
        List<Expression> columns = QuotationSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(CustomerSqlHelper.getColumns(customerTable, "customer"));

        SelectBuilder.SelectFromAndJoinCondition selectFrom = Select.builder().select(columns).from(entityTable)
            .leftOuterJoin(customerTable)
            .on(Column.create("customer_id", entityTable))
            .equals(Column.create("id", customerTable));
        // we do not support Criteria here for now as of
        // https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, Quotation.class, pageable, whereClause);
        var query = db.sql(select);
        if (params != null && !params.isEmpty()) {
            query = query.bindValues(params);
        }
        return query.map(this::process);
    }

    @Override
    public Flux<Quotation> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<Quotation> findById(UUID id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"),
                Conditions.just(StringUtils.wrap(id.toString(), "'")));
        return createQuery(null, whereClause).one();
    }

    private Quotation process(Row row, RowMetadata metadata) {
        Quotation entity = quotationMapper.apply(row, "e");
        entity.setCustomer(customerMapper.apply(row, "customer"));
        return entity;
    }

    @Override
    public <S extends Quotation> Mono<S> save(S entity) {
        return super.save(entity);
    }

    private Pageable getDefaultSort(Pageable pageable) {
        Sort defaultSort = Sort.by(Sort.Direction.DESC, "created_date");
        if (Objects.isNull(pageable)) {
            pageable = PageRequest.of(0, Integer.MAX_VALUE, defaultSort);
        }
        if (pageable.getSort().equals(Sort.unsorted())) {
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), defaultSort);
        }
        return pageable;
    }

    @Override
    public Flux<Quotation> findAllByQuery(Pageable pageable, QuotationGetListDTO dto) {
        var condition = buildConditionFromRO(dto);
        pageable = getDefaultSort(pageable);
        return createQueryCustom(pageable, condition.getT1(), condition.getT2()).all();
    }

    private Tuple2<Condition, Map<String, Object>> buildConditionFromRO(QuotationGetListDTO ro) {
        Condition condition = Conditions.isNull(entityTable.column("deleted_by"));
        condition = condition.and(Conditions.isNull(entityTable.column("deleted_date")));
        condition = condition.and(Conditions.isEqual(entityTable.column("is_deleted"), Conditions.just("false")));
        Map<String, Object> params = new HashMap<>();
        if (StringUtils.isNotBlank(ro.getSearch())) {
            var columnAlias = EntityManager.ENTITY_ALIAS + ".name";
            String likeClause = String.format("unaccent(%s) ILIKE unaccent(:search)", columnAlias);
            condition = condition.and(Conditions.just(likeClause));
            params.put("search", "%" + ro.getSearch() + "%");
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

        if (ro.getAuth().contains(AuthoritiesConstants.STAFF)
                || ro.getAuth().contains(AuthoritiesConstants.DEPARTMENT_MANAGER)) {
            condition = condition.and(Conditions.isEqual(entityTable.column("department"),
                    Conditions.just("'" + ro.getDepartment() + "'")));
        }

        return Tuples.of(condition, params);
    }

    @Override
    public Mono<Long> countByQuery(QuotationGetListDTO dto) {
        // TODO Auto-generated method stub
        var condition = buildConditionFromRO(dto);
        return createQueryCustom(null, condition.getT1(), condition.getT2()).all().count();
    }
}
