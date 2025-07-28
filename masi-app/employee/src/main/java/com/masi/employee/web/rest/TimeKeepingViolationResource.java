package com.masi.employee.web.rest;

import com.carevn.masi.constants.AuthoritiesConstants;
import com.carevn.masi.dto.ApiResponse;
import com.carevn.masi.utils.SecurityUtils;
import com.carevn.masi.utils.StringUtils;
import com.masi.employee.repository.TimeKeepingViolationRepository;
import com.masi.employee.service.TimeKeepingViolationService;
import com.masi.employee.service.dto.TimeKeepingExplanationDTO;
import com.masi.employee.service.dto.TimeKeepingViolationDTO;
import com.masi.employee.service.dto.TimeKeepingViolationQuery;
import com.masi.employee.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link com.masi.employee.domain.TimeKeepingViolation}.
 */
@RestController
@RequestMapping("/api/time-keeping-violations")
public class TimeKeepingViolationResource {

    private final Logger log = LoggerFactory.getLogger(TimeKeepingViolationResource.class);

    private static final String ENTITY_NAME = "masiEmployeeTimeKeepingViolation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TimeKeepingViolationService timeKeepingViolationService;

    private final TimeKeepingViolationRepository timeKeepingViolationRepository;

    public TimeKeepingViolationResource(
        TimeKeepingViolationService timeKeepingViolationService,
        TimeKeepingViolationRepository timeKeepingViolationRepository
    ) {
        this.timeKeepingViolationService = timeKeepingViolationService;
        this.timeKeepingViolationRepository = timeKeepingViolationRepository;
    }


    private Mono<TimeKeepingViolationQuery> resolveQuery(TimeKeepingViolationQuery query) {
        return SecurityUtils.getUserJWTDetail()
            .map(user -> {
                if (user.isHasAbove(AuthoritiesConstants.DIRECTOR)) {
                    return query;
                }
                if (user.isHasAbove(AuthoritiesConstants.DEPARTMENT_MANAGER)) {
                    query.setWorkspaceIds(List.of(StringUtils.tryToUUID(user.getGroupUId(), UUID.randomUUID())));
                    return query;
                }
                query.setCompany(user.getCompanyId());
                query.setEmployeeIds(List.of(user.getUserId()));
                return query;
            });
    }


    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ApiResponse<TimeKeepingViolationDTO>> getAllTimeKeepingViolations(
        @ParameterObject TimeKeepingViolationQuery timeKeepingViolationQuery,
        @ParameterObject Pageable pageable) {
        log.debug("REST request to get all TimeKeepingViolations as a stream");

        return
            this.resolveQuery(timeKeepingViolationQuery).flatMap(query -> {
                return timeKeepingViolationService
                    .countAll(query)
                    .zipWith(timeKeepingViolationService.findAll(pageable, query).collectList())
                    .map(data -> new ApiResponse<>(data.getT2(), data.getT1()));
            });
    }

    /**
     * {@code GET  /time-keeping-violations/:id} : get the "id" timeKeepingViolation.
     *
     * @param id the id of the timeKeepingViolationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the timeKeepingViolationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<TimeKeepingViolationDTO>> getTimeKeepingViolation(@PathVariable("id") UUID id) {
        log.debug("REST request to get TimeKeepingViolation : {}", id);
        Mono<TimeKeepingViolationDTO> timeKeepingViolationDTO = timeKeepingViolationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(timeKeepingViolationDTO);
    }

    /**
     * {@code GET  /time-keeping-violations/explanation-id/:id} : get the list of timeKeepingViolations by explanation id.
     *
     * @param id the id of the explanation to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the timeKeepingViolationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/explanation-id/{id}")
    public Mono<ResponseEntity<ApiResponse<TimeKeepingViolationDTO>>> getTimeKeepingViolationByExplanationId(@PathVariable("id") UUID id
        , @ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get TimeKeepingViolations by explanation id : {}", id);
        return timeKeepingViolationService
            .countAllByExplanationId(id)
            .zipWith(timeKeepingViolationService.findAllByExplanationId(id, pageable).collectList())
            .map(countWithDtos -> {
                Long count = countWithDtos.getT1();
                List<TimeKeepingViolationDTO> dtos = countWithDtos.getT2();
                return ResponseEntity.ok(
                    new ApiResponse<>(
                        dtos,
                        count
                    )
                );
            });
    }

    @GetMapping("/test_create")
    public Mono<ResponseEntity<Void>> scanAndCreateViolation() {
        log.debug("REST request to test create daily TimeKeepingViolation");
        return timeKeepingViolationService.generateViolationRecord()
            .then(
                Mono.just(
                    ResponseEntity.noContent()
                        .build()
                )
            );
    }
}
