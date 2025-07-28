package com.masi.logistics.web.rest;

import com.carevn.masi.dto.ApiResponse;
import com.masi.logistics.domain.DocumentCodeSequence;
import com.masi.logistics.domain.IncomingInvoice;
import com.masi.logistics.domain.criteria.PaymentRequestCriteria;
import com.masi.logistics.repository.IncomingInvoiceRepository;
import com.masi.logistics.service.DocumentCodeSequenceService;
import com.masi.logistics.service.IncomingInvoiceService;
import com.masi.logistics.service.dto.IncomingInvoiceDTO;
import com.masi.logistics.service.dto.IncomingInvoiceQuery;
import com.masi.logistics.service.dto.RequestApprovalDTO;
import com.masi.logistics.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import lombok.AllArgsConstructor;
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
 * REST controller for managing {@link com.masi.logistics.domain.IncomingInvoice}.
 */
@RestController
@RequestMapping("/api/incoming-invoices")
public class IncomingInvoiceResource {

    private static final Logger LOG = LoggerFactory.getLogger(IncomingInvoiceResource.class);

    private static final String ENTITY_NAME = "masiLogisticsIncomingInvoice";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final IncomingInvoiceService incomingInvoiceService;

    private final IncomingInvoiceRepository incomingInvoiceRepository;
    private final DocumentCodeSequenceService documentCodeSequenceService;

    public IncomingInvoiceResource(IncomingInvoiceService incomingInvoiceService, IncomingInvoiceRepository incomingInvoiceRepository, DocumentCodeSequenceService documentCodeSequenceService) {
        this.incomingInvoiceService = incomingInvoiceService;
        this.incomingInvoiceRepository = incomingInvoiceRepository;
        this.documentCodeSequenceService = documentCodeSequenceService;
    }


    @GetMapping("/next-invoice-no")
    public Mono<ResponseEntity<DocumentCodeSequence>> getNextInvoiceNo() {
        LOG.debug("REST request to get next invoice no");
        return documentCodeSequenceService.getByDocumentType(IncomingInvoice.ENTITY_NAME)
            .map(ResponseEntity::ok);
    }
    @PostMapping("")
    public Mono<ResponseEntity<IncomingInvoiceDTO>> createIncomingInvoice(@Valid @RequestBody IncomingInvoiceDTO incomingInvoiceDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save IncomingInvoice : {}", incomingInvoiceDTO);
        return incomingInvoiceService
            .save(incomingInvoiceDTO)
            .handle((result, sink) -> {
                try {
                    sink.next(ResponseEntity.created(new URI("/api/incoming-invoices/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result));
                } catch (URISyntaxException e) {
                    sink.error(new RuntimeException(e));
                }
            });
    }


    @PatchMapping(value = "/{id}", consumes = {"application/json", "application/merge-patch+json"})
    public Mono<ResponseEntity<IncomingInvoiceDTO>> partialUpdateIncomingInvoice(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody IncomingInvoiceDTO incomingInvoiceDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update IncomingInvoice partially : {}, {}", id, incomingInvoiceDTO);
        incomingInvoiceDTO.setId(id);
        return incomingInvoiceRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<IncomingInvoiceDTO> result = incomingInvoiceService.partialUpdate(incomingInvoiceDTO);

                return result
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(res ->
                        ResponseEntity.ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, res.getId().toString()))
                            .body(res)
                    );
            });
    }


    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<ApiResponse<IncomingInvoiceDTO>>> getAllIncomingInvoices(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        IncomingInvoiceQuery query
    ) {
        LOG.debug("REST request to get a page of IncomingInvoices");
        return ApiResponse.from(incomingInvoiceService.findAll(query, pageable), incomingInvoiceService.countAll(query))
            .map(ResponseEntity::ok);
    }


    @GetMapping("/{id}")
    public Mono<ResponseEntity<IncomingInvoiceDTO>> getIncomingInvoice(@PathVariable("id") UUID id) {
        LOG.debug("REST request to get IncomingInvoice : {}", id);
        Mono<IncomingInvoiceDTO> incomingInvoiceDTO = incomingInvoiceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(incomingInvoiceDTO);
    }


    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteIncomingInvoice(@PathVariable("id") UUID id) {
        LOG.debug("REST request to delete IncomingInvoice : {}", id);
        return incomingInvoiceService
            .delete(id)
            .then(
                Mono.just(
                    ResponseEntity.noContent()
                        .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                        .build()
                )
            );
    }

    // api gửi duyệt hoá đơn đầu vào
    @PatchMapping("/{id}/send-approve")
    public Mono<ResponseEntity<IncomingInvoiceDTO>> sendApproveIncomingInvoice(@PathVariable("id") UUID id,
                                                                               @RequestBody List<RequestApprovalDTO> suppliesRequestDTO ) {
        LOG.debug("REST request to send approve IncomingInvoice : {}", id);
        return incomingInvoiceService
            .sendForApproval(id, suppliesRequestDTO)
            .map(incomingInvoiceDTOs -> ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, incomingInvoiceDTOs.getId().toString()))
                .body(incomingInvoiceDTOs)
            );
    }

    // api duyệt hoá đơn đầu vào
    @PatchMapping("/{id}/approve")
    public Mono<ResponseEntity<IncomingInvoiceDTO>> approveIncomingInvoice(@PathVariable("id") UUID id,
                                                                           @RequestBody RequestApprovalDTO requestApprovalDTO) {
        LOG.debug("REST request to approve IncomingInvoice : {}", id);
        return incomingInvoiceService
            .approve(id, requestApprovalDTO)
            .map(incomingInvoiceDTOs -> ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, incomingInvoiceDTOs.getId().toString()))
                .body(incomingInvoiceDTOs)
            );
    }

    // api từ chối duyệt hoá đơn đầu vào
    @PatchMapping("/{id}/reject")
    public Mono<ResponseEntity<IncomingInvoiceDTO>> rejectIncomingInvoice(@PathVariable("id") UUID id,
                                                                          @RequestBody RequestApprovalDTO requestApprovalDTO) {
        LOG.debug("REST request to reject IncomingInvoice : {}", id);
        return incomingInvoiceService
            .reject(id, requestApprovalDTO)
            .map(incomingInvoiceDTOs -> ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, incomingInvoiceDTOs.getId().toString()))
                .body(incomingInvoiceDTOs)
            );
    }

    // api hủy duyệt hoá đơn đầu vào
    @PatchMapping("/{id}/cancel")
    public Mono<ResponseEntity<IncomingInvoiceDTO>> cancelIncomingInvoice(@PathVariable("id") UUID id) {
        LOG.debug("REST request to cancel IncomingInvoice : {}", id);
        return incomingInvoiceService
            .cancel(id)
            .map(incomingInvoiceDTOs -> ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, incomingInvoiceDTOs.getId().toString()))
                .body(incomingInvoiceDTOs)
            );
    }

    @GetMapping("/export")
    public Mono<ResponseEntity<InputStreamResource>> export(
        @ParameterObject IncomingInvoiceQuery query,
        @ParameterObject boolean download
    ) {
        LOG.debug("REST request to export PaymentRequests by query: {}", query);
        return incomingInvoiceService
            .findAll(query, null)
            .collectList()
            .flatMap(pr -> {
                return incomingInvoiceService.export(pr).handle((file, sink) -> {
                    try {
                        FileInputStream fileInputStream = new FileInputStream(file);
                        InputStreamResource inputStreamResource = new InputStreamResource(fileInputStream);
                        HttpHeaders headers = new HttpHeaders();
                        String contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

                        if (download) {
                            headers.add(HttpHeaders.CONTENT_DISPOSITION,
                                String.format("attachment; filename=\"%s\"", "HDDV" + ".xlsx"));
                        } else {
                            headers.add(HttpHeaders.CONTENT_TYPE, contentType);
                            headers.add("content-name", "HDDV" + ".xlsx");
                        }
                        sink.next(new ResponseEntity<>(inputStreamResource, headers, HttpStatus.OK));
                    } catch (IOException e) {
                        sink.error(e);
                    }
                });
            });
    }

}
