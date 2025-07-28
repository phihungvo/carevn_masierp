package com.masi.logistics.web.rest;

import com.carevn.masi.dto.ApiResponse;
import com.carevn.masi.utils.SecurityUtils;
import com.masi.logistics.domain.criteria.InventoriesCriteria;
import com.masi.logistics.domain.criteria.ItemAssetDepreciationCriteria;
import com.masi.logistics.domain.enumeration.StatusEntity;
import com.masi.logistics.repository.ItemAssetDepreciationRepository;
import com.masi.logistics.security.AuthoritiesConstants;
import com.masi.logistics.service.ItemAssetDepreciationService;
import com.masi.logistics.service.RequestApprovalService;
import com.masi.logistics.service.dto.ItemAssetDepreciationDTO;
import com.masi.logistics.service.dto.ItemAssetDepreciationDetailDTO;
import com.masi.logistics.service.dto.ItemLiquidationDTO;
import com.masi.logistics.service.dto.UpdateReview;
import com.masi.logistics.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import jakarta.validation.constraints.NotNull;
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
 * REST controller for managing {@link com.masi.logistics.domain.ItemAssetDepreciation}.
 */
@RestController
@RequestMapping("/api/item-asset-depreciations")
public class ItemAssetDepreciationResource {

    private static final Logger log = LoggerFactory.getLogger(ItemAssetDepreciationResource.class);

    private static final String ENTITY_NAME = "masiLogisticsItemAssetDepreciation";
    private final RequestApprovalService requestApprovalService;

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ItemAssetDepreciationService itemAssetDepreciationService;

    private final ItemAssetDepreciationRepository itemAssetDepreciationRepository;

    public ItemAssetDepreciationResource(
        ItemAssetDepreciationService itemAssetDepreciationService,
        ItemAssetDepreciationRepository itemAssetDepreciationRepository,
        RequestApprovalService requestApprovalService) {
        this.itemAssetDepreciationService = itemAssetDepreciationService;
        this.itemAssetDepreciationRepository = itemAssetDepreciationRepository;
        this.requestApprovalService = requestApprovalService;
    }

    /**
     * {@code POST  /item-asset-depreciations} : Create a new itemAssetDepreciation.
     *
     * @param itemAssetDepreciationDTO the itemAssetDepreciationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new itemAssetDepreciationDTO, or with status {@code 400 (Bad Request)} if the itemAssetDepreciation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<ItemAssetDepreciationDTO>> createItemAssetDepreciation(
        @RequestBody ItemAssetDepreciationDTO itemAssetDepreciationDTO
    ) throws URISyntaxException {
        log.debug("REST request to save ItemAssetDepreciation : {}", itemAssetDepreciationDTO);
        itemAssetDepreciationDTO.setId(UUID.randomUUID());
        return itemAssetDepreciationService
            .save(itemAssetDepreciationDTO)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/item-asset-depreciations/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }


    /**
     * {@code PATCH  /item-asset-depreciations/:id} : Partial updates given fields of an existing itemAssetDepreciation, field will ignore if it is null
     *
     * @param id the id of the itemAssetDepreciationDTO to save.
     * @param itemAssetDepreciationDTO the itemAssetDepreciationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated itemAssetDepreciationDTO,
     * or with status {@code 400 (Bad Request)} if the itemAssetDepreciationDTO is not valid,
     * or with status {@code 404 (Not Found)} if the itemAssetDepreciationDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the itemAssetDepreciationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<ItemAssetDepreciationDTO>> partialUpdateItemAssetDepreciation(
        @PathVariable(value = "id", required = false) final UUID id,
        @RequestBody ItemAssetDepreciationDTO itemAssetDepreciationDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ItemAssetDepreciation partially : {}, {}", id, itemAssetDepreciationDTO);
        itemAssetDepreciationDTO.setId(id);
        return itemAssetDepreciationRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<ItemAssetDepreciationDTO> result = itemAssetDepreciationService.partialUpdate(itemAssetDepreciationDTO);

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
    @GetMapping("/item-info/{id}")
    public Mono<ResponseEntity<List<ItemAssetDepreciationDTO>>> getItemAssetTransferByItemInfo(@PathVariable("id") UUID id) {
        log.debug("REST request to get ItemAssetTransfer: {}", id);
        return itemAssetDepreciationService.findOneByItemInfo(id)
                .collectList()
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    /**
     * {@code GET  /item-asset-depreciations} : get all the itemAssetDepreciations.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of itemAssetDepreciations in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<ApiResponse<ItemAssetDepreciationDTO>>> getAllItemAssetDepreciations(
        ItemAssetDepreciationCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get ItemAssetDepreciations by criteria: {}", criteria);
        return SecurityUtils.getUserJWTDetail().flatMap(user -> {
                    if (!user.getAuthorities().contains(AuthoritiesConstants.SUPER_ADMIN)) {
                        var company = (criteria.getCompany() == null ? new StringFilter() : criteria.getCompany()).getEquals();
                        if (company == null) {
                            criteria.company().setEquals(user.getCompanyId());
                        }
                    } else {
                        criteria.company().setEquals(user.getCompanyId());
                    }
        return itemAssetDepreciationService
            .countByCriteria(criteria)
            .zipWith(itemAssetDepreciationService.findByCriteria(criteria, pageable).collectList())
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

    @GetMapping(value = "detail/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<ApiResponse<ItemAssetDepreciationDetailDTO>>> getInventoriesStorageDetail(
            @PathVariable("id") UUID id,
            @org.springdoc.core.annotations.ParameterObject Pageable pageable,
            ServerHttpRequest request
    ) {
        log.debug("REST request to get InventoriesStorage by criteria: {}", id);
        return itemAssetDepreciationService
                .countDetailById(id)
                .zipWith(itemAssetDepreciationService.findDetailById(id, pageable).collectList())
                .map(countWithEntities -> {
                    var page = new PageImpl<>(countWithEntities.getT2(), pageable, countWithEntities.getT1());
                    return ResponseEntity.ok()
                            .headers(
                                    PaginationUtil.generatePaginationHttpHeaders(
                                            ForwardedHeaderUtils.adaptFromForwardedHeaders(request.getURI(), request.getHeaders()),
                                            page
                                    )
                            )
                            .body(new ApiResponse<>(countWithEntities.getT2(), countWithEntities.getT1()));
                });
    }



    /**
     * {@code GET  /item-asset-depreciations/:id} : get the "id" itemAssetDepreciation.
     *
     * @param id the id of the itemAssetDepreciationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the itemAssetDepreciationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<ItemAssetDepreciationDTO>> getItemAssetDepreciation(@PathVariable("id") UUID id) {
        log.debug("REST request to get ItemAssetDepreciation : {}", id);
        Mono<ItemAssetDepreciationDTO> itemAssetDepreciationDTO = itemAssetDepreciationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(itemAssetDepreciationDTO);
    }

    /**
     * {@code DELETE  /item-asset-depreciations/:id} : delete the "id" itemAssetDepreciation.
     *
     * @param id the id of the itemAssetDepreciationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteItemAssetDepreciation(@PathVariable("id") UUID id) {
        log.debug("REST request to delete ItemAssetDepreciation : {}", id);
        return itemAssetDepreciationService
            .delete(id)
            .then(
                Mono.just(
                    ResponseEntity.noContent()
                        .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                        .build()
                )
            );
    }

    @PatchMapping(value = "/{id}/cancel")
    public Mono<ResponseEntity<Map<String, UUID>>> cancel(@PathVariable UUID id) {
        return itemAssetDepreciationService.setStatus(id, StatusEntity.CANCELLED)
                .thenReturn(ResponseEntity.ok(Map.of("id", id)));
    }

    @GetMapping("/export")
    public Mono<ResponseEntity<byte[]>> exportInventories(
            ItemAssetDepreciationCriteria criteria,
            @org.springdoc.core.annotations.ParameterObject Pageable pageable,
            ServerHttpRequest request
    ) {
        return itemAssetDepreciationService.exportRecordsAsCSV(criteria, pageable)
                .map(csvBytes -> {
                    log.debug("REST request to export Inventories as CSV");

                    return ResponseEntity.ok()
                            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"ItemAssetDepreciation" + ".xlsx\"")
                            .contentType(org.springframework.http.MediaType.APPLICATION_OCTET_STREAM)
                            .body(csvBytes);
                });
    }

    @PatchMapping(value = "/{id}/request-review")
    public Mono<ResponseEntity<Map<String, UUID>>> requestReview(@PathVariable UUID id) {
        return itemAssetDepreciationService.setStatus(id, StatusEntity.WAITING_APPROVED)
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
                        .anyMatch(requestApproval -> {
                            if (requestApproval.getResult() == null) {
                                return false;
                            }
                            return !requestApproval.getResult();
                        });

                if (isAllApproved) {
                    return itemAssetDepreciationService.setStatus(e.getDocumentId(), StatusEntity.APPROVED)
                            .then(itemAssetDepreciationService.handleDepreciation(e.getDocumentId()))
                            .then(Mono.just(ResponseEntity.ok(Map.of("status", "APPROVED"))));
                }
                if (isOneRejected) {
                    return itemAssetDepreciationService.setStatus(e.getDocumentId(), StatusEntity.REJECTED).then(Mono.just(ResponseEntity.ok(Map.of("status", "REJECTED"))));
                }
                return Mono.just(ResponseEntity.ok(Map.of("status", "PENDING")));
            });

        });
    }

}
