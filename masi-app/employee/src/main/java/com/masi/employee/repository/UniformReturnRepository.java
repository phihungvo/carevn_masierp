package com.masi.employee.repository;

import com.masi.employee.domain.Uniform;
import com.masi.employee.domain.UniformReturn;

import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the UniformReturn entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UniformReturnRepository extends ReactiveCrudRepository<UniformReturn, UUID>, UniformReturnRepositoryInternal {
    Flux<UniformReturn> findAllBy(Pageable pageable);

    @Override
    <S extends UniformReturn> Mono<S> save(S entity);

    @Override
    Flux<UniformReturn> findAll();

    @Override
    Mono<UniformReturn> findById(UUID id);

    @Override
    Mono<Void> deleteById(UUID id);

    // find not delete uniform return by uniform release id
    Flux<UniformReturn> findAllByUniformReleaseIdAndCompanyAndDeleteAtIsNull(UUID uniformReleaseId, String company);
}

interface UniformReturnRepositoryInternal {
    <S extends UniformReturn> Mono<S> save(S entity);

    Flux<UniformReturn> findAllBy(Pageable pageable);

    Flux<UniformReturn> findAll();

    Mono<UniformReturn> findById(UUID id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<UniformReturn> findAllBy(Pageable pageable, Criteria criteria);

    Flux<UniformReturn> findByUniformReleaseId(UUID uniformReleaseId, String company, List<Uniform> uniformIds);
}
