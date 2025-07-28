package com.masi.sale.repository;

import com.masi.sale.domain.Customer;
import com.masi.sale.repository.rowmapper.CustomerRowMapper;
import com.masi.sale.service.dto.CustomerRO;
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
import org.springframework.util.CollectionUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import static com.masi.sale.domain.enumeration.CustomerStatus.ENABLED;

/**
 * Spring Data R2DBC custom repository implementation for the Customer entity.
 */
@SuppressWarnings("unused")
class CustomerRepositoryInternalImpl extends SimpleR2dbcRepository<Customer, UUID> implements CustomerRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final CustomerRowMapper customerMapper;

    private static final Table entityTable = Table.aliased("customer", EntityManager.ENTITY_ALIAS);

    public CustomerRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        CustomerRowMapper customerMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(Customer.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.customerMapper = customerMapper;
    }

    @Override
    public Flux<Customer> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<Customer> createQuery(Pageable pageable, Condition whereClause) {
        return this.createQuery(pageable, whereClause, null);
    }


    RowsFetchSpec<Customer> createQuery(Pageable pageable, Condition whereClause, Map<String, Object> parameters) {
        List<Expression> columns = CustomerSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        SelectFromAndJoin selectFrom = Select.builder().select(columns).from(entityTable);
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, Customer.class, pageable, whereClause);
        if (parameters != null && !parameters.isEmpty()) {
            return db.sql(select).bindValues(parameters).map(this::process);
        }
        return db.sql(select).map(this::process);
    }


    private Tuple2<Condition, Map<String, Object>> buildWhereClause(CustomerRO moRO) {
        Condition whereClause = Conditions.isEqual(entityTable.column("is_deleted"), Conditions.just("false"));
        Map<String, Object> parameters = new HashMap<>();
        DateTimeFormatter sqlDateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        if (Objects.nonNull(moRO)) {
            //append where clause with date between if RO fromDate and toDate are not null
            if (Objects.nonNull(moRO.getContractFrom()) && Objects.nonNull(moRO.getContractTo())) {
                whereClause = whereClause.and(Conditions.between(entityTable.column("contract_signed"),
                    Conditions.just(StringUtils.wrap(moRO.getContractFrom().toString(), "'")),
                    Conditions.just(StringUtils.wrap(moRO.getContractTo().toString(), "'"))));
            }
            if (Objects.nonNull(moRO.getCustomerId()) && !moRO.getCustomerId().isEmpty()) {
                String inClause = moRO.getCustomerId().stream().map(id -> StringUtils.wrap(id.toString(), "'")).collect(Collectors.joining(", "));
                whereClause = whereClause.and(Conditions.in(entityTable.column("id"), Conditions.just(inClause)));
            }
            if (Objects.nonNull(moRO.getBirthdayFrom())) {

                int month = moRO.getBirthdayFrom().getMonthValue();
                String rawSql = String.format("((extract(month from %s) = %d and extract(day from %s) >= %d) or extract(month from %s) > %d)",
                    entityTable.column("birthday"), month, entityTable.column("birthday"), moRO.getBirthdayFrom().getDayOfMonth(),
                    entityTable.column("birthday"), month);
                whereClause = whereClause.and(Conditions.just(rawSql));
            }
            if (Objects.nonNull(moRO.getBirthdayTo())) {
                int month = moRO.getBirthdayTo().getMonthValue();
                String rawSql = String.format("((extract(month from %s) = %d and extract(day from %s) <= %d) or extract(month from %s) < %d)",
                    entityTable.column("birthday"), month, entityTable.column("birthday"), moRO.getBirthdayTo().getDayOfMonth(),
                    entityTable.column("birthday"), month);
                whereClause = whereClause.and(Conditions.just(rawSql));
            }

            if (moRO.getCustomerStatus() == ENABLED) {
                whereClause = whereClause.and(Conditions.isEqual(entityTable.column("customer_status"), Conditions.just("'ENABLED'")));
            } else {
                whereClause = whereClause.and(Conditions.isEqual(entityTable.column("customer_status"), Conditions.just("'DISABLED'")));
            }

            //append where clause with search string if column 'name' contain search string
            if (StringUtils.isNotBlank(moRO.getSearch())) {
                List<String> columns = List.of("customer_code", "company_name", "tax_code", "first_name", "last_name", "phone_number", "email");
                var columnAliases = columns.stream().map(c -> String.format("%s.%s", EntityManager.ENTITY_ALIAS, c)).toList();

                StringBuilder searchCondition = new StringBuilder("(");
                columnAliases.forEach(column -> {
                    searchCondition.append(String.format("unaccent(%s) iLIKE unaccent(:searchString) OR ", column));
                });
                var length = searchCondition.length();
                searchCondition.delete(length - 4, length);

                whereClause = whereClause.and(Conditions.just(searchCondition.append(")").toString()));
                parameters.put("searchString", "%" + moRO.getSearch() + "%");
            }
            if (Objects.nonNull(moRO.getListEmployeeOwner()) && !moRO.getListEmployeeOwner().isEmpty()) {
                List<String> employeeOwnerList = moRO.getListEmployeeOwner().stream()
                    .map(UUID::toString)
                    .toList();
                String employeeOwnerCondition = employeeOwnerList.stream()
                    .map(owner -> EntityManager.ENTITY_ALIAS + ".customer_owner = '" + owner + "'")
                    .collect(Collectors.joining(" OR ", "(", ")"));

                whereClause = whereClause.and(Conditions.just(employeeOwnerCondition));
            }

            if (Objects.nonNull(moRO.getCompany())) {
                whereClause = whereClause.and(Conditions.isEqual(entityTable.column("company"), Conditions.just(StringUtils.wrap(moRO.getCompany(), "'"))));
            }
//            if (Objects.nonNull(ro.getDepartment())) {
//                whereClause = whereClause.and(Conditions.isEqual(entityTable.column("department"), Conditions.just(StringUtils.wrap(RO.getDepartment(), "'"))));
//            }

        }

        return Tuples.of(whereClause, parameters);
    }


    @Override
    public Flux<Customer> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<Customer> findById(UUID id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(StringUtils.wrap(id.toString(), "'")));
        return createQuery(null, whereClause).one();
    }

    private Customer process(Row row, RowMetadata metadata) {
        Customer entity = customerMapper.apply(row, "e");
        return entity;
    }

    @Override
    public <S extends Customer> Mono<S> save(S entity) {
        return super.save(entity);
    }


    @Override
    public Flux<Customer> findAllByFilter(CustomerRO moRO, Pageable pageable) {
        Sort defaultSort = Sort.by(Sort.Direction.DESC, "created_date");
        if (Objects.isNull(pageable) || pageable.getSort().equals(Sort.unsorted())) {
            int pageNumber = pageable != null ? pageable.getPageNumber() : 0;
            int pageSize = pageable != null ? pageable.getPageSize() : Integer.MAX_VALUE;
            pageable = PageRequest.of(pageNumber, pageSize, defaultSort);
        }
        Tuple2<Condition, Map<String, Object>> whereClauses = buildWhereClause(moRO);
        return createQuery(pageable, whereClauses.getT1(), whereClauses.getT2()).all();
    }


    @Override
    public Mono<Long> countAllByFilter(CustomerRO moRO) {
        Tuple2<Condition, Map<String, Object>> whereClauses = buildWhereClause(moRO);
        return createQuery(null, whereClauses.getT1(), whereClauses.getT2()).all().count();
    }
}
