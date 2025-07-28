package com.masi.logistics.web.rest;

import com.masi.logistics.domain.criteria.WarehouseRoleCriteria;
import com.masi.logistics.repository.WarehouseRoleRepository;
import com.masi.logistics.service.WarehouseRoleService;
import com.masi.logistics.service.dto.WarehouseRoleDTO;
import com.masi.logistics.web.rest.errors.BadRequestAlertException;
import io.swagger.v3.oas.annotations.Operation;
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
 * REST controller for managing {@link com.masi.logistics.domain.WarehouseRole}.
 */
@RestController
@RequestMapping("/api/warehouse-roles")
public class WarehouseRoleResource {

    private static final Logger log = LoggerFactory.getLogger(WarehouseRoleResource.class);

    private static final String ENTITY_NAME = "masiLogisticsWarehouseRole";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WarehouseRoleService warehouseRoleService;

    private final WarehouseRoleRepository warehouseRoleRepository;

    public WarehouseRoleResource(WarehouseRoleService warehouseRoleService, WarehouseRoleRepository warehouseRoleRepository) {
        this.warehouseRoleService = warehouseRoleService;
        this.warehouseRoleRepository = warehouseRoleRepository;
    }

    /**
     * {@code POST  /warehouse-roles} : Create a new warehouseRole.
     *
     * @param warehouseRoleDTO the warehouseRoleDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new warehouseRoleDTO, or with status {@code 400 (Bad Request)} if the warehouseRole has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Operation(summary = "PERMISSION.WAREHOUSE_ROLE.CREATE", description = "Quyền tạo mơí chức năng giám sát kho")
    @PostMapping("")
    public Mono<ResponseEntity<WarehouseRoleDTO>> createWarehouseRole(@Valid @RequestBody WarehouseRoleDTO warehouseRoleDTO)
        throws URISyntaxException {
        log.debug("REST request to save WarehouseRole : {}", warehouseRoleDTO);
        if (warehouseRoleDTO.getId() != null) {
            throw new BadRequestAlertException("A new warehouseRole cannot already have an ID", ENTITY_NAME, "idexists");
        }
        warehouseRoleDTO.setId(UUID.randomUUID());
        return warehouseRoleService
            .save(warehouseRoleDTO)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/warehouse-roles/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /warehouse-roles/:id} : Updates an existing warehouseRole.
     *
     * @param id the id of the warehouseRoleDTO to save.
     * @param warehouseRoleDTO the warehouseRoleDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated warehouseRoleDTO,
     * or with status {@code 400 (Bad Request)} if the warehouseRoleDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the warehouseRoleDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<WarehouseRoleDTO>> updateWarehouseRole(
        @PathVariable(value = "id", required = false) final UUID id,
        @Valid @RequestBody WarehouseRoleDTO warehouseRoleDTO
    ) throws URISyntaxException {
        log.debug("REST request to update WarehouseRole : {}, {}", id, warehouseRoleDTO);
        if (warehouseRoleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, warehouseRoleDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return warehouseRoleRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return warehouseRoleService
                    .update(warehouseRoleDTO)
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
     * {@code PATCH  /warehouse-roles/:id} : Partial updates given fields of an existing warehouseRole, field will ignore if it is null
     *
     * @param id the id of the warehouseRoleDTO to save.
     * @param warehouseRoleDTO the warehouseRoleDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated warehouseRoleDTO,
     * or with status {@code 400 (Bad Request)} if the warehouseRoleDTO is not valid,
     * or with status {@code 404 (Not Found)} if the warehouseRoleDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the warehouseRoleDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<WarehouseRoleDTO>> partialUpdateWarehouseRole(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody WarehouseRoleDTO warehouseRoleDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update WarehouseRole partially : {}, {}", id, warehouseRoleDTO);
        if (warehouseRoleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, warehouseRoleDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return warehouseRoleRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<WarehouseRoleDTO> result = warehouseRoleService.partialUpdate(warehouseRoleDTO);

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
     * {@code GET  /warehouse-roles} : get all the warehouseRoles.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of warehouseRoles in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<WarehouseRoleDTO>>> getAllWarehouseRoles(
        WarehouseRoleCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get WarehouseRoles by criteria: {}", criteria);
        return warehouseRoleService
            .countByCriteria(criteria)
            .zipWith(warehouseRoleService.findByCriteria(criteria, pageable).collectList())
            .map(
                countWithEntities ->
                    ResponseEntity.ok()
                        .headers(
                            PaginationUtil.generatePaginationHttpHeaders(
                                ForwardedHeaderUtils.adaptFromForwardedHeaders(request.getURI(), request.getHeaders()),
                                new PageImpl<>(countWithEntities.getT2(), pageable, countWithEntities.getT1())
                            )
                        )
                        .body(countWithEntities.getT2())
            );
    }

    /**
     * {@code GET  /warehouse-roles/count} : count all the warehouseRoles.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public Mono<ResponseEntity<Long>> countWarehouseRoles(WarehouseRoleCriteria criteria) {
        log.debug("REST request to count WarehouseRoles by criteria: {}", criteria);
        return warehouseRoleService.countByCriteria(criteria).map(count -> ResponseEntity.status(HttpStatus.OK).body(count));
    }

    /**
     * {@code GET  /warehouse-roles/:id} : get the "id" warehouseRole.
     *
     * @param id the id of the warehouseRoleDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the warehouseRoleDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<WarehouseRoleDTO>> getWarehouseRole(@PathVariable("id") UUID id) {
        log.debug("REST request to get WarehouseRole : {}", id);
        Mono<WarehouseRoleDTO> warehouseRoleDTO = warehouseRoleService.findOne(id);
        return ResponseUtil.wrapOrNotFound(warehouseRoleDTO);
    }

    /**
     * {@code DELETE  /warehouse-roles/:id} : delete the "id" warehouseRole.
     *
     * @param id the id of the warehouseRoleDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteWarehouseRole(@PathVariable("id") UUID id) {
        log.debug("REST request to delete WarehouseRole : {}", id);
        return warehouseRoleService
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
