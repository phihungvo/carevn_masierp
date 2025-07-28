package com.masi.employee.web.rest;

import com.carevn.masi.dto.ApiResponse;
import com.carevn.masi.utils.SecurityUtils;
import com.masi.employee.domain.criteria.CallCenterCriteria;
import com.masi.employee.domain.enumeration.StatusEntity;
import com.masi.employee.repository.CallCenterRepository;
import com.masi.employee.service.CallCenterService;
import com.masi.employee.service.dto.CallCenterDTO;
import com.masi.employee.service.dto.request.EmployeeRequest;
import com.masi.employee.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.ForwardedHeaderUtils;
import reactor.core.publisher.Mono;
import tech.jhipster.service.filter.StringFilter;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;
import com.masi.employee.security.AuthoritiesConstants;

/**
 * REST controller for managing {@link com.masi.employee.domain.CallCenter}.
 */
@RestController
@RequestMapping("/api/call-centers")
public class CallCenterResource {

    private static final Logger log = LoggerFactory.getLogger(CallCenterResource.class);

    private static final String ENTITY_NAME = "masiEmployeeCallCenter";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CallCenterService callCenterService;

    private final CallCenterRepository callCenterRepository;

    public CallCenterResource(CallCenterService callCenterService, CallCenterRepository callCenterRepository) {
        this.callCenterService = callCenterService;
        this.callCenterRepository = callCenterRepository;
    }

    /**
     * {@code POST  /call-centers} : Create a new callCenter.
     *
     * @param callCenterDTO the callCenterDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new callCenterDTO, or with status {@code 400 (Bad Request)} if the callCenter has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<CallCenterDTO>> createCallCenter(@Valid @RequestBody CallCenterDTO callCenterDTO) throws URISyntaxException {
        log.debug("REST request to save CallCenter : {}", callCenterDTO);

        callCenterDTO.setId(UUID.randomUUID());
        return callCenterService
            .save(callCenterDTO)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/call-centers/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }


    /**
     * {@code PATCH  /call-centers/:id} : Partial updates given fields of an existing callCenter, field will ignore if it is null
     *
     * @param id the id of the callCenterDTO to save.
     * @param callCenterDTO the callCenterDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated callCenterDTO,
     * or with status {@code 400 (Bad Request)} if the callCenterDTO is not valid,
     * or with status {@code 404 (Not Found)} if the callCenterDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the callCenterDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<CallCenterDTO>> partialUpdateCallCenter(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody CallCenterDTO callCenterDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update CallCenter partially : {}, {}", id, callCenterDTO);
        callCenterDTO.setId(id);
        return callCenterRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }
                Mono<CallCenterDTO> result = callCenterService.partialUpdate(callCenterDTO);
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

    /**
     * {@code GET  /call-centers} : get all the callCenters.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of callCenters in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<ApiResponse<CallCenterDTO>>> getAllCallCenters(
            CallCenterCriteria criteria,
            @org.springdoc.core.annotations.ParameterObject Pageable pageable,
            ServerHttpRequest request
    ) {
        log.debug("REST request to get CallCenters by criteria: {}", criteria);

        return SecurityUtils.getUserJWTDetail().flatMap(user -> {
            if (!user.getAuthorities().contains(AuthoritiesConstants.SUPER_ADMIN)) {
                if (criteria.getCompany() == null) {
                    criteria.setCompany(new StringFilter());
                }
                if (criteria.getCompany().getEquals() == null) {
                    criteria.getCompany().setEquals(user.getCompanyId());
                }
            }
            return callCenterService.countByCriteria(criteria)
                    .zipWith(callCenterService.findByCriteria(criteria, pageable).collectList())
                    .map(countWithEntities -> {
                        long totalCount = countWithEntities.getT1();
                        List<CallCenterDTO> entities = countWithEntities.getT2();
                        return ResponseEntity.ok()
                                .headers(
                                        PaginationUtil.generatePaginationHttpHeaders(
                                                ForwardedHeaderUtils.adaptFromForwardedHeaders(request.getURI(), request.getHeaders()),
                                                new PageImpl<>(entities, pageable, totalCount)
                                        )
                                )
                                .body(new ApiResponse<>(entities, totalCount));
                    });
        }).switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()));
    }




    /**
     * {@code GET  /call-centers/:id} : get the "id" callCenter.
     *
     * @param id the id of the callCenterDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the callCenterDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<CallCenterDTO>> getCallCenter(@PathVariable("id") UUID id) {
        log.debug("REST request to get CallCenter : {}", id);
        Mono<CallCenterDTO> callCenterDTO = callCenterService.findOne(id);
        return ResponseUtil.wrapOrNotFound(callCenterDTO);
    }

    /**
     * {@code DELETE  /call-centers/:id} : delete the "id" callCenter.
     *
     * @param id the id of the callCenterDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteCallCenter(@PathVariable("id") UUID id) {
        log.debug("REST request to delete CallCenter : {}", id);
        return callCenterService
            .delete(id)
            .then(
                Mono.just(
                    ResponseEntity.noContent()
                        .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                        .build()
                )
            );
    }

    @PatchMapping(value = "/{id}/processing")
    public Mono<ResponseEntity<Map<String, UUID>>> processingCallCenter(@PathVariable UUID id) {
        return SecurityUtils.getUserJWTDetail()
                .flatMap(user ->
                        callCenterService.setStatus(id, StatusEntity.PROCESSING, user.getUserId(), 1)
                                .then(Mono.just(ResponseEntity.ok(Map.of("id", id))))
                );
    }

    @PatchMapping(value = "/{id}/closed")
    public Mono<ResponseEntity<Map<String, UUID>>> closedCallCenter(@PathVariable UUID id) {
        return SecurityUtils.getUserJWTDetail()
                .flatMap(user ->
                        callCenterService.setStatus(id, StatusEntity.CLOSED, user.getUserId(), 2)
                                .then(Mono.just(ResponseEntity.ok(Map.of("id", id))))
                );
    }

    @PatchMapping(value = "/{id}/completed")
    public Mono<ResponseEntity<Map<String, UUID>>> completedCallCenter(@PathVariable UUID id) {
        return callCenterService.setStatus(id, StatusEntity.COMPLETED,null, 0)
                .then(Mono.just(ResponseEntity.ok(Map.of("id", id))));
    }
}
