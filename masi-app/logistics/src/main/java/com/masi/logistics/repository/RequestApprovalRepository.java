package com.masi.logistics.repository;

import com.masi.logistics.domain.RequestApproval;
import com.masi.logistics.domain.criteria.RequestApprovalCriteria;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.List;
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

    @Query("SELECT * FROM request_approval entity WHERE entity.document_id = :documentId AND entity.is_deleted = false AND entity.deleted_by IS NULL AND entity.deleted_at IS NULL")
    Flux<RequestApproval> findAllByDocumentIdAndIsDeletedIsFalse(UUID documentId);

    @Query("SELECT * FROM request_approval entity WHERE entity.document_id In (:documentIds) AND entity.is_deleted = false AND entity.deleted_by IS NULL AND entity.deleted_at IS NULL")
    Flux<RequestApproval> findAllByDocumentIdsAndIsDeletedIsFalse(List<UUID> documentIds);

    Mono<RequestApproval> findFirstByDocumentIdAndEmployeeId(UUID documentId, UUID employeeId);

    Mono<RequestApproval> findFirstByDocumentIdAndEmployeeIdAndIsDeleted(UUID documentId, UUID employeeId, Boolean isDeleted);

    @Override
    Mono<Void> deleteById(UUID id);

    Flux<RequestApproval> findByDocumentId(UUID documentId);

    Flux<RequestApproval> findByDocumentIdAndIsDeleted(UUID documentId, Boolean isDeleted);

    @Query("SELECT * FROM request_approval entity WHERE entity.document_id = :documentId AND entity.is_deleted = :isDeleted AND entity.deleted_by IS NULL AND entity.deleted_at IS NULL AND entity.group_request = :group")
    Flux<RequestApproval> findByDocumentIdAndIsDeletedAndGroup(UUID documentId, Boolean isDeleted, String group);


    @Query("SELECT * FROM request_approval entity WHERE entity.document_id IN (:documentIds) AND entity.is_deleted = false AND entity.deleted_by IS NULL AND entity.deleted_at IS NULL")
    Flux<RequestApproval> findByDocumentIdIn(Collection<UUID> documentIds);

    Flux<RequestApproval> findAllByDocumentIdInAndIsDeleted(Collection<UUID> documentIds, Boolean isDeleted);


    Mono<Long> countByDocumentIdAndResult(UUID documentId, Boolean result);

    @Modifying
    @Query("UPDATE request_approval SET is_deleted = true, deleted_at = NOW(), deleted_by = :deletedBy WHERE document_id = :id AND company = :company")
    Mono<Void> deleteAllByDocumentIdAndCompany(UUID id, String company, String deletedBy);

    @Modifying
    @Query("UPDATE request_approval SET is_deleted = true, deleted_at = NOW(), deleted_by = :deletedBy WHERE document_id = :id AND group_request = :group AND company = :company")
    Mono<Void> deleteAllByDocumentIdAndGroupAndCompany(UUID id, String group, String deletedBy, String company);

    @Query("UPDATE request_approval SET is_deleted = true, deleted_at = NOW() WHERE document_id = :id AND is_deleted = false")
    Mono<Void> deleteAllByDocumentId(UUID id);

    @Modifying
    @Query("UPDATE request_approval SET approved_sign = NULL , approved_sign_name = NULL, reject_note = NULL, result = NULL WHERE document_id = :id AND company = :company AND deleted_by IS NULL AND deleted_at IS NULL AND is_deleted = false")
    Mono<Void> removeResultByDocumentIdAndCompanyAndDeleted(UUID id, String company);

    @Query("UPDATE request_approval SET approved_sign = NULL , approved_sign_name = NULL, reject_note = NULL, result = NULL WHERE document_id = :id AND is_deleted = false")
    Mono<Void> updateCleanAgain(UUID documentId);
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
