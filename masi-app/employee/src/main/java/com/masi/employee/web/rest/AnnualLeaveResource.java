package com.masi.employee.web.rest;

import com.masi.employee.domain.EmployeeProfile;
import com.masi.employee.domain.enumeration.WorkPlace;
import com.masi.employee.repository.AnnualLeaveRepository;
import com.masi.employee.repository.EmployeeProfileRepository;
import com.masi.employee.service.AnnualLeaveService;
import com.masi.employee.service.dto.AnnualLeaveDTO;
import com.masi.employee.service.dto.DayOffDTO;
import com.masi.employee.service.dto.EmployeeProfileDTO;
import com.masi.employee.service.dto.reponse.AnnualLeaveDTOReponse;
import com.masi.employee.service.dto.reponse.SeniorityDtoReponse;
import com.masi.employee.service.dto.request.AnnualLeaveDTORequest;
import com.masi.employee.service.dto.request.DayOffRequest;
import com.masi.employee.web.rest.errors.BadRequestAlertException;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.constraints.NotNull;

import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.ForwardedHeaderUtils;
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link com.masi.employee.domain.AnnualLeave}.
 */
@RestController
@RequestMapping("/api/annual-leaves")
public class AnnualLeaveResource {

    private static final Logger log = LoggerFactory.getLogger(AnnualLeaveResource.class);

    private static final String ENTITY_NAME = "masiEmployeeAnnualLeave";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AnnualLeaveService annualLeaveService;
    private final EmployeeProfileRepository employeeProfileRepository;

    public AnnualLeaveResource(AnnualLeaveService annualLeaveService,
            EmployeeProfileRepository employeeProfileRepository) {
        this.annualLeaveService = annualLeaveService;
        this.employeeProfileRepository = employeeProfileRepository;
    }

    @Operation(summary = "Update data office and factory or create if not exist")
    @PatchMapping(value = "", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<Map<String, Object>>> partialUpdateAnnualLeave(
            @NotNull @RequestBody AnnualLeaveDTORequest annualLeaveRequest) throws URISyntaxException {

        log.debug("REST request to partial update AnnualLeave partially : {}", annualLeaveRequest);

        AnnualLeaveDTO annualLeaveFACTORY = annualLeaveRequest.getAnnualLeave_FACTORY();
        AnnualLeaveDTO annualLeaveOFFICE = annualLeaveRequest.getAnnualLeave_OFFICE();
        annualLeaveFACTORY.setWorkPlace(WorkPlace.FACTORY);
        annualLeaveOFFICE.setWorkPlace(WorkPlace.OFFICE);
        return annualLeaveService.partialUpdate(annualLeaveFACTORY, annualLeaveOFFICE)
                .map(result -> {
                    Map<String, Object> responseBody = new HashMap<>();
                    responseBody.put("message", "Contract recovered review successfully");
                    responseBody.put("status", "success");
                    return ResponseEntity.ok().body(responseBody);
                })
                .onErrorResume(e -> Mono.error(
                        new BadRequestAlertException("Failed to approved contract: " + e.getMessage(), "contract",
                                "approved")));
    }

    /**
     * {@code GET  /annual-leaves} : get all the annualLeaves.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
     *         of annualLeaves in body.
     */

    @Operation(summary = "Get data office and factory")
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<AnnualLeaveDTOReponse>> getAllAnnualLeaves() {
        log.debug("REST request to get all AnnualLeaves");
        return annualLeaveService.findAll()
                .map(annualLeaveDTOReponse -> ResponseEntity.ok().body(annualLeaveDTOReponse))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Get data Leave By Id Employee")
    @GetMapping("/employee/{id}")
    public Mono<ResponseEntity<DayOffDTO>> getYourAnnualLeaves(@PathVariable("id") UUID id) {
        log.debug("REST request to get id DayOffRequest");
        Mono<DayOffDTO> annualLeaveDTOMono = annualLeaveService.findOneById(id);

        return ResponseUtil.wrapOrNotFound(annualLeaveDTOMono);
    }

    // @Operation(summary = "Get data Leave By Id Employee")
    // @GetMapping("/test/{date}")
    // public Mono<ResponseEntity<DayOffDTO>>
    // getYourAnnualLeavess(@PathVariable("date") LocalDate date) {
    // log.debug("REST request to get id DayOffRequest");
    //
    // // Thực hiện hành động với `employeeProfileRepository` và
    // `annualLeaveService`
    // return
    // employeeProfileRepository.test(UUID.fromString("548ff4c4-2c66-4105-930d-7ae74604b123"),
    // date)
    // .then(annualLeaveService.findOneById(UUID.fromString("548ff4c4-2c66-4105-930d-7ae74604b123"))
    // .flatMap(dayOffDTO -> ResponseUtil.wrapOrNotFound(Mono.just(dayOffDTO))));
    //
    // }

    @Operation(summary = "Get data Leave By Id Employee")
    @GetMapping("/seniority/{workspaceId}/{date}")
    public Mono<ResponseEntity<SeniorityDtoReponse>> getSeniority(@PathVariable("date") LocalDate date,
            @PathVariable("workspaceId") UUID workspaceId) {
        log.debug("REST seniority to get date");
        return ResponseUtil.wrapOrNotFound(annualLeaveService.cacularotrSeniority(workspaceId, date));
    }

    // @Operation(summary = "Get data my Leave")
    // @GetMapping("/id")
    // public Mono<ResponseEntity<DayOffDTO>> getMyAnnualLeaves(
    // ) {
    // log.debug("REST request to get all AnnualLeaves");
    // Mono<DayOffDTO> annualLeaveDTOMono = annualLeaveService.findOneByAuto();
    // return ResponseUtil.wrapOrNotFound(annualLeaveDTOMono);
    //
    // }

}
