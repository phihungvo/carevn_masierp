package com.masi.sale.repository;

import com.masi.sale.domain.DeliveryDetail;
import com.masi.sale.domain.criteria.DeliveryDetailCriteria;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import com.masi.sale.service.dto.DeliveryDetailCalendarDTO;
import org.springframework.data.domain.Pageable;
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

    @Query("SELECT * FROM delivery_detail WHERE delivery_id IN (:uuids)")
    Flux<DeliveryDetail> findAllByDeliveryIdIn(List<UUID> uuids);

    @Query("""
        select
           dd.id,
           m.id as item_id,
           m.item_id as logistic_item,
            m."name" as item_name,
          cus.id as customer_id,
          cus.customer_code as customer_code,
          cus.company_name as customer_name,
           mo.id as order_id,
           dd.address,
           dd.note,
           mo.order_code,
           c.id as contract_id,
           dd.quantity as expected_quantity,
           cm.quantity as contract_quantity,
          COALESCE(sum(dd.actual_quantity), 0) as received,
          dd.actual_quantity as actual_quantity,
           dd.actual_delivery_date,
           (dd.quantity - dd.actual_quantity) as remain,
           coalesce (
           (cast(cm.quantity as bigint) - sum(dd.quantity)),
           0
               ) as planing_import,
           (
               select sum(coalesce(difference_quantity, 0))
               from delivery_detail
               where contract_material_id = dd.contract_material_id
               and order_id = dd.order_id
               and delivery_date <= CURRENT_DATE
           ) as outstanding_quantity,
           c.contract_name,
               dd.created_at,
           dd.delivery_date
               from
           delivery_detail dd
               left join masi_order mo on
           mo.id = dd.order_id AND dd.company = :company
               left join contract_material cm on
           cm.order_id = mo.id and dd.contract_material_id = cm.id_material
               join contract c on
           cm.id_contract = c.id
               left join material m on
           m.id = dd.contract_material_id
           left join customer cus on cus.id = c.customer_id
         where (cast(dd.delivery_date as DATE) >= cast(:startDate as date) or :startDate is null) and (cast(dd.delivery_date as date) <= cast(:endDate as date) or :endDate is null)
            AND dd.deleted_at is null
       group by
           dd.id,
           mo.id,
           m.id,
           cus.id,
           cus.customer_code,
           cus.company_name,
           m."name",
        dd.address,
           dd.note,
           c.id,
           dd.quantity,
           cm.quantity ,
           dd.actual_quantity,
           dd.actual_delivery_date,
           m.item_id
       order by
          dd.created_at desc
    """)
    Flux<DeliveryDetailCalendarDTO> getDeliveryDetailCalendar(ZonedDateTime startDate, ZonedDateTime endDate, String company);
}

interface DeliveryDetailRepositoryInternal {
    <S extends DeliveryDetail> Mono<S> save(S entity);

    Flux<DeliveryDetail> findAllBy(Pageable pageable);

    Flux<DeliveryDetail> findAll();

    Mono<DeliveryDetail> findById(UUID id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<DeliveryDetail> findAllBy(Pageable pageable, Criteria criteria);
    Flux<DeliveryDetail> findByCriteria(DeliveryDetailCriteria criteria, Pageable pageable);

    Mono<Long> countByCriteria(DeliveryDetailCriteria criteria);
}
