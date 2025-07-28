package com.masi.logistics.web.rest;

import com.carevn.masi.dto.ApiResponse;
import com.carevn.masi.utils.SecurityUtils;
import com.masi.logistics.domain.DocumentCodeSequence;
import com.masi.logistics.domain.ItemLiquidation;
import com.masi.logistics.domain.PaymentRequest;
import com.masi.logistics.domain.criteria.ItemLiquidationCriteria;
import com.masi.logistics.domain.enumeration.RequestTypeEnum;
import com.masi.logistics.domain.enumeration.StatusEntity;
import com.masi.logistics.repository.ItemLiquidationRepository;
import com.masi.logistics.security.AuthoritiesConstants;
import com.masi.logistics.service.DocumentCodeSequenceService;
import com.masi.logistics.service.ItemLiquidationService;
import com.masi.logistics.service.RequestApprovalService;
import com.masi.logistics.service.dto.ItemAssetDepreciationDTO;
import com.masi.logistics.service.dto.ItemAssetTransferDTO;
import com.masi.logistics.service.dto.ItemLiquidationDTO;
import com.masi.logistics.service.dto.UpdateReview;
import com.masi.logistics.web.rest.errors.BadRequestAlertException;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import org.jetbrains.annotations.NotNull;
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
 * REST controller for managing {@link com.masi.logistics.domain.ItemLiquidation}.
 */
@RestController
@RequestMapping("/api/item-liquidations")
public class ItemLiquidationResource {

    private static final Logger log = LoggerFactory.getLogger(ItemLiquidationResource.class);

    private static final String ENTITY_NAME = "masiLogisticsItemLiquidation";
    private final RequestApprovalService requestApprovalService;

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ItemLiquidationService itemLiquidationService;
    private final DocumentCodeSequenceService documentCodeSequenceService;
    private final ItemLiquidationRepository itemLiquidationRepository;

    public ItemLiquidationResource(ItemLiquidationService itemLiquidationService, ItemLiquidationRepository itemLiquidationRepository, RequestApprovalService requestApprovalService, DocumentCodeSequenceService documentCodeSequenceService) {
        this.itemLiquidationService = itemLiquidationService;
        this.itemLiquidationRepository = itemLiquidationRepository;
        this.requestApprovalService = requestApprovalService;
        this.documentCodeSequenceService = documentCodeSequenceService;
    }

    @GetMapping("/next-code")
    public Mono<ResponseEntity<DocumentCodeSequence>> getNextCode() {
        return documentCodeSequenceService.makeSureDocumentCodeSequenceExist(ItemLiquidation.ENTITY_NAME, "%05d").then(
                documentCodeSequenceService.getByDocumentType(ItemLiquidation.ENTITY_NAME)
                        .flatMap(documentCodeSequence -> Mono.just(ResponseEntity.ok().body(documentCodeSequence)))
        );
    }

    /**
     * {@code POST  /item-liquidations} : Create a new itemLiquidation.
     *
     * @param itemLiquidationDTO the itemLiquidationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new itemLiquidationDTO, or with status {@code 400 (Bad Request)} if the itemLiquidation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<ItemLiquidationDTO>> createItemLiquidation(@RequestBody ItemLiquidationDTO itemLiquidationDTO)
            throws URISyntaxException {
        log.debug("REST request to save ItemLiquidation : {}", itemLiquidationDTO);
        itemLiquidationDTO.setId(UUID.randomUUID());
        return itemLiquidationService
                .save(itemLiquidationDTO)
                .map(result -> {
                    try {
                        return ResponseEntity.created(new URI("/api/item-liquidations/" + result.getId()))
                                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                                .body(result);
                    } catch (URISyntaxException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    /**
     * {@code PATCH  /item-liquidations/:id} : Partial updates given fields of an existing itemLiquidation, field will ignore if it is null
     *
     * @param id                 the id of the itemLiquidationDTO to save.
     * @param itemLiquidationDTO the itemLiquidationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated itemLiquidationDTO,
     * or with status {@code 400 (Bad Request)} if the itemLiquidationDTO is not valid,
     * or with status {@code 404 (Not Found)} if the itemLiquidationDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the itemLiquidationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = {"application/json", "application/merge-patch+json"})
    public Mono<ResponseEntity<ItemLiquidationDTO>> partialUpdateItemLiquidation(
            @PathVariable(value = "id", required = false) final UUID id,
            @RequestBody ItemLiquidationDTO itemLiquidationDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ItemLiquidation partially : {}, {}", id, itemLiquidationDTO);
        itemLiquidationDTO.setId(id);
        return itemLiquidationRepository
                .existsById(id)
                .flatMap(exists -> {
                    if (!exists) {
                        return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                    }

                    Mono<ItemLiquidationDTO> result = itemLiquidationService.partialUpdate(itemLiquidationDTO);

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
     * {@code GET  /item-liquidations} : get all the itemLiquidations.
     *
     * @param pageable the pagination information.
     * @param request  a {@link ServerHttpRequest} request.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of itemLiquidations in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<ApiResponse<ItemLiquidationDTO>>> getAllItemLiquidations(
            ItemLiquidationCriteria criteria,
            @org.springdoc.core.annotations.ParameterObject Pageable pageable,
            ServerHttpRequest request
    ) {
        log.debug("REST request to get ItemLiquidations by criteria: {}", criteria);
        return SecurityUtils.getUserJWTDetail().flatMap(user -> {
            if (!user.getAuthorities().contains(AuthoritiesConstants.SUPER_ADMIN)) {
                var company = (criteria.getCompany() == null ? new StringFilter() : criteria.getCompany()).getEquals();
                if (company == null) {
                    criteria.company().setEquals(user.getCompanyId());
                }
            } else {
                criteria.company().setEquals(user.getCompanyId());
            }
            return itemLiquidationService
                    .countByCriteria(criteria)
                    .zipWith(itemLiquidationService.findByCriteria(criteria, pageable).collectList())
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
     * {@code GET  /item-liquidations/:id} : get the "id" itemLiquidation.
     *
     * @param id the id of the itemLiquidationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the itemLiquidationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<ItemLiquidationDTO>> getItemLiquidation(@PathVariable("id") UUID id) {
        log.debug("REST request to get ItemLiquidation : {}", id);
        Mono<ItemLiquidationDTO> itemLiquidationDTO = itemLiquidationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(itemLiquidationDTO);
    }

    @GetMapping("/item-info/{id}")
    public Mono<ResponseEntity<List<ItemLiquidationDTO>>> getItemAssetTransferByItemInfo(@PathVariable("id") UUID id) {
        log.debug("REST request to get ItemAssetTransfer: {}", id);
        return itemLiquidationService.findOneByItemInfo(id)
                .collectList()
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    /**
     * {@code DELETE  /item-liquidations/:id} : delete the "id" itemLiquidation.
     *
     * @param id the id of the itemLiquidationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteItemLiquidation(@PathVariable("id") UUID id) {
        log.debug("REST request to delete ItemLiquidation : {}", id);
        return itemLiquidationService
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
        return itemLiquidationService.setStatus(id, StatusEntity.WAITING_APPROVED)
                .then(requestApprovalService.updateCleanAgain(id))
                .thenReturn(ResponseEntity.ok(Map.of("id", id)));
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
                    return itemLiquidationService.setStatus(e.getDocumentId(), StatusEntity.APPROVED)
                            .then(itemLiquidationService.handleConfirmItemLiquidation(e.getDocumentId()))
                            .then(Mono.just(ResponseEntity.ok(Map.of("status", "APPROVED"))))
                            ;
                }
                if (isOneRejected) {
                    return itemLiquidationService.setStatus(e.getDocumentId(), StatusEntity.REJECTED).then(Mono.just(ResponseEntity.ok(Map.of("status", "REJECTED"))));
                }
                return Mono.just(ResponseEntity.ok(Map.of("status", "PENDING")));
            });

        });
    }

    @GetMapping("/export")
    public Mono<ResponseEntity<byte[]>> exportInventories(
            ItemLiquidationCriteria criteria,
            @org.springdoc.core.annotations.ParameterObject Pageable pageable,
            ServerHttpRequest request
    ) {
        return itemLiquidationService.exportRecordsAsCSV(criteria, pageable)
                .map(csvBytes -> {
                    log.debug("REST request to export Inventories as CSV");

                    return ResponseEntity.ok()
                            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"ItemLiquidation" + ".xlsx\"")
                            .contentType(org.springframework.http.MediaType.APPLICATION_OCTET_STREAM)
                            .body(csvBytes);
                });
    }

    @PatchMapping(value = "/{id}/cancel")
    public Mono<ResponseEntity<Map<String, UUID>>> requestCancel(@PathVariable UUID id) {
        return itemLiquidationService.setStatus(id, StatusEntity.CANCELLED)
                .then(requestApprovalService.updateCleanAgain(id))
                .thenReturn(ResponseEntity.ok(Map.of("id", id)));
    }
}
