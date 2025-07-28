package com.masi.logistics.repository;

import com.masi.logistics.domain.VatRate;
import com.masi.logistics.domain.criteria.VatRateCriteria;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the VatRate entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VatRateRepository extends ReactiveCrudRepository<VatRate, UUID>, VatRateRepositoryInternal {
    Flux<VatRate> findAllBy(Pageable pageable);

    @Override
    <S extends VatRate> Mono<S> save(S entity);

    @Override
    Flux<VatRate> findAll();

    @Override
    Mono<VatRate> findById(UUID id);

    @Override
    Mono<Void> deleteById(UUID id);
}

interface VatRateRepositoryInternal {
    <S extends VatRate> Mono<S> save(S entity);

    Flux<VatRate> findAllBy(Pageable pageable);

    Flux<VatRate> findAll();

    Mono<VatRate> findById(UUID id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<VatRate> findAllBy(Pageable pageable, Criteria criteria);
    Flux<VatRate> findByCriteria(VatRateCriteria criteria, Pageable pageable);

    Mono<Long> countByCriteria(VatRateCriteria criteria);
}
