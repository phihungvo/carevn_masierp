package com.masi.employee.repository;

import com.masi.employee.domain.LeaveDay;

import java.time.LocalDate;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the LeaveDay entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LeaveDayRepository extends ReactiveCrudRepository<LeaveDay, UUID>, LeaveDayRepositoryInternal {
    @Override
    <S extends LeaveDay> Mono<S> save(S entity);

    @Override
    Flux<LeaveDay> findAll();

    @Override
    Mono<LeaveDay> findById(UUID id);

    @Override
    Mono<Void> deleteById(UUID id);

    Flux<LeaveDay> findByEmployeeIdAndDateBetween(UUID employeeId, LocalDate startDate, LocalDate endDate);

    Mono<LeaveDay> findByEmployeeIdAndDate(UUID employeeId, LocalDate date);

}

interface LeaveDayRepositoryInternal {
    <S extends LeaveDay> Mono<S> save(S entity);

    Flux<LeaveDay> findAllBy(Pageable pageable);

    Flux<LeaveDay> findAll();

    Mono<LeaveDay> findById(UUID id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<LeaveDay> findAllBy(Pageable pageable, Criteria criteria);
}
