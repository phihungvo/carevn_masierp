package com.masi.logistics.web.rest;

import com.carevn.masi.dto.ApiResponse;
import com.carevn.masi.utils.SecurityUtils;
import com.masi.logistics.domain.criteria.WarehouseCriteria;
import com.masi.logistics.repository.WarehouseRepository;
import com.masi.logistics.service.WarehouseService;
import com.masi.logistics.service.dto.WarehouseDTO;
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
 * REST controller for managing {@link com.masi.logistics.domain.Warehouse}.
 */
@RestController
@RequestMapping("/api/warehouses")
public class WarehouseResource {

    private static final Logger log = LoggerFactory.getLogger(WarehouseResource.class);

    private static final String ENTITY_NAME = "masiLogisticsWarehouse";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WarehouseService warehouseService;

    private final WarehouseRepository warehouseRepository;

    public WarehouseResource(WarehouseService warehouseService, WarehouseRepository warehouseRepository) {
        this.warehouseService = warehouseService;
        this.warehouseRepository = warehouseRepository;
    }

    /**
     * {@code POST  /warehouses} : Create a new warehouse.
     *
     * @param warehouseDTO the warehouseDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new warehouseDTO, or with status {@code 400 (Bad Request)} if the warehouse has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<WarehouseDTO>> createWarehouse(@Valid @RequestBody WarehouseDTO warehouseDTO) throws URISyntaxException {
        log.debug("REST request to save Warehouse : {}", warehouseDTO);
        if (warehouseDTO.getId() != null) {
            throw new BadRequestAlertException("A new warehouse cannot already have an ID", ENTITY_NAME, "idexists");
        }
        warehouseDTO.setId(UUID.randomUUID());
        return warehouseService
            .save(warehouseDTO)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/warehouses/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /warehouses/:id} : Updates an existing warehouse.
     *
     * @param id the id of the warehouseDTO to save.
     * @param warehouseDTO the warehouseDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated warehouseDTO,
     * or with status {@code 400 (Bad Request)} if the warehouseDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the warehouseDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<WarehouseDTO>> updateWarehouse(
        @PathVariable(value = "id", required = false) final UUID id,
        @Valid @RequestBody WarehouseDTO warehouseDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Warehouse : {}, {}", id, warehouseDTO);
        if (warehouseDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, warehouseDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return warehouseRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return warehouseService
                    .update(warehouseDTO)
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
     * {@code PATCH  /warehouses/:id} : Partial updates given fields of an existing warehouse, field will ignore if it is null
     *
     * @param id the id of the warehouseDTO to save.
     * @param warehouseDTO the warehouseDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated warehouseDTO,
     * or with status {@code 400 (Bad Request)} if the warehouseDTO is not valid,
     * or with status {@code 404 (Not Found)} if the warehouseDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the warehouseDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<WarehouseDTO>> partialUpdateWarehouse(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody WarehouseDTO warehouseDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Warehouse partially : {}, {}", id, warehouseDTO);
        warehouseDTO.setId(id);
        return warehouseRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<WarehouseDTO> result = warehouseService.partialUpdate(warehouseDTO);

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
     * {@code GET  /warehouses} : get all the warehouses.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of warehouses in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<ApiResponse<WarehouseDTO>>> getAllWarehouses(
        WarehouseCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get Warehouses by criteria: {}", criteria);
        return warehouseService
            .countByCriteria(criteria)
            .zipWith(warehouseService.findByCriteria(criteria, pageable).collectList())
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
     * {@code GET  /warehouses} : get all the warehouses.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of warehouses in body.
     */
    @GetMapping(value = "/routing", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<ApiResponse<WarehouseDTO>>> getRoutingWarehouse(
        @RequestParam(required = false)  UUID orderId,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get Warehouses by: {}", orderId);
        return SecurityUtils.getUserJWTDetail().flatMap(user -> {
            return warehouseService
                .countByItemCategory(user.getCompanyId(), orderId)
                .zipWith(warehouseService.findByAndItemCategory(user.getCompanyId(), pageable, orderId).collectList())
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
        });
    }

    /**
     * {@code GET  /warehouses/count} : count all the warehouses.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public Mono<ResponseEntity<Long>> countWarehouses(WarehouseCriteria criteria) {
        log.debug("REST request to count Warehouses by criteria: {}", criteria);
        return warehouseService.countByCriteria(criteria).map(count -> ResponseEntity.status(HttpStatus.OK).body(count));
    }

    /**
     * {@code GET  /warehouses/:id} : get the "id" warehouse.
     *
     * @param id the id of the warehouseDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the warehouseDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<WarehouseDTO>> getWarehouse(@PathVariable("id") UUID id) {
        log.debug("REST request to get Warehouse : {}", id);
        Mono<WarehouseDTO> warehouseDTO = warehouseService.findOne(id);
        return ResponseUtil.wrapOrNotFound(warehouseDTO);
    }

    /**
     * {@code DELETE  /warehouses/:id} : delete the "id" warehouse.
     *
     * @param id the id of the warehouseDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteWarehouse(@PathVariable("id") UUID id) {
        log.debug("REST request to delete Warehouse : {}", id);
        return warehouseService
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
