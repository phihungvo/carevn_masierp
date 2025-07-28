package com.masi.production.web.rest;

import com.carevn.masi.dto.ApiResponse;
import com.carevn.masi.utils.SecurityUtils;
import com.carevn.masi.utils.Utilities;
import com.masi.production.repository.ManufactureOrderRepository;
import com.masi.production.security.AuthoritiesConstants;
import com.masi.production.service.ManufactureOrderService;
import com.masi.production.service.ProductionStandardService;
import com.masi.production.service.dto.ManuFactureDTO;
import com.masi.production.service.dto.ManufactureOrderDTO;
import com.masi.production.service.dto.ManufactureOrderRO;
import com.masi.production.service.dto.ManufactureWorkOrdersRO;
import com.masi.production.web.rest.errors.BadRequestAlertException;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.UUID;

import jakarta.validation.constraints.Null;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
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

import java.util.List;
import java.util.Collection;

/**
 * REST controller for managing {@link com.masi.production.domain.ManufactureOrder}.
 */
@RestController
@RequestMapping("/api/manufacture-orders")
public class ManufactureOrderResource {

    private final Logger log = LoggerFactory.getLogger(ManufactureOrderResource.class);

    private static final String ENTITY_NAME = "masiProductionManufactureOrder";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ManufactureOrderService manufactureOrderService;

    private final ProductionStandardService productionStandardService;

    private final ManufactureOrderRepository manufactureOrderRepository;

    public ManufactureOrderResource(
        ManufactureOrderService manufactureOrderService, ProductionStandardService productionStandardService,
        ManufactureOrderRepository manufactureOrderRepository
    ) {
        this.manufactureOrderService = manufactureOrderService;
        this.productionStandardService = productionStandardService;
        this.manufactureOrderRepository = manufactureOrderRepository;
    }

    /**
     * {@code PATCH  /manufacture-orders/:id/cancel} : Cancel the "id" manufactureOrder.
     *
     * @param id the id of the manufactureOrderDTO to cancel.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated manufactureOrderDTO,
     * or with status {@code 400 (Bad Request)} if the manufactureOrderDTO is already cancelled,
     * or with status {@code 404 (Not Found)} if the manufactureOrderDTO is not found,
     */
    @PatchMapping("/{id}/cancel")
    public Mono<ResponseEntity<Map<String, Object>>> cancelManufactureOrder(@PathVariable("id") UUID id) throws URISyntaxException {
        log.debug("REST request to cancel ManufactureOrder : {}", id);
        return manufactureOrderService
                .cancel(id)
                .map(
                        result ->
                                ResponseEntity.ok()
                                        .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                                        .body(Utilities.generateResponse("success", null))
                );
    }

    @GetMapping("/list-by-orders")
    public Mono<List<ManufactureOrderDTO>> getManufactureOrdersByOrderIds(
            @RequestParam Collection<UUID> ids
    ) {
        log.info("REST request to get ManufactureOrders by OrderIds : {}", ids);
        return manufactureOrderService
                .findByListOrderId(ids).map(e -> {
                    return e;
                });
    }

//    /**
//     * {@code POST  /manufacture-orders} : Create a new manufactureOrder.
//     *
//     * @param manufactureOrderDTO the manufactureOrderDTO to create.
//     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new manufactureOrderDTO, or with status {@code 400 (Bad Request)} if the manufactureOrder has already an ID.
//     * @throws URISyntaxException if the Location URI syntax is incorrect.
//     */
//    @PostMapping("")
//    public Mono<ResponseEntity<Map>> createManufactureOrder(@Valid @RequestBody ManuFactureDTO manufactureOrderDTO)
//            throws URISyntaxException {
//        log.debug("REST request to save ManufactureOrder : {}", manufactureOrderDTO);
//        manufactureOrderDTO.setId(UUID.randomUUID());
//        return manufactureOrderService
//                .save(manufactureOrderDTO)
//                .handle((result, sink) -> {
//                    try {
//                        sink.next(ResponseEntity.created(new URI("/api/manufacture-orders/" + result.getId()))
//                                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
//                                .body(Utilities.generateResponse("success", result)));
//                    } catch (URISyntaxException e) {
//                        sink.error(new RuntimeException(e));
//                    }
//                });
//    }

    @GetMapping("/export")
    public Mono<ResponseEntity<byte[]>> exportInventories(
            ManufactureOrderRO criteria,
            @org.springdoc.core.annotations.ParameterObject Pageable pageable,
            ServerHttpRequest request
    ) {
        return SecurityUtils.getUserJWTDetail().flatMap(user -> {
            if (user.getAuthorities().contains(AuthoritiesConstants.SUPER_ADMIN)) {
                criteria.setCompany(null);
            } else {
                criteria.setCompany(user.getCompanyId());
            }
            try {
                return manufactureOrderService.exportRecordsAsCSV(criteria, pageable)
                        .map(csvBytes -> {
                            log.debug("REST request to export Inventories as CSV");

                            return ResponseEntity.ok()
                                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"Inventories.xlsx\"")
                                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                                    .body(csvBytes);
                        });
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<ApiResponse<ManuFactureDTO>>> getAllInventoriesStorages(
            ManufactureOrderRO criteria,
            @org.springdoc.core.annotations.ParameterObject Pageable pageable,
            ServerHttpRequest request
    ) {
        return SecurityUtils.getUserJWTDetail().flatMap(user -> {
            if (user.getAuthorities().contains(AuthoritiesConstants.SUPER_ADMIN)) {
                    criteria.setCompany(null);
            } else {
                criteria.setCompany(user.getCompanyId());
            }
            return manufactureOrderService
                    .countAllByCriteria(criteria)
                    .zipWith(manufactureOrderService.findByCriteria(criteria, pageable).collectList())
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

    @GetMapping("/{id}")
    public Mono<ResponseEntity<ManuFactureDTO>> getManufactureOrder(@PathVariable("id") UUID id) {
        log.debug("REST request to get ManufactureOrder : {}", id);
        Mono<ManuFactureDTO> manufactureOrderDTO = manufactureOrderService.findOne(id);
        return ResponseUtil.wrapOrNotFound(manufactureOrderDTO);
    }

    @PostMapping("")
    public Mono<ResponseEntity<Map>> createManufactureOrder(@Valid @RequestBody ManuFactureDTO manufactureOrderDTO,@RequestParam(required = false,defaultValue = "false") boolean isAutoWorkOrder)
        throws URISyntaxException {
        log.debug("REST request to save ManufactureOrder : {}", manufactureOrderDTO);
        manufactureOrderDTO.setId(UUID.randomUUID());
        return manufactureOrderService
            .save(manufactureOrderDTO)
            .handle((result, sink) -> {
                try {
                    sink.next(ResponseEntity.created(new URI("/api/manufacture-orders/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(Utilities.generateResponse("success", result)));
                } catch (URISyntaxException e) {
                    sink.error(new RuntimeException(e));
                }
            });
    }

    @PatchMapping("/{id}/complete-state")
    public Mono<ResponseEntity<Map>> startProcessProduction(@PathVariable("id") UUID id,
                                                            @RequestParam(required = false, defaultValue = "1") Integer isSkip)
            throws URISyntaxException {
        log.debug("Request to start production for ManufactureOrder ID: {}", id);
        return manufactureOrderService
                .completeManuFacture(id, isSkip)
                .doOnError(e -> log.error("Error starting production: {}", e.getMessage())) // Log any errors
                .handle((result, sink) -> {
                    try {
                        sink.next(ResponseEntity.created(new URI("/api/manufacture-orders/"))
                                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, ""))
                                .body(Utilities.generateResponse("success", result)));
                    } catch (URISyntaxException e) {
                        sink.error(new RuntimeException(e));
                    }
                });
    }


    /**
     * {@code PATCH  /manufacture-orders/:id} : Partial updates given fields of an existing manufactureOrder, field will ignore if it is null
     *
     * @param id                  the id of the manufactureOrderDTO to save.
     * @param manufactureOrderDTO the manufactureOrderDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated manufactureOrderDTO,
     * or with status {@code 400 (Bad Request)} if the manufactureOrderDTO is not valid,
     * or with status {@code 404 (Not Found)} if the manufactureOrderDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the manufactureOrderDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = {"application/json", "application/merge-patch+json"})
    public Mono<ResponseEntity<Map>> partialUpdateManufactureOrder(
            @PathVariable(value = "id", required = false) final UUID id,
            @NotNull @RequestBody ManuFactureDTO manufactureOrderDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ManufactureOrder partially : {}, {}", id, manufactureOrderDTO);
        manufactureOrderDTO.setId(id);

        return manufactureOrderRepository
                .existsById(id)
                .flatMap(exists -> {
                    if (!exists) {
                        return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                    }
                    Mono<ManuFactureDTO> result = manufactureOrderService.partialUpdate(manufactureOrderDTO);
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
     * {@code GET  /manufacture-orders} : get all the manufactureOrders.
     *
     * @param moRO     the  query of the manufactureOrder.
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of manufactureOrders in body.
     */
//    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
//    public Mono<ResponseEntity<ApiResponse<ManufactureOrderDTO>>> getAllManufactureOrders(
//            @org.springdoc.core.annotations.ParameterObject ManufactureOrderRO moRO,
//            @org.springdoc.core.annotations.ParameterObject Pageable pageable
//    ) {
//        log.debug("REST request to get a page of ManufactureOrders");
//        return SecurityUtils.getUserJWTDetail().flatMap(user -> {
//            moRO.setCompany(String.valueOf(user.getCompanyId()));
//            moRO.setDepartment(user.getGroupId().toString());
//            return manufactureOrderService
//                    .countAll(moRO)
//                    .zipWith(manufactureOrderService.findAll(pageable, moRO).collectList())
//                    .map(countWithEntities -> ResponseEntity.ok().body(new ApiResponse<>(countWithEntities.getT2(), countWithEntities.getT1())));
//
//        });
//    }

    /**
     * {@code GET  /manufacture-orders/:id} : get the "id" manufactureOrder.
     *
     * @param id the id of the manufactureOrderDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the manufactureOrderDTO, or with status {@code 404 (Not Found)}.
     */


    /**
     * {@code GET  /manufacture-orders/:id} : get the "id" manufactureOrder.
     *
     * @param id the id of the manufactureOrderDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the manufactureOrderDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/maintain/{id}")
    public Mono<ResponseEntity<ManufactureOrderDTO>> getByMaintainId(@PathVariable("id") UUID id) {
        log.debug("REST request to get getByMaintainId : {}", id);
        return SecurityUtils.getUserJWTDetail().flatMap(user -> {
            Mono<ManufactureOrderDTO> manufactureOrderDTO = manufactureOrderService.findByMaintainId(id, user.getCompanyId());
            return ResponseUtil.wrapOrNotFound(manufactureOrderDTO);
        });
    }

    /**
     * {@code DELETE  /manufacture-orders/:id} : delete the "id" manufactureOrder.
     *
     * @param id the id of the manufactureOrderDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Map>> deleteManufactureOrder(@PathVariable("id") UUID id) {
        log.debug("REST request to delete ManufactureOrder : {}", id);
        return manufactureOrderService
                .delete(id)
                .then(
                        Mono.just(
                                ResponseEntity.ok()
                                        .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                                        .body(Utilities.generateResponse("success", null))
                        )
                );
    }

    /**
     * {@code GET  /manufacture-orders/work-orders} : get all the manufactureOrders.
     *
     * @param moRO     the  query of the manufactureOrder.
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of manufactureOrders in body.
     */
    @GetMapping(value = "/work-orders", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<ApiResponse<ManufactureOrderDTO>>> getAllManufactureOrdersWithWorkOrders(
            @org.springdoc.core.annotations.ParameterObject ManufactureWorkOrdersRO moRO,
            @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        return manufactureOrderService
                .countAllWithWorkOrder(moRO)
                .zipWith(manufactureOrderService.findAllWithWorkOrder(pageable, moRO).collectList())
                .map(countWithEntities -> ResponseEntity.ok().body(new ApiResponse<>(countWithEntities.getT2(), countWithEntities.getT1())));
    }

    @GetMapping("/count-standards/{productStandardId}")
    public Mono<ResponseEntity<BigDecimal>> countStandards(@PathVariable UUID productStandardId) {
        return manufactureOrderService.countTotalQuantityByStandardId(productStandardId).map(ResponseEntity::ok);
    }

    @GetMapping("/count-standards")
    public Mono<ResponseEntity<Map<String, BigDecimal>>> countTotalQualityPurchaseActual() {
    return manufactureOrderService.countTotalQuantityByStandardId(null)
        .zipWith(productionStandardService.countTotalQuantityInMo())
        .map(countStandards -> {
            return ResponseEntity.ok(Map.of("planing", countStandards.getT2(), "actual", countStandards.getT1()));
        });
}
}
