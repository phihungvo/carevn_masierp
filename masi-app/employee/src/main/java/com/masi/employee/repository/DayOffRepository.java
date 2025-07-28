package com.masi.employee.repository;

import com.masi.employee.domain.DayOff;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.sql.LockMode;
import org.springframework.data.relational.repository.Lock;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the DayOff entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DayOffRepository extends ReactiveCrudRepository<DayOff, UUID>, DayOffRepositoryInternal {
    Flux<DayOff> findAllBy(Pageable pageable);

    @Override
    <S extends DayOff> Mono<S> save(S entity);

    @Override
    Flux<DayOff> findAll();

    @Override
    Mono<DayOff> findById(UUID id);

    @Override
    Mono<Void> deleteById(UUID id);

    Mono<DayOff> findOneByEmployeeIdAndAndIsActive(UUID employeeId, Boolean isActive);

    @Modifying
    @Query("UPDATE day_off SET number_days_off = :numberOfDayOff WHERE id = :id")
    Mono<Void> setNumberOfDayOff(UUID id, float numberOfDayOff);

    @Modifying
    @Query("UPDATE day_off SET use_days_off = :numberOfDayOff WHERE id = :id")
    Mono<Void> setUseDayOff(UUID id, float numberOfDayOff);


    @Query("UPDATE day_off SET is_active = false WHERE annual_leave = :annualLeave")
    Mono<Void> resetDayOffEmployee(UUID annualLeave);


    @Query("SELECT * " +
        "FROM day_off " +
        "WHERE employee_id IN (:employeeIds) " +
        "  AND is_active = :isActive")
    Flux<DayOff> findAllByEmployeeIdInAndIsActive(List<UUID> employeeIds, boolean isActive);
    @Modifying
    @Query("DELETE FROM day_off WHERE employee_id = :employeeId")
    Mono<Void> removeDayOffByEmployeeId(UUID employeeId);

}

interface DayOffRepositoryInternal {
    <S extends DayOff> Mono<S> save(S entity);

    Flux<DayOff> findAllBy(Pageable pageable);

    Flux<DayOff> findAll();

    Mono<DayOff> findById(UUID id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<DayOff> findAllBy(Pageable pageable, Criteria criteria);
}
