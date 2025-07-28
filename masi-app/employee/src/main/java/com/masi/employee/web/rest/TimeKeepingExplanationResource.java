package com.masi.employee.web.rest;

import com.carevn.masi.constants.AuthoritiesConstants;
import com.carevn.masi.dto.ApiResponse;
import com.carevn.masi.utils.SecurityUtils;
import com.carevn.masi.utils.StringUtils;
import com.masi.employee.repository.TimeKeepingExplanationRepository;
import com.masi.employee.service.TimeKeepingExplanationService;
import com.masi.employee.service.dto.*;
import com.masi.employee.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.ZonedDateTime;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
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
 * REST controller for managing {@link com.masi.employee.domain.TimeKeepingExplanation}.
 */
@RestController
@RequestMapping("/api/time-keeping-explanations")
public class TimeKeepingExplanationResource {

    private final Logger log = LoggerFactory.getLogger(TimeKeepingExplanationResource.class);

    private static final String ENTITY_NAME = "masiEmployeeTimeKeepingExplanation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TimeKeepingExplanationService timeKeepingExplanationService;

    private final TimeKeepingExplanationRepository timeKeepingExplanationRepository;

    public TimeKeepingExplanationResource(
        TimeKeepingExplanationService timeKeepingExplanationService,
        TimeKeepingExplanationRepository timeKeepingExplanationRepository
    ) {
        this.timeKeepingExplanationService = timeKeepingExplanationService;
        this.timeKeepingExplanationRepository = timeKeepingExplanationRepository;
    }

    /**
     * {@code POST  /time-keeping-explanations} : Create a new timeKeepingExplanation.
     *
     * @param timeKeepingExplanationDTO the timeKeepingExplanationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new timeKeepingExplanationDTO, or with status {@code 400 (Bad Request)} if the timeKeepingExplanation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
//    @PostMapping("")
//    public Mono<ResponseEntity<TimeKeepingExplanationDTO>> createTimeKeepingExplanation(
//        @Valid @RequestBody TimeKeepingExplanationDTO timeKeepingExplanationDTO
//    ) throws URISyntaxException {
//        log.debug("REST request to save TimeKeepingExplanation : {}", timeKeepingExplanationDTO);
//        if (timeKeepingExplanationDTO.getId() != null) {
//            throw new BadRequestAlertException("A new timeKeepingExplanation cannot already have an ID", ENTITY_NAME, "idexists");
//        }
//        timeKeepingExplanationDTO.setId(UUID.randomUUID());
//        return timeKeepingExplanationService
//            .save(timeKeepingExplanationDTO)
//            .map(result -> {
//                try {
//                    return ResponseEntity.created(new URI("/api/time-keeping-explanations/" + result.getId()))
//                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
//                        .body(result);
//                } catch (URISyntaxException e) {
//                    throw new RuntimeException(e);
//                }
//            });
//    }
    @PostMapping("")
    public Mono<ResponseEntity<Map<String, String>>> createTimeKeepingExplanation(
        @Valid @RequestBody Collection<ExplanationRequest> timeKeepingExplanationDTO
    ) throws URISyntaxException {

        return timeKeepingExplanationService
            .saveV2(timeKeepingExplanationDTO)
            .then(
                Mono.just(
                    ResponseEntity.ok()
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, ""))
                        .body(Map.of("message", "success"))
                )
            );
    }

    /**
     * {@code PATCH  /time-keeping-explanations/:id} : Partial updates given fields of an existing timeKeepingExplanation, field will ignore if it is null
     *
     * @param id                        the id of the timeKeepingExplanationDTO to save.
     * @param timeKeepingExplanationDTO the timeKeepingExplanationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated timeKeepingExplanationDTO,
     * or with status {@code 400 (Bad Request)} if the timeKeepingExplanationDTO is not valid,
     * or with status {@code 404 (Not Found)} if the timeKeepingExplanationDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the timeKeepingExplanationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = {"application/json", "application/merge-patch+json"})
    public Mono<ResponseEntity<TimeKeepingExplanationDTO>> partialUpdateTimeKeepingExplanation(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody TimeKeepingExplanationDTO timeKeepingExplanationDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update TimeKeepingExplanation partially : {}, {}", id, timeKeepingExplanationDTO);
        timeKeepingExplanationDTO.setId(id);
        return timeKeepingExplanationService.partialUpdate(timeKeepingExplanationDTO).map(result -> ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, timeKeepingExplanationDTO.getId().toString())).body(result));
    }

    private Mono<ExplanationRO> resolveQuery(ExplanationRO query) {
        return SecurityUtils.getUserJWTDetail()
            .map(user -> {
                if (user.isHasAbove(AuthoritiesConstants.DIRECTOR) || "HCNS".equalsIgnoreCase(user.getGroupId())) {
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

    /**
     * {@code GET  /time-keeping-explanations} : get all the timeKeepingExplanations.
     *
     * @param ro       the request object of the timeKeepingExplanation.
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of timeKeepingExplanations in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ApiResponse<TimeKeepingExplanationDTO>> getAllTimeKeepingExplanations(
        @ParameterObject ExplanationRO ro,
        @ParameterObject Pageable pageable) {
        log.debug("REST request to get all TimeKeepingExplanations");
        return this.resolveQuery(ro).flatMap(query -> {
            return timeKeepingExplanationService
                .countAll(query)
                .zipWith(timeKeepingExplanationService.findAll(pageable, query).collectList())
                .map(data -> new ApiResponse<>(data.getT2(), data.getT1()));
        });
    }

    /**
     * {@code GET  /time-keeping-explanations/:id} : get the "id" timeKeepingExplanation.
     *
     * @param id the id of the timeKeepingExplanationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the timeKeepingExplanationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<TimeKeepingExplanationDTO>> getTimeKeepingExplanation(@PathVariable("id") UUID id) {
        log.debug("REST request to get TimeKeepingExplanation : {}", id);
        Mono<TimeKeepingExplanationDTO> timeKeepingExplanationDTO = timeKeepingExplanationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(timeKeepingExplanationDTO);
    }

    @PatchMapping(value = "/{id}/cancel")
    public Mono<Map<String, Object>> cancelExplanation(@PathVariable("id") UUID id) {
        log.debug("REST request to cancel explanation : {}", id);
        return timeKeepingExplanationService.cancelLeaveRequest(id);
    }

    /**
     * {@code DELETE  /time-keeping-explanations/:id} : delete the "id" timeKeepingExplanation.
     *
     * @param id the id of the timeKeepingExplanationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteTimeKeepingExplanation(@PathVariable("id") UUID id) {
        log.debug("REST request to delete TimeKeepingExplanation : {}", id);
        return timeKeepingExplanationService
            .delete(id)
            .then(
                Mono.just(
                    ResponseEntity.noContent()
                        .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                        .build()
                )
            );
    }
}
