package com.masi.employee.repository;

import com.masi.employee.domain.TimeKeepingExplanation;
import java.util.UUID;
import com.masi.employee.domain.TimeKeepingViolation;
import com.masi.employee.service.dto.ExplanationRO;
import com.masi.employee.service.dto.TimeKeepingExplanationFilter;
import com.masi.employee.service.dto.TimeKeepingViolationFilter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the TimeKeepingExplanation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TimeKeepingExplanationRepository
    extends ReactiveCrudRepository<TimeKeepingExplanation, UUID>, TimeKeepingExplanationRepositoryInternal {
    @Query("SELECT * FROM time_keeping_explanation entity WHERE entity.employee_id = :id")
    Flux<TimeKeepingExplanation> findByEmployee(UUID id);

    @Query("SELECT * FROM time_keeping_explanation entity WHERE entity.employee_id IS NULL")
    Flux<TimeKeepingExplanation> findAllWhereEmployeeIsNull();

    @Override
    <S extends TimeKeepingExplanation> Mono<S> save(S entity);

    @Override
    Flux<TimeKeepingExplanation> findAll();

    @Override
    Mono<TimeKeepingExplanation> findById(UUID id);

    @Override
    Mono<Void> deleteById(UUID id);
}

interface TimeKeepingExplanationRepositoryInternal {
    <S extends TimeKeepingExplanation> Mono<S> save(S entity);

    Flux<TimeKeepingExplanation> findAllBy(Pageable pageable);

    Flux<TimeKeepingExplanation> findAllByIsActive(Boolean isActive, Pageable pageable);

    Flux<TimeKeepingExplanation> findAll();

    Mono<TimeKeepingExplanation> findById(UUID id);

    Mono<TimeKeepingExplanation> findByIdAndIsActive(UUID id, Boolean isActive);

    Mono<Long> countAllByIsActive(Boolean isActive);

    Mono<Long> countAllByFilter(ExplanationRO filter);

    Flux<TimeKeepingExplanation> findAllByFilter(Pageable pageable, ExplanationRO filter);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<TimeKeepingExplanation> findAllBy(Pageable pageable, Criteria criteria);
}
