package com.masi.production.web.rest;

import com.carevn.masi.dto.ApiResponse;
import com.carevn.masi.utils.SecurityUtils;
import com.masi.production.repository.ProductPackageRepository;
import com.masi.production.service.ProductPackageService;
import com.masi.production.service.dto.ProductPackageDTO;
import com.masi.production.service.dto.ProductPackageQuery;
import com.masi.production.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link com.masi.production.domain.ProductPackage}.
 */
@RestController
@RequestMapping("/api/product-packages")
public class ProductPackageResource {

    private final Logger log = LoggerFactory.getLogger(ProductPackageResource.class);

    private static final String ENTITY_NAME = "masiProductionProductPackage";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProductPackageService productPackageService;

    private final ProductPackageRepository productPackageRepository;

    public ProductPackageResource(ProductPackageService productPackageService, ProductPackageRepository productPackageRepository) {
        this.productPackageService = productPackageService;
        this.productPackageRepository = productPackageRepository;
    }

    /**
     * {@code POST  /product-packages} : Create a new productPackage.
     *
     * @param productPackageDTO the productPackageDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new productPackageDTO, or with status {@code 400 (Bad Request)} if the productPackage has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<ProductPackageDTO>> createProductPackage(@Valid @RequestBody ProductPackageDTO productPackageDTO)
            throws URISyntaxException {
        log.debug("REST request to save ProductPackage : {}", productPackageDTO);
        productPackageDTO.setId(UUID.randomUUID());
        return productPackageService
                .save(productPackageDTO)
                .map(result -> {
                    try {
                        return ResponseEntity.created(new URI("/api/product-packages/" + result.getId()))
                                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                                .body(result);
                    } catch (URISyntaxException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    /**
     * {@code PATCH  /product-packages/:id} : Partial updates given fields of an existing productPackage, field will ignore if it is null
     *
     * @param id                the id of the productPackageDTO to save.
     * @param productPackageDTO the productPackageDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productPackageDTO,
     * or with status {@code 400 (Bad Request)} if the productPackageDTO is not valid,
     * or with status {@code 404 (Not Found)} if the productPackageDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the productPackageDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = {"application/json", "application/merge-patch+json"})
    public Mono<ResponseEntity<ProductPackageDTO>> partialUpdateProductPackage(
            @PathVariable(value = "id", required = false) final UUID id,
            @NotNull @RequestBody ProductPackageDTO productPackageDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ProductPackage partially : {}, {}", id, productPackageDTO);
        productPackageDTO.setId(id);

        return productPackageRepository
                .existsById(id)
                .flatMap(exists -> {
                    if (!exists) {
                        return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                    }

                    Mono<ProductPackageDTO> result = productPackageService.partialUpdate(productPackageDTO);

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


    @PatchMapping(value = "/{id}/updateSew", consumes = {"application/json", "application/merge-patch+json"})
    public Mono<ResponseEntity<ProductPackageDTO>> updateSew(
            @PathVariable(value = "id", required = false) final UUID id
    ) throws URISyntaxException {
        return productPackageRepository
                .existsById(id)
                .flatMap(exists -> {
                    if (!exists) {
                        return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                    }
                    Mono<ProductPackageDTO> result = productPackageService.updateSew(id);
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
     * {@code GET  /product-packages} : get all the productPackages.
     *
     * @param pageable the pagination information.
     * @param request  a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of productPackages in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<ApiResponse<ProductPackageDTO>>> getAllProductPackages(
            @org.springdoc.core.annotations.ParameterObject Pageable pageable,
            ProductPackageQuery query
    ) {
        log.debug("REST request to get a page of ProductPackages");
        return SecurityUtils.getUserJWTDetail().flatMap(user -> {
            query.setCompany(String.valueOf(user.getCompanyId()));
            query.setDepartment(user.getGroupId().toString());
            return productPackageService
                    .countByQuery(query)
                    .zipWith(productPackageService.findByQuery(query, pageable).collectList())
                    .map(
                            countWithEntities ->
                                    ResponseEntity.ok()

                                            .body(
                                                    new ApiResponse<>(
                                                            countWithEntities.getT2(),
                                                            countWithEntities.getT1()
                                                    )
                                            )
                    );
        });
    }

    /**
     * {@code GET  /product-packages/:id} : get the "id" productPackage.
     *
     * @param id the id of the productPackageDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the productPackageDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<ProductPackageDTO>> getProductPackage(@PathVariable("id") UUID id) {
        log.debug("REST request to get ProductPackage : {}", id);
        Mono<ProductPackageDTO> productPackageDTO = productPackageService.findOne(id);
        return ResponseUtil.wrapOrNotFound(productPackageDTO);
    }

    /**
     * {@code DELETE  /product-packages/:id} : delete the "id" productPackage.
     *
     * @param id the id of the productPackageDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteProductPackage(@PathVariable("id") UUID id) {
        log.debug("REST request to delete ProductPackage : {}", id);
        return productPackageService
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
