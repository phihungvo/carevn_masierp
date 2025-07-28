package com.masi.logistics.repository;

import com.masi.logistics.domain.TransactionOut;
import com.masi.logistics.domain.criteria.TransactionOutCriteria;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the TransactionOut entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TransactionOutRepository extends ReactiveCrudRepository<TransactionOut, UUID>, TransactionOutRepositoryInternal {
    Flux<TransactionOut> findAllBy(Pageable pageable);

    @Override
    <S extends TransactionOut> Mono<S> save(S entity);

    @Override
    Flux<TransactionOut> findAll();

    @Override
    Mono<TransactionOut> findById(UUID id);

    @Override
    Mono<Void> deleteById(UUID id);
}

interface TransactionOutRepositoryInternal {
    <S extends TransactionOut> Mono<S> save(S entity);

    Flux<TransactionOut> findAllBy(Pageable pageable);

    Flux<TransactionOut> findAll();

    Mono<TransactionOut> findById(UUID id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<TransactionOut> findAllBy(Pageable pageable, Criteria criteria);
    Flux<TransactionOut> findByCriteria(TransactionOutCriteria criteria, Pageable pageable);

    Mono<Long> countByCriteria(TransactionOutCriteria criteria);
}
