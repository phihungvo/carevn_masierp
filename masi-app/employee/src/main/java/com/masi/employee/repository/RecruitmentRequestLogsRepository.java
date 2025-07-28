package com.masi.employee.repository;

import com.masi.employee.domain.RecruitmentRequestLogs;
import com.masi.employee.service.dto.RecruitmentChangeLogsDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RecruitmentRequestLogsRepository extends ReactiveCrudRepository<RecruitmentRequestLogs, UUID> {

    <S extends RecruitmentRequestLogs> Mono<S> save(S entity);

    @Query("SELECT COUNT(*) FROM recruitment_request_logs WHERE recruitment_request_id = :recruitmentRequestId")
    Mono<Long> countAllByRecruitmentRequestId(UUID recruitmentRequestId);

    // Tìm tất cả log theo recruitment_request_id, sắp xếp theo change_date giảm dần
    @Query("SELECT * FROM recruitment_request_logs WHERE recruitment_request_id = :recruitmentRequestId ORDER BY change_date ASC ")
    Flux<RecruitmentRequestLogs> findAllByRecruitmentRequestIdOrderByChangeDateAsc(UUID recruitmentRequestId, Pageable pageable);
}
