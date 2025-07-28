package com.masi.employee.repository;

import com.masi.employee.domain.ExplanationReview;
import java.util.UUID;

import com.masi.employee.service.dto.ExplanationRequestObjectBase;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the ExplanationReview entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ExplanationReviewRepository extends ReactiveCrudRepository<ExplanationReview, UUID>, ExplanationReviewRepositoryInternal {
    @Query("SELECT * FROM explanation_review entity WHERE entity.reviewer_id = :id")
    Flux<ExplanationReview> findByReviewer(UUID id);

    @Query("SELECT * FROM explanation_review entity WHERE entity.reviewer_id IS NULL")
    Flux<ExplanationReview> findAllWhereReviewerIsNull();

    @Query("SELECT * FROM explanation_review entity WHERE entity.explanation_id = :id")
    Flux<ExplanationReview> findByExplanation(UUID id);

    @Query("SELECT * FROM explanation_review entity WHERE entity.explanation_id IS NULL")
    Flux<ExplanationReview> findAllWhereExplanationIsNull();

    @Override
    <S extends ExplanationReview> Mono<S> save(S entity);

    @Override
    Flux<ExplanationReview> findAll();

    @Override
    Mono<ExplanationReview> findById(UUID id);

    Mono<ExplanationReview> findByIdAndIsActive(UUID id, Boolean isActive);

    @Override
    Mono<Void> deleteById(UUID id);

    Flux<ExplanationReview> findByExplanationId(UUID id);
}

interface ExplanationReviewRepositoryInternal {
    <S extends ExplanationReview> Mono<S> save(S entity);

    Flux<ExplanationReview> findAllBy(Pageable pageable);

    Flux<ExplanationReview> findAllBy(ExplanationRequestObjectBase reviewRO, Pageable pageable);

    Flux<ExplanationReview> findAll();

    Mono<ExplanationReview> findById(UUID id);

    Flux<ExplanationReview> findAllByExplanationId(UUID id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<ExplanationReview> findAllBy(Pageable pageable, Criteria criteria);
}
