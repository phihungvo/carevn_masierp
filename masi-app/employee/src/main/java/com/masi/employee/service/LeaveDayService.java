package com.masi.employee.service;

import com.masi.employee.domain.LeaveDay;
import com.masi.employee.domain.LeaveRequest;
import com.masi.employee.domain.TimeKeeping;
import com.masi.employee.domain.enumeration.LeaveType;
import com.masi.employee.domain.enumeration.TimeKeepingType;
import com.masi.employee.repository.LeaveDayRepository;
import com.masi.employee.repository.TimeKeepingRepository;
import com.masi.employee.repository.TimeKeepingViolationRepository;
import com.masi.employee.service.dto.LeaveDayDTO;
import com.masi.employee.service.mapper.LeaveDayMapper;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.masi.employee.domain.LeaveDay}.
 */
@Service
@Transactional
public class LeaveDayService {

    private final Logger log = LoggerFactory.getLogger(LeaveDayService.class);

    private final LeaveDayRepository leaveDayRepository;

    private final LeaveDayMapper leaveDayMapper;
    private final TimeKeepingRepository timeKeepingRepository;
    private final TimeKeepingViolationRepository violationRepository;

    public LeaveDayService(LeaveDayRepository leaveDayRepository, LeaveDayMapper leaveDayMapper,
                           TimeKeepingRepository timeKeepingRepository,
                           TimeKeepingViolationRepository timeKeepingViolationRepository) {
        this.leaveDayRepository = leaveDayRepository;
        this.leaveDayMapper = leaveDayMapper;
        this.timeKeepingRepository = timeKeepingRepository;
        this.violationRepository = timeKeepingViolationRepository;
    }

    /**
     * Save a leaveDay.
     *
     * @param leaveDayDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<LeaveDayDTO> save(LeaveDayDTO leaveDayDTO) {
        log.debug("Request to save LeaveDay : {}", leaveDayDTO);
        return leaveDayRepository.save(leaveDayMapper.toEntity(leaveDayDTO)).map(leaveDayMapper::toDto);
    }

    /**
     * Update a leaveDay.
     *
     * @param leaveDayDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<LeaveDayDTO> update(LeaveDayDTO leaveDayDTO) {
        log.debug("Request to update LeaveDay : {}", leaveDayDTO);
        return leaveDayRepository.save(leaveDayMapper.toEntity(leaveDayDTO).setIsPersisted()).map(leaveDayMapper::toDto);
    }

    /**
     * Partially update a leaveDay.
     *
     * @param leaveDayDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<LeaveDayDTO> partialUpdate(LeaveDayDTO leaveDayDTO) {
        log.debug("Request to partially update LeaveDay : {}", leaveDayDTO);

        return leaveDayRepository
            .findById(leaveDayDTO.getId())
            .map(existingLeaveDay -> {
                leaveDayMapper.partialUpdate(existingLeaveDay, leaveDayDTO);

                return existingLeaveDay;
            })
            .flatMap(leaveDayRepository::save)
            .map(leaveDayMapper::toDto);
    }

    /**
     * Get all the leaveDays.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<LeaveDayDTO> findAll() {
        log.debug("Request to get all LeaveDays");
        return leaveDayRepository.findAll().map(leaveDayMapper::toDto);
    }

    /**
     * Returns the number of leaveDays available.
     *
     * @return the number of entities in the database.
     */
    public Mono<Long> countAll() {
        return leaveDayRepository.count();
    }

    /**
     * Get one leaveDay by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<LeaveDayDTO> findOne(UUID id) {
        log.debug("Request to get LeaveDay : {}", id);
        return leaveDayRepository.findById(id).map(leaveDayMapper::toDto);
    }

    /**
     * Delete the leaveDay by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(UUID id) {
        log.debug("Request to delete LeaveDay : {}", id);
        return leaveDayRepository.deleteById(id);
    }

    public Mono<LeaveRequest> createLeaveDayForRequest(LeaveRequest leaveRequest) {
        LocalDate start = leaveRequest.getFromDate();
        LocalDate end = leaveRequest.getToDate();
        LeaveType type = leaveRequest.getLeaveRequestType();

        return Flux.fromStream(Stream.iterate(start, date -> !date.isAfter(end), date -> date.plusDays(1)))
            .map(date -> {
                // Create a new LeaveDay for each date
                LeaveDay leaveDay = new LeaveDay();
                leaveDay.setId(UUID.randomUUID());
                leaveDay.setLeaveRequestId(leaveRequest.getId());
                leaveDay.setEmployeeId(leaveRequest.getEmployeeId());
                leaveDay.setDate(date);
                leaveDay.setLeaveType(type);
                leaveDay.setLeaveRequestDayType(leaveRequest.getLeaveRequestDayType());
                leaveDay.setIsLocked(false);
                // Return the LeaveDay
                return leaveDay;
            })
            //create time keeping for each day
            .flatMap(leaveDay -> {
                return timeKeepingRepository.findByEmployeeIdAndDateAndTimeKeepingType(leaveDay.getEmployeeId(), leaveDay.getDate(), TimeKeepingType.HOUR)
                    .hasElement()
                    .flatMap(hasElement -> {
                        if (Boolean.TRUE.equals(hasElement)) {
                            return timeKeepingRepository.findByEmployeeIdAndDateAndTimeKeepingType(leaveDay.getEmployeeId(), leaveDay.getDate(), TimeKeepingType.HOUR)
                                .flatMap(timeKeeping -> {
                                    timeKeeping.setIsOverride(true);
                                    timeKeeping.setIsAbnormal(false);
                                    timeKeeping.setHoursWorked(0f);
                                    return Mono.just(timeKeeping);
                                });
                        } else {
                            TimeKeeping timeKeeping = new TimeKeeping();
                            timeKeeping.setId(UUID.randomUUID());
                            timeKeeping.setEmployeeId(leaveDay.getEmployeeId());
                            timeKeeping.setDate(leaveDay.getDate());
                            timeKeeping.setLocked(false);
                            timeKeeping.setHoursWorked(0.0f);
                            timeKeeping.setCreatedAt(ZonedDateTime.now());
                            timeKeeping.setLastUpdatedAt(ZonedDateTime.now());
                            timeKeeping.setIsOverride(false);
                            timeKeeping.setIsAbnormal(false);
                            return Mono.just(timeKeeping);
                        }
                    })
                    .flatMap(timeKeepingRepository::save)
                    //remove time keeping violation if exists
                    .flatMap(timeKeeping -> violationRepository.findByTimeKeepingId(timeKeeping.getId())
                        .flatMap(violationRepository::delete)
                        .then(Mono.just(timeKeeping)))
                    .then(Mono.just(leaveDay));
            })
            .collectList()
            .flatMapIterable(Function.identity())
            .flatMap(leaveDayRepository::save)
            .then(Mono.just(leaveRequest));
    }

    public Flux<LeaveDay> findAllByEmployeeAndDateBetween(UUID employeeId, LocalDate startDate, LocalDate endDate) {
        return leaveDayRepository.findByEmployeeIdAndDateBetween(employeeId, startDate, endDate);
    }

    public Mono<LeaveDay> findByEmployeeIdAndDate(UUID employeeId, LocalDate date) {
        return leaveDayRepository.findByEmployeeIdAndDate(employeeId, date);
    }

    public Mono<Void> deleteByEmployeeAndDate(UUID employeeId, LocalDate date) {
        return leaveDayRepository.findByEmployeeIdAndDate(employeeId, date)
            .flatMap(leaveDayRepository::delete)
            .then();
    }
}
