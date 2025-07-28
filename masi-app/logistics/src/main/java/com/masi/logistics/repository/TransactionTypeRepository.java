package com.masi.logistics.repository;

import com.masi.logistics.domain.TransactionType;
import com.masi.logistics.domain.criteria.TransactionTypeCriteria;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the TransactionType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TransactionTypeRepository extends ReactiveCrudRepository<TransactionType, UUID>, TransactionTypeRepositoryInternal {
    Flux<TransactionType> findAllBy(Pageable pageable);

    @Override
    <S extends TransactionType> Mono<S> save(S entity);

    @Override
    Flux<TransactionType> findAll();

    @Override
    Mono<TransactionType> findById(UUID id);

    @Override
    Mono<Void> deleteById(UUID id);
}

interface TransactionTypeRepositoryInternal {
    <S extends TransactionType> Mono<S> save(S entity);

    Flux<TransactionType> findAllBy(Pageable pageable);

    Flux<TransactionType> findAll();

    Mono<TransactionType> findById(UUID id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<TransactionType> findAllBy(Pageable pageable, Criteria criteria);
    Flux<TransactionType> findByCriteria(TransactionTypeCriteria criteria, Pageable pageable);

    Mono<Long> countByCriteria(TransactionTypeCriteria criteria);
}
