package com.masi.employee.repository;

import com.masi.employee.domain.EmployeeShift;
import com.masi.employee.domain.criteria.EmployeeShiftCriteria;

import java.util.Collection;

import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the EmployeeShift entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EmployeeShiftRepository extends ReactiveCrudRepository<EmployeeShift, UUID>, EmployeeShiftRepositoryInternal {
    Flux<EmployeeShift> findAllBy(Pageable pageable);

    @Override
    <S extends EmployeeShift> Mono<S> save(S entity);

    @Override
    Flux<EmployeeShift> findAll();

    @Override
    Mono<EmployeeShift> findById(UUID id);

    @Override
    Mono<Void> deleteById(UUID id);

    @Query("""
                SELECT DISTINCT ON (employee_id) *
                FROM employee_shift
                WHERE company = :company
                ORDER BY employee_id DESC;
            """)
    Flux<EmployeeShift> findAllDistinctByEmployeeId(String company);

    Flux<EmployeeShift> findAllByShiftIdIn(Collection<UUID> shiftIds);


    @Query("SELECT * FROM employee_shift WHERE shift_id = :id and is_deleted = false")
    Flux<EmployeeShift> findAllByShiftId(UUID id);

    @Query("SELECT * FROM employee_shift WHERE shift_id IN (:id) and is_deleted = false")
    Flux<EmployeeShift> findAllByShiftIds(Collection<UUID> id);

}

interface EmployeeShiftRepositoryInternal {
    <S extends EmployeeShift> Mono<S> save(S entity);

    Flux<EmployeeShift> findAllBy(Pageable pageable);

    Flux<EmployeeShift> findAll();

    Mono<EmployeeShift> findById(UUID id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<EmployeeShift> findAllBy(Pageable pageable, Criteria criteria);
    Flux<EmployeeShift> findByCriteria(EmployeeShiftCriteria criteria, Pageable pageable);

    Mono<Long> countByCriteria(EmployeeShiftCriteria criteria);
}
