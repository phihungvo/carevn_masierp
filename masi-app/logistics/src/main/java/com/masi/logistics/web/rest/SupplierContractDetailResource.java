package com.masi.logistics.web.rest;

import com.masi.logistics.domain.criteria.SupplierContractDetailCriteria;
import com.masi.logistics.repository.SupplierContractDetailRepository;
import com.masi.logistics.service.SupplierContractDetailService;
import com.masi.logistics.service.dto.SupplierContractDetailDTO;
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
 * REST controller for managing {@link com.masi.logistics.domain.SupplierContractDetail}.
 */
@RestController
@RequestMapping("/api/supplier-contract-details")
public class SupplierContractDetailResource {

    private static final Logger LOG = LoggerFactory.getLogger(SupplierContractDetailResource.class);

    private static final String ENTITY_NAME = "masiLogisticsSupplierContractDetail";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SupplierContractDetailService supplierContractDetailService;

    private final SupplierContractDetailRepository supplierContractDetailRepository;

    public SupplierContractDetailResource(
        SupplierContractDetailService supplierContractDetailService,
        SupplierContractDetailRepository supplierContractDetailRepository
    ) {
        this.supplierContractDetailService = supplierContractDetailService;
        this.supplierContractDetailRepository = supplierContractDetailRepository;
    }

    /**
     * {@code POST  /supplier-contract-details} : Create a new supplierContractDetail.
     *
     * @param supplierContractDetailDTO the supplierContractDetailDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new supplierContractDetailDTO, or with status {@code 400 (Bad Request)} if the supplierContractDetail has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<SupplierContractDetailDTO>> createSupplierContractDetail(
        @Valid @RequestBody SupplierContractDetailDTO supplierContractDetailDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to save SupplierContractDetail : {}", supplierContractDetailDTO);
        if (supplierContractDetailDTO.getId() != null) {
            throw new BadRequestAlertException("A new supplierContractDetail cannot already have an ID", ENTITY_NAME, "idexists");
        }
        supplierContractDetailDTO.setId(UUID.randomUUID());
        return supplierContractDetailService
            .save(supplierContractDetailDTO)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/supplier-contract-details/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /supplier-contract-details/:id} : Updates an existing supplierContractDetail.
     *
     * @param id the id of the supplierContractDetailDTO to save.
     * @param supplierContractDetailDTO the supplierContractDetailDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated supplierContractDetailDTO,
     * or with status {@code 400 (Bad Request)} if the supplierContractDetailDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the supplierContractDetailDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<SupplierContractDetailDTO>> updateSupplierContractDetail(
        @PathVariable(value = "id", required = false) final UUID id,
        @Valid @RequestBody SupplierContractDetailDTO supplierContractDetailDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update SupplierContractDetail : {}, {}", id, supplierContractDetailDTO);
        if (supplierContractDetailDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, supplierContractDetailDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return supplierContractDetailRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return supplierContractDetailService
                    .update(supplierContractDetailDTO)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(result ->
                        ResponseEntity.ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                            .body(result)
                    );
            });
    }

    /**
     * {@code PATCH  /supplier-contract-details/:id} : Partial updates given fields of an existing supplierContractDetail, field will ignore if it is null
     *
     * @param id the id of the supplierContractDetailDTO to save.
     * @param supplierContractDetailDTO the supplierContractDetailDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated supplierContractDetailDTO,
     * or with status {@code 400 (Bad Request)} if the supplierContractDetailDTO is not valid,
     * or with status {@code 404 (Not Found)} if the supplierContractDetailDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the supplierContractDetailDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<SupplierContractDetailDTO>> partialUpdateSupplierContractDetail(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody SupplierContractDetailDTO supplierContractDetailDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update SupplierContractDetail partially : {}, {}", id, supplierContractDetailDTO);
        if (supplierContractDetailDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, supplierContractDetailDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return supplierContractDetailRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<SupplierContractDetailDTO> result = supplierContractDetailService.partialUpdate(supplierContractDetailDTO);

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
     * {@code GET  /supplier-contract-details} : get all the supplierContractDetails.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of supplierContractDetails in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<SupplierContractDetailDTO>>> getAllSupplierContractDetails(
        SupplierContractDetailCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        LOG.debug("REST request to get SupplierContractDetails by criteria: {}", criteria);
        return supplierContractDetailService
            .countByCriteria(criteria)
            .zipWith(supplierContractDetailService.findByCriteria(criteria, pageable).collectList())
            .map(countWithEntities ->
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
     * {@code GET  /supplier-contract-details/count} : count all the supplierContractDetails.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public Mono<ResponseEntity<Long>> countSupplierContractDetails(SupplierContractDetailCriteria criteria) {
        LOG.debug("REST request to count SupplierContractDetails by criteria: {}", criteria);
        return supplierContractDetailService.countByCriteria(criteria).map(count -> ResponseEntity.status(HttpStatus.OK).body(count));
    }

    /**
     * {@code GET  /supplier-contract-details/:id} : get the "id" supplierContractDetail.
     *
     * @param id the id of the supplierContractDetailDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the supplierContractDetailDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<SupplierContractDetailDTO>> getSupplierContractDetail(@PathVariable("id") UUID id) {
        LOG.debug("REST request to get SupplierContractDetail : {}", id);
        Mono<SupplierContractDetailDTO> supplierContractDetailDTO = supplierContractDetailService.findOne(id);
        return ResponseUtil.wrapOrNotFound(supplierContractDetailDTO);
    }

    /**
     * {@code DELETE  /supplier-contract-details/:id} : delete the "id" supplierContractDetail.
     *
     * @param id the id of the supplierContractDetailDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteSupplierContractDetail(@PathVariable("id") UUID id) {
        LOG.debug("REST request to delete SupplierContractDetail : {}", id);
        return supplierContractDetailService
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
