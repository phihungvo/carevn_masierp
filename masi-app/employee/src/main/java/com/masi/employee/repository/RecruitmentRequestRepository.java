package com.masi.employee.repository;

import com.masi.employee.domain.RecruitmentRequest;

import java.time.LocalDate;
import java.util.UUID;

import com.masi.employee.service.dto.RecruitmentRequestRO;
import com.masi.employee.service.mapper.ExplanationReviewMapper;
import com.masi.employee.service.reports.RecruitmentQuantityQuery;
import com.masi.employee.service.reports.RecruitmentReport;
import com.masi.employee.service.reports.RecruitmentReportQuery;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the RecruitmentRequest entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RecruitmentRequestRepository
    extends ReactiveCrudRepository<RecruitmentRequest, UUID>, RecruitmentRequestRepositoryInternal {
    Flux<RecruitmentRequest> findAllBy(Pageable pageable);

    @Override
    <S extends RecruitmentRequest> Mono<S> save(S entity);

    @Override
    Flux<RecruitmentRequest> findAll();

    @Override
    Mono<RecruitmentRequest> findById(UUID id);

    @Override
    Mono<Void> deleteById(UUID id);

    @Query("""
        SELECT * FROM
        (

        SELECT ic.interview_result , rr.position from interview_schedule ic join recruitment_request rr on ic.recruitment_request_id = rr.id
        where rr.created_date >= :from and rr.created_date <= :to and company = :company
        UNION ALL

        SELECT ic.interview_result, rr.job_title as "position" from interview_schedule ic join recruitment_request rr on ic.recruitment_request_id = rr.id
        where rr.created_date >= :from and rr.created_date <= :to and company = :company
                                                               ) as irpirp
                """)
    Flux<RecruitmentReportQuery> getReport(LocalDate from, LocalDate to, String company);


    @Query("""
                    SELECT count(distinct irpirp.position) FROM
                    (

                    SELECT ic.interview_result , rr.position from interview_schedule ic join recruitment_request rr on ic.recruitment_request_id = rr.id
                    where rr.created_date >= :from and rr.created_date <= :to and company = :company
                    UNION ALL

                    SELECT ic.interview_result, rr.job_title as "position" from interview_schedule ic join recruitment_request rr on ic.recruitment_request_id = rr.id
                    where rr.created_date >= :from and rr.created_date <= :to and company = :company
                                                                           ) as irpirp
        """)
    Mono<Long> countReport(LocalDate from, LocalDate to, String company);


    @Query("""
            SELECT rr.position,sum(rr.quantity) as total_request FROM recruitment_request rr
                                                                 where rr.created_date >= :from and rr.created_date <= :to and company = :company
                                                                 GROUP BY rr.position
            UNION ALL
            SELECT rr.job_title,sum(rr.quantity) as total_request FROM recruitment_request rr
                                                                  where rr.created_date >= :from and rr.created_date <= :to and company = :company
                                                                  GROUP BY rr.job_title
            """)
    Flux<RecruitmentQuantityQuery> getRecruitmentQuantity(LocalDate from, LocalDate to, String company);

    @Query("UPDATE recruitment_request SET status = :status WHERE id = :idRecruitment ")
    Mono<Void> updateStatusByid(UUID idRecruitment, String status);
}

interface RecruitmentRequestRepositoryInternal {
    <S extends RecruitmentRequest> Mono<S> save(S entity);

    Flux<RecruitmentRequest> findAllBy(Pageable pageable);

    Flux<RecruitmentRequest> findAll();

    Mono<RecruitmentRequest> findByIdAndNotDelete(UUID id);

    Flux<RecruitmentRequest> findAllByFilter(RecruitmentRequestRO moRO, Pageable pageable);


    Mono<Long> countAllByFilter(RecruitmentRequestRO moRO);

    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<RecruitmentRequest> findAllBy(Pageable pageable, Criteria criteria);
}
