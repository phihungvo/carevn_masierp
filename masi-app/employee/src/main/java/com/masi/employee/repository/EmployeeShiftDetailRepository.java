package com.masi.employee.repository;

import com.masi.employee.domain.EmployeeShiftDetail;
import com.masi.employee.domain.criteria.EmployeeShiftDetailCriteria;

import java.time.LocalDate;
import java.util.Collection;
import java.util.UUID;

import com.masi.employee.domain.enumeration.TimeKeepingViolationType;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the EmployeeShiftDetail entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EmployeeShiftDetailRepository
        extends ReactiveCrudRepository<EmployeeShiftDetail, UUID>, EmployeeShiftDetailRepositoryInternal {
    Flux<EmployeeShiftDetail> findAllBy(Pageable pageable);

    @Override
    <S extends EmployeeShiftDetail> Mono<S> save(S entity);

    @Override
    Flux<EmployeeShiftDetail> findAll();

    @Override
    Mono<EmployeeShiftDetail> findById(UUID id);

    @Override
    Mono<Void> deleteById(UUID id);

    @Query("""
                UPDATE employee_shift_detail SET is_deleted = true
                WHERE shift_id IN (:shiftIds) AND date = :date
            """)
    Mono<Integer> deleteAllByShiftIdInAndDate(Collection<UUID> shiftIds, LocalDate date);

    Flux<EmployeeShiftDetail> findAllByEmployeeIdAndDateAndIsDeleted(UUID employeeId, LocalDate date, Boolean isDeleted);


    @Query("""
                SELECT entity.time_keeping_id,
                       SUM(entity.completion_percent) AS completion_percent
                FROM employee_shift_detail entity
                WHERE entity.time_keeping_id IN (:timekeepingIds)
                GROUP BY entity.time_keeping_id
            """)
    Flux<EmployeeShiftDetail> findAllByTimeKeepingIdAndGroupBy(Collection<UUID> timekeepingIds);

//    @Query("""
//            SELECT e.*
//            FROM employee_shift_detail e
//            LEFT JOIN shift s ON e.shift_id = s.id
//            WHERE e.time_keeping_id = :timeKeepingId
//            ORDER BY s.hour_start_time ASC;
//                        """)
//    Flux<EmployeeShiftDetail> findByTimeKeepingId(UUID timeKeepingId);

    @Query("""
            SELECT e.*
            FROM employee_shift_detail e
            LEFT JOIN shift s ON e.shift_id = s.id
            WHERE e.date = :date
            ORDER BY s.hour_start_time ASC;
                        """)
    Flux<EmployeeShiftDetail> findByTimeKeepingId(LocalDate date);

    @Query("""
                       
            SELECT e.*
            FROM employee_shift_detail e
            WHERE e.violation_type IS NOT NULL AND e.violation_type != '';
                                          
                        """)
    Flux<EmployeeShiftDetail> findAllByIsVioLationType();

    @Query("""
                SELECT esd.shift_id
                FROM employee_shift_detail esd
                WHERE esd.shift_id IN (:listIdShifts)
                AND esd.date = :dateCheck
                GROUP BY esd.shift_id
            """)
    Flux<EmployeeShiftDetail> findAllByShiftIdInAndDate(Collection<UUID> listIdShifts, LocalDate dateCheck);

    @Query("""
                SELECT e.*
                FROM employee_shift_detail e
                WHERE 
                 e.date BETWEEN :startDate AND :endDate
            """)
    Flux<EmployeeShiftDetail> findAllByIsViolationType(LocalDate startDate, LocalDate endDate);

    @Query("UPDATE employee_shift_detail SET violation_id = :violationId, violation_type = :type  WHERE id = :employeeShiftId")
    Mono<Void> updateIdViolation(UUID employeeShiftId, UUID violationId, TimeKeepingViolationType type);

    @Query("""
                SELECT e.*
                FROM employee_shift_detail e
                WHERE e.employee_id IN (:listEmployeeId)
                AND e.date BETWEEN :dateFrom AND :dateTo
                AND e.is_deleted = :isDeleted
            """)
    Flux<EmployeeShiftDetail> findAllByEmployeeIdAndDateFromAndDateToAndIsDeleted(Collection<UUID> listEmployeeId, LocalDate dateFrom, LocalDate dateTo, boolean isDeleted);
}

interface EmployeeShiftDetailRepositoryInternal {
    <S extends EmployeeShiftDetail> Mono<S> save(S entity);

    Flux<EmployeeShiftDetail> findAllBy(Pageable pageable);

    Flux<EmployeeShiftDetail> findAll();

    Mono<EmployeeShiftDetail> findById(UUID id);

    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<EmployeeShiftDetail> findAllBy(Pageable pageable, Criteria criteria);
    Flux<EmployeeShiftDetail> findByCriteria(EmployeeShiftDetailCriteria criteria, Pageable pageable);

    Mono<Long> countByCriteria(EmployeeShiftDetailCriteria criteria);
}
