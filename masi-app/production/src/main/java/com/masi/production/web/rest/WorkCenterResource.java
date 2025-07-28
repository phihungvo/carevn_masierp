package com.masi.production.web.rest;

import com.carevn.masi.dto.ApiResponse;
import com.carevn.masi.utils.Utilities;
import com.masi.production.domain.WorkCenter;
import com.masi.production.domain.enumeration.WorkCenterStatusEnum;
import com.masi.production.repository.WorkCenterRepository;
import com.masi.production.security.SecurityUtils;
import com.masi.production.service.WorkCenterService;
import com.masi.production.service.dto.QualityCheckSampleDTO;
import com.masi.production.service.dto.WorkCenterCreateDTO;
import com.masi.production.service.dto.WorkCenterDTO;
import com.masi.production.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.annotations.ParameterObject;
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
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link WorkCenter}.
 */
@RestController
@RequestMapping("/api/work-centers")
public class WorkCenterResource {

    private final Logger log = LoggerFactory.getLogger(WorkCenterResource.class);

    private static final String ENTITY_NAME = "masiProductionWorkCenter";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WorkCenterService workCenterService;

    private final WorkCenterRepository workCenterRepository;

    public WorkCenterResource(WorkCenterService workCenterService, WorkCenterRepository workCenterRepository) {
        this.workCenterService = workCenterService;
        this.workCenterRepository = workCenterRepository;
    }

    /**
     * {@code POST  /work-centers} : Create a new workCenter.
     *
     * @param workCenterCreateDTO the workCenterDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with
     *         body the new workCenterDTO, or with status {@code 400 (Bad Request)}
     *         if the workCenter has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<WorkCenterDTO>> createWorkCenter(
            @Valid @RequestBody WorkCenterDTO workCenterDTO)
            throws URISyntaxException {
        log.debug("REST request to save WorkCenter : {}", workCenterDTO);
        return SecurityUtils.getCurrentUserLogin()
                .flatMap(currentUserLogin -> {
                    return workCenterService.save(workCenterDTO);
                })
                .map(result -> {
                    try {
                        return ResponseEntity.created(new URI("/api/work-centers/" + result.getId()))
                                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME,
                                        result.getId().toString()))
                                .body(result);
                    } catch (URISyntaxException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    /**
     * {@code PATCH  /work-centers/:id} : Partial updates given fields of an
     * existing workCenter, field will ignore if it is null
     *
     * @param id            the id of the workCenterDTO to save.
     * @param workCenterCreateDTO the workCenterDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the updated workCenterDTO,
     *         or with status {@code 400 (Bad Request)} if the workCenterDTO is not
     *         valid,
     *         or with status {@code 404 (Not Found)} if the workCenterDTO is not
     *         found,
     *         or with status {@code 500 (Internal Server Error)} if the
     *         workCenterDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<Map>> partialUpdateWorkCenter(
            @PathVariable(value = "id", required = false) final UUID id,
            @NotNull @RequestBody WorkCenterDTO workCenterDTO) throws URISyntaxException {
        log.debug("REST request to partial update WorkCenter partially : {}, {}", id, workCenterDTO);
        workCenterDTO.setId(id);

        if (workCenterDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, workCenterDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return workCenterRepository
                .existsById(id)
                .flatMap(exists -> {
                    if (!exists) {
                        return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                    }

                    return SecurityUtils.getCurrentUserLogin()
                            .flatMap(currentUserLogin -> {

                                Mono<WorkCenterDTO> result = workCenterService.partialUpdate(workCenterDTO,
                                        currentUserLogin);

                                return result
                                        .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                                        .map(
                                                res -> ResponseEntity.ok()
                                                        .headers(HeaderUtil.createEntityUpdateAlert(applicationName,
                                                                true, ENTITY_NAME, res.getId().toString()))
                                                        .body(Utilities.generateResponse("success", res)));
                            });
                });
    }

    /**
     * {@code GET  /work-centers} : get all the workCenters.
     *
     * @param pageable the pagination information.
     * @param request  a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
     *         of workCenters in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<ApiResponse<WorkCenterDTO>>> getAllWorkCenters(
            @ParameterObject Pageable pageable,
            ServerHttpRequest request,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) UUID companyId,
            @RequestParam(required = false) List<WorkCenterStatusEnum> status) {
        log.debug("REST request to get a page of WorkCenters");
        return com.carevn.masi.utils.SecurityUtils.getUserJWTDetail().flatMap(user -> {
            return workCenterService
                .CountWithFilter(search, status, companyId,String.valueOf(user.getCompanyId()),user.getGroupId().toString())
                .zipWith(workCenterService.GetAllWithFilter(pageable, search, status, companyId,String.valueOf(user.getCompanyId()),user.getGroupId().toString()).collectList())
                .map(countWithEntities -> ResponseEntity.ok()
                    .body(new ApiResponse<>(countWithEntities.getT2(), countWithEntities.getT1())));
        });

    }

    /**
     * {@code GET  /work-centers/:id} : get the "id" workCenter.
     *
     * @param id the id of the workCenterDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the workCenterDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<WorkCenterDTO>> getWorkCenter(@PathVariable("id") UUID id) {
        log.debug("REST request to get WorkCenter : {}", id);
        Mono<WorkCenterDTO> workCenterDTO = workCenterService.findOne(id);
        return ResponseUtil.wrapOrNotFound(workCenterDTO);
    }

    /**
     * {@code DELETE  /work-centers/:id} : delete the "id" workCenter.
     *
     * @param id the id of the workCenterDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteWorkCenter(@PathVariable("id") UUID id) {
        log.debug("REST request to delete WorkCenter : {}", id);
        return SecurityUtils.getCurrentUserLogin().flatMap(currentUserLogin -> {
            return workCenterService.softDelete(id, currentUserLogin).then(Mono.just(ResponseEntity.noContent()
                    .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                    .build()));
        });
    }
}
