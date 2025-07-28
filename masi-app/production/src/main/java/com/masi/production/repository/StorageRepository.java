package com.masi.production.repository;

import com.masi.production.domain.Storage;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the Storage entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StorageRepository extends ReactiveCrudRepository<Storage, UUID>, StorageRepositoryInternal {
    Flux<Storage> findAllBy(Pageable pageable);

    @Override
    <S extends Storage> Mono<S> save(S entity);

    @Override
    Flux<Storage> findAll();

    @Override
    Mono<Storage> findById(UUID id);

    @Override
    Mono<Void> deleteById(UUID id);

    Mono<Storage> findByName(String name);

}

interface StorageRepositoryInternal {
    <S extends Storage> Mono<S> save(S entity);

    Flux<Storage> findAllBy(Pageable pageable);

    Flux<Storage> findAll();

    Mono<Storage> findById(UUID id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Storage> findAllBy(Pageable pageable, Criteria criteria);
}
