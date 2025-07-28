package com.masi.logistics.web.rest;

import com.carevn.masi.dto.ApiResponse;
import com.masi.logistics.repository.UomGroupRepository;
import com.masi.logistics.service.UomGroupService;
import com.masi.logistics.service.dto.UomGroupDTO;
import com.masi.logistics.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.ZonedDateTime;
import java.util.List;
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
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;
import com.carevn.masi.utils.SecurityUtils;

/**
 * REST controller for managing {@link com.masi.logistics.domain.UomGroup}.
 */
@RestController
@RequestMapping("/api/uom-groups")
public class UomGroupResource {

    private static final Logger log = LoggerFactory.getLogger(UomGroupResource.class);

    private static final String ENTITY_NAME = "masiLogisticsUomGroup";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UomGroupService uomGroupService;

    private final UomGroupRepository uomGroupRepository;

    public UomGroupResource(UomGroupService uomGroupService, UomGroupRepository uomGroupRepository) {
        this.uomGroupService = uomGroupService;
        this.uomGroupRepository = uomGroupRepository;
    }

    /**
     * {@code POST  /uom-groups} : Create a new uomGroup.
     *
     * @param uomGroupDTO the uomGroupDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with
     * body the new uomGroupDTO, or with status {@code 400 (Bad Request)} if
     * the uomGroup has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<UomGroupDTO>> createUomGroup(@Valid @RequestBody UomGroupDTO uomGroupDTO)
        throws URISyntaxException {
        log.debug("REST request to save UomGroup : {}", uomGroupDTO);
        if (uomGroupDTO.getId() != null) {
            throw new BadRequestAlertException("A new uomGroup cannot already have an ID", ENTITY_NAME,
                "idexists");
        }
        //uomGroupDTO.setId(UUID.randomUUID());
        return uomGroupService
            .save(uomGroupDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/uom-groups/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(
                            applicationName, true, ENTITY_NAME,
                            result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /uom-groups/:id} : Updates an existing uomGroup.
     *
     * @param id          the id of the uomGroupDTO to save.
     * @param uomGroupDTO the uomGroupDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     * the updated uomGroupDTO,
     * or with status {@code 400 (Bad Request)} if the uomGroupDTO is not
     * valid,
     * or with status {@code 500 (Internal Server Error)} if the uomGroupDTO
     * couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<UomGroupDTO>> updateUomGroup(
        @PathVariable(value = "id", required = false) final UUID id,
        @Valid @RequestBody UomGroupDTO uomGroupDTO) throws URISyntaxException {
        log.debug("REST request to update UomGroup : {}, {}", id, uomGroupDTO);
        if (uomGroupDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, uomGroupDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return uomGroupRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found",
                        ENTITY_NAME, "idnotfound"));
                }

                return uomGroupService
                    .update(uomGroupDTO)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(
                        HttpStatus.NOT_FOUND)))
                    .map(
                        result -> ResponseEntity.ok()
                            .headers(HeaderUtil
                                .createEntityUpdateAlert(
                                    applicationName,
                                    true,
                                    ENTITY_NAME,
                                    result.getId().toString()))
                            .body(result));
            });
    }

    /**
     * {@code PATCH  /uom-groups/:id} : Partial updates given fields of an existing
     * uomGroup, field will ignore if it is null
     *
     * @param id          the id of the uomGroupDTO to save.
     * @param uomGroupDTO the uomGroupDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     * the updated uomGroupDTO,
     * or with status {@code 400 (Bad Request)} if the uomGroupDTO is not
     * valid,
     * or with status {@code 404 (Not Found)} if the uomGroupDTO is not
     * found,
     * or with status {@code 500 (Internal Server Error)} if the uomGroupDTO
     * couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = {"application/json", "application/merge-patch+json"})
    public Mono<ResponseEntity<UomGroupDTO>> partialUpdateUomGroup(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody UomGroupDTO uomGroupDTO) throws URISyntaxException {
        log.debug("REST request to partial update UomGroup partially : {}, {}", id, uomGroupDTO);
        uomGroupDTO.setId(id);
        var user = SecurityUtils.getUserJWTDetail();
        return user.flatMap(login -> {
            uomGroupDTO.setCompany(login.getCompanyId());
            uomGroupDTO.setUpdateAt(ZonedDateTime.now());
            uomGroupDTO.setUpdateBy(login.getUserId().toString());
            return uomGroupRepository
                .existsById(id)
                .flatMap(exists -> {
                    if (!exists) {
                        return Mono.error(new BadRequestAlertException("Entity not found",
                            ENTITY_NAME, "idnotfound"));
                    }

                    Mono<UomGroupDTO> result = uomGroupService.partialUpdate(uomGroupDTO);

                    return result
                        .switchIfEmpty(Mono.error(new ResponseStatusException(
                            HttpStatus.NOT_FOUND)))
                        .map(
                            res -> ResponseEntity.ok()
                                .headers(HeaderUtil
                                    .createEntityUpdateAlert(
                                        applicationName,
                                        true,
                                        ENTITY_NAME,
                                        res.getId().toString()))
                                .body(res));
                });
        });
    }

    /**
     * {@code GET  /uom-groups} : get all the uomGroups.
     *
     * @param pageable the pagination information.
     * @param request  a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
     * of uomGroups in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<ApiResponse<UomGroupDTO>>> getAllUomGroups(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request) {
        log.debug("REST request to get a page of UomGroups");
        return uomGroupService
            .countAll()
            .zipWith(uomGroupService.findAll(pageable).collectList())
            .map(
                countWithEntities -> ResponseEntity.ok()
                    .headers(
                        PaginationUtil.generatePaginationHttpHeaders(
                            ForwardedHeaderUtils
                                .adaptFromForwardedHeaders(
                                    request.getURI(),
                                    request.getHeaders()),
                            new PageImpl<>(countWithEntities.getT2(), pageable,
                                countWithEntities.getT1())))
                    .body(new ApiResponse<>(countWithEntities.getT2(), countWithEntities.getT1()))
            );
    }

    /**
     * {@code GET  /uom-groups/:id} : get the "id" uomGroup.
     *
     * @param id the id of the uomGroupDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     * the uomGroupDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<UomGroupDTO>> getUomGroup(@PathVariable("id") UUID id) {
        log.debug("REST request to get UomGroup : {}", id);
        return SecurityUtils.getUserJWTDetail().flatMap(login -> {
            Mono<UomGroupDTO> uomGroupDTO = uomGroupService.findOne(id, login.getCompanyId());
            return ResponseUtil.wrapOrNotFound(uomGroupDTO);
        });
    }

    /**
     * {@code DELETE  /uom-groups/:id} : delete the "id" uomGroup.
     *
     * @param id the id of the uomGroupDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteUomGroup(@PathVariable("id") UUID id) {
        log.debug("REST request to delete UomGroup : {}", id);
        return SecurityUtils.getUserJWTDetail().flatMap(login -> {
            return uomGroupService
                .delete(id, login.getCompanyId(), login.getUserId().toString())
                .then(
                    Mono.just(
                        ResponseEntity.noContent()
                            .headers(HeaderUtil
                                .createEntityDeletionAlert(
                                    applicationName,
                                    true,
                                    ENTITY_NAME,
                                    id.toString()))
                            .build()));
        });
    }
}
