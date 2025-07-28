package com.masi.employee.web.rest;

import com.carevn.masi.dto.ApiResponse;
import com.masi.employee.domain.TimeKeeping;
import com.masi.employee.domain.enumeration.TimeKeepingType;
import com.masi.employee.domain.enumeration.WorkspaceType;
import com.masi.employee.repository.TimeKeepingRepository;
import com.masi.employee.service.TimeKeepingService;
import com.masi.employee.service.dto.EmployeeDTO;
import com.masi.employee.service.dto.LeaveDayReport;
import com.masi.employee.service.dto.TimeKeepingDTO;
import com.masi.employee.service.dto.TimekeepingQuery;
import com.masi.employee.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * REST controller for managing {@link com.masi.employee.domain.TimeKeeping}.
 */
@RestController
@RequestMapping("/api/time-keeping")
public class TimeKeepingResource {

    private final Logger log = LoggerFactory.getLogger(TimeKeepingResource.class);

    private static final String ENTITY_NAME = "masiEmployeeTimeKeeping";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TimeKeepingService timeKeepingService;

    private final TimeKeepingRepository timeKeepingRepository;

    public TimeKeepingResource(TimeKeepingService timeKeepingService, TimeKeepingRepository timeKeepingRepository) {
        this.timeKeepingService = timeKeepingService;
        this.timeKeepingRepository = timeKeepingRepository;
    }

    @GetMapping("/test")
    public Mono<ResponseEntity<Collection<LeaveDayReport>>> test(
        @RequestParam("start_date") LocalDate startDate,
        @RequestParam("end_date") LocalDate endDate
    ) {
        log.debug("REST request to test");
        return timeKeepingService.getLeaveDayReport(startDate, endDate, WorkspaceType.FACTORY)
            .map(result -> ResponseEntity.ok().body(result));
    }


    @GetMapping("/off-tracking/{employeeId}")
    public Mono<ResponseEntity<Collection<TimeKeepingDTO>>> isEmployeeOffInDay(
        @PathVariable("employeeId") UUID employeeId,
        @RequestParam("start_date") LocalDate startDate,
        @RequestParam("end_date") LocalDate endDate
    ) {
        return timeKeepingService.isEmployeeOffInDay(employeeId, startDate, endDate).collectList()
            .map(result -> ResponseEntity.ok().body(result));
    }

    @GetMapping("/leave-tracking/export")
    public Mono<ResponseEntity<ByteArrayResource>> exportLeaveTrackingData
        (@RequestParam("year") int year,
         @RequestParam("workspaceType") WorkspaceType workspaceType
        ) {
        // Generate the Excel file

        LocalDate startDate = LocalDate.of(year, 1, 1);
        LocalDate endDate = LocalDate.of(year, 12, 31);


        Mono<Collection<LeaveDayReport>> leaveDayReports = timeKeepingService.getLeaveDayReport(startDate, endDate, workspaceType);

        return leaveDayReports.flatMap(reports ->
            timeKeepingService.generateLeaveTrackingExcel(Mono.just(reports), year, workspaceType) // Generate the Excel file
                .map(excelBytes -> {
                    ByteArrayResource excelResource = new ByteArrayResource(excelBytes);

                    // Set the response headers
                    HttpHeaders headers = new HttpHeaders();
                    headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=leave_tracking_" + year + ".xlsx");
                    headers.add(HttpHeaders.CONTENT_TYPE, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

                    return ResponseEntity.ok()
                        .headers(headers)
                        .contentLength(excelResource.contentLength())
                        .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                        .body(excelResource);
                })
                .onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ByteArrayResource("Error generating file".getBytes()))))
        );
    }

    @PostMapping("")
    public Mono<ResponseEntity<Object>> createTimeKeeping(@Valid @RequestBody TimeKeepingDTO timeKeepingDTO)
        throws URISyntaxException {
        log.debug("REST request to save TimeKeeping : {}", timeKeepingDTO);
        if (timeKeepingDTO.getId() != null) {
            throw new BadRequestAlertException("A new timeKeeping cannot already have an ID", ENTITY_NAME, "idexists");
        }
        timeKeepingDTO.setId(UUID.randomUUID());
        return timeKeepingService
            .createNew(timeKeepingDTO)
            .handle((result, sink) -> {
                try {
                    sink.next(ResponseEntity.created(new URI("/api/time-keepings/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result));
                } catch (URISyntaxException e) {
                    sink.error(new RuntimeException(e));
                }
            });
    }

    @PatchMapping(value = "/{id}", consumes = {"application/json", "application/merge-patch+json"})
    public Mono<ResponseEntity<TimeKeepingDTO>> partialUpdateTimeKeeping(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody TimeKeepingDTO timeKeepingDTO,
        Authentication authentication
    ) throws URISyntaxException {
        log.debug("REST request to partial update TimeKeeping partially : {}, {}", id, timeKeepingDTO);
        if (timeKeepingDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, timeKeepingDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return timeKeepingRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<TimeKeepingDTO> result = timeKeepingService.partialUpdate(timeKeepingDTO, authentication);

                return result
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(
                        res ->
                            ResponseEntity.ok()
                                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, res.getId().toString()))
                                .body(res)
                    );
            });
    }

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ApiResponse<TimeKeepingDTO>> getAllTimeKeepings(@ParameterObject Pageable pageable) {
        log.debug("REST request to get all TimeKeepings");
        return timeKeepingService.findAndCount(pageable);
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<TimeKeepingDTO>> getTimeKeeping(@PathVariable("id") UUID id) {
        log.debug("REST request to get TimeKeeping : {}", id);
        Mono<TimeKeepingDTO> timeKeepingDTO = timeKeepingService.findOne(id);
        return ResponseUtil.wrapOrNotFound(timeKeepingDTO);
    }

    @GetMapping("/group-by-employee")
    public Mono<ApiResponse<EmployeeDTO>> getAllTimeKeepingsGroupByEmployee(
        @ParameterObject Pageable pageable,
        @RequestParam("start_date") LocalDate startDate,
        @RequestParam("end_date") LocalDate endDate,
        TimekeepingQuery timekeepingQuery

    ) {
        timekeepingQuery.setStartDate(startDate);
        timekeepingQuery.setEndDate(endDate);
        timekeepingQuery.setPageable(pageable);
        return timeKeepingService.findAndMapToEmployeeByDateBetween(timekeepingQuery);
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteTimeKeeping(@PathVariable("id") UUID id) {
        log.debug("REST request to delete TimeKeeping : {}", id);
        return timeKeepingService
            .delete(id)
            .then(
                Mono.just(
                    ResponseEntity.noContent()
                        .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                        .build()
                )
            );
    }

    @GetMapping("/employee/{employee_id}/{date}")
    public Mono<TimeKeepingDTO> getAllTimeKeepingsByEmployeeAndDate(
        @PathVariable("employee_id") UUID employeeId,
        @PathVariable("date") LocalDate date,
        @RequestParam(value = "type", defaultValue = "HOUR") TimeKeepingType type
    ) {
        log.debug("REST request to get all TimeKeepings by employee and date");
        return timeKeepingService.findAllByEmployeeAndDate(employeeId, date, type);
    }

    @GetMapping("/employee/{employee_id}/{start_date}/{end_date}")
    public Mono<ApiResponse<TimeKeepingDTO>> getAllTimeKeepingsByEmployeeAndDateBetween(
        @PathVariable("employee_id") UUID employeeId,
        @PathVariable("start_date") LocalDate startDate,
        @PathVariable("end_date") LocalDate endDate,
        @ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get all TimeKeepings by employee and date between");
        return timeKeepingService.findAllByEmployeeAndDateBetween(employeeId, startDate, endDate, pageable);
    }

    @GetMapping("/range-date/{start_date}/{end_date}")
    public Mono<ApiResponse<TimeKeepingDTO>> getAllTimeKeepingsByDateBetween(
        @PathVariable("start_date") LocalDate startDate,
        @PathVariable("end_date") LocalDate endDate,
        TimekeepingQuery timekeepingQuery,
        @ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get all TimeKeepings by date between");
        return timeKeepingService.findAllByDateBetween(startDate, endDate, pageable, timekeepingQuery.getType(), timekeepingQuery.getWorkSpaceTypes());
    }

    @GetMapping("/date/{date}")
    public Mono<ApiResponse<TimeKeepingDTO>> getAllTimeKeepingsByDate(
        @PathVariable("date") LocalDate date,
        @ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get all TimeKeepings by date");
        return timeKeepingService.findAllByDate(date, pageable);
    }

//    @GetMapping("/test_lock")
//    public Mono<ResponseEntity<Void>> lockTimeKeeping() {
//        log.debug("REST request to test lock time-keeping at the end of the week");
//        return timeKeepingService.lockTimeKeeping()
//            .then(
//                Mono.just(
//                    ResponseEntity.noContent()
//                        .build()
//                )
//            );
//    }

    @GetMapping("/export/{startDate}/{endDate}")
    public Mono<ResponseEntity<byte[]>> exportTimeKeeping(@PathVariable LocalDate startDate, @PathVariable LocalDate endDate,
                                                          @RequestParam(value = "type", defaultValue = "HOUR") TimeKeepingType type, @ParameterObject Pageable pageable) {
        return timeKeepingService.exportRecordsAsCSV(startDate, endDate, pageable, type)
            .map(csvBytes -> ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"time-keeping" + ".csv\"")
                .contentType(org.springframework.http.MediaType.APPLICATION_OCTET_STREAM)
                .body(csvBytes));
    }
}
