package com.masi.production.web.rest;

import com.carevn.masi.dto.ApiResponse;
import com.masi.production.repository.WorkOrderRepository;
import com.masi.production.service.WorkOrderService;
import com.masi.production.service.dto.WorkOrderDTO;
import com.masi.production.service.dto.WorkOrderRO;
import com.masi.production.service.dto.WorkOrderRO;
import com.masi.production.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.annotations.ParameterObject;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link com.masi.production.domain.WorkOrder}.
 */
@RestController
@RequestMapping("/api/work-orders")
public class WorkOrderResource {

    private final Logger log = LoggerFactory.getLogger(WorkOrderResource.class);

    private static final String ENTITY_NAME = "masiProductionWorkOrder";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WorkOrderService workOrderService;

    private final WorkOrderRepository workOrderRepository;

    public WorkOrderResource(WorkOrderService workOrderService, WorkOrderRepository workOrderRepository) {
        this.workOrderService = workOrderService;
        this.workOrderRepository = workOrderRepository;
    }

    /**
     * {@code POST  /work-orders} : Create a new workOrder.
     *
     * @param workOrderDTO the workOrderDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new workOrderDTO, or with status {@code 400 (Bad Request)} if the workOrder has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<WorkOrderDTO>> createWorkOrder(@Valid @RequestBody WorkOrderDTO workOrderDTO) throws URISyntaxException {
        log.debug("REST request to save WorkOrder : {}", workOrderDTO);
        if (workOrderDTO.getId() != null) {
            throw new BadRequestAlertException("A new workOrder cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return workOrderService
            .save(workOrderDTO)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/work-orders/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PATCH  /work-orders/:id} : Partial updates given fields of an existing workOrder, field will ignore if it is null
     *
     * @param id the id of the workOrderDTO to save.
     * @param workOrderDTO the workOrderDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated workOrderDTO,
     * or with status {@code 400 (Bad Request)} if the workOrderDTO is not valid,
     * or with status {@code 404 (Not Found)} if the workOrderDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the workOrderDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<WorkOrderDTO>> partialUpdateWorkOrder(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody WorkOrderDTO workOrderDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update WorkOrder partially : {}, {}", id, workOrderDTO);
        workOrderDTO.setId(id);
        return workOrderRepository
            .existsById(id)
            .flatMap(exists -> {
                if (Boolean.FALSE.equals(exists)) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<WorkOrderDTO> result = workOrderService.partialUpdate(workOrderDTO);

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
     * {@code GET  /work-orders} : get all the workOrders.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of workOrders in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ApiResponse> getAllWorkOrders(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of WorkOrders");
        return workOrderService
            .countAllActive()
            .zipWith(workOrderService.findAllActive(pageable).collectList())
            .map(
                countWithEntities -> new ApiResponse<>(countWithEntities.getT2(), countWithEntities.getT1()));
    }

    /**
     * {@code GET  /work-orders/:id} : get the "id" workOrder.
     *
     * @param id the id of the workOrderDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the workOrderDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<WorkOrderDTO>> getWorkOrder(@PathVariable("id") UUID id,  @ParameterObject WorkOrderRO ro,@ParameterObject Pageable pageable) {
        log.debug("REST request to get WorkOrder : {}", id);
        Mono<WorkOrderDTO> workOrderDTO = workOrderService.findOne(ro, id, pageable);
        return ResponseUtil.wrapOrNotFound(workOrderDTO);
    }

    @PatchMapping(value = "/{id}/start")
    public Mono<Void> startWorkOrder(
        @PathVariable(value = "id", required = false) final UUID id
    ) throws URISyntaxException {
        log.debug("REST request to start WorkOrder : {}", id);
        return workOrderRepository
            .existsById(id)
            .flatMap(exists -> {
                if (Boolean.FALSE.equals(exists)) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }
                return workOrderService.startWorkOrder(id);
            });
    }

    @PatchMapping(value = "/{id}/stop")
    public Mono<Void> stopWorkOrder(
        @PathVariable(value = "id", required = false) final UUID id
    ) throws URISyntaxException {
        log.debug("REST request to stop WorkOrder : {}", id);
        return workOrderRepository
            .existsById(id)
            .flatMap(exists -> {
                if (Boolean.FALSE.equals(exists)) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }
                return workOrderService.stopWorkOrder(id);
            });
    }

    @PatchMapping(value = "/{id}/complete")
    public Mono<Void> completeWorkOrder(
        @PathVariable(value = "id", required = false) final UUID id
    ) throws URISyntaxException {
        log.debug("REST request to complete WorkOrder : {}", id);
        return workOrderRepository
            .existsById(id)
            .flatMap(exists -> {
                if (Boolean.FALSE.equals(exists)) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }
                return workOrderService.completeWorkOrder(id);
            });
    }

    /**
     * {@code DELETE  /work-orders/:id} : delete the "id" workOrder.
     *
     * @param id the id of the workOrderDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteWorkOrder(@PathVariable("id") UUID id) {
        log.debug("REST request to delete WorkOrder : {}", id);
        return workOrderService
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
