package com.masi.production.web.rest;

import com.carevn.masi.dto.ApiResponse;
import com.carevn.masi.utils.SecurityUtils;
import com.carevn.masi.utils.Utilities;
import com.masi.production.repository.ProductMaintainRepository;
import com.masi.production.service.ProductMaintainService;
import com.masi.production.service.dto.ProductMaintainDTO;
import com.masi.production.service.dto.ProductMaintainQuery;
import com.masi.production.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link com.masi.production.domain.ProductMaintain}.
 */
@RestController
@RequestMapping("/api/product-maintains")
public class ProductMaintainResource {

    private final Logger log = LoggerFactory.getLogger(ProductMaintainResource.class);

    private static final String ENTITY_NAME = "masiProductionProductMaintain";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProductMaintainService productMaintainService;

    private final ProductMaintainRepository productMaintainRepository;

    public ProductMaintainResource(ProductMaintainService productMaintainService, ProductMaintainRepository productMaintainRepository) {
        this.productMaintainService = productMaintainService;
        this.productMaintainRepository = productMaintainRepository;
    }

    /**
     * {@code POST  /product-maintains} : Create a new productMaintain.
     *
     * @param productMaintainDTO the productMaintainDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new productMaintainDTO, or with status {@code 400 (Bad Request)} if the productMaintain has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<ProductMaintainDTO>> createProductMaintain(@Valid @RequestBody ProductMaintainDTO productMaintainDTO) {
        log.debug("REST request to save ProductMaintain : {}", productMaintainDTO);
        productMaintainDTO.setId(UUID.randomUUID());

        return productMaintainService.save(productMaintainDTO)
                .map(savedProduct -> {
                    try {
                        URI location = new URI("/api/ProductMaintainDTO/" + savedProduct.getId());
                        return ResponseEntity.created(location)
                                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, savedProduct.getId().toString()))
                                .body(savedProduct);
                    } catch (URISyntaxException e) {
                        throw new RuntimeException("Failed to create URI", e);
                    }
                });
    }


    /**
     * {@code PATCH  /product-maintains/:id} : Partial updates given fields of an existing productMaintain, field will ignore if it is null
     *
     * @param id                 the id of the productMaintainDTO to save.
     * @param productMaintainDTO the productMaintainDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productMaintainDTO,
     * or with status {@code 400 (Bad Request)} if the productMaintainDTO is not valid,
     * or with status {@code 404 (Not Found)} if the productMaintainDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the productMaintainDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = {"application/json", "application/merge-patch+json"})
    public Mono<ResponseEntity<Map>> partialUpdateProductMaintain(
            @PathVariable(value = "id", required = false) final UUID id,
            @NotNull @RequestBody ProductMaintainDTO productMaintainDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ProductMaintain partially : {}, {}", id, productMaintainDTO);
        productMaintainDTO.setId(id);
        return productMaintainRepository
                .existsById(id)
                .flatMap(exists -> {
                    if (!exists) {
                        return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                    }
                    Mono<ProductMaintainDTO> result = productMaintainService.partialUpdate(productMaintainDTO);

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
     * {@code GET  /product-maintains} : get all the productMaintains.
     *
     * @param pageable the pagination information.
     * @param request  a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of productMaintains in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<ApiResponse<ProductMaintainDTO>>> getAllProductMaintains(
            @org.springdoc.core.annotations.ParameterObject ProductMaintainQuery filter,
            @org.springdoc.core.annotations.ParameterObject Pageable pageable,
            ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of ProductMaintain");
        return SecurityUtils.getUserJWTDetail().flatMap(user -> {
            filter.setCompany(String.valueOf(user.getCompanyId()));
            filter.setDepartment(user.getGroupId().toString());
            return productMaintainService
                    .countAllByQuery(filter)
                    .zipWith(productMaintainService.findAllByQuery(filter, pageable).collectList())
                    .map(countWithEntities -> ResponseEntity.ok().body(new ApiResponse<>(countWithEntities.getT2(), countWithEntities.getT1())));

        });

    }


    /**
     * {@code GET  /product-maintains/:id} : get the "id" productMaintain.
     *
     * @param id the id of the productMaintainDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the productMaintainDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<ProductMaintainDTO>> getProductMaintain(@PathVariable("id") UUID id) {
        log.debug("REST request to get ProductMaintain : {}", id);
        Mono<ProductMaintainDTO> productMaintainDTO = productMaintainService.findOne(id);
        return ResponseUtil.wrapOrNotFound(productMaintainDTO);
    }


    /**
     * {@code DELETE  /product-maintains/:id} : delete the "id" productMaintain.
     *
     * @param id the id of the productMaintainDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteProductMaintain(@PathVariable("id") UUID id) {
        log.debug("REST request to delete ProductMaintain : {}", id);
        return productMaintainService
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
