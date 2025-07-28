package com.masi.employee.web.rest;

import com.carevn.masi.dto.ApiResponse;
import com.masi.employee.domain.criteria.RequestApprovalDetailCriteria;
import com.masi.employee.repository.RequestApprovalDetailRepository;
import com.masi.employee.service.RequestApprovalDetailService;
import com.masi.employee.service.dto.DocumentReviewDTO;
import com.masi.employee.service.dto.RequestApprovalDetailDTO;
import com.masi.employee.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link com.masi.employee.domain.RequestApprovalDetail}.
 */
@RestController
@RequestMapping("/api/request-approval-details")
public class RequestApprovalDetailResource {

    private static final Logger LOG = LoggerFactory.getLogger(RequestApprovalDetailResource.class);

    private static final String ENTITY_NAME = "masiEmployeeRequestApprovalDetail";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RequestApprovalDetailService requestApprovalDetailService;

    private final RequestApprovalDetailRepository requestApprovalDetailRepository;

    public RequestApprovalDetailResource(
        RequestApprovalDetailService requestApprovalDetailService,
        RequestApprovalDetailRepository requestApprovalDetailRepository
    ) {
        this.requestApprovalDetailService = requestApprovalDetailService;
        this.requestApprovalDetailRepository = requestApprovalDetailRepository;
    }


    @PostMapping("")
    public Mono<ResponseEntity<RequestApprovalDetailDTO>> createRequestApprovalDetail(
        @Valid @RequestBody RequestApprovalDetailDTO requestApprovalDetailDTO
    ) throws URISyntaxException {
        requestApprovalDetailDTO.setId(UUID.randomUUID());
        return requestApprovalDetailService
            .save(requestApprovalDetailDTO)
            .handle((result, sink) -> {
                try {
                    sink.next(ResponseEntity.created(new URI("/api/request-approval-details/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result));
                } catch (URISyntaxException e) {
                    sink.error(new RuntimeException(e));
                }
            });
    }


    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<ApiResponse<RequestApprovalDetailDTO>>> getAllRequestApprovalDetails(
        RequestApprovalDetailCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        LOG.debug("REST request to get RequestApprovalDetails by criteria: {}", criteria);
        return ApiResponse.from(requestApprovalDetailService.findByCriteria(criteria, pageable), requestApprovalDetailService.countByCriteria(criteria))
            .map(ResponseEntity.ok()::body);
    }

    @PatchMapping("/{id}/review")
    public Mono<ResponseEntity<Void>> approveRequestApprovalDetail(
        @PathVariable UUID id,
        @RequestBody DocumentReviewDTO requestApprovalDetailDTO
    ) {
        requestApprovalDetailDTO.setId(id);
        return requestApprovalDetailService.handleReview(requestApprovalDetailDTO)
            .then(Mono.just(ResponseEntity.noContent().headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, id.toString())).build()));

    }


}
