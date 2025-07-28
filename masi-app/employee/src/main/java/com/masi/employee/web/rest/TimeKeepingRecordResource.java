package com.masi.employee.web.rest;

import com.carevn.masi.dto.ApiResponse;
import com.carevn.masi.utils.Utilities;
import com.masi.employee.constants.ResponseMessageConstants;
import com.masi.employee.domain.TimeKeepingRecord;
import com.masi.employee.repository.TimeKeepingRecordRepository;
import com.masi.employee.service.TimeKeepingRecordService;
import com.masi.employee.service.dto.TimeKeepingDTO;
import com.masi.employee.service.dto.TimeKeepingRecordDTO;
import com.masi.employee.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing
 * {@link com.masi.employee.domain.TimeKeepingRecord}.
 */
@RestController
@RequestMapping("/api/time-keeping-record")
public class TimeKeepingRecordResource {

    private final Logger log = LoggerFactory.getLogger(TimeKeepingRecordResource.class);

    public static final String ENTITY_NAME = "masiEmployeeTimeKeepingRecord";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TimeKeepingRecordService timeKeepingRecordService;

    private final TimeKeepingRecordRepository timeKeepingRecordRepository;

    public TimeKeepingRecordResource(
        TimeKeepingRecordService timeKeepingRecordService,
        TimeKeepingRecordRepository timeKeepingRecordRepository) {
        this.timeKeepingRecordService = timeKeepingRecordService;
        this.timeKeepingRecordRepository = timeKeepingRecordRepository;
    }

    @PostMapping("")
    public Mono<ResponseEntity<TimeKeepingRecordDTO>> createTimeKeepingRecord(
        @Valid @RequestBody TimeKeepingRecordDTO timeKeepingRecordDTO) throws URISyntaxException {
        log.debug("REST request to save TimeKeepingRecord : {}", timeKeepingRecordDTO);
        if (timeKeepingRecordDTO.getId() != null) {
            throw new BadRequestAlertException("A new timeKeepingRecord cannot already have an ID", ENTITY_NAME,
                "idexists");
        }
        timeKeepingRecordDTO.setId(UUID.randomUUID());
        return timeKeepingRecordService
            .save(timeKeepingRecordDTO)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/time-keeping-records/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME,
                            result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            }).onErrorMap(BadRequestAlertException.class, e -> {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
            });
    }

    @PutMapping(value = "/{id}", consumes = {"application/json", "application/merge-patch+json"})
    public Mono<ResponseEntity<TimeKeepingRecordDTO>> partialUpdateTimeKeepingRecord(
        @PathVariable(value = "id") final UUID id,
        @NotNull @RequestBody TimeKeepingRecordDTO timeKeepingRecordDTO) throws URISyntaxException {
        log.debug("REST request to partial update TimeKeepingRecord partially : {}, {}", id, timeKeepingRecordDTO);
        return timeKeepingRecordRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME,
                        ResponseMessageConstants.TIMEKEEPING_RECORD_NOT_FOUND));
                }

                Mono<TimeKeepingRecordDTO> result = timeKeepingRecordService.partialUpdate(id,
                    timeKeepingRecordDTO);

                return result
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(
                        res -> ResponseEntity.ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true,
                                ENTITY_NAME, res.getId().toString()))
                            .body(res));
            });
    }

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ApiResponse<TimeKeepingRecordDTO>> getAllTimeKeepingRecords(@ParameterObject Pageable pageable) {
        log.debug("REST request to get all TimeKeepingRecords");
        return timeKeepingRecordService.findAndCount(pageable);
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<TimeKeepingRecordDTO>> getTimeKeepingRecord(@PathVariable("id") UUID id) {
        log.debug("REST request to get TimeKeepingRecord : {}", id);
        Mono<TimeKeepingRecordDTO> timeKeepingRecordDTO = timeKeepingRecordService.findOne(id);
        return ResponseUtil.wrapOrNotFound(timeKeepingRecordDTO);
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Map<String, Object>>> deleteTimeKeepingRecord(@PathVariable("id") UUID id) {
        log.debug("REST request to delete TimeKeepingRecord : {}", id);
        return timeKeepingRecordService
            .delete(id)
            .then(
                Mono.just(
                    ResponseEntity.ok()
                        .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true,
                            ENTITY_NAME, id.toString()))
                        .body(Utilities.generateResponse(
                            ResponseMessageConstants.TIMEKEEPING_RECORD_DELETED_SUCCESS, null))));
    }

    @GetMapping("/{employeeId}/{date}")
    public Mono<ApiResponse<TimeKeepingRecordDTO>> getTimeKeepingRecordByEmployeeAndDate(
        @ParameterObject Pageable pageable,
        @PathVariable("employeeId") UUID employeeId,
        @PathVariable("date") LocalDate date) {
        log.debug("REST request to get TimeKeepingRecord by employeeId {} on {}", employeeId, date);
        return timeKeepingRecordService.findByEmployeeAndDateAsDto(employeeId, date, pageable);
    }

    @GetMapping("/export/v1/{startDate}/{endDate}")
    public Mono<ResponseEntity<byte[]>> exportTimeKeepingRecords(@PathVariable LocalDate startDate,
                                                                 @PathVariable LocalDate endDate, @ParameterObject Pageable pageable) {
        return timeKeepingRecordService.exportRecordsAsCSV(startDate, endDate, pageable)
            .map(csvBytes -> ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"time-keeping" + ".csv\"")
                .contentType(org.springframework.http.MediaType.APPLICATION_OCTET_STREAM)
                .body(csvBytes));
    }
    @GetMapping("/export/{startDate}/{endDate}")
    public Mono<ResponseEntity<byte[]>> exportTimeKeepingRecordsV2(@PathVariable LocalDate startDate,
                                                                   @PathVariable LocalDate endDate, @ParameterObject Pageable pageable) {
        return timeKeepingRecordService.exportRecordsAsCSVV2(startDate, endDate, pageable)
            .map(csvBytes -> ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"time-keeping" + ".csv\"")
                .contentType(org.springframework.http.MediaType.APPLICATION_OCTET_STREAM)
                .body(csvBytes));
    }

}

