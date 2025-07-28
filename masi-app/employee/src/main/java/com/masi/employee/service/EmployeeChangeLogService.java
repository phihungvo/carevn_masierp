package com.masi.employee.service;

import com.masi.employee.domain.EmployeeChangeLog;
import com.masi.employee.domain.EmployeeProfile;
import com.masi.employee.helper.ObjectComparator;
import com.masi.employee.repository.EmployeeChangeLogRepository;
import com.masi.employee.service.dto.EmployeeChangeLogDTO;
import com.masi.employee.service.dto.request.UpdateEmployeeProfile;
import com.masi.employee.service.mapper.EmployeeChangeLogMapper;

import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

import io.r2dbc.postgresql.codec.Json;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.masi.employee.domain.EmployeeChangeLog}.
 */
@Service
@Transactional
public class EmployeeChangeLogService {

    private static final Logger log = LoggerFactory.getLogger(EmployeeChangeLogService.class);

    private final EmployeeChangeLogRepository employeeChangeLogRepository;

    private final EmployeeChangeLogMapper employeeChangeLogMapper;

    private AnnualLeaveService annualLeaveService;

    @Autowired
    public void setAnnualLeaveService(@Lazy AnnualLeaveService annualLeaveService) {
        this.annualLeaveService = annualLeaveService;
    }

    public EmployeeChangeLogService(
        EmployeeChangeLogRepository employeeChangeLogRepository,
        EmployeeChangeLogMapper employeeChangeLogMapper
    ) {
        this.employeeChangeLogRepository = employeeChangeLogRepository;
        this.employeeChangeLogMapper = employeeChangeLogMapper;
    }


    public Mono<EmployeeChangeLogDTO> makeChange(EmployeeProfile oldProfile, EmployeeProfile newProfile, String changeBy) {
        oldProfile.setUpdatedAt(null);
        newProfile.setUpdatedAt(null);

        var newProfileBuilder = oldProfile.toBuilder()
            .officialWorkTypeDuration(newProfile.getOfficialWorkTypeDuration())
            .insurancePaymentLevel(newProfile.getInsurancePaymentLevel())
            .probationDateFrom(newProfile.getProbationDateFrom())
            .probationDateTo(newProfile.getProbationDateTo());

        Optional<Json> change = ObjectComparator.compareObjectsToPostgresJson(oldProfile, newProfileBuilder.build());
        UpdateEmployeeProfile updateEmployeeProfile = UpdateEmployeeProfile.builder()
            .employeeId(oldProfile.getId())
            .build();
        updateEmployeeProfile.applyDiff(oldProfile, newProfileBuilder
            .contractDate(newProfile.getContractDate())
            .startWorkDate(newProfile.getStartWorkDate())
            .probationDateTo(newProfile.getProbationDateTo())
            .probationDateFrom(newProfile.getProbationDateFrom())
            .build()
        );
        if (change.isEmpty())
            return annualLeaveService.updateEmployeeProfile(updateEmployeeProfile).then(Mono.empty());
        EmployeeChangeLog employeeChangeLog = EmployeeChangeLog.builder()
            .id(UUID.randomUUID())
            .change(change.orElseThrow())
            .changeBy(changeBy)
            .employeeId(oldProfile.getId())
            .changeDate(ZonedDateTime.now())
            .build();


        return employeeChangeLogRepository.save(employeeChangeLog)
            .map(employeeChangeLogMapper::toDto)
            .flatMap(employeeChangeLogDTO -> annualLeaveService.updateEmployeeProfile(updateEmployeeProfile).then(Mono.just(employeeChangeLogDTO)));
    }

    public Flux<EmployeeChangeLogDTO> findAllByEmployeeId(UUID employeeId, Pageable pageable) {
        return employeeChangeLogRepository.findAllByEmployeeIdOrderByChangeDateDesc(employeeId, pageable)
            .map(employeeChangeLogMapper::toDto);
    }

    public Mono<Long> countAllByEmployeeId(UUID employeeId) {
        return employeeChangeLogRepository.countAllByEmployeeId(employeeId);
    }

    public Mono<EmployeeChangeLogDTO> findById(UUID id) {
        return employeeChangeLogRepository.findById(id)
            .map(employeeChangeLogMapper::toDto);
    }

}
