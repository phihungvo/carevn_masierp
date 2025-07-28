package com.masi.logistics.web.rest;

import com.carevn.masi.dto.ApiResponse;
import com.masi.logistics.domain.DocumentCodeSequence;
import com.masi.logistics.domain.RequestApproval;
import com.masi.logistics.domain.SupplierContract;
import com.masi.logistics.domain.criteria.SupplierContractCriteria;
import com.masi.logistics.domain.enumeration.ContractStatus;
import com.masi.logistics.domain.enumeration.RequestTypeEnum;
import com.masi.logistics.repository.SupplierContractRepository;
import com.masi.logistics.service.DocumentCodeSequenceService;
import com.masi.logistics.service.RequestApprovalService;
import com.masi.logistics.service.SupplierContractService;
import com.masi.logistics.service.dto.CreateReviewRequest;
import com.masi.logistics.service.dto.RequestApprovalDTO;
import com.masi.logistics.service.dto.SupplierContractDTO;
import com.masi.logistics.service.dto.UpdateReview;
import com.masi.logistics.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link com.masi.logistics.domain.SupplierContract}.
 */
@RestController
@RequestMapping("/api/supplier-contracts")
public class SupplierContractResource {

    private static final Logger LOG = LoggerFactory.getLogger(SupplierContractResource.class);

    private static final String ENTITY_NAME = "masiLogisticsSupplierContract";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SupplierContractService supplierContractService;

    private final RequestApprovalService requestApprovalService;
    private final DocumentCodeSequenceService documentCodeSequenceService;
    private final SupplierContractRepository supplierContractRepository;

    public SupplierContractResource(
        SupplierContractService supplierContractService, RequestApprovalService requestApprovalService, DocumentCodeSequenceService documentCodeSequenceService,
        SupplierContractRepository supplierContractRepository
    ) {
        this.supplierContractService = supplierContractService;
        this.requestApprovalService = requestApprovalService;
        this.documentCodeSequenceService = documentCodeSequenceService;
        this.supplierContractRepository = supplierContractRepository;
    }

    /**
     * {@code POST  /supplier-contracts} : Create a new supplierContract.
     *
     * @param supplierContractDTO the supplierContractDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new supplierContractDTO, or with status {@code 400 (Bad Request)} if the supplierContract has already an ID.
     */
    @PostMapping("")
    public Mono<ResponseEntity<SupplierContractDTO>> createSupplierContract(@Valid @RequestBody SupplierContractDTO supplierContractDTO) {

        supplierContractDTO.setId(UUID.randomUUID());
        return supplierContractService
            .save(supplierContractDTO)
            .handle((result, sink) -> {
                try {
                    sink.next(ResponseEntity.created(new URI("/api/supplier-contracts/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result));
                } catch (URISyntaxException e) {
                    sink.error(new RuntimeException(e));
                }
            });
    }

    @PatchMapping(value = "/{id}/send-approve")
    public Mono<ResponseEntity<Map>> requestReview(@PathVariable UUID id) {
        return supplierContractService.sendForApproval(id).then(Mono.just(ResponseEntity.ok(Map.of("id", id))));
    }

    @PatchMapping(value = "/{id}/send-liquidation")
    public Mono<ResponseEntity<Map>> requestLiquidation(@PathVariable UUID id) {
        return supplierContractService.requestLiquidation(id).then(Mono.just(ResponseEntity.ok(Map.of("id", id))));
    }

    public record UpdateStatusRequest(ContractStatus status) {}

    @PatchMapping(value = "/{id}/update-status")
    public Mono<ResponseEntity<SupplierContractDTO>> updateStatus(@PathVariable UUID id, @RequestBody UpdateStatusRequest status) {
        return supplierContractService.updateStatus(id, status.status).map(supplierContractDTO -> ResponseEntity.ok().body(supplierContractDTO));
    }

    @GetMapping("/next-code")
    public Mono<ResponseEntity<DocumentCodeSequence>> getNextCode(@ParameterObject RequestTypeEnum type) {
        LOG.debug("REST request to get next invoice no");
        return documentCodeSequenceService.makeSureDocumentCodeSequenceExist(SupplierContract.ENTITY_NAME, "%04d").then(
            documentCodeSequenceService.getByDocumentType(SupplierContract.ENTITY_NAME)
                .flatMap(documentCodeSequence -> Mono.just(ResponseEntity.ok().body(documentCodeSequence)))
        );
    }

    @PatchMapping(value = "/{id}/approve")
    public Mono<ResponseEntity<SupplierContractDTO>> approve(@PathVariable("id") UUID id, @RequestBody RequestApprovalDTO requestApprovalDTO) {
        var approve = supplierContractService.approve(id, requestApprovalDTO);
        return approve.map(paymentRequestDTOs -> ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, paymentRequestDTOs.getId().toString()))
            .body(paymentRequestDTOs)
        );
    }

    @PatchMapping(value = "/{id}/reject")
    public Mono<ResponseEntity<SupplierContractDTO>> reject(@PathVariable("id") UUID id, @RequestBody RequestApprovalDTO requestApprovalDTO) {
        var reject = supplierContractService.reject(id, requestApprovalDTO);
        return reject.map(supplierContractDTO -> ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, supplierContractDTO.getId().toString()))
            .body(supplierContractDTO)
        );
    }

    // approve liquidation
    /**
     * {@code PATCH  /supplier-contracts/:id/approve-liquidation} : Approve the liquidation of a supplier contract.
     *
     * @param id the id of the supplier contract to approve liquidation.
     * @param requestApprovalDTO the request approval details.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated supplier contract,
     * or with status {@code 400 (Bad Request)} if the request approval details are not valid,
     * or with status {@code 404 (Not Found)} if the supplier contract is not found,
     * or with status {@code 500 (Internal Server Error)} if the supplier contract couldn't be updated.
     */
    @PatchMapping(value = "/{id}/approve-liquidation")
    public Mono<ResponseEntity<SupplierContractDTO>> approveLiquidation(@PathVariable("id") UUID id, @RequestBody RequestApprovalDTO requestApprovalDTO) {
        var approve = supplierContractService.approveLiquidation(id, requestApprovalDTO);
        return approve.map(supplierContractDTO -> ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, supplierContractDTO.getId().toString()))
            .body(supplierContractDTO)
        );
    }

    // reject liquidation
    /**
     * {@code PATCH  /supplier-contracts/:id/reject-liquidation} : Reject the liquidation of a supplier contract.
     *
     * @param id the id of the supplier contract to reject liquidation.
     * @param requestApprovalDTO the request approval details.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated supplier contract,
     * or with status {@code 400 (Bad Request)} if the request approval details are not valid,
     * or with status {@code 404 (Not Found)} if the supplier contract is not found,
     * or with status {@code 500 (Internal Server Error)} if the supplier contract couldn't be updated.
     */
    @PatchMapping(value = "/{id}/reject-liquidation")
    public Mono<ResponseEntity<SupplierContractDTO>> rejectLiquidation(@PathVariable("id") UUID id, @RequestBody RequestApprovalDTO requestApprovalDTO) {
        var reject = supplierContractService.rejectLiquidation(id, requestApprovalDTO);
        return reject.map(supplierContractDTO -> ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, supplierContractDTO.getId().toString()))
            .body(supplierContractDTO)
        );
    }

    /**
     * {@code PATCH  /supplier-contracts/:id} : Partial updates given fields of an existing supplierContract, field will ignore if it is null
     *
     * @param id                  the id of the supplierContractDTO to save.
     * @param supplierContractDTO the supplierContractDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated supplierContractDTO,
     * or with status {@code 400 (Bad Request)} if the supplierContractDTO is not valid,
     * or with status {@code 404 (Not Found)} if the supplierContractDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the supplierContractDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = {"application/json", "application/merge-patch+json"})
    public Mono<ResponseEntity<SupplierContractDTO>> partialUpdateSupplierContract(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody SupplierContractDTO supplierContractDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update SupplierContract partially : {}, {}", id, supplierContractDTO);
        supplierContractDTO.setId(id);
        return supplierContractRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<SupplierContractDTO> result = supplierContractService.partialUpdate(supplierContractDTO);

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
     * {@code GET  /supplier-contracts} : get all the supplierContracts.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of supplierContracts in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<ApiResponse<SupplierContractDTO>>> getAllSupplierContracts(
        SupplierContractCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get SupplierContracts by criteria: {}", criteria);
        return ApiResponse.from(supplierContractService.findByCriteria(criteria, pageable), supplierContractService.countByCriteria(criteria))
            .map(response -> ResponseEntity.ok().body(response));
    }


    /**
     * {@code GET  /supplier-contracts/:id} : get the "id" supplierContract.
     *
     * @param id the id of the supplierContractDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the supplierContractDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<SupplierContractDTO>> getSupplierContract(@PathVariable("id") UUID id) {
        LOG.debug("REST request to get SupplierContract : {}", id);
        Mono<SupplierContractDTO> supplierContractDTO = supplierContractService.findOne(id);
        return ResponseUtil.wrapOrNotFound(supplierContractDTO);
    }

    /**
     * {@code DELETE  /supplier-contracts/:id} : delete the "id" supplierContract.
     *
     * @param id the id of the supplierContractDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteSupplierContract(@PathVariable("id") UUID id) {
        LOG.debug("REST request to delete SupplierContract : {}", id);
        return supplierContractService
            .delete(id)
            .then(
                Mono.just(
                    ResponseEntity.noContent()
                        .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                        .build()
                )
            );
    }

    // cancel contract
    @PatchMapping(value = "/{id}/cancel")
    public Mono<ResponseEntity<Map>> cancel(@PathVariable UUID id) {
        return supplierContractService.setStatus(id, ContractStatus.CANCELLED).then(Mono.just(ResponseEntity.ok(Map.of("id", id))));
    }
}
