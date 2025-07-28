package com.masi.logistics.repository;

import com.masi.logistics.domain.Factories;
import com.masi.logistics.domain.criteria.FactoriesCriteria;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the Factories entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FactoriesRepository extends ReactiveCrudRepository<Factories, UUID>, FactoriesRepositoryInternal {
    Flux<Factories> findAllBy(Pageable pageable);

    @Override
    <S extends Factories> Mono<S> save(S entity);

    @Override
    Flux<Factories> findAll();

    @Override
    Mono<Factories> findById(UUID id);

    @Override
    Mono<Void> deleteById(UUID id);
}

interface FactoriesRepositoryInternal {
    <S extends Factories> Mono<S> save(S entity);

    Flux<Factories> findAllBy(Pageable pageable);

    Flux<Factories> findAll();

    Mono<Factories> findById(UUID id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Factories> findAllBy(Pageable pageable, Criteria criteria);
    Flux<Factories> findByCriteria(FactoriesCriteria criteria, Pageable pageable);

    Mono<Long> countByCriteria(FactoriesCriteria criteria);
}
