package com.masi.employee.repository;

import com.masi.employee.domain.TimeKeepingViolation;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

import com.masi.employee.service.dto.TimeKeepingViolationFilter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the TimeKeepingViolation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TimeKeepingViolationRepository
    extends ReactiveCrudRepository<TimeKeepingViolation, UUID>, TimeKeepingViolationRepositoryInternal {
    @Query("SELECT * FROM time_keeping_violation entity WHERE entity.time_keeping_id = :id")
    Flux<TimeKeepingViolation> findByTimeKeeping(UUID id);


    @Query("""
          select  tkv.* from  time_keeping_violation tkv join time_keeping tk on tkv.time_keeping_id = tk.id
          where tk.date between :fromDate and :toDate and tk.employee_id = :employeeId
          and tkv.explanation_id is null
        """)
    Flux<TimeKeepingViolation> findAllByDateBetweenAndEmployeeId(LocalDate fromDate, LocalDate toDate, UUID employeeId);

    @Query("SELECT * FROM time_keeping_violation entity WHERE entity.time_keeping_id IS NULL")
    Flux<TimeKeepingViolation> findAllWhereTimeKeepingIsNull();

    @Override
    <S extends TimeKeepingViolation> Mono<S> save(S entity);

    @Override
    Flux<TimeKeepingViolation> findAll();

    Flux<TimeKeepingViolation> findByIdIn(Set<UUID> ids);

    Flux<TimeKeepingViolation> findAllByExplanationId(UUID explanationId);

    @Override
    Mono<TimeKeepingViolation> findById(UUID id);

    @Override
    Mono<Void> deleteById(UUID id);

    Mono<TimeKeepingViolation> findByTimeKeepingId(UUID timeKeepingId);

    Mono<Long> countAllByExplanationId(UUID explanationId);

    Flux<TimeKeepingViolation> findByExplanationId(UUID explanationId);

    Flux<TimeKeepingViolation> findAllByEmployeeId(UUID id);

    Flux<TimeKeepingViolation> deleteByTimeKeepingId(UUID timeKeepingId);
}

interface TimeKeepingViolationRepositoryInternal {
    <S extends TimeKeepingViolation> Mono<S> save(S entity);

    Flux<TimeKeepingViolation> findAllBy(Pageable pageable);

    Flux<TimeKeepingViolation> findAll();

    Mono<TimeKeepingViolation> findById(UUID id);

    Flux<TimeKeepingViolation> findAllByIsActive(Boolean isActive, Pageable pageable);

    Mono<TimeKeepingViolation> findByIdAndIsActive(UUID id, Boolean isActive);

    Mono<Long> countAllByIsActive(Boolean isActive);

    Mono<Long> countAllByFilter(TimeKeepingViolationFilter filter);
    Flux<TimeKeepingViolation> findAllByExplanationId(UUID explanationId, Pageable pageable);

    Flux<TimeKeepingViolation> findAllByFilter(Pageable pageable, TimeKeepingViolationFilter filter);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<TimeKeepingViolation> findAllBy(Pageable pageable, Criteria criteria);
}
