package com.masi.logistics.repository;

import com.masi.logistics.domain.Currency;
import com.masi.logistics.domain.criteria.CurrencyCriteria;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the Currency entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CurrencyRepository extends ReactiveCrudRepository<Currency, UUID>, CurrencyRepositoryInternal {
    Flux<Currency> findAllBy(Pageable pageable);

    @Override
    <S extends Currency> Mono<S> save(S entity);

    @Override
    Flux<Currency> findAll();

    @Override
    Mono<Currency> findById(UUID id);

    @Override
    Mono<Void> deleteById(UUID id);
}

interface CurrencyRepositoryInternal {
    <S extends Currency> Mono<S> save(S entity);

    Flux<Currency> findAllBy(Pageable pageable);

    Flux<Currency> findAll();

    Mono<Currency> findById(UUID id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Currency> findAllBy(Pageable pageable, Criteria criteria);
    Flux<Currency> findByCriteria(CurrencyCriteria criteria, Pageable pageable);

    Mono<Long> countByCriteria(CurrencyCriteria criteria);
}
