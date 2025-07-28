package com.masi.logistics.web.rest;

import com.carevn.masi.dto.ApiResponse;
import com.masi.logistics.repository.SupplierGroupRepository;
import com.masi.logistics.service.SupplierGroupService;
import com.masi.logistics.service.dto.SupplierGroupDTO;
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
 * REST controller for managing {@link com.masi.logistics.domain.SupplierGroup}.
 */
@RestController
@RequestMapping("/api/supplier-groups")
public class SupplierGroupResource {

    private static final Logger log = LoggerFactory.getLogger(SupplierGroupResource.class);

    private static final String ENTITY_NAME = "masiLogisticsSupplierGroup";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SupplierGroupService supplierGroupService;

    private final SupplierGroupRepository supplierGroupRepository;

    public SupplierGroupResource(SupplierGroupService supplierGroupService, SupplierGroupRepository supplierGroupRepository) {
        this.supplierGroupService = supplierGroupService;
        this.supplierGroupRepository = supplierGroupRepository;
    }

    /**
     * {@code POST  /supplier-groups} : Create a new supplierGroup.
     *
     * @param supplierGroupDTO the supplierGroupDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new supplierGroupDTO, or with status {@code 400 (Bad Request)} if the supplierGroup has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<SupplierGroupDTO>> createSupplierGroup(@Valid @RequestBody SupplierGroupDTO supplierGroupDTO)
        throws URISyntaxException {
        log.debug("REST request to save SupplierGroup : {}", supplierGroupDTO);
        if (supplierGroupDTO.getId() != null) {
            throw new BadRequestAlertException("A new supplierGroup cannot already have an ID", ENTITY_NAME, "idexists");
        }
        supplierGroupDTO.setId(UUID.randomUUID());
        return supplierGroupService
            .save(supplierGroupDTO)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/supplier-groups/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /supplier-groups/:id} : Updates an existing supplierGroup.
     *
     * @param id the id of the supplierGroupDTO to save.
     * @param supplierGroupDTO the supplierGroupDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated supplierGroupDTO,
     * or with status {@code 400 (Bad Request)} if the supplierGroupDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the supplierGroupDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<SupplierGroupDTO>> updateSupplierGroup(
        @PathVariable(value = "id", required = false) final UUID id,
        @Valid @RequestBody SupplierGroupDTO supplierGroupDTO
    ) throws URISyntaxException {
        log.debug("REST request to update SupplierGroup : {}, {}", id, supplierGroupDTO);
        if (supplierGroupDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, supplierGroupDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return supplierGroupRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return supplierGroupService
                    .update(supplierGroupDTO)
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
     * {@code PATCH  /supplier-groups/:id} : Partial updates given fields of an existing supplierGroup, field will ignore if it is null
     *
     * @param id the id of the supplierGroupDTO to save.
     * @param supplierGroupDTO the supplierGroupDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated supplierGroupDTO,
     * or with status {@code 400 (Bad Request)} if the supplierGroupDTO is not valid,
     * or with status {@code 404 (Not Found)} if the supplierGroupDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the supplierGroupDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<SupplierGroupDTO>> partialUpdateSupplierGroup(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody SupplierGroupDTO supplierGroupDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update SupplierGroup partially : {}, {}", id, supplierGroupDTO);
        supplierGroupDTO.setId(id);
        return supplierGroupRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<SupplierGroupDTO> result = supplierGroupService.partialUpdate(supplierGroupDTO);

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
     * {@code GET  /supplier-groups} : get all the supplierGroups.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of supplierGroups in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<ApiResponse<SupplierGroupDTO>>> getAllSupplierGroups(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of SupplierGroups");
        return supplierGroupService
            .countAll()
            .zipWith(supplierGroupService.findAll(pageable).collectList())
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
     * {@code GET  /supplier-groups/:id} : get the "id" supplierGroup.
     *
     * @param id the id of the supplierGroupDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the supplierGroupDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<SupplierGroupDTO>> getSupplierGroup(@PathVariable("id") UUID id) {
        log.debug("REST request to get SupplierGroup : {}", id);
        Mono<SupplierGroupDTO> supplierGroupDTO = supplierGroupService.findOne(id);
        return ResponseUtil.wrapOrNotFound(supplierGroupDTO);
    }

    /**
     * {@code DELETE  /supplier-groups/:id} : delete the "id" supplierGroup.
     *
     * @param id the id of the supplierGroupDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteSupplierGroup(@PathVariable("id") UUID id) {
        log.debug("REST request to delete SupplierGroup : {}", id);
        return supplierGroupService
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
