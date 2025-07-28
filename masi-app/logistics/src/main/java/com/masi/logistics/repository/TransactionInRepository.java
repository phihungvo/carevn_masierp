package com.masi.logistics.repository;

import com.masi.logistics.domain.TransactionIn;
import com.masi.logistics.domain.criteria.TransactionInCriteria;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the TransactionIn entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TransactionInRepository extends ReactiveCrudRepository<TransactionIn, UUID>, TransactionInRepositoryInternal {
    Flux<TransactionIn> findAllBy(Pageable pageable);

    @Override
    <S extends TransactionIn> Mono<S> save(S entity);

    @Override
    Flux<TransactionIn> findAll();

    @Override
    Mono<TransactionIn> findById(UUID id);

    @Override
    Mono<Void> deleteById(UUID id);
}

interface TransactionInRepositoryInternal {
    <S extends TransactionIn> Mono<S> save(S entity);

    Flux<TransactionIn> findAllBy(Pageable pageable);

    Flux<TransactionIn> findAll();

    Mono<TransactionIn> findById(UUID id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<TransactionIn> findAllBy(Pageable pageable, Criteria criteria);
    Flux<TransactionIn> findByCriteria(TransactionInCriteria criteria, Pageable pageable);

    Mono<Long> countByCriteria(TransactionInCriteria criteria);
}
