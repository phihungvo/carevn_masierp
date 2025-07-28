package com.masi.production.repository;

import com.masi.production.domain.Factory;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the Factory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FactoryRepository extends ReactiveCrudRepository<Factory, UUID>, FactoryRepositoryInternal {
    Flux<Factory> findAllBy(Pageable pageable);

    @Override
    <S extends Factory> Mono<S> save(S entity);

    @Override
    Flux<Factory> findAll();

    @Override
    Mono<Factory> findById(UUID id);

    @Override
    Mono<Void> deleteById(UUID id);

    Mono<Factory> findByName(String name);
}

interface FactoryRepositoryInternal {
    <S extends Factory> Mono<S> save(S entity);

    Flux<Factory> findAllBy(Pageable pageable);

    Flux<Factory> findAll();

    Mono<Factory> findById(UUID id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Factory> findAllBy(Pageable pageable, Criteria criteria);
}
