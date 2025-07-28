package com.masi.production.web.rest;

import com.carevn.masi.dto.ApiResponse;
import com.carevn.masi.utils.SecurityUtils;
import com.carevn.masi.utils.Utilities;
import com.masi.production.repository.ProductRoutingRepository;
import com.masi.production.service.ProductRoutingService;
import com.masi.production.service.dto.ProductRoutingDTO;
import com.masi.production.service.dto.ProductRoutingQuery;
import com.masi.production.service.dto.QualityCheckSampleDTO;
import com.masi.production.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

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
 * REST controller for managing {@link com.masi.production.domain.ProductRouting}.
 */
@RestController
@RequestMapping("/api/product-routings")
public class ProductRoutingResource {

    private final Logger log = LoggerFactory.getLogger(ProductRoutingResource.class);

    private static final String ENTITY_NAME = "masiProductionProductRouting";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProductRoutingService productRoutingService;

    private final ProductRoutingRepository productRoutingRepository;

    public ProductRoutingResource(ProductRoutingService productRoutingService, ProductRoutingRepository productRoutingRepository) {
        this.productRoutingService = productRoutingService;
        this.productRoutingRepository = productRoutingRepository;
    }

    /**
     * {@code POST  /product-routings} : Create a new productRouting.
     *
     * @param productRoutingDTO the productRoutingDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new productRoutingDTO, or with status {@code 400 (Bad Request)} if the productRouting has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<Map<String, Object>>> createProductRouting(@Valid @RequestBody ProductRoutingDTO productRoutingDTO) throws URISyntaxException {
        log.debug("REST request to save ProductRouting : {}", productRoutingDTO);
        productRoutingDTO.setId(UUID.randomUUID());

        return productRoutingService.save(productRoutingDTO)
            .map(savedProduct -> {
                try {
                    URI location = new URI("/api/product-routings/" + savedProduct.getId());
                    return ResponseEntity.created(location)
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, savedProduct.getId().toString()))
                        .body(Utilities.generateResponse("success", savedProduct));
                } catch (URISyntaxException e) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
                }
            });
    }



    /**
     * {@code PATCH  /product-routings/:id} : Partial updates given fields of an existing productRouting, field will ignore if it is null
     *
     * @param id the id of the productRoutingDTO to save.
     * @param productRoutingDTO the productRoutingDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productRoutingDTO,
     * or with status {@code 400 (Bad Request)} if the productRoutingDTO is not valid,
     * or with status {@code 404 (Not Found)} if the productRoutingDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the productRoutingDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<Map>> partialUpdateProductRouting(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody ProductRoutingDTO productRoutingDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ProductRouting partially : {}, {}", id, productRoutingDTO);
        productRoutingDTO.setId(id);
        return productRoutingRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }
                Mono<ProductRoutingDTO> result = productRoutingService.partialUpdate(productRoutingDTO);

                return result
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(
                        res ->
                            ResponseEntity.ok()
                                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, res.getId().toString()))
                                .body(Utilities.generateResponse("success", res))
                    );
            });
    }



    /**
     * {@code GET  /product-routings} : get all the productRoutings.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of productRoutings in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<ApiResponse<ProductRoutingDTO>>> getAllProductRoutings(
        @org.springdoc.core.annotations.ParameterObject ProductRoutingQuery filter,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.info("REST request to get a page of ProductRoutings");

        return SecurityUtils.getUserJWTDetail().flatMap(user -> {
            filter.setCompany(String.valueOf(user.getCompanyId()));
            filter.setDepartment(user.getGroupId().toString());
            return productRoutingService
                .countAllByQuery(filter)
                .zipWith(productRoutingService.findAllByQuery(filter,pageable).collectList())
                .map(countWithEntities -> ResponseEntity.ok().body(new ApiResponse<>(countWithEntities.getT2(), countWithEntities.getT1())));
        });


    }


    /**
     * {@code DELETE  /product-routings/:id} : delete the "id" productRouting.
     *
     * @param id the id of the productRoutingDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteProductRouting(@PathVariable("id") UUID id) {
        log.debug("REST request to delete ProductRouting : {}", id);
        return productRoutingService
            .delete(id)
            .then(
                Mono.just(
                    ResponseEntity.ok()
                        .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                        .build()
                )
            );
    }


    @GetMapping("/{id}")
    public Mono<ResponseEntity<ProductRoutingDTO>> getQualityCheckSample(@PathVariable("id") UUID id) {
        log.debug("REST request to get QualityCheckSample : {}", id);
        Mono<ProductRoutingDTO> productRoutingDTO = productRoutingService.findOne(id);
        return ResponseUtil.wrapOrNotFound(productRoutingDTO);
    }
}
