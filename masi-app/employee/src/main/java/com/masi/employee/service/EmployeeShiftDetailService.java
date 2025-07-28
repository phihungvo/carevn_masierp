package com.masi.employee.service;

import com.masi.employee.domain.EmployeeShiftDetail;
import com.masi.employee.domain.Shift;
import com.masi.employee.domain.TimeKeeping;
import com.masi.employee.domain.criteria.EmployeeShiftDetailCriteria;
import com.masi.employee.domain.enumeration.LeaveType;
import com.masi.employee.domain.enumeration.TimeKeepingViolationType;
import com.masi.employee.domain.enumeration.WorkspaceType;
import com.masi.employee.repository.EmployeeShiftDetailRepository;
import com.masi.employee.repository.ShiftRepository;
import com.masi.employee.service.dto.EmployeeShiftDetailDTO;
import com.masi.employee.service.dto.PersonalMonthlyTimesheetDTO;
import com.masi.employee.service.dto.TimeKeepingDTO;
import com.masi.employee.service.mapper.EmployeeShiftDetailMapper;

import java.time.*;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.masi.employee.domain.EmployeeShiftDetail}.
 */
@Service
@Transactional
public class EmployeeShiftDetailService {

    private static final Logger log = LoggerFactory.getLogger(EmployeeShiftDetailService.class);

    private final EmployeeShiftDetailRepository employeeShiftDetailRepository;

    private final EmployeeShiftDetailMapper employeeShiftDetailMapper;
    private final ShiftRepository shiftRepository;
    private final int UTC_OFFSET = -7;

    public EmployeeShiftDetailService(
        EmployeeShiftDetailRepository employeeShiftDetailRepository,
        EmployeeShiftDetailMapper employeeShiftDetailMapper,
        ShiftRepository shiftRepository) {
        this.employeeShiftDetailRepository = employeeShiftDetailRepository;
        this.employeeShiftDetailMapper = employeeShiftDetailMapper;
        this.shiftRepository = shiftRepository;
    }

    /**
     * Save a employeeShiftDetail.
     *
     * @param employeeShiftDetailDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<EmployeeShiftDetailDTO> save(EmployeeShiftDetailDTO employeeShiftDetailDTO) {
        log.debug("Request to save EmployeeShiftDetail : {}", employeeShiftDetailDTO);
        return employeeShiftDetailRepository
            .save(employeeShiftDetailMapper.toEntity(employeeShiftDetailDTO))
            .map(employeeShiftDetailMapper::toDto);
    }

    /**
     * Update a employeeShiftDetail.
     *
     * @param employeeShiftDetailDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<EmployeeShiftDetailDTO> update(EmployeeShiftDetailDTO employeeShiftDetailDTO) {
        log.debug("Request to update EmployeeShiftDetail : {}", employeeShiftDetailDTO);
        return employeeShiftDetailRepository
            .save(employeeShiftDetailMapper.toEntity(employeeShiftDetailDTO).setIsPersisted())
            .map(employeeShiftDetailMapper::toDto);
    }

    /**
     * Partially update a employeeShiftDetail.
     *
     * @param employeeShiftDetailDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<EmployeeShiftDetailDTO> partialUpdate(EmployeeShiftDetailDTO employeeShiftDetailDTO) {
        log.debug("Request to partially update EmployeeShiftDetail : {}", employeeShiftDetailDTO);
        return employeeShiftDetailRepository
            .findById(employeeShiftDetailDTO.getId())
            .map(existingEmployeeShiftDetail -> {
                employeeShiftDetailMapper.partialUpdate(existingEmployeeShiftDetail, employeeShiftDetailDTO);

                return existingEmployeeShiftDetail;
            })
            .flatMap(employeeShiftDetailRepository::save)
            .map(employeeShiftDetailMapper::toDto);
    }


    public Mono<Void> partialUpdateAbbreviated(TimeKeeping timeKeeping) {
        UUID timeKeepingId = timeKeeping.getId();
        Float totalCompletionPercent = timeKeeping.getHoursWorked();
        EmployeeShiftDetail employeeShiftDetail = timeKeeping.getEmployeeShiftDetail();
        log.info("Request to partially update EmployeeShiftDetail : {}", timeKeepingId);
        final Float[] completionPercent = {totalCompletionPercent};
        return employeeShiftDetailRepository
            .findByTimeKeepingId(timeKeeping.getDate())
            .collectList()
            .flatMap(exists -> {
                log.info("EmployeeShiftDetail exist: {}", exists);
                if (exists.size() < 2) {
                    log.warn("Không đủ EmployeeShiftDetail để xử lý.");
                    return Mono.empty();
                }
                return Flux.fromIterable(exists)
                    .flatMap(existingDetail ->
                        shiftRepository.findById(existingDetail.getShiftId())
                            .flatMap(shift -> {
                                if (shift.getDurationHours() < completionPercent[0]) {
                                    existingDetail.setCompletionPercent(shift.getDurationHours());
                                    completionPercent[0] -= shift.getDurationHours();
                                } else if (completionPercent[0] < 0) {
                                    existingDetail.setCompletionPercent(0f);
                                } else {
                                    existingDetail.setCompletionPercent(completionPercent[0]);
                                    completionPercent[0] = 0f;
                                }
                                existingDetail.setIsShiftOff(true);
                                existingDetail.setLeaveDayType(null);
                                existingDetail.setIsWfh(false);
                                existingDetail.setViolationType(null);
                                existingDetail.setNote(null);
                                existingDetail.setIsPaidShift(true);
                                existingDetail.setTimeKeepingId(timeKeepingId);
                                if (employeeShiftDetail.getIsShiftOff() != null) {
                                    existingDetail.setIsShiftOff(employeeShiftDetail.getIsShiftOff());
                                }
                                if (employeeShiftDetail.getLeaveDayType() != null) {
                                    existingDetail.setLeaveDayType(employeeShiftDetail.getLeaveDayType());
                                }
                                if (employeeShiftDetail.getIsWfh() != null) {
                                    existingDetail.setIsWfh(employeeShiftDetail.getIsWfh());
                                }
                                if (employeeShiftDetail.getViolationType() != null) {
                                    existingDetail.setViolationType(employeeShiftDetail.getViolationType());
                                }
                                if (employeeShiftDetail.getNote() != null) {
                                    existingDetail.setNote(employeeShiftDetail.getNote());
                                }
                                if (employeeShiftDetail.getIsPaidShift() != null) {
                                    existingDetail.setIsPaidShift(employeeShiftDetail.getIsPaidShift());
                                }
                                return employeeShiftDetailRepository.save(existingDetail);
                            })
                    )
                    .then();
            });
    }


    /**
     * Find employeeShiftDetails by Criteria.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<EmployeeShiftDetailDTO> findByCriteria(EmployeeShiftDetailCriteria criteria, Pageable pageable) {
        log.debug("Request to get all EmployeeShiftDetails by Criteria");
        return employeeShiftDetailRepository.findByCriteria(criteria, pageable).map(employeeShiftDetailMapper::toDto);
    }

    /**
     * Find the count of employeeShiftDetails by criteria.
     *
     * @param criteria filtering criteria
     * @return the count of employeeShiftDetails
     */
    public Mono<Long> countByCriteria(EmployeeShiftDetailCriteria criteria) {
        log.debug("Request to get the count of all EmployeeShiftDetails by Criteria");
        return employeeShiftDetailRepository.countByCriteria(criteria);
    }

    /**
     * Returns the number of employeeShiftDetails available.
     *
     * @return the number of entities in the database.
     */
    public Mono<Long> countAll() {
        return employeeShiftDetailRepository.count();
    }

    /**
     * Get one employeeShiftDetail by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<EmployeeShiftDetailDTO> findOne(UUID id) {
        log.debug("Request to get EmployeeShiftDetail : {}", id);
        return employeeShiftDetailRepository.findById(id).map(employeeShiftDetailMapper::toDto);
    }

    /**
     * Delete the employeeShiftDetail by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(UUID id) {
        log.debug("Request to delete EmployeeShiftDetail : {}", id);
        return employeeShiftDetailRepository.deleteById(id);
    }

    public Mono<Void> createOrUpdateEmployeeShiftDetail(TimeKeepingDTO timeKeepingDTO) {
        log.info("Request to create or update EmployeeShiftDetail : {}", timeKeepingDTO);
        LocalDate date = timeKeepingDTO.getDate();

        return employeeShiftDetailRepository
            .findAllByEmployeeIdAndDateAndIsDeleted(timeKeepingDTO.getEmployeeId(), date, false)
            .flatMap(employeeShiftDetail ->
                    shiftRepository.findById(employeeShiftDetail.getShiftId())
                        .flatMap(shift -> {
                            // update completion percent
                            employeeShiftDetail.setTimeKeepingId(timeKeepingDTO.getId());
                            employeeShiftDetail.setIsShiftOff(false);
                            employeeShiftDetail.setLeaveDayType(null);
                            employeeShiftDetail.setIsWfh(false);
                            employeeShiftDetail.setViolationType(null);
                            employeeShiftDetail.setNote(null);
                            employeeShiftDetail.setIsPaidShift(false);
                            // update check-in and check-out times
                            updateCheckInOutTimes(employeeShiftDetail, timeKeepingDTO);

                            // update completion percent
                            Float completionPercent = calculateDurationHours(timeKeepingDTO, shift);
//                                    if (employeeShiftDetail.getCompletionPercent() < completionPercent)
//                                        employeeShiftDetail.setCompletionPercent(completionPercent);

                            if (completionPercent > 0)
                                employeeShiftDetail.setCompletionPercent(completionPercent);
                            else
                                employeeShiftDetail.setCompletionPercent(0f);

                            return employeeShiftDetailRepository.save(employeeShiftDetail);
                        })
            )
            .then();
    }

    private void updateCheckInOutTimes(EmployeeShiftDetail employeeShiftDetail, TimeKeepingDTO timeKeepingDTO) {
        // update check-in time
        ZonedDateTime firstCheckIn = timeKeepingDTO.getFirstCheckIn();
        if (firstCheckIn != null) {
            employeeShiftDetail.setCheckInTime(firstCheckIn);
        } else {
            log.info("Check-in time is null for employeeShiftDetail: {}", employeeShiftDetail);
        }

        // update check-out time
        ZonedDateTime lastCheckOut = timeKeepingDTO.getLastCheckIn();
        if (lastCheckOut != null) {
            employeeShiftDetail.setCheckOutTime(lastCheckOut);
        } else {
            log.info("Check-out time is null for employeeShiftDetail: {}", employeeShiftDetail);
        }

//        // update completion percent
//        if (timeKeepingDTO.getHoursWorked() != null) {
//            employeeShiftDetail.setCompletionPercent(timeKeepingDTO.getHoursWorked());
//        }
    }

    public Mono<Float> calculateDurationHours(ZonedDateTime firstCheckIn, ZonedDateTime lastCheckOut, Shift shift) {
        TimeKeepingDTO timeKeepingDTO = new TimeKeepingDTO();
        timeKeepingDTO.setFirstCheckIn(firstCheckIn);
        timeKeepingDTO.setLastCheckIn(lastCheckOut);
        return Mono.just(calculateDurationHours(timeKeepingDTO, shift));
    }

    private Float calculateDurationHours(TimeKeepingDTO timeKeepingDTO, Shift shift) {
        // calculate required hours

        if (timeKeepingDTO.getLastCheckIn() == null)
            return 0f;

        LocalTime hourStart = LocalTime.of(shift.getHourStartTime(), shift.getMinuteStartTime(), shift.getSecondStartTime());
        LocalTime hourEnd = LocalTime.of(shift.getHourEndTime(), shift.getMinuteEndTime(), shift.getSecondEndTime());
        float requiredHours = (float) Duration.between(hourStart, hourEnd).toMinutes() / 60;

        // get check-in and check-out times
        ZonedDateTime firstCheckIn = timeKeepingDTO.getFirstCheckIn();
        ZonedDateTime lastCheckOut;
        try {
            lastCheckOut = timeKeepingDTO.getLastCheckIn();
        } catch (Exception e) {
            log.info("Error while setting lastcheckout: {}", e.getMessage());
            lastCheckOut = timeKeepingDTO.getLastCheckIn();

        }

        // get actual check-in and check-out times
        LocalTime actualCheckIn = (firstCheckIn != null && firstCheckIn.toLocalTime().isBefore(hourStart)) ? hourStart : firstCheckIn.toLocalTime();
        LocalTime actualCheckOut = (lastCheckOut != null && lastCheckOut.toLocalTime().isAfter(hourEnd)) ? hourEnd : lastCheckOut.toLocalTime();

        // calculate actual hours
        float actualHours = (float) Duration.between(
            LocalDateTime.of(firstCheckIn.toLocalDate(), actualCheckIn),
            LocalDateTime.of(lastCheckOut.toLocalDate(), actualCheckOut)
        ).toMinutes() / 60;

        // calculate percentage
        return Math.round(Math.min((actualHours / requiredHours), 1) * shift.getDurationHours() * 10) / 10.0f;
    }


    public Mono<List<PersonalMonthlyTimesheetDTO>> updatePersonalMonthlyTimesheetEmployeeOffice(List<PersonalMonthlyTimesheetDTO> list, Collection<UUID> listEmployeeId, LocalDate month) {
        log.info("Request to update PersonalMonthlyTimesheet Employee Office : {}", list);
        LocalDate dateFrom = month.withDayOfMonth(1);
        LocalDate dateTo = month.withDayOfMonth(month.lengthOfMonth());

        return employeeShiftDetailRepository.findAllByEmployeeIdAndDateFromAndDateToAndIsDeleted(listEmployeeId, dateFrom, dateTo, false)
            .collectList()
            .flatMap(employeeShiftDetailDTOS -> {
                // Sum All PersonalMonthly
                float totalShiftHours = 0f;
                float totalHoliday300 = 0f;
                float totalOffDay = 0f;
                float totalAnnualLeave = 0f;
                float totalTotalHoursAtFactory = 0f;
                float totalTotalWorkAtFactory = 0f;
                float totalTotalWork = 0f;
                float totalOffDayInMonth = 0f;
                float totalTotalWorkFromHome = 0f;

                for (PersonalMonthlyTimesheetDTO personalDto : list) {
                    // UUID When "00000000-0000-0000-0000-000000000000" is passed, this total of PersonalMonthly.
                    if (personalDto.getId().equals("00000000-0000-0000-0000-000000000000")) {
                        personalDto.setShiftHours(totalShiftHours);
                        personalDto.setHoliday300(totalHoliday300);
                        personalDto.setOffDay(totalOffDay);
                        personalDto.setAnnualLeave(totalAnnualLeave);
                        personalDto.setTotalHoursAtFactory(totalTotalHoursAtFactory);
                        personalDto.setTotalWorkAtFactory(totalTotalWorkAtFactory);
                        personalDto.setTotalWork(totalTotalWork);
                        personalDto.setOffDayInMonth(totalOffDayInMonth);
                        personalDto.setTotalWorkFromHome(totalTotalWorkFromHome);
                        break;
                    }

                    // Sum One PersonalMonthly
                    Float totalShiftHoursOne = 0f;          // Giờ ca
                    Float totalHoliday300One = 0f;          // Lễ 300%
                    Float totalOffDayOne = 0f;              // Ngày off hưởng nguyên lương
                    Float totalAnnualLeaveOne = 0f;         // Phép năm
                    Float totalTotalWorkOne = 0f;           // Tổng công
                    Float totalOffDayInMonthOne = 0f;       // Ngày off trong tháng
                    Float totalTotalWorkFromHomeOne = 0f;   // số ngày wfh trong tháng
                    Float totalCompensatoryLeave = 0f;

                    for (TimeKeeping timeKeepingDto : personalDto.getTimeKeepings()) {
                        var employeeShiftDetailDTO = employeeShiftDetailDTOS.stream()
                            .filter(employeeShiftDetail -> employeeShiftDetail.getTimeKeepingId() != null
                                && employeeShiftDetail.getTimeKeepingId().equals(timeKeepingDto.getId()))
                            .findFirst()
                            .orElse(null);

                            employeeShiftDetailDTOS.removeIf(employeeShiftDetail ->
                                    employeeShiftDetail.getTimeKeepingId() != null
                                            && employeeShiftDetail.getTimeKeepingId().equals(timeKeepingDto.getId()));


                        if (employeeShiftDetailDTO == null) {
                            continue;
                        }

                            String note = "";
                            if (employeeShiftDetailDTO.getIsShiftOff()) {
                                note = "OFF";
                                totalOffDayInMonthOne += 1;
                                totalOffDayInMonth += 1;
                            }
                            else if(employeeShiftDetailDTO.getIsWfh()){
                                note = "WFH";
                                totalTotalWorkFromHomeOne += 1;
                                totalTotalWorkFromHome += 1;
                            }
                            else if (employeeShiftDetailDTO.getViolationType() != null
                                    && employeeShiftDetailDTO.getViolationType().equals(TimeKeepingViolationType.ABSENT_WITHOUT_REQUEST)) {
                                note = "KP";
                            }
                            else if (Objects.nonNull(employeeShiftDetailDTO.getLeaveDayType())) {
                                if (employeeShiftDetailDTO.getLeaveDayType().equals(LeaveType.ANNUAL_LEAVE_FULL_DAY) && !employeeShiftDetailDTO.getIsPaidShift()) {
                                    note = "P";
                                    totalAnnualLeaveOne += 1;
                                    totalAnnualLeave += 1;
                                }
                                else if(employeeShiftDetailDTO.getLeaveDayType().equals(LeaveType.ANNUAL_LEAVE_HALF_DAY) && !employeeShiftDetailDTO.getIsPaidShift()) {
                                    note = "PO";
                                }
                                else if(employeeShiftDetailDTO.getLeaveDayType().equals(LeaveType.UNPAID_LEAVE) && !employeeShiftDetailDTO.getIsPaidShift()){
                                    note = "V";
                                }
                                else if(employeeShiftDetailDTO.getLeaveDayType().equals(LeaveType.COMPENSATION_LEAVE) && !employeeShiftDetailDTO.getIsPaidShift()) {
                                    note = "NB";
                                    totalCompensatoryLeave += 1;
                                }
                            }
                            else {
                                // if day holiday 300%
//                                if(){
//                                    totalShiftHoursOne += employeeShiftDetailDTO.getCompletionPercent() * 3;
//                                }
                                note = employeeShiftDetailDTO.getCompletionPercent().toString();
                                totalShiftHoursOne += employeeShiftDetailDTO.getCompletionPercent();
                            }
                            timeKeepingDto.setNote(note);
                            timeKeepingDto.setHoursWorked(employeeShiftDetailDTO.getCompletionPercent());
                        }

                        personalDto.setShiftHours(totalShiftHoursOne);
                        personalDto.setTotalWorkFromHome(totalTotalWorkFromHomeOne);
                        personalDto.setHoliday300(totalHoliday300One);
                        personalDto.setAnnualLeave(totalAnnualLeaveOne);

                        personalDto.setTotalHoursAtFactory(totalShiftHoursOne);
                        personalDto.setTotalWorkAtFactory(totalShiftHoursOne/8);
                        personalDto.setTotalWork(totalShiftHoursOne/8 + totalCompensatoryLeave);


                    }
                    return Mono.just(list);
                });
    }
}
