package com.masi.employee.repository;

import com.masi.employee.domain.Employee;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import com.masi.employee.domain.EmployeeJoinCode;
import com.masi.employee.domain.enumeration.TimeKeepingType;
import com.masi.employee.domain.enumeration.WorkspaceType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the Employee entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EmployeeRepository extends ReactiveCrudRepository<Employee, UUID>, EmployeeRepositoryInternal {
    @Override
    <S extends Employee> Mono<S> save(S entity);

//    @Query("SELECT e.* FROM employee  e left join  employee_profile  ep on e.id = ep.id WHERE e.is_active = true and ep.status!='RESIGNED' and ep.company = :company")
//    Flux<Employee> findAllActive(String company);
    @Query("SELECT count(e.id) FROM employee  e left join  employee_profile  ep on e.id = ep.id WHERE e.is_active = true and ep.status!='RESIGNED' and ep.company = :company")
    Mono<Long> countAllActive(String company);

    Mono<Long> countAllByWorkspaceIdAndIsActiveIsTrue(UUID workspaceId);

    @Override
    Flux<Employee> findAllActive();

    @Override
    Mono<Employee> findById(UUID id);


    Flux<Employee> findAllByIdIn(List<UUID> ids);

    @Query("""
            SELECT
                e.*,
                ep.employee_code AS employee_code,
            		ws.name as workspace_name,
            		ws.normalized_name as workspace_normalized_name
            FROM
                employee e
                LEFT JOIN employee_profile ep ON ep.ID = e.ID
                LEFT JOIN workspace ws ON ws.ID = e.workspace_id
            WHERE
                e.ID IN (:ids)
                AND e.is_active = TRUE;
                        """)
    Flux<EmployeeJoinCode> findAllByIdIns(List<UUID> ids);


    @Query("SELECT * FROM employee WHERE CAST(id as VARCHAR) IN (:ids)")
    Flux<Employee> findAllByIdInListString(List<String> ids);

    @Override
    Mono<Void> deleteById(UUID id);

    Flux<Employee> findAllByIsActive(Boolean isActive);

    Mono<Long> countAllByIdInAndIsActiveTrue(Collection<UUID> ids);

    @Modifying
    @Query("UPDATE employee SET is_active = false WHERE id = :id")
    Mono<Void> disableEmployeeById(UUID id);

    @Modifying
    @Query("UPDATE employee SET is_active = true WHERE id = :id")
    Mono<Void> enableEmployeeById(UUID id);

}

interface EmployeeRepositoryInternal {
    <S extends Employee> Mono<S> save(S entity);

    Flux<Employee> findAllActiveBy(WorkspaceType workspaceType, Pageable pageable);
    Flux<Employee> findAllActiveBy(WorkspaceType workspaceType, Pageable pageable, TimeKeepingType timeKeepingType);
    Flux<Employee> findAllActiveBy(WorkspaceType workspaceType, Pageable pageable, TimeKeepingType timeKeepingType, String companyId);
    Flux<Employee> findAllActive();

    Mono<Employee> findById(UUID id);

    // this is not supported at the moment because of
    // https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Employee> findAllBy(Pageable pageable, Criteria criteria);
    Mono<Long> countAllActive();
    Mono<Long> countAllByWorkspaceType(WorkspaceType workspaceType);
    Mono<Long> countAllByWorkspaceType(WorkspaceType workspaceType, TimeKeepingType timeKeepingType);
    Flux<Employee> findAllActive(String company);
}
