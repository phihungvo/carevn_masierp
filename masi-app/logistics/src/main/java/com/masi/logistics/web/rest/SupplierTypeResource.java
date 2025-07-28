package com.masi.logistics.web.rest;

import com.carevn.masi.dto.ApiResponse;
import com.masi.logistics.domain.criteria.SupplierTypeCriteria;
import com.masi.logistics.repository.SupplierTypeRepository;
import com.masi.logistics.service.SupplierTypeService;
import com.masi.logistics.service.dto.SupplierTypeDTO;
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
 * REST controller for managing {@link com.masi.logistics.domain.SupplierType}.
 */
@RestController
@RequestMapping("/api/supplier-types")
public class SupplierTypeResource {

    private static final Logger LOG = LoggerFactory.getLogger(SupplierTypeResource.class);

    private static final String ENTITY_NAME = "masiLogisticsSupplierType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SupplierTypeService supplierTypeService;

    private final SupplierTypeRepository supplierTypeRepository;

    public SupplierTypeResource(SupplierTypeService supplierTypeService, SupplierTypeRepository supplierTypeRepository) {
        this.supplierTypeService = supplierTypeService;
        this.supplierTypeRepository = supplierTypeRepository;
    }

    /**
     * {@code POST  /supplier-types} : Create a new supplierType.
     *
     * @param supplierTypeDTO the supplierTypeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new supplierTypeDTO, or with status {@code 400 (Bad Request)} if the supplierType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<SupplierTypeDTO>> createSupplierType(@Valid @RequestBody SupplierTypeDTO supplierTypeDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save SupplierType : {}", supplierTypeDTO);
        if (supplierTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new supplierType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        supplierTypeDTO.setId(UUID.randomUUID());
        return supplierTypeService
            .save(supplierTypeDTO)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/supplier-types/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /supplier-types/:id} : Updates an existing supplierType.
     *
     * @param id the id of the supplierTypeDTO to save.
     * @param supplierTypeDTO the supplierTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated supplierTypeDTO,
     * or with status {@code 400 (Bad Request)} if the supplierTypeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the supplierTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<SupplierTypeDTO>> updateSupplierType(
        @PathVariable(value = "id", required = false) final UUID id,
        @Valid @RequestBody SupplierTypeDTO supplierTypeDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update SupplierType : {}, {}", id, supplierTypeDTO);
        if (supplierTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, supplierTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return supplierTypeRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return supplierTypeService
                    .update(supplierTypeDTO)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(result ->
                        ResponseEntity.ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                            .body(result)
                    );
            });
    }

    /**
     * {@code PATCH  /supplier-types/:id} : Partial updates given fields of an existing supplierType, field will ignore if it is null
     *
     * @param id the id of the supplierTypeDTO to save.
     * @param supplierTypeDTO the supplierTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated supplierTypeDTO,
     * or with status {@code 400 (Bad Request)} if the supplierTypeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the supplierTypeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the supplierTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<SupplierTypeDTO>> partialUpdateSupplierType(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody SupplierTypeDTO supplierTypeDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update SupplierType partially : {}, {}", id, supplierTypeDTO);
        if (supplierTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, supplierTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return supplierTypeRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<SupplierTypeDTO> result = supplierTypeService.partialUpdate(supplierTypeDTO);

                return result
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(res ->
                        ResponseEntity.ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, res.getId().toString()))
                            .body(res)
                    );
            });
    }

    /**
     * {@code GET  /supplier-types} : get all the supplierTypes.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of supplierTypes in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<ApiResponse<SupplierTypeDTO>>> getAllSupplierTypes(
        SupplierTypeCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        LOG.debug("REST request to get SupplierTypes by criteria: {}", criteria);
        return supplierTypeService
            .countByCriteria(criteria)
            .zipWith(supplierTypeService.findByCriteria(criteria, pageable).collectList())
            .map(countWithEntities ->
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
     * {@code GET  /supplier-types/count} : count all the supplierTypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public Mono<ResponseEntity<Long>> countSupplierTypes(SupplierTypeCriteria criteria) {
        LOG.debug("REST request to count SupplierTypes by criteria: {}", criteria);
        return supplierTypeService.countByCriteria(criteria).map(count -> ResponseEntity.status(HttpStatus.OK).body(count));
    }

    /**
     * {@code GET  /supplier-types/:id} : get the "id" supplierType.
     *
     * @param id the id of the supplierTypeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the supplierTypeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<SupplierTypeDTO>> getSupplierType(@PathVariable("id") UUID id) {
        LOG.debug("REST request to get SupplierType : {}", id);
        Mono<SupplierTypeDTO> supplierTypeDTO = supplierTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(supplierTypeDTO);
    }

    /**
     * {@code DELETE  /supplier-types/:id} : delete the "id" supplierType.
     *
     * @param id the id of the supplierTypeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteSupplierType(@PathVariable("id") UUID id) {
        LOG.debug("REST request to delete SupplierType : {}", id);
        return supplierTypeService
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
