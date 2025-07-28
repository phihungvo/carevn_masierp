package com.masi.production.repository;

import com.masi.production.domain.WorkOrder;

import java.util.List;
import java.util.UUID;

import com.masi.production.domain.enumeration.MoStatus;
import com.masi.production.domain.enumeration.WoStatus;
import com.masi.production.service.dto.ManufactureOrderRO;
import com.masi.production.service.dto.ManufactureWorkOrdersRO;
import com.masi.production.service.dto.WorkOrderRO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the WorkOrder entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WorkOrderRepository extends ReactiveCrudRepository<WorkOrder, UUID>, WorkOrderRepositoryInternal {
    Flux<WorkOrder> findAllBy(Pageable pageable);

    @Query("SELECT * FROM work_order entity WHERE entity.work_item_id = :id")
    Flux<WorkOrder> findByWorkItem(UUID id);

    @Query("SELECT * FROM work_order entity WHERE entity.work_item_id IS NULL")
    Flux<WorkOrder> findAllWhereWorkItemIsNull();

    @Query("SELECT * FROM work_order entity WHERE entity.manufacture_order_id = :id")
    Flux<WorkOrder> findByManufactureOrder(UUID id);

    @Query("SELECT * FROM work_order entity WHERE entity.manufacture_order_id IS NULL")
    Flux<WorkOrder> findAllWhereManufactureOrderIsNull();

    @Query("SELECT * FROM work_order entity WHERE entity.manufacture_order_id = :id AND entity.status != :status")
    Flux<WorkOrder> findByManufactureOrderAndStatus(UUID id, String status);

    @Override
    <S extends WorkOrder> Mono<S> save(S entity);

    @Override
    Flux<WorkOrder> findAll();

    @Override
    Mono<WorkOrder> findById(UUID id);

    @Override
    Mono<Void> deleteById(UUID id);

    @Modifying
    @Query("UPDATE  work_order set is_active = false WHERE manufacture_order_id = :id")
    Mono<Void> deleteByManufactureOrder(UUID id);

    @Query("update  work_order set last_updated = now() where work_item_id = :id")
    @Modifying
    Mono<Void> updateLastUpdatedByWorkItem(UUID id);

}

interface WorkOrderRepositoryInternal {
    <S extends WorkOrder> Mono<S> save(S entity);

    Flux<WorkOrder> findAllBy(Pageable pageable);

    Flux<WorkOrder> findAll();

    Flux<WorkOrder> findAllByIsActive(Pageable pageable, Boolean isActive, String company, String department);

    Mono<WorkOrder> findById(UUID id);

    Mono<WorkOrder> findByIdAndIsActive(UUID id, Boolean isActive);

    Mono<WorkOrder> findByIdAndFilter(WorkOrderRO ro, UUID id, Boolean isActive);

    Flux<WorkOrder> findAllByManufactureOrderIdAndIsActive(UUID manufactureOrderId, Boolean isActive);

    Flux<WorkOrder> findAllByMoIdAndStatuses(ManufactureWorkOrdersRO moRO, UUID manufactureOrderId, Boolean isActive);

    Mono<Long> countIsActive(Boolean isActive);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<WorkOrder> findAllBy(Pageable pageable, Criteria criteria);
}
