package com.masi.sale.web.rest;

import com.masi.sale.repository.OrderReviewRepository;
import com.masi.sale.service.OrderReviewService;
import com.masi.sale.service.dto.OrderReviewDTO;
import com.masi.sale.service.dto.RequestReviewDTO;
import com.masi.sale.web.rest.errors.BadRequestAlertException;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URISyntaxException;
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
 * REST controller for managing {@link com.masi.sale.domain.OrderReview}.
 */
@RestController
@RequestMapping("/api/order-reviews")
public class OrderReviewResource {

    private static final Logger log = LoggerFactory.getLogger(OrderReviewResource.class);

    private static final String ENTITY_NAME = "masiSaleOrderReview";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OrderReviewService orderReviewService;

    private final OrderReviewRepository orderReviewRepository;

    public OrderReviewResource(OrderReviewService orderReviewService, OrderReviewRepository orderReviewRepository) {
        this.orderReviewService = orderReviewService;
        this.orderReviewRepository = orderReviewRepository;
    }

    @Operation(summary = "Create a new order review request", description = "\nInput: Order Id, List of EmployeeId max is 4,\nOutput: Success or Error message")
    @PostMapping("")
    public Mono<ResponseEntity<Map<String, Object>>> createOrderReview(@Valid @RequestBody RequestReviewDTO dto)
        throws URISyntaxException {
        log.debug("REST request to save request review : {}", dto);

        return orderReviewService
            .requestReview(dto.getDocumentId(), dto)
            .then(Mono.fromCallable(() -> ResponseEntity.ok()
                .body(Map.of("status", "success"))));
    }


    @Operation(summary = "Reject/Approve an order review")
    @PatchMapping(value = "/{id}/review", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<OrderReviewDTO>> reviewOrderReview(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody OrderReviewDTO orderReviewDTO
    ) throws URISyntaxException {
       orderReviewDTO.setId(id);

        return orderReviewRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }
                Mono<OrderReviewDTO> result = orderReviewService.review(orderReviewDTO);
                return result
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(
                        res -> ResponseEntity.ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, res.getId().toString()))
                            .body(res)
                    );
            });
    }





    @Operation(summary = "Get review by id")
    @GetMapping("/{id}")
    public Mono<ResponseEntity<OrderReviewDTO>> getOrderReview(@PathVariable("id") UUID id) {
        log.debug("REST request to get OrderReview : {}", id);
        Mono<OrderReviewDTO> orderReviewDTO = orderReviewService.findOne(id);
        return ResponseUtil.wrapOrNotFound(orderReviewDTO);
    }

    @Operation(summary = "Get all order reviews")
    @GetMapping("order/{orderId}")
    public Mono<ResponseEntity<List<OrderReviewDTO>>> getOrderReviewByOrderId(@PathVariable("orderId") UUID orderId) {
        log.debug("REST request to get OrderReview : {}", orderId);
        return orderReviewService.findByOrder(orderId)
            .collectList()
            .map(list -> ResponseEntity.ok().body(list));
    }


    @Operation(summary = "Delete all order review by order id")
    @DeleteMapping("order/{orderId}")
    public Mono<Void> deleteOrderReviewByOrderId(@PathVariable("orderId") UUID orderId) {
        return orderReviewService.deleteByOrderId(orderId);
    }
}
