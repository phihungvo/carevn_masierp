package com.masi.employee.repository;

import com.masi.employee.domain.LeaveRequestReview;
import java.util.UUID;

import com.masi.employee.service.dto.LeaveRequestRequestObjectBase;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the LeaveRequestReview entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LeaveRequestReviewRepository
    extends ReactiveCrudRepository<LeaveRequestReview, UUID>, LeaveRequestReviewRepositoryInternal {
    @Query("SELECT * FROM leave_request_review entity WHERE entity.leave_request_id = :id")
    Flux<LeaveRequestReview> findByLeaveRequest(UUID id);

    @Query("SELECT * FROM leave_request_review entity WHERE entity.leave_request_id IS NULL")
    Flux<LeaveRequestReview> findAllWhereLeaveRequestIsNull();

    @Override
    <S extends LeaveRequestReview> Mono<S> save(S entity);

    @Override
    Flux<LeaveRequestReview> findAll();

    @Override
    Mono<LeaveRequestReview> findById(UUID id);

    @Override
    Mono<Void> deleteById(UUID id);


    Flux<LeaveRequestReview> findAllByLeaveRequestIdAndIsActive(UUID leaveRequestId, Boolean isActive);
}

interface LeaveRequestReviewRepositoryInternal {
    <S extends LeaveRequestReview> Mono<S> save(S entity);

    Flux<LeaveRequestReview> findAllBy(Pageable pageable);

    Flux<LeaveRequestReview> findAllBy(LeaveRequestRequestObjectBase reviewRO, Pageable pageable);

    Flux<LeaveRequestReview> findAll();

    Mono<LeaveRequestReview> findById(UUID id);

    Flux<LeaveRequestReview> findAllByLeaveRequestId(UUID leaveRequestId);

    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<LeaveRequestReview> findAllBy(Pageable pageable, Criteria criteria);
}
