package com.masi.employee.service;

import com.masi.employee.domain.*;
import com.masi.employee.domain.criteria.ShiftCriteria;
import com.masi.employee.domain.enumeration.LeaveRequestDayType;
import com.masi.employee.domain.enumeration.WorkspaceType;
import com.masi.employee.repository.*;
import com.masi.employee.service.dto.ShiftDTO;
import com.masi.employee.service.mapper.ShiftMapper;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.masi.employee.domain.Shift}.
 */
@Service
@Transactional
public class ShiftService {

    private static final Logger log = LoggerFactory.getLogger(ShiftService.class);

    private final ShiftRepository shiftRepository;

    private final ShiftMapper shiftMapper;

    private final EmployeeProfileRepository employeeProfileRepository;

    private final EmployeeShiftRepository employeeShiftRepository;

    private final EmployeeShiftDetailRepository employeeShiftDetailRepository;

    private final LeaveRegimeRequestRepository leaveRequestRepository;

    public ShiftService(ShiftRepository shiftRepository, ShiftMapper shiftMapper, EmployeeProfileRepository employeeProfileRepository, EmployeeShiftRepository employeeShiftRepository, EmployeeShiftDetailRepository employeeShiftDetailRepository, LeaveRegimeRequestRepository leaveRequestRepository) {
        this.shiftRepository = shiftRepository;
        this.shiftMapper = shiftMapper;
        this.employeeProfileRepository = employeeProfileRepository;
        this.employeeShiftRepository = employeeShiftRepository;
        this.employeeShiftDetailRepository = employeeShiftDetailRepository;
        this.leaveRequestRepository = leaveRequestRepository;
    }

    /**
     * Save a shift.
     *
     * @param shiftDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ShiftDTO> save(ShiftDTO shiftDTO) {
        log.debug("Request to save Shift : {}", shiftDTO);
        return shiftRepository.save(shiftMapper.toEntity(shiftDTO)).map(shiftMapper::toDto);
    }

    /**
     * Update a shift.
     *
     * @param shiftDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ShiftDTO> update(ShiftDTO shiftDTO) {
        log.debug("Request to update Shift : {}", shiftDTO);
        return shiftRepository.save(shiftMapper.toEntity(shiftDTO).setIsPersisted()).map(shiftMapper::toDto);
    }

    /**
     * Partially update a shift.
     *
     * @param shiftDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<ShiftDTO> partialUpdate(ShiftDTO shiftDTO) {
        log.debug("Request to partially update Shift : {}", shiftDTO);

        return shiftRepository
            .findById(shiftDTO.getId())
            .map(existingShift -> {
                shiftMapper.partialUpdate(existingShift, shiftDTO);

                return existingShift;
            })
            .flatMap(shiftRepository::save)
            .map(shiftMapper::toDto);
    }

    /**
     * Find shifts by Criteria.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<ShiftDTO> findByCriteria(ShiftCriteria criteria, Pageable pageable) {
        log.debug("Request to get all Shifts by Criteria");
        return shiftRepository.findByCriteria(criteria, pageable).map(shiftMapper::toDto);
    }

    /**
     * Find the count of shifts by criteria.
     *
     * @param criteria filtering criteria
     * @return the count of shifts
     */
    public Mono<Long> countByCriteria(ShiftCriteria criteria) {
        log.debug("Request to get the count of all Shifts by Criteria");
        return shiftRepository.countByCriteria(criteria);
    }

    /**
     * Returns the number of shifts available.
     *
     * @return the number of entities in the database.
     */
    public Mono<Long> countAll() {
        return shiftRepository.count();
    }

    /**
     * Get one shift by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<ShiftDTO> findOne(UUID id) {
        log.debug("Request to get Shift : {}", id);
        return shiftRepository.findById(id).map(shiftMapper::toDto);
    }

    /**
     * Delete the shift by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(UUID id) {
        return null;
    }


    public Mono<Map<String, Object>> autoGenerateShiftsEmployee(String company) {
        return employeeShiftRepository.findAllDistinctByEmployeeId(company)
            .map(EmployeeShift::getEmployeeId)
            .collectList()
            .flatMap(employeeIds -> {
                    // Add Employee to avoid SQL error
                    employeeIds.add(UUID.fromString("cd8cfb29-138e-44c9-a1a4-fe6fe11c1fce"));
                    return employeeProfileRepository
                        .findAllByCompanyAndWorkspaceType(company, WorkspaceType.OFFICE, employeeIds)
                        .collectList();
                }
            )
            .flatMap(employeeProfiles ->
                Flux.fromIterable(employeeProfiles)
                    .flatMap(employeeProfile ->
                        generateShiftsForEmployee(employeeProfile.getId(), employeeProfile.getCompany())
                            .thenReturn(employeeProfile.getId()) // Trả về ID sau khi tạo xong shift
                    )
                    .collectList()
            )
            .map(generatedEmployeeIds -> {
                Map<String, Object> result = new HashMap<>();
                result.put("generatedEmployeeIds", generatedEmployeeIds);
                return result;
            });
    }

    public Mono<Void> generateShiftsForEmployee(UUID employeeId, String company) {
        return shiftRepository.findAllByCompany(company)
            .flatMap(shift -> {
                EmployeeShift employeeShift = new EmployeeShift();
                employeeShift.setId(UUID.randomUUID());
                employeeShift.setEmployeeId(employeeId);
                employeeShift.setShiftId(shift.getId());
                employeeShift.setCompany(company);
                employeeShift.setIsDeleted(false);
                return employeeShiftRepository.save(employeeShift);
            })
            .then();
    }

//    public Mono<Map<String, Object>> autoGenerateDailyShiftsEmployee(Collection<UUID> shiftIds, LocalDate date) {
//        return shiftRepository.findAllByIdStandardWorkScheduleConfigIn(shiftIds)
//                .collectList()
//                .flatMap(shifts -> {
//                    if (shifts.isEmpty()) {
//                        return Mono.just(Collections.singletonMap("message", "No suitable shifts found"));
//                    }
//                    Collection<UUID> listIdShifts = shifts.stream().map(Shift::getId).toList();
//                    LocalDate dateCheck = LocalDate.of(date.getYear(), date.getMonthValue(), date.getDayOfMonth());
//                    return employeeShiftDetailRepository.deleteAllByShiftIdInAndDate(listIdShifts, dateCheck)
//                            .thenMany(Flux.fromIterable(shifts)
//                                    .flatMap(shift ->
//                                            employeeShiftRepository.findAllByShiftId(shift.getId())
//                                                    .flatMap(employeeShift -> {
//                                                        log.info("Generating shift detail for shiftId: {}", shift.getId());
//                                                        EmployeeShiftDetail employeeShiftDetail = new EmployeeShiftDetail();
//                                                        employeeShiftDetail.setId(UUID.randomUUID());
//                                                        employeeShiftDetail.setShiftId(shift.getId());
//                                                        employeeShiftDetail.setDate(dateCheck);
//                                                        employeeShiftDetail.setEmployeeId(employeeShift.getEmployeeId());
//                                                        return employeeShiftDetailRepository.save(employeeShiftDetail)
//                                                                .map(EmployeeShiftDetail::getId);
//                                                    })
//                                    )
//                            )
//                            .collectList()
//                            .map(savedIds -> {
//                                Map<String, Object> responseMap = new HashMap<>();
//                                responseMap.put("savedIds", savedIds);
//                                return responseMap;
//                            });
//                });
//    }

 /*   public Mono<Map<String, Object>> autoGenerateDailyShiftsEmployee(String company, Collection<UUID> shiftIds, LocalDate date, Boolean override) {
        return shiftRepository.findAllByIdStandardWorkScheduleConfigIn(shiftIds, company)
                .collectList()
                .flatMap(shifts -> {
                    if (shifts.isEmpty()) {
                        return Mono.just(Collections.singletonMap("message", "No suitable shifts found"));
                    }

                    // Get list of shift IDs
                    log.info("Shifts: {}", shifts);
                    Collection<UUID> listIdShifts = shifts.stream().map(Shift::getId).toList();
                    LocalDate dateCheck = date; // Không cần tạo LocalDate mới

                    return employeeShiftDetailRepository.deleteAllByShiftIdInAndDate(listIdShifts, dateCheck)
                            .thenMany(Flux.fromIterable(shifts))
                            .collectList()
                            .flatMap(listShifts ->
                                    employeeShiftRepository.findAllByShiftIds(listIdShifts)
                                            .collectList()
                                            .flatMap(employeeShifts -> {
                                                log.info("Employee shifts: {}", employeeShifts);

                                                // Get list of employee IDs
                                                Collection<UUID> employeeHaveShift = employeeShifts.stream()
                                                        .map(EmployeeShift::getEmployeeId)
                                                        .distinct()
                                                        .toList();

                                                // Find leave request for employee
                                                return leaveRequestRepository.findByEmployeeIsHaveLeaveRequestToDate(employeeHaveShift, dateCheck)
                                                        .map(leaveRequest -> {
                                                            log.info("Leave request found for employeeId: {}", leaveRequest.getEmployeeId());

                                                            EmployeeHaveLeaveRequestToDate employeeLeaveData = new EmployeeHaveLeaveRequestToDate();
                                                            employeeLeaveData.setEmployeeId(leaveRequest.getEmployeeId());

                                                            // Handle leave request and shift assignment
                                                            Collection<UUID> shiftIdsToAssign;
                                                            if (leaveRequest.getLeaveRequestDayType().equals(LeaveRequestDayType.FULL_DAY)) {
                                                                shiftIdsToAssign = listShifts.stream()
                                                                        .map(Shift::getId)
                                                                        .toList();
                                                            } else if (leaveRequest.getFromTime().getHour() == 8) {
                                                                shiftIdsToAssign = listShifts.stream()
                                                                        .filter(shift -> shift.getHourStartTime() == 8)
                                                                        .map(Shift::getId)
                                                                        .findFirst()
                                                                        .stream()
                                                                        .toList();
                                                            } else {
                                                                shiftIdsToAssign = listShifts.stream()
                                                                        .filter(shift -> shift.getHourStartTime() == 13)
                                                                        .map(Shift::getId)
                                                                        .findFirst()
                                                                        .stream()
                                                                        .toList();
                                                            }
                                                            employeeLeaveData.setShiftIds(shiftIdsToAssign);

                                                            return employeeLeaveData;
                                                        })
                                                        .collectList()
                                                        .flatMapMany(employeeLeaveDataList -> {
                                                            // Get list of employee IDs
                                                            Set<UUID> leaveEmployeeIds = employeeLeaveDataList.stream()
                                                                    .map(EmployeeHaveLeaveRequestToDate::getEmployeeId)
                                                                    .collect(Collectors.toSet());

                                                            // Filter shifts that are not in the list of employees in the leave request
                                                            return Flux.fromIterable(shifts)
                                                                    .flatMap(shift ->
                                                                            employeeShiftRepository.findAllByShiftId(shift.getId())
//                                                                                    .filter(employeeShift ->
//                                                                                            !leaveEmployeeIds.contains(employeeShift.getEmployeeId())
//                                                                                                    && employeeLeaveDataList.stream().noneMatch(
//                                                                                                    leaveData -> leaveData.getEmployeeId().equals(employeeShift.getEmployeeId())
//                                                                                                            && leaveData.getShiftIds().contains(shift.getId())
//                                                                                            )
//                                                                                    )
                                                                                    .filter(employeeShift ->
                                                                                            employeeLeaveDataList.stream()
                                                                                                    .filter(leaveData -> leaveData.getEmployeeId().equals(employeeShift.getEmployeeId()))
                                                                                                    .noneMatch(leaveData -> leaveData.getShiftIds().contains(shift.getId()))
                                                                                    )

                                                                                    .flatMap(employeeShift -> {
                                                                                        log.info("Generating shift detail for employeeId: {}, shiftId: {}", employeeShift.getEmployeeId(), shift.getId());

                                                                                        EmployeeShiftDetail shiftDetail = new EmployeeShiftDetail();
                                                                                        shiftDetail.setId(UUID.randomUUID());
                                                                                        shiftDetail.setShiftId(shift.getId());
                                                                                        shiftDetail.setDate(dateCheck);
                                                                                        shiftDetail.setEmployeeId(employeeShift.getEmployeeId());

                                                                                        return employeeShiftDetailRepository.save(shiftDetail)
                                                                                                .map(EmployeeShiftDetail::getId);
                                                                                    })
                                                                    );
                                                        })
                                                        .collectList()
                                                        .map(savedIds -> {
                                                            Map<String, Object> responseMap = new HashMap<>();
                                                            responseMap.put("savedIds", savedIds);
                                                            return responseMap;
                                                        });
                                            })
                            );
                });
    }*/

    public Mono<Map<String, Object>> autoGenerateDailyShiftsEmployee(String company, Collection<UUID> shiftIds, LocalDate date, Boolean override) {
        Flux<Shift> shiftPromises = Flux.empty();
        if (CollectionUtils.isNotEmpty(shiftIds)) {
            shiftPromises = shiftRepository.findAllByIdStandardWorkScheduleConfigIn(shiftIds, company);
        }
        return shiftPromises
            .collectList()
            .flatMap(shifts -> {
                if (shifts.isEmpty()) {
                    return Mono.just(Collections.singletonMap("message", "No suitable shifts found"));
                }

                // Get list of shift IDs
                log.info("Shifts: {}", shifts);
                Collection<UUID> listIdShifts = shifts.stream().map(Shift::getId).toList();
                Flux<EmployeeShiftDetail> employeeShiftDetailFlux = Flux.empty();
                if (CollectionUtils.isNotEmpty(listIdShifts)) {
                    employeeShiftDetailFlux = employeeShiftDetailRepository.findAllByShiftIdInAndDate(listIdShifts, date);
                }
                return employeeShiftDetailFlux
                    .collectList()
                    .flatMap(existingShifts -> {
                        Collection<UUID> listIdShiftsToDelete;

                        if (!existingShifts.isEmpty()) {
                            log.info("Shifts already exist for date: {}", date);
                            listIdShiftsToDelete = listIdShifts; // if shifts exist, delete all shifts
                        } else {
                            log.info("Shifts do not exist for date: {}", date);
                            if (override) {
                                listIdShiftsToDelete = listIdShifts; // if override is true, delete all shifts
                            } else {
                                listIdShiftsToDelete = existingShifts.stream().map(EmployeeShiftDetail::getShiftId).toList(); // Get list of shifts to delete
                            }
                        }

                        // Delete existing shifts
                        Mono<Integer> deletePromise = Mono.just(0);
                        if (CollectionUtils.isNotEmpty(listIdShiftsToDelete)) {
                            deletePromise = employeeShiftDetailRepository.deleteAllByShiftIdInAndDate(listIdShiftsToDelete, date);
                        }
                        Flux<EmployeeShift> employeeShiftFlux;
                        if (CollectionUtils.isNotEmpty(listIdShiftsToDelete)) {
                            employeeShiftFlux = employeeShiftRepository.findAllByShiftIds(listIdShiftsToDelete);
                        } else {
                            employeeShiftFlux = Flux.empty();
                        }

                        return deletePromise
                            .thenMany(Flux.fromIterable(shifts))
                            .collectList()
                            .flatMap(listShifts ->
                                employeeShiftFlux
                                    .collectList()
                                    .flatMap(employeeShifts -> {
                                        log.info("Employee shifts: {}", employeeShifts);

                                        // Get list of employee IDs
                                        Collection<UUID> employeeHaveShift = employeeShifts.stream()
                                            .map(EmployeeShift::getEmployeeId)
                                            .distinct()
                                            .toList();
                                        Flux<LeaveRegimeRequest> haveShiftPromise;
                                        if (CollectionUtils.isNotEmpty(employeeHaveShift)) {
                                            haveShiftPromise = leaveRequestRepository.findByEmployeeIsHaveLeaveRequestToDate(employeeHaveShift, date);
                                        } else {
                                            haveShiftPromise = Flux.empty();
                                        }
                                        // Find leave request for employee
                                        return haveShiftPromise
                                            .flatMap(leaveRequest -> {
                                                log.info("Leave request found for employeeId: {}", leaveRequest.getEmployeeId());

                                                EmployeeHaveLeaveRequestToDate employeeLeaveData = new EmployeeHaveLeaveRequestToDate();
                                                employeeLeaveData.setEmployeeId(leaveRequest.getEmployeeId());

                                                // Handle leave request and shift assignment
                                                Collection<UUID> shiftIdsToAssign;
                                                if (leaveRequest.getLeaveRequestDayType().equals(LeaveRequestDayType.FULL_DAY)) {
                                                    shiftIdsToAssign = listShifts.stream()
                                                        .map(Shift::getId)
                                                        .toList();
                                                } else if (leaveRequest.getFromTime().getHour() == 8) {
                                                    shiftIdsToAssign = listShifts.stream()
                                                        .filter(shift -> shift.getHourStartTime() == 8 && shift.getId() != null)
                                                        .map(Shift::getId)
                                                        .findFirst()
                                                        .stream()
                                                        .toList();
                                                } else {
                                                    shiftIdsToAssign = listShifts.stream()
                                                        .filter(shift -> shift.getHourStartTime() == 13 && shift.getId() != null)
                                                        .map(Shift::getId)
                                                        .findFirst()
                                                        .stream()
                                                        .toList();
                                                }
                                                employeeLeaveData.setShiftIds(shiftIdsToAssign);

                                                return Mono.just(employeeLeaveData); // Trả về dữ liệu nghỉ phép
                                            })
                                            .collectList()
                                            .flatMapMany(employeeLeaveDataList -> {
                                                // Get list of employee IDs
                                                Set<UUID> leaveEmployeeIds = employeeLeaveDataList.stream()
                                                    .map(EmployeeHaveLeaveRequestToDate::getEmployeeId)
                                                    .collect(Collectors.toSet());

                                                // Filter shifts that are not in the list of employees in the leave request
                                                return Flux.fromIterable(shifts)
                                                    .flatMap(shift -> employeeShiftRepository.findAllByShiftId(shift.getId())
                                                        .filter(employeeShift ->
                                                            employeeLeaveDataList.stream()
                                                                .filter(leaveData -> leaveData.getEmployeeId().equals(employeeShift.getEmployeeId()))
                                                                .noneMatch(leaveData -> leaveData.getShiftIds().contains(shift.getId()))
                                                        )
                                                        .flatMap(employeeShift -> {
                                                            log.info("Generating shift detail for employeeId: {}, shiftId: {}", employeeShift.getEmployeeId(), shift.getId());

                                                            EmployeeShiftDetail shiftDetail = new EmployeeShiftDetail();
                                                            shiftDetail.setId(UUID.randomUUID());
                                                            shiftDetail.setShiftId(shift.getId());
                                                            shiftDetail.setDate(date);
                                                            shiftDetail.setEmployeeId(employeeShift.getEmployeeId());

                                                            return employeeShiftDetailRepository.save(shiftDetail)
                                                                .map(EmployeeShiftDetail::getId);
                                                        })
                                                    );
                                            })
                                            .collectList()
                                            .map(savedIds -> {
                                                Map<String, Object> responseMap = new HashMap<>();
                                                responseMap.put("savedIds", savedIds);
                                                return responseMap;
                                            });
                                    }));
                    });
            });
    }

    public Mono<Void> autoGenerateDailyShiftsEmployeeReturnVoid(String company, Collection<UUID> shiftIds, LocalDate date, Boolean override) {
        Flux<Shift> shiftPromises = Flux.empty();
        if (CollectionUtils.isNotEmpty(shiftIds)) {
            shiftPromises = shiftRepository.findAllByIdStandardWorkScheduleConfigIn(shiftIds, company);
        }
        return shiftPromises
                .collectList()
                .flatMap(shifts -> {
                    if (shifts.isEmpty()) {
                        return Mono.just(Collections.singletonMap("message", "No suitable shifts found"));
                    }

                    // Get list of shift IDs
                    log.info("Shifts: {}", shifts);
                    Collection<UUID> listIdShifts = shifts.stream().map(Shift::getId).toList();
                    Flux<EmployeeShiftDetail> employeeShiftDetailFlux = Flux.empty();
                    if (CollectionUtils.isNotEmpty(listIdShifts)) {
                        employeeShiftDetailFlux = employeeShiftDetailRepository.findAllByShiftIdInAndDate(listIdShifts, date);
                    }
                    return employeeShiftDetailFlux
                            .collectList()
                            .flatMap(existingShifts -> {
                                Collection<UUID> listIdShiftsToDelete;

                                if (!existingShifts.isEmpty()) {
                                    log.info("Shifts already exist for date: {}", date);
                                    listIdShiftsToDelete = listIdShifts; // if shifts exist, delete all shifts
                                } else {
                                    log.info("Shifts do not exist for date: {}", date);
                                    if (override) {
                                        listIdShiftsToDelete = listIdShifts; // if override is true, delete all shifts
                                    } else {
                                        listIdShiftsToDelete = existingShifts.stream().map(EmployeeShiftDetail::getShiftId).toList(); // Get list of shifts to delete
                                    }
                                }

                                // Delete existing shifts
                                Mono<Integer> deletePromise = Mono.just(0);
                                if (CollectionUtils.isNotEmpty(listIdShiftsToDelete)) {
                                    deletePromise = employeeShiftDetailRepository.deleteAllByShiftIdInAndDate(listIdShiftsToDelete, date);
                                }
                                Flux<EmployeeShift> employeeShiftFlux;
                                if (CollectionUtils.isNotEmpty(listIdShiftsToDelete)) {
                                    employeeShiftFlux = employeeShiftRepository.findAllByShiftIds(listIdShiftsToDelete);
                                } else {
                                    employeeShiftFlux = Flux.empty();
                                }

                                return deletePromise
                                        .thenMany(Flux.fromIterable(shifts))
                                        .collectList()
                                        .flatMap(listShifts ->
                                                employeeShiftFlux
                                                        .collectList()
                                                        .flatMap(employeeShifts -> {
                                                            log.info("Employee shifts: {}", employeeShifts);

                                                            // Get list of employee IDs
                                                            Collection<UUID> employeeHaveShift = employeeShifts.stream()
                                                                    .map(EmployeeShift::getEmployeeId)
                                                                    .distinct()
                                                                    .toList();
                                                            Flux<LeaveRegimeRequest> haveShiftPromise;
                                                            if (CollectionUtils.isNotEmpty(employeeHaveShift)) {
                                                                haveShiftPromise = leaveRequestRepository.findByEmployeeIsHaveLeaveRequestToDate(employeeHaveShift, date);
                                                            } else {
                                                                haveShiftPromise = Flux.empty();
                                                            }
                                                            // Find leave request for employee
                                                            return haveShiftPromise
                                                                    .flatMap(leaveRequest -> {
                                                                        log.info("Leave request found for employeeId: {}", leaveRequest.getEmployeeId());

                                                                        EmployeeHaveLeaveRequestToDate employeeLeaveData = new EmployeeHaveLeaveRequestToDate();
                                                                        employeeLeaveData.setEmployeeId(leaveRequest.getEmployeeId());

                                                                        // Handle leave request and shift assignment
                                                                        Collection<UUID> shiftIdsToAssign;
                                                                        if (leaveRequest.getLeaveRequestDayType().equals(LeaveRequestDayType.FULL_DAY)) {
                                                                            shiftIdsToAssign = listShifts.stream()
                                                                                    .map(Shift::getId)
                                                                                    .toList();
                                                                        } else if (leaveRequest.getFromTime().getHour() == 8) {
                                                                            shiftIdsToAssign = listShifts.stream()
                                                                                    .filter(shift -> shift.getHourStartTime() == 8 && shift.getId() != null)
                                                                                    .map(Shift::getId)
                                                                                    .findFirst()
                                                                                    .stream()
                                                                                    .toList();
                                                                        } else {
                                                                            shiftIdsToAssign = listShifts.stream()
                                                                                    .filter(shift -> shift.getHourStartTime() == 13 && shift.getId() != null)
                                                                                    .map(Shift::getId)
                                                                                    .findFirst()
                                                                                    .stream()
                                                                                    .toList();
                                                                        }
                                                                        employeeLeaveData.setShiftIds(shiftIdsToAssign);

                                                                        return Mono.just(employeeLeaveData); // Trả về dữ liệu nghỉ phép
                                                                    })
                                                                    .collectList()
                                                                    .flatMapMany(employeeLeaveDataList -> {
                                                                        // Get list of employee IDs
                                                                        Set<UUID> leaveEmployeeIds = employeeLeaveDataList.stream()
                                                                                .map(EmployeeHaveLeaveRequestToDate::getEmployeeId)
                                                                                .collect(Collectors.toSet());

                                                                        // Filter shifts that are not in the list of employees in the leave request
                                                                        return Flux.fromIterable(shifts)
                                                                                .flatMap(shift -> employeeShiftRepository.findAllByShiftId(shift.getId())
                                                                                        .filter(employeeShift ->
                                                                                                employeeLeaveDataList.stream()
                                                                                                        .filter(leaveData -> leaveData.getEmployeeId().equals(employeeShift.getEmployeeId()))
                                                                                                        .noneMatch(leaveData -> leaveData.getShiftIds().contains(shift.getId()))
                                                                                        )
                                                                                        .flatMap(employeeShift -> {
                                                                                            log.info("Generating shift detail for employeeId: {}, shiftId: {}", employeeShift.getEmployeeId(), shift.getId());

                                                                                            EmployeeShiftDetail shiftDetail = new EmployeeShiftDetail();
                                                                                            shiftDetail.setId(UUID.randomUUID());
                                                                                            shiftDetail.setShiftId(shift.getId());
                                                                                            shiftDetail.setDate(date);
                                                                                            shiftDetail.setEmployeeId(employeeShift.getEmployeeId());

                                                                                            return employeeShiftDetailRepository.save(shiftDetail)
                                                                                                    .map(EmployeeShiftDetail::getId);
                                                                                        })
                                                                                );
                                                                    })
                                                                    .collectList()
                                                                    .map(savedIds -> {
                                                                        Map<String, Object> responseMap = new HashMap<>();
                                                                        responseMap.put("savedIds", savedIds);
                                                                        return responseMap;
                                                                    });
                                                        }));
                            });
                }).then();
    }

}
