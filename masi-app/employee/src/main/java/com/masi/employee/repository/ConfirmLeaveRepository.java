package com.masi.employee.repository;

import com.masi.employee.domain.ConfirmLeave;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the ConfirmLeave entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ConfirmLeaveRepository extends ReactiveCrudRepository<ConfirmLeave, UUID>, ConfirmLeaveRepositoryInternal {
    Flux<ConfirmLeave> findAllBy(Pageable pageable);

    @Override
    <S extends ConfirmLeave> Mono<S> save(S entity);

    @Override
    Flux<ConfirmLeave> findAll();

    @Override
    Mono<ConfirmLeave> findById(UUID id);

    @Override
    Mono<Void> deleteById(UUID id);
}

interface ConfirmLeaveRepositoryInternal {
    <S extends ConfirmLeave> Mono<S> save(S entity);

    Flux<ConfirmLeave> findAllBy(Pageable pageable);

    Flux<ConfirmLeave> findAll();

    Mono<ConfirmLeave> findById(UUID id);

    Mono<ConfirmLeave> findWithDetailById(UUID id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<ConfirmLeave> findAllBy(Pageable pageable, Criteria criteria);
}
