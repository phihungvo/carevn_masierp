package com.masi.production.repository;

import com.masi.production.domain.ManufactureOrder;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import com.masi.production.domain.enumeration.MoStatus;
import com.masi.production.service.dto.ManufactureOrderRO;
import com.masi.production.service.dto.ManufactureWorkOrdersRO;
import com.masi.production.service.dto.ProductRoutingDTO;
import com.masi.production.service.mapper.AdditiveMaterialChecklistMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.function.Function;

/**
 * Spring Data R2DBC repository for the ManufactureOrder entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ManufactureOrderRepository extends ReactiveCrudRepository<ManufactureOrder, UUID>, ManufactureOrderRepositoryInternal {
    Flux<ManufactureOrder> findAllBy(Pageable pageable);

    Flux<ManufactureOrder> findAllByIsActiveIsTrue(Pageable pageable);

    Mono<Long> countAllByIsActiveIsTrue();

    @Modifying
    @Query("UPDATE manufacture_order SET is_active = false WHERE id = :id")
    Mono<Void> softDeleteById(UUID id);

    Flux<ManufactureOrder> findAllByOrderIdInAndIsActiveIsTrue(Collection<UUID> orderIds);

    @Query("SELECT * FROM manufacture_order WHERE id IN (:ids)")
    Flux<ManufactureOrder> findAllByIds(List<UUID> ids);

    @Override
    <S extends ManufactureOrder> Mono<S> save(S entity);

    @Override
    Flux<ManufactureOrder> findAll();

    @Query("SELECT * FROM manufacture_order WHERE id = :id")
    Mono<ManufactureOrder> findByIdBrier(UUID id);

//    @Override
    Mono<ManufactureOrder> findById(UUID id);

    @Override
    Mono<Void> deleteById(UUID id);

    @Query("SELECT * FROM manufacture_order WHERE production_standard_id IN (:listId) and is_active = true")
    Flux<ManufactureOrder> findAllByProductionStandardIds(Collection<UUID> listId);

    @Modifying
    @Query("UPDATE manufacture_order  SET status = :status WHERE id = :id")
    Mono<Void> updateStatusMo(@Param("id") UUID id, @Param("status") MoStatus status);


    @Query("""
           SELECT mo.*
           FROM pro_maintain_pro_package pmp
           JOIN product_package pp ON pmp.product_package_id = pp.id
           JOIN manufacture_order mo ON pp.manufacture_order_id = mo.id
        WHERE pmp.product_maintain_id = :productMaintainId AND pmp.is_deleted = false
        LIMIT 1;
    """)
    Mono<ManufactureOrder> findByMantainId(UUID productMaintainId);

    @Query("""
           SELECT mo.*
           FROM pro_maintain_pro_package pmp
           JOIN product_package pp ON pmp.product_package_id = pp.id
           AND pp.company = :company
           JOIN manufacture_order mo ON pp.manufacture_order_id = mo.id
            AND mo.company = :company
        WHERE pmp.product_maintain_id = :productMaintainId AND pmp.is_deleted = false AND pmp.company = :company
        LIMIT 1;
    """)
    Mono<ManufactureOrder> findByMaintainIdAndCompany(UUID productMaintainId, String company);

    @Query("""
            select coalesce (SUM(
             COALESCE((attributes->'rawMaterial'->>'fishHead')::numeric, 0)
             + COALESCE((attributes->'rawMaterial'->>'freshFish')::numeric, 0)
         ) , 0) AS total_value
        from manufacture_order mo
        where (:standardId is null or mo.production_standard_id = :standardId)
    """)
    Mono<BigDecimal> countTotalQuantityByStandardId(UUID standardId);


    @Query("SELECT * FROM manufacture_order WHERE production_routing_id = :id LIMIT 1")
    Mono<ManufactureOrder> findByProductRoutingId(UUID id);
}

interface ManufactureOrderRepositoryInternal {
    <S extends ManufactureOrder> Mono<S> save(S entity);

    Flux<ManufactureOrder> findAllBy(Pageable pageable);

    Flux<ManufactureOrder> findAll();

//    Mono<ManufactureOrder> findById(UUID id);

    Mono<ManufactureOrder> findByIdManufacture(UUID id);

    Flux<ManufactureOrder> findAllByFilter(ManufactureOrderRO moRO, Pageable pageable);

    Flux<ManufactureOrder> findAllWithWorkOrdersByFilter(ManufactureWorkOrdersRO moRO, Pageable pageable);

    Mono<Long> countAllByFilter(ManufactureOrderRO moRO);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<ManufactureOrder> findAllBy(Pageable pageable, Criteria criteria);
}
