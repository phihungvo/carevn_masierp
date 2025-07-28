package com.masi.employee.service;

import com.carevn.masi.utils.SecurityUtils;
import com.masi.employee.domain.*;
import com.masi.employee.domain.enumeration.TimeKeepingType;
import com.masi.employee.domain.enumeration.TimeKeepingViolationType;
import com.masi.employee.domain.enumeration.WorkspaceType;
import com.masi.employee.repository.*;
import com.masi.employee.service.dto.EmployeeProfileQuery;
import com.masi.employee.service.dto.TimeKeepingViolationDTO;
import com.masi.employee.service.dto.TimeKeepingViolationFilter;
import com.masi.employee.service.dto.TimeKeepingViolationQuery;
import com.masi.employee.service.mapper.TimeKeepingViolationMapper;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;
import tech.jhipster.service.filter.StringFilter;
import tech.jhipster.service.filter.UUIDFilter;

/**
 * Service Implementation for managing
 * {@link com.masi.employee.domain.TimeKeepingViolation}.
 */
@Service
@Transactional
public class TimeKeepingViolationService {

    private final Logger log = LoggerFactory.getLogger(TimeKeepingViolationService.class);
    private final Float STANDARD_WORK_HOURS = 8.0f;
    private final TimeKeepingViolationRepository timeKeepingViolationRepository;
    private final EmployeeRepository employeeRepository;
    private final TimeKeepingRepository timeKeepingRepository;
    private final TimeKeepingService timeKeepingService;
    private final TimeKeepingViolationMapper timeKeepingViolationMapper;
    private final LeaveRequestService leaveRequestService;
    private final LeaveDayRepository leaveDayRepository;
    private final EmployeeProfileRepository employeeProfileRepository;
    private final EmployeeShiftDetailRepository employeeShiftDetailRepository;
    private final EmployeeShiftDetailService employeeShiftDetailService;
    private final ShiftRepository shiftRepository;

    public TimeKeepingViolationService(TimeKeepingViolationRepository timeKeepingViolationRepository, EmployeeRepository employeeRepository, TimeKeepingRepository timeKeepingRepository, TimeKeepingService timeKeepingService, TimeKeepingViolationMapper timeKeepingViolationMapper, LeaveRequestService leaveRequestService, LeaveDayRepository leaveDayRepository, EmployeeProfileRepository employeeProfileRepository, EmployeeShiftDetailRepository employeeShiftDetailRepository, EmployeeShiftDetailService employeeShiftDetailService, ShiftRepository shiftRepository) {
        this.timeKeepingViolationRepository = timeKeepingViolationRepository;
        this.employeeRepository = employeeRepository;
        this.timeKeepingRepository = timeKeepingRepository;
        this.timeKeepingService = timeKeepingService;
        this.timeKeepingViolationMapper = timeKeepingViolationMapper;
        this.leaveRequestService = leaveRequestService;
        this.leaveDayRepository = leaveDayRepository;
        this.employeeProfileRepository = employeeProfileRepository;
        this.employeeShiftDetailRepository = employeeShiftDetailRepository;
        this.employeeShiftDetailService = employeeShiftDetailService;
        this.shiftRepository = shiftRepository;
    }

    /**
     * Save a timeKeepingViolation.
     *
     * @param timeKeepingViolationDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<TimeKeepingViolationDTO> save(TimeKeepingViolationDTO timeKeepingViolationDTO) {
        log.debug("Request to save TimeKeepingViolation : {}", timeKeepingViolationDTO);
        timeKeepingViolationDTO.setCreatedAt(ZonedDateTime.now());
        timeKeepingViolationDTO.setLastUpdatedAt(ZonedDateTime.now());
        return timeKeepingViolationRepository.save(timeKeepingViolationMapper.toEntity(timeKeepingViolationDTO)).map(timeKeepingViolationMapper::toDto);
    }

    /**
     * Update a timeKeepingViolation.
     *
     * @param timeKeepingViolationDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<TimeKeepingViolationDTO> update(TimeKeepingViolationDTO timeKeepingViolationDTO) {
        log.debug("Request to update TimeKeepingViolation : {}", timeKeepingViolationDTO);
        return timeKeepingViolationRepository.save(timeKeepingViolationMapper.toEntity(timeKeepingViolationDTO).setIsPersisted()).map(timeKeepingViolationMapper::toDto);
    }

    /**
     * Partially update a timeKeepingViolation.
     *
     * @param timeKeepingViolationDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<TimeKeepingViolationDTO> partialUpdate(TimeKeepingViolationDTO timeKeepingViolationDTO) {
        log.debug("Request to partially update TimeKeepingViolation : {}", timeKeepingViolationDTO);

        return timeKeepingViolationRepository.findById(timeKeepingViolationDTO.getId()).map(existingTimeKeepingViolation -> {
            timeKeepingViolationMapper.partialUpdate(existingTimeKeepingViolation, timeKeepingViolationDTO);
            existingTimeKeepingViolation.setLastUpdatedAt(ZonedDateTime.now());
            return existingTimeKeepingViolation;
        }).flatMap(timeKeepingViolationRepository::save).map(timeKeepingViolationMapper::toDto);
    }

    /**
     * Get all the timeKeepingViolations.
     *
     * @return the list of entities.
     */
//    @Transactional(readOnly = true)
//    public Flux<TimeKeepingViolationDTO> findAll(Pageable pageable, TimeKeepingViolationQuery query) {
//        log.debug("Request to get all TimeKeepingViolations");
//        if (query == null) {
//            return timeKeepingViolationRepository.findAllByIsActive(true, pageable).map(TimeKeepingViolation::toDto);
//        }
//        TimeKeepingViolationFilter filter = createFilter(query);
//        log.debug("TimeKeepingViolationFilter : {}", filter);
//        return timeKeepingViolationRepository.findAllByFilter(pageable, filter)
//                .map(TimeKeepingViolation::toDto);
//    }

    @Transactional(readOnly = true)
    public Flux<TimeKeepingViolationDTO> findAll(Pageable pageable, TimeKeepingViolationQuery query) {
        log.debug("Request to get all TimeKeepingViolations");

        return SecurityUtils.getUserJWTDetail().flatMapMany(user -> {
            query.setCompany(String.valueOf(user.getCompanyId()));
            final Flux<TimeKeepingViolationDTO> resultFlux;
            if (query == null) {
                resultFlux = timeKeepingViolationRepository.findAllByIsActive(true, pageable)
                        .map(TimeKeepingViolation::toDto);
            } else {
                TimeKeepingViolationFilter filter = createFilter(query);
                log.debug("TimeKeepingViolationFilter : {}", filter);

                resultFlux = timeKeepingViolationRepository.findAllByFilter(pageable, filter)
                        .map(TimeKeepingViolation::toDto);
            }

            return resultFlux.collectList().flatMapMany(timeKeepingViolationDTOs -> {
                // group by employeeId and createdAt
                Map<Tuple2<UUID, LocalDate>, TimeKeepingViolationDTO> mergedMap = timeKeepingViolationDTOs.stream()
                        .collect(Collectors.toMap(
                                dto -> Tuples.of(dto.getEmployeeId(), dto.getCreatedAt().toLocalDate()),
                                dto -> dto,
                                (dto1, dto2) -> mergeViolations(dto1, dto2)
                        ));
                return Flux.fromIterable(mergedMap.values());
            });
        });
    }

    // function to merge two TimeKeepingViolationDTO
    private TimeKeepingViolationDTO mergeViolations(TimeKeepingViolationDTO dto1, TimeKeepingViolationDTO dto2) {
//        dto1.setIsActive(dto1.getIsActive() != null && dto1.getIsActive() ? true : dto2.getIsActive());
        return dto1;
    }

    /**
     * Returns the number of timeKeepingViolations available.
     *
     * @return the number of entities in the database.
     */
    public Mono<Long> countAll(TimeKeepingViolationQuery query) {
        if (query == null) {
            log.debug("Count - TimeKeepViolation - no filter");
            return timeKeepingViolationRepository.countAllByIsActive(true);
        }
        TimeKeepingViolationFilter filter = createFilter(query);
        log.debug("Count - TimeKeepViolation - filter = {}", filter);
        return timeKeepingViolationRepository.countAllByFilter(filter);
    }

    /**
     * Get one timeKeepingViolation by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<TimeKeepingViolationDTO> findOne(UUID id) {
        log.debug("Request to get TimeKeepingViolation : {}", id);
        return timeKeepingViolationRepository.findByIdAndIsActive(id, true).map(TimeKeepingViolation::toDto).doOnError(error -> {
            log.error("Error getting time keeping violation {}", id, error);
        });
    }

    /**
     * Get list of timeKeepingViolations by explanation id.
     *
     * @param id the id of the explanation to retrieve.
     * @return list of timeKeepingViolations.
     */
    @Transactional(readOnly = true)
    public Flux<TimeKeepingViolationDTO> findAllByExplanationId(UUID id, Pageable pageable) {
        log.debug("Request to get TimeKeepingViolation by explanation id : {}", id);
        return timeKeepingViolationRepository.findAllByExplanationId(id, pageable).collectList().flatMap(violations -> {
            // Get timeKeepingIds from violations
            Set<UUID> timeKeepingIds = violations.stream().map(TimeKeepingViolation::getTimeKeepingId).filter(Objects::nonNull).collect(Collectors.toSet());
            // map timeKeepingViolation to dto
            List<TimeKeepingViolationDTO> violationsDto = violations.stream().map(TimeKeepingViolation::toDto).collect(Collectors.toList());
            return timeKeepingRepository.findAllByIdIn(timeKeepingIds).collectList().map(timeKeepings -> {
                // map timeKeeping to violation
                violationsDto.forEach(violation -> {
                    timeKeepings.stream().filter(tk -> {
                        if (Objects.isNull(violation.getTimeKeepingId())) {
                            return false;
                        }
                        return violation.getTimeKeepingId().equals(tk.getId());
                    }).findFirst().ifPresent(timeKeeping -> {
                        violation.setTimeKeeping(timeKeeping.toDto());
                    });
                });
                return violationsDto;
            });
        }).flatMapMany(Flux::fromIterable);
    }

    /**
     * Count all the timeKeepingViolations by explanation id.
     *
     * @param id the id of the explanation to retrieve.
     * @return the number of entities in the database.
     */
    @Transactional(readOnly = true)
    public Mono<Long> countAllByExplanationId(UUID id) {
        return timeKeepingViolationRepository.countAllByExplanationId(id);
    }

    /**
     * Delete the timeKeepingViolation by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(UUID id) {
        log.debug("Request to delete TimeKeepingViolation : {}", id);
        return timeKeepingViolationRepository.deleteById(id);
    }

    //    @Scheduled(cron = "0 7 0 * * *", zone = "Asia/Ho_Chi_Minh")
//    @Scheduled(fixedRate = 1000 * 60 * 60 * 24)
/*    public Mono<Void> generateViolationRecord() {
        log.info("Starting to scan and check for time keeping violation...");
        final LocalDate startDate = LocalDate.now().withDayOfMonth(1);
        final LocalDate endDate = LocalDate.now();

        List<LocalDate> dates = startDate.datesUntil(endDate.plusDays(1)).collect(Collectors.toList());
        var employeeQuery = new EmployeeProfileQuery();
        employeeQuery.setWorkspaceTypes(List.of(WorkspaceType.OFFICE));
        return Flux.fromIterable(dates)
            .flatMap(currentDate -> {
                return employeeProfileRepository.findAllByQuery(employeeQuery, null)
                    .flatMap(employee -> timeKeepingRepository
                        .findByEmployeeIdAndDateAndTimeKeepingType(employee.getId(), currentDate, TimeKeepingType.HOUR)
                        .hasElement()
                        .flatMap(hasTimeKeeping -> {
                            if (Boolean.TRUE.equals(hasTimeKeeping)) {
                                return timeKeepingRepository
                                    .findByEmployeeIdAndDateAndTimeKeepingType(employee.getId(), currentDate, TimeKeepingType.HOUR)
                                    .flatMap(timeKeeping -> processTimeKeeping(timeKeeping, employee.toShortEmployee())
                                        .then(Mono.just(timeKeeping))
                                        .flatMap(t -> {
                                            return timeKeepingRepository.save(reEvaluate(t));
                                        }))
                                    .onErrorResume(error -> {
                                        log.error("Error processing time keeping of employee {} on {}",
                                            employee.getId(), currentDate, error);
                                        return Mono.empty();
                                    });
                            } else {
                                return leaveRequestService
                                    .getByEmployeeIdAndDate(employee.getId(), currentDate)
                                    .flatMap(leaveRequest -> Mono.empty())
                                    .switchIfEmpty(
                                        timeKeepingService
                                            .createNewAbsentRecord(employee,
                                                currentDate)
                                            .flatMap(newTimeKeeping -> processTimeKeeping(
                                                newTimeKeeping, employee.toShortEmployee())))
                                    .onErrorResume(error -> {
                                        log.error("Error processing leave request of employee {} on {}",
                                            employee.getId(), currentDate, error);
                                        return Mono.empty();
                                    });
                            }
                        }));
            }, 1)
            .then(
                timeKeepingRepository.updateAllTimekeepingTimesheets());
    }*/

    public Mono<Void> makeSureTimekeepingExist(Collection<EmployeeShiftDetail> employeeShiftDetails) {
        var needNewTimekeeping = employeeShiftDetails.stream().filter(employeeShiftDetail -> employeeShiftDetail.getTimeKeepingId() == null).toList();
        return Flux.fromIterable(needNewTimekeeping).flatMap(employeeShiftDetail -> {
            var timeKeeping = new TimeKeeping();
            timeKeeping.setId(UUID.randomUUID());
            employeeShiftDetail.setTimeKeepingId(timeKeeping.getId());
            timeKeeping.applyCharacteristics("NULL");
            timeKeeping.setEmployeeId(employeeShiftDetail.getEmployeeId());
            timeKeeping.setDate(employeeShiftDetail.getDate());
            timeKeeping.setTimeKeepingType(TimeKeepingType.HOUR);
            return timeKeepingRepository.save(timeKeeping).then(employeeShiftDetailRepository.save(employeeShiftDetail.setIsPersisted()));
        }).then();
    }

    public Mono<Void> generateViolationRecord() {
        log.info("Starting to scan and check for time keeping violation...");
        final LocalDate startDate = LocalDate.now().withDayOfMonth(1);
        final LocalDate endDate = LocalDate.now();

        return employeeShiftDetailRepository.findAllByIsViolationType(startDate, endDate).collectList()
            .flatMap(employeeShiftDetails -> this.makeSureTimekeepingExist(employeeShiftDetails).thenReturn(employeeShiftDetails))
            .flatMap(employeeShiftDetails -> {
                Collection<UUID> shiftIds = employeeShiftDetails.stream().map(EmployeeShiftDetail::getShiftId).collect(Collectors.toSet());

                return shiftRepository.findAllById(shiftIds).collectList().flatMap(shifts -> {
                    Set<UUID> seenIds = new HashSet<>();
                    Map<UUID, Shift> shiftMap = new HashMap<>();
                    for (Shift shift : shifts) {
                        if (seenIds.add(shift.getId())) {
                            shiftMap.put(shift.getId(), shift);
                        }
                    }

                    return Flux.fromIterable(employeeShiftDetails).flatMap(employeeShift -> {
                        if (!employeeShift.getIsShiftOff()) {
                            return Mono.empty();
                        }

                        TimeKeepingViolation violation = new TimeKeepingViolation();
                        violation.setId(UUID.randomUUID());
                        violation.setIsActive(true);
                        violation.setEmployeeId(employeeShift.getEmployeeId());
                        violation.setTimeKeepingId(employeeShift.getTimeKeepingId());
                        violation.setCreatedAt(ZonedDateTime.now());
                        violation.setLastUpdatedAt(ZonedDateTime.now());

                        Shift matchingShift = shiftMap.get(employeeShift.getShiftId());

                        return getViolationType(employeeShift, matchingShift).flatMap(violationType -> {
                            violation.setType(violationType);

                            return employeeShiftDetailRepository.updateIdViolation(employeeShift.getId(), violation.getId(), violation.getType()).thenMany(timeKeepingViolationRepository.deleteByTimeKeepingId(employeeShift.getTimeKeepingId())).then(timeKeepingViolationRepository.save(violation));
                        });
                    }).then();
                });
            });
    }


    private Mono<TimeKeepingViolationType> getViolationType(EmployeeShiftDetail employeeShiftDetail, Shift shift) {
        if (employeeShiftDetail.getViolationType() != null) {
            return Mono.just(employeeShiftDetail.getViolationType());
        }

        if (employeeShiftDetail.getCheckInTime() == null) {
            return Mono.just(TimeKeepingViolationType.ABSENT_WITHOUT_REQUEST);
        }
        if (employeeShiftDetail.getCheckOutTime() == null) {
            return Mono.just(TimeKeepingViolationType.MISSING_CHECKOUT);
        }

        return employeeShiftDetailService.calculateDurationHours(employeeShiftDetail.getCheckInTime(), employeeShiftDetail.getCheckOutTime(), shift).map(durationHoursEmployee -> {
            if (durationHoursEmployee < shift.getDurationHours()) {
                return TimeKeepingViolationType.INSUFFICIENT_WORKING_TIME;
            }
            return TimeKeepingViolationType.OVERTIME;
        });
    }


    private TimeKeeping reEvaluate(final TimeKeeping timeKeeping) {
        if (timeKeeping.hasInsufficientWorkedHours()) {
            timeKeeping.setViolationType(TimeKeepingViolationType.INSUFFICIENT_WORKING_TIME);
        } else if (timeKeeping.isAbsent()) {
            timeKeeping.setViolationType(TimeKeepingViolationType.ABSENT_WITHOUT_REQUEST);
        }
        return timeKeeping;
    }

    private Mono<Void> processTimeKeeping(TimeKeeping timeKeeping, Employee employee) {
        log.debug("Found existing time keeping data for employee {}: {}", employee.getId(), timeKeeping);
        return timeKeepingViolationRepository.findByTimeKeepingId(timeKeeping.getId()).flatMap(existingViolation -> {
//                log.info("Existing violation found for time keeping {} of user {}", timeKeeping.getId(),
//                    employee.getId());
            if (timeKeeping.hasValidWorkedHours()) {
                timeKeeping.setViolation(null);
                return timeKeepingViolationRepository.deleteByTimeKeepingId(timeKeeping.getId()).then(Mono.just(new Object()));
            }
            return Mono.empty();
        }).switchIfEmpty(Mono.defer(() -> {
            if (!validTimeKeeping(timeKeeping)) {
//                    log.info("User {} has invalid time keeping data {}", employee.getId(), timeKeeping);
                return timeKeepingViolationRepository.findByTimeKeepingId(timeKeeping.getId()).hasElement().flatMap(hasViolation -> {
                    if (Boolean.FALSE.equals(hasViolation)) {
                        return save(buildCreateDto(employee, timeKeeping)).then(Mono.empty());
                    }
//                            log.info("Violation already exists for user {}", employee.getId());
                    return Mono.empty();
                }).onErrorResume(error -> {
                    log.error("Error processing time keeping {} of user {}", timeKeeping.getId(), employee.getId(), error);
                    return Mono.empty();
                });
            }
            log.debug("Time keeping data is valid for employee {}", employee.getId());
            return Mono.empty();
        })).onErrorResume(error -> {
            log.error("Error processing time keeping {} of user {}", timeKeeping.getId(), employee.getId(), error);
            return Mono.empty();
        }).then();
    }

    private TimeKeepingViolationDTO buildCreateDto(Employee employee, TimeKeeping timeKeeping) {
        TimeKeepingViolationDTO dto = new TimeKeepingViolationDTO();
        dto.setId(UUID.randomUUID());
        dto.setTimeKeeping(timeKeeping.toDto());
        dto.setEmployee(employee.toDto());
        dto.setTimeKeepingId(timeKeeping.getId());
        dto.setEmployeeId(employee.getId());
        dto.setType(calculateViolationType(timeKeeping));
        dto.setIsActive(true);
        return dto;
    }

    private boolean validTimeKeeping(TimeKeeping timeKeeping) {
        return TimeKeepingViolationType.NO_VIOLATION.equals(calculateViolationType(timeKeeping));
    }

    private TimeKeepingViolationType calculateViolationType(TimeKeeping timeKeeping) {
        if (timeKeeping.isAbsent()) {
            return TimeKeepingViolationType.ABSENT_WITHOUT_REQUEST;
        }
        if (timeKeeping.isMissingCheckout()) {
            return TimeKeepingViolationType.MISSING_CHECKOUT; // Recognize this as invalid without creating a new
            // violation
        }
        if (timeKeeping.getHoursWorked() < getStandardWorkHours()) {
            return TimeKeepingViolationType.INSUFFICIENT_WORKING_TIME;
        }
        return TimeKeepingViolationType.NO_VIOLATION;
    }

    private float getStandardWorkHours() {
        // TODO: get standard work hours by workplace
        return STANDARD_WORK_HOURS;
    }

    private TimeKeepingViolationFilter createFilter(TimeKeepingViolationQuery query) {
        TimeKeepingViolationFilter filter = new TimeKeepingViolationFilter();
        filter.setIsActive(true);
        if (query.getFromDate() != null && query.getToDate() != null) {
            filter.fromDate().setGreaterThanOrEqual(query.getFromDate());
            filter.toDate().setLessThanOrEqual(query.getToDate());
        }
        if (query.getType() != null) {
            filter.type().setIn(query.getType());
        }
        if (query.getEmployeeIds() != null) {
            filter.employeeIds().setIn(query.getEmployeeIds());
        }
        if (query.getWorkspaceIds() != null) {
            filter.workspaceIds().setIn(query.getWorkspaceIds());
        }
        if (query.getCompany() != null) {
            filter.setCompany(new StringFilter());
            filter.getCompany().setEquals(query.getCompany());
        }
        if (query.getExplanationId() != null) {
            filter.setExplanationId(new UUIDFilter());
            filter.getExplanationId().setEquals(query.getExplanationId());
        }
        if (Boolean.FALSE.equals(query.getExplained())) {
            filter.setExplanationId(new UUIDFilter());
            filter.getExplanationId().setSpecified(false);
        }
        if (Boolean.TRUE.equals(query.getExplained())) {
            filter.setExplanationId(new UUIDFilter());
            filter.getExplanationId().setSpecified(true);
        }
        return filter;
    }

    public Mono<Void> unlinkExplanation(UUID explanationId) {
        return timeKeepingViolationRepository.findByExplanationId(explanationId).flatMap(violation -> {
            violation.setExplanationId(null);
            violation.setIsPersisted();
            return timeKeepingViolationRepository.save(violation);
        }).then();
    }
}
