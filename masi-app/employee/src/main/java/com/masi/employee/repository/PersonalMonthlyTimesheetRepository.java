package com.masi.employee.repository;

import com.masi.employee.domain.PersonalMonthlyTimesheet;
import com.masi.employee.domain.enumeration.TimeKeepingType;
import com.masi.employee.domain.enumeration.TimesheetReviewStatus;
import com.masi.employee.service.dto.PersonalMonthlyTimesheetQuery;

import java.time.LocalDate;
import java.util.Collection;
import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.sql.LockMode;
import org.springframework.data.relational.repository.Lock;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the PersonalMonthlyTimesheet entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PersonalMonthlyTimesheetRepository
    extends ReactiveCrudRepository<PersonalMonthlyTimesheet, UUID>, PersonalMonthlyTimesheetRepositoryInternal {
    Flux<PersonalMonthlyTimesheet> findAllBy(Pageable pageable);

    @Query("SELECT * FROM personal_monthly_timesheet entity WHERE entity.employee_id = :id")
    Flux<PersonalMonthlyTimesheet> findByEmployee(UUID id);

    @Query("SELECT * FROM personal_monthly_timesheet entity WHERE entity.employee_id IS NULL")
    Flux<PersonalMonthlyTimesheet> findAllWhereEmployeeIsNull();

    @Query("SELECT * FROM personal_monthly_timesheet entity WHERE entity.review_id = :id")
    Flux<PersonalMonthlyTimesheet> findByReview(UUID id);

    @Query("SELECT * FROM personal_monthly_timesheet entity WHERE entity.review_id IS NULL")
    Flux<PersonalMonthlyTimesheet> findAllWhereReviewIsNull();

    @Query("SELECT * FROM personal_monthly_timesheet entity WHERE entity.time_keepings_id = :id")
    Flux<PersonalMonthlyTimesheet> findByTimeKeepings(UUID id);

    @Query("SELECT * FROM personal_monthly_timesheet entity WHERE entity.time_keepings_id IS NULL")
    Flux<PersonalMonthlyTimesheet> findAllWhereTimeKeepingsIsNull();

    Flux<PersonalMonthlyTimesheet> findAllByIdIn(Collection<UUID> id);

    @Query("UPDATE personal_monthly_timesheet SET status = :status WHERE id = :id")
    Mono<Void> updateStatusById(UUID id, TimesheetReviewStatus status);

    Flux<PersonalMonthlyTimesheet> findAllByMonthAndTimeKeepingType(LocalDate month, TimeKeepingType type);

    Flux<PersonalMonthlyTimesheet> findAllByMonthAndTimeKeepingTypeAndEmployeeId(LocalDate month, TimeKeepingType type, UUID employeeId);

    @Override
    <S extends PersonalMonthlyTimesheet> Mono<S> save(S entity);

    @Override
    Flux<PersonalMonthlyTimesheet> findAll();

    @Override
    Mono<PersonalMonthlyTimesheet> findById(UUID id);

    @Override
    Mono<Void> deleteById(UUID id);


}

interface PersonalMonthlyTimesheetRepositoryInternal {
    <S extends PersonalMonthlyTimesheet> Mono<S> save(S entity);

    Flux<PersonalMonthlyTimesheet> findAllBy(Pageable pageable);

    Flux<PersonalMonthlyTimesheet> findAll();

    Mono<PersonalMonthlyTimesheet> findById(UUID id);

    Mono<PersonalMonthlyTimesheet> findByEmployeeIdAndMonthAndType(UUID employeeId, LocalDate month, TimeKeepingType type);

    Flux<PersonalMonthlyTimesheet> findAllByQuery(Pageable pageable, PersonalMonthlyTimesheetQuery query);

    Mono<Long> countAllByQuery(PersonalMonthlyTimesheetQuery query);

    // this is not supported at the moment because of
    // https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<PersonalMonthlyTimesheet> findAllBy(Pageable pageable, Criteria
    // criteria);
}
