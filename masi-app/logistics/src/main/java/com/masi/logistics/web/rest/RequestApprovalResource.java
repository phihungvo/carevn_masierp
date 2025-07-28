package com.masi.logistics.web.rest;

import com.carevn.masi.dto.DocumentReviewMap;
import com.masi.logistics.domain.criteria.RequestApprovalCriteria;
import com.masi.logistics.repository.RequestApprovalRepository;
import com.masi.logistics.service.RequestApprovalService;
import com.masi.logistics.service.dto.RequestApprovalDTO;
import com.masi.logistics.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
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

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * REST controller for managing {@link com.masi.logistics.domain.RequestApproval}.
 */
@RestController
@RequestMapping("/api/request-approvals")
public class RequestApprovalResource {

    private static final Logger log = LoggerFactory.getLogger(RequestApprovalResource.class);

    private static final String ENTITY_NAME = "masiSaleRequestApproval";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RequestApprovalService requestApprovalService;

    private final RequestApprovalRepository requestApprovalRepository;

    public RequestApprovalResource(RequestApprovalService requestApprovalService, RequestApprovalRepository requestApprovalRepository) {
        this.requestApprovalService = requestApprovalService;
        this.requestApprovalRepository = requestApprovalRepository;
    }


    @PostMapping("/map-by-id")
    public Mono<ResponseEntity<List<DocumentReviewMap<RequestApprovalDTO>>>> getMapByIds(@RequestBody List<UUID> ids) {
        return requestApprovalService
                .findReviewMapByDocumentIds(ids)
                .map(
                        requestApprovalDTOS -> {
                            return ResponseEntity.ok().body(requestApprovalDTOS);
                        }
                );
    }


    @PostMapping("")
    public Mono<ResponseEntity<RequestApprovalDTO>> createRequestApproval(@Valid @RequestBody RequestApprovalDTO requestApprovalDTO)
            throws URISyntaxException {
        log.debug("REST request to save RequestApproval : {}", requestApprovalDTO);
        if (requestApprovalDTO.getId() != null) {
            throw new BadRequestAlertException("A new requestApproval cannot already have an ID", ENTITY_NAME, "idexists");
        }
        requestApprovalDTO.setId(UUID.randomUUID());
        return requestApprovalService
                .save(requestApprovalDTO)
                .map(result -> {
                    try {
                        return ResponseEntity.created(new URI("/api/request-approvals/" + result.getId()))
                                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                                .body(result);
                    } catch (URISyntaxException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    /**
     * {@code PUT  /request-approvals/:id} : Updates an existing requestApproval.
     *
     * @param id                 the id of the requestApprovalDTO to save.
     * @param requestApprovalDTO the requestApprovalDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated requestApprovalDTO,
     * or with status {@code 400 (Bad Request)} if the requestApprovalDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the requestApprovalDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<RequestApprovalDTO>> updateRequestApproval(
            @PathVariable(value = "id", required = false) final UUID id,
            @Valid @RequestBody RequestApprovalDTO requestApprovalDTO
    ) throws URISyntaxException {
        log.debug("REST request to update RequestApproval : {}, {}", id, requestApprovalDTO);
        if (requestApprovalDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, requestApprovalDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return requestApprovalRepository
                .existsById(id)
                .flatMap(exists -> {
                    if (!exists) {
                        return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                    }

                    return requestApprovalService
                            .update(requestApprovalDTO)
                            .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                            .map(
                                    result ->
                                            ResponseEntity.ok()
                                                    .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                                                    .body(result)
                            );
                });
    }

    /**
     * {@code PATCH  /request-approvals/:id} : Partial updates given fields of an existing requestApproval, field will ignore if it is null
     *
     * @param id                 the id of the requestApprovalDTO to save.
     * @param requestApprovalDTO the requestApprovalDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated requestApprovalDTO,
     * or with status {@code 400 (Bad Request)} if the requestApprovalDTO is not valid,
     * or with status {@code 404 (Not Found)} if the requestApprovalDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the requestApprovalDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = {"application/json", "application/merge-patch+json"})
    public Mono<ResponseEntity<RequestApprovalDTO>> partialUpdateRequestApproval(
            @PathVariable(value = "id", required = false) final UUID id,
            @NotNull @RequestBody RequestApprovalDTO requestApprovalDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update RequestApproval partially : {}, {}", id, requestApprovalDTO);
        if (requestApprovalDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, requestApprovalDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return requestApprovalRepository
                .existsById(id)
                .flatMap(exists -> {
                    if (!exists) {
                        return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                    }

                    Mono<RequestApprovalDTO> result = requestApprovalService.partialUpdate(requestApprovalDTO);

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

    /**
     * {@code GET  /request-approvals} : get all the requestApprovals.
     *
     * @param pageable the pagination information.
     * @param request  a {@link ServerHttpRequest} request.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of requestApprovals in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<RequestApprovalDTO>>> getAllRequestApprovals(
            RequestApprovalCriteria criteria,
            @org.springdoc.core.annotations.ParameterObject Pageable pageable,
            ServerHttpRequest request
    ) {
        log.debug("REST request to get RequestApprovals by criteria: {}", criteria);
        return requestApprovalService
                .countByCriteria(criteria)
                .zipWith(requestApprovalService.findByCriteria(criteria, pageable).collectList())
                .map(
                        countWithEntities ->
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
     * {@code GET  /request-approvals/count} : count all the requestApprovals.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public Mono<ResponseEntity<Long>> countRequestApprovals(RequestApprovalCriteria criteria) {
        log.debug("REST request to count RequestApprovals by criteria: {}", criteria);
        return requestApprovalService.countByCriteria(criteria).map(count -> ResponseEntity.status(HttpStatus.OK).body(count));
    }

    /**
     * {@code GET  /request-approvals/:id} : get the "id" requestApproval.
     *
     * @param id the id of the requestApprovalDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the requestApprovalDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<RequestApprovalDTO>> getRequestApproval(@PathVariable("id") UUID id) {
        log.debug("REST request to get RequestApproval : {}", id);
        Mono<RequestApprovalDTO> requestApprovalDTO = requestApprovalService.findOne(id);
        return ResponseUtil.wrapOrNotFound(requestApprovalDTO);
    }

    /**
     * {@code DELETE  /request-approvals/:id} : delete the "id" requestApproval.
     *
     * @param id the id of the requestApprovalDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteRequestApproval(@PathVariable("id") UUID id) {
        log.debug("REST request to delete RequestApproval : {}", id);
        return requestApprovalService
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
