package com.masi.logistics.repository;

import com.masi.logistics.domain.Warehouse;
import com.masi.logistics.domain.criteria.WarehouseCriteria;

import java.util.List;
import java.util.UUID;

import com.masi.logistics.service.mapper.ContactGiftMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the Warehouse entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WarehouseRepository extends ReactiveCrudRepository<Warehouse, UUID>, WarehouseRepositoryInternal {
    Flux<Warehouse> findAllBy(Pageable pageable);

    @Query("SELECT * FROM warehouse entity WHERE entity.warehouse_type_id = :id")
    Flux<Warehouse> findByWarehouseType(UUID id);

    @Query("SELECT * FROM warehouse entity WHERE entity.warehouse_type_id IS NULL")
    Flux<Warehouse> findAllWhereWarehouseTypeIsNull();

    @Override
    <S extends Warehouse> Mono<S> save(S entity);

    @Override
    Flux<Warehouse> findAll();

    @Override
    Mono<Warehouse> findById(UUID id);

    @Override
    Mono<Void> deleteById(UUID id);

    @Query("""
        SELECT w.* FROM warehouse w
        JOIN warehouse_type wt on w.warehouse_type_id = wt."id"
            AND w.delete_at IS NULL
            AND w.delete_by IS NULL
            AND w.active = TRUE
        JOIN item_category ic on ic.warehouse_type_id = wt."id" AND ic.code IN (:itemCategoryCodes) AND wt.delete_at IS NULL AND wt.delete_by IS NULL
        WHERE (w.company = :company AND wt.company = :company AND ic.company = :company )
        ORDER BY w.create_at DESC
        LIMIT :limit OFFSET :offset
    """)
    Flux<Warehouse> findByItemCategory(String company, List<String> itemCategoryCodes, int limit, int offset);

    @Query("""
        SELECT COUNT(*) FROM warehouse w
        JOIN warehouse_type wt on w.warehouse_type_id = wt."id"
            AND w.delete_at IS NULL
            AND w.delete_by IS NULL
            AND w.active = TRUE
        JOIN item_category ic on ic.warehouse_type_id = wt."id" AND ic.code IN (:itemCategoryCodes) AND wt.delete_at IS NULL AND wt.delete_by IS NULL
        WHERE (w.company = :company AND wt.company = :company AND ic.company = :company )
    """)
    Mono<Long> countByItemCategory(String company, List<String> itemCategoryCodes);

    @Query("SELECT * FROM warehouse WHERE id IN (:warehouseIds) AND active = true")
    Flux<Warehouse> findAllByIdIn(List<UUID> warehouseIds);

    @Query("SELECT * FROM warehouse WHERE company = :company AND warehouse_type_page = :warehouseType")
    Mono<Warehouse> findByWarehouseTypePageAndCompany( String warehouseType , String company);
}

interface WarehouseRepositoryInternal {
    <S extends Warehouse> Mono<S> save(S entity);

    Flux<Warehouse> findAllBy(Pageable pageable);

    Flux<Warehouse> findAll();

    Mono<Warehouse> findById(UUID id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Warehouse> findAllBy(Pageable pageable, Criteria criteria);
    Flux<Warehouse> findByCriteria(WarehouseCriteria criteria, Pageable pageable);

    Mono<Long> countByCriteria(WarehouseCriteria criteria);
}
