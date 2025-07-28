package com.masi.sale.repository;

import com.masi.sale.domain.Contract;
import com.masi.sale.domain.enumeration.ContractStatus;
import com.masi.sale.repository.rowmapper.ContractRowMapper;
import com.masi.sale.repository.rowmapper.CustomerRowMapper;
import com.masi.sale.service.dto.ContractRO;
import com.masi.sale.service.dto.reponse.ContractTotalReponse;
import com.masi.sale.service.mapper.CustomerMapper;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;

import java.math.BigDecimal;
import java.time.LocalDate;
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
 * Spring Data R2DBC custom repository implementation for the Contract entity.
 */
@SuppressWarnings("unused")
class ContractRepositoryInternalImpl extends SimpleR2dbcRepository<Contract, UUID> implements ContractRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final CustomerRowMapper customerMapper;
    private final ContractRowMapper contractMapper;

    private static final Table entityTable = Table.aliased("contract", EntityManager.ENTITY_ALIAS);
    private static final Table customerTable = Table.aliased("customer", "customer");

    public ContractRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        ContractRowMapper contractMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter, CustomerMapper customerMapper, CustomerRowMapper customerMapper1
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(Contract.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.contractMapper = contractMapper;
        this.customerMapper = customerMapper1;
    }

    @Override
    public Flux<Contract> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<Contract> createQuery(Pageable pageable, Condition whereClause) {
        return this.createQuery(pageable, whereClause, null);
    }


    RowsFetchSpec<Contract> createQuery(Pageable pageable, Condition whereClause, Map<String, Object> params) {
        List<Expression> columns = ContractSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(CustomerSqlHelper.getColumns(customerTable, "customer"));
        SelectBuilder.SelectFromAndJoinCondition selectFrom = Select.builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(customerTable)
            .on(Column.create("customer_id", entityTable))
            .equals(Column.create("id", customerTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, Contract.class, pageable, whereClause);
        DatabaseClient.GenericExecuteSpec query = db.sql(select);
        // bind params
        if (params != null && !params.isEmpty()) {
            query = query.bindValues(params);
        }
        return query.map(this::process);
    }


    @Override
    public Flux<Contract> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<Contract> findById(UUID id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(StringUtils.wrap(id.toString(), "'")));
        return createQuery(null, whereClause).one();
    }


    private Contract process(Row row, RowMetadata metadata) {
        Contract entity = contractMapper.apply(row, "e");
        entity.setCustomer(customerMapper.apply(row, "customer"));
        return entity;
    }

    @Override
    public <S extends Contract> Mono<S> save(S entity) {
        return super.save(entity);
    }

    private Pageable getDefaultSortIfUnsorted(Pageable pageable) {
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
    public Flux<Contract> findAllByFilter(Pageable pageable, ContractRO ro) {
        pageable = getDefaultSortIfUnsorted(pageable);
        Tuple2<Condition, Map<String, Object>> whereTuple = buildWhereClause(ro);
        return createQuery(pageable, whereTuple.getT1(), whereTuple.getT2()).all();
    }

    private Tuple2<Condition, Map<String, Object>> buildWhereClause(ContractRO ro) {

        Condition whereClause = Conditions.isEqual(entityTable.column("is_deleted"), Conditions.just("false"));

        Map<String, Object> params = new HashMap<>();
        if (Objects.nonNull(ro)) {
            if (!ro.getWithFull()) {
                var subQuery1 = "SELECT count(*) from contract_material where is_deleted = false and e.id = id_contract";
                var subQuery2 = "SELECT count(*) from contract_material where is_deleted = false and e.id = id_contract and order_id is not null";
                whereClause = whereClause.and(Conditions.just("((" + subQuery1 + ") - (" + subQuery2 + ")) > 0"));
//                ro.setContractStatusList(List.of(ContractStatus.));/
            }
            if (Objects.nonNull(ro.getContractValidFrom()) && Objects.nonNull(ro.getContractValidTo())) {
                String contractFrom = "'" + ro.getContractValidFrom().toString() + "'";
                String contractTo = "'" + ro.getContractValidTo().toString() + "'";
                whereClause = whereClause.and(Conditions.isGreaterOrEqualTo(entityTable.column("contract_valid_from"), Conditions.just(contractFrom)));
                whereClause = whereClause.and(Conditions.isLessOrEqualTo(entityTable.column("contract_valid_to"), Conditions.just(contractTo)));
            } else {
                // Điều kiện cho năm hiện tại
                LocalDate now = LocalDate.now();
                int currentYear = now.getYear();
                whereClause = whereClause.and(Conditions.isGreaterOrEqualTo(entityTable.column("contract_valid_to"), Conditions.just("'" + currentYear + "-01-01'")));
//                whereClause = whereClause.and(Conditions.isLessOrEqualTo(entityTable.column("contract_valid_to"), Conditions.just("'" + currentYear + "-12-31'")));

            }
            if (Objects.nonNull(ro.getIsExpired())) {
                LocalDate now = LocalDate.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                String nowString = now.format(formatter);
                if (ro.getIsExpired()) {
                    whereClause = whereClause.and(Conditions.isLess(entityTable.column("contract_valid_to"), Conditions.just("'" + nowString + "'")));
                } else {
                    whereClause = whereClause.and(Conditions.isGreaterOrEqualTo(entityTable.column("contract_valid_to"), Conditions.just("'" + nowString + "'")));
                }
            }


            if (Objects.nonNull(ro.getContractType())) {
                whereClause = whereClause.and(Conditions.isEqual(entityTable.column("contract_type"), Conditions.just(StringUtils.wrap(ro.getContractType().toString(), "'"))));
            }

            if (Objects.nonNull(ro.getProteinPercent())) {
                whereClause = whereClause.and(Conditions.isEqual(entityTable.column("protein_percent"), Conditions.just(StringUtils.wrap(ro.getProteinPercent(), "'"))));
            }

            if (StringUtils.isNotBlank(ro.getSearch())) {
                String column = EntityManager.ENTITY_ALIAS + ".contract_name";
                String inCondition = "unaccent(" + column + ") iLIKE unaccent(:searchString)";
                whereClause = whereClause.and(Conditions.just(inCondition));
                params.put("searchString", "%" + ro.getSearch() + "%");
            }


            try {
                if (ro.getContractStatusList().contains(ContractStatus.DELETED)) {
                    whereClause = whereClause.and(Conditions.isEqual(entityTable.column("is_active"), Conditions.just("false")));
//                    return Tuples.of(whereClause, params);
                }
                else {
                    whereClause = whereClause.and(Conditions.isEqual(entityTable.column("is_active"), Conditions.just("true")));
                }
            } catch (Exception ignored) {
                whereClause = whereClause.and(Conditions.isEqual(entityTable.column("is_active"), Conditions.just("true")));
            }

            if (Objects.nonNull(ro.getContractStatusList()) && !ro.getContractStatusList().isEmpty()) {
                List<String> excludedStatuses = Arrays.asList(ContractStatus.DELETED.toString());

                List<Expression> statusExpressions = ro.getContractStatusList().stream()
                        .filter(status -> !excludedStatuses.contains(status.toString()))
                        .map(status -> Conditions.just("'" + status.toString() + "'"))
                        .collect(Collectors.toList());

                // Proceed with the rest of your logic using `statusExpressions`
            }


            if (StringUtils.isNotBlank(ro.getCompany())) {
                whereClause = whereClause.and(Conditions.isEqual(entityTable.column("company"), Conditions.just(StringUtils.wrap(ro.getCompany(), "'"))));
            }

            if (Objects.nonNull(ro.getEmployeeOwner()) && !ro.getEmployeeOwner().isEmpty()) {
//                List<Expression> companyExpressions = ro.getCompanyName().stream()
//                        .map(company -> Conditions.just("'" + company + "'"))
//                        .collect(Collectors.toList());
                var inClause = ro.getEmployeeOwner().stream().map(company -> "'" + company + "'").collect(Collectors.joining(", "));
                whereClause = whereClause.and(Conditions.in(entityTable.column("contract_owner"), Conditions.just(inClause)));
            }


            if (Objects.nonNull(ro.getCompanyName()) && !ro.getCompanyName().isEmpty()) {
                var inClause = ro.getCompanyName().stream().map(company -> "'" + company.toString() + "'").collect(Collectors.joining(", "));
                whereClause = whereClause.and(Conditions.in(entityTable.column("customer_id"), Conditions.just(inClause)));
            }



        }

        return Tuples.of(whereClause, params);
    }

    /*    @Override
        public Mono<Long> countByFilter(ContractRO ro) {
            Tuple2<Condition, Map<String, Object>> whereTuple = buildWhereClause(ro);
            return createQuery(null, whereTuple.getT1(), whereTuple.getT2()).all().count();
        }*/

/*    @Override
    public Mono<ContractTotalReponse> countByFilter(ContractRO ro) {
        Tuple2<Condition, Map<String, Object>> whereTuple = buildWhereClause(ro);

        return createQuery(null, whereTuple.getT1(), whereTuple.getT2())
            .all()
            .collect(Collectors.summarizingDouble(contract -> {
                // Assuming your contract entity has a method getTotalValue() that returns the value of the contract
                return contract.getContractTotal().doubleValue();
            }))
            .map(summary -> {
                ContractTotalReponse response = new ContractTotalReponse();
                response.setTotal_quantity(summary.getCount());
                response.setTotal_value(BigDecimal.valueOf(summary.getSum()));
                return response;
            });
    }*/

    @Override
    public Mono<Long> countByFilter(ContractRO ro) {
        Tuple2<Condition, Map<String, Object>> whereTuple = buildWhereClause(ro);
        return createQuery(null, whereTuple.getT1(), whereTuple.getT2()).all().count();
    }

}
