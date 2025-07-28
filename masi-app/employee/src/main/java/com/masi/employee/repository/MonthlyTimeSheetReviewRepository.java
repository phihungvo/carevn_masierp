package com.masi.employee.repository;

import com.masi.employee.domain.MonthlyTimeSheetReview;

import java.time.LocalDate;
import java.util.UUID;

import com.masi.employee.domain.enumeration.TimeKeepingType;
import com.masi.employee.domain.enumeration.WorkspaceType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the MonthlyTimeSheetReview entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MonthlyTimeSheetReviewRepository
    extends ReactiveCrudRepository<MonthlyTimeSheetReview, UUID>, MonthlyTimeSheetReviewRepositoryInternal {
    Flux<MonthlyTimeSheetReview> findAllBy(Pageable pageable);

    @Override
    <S extends MonthlyTimeSheetReview> Mono<S> save(S entity);

    @Override
    Flux<MonthlyTimeSheetReview> findAll();

    @Override
    Mono<MonthlyTimeSheetReview> findById(UUID id);

    @Override
    Mono<Void> deleteById(UUID id);
}

interface MonthlyTimeSheetReviewRepositoryInternal {
    <S extends MonthlyTimeSheetReview> Mono<S> save(S entity);

    Flux<MonthlyTimeSheetReview> findAllBy(Pageable pageable);

    Flux<MonthlyTimeSheetReview> findAll();

    Mono<MonthlyTimeSheetReview> findById(UUID id);

    Mono<MonthlyTimeSheetReview> findByMonth(LocalDate month, WorkspaceType type, TimeKeepingType timeKeepingType);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<MonthlyTimeSheetReview> findAllBy(Pageable pageable, Criteria criteria);
}
