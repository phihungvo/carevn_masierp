package com.masi.logistics.web.rest;

import com.carevn.masi.dto.ApiResponse;
import com.masi.logistics.domain.DeliverySchedule;
import com.masi.logistics.domain.DocumentCodeSequence;
import com.masi.logistics.domain.criteria.DeliveryScheduleCriteria;
import com.masi.logistics.repository.DeliveryScheduleRepository;
import com.masi.logistics.service.DeliveryScheduleService;
import com.masi.logistics.service.DocumentCodeSequenceService;
import com.masi.logistics.service.RequestApprovalService;
import com.masi.logistics.service.dto.*;
import com.masi.logistics.web.rest.errors.BadRequestAlertException;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
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
 * REST controller for managing {@link com.masi.logistics.domain.DeliverySchedule}.
 */
@RestController
@RequestMapping("/api/delivery-schedules")
public class DeliveryScheduleResource {

    private static final Logger LOG = LoggerFactory.getLogger(DeliveryScheduleResource.class);

    private static final String ENTITY_NAME = "masiLogisticsDeliverySchedule";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;
    private final DocumentCodeSequenceService documentCodeSequenceService;

    private final DeliveryScheduleService deliveryScheduleService;

    private final DeliveryScheduleRepository deliveryScheduleRepository;
    private final RequestApprovalService requestApprovalService;

    public DeliveryScheduleResource(DocumentCodeSequenceService documentCodeSequenceService, DeliveryScheduleService deliveryScheduleService, DeliveryScheduleRepository deliveryScheduleRepository, RequestApprovalService requestApprovalService) {
        this.documentCodeSequenceService = documentCodeSequenceService;
        this.deliveryScheduleService = deliveryScheduleService;
        this.deliveryScheduleRepository = deliveryScheduleRepository;
        this.requestApprovalService = requestApprovalService;
    }


    @PostMapping("")
    public Mono<ResponseEntity<DeliveryScheduleDTO>> createDeliverySchedule(@Valid @RequestBody DeliveryScheduleDTO deliveryScheduleDTO) throws URISyntaxException {

        deliveryScheduleDTO.setId(UUID.randomUUID());
        return deliveryScheduleService.save(deliveryScheduleDTO).handle((result, sink) -> {
            try {
                sink.next(ResponseEntity.created(new URI("/api/delivery-schedules/" + result.getId())).headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString())).body(result));
            } catch (URISyntaxException e) {
                sink.error(new RuntimeException(e));
            }
        });
    }

    @GetMapping("/code/next")
    public Mono<ResponseEntity<DocumentCodeSequence>> getNextCodeNo() {
        LOG.debug("REST request to get next invoice no");
        return documentCodeSequenceService.getByDocumentType(DeliverySchedule.ENTITY_NAME)
            .map(ResponseEntity::ok);
    }


    @PatchMapping(value = "/{id}", consumes = {"application/json", "application/merge-patch+json"})
    public Mono<ResponseEntity<DeliveryScheduleDTO>> partialUpdateDeliverySchedule(@PathVariable(value = "id", required = false) final UUID id, @NotNull @RequestBody DeliveryScheduleDTO deliveryScheduleDTO) throws URISyntaxException {
        LOG.debug("REST request to partial update DeliverySchedule partially : {}, {}", id, deliveryScheduleDTO);
        deliveryScheduleDTO.setId(id);

        return deliveryScheduleRepository.existsById(id).flatMap(exists -> {
            if (!exists) {
                return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
            }
            Mono<DeliveryScheduleDTO> result = deliveryScheduleService.partialUpdate(deliveryScheduleDTO);
            return result.switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND))).map(res -> ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, res.getId().toString())).body(res));
        });
    }

    @Operation(summary = "Set delivery schedule status to DELIVERED")
    @PatchMapping(value = "/{id}/delivered")
    public Mono<ResponseEntity<DeliveryScheduleDTO>> delivered(@PathVariable UUID id) {
        return deliveryScheduleService.markAsDelivered(id).map(deliveryScheduleDTO -> ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, deliveryScheduleDTO.getId().toString())).body(deliveryScheduleDTO));
    }

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<ApiResponse<DeliveryScheduleDTO>>> getAllDeliverySchedules(DeliveryScheduleQuery query, @org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        return ApiResponse.from(deliveryScheduleService.findByCriteria(query.toCriteria(), pageable), deliveryScheduleService.countByCriteria(query.toCriteria())).map(response -> ResponseEntity.ok().body(response));
    }


    /**
     * {@code GET  /delivery-schedules/:id} : get the "id" deliverySchedule.
     *
     * @param id the id of the deliveryScheduleDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the deliveryScheduleDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<DeliveryScheduleDTO>> getDeliverySchedule(@PathVariable("id") UUID id) {
        LOG.debug("REST request to get DeliverySchedule : {}", id);
        Mono<DeliveryScheduleDTO> deliveryScheduleDTO = deliveryScheduleService.findOne(id);
        return ResponseUtil.wrapOrNotFound(deliveryScheduleDTO);
    }

    /**
     * {@code DELETE  /delivery-schedules/:id} : delete the "id" deliverySchedule.
     *
     * @param id the id of the deliveryScheduleDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteDeliverySchedule(@PathVariable("id") UUID id) {
        LOG.debug("REST request to delete DeliverySchedule : {}", id);
        return deliveryScheduleService.delete(id).then(Mono.just(ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build()));
    }

    @PatchMapping(value = "/{id}/request-review")
    public Mono<ResponseEntity<Map>> requestReview(@PathVariable UUID id, CreateReviewRequest createReviewRequest) {
        createReviewRequest.setDocumentId(id);
        return requestApprovalService.requestReview(createReviewRequest).flatMap(requestApprovalDTO -> {
            return deliveryScheduleService.setStatus(id, DeliverySchedule.Status.WAITING_APPROVAL).then(Mono.just(ResponseEntity.ok(Map.of("id", requestApprovalDTO.getId()))));
        });
    }

    @PatchMapping(value = "review")
    public Mono<ResponseEntity<Map>> approve(UpdateReview updateReview) {

        return requestApprovalService.handleReview(updateReview).flatMap(e -> {
            return requestApprovalService.findByDocumentId(e.getDocumentId()).collectList().flatMap(requestApprovals -> {
                var isAllApproved = requestApprovals.stream().allMatch(requestApproval -> requestApproval.getResult() != null && requestApproval.getResult());

                var isOneRejected = requestApprovals.stream().anyMatch(requestApproval -> requestApproval.getResult() != null && !requestApproval.getResult());
                if (isAllApproved) {
                    return deliveryScheduleService.setStatus(e.getDocumentId(), DeliverySchedule.Status.APPROVED).then(Mono.just(ResponseEntity.ok(Map.of("status", "APPROVED"))));
                }
                if (isOneRejected) {
                    return deliveryScheduleService.setStatus(e.getDocumentId(), DeliverySchedule.Status.REJECTED).then(Mono.just(ResponseEntity.ok(Map.of("status", "REJECTED"))));
                }
                return Mono.just(ResponseEntity.ok(Map.of("status", "PENDING")));
            });

        });
    }
}
