package com.masi.logistics.web.rest;

import com.carevn.masi.dto.ApiResponse;
import com.masi.logistics.domain.criteria.InventoriesDetailCriteria;
import com.masi.logistics.repository.InventoriesDetailRepository;
import com.masi.logistics.service.InventoriesDetailService;
import com.masi.logistics.service.dto.InventoriesDetailDTO;
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
 * REST controller for managing {@link com.masi.logistics.domain.InventoriesDetail}.
 */
@RestController
@RequestMapping("/api/inventories-details")
public class InventoriesDetailResource {

    private static final Logger log = LoggerFactory.getLogger(InventoriesDetailResource.class);

    private static final String ENTITY_NAME = "masiLogisticsInventoriesDetail";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InventoriesDetailService inventoriesDetailService;

    private final InventoriesDetailRepository inventoriesDetailRepository;

    public InventoriesDetailResource(
        InventoriesDetailService inventoriesDetailService,
        InventoriesDetailRepository inventoriesDetailRepository
    ) {
        this.inventoriesDetailService = inventoriesDetailService;
        this.inventoriesDetailRepository = inventoriesDetailRepository;
    }

    /**
     * {@code POST  /inventories-details} : Create a new inventoriesDetail.
     *
     * @param inventoriesDetailDTO the inventoriesDetailDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new inventoriesDetailDTO, or with status {@code 400 (Bad Request)} if the inventoriesDetail has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<InventoriesDetailDTO>> createInventoriesDetail(
        @Valid @RequestBody InventoriesDetailDTO inventoriesDetailDTO
    ) throws URISyntaxException {
        log.debug("REST request to save InventoriesDetail : {}", inventoriesDetailDTO);

        inventoriesDetailDTO.setId(UUID.randomUUID());
        return inventoriesDetailService
            .save(inventoriesDetailDTO)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/inventories-details/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }


    /**
     * {@code PATCH  /inventories-details/:id} : Partial updates given fields of an existing inventoriesDetail, field will ignore if it is null
     *
     * @param id the id of the inventoriesDetailDTO to save.
     * @param inventoriesDetailDTO the inventoriesDetailDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated inventoriesDetailDTO,
     * or with status {@code 400 (Bad Request)} if the inventoriesDetailDTO is not valid,
     * or with status {@code 404 (Not Found)} if the inventoriesDetailDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the inventoriesDetailDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<InventoriesDetailDTO>> partialUpdateInventoriesDetail(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody InventoriesDetailDTO inventoriesDetailDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update InventoriesDetail partially : {}, {}", id, inventoriesDetailDTO);
        if (inventoriesDetailDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, inventoriesDetailDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return inventoriesDetailRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<InventoriesDetailDTO> result = inventoriesDetailService.partialUpdate(inventoriesDetailDTO);

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
     * {@code GET  /inventories-details} : get all the inventoriesDetails.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of inventoriesDetails in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<ApiResponse<InventoriesDetailDTO>>> getAllInventoriesDetails(
        InventoriesDetailCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get InventoriesDetails by criteria: {}", criteria);
        return inventoriesDetailService
            .countByCriteria(criteria)
            .zipWith(inventoriesDetailService.findByCriteria(criteria, pageable).collectList())
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
     * {@code GET  /inventories-details/count} : count all the inventoriesDetails.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public Mono<ResponseEntity<Long>> countInventoriesDetails(InventoriesDetailCriteria criteria) {
        log.debug("REST request to count InventoriesDetails by criteria: {}", criteria);
        return inventoriesDetailService.countByCriteria(criteria).map(count -> ResponseEntity.status(HttpStatus.OK).body(count));
    }

    /**
     * {@code GET  /inventories-details/:id} : get the "id" inventoriesDetail.
     *
     * @param id the id of the inventoriesDetailDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the inventoriesDetailDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<InventoriesDetailDTO>> getInventoriesDetail(@PathVariable("id") UUID id) {
        log.debug("REST request to get InventoriesDetail : {}", id);
        Mono<InventoriesDetailDTO> inventoriesDetailDTO = inventoriesDetailService.findOne(id);
        return ResponseUtil.wrapOrNotFound(inventoriesDetailDTO);
    }

    /**
     * {@code DELETE  /inventories-details/:id} : delete the "id" inventoriesDetail.
     *
     * @param id the id of the inventoriesDetailDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteInventoriesDetail(@PathVariable("id") UUID id) {
        log.debug("REST request to delete InventoriesDetail : {}", id);
        return inventoriesDetailService
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
