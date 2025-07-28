package com.masi.logistics.web.rest;

import com.carevn.masi.dto.ApiResponse;
import com.masi.logistics.domain.criteria.WarehouseTypeCriteria;
import com.masi.logistics.repository.WarehouseTypeRepository;
import com.masi.logistics.service.WarehouseTypeService;
import com.masi.logistics.service.dto.WarehouseTypeDTO;
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
 * REST controller for managing {@link com.masi.logistics.domain.WarehouseType}.
 */
@RestController
@RequestMapping("/api/warehouse-types")
public class WarehouseTypeResource {

    private static final Logger log = LoggerFactory.getLogger(WarehouseTypeResource.class);

    private static final String ENTITY_NAME = "masiLogisticsWarehouseType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WarehouseTypeService warehouseTypeService;

    private final WarehouseTypeRepository warehouseTypeRepository;

    public WarehouseTypeResource(WarehouseTypeService warehouseTypeService, WarehouseTypeRepository warehouseTypeRepository) {
        this.warehouseTypeService = warehouseTypeService;
        this.warehouseTypeRepository = warehouseTypeRepository;
    }

    /**
     * {@code POST  /warehouse-types} : Create a new warehouseType.
     *
     * @param warehouseTypeDTO the warehouseTypeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new warehouseTypeDTO, or with status {@code 400 (Bad Request)} if the warehouseType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<WarehouseTypeDTO>> createWarehouseType(@Valid @RequestBody WarehouseTypeDTO warehouseTypeDTO)
        throws URISyntaxException {
        log.debug("REST request to save WarehouseType : {}", warehouseTypeDTO);
        if (warehouseTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new warehouseType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return warehouseTypeService
            .save(warehouseTypeDTO)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/warehouse-types/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /warehouse-types/:id} : Updates an existing warehouseType.
     *
     * @param id the id of the warehouseTypeDTO to save.
     * @param warehouseTypeDTO the warehouseTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated warehouseTypeDTO,
     * or with status {@code 400 (Bad Request)} if the warehouseTypeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the warehouseTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<WarehouseTypeDTO>> updateWarehouseType(
        @PathVariable(value = "id", required = false) final UUID id,
        @Valid @RequestBody WarehouseTypeDTO warehouseTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update WarehouseType : {}, {}", id, warehouseTypeDTO);
        if (warehouseTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, warehouseTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return warehouseTypeRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return warehouseTypeService
                    .update(warehouseTypeDTO)
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
     * {@code PATCH  /warehouse-types/:id} : Partial updates given fields of an existing warehouseType, field will ignore if it is null
     *
     * @param id the id of the warehouseTypeDTO to save.
     * @param warehouseTypeDTO the warehouseTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated warehouseTypeDTO,
     * or with status {@code 400 (Bad Request)} if the warehouseTypeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the warehouseTypeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the warehouseTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<WarehouseTypeDTO>> partialUpdateWarehouseType(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody WarehouseTypeDTO warehouseTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update WarehouseType partially : {}, {}", id, warehouseTypeDTO);
        warehouseTypeDTO.setId(id);
        return warehouseTypeRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<WarehouseTypeDTO> result = warehouseTypeService.partialUpdate(warehouseTypeDTO);

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
     * {@code GET  /warehouse-types} : get all the warehouseTypes.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of warehouseTypes in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<ApiResponse<WarehouseTypeDTO>>> getAllWarehouseTypes(
        WarehouseTypeCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get WarehouseTypes by criteria: {}", criteria);
        return warehouseTypeService
            .countByCriteria(criteria)
            .zipWith(warehouseTypeService.findByCriteria(criteria, pageable).collectList())
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
     * {@code GET  /warehouse-types/count} : count all the warehouseTypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public Mono<ResponseEntity<Long>> countWarehouseTypes(WarehouseTypeCriteria criteria) {
        log.debug("REST request to count WarehouseTypes by criteria: {}", criteria);
        return warehouseTypeService.countByCriteria(criteria).map(count -> ResponseEntity.status(HttpStatus.OK).body(count));
    }

    /**
     * {@code GET  /warehouse-types/:id} : get the "id" warehouseType.
     *
     * @param id the id of the warehouseTypeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the warehouseTypeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<WarehouseTypeDTO>> getWarehouseType(@PathVariable("id") UUID id) {
        log.debug("REST request to get WarehouseType : {}", id);
        Mono<WarehouseTypeDTO> warehouseTypeDTO = warehouseTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(warehouseTypeDTO);
    }

    /**
     * {@code DELETE  /warehouse-types/:id} : delete the "id" warehouseType.
     *
     * @param id the id of the warehouseTypeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteWarehouseType(@PathVariable("id") UUID id) {
        log.debug("REST request to delete WarehouseType : {}", id);
        return warehouseTypeService
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
