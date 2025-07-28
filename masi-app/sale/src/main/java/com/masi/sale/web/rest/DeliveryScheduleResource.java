package com.masi.sale.web.rest;

import com.carevn.masi.dto.ApiResponse;
import com.masi.sale.repository.DeliveryScheduleRepository;
import com.masi.sale.service.DeliveryScheduleService;
import com.masi.sale.service.dto.*;
import com.masi.sale.service.dto.reponse.DeliveryScheduleResponse;
import com.masi.sale.service.dto.request.DeliveryScheduleRequest;
import com.masi.sale.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * REST controller for managing {@link com.masi.sale.domain.DeliverySchedule}.
 */
@RestController
@RequestMapping("/api/delivery-schedules")
public class DeliveryScheduleResource {

    private static final Logger log = LoggerFactory.getLogger(DeliveryScheduleResource.class);

    private static final String ENTITY_NAME = "masiSaleDeliverySchedule";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DeliveryScheduleService deliveryScheduleService;

    private final DeliveryScheduleRepository deliveryScheduleRepository;

    public DeliveryScheduleResource(
        DeliveryScheduleService deliveryScheduleService,
        DeliveryScheduleRepository deliveryScheduleRepository
    ) {
        this.deliveryScheduleService = deliveryScheduleService;
        this.deliveryScheduleRepository = deliveryScheduleRepository;
    }

    /**
     * {@code POST  /delivery-schedules} : Create a new deliverySchedule.
     *
     * @param deliveryScheduleDTO the deliveryScheduleDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new deliveryScheduleDTO, or with status {@code 400 (Bad Request)} if the deliverySchedule has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<DeliveryScheduleDTO>> createDeliverySchedule(@RequestBody DeliveryScheduleDTO deliveryScheduleDTO)
        throws URISyntaxException {
        log.debug("REST request to save DeliverySchedule : {}", deliveryScheduleDTO);
        if (deliveryScheduleDTO.getId() != null) {
            throw new BadRequestAlertException("A new deliverySchedule cannot already have an ID", ENTITY_NAME, "idexists");
        }
        deliveryScheduleDTO.setId(UUID.randomUUID());
        return deliveryScheduleService
            .save(deliveryScheduleDTO)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/delivery-schedules/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }


    /**
     * {@code PATCH  /delivery-schedules/:id} : Partial updates given fields of an existing deliverySchedule, field will ignore if it is null
     *
     * @param id the id of the deliveryScheduleDTO to save.
     * @param deliveryScheduleDTO the deliveryScheduleDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated deliveryScheduleDTO,
     * or with status {@code 400 (Bad Request)} if the deliveryScheduleDTO is not valid,
     * or with status {@code 404 (Not Found)} if the deliveryScheduleDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the deliveryScheduleDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<DeliveryScheduleDTO>> partialUpdateDeliverySchedule(
        @PathVariable(value = "id", required = false) final UUID id,
        @RequestBody DeliveryScheduleDTO deliveryScheduleDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update DeliverySchedule partially : {}, {}", id, deliveryScheduleDTO);
        if (deliveryScheduleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, deliveryScheduleDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return deliveryScheduleRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<DeliveryScheduleDTO> result = deliveryScheduleService.partialUpdate(deliveryScheduleDTO);

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
     * {@code GET  /delivery-schedules} : get all the deliverySchedules.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of deliverySchedules in body.
     */

    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<ApiResponse<DeliveryScheduleResponse>>> getAllDeliverySchedulesList(
            DeliveryScheduleRequest criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get DeliverySchedules by criteria: {}", criteria);
        return deliveryScheduleService
            .countByCriteria(criteria)
            .zipWith(deliveryScheduleService.findByCriteriaList(criteria, pageable).collectList())
                .map(countWithEntities -> ResponseEntity.ok().body(new ApiResponse<>(countWithEntities.getT2(), countWithEntities.getT1())));
    }

    @GetMapping(value = "/calendar", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<ApiResponse<DeliveryScheduleDTO>>> getAllDeliverySchedulesCalendar(
            DeliveryScheduleRequest criteria,
            @org.springdoc.core.annotations.ParameterObject Pageable pageable,
            ServerHttpRequest request
    ) {
        log.debug("REST request to get DeliverySchedules by criteria: {}", criteria);
        return deliveryScheduleService
                .countByCriteria(criteria)
                .zipWith(deliveryScheduleService.findByCriteriaCalendar(criteria, pageable).collectList())
                .map(countWithEntities -> ResponseEntity.ok().body(new ApiResponse<>(countWithEntities.getT2(), countWithEntities.getT1())));
    }


    /**
     * {@code GET  /delivery-schedules/count} : count all the deliverySchedules.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public Mono<ResponseEntity<Long>> countDeliverySchedules(DeliveryScheduleRequest criteria) {
        log.debug("REST request to count DeliverySchedules by criteria: {}", criteria);
        return deliveryScheduleService.countByCriteria(criteria).map(count -> ResponseEntity.status(HttpStatus.OK).body(count));
    }

    /**
     * {@code GET  /delivery-schedules/:id} : get the "id" deliverySchedule.
     *
     * @param id the id of the deliveryScheduleDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the deliveryScheduleDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<DeliveryScheduleDTO>> getDeliverySchedule(@PathVariable("id") UUID id) {
        log.debug("REST request to get DeliverySchedule : {}", id);
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
        log.debug("REST request to delete DeliverySchedule : {}", id);
        return deliveryScheduleService
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
