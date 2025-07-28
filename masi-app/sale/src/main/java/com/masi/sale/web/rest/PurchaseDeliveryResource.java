package com.masi.sale.web.rest;

import com.masi.sale.repository.PurchaseDeliveryRepository;
import com.masi.sale.service.PurchaseDeliveryService;
import com.masi.sale.service.dto.PurchaseDeliveryDTO;
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
 * REST controller for managing {@link com.masi.sale.domain.PurchaseDelivery}.
 */
@RestController
@RequestMapping("/api/purchase-deliveries")
public class PurchaseDeliveryResource {

    private static final Logger log = LoggerFactory.getLogger(PurchaseDeliveryResource.class);

    private static final String ENTITY_NAME = "masiSalePurchaseDelivery";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PurchaseDeliveryService purchaseDeliveryService;


    public PurchaseDeliveryResource(
        PurchaseDeliveryService purchaseDeliveryService
    ) {
        this.purchaseDeliveryService = purchaseDeliveryService;
    }

    @PatchMapping("/{id}")
    public Mono<ResponseEntity<PurchaseDeliveryDTO>> updatePurchaseDelivery(
        @PathVariable("id") UUID id,
        @Valid @RequestBody PurchaseDeliveryDTO purchaseDeliveryDTO
    ) {
        purchaseDeliveryDTO.setId(id);
        return purchaseDeliveryService
            .partialUpdate(purchaseDeliveryDTO)
            .handle((result, sink) -> {
                sink.next(
                    ResponseEntity.ok()
                        .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, purchaseDeliveryDTO.getId().toString()))
                        .body(result)
                );
            });
    }
}
