package com.masi.logistics.web.rest;

import com.carevn.masi.dto.ApiResponse;
import com.carevn.masi.utils.SecurityUtils;
import com.masi.logistics.domain.DocumentCodeSequence;
import com.masi.logistics.domain.SuppliesRequest;
import com.masi.logistics.domain.criteria.SuppliesRequestCriteria;
import com.masi.logistics.repository.SuppliesRequestRepository;
import com.masi.logistics.service.DocumentCodeSequenceService;
import com.masi.logistics.service.SuppliesRequestService;
import com.masi.logistics.service.dto.RequestApprovalDTO;
import com.masi.logistics.service.dto.SuppliesRequestDTO;
import com.masi.logistics.web.rest.errors.BadRequestAlertException;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

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
 * REST controller for managing {@link com.masi.logistics.domain.SuppliesRequest}.
 */
@RestController
@RequestMapping("/api/supplies-requests")
public class SuppliesRequestResource {

    private static final Logger log = LoggerFactory.getLogger(SuppliesRequestResource.class);

    private static final String ENTITY_NAME = "masiLogisticsSuppliesRequest";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SuppliesRequestService suppliesRequestService;

    private final SuppliesRequestRepository suppliesRequestRepository;

    private final DocumentCodeSequenceService documentCodeSequenceService;

    public SuppliesRequestResource(SuppliesRequestService suppliesRequestService, SuppliesRequestRepository suppliesRequestRepository, DocumentCodeSequenceService documentCodeSequenceService) {
        this.suppliesRequestService = suppliesRequestService;
        this.suppliesRequestRepository = suppliesRequestRepository;
        this.documentCodeSequenceService = documentCodeSequenceService;
    }

    /**
     * {@code POST  /supplies-requests} : Create a new suppliesRequest.
     *
     * @param suppliesRequestDTO the suppliesRequestDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new suppliesRequestDTO, or with status {@code 400 (Bad Request)} if the suppliesRequest has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<SuppliesRequestDTO>> createSuppliesRequest(@Valid @RequestBody SuppliesRequestDTO suppliesRequestDTO)
        throws URISyntaxException {
        log.debug("REST request to save SuppliesRequest : {}", suppliesRequestDTO);

        return suppliesRequestService
            .save(suppliesRequestDTO)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/supplies-requests/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code GET  /supplies-requests/count} : count all the suppliesRequests.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public Mono<ResponseEntity<Long>> countSuppliesRequests(SuppliesRequestCriteria criteria) {
        log.debug("REST request to count SuppliesRequests by criteria: {}", criteria);
        return suppliesRequestService.countByCriteria(criteria).map(count -> ResponseEntity.status(HttpStatus.OK).body(count));
    }

    /**
     * {@code GET  /supplies-requests/:id} : get the "id" suppliesRequest.
     *
     * @param id the id of the suppliesRequestDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the suppliesRequestDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<SuppliesRequestDTO>> getSuppliesRequest(@PathVariable("id") UUID id) {
        log.debug("REST request to get SuppliesRequest : {}", id);
        return SecurityUtils.getUserJWTDetail().flatMap(userJWTDetail -> {
            Mono<SuppliesRequestDTO> suppliesRequestDTO = suppliesRequestService.findOne(id, userJWTDetail.getCompanyId());
            return ResponseUtil.wrapOrNotFound(suppliesRequestDTO);
        });
    }

    /**
     * {@code DELETE  /supplies-requests/:id} : delete the "id" suppliesRequest.
     *
     * @param id the id of the suppliesRequestDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteSuppliesRequest(@PathVariable("id") UUID id) {
        log.debug("REST request to delete SuppliesRequest : {}", id);
        return SecurityUtils.getUserJWTDetail().flatMap(user -> {
            return suppliesRequestService.delete(id, user.getCompanyId(), user.getUserId().toString()).then(
                Mono.just(ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build())
            );
        });
    }

    @Operation(summary = "Trình duyệt đề nghị")
    @PatchMapping("/approved-review/{id}")
    public Mono<ResponseEntity<Map<String, Object>>> proposeReview(@PathVariable("id") UUID id) {
        log.debug("REST request to propose review suppliesRequestService : {}", id);
        return SecurityUtils.getUserJWTDetail().flatMap(userJWTDetail -> {
            return suppliesRequestService.proposeReview(id).map(result -> {
                Map<String, Object> responseBody = new HashMap<>();
                responseBody.put("message", "recovered review successfully");
                responseBody.put("suppliesRequestId", id);
                responseBody.put("status", "success");
                return ResponseEntity.ok().body(responseBody);
            }).onErrorResume(e -> {
                throw new BadRequestAlertException("Failed to approved: " + e.getMessage(), "supplies-request", "approved");
            });
        });
    }

    @Operation(summary = "approved suppliesRequestService ")
    @PatchMapping("/approved/{id}")
    public Mono<ResponseEntity<Map<String, Object>>> approved(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody RequestApprovalDTO approvedRequest) throws URISyntaxException {
        log.debug("REST approved request to suppliesRequestService : {}", id);
        return SecurityUtils.getUserJWTDetail().flatMap(userJWTDetail -> {
            return suppliesRequestService.approved(id, userJWTDetail.getCompanyId(), approvedRequest, userJWTDetail.getUserId().toString()).map(result -> {
                Map<String, Object> responseBody = new HashMap<>();
                responseBody.put("message", "approved successfully");
                responseBody.put("suppliesRequestId", id);
                responseBody.put("status", "success");
                return ResponseEntity.ok().body(responseBody);
            }).onErrorResume(e -> {
                throw new BadRequestAlertException("Failed to approved: " + e.getMessage(), "supplies-request", "approved");
            });
        });
    }

    @Operation(summary = "reject suppliesRequestService ")
    @PatchMapping("/{id}/reject")
    public Mono<ResponseEntity<Map<String, Object>>> reject(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody RequestApprovalDTO approvedRequest) throws URISyntaxException {
        log.debug("REST request to suppliesRequestService : {}", id);
        return SecurityUtils.getUserJWTDetail().flatMap(userJWTDetail -> {
            return suppliesRequestService.reject(id, userJWTDetail.getCompanyId(), approvedRequest, userJWTDetail.getUserId().toString()).map(result -> {
                Map<String, Object> responseBody = new HashMap<>();
                responseBody.put("message", "rejected successfully");
                responseBody.put("suppliesRequestId", id);
                responseBody.put("status", "success");
                return ResponseEntity.ok().body(responseBody);
            }).onErrorResume(e -> {
                throw new BadRequestAlertException("Failed to reject: " + e.getMessage(), "supplies-request", "reject");
            });
        });
    }

    @GetMapping("/code/next")
    public Mono<ResponseEntity<DocumentCodeSequence>> getNextCodeNo() {
        return documentCodeSequenceService.getByDocumentType(SuppliesRequest.ENTITY_NAME)
            .map(ResponseEntity::ok);
    }

    @GetMapping("/export")
    public Mono<ResponseEntity<InputStreamResource>> export(
        @ParameterObject SuppliesRequestCriteria criteria,
        @RequestParam(required = false, defaultValue = "true") boolean download
    ) {
        return suppliesRequestService
            .findByCriteria(criteria, null)
            .collectList()
            .flatMap(pr -> {
                return suppliesRequestService.exportSuppliesRequest(pr).handle((file, sink) -> {
                    try {
                        FileInputStream fileInputStream = new FileInputStream(file);
                        InputStreamResource inputStreamResource = new InputStreamResource(fileInputStream);
                        HttpHeaders headers = new HttpHeaders();
                        String contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
                        var now = new Date();
                        var format = new java.text.SimpleDateFormat("ddMMyyyy");
                        var date = format.format(now);

                        if (download) {
                            headers.add(HttpHeaders.CONTENT_DISPOSITION,
                                String.format("attachment; filename=\"%s\"", "DXMH_"+date+ ".xlsx"));
                        } else {
                            headers.add(HttpHeaders.CONTENT_TYPE, contentType);
                            headers.add("content-name", "DXMH_" + date + ".xlsx");
                        }
                        sink.next(new ResponseEntity<>(inputStreamResource, headers, HttpStatus.OK));
                    } catch (IOException e) {
                        sink.error(e);
                    }
                });
            });
    }

    /**
     * {@code PATCH  /supplies-requests/:id} : Partial updates given fields of an existing suppliesRequest, field will ignore if it is null
     *
     * @param id the id of the suppliesRequestDTO to save.
     * @param suppliesRequestDTO the suppliesRequestDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated suppliesRequestDTO,
     * or with status {@code 400 (Bad Request)} if the suppliesRequestDTO is not valid,
     * or with status {@code 404 (Not Found)} if the suppliesRequestDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the suppliesRequestDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<SuppliesRequestDTO>> partialUpdateSuppliesRequest(
            @PathVariable(value = "id", required = false) final UUID id,
            @NotNull @RequestBody SuppliesRequestDTO suppliesRequestDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update SuppliesRequest partially : {}, {}", id, suppliesRequestDTO);
        suppliesRequestDTO.setId(id);
        return SecurityUtils.getUserJWTDetail().flatMap(user ->{
            suppliesRequestDTO.setUpdatedBy(user.getUserId().toString());
            suppliesRequestDTO.setCompany(user.getCompanyId());
            return suppliesRequestRepository
                    .existsById(id)
                    .flatMap(exists -> {
                        if (!exists) {
                            return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                        }

                        Mono<SuppliesRequestDTO> result = suppliesRequestService.partialUpdate(suppliesRequestDTO);

                        return result
                                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                                .map(
                                        res ->
                                                ResponseEntity.ok()
                                                        .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, res.getId().toString()))
                                                        .body(res)
                                );
                    });
        });
    }

    /**
     * {@code GET  /supplies-requests} : get all the suppliesRequests.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of suppliesRequests in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<ApiResponse<SuppliesRequestDTO>>> getAllSuppliesRequests(
            SuppliesRequestCriteria criteria,
            @org.springdoc.core.annotations.ParameterObject Pageable pageable,
            ServerHttpRequest request
    ) {
        log.debug("REST request to get SuppliesRequests by criteria: {}", criteria);
        return suppliesRequestService
                .countByCriteria(criteria)
                .zipWith(suppliesRequestService.findByCriteria(criteria, pageable).collectList())
                .map(
                        countWithEntities -> ResponseEntity.ok()
                                .headers(
                                        PaginationUtil.generatePaginationHttpHeaders(
                                                ForwardedHeaderUtils
                                                        .adaptFromForwardedHeaders(
                                                                request.getURI(),
                                                                request.getHeaders()),
                                                new PageImpl<>(countWithEntities.getT2(), pageable,
                                                        countWithEntities.getT1())))
                                .body(new ApiResponse<>(countWithEntities.getT2(), countWithEntities.getT1()))
                );
    }

    // cancel request:
    @Operation(summary = "cancel suppliesRequestService ")
    @PatchMapping("/{id}/cancel")
    public Mono<ResponseEntity<Map<String, Object>>> cancel(
        @PathVariable(value = "id", required = false) final UUID id)  {
        log.debug("REST request to cancel suppliesRequestService : {}", id);
        return suppliesRequestService.cancel(id).map(result -> {
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("message", "canceled successfully");
            responseBody.put("suppliesRequestId", id);
            responseBody.put("status", "success");
            return ResponseEntity.ok().body(responseBody);
        }).onErrorResume(e -> {
            throw new BadRequestAlertException("Failed to cancel: " + e.getMessage(), "supplies-request", "cancel");
        });
    }
}
