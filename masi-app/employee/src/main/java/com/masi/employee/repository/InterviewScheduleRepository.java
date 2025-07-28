package com.masi.employee.repository;

import com.masi.employee.domain.InterviewSchedule;

import java.util.Collection;
import java.util.UUID;

import com.masi.employee.domain.enumeration.InterviewProcess;
import com.masi.employee.domain.enumeration.InterviewResult;
import com.masi.employee.service.dto.InterviewScheduleQuery;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the InterviewSchedule entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InterviewScheduleRepository extends ReactiveCrudRepository<InterviewSchedule, UUID>, InterviewScheduleRepositoryInternal {
    Flux<InterviewSchedule> findAllBy(Pageable pageable);

    @Query("SELECT * FROM interview_schedule entity WHERE entity.recruitment_request_id = :id")
    Flux<InterviewSchedule> findByRecruitmentRequest(UUID id);

    @Query("SELECT * FROM interview_schedule entity WHERE entity.recruitment_request_id IS NULL")
    Flux<InterviewSchedule> findAllWhereRecruitmentRequestIsNull();

    @Override
    <S extends InterviewSchedule> Mono<S> save(S entity);

    @Override
    Flux<InterviewSchedule> findAll();

    @Override
    Mono<InterviewSchedule> findById(UUID id);

    @Override
    Mono<Void> deleteById(UUID id);

    Mono<Long> countByRecruitmentRequestIdAndIsDeletedIsFalseAndInterviewResult(UUID recruitmentRequestId, InterviewResult result);

    Mono<Integer> countByRecruitmentRequestIdAndIsDeletedIsFalse(UUID recruitmentRequestId);


    Flux<InterviewSchedule> findByRecruitmentRequestId(UUID id);

    Flux<InterviewSchedule> findAllByRecruitmentRequestIdInAndIsDeletedIsFalse(Collection<UUID> recruitmentRequestId);

    @Query("SELECT * FROM interview_schedule entity WHERE entity.recruitment_request_id = :id AND process = :interviewProcess")
    Flux<InterviewSchedule> findByIdAndProcess(UUID id, InterviewProcess interviewProcess);
}

interface InterviewScheduleRepositoryInternal {
    <S extends InterviewSchedule> Mono<S> save(S entity);

    Flux<InterviewSchedule> findAllBy(Pageable pageable);

    Flux<InterviewSchedule> findAll();

    Mono<InterviewSchedule> findById(UUID id);

    Flux<InterviewSchedule> findByQuery(InterviewScheduleQuery query, Pageable pageable);

    Mono<Long> countByQuery(InterviewScheduleQuery query);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<InterviewSchedule> findAllBy(Pageable pageable, Criteria criteria);
}
