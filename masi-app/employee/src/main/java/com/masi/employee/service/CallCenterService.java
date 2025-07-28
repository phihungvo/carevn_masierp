package com.masi.employee.service;

import com.masi.employee.domain.EmployeeProfile;
import com.masi.employee.domain.criteria.CallCenterCriteria;
import com.masi.employee.domain.enumeration.StatusEntity;
import com.masi.employee.repository.CallCenterRepository;
import com.masi.employee.repository.EmployeeProfileRepository;
import com.masi.employee.service.dto.CallCenterDTO;
import com.masi.employee.service.dto.EmployeeProfileDTO;
import com.masi.employee.service.mapper.CallCenterMapper;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.masi.employee.domain.CallCenter}.
 */
@Service
@Transactional
public class CallCenterService {

    private static final Logger log = LoggerFactory.getLogger(CallCenterService.class);

    private final CallCenterRepository callCenterRepository;

    private final CallCenterMapper callCenterMapper;
    private final EmployeeProfileRepository employeeProfileRepository;

    public CallCenterService(CallCenterRepository callCenterRepository, CallCenterMapper callCenterMapper, EmployeeProfileRepository employeeProfileRepository) {
        this.callCenterRepository = callCenterRepository;
        this.callCenterMapper = callCenterMapper;
        this.employeeProfileRepository = employeeProfileRepository;
    }

    /**
     * Save a callCenter.
     *
     * @param callCenterDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<CallCenterDTO> save(CallCenterDTO callCenterDTO) {
        log.debug("Request to save CallCenter : {}", callCenterDTO);
        callCenterDTO.setStatus(StatusEntity.NEW);
        return callCenterRepository.save(callCenterMapper.toEntity(callCenterDTO))
                .map(e -> {
                            log.debug("Saved CallCenter: {}", e);
                            return callCenterMapper.toDto(e);
                        }
                );
    }

    /**
     * Update a callCenter.
     *
     * @param callCenterDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<CallCenterDTO> update(CallCenterDTO callCenterDTO) {
        log.debug("Request to update CallCenter : {}", callCenterDTO);
        return callCenterRepository.save(callCenterMapper.toEntity(callCenterDTO).setIsPersisted()).map(callCenterMapper::toDto);
    }

    /**
     * Partially update a callCenter.
     *
     * @param callCenterDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<CallCenterDTO> partialUpdate(CallCenterDTO callCenterDTO) {
        log.debug("Request to partially update CallCenter : {}", callCenterDTO);

//        if(callCenterDTO.getEmployeeAssignId() != null ) {
//            callCenterDTO.setStatus(StatusEntity.PROCESSING);
//        }
//
//        if(callCenterDTO.getEmployeeCloseId() != null ) {
//            callCenterDTO.setStatus(StatusEntity.COMPLETED);
//        }
        return callCenterRepository
                .findById(callCenterDTO.getId())
                .map(existingCallCenter -> {
                    callCenterMapper.partialUpdate(existingCallCenter, callCenterDTO);
                    existingCallCenter.setIsPersisted();
                    return existingCallCenter;
                })
                .flatMap(callCenterRepository::save)
                .map(callCenterMapper::toDto);
    }

    /**
     * Find callCenters by Criteria.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<CallCenterDTO> findByCriteria(CallCenterCriteria criteria, Pageable pageable) {
        log.debug("Request to get all CallCenters by Criteria");
        return callCenterRepository.findByCriteria(criteria, pageable)
                .map(callCenterMapper::toDto);
    }

    /**
     * Find the count of callCenters by criteria.
     *
     * @param criteria filtering criteria
     * @return the count of callCenters
     */
    public Mono<Long> countByCriteria(CallCenterCriteria criteria) {
        log.debug("Request to get the count of all CallCenters by Criteria");
        return callCenterRepository.countByCriteria(criteria);
    }

    /**
     * Returns the number of callCenters available.
     *
     * @return the number of entities in the database.
     */
    public Mono<Long> countAll() {
        return callCenterRepository.count();
    }

    /**
     * Get one callCenter by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<CallCenterDTO> findOne(UUID id) {
        log.debug("Request to get CallCenter : {}", id);

        return callCenterRepository.findById(id)
                .map(callCenterMapper::toDto)
                .flatMap(callCenterDTO -> {
                    List<UUID> employeeIds = Stream.of(
                                    callCenterDTO.getEmployeeCreatedId(),
                                    callCenterDTO.getEmployeeAssignId(),
                                    callCenterDTO.getEmployeeCloseId(),
                                    UUID.fromString(callCenterDTO.getCreatedBy()))
                            .filter(Objects::nonNull)  // Lọc các giá trị không null
                            .collect(Collectors.toList());  // Thu thập vào List

                    return employeeProfileRepository.findByIdIn(employeeIds)
                            .map(EmployeeProfile::toBriefDTO)
                            .collectList()
                            .flatMap(employees -> {
                                // Map employees by their ID for easier lookup
                                Map<UUID, EmployeeProfileDTO> employeeMap = employees.stream()
                                        .collect(Collectors.toMap(EmployeeProfileDTO::getId, Function.identity()));
                                // Set employee details in the DTO
                                if (employeeMap.containsKey(callCenterDTO.getEmployeeCreatedId())) {
                                    callCenterDTO.setEmployeeCreated(employeeMap.get(callCenterDTO.getEmployeeCreatedId()));
                                }
                                if (employeeMap.containsKey(callCenterDTO.getEmployeeAssignId())) {
                                    callCenterDTO.setEmployeeAssign(employeeMap.get(callCenterDTO.getEmployeeAssignId()));
                                }
                                if (employeeMap.containsKey(callCenterDTO.getEmployeeCloseId())) {
                                    callCenterDTO.setEmployeeClose(employeeMap.get(callCenterDTO.getEmployeeCloseId()));
                                }
                                if (employeeMap.containsKey(UUID.fromString(callCenterDTO.getCreatedBy()))) {
                                    callCenterDTO.setCreatedByEmployee(employeeMap.get(UUID.fromString(callCenterDTO.getCreatedBy())));
                                }

                                return Mono.just(callCenterDTO); // Ensure Mono.just is returned here
                            });
                });
    }


    /**
     * Delete the callCenter by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(UUID id) {
        log.debug("Request to delete CallCenter : {}", id);
        return callCenterRepository.changeIsDeletedById(id);
    }

    public Mono<Void> setStatus(UUID id, StatusEntity status, UUID employeeId, int type) {
        return callCenterRepository.findById(id)
                .flatMap(e -> {
                    if (type == 1) {
//                        e.setEmployeeAssignDate(ZonedDateTime.now());
//                        e.setEmployeeAssignId(employeeId);
                    } else if (type == 2) {
                        e.setEmployeeCloseDate(ZonedDateTime.now());
                        e.setEmployeeCloseId(employeeId);
                    }
                    e.setStatus(status);
                    e.setIsPersisted();
                    return callCenterRepository.save(e).then();
                });
    }
}
