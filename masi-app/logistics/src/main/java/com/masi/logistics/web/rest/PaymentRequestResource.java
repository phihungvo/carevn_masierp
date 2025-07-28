package com.masi.logistics.web.rest;

import com.carevn.masi.dto.ApiResponse;
import com.masi.logistics.domain.DocumentCodeSequence;
import com.masi.logistics.domain.IncomingInvoice;
import com.masi.logistics.domain.PaymentRequest;
import com.masi.logistics.domain.criteria.PaymentRequestCriteria;
import com.masi.logistics.domain.enumeration.RequestTypeEnum;
import com.masi.logistics.repository.PaymentRequestRepository;
import com.masi.logistics.service.DocumentCodeSequenceService;
import com.masi.logistics.service.PaymentRequestService;
import com.masi.logistics.service.dto.PaymentRequestDTO;
import com.masi.logistics.service.dto.RequestApprovalDTO;
import com.masi.logistics.service.web.client.EmployeeClient;
import com.masi.logistics.web.rest.errors.BadRequestAlertException;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
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

/**
 * REST controller for managing {@link com.masi.logistics.domain.PaymentRequest}.
 */
@RestController
@RequestMapping("/api/payment-requests")
public class PaymentRequestResource {

    private static final Logger LOG = LoggerFactory.getLogger(PaymentRequestResource.class);

    private static final String ENTITY_NAME = "masiLogisticsPaymentRequest";
    private final EmployeeClient employeeClient;

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PaymentRequestService paymentRequestService;

    private final PaymentRequestRepository paymentRequestRepository;
    private final DocumentCodeSequenceService documentCodeSequenceService;

    public PaymentRequestResource(PaymentRequestService paymentRequestService, PaymentRequestRepository paymentRequestRepository, DocumentCodeSequenceService documentCodeSequenceService, EmployeeClient employeeClient) {
        this.paymentRequestService = paymentRequestService;
        this.paymentRequestRepository = paymentRequestRepository;
        this.documentCodeSequenceService = documentCodeSequenceService;
        this.employeeClient = employeeClient;
    }

    /**
     * {@code POST  /payment-requests} : Create a new paymentRequest.
     *
     * @param paymentRequestDTO the paymentRequestDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new paymentRequestDTO, or with status {@code 400 (Bad Request)} if the paymentRequest has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<PaymentRequestDTO>> createPaymentRequest(@RequestBody PaymentRequestDTO paymentRequestDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save PaymentRequest : {}", paymentRequestDTO);
        if (paymentRequestDTO.getId() != null) {
            throw new BadRequestAlertException("A new paymentRequest cannot already have an ID", ENTITY_NAME, "idexists");
        }
        paymentRequestDTO.setId(UUID.randomUUID());
        return paymentRequestService
            .save(paymentRequestDTO)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/payment-requests/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /payment-requests/:id} : Updates an existing paymentRequest.
     *
     * @param id the id of the paymentRequestDTO to save.
     * @param paymentRequestDTO the paymentRequestDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated paymentRequestDTO,
     * or with status {@code 400 (Bad Request)} if the paymentRequestDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the paymentRequestDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<PaymentRequestDTO>> updatePaymentRequest(
        @PathVariable(value = "id", required = false) final UUID id,
        @RequestBody PaymentRequestDTO paymentRequestDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update PaymentRequest : {}, {}", id, paymentRequestDTO);
        if (paymentRequestDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, paymentRequestDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return paymentRequestRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return paymentRequestService
                    .update(paymentRequestDTO)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(result ->
                        ResponseEntity.ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                            .body(result)
                    );
            });
    }

    /**
     * {@code PATCH  /payment-requests/:id} : Partial updates given fields of an existing paymentRequest, field will ignore if it is null
     *
     * @param id the id of the paymentRequestDTO to save.
     * @param paymentRequestDTO the paymentRequestDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated paymentRequestDTO,
     * or with status {@code 400 (Bad Request)} if the paymentRequestDTO is not valid,
     * or with status {@code 404 (Not Found)} if the paymentRequestDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the paymentRequestDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<PaymentRequestDTO>> partialUpdatePaymentRequest(
        @PathVariable(value = "id", required = false) final UUID id,
        @RequestBody PaymentRequestDTO paymentRequestDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update PaymentRequest partially : {}, {}", id, paymentRequestDTO);
        paymentRequestDTO.setId(id);
        return paymentRequestRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<PaymentRequestDTO> result = paymentRequestService.partialUpdateV2(paymentRequestDTO);

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
     * {@code GET  /payment-requests} : get all the paymentRequests.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of paymentRequests in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<ApiResponse<PaymentRequestDTO>>> getAllPaymentRequests(
        PaymentRequestCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        LOG.debug("REST request to get PaymentRequests by criteria: {}", criteria);
        return paymentRequestService
            .countByCriteria(criteria)
            .zipWith(paymentRequestService.findByCriteria(criteria, pageable).collectList())
            .map(countWithEntities ->
                ResponseEntity.ok()
                    .body(
                        new ApiResponse<>(countWithEntities.getT2(), countWithEntities.getT1()))
            );
    }

    /**
     * {@code GET  /payment-requests/count} : count all the paymentRequests.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public Mono<ResponseEntity<Long>> countPaymentRequests(PaymentRequestCriteria criteria) {
        LOG.debug("REST request to count PaymentRequests by criteria: {}", criteria);
        return paymentRequestService.countByCriteria(criteria).map(count -> ResponseEntity.status(HttpStatus.OK).body(count));
    }

    /**
     * {@code GET  /payment-requests/:id} : get the "id" paymentRequest.
     *
     * @param id the id of the paymentRequestDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the paymentRequestDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<PaymentRequestDTO>> getPaymentRequest(@PathVariable("id") UUID id) {
        LOG.debug("REST request to get PaymentRequest : {}", id);
        Mono<PaymentRequestDTO> paymentRequestDTO = paymentRequestService.findOne(id);
        return ResponseUtil.wrapOrNotFound(paymentRequestDTO);
    }

    /**
     * {@code DELETE  /payment-requests/:id} : delete the "id" paymentRequest.
     *
     * @param id the id of the paymentRequestDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deletePaymentRequest(@PathVariable("id") UUID id) {
        LOG.debug("REST request to delete PaymentRequest : {}", id);
        return paymentRequestService
            .delete(id)
            .then(
                Mono.just(
                    ResponseEntity.noContent()
                        .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                        .build()
                )
            );
    }

//    @GetMapping("/auto-generated-code/{type}")
//    public Mono<ResponseEntity<Map<String, String>>> generateAutoCode(@PathVariable("type") RequestTypeEnum type) {
//        return paymentRequestService.getCode(type).map(code -> ResponseEntity.ok().body(Map.of("code", code)));
//    }

    // send to approve
    @PatchMapping("/{id}/send-approve")
    public Mono<ResponseEntity<PaymentRequestDTO>> sendApprovePaymentRequest(@PathVariable("id") UUID id) {
        LOG.debug("REST request to send approve PaymentRequest : {}", id);
        return paymentRequestService
            .sendForApproval(id)
            .map(paymentRequestDTOs -> ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, paymentRequestDTOs.getId().toString()))
                .body(paymentRequestDTOs)
            );
    }

    // approve
    @PatchMapping("/{id}/approve")
    public Mono<ResponseEntity<PaymentRequestDTO>> approvePaymentRequest(@PathVariable("id") UUID id, @RequestBody RequestApprovalDTO requestApprovalDTO) {
        LOG.debug("REST request to approve PaymentRequest : {}", id);
        return paymentRequestService
            .approve(id, requestApprovalDTO)
            .map(paymentRequestDTOs -> ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, paymentRequestDTOs.getId().toString()))
                .body(paymentRequestDTOs)
            );
    }

    //reject
    @PatchMapping("/{id}/reject")
    public Mono<ResponseEntity<PaymentRequestDTO>> rejectPaymentRequest(@PathVariable("id") UUID id, @RequestBody RequestApprovalDTO requestApprovalDTO)
    {
        LOG.debug("REST request to reject PaymentRequest : {}", id);
        return paymentRequestService
            .reject(id, requestApprovalDTO)
            .map(paymentRequestDTOs -> ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, paymentRequestDTOs.getId().toString()))
                .body(paymentRequestDTOs)
            );
    }

    // cancel
    @PatchMapping("/{id}/cancel")
    public Mono<ResponseEntity<PaymentRequestDTO>> cancelPaymentRequest(@PathVariable("id") UUID id) {
        LOG.debug("REST request to cancel PaymentRequest : {}", id);
        return paymentRequestService
            .cancel(id)
            .map(paymentRequestDTOs -> ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, paymentRequestDTOs.getId().toString()))
                .body(paymentRequestDTOs)
            );
    }

    @GetMapping("/next-code")
    public Mono<ResponseEntity<DocumentCodeSequence>> getNextCode(@ParameterObject RequestTypeEnum type) {
        LOG.debug("REST request to get next invoice no");
        return documentCodeSequenceService.makeSureDocumentCodeSequenceExist(PaymentRequest.ENTITY_NAME + "_" + type.name(), "%04d").then(
             documentCodeSequenceService.getByDocumentType(PaymentRequest.ENTITY_NAME + "_" +type.name().toUpperCase())
                .flatMap(documentCodeSequence -> Mono.just(ResponseEntity.ok().body(documentCodeSequence)))
        );
    }

    @GetMapping("/export")
    public Mono<ResponseEntity<InputStreamResource>> export(
        @ParameterObject PaymentRequestCriteria criteria,
        @ParameterObject boolean download
        ) {
        LOG.debug("REST request to export PaymentRequests by criteria: {}", criteria);
        return paymentRequestService
            .findByCriteria(criteria, null)
            .collectList()
            .flatMap(pr -> {
                return paymentRequestService.exportPaymentRequest(pr).handle((file, sink) -> {
                    try {
                        FileInputStream fileInputStream = new FileInputStream(file);
                        InputStreamResource inputStreamResource = new InputStreamResource(fileInputStream);
                        HttpHeaders headers = new HttpHeaders();
                        String contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

                        if (download) {
                            headers.add(HttpHeaders.CONTENT_DISPOSITION,
                                String.format("attachment; filename=\"%s\"", "payment_request" + ".xlsx"));
                        } else {
                            headers.add(HttpHeaders.CONTENT_TYPE, contentType);
                            headers.add("content-name", "payment_request" + ".xlsx");
                        }
                        sink.next(new ResponseEntity<>(inputStreamResource, headers, HttpStatus.OK));
                    } catch (IOException e) {
                        sink.error(e);
                    }
                });
            });
    }
}
