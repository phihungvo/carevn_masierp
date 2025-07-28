package com.masi.logistics.repository;

import com.masi.logistics.domain.DeliveryDetail;
import com.masi.logistics.domain.criteria.DeliveryDetailCriteria;

import java.time.LocalDate;
import java.util.UUID;

import com.masi.logistics.service.dto.DeliveryDetailCalendarDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the DeliveryDetail entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DeliveryDetailRepository extends ReactiveCrudRepository<DeliveryDetail, UUID>, DeliveryDetailRepositoryInternal {
    Flux<DeliveryDetail> findAllBy(Pageable pageable);

    @Override
    <S extends DeliveryDetail> Mono<S> save(S entity);

    @Override
    Flux<DeliveryDetail> findAll();

    @Override
    Mono<DeliveryDetail> findById(UUID id);

    @Override
    Mono<Void> deleteById(UUID id);

    @Modifying
    @Query("Delete from delivery_detail where delivery_id = :deliveryId")
    Mono<Void> deleteByDeliveryId(UUID deliveryId);

    Flux<DeliveryDetail> findAllByDeliveryId(UUID deliveryId);

    @Query("""
        select
            dd.id,
            COALESCE(i.id, null) as item_id,
            COALESCE(i."name", null) as item_name,
            i.code as item_code,
            COALESCE(s.id, null) as supplier_id,
            s.code as supplier_code,
            COALESCE(s."name", null) as supplier_name,
            sc.contract_code,
            sc.id as contract_id,
            COALESCE(scd.quantity, 0) as contract_quantity,
            scd.id as contract_detail_id,
            COALESCE(sum(dd.actual_quantity), 0) as received,
            COALESCE(scd.quantity - sum(dd.actual_quantity), 0) as remain,
            dd.actual_delivery_date,
            dd.note,
            dd.address,
            coalesce ((
                scd.quantity - sum(dd.quantity )
            ), 0) as planing_import,
            (
                select sum(coalesce(difference_quantity, 0))
                from delivery_detail
                where contract_material_id = dd.contract_material_id
                and supplier_contract_id = dd.supplier_contract_id
                and delivery_date <= CURRENT_DATE
            ) as outstanding_quantity,
            delivery_date,
            COALESCE(dd.quantity, 0) as expected_quantity,
            dd.actual_quantity,
            dd.difference_quantity,
            dd.created_at
        from
            delivery_detail dd
        left join supplier_contract_detail scd on
            dd.supplier_contract_id = scd.supplier_contract_id
            and scd.supply_item_id = dd.contract_material_id
            and scd.id = dd.contract_detail_id
        left join supplier_contract sc
        on sc.id = scd.supplier_contract_id
        and scd.is_deleted = false
        left join item i on
            i.id = dd.contract_material_id
        left join suppliers s on
            s.id = i.supplier_id
        and s.delete_at is null
    where (dd.delivery_date >= :startDate or :startDate is null) and (dd.delivery_date <= :endDate or :endDate is null)
        AND dd.company = :company
        group by
            dd.id,
            i.id,
            s.id,
            sc.contract_code,
            sc.id,
            scd.id,
            scd.quantity,
            delivery_date,
            dd.created_at,
            dd.quantity,
            dd.actual_quantity,
            dd.actual_delivery_date,
            dd.note,
            dd.address
        order by dd.created_at desc;
    """)
    Flux<DeliveryDetailCalendarDTO> getDeliveryDetailCalendar(LocalDate startDate, LocalDate endDate, String company);

    @Query("SELECT COUNT(supplier_contract_id) from delivery_detail where supplier_contract_id = :supplierContractId")
    Mono<Long> countBySupplierContractId(UUID supplierContractId);

}

interface DeliveryDetailRepositoryInternal {
    <S extends DeliveryDetail> Mono<S> save(S entity);

    Flux<DeliveryDetail> findAllBy(Pageable pageable);

    Flux<DeliveryDetail> findAll();

    Mono<DeliveryDetail> findById(UUID id);

    Mono<DeliveryDetail> findByIdAndCompany(UUID id, String company);

    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<DeliveryDetail> findAllBy(Pageable pageable, Criteria criteria);
    Flux<DeliveryDetail> findByCriteria(DeliveryDetailCriteria criteria, Pageable pageable);

    Mono<Long> countByCriteria(DeliveryDetailCriteria criteria);
}
