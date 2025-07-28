package com.masi.sale.repository;

import com.masi.sale.domain.ContractMaterialFull;
import com.masi.sale.domain.DeliverySchedule;
import com.masi.sale.domain.criteria.DeliveryScheduleCriteria;

import java.util.List;
import java.util.UUID;

import com.masi.sale.service.dto.request.DeliveryScheduleRequest;
import com.masi.sale.service.mapper.ContractFileMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the DeliverySchedule entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DeliveryScheduleRepository extends ReactiveCrudRepository<DeliverySchedule, UUID>, DeliveryScheduleRepositoryInternal {
    Flux<DeliverySchedule> findAllBy(Pageable pageable);

    @Override
    <S extends DeliverySchedule> Mono<S> save(S entity);

    @Override
    Flux<DeliverySchedule> findAll();

    @Override
    Mono<DeliverySchedule> findById(UUID id);

    @Override
    Mono<Void> deleteById(UUID id);

//    Flux<DeliverySchedule> findByCriteria(DeliveryScheduleRequest criteria, Pageable pageable);

    @Query("SELECT * FROM delivery_schedule WHERE contract_id IN (:contractIds)")
    Flux<DeliverySchedule> findAllByContractIdIn(List<UUID> contractIds);

    @Query("SELECT * FROM delivery_schedule WHERE order_id IN (:orderIds)")
    Flux<DeliverySchedule> findAllByOrderIdIn(List<UUID> orderIds);

//    Mono<Long> countByCriteria(DeliveryScheduleRequest criteria);


}

interface DeliveryScheduleRepositoryInternal {
    <S extends DeliverySchedule> Mono<S> save(S entity);

    Flux<DeliverySchedule> findAllBy(Pageable pageable);

    Flux<DeliverySchedule> findAll();

    Mono<DeliverySchedule> findById(UUID id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<DeliverySchedule> findAllBy(Pageable pageable, Criteria criteria);

}
