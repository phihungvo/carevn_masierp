package com.masi.sale.repository;

import com.masi.sale.domain.QuotationDetail;
import com.masi.sale.repository.rowmapper.MaterialRowMapper;
import com.masi.sale.repository.rowmapper.QuotationDetailRowMapper;
import com.masi.sale.repository.rowmapper.QuotationRowMapper;
import com.masi.sale.service.dto.QuotationDetailGetListDTO;
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
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Comparison;
import org.springframework.data.relational.core.sql.Condition;
import org.springframework.data.relational.core.sql.Conditions;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Select;
import org.springframework.data.relational.core.sql.SelectBuilder.SelectFromAndJoinCondition;
import org.springframework.data.relational.core.sql.Table;
import org.springframework.data.relational.repository.support.MappingRelationalEntityInformation;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.r2dbc.core.RowsFetchSpec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

/**
 * Spring Data R2DBC custom repository implementation for the QuotationDetail
 * entity.
 */
@SuppressWarnings("unused")
class QuotationDetailRepositoryInternalImpl
        extends SimpleR2dbcRepository<QuotationDetail, UUID>
        implements QuotationDetailRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final QuotationRowMapper quotationMapper;
    private final QuotationDetailRowMapper quotationdetailMapper;
    private final MaterialRowMapper materialMapper;

    private static final Table entityTable = Table.aliased("quotation_detail", EntityManager.ENTITY_ALIAS);
    private static final Table quotationTable = Table.aliased("quotation", "quotation");
    private static final Table materialTable = Table.aliased("material", "material");

    public QuotationDetailRepositoryInternalImpl(
            R2dbcEntityTemplate template,
            EntityManager entityManager,
            QuotationRowMapper quotationMapper,
            QuotationDetailRowMapper quotationdetailMapper,
            R2dbcEntityOperations entityOperations,
            R2dbcConverter converter, MaterialRowMapper materialMapper) {
        super(
                new MappingRelationalEntityInformation(
                        converter.getMappingContext().getRequiredPersistentEntity(QuotationDetail.class)),
                entityOperations,
                converter);
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.quotationMapper = quotationMapper;
        this.quotationdetailMapper = quotationdetailMapper;
        this.materialMapper = materialMapper;
    }

    @Override
    public Flux<QuotationDetail> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<QuotationDetail> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = QuotationDetailSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(MaterialSqlHelper.getColumns(materialTable, "material"));
        SelectFromAndJoinCondition selectFrom = Select.builder()
                .select(columns)
                .from(entityTable)
                .leftOuterJoin(materialTable)
                .on(Column.create("material_id", entityTable))
                .equals(Column.create("id", materialTable));
        // we do not support Criteria here for now as of
        // https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, QuotationDetail.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override

    public Flux<QuotationDetail> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<QuotationDetail> findById(UUID id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"),
                Conditions.just(StringUtils.wrap(id.toString(), "'")));
        return createQuery(null, whereClause).one();
    }

    private QuotationDetail process(Row row, RowMetadata metadata) {
        QuotationDetail entity = quotationdetailMapper.apply(row, "e");
        entity.setMaterial(materialMapper.apply(row, "material"));
        return entity;
    }

    @Override
    public <S extends QuotationDetail> Mono<S> save(S entity) {
        return super.save(entity);
    }

    private Pageable getDefaultSort(Pageable pageable) {
        Sort defaultSort = Sort.by(Sort.Direction.ASC, "index");
        if (Objects.isNull(pageable)) {
            pageable = PageRequest.of(0, Integer.MAX_VALUE, defaultSort);
        }
        if (pageable.getSort().equals(Sort.unsorted())) {
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), defaultSort);
        }
        return pageable;
    }

    @Override
    public Flux<QuotationDetail> findAllByQuery(Pageable pageable, QuotationDetailGetListDTO dto) {
        // TODO Auto-generated method stub
        var condition = buildConditionFromRO(dto);
        pageable= getDefaultSort(pageable);
        return createQuery(pageable, condition.getT1()).all();
    }

    private Tuple2<Condition, Map<String, Object>> buildConditionFromRO(QuotationDetailGetListDTO dto) {
        Condition condition = Conditions.isNull(entityTable.column("deleted_by"));
        condition = condition.and(Conditions.isNull(entityTable.column("deleted_date")));
        condition = condition.and(Conditions.isEqual(entityTable.column("is_deleted"), Conditions.just("false")));
        condition = condition.and(Conditions.isEqual(materialTable.column("is_deleted"), Conditions.just("false")));

        Map<String, Object> params = new HashMap<>();

        if (dto.getQuotationId() != null) {
            condition = condition.and(Conditions.isEqual(entityTable.column("quotation_id"),
                    Conditions.just(StringUtils.wrap(dto.getQuotationId().toString(), "'"))));
        }
        if (dto.getCompany() != null) {
            condition = condition.and(Conditions.isEqual(entityTable.column("company"),
                    Conditions.just(StringUtils.wrap(dto.getCompany().toString(), "'"))));
        }

        if (dto.getMaterialId() != null) {
            condition = condition.and(Conditions.isEqual(entityTable.column("material_id"),
                    Conditions.just(StringUtils.wrap(dto.getMaterialId().toString(), "'"))));
        }

        if (dto.getListId() != null && !dto.getListId().isEmpty()) {
            String inClause = dto.getListId().stream().map(e -> "'" + e + "'")
                    .reduce((a, b) -> a + "," + b).orElseThrow();
            condition = condition.and(Conditions.in(entityTable.column("id"), Conditions.just(inClause)));
        }

        return Tuples.of(condition, params);
    }

    @Override
    public Mono<Long> countByQuery(QuotationDetailGetListDTO dto) {
        // TODO Auto-generated method stub
        var condition = buildConditionFromRO(dto);
        return createQuery(null, condition.getT1()).all().count();
    }

}
