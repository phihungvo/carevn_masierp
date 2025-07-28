package com.masi.production.web.rest;

import com.carevn.masi.dto.ApiResponse;
import com.carevn.masi.utils.SecurityUtils;
import com.masi.production.repository.ProductionStandardRepository;
import com.masi.production.service.ProductionStandardService;
import com.masi.production.service.dto.ProductionStandardDTO;
import com.masi.production.service.dto.ProductionStandardRO;
import com.masi.production.service.dto.UomDTO;
import com.masi.production.service.web.LogisticClient;
import com.masi.production.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing
 * {@link com.masi.production.domain.ProductionStandard}.
 */
@RestController
@RequestMapping("/api/production-standards")
public class ProductionStandardResource {

    private final Logger log = LoggerFactory.getLogger(ProductionStandardResource.class);

    private static final String ENTITY_NAME = "masiProductionProductionStandard";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProductionStandardService productionStandardService;

    private final ProductionStandardRepository productionStandardRepository;

    private final LogisticClient logisticClient;

    public ProductionStandardResource(
            ProductionStandardService productionStandardService,
            ProductionStandardRepository productionStandardRepository, LogisticClient logisticClient) {
        this.productionStandardService = productionStandardService;
        this.productionStandardRepository = productionStandardRepository;
        this.logisticClient = logisticClient;
    }

    /**
     * {@code POST  /production-standards} : Create a new productionStandard.
     *
     * @param productionStandardDTO the productionStandardDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with
     *         body the new productionStandardDTO, or with status
     *         {@code 400 (Bad Request)} if the productionStandard has already an
     *         ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<ProductionStandardDTO>> createProductionStandard(
            @Valid @RequestBody ProductionStandardDTO productionStandardDTO) throws URISyntaxException {
        log.debug("REST request to save ProductionStandard : {}", productionStandardDTO);
        if (productionStandardDTO.getId() != null) {
            throw new BadRequestAlertException("A new productionStandard cannot already have an ID", ENTITY_NAME,
                    "idexists");
        }
        productionStandardDTO.setId(UUID.randomUUID());
        productionStandardDTO.setIsDeleted(false);
        return productionStandardService
                .save(productionStandardDTO)
                .map(result -> {
                    try {
                        return ResponseEntity.created(new URI("/api/production-standards/" + result.getId()))
                                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME,
                                        result.getId().toString()))
                                .body(result);
                    } catch (URISyntaxException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    /**
     * {@code PUT  /production-standards/:id} : Updates an existing
     * productionStandard.
     *
     * @param id                    the id of the productionStandardDTO to save.
     * @param productionStandardDTO the productionStandardDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the updated productionStandardDTO,
     *         or with status {@code 400 (Bad Request)} if the productionStandardDTO
     *         is not valid,
     *         or with status {@code 500 (Internal Server Error)} if the
     *         productionStandardDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<ProductionStandardDTO>> updateProductionStandard(
            @PathVariable(value = "id", required = false) final UUID id,
            @Valid @RequestBody ProductionStandardDTO productionStandardDTO) throws URISyntaxException {
        log.debug("REST request to update ProductionStandard : {}, {}", id, productionStandardDTO);

        return productionStandardRepository
                .existsById(id)
                .flatMap(exists -> {
                    if (!exists) {
                        return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                    }
                    productionStandardDTO.setId(id);
                    if (Objects.isNull(productionStandardDTO.getIsDeleted())) {
                        productionStandardDTO.setIsDeleted(false);
                    }
                    return productionStandardService
                            .update(productionStandardDTO)
                            .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                            .map(
                                    result -> ResponseEntity.ok()
                                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true,
                                                    ENTITY_NAME, result.getId().toString()))
                                            .body(result));
                });
    }

    /**
     * {@code PATCH  /production-standards/:id} : Partial updates given fields of an
     * existing productionStandard, field will ignore if it is null
     *
     * @param id                    the id of the productionStandardDTO to save.
     * @param productionStandardDTO the productionStandardDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the updated productionStandardDTO,
     *         or with status {@code 400 (Bad Request)} if the productionStandardDTO
     *         is not valid,
     *         or with status {@code 404 (Not Found)} if the productionStandardDTO
     *         is not found,
     *         or with status {@code 500 (Internal Server Error)} if the
     *         productionStandardDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<ProductionStandardDTO>> partialUpdateProductionStandard(
            @PathVariable(value = "id", required = false) final UUID id,
            @NotNull @RequestBody ProductionStandardDTO productionStandardDTO) throws URISyntaxException {
        log.debug("REST request to partial update ProductionStandard partially : {}, {}", id, productionStandardDTO);
        if (productionStandardDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productionStandardDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return productionStandardRepository
                .existsById(id)
                .flatMap(exists -> {
                    if (!exists) {
                        return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                    }

                    Mono<ProductionStandardDTO> result = productionStandardService.partialUpdate(productionStandardDTO);

                    return result
                            .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                            .map(
                                    res -> ResponseEntity.ok()
                                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true,
                                                    ENTITY_NAME, res.getId().toString()))
                                            .body(res));
                });
    }

    @DeleteMapping("/{id}/cancel")
    public Mono<ResponseEntity<Map<String, Object>>> cancelProductionStandard(@PathVariable("id") UUID id) {
        log.debug("REST request to cancel ProductionStandard : {}", id);
        return productionStandardService.cancel(id).<ResponseEntity<Map<String, Object>>>handle((result, sink) -> {
            try {
                Map<String, Object> responseBody = Map.of("status", "success", "data", result);
                sink.next(ResponseEntity.ok()
                    .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, id.toString()))
                    .body(responseBody));
            } catch (Exception e) {
                sink.error(new RuntimeException(e));
            }
        }).onErrorResume(e -> {
            log.error("Error canceling ProductionStandard: {}", e.getMessage());
            Map<String, Object> errorResponse = Map.of("status", "error", "message", e.getMessage());
            return Mono.just(ResponseEntity.badRequest().body(errorResponse));
        });
    }
    /**
     * {@code GET  /production-standards} : get all the productionStandards as a
     * stream.
     *
     * @return the {@link Flux} of productionStandards.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Mono<ResponseEntity<ApiResponse<ProductionStandardDTO>>> getAllProductionStandardsAsStream(
            @ParameterObject ProductionStandardRO ro,
            @ParameterObject Pageable pageable) {
        log.debug("REST request to get all ProductionStandards as a stream");
        return SecurityUtils.getUserJWTDetail().flatMap(user -> {
            ro.setCompany(String.valueOf(user.getCompanyId()));
            ro.setDepartment(user.getGroupId().toString());
            return productionStandardService
                    .countAllByIsDeleted(false, ro)
                    .zipWith(productionStandardService.findAll(false, pageable, ro)
                            .collectList()
                            .flatMap(t -> {
                                List<UUID> uomIds = t.stream()
                                        .map(uomid -> UUID.fromString(uomid.getUnit()))
                                        .toList();

                                return logisticClient.getUomByListIds(uomIds).collectList().map(emps -> {
                                    Map<UUID, UomDTO> empMap = emps.stream()
                                            .collect(Collectors.toMap(UomDTO::getId, Function.identity()));
                                    t.forEach(c -> {
                                        c.setUomDTO(empMap.get(UUID.fromString(c.getUnit())));
                                    });
                                    return t;
                                });
                            }))
                    .map(countWithEntities -> ResponseEntity.ok()
                            .body(new ApiResponse<>(countWithEntities.getT2(), countWithEntities.getT1())));
        });
    }

    @GetMapping(value = "/full", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Mono<ResponseEntity<ApiResponse<ProductionStandardDTO>>> getAllProductionStandardsAsStreamFull(
            @ParameterObject ProductionStandardRO ro,
            @ParameterObject Pageable pageable) {
        log.debug("REST request to get all ProductionStandards as a stream");
        return SecurityUtils.getUserJWTDetail().flatMap(user -> {
            ro.setCompany(String.valueOf(user.getCompanyId()));
            ro.setDepartment(user.getGroupId().toString());
            return productionStandardService
                    .countAllByIsDeleted(false, ro)
                    .zipWith(productionStandardService.findAllFull(false, pageable, ro)
                            .collectList()
                            .flatMap(t -> {
                                List<UUID> uomIds = t.stream()
                                        .map(uomid -> UUID.fromString(uomid.getUnit()))
                                        .toList();

                                return logisticClient.getUomByListIds(uomIds).collectList().map(emps -> {
                                    Map<UUID, UomDTO> empMap = emps.stream()
                                            .collect(Collectors.toMap(UomDTO::getId, Function.identity()));
                                    t.forEach(c -> {
                                        c.setUomDTO(empMap.get(UUID.fromString(c.getUnit())));
                                    });
                                    return t;
                                });
                            }))
                    .map(countWithEntities -> ResponseEntity.ok()
                            .body(new ApiResponse<>(countWithEntities.getT2(), countWithEntities.getT1())));
        });
    }

    /**
     * {@code GET  /production-standards/:id} : get the "id" productionStandard.
     *
     * @param id the id of the productionStandardDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the productionStandardDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<ProductionStandardDTO>> getProductionStandard(@PathVariable("id") UUID id) {
        log.debug("REST request to get ProductionStandard : {}", id);
        Mono<ProductionStandardDTO> productionStandardDTO = productionStandardService.findOne(id);
        return ResponseUtil.wrapOrNotFound(productionStandardDTO);
    }

    /**
     * {@code DELETE  /production-standards/:id} : delete the "id"
     * productionStandard.
     *
     * @param id the id of the productionStandardDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteProductionStandard(@PathVariable("id") UUID id) {
        log.debug("REST request to delete ProductionStandard : {}", id);
        return productionStandardService
                .delete(id)
                .then(
                        Mono.just(
                                ResponseEntity.noContent()
                                        .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true,
                                                ENTITY_NAME, id.toString()))
                                        .build()));
    }
}
