package com.masi.logistics.web.rest;

import com.masi.logistics.domain.criteria.SupplierDetailCriteria;
import com.masi.logistics.repository.SupplierDetailRepository;
import com.masi.logistics.service.SupplierDetailService;
import com.masi.logistics.service.dto.SupplierDetailDTO;
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
 * REST controller for managing {@link com.masi.logistics.domain.SupplierDetail}.
 */
@RestController
@RequestMapping("/api/supplier-details")
public class SupplierDetailResource {

    private static final Logger log = LoggerFactory.getLogger(SupplierDetailResource.class);

    private static final String ENTITY_NAME = "masiLogisticsSupplierDetail";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SupplierDetailService supplierDetailService;

    private final SupplierDetailRepository supplierDetailRepository;

    public SupplierDetailResource(SupplierDetailService supplierDetailService, SupplierDetailRepository supplierDetailRepository) {
        this.supplierDetailService = supplierDetailService;
        this.supplierDetailRepository = supplierDetailRepository;
    }

    /**
     * {@code POST  /supplier-details} : Create a new supplierDetail.
     *
     * @param supplierDetailDTO the supplierDetailDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new supplierDetailDTO, or with status {@code 400 (Bad Request)} if the supplierDetail has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<SupplierDetailDTO>> createSupplierDetail(@Valid @RequestBody SupplierDetailDTO supplierDetailDTO)
        throws URISyntaxException {
        log.debug("REST request to save SupplierDetail : {}", supplierDetailDTO);

        return supplierDetailService
            .save(supplierDetailDTO)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/supplier-details/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /supplier-details/:id} : Updates an existing supplierDetail.
     *
     * @param id the id of the supplierDetailDTO to save.
     * @param supplierDetailDTO the supplierDetailDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated supplierDetailDTO,
     * or with status {@code 400 (Bad Request)} if the supplierDetailDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the supplierDetailDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<SupplierDetailDTO>> updateSupplierDetail(
        @PathVariable(value = "id", required = false) final UUID id,
        @Valid @RequestBody SupplierDetailDTO supplierDetailDTO
    ) throws URISyntaxException {
        log.debug("REST request to update SupplierDetail : {}, {}", id, supplierDetailDTO);
        if (supplierDetailDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, supplierDetailDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return supplierDetailRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return supplierDetailService
                    .update(supplierDetailDTO)
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
     * {@code PATCH  /supplier-details/:id} : Partial updates given fields of an existing supplierDetail, field will ignore if it is null
     *
     * @param id the id of the supplierDetailDTO to save.
     * @param supplierDetailDTO the supplierDetailDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated supplierDetailDTO,
     * or with status {@code 400 (Bad Request)} if the supplierDetailDTO is not valid,
     * or with status {@code 404 (Not Found)} if the supplierDetailDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the supplierDetailDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<SupplierDetailDTO>> partialUpdateSupplierDetail(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody SupplierDetailDTO supplierDetailDTO
    ) throws URISyntaxException {

        return supplierDetailRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<SupplierDetailDTO> result = supplierDetailService.partialUpdate(supplierDetailDTO);

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
     * {@code GET  /supplier-details} : get all the supplierDetails.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of supplierDetails in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<SupplierDetailDTO>>> getAllSupplierDetails(
        SupplierDetailCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get SupplierDetails by criteria: {}", criteria);
        return supplierDetailService
            .countByCriteria(criteria)
            .zipWith(supplierDetailService.findByCriteria(criteria, pageable).collectList())
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
     * {@code GET  /supplier-details/count} : count all the supplierDetails.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public Mono<ResponseEntity<Long>> countSupplierDetails(SupplierDetailCriteria criteria) {
        log.debug("REST request to count SupplierDetails by criteria: {}", criteria);
        return supplierDetailService.countByCriteria(criteria).map(count -> ResponseEntity.status(HttpStatus.OK).body(count));
    }

    /**
     * {@code GET  /supplier-details/:id} : get the "id" supplierDetail.
     *
     * @param id the id of the supplierDetailDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the supplierDetailDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<SupplierDetailDTO>> getSupplierDetail(@PathVariable("id") UUID id) {
        log.debug("REST request to get SupplierDetail : {}", id);
        Mono<SupplierDetailDTO> supplierDetailDTO = supplierDetailService.findOne(id);
        return ResponseUtil.wrapOrNotFound(supplierDetailDTO);
    }

    /**
     * {@code DELETE  /supplier-details/:id} : delete the "id" supplierDetail.
     *
     * @param id the id of the supplierDetailDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteSupplierDetail(@PathVariable("id") UUID id) {
        log.debug("REST request to delete SupplierDetail : {}", id);
        return supplierDetailService
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
