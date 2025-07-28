package com.masi.logistics.repository;

import com.masi.logistics.domain.PaymentRequest;
import com.masi.logistics.domain.criteria.PaymentRequestCriteria;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the PaymentRequest entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PaymentRequestRepository extends ReactiveCrudRepository<PaymentRequest, UUID>, PaymentRequestRepositoryInternal {
    Flux<PaymentRequest> findAllBy(Pageable pageable);

    @Override
    <S extends PaymentRequest> Mono<S> save(S entity);

    @Override
    Flux<PaymentRequest> findAll();

    @Override
    Mono<PaymentRequest> findById(UUID id);

    @Override
    Mono<Void> deleteById(UUID id);
}

interface PaymentRequestRepositoryInternal {
    <S extends PaymentRequest> Mono<S> save(S entity);

    Flux<PaymentRequest> findAllBy(Pageable pageable);

    Flux<PaymentRequest> findAll();

    Mono<PaymentRequest> findById(UUID id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<PaymentRequest> findAllBy(Pageable pageable, Criteria criteria);
    Flux<PaymentRequest> findByCriteria(PaymentRequestCriteria criteria, Pageable pageable);

    Mono<Long> countByCriteria(PaymentRequestCriteria criteria);
}
