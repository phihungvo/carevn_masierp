package com.masi.employee.service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.masi.employee.domain.LeaveDay;
import com.masi.employee.domain.PersonalMonthlyTimesheet;
import com.masi.employee.domain.TimeKeeping;
import com.masi.employee.domain.TimeKeepingViolation;
import com.masi.employee.domain.enumeration.TimeKeepingType;
import com.masi.employee.domain.enumeration.TimesheetReviewStatus;
import com.masi.employee.domain.enumeration.WorkspaceType;
import com.masi.employee.repository.PersonalMonthlyTimesheetRepository;
import com.masi.employee.repository.TimeKeepingRepository;
import com.masi.employee.repository.TimeKeepingViolationRepository;
import com.masi.employee.service.dto.EmployeeDTO;
import com.masi.employee.service.dto.PersonalMonthlyTimesheetDTO;
import com.masi.employee.service.dto.PersonalMonthlyTimesheetQuery;
import com.masi.employee.service.dto.ReviewMultipleTimeSheetDTO;
import com.masi.employee.service.dto.ReviewTimeSheetDTO;
import com.masi.employee.service.mapper.PersonalMonthlyTimesheetMapper;
import com.masi.employee.web.rest.errors.BadRequestAlertException;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing
 * {@link PersonalMonthlyTimesheet}.
 */
@Service
@Transactional
public class PersonalMonthlyTimesheetService {

    private final Logger log = LoggerFactory.getLogger(PersonalMonthlyTimesheetService.class);

    private final PersonalMonthlyTimesheetRepository personalMonthlyTimesheetRepository;

    private final PersonalMonthlyTimesheetMapper personalMonthlyTimesheetMapper;
    private final TimeKeepingRepository timeKeepingRepository;
    private final LeaveDayService leaveDayService;
    private final TimeKeepingViolationRepository violationRepository;
    private final AnnualLeaveService annualLeaveService;
    private TimeKeepingService timeKeepingService;
    private MonthlyTimeSheetReviewService monthlyTimeSheetReviewService;
    private EmployeeShiftDetailService employeeShiftDetailService;

    @Autowired
    public void setMonthlyTimeSheetReviewService(@Lazy MonthlyTimeSheetReviewService monthlyTimeSheetReviewService) {
        this.monthlyTimeSheetReviewService = monthlyTimeSheetReviewService;
    }

    @Autowired
    public void setTimeKeepingService(@Lazy TimeKeepingService timeKeepingService) {
        this.timeKeepingService = timeKeepingService;
    }

    public PersonalMonthlyTimesheetService(
        PersonalMonthlyTimesheetRepository personalMonthlyTimesheetRepository,
        TimeKeepingRepository timeKeepingRepository,
        PersonalMonthlyTimesheetMapper personalMonthlyTimesheetMapper,
        LeaveDayService leaveDayService,
        TimeKeepingViolationRepository violationRepository, AnnualLeaveService annualLeaveService) {

        this.personalMonthlyTimesheetRepository = personalMonthlyTimesheetRepository;
        this.personalMonthlyTimesheetMapper = personalMonthlyTimesheetMapper;
        this.timeKeepingRepository = timeKeepingRepository;
        this.leaveDayService = leaveDayService;
        this.violationRepository = violationRepository;
        this.annualLeaveService = annualLeaveService;
    }


    @Transactional(readOnly = true)
    public Mono<PersonalMonthlyTimesheetDTO> findByEmployeeIdAndMonthAndType(UUID employeeId, LocalDate month, TimeKeepingType type) {
        return personalMonthlyTimesheetRepository.findByEmployeeIdAndMonthAndType(employeeId, month.withDayOfMonth(1), type)
            .map(personalMonthlyTimesheetMapper::toDto);
    }

    public Mono<Boolean> isEmployeeMonthLocked(UUID employeeId, LocalDate month, TimeKeepingType type) {
        return this.findByEmployeeIdAndMonthAndType(employeeId, month.withDayOfMonth(1), type)
            .map(entity -> TimesheetReviewStatus.APPROVED.equals(entity.getStatus()))
            .defaultIfEmpty(false);
    }

    //    @Scheduled(fixedRate = 1000 * 60 * 60 * 24)
    public Mono<Void> savePersonalMonthlyTimesheetToFile() {
        PersonalMonthlyTimesheetQuery query = new PersonalMonthlyTimesheetQuery();
        query.setMonth(LocalDate.of(2024, 8, 1));
        query.setType(TimeKeepingType.HOUR);
        query.setWorkspaceType(WorkspaceType.OFFICE);
        return this.findAllByQuery(query, null).collectList()
            .handle((entity, sink) -> {
                try {
                    Path temp = Files.createTempFile("timesheet", ".bin");
                    System.out.println(temp.toFile().getAbsolutePath());
                    FileOutputStream fileOutputStream = new FileOutputStream(temp.toFile());
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
                    objectOutputStream.writeObject(entity);
                    objectOutputStream.close();
                    fileOutputStream.close();
                    sink.complete();
                } catch (IOException e) {
                    sink.error(new RuntimeException(e));
                }

            })
            .then();
    }

//    public Flux<PersonalMonthlyTimesheetDTO> findAllByQuery(PersonalMonthlyTimesheetQuery query, Pageable pageable) {
//        return personalMonthlyTimesheetRepository.findAllByQuery(pageable, query)
//            .map(e -> {
//                var dto = e.toDto();
//                dto.setWorkspaceType(query.getWorkspaceType());
//                return dto;
//            })
//                // If WorkSpace Office then return
//            .flatMap(this::joinWithTimeKeepings)
//            .flatMap(this::calculateField)
//            .collectList()
//            .flatMapMany(dtos -> {
//                sumColumn(query, dtos);
//                return Flux.fromIterable(dtos);
//            });
//    }

    public Flux<PersonalMonthlyTimesheetDTO> findAllByQuery(PersonalMonthlyTimesheetQuery query, Pageable pageable) {
        return personalMonthlyTimesheetRepository.findAllByQuery(pageable, query)
                .map(e -> {
                    var dto = e.toDto();
                    dto.setWorkspaceType(query.getWorkspaceType());
                    return dto;
                })
                .flatMap(this::joinWithTimeKeepings)
                .flatMap(this::calculateField)
                .collectList()
                .flatMapMany(dtos -> {
                    Mono<List<PersonalMonthlyTimesheetDTO>> updatedListMono;
                    if (query.getWorkspaceType().equals(WorkspaceType.OFFICE)) {
                        Collection<UUID> listEmployeeId = dtos.stream()
                                .filter(dto -> dto.getWorkspaceType() != null && dto.getWorkspaceType().equals(WorkspaceType.OFFICE))
                                .map(dto -> dto.getEmployee().getId())
                                .toList();
                        updatedListMono = employeeShiftDetailService.updatePersonalMonthlyTimesheetEmployeeOffice(dtos, listEmployeeId, query.getMonth());
                    } else {
                        updatedListMono = Mono.just(dtos);
                    }
                    return updatedListMono.flatMapMany(updatedDtos -> {
                        sumColumn(query, updatedDtos);
                        return Flux.fromIterable(updatedDtos);
                    });
                });
    }



    public void sumColumn(PersonalMonthlyTimesheetQuery query, Collection<PersonalMonthlyTimesheetDTO> dtos) {
        PersonalMonthlyTimesheetDTO sum = new PersonalMonthlyTimesheetDTO();
        PersonalMonthlyTimesheetDTO avg = new PersonalMonthlyTimesheetDTO();

        sum.setId(UUID.fromString("00000000-0000-0000-0000-000000000000"));
        avg.setId(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        EmployeeDTO employee = new EmployeeDTO();
        employee.setFirstName("Tổng giờ");
        employee.setLastName("");
        sum.setEmployee(employee);
        employee = new EmployeeDTO();
        employee.setFirstName("Tổng công");
        employee.setLastName("");
        avg.setEmployee(employee);

        sum.setTimeKeepings(new ArrayList<>());
        avg.setTimeKeepings(new ArrayList<>());
        LocalDate startOfMonth = query.getMonth().withDayOfMonth(1);
        LocalDate endOfMonth = query.getMonth().withDayOfMonth(query.getMonth().lengthOfMonth());
        // này là tổng cột mở rộng
        for (PersonalMonthlyTimesheetDTO dto : dtos) {
            sum.setShiftHours(sum.getShiftHours() + dto.getShiftHours());
            sum.setHoliday300(sum.getHoliday300() + dto.getHoliday300());
            sum.setOffDay(sum.getOffDay() + dto.getOffDay());
            sum.setAnnualLeave(sum.getAnnualLeave() + dto.getAnnualLeave());
            sum.setTotalHoursAtFactory(sum.getTotalHoursAtFactory() + dto.getTotalHoursAtFactory());
            sum.setTotalWorkAtFactory(sum.getTotalWorkAtFactory() + dto.getTotalWorkAtFactory());
            sum.setTotalWork(sum.getTotalWork() + dto.getTotalWork());
            sum.setOffDayInMonth(sum.getOffDayInMonth() + dto.getOffDayInMonth());
            sum.setTotalWorkFromHome(sum.getTotalWorkFromHome() + dto.getTotalWorkFromHome());
        }
        Integer HOURS_IN_DAY = TimeKeepingType.DRIVER.equals(query.getType()) ? 12 : 8;

        // giờ tính cột tháng
        int totalEmployee = dtos.size();
        for (PersonalMonthlyTimesheetDTO dto : dtos) {

            for (TimeKeeping timeKeepingDTO : dto.getTimeKeepings()) {
                LocalDate date = timeKeepingDTO.getDate();
                TimeKeeping existTimeKeeping = sum.getTimeKeepings().stream()
                    .filter(t -> t.getDate().equals(date))
                    .findFirst()
                    .orElse(null);

                if (existTimeKeeping == null) {
                    existTimeKeeping = new TimeKeeping();
                    existTimeKeeping.setDate(date);
                    existTimeKeeping.setHoursWorked(0f);
                    sum.getTimeKeepings().add(existTimeKeeping);
                }
                if (existTimeKeeping.getHoursWorked() == null) {
                    existTimeKeeping.setHoursWorked(0f);
                }
                existTimeKeeping.setHoursWorked(existTimeKeeping.getHoursWorked()
                    + (timeKeepingDTO.getHoursWorked() == null ? 0 : timeKeepingDTO.getHoursWorked()));
                existTimeKeeping.setNote(String.format("%.1f", existTimeKeeping.getHoursWorked()));
            }
        }
        for (TimeKeeping timeKeeping : sum.getTimeKeepings()) {
            TimeKeeping avgTimeKeeping = new TimeKeeping();
            avgTimeKeeping.setDate(timeKeeping.getDate());
            avgTimeKeeping.setHoursWorked(timeKeeping.getHoursWorked() /HOURS_IN_DAY);
            avgTimeKeeping.setNote(String.format("%.1f", avgTimeKeeping.getHoursWorked()));
            avg.getTimeKeepings().add(avgTimeKeeping);
        }

        avg.setShiftHours(sum.getShiftHours() / HOURS_IN_DAY);
        dtos.add(sum);
        dtos.add(avg);

    }

    private Mono<PersonalMonthlyTimesheetDTO> calculateField(PersonalMonthlyTimesheetDTO dto) {

        Integer HOURS_IN_DAY = TimeKeepingType.DRIVER.equals(dto.getTimeKeepingType()) ? 12 : 8;
        var timeKeepings = dto.getTimeKeepings();
        // // Giờ ca Lễ 300% Ngày off hưởng nguyên lương Phép năm Tổng giờ tại NM Tổng
        // công tại NM Tổng công Ngày off trong tháng

        // Giờ ca
        float shiftHours = timeKeepingService.countTotalWorkedHours(timeKeepings);
        // Lễ 300%
        float holiday300 = 0;
        // Ngày off hưởng nguyên lương
        float offDay = timeKeepingService.countDayOff(timeKeepings);
        //
        // Phép năm
        float annualLeave = timeKeepingService.countPaidLeave(timeKeepings);
        // Tổng giờ tại Nhà máy
        float totalHoursAtFactory = timeKeepingService.countTotalWorkedHours(timeKeepings);
        // Tổng công tại Nhà máy
        float totalWorkAtFactory = totalHoursAtFactory / HOURS_IN_DAY;
        // số ngày wfh trong tháng
        float totalWorkFromHome = timeKeepingService.countTotalWorkFromHomeDays(timeKeepings);
        // Tổng công
        // dayOff + paidLeave + totalWorkDays + compensationLeave;
        // compensationLeave là nghỉ bù;
        float totalWork = offDay + dto.getAnnualLeave() + totalWorkAtFactory
            + timeKeepingService.countCompensationLeave(timeKeepings) + totalWorkFromHome;
        // Ngày off trong tháng
        float offDayInMonth = timeKeepingService.countUnpaidLeave(timeKeepings);

        dto.setShiftHours(shiftHours);
        dto.setHoliday300(holiday300);
        dto.setOffDay(offDay);
        dto.setAnnualLeave(annualLeave);
        dto.setTotalHoursAtFactory(totalHoursAtFactory);
        dto.setTotalWorkAtFactory(totalWorkAtFactory);
        dto.setTotalWork(totalWork);
        dto.setOffDayInMonth(offDayInMonth);
        dto.setTotalWorkFromHome(totalWorkFromHome);

        return Mono.just(dto);

    }

    public Mono<Long> countByQuery(PersonalMonthlyTimesheetQuery query) {
        return personalMonthlyTimesheetRepository.countAllByQuery(query);
    }

    public Mono<PersonalMonthlyTimesheetDTO> getByEmployeeIdAndMonthOrCreate(UUID employeeId, LocalDate month, TimeKeepingType type) {
        return this.findByEmployeeIdAndMonthAndType(employeeId, month.withDayOfMonth(1), type)
            .switchIfEmpty(Mono.defer(() -> {
                PersonalMonthlyTimesheet entity = new PersonalMonthlyTimesheet()
                    .id(UUID.randomUUID())
                    .employeeId(employeeId)
                    .createdDate(ZonedDateTime.now())
                    .month(month.withDayOfMonth(1))
                    .lastUpdated(ZonedDateTime.now())
                    .timeKeepingType(type)
                    .status(TimesheetReviewStatus.PENDING);

                return personalMonthlyTimesheetRepository.save(entity)
                    .map(personalMonthlyTimesheetMapper::toDto);
            }));
    }

    public Mono<Void> fillTimeKeepingForPersonalMonthlyTimesheet(PersonalMonthlyTimesheet dto) {
        LocalDate startOfMonth = dto.getMonth().withDayOfMonth(1);
        LocalDate endOfMonth = dto.getMonth().withDayOfMonth(dto.getMonth().lengthOfMonth());
        List<LocalDate> dates = new ArrayList<>();
        for (LocalDate date = startOfMonth; date.isBefore(endOfMonth) || date.isEqual(endOfMonth); date = date.plusDays(1)) {
            dates.add(date);
        }
        return Flux.fromIterable(dates)
            .flatMap(date -> {
                return timeKeepingRepository.findByEmployeeIdAndDateAndTimeKeepingType(dto.getEmployeeId(), date, dto.getTimeKeepingType())
                    .map(timeKeeping -> {
                        timeKeeping.setPersonalMonthlyTimesheetId(dto.getId());
                        timeKeeping.setLastUpdatedAt(ZonedDateTime.now());
                        timeKeeping.setLocked(TimesheetReviewStatus.APPROVED.equals(dto.getStatus()));
                        return timeKeeping.setIsPersisted();
                    })
                    .switchIfEmpty(Mono.defer(() -> {
                        TimeKeeping timeKeeping = TimeKeeping.builder()
                            .id(UUID.randomUUID())
                            .employeeId(dto.getEmployeeId())
                            .date(date)
                            .timeKeepingType(dto.getTimeKeepingType())
                            .personalMonthlyTimesheetId(dto.getId())
                            .hoursWorked(0f)
                            .isAbnormal(false)
                            .isDayOff(false)
                            .isOverride(false)
                            .locked(TimesheetReviewStatus.APPROVED.equals(dto.getStatus()))
                            .createdAt(ZonedDateTime.now())
                            .lastUpdatedAt(ZonedDateTime.now())
                            .personalMonthlyTimesheetId(dto.getId()).build();
                        return Mono.just(timeKeeping);
                    })).flatMap(timeKeepingRepository::save);
            }).then();

    }

    public Mono<Void> bulkReject(LocalDate month, UUID reviewId, TimeKeepingType type) {
        return personalMonthlyTimesheetRepository.findAllByMonthAndTimeKeepingType(month.withDayOfMonth(1), type)
            .flatMap(entity -> {
                if (!TimesheetReviewStatus.APPROVED.equals(entity.getStatus())) {
                    entity.setStatus(TimesheetReviewStatus.REJECTED);
                    entity.setLastUpdated(ZonedDateTime.now());
                    entity.setReviewId(reviewId);

                }
                return Mono.just(entity.setIsPersisted());
            })
            .flatMap(personalMonthlyTimesheetRepository::save)
            .flatMap(this::fillTimeKeepingForPersonalMonthlyTimesheet)
            .then();
    }

    public Mono<Void> bulkApprove(LocalDate month, UUID reviewId, TimeKeepingType type) {
        return personalMonthlyTimesheetRepository.findAllByMonthAndTimeKeepingType(month.withDayOfMonth(1), type)
            .flatMap(entity -> {
                if (!TimesheetReviewStatus.APPROVED.equals(entity.getStatus())) {
                    entity.setLastUpdated(ZonedDateTime.now());
                }
                entity.setStatus(TimesheetReviewStatus.APPROVED);
                entity.setReviewId(reviewId);
                return Mono.just(entity.setIsPersisted());
            })
            .flatMap(personalMonthlyTimesheetRepository::save)
            .flatMap(this::fillTimeKeepingForPersonalMonthlyTimesheet
            ).then();
    }

    public Mono<PersonalMonthlyTimesheetDTO> reject(UUID id) {
        return personalMonthlyTimesheetRepository.findById(id)
            .flatMap(entity -> {
                if (TimesheetReviewStatus.APPROVED.equals(entity.getStatus())) {
                    return Mono.error(new BadRequestAlertException("Cannot reject an approved timesheet",
                        "personalMonthlyTimesheet", "cannotrejectapproved"));
                }
                entity.setStatus(TimesheetReviewStatus.REJECTED);
                return Mono.just(entity.setIsPersisted());
            })
            .flatMap(personalMonthlyTimesheetRepository::save)
            .map(personalMonthlyTimesheetMapper::toDto);
    }

    public Mono<Boolean> isLocked(UUID id) {
        return personalMonthlyTimesheetRepository.findById(id)
            .map(entity -> TimesheetReviewStatus.APPROVED.equals(entity.getStatus()))
            .defaultIfEmpty(false);
    }

    public Mono<Boolean> isLocked(UUID employeeId, LocalDate month, TimeKeepingType type) {
        return personalMonthlyTimesheetRepository.findByEmployeeIdAndMonthAndType(employeeId, month.withDayOfMonth(1), type)
            .map(entity -> TimesheetReviewStatus.APPROVED.equals(entity.getStatus()))
            .defaultIfEmpty(false);
    }

    public Mono<PersonalMonthlyTimesheetDTO> approve(UUID id) {
        return personalMonthlyTimesheetRepository.findById(id)
            .map(entity -> {
                entity.setStatus(TimesheetReviewStatus.APPROVED);
                return entity.setIsPersisted();
            })
            .flatMap(personalMonthlyTimesheetRepository::save)
            .map(personalMonthlyTimesheetMapper::toDto)
            .flatMap(dto -> {
                return timeKeepingRepository.lockAllByMonthlyTimesheetId(id).then(Mono.just(dto));
            });
    }

    /**
     * Save a personalMonthlyTimesheet.
     *
     * @param personalMonthlyTimesheetDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<PersonalMonthlyTimesheetDTO> save(PersonalMonthlyTimesheetDTO personalMonthlyTimesheetDTO) {
        log.debug("Request to save PersonalMonthlyTimesheet : {}", personalMonthlyTimesheetDTO);
        return personalMonthlyTimesheetRepository
            .save(personalMonthlyTimesheetMapper.toEntity(personalMonthlyTimesheetDTO))
            .map(personalMonthlyTimesheetMapper::toDto);
    }

    /**
     * Update a personalMonthlyTimesheet.
     *
     * @param personalMonthlyTimesheetDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<PersonalMonthlyTimesheetDTO> update(PersonalMonthlyTimesheetDTO personalMonthlyTimesheetDTO) {
        log.debug("Request to update PersonalMonthlyTimesheet : {}", personalMonthlyTimesheetDTO);
        return personalMonthlyTimesheetRepository
            .save(personalMonthlyTimesheetMapper.toEntity(personalMonthlyTimesheetDTO).setIsPersisted())
            .map(personalMonthlyTimesheetMapper::toDto);
    }

    /**
     * Partially update a personalMonthlyTimesheet.
     *
     * @param personalMonthlyTimesheetDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<PersonalMonthlyTimesheetDTO> partialUpdate(PersonalMonthlyTimesheetDTO personalMonthlyTimesheetDTO) {
        log.debug("Request to partially update PersonalMonthlyTimesheet : {}", personalMonthlyTimesheetDTO);

        return personalMonthlyTimesheetRepository
            .findById(personalMonthlyTimesheetDTO.getId())
            .map(existingPersonalMonthlyTimesheet -> {
                personalMonthlyTimesheetMapper.partialUpdate(existingPersonalMonthlyTimesheet,
                    personalMonthlyTimesheetDTO);

                return existingPersonalMonthlyTimesheet;
            })
            .flatMap(personalMonthlyTimesheetRepository::save)
            .map(personalMonthlyTimesheetMapper::toDto);
    }

    /**
     * Get all the personalMonthlyTimesheets.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<PersonalMonthlyTimesheetDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PersonalMonthlyTimesheets");
        return personalMonthlyTimesheetRepository.findAllBy(pageable).map(personalMonthlyTimesheetMapper::toDto)
            .flatMap(this::joinWithTimeKeepings);
    }

    /**
     * Returns the number of personalMonthlyTimesheets available.
     *
     * @return the number of entities in the database.
     */
    public Mono<Long> countAll() {
        return personalMonthlyTimesheetRepository.count();
    }

    /**
     * Get one personalMonthlyTimesheet by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<PersonalMonthlyTimesheetDTO> findOne(UUID id) {
        log.debug("Request to get PersonalMonthlyTimesheet : {}", id);
        return personalMonthlyTimesheetRepository.findById(id).map(PersonalMonthlyTimesheet::toDto)
            .flatMap(this::joinWithTimeKeepings);
    }

    private Mono<PersonalMonthlyTimesheetDTO> joinWithTimeKeepings(PersonalMonthlyTimesheetDTO dto) {

        return timeKeepingRepository.findAllByPersonalMonthlyTimesheetId(dto.getId())
            .map(entity -> {
                entity.setEmployee(null);
                entity.setPersonalMonthlyTimesheet(null);
                entity.resolveHours(entity.getTimeKeepingType(), dto.getWorkspaceType());
                entity.setNote(entity.resolveTimesheetDayValue());
                return entity;
            })
            .collectList()
            .flatMap(timeKeeping -> {
                dto.setTimeKeepings(timeKeeping);
                Mono<List<LeaveDay>> leaveDayList = leaveDayService
                    .findAllByEmployeeAndDateBetween(dto.getEmployee().getId(),
                        dto.getMonth().withDayOfMonth(1),
                        dto.getMonth().withDayOfMonth(dto.getMonth().lengthOfMonth()))
                    .collectList();

                Mono<List<TimeKeepingViolation>> violationList = violationRepository
                    .findAllByEmployeeId(dto.getEmployee().getId())
                    .collectList();
                return Mono.zip(leaveDayList, violationList, Mono.just(dto))
                    .flatMap(tuple -> {
                        List<LeaveDay> leaveDays = tuple.getT1();
                        List<TimeKeepingViolation> violations = tuple.getT2();
                        PersonalMonthlyTimesheetDTO personalMonthlyTimesheetDTO = tuple.getT3();
                        for (TimeKeeping timeKeeping1 : personalMonthlyTimesheetDTO.getTimeKeepings()) {
                            timeKeeping1.setLeaveDay(leaveDays.stream()
                                .filter(leaveDay -> leaveDay.getDate().equals(timeKeeping1.getDate()))
                                .findFirst()
                                .orElse(null));
                            timeKeeping1.setNote(timeKeeping1.resolveTimesheetDayValue());
                            timeKeeping1.setViolation(violations.stream()
                                .filter(violation -> violation.getTimeKeepingId()
                                    .equals(timeKeeping1.getId()))
                                .findFirst()
                                .orElse(null));
                            if (timeKeeping1.getViolation() != null) {
                                timeKeeping1.setViolationType(timeKeeping1.getViolation().getType());
                            }
                        }
                        return Mono.just(personalMonthlyTimesheetDTO);
                    });
            });
    }

    /**
     * Delete the personalMonthlyTimesheet by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(UUID id) {
        log.debug("Request to delete PersonalMonthlyTimesheet : {}", id);
        return personalMonthlyTimesheetRepository.deleteById(id);
    }

    public Flux<PersonalMonthlyTimesheetDTO> approveMulti(ReviewMultipleTimeSheetDTO dto) {
        var monthlyReviewDTO = new ReviewTimeSheetDTO();
        monthlyReviewDTO.setStatus(TimesheetReviewStatus.APPROVED);
        monthlyReviewDTO.setNote(dto.getNote());
        monthlyReviewDTO.setSignatureFile(dto.getSignatureFile());
        return monthlyTimeSheetReviewService.save(monthlyReviewDTO).flatMap(monthly -> {
            return personalMonthlyTimesheetRepository.findAllByIdIn(List.of(dto.getIds()))
                .map(entity -> {
                    entity.setStatus(TimesheetReviewStatus.APPROVED);
                    entity.setReviewId(monthly.getId());
                    return entity.setIsPersisted();
                })
                .flatMap(personalMonthlyTimesheetRepository::save)

                .flatMap(item -> {
                    return this.fillTimeKeepingForPersonalMonthlyTimesheet(item).then(
                        timeKeepingRepository.lockAllByMonthlyTimesheetId(item.getId()).then(Mono.just(personalMonthlyTimesheetMapper.toDto(item))));
                }).collectList();
        }).flatMapMany(Flux::fromIterable);
    }

    public Flux<PersonalMonthlyTimesheetDTO> rejectMulti(ReviewMultipleTimeSheetDTO dto) {
        var monthlyReviewDTO = new ReviewTimeSheetDTO();
        monthlyReviewDTO.setStatus(TimesheetReviewStatus.REJECTED);
        monthlyReviewDTO.setNote(dto.getNote());
        monthlyReviewDTO.setSignatureFile(dto.getSignatureFile());
        return monthlyTimeSheetReviewService.save(monthlyReviewDTO).flatMap(monthly -> {
            return personalMonthlyTimesheetRepository.findAllByIdIn(List.of(dto.getIds()))
                .map(entity -> {
                    entity.setStatus(TimesheetReviewStatus.REJECTED);
                    entity.setReviewId(monthly.getId());
                    return entity.setIsPersisted();
                })
                .flatMap(personalMonthlyTimesheetRepository::save)
                .flatMap(item -> {

                    return this.fillTimeKeepingForPersonalMonthlyTimesheet(item).then(
                        timeKeepingRepository.lockAllByMonthlyTimesheetId(item.getId()).then(Mono.just(personalMonthlyTimesheetMapper.toDto(item))));
                })
                .collectList();
        }).flatMapMany(Flux::fromIterable);
    }

    @Autowired
    public void setEmployeeShiftDetailService(EmployeeShiftDetailService employeeShiftDetailService) {
        this.employeeShiftDetailService = employeeShiftDetailService;
    }
}
