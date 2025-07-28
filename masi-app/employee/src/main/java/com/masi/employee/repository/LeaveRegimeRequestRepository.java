package com.masi.employee.repository;

import com.masi.employee.domain.LeaveRegimeRequest;
import com.masi.employee.domain.LeaveRequest;
import com.masi.employee.domain.enumeration.LeaveRegimeRequestStatus;
import com.masi.employee.domain.enumeration.LeaveType;
import com.masi.employee.service.dto.LeaveRegimeRequestGetListDTO;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the LeaveRegimeRequest entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LeaveRegimeRequestRepository
    extends ReactiveCrudRepository<LeaveRegimeRequest, UUID>, LeaveRegimeRequestRepositoryInternal {
    Flux<LeaveRegimeRequest> findAllBy(Pageable pageable);

    @Override
    <S extends LeaveRegimeRequest> Mono<S> save(S entity);

    @Override
    Flux<LeaveRegimeRequest> findAll();

    @Override
    Mono<LeaveRegimeRequest> findById(UUID id);

    @Override
    Mono<Void> deleteById(UUID id);

    Mono<LeaveRegimeRequest> findFirstByIsDeletedIsFalseAndId(UUID id);

    @Query("UPDATE leave_regime_request SET status = :status WHERE id = :id")
    Mono<Void> updateStatusById(UUID id, LeaveRegimeRequestStatus status);

    //    Báo cáo này sẽ hiển thị số lượng NV đăng ký nghỉ chế độ theo từng loại nghỉ.
    Flux<LeaveRegimeRequest> findAllByLastWorkDateAfterAndReturnWorkDateBeforeAndIsDeletedIsFalseAndCompanyId(ZonedDateTime lastWorkDate, ZonedDateTime returnWorkDate, String companyId);

    @Query("""
            SELECT DISTINCT ON (lr.employee_id) lr.*
            FROM leave_regime_request lr
            WHERE lr.employee_id IN (:employeeHaveShift)
            AND :dateCheck > lr.last_work_date
            AND :dateCheck < lr.return_work_date
            AND lr.is_deleted = false
            ORDER BY lr.employee_id, lr.created_at DESC;
                        """)
    Flux<LeaveRegimeRequest> findByEmployeeIsHaveLeaveRequestToDate(Collection<UUID> employeeHaveShift, LocalDate dateCheck);
}

interface LeaveRegimeRequestRepositoryInternal {
    <S extends LeaveRegimeRequest> Mono<S> save(S entity);

    Flux<LeaveRegimeRequest> findAllBy(Pageable pageable);

    Flux<LeaveRegimeRequest> findAll();

    Mono<LeaveRegimeRequest> findById(UUID id);

    // this is not supported at the moment because of
    // https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<LeaveRegimeRequest> findAllBy(Pageable pageable, Criteria criteria);
    Flux<LeaveRegimeRequest> findAllByFilter(Pageable pageable,
                                             LeaveRegimeRequestGetListDTO leaveRegimeRequestGetListDTO, String role);

    Mono<Long> countByFilter(LeaveRegimeRequestGetListDTO leaveRegimeRequestGetListDTO, String role);

    Mono<LeaveRegimeRequest> findNotDeleteById(UUID id);
}
