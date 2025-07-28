package com.masi.employee.repository;

import com.masi.employee.domain.TimeKeeping;

import java.sql.Time;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.masi.employee.domain.enumeration.TimeKeepingType;
import com.masi.employee.domain.enumeration.WorkspaceType;
import com.masi.employee.service.dto.LeaveDayReportQuery;
import com.masi.employee.service.dto.TimekeepingQuery;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.core.Local;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

/**
 * Spring Data R2DBC repository for the TimeKeeping entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TimeKeepingRepository extends ReactiveCrudRepository<TimeKeeping, UUID>, TimeKeepingRepositoryInternal {
    @Query("SELECT * FROM time_keeping entity WHERE entity.employee_id = :id")
    Flux<TimeKeeping> findByEmployee(UUID id);

    @Query("SELECT * FROM time_keeping entity WHERE entity.employee_id IS NULL")
    Flux<TimeKeeping> findAllWhereEmployeeIsNull();

    @Query("SELECT * FROM time_keeping entity WHERE entity.id not in (select violation_id from time_keeping_violation)")
    Flux<TimeKeeping> findAllWhereViolationIsNull();

    @Override
    <S extends TimeKeeping> Mono<S> save(S entity);

    Flux<TimeKeeping> findAllByIdIn(Collection<UUID> id);

    @Query("SELECT * FROM time_keeping entity WHERE entity.date = :date  and entity.employee_id = :employeeId and entity.type = :type ORDER BY entity.date DESC LIMIT 1")
    Mono<TimeKeeping> findByEmployeeIdAndDate(UUID employeeId, TimeKeepingType type, LocalDate date);

    @Override
    Flux<TimeKeeping> findAll();

    @Override
    Mono<TimeKeeping> findById(UUID id);

    @Override
    Mono<Void> deleteById(UUID id);


    @Query("SELECT * FROM time_keeping entity WHERE entity.employee_id=:employeeId and  entity.date BETWEEN :startDate AND :endDate and  entity.leave_day_type is not null")
    Flux<TimeKeeping> findByEmployeeIdOfAndDateIn(UUID employeeId, LocalDate startDate, LocalDate endDate);

//    CALL update_timekeeping_timesheets();

    @Query("CALL update_timekeeping_timesheets();")
    Mono<Void> updateAllTimekeepingTimesheets();

    Mono<TimeKeeping> findByEmployeeIdAndDateAndTimeKeepingType(UUID employeeId, LocalDate date, TimeKeepingType timeKeepingType);

    @Query("SELECT * FROM time_keeping entity WHERE entity.employee_id = :employeeId AND entity.date BETWEEN :startDate AND :endDate ORDER BY entity.created_at DESC LIMIT :#{#pageable.pageSize} OFFSET :#{#pageable.offset} ")
    Flux<TimeKeeping> findAllByEmployeeAndDateBetween(UUID employeeId, LocalDate startDate, LocalDate endDate, Pageable pageable);


    /*
        @Query("SELECT * FROM time_keeping entity WHERE entity.employee_id = :employeeId AND entity.date BETWEEN :startDate AND :endDate and type =:type ORDER BY entity.created_at DESC")
    */
    @Query("""
                    SELECT tk.*, COALESCE(SUM(esd.completion_percent), 0) AS completion_percent
                    FROM time_keeping tk
                    LEFT JOIN employee_shift_detail esd ON tk.id = esd.time_keeping_id
                    WHERE tk.employee_id = :employeeId
                      AND tk.date BETWEEN :startDate AND :endDate
                      AND tk.type = :type
                    GROUP BY tk.id
                    ORDER BY tk.created_at DESC
            """)
    Flux<TimeKeeping> findAllByEmployeeAndDateBetweenWithoutPaginate(UUID employeeId, LocalDate startDate, LocalDate endDate, TimeKeepingType type);

    @Query(("SELECT COUNT(*) FROM time_keeping entity WHERE entity.employee_id = :employeeId AND entity.date BETWEEN :startDate AND :endDate"))
    Mono<Long> countAllByEmployeeAndDateBetween(UUID employeeId, LocalDate startDate, LocalDate endDate);

    @Query(("SELECT COUNT(*) FROM time_keeping entity WHERE entity.date BETWEEN :startDate AND :endDate"))
    Mono<Long> countAllByDateBetween(UUID employeeId, LocalDate startDate, LocalDate endDate);

    //personal_monthly_timesheet_id
    @Query("UPDATE time_keeping SET locked = true, last_updated_at = now() WHERE personal_monthly_timesheet_id = :personalMonthlyTimesheetId")
    Mono<Void> lockAllByMonthlyTimesheetId(UUID personalMonthlyTimesheetId);

    Flux<TimeKeeping> findAllByIsDayOffAndDateBetween(Boolean isDayOff, LocalDate startDate, LocalDate endDate);

    @Query("""
                 select e.full_name as full_name,
                                e.employee_code as employee_id,
                                date_part('month', tk.date) as month,
                                sum(tk.value) as value,
                                e.contract_date as join_date
                                from employee_profile e
                                left join (select time_keeping.date,
                                                       case
                                                           WHEN leave_type = 'UNPAID_LEAVE' THEN 0
                                                           when leave_day_type= 'HALF_DAY' then 0.5
                                                           when leave_day_type= 'FULL_DAY' then 1
                                                           else 0
                                                       end as value,
                                           time_keeping.employee_id
                                               from time_keeping
                                               where (leave_day_type is not null
                                    or leave_day_type != '') and time_keeping.date::date between :startDate and :endDate
                                              ) as tk
                                on e.id = tk.employee_id
                                left   join  workspace w on e.workspace_id = w.id
                                where e.company = :company and w.workspace_type = :workspaceType
                                and e.contract_date <= :endDate
                                GROUP BY e.full_name,  e.id, e.contract_date, month
                                order by e.full_name
            """)
    Flux<LeaveDayReportQuery> getLeaveDayReport(LocalDate startDate, LocalDate endDate, String company, WorkspaceType workspaceType);


    @Query("""

                   SELECT e.employee_id,
                          SUM(CASE\s
                                WHEN e.leave_type = 'UNPAID_LEAVE' THEN 0
                                WHEN e.leave_type = 'COMPENSATION_LEAVE' THEN 0
                                WHEN e.leave_day_type = 'FULL_DAY' THEN 1
                                WHEN e.leave_day_type = 'HALF_DAY' THEN 0.5
                                ELSE 0\s
                              END) AS total_hours
                   FROM time_keeping e
                   WHERE e.employee_id IN (:ids)
                     AND EXTRACT(YEAR FROM e.created_at) = EXTRACT(YEAR FROM CURRENT_DATE)
                   GROUP BY e.employee_id;
            """)
    Flux<EmployeeHoursDTO> countByListEmployee(List<UUID> ids);


}

interface TimeKeepingRepositoryInternal {
    <S extends TimeKeeping> Mono<S> save(S entity);

    Flux<TimeKeeping> findAllBy(Pageable pageable);

    Flux<TimeKeeping> findAll();

    Mono<TimeKeeping> findById(UUID id);

    Flux<TimeKeeping> findAllByDateBetween(TimekeepingQuery query);

    Flux<TimeKeeping> findAllByDate(LocalDate date, Pageable pageable);

    Mono<Long> countByDateBetween(TimekeepingQuery query);

    Mono<Long> countByDate(LocalDate date);

    Flux<TimeKeeping> findAllByPersonalMonthlyTimesheetId(UUID id);

    Flux<TimeKeeping> findAllWhereNotHaveRecord(LocalDate startDate, LocalDate endDate);


}
