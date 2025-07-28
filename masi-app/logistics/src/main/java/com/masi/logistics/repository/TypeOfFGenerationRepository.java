package com.masi.logistics.repository;

import com.masi.logistics.domain.TypeOfFGeneration;
import com.masi.logistics.domain.criteria.TypeOfFGenerationCriteria;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the TypeOfFGeneration entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TypeOfFGenerationRepository extends ReactiveCrudRepository<TypeOfFGeneration, UUID>, TypeOfFGenerationRepositoryInternal {
    Flux<TypeOfFGeneration> findAllBy(Pageable pageable);

    @Override
    <S extends TypeOfFGeneration> Mono<S> save(S entity);

    @Override
    Flux<TypeOfFGeneration> findAll();

    @Override
    Mono<TypeOfFGeneration> findById(UUID id);

    @Override
    Mono<Void> deleteById(UUID id);
}

interface TypeOfFGenerationRepositoryInternal {
    <S extends TypeOfFGeneration> Mono<S> save(S entity);

    Flux<TypeOfFGeneration> findAllBy(Pageable pageable);

    Flux<TypeOfFGeneration> findAll();

    Mono<TypeOfFGeneration> findById(UUID id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<TypeOfFGeneration> findAllBy(Pageable pageable, Criteria criteria);
    Flux<TypeOfFGeneration> findByCriteria(TypeOfFGenerationCriteria criteria, Pageable pageable);

    Mono<Long> countByCriteria(TypeOfFGenerationCriteria criteria);
}
