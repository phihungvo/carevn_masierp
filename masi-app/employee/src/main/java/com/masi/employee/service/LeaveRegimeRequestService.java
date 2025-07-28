package com.masi.employee.service;

import com.masi.employee.domain.LeaveRegimeRequest;
import com.masi.employee.domain.ProcessLeaveRegimeRequest;
import com.masi.employee.domain.enumeration.LeaveRegimeRequestStatus;
import com.masi.employee.domain.enumeration.ProcessLeaveRegimeRequestStatus;
import com.masi.employee.repository.DayOffRepository;
import com.masi.employee.repository.LeaveRegimeRequestRepository;
import com.masi.employee.repository.ProcessLeaveRegimeRequestRepository;
import com.masi.employee.service.dto.LeaveRegimeRequestDTO;
import com.masi.employee.service.dto.LeaveRegimeRequestGetListDTO;
import com.masi.employee.service.dto.LeaveRegimeRequestProcessDTO;
import com.masi.employee.service.dto.LeaveRegimeRequestUpdateDTO;
import com.masi.employee.service.dto.LeaveRequestDTO;
import com.masi.employee.service.dto.ProcessLeaveRegimeRequestDTO;
import com.masi.employee.service.mapper.LeaveRegimeRequestMapper;
import com.masi.employee.service.mapper.ProcessLeaveRegimeRequestMapper;
import com.masi.employee.web.rest.errors.BadRequestAlertException;

import lombok.Data;

import java.lang.reflect.Array;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import org.springframework.data.domain.Sort;

/**
 * Service Implementation for managing
 * {@link com.masi.employee.domain.LeaveRegimeRequest}.
 */
@Service
@Data
@Transactional
public class LeaveRegimeRequestService {

    private static final Logger log = LoggerFactory.getLogger(LeaveRegimeRequestService.class);

    private final LeaveRegimeRequestRepository leaveRegimeRequestRepository;
    private final ProcessLeaveRegimeRequestRepository processLeaveRegimeRequestRepository;
    private final LeaveRegimeRequestMapper leaveRegimeRequestMapper;
    private final ProcessLeaveRegimeRequestMapper processLeaveRegimeRequestMapper;

    private final DayOffRepository dayOffRepository;
    private AnnualLeaveService annualLeaveService;

    private LeaveRequestService leaveRequestService;
    private ProcessLeaveRegimeRequestService processLeaveRegimeRequestService;
    private EmployeeService employeeService;
    private final TimeKeepingService timeKeepingService;

    @Autowired
    public void setAnnualLeaveService(AnnualLeaveService annualLeaveService) {
        this.annualLeaveService = annualLeaveService;
    }

    @Autowired
    public void setEmployeeService(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @Autowired
    public void setLeaveRequestService(LeaveRequestService leaveRequestService) {
        this.leaveRequestService = leaveRequestService;
    }

    @Autowired
    public void setProcessLeaveRegimeRequestService(ProcessLeaveRegimeRequestService processLeaveRegimeRequestService) {
        this.processLeaveRegimeRequestService = processLeaveRegimeRequestService;
    }

    @Lazy
    public LeaveRegimeRequestService(
        LeaveRegimeRequestRepository leaveRegimeRequestRepository,
        LeaveRegimeRequestMapper leaveRegimeRequestMapper, LeaveRequestService leaveRequestService,
        ProcessLeaveRegimeRequestService processLeaveRegimeRequestService,
        ProcessLeaveRegimeRequestRepository processLeaveRegimeRequestRepository,
        EmployeeService employeeService, ProcessLeaveRegimeRequestMapper processLeaveRegimeRequestMapper,
        DayOffRepository dayOffRepository,
        AnnualLeaveService annualLeaveService, TimeKeepingService timeKeepingService) {
        this.leaveRegimeRequestRepository = leaveRegimeRequestRepository;
        this.processLeaveRegimeRequestRepository = processLeaveRegimeRequestRepository;
        this.leaveRegimeRequestMapper = leaveRegimeRequestMapper;
        this.leaveRequestService = leaveRequestService;
        this.processLeaveRegimeRequestService = processLeaveRegimeRequestService;
        this.employeeService = employeeService;
        this.annualLeaveService = annualLeaveService;
        this.processLeaveRegimeRequestMapper = processLeaveRegimeRequestMapper;
        this.dayOffRepository = dayOffRepository;
        this.timeKeepingService = timeKeepingService;
    }

    /**
     * Save a leaveRegimeRequest.
     *
     * @param leaveRegimeRequestDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<LeaveRegimeRequestDTO> save(LeaveRegimeRequestDTO leaveRegimeRequestDTO) {
        log.debug("Request to save LeaveRegimeRequest : {}", leaveRegimeRequestDTO);
        return leaveRegimeRequestRepository
            .save(leaveRegimeRequestMapper.toEntity(leaveRegimeRequestDTO))
            .map(leaveRegimeRequestMapper::toDto);
    }

    public Mono<LeaveRegimeRequestDTO> createLeaveRegimeRequest(LeaveRegimeRequestDTO leaveRegimeRequestDTO,
                                                                ArrayList<UUID> approvers) {
        log.debug("Request to createLeaveRegimeRequest LeaveRegimeRequest : {}", leaveRegimeRequestDTO);

        var validateList = new ArrayList<>(approvers);
        validateList.add(leaveRegimeRequestDTO.getEmployeeId());
        validateList.add(leaveRegimeRequestDTO.getSubstituteId());
        var valid = employeeService.areAllEmployeesValid(validateList);
        return valid.flatMap(validValue -> {
            if (!validValue) {
                return Mono.error(new BadRequestAlertException("Invalid employee", "LeaveRegimeRequest",
                    "INVALID_EMPLOYEE"));
            }

            return annualLeaveService.findOneById(leaveRegimeRequestDTO.getEmployeeId()).flatMap(employee -> {
//                if (employee.getNumberDaysOff() < leaveRegimeRequestDTO.calculateLeaveDays()) {
//                    return Mono.error(new BadRequestAlertException("Not enough leave days", "LeaveRegimeRequest",
//                        "NOT_ENOUGH_LEAVE_DAYS"));
//                }
                leaveRegimeRequestDTO.setStatus(LeaveRegimeRequestStatus.NEW);
                var entity1 = leaveRegimeRequestMapper.toEntity(leaveRegimeRequestDTO);
                entity1.setFiles(leaveRegimeRequestDTO.getFilesJson());
                return leaveRegimeRequestRepository
                    .save(entity1)
                    .switchIfEmpty(Mono.error(new RuntimeException("Failed to save leave regime request")))
                    .map(leaveRegimeRequestMapper::toDto).flatMap(entity -> {
                        if (approvers.isEmpty()) {
                            return Mono.just(entity);
                        }
                        var a = entity.toProcessLeaveRegimeRequest(approvers);
                        return processLeaveRegimeRequestService.saveAllRequests(a).then(Mono.just(entity));
                    });
            });

        });
    }

    /**
     * Update a leaveRegimeRequest.
     *
     * @param leaveRegimeRequestDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<LeaveRegimeRequestDTO> update(LeaveRegimeRequestDTO leaveRegimeRequestDTO) {
        log.debug("Request to update LeaveRegimeRequest : {}", leaveRegimeRequestDTO);
        return leaveRegimeRequestRepository
            .save(leaveRegimeRequestMapper.toEntity(leaveRegimeRequestDTO).setIsPersisted())
            .map(leaveRegimeRequestMapper::toDto);
    }

    /**
     * Partially update a leaveRegimeRequest.
     *
     * @param leaveRegimeRequestDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<LeaveRegimeRequestDTO> partialUpdate(LeaveRegimeRequestDTO leaveRegimeRequestDTO,
                                                     LeaveRegimeRequestUpdateDTO leaveRegimeRequestUpdateDTO) {
        log.info("Request to partially update LeaveRegimeRequest : {}, {}", leaveRegimeRequestDTO,
            leaveRegimeRequestUpdateDTO);
        return leaveRegimeRequestRepository
            .findFirstByIsDeletedIsFalseAndId(leaveRegimeRequestDTO.getId())
            .flatMap(existingLeaveRegimeRequest -> {
//                if (!LeaveRegimeRequestStatus.NEW.equals(existingLeaveRegimeRequest.getStatus())) {
//                    return Mono
//                        .error(new BadRequestAlertException("LeaveRegimeRequest is not in the correct status",
//                            "LeaveRegimeRequest", "INVALID_STATUS"));
//                }

                return annualLeaveService.findOneById(leaveRegimeRequestDTO.getEmployeeId()).flatMap(employee -> {
//                    if (employee.getNumberDaysOff() < leaveRegimeRequestDTO.calculateLeaveDays()) {
//                        return Mono
//                            .error(new BadRequestAlertException("Not enough leave days", "LeaveRegimeRequest",
//                                "NOT_ENOUGH_LEAVE_DAYS"));
//                    }
                    leaveRegimeRequestMapper.partialUpdate(
                        existingLeaveRegimeRequest, leaveRegimeRequestDTO);
                    existingLeaveRegimeRequest.setFiles(leaveRegimeRequestDTO.getFilesJson());
                    return leaveRegimeRequestRepository
                        .save(existingLeaveRegimeRequest)
                        .map(leaveRegimeRequestMapper::toDto);

//                    return processLeaveRegimeRequestService
//                        .findAllProcessByLeaveRegimeRequestId(existingLeaveRegimeRequest.getId())
//                        .collectList().flatMap(approvers -> {
//                            boolean anyNonWaitingApproval = approvers.stream()
//                                .anyMatch(process -> !Objects.equals(ProcessLeaveRegimeRequestStatus.WAITING_APPROVAL
//                                    .toString(), process.getStatus().toString()));
//                            if (anyNonWaitingApproval) {
//                                return Mono.error(new BadRequestAlertException(
//                                    "Some requests are not waiting for approval",
//                                    "ProcessLeaveRegimeRequest", "INVALID_STATUS"));
//                            }
//                         //
//                        });
                });
            });
    }

    public Mono<LeaveRegimeRequestDTO> processLeaveRequestRegime(LeaveRegimeRequestDTO leaveRegimeRequestDTO,
                                                                 LeaveRegimeRequestProcessDTO leaveRegimeRequestProcessDTO) {
        log.debug("Request to partially update LeaveRegimeRequest : {}", leaveRegimeRequestDTO);
        return switch (leaveRegimeRequestDTO.getStatus()) {
            case APPROVED -> processApproveLeaveRegimeRequest(leaveRegimeRequestDTO, leaveRegimeRequestProcessDTO);
//                timeKeepingService.isEmployeeOffInDay(leaveRegimeRequestDTO.getEmployeeId(),
//                    leaveRegimeRequestDTO.getLastWorkDate()
//                        .withZoneSameLocal(ZoneId.of("Asia/Ho_Chi_Minh")).toLocalDate()
//                    , leaveRegimeRequestDTO.getReturnWorkDate()
//                        .withZoneSameLocal(ZoneId.of("Asia/Ho_Chi_Minh")).toLocalDate())
//                .collectList()
//                .flatMap(list -> {
//                    if (!list.isEmpty()) {
//                        return Mono.error(new BadRequestAlertException("Employee is off in this day",
//                            "LeaveRegimeRequest", "INVALID_DATE"));
//                    }
//                    return processApproveLeaveRegimeRequest(leaveRegimeRequestDTO, leaveRegimeRequestProcessDTO);
//                });
            case REJECTED -> processRejectLeaveRequestRegimeRequest(leaveRegimeRequestDTO,
                leaveRegimeRequestProcessDTO);
            default ->
                Mono.error(new BadRequestAlertException("Invalid status", "LeaveRegimeRequest", "INVALID_STATUS"));
        };
    }

    public Mono<LeaveRegimeRequestDTO> processRejectLeaveRequestRegimeRequest(
        LeaveRegimeRequestDTO leaveRegimeRequestDTO,
        LeaveRegimeRequestProcessDTO leaveRegimeRequestProcessDTO) {
        if (StringUtils.isBlank(leaveRegimeRequestProcessDTO.getReason())) {
            return Mono
                .error(new BadRequestAlertException("Reason is required", "LeaveRegimeRequest", "INVALID_REASON"));
        }
        return processLeaveRegimeRequestRepository.findAllByLeaveRegimeRequestIdAndIsDeletedIsFalse(leaveRegimeRequestDTO.getId())
            .collectList().flatMap((processLeaveRegimeRequests) -> {
                var current = processLeaveRegimeRequests.stream()
                    .filter(process -> process.getApproverId().equals(leaveRegimeRequestDTO.getUpdatedBy()))
                    .findFirst().orElse(null);
                if (Objects.isNull(current)) {
                    return Mono.error(new BadRequestAlertException("ProcessLeaveRegimeRequest not found 3",
                        "LeaveRegimeRequest", "NOT_FOUND"));
                }
                leaveRegimeRequestProcessDTO.applyUpdateTo(current);
                return processLeaveRegimeRequestRepository.save(current)
                    .then(Mono.just(LeaveRegimeRequestStatus.REJECTED));

            }).flatMap(status -> {
                return leaveRegimeRequestRepository.findFirstByIsDeletedIsFalseAndId(leaveRegimeRequestDTO.getId())
                    .flatMap(
                        existingLeaveRegimeRequest -> {
                            if (!LeaveRegimeRequestStatus.WAITING_APPROVAL.equals(existingLeaveRegimeRequest.getStatus())) {
                                return Mono.error(new BadRequestAlertException("LeaveRegimeRequest is not in the correct status",
                                    "LeaveRegimeRequest", "INVALID_STATUS"));
                            }
                            existingLeaveRegimeRequest.setStatus(status);
                            existingLeaveRegimeRequest.setUpdatedAt(ZonedDateTime.now());
                            existingLeaveRegimeRequest.setUpdatedBy(leaveRegimeRequestDTO.getUpdatedBy());
                            existingLeaveRegimeRequest.setIsPersisted();
                            return leaveRegimeRequestRepository.save(existingLeaveRegimeRequest)
                                .map(leaveRegimeRequestMapper::toDto);
                        }
                    );
            });
    }

    public Mono<LeaveRegimeRequestDTO> processApproveLeaveRegimeRequest(LeaveRegimeRequestDTO leaveRegimeRequestDTO,
                                                                        LeaveRegimeRequestProcessDTO leaveRegimeRequestProcessDTO) {
        return processLeaveRegimeRequestRepository.findAllByLeaveRegimeRequestIdAndIsDeletedIsFalse(leaveRegimeRequestDTO.getId())
            .collectList().flatMap((processLeaveRegimeRequests) -> {
                var current = processLeaveRegimeRequests.stream()
                    .filter(process -> process.getApproverId().equals(leaveRegimeRequestDTO.getUpdatedBy()))
                    .findFirst().orElse(null);
                if (Objects.isNull(current)) {
                    return Mono.error(new BadRequestAlertException("ProcessLeaveRegimeRequest not found",
                        "LeaveRegimeRequest", "NOT_FOUND"));
                }
                leaveRegimeRequestProcessDTO.applyUpdateTo(current);
                var isAllApprove = processLeaveRegimeRequests.stream()
                    .allMatch(match -> match.getStatus().toString()
                        .equals(ProcessLeaveRegimeRequestStatus.APPROVED.toString()));
                var saveProcess = processLeaveRegimeRequestRepository.save(current);
                if (isAllApprove) {
                    return saveProcess.then(Mono.just(LeaveRegimeRequestStatus.APPROVED));
                }
                return saveProcess.then(Mono.just(LeaveRegimeRequestStatus.WAITING_APPROVAL));

            }).flatMap(status -> {
//                        return leaveRegimeRequestRepository.findFirstByIsDeletedIsFalseAndId(leaveRegimeRequestDTO.getId())
//                            .flatMap(
//                                existingLeaveRegimeRequest -> handleApprove(leaveRegimeRequestDTO, existingLeaveRegimeRequest,
//                                    leaveRegimeRequestProcessDTO));
                return leaveRegimeRequestRepository.findFirstByIsDeletedIsFalseAndId(leaveRegimeRequestDTO.getId())
                    .flatMap(

                        existingLeaveRegimeRequest -> {
                            if (!LeaveRegimeRequestStatus.WAITING_APPROVAL.equals(existingLeaveRegimeRequest.getStatus())) {
                                return Mono.error(new BadRequestAlertException("LeaveRegimeRequest is not in the correct status",
                                    "LeaveRegimeRequest", "INVALID_STATUS"));
                            }
                            existingLeaveRegimeRequest.setStatus(status);
                            existingLeaveRegimeRequest.setUpdatedAt(ZonedDateTime.now());
                            existingLeaveRegimeRequest.setUpdatedBy(leaveRegimeRequestDTO.getUpdatedBy());
                            existingLeaveRegimeRequest.setIsPersisted();
                            return leaveRegimeRequestRepository.save(existingLeaveRegimeRequest)
                                .map(leaveRegimeRequestMapper::toDto)
                                .flatMap(leaveRegimeRequest -> {
                                    if (LeaveRegimeRequestStatus.APPROVED.equals(status)) {
                                        return createLeaveRequest(leaveRegimeRequest).map(id -> leaveRegimeRequest);
                                    }
                                    return Mono.just(leaveRegimeRequest);
                                });
                        }
                    );
            });

    }

    private Mono<? extends LeaveRegimeRequestDTO> handleApprove(LeaveRegimeRequestDTO leaveRegimeRequestDTO,
                                                                LeaveRegimeRequest existingLeaveRegimeRequest, LeaveRegimeRequestProcessDTO leaveRegimeRequestProcessDTO) {
        if (existingLeaveRegimeRequest == null || existingLeaveRegimeRequest.getIsDeleted()) {
//            return Mono.error(new RuntimeException("Existing leave regime request is null or deleted"));
            return Mono.error(new BadRequestAlertException("LeaveRegimeRequest not found 1", "LeaveRegimeRequest",
                "NOT_FOUND"));
        }

//        if (!LeaveRegimeRequestStatus.WAITING_APPROVAL.equals(existingLeaveRegimeRequest.getStatus())) {
//            return Mono
//                    .error(new RuntimeException("Existing leave regime request is not waiting approval"));
//            return Mono.error(new BadRequestAlertException("LeaveRegimeRequest is not in the correct status",
//                "LeaveRegimeRequest", "INVALID_STATUS"));
//        }

        if (leaveRegimeRequestProcessDTO.getFileId() == null) {
            return Mono.error(new BadRequestAlertException("File is required", "LeaveRegimeRequest", "INVALID_FILE"));
        }
        var existingLeaveRegimeRequestDTO = leaveRegimeRequestMapper.toDto(existingLeaveRegimeRequest);
        return processLeaveRegimeRequestRepository.findByLeaveRegimeRequestIdAndStatusAndIsDeletedIsFalseAndApproverId(
            leaveRegimeRequestDTO.getId(), ProcessLeaveRegimeRequestStatus.WAITING_APPROVAL.toString(),
            leaveRegimeRequestDTO.getUpdatedBy()).flatMap(approve -> {
            approve.setStatus(ProcessLeaveRegimeRequestStatus.APPROVED);
            approve.setUpdatedAt(ZonedDateTime.now());
            approve.setUpdatedBy(leaveRegimeRequestDTO.getUpdatedBy());
            approve.setFileId(leaveRegimeRequestProcessDTO.getFileId());
            approve.setFileName(leaveRegimeRequestProcessDTO.getFileName());
            approve.setIsPersisted();
            return processLeaveRegimeRequestRepository.save(approve)
                .flatMap(savedProcess -> processLeaveRegimeRequestService
                    .findAllProcessByLeaveRegimeRequestId(existingLeaveRegimeRequest.getId())
                    .collectList()
                    .flatMap(process -> {
                        var isAllApprove = process.stream()
                            .allMatch(match -> match.getStatus().toString()
                                .equals(ProcessLeaveRegimeRequestStatus.APPROVED.toString()));
                        if (isAllApprove) {
                            var copyDto = new LeaveRegimeRequestDTO();
                            copyDto = existingLeaveRegimeRequestDTO;
                            var listDetails = new ArrayList<>(process);
                            copyDto.setProcessLeaveRegimeRequests(listDetails);
                            return createLeaveRequest(copyDto).flatMap(id -> {
                                existingLeaveRegimeRequest.setStatus(LeaveRegimeRequestStatus.APPROVED);
                                existingLeaveRegimeRequest.setUpdatedAt(ZonedDateTime.now());
                                existingLeaveRegimeRequest
                                    .setUpdatedBy(leaveRegimeRequestDTO.getUpdatedBy());
                                existingLeaveRegimeRequest.setIsPersisted();
                                return leaveRegimeRequestRepository
                                    .save(existingLeaveRegimeRequest)
                                    .map(res -> {
                                        var rs = leaveRegimeRequestMapper.toDto(res);
                                        rs.setProcessLeaveRegimeRequests(process);
                                        return rs;
                                    });
                            });
                        } else {
                            return Mono.just(existingLeaveRegimeRequestDTO);
                        }
                    }));
        }).switchIfEmpty(Mono.error(new BadRequestAlertException("ProcessLeaveRegimeRequest not found 2F",
            "LeaveRegimeRequest", "NOT_FOUND")));
    }

    public Mono<UUID> createLeaveRequest(LeaveRegimeRequestDTO leaveRegimeRequestDTO) {
        LeaveRequestDTO leaveRequestDTO = leaveRegimeRequestDTO.toLeaveRequestDTO();
        leaveRequestDTO.setTotalDayOff(leaveRegimeRequestDTO.getTotalDayOff());
        leaveRequestDTO.setFiles(leaveRegimeRequestDTO.getFilesJson());

        return leaveRequestService.saveLeaveRequestWithApprove(leaveRequestDTO).map(LeaveRequestDTO::getId);
    }

    /**
     * Get all the leaveRegimeRequests.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<LeaveRegimeRequestDTO> findAll(Pageable pageable) {
        log.debug("Request to get all LeaveRegimeRequests");
        return leaveRegimeRequestRepository.findAllBy(pageable).map(leaveRegimeRequestMapper::toDto);
    }

    /**
     * Returns the number of leaveRegimeRequests available.
     *
     * @return the number of entities in the database.
     */
    public Mono<Long> countAll() {
        return leaveRegimeRequestRepository.count();
    }

    /**
     * Get one leaveRegimeRequest by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<LeaveRegimeRequestDTO> findOne(UUID id) {
        log.debug("Request to get LeaveRegimeRequest : {}", id);
        return leaveRegimeRequestRepository.findById(id).map(leaveRegimeRequestMapper::toDto);
    }

    /**
     * Delete the leaveRegimeRequest by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(UUID id) {
        log.debug("Request to delete LeaveRegimeRequest : {}", id);
        return leaveRegimeRequestRepository.deleteById(id);
    }

    public Mono<Long> countAllWithQuery(LeaveRegimeRequestGetListDTO leaveRegimeRequestGetListDTO, String role) {
        return leaveRegimeRequestRepository.countByFilter(leaveRegimeRequestGetListDTO, role);
    }

    public Flux<LeaveRegimeRequestDTO> findAllWithQuery(Pageable pageable,
                                                        LeaveRegimeRequestGetListDTO leaveRegimeRequestGetListDTO, String role) {
        if (pageable.getSort().isUnsorted()) {
            log.debug("Sorting by createdAt desc");
            Sort sort = Sort.by(Sort.Order.desc("createdAt"));
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        }
        return leaveRegimeRequestRepository.findAllByFilter(pageable, leaveRegimeRequestGetListDTO, role)
            .map(leaveRegimeRequestMapper::toDto)
            .flatMap(header -> {
                return processLeaveRegimeRequestService.findAllProcessByLeaveRegimeRequestId(header.getId())
                    .collectList().flatMap(details -> {
                        header.setProcessLeaveRegimeRequests(details);
                        return Mono.just(header);
                    });
            });
    }

    public Mono<LeaveRegimeRequestDTO> findOneWithQuery(UUID id) {
        return leaveRegimeRequestRepository.findFirstByIsDeletedIsFalseAndId(id).map(leaveRegimeRequestMapper::toDto);
    }

    public Mono<Void> softDelete(UUID id, UUID deletedBy) {
        log.debug("Request to soft delete leaveRegimeRequest : {}", id);
        return leaveRegimeRequestRepository.findFirstByIsDeletedIsFalseAndId(id).flatMap(leaveRegimeRequest -> {
            if (leaveRegimeRequest == null) {
                return Mono.error(new BadRequestAlertException("leaveRegimeRequest already deleted",
                    "leaveRegimeRequest", "NOT_FOUND"));
            }

            if (!LeaveRegimeRequestStatus.CANCEL.equals(leaveRegimeRequest.getStatus())) {
                return Mono.error(new BadRequestAlertException("leaveRegimeRequest already deleted",
                    "leaveRegimeRequest", "DELETED"));
            }

            leaveRegimeRequest.setIsDeleted(true);
            leaveRegimeRequest.setDeletedBy(id);
            leaveRegimeRequest.setDeletedAt(ZonedDateTime.now());
            leaveRegimeRequest.setIsPersisted();
            return leaveRegimeRequestRepository.save(leaveRegimeRequest).then();
        });
    }

    public Mono<LeaveRegimeRequestDTO> cancelRequest(UUID id, UUID deletedBy) {
        log.debug("Request to soft delete leaveRegimeRequest : {}", id);
        return leaveRegimeRequestRepository.findFirstByIsDeletedIsFalseAndId(id).flatMap(leaveRegimeRequest -> {
            if (leaveRegimeRequest == null) {
                return Mono.error(new BadRequestAlertException("leaveRegimeRequest already deleted",
                    "leaveRegimeRequest", "NOT_FOUND"));
            }

            if (!LeaveRegimeRequestStatus.WAITING_APPROVAL.equals(leaveRegimeRequest.getStatus())) {
                return Mono.error(new BadRequestAlertException("leaveRegimeRequest already deleted",
                    "leaveRegimeRequest", "NOT_FOUND"));
            }

            leaveRegimeRequest.setStatus(LeaveRegimeRequestStatus.CANCEL);
            // leaveRegimeRequest.setIsDeleted(true);
            leaveRegimeRequest.setUpdatedBy(deletedBy);
            leaveRegimeRequest.setUpdatedAt(ZonedDateTime.now());
            leaveRegimeRequest.setIsPersisted();
            return leaveRegimeRequestRepository.save(leaveRegimeRequest).map(leaveRegimeRequestMapper::toDto);
        });
    }

    public Mono<LeaveRegimeRequestDTO> findFirstIsDeletedIsFalseAndId(UUID id) {
        return leaveRegimeRequestRepository.findNotDeleteById(id).map(leaveRegimeRequestMapper::toDto).flatMap(x -> {
            return processLeaveRegimeRequestRepository.findByLeaveRegimeRequestIdAndIsDeletedIsFalseQuery(x.getId())
                .map(processLeaveRegimeRequestMapper::toDto).collectList().flatMap(y -> {
                    x.setProcessLeaveRegimeRequests(y);
                    return Mono.just(x);
                });
        });
    }

}
