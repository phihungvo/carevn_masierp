package com.masi.employee.repository;

import com.masi.employee.domain.LeaveRequest;

import java.time.LocalDate;
import java.util.UUID;

import com.masi.employee.service.dto.LeaveRequestFilter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the LeaveRequest entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LeaveRequestRepository extends ReactiveCrudRepository<LeaveRequest, UUID>, LeaveRequestRepositoryInternal {
    @Query("SELECT * FROM leave_request entity WHERE entity.employee_id = :id")
    Flux<LeaveRequest> findByEmployee(UUID id);

    @Query("SELECT * FROM leave_request entity WHERE entity.employee_id IS NULL")
    Flux<LeaveRequest> findAllWhereEmployeeIsNull();

    @Query("SELECT * FROM leave_request entity WHERE entity.sensor_id = :id")
    Flux<LeaveRequest> findBySensor(UUID id);

    @Query("SELECT * FROM leave_request entity WHERE entity.sensor_id IS NULL")
    Flux<LeaveRequest> findAllWhereSensorIsNull();

    @Override
    <S extends LeaveRequest> Mono<S> save(S entity);

    @Override
    Flux<LeaveRequest> findAll();

    @Override
    Mono<LeaveRequest> findById(UUID id);

    @Override
    Mono<Void> deleteById(UUID id);

    @Query("SELECT * FROM leave_request lr " +
            "WHERE lr.employee_id = :employeeId " +
            "AND :date >= lr.from_date " +
            "AND :date <= lr.to_date limit 1")
    Mono<LeaveRequest> findByEmployeeIdDateRangeInclude(UUID employeeId, LocalDate date);
}

interface LeaveRequestRepositoryInternal {
    <S extends LeaveRequest> Mono<S> save(S entity);

    Flux<LeaveRequest> findAllBy(Pageable pageable);

    Flux<LeaveRequest> findAll();

    Flux<LeaveRequest> findAllByIsActive(Boolean isActive, Pageable pageable);

    Mono<LeaveRequest> findByIdAndIsActive(UUID id, Boolean isActive);

    Mono<LeaveRequest> findById(UUID id);

    Mono<Long> countAllByIsActive(Boolean isActive);

    Mono<Long> countAllByFilter(LeaveRequestFilter filter);

    Flux<LeaveRequest> findAllByFilter(Pageable pageable, LeaveRequestFilter filter);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
//     Flux<LeaveRequest> findAllBy(Pageable pageable, Criteria criteria);
}
