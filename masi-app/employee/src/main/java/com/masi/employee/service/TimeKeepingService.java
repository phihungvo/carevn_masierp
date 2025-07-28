package com.masi.employee.service;

import com.carevn.masi.dto.ApiResponse;
import com.carevn.masi.utils.SecurityUtils;
import com.masi.employee.domain.*;
import com.masi.employee.domain.enumeration.*;
import com.masi.employee.repository.*;
import com.masi.employee.security.AuthoritiesConstants;
import com.masi.employee.service.dto.*;
import com.masi.employee.service.mapper.TimeKeepingMapper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import com.masi.employee.service.web.client.ConfigClient;
import com.masi.employee.web.rest.errors.BadRequestAlertException;
import com.opencsv.CSVWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing
 * {@link com.masi.employee.domain.TimeKeeping}.
 */
@Service
public class TimeKeepingService {

    private final Logger log = LoggerFactory.getLogger(TimeKeepingService.class);

    private final TimeKeepingRepository timeKeepingRepository;
    private final EmployeeRepository employeeRepository;
    private final TimeKeepingMapper timeKeepingMapper;
    private final EmployeeProfileRepository employeeProfileRepository;
    private final TimeKeepingRecordRepository timeKeepingRecordRepository;
    private final PersonalMonthlyTimesheetService personalMonthlyTimesheetService;
    private final LeaveDayService leaveDayService;
    private final TimeKeepingViolationRepository violationRepository;
    private final LeaveReportService leaveReportService;
    private final int MAX_HOURS = 16;
    private final int MIN_HOUR = 8;
    private static final String ENTITY_NAME = "masiEmployeeTimeKeeping";
    private final ConfigClient configClient;
    private final EmployeeShiftDetailService employeeShiftDetailService;
    private final EmployeeShiftDetailRepository employeeShiftDetailRepository;

    public TimeKeepingService(TimeKeepingRepository timeKeepingRepository, TimeKeepingMapper timeKeepingMapper, EmployeeProfileRepository employeeProfileRepository, PersonalMonthlyTimesheetService personalMonthlyTimesheetService, TimeKeepingRecordRepository timeKeepingRecordRepository, LeaveDayService leaveDayService, EmployeeRepository employeeRepository, TimeKeepingViolationRepository violationRepository, LeaveReportService leaveReportService, ConfigClient configClient, EmployeeShiftDetailService employeeShiftDetailService, EmployeeShiftDetailRepository employeeShiftDetailRepository) {
        this.timeKeepingRepository = timeKeepingRepository;
        this.timeKeepingMapper = timeKeepingMapper;
        this.employeeProfileRepository = employeeProfileRepository;
        this.timeKeepingRecordRepository = timeKeepingRecordRepository;
        this.employeeRepository = employeeRepository;
        this.leaveDayService = leaveDayService;
        this.violationRepository = violationRepository;
        this.personalMonthlyTimesheetService = personalMonthlyTimesheetService;

        this.leaveReportService = leaveReportService;

        this.configClient = configClient;
        this.employeeShiftDetailService = employeeShiftDetailService;
        this.employeeShiftDetailRepository = employeeShiftDetailRepository;
    }


    public Mono<byte[]> generateLeaveTrackingExcel(Mono<Collection<LeaveDayReport>> collectionMono, int year, WorkspaceType workspaceType) {

        return collectionMono.flatMap(leaveDayReports -> {
            try {
                byte[] excelBytes = leaveReportService.generateLeaveTrackingExcel(leaveDayReports, year, workspaceType);
                return Mono.just(excelBytes);
            } catch (IOException e) {
                return Mono.error(e); // Nếu có lỗi thì trả về lỗi
            }
        });
    }

    @Transactional(readOnly = true)
    public Mono<Collection<LeaveDayReport>> getLeaveDayReport(LocalDate startDate, LocalDate endDate, WorkspaceType workspaceType) {
        return SecurityUtils.getUserJWTDetail().flatMap(u -> timeKeepingRepository.getLeaveDayReport(startDate, endDate, u.getCompanyId(), workspaceType).collectList().flatMap(this::convertToReport));
    }

    private Mono<Collection<LeaveDayReport>> convertToReport(List<LeaveDayReportQuery> raw) {
        Map<String, LeaveDayReport> grouped = new HashMap<>();
        raw.forEach(query -> {
            grouped.computeIfAbsent(query.getEmployeeId(), id -> LeaveDayReport.builder().employeeId(id).fullName(query.getFullName()).joinDate(query.getJoinDate()).startDate(query.getStartDate()).leaveDayReportItems(new LinkedList<>()).build()).getLeaveDayReportItems().add(new LeaveDayReport.LeaveDayReportItem(query.getMonth(), query.getValue()));
        });
        return Mono.just(grouped.values());
    }


    @Transactional(readOnly = true)
    public Flux<TimeKeepingDTO> findAllByListId(List<UUID> ids) {
        return timeKeepingRepository.findAllByIdIn(ids).map(TimeKeeping::toDto);
    }

    @Transactional
    public Mono<TimeKeepingDTO> save(TimeKeepingDTO timeKeepingDTO) {
        log.debug("Request to save TimeKeeping : {}", timeKeepingDTO);
        var entity = timeKeepingMapper.toEntity(timeKeepingDTO);
        entity.applyCharacteristics(timeKeepingDTO.getCharacter());
        return timeKeepingRepository.save(entity).map(TimeKeeping::toDto);
    }

    @Transactional
    public Mono<TimeKeepingDTO> update(TimeKeepingDTO timeKeepingDTO) {
        log.debug("Request to update TimeKeeping : {}", timeKeepingDTO);
        var entity = timeKeepingMapper.toEntity(timeKeepingDTO).setIsPersisted();
        entity.applyCharacteristics(timeKeepingDTO.getCharacter());
        return timeKeepingRepository.save(entity).map(TimeKeeping::toDto);
    }

    @Transactional
    public Mono<TimeKeepingDTO> createNew(TimeKeepingDTO timeKeepingDTO) {
        log.debug("Request to create new TimeKeeping : {}", timeKeepingDTO);
        timeKeepingDTO.setId(UUID.randomUUID());
        timeKeepingDTO.setCreatedAt(ZonedDateTime.now());
        timeKeepingDTO.setLastUpdatedAt(ZonedDateTime.now());
        timeKeepingDTO.setIsAbnormal(false);
        timeKeepingDTO.setIsOverride(false);
        timeKeepingDTO.setLocked(false);
        log.info("Creating new TimeKeeping for employee {} on date {}", timeKeepingDTO.getEmployeeId(), timeKeepingDTO.getDate());
        return

            personalMonthlyTimesheetService.getByEmployeeIdAndMonthOrCreate(timeKeepingDTO.getEmployeeId(), timeKeepingDTO.getDate(), timeKeepingDTO.getType()).flatMap(personalMonthlyTimesheetDTO -> {
                if (TimesheetReviewStatus.APPROVED.equals(personalMonthlyTimesheetDTO.getStatus())) {
                    return Mono.error(new BadRequestAlertException("Cannot create new timekeeping record for approved timesheet", ENTITY_NAME, "timesheetlocked"));
                }
                timeKeepingDTO.setPersonalMonthlyTimesheet(personalMonthlyTimesheetDTO);
                return Mono.just(timeKeepingDTO);
            }).flatMap(dto -> {
                return timeKeepingRepository.findByEmployeeIdAndDateAndTimeKeepingType(timeKeepingDTO.getEmployeeId(), timeKeepingDTO.getDate(), timeKeepingDTO.getType()).hasElement().flatMap(isExist -> {
                    if (Boolean.TRUE.equals(isExist)) {
                        return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("TimeKeeping record already exists for employee %s on %s with type %s", timeKeepingDTO.getEmployeeId(), timeKeepingDTO.getDate(), timeKeepingDTO.getType())));
                    } else {
                        TimeKeeping entity = timeKeepingMapper.toEntity(timeKeepingDTO);
                        entity.setPersonalMonthlyTimesheetId(timeKeepingDTO.getPersonalMonthlyTimesheet().getId());
                        entity.applyCharacteristics(timeKeepingDTO.getCharacter());
                        entity.setTimeKeepingType(timeKeepingDTO.getType());
                        return timeKeepingRepository.save(entity).map(TimeKeeping::toDto);
                    }
                });
            });

    }

    public Mono<TimeKeepingRecord> updateWithNewRecord(TimeKeepingRecord timeKeepingRecord, TimeKeepingType timeKeepingType) {
        log.debug("Request to update TimeKeeping with new record : {}", timeKeepingRecord);

        return timeKeepingRepository.findByEmployeeIdAndDateAndTimeKeepingType(
                timeKeepingRecord.getEmployeeId(),
                timeKeepingRecord.getCheckIn().toLocalDate(),
                timeKeepingType
            )
            .flatMap(timeKeeping -> {
                log.info("Time keeping record found for employee {} on date {}. Updating the record.",
                    timeKeepingRecord.getEmployeeId(), timeKeepingRecord.getCheckIn().toLocalDate());

                return timeKeepingRecordRepository.findByEmployeeAndDateNoPagination(
                        timeKeepingRecord.getEmployeeId(),
                        timeKeepingRecord.getCheckIn().toLocalDate()
                    )
                    .collectList()
                    .flatMap(existingRecords -> {
                        float hoursWorked = calculateHoursWorked(existingRecords);
                        timeKeeping.setHoursWorked(hoursWorked);
                        ZonedDateTime max = existingRecords.stream()
                            .map(TimeKeepingRecord::getCheckIn)
                            .max(ZonedDateTime::compareTo).orElse(null);
                        ZonedDateTime min = existingRecords.stream()
                            .map(TimeKeepingRecord::getCheckIn)
                            .min(ZonedDateTime::compareTo).orElse(null);
                        timeKeeping.setFirstCheckIn(min);
                        timeKeeping.setLastCheckIn(max);
                        timeKeeping.setIsAbnormal(hoursWorked > MAX_HOURS || hoursWorked < MIN_HOUR);
                        timeKeeping.setLastUpdatedAt(ZonedDateTime.now());

                        return timeKeepingRepository.save(timeKeeping)
                            .map(TimeKeeping::toDto)
                            .flatMap(employeeShiftDetailService::createOrUpdateEmployeeShiftDetail)
                            .thenReturn(timeKeepingRecord);
                    });
            })
            .switchIfEmpty(Mono.defer(() -> {
                log.info("No time keeping record found for employee {} on date {}. Creating a new one.",
                    timeKeepingRecord.getEmployeeId(), timeKeepingRecord.getCheckIn().toLocalDate());

                TimeKeepingDTO timeKeepingDTO = new TimeKeepingDTO();
                timeKeepingDTO.setId(UUID.randomUUID());
                timeKeepingDTO.setEmployeeId(timeKeepingRecord.getEmployeeId());
                timeKeepingDTO.setDate(timeKeepingRecord.getCheckIn().toLocalDate());
                timeKeepingDTO.setFirstCheckIn(timeKeepingRecord.getCheckIn());
                timeKeepingDTO.setHoursWorked(0F);
                timeKeepingDTO.setIsAbnormal(true);
                timeKeepingDTO.setIsOverride(false);
                timeKeepingDTO.setLocked(false);
                timeKeepingDTO.setCreatedAt(ZonedDateTime.now());
                timeKeepingDTO.setLastUpdatedAt(ZonedDateTime.now());
                timeKeepingDTO.setType(timeKeepingType);

                return this.addMonthlyTimeSheet(timeKeepingDTO)
                    .flatMap(dto -> timeKeepingRepository.save(timeKeepingDTO.toEntity()))
                    .then(employeeShiftDetailService.createOrUpdateEmployeeShiftDetail(timeKeepingDTO))
                    .thenReturn(timeKeepingRecord);
            }));
    }

    private Mono<TimeKeepingDTO> addMonthlyTimeSheet(TimeKeepingDTO timeKeepingDTO) {
        return personalMonthlyTimesheetService.getByEmployeeIdAndMonthOrCreate(timeKeepingDTO.getEmployeeId(), timeKeepingDTO.getDate(), timeKeepingDTO.getType()).map(personalMonthlyTimesheetDTO -> {
            timeKeepingDTO.setPersonalMonthlyTimesheet(personalMonthlyTimesheetDTO);
            timeKeepingDTO.setPersonalMonthlyTimesheetId(personalMonthlyTimesheetDTO.getId());
            return timeKeepingDTO;
        }).doOnError(error -> log.error("Failed to add monthly time sheet: {}", error));
    }

    public Mono<TimeKeeping> createNewAbsentRecord(EmployeeProfile employeeProfile, LocalDate date) {
        log.debug("Request to create new TimeKeeping for absent");
        if (date.getDayOfWeek() == DayOfWeek.SUNDAY && employeeProfile.getWorkspace().getWorkspaceType() == WorkspaceType.OFFICE) {
            return Mono.empty();
        }
        TimeKeepingDTO timeKeepingDTO = new TimeKeepingDTO();
        timeKeepingDTO.setId(UUID.randomUUID());
        timeKeepingDTO.setEmployeeId(employeeProfile.getId());
        timeKeepingDTO.setDate(date);
        timeKeepingDTO.setIsAbnormal(true);
        timeKeepingDTO.setIsOverride(false);
        timeKeepingDTO.setLocked(false);
        timeKeepingDTO.setCreatedAt(ZonedDateTime.now());
        timeKeepingDTO.setLastUpdatedAt(ZonedDateTime.now());
        TimeKeeping newTimeKeeping = timeKeepingDTO.toEntity();
        newTimeKeeping.applyCharacteristics(timeKeepingDTO.getCharacter());
        newTimeKeeping.setViolationType(TimeKeepingViolationType.ABSENT_WITHOUT_REQUEST);
        return this.addMonthlyTimeSheet(timeKeepingDTO).flatMap(dto -> {
            return timeKeepingRepository.save(newTimeKeeping).doOnError(error -> log.error("Failed to save new absent record: {}", error.getMessage()));
        }).switchIfEmpty(Mono.defer(() -> {
            log.error("Failed to save new absent record");
            return Mono.empty();
        }));

    }

    public Mono<TimeKeeping> createOffTimeKeeping(UUID employeeId, LocalDate date) {
        log.debug("Request to create new TimeKeeping for off");
        TimeKeepingDTO timeKeepingDTO = new TimeKeepingDTO();
        timeKeepingDTO.setId(UUID.randomUUID());
        timeKeepingDTO.setEmployeeId(employeeId);
        timeKeepingDTO.setDate(date);
        timeKeepingDTO.setIsDayOff(false);
        timeKeepingDTO.setHoursWorked(0f);
        timeKeepingDTO.setIsAbnormal(false);
        timeKeepingDTO.setIsOverride(false);
        timeKeepingDTO.setLocked(false);
        timeKeepingDTO.setCreatedAt(ZonedDateTime.now());
        timeKeepingDTO.setLastUpdatedAt(ZonedDateTime.now());
        return this.addMonthlyTimeSheet(timeKeepingDTO).flatMap(dto -> {
            return timeKeepingRepository.save(timeKeepingDTO.toEntity()).doOnError(error -> log.error("Failed to save new off record: {}", error.getMessage()));
        }).switchIfEmpty(Mono.defer(() -> {
            log.error("Failed to save new off record");
            return Mono.empty();
        }));
    }

    private float calculateHoursWorked(List<TimeKeepingRecord> records) {
        if (records.size() < 2) return 0F;
        ZonedDateTime max = records.stream().map(timeKeeping -> {
            log.info("Check in: {}", timeKeeping.getCheckIn());
            return timeKeeping.getCheckIn();
        }).max(ZonedDateTime::compareTo).orElse(null);

        ZonedDateTime min = records.stream().map(TimeKeepingRecord::getCheckIn).min(ZonedDateTime::compareTo).orElse(null);
        if (Objects.isNull(min)) return 0F;
        try {
            float hoursWorked = (max.toEpochSecond() - min.toEpochSecond()) / 3600F;
            BigDecimal roundedHoursWorked = new BigDecimal(hoursWorked).setScale(2, RoundingMode.HALF_UP);
            return roundedHoursWorked.floatValue();
        } catch (NumberFormatException e) {
            log.debug("Error parsing hoursWorked: {} ", e.getMessage());
            return 0.0F;
        }
    }

    public Flux<TimeKeepingDTO> findAllWhereNotHaveRecord(LocalDate startDate, LocalDate endDate) {
        return timeKeepingRepository.findAllWhereNotHaveRecord(startDate, endDate).map(TimeKeeping::toDto);
    }

    @Transactional
    public Mono<TimeKeepingDTO> partialUpdate(TimeKeepingDTO timeKeepingDTO, Authentication authentication) {
        log.debug("Request to partially update TimeKeeping : {}", timeKeepingDTO);
        timeKeepingDTO.isValid();
        return timeKeepingRepository.findById(timeKeepingDTO.getId()).flatMap(existingTimeKeeping -> {

                if (existingTimeKeeping.getLocked() && authentication.getAuthorities().stream().noneMatch(authority -> authority.getAuthority().equals(AuthoritiesConstants.ADMIN))) {
                    return Mono.error(new BadRequestAlertException("Entity is locked, and there are no update permissions.", ENTITY_NAME, "timesheetlocked"));
                }
                existingTimeKeeping.partialUpdate(timeKeepingDTO);
                existingTimeKeeping.setIsPersisted();
                existingTimeKeeping.setLastUpdatedAt(ZonedDateTime.now());
                if (existingTimeKeeping.getTimeKeepingType() == TimeKeepingType.HOUR) {
                    existingTimeKeeping.setIsAbnormal(existingTimeKeeping.getHoursWorked() > MAX_HOURS || existingTimeKeeping.getHoursWorked() < MIN_HOUR);
                    // remove violation if hours worked is valid
                    if (existingTimeKeeping.hasValidWorkedHours()) {
                        existingTimeKeeping.setViolationType(null);
                    }
                }
                EmployeeShiftDetail employeeShiftDetail = new EmployeeShiftDetail();
                existingTimeKeeping.setEmployeeShiftDetail(employeeShiftDetail);
                existingTimeKeeping.applyCharacteristics(timeKeepingDTO.getCharacter());
                return timeKeepingRepository.save(existingTimeKeeping)
                    .flatMap(e -> employeeShiftDetailService.partialUpdateAbbreviated(e).thenReturn(e));
            })
            // remove violation if hours worked is valid
            .flatMap(timeKeeping -> {
                if (timeKeeping.hasValidWorkedHours()) {
                    return violationRepository.deleteByTimeKeepingId(timeKeeping.getId()).then(Mono.just(timeKeeping));
                }
                return Mono.just(timeKeeping);
            })
            // remove day off if employee has worked hours valid
            .flatMap(timeKeeping -> {
                if (timeKeeping.hasValidWorkedHours()) {
                    return leaveDayService.deleteByEmployeeAndDate(timeKeeping.getEmployeeId(), timeKeeping.getDate()).then(Mono.just(timeKeeping));
                }
                return Mono.just(timeKeeping);
            }).map(TimeKeeping::toDto).doOnError(error -> log.error("Failed to partially update TimeKeeping: {}", error.getMessage()));
    }

    @Transactional(readOnly = true)
    public Flux<TimeKeepingDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TimeKeepings");
        return timeKeepingRepository.findAllBy(pageable).map(TimeKeeping::toDto);
    }

    @Transactional(readOnly = true)
    public Mono<ApiResponse<TimeKeepingDTO>> findAndCount(Pageable pageable) {
        log.debug("Request to get all TimeKeepings");

        Mono<List<TimeKeepingDTO>> timeKeepingList = timeKeepingRepository.findAllBy(pageable).map(TimeKeeping::toDto).collectList();

        Mono<Long> count = timeKeepingRepository.count();

        return Mono.zip(timeKeepingList, count).map(tuple -> new ApiResponse<TimeKeepingDTO>(tuple.getT1(), tuple.getT2()));
    }

    public Mono<Long> countAll() {
        return timeKeepingRepository.count();
    }

//    @Transactional(readOnly = true)
//    public Mono<TimeKeepingDTO> findOne(UUID id) {
//        log.debug("Request to get TimeKeeping : {}", id);
//        return timeKeepingRepository.findById(id).map(TimeKeeping::toDto);
//    }

    @Transactional(readOnly = true)
    public Mono<TimeKeepingDTO> findOne(UUID id) {
        log.debug("Request to get TimeKeeping : {}", id);

        return timeKeepingRepository.findById(id)
            .flatMap(e -> {
                TimeKeepingDTO timeKeepingDTO = e.toDto();
                return employeeShiftDetailRepository.findAllByTimeKeepingIdAndGroupBy(Collections.singleton(id))
                    .map(employeeShiftDetail -> {
                        if (employeeShiftDetail != null)
                            timeKeepingDTO.setTotalCompletionPercent(employeeShiftDetail.getCompletionPercent());
                        else
                            timeKeepingDTO.setTotalCompletionPercent(0f);
                        return timeKeepingDTO;
                    }).then(Mono.just(timeKeepingDTO))
                    .switchIfEmpty(Mono.just(timeKeepingDTO));
            });
    }


    public Mono<Void> delete(UUID id) {
        log.debug("Request to delete TimeKeeping : {}", id);
        return timeKeepingRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Mono<TimeKeepingDTO> findAllByEmployeeAndDate(UUID employeeId, LocalDate date, TimeKeepingType timeKeepingType) {
        return timeKeepingRepository.findByEmployeeIdAndDateAndTimeKeepingType(employeeId, date, timeKeepingType).map(TimeKeeping::toDto);
    }

    @Transactional(readOnly = true)
    public Mono<ApiResponse<TimeKeepingDTO>> findAllByEmployeeAndDateBetween(UUID employeeId, LocalDate startDate, LocalDate endDate, Pageable pageable) {
        Mono<List<TimeKeepingDTO>> timeKeepings = timeKeepingRepository.findAllByEmployeeAndDateBetween(employeeId, startDate, endDate, pageable).map(TimeKeeping::toDto).collectList();

        Mono<Long> count = timeKeepingRepository.countAllByEmployeeAndDateBetween(employeeId, startDate, endDate);

        return Mono.zip(timeKeepings, count).map(tuple -> new ApiResponse<TimeKeepingDTO>(tuple.getT1(), tuple.getT2()));
    }

    @Transactional(readOnly = true)
    public Mono<ApiResponse<TimeKeepingDTO>> findAllByDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable, TimeKeepingType type, WorkspaceType workspaceType) {

        return SecurityUtils.getCompanyId().flatMap(companyId -> {
            var query = TimekeepingQuery.builder().startDate(startDate).endDate(endDate).type(type).workSpaceTypes(workspaceType).pageable(pageable).companyId(companyId).build();
            Mono<List<TimeKeepingDTO>> timeKeepings = timeKeepingRepository
                .findAllByDateBetween(query)
                .map(TimeKeeping::toDto)
                .collectList()
                .flatMap(timeKeepingDTOS -> {
                    Collection<UUID> timekeepingIds = timeKeepingDTOS.stream()
//                                .filter(timeKeepingDTO -> {
//                                    try {
//                                        return timeKeepingDTO.getEmployee().getWorkspace().equals(WorkspaceType.OFFICE);
//                                    } catch (Exception e) {
//                                        return false;
//                                    }
//                                })
                        .map(TimeKeepingDTO::getId)
                        .collect(Collectors.toCollection(ArrayList::new));
                    timekeepingIds.add(UUID.randomUUID());
                    return employeeShiftDetailRepository.findAllByTimeKeepingIdAndGroupBy(timekeepingIds)
                        .collectList()
                        .map(employeeShiftDetails -> {
                            Map<UUID, Float> employeeShiftDetailMap = employeeShiftDetails
                                .stream()
                                .collect(Collectors.toMap(EmployeeShiftDetail::getTimeKeepingId, EmployeeShiftDetail::getCompletionPercent));

                            timeKeepingDTOS.forEach(timeKeepingDTO -> {
                                Float completionPercent = employeeShiftDetailMap.get(timeKeepingDTO.getId());
                                if (completionPercent != null)
                                    timeKeepingDTO.setTotalCompletionPercent(completionPercent);

                            });
                            return timeKeepingDTOS;
                        });
                });
            Mono<Long> count = timeKeepingRepository.countByDateBetween(query);
            return Mono.zip(timeKeepings, count).map(tuple -> new ApiResponse<TimeKeepingDTO>(tuple.getT1(), tuple.getT2()));
        });
    }

    @Transactional(readOnly = true)
    public Mono<ApiResponse<TimeKeepingDTO>> findAllByDate(LocalDate date, Pageable pageable) {
        log.debug("Request to find all by date");
        Mono<List<TimeKeepingDTO>> timeKeepings = timeKeepingRepository.findAllByDate(date, pageable).map(TimeKeeping::toDto).collectList();

        Mono<Long> count = timeKeepingRepository.countByDate(date);

        return Mono.zip(timeKeepings, count).map(tuple -> new ApiResponse<TimeKeepingDTO>(tuple.getT1(), tuple.getT2()));
    }

    @Transactional(readOnly = true)
    protected Flux<Employee> findAndMapToEmployee(TimekeepingQuery query) {
        var pageable = query.getPageable();
        var startDate = query.getStartDate();
        var endDate = query.getEndDate();
        var type = query.getType();
        var workspaceType = query.getWorkSpaceTypes();
        EmployeeProfileQuery.EmployeeProfileQueryBuilder employeeProfileQuery = EmployeeProfileQuery
            .builder()
            .workspaceTypes(query.getWorkSpaceTypes() == null ? null : List.of(query.getWorkSpaceTypes()))
            .workspaceIds(query.getWorkspaceIds())
            .company(query.getCompanyId());
        List<String> dependOnWorkspace = List.of(TimeKeepingType.MIXING_FLOUR.name(), TimeKeepingType.LOADING_UNLOADING.name(), TimeKeepingType.DRIVER.name());
        if (query.getType() != null && dependOnWorkspace.stream().anyMatch(s -> s.equals(query.getType().name()))) {
            employeeProfileQuery.workspaceNNames(List.of(query.getType().name()));
        }
        return SecurityUtils.getCompanyId().flatMap(companyId -> {
            employeeProfileQuery.company(companyId);
            return employeeProfileRepository.findAllByQuery(employeeProfileQuery.build(), query.getPageable()).map(EmployeeProfile::toShortEmployee).collectList();
        }).switchIfEmpty(employeeProfileRepository.findAllByQuery(employeeProfileQuery.build(), query.getPageable()).map(EmployeeProfile::toShortEmployee).collectList()).flatMapMany(Flux::fromIterable).concatMap(employee -> {

            Mono<List<TimeKeeping>> timeKeepingList = timeKeepingRepository.findAllByEmployeeAndDateBetweenWithoutPaginate(employee.getId(), startDate, endDate, type).map(e -> {
                if (e.getCompletion_percent() != null && e.getCompletion_percent() >= 0)
                    e.setHoursWorked(e.getCompletion_percent());
                e.setNote(e.resolveTimesheetDayValue());
                return e;
            }).collectList();

            Mono<List<LeaveDay>> leaveDayList = leaveDayService.findAllByEmployeeAndDateBetween(employee.getId(), startDate, endDate).collectList();

            Mono<List<TimeKeepingViolation>> violationList = violationRepository.findAllByEmployeeId(employee.getId()).collectList();

            return Mono.zip(timeKeepingList, leaveDayList, violationList).flatMap(tuple -> {
                List<TimeKeeping> timeKeepings = tuple.getT1();
                List<LeaveDay> leaveDays = tuple.getT2();
                List<TimeKeepingViolation> violations = tuple.getT3();

                for (TimeKeeping timeKeeping : timeKeepings) {
                    timeKeeping.setLeaveDay(leaveDays.stream().filter(leaveDay -> leaveDay.getDate().equals(timeKeeping.getDate())).findFirst().orElse(null));
                    timeKeeping.setViolation(violations.stream().filter(violation -> Objects.equals(violation.getTimeKeepingId(), timeKeeping.getId())).findFirst().orElse(null));
                    timeKeeping.setNote(timeKeeping.resolveTimesheetDayValue());
                }

                employee.setTimeKeepings(timeKeepings);
                return Mono.just(employee);
            });
        });
    }

    @Transactional(readOnly = true)
    public Mono<ApiResponse<EmployeeDTO>> findAndMapToEmployeeByDateBetween(TimekeepingQuery query) {
        EmployeeProfileQuery.EmployeeProfileQueryBuilder employeeProfileQuery = EmployeeProfileQuery.builder().workspaceTypes(List.of(query.getWorkSpaceTypes())).workspaceIds(query.getWorkspaceIds()).company(query.getCompanyId());
        List<String> dependOnWorkspace = List.of(TimeKeepingType.MIXING_FLOUR.name(), TimeKeepingType.LOADING_UNLOADING.name(), TimeKeepingType.DRIVER.name());
        if (query.getType() != null && dependOnWorkspace.stream().anyMatch(s -> s.equals(query.getType().name()))) {
            employeeProfileQuery.workspaceNNames(List.of(query.getType().name()));
        }
        return findAndMapToEmployee(query).map(Employee::toDto).collectList().zipWith(
            SecurityUtils.getCompanyId().flatMap(companyId -> {
                employeeProfileQuery.company(companyId);
                return employeeProfileRepository.countByQuery(employeeProfileQuery.build());
            })
        ).map(data -> new ApiResponse<>(data.getT1(), data.getT2()));
    }

    //    @Scheduled(cron = "0 0 23 * * SUN", zone = "Asia/Ho_Chi_Minh")
    public Mono<Void> lockTimeKeeping() {
        log.info("Starting lock time-keeping record at the end of the week");
        final LocalDate currentDate = LocalDate.now();
        LocalDate startDate = currentDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        TimekeepingQuery query = TimekeepingQuery.builder().startDate(startDate).endDate(currentDate).type(TimeKeepingType.HOUR).build();
        return timeKeepingRepository.findAllByDateBetween(query).flatMap(timeKeeping -> processTimeKeeping(timeKeeping).onErrorResume(error -> {
            log.error("Error processing lock time-keeping at the end of the week", error);
            return Mono.empty();
        })).then();
    }

    private Mono<Void> processTimeKeeping(TimeKeeping timeKeeping) {
        log.debug("Processing lock time-keeping at the end of the week");
        timeKeeping.locked(true);
        return timeKeepingRepository.save(timeKeeping.setIsPersisted()).then(Mono.empty());
    }

    @Transactional(readOnly = true)
    public Mono<byte[]> exportRecordsAsCSV(LocalDate startDate, LocalDate endDate, Pageable pageable, TimeKeepingType type) {
        var query = new TimekeepingQuery();
        query.setStartDate(startDate);
        query.setEndDate(endDate);
        query.setType(type);
        return findAndMapToEmployee(query)
            .doOnError(error -> log.error("Error while exporting records as CSV", error))
            .collectList().handle((records, sink) -> {
                try {
                    sink.next(convertListToCSV(records, startDate, endDate));
                } catch (Exception e) {
                    log.error("Error while exporting records as CSV", e);
                    sink.error(new RuntimeException(e));
                }
            });
    }

    private byte[] convertListToCSV(List<Employee> records, LocalDate startDate, LocalDate endDate) {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(); OutputStreamWriter outputStreamWriter = new OutputStreamWriter(byteArrayOutputStream, StandardCharsets.UTF_8); CSVWriter csvWriter = new CSVWriter(outputStreamWriter)) {

            // compose the headers
            List<String> headers = new ArrayList<>();
            headers.add("Mã NV");
            headers.add("Tên");
            Locale locale = new Locale("vi", "VN");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE d-M").withLocale(locale);

            LocalDate currentDate = startDate;
            while (!currentDate.isAfter(endDate)) {
                headers.add(formatter.format(currentDate));
                currentDate = currentDate.plusDays(1);
            }
            headers.add("Giờ ca");
            headers.add("Lễ 300%");
            headers.add("Ngày off hưởng nguyên lương");
            headers.add("Phép năm");
            headers.add("Tổng giờ tại NM");
            headers.add("Tổng công tại NM");
            headers.add("Tổng công");
            headers.add("Ngày off trong tháng");
            headers.add("Ghi chú");

            // Writing header
            csvWriter.writeNext(headers.toArray(new String[0]));

            // Writing rows for employee
            for (Employee record : records) {
                List<String> row = new ArrayList<>();
                assert record.getId() != null;
                String code = record.getId().toString();
                if (record.getEmployeeProfile() != null) {
                    code = record.getEmployeeProfile().getEmployeeCode();
                }
                row.add(String.valueOf(code));
                row.add(record.getLastName() + " " + record.getFirstName());

                currentDate = startDate;
                while (!currentDate.isAfter(endDate)) {
                    boolean found = false;
                    for (TimeKeeping timeKeeping : record.getTimeKeepings()) {
                        if (timeKeeping.getDate().equals(currentDate)) {
                            row.add(timeKeeping.resolveTimesheetDayValue());
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        row.add("");
                    }
                    currentDate = currentDate.plusDays(1);
                }
                // Giờ ca
                row.add(friendlyNumberFormat(countTotalWorkedHours(record.getTimeKeepings())));
                // Lễ 300%
                row.add(""); // TODO: implement later when PH calendar is available
                // Ngày off hưởng nguyên lương
                float dayOff = countDayOff(record.getTimeKeepings());
                row.add(friendlyNumberFormat(dayOff));
                // Phép năm
                final float paidLeave = countPaidLeave(record.getTimeKeepings());
                row.add(friendlyNumberFormat(paidLeave));
                // Tổng giờ tại NM
                row.add(friendlyNumberFormat(countTotalWorkedHours(record.getTimeKeepings())));
                // Tổng công tại NM
                float totalWorkDays = (float) countTotalWorkedHours(record.getTimeKeepings()) / 8;
                row.add(friendlyNumberFormat(totalWorkDays, 2));
                // Tổng công
                final float compensationLeave = countCompensationLeave(record.getTimeKeepings());
                final float totalDays = dayOff + paidLeave + totalWorkDays + compensationLeave;
                row.add(friendlyNumberFormat(totalDays, 2));
                // Ngày off trong tháng
                row.add(friendlyNumberFormat(countUnpaidLeave(record.getTimeKeepings())));

                // write the row
                csvWriter.writeNext(row.toArray(new String[0]));
            }
            // write footer rows
            List<List<String>> footerRows = computeTableFooter(records, startDate, endDate);
            footerRows.forEach(row -> csvWriter.writeNext(row.toArray(new String[0])));

            csvWriter.flush();
            return byteArrayOutputStream.toByteArray();

        } catch (IOException e) {
            throw new RuntimeException("Error while converting list to CSV", e);
        }
    }

    private String friendlyNumberFormat(float number) {
        return friendlyNumberFormat(number, 1);
    }

    private String friendlyNumberFormat(float number, int scale) {
        if (number == (int) number) {
            return String.format("%d", (int) number);
        } else {
            var format = "%." + scale + "f";
            return String.format(format, number);
        }
    }

    public Flux<Map<UUID, Float>> countAllTotalWorkedHours(List<UUID> epIds) {
        try {
            if (CollectionUtils.isEmpty(epIds)) {
                return Flux.empty();
            }
            String epIdsString = epIds.stream().map(UUID::toString).collect(Collectors.joining(","));
            return timeKeepingRepository.countByListEmployee(epIds).collectList().flatMapMany(results -> {
                Map<UUID, Float> empMap = epIds.stream().collect(Collectors.toMap(id -> id, id -> 0f // Giá trị mặc định là 0 nếu không có dữ liệu
                ));

                results.forEach(result -> {
                    UUID employeeId = UUID.fromString(result.getEmployeeId());
                    Float totalHours = (float) result.getTotalHours();
                    empMap.put(employeeId, totalHours);
                });

                return Flux.just(empMap);
            }).onErrorResume(e -> {
                e.printStackTrace();
                Map<UUID, Float> defaultMap = epIds.stream().collect(Collectors.toMap(id -> id, id -> 0f));
                return Flux.just(defaultMap);
            });
        } catch (Exception e) {
            Map<UUID, Float> defaultMap = epIds.stream().collect(Collectors.toMap(id -> id, id -> 1f));
            return Flux.just(defaultMap);
        }
    }


    public float countDayOff(List<TimeKeeping> timeKeepings) {
        return timeKeepings.stream().filter(TimeKeeping::getIsDayOff).map(t -> {
//            if (LeaveRequestDayType.FULL_DAY.equals(t.getLeaveDayType())) {
//                return 1f;
//            } else if (LeaveRequestDayType.HALF_DAY.equals(t.getLeaveDayType())) {
//                return 0.5f;
//            } else {
//                return 0f;
//            }
            return 1f;
        }).reduce(0f, Float::sum);
    }

    public float countPaidLeave(List<TimeKeeping> timeKeepings) {
        AtomicReference<Float> total = new AtomicReference<>(0f);
        timeKeepings.stream().filter(TimeKeeping::isAnnualLeave).forEach(tk -> {
            if (LeaveRequestDayType.FULL_DAY.equals(tk.getLeaveDayType())) {
                total.getAndSet(total.get() + 1);
            } else if (LeaveRequestDayType.HALF_DAY.equals(tk.getLeaveDayType())) {
                total.getAndSet(total.get() + 0.5f);
            }
        });
        return total.get();
    }

    public float countCompensationLeave(List<TimeKeeping> timeKeepings) {
        AtomicReference<Float> total = new AtomicReference<>(0f);
        timeKeepings.stream().filter(tk -> LeaveType.COMPENSATION_LEAVE.equals(tk.getLeaveType())).forEach(tk -> total.getAndSet(total.get() + 1));
        return total.get();
    }

    public float countTotalWorkedHours(List<TimeKeeping> timeKeepings) {
        return (float) timeKeepings.stream().mapToDouble(timeKeeping -> timeKeeping.getHoursWorked() != null ? timeKeeping.getHoursWorked().doubleValue() : 0).sum();
    }

    public float countTotalWorkFromHomeDays(List<TimeKeeping> timeKeepings) {
        return (float) timeKeepings.stream().filter(timeKeeping -> Boolean.TRUE.equals(timeKeeping.getIsWorkFromHome())).count();
    }

    public float countUnpaidLeave(List<TimeKeeping> timeKeepings) {
        AtomicReference<Float> total = new AtomicReference<>(0f);
        timeKeepings.stream().filter(TimeKeeping::isUnpaidLeave).forEach(tk -> total.getAndSet(total.get() + 1));
        return total.get();
    }


    public List<List<String>> computeTableFooter(List<Employee> employeeList, LocalDate startDate, LocalDate endDate) {
        List<List<String>> footerRows = new ArrayList<>();
        LocalDate currentDate = startDate;
        List<String> rowTotalHours = new ArrayList<>();
        List<String> rowTotalDays = new ArrayList<>();
        // insert row's title
        rowTotalHours.add("Tổng giờ");
        rowTotalHours.add("");
        rowTotalDays.add("Tổng công");
        rowTotalDays.add("");
        // process data
        while (!currentDate.isAfter(endDate)) {
            AtomicReference<Float> employeeTotalHours = new AtomicReference<>(0f);
            LocalDate finalCurrentDate = currentDate;
            employeeList.forEach(employee -> {
                if (!CollectionUtils.isEmpty(employee.getTimeKeepings())) {
                    employee.getTimeKeepings().stream().filter(t -> finalCurrentDate.equals(t.getDate())).findFirst().map(t -> Objects.nonNull(t.getHoursWorked()) ? employeeTotalHours.updateAndGet(v -> (float) (v + t.getHoursWorked())) : null);
                }
            });
            rowTotalHours.add(friendlyNumberFormat(employeeTotalHours.get()));
            rowTotalDays.add(friendlyNumberFormat((employeeTotalHours.get() / 8), 2));
            currentDate = currentDate.plusDays(1);
        }
        // count total worked hours of all employees (Giờ ca)
        AtomicReference<Float> totalHours = new AtomicReference<>(0f);
        employeeList.forEach(employee -> {
            if (!CollectionUtils.isEmpty(employee.getTimeKeepings())) {
                employee.getTimeKeepings().forEach(t -> {
                    if (Objects.nonNull(t.getHoursWorked())) {
                        totalHours.updateAndGet(v -> (float) (v + t.getHoursWorked()));
                    }
                });
            }
        });
        rowTotalHours.add(friendlyNumberFormat(totalHours.get()));
        // append placeholder for PH (Lễ 300%)
        rowTotalHours.add("-");
        // count time keeping has isDayOff = true of all employees (Ngày off hưởng
        // nguyên lương)
        AtomicLong totalDayOff = new AtomicLong();
        employeeList.stream().map(Employee::getTimeKeepings).forEach(timeKeepings -> {
            totalDayOff.addAndGet(timeKeepings.stream().filter(TimeKeeping::getIsDayOff).count());
        });
        rowTotalHours.add(totalDayOff.toString());
        // count total paid leave of all employees (Phép năm)
        AtomicLong totalPaidLeave = new AtomicLong();
        employeeList.stream().map(Employee::getTimeKeepings).forEach(timeKeepings -> {
            totalPaidLeave.addAndGet(timeKeepings.stream().filter(t -> Objects.nonNull(t.getLeaveDay()) && t.getLeaveDay().isPaidLeave()).count());
        });
        rowTotalHours.add(totalPaidLeave.toString());
        // count total worked hours of all employees (Tổng giờ tại NM)
        rowTotalHours.add(friendlyNumberFormat(totalHours.get(), 1));
        // count total work days of all employees (Tổng công tại NM)
        rowTotalHours.add(friendlyNumberFormat((totalHours.get() / 8), 2));
        // count total days of all employees (Tổng công)
        rowTotalHours.add(friendlyNumberFormat((totalDayOff.get() + totalPaidLeave.get() + (totalHours.get() / 8)), 2));
        // count total unpaid leave of all employees (Ngày off trong tháng)
        AtomicLong totalUnpaidLeave = new AtomicLong();
        employeeList.stream().map(Employee::getTimeKeepings).forEach(timeKeepings -> {
            totalUnpaidLeave.addAndGet(timeKeepings.stream().filter(t -> Objects.nonNull(t.getLeaveDay()) && !t.getLeaveDay().isPaidLeave()).count());
            // also count for time keeping has violation type is ABSENT_WITHOUT_REQUEST
            totalUnpaidLeave.addAndGet(timeKeepings.stream().filter(t -> Objects.nonNull(t.getViolation()) && TimeKeepingViolationType.ABSENT_WITHOUT_REQUEST.equals(t.getViolation().getType())).count());
        });
        rowTotalHours.add(totalUnpaidLeave.toString());
        // append the footer rows
        footerRows.add(rowTotalHours);
        footerRows.add(rowTotalDays);
        return footerRows;
    }

    public Flux<TimeKeepingDTO> isEmployeeOffInDay(UUID employeeId, LocalDate startDate, LocalDate endDate) {

        return timeKeepingRepository.findByEmployeeIdOfAndDateIn(employeeId, startDate, endDate).map(TimeKeeping::toDto);
    }


    public Mono<List<TimeKeeping>> resolveListLeaveDay(LeaveRequest leaveRequest) {
        return employeeProfileRepository.findFirstById(leaveRequest.getEmployeeId())
            .flatMap(e -> configClient.getStandardWorkScheduleConfig(
                    e.getWorkspace().getWorkspaceType().name(), e.getCompany())
                .flatMap(configDTO -> {
                    List<TimeKeeping> processDays = new ArrayList<>();
                    float totalWorkDays = 0;
                    LocalDate start = leaveRequest.getFromDate().plusDays(1);
                    LocalDate toDate = leaveRequest.getToDate();

                    // Xử lý nghỉ nửa ngày
                    if (LeaveRequestDayType.HALF_DAY.equals(leaveRequest.getLeaveRequestDayType())) {
                        processDays.add(createHalfDayTimeKeeping(leaveRequest));
                        return Mono.just(processDays);
                    }

                    // Xử lý nghỉ nhiều ngày
                    for (LocalDate date = start; !date.isEqual(toDate); date = date.plusDays(1)) {
                        int dayOfWeek = date.getDayOfWeek().getValue();
                        int numberOfShifts = configDTO.stream()
                            .filter(config -> config.getDayOfWeek() == dayOfWeek)
                            .map(StandardWorkScheduleConfigDTO::getNumberOfShifts)
                            .findFirst().orElse(0);

                        if (numberOfShifts > 0) {
                            float workFraction = numberOfShifts * 1.0f / (float) LeaveRequestService.TOTAL_SHIFT_OF_DAY;
                            totalWorkDays += workFraction;

                            processDays.add(TimeKeeping.builder()
                                .employeeId(leaveRequest.getEmployeeId())
                                .date(date)
                                .leaveDayType(workFraction == 1 ? LeaveRequestDayType.FULL_DAY : LeaveRequestDayType.HALF_DAY)
                                .build());
                        }
                    }

                    // Nếu tổng số ngày nghỉ chưa đủ, thêm nửa ngày vào ngày cuối cùng
                    if (totalWorkDays < leaveRequest.getTotalDayOff()) {
                        processDays.add(TimeKeeping.builder()
                            .employeeId(leaveRequest.getEmployeeId())
                            .date(toDate)
                            .leaveDayType(LeaveRequestDayType.HALF_DAY)
                            .build());
                    }

                    return Mono.just(processDays);
                }));
    }


    public Mono<Boolean> createLeaveDayForRequest(LeaveRequest leaveRequest) {
        // Điều chỉnh thời gian nếu nghỉ nửa ngày
//        if (leaveRequest.getTotalDayOff() != null && leaveRequest.getTotalDayOff() == 0.5f) {
//            leaveRequest.setLeaveRequestDayType(LeaveRequestDayType.HALF_DAY);
//            setHalfDayTimes(leaveRequest);
//        }

        return resolveListLeaveDay(leaveRequest)
            .flatMapMany(Flux::fromIterable)
            .flatMap(leaveDay -> timeKeepingRepository.findByEmployeeIdAndDateAndTimeKeepingType(
                    leaveRequest.getEmployeeId(), leaveDay.getDate(), TimeKeepingType.HOUR)
                .flatMap(existingTimeKeeping -> updateExistingTimeKeeping(existingTimeKeeping, leaveDay, leaveRequest))
                .switchIfEmpty(createNewTimeKeeping(leaveDay, leaveRequest))
                .flatMap(timeKeepingRepository::save)
                .flatMap(timeKeeping -> violationRepository.findByTimeKeepingId(timeKeeping.getId())
                    .flatMap(violationRepository::delete)
                )).then()
            .then(timeKeepingRepository.updateAllTimekeepingTimesheets())
            .thenReturn(true)
            .onErrorReturn(false);

    }

    private TimeKeeping createHalfDayTimeKeeping(LeaveRequest leaveRequest) {
        log.info("***********************************************************");
        log.info("Create half day timekeeping fromTime {} toTime {}", leaveRequest.getFromTime(), leaveRequest.getToTime());
        LocalDate date = leaveRequest.getFromTime().plusHours(7).getHour() < 12 ? leaveRequest.getToDate() : leaveRequest.getToDate().minusDays(1);
        log.info("Create half day timekeeping for employee {} on date {}", leaveRequest.getEmployeeId(), date);
        return TimeKeeping.builder()
            .employeeId(leaveRequest.getEmployeeId())
            .date(date)
            .leaveDayType(LeaveRequestDayType.HALF_DAY)
            .build();
    }


    private void setHalfDayTimes(LeaveRequest leaveRequest) {
        leaveRequest.setFromTime(ZonedDateTime.of(
            leaveRequest.getFromDate(), LocalTime.of(8, 30), ZoneId.of("UTC")));
        leaveRequest.setToTime(ZonedDateTime.of(
            leaveRequest.getFromDate(), LocalTime.of(12, 0), ZoneId.of("UTC")));
    }

    private Mono<TimeKeeping> updateExistingTimeKeeping(TimeKeeping existingTimeKeeping, TimeKeeping leaveDay, LeaveRequest leaveRequest) {
        if (LeaveRequestDayType.HALF_DAY.equals(leaveDay.getLeaveDayType()) && LeaveRequestDayType.HALF_DAY.equals(existingTimeKeeping.getLeaveDayType())) {
            log.error("Already have a half day off record for this day");
            return Mono.error(new BadRequestAlertException("Already have a full day off record for this day", ENTITY_NAME, "fullDayOff"));
        }
        existingTimeKeeping.setIsOverride(false);
        existingTimeKeeping.setIsAbnormal(false);
        existingTimeKeeping.setHoursWorked(0.0f);
        existingTimeKeeping.setIsDayOff(false);
        existingTimeKeeping.setLeaveType(leaveRequest.getLeaveRequestType());
        existingTimeKeeping.setLeaveDayType(leaveDay.getLeaveDayType());
        existingTimeKeeping.setViolationType(null);
        return Mono.just(existingTimeKeeping);
    }

    private Mono<TimeKeeping> createNewTimeKeeping(TimeKeeping leaveDay, LeaveRequest leaveRequest) {
        return Mono.just(TimeKeeping.builder()
            .id(UUID.randomUUID())
            .employeeId(leaveRequest.getEmployeeId())
            .date(leaveDay.getDate())
            .locked(false)
            .hoursWorked(0.0f)
            .createdAt(ZonedDateTime.now())
            .lastUpdatedAt(ZonedDateTime.now())
            .leaveDayType(leaveDay.getLeaveDayType())
            .leaveType(leaveRequest.getLeaveRequestType())
            .timeKeepingType(TimeKeepingType.HOUR)
            .isDayOff(false)
            .isOverride(false)
            .isAbnormal(false)
            .build());
    }


}
