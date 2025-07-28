package com.masi.sale.repository;

import com.masi.sale.domain.Order;
import com.masi.sale.repository.rowmapper.ContractRowMapper;
import com.masi.sale.repository.rowmapper.CustomerRowMapper;
import com.masi.sale.repository.rowmapper.OrderRowMapper;
import com.masi.sale.service.dto.OrderQueryDTO;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import lombok.val;

import java.time.format.DateTimeFormatter;
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
import org.springframework.r2dbc.core.DatabaseClient.GenericExecuteSpec;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

/**
 * Spring Data R2DBC custom repository implementation for the Order entity.
 */
@SuppressWarnings("unused")
class OrderRepositoryInternalImpl extends SimpleR2dbcRepository<Order, UUID> implements OrderRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final OrderRowMapper orderMapper;
    private final ContractRowMapper contractRowMapper;
    private final CustomerRowMapper customerRowMapper;

    private static final Table entityTable = Table.aliased("masi_order", EntityManager.ENTITY_ALIAS);
    private static final Table contractTable = Table.aliased("contract", "e_contract");
    private static final Table customerTable = Table.aliased("customer", "e_customer");

    public OrderRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        OrderRowMapper orderMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter, ContractRowMapper contractRowMapper, CustomerRowMapper customerRowMapper) {
        super(
            new MappingRelationalEntityInformation(
                converter.getMappingContext().getRequiredPersistentEntity(Order.class)),
            entityOperations,
            converter);
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.orderMapper = orderMapper;
        this.contractRowMapper = contractRowMapper;
        this.customerRowMapper = customerRowMapper;
    }

    @Override
    public Flux<Order> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<Order> createQuery(Pageable pageable, Condition whereClause) {
        return createQuery(pageable, whereClause, null);
    }

    RowsFetchSpec<Order> createQuery(Pageable pageable, Condition whereClause, Map<String, Object> parameters) {
        List<Expression> columns = OrderSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(ContractSqlHelper.getColumns(contractTable, "contract"));
        columns.addAll(CustomerSqlHelper.getColumns(customerTable, "customer"));
        SelectBuilder.SelectFromAndJoinCondition selectFrom = Select.builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(contractTable)
            .on(Column.create("contract_id", entityTable))
            .equals(Column.create("id", contractTable))
            .leftOuterJoin(customerTable)
            .on(Column.create("customer_id", contractTable))
            .equals(Column.create("id", customerTable));

        // we do not support Criteria here for now as of
        // https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, Order.class, pageable, whereClause);
        GenericExecuteSpec query = db.sql(select);
        if (parameters != null && !parameters.isEmpty()) {
            query = query.bindValues(parameters);
        }
        return query.map(this::process);
    }

    @Override
    public Flux<Order> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<Order> findById(UUID id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"),
            Conditions.just(StringUtils.wrap(id.toString(), "'")));
        return createQuery(null, whereClause).one();
    }

    private Order process(Row row, RowMetadata metadata) {
        Order entity = orderMapper.apply(row, "e");
        entity.setContract(contractRowMapper.apply(row, "contract"));
        entity.getContract().setCustomer(customerRowMapper.apply(row, "customer"));
        return entity;
    }

    @Override
    public <S extends Order> Mono<S> save(S entity) {
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
    public Flux<Order> findAllByQuery(OrderQueryDTO dto, Pageable pageable) {
        pageable = getDefaultSort(pageable);
        Tuple2<Condition, Map<String, Object>> whereClause = createWhereClause(dto);
        return createQuery(pageable, whereClause.getT1(), whereClause.getT2()).all();
    }

    private Tuple2<Condition, Map<String, Object>> createWhereClause(OrderQueryDTO dto) {
        Condition whereClause = Conditions.isEqual(entityTable.column("is_deleted"), Conditions.just("false"));
        Map<String, Object> parameters = new HashMap<>();
        if (dto.getStatuses() != null && !dto.getStatuses().isEmpty()) {
            String inClause = dto.getStatuses().stream().map(status -> StringUtils.wrap(status.toString(), "'"))
                .reduce((s1, s2) -> s1 + "," + s2).orElseThrow();
            whereClause = whereClause.and(Conditions.in(entityTable.column("status"), Conditions.just(inClause)));
        }
        if (dto.getCompany() != null) {
            whereClause = whereClause.and(Conditions.isEqual(entityTable.column("company"),
                Conditions.just(StringUtils.wrap(dto.getCompany(), "'"))));
        }
        if (StringUtils.isNotBlank(dto.getSearchString())) {

            var contractNameColumn = String.format("%s.%s", contractTable.getReferenceName(), "contract_name");
            var likeContractClause = String.format("unaccent(%s) iLIKE unaccent(:search)", contractNameColumn);
            var nameLikeClause = String.format("unaccent(%s) iLIKE unaccent(:search)", entityTable.column("order_code"));

            var fullClause = String.format("(%s OR %s)", likeContractClause, nameLikeClause);

            whereClause = whereClause.and(Conditions.just(fullClause));
            parameters.put("search", "%" + dto.getSearchString().trim() + "%");
        }
        if (dto.getStartDate() != null && dto.getEndDate() != null) {
            var sqlDateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            whereClause = whereClause.and(Conditions.between(entityTable.column("date_order"),
                Conditions.just(StringUtils.wrap(dto.getStartDate().format(sqlDateFormatter), "'")),
                Conditions.just(StringUtils.wrap(dto.getEndDate().format(sqlDateFormatter), "'"))));
        }

        if (dto.getCustomerId() != null) {
            whereClause = whereClause.and(Conditions.isEqual(contractTable.column("customer_id"),
                Conditions.just(StringUtils.wrap(dto.getCustomerId().toString(), "'"))));
        }
        return Tuples.of(whereClause, parameters);
    }

    @Override
    public Mono<Long> countAllByQuery(OrderQueryDTO dto) {
        Tuple2<Condition, Map<String, Object>> whereClause = createWhereClause(dto);
        return createQuery(null, whereClause.getT1(), whereClause.getT2()).all().count();
    }

    @Override
    public Mono<Order> findByIdAndIsDeletedIsFalse(UUID id) {
        Condition whereClause = Conditions
            .isEqual(entityTable.column("id"), Conditions.just(StringUtils.wrap(id.toString(), "'")))
            .and(Conditions.isEqual(entityTable.column("is_deleted"), Conditions.just("false")));
        return createQuery(null, whereClause).one();

    }
}
