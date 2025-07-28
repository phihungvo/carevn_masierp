package com.masi.employee.repository;

import com.masi.employee.domain.ProcessLeaveRegimeRequest;

import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the ProcessLeaveRegimeRequest entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProcessLeaveRegimeRequestRepository
    extends ReactiveCrudRepository<ProcessLeaveRegimeRequest, UUID>, ProcessLeaveRegimeRequestRepositoryInternal {
    Flux<ProcessLeaveRegimeRequest> findAllBy(Pageable pageable);

    @Override
    <S extends ProcessLeaveRegimeRequest> Mono<S> save(S entity);

    @Override
    Flux<ProcessLeaveRegimeRequest> findAll();

    Flux<ProcessLeaveRegimeRequest> findAllByLeaveRegimeRequestIdAndIsDeletedIsFalse(UUID leaveRegimeRequestId);

    @Override
    Mono<ProcessLeaveRegimeRequest> findById(UUID id);

    Mono<ProcessLeaveRegimeRequest> findByLeaveRegimeRequestIdAndStatusAndIsDeletedIsFalseAndApproverId(
        UUID leaveRegimeRequestId, String status, UUID approverId);

    Flux<ProcessLeaveRegimeRequest> findByLeaveRegimeRequestIdAndIsDeletedIsFalse(UUID leaveRegimeRequestId);

    @Override
    Mono<Void> deleteById(UUID id);
}

interface ProcessLeaveRegimeRequestRepositoryInternal {
    <S extends ProcessLeaveRegimeRequest> Mono<S> save(S entity);

    Flux<ProcessLeaveRegimeRequest> findAllBy(Pageable pageable);

    Flux<ProcessLeaveRegimeRequest> findAll();

    Mono<ProcessLeaveRegimeRequest> findById(UUID id);

    Flux<ProcessLeaveRegimeRequest> findByLeaveRegimeRequestIdAndIsDeletedIsFalseQuery(UUID leaveRegimeRequestId);
    // this is not supported at the moment because of
    // https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<ProcessLeaveRegimeRequest> findAllBy(Pageable pageable, Criteria
    // criteria);
}
