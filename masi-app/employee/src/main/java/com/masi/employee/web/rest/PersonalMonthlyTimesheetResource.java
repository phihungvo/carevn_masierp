package com.masi.employee.web.rest;

import com.carevn.masi.dto.ApiResponse;
import com.carevn.masi.utils.SecurityUtils;
import com.carevn.masi.utils.Utilities;
import com.masi.employee.domain.EmployeeShiftDetail;
import com.masi.employee.domain.enumeration.WorkspaceType;
import com.masi.employee.repository.PersonalMonthlyTimesheetRepository;
import com.masi.employee.service.EmployeeShiftDetailService;
import com.masi.employee.service.PersonalMonthlyTimesheetService;
import com.masi.employee.service.TimesheetExportService;
import com.masi.employee.service.dto.*;
import com.masi.employee.service.web.client.FileClient;
import jakarta.validation.Valid;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * REST controller for managing
 * {@link com.masi.employee.domain.PersonalMonthlyTimesheet}.
 */
@RestController
@RequestMapping("/api/personal-monthly-timesheets")
public class PersonalMonthlyTimesheetResource {

    private final Logger log = LoggerFactory.getLogger(PersonalMonthlyTimesheetResource.class);

    private static final String ENTITY_NAME = "masiEmployeePersonalMonthlyTimesheet";
    private final EmployeeShiftDetailService employeeShiftDetailService;

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PersonalMonthlyTimesheetService personalMonthlyTimesheetService;
    private final TimesheetExportService timesheetExportService;
    private final FileClient fileClient;

    public PersonalMonthlyTimesheetResource(PersonalMonthlyTimesheetService personalMonthlyTimesheetService,
                                            TimesheetExportService timesheetExportService, FileClient fileClient, EmployeeShiftDetailService employeeShiftDetailService) {
        this.personalMonthlyTimesheetService = personalMonthlyTimesheetService;
        this.timesheetExportService = timesheetExportService;
        this.fileClient = fileClient;
        this.employeeShiftDetailService = employeeShiftDetailService;
    }

    @GetMapping("export-timesheet")
    public Mono<ResponseEntity<InputStreamResource>> exportTimesheet(
        @ParameterObject PersonalMonthlyTimesheetQuery query,
        ServerHttpRequest request) {
        return timesheetExportService.exportTimesheet(query).flatMap(
            fileName -> {
                try {
                    FileInputStream fileInputStream = new FileInputStream(fileName);
                    InputStreamResource inputStreamResource = new InputStreamResource(fileInputStream);
                    HttpHeaders headers = new HttpHeaders();
                    headers.add(HttpHeaders.CONTENT_DISPOSITION,
                        String.format("attachment; filename=\"%s-%d.xlsx\"", "BangChamCongThang",
                            query.getMonth().getMonth().getValue()));
                    return Mono.just(new ResponseEntity<>(inputStreamResource, headers, HttpStatus.OK));
                } catch (FileNotFoundException e) {
                    return Mono.error(new RuntimeException(e));
                }
            });

    }

    private UUID tryParseUUID(String id) {
        try {
            return UUID.fromString(id);
        } catch (Exception e) {
            return UUID.randomUUID();
        }
    }

    /**
     * {@code GET  /personal-monthly-timesheets} : get all the
     * personalMonthlyTimesheets.
     *
     * @param pageable the pagination information.
     * @param request  a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
     * of personalMonthlyTimesheets in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<ApiResponse<PersonalMonthlyTimesheetDTO>>> getAllPersonalMonthlyTimesheets(
            @org.springdoc.core.annotations.ParameterObject Pageable pageable,
            PersonalMonthlyTimesheetQuery query,
            ServerHttpRequest request) {
        log.debug("REST request to get a page of PersonalMonthlyTimesheets");
        return SecurityUtils.getUserJWTDetail().flatMap(u -> {
            query.setCompany(u.getCompanyId());
            return personalMonthlyTimesheetService
                    .countByQuery(query)
                    .zipWith(personalMonthlyTimesheetService.findAllByQuery(query, pageable).collectList()
                            .flatMap(list -> {
                                var mapFileId = new HashMap<UUID, MonthlyTimeSheetReviewDTO>();
                                for (PersonalMonthlyTimesheetDTO personalMonthlyTimesheetDTO : list) {
                                    if (personalMonthlyTimesheetDTO.getReview() != null) {
                                        mapFileId.put(this.tryParseUUID(personalMonthlyTimesheetDTO.getReview().getSignatureFile()), personalMonthlyTimesheetDTO.getReview());
                                    }
                                }
                                var keys = mapFileId.keySet();
                                System.out.println("lt " + keys.size());
                                return fileClient.getFileAttachmentsByListIds(keys)
                                        .collectList()
                                        .map(files -> {
                                            for (var file : files) {
                                                mapFileId.get(file.getId()).setSignature(file);
                                            }
                                            return list;
                                        }).then(Mono.just(list));
                            })
                    )
                    .map(countWithEntities -> ResponseEntity.ok()
                            .body(new ApiResponse<>(countWithEntities.getT2(), countWithEntities.getT1())));
        });
    }



    /**
     * {@code GET  /personal-monthly-timesheets/:id} : get the "id"
     * personalMonthlyTimesheet.
     *
     * @param id the id of the personalMonthlyTimesheetDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     * the personalMonthlyTimesheetDTO, or with status
     * {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<PersonalMonthlyTimesheetDTO>> getPersonalMonthlyTimesheet(@PathVariable("id") UUID id) {
        log.debug("REST request to get PersonalMonthlyTimesheet : {}", id);
        Mono<PersonalMonthlyTimesheetDTO> personalMonthlyTimesheetDTO = personalMonthlyTimesheetService.findOne(id).flatMap(
            personalMonthlyTimesheet -> {
                if (personalMonthlyTimesheet.getReview() != null) {
                    return fileClient.getFileAttachment(tryParseUUID(personalMonthlyTimesheet.getReview().getSignatureFile()))
                        .map(file -> {
                            personalMonthlyTimesheet.getReview().setSignature(file);
                            return personalMonthlyTimesheet;
                        }).then(Mono.just(personalMonthlyTimesheet));
                }
                return Mono.just(personalMonthlyTimesheet);
            });

        return ResponseUtil.wrapOrNotFound(personalMonthlyTimesheetDTO);
    }

    /**
     * {@code PATCH  /personal-monthly-timesheets/:id/approve} : approve the "id"
     * personalMonthlyTimesheet.
     *
     * @param id the id of the personalMonthlyTimesheetDTO to approve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     * the personalMonthlyTimesheetDTO, or with status
     * {@code 404 (Not Found)}.
     */
    @PatchMapping(value = "/{id}/approve")
    public Mono<ResponseEntity<PersonalMonthlyTimesheetDTO>> approvePersonalMonthlyTimesheet(
        @PathVariable("id") UUID id) {
        log.debug("REST request to approve PersonalMonthlyTimesheet : {}", id);
        return personalMonthlyTimesheetService
            .approve(id)
            .map(
                res -> ResponseEntity.ok()
                    .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME,
                        res.getId().toString()))
                    .body(res));
    }

    /**
     * {@code PATCH  /personal-monthly-timesheets/:id/reject} : reject the "id"
     * personalMonthlyTimesheet.
     *
     * @param id the id of the personalMonthlyTimesheetDTO to reject.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     * the personalMonthlyTimesheetDTO, or with status
     * {@code 404 (Not Found)}.
     */
    @PatchMapping(value = "/{id}/reject")
    public Mono<ResponseEntity<PersonalMonthlyTimesheetDTO>> rejectPersonalMonthlyTimesheet(
        @PathVariable("id") UUID id) {
        log.debug("REST request to reject PersonalMonthlyTimesheet : {}", id);
        return personalMonthlyTimesheetService
            .reject(id)
            .map(
                res -> ResponseEntity.ok()
                    .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME,
                        res.getId().toString()))
                    .body(res));
    }

    @PostMapping(value = "/approve")
    public Mono<ResponseEntity<Map<String, Object>>> approveMultiPersonalMonthlyTimesheet(
        @Valid @RequestBody ReviewMultipleTimeSheetDTO dto) {
        log.debug("REST request to approve Multi PersonalMonthlyTimesheet : {}", dto);
        return personalMonthlyTimesheetService
            .approveMulti(dto)
            .collectList()
            .map(leave -> ResponseEntity.ok().body(Utilities.generateResponse("SUCCESS", leave)));
    }

    @PostMapping(value = "/reject")
    public Mono<ResponseEntity<Map<String, Object>>> rejectMultiPersonalMonthlyTimesheet(
        @Valid @RequestBody ReviewMultipleTimeSheetDTO dto) {
        log.debug("REST request to reject Multi PersonalMonthlyTimesheet : {}", dto);
        return personalMonthlyTimesheetService
            .rejectMulti(dto)
            .collectList()
            .map(leave -> ResponseEntity.ok().body(Utilities.generateResponse("SUCCESS", leave)));
    }
}
