package com.masi.logistics.repository;

import com.masi.logistics.domain.DeliverySchedule;
import com.masi.logistics.domain.criteria.DeliveryScheduleCriteria;

import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Modifying;
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

    Mono<DeliverySchedule> findByIdAndDeletedAtIsNull(UUID id);

    @Override
    Mono<Void> deleteById(UUID id);

}

interface DeliveryScheduleRepositoryInternal {
    <S extends DeliverySchedule> Mono<S> save(S entity);

    Flux<DeliverySchedule> findAllBy(Pageable pageable);

    Flux<DeliverySchedule> findAll();

    Mono<DeliverySchedule> findById(UUID id);

    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<DeliverySchedule> findAllBy(Pageable pageable, Criteria criteria);
    Flux<DeliverySchedule> findByCriteria(DeliveryScheduleCriteria criteria, Pageable pageable);

    Mono<Long> countByCriteria(DeliveryScheduleCriteria criteria);
}
