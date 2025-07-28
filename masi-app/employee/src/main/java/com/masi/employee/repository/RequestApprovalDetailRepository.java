package com.masi.employee.repository;

import com.masi.employee.domain.RequestApprovalDetail;
import com.masi.employee.domain.criteria.RequestApprovalDetailCriteria;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the RequestApprovalDetail entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RequestApprovalDetailRepository
    extends ReactiveCrudRepository<RequestApprovalDetail, UUID>, RequestApprovalDetailRepositoryInternal {
    Flux<RequestApprovalDetail> findAllBy(Pageable pageable);

    @Override
    <S extends RequestApprovalDetail> Mono<S> save(S entity);

    @Override
    Flux<RequestApprovalDetail> findAll();
    Flux<RequestApprovalDetail> findAllByDocumentId(UUID documentId);

    @Override
    Mono<RequestApprovalDetail> findById(UUID id);

    @Override
    Mono<Void> deleteById(UUID id);
}

interface RequestApprovalDetailRepositoryInternal {
    <S extends RequestApprovalDetail> Mono<S> save(S entity);

    Flux<RequestApprovalDetail> findAllBy(Pageable pageable);

    Flux<RequestApprovalDetail> findAll();

    Mono<RequestApprovalDetail> findById(UUID id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<RequestApprovalDetail> findAllBy(Pageable pageable, Criteria criteria);
    Flux<RequestApprovalDetail> findByCriteria(RequestApprovalDetailCriteria criteria, Pageable pageable);

    Mono<Long> countByCriteria(RequestApprovalDetailCriteria criteria);
}
