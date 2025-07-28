package com.masi.sale.repository;

import com.masi.sale.domain.RequestApproval;
import com.masi.sale.domain.query.RequestApprovalCriteria;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.UUID;

/**
 * Spring Data R2DBC repository for the RequestApproval entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RequestApprovalRepository extends ReactiveCrudRepository<RequestApproval, UUID>, RequestApprovalRepositoryInternal {
    Flux<RequestApproval> findAllBy(Pageable pageable);

    @Override
    <S extends RequestApproval> Mono<S> save(S entity);

    @Override
    Flux<RequestApproval> findAll();

    @Override
    Mono<RequestApproval> findById(UUID id);

    @Query("SELECT * FROM request_approval entity WHERE entity.document_id = :documentId and entity.employee_id = :employeeId and entity.type is null and is_deleted = false  order by created_date desc limit 1")
    Mono<RequestApproval> findFirstByDocumentIdAndEmployeeId(UUID documentId, UUID employeeId);

    Mono<RequestApproval> findFirstByDocumentIdAndEmployeeIdAndTypeAndIsDeletedIsFalse(UUID documentId, UUID employeeId, String type);

    @Override
    Mono<Void> deleteById(UUID id);

    @Query("update request_approval set is_deleted = true where document_id = :documentId and type = :type")
    Mono<Void> deleteAllByDocumentIdAndType(UUID documentId, String type);

    @Query("update request_approval set is_deleted = true where document_id = :documentId and type is null")
    Mono<Void> deleteAllByDocumentIdAndTypeIsNull(UUID documentId);


    Flux<RequestApproval> findByDocumentIdAndIsDeletedIsFalse(UUID documentId);

    Flux<RequestApproval> findByDocumentIdAndTypeAndIsDeletedIsFalse(UUID documentId, String type);

    Flux<RequestApproval> findByDocumentIdInAndTypeIsNullAndIsDeletedIsFalse(Collection<UUID> documentIds);

    Flux<RequestApproval> findByDocumentIdInAndTypeAndIsDeletedIsFalse(Collection<UUID> documentIds, String type);

    Mono<Long> countByDocumentIdAndResult(UUID documentId, Boolean result);
}

interface RequestApprovalRepositoryInternal {
    <S extends RequestApproval> Mono<S> save(S entity);

    Flux<RequestApproval> findAllBy(Pageable pageable);

    Flux<RequestApproval> findAll();

    Mono<RequestApproval> findById(UUID id);

    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<RequestApproval> findAllBy(Pageable pageable, Criteria criteria);
    Flux<RequestApproval> findByCriteria(RequestApprovalCriteria criteria, Pageable pageable);

    Mono<Long> countByCriteria(RequestApprovalCriteria criteria);
}
