package com.masi.logistics.web.rest;

import com.carevn.masi.dto.ApiResponse;
import com.masi.logistics.domain.DocumentCodeSequence;
import com.masi.logistics.domain.ItemAssetTransfer;
import com.masi.logistics.domain.SuppliesRequest;
import com.masi.logistics.domain.criteria.ItemAssetDepreciationCriteria;
import com.masi.logistics.domain.criteria.ItemAssetTransferCriteria;
import com.masi.logistics.domain.enumeration.StatusEntity;
import com.masi.logistics.repository.ItemAssetTransferRepository;
import com.masi.logistics.service.DocumentCodeSequenceService;
import com.masi.logistics.service.ItemAssetTransferService;
import com.masi.logistics.service.RequestApprovalService;
import com.masi.logistics.service.dto.ItemAssetTransferDTO;
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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link com.masi.logistics.domain.ItemAssetTransfer}.
 */
@RestController
@RequestMapping("/api/item-asset-transfers")
public class ItemAssetTransferResource {

    private static final Logger log = LoggerFactory.getLogger(ItemAssetTransferResource.class);

    private static final String ENTITY_NAME = "masiLogisticsItemAssetTransfer";
    private final RequestApprovalService requestApprovalService;

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ItemAssetTransferService itemAssetTransferService;

    private final DocumentCodeSequenceService documentCodeSequenceService;

    private final ItemAssetTransferRepository itemAssetTransferRepository;

    public ItemAssetTransferResource(
        ItemAssetTransferService itemAssetTransferService, DocumentCodeSequenceService documentCodeSequenceService,
        ItemAssetTransferRepository itemAssetTransferRepository,
        RequestApprovalService requestApprovalService) {
        this.itemAssetTransferService = itemAssetTransferService;
        this.documentCodeSequenceService = documentCodeSequenceService;
        this.itemAssetTransferRepository = itemAssetTransferRepository;
        this.requestApprovalService = requestApprovalService;
    }

    /**
     * {@code POST  /item-asset-transfers} : Create a new itemAssetTransfer.
     *
     * @param itemAssetTransferDTO the itemAssetTransferDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new itemAssetTransferDTO, or with status {@code 400 (Bad Request)} if the itemAssetTransfer has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<ItemAssetTransferDTO>> createItemAssetTransfer(@RequestBody ItemAssetTransferDTO itemAssetTransferDTO)
        throws URISyntaxException {
        log.debug("REST request to save ItemAssetTransfer : {}", itemAssetTransferDTO);

        itemAssetTransferDTO.setId(UUID.randomUUID());
        return itemAssetTransferService
            .save(itemAssetTransferDTO)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/item-asset-transfers/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }


    /**
     * {@code PATCH  /item-asset-transfers/:id} : Partial updates given fields of an existing itemAssetTransfer, field will ignore if it is null
     *
     * @param id the id of the itemAssetTransferDTO to save.
     * @param itemAssetTransferDTO the itemAssetTransferDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated itemAssetTransferDTO,
     * or with status {@code 400 (Bad Request)} if the itemAssetTransferDTO is not valid,
     * or with status {@code 404 (Not Found)} if the itemAssetTransferDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the itemAssetTransferDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<ItemAssetTransferDTO>> partialUpdateItemAssetTransfer(
        @PathVariable(value = "id", required = false) final UUID id,
        @RequestBody ItemAssetTransferDTO itemAssetTransferDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ItemAssetTransfer partially : {}, {}", id, itemAssetTransferDTO);
        itemAssetTransferDTO.setId(id);

        return itemAssetTransferRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<ItemAssetTransferDTO> result = itemAssetTransferService.partialUpdate(itemAssetTransferDTO);

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
     * {@code GET  /item-asset-transfers} : get all the itemAssetTransfers.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of itemAssetTransfers in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<ApiResponse<ItemAssetTransferDTO>>> getAllItemAssetTransfers(
        ItemAssetTransferCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get ItemAssetTransfers by criteria: {}", criteria);
        return itemAssetTransferService
            .countByCriteria(criteria)
            .zipWith(itemAssetTransferService.findByCriteria(criteria, pageable).collectList())
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
    }

    /**
     * {@code GET  /item-asset-transfers/count} : count all the itemAssetTransfers.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public Mono<ResponseEntity<Long>> countItemAssetTransfers(ItemAssetTransferCriteria criteria) {
        log.debug("REST request to count ItemAssetTransfers by criteria: {}", criteria);
        return itemAssetTransferService.countByCriteria(criteria).map(count -> ResponseEntity.status(HttpStatus.OK).body(count));
    }

    /**
     * {@code GET  /item-asset-transfers/:id} : get the "id" itemAssetTransfer.
     *
     * @param id the id of the itemAssetTransferDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the itemAssetTransferDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<ItemAssetTransferDTO>> getItemAssetTransfer(@PathVariable("id") UUID id) {
        log.debug("REST request to get ItemAssetTransfer : {}", id);
        Mono<ItemAssetTransferDTO> itemAssetTransferDTO = itemAssetTransferService.findOne(id);
        return ResponseUtil.wrapOrNotFound(itemAssetTransferDTO);
    }

    @GetMapping("/item-info/{id}")
    public Mono<ResponseEntity<List<ItemAssetTransferDTO>>> getItemAssetTransferByItemInfo(@PathVariable("id") UUID id) {
        log.debug("REST request to get ItemAssetTransfer: {}", id);
        return itemAssetTransferService.findOneByItemInfo(id)
                .collectList()
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    /**
     * {@code DELETE  /item-asset-transfers/:id} : delete the "id" itemAssetTransfer.
     *
     * @param id the id of the itemAssetTransferDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteItemAssetTransfer(@PathVariable("id") UUID id) {
        log.debug("REST request to delete ItemAssetTransfer : {}", id);
        return itemAssetTransferService
            .delete(id)
            .then(
                Mono.just(
                    ResponseEntity.noContent()
                        .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                        .build()
                )
            );
    }

    @GetMapping("/export")
    public Mono<ResponseEntity<byte[]>> exportInventories(
            ItemAssetTransferCriteria criteria,
            @org.springdoc.core.annotations.ParameterObject Pageable pageable,
            ServerHttpRequest request
    ) {
        return itemAssetTransferService.exportRecordsAsCSV(criteria, pageable)
                .map(csvBytes -> {
                    log.debug("REST request to export Inventories as CSV");

                    return ResponseEntity.ok()
                            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"ItemAssetDepreciation" + ".xlsx\"")
                            .contentType(org.springframework.http.MediaType.APPLICATION_OCTET_STREAM)
                            .body(csvBytes);
                });
    }

    @GetMapping("/code/next")
    public Mono<ResponseEntity<DocumentCodeSequence>> getNextCodeNo() {
        return documentCodeSequenceService.getByDocumentType(ItemAssetTransfer.ENTITY_NAME)
            .map(ResponseEntity::ok);
    }

    @PatchMapping(value = "/{id}/cancel")
    public Mono<ResponseEntity<Map<String, UUID>>> requestCancel(@PathVariable UUID id) {
        return itemAssetTransferService.setStatus(id, StatusEntity.CANCELLED)
                .thenReturn(ResponseEntity.ok(Map.of("id", id)));
    }

    @PatchMapping(value = "/{id}/confirm")
    public Mono<ResponseEntity<Map<String, UUID>>> requestConfirm(@PathVariable UUID id) {
        return itemAssetTransferService.setStatus(id, StatusEntity.COMPLETED)
                .then(itemAssetTransferService.handleConfirm(id))
                .thenReturn(ResponseEntity.ok(Map.of("id", id)));
    }

    @PatchMapping(value = "/{id}/request-review")
    public Mono<ResponseEntity<Map<String, UUID>>> requestReview(@PathVariable UUID id) {
        return itemAssetTransferService.setStatus(id, StatusEntity.WAITING_APPROVED)
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
                    return itemAssetTransferService.setStatus(e.getDocumentId(), StatusEntity.APPROVED)
                            .then(itemAssetTransferService.handleConfirm(e.getId()))
                            .then(Mono.just(ResponseEntity.ok(Map.of("status", "APPROVED"))))
                            ;
                }
                if (isOneRejected) {
                    return itemAssetTransferService.setStatus(e.getDocumentId(), StatusEntity.REJECTED).then(Mono.just(ResponseEntity.ok(Map.of("status", "REJECTED"))));
                }
                return Mono.just(ResponseEntity.ok(Map.of("status", "PENDING")));
            });

        });
    }
}
