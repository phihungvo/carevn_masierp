package com.masi.logistics.web.rest;

import com.masi.logistics.domain.criteria.PaymentDetailCriteria;
import com.masi.logistics.repository.PaymentDetailRepository;
import com.masi.logistics.service.PaymentDetailService;
import com.masi.logistics.service.dto.PaymentDetailDTO;
import com.masi.logistics.web.rest.errors.BadRequestAlertException;
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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.service.filter.UUIDFilter;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link com.masi.logistics.domain.PaymentDetail}.
 */
@RestController
@RequestMapping("/api/payment-details")
public class PaymentDetailResource {

    private static final Logger LOG = LoggerFactory.getLogger(PaymentDetailResource.class);

    private static final String ENTITY_NAME = "masiLogisticsPaymentDetail";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PaymentDetailService paymentDetailService;

    private final PaymentDetailRepository paymentDetailRepository;

    public PaymentDetailResource(PaymentDetailService paymentDetailService, PaymentDetailRepository paymentDetailRepository) {
        this.paymentDetailService = paymentDetailService;
        this.paymentDetailRepository = paymentDetailRepository;
    }

    /**
     * {@code POST  /payment-details} : Create a new paymentDetail.
     *
     * @param paymentDetailDTOs the paymentDetailDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new paymentDetailDTO, or with status {@code 400 (Bad Request)} if the paymentDetail has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<List<PaymentDetailDTO>>> createPaymentDetail(@RequestBody List<PaymentDetailDTO> paymentDetailDTOs)
        throws URISyntaxException {
        LOG.debug("REST request to save PaymentDetails : {}", paymentDetailDTOs);
        paymentDetailDTOs.forEach(paymentDetailDTO -> {
            if (paymentDetailDTO.getId() != null) {
                throw new BadRequestAlertException("A new paymentDetail cannot already have an ID", ENTITY_NAME, "idexists");
            }
            paymentDetailDTO.setId(UUID.randomUUID());
        });

        return Flux.fromIterable(paymentDetailDTOs)
            .flatMap(paymentDetailService::save)
            .collectList()
            .map(results -> {
                try {
                    return ResponseEntity.created(new URI("/api/payment-details"))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, ""))
                        .body(results);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /payment-details/:id} : Updates an existing paymentDetail.
     *
     * @param id the id of the paymentDetailDTO to save.
     * @param paymentDetailDTO the paymentDetailDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated paymentDetailDTO,
     * or with status {@code 400 (Bad Request)} if the paymentDetailDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the paymentDetailDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<PaymentDetailDTO>> updatePaymentDetail(
        @PathVariable(value = "id", required = false) final UUID id,
        @RequestBody PaymentDetailDTO paymentDetailDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update PaymentDetail : {}, {}", id, paymentDetailDTO);
        if (paymentDetailDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, paymentDetailDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return paymentDetailRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return paymentDetailService
                    .update(paymentDetailDTO)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(result ->
                        ResponseEntity.ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                            .body(result)
                    );
            });
    }

    /**
     * {@code PATCH  /payment-details/:id} : Partial updates given fields of an existing paymentDetail, field will ignore if it is null
     *
     * @param id the id of the paymentDetailDTO to save.
     * @param paymentDetailDTO the paymentDetailDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated paymentDetailDTO,
     * or with status {@code 400 (Bad Request)} if the paymentDetailDTO is not valid,
     * or with status {@code 404 (Not Found)} if the paymentDetailDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the paymentDetailDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<PaymentDetailDTO>> partialUpdatePaymentDetail(
        @PathVariable(value = "id", required = false) final UUID id,
        @RequestBody PaymentDetailDTO paymentDetailDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update PaymentDetail partially : {}, {}", id, paymentDetailDTO);
        if (paymentDetailDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, paymentDetailDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return paymentDetailRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<PaymentDetailDTO> result = paymentDetailService.partialUpdate(paymentDetailDTO);

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
     * {@code GET  /payment-details} : get all the paymentDetails.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of paymentDetails in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<PaymentDetailDTO>>> getAllPaymentDetails(
        PaymentDetailCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        LOG.debug("REST request to get PaymentDetails by criteria: {}", criteria);
        return paymentDetailService
            .countByCriteria(criteria)
            .zipWith(paymentDetailService.findByCriteria(criteria, pageable).collectList())
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

    @GetMapping(value = "/payment-request-id/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<PaymentDetailDTO>>> getPaymentDetailsByRequestId(
        @PathVariable("id") UUID requestId,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        LOG.debug("REST request to get PaymentDetails by requestId: {}", requestId);
        return paymentDetailService
            .countByRequestId(requestId)
            .zipWith(paymentDetailService.findByRequestId(requestId, pageable).collectList())
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
     * {@code GET  /payment-details/count} : count all the paymentDetails.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public Mono<ResponseEntity<Long>> countPaymentDetails(PaymentDetailCriteria criteria) {
        LOG.debug("REST request to count PaymentDetails by criteria: {}", criteria);
        return paymentDetailService.countByCriteria(criteria).map(count -> ResponseEntity.status(HttpStatus.OK).body(count));
    }

    /**
     * {@code GET  /payment-details/:id} : get the "id" paymentDetail.
     *
     * @param id the id of the paymentDetailDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the paymentDetailDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<PaymentDetailDTO>> getPaymentDetail(@PathVariable("id") UUID id) {
        LOG.debug("REST request to get PaymentDetail : {}", id);
        Mono<PaymentDetailDTO> paymentDetailDTO = paymentDetailService.findOne(id);
        return ResponseUtil.wrapOrNotFound(paymentDetailDTO);
    }

    /**
     * {@code DELETE  /payment-details/:id} : delete the "id" paymentDetail.
     *
     * @param id the id of the paymentDetailDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deletePaymentDetail(@PathVariable("id") UUID id) {
        LOG.debug("REST request to delete PaymentDetail : {}", id);
        return paymentDetailService
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
