package com.masi.logistics.repository;

import com.masi.logistics.domain.Uom;

import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the Uom entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UomRepository extends ReactiveCrudRepository<Uom, UUID>, UomRepositoryInternal {
    Flux<Uom> findAllBy(Pageable pageable);

    @Override
    <S extends Uom> Mono<S> save(S entity);

    @Override
    Flux<Uom> findAll();

    @Override
    Mono<Uom> findById(UUID id);

    @Override
    Mono<Void> deleteById(UUID id);

    Flux<Uom> findAllByIdIn(List<UUID> ids);

    Flux<Uom> findAllByIdInAndCompanyAndDeleteAtIsNullAndDeleteByIsNull(List<UUID> ids, String company);
}

interface UomRepositoryInternal {
    <S extends Uom> Mono<S> save(S entity);

    Flux<Uom> findAllBy(Pageable pageable);

    Flux<Uom> findAll();

    Mono<Uom> findById(UUID id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Uom> findAllBy(Pageable pageable, Criteria criteria);
}
