package com.masi.sale.web.rest;


import com.masi.sale.repository.PurchaseReviewRepository;
import com.masi.sale.service.PurchaseReviewService;
import com.masi.sale.service.dto.PurchaseReviewDTO;
import com.masi.sale.service.dto.RequestReviewDTO;
import com.masi.sale.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link com.masi.sale.domain.PurchaseReview}.
 */
@RestController
@RequestMapping("/api/purchase-reviews")
public class PurchaseReviewResource {

    private static final Logger log = LoggerFactory.getLogger(PurchaseReviewResource.class);

    private static final String ENTITY_NAME = "masiSalePurchaseReview";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PurchaseReviewService purchaseReviewService;

    private final PurchaseReviewRepository purchaseReviewRepository;

    public PurchaseReviewResource(PurchaseReviewService purchaseReviewService, PurchaseReviewRepository purchaseReviewRepository) {
        this.purchaseReviewService = purchaseReviewService;
        this.purchaseReviewRepository = purchaseReviewRepository;
    }

    @PostMapping("")
    public Mono<ResponseEntity<Map<String, Object>>> createPurchaseReview(@Valid @RequestBody RequestReviewDTO dto) {
        log.debug("REST request to save PurchaseReview : {}", dto);

        return purchaseReviewService
            .requestReview(dto)
            .then(Mono.fromCallable(() -> ResponseEntity.ok()
                .body(Map.of("status", "success"))));
    }


    @GetMapping("/{id}")
    public Mono<ResponseEntity<PurchaseReviewDTO>> getPurchaseReview(@PathVariable("id") UUID id) {
        log.debug("REST request to get PurchaseReview : {}", id);
        Mono<PurchaseReviewDTO> purchaseReviewDTO = purchaseReviewService.findOne(id);
        return ResponseUtil.wrapOrNotFound(purchaseReviewDTO);
    }

    @GetMapping("purchase-request/{id}")
    public Mono<ResponseEntity<List<PurchaseReviewDTO>>> getPurchaseReviewByPurchaseRequestId(@PathVariable("id") UUID id) {
        log.debug("REST request to get PurchaseReview by PurchaseRequest : {}", id);
        return purchaseReviewService.findAllByPurchaseRequest(id)
            .collectList()
            .map(list -> ResponseEntity.ok().body(list));
    }

    @PatchMapping(value = "/{id}/review", consumes = {"application/json", "application/merge-patch+json"})
    public Mono<ResponseEntity<PurchaseReviewDTO>> reviewOrderReview(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody PurchaseReviewDTO dto
    ) {
        dto.setId(id);

        return purchaseReviewRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }
                Mono<PurchaseReviewDTO> result = purchaseReviewService.review(dto);
                return result
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(
                        res -> ResponseEntity.ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, res.getId().toString()))
                            .body(res)
                    );
            });
    }


    @DeleteMapping("purchase-request/{id}")
    public Mono<ResponseEntity<Void>> deletePurchaseReview(@PathVariable("id") UUID id) {
        log.debug("REST request to delete PurchaseReview : {}", id);
        return purchaseReviewService
            .deleteByPurchaseRequestId(id)
            .then(Mono.fromCallable(() -> ResponseEntity.noContent()
                .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                .build()));

    }
}
