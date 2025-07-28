package com.masi.employee.repository;

import com.masi.employee.domain.TimeKeepingRecord;

import java.time.LocalDate;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the TimeKeepingRecord entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TimeKeepingRecordRepository extends ReactiveCrudRepository<TimeKeepingRecord, UUID>, TimeKeepingRecordRepositoryInternal {
    @Query("SELECT * FROM time_keeping_record entity WHERE entity.employee_id = :id")
    Flux<TimeKeepingRecord> findByEmployee(UUID id);

    @Query("SELECT * FROM time_keeping_record entity WHERE entity.employee_id IS NULL")
    Flux<TimeKeepingRecord> findAllWhereEmployeeIsNull();

    @Override
    <S extends TimeKeepingRecord> Mono<S> save(S entity);

//    @Override
//    Mono<TimeKeepingRecord> findById(UUID id);

    @Override
    Mono<Void> deleteById(UUID id);

    @Query("SELECT * FROM time_keeping_record entity WHERE entity.employee_id = :employeeId AND DATE(entity.check_in) = :date LIMIT :#{#pageable.pageSize} OFFSET :#{#pageable.offset}")
    Flux<TimeKeepingRecord> findByEmployeeAndDate(UUID employeeId, LocalDate date, Pageable pageable);

    @Query("SELECT * FROM time_keeping_record entity WHERE entity.employee_id = :employeeId AND DATE(entity.check_in) = :date")
    Flux<TimeKeepingRecord> findByEmployeeAndDateNoPagination(UUID employeeId, LocalDate date);

    @Query("SELECT * FROM time_keeping_record entity WHERE entity.employee_id = :employeeId AND DATE(entity.check_in) BETWEEN :startDate AND :endDate LIMIT :#{#pageable.pageSize} OFFSET :#{#pageable.offset}")
    Flux<TimeKeepingRecord> findByEmployeeAndDateBetween(UUID employeeId, LocalDate startDate, LocalDate endDate, Pageable pageable);
}

interface TimeKeepingRecordRepositoryInternal {
    <S extends TimeKeepingRecord> Mono<S> save(S entity);

    Flux<TimeKeepingRecord> findAllBy(Pageable pageable);

    Flux<TimeKeepingRecord> findAllByRangeDate(LocalDate startDate, LocalDate endDate, Pageable pageable);

//    Mono<TimeKeepingRecord> findById(UUID id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<TimeKeepingRecord> findAllBy(Pageable pageable, Criteria criteria);
}
