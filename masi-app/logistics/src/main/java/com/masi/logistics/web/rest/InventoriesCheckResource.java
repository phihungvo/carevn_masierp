package com.masi.logistics.web.rest;

import com.carevn.masi.dto.ApiResponse;
import com.carevn.masi.utils.SecurityUtils;
import com.masi.logistics.domain.DocumentCodeSequence;
import com.masi.logistics.domain.Inventories;
import com.masi.logistics.domain.InventoriesCheck;
import com.masi.logistics.domain.criteria.InventoriesCheckCriteria;
import com.masi.logistics.domain.criteria.ItemCriteria;
import com.masi.logistics.domain.enumeration.StatusEntity;
import com.masi.logistics.repository.InventoriesCheckRepository;
import com.masi.logistics.security.AuthoritiesConstants;
import com.masi.logistics.service.DocumentCodeSequenceService;
import com.masi.logistics.service.InventoriesCheckService;
import com.masi.logistics.service.RequestApprovalService;
import com.masi.logistics.service.dto.InventoriesCheckDTO;
import com.masi.logistics.service.dto.UpdateReview;
import com.masi.logistics.service.web.client.EmployeeClient;
import com.masi.logistics.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * REST controller for managing {@link com.masi.logistics.domain.InventoriesCheck}.
 */
@RestController
@RequestMapping("/api/inventories-checks")
public class InventoriesCheckResource {

    private static final Logger log = LoggerFactory.getLogger(InventoriesCheckResource.class);

    private static final String ENTITY_NAME = "masiLogisticsInventoriesCheck";
    private final RequestApprovalService requestApprovalService;
    private final DocumentCodeSequenceService documentCodeSequenceService;
    private final EmployeeClient employeeClient;

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InventoriesCheckService inventoriesCheckService;

    private final InventoriesCheckRepository inventoriesCheckRepository;

    public InventoriesCheckResource(
            InventoriesCheckService inventoriesCheckService,
            InventoriesCheckRepository inventoriesCheckRepository,
            RequestApprovalService requestApprovalService, DocumentCodeSequenceService documentCodeSequenceService, EmployeeClient employeeClient) {
        this.inventoriesCheckService = inventoriesCheckService;
        this.inventoriesCheckRepository = inventoriesCheckRepository;
        this.requestApprovalService = requestApprovalService;
        this.documentCodeSequenceService = documentCodeSequenceService;
        this.employeeClient = employeeClient;
    }

    /**
     * {@code POST  /inventories-checks} : Create a new inventoriesCheck.
     *
     * @param inventoriesCheckDTO the inventoriesCheckDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new inventoriesCheckDTO, or with status {@code 400 (Bad Request)} if the inventoriesCheck has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<InventoriesCheckDTO>> createInventoriesCheck(@Valid @RequestBody InventoriesCheckDTO inventoriesCheckDTO)
            throws URISyntaxException {
        log.debug("REST request to save InventoriesCheck : {}", inventoriesCheckDTO);

        inventoriesCheckDTO.setId(UUID.randomUUID());
        return inventoriesCheckService
                .save(inventoriesCheckDTO)
                .map(result -> {
                    try {
                        return ResponseEntity.created(new URI("/api/inventories-checks/" + result.getId()))
                                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                                .body(result);
                    } catch (URISyntaxException e) {
                        throw new RuntimeException(e);
                    }
                });
    }


    /**
     * {@code PATCH  /inventories-checks/:id} : Partial updates given fields of an existing inventoriesCheck, field will ignore if it is null
     *
     * @param id                  the id of the inventoriesCheckDTO to save.
     * @param inventoriesCheckDTO the inventoriesCheckDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated inventoriesCheckDTO,
     * or with status {@code 400 (Bad Request)} if the inventoriesCheckDTO is not valid,
     * or with status {@code 404 (Not Found)} if the inventoriesCheckDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the inventoriesCheckDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = {"application/json", "application/merge-patch+json"})
    public Mono<ResponseEntity<InventoriesCheckDTO>> partialUpdateInventoriesCheck(
            @PathVariable(value = "id", required = false) final UUID id,
            @NotNull @RequestBody InventoriesCheckDTO inventoriesCheckDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update InventoriesCheck partially : {}, {}", id, inventoriesCheckDTO);
        inventoriesCheckDTO.setId(id);
        return inventoriesCheckRepository
                .existsById(id)
                .flatMap(exists -> {
                    if (!exists) {
                        return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                    }
                    Mono<InventoriesCheckDTO> result = inventoriesCheckService.partialUpdate(inventoriesCheckDTO);
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
     * {@code GET  /inventories-checks} : get all the inventoriesChecks.
     *
     * @param pageable the pagination information.
     * @param request  a {@link ServerHttpRequest} request.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of inventoriesChecks in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<ApiResponse<InventoriesCheckDTO>>> getAllInventoriesChecks(
            InventoriesCheckCriteria criteria,
            @org.springdoc.core.annotations.ParameterObject Pageable pageable,
            ServerHttpRequest request
    ) {
        log.debug("REST request to get InventoriesChecks by criteria: {}", criteria);

        return SecurityUtils.getUserJWTDetail()
                .flatMap(user -> {
                    if (!user.getAuthorities().contains(AuthoritiesConstants.SUPER_ADMIN)) {
                        var companyFilter = criteria.getCompany() == null ? new StringFilter() : criteria.getCompany();
                        if (companyFilter.getEquals() == null) {
                            companyFilter.setEquals(user.getCompanyId());
                        }
                        criteria.setCompany(companyFilter);
                    }

                    Mono<InventoriesCheckCriteria> criteriaMono;
                    if (criteria.getCode() != null) {
                        criteriaMono = employeeClient.getEmployeesBySearch(criteria.getCode().getContains())
                                .collectList()
                                .flatMap(employeeIds -> {
                                    if (!employeeIds.isEmpty()) {
                                        criteria.setEmployeeIds(new ArrayList<>(employeeIds));
                                    }
                                    return Mono.just(criteria);
                                });
                    } else {
                        criteriaMono = Mono.just(criteria);
                    }

                    return criteriaMono.flatMap(updatedCriteria -> inventoriesCheckService
                            .countByCriteria(updatedCriteria)
                            .zipWith(
                                    inventoriesCheckService.findByCriteria(updatedCriteria, pageable).collectList()
                            )
                            .map(countWithEntities -> {
                                var page = new PageImpl<>(countWithEntities.getT2(), pageable, countWithEntities.getT1());
                                var headers = PaginationUtil.generatePaginationHttpHeaders(
                                        ForwardedHeaderUtils.adaptFromForwardedHeaders(request.getURI(), request.getHeaders()),
                                        page
                                );
                                var response = new ApiResponse<>(countWithEntities.getT2(), countWithEntities.getT1());
                                return ResponseEntity.ok().headers(headers).body(response);
                            })
                    );
                });
    }


    /**
     * {@code GET  /inventories-checks/:id} : get the "id" inventoriesCheck.
     *
     * @param id the id of the inventoriesCheckDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the inventoriesCheckDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<InventoriesCheckDTO>> getInventoriesCheck(@PathVariable("id") UUID id) {
        log.debug("REST request to get InventoriesCheck : {}", id);
        Mono<InventoriesCheckDTO> inventoriesCheckDTO = inventoriesCheckService.findOne(id);
        return ResponseUtil.wrapOrNotFound(inventoriesCheckDTO);
    }

    /**
     * {@code DELETE  /inventories-checks/:id} : delete the "id" inventoriesCheck.
     *
     * @param id the id of the inventoriesCheckDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteInventoriesCheck(@PathVariable("id") UUID id) {
        log.debug("REST request to delete InventoriesCheck : {}", id);
        return inventoriesCheckService
                .delete(id)
                .then(
                        Mono.just(
                                ResponseEntity.noContent()
                                        .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                                        .build()
                        )
                );
    }

    @PatchMapping(value = "/{id}/request-review")
    public Mono<ResponseEntity<Map<String, UUID>>> requestReview(@PathVariable UUID id) {
        return inventoriesCheckService.setStatus(id, StatusEntity.WAITING_APPROVED)
                .then(requestApprovalService.updateCleanAgain(id))
                .thenReturn(ResponseEntity.ok(Map.of("id", id)));
    }

    @PatchMapping(value = "/{id}/cancel")
    public Mono<ResponseEntity<Map<String, UUID>>> cancel(@PathVariable UUID id) {
        return inventoriesCheckService.setStatus(id, StatusEntity.CANCELLED)
                .thenReturn(ResponseEntity.ok(Map.of("id", id)));
    }

    @PatchMapping(value = "/{id}/cancel-inventories")
    public Mono<ResponseEntity<Map>> cancelConfirmInventories(@PathVariable UUID id) {
        return inventoriesCheckService.setStatus(id, StatusEntity.CANCELLED)
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
                    return inventoriesCheckService.setStatus(e.getDocumentId(), StatusEntity.APPROVED).then(Mono.just(ResponseEntity.ok(Map.of("status", "APPROVED"))));
                }
                if (isOneRejected) {
                    return inventoriesCheckService.setStatus(e.getDocumentId(), StatusEntity.REJECTED).then(Mono.just(ResponseEntity.ok(Map.of("status", "REJECTED"))));
                }
                return Mono.just(ResponseEntity.ok(Map.of("status", "PENDING")));
            });

        });
    }

    @GetMapping("/code/next")
    public Mono<ResponseEntity<DocumentCodeSequence>> getNextCodeNo() {
        log.debug("REST request to get next inventory no");

        return documentCodeSequenceService.getByDocumentType(InventoriesCheck.ENTITY_NAME)
                .map(documentCodeSequence -> {
                    String formattedDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd_MM_yyyy"));
                    String newCode = formattedDate + "/%" + "04d";
                    documentCodeSequence.setJavaFormat(newCode);
                    return documentCodeSequence;
                })
                .map(ResponseEntity::ok);
    }

    @GetMapping("/export")
    public Mono<ResponseEntity<byte[]>> exportInventories(
            InventoriesCheckCriteria criteria,
            @org.springdoc.core.annotations.ParameterObject Pageable pageable,
            ServerHttpRequest request
    ) {
        return inventoriesCheckService.exportRecordsAsCSV(criteria, pageable)
                .map(csvBytes -> {
                    log.debug("REST request to export Inventories as CSV");

                    return ResponseEntity.ok()
                            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"InventoriesCheck" + ".xlsx\"")
                            .contentType(org.springframework.http.MediaType.APPLICATION_OCTET_STREAM)
                            .body(csvBytes);
                });
    }

}
