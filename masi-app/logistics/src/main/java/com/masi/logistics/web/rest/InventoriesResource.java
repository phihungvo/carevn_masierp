package com.masi.logistics.web.rest;

import com.carevn.masi.dto.ApiResponse;
import com.carevn.masi.utils.SecurityUtils;
import com.masi.logistics.domain.DeliverySchedule;
import com.masi.logistics.domain.DocumentCodeSequence;
import com.masi.logistics.domain.Inventories;
import com.masi.logistics.domain.criteria.FactoriesCriteria;
import com.masi.logistics.domain.criteria.InventoriesCriteria;
import com.masi.logistics.domain.enumeration.StatusEntity;
import com.masi.logistics.repository.InventoriesRepository;
import com.masi.logistics.security.AuthoritiesConstants;
import com.masi.logistics.service.DocumentCodeSequenceService;
import com.masi.logistics.service.InventoriesService;
import com.masi.logistics.service.RequestApprovalService;
import com.masi.logistics.service.dto.*;
import com.masi.logistics.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Value;
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
import tech.jhipster.service.filter.StringFilter;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link com.masi.logistics.domain.Inventories}.
 */
@RestController
@RequestMapping("/api/inventories")
public class InventoriesResource {

    private static final Logger log = LoggerFactory.getLogger(InventoriesResource.class);

    private static final String ENTITY_NAME = "masiLogisticsInventories";
    private final RequestApprovalService requestApprovalService;
    private final DocumentCodeSequenceService documentCodeSequenceService;

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InventoriesService inventoriesService;

    private final InventoriesRepository inventoriesRepository;

    public InventoriesResource(InventoriesService inventoriesService, InventoriesRepository inventoriesRepository, RequestApprovalService requestApprovalService, DocumentCodeSequenceService documentCodeSequenceService) {
        this.inventoriesService = inventoriesService;
        this.inventoriesRepository = inventoriesRepository;
        this.requestApprovalService = requestApprovalService;
        this.documentCodeSequenceService = documentCodeSequenceService;
    }

    /**
     * {@code POST  /inventories} : Create a new inventories.
     *
     * @param inventoriesDTO the inventoriesDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new inventoriesDTO, or with status {@code 400 (Bad Request)} if the inventories has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<InventoriesDTO>> createInventories(@Valid @RequestBody InventoriesDTO inventoriesDTO) {
        log.debug("REST request to save Inventories : {}", inventoriesDTO);

        inventoriesDTO.setId(UUID.randomUUID());
        return documentCodeSequenceService.getByDocumentType(Inventories.ENTITY_NAME)
                .flatMap(code -> {
                    inventoriesDTO.setCode(code.getNext());
                    return inventoriesService.save(inventoriesDTO)
                            .map(this::buildCreationResponse);
                });
    }

    private ResponseEntity<InventoriesDTO> buildCreationResponse(InventoriesDTO result) {
        try {
            return ResponseEntity.created(new URI("/api/inventories/" + result.getId()))
                    .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                    .body(result);
        } catch (URISyntaxException e) {
            log.error("Invalid URI syntax for created entity: {}", result.getId(), e);
            throw new IllegalStateException("Failed to create URI for new entity", e);
        }
    }

    /**
     * {@code PATCH  /inventories/:id} : Partial updates given fields of an existing inventories, field will ignore if it is null
     *
     * @param id             the id of the inventoriesDTO to save.
     * @param inventoriesDTO the inventoriesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated inventoriesDTO,
     * or with status {@code 400 (Bad Request)} if the inventoriesDTO is not valid,
     * or with status {@code 404 (Not Found)} if the inventoriesDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the inventoriesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = {"application/json", "application/merge-patch+json"})
    public Mono<ResponseEntity<InventoriesDTO>> partialUpdateInventories(
            @PathVariable(value = "id", required = false) final UUID id,
            @NotNull @RequestBody InventoriesDTO inventoriesDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Inventories partially : {}, {}", id, inventoriesDTO);
        inventoriesDTO.setId(id);
        return inventoriesRepository
                .existsById(id)
                .flatMap(exists -> {
                    if (!exists) {
                        return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                    }

                    Mono<InventoriesDTO> result = inventoriesService.partialUpdate(inventoriesDTO);

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
     * {@code GET  /inventories} : get all the inventories.
     *
     * @param pageable the pagination information.
     * @param request  a {@link ServerHttpRequest} request.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of inventories in body.
     */

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<ApiResponse<InventoriesDTO>>> getAllInventories(
            InventoriesCriteria criteria,
            @org.springdoc.core.annotations.ParameterObject Pageable pageable,
            ServerHttpRequest request
    ) {
        log.debug("REST request to get Inventories by criteria: {}", criteria);
        return SecurityUtils.getUserJWTDetail().flatMap(user -> {
            if (!user.getAuthorities().contains(AuthoritiesConstants.SUPER_ADMIN)) {
                var company = (criteria.getCompany() == null ? new StringFilter() : criteria.getCompany()).getEquals();
                if (company == null) {
                    criteria.company().setEquals(user.getCompanyId());
                }
            } else {
                criteria.company().setEquals(user.getCompanyId());
            }
            return inventoriesService
                    .countByCriteria(criteria)
                    .zipWith(inventoriesService.findByCriteria(criteria, pageable).collectList())
                    .map(
                            countWithEntities ->
                                    ResponseEntity.ok()
                                            .headers(
                                                    PaginationUtil.generatePaginationHttpHeaders(
                                                            ForwardedHeaderUtils.adaptFromForwardedHeaders(request.getURI(), request.getHeaders()),
                                                            new PageImpl<>(countWithEntities.getT2(), pageable, countWithEntities.getT1())
                                                    )
                                            )
                                            .body(new ApiResponse<>(countWithEntities.getT2(), countWithEntities.getT1()))
                    );
        });
    }

    /**
     * {@code GET  /inventories/count} : count all the inventories.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public Mono<ResponseEntity<Long>> countInventories(InventoriesCriteria criteria) {
        log.debug("REST request to count Inventories by criteria: {}", criteria);
        return inventoriesService.countByCriteria(criteria).map(count -> ResponseEntity.status(HttpStatus.OK).body(count));
    }

    /**
     * {@code GET  /inventories/:id} : get the "id" inventories.
     *
     * @param id the id of the inventoriesDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the inventoriesDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<InventoriesDTO>> getInventories(@PathVariable("id") UUID id) {
        log.debug("REST request to get Inventories : {}", id);
        Mono<InventoriesDTO> inventoriesDTO = inventoriesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(inventoriesDTO);
    }

    /**
     * {@code DELETE  /inventories/:id} : delete the "id" inventories.
     *
     * @param id the id of the inventoriesDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @PatchMapping("/{id}/disable")
    public Mono<ResponseEntity<Map<String, Object>>> deleteInventories(@PathVariable("id") UUID id) {
        log.debug("REST request to delete Inventories : {}", id);

        return inventoriesService.delete(id)
                .then(Mono.fromSupplier(() -> {
                    Map<String, Object> responseBody = new HashMap<>();
                    responseBody.put("message", "Inventories deleted successfully");
                    return ResponseEntity.ok().body(responseBody);
                }))
                .onErrorResume(IllegalStateException.class, ex -> {
                    Map<String, Object> responseBody = new HashMap<>();
                    responseBody.put("error", "Cannot delete Inventories");
                    responseBody.put("message", ex.getMessage());
                    responseBody.put("status", "failed");
                    return Mono.just(ResponseEntity.badRequest().body(responseBody));
                });
    }


//    @PatchMapping("/{id}/review")
//    public Mono<ResponseEntity<Map<String, Object>>> reviewInventories(
//            @PathVariable("id") UUID id,
//            @RequestBody(required = false) Collection<UUID> employeeReviewIds) throws URISyntaxException {
//        log.debug("REST request to confirm Inventories with id: {}", id);
//
//        return (employeeReviewIds == null || employeeReviewIds.isEmpty() ?
//                SecurityUtils.getUserJWTDetail().map(user -> {
//                    List<UUID> newEmployeeReviewIds = new ArrayList<>();
//                    newEmployeeReviewIds.add(UUID.fromString(String.valueOf(user.getUserId())));
//                    return newEmployeeReviewIds;
//                }) :
//                Mono.just(employeeReviewIds)
//        ).flatMap(ids ->
//                inventoriesService.reviewInventories(id, ids)
//                        .then(Mono.fromSupplier(() -> {
//                            Map<String, Object> responseBody = new HashMap<>();
//                            responseBody.put("message", "Inventories change status successfully");
//                            return ResponseEntity.ok().body(responseBody);
//                        }))
//                        .onErrorResume(IllegalStateException.class, ex -> {
//                            log.error("Failed to confirm Inventories with id: {}", id, ex);
//                            Map<String, Object> responseBody = new HashMap<>();
//                            responseBody.put("error", "Cannot confirm Inventories");
//                            responseBody.put("message", ex.getMessage());
//                            responseBody.put("status", "failed");
//                            return Mono.just(ResponseEntity.badRequest().body(responseBody));
//                        })
//        );
//    }
//
//
//    @PatchMapping("/{id}/confirm")
//    public Mono<ResponseEntity<Map<String, Object>>> confirmInventories(@PathVariable(value = "id", required = false) final UUID id,
//                                                                        @RequestBody(required = false) FileRequestDTO fileId) throws URISyntaxException {
//        log.debug("REST request to confirm Inventories with id: {}", id);
//        return inventoriesService.confirmInventories(id, fileId.getFileId())
//                .then(Mono.fromSupplier(() -> {
//                    Map<String, Object> responseBody = new HashMap<>();
//                    responseBody.put("message", "Inventories change status successfully");
//                    return ResponseEntity.ok().body(responseBody);
//                }))
//                .onErrorResume(IllegalStateException.class, ex -> {
//                    log.error("Failed to confirm Inventories with id: {}", id, ex);
//                    Map<String, Object> responseBody = new HashMap<>();
//                    responseBody.put("error", "Cannot confirm Inventories");
//                    responseBody.put("message", ex.getMessage());
//                    responseBody.put("status", "failed");
//                    return Mono.just(ResponseEntity.badRequest().body(responseBody));
//                });
//    }
//
//    @PatchMapping("/{id}/reject")
//    public Mono<ResponseEntity<Map<String, Object>>> rejectInventories(@PathVariable("id") UUID id,
//                                                                       @RequestBody(required = false) String reasonNote) throws URISyntaxException {
//        log.debug("REST request to confirm Inventories with id: {}", id);
//        if (reasonNote == null || reasonNote.isEmpty()) {
//           reasonNote = "";
//        }
//        return inventoriesService.rejectInventories(id, reasonNote)
//                .then(Mono.fromSupplier(() -> {
//                    Map<String, Object> responseBody = new HashMap<>();
//                    responseBody.put("message", "Inventories change status successfully");
//                    return ResponseEntity.ok().body(responseBody);
//                }))
//                .onErrorResume(IllegalStateException.class, ex -> {
//                    log.error("Failed to confirm Inventories with id: {}", id, ex);
//                    Map<String, Object> responseBody = new HashMap<>();
//                    responseBody.put("error", "Cannot confirm Inventories");
//                    responseBody.put("message", ex.getMessage());
//                    responseBody.put("status", "failed");
//                    return Mono.just(ResponseEntity.badRequest().body(responseBody));
//                });
//    }

    @GetMapping("/export")
    public Mono<ResponseEntity<byte[]>> exportInventories(
            InventoriesCriteria criteria,
            @org.springdoc.core.annotations.ParameterObject Pageable pageable,
            ServerHttpRequest request
    ) {
        return inventoriesService.exportRecordsAsCSV(criteria, pageable)
                .map(csvBytes -> {
                    log.debug("REST request to export Inventories as CSV");

                    return ResponseEntity.ok()
                            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"Inventories" + ".xlsx\"")
                            .contentType(org.springframework.http.MediaType.APPLICATION_OCTET_STREAM)
                            .body(csvBytes);
                });
    }

    @GetMapping("/notification-item-update")
    public Mono<Void> NotificationInventories() throws URISyntaxException {
        log.debug("REST request to notification Inventories");
        return inventoriesService.NotificationInventories();
    }


    @PatchMapping(value = "/{id}/request-review")
    public Mono<ResponseEntity<Map<String, UUID>>> requestReview(@PathVariable UUID id) {
        return inventoriesService.setStatus(id, StatusEntity.WAITING_APPROVED)
                .then(requestApprovalService.updateCleanAgain(id))
                .thenReturn(ResponseEntity.ok(Map.of("id", id)));
    }

    @PatchMapping(value = "/{id}/confirm-inventories")
    public Mono<ResponseEntity<Map>> requestConfirmInventories(@PathVariable UUID id) {
        return inventoriesService.setStatus(id, StatusEntity.COMPLETED)
                .then(inventoriesService.createItemInWareHouse(id))
                .thenReturn(ResponseEntity.ok(Map.of("id", id)));
    }

    @PatchMapping(value = "/{id}/cancel-inventories")
    public Mono<ResponseEntity<Map>> cancelConfirmInventories(@PathVariable UUID id) {
        return inventoriesService.setStatus(id, StatusEntity.CANCELLED)
                .then(Mono.just(ResponseEntity.ok(Map.of("id", id))));
    }

    @PatchMapping(value = "/{id}/review")
    public Mono<ResponseEntity<Map<String, Object>>> approve(
            @PathVariable(value = "id") final UUID id,
            @NotNull @RequestBody UpdateReview updateReview
    ) throws URISyntaxException {

        updateReview.setDocumentId(id);
        return requestApprovalService.handleReview(updateReview).flatMap(e -> {
            return requestApprovalService.findByDocumentId(e.getDocumentId()).collectList().flatMap(requestApprovals -> {

                boolean isAllApproved = requestApprovals.stream()
                        .allMatch(requestApproval -> requestApproval.getResult() != null && requestApproval.getResult());

                boolean isOneRejected = requestApprovals.stream()
                        .map(requestApproval -> {
                            if (requestApproval.getResult() == null) {
                                return false;
                            }
                            return !requestApproval.getResult();
                        })
                        .anyMatch(result -> result);



                if (isAllApproved) {
                    return inventoriesService.setStatus(e.getDocumentId(), StatusEntity.APPROVED).then(Mono.just(ResponseEntity.ok(Map.of("status", "APPROVED"))));
                }
                if (isOneRejected) {
                    return inventoriesService.setStatus(e.getDocumentId(), StatusEntity.REJECTED).then(Mono.just(ResponseEntity.ok(Map.of("status", "REJECTED"))));
                }
                return Mono.just(ResponseEntity.ok(Map.of("status", "PENDING")));
            });

        });
    }

    @GetMapping("/code/next")
    public Mono<ResponseEntity<DocumentCodeSequence>> getNextCodeNo() {
        log.debug("REST request to get next inventory no");
        return documentCodeSequenceService.getByDocumentType(Inventories.ENTITY_NAME)
                .map(ResponseEntity::ok);
    }
}
