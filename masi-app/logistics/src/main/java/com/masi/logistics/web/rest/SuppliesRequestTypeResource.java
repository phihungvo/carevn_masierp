package com.masi.logistics.web.rest;

import com.carevn.masi.dto.ApiResponse;
import com.masi.logistics.domain.criteria.SuppliesRequestTypeCriteria;
import com.masi.logistics.repository.SuppliesRequestTypeRepository;
import com.masi.logistics.service.SuppliesRequestTypeService;
import com.masi.logistics.service.dto.SuppliesRequestTypeDTO;
import com.masi.logistics.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
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

/**
 * REST controller for managing {@link com.masi.logistics.domain.SuppliesRequestType}.
 */
@RestController
@RequestMapping("/api/supplies-request-types")
public class SuppliesRequestTypeResource {

    private static final Logger log = LoggerFactory.getLogger(SuppliesRequestTypeResource.class);

    private static final String ENTITY_NAME = "masiLogisticsSuppliesRequestType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SuppliesRequestTypeService suppliesRequestTypeService;

    private final SuppliesRequestTypeRepository suppliesRequestTypeRepository;

    public SuppliesRequestTypeResource(
        SuppliesRequestTypeService suppliesRequestTypeService,
        SuppliesRequestTypeRepository suppliesRequestTypeRepository
    ) {
        this.suppliesRequestTypeService = suppliesRequestTypeService;
        this.suppliesRequestTypeRepository = suppliesRequestTypeRepository;
    }

    /**
     * {@code POST  /supplies-request-types} : Create a new suppliesRequestType.
     *
     * @param suppliesRequestTypeDTO the suppliesRequestTypeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new suppliesRequestTypeDTO, or with status {@code 400 (Bad Request)} if the suppliesRequestType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<SuppliesRequestTypeDTO>> createSuppliesRequestType(
        @Valid @RequestBody SuppliesRequestTypeDTO suppliesRequestTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to save SuppliesRequestType : {}", suppliesRequestTypeDTO);
        if (suppliesRequestTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new suppliesRequestType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        suppliesRequestTypeDTO.setId(UUID.randomUUID());
        return suppliesRequestTypeService
            .save(suppliesRequestTypeDTO)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/supplies-request-types/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /supplies-request-types/:id} : Updates an existing suppliesRequestType.
     *
     * @param id the id of the suppliesRequestTypeDTO to save.
     * @param suppliesRequestTypeDTO the suppliesRequestTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated suppliesRequestTypeDTO,
     * or with status {@code 400 (Bad Request)} if the suppliesRequestTypeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the suppliesRequestTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<SuppliesRequestTypeDTO>> updateSuppliesRequestType(
        @PathVariable(value = "id", required = false) final UUID id,
        @Valid @RequestBody SuppliesRequestTypeDTO suppliesRequestTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update SuppliesRequestType : {}, {}", id, suppliesRequestTypeDTO);
        if (suppliesRequestTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, suppliesRequestTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return suppliesRequestTypeRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return suppliesRequestTypeService
                    .update(suppliesRequestTypeDTO)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(
                        result ->
                            ResponseEntity.ok()
                                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                                .body(result)
                    );
            });
    }

    /**
     * {@code PATCH  /supplies-request-types/:id} : Partial updates given fields of an existing suppliesRequestType, field will ignore if it is null
     *
     * @param id the id of the suppliesRequestTypeDTO to save.
     * @param suppliesRequestTypeDTO the suppliesRequestTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated suppliesRequestTypeDTO,
     * or with status {@code 400 (Bad Request)} if the suppliesRequestTypeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the suppliesRequestTypeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the suppliesRequestTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<SuppliesRequestTypeDTO>> partialUpdateSuppliesRequestType(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody SuppliesRequestTypeDTO suppliesRequestTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update SuppliesRequestType partially : {}, {}", id, suppliesRequestTypeDTO);
        if (suppliesRequestTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, suppliesRequestTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return suppliesRequestTypeRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<SuppliesRequestTypeDTO> result = suppliesRequestTypeService.partialUpdate(suppliesRequestTypeDTO);

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
     * {@code GET  /supplies-request-types} : get all the suppliesRequestTypes.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of suppliesRequestTypes in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<ApiResponse<SuppliesRequestTypeDTO>>> getAllSuppliesRequestTypes(
        SuppliesRequestTypeCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get SuppliesRequestTypes by criteria: {}", criteria);
        return suppliesRequestTypeService
            .countByCriteria(criteria)
            .zipWith(suppliesRequestTypeService.findByCriteria(criteria, pageable).collectList())
            .map(
                countWithEntities ->
                    ResponseEntity.ok()
                        .headers(
                            PaginationUtil.generatePaginationHttpHeaders(
                                ForwardedHeaderUtils.adaptFromForwardedHeaders(request.getURI(), request.getHeaders()),
                                new PageImpl<>(countWithEntities.getT2(), pageable, countWithEntities.getT1())
                            )
                        )
                        .body(new ApiResponse<>(countWithEntities.getT2(), countWithEntities.getT1()))
            );
    }

    /**
     * {@code GET  /supplies-request-types/count} : count all the suppliesRequestTypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public Mono<ResponseEntity<Long>> countSuppliesRequestTypes(SuppliesRequestTypeCriteria criteria) {
        log.debug("REST request to count SuppliesRequestTypes by criteria: {}", criteria);
        return suppliesRequestTypeService.countByCriteria(criteria).map(count -> ResponseEntity.status(HttpStatus.OK).body(count));
    }

    /**
     * {@code GET  /supplies-request-types/:id} : get the "id" suppliesRequestType.
     *
     * @param id the id of the suppliesRequestTypeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the suppliesRequestTypeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<SuppliesRequestTypeDTO>> getSuppliesRequestType(@PathVariable("id") UUID id) {
        log.debug("REST request to get SuppliesRequestType : {}", id);
        Mono<SuppliesRequestTypeDTO> suppliesRequestTypeDTO = suppliesRequestTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(suppliesRequestTypeDTO);
    }

    /**
     * {@code DELETE  /supplies-request-types/:id} : delete the "id" suppliesRequestType.
     *
     * @param id the id of the suppliesRequestTypeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteSuppliesRequestType(@PathVariable("id") UUID id) {
        log.debug("REST request to delete SuppliesRequestType : {}", id);
        return suppliesRequestTypeService
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
