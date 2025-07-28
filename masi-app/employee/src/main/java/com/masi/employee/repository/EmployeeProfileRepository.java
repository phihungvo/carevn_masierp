package com.masi.employee.repository;

import com.masi.employee.domain.EmployeeProfile;
import com.masi.employee.domain.EmployeeProfileAndTimeKeeping;
import com.masi.employee.domain.ProfileAttachment;
import com.masi.employee.domain.enumeration.WorkspaceType;
import com.masi.employee.service.dto.EmployeeProfileQuery;
import com.masi.employee.service.dto.EmployeeProfileXlsx;
import com.masi.employee.service.reports.HumanResourceChangeReport;
import com.masi.employee.service.reports.UniformExpiringReport;

import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.List;

import java.time.LocalDate;

import java.util.UUID;

/**
 * Spring Data R2DBC repository for the EmployeeProfile entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EmployeeProfileRepository
        extends ReactiveCrudRepository<EmployeeProfile, UUID>, EmployeeProfileRepositoryInternal {
    Flux<EmployeeProfile> findAllBy(Pageable pageable);

    @Override
    <S extends EmployeeProfile> Mono<S> save(S entity);

    @Override
    Flux<EmployeeProfile> findAll();

    Flux<EmployeeProfile> findByIdIn(Collection<UUID> id);

//    @Query("SELECT * FROM  profile_attachment WHERE employee_profile_id IN (:ids)")

    //    @Query("""
//             SELECT e.*, t.check_in AS check_in
//                                   FROM employee_profile e
//                                   JOIN time_keeping_record t ON e.id = t.employee_id
//                                   WHERE
//                   								e.pin IN (:pins) AND
//                                      t.check_in >= '2024-10-15 13:46:57+00'
//                                     AND t.check_in <= '2024-10-17 13:46:57+00'
//                                     AND t.check_in = (
//                                         SELECT MAX(t2.check_in)
//                                         FROM time_keeping_record t2
//                                         WHERE t2.employee_id = e.id
//                                     )
//            """)
//    Flux<EmployeeProfileAndTimeKeeping> findByPinInAndTimeKeeping(Collection<String> pins,String formattedTimeStart, String formattedTimeEnd);

    @Query("""
                SELECT e.*, t.check_in AS check_in
                FROM employee_profile e
                LEFT JOIN time_keeping_record t ON e.id = t.employee_id
                  AND t.check_in >= CAST(:formattedTimeStart AS timestamp)
                  AND t.check_in <= CAST(:formattedTimeEnd AS timestamp)
                  AND t.check_in = (
                      SELECT MAX(t2.check_in)
                      FROM time_keeping_record t2
                      WHERE t2.employee_id = e.id
                  )
                WHERE e.pin IN (:pins)
            """)
    Flux<EmployeeProfileAndTimeKeeping> findByPinInAndTimeKeeping(Collection<String> pins, String formattedTimeStart, String formattedTimeEnd);


    @Query("SELECT * FROM employee_profile entity WHERE entity.is_deleted = false and entity.updated_at >= (:startDate) and entity.updated_at <= (:endDate)")
    Flux<EmployeeProfile> findAllByLastUpdate(String startDate, String endDate);

    @Override
    @Modifying
    @Query("UPDATE employee_profile SET is_deleted = true, updated_at = NOW() WHERE id = :id")
    Mono<Void> deleteById(UUID id);


//    @Query("UPDATE employee_profile SET probation_date_to = :date WHERE id = :id")
//    Mono<Void> test(UUID id,LocalDate date);

    // tìm theo CMND
    @Query("SELECT * FROM employee_profile entity WHERE entity.citizen_id = :idCard and entity.is_deleted = false and entity.company =:company limit 1")
    Mono<EmployeeProfile> findByCitizenId(String idCard, String company);

    // tìm theo tax
    @Query("SELECT * FROM employee_profile entity WHERE entity.tax_code = :taxId and entity.is_deleted = false and entity.company =:company limit 1")
    Mono<EmployeeProfile> findByTaxCode(String taxId, String company);

    // sbank-code
    @Query("SELECT * FROM employee_profile entity WHERE entity.bank_number = :bankCode and entity.is_deleted = false and entity.company =:company limit 1")
    Mono<EmployeeProfile> findByBankCode(String bankCode, String company);

    Flux<EmployeeProfile> findAllByContractEndDateBeforeAndContractEndDateAfterAndIsDeletedIsFalseAndCompany(
            LocalDate contractEndDate, LocalDate contractEndDate2, String company, Pageable pageable);

    Mono<Long> countAllByContractEndDateBeforeAndContractEndDateAfterAndIsDeletedIsFalseAndCompany(
            LocalDate contractEndDate, LocalDate contractEndDate2, String company);


    @Query("SELECT id FROM employee_profile entity WHERE entity.company = :company and (entity.employee_code = :newCode or entity.bank_number = :newBank or entity.tax_code = :newTax or entity.citizen_id = :newCitizen) and entity.is_deleted = false limit 1")
    Mono<UUID> isValidNewEmployee(String newCode, String newBank, String newTax, String newCitizen, String company);

    @Query("SELECT  exists(SELECT 1 FROM employee_profile entity WHERE entity.employee_code = :newCode and entity.company = :company and entity.is_deleted = false)")
    Mono<Boolean> isNewCodeExist(String newCode, String company);

    @Query("SELECT  exists(SELECT 1 FROM employee_profile entity WHERE entity.bank_number = :newBank and entity.company = :company and entity.is_deleted = false)")
    Mono<Boolean> isNewBankExist(String newBank, String company);

    @Query("SELECT  exists(SELECT 1 FROM employee_profile entity WHERE entity.tax_code = :newTax and entity.company = :company and entity.is_deleted = false)")
    Mono<Boolean> isNewTaxExist(String newTax, String company);

    @Query("SELECT  exists(SELECT 1 FROM employee_profile entity WHERE entity.citizen_id = :newCitizen and entity.company = :company and entity.is_deleted = false)")
    Mono<Boolean> isNewCitizenExist(String newCitizen, String company);

    Mono<EmployeeProfile> findFirstByEmployeeCodeAndCompany(String employeeCode, String company);


    @Query("""
        SELECT DISTINCT
          employee_profile.ID AS employee_id,
          employee_profile.employee_code,
          employee_profile.start_work_date,
          employee_profile.full_name
        FROM
          employee_profile
          LEFT JOIN uniform_release ur ON employee_profile.ID = ur.employee_id
          AND employee_profile.company = :company AND ur.company = :company

        WHERE
          date_part('month', employee_profile.start_work_date) =
            CASE
              WHEN date_part('month', now()) = 1 THEN 12
              ELSE date_part('month', now()) - 1
            END
          AND employee_profile.status = 'WORKING'
          AND (ur.DATE IS NULL OR date_part('year', ur.DATE) != date_part('year', now()))
        LIMIT :take
        OFFSET :skip;
        """)
    Flux<UniformExpiringReport> getUniformExpiringReportUnallocated(int take, long skip, String company);

    @Query("""
            SELECT DISTINCT
               employee_profile.ID AS employee_id,
               employee_profile.employee_code,
               employee_profile.start_work_date,
               employee_profile.full_name
            FROM
               employee_profile
               INNER JOIN uniform_release ur ON employee_profile.ID = ur.employee_id
               AND employee_profile.company = :company AND ur.company = :company
             WHERE
               date_part('month', employee_profile.start_work_date) =
                 CASE
                   WHEN date_part('month', now()) = 1 THEN 12
                   ELSE date_part('month', now()) - 1
                 END
               AND employee_profile.status = 'WORKING'
               AND date_part('year', ur.DATE) = date_part('year', now()) -- Cấp trong năm hiện tại
            LIMIT :take
            OFFSET :skip;
    """)
    Flux<UniformExpiringReport> getUniformExpiringReportAllocated(int take, long skip, String company);

    @Query("""
       SELECT COUNT(DISTINCT employee_profile.ID)
       FROM employee_profile
       LEFT JOIN uniform_release ur ON employee_profile.ID = ur.employee_id
          AND employee_profile.company = :company AND ur.company = :company

       WHERE
         date_part('month', employee_profile.start_work_date) =
           CASE
             WHEN date_part('month', now()) = 1 THEN 12
             ELSE date_part('month', now()) - 1
           END
         AND employee_profile.status = 'WORKING'
         AND (
           ur.DATE IS NULL -- Chưa từng cấp đồng phục
           OR date_part('year', ur.DATE) != date_part('year', now()) -- Không cấp trong năm hiện tại
             );
       """
    )
    Mono<Long> countUniformExpiringReportUnallocated(String company);

    @Query("""
        SELECT COUNT(DISTINCT employee_profile.ID) AS total_count
            FROM
              employee_profile
              JOIN uniform_release ur ON employee_profile.ID = ur.employee_id
            AND employee_profile.company = :company AND ur.company = :company
            WHERE
               date_part('month', employee_profile.start_work_date) =
                 CASE
                   WHEN date_part('month', now()) = 1 THEN 12
                   ELSE date_part('month', now()) - 1
                 END
               AND employee_profile.status = 'WORKING'
               AND date_part('year', ur.DATE) = date_part('year', now()) -- Cấp trong năm hiện tại;
    """)
    Mono<Long> countUniformExpiringReportAllocated(String company);

    @Query("""
                        SELECT DISTINCT
                                employee_profile.id as employee_id,
                                employee_profile.employee_code,
                                employee_profile.start_work_date,
                                full_name
                            FROM employee_profile
                            WHERE date_part('month', employee_profile.start_work_date) = :month
                                AND employee_profile.status = 'WORKING'
                            limit :limit offset :skip
            """)
    Flux<UniformExpiringReport> getUniformExpiringReportBoth(int month, int limit, long skip);

    @Query("""
            SELECT count(DISTINCT employee_profile.employee_code)
                FROM employee_profile
                WHERE date_part('month', employee_profile.start_work_date) = :month
                  and employee_profile.status = 'WORKING'
            """
    )
    Mono<Long> countUniformExpiringReportBoth(int month);

    @Query("""

                    SELECT sum(temp.total_join) as total_join, sum(temp.total_leave) as total_leave,temp.month from
                    (
                            SELECT to_char(cl.leave_date, 'YYYY-MM') as month, count(*) as total_leave, 0 as total_join  from confirm_leave cl join employee_profile ep on cl.id = ep.id
                                                                                                                         where cl.leave_date >= :startDate and cl.leave_date <= :endDate
                                                                                                                         and ep.company = :company
                                                                                                                         GROUP BY(to_char(cl.leave_date, 'YYYY-MM'))
                            UNION ALL
                            SELECT to_char(ep.start_work_date, 'YYYY-MM') as month, 0 as total_leave,count(*) as total_join  from employee_profile ep

                                                                                                                            where ep.start_work_date >= :startDate and ep.start_work_date <= :endDate
                                                                                                                             and ep.company = :company
                                                                                                                             GROUP BY(to_char(ep.start_work_date, 'YYYY-MM'))

                            ) as temp GROUP BY temp.month ORDER BY temp.month DESC limit :limit offset :skip
            """)
    Flux<HumanResourceChangeReport> getHumanResourceChangeReport(LocalDate startDate, LocalDate endDate, String company, int limit, long skip);

    @Query("""
                    SELECT count(*) from
            (
            SELECT to_char(cl.leave_date, 'YYYY-MM') as month, count(*) as total_leave, 0 as total_join  from confirm_leave cl join employee_profile ep on cl.id = ep.id
                                                                                                         where cl.leave_date >= :startDate and cl.leave_date <= :endDate
                                                                                                            and ep.company = :company
                                                                                                         GROUP BY(to_char(cl.leave_date, 'YYYY-MM'))
            UNION ALL
            SELECT to_char(ep.start_work_date, 'YYYY-MM') as month, 0 as total_leave,count(*) as total_join  from employee_profile ep
                                                                                                            where ep.start_work_date >= :startDate and ep.start_work_date <= :endDate
                                                                                                                and ep.company = :company
                                                                                                             GROUP BY(to_char(ep.start_work_date, 'YYYY-MM'))

            ) as temp

            """)
    Mono<Long> countHumanResourceChangeReport(LocalDate startDate, LocalDate endDate, String company);

    @Query("SELECT * FROM employee_profile entity WHERE entity.id = :id and entity.company = :company and entity.is_deleted = false limit 1")
    Mono<EmployeeProfile> findByIdAndCompany(UUID id, String company);

    @Query("SELECT * FROM employee_profile entity WHERE entity.employee_code = :code and entity.company = :company and entity.is_deleted = false limit 1")
    Mono<EmployeeProfile> findByCodeAndCompany(String code, String company);

    @Query("SELECT * FROM employee_profile entity WHERE entity.company = :company AND entity.department IN (:departments) AND entity.is_deleted = false")
    Flux<EmployeeProfile> findByCompanyAndListDepartment(String company, List<String> departments);

    @Query("SELECT * FROM  profile_attachment WHERE employee_profile_id IN (:ids) and type in (:types)")
    Flux<ProfileAttachment> getProfileAttachmentInEmployeeIds(List<UUID> ids, List<String> types);


    @Query("SELECT * FROM employee_profile entity WHERE CAST(entity.id as VARCHAR) IN (:ids) AND entity.company = :company and entity.is_deleted = false")
    Flux<EmployeeProfile> findAllByIdInAndCompany(List<String> ids, String company);

    @Query("""
             SELECT ep.*
             FROM public.employee_profile ep
             JOIN public.workspace w ON ep.workspace_id = w.id
             WHERE w.company = :company
               AND w.workspace_type = :office
               AND ((:employeeIds) IS NULL OR ep.id NOT IN (:employeeIds))
            """)
    Flux<EmployeeProfile> findAllByCompanyAndWorkspaceType(String company, WorkspaceType office, List<UUID> employeeIds);

}

interface EmployeeProfileRepositoryInternal {
    <S extends EmployeeProfile> Mono<S> save(S entity);

    Flux<EmployeeProfile> findAllBy(Pageable pageable);

    Flux<EmployeeProfile> findAll();

    Mono<EmployeeProfile> findFirstById(UUID id);

    Flux<EmployeeProfile> findAllByQuery(EmployeeProfileQuery query, Pageable pageable);

    Mono<Long> countByQuery(EmployeeProfileQuery query);

    Mono<EmployeeProfile> findByIdAndNotDelete(UUID id);

    Flux<HumanResourceChangeReport> getHumanResourceChangeReport(HumanResourceChangeReport.Query query);

    Mono<Long> countHumanResourceChangeReport(HumanResourceChangeReport.Query query);
    // this is not supported at the moment because of
    // https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<EmployeeProfile> findAllBy(Pageable pageable, Criteria criteria);

    Mono<Void> callInsertEmployeeProfile(EmployeeProfileXlsx employeeProfile);

}
