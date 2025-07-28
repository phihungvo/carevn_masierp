package com.masi.employee.service;

import com.carevn.masi.dto.ApiResponse;
import com.masi.employee.constants.EntityNameConstants;
import com.masi.employee.constants.ResponseMessageConstants;
import com.masi.employee.domain.TimeKeeping;
import com.masi.employee.domain.TimeKeepingRecord;
import com.masi.employee.domain.enumeration.TimeKeepingType;
import com.masi.employee.repository.EmployeeRepository;
import com.masi.employee.repository.EmployeeShiftDetailRepository;
import com.masi.employee.repository.TimeKeepingRecordRepository;
import com.masi.employee.repository.TimeKeepingRepository;
import com.masi.employee.service.dto.TimeKeepingDTO;
import com.masi.employee.service.dto.TimeKeepingRecordDTO;
import com.masi.employee.service.mapper.TimeKeepingRecordMapper;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.masi.employee.web.rest.errors.BadRequestAlertException;
import com.opencsv.CSVWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.masi.employee.domain.TimeKeepingRecord}.
 */
@Service
@Transactional
public class TimeKeepingRecordService {

    private final Logger log = LoggerFactory.getLogger(TimeKeepingRecordService.class);

    private final TimeKeepingRecordRepository timeKeepingRecordRepository;

    private final TimeKeepingRecordMapper timeKeepingRecordMapper;

    private final EmployeeRepository employeeRepository;

    private final TimeKeepingService timeKeepingService;
    private final TimeKeepingRepository timeKeepingRepository;
    private final EmployeeShiftDetailRepository employeeShiftDetailRepository;


    public TimeKeepingRecordService(
        TimeKeepingRecordRepository timeKeepingRecordRepository,
        TimeKeepingRecordMapper timeKeepingRecordMapper,
        EmployeeRepository employeeRepository,
        TimeKeepingService timeKeepingService,
        TimeKeepingRepository timeKeepingRepository, EmployeeShiftDetailRepository employeeShiftDetailRepository) {
        this.timeKeepingRecordRepository = timeKeepingRecordRepository;
        this.timeKeepingRecordMapper = timeKeepingRecordMapper;
        this.employeeRepository = employeeRepository;
        this.timeKeepingService = timeKeepingService;
        this.timeKeepingRepository = timeKeepingRepository;
        this.employeeShiftDetailRepository = employeeShiftDetailRepository;
    }

    public Mono<Integer> handleDriverCheckIn(TimeKeepingRecordDTO dto) {
        //todo: find last record of this employee
        // tìm ngày hoôm qua nếu hôm qua chưa checkout thì done hôm qua
        // nếu hôm qua đã checkout thì tạo mới
        LocalDate now = dto.getCheckIn().withZoneSameInstant(ZoneId.of("Asia/Ho_Chi_Minh")).toLocalDate();
        LocalDate yesterday = now.minusDays(1);
        return timeKeepingRepository.findByEmployeeIdAndDate(dto.getEmployeeId(), dto.getTimeKeepingType(), yesterday)
            .flatMap(timeKeeping -> {
                if (timeKeeping.getLastCheckIn() == null) {
                    // set at midnight
                    timeKeeping.setLastCheckIn(now.atStartOfDay(ZoneId.of("Asia/Ho_Chi_Minh")).minusMinutes(1).withZoneSameInstant(ZoneId.of("UTC")));
                    timeKeeping.calHoursWorked();
                    return timeKeepingRepository.save(timeKeeping).thenReturn(true);
                } else {
                    // đã checkout hôm qua
                    return Mono.just(false);
                }
            }).flatMap(res -> {
                if (Boolean.TRUE.equals(res)) {
                    return this.startDriverCheckInAtNight(dto).then(Mono.just(1));
                } else {
                    return Mono.just(-1);
                }
            }).switchIfEmpty(Mono.just(-1));
    }

    // nếu hôm qua chưa checkout thì done hôm qua rồi sài hàm này (tạo mới 1 record checkin lúc 0h và checkout lúc now)
    public Mono<Boolean> startDriverCheckInAtNight(TimeKeepingRecordDTO dto) {
        LocalDate now = dto.getCheckIn().withZoneSameInstant(ZoneId.of("Asia/Ho_Chi_Minh")).toLocalDate();
        return timeKeepingRepository.findByEmployeeIdAndDate(dto.getEmployeeId(), dto.getTimeKeepingType(), now)
            .hasElement().flatMap(hasElement -> {
                if (Boolean.TRUE.equals(hasElement)) {
                    return Mono.error(new BadRequestAlertException("Entity is existed", "masiEmployeeTimeKeeping", "entityexisted"));
                } else {
                    TimeKeeping timeKeeping = new TimeKeeping();
                    timeKeeping.setEmployeeId(dto.getEmployeeId());
                    timeKeeping.setId(UUID.randomUUID());
                    timeKeeping.setDate(now);
                    timeKeeping.setFirstCheckIn(now.atStartOfDay(ZoneId.of("Asia/Ho_Chi_Minh")).withZoneSameInstant(ZoneId.of("UTC")));
                    timeKeeping.setLastCheckIn(dto.getCheckIn());
                    timeKeeping.setTimeKeepingType(dto.getTimeKeepingType());
                    timeKeeping.setLocked(false);
                    timeKeeping.setCreatedAt(dto.getCheckIn());
                    timeKeeping.calHoursWorked();
                    timeKeeping.setLastUpdatedAt(dto.getCheckIn());
                    return timeKeepingRepository.save(timeKeeping).thenReturn(true);
                }
            });
    }

    public Mono<TimeKeepingRecordDTO> save(TimeKeepingRecordDTO timeKeepingRecordDTO) {
        log.debug("Request to save TimeKeepingRecord : {}", timeKeepingRecordDTO);
        Mono<Integer> result = Mono.just(-1);
        if (timeKeepingRecordDTO.getTimeKeepingType() == TimeKeepingType.DRIVER) {
            result = this.handleDriverCheckIn(timeKeepingRecordDTO);
        }
        return result.flatMap(flag -> {
            if (flag == -1) return employeeRepository.existsById(timeKeepingRecordDTO.getEmployeeId())
                .flatMap(exists -> {
                    if (exists) {
                        LocalDate currentDate = timeKeepingRecordDTO.getCheckIn().toLocalDate();
                        return timeKeepingRepository.findByEmployeeIdAndDateAndTimeKeepingType(timeKeepingRecordDTO.getEmployeeId(), currentDate, timeKeepingRecordDTO.getTimeKeepingType())
                            .hasElement().flatMap(hasElement -> {
                                if (Boolean.TRUE.equals(hasElement)) {
                                    return timeKeepingRepository.findByEmployeeIdAndDateAndTimeKeepingType(timeKeepingRecordDTO.getEmployeeId(), currentDate, timeKeepingRecordDTO.getTimeKeepingType())
                                        .flatMap(timeKeeping -> {
                                            if (timeKeeping.getLocked() != null && timeKeeping.getLocked()) {
                                                return Mono.error(new BadRequestAlertException("Entity is locked, and there are no update permissions.", "masiEmployeeTimeKeeping", "entitylocked"));
                                            } else {
                                                return timeKeepingRecordRepository.save(timeKeepingRecordDTO.toEntity())
                                                    .flatMap(t -> timeKeepingService.updateWithNewRecord(t, timeKeepingRecordDTO.getTimeKeepingType()))
                                                    .map(TimeKeepingRecord::toDto);
                                            }
                                        });
                                } else {
                                    return timeKeepingRecordRepository.save(timeKeepingRecordDTO.toEntity())
                                        .flatMap(t -> timeKeepingService.updateWithNewRecord(t, timeKeepingRecordDTO.getTimeKeepingType()))
                                        .map(TimeKeepingRecord::toDto);
                                }
                            });
                    } else {
                        return Mono.error(new BadRequestAlertException("Invalid employee ID", "employee", "idnotfound"));
                    }
                });
            else return Mono.just(timeKeepingRecordDTO);
        });
    }

    /**
     * Update a timeKeepingRecord.
     *
     * @param timeKeepingRecordDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<TimeKeepingRecordDTO> update(TimeKeepingRecordDTO timeKeepingRecordDTO) {
        log.debug("Request to update TimeKeepingRecord : {}", timeKeepingRecordDTO);
        return timeKeepingRecordRepository
            .save(timeKeepingRecordDTO.toEntity().setIsPersisted())
            .map(TimeKeepingRecord::toDto);
    }

    /**
     * Partially update a timeKeepingRecord.
     *
     * @param timeKeepingRecordDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<TimeKeepingRecordDTO> partialUpdate(UUID id, TimeKeepingRecordDTO timeKeepingRecordDTO) {
        log.debug("Request to partially update TimeKeepingRecord : {}", timeKeepingRecordDTO);

        return timeKeepingRecordRepository
            .findById(id)
            .map(existingTimeKeepingRecord -> {
                timeKeepingRecordMapper.partialUpdate(existingTimeKeepingRecord, timeKeepingRecordDTO);
                existingTimeKeepingRecord.setIsPersisted();
                return existingTimeKeepingRecord;
            })
            .flatMap(timeKeepingRecordRepository::save)
            .map(TimeKeepingRecord::toDto);
    }


    /**
     * Get all the timeKeepingRecords.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<TimeKeepingRecordDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TimeKeepingRecords");
        return timeKeepingRecordRepository.findAllBy(pageable).map(timeKeepingRecordMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Mono<ApiResponse<TimeKeepingRecordDTO>> findAndCount(Pageable pageable) {
        log.debug("Request to get all TimeKeepingRecords");

        Mono<List<TimeKeepingRecordDTO>> timeKeepingRecords = timeKeepingRecordRepository.findAllBy(pageable)
            .map(TimeKeepingRecord::toDto)
            .collectList();

        Mono<Long> count = timeKeepingRecordRepository.count();

        return Mono.zip(timeKeepingRecords, count).map(tuple -> new ApiResponse<TimeKeepingRecordDTO>(tuple.getT1(), tuple.getT2()));

    }

    /**
     * Returns the number of timeKeepingRecords available.
     *
     * @return the number of entities in the database.
     */
    public Mono<Long> countAll() {
        return timeKeepingRecordRepository.count();
    }

    /**
     * Get one timeKeepingRecord by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<TimeKeepingRecordDTO> findOne(UUID id) {
        log.debug("Request to get TimeKeepingRecord : {}", id);
        return timeKeepingRecordRepository.findById(id).map(TimeKeepingRecord::toDto);
    }

    /**
     * Delete the timeKeepingRecord by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(UUID id) {
        log.debug("Request to delete TimeKeepingRecord : {}", id);
        return timeKeepingRecordRepository.findById(id)
            .switchIfEmpty(Mono.error(new BadRequestAlertException("Record not found", EntityNameConstants.TIMEKEEPING_RECORD, ResponseMessageConstants.TIMEKEEPING_RECORD_NOT_FOUND)))
            .flatMap(timeKeepingRecord -> timeKeepingRecordRepository.deleteById(id))
            .onErrorResume(e -> {
                log.error("Failed to delete TimeKeepingRecord {}: {}", id, e.getMessage());
                return Mono.error(e);
            });
    }

    @Transactional(readOnly = true)
    public Mono<ApiResponse<TimeKeepingRecordDTO>> findByEmployeeAndDateAsDto(UUID employeeId, LocalDate date, Pageable pageable) {
        log.debug("Request to get TimeKeepingRecord by employeeId {} and date {}", employeeId, date);
        Mono<List<TimeKeepingRecordDTO>> timeKeepingRecords = timeKeepingRecordRepository.findByEmployeeAndDate(employeeId, date, pageable)
            .map(timeKeepingRecordMapper::toDto)
            .collectList();

        Flux<TimeKeepingRecord> dataNoPagination = timeKeepingRecordRepository.findByEmployeeAndDateNoPagination(employeeId, date);
        Mono<Long> count = dataNoPagination.collectList().flatMap(list -> {
            Long cnt = (long) list.size();
            return Mono.just(cnt);
        });
        return Mono.zip(timeKeepingRecords, count)
            .map(tuple -> new ApiResponse<TimeKeepingRecordDTO>(tuple.getT1(), tuple.getT2()));
    }

    @Transactional(readOnly = true)
    public Flux<TimeKeepingRecord> findByEmployeeAndDate(UUID employeeId, LocalDate date, Pageable pageable) {
        log.debug("Request to get TimeKeepingRecord by employeeId {} and date {}", employeeId, date);
        return timeKeepingRecordRepository.findByEmployeeAndDate(employeeId, date, pageable);
    }

    public Flux<TimeKeepingRecordDTO> findAllByRangeDate(LocalDate startDate, LocalDate endDate) {
        return timeKeepingRecordRepository.findAllByRangeDate(startDate, endDate, null)
            .map(e -> {
                TimeKeepingRecordDTO dto = e.toDto();
                if (e.getEmployee() != null) {
                    dto.setEmployee(e.getEmployee().toDto());
                    dto.getEmployee().setCode(e.getEmployee().getEmployeeProfile().getEmployeeCode());

                }
                return dto;
            })
            .collectList()
            .zipWith(timeKeepingService.findAllWhereNotHaveRecord(startDate, endDate).collectList())
            .flatMap(tuple -> {
                List<TimeKeepingRecordDTO> listRecord = new ArrayList<TimeKeepingRecordDTO>(tuple.getT1());
                for (var timeKeeping : tuple.getT2()) {
                    TimeKeepingRecordDTO emptyRecord = new TimeKeepingRecordDTO();
                    emptyRecord.setEmployee(timeKeeping.getEmployee());
                    emptyRecord.setCheckIn(timeKeeping.getDate().atStartOfDay(ZoneId.of("Asia/Ho_Chi_Minh")));
                    // emptyRecord.setCheckIn(timeKeeping.getZonedDate());
                    emptyRecord.setId(UUID.randomUUID());
                    listRecord.add(emptyRecord);
                }
                return Mono.just(listRecord);
            }).flatMapMany(Flux::fromIterable);
    }


    public Mono<byte[]> exportRecordsAsCSV(LocalDate startDate, LocalDate endDate, Pageable pageable) {
        return
            this.findAllByRangeDate(startDate, endDate)
                .collectList()
                .map(this::convertListToCSV);
    }

    public Mono<byte[]> exportRecordsAsCSVV2(LocalDate startDate, LocalDate endDate, Pageable pageable) {
        var sort = Sort.by(Sort.Order.desc("date"), Sort.Order.desc("last_check_in"), Sort.Order.desc("first_check_in"));
        var pageableWithSort = pageable = PageRequest.of(0, Integer.MAX_VALUE, sort);
        return this.timeKeepingService.findAllByDateBetween(startDate, endDate, pageableWithSort, TimeKeepingType.HOUR, null)
            .flatMap(e -> {
                var data = e.getData();
                var listIds = data.stream().map(TimeKeepingDTO::getId).toList();
                return employeeShiftDetailRepository.findAllByTimeKeepingIdAndGroupBy(listIds)
                    .collectList()
                    .map(shiftDetails -> {
                        for (var timeKeeping : data) {
                            var shiftDetail = shiftDetails.stream().filter(s -> s.getTimeKeepingId().equals(timeKeeping.getId())).findFirst();
                            shiftDetail.ifPresent(employeeShiftDetail -> timeKeeping.setTotalCompletionPercent(employeeShiftDetail.getCompletionPercent()));
                        }
                        return data;
                    });
            })
            .map(this::convertListToCSVV2);
    }

    private byte[] convertListToCSVV2(List<TimeKeepingDTO> records) {
        StringWriter stringWriter = new StringWriter();
        CSVWriter csvWriter = new CSVWriter(stringWriter);


        csvWriter.writeNext(new String[]{"Mã nhân viên", "Họ và tên", "Thực tế công", "Tổng thời gian chấm công", "Giờ vào", "Giờ ra", "Ngày", "Trạng thái"});
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        DateTimeFormatter dateOnly = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        // Writing data
        records.forEach(r -> {
            if (r.getFirstCheckIn() != null && r.getLastCheckIn() != null) {
                r.setHoursWorked((float) r.getFirstCheckIn().until(r.getLastCheckIn(), java.time.temporal.ChronoUnit.MINUTES) / 60f);
            }else {
                r.setHoursWorked(0f);
            }
            boolean isHoursWorkedFloat = r.getHoursWorked() != null && (r.getHoursWorked() - r.getHoursWorked().intValue()) != 0;
            boolean isShiftTimeFloat = r.getTotalCompletionPercent() != null && (r.getTotalCompletionPercent() - r.getTotalCompletionPercent().intValue()) != 0;
            var checkinStr = r.getFirstCheckIn() == null ? "" : r.getFirstCheckIn().plusHours(7).format(formatter);
            var checkoutStr = r.getLastCheckIn() == null ? "" : r.getLastCheckIn().plusHours(7).format(formatter);
            csvWriter.writeNext(new String[]{
                String.valueOf(r.getEmployee().getEmployeeProfile() != null ? r.getEmployee().getEmployeeProfile().getEmployeeCode() : ""),
                r.getEmployee().getEmployeeProfile() != null ? r.getEmployee().getEmployeeProfile().getFullName() : "",
                r.getTotalCompletionPercent() != null ? (isShiftTimeFloat ? String.format("%.2f", r.getTotalCompletionPercent()) : String.valueOf(r.getTotalCompletionPercent().intValue())) : "",
                r.getHoursWorked() != null ? (isHoursWorkedFloat ? String.format("%.2f", r.getHoursWorked()) : String.valueOf(r.getHoursWorked().intValue())) : "",
                checkinStr,
                checkoutStr,
                r.getDate().format(dateOnly),
                Boolean.TRUE.equals(r.getLocked()) ? "Đã khóa" : r.getHoursWorked() != null && r.getHoursWorked() != 0 ? "Đã chấm công" : ""
            });
        });

        try {
            csvWriter.close();
        } catch (IOException e) {
            // Handle potential exception here
        }

        return stringWriter.toString().getBytes(StandardCharsets.UTF_8);
    }

    private byte[] convertListToCSV(List<TimeKeepingRecordDTO> records) {
        StringWriter stringWriter = new StringWriter();
        CSVWriter csvWriter = new CSVWriter(stringWriter);

        // Writing header
        csvWriter.writeNext(new String[]{"Mã nhân viên", "Họ và tên", "Chi tiết giờ vào/ra"});
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        DateTimeFormatter dateOnly = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        // Writing data
        records.forEach(r -> {
            String checkInStr = "";
            if (r.getCheckIn() != null) {
                // if at start of date then only show date
                if (r.getCheckIn().getHour() == 0 && r.getCheckIn().getMinute() == 0 && r.getCheckIn().getSecond() == 0) {
                    checkInStr = r.getCheckIn().format(dateOnly);
                } else {
                    checkInStr = r.getCheckIn().format(formatter);
                }
            }
            csvWriter.writeNext(new String[]{

                String.valueOf(r.getEmployee().getCode()),
                r.getEmployee().getLastName() + " " + r.getEmployee().getFirstName(),
                checkInStr
            });
        });

        try {
            csvWriter.close();
        } catch (IOException e) {
            // Handle potential exception here
        }

        return stringWriter.toString().getBytes(StandardCharsets.UTF_8);
    }
}
