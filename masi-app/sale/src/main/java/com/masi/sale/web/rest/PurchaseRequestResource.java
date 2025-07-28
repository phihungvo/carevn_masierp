package com.masi.sale.web.rest;

import com.carevn.masi.dto.ApiResponse;
import com.masi.sale.repository.PurchaseRequestRepository;
import com.masi.sale.service.PurchaseRequestService;
import com.masi.sale.service.dto.PurchaseRequestDTO;
import com.masi.sale.service.dto.PurchaseRequestQueryDTO;
import com.masi.sale.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link com.masi.sale.domain.PurchaseRequest}.
 */
@RestController
@RequestMapping("/api/purchase-requests")
public class PurchaseRequestResource {

    private static final Logger log = LoggerFactory.getLogger(PurchaseRequestResource.class);

    private static final String ENTITY_NAME = "masiSalePurchaseRequest";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PurchaseRequestService purchaseRequestService;

    private final PurchaseRequestRepository purchaseRequestRepository;

    public PurchaseRequestResource(PurchaseRequestService purchaseRequestService, PurchaseRequestRepository purchaseRequestRepository) {
        this.purchaseRequestService = purchaseRequestService;
        this.purchaseRequestRepository = purchaseRequestRepository;
    }

    /**
     * {@code POST  /purchase-requests} : Create a new purchaseRequest.
     *
     * @param purchaseRequestDTO the purchaseRequestDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new purchaseRequestDTO, or with status {@code 400 (Bad Request)} if the purchaseRequest has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<PurchaseRequestDTO>> createPurchaseRequest(@Valid @RequestBody PurchaseRequestDTO purchaseRequestDTO)
        throws URISyntaxException {

        purchaseRequestDTO.setId(UUID.randomUUID());
        return purchaseRequestService
            .save(purchaseRequestDTO)
            .handle((result, sink) -> {
                try {
                    sink.next(ResponseEntity.created(new URI("/api/purchase-requests/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result));
                } catch (URISyntaxException e) {
                    sink.error(new RuntimeException(e));
                }
            });
    }

    @PatchMapping(value = "/{id}", consumes = {"application/json", "application/merge-patch+json"})
    public Mono<ResponseEntity<PurchaseRequestDTO>> partialUpdatePurchaseRequest(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody PurchaseRequestDTO purchaseRequestDTO
    ) {
        log.debug("REST request to partial update PurchaseRequest partially : {}, {}", id, purchaseRequestDTO);
        purchaseRequestDTO.setId(id);
        return purchaseRequestRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<PurchaseRequestDTO> result = purchaseRequestService.partialUpdate(purchaseRequestDTO);

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


    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<ApiResponse<PurchaseRequestDTO>>> getAllPurchaseRequests(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        PurchaseRequestQueryDTO query
    ) {
        log.debug("REST request to get a page of PurchaseRequests");
        return purchaseRequestService
            .countAllByQuery(query)
            .zipWith(purchaseRequestService.findAllByQuery(query,pageable).collectList())
            .map(
                countWithEntities ->
                    ResponseEntity.ok()
                        .body(new ApiResponse<>(
                            countWithEntities.getT2(),
                            countWithEntities.getT1()
                        ))
            );
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<PurchaseRequestDTO>> getPurchaseRequest(@PathVariable("id") UUID id) {
        log.debug("REST request to get PurchaseRequest : {}", id);
        Mono<PurchaseRequestDTO> purchaseRequestDTO = purchaseRequestService.findOne(id);
        return ResponseUtil.wrapOrNotFound(purchaseRequestDTO);
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deletePurchaseRequest(@PathVariable("id") UUID id) {
        log.debug("REST request to delete PurchaseRequest : {}", id);
        return purchaseRequestService
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
