package com.masi.employee.repository;

import com.masi.employee.domain.RecruitmentReviewRequest;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.masi.employee.domain.RecruitmentReviewRequestFull;
import com.masi.employee.service.dto.RecruitmentRequestDTO;
import org.reactivestreams.Publisher;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the RecruitmentReviewRequest entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RecruitmentReviewRequestRepository
    extends ReactiveCrudRepository<RecruitmentReviewRequest, UUID>, RecruitmentReviewRequestRepositoryInternal {
    Flux<RecruitmentReviewRequest> findAllBy(Pageable pageable);

    @Override
    <S extends RecruitmentReviewRequest> Mono<S> save(S entity);

    @Override
    Flux<RecruitmentReviewRequest> findAll();

    @Override
    Mono<RecruitmentReviewRequest> findById(UUID id);

    @Override
    Mono<Void> deleteById(UUID id);


    Flux<RecruitmentReviewRequest> findAllByRequestIdAndResultAndIsDeletedIsFalse(UUID recruitmentRequestId, boolean result);

    Mono<RecruitmentReviewRequest> findFirstByRequestIdOrderByCreatedDateDesc(UUID recruitmentRequestId);

    Mono<RecruitmentReviewRequest> findFirstByRequestIdAndCreatedDateBefore(UUID recruitmentRequestId, ZonedDateTime createdDate);

    @Query("UPDATE recruitment_review_request SET is_deleted = true WHERE request_id = :recruitmentRequestId and is_deleted = false")
    Mono<Void> deleteByRequestId(UUID recruitmentRequestId);

    // format DD/MM/YYYY HH:mm:ss in postgres
    @Query("UPDATE recruitment_review_request SET is_deleted = false WHERE request_id = :recruitmentRequestId and  to_char(created_date, 'DD/MM/YYYY HH24:MI:SS') = :createDate and is_deleted = true")
    Mono<Void> recoverWhereCreateDateEqual(UUID recruitmentRequestId, String createDate);

    Mono<RecruitmentReviewRequest> findByRequestIdAndResultAndIsDeletedIsFalse(UUID recruitmentRequestId, boolean result);

    Mono<RecruitmentReviewRequest> findByRequestIdAndPosition(UUID recruitmentRequestId, String position);

    @Query("UPDATE recruitment_review_request SET result = :result WHERE id = :id AND is_deleted = false")
    Mono<Void> updateStatus(UUID id, boolean result);

    @Query("UPDATE recruitment_review_request SET result = :result WHERE request_id = :id AND is_deleted = false")
    Mono<Void> approveRefusalRecruitmentAll(UUID id, boolean result);

    @Query("UPDATE recruitment_review_request SET result = false,updated_at=now(), reject_note = :reject_note WHERE request_id = :id AND result = true AND is_deleted = false")
    Mono<Void> approveRefusalRecruitment(UUID id, String rejectNote);

    @Query("UPDATE recruitment_review_request SET result = false, approval_sign_file = :approvalSign  WHERE id = :id AND is_deleted = false")
    Mono<Void> approveConsentRecruitment(UUID id, String approvalSign);

    @Query("UPDATE recruitment_review_request SET is_deleted = true WHERE request_id = :id")
    Mono<Void> approveConsentRecruitments(UUID id);


    @Query("SELECT r.*, e.full_name AS employee_name " +
        "FROM recruitment_review_request r " +
        "JOIN employee_profile e ON r.employee_id = e.id " +
        "WHERE r.request_id = :requestId AND r.is_deleted = false")
    Flux<RecruitmentReviewRequestFull> findByRequestId(UUID requestId);

    @Query("SELECT r.* from recruitment_review_request r where r.request_id = :requestId AND r.is_deleted = false")
    Flux<RecruitmentReviewRequest> findAllByRequestId(UUID requestId);

    @Query("SELECT r.*, e.full_name AS employee_name " +
        "FROM recruitment_review_request r " +
        "JOIN employee_profile e ON r.employee_id = e.id " +
        "WHERE r.request_id = :requestId AND r.is_deleted = false AND r.result = :result")
    Flux<RecruitmentReviewRequestFull> findByRequestIdAndResults(UUID requestId, Boolean result);

    @Query("SELECT r.*, e.full_name AS employee_name " +
        "FROM recruitment_review_request r " +
        "JOIN employee_profile e ON r.employee_id = e.id " +
        "WHERE r.request_id IN (:requestIds) AND r.is_deleted = false")
    Flux<RecruitmentReviewRequestFull> findByRequestIds(List<UUID> requestIds);


    @Query("""
        SELECT * FROM recruitment_review_request
        WHERE employee_id = :idEmployee
        AND ((result = true AND is_deleted = false AND position != 'REVIEWER')
        OR (position = 'REVIEWER' AND result = false AND is_deleted = false ))
        """)
    Flux<RecruitmentReviewRequest> findListCheckEmployee(UUID idEmployee);

    @Modifying
    @Query("UPDATE recruitment_review_request SET result = :result,updated_at = now(), approval_sign_file=:sign WHERE employee_id = :userId and request_id =:requestId AND is_deleted = false")
    Mono<Integer> updateResultById(UUID userId, UUID requestId, String sign, boolean result);
}

interface RecruitmentReviewRequestRepositoryInternal {
    <S extends RecruitmentReviewRequest> Mono<S> save(S entity);

    Flux<RecruitmentReviewRequest> findAllBy(Pageable pageable);

    Flux<RecruitmentReviewRequest> findAll();

    Mono<RecruitmentReviewRequest> findById(UUID id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<RecruitmentReviewRequest> findAllBy(Pageable pageable, Criteria criteria);
}
