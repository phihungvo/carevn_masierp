package com.masi.production.repository;

import com.masi.production.domain.SampleDisposal;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the SampleDisposal entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SampleDisposalRepository extends ReactiveCrudRepository<SampleDisposal, UUID>, SampleDisposalRepositoryInternal {
    Flux<SampleDisposal> findAllBy(Pageable pageable);

    @Query("SELECT * FROM sample_disposal entity WHERE entity.id not in (select sample_id from quality_check_sample)")
    Flux<SampleDisposal> findAllWhereSampleIsNull();

    @Override
    <S extends SampleDisposal> Mono<S> save(S entity);

    @Override
    Flux<SampleDisposal> findAll();

    @Override
    Mono<SampleDisposal> findById(UUID id);

    @Override
    Mono<Void> deleteById(UUID id);
}

interface SampleDisposalRepositoryInternal {
    <S extends SampleDisposal> Mono<S> save(S entity);

    Flux<SampleDisposal> findAllBy(Pageable pageable);

    Flux<SampleDisposal> findAll();

    Mono<SampleDisposal> findById(UUID id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<SampleDisposal> findAllBy(Pageable pageable, Criteria criteria);
}
