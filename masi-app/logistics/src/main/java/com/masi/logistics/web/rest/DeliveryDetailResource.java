package com.masi.logistics.web.rest;

import com.carevn.masi.dto.ApiResponse;
import com.masi.logistics.domain.SupplierContract;
import com.masi.logistics.domain.criteria.DeliveryDetailCriteria;
import com.masi.logistics.domain.criteria.SupplierContractCriteria;
import com.masi.logistics.repository.DeliveryDetailRepository;
import com.masi.logistics.service.DeliveryDetailService;
import com.masi.logistics.service.SupplierContractService;
import com.masi.logistics.service.dto.DeliveryDetailCalendarDTO;
import com.masi.logistics.service.dto.DeliveryDetailDTO;
import com.masi.logistics.service.dto.SupplierContractDTO;
import com.masi.logistics.web.rest.errors.BadRequestAlertException;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.List;
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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.service.filter.UUIDFilter;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link com.masi.logistics.domain.DeliveryDetail}.
 */
@RestController
@RequestMapping("/api/delivery-details")
public class DeliveryDetailResource {

    private static final Logger LOG = LoggerFactory.getLogger(DeliveryDetailResource.class);

    private static final String ENTITY_NAME = "masiLogisticsDeliveryDetail";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DeliveryDetailService deliveryDetailService;

    private final SupplierContractService supplierContractService;

    private final DeliveryDetailRepository deliveryDetailRepository;

    public DeliveryDetailResource(DeliveryDetailService deliveryDetailService, SupplierContractService supplierContractService, DeliveryDetailRepository deliveryDetailRepository) {
        this.deliveryDetailService = deliveryDetailService;
        this.supplierContractService = supplierContractService;
        this.deliveryDetailRepository = deliveryDetailRepository;

    }


    /**
     * {@code POST  /delivery-details} : Create a new deliveryDetail.
     *
     * @param deliveryDetailDTO the deliveryDetailDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new deliveryDetailDTO, or with status {@code 400 (Bad Request)} if the deliveryDetail has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono <ResponseEntity<List<DeliveryDetailDTO>>> createDeliveryDetail(@RequestBody List<DeliveryDetailDTO> deliveryDetailDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save DeliveryDetail : {}", deliveryDetailDTO);

        return deliveryDetailService
            .saveAll(deliveryDetailDTO)
            .collectList()
            .map(results -> {
                try {
                    return ResponseEntity.created(new URI("/api/delivery-details"))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, ""))
                        .body(results);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /delivery-details/:id} : Updates an existing deliveryDetail.
     *
     * @param id                the id of the deliveryDetailDTO to save.
     * @param deliveryDetailDTO the deliveryDetailDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated deliveryDetailDTO,
     * or with status {@code 400 (Bad Request)} if the deliveryDetailDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the deliveryDetailDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<DeliveryDetailDTO>> updateDeliveryDetail(
        @PathVariable(value = "id", required = false) final UUID id,
        @RequestBody DeliveryDetailDTO deliveryDetailDTO
    ) throws URISyntaxException {
        deliveryDetailDTO.setId(id);

        return deliveryDetailRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return deliveryDetailService
                    .update(deliveryDetailDTO)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(result ->
                        ResponseEntity.ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                            .body(result)
                    );
            });
    }


    @PatchMapping(value = "/{id}", consumes = {"application/json", "application/merge-patch+json"})
    public Mono<ResponseEntity<DeliveryDetailDTO>> partialUpdateDeliveryDetail(
        @PathVariable(value = "id", required = false) final UUID id,
        @RequestBody DeliveryDetailDTO deliveryDetailDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update DeliveryDetail partially : {}, {}", id, deliveryDetailDTO);
        deliveryDetailDTO.setId(id);
        return deliveryDetailRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<DeliveryDetailDTO> result = deliveryDetailService.partialUpdate(deliveryDetailDTO);

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
     * {@code GET  /delivery-details} : get all the deliveryDetails.
     *
     * @param pageable the pagination information.
     * @param request  a {@link ServerHttpRequest} request.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of deliveryDetails in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<ApiResponse<DeliveryDetailDTO>>> getAllDeliveryDetails(
        DeliveryDetailCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        LOG.debug("REST request to get DeliveryDetails by criteria: {}", criteria);
        return deliveryDetailService
            .countByCriteria(criteria)
            .zipWith(deliveryDetailService.findByCriteria(criteria, pageable).collectList().flatMap(dd -> {
                var listContractIds = dd.stream().filter(Objects::nonNull)
                    .map(DeliveryDetailDTO::getSupplierContractId)
                    .distinct()
                    .toList();
                var supplierContractCriteria = new SupplierContractCriteria();
                UUIDFilter uuidFilter = new UUIDFilter();
                uuidFilter.setIn(listContractIds);
                supplierContractCriteria.setId(uuidFilter);
                return supplierContractService.findByCriteria(supplierContractCriteria, null)
                    .collectList()
                    .flatMap(supplierContracts -> {
                        dd.forEach(deliveryDetailDTO -> {
                            supplierContracts.stream()
                                .filter(supplierContract1 -> supplierContract1.getId().equals(deliveryDetailDTO.getSupplierContractId()))
                                .findFirst().ifPresent(deliveryDetailDTO::setSupplierContract);
                        });
                        return Mono.just(dd);
                    });
            }))
            .map(countWithEntities ->
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

    // api get calendar
    /**
     * {@code GET  /delivery-details/calendar} : get all the deliveryDetails.
     *
     * @param startDate the start date of the deliveryDetails.
     * @param endDate   the end date of the deliveryDetails.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of deliveryDetails in body.
     */
    @GetMapping("/table")
    public Mono<ResponseEntity<ApiResponse<DeliveryDetailCalendarDTO>>> getDeliveryDetailTable(
        @RequestParam(value = "startDate", required = false) LocalDate startDate,
        @RequestParam(value = "endDate", required = false) LocalDate endDate
    ) {
        LOG.debug("REST request to get getDeliveryDetailTable by criteria: {}, {}", startDate, endDate);
        return deliveryDetailService.getDeliveryDetailTable(startDate, endDate)
            .collectList()
            .map(deliveryDetailCalendarDTOS -> ResponseEntity.ok().body(new ApiResponse<>(deliveryDetailCalendarDTOS, Long.getLong("0"))));
    }

    @GetMapping("/calendar")
    public Mono<ResponseEntity<Object>> getDeliveryDetailCalendar(
        @RequestParam(value = "startDate", required = false) LocalDate startDate,
        @RequestParam(value = "endDate", required = false) LocalDate endDate
    ) {
        LOG.debug("REST request to get getDeliveryDetailCalendar by criteria: {}, {}", startDate, endDate);
        return deliveryDetailService.getDeliveryDetailCalendar(startDate, endDate)
            .collectList()
            .map(deliveryDetailCalendarDTOS -> ResponseEntity.ok().body(deliveryDetailCalendarDTOS));
    }

    /**
     * {@code GET  /delivery-details/count} : count all the deliveryDetails.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public Mono<ResponseEntity<Long>> countDeliveryDetails(DeliveryDetailCriteria criteria) {
        LOG.debug("REST request to count DeliveryDetails by criteria: {}", criteria);
        return deliveryDetailService.countByCriteria(criteria).map(count -> ResponseEntity.status(HttpStatus.OK).body(count));
    }

    /**
     * {@code GET  /delivery-details/:id} : get the "id" deliveryDetail.
     *
     * @param id the id of the deliveryDetailDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the deliveryDetailDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<DeliveryDetailDTO>> getDeliveryDetail(@PathVariable("id") UUID id) {
        LOG.debug("REST request to get DeliveryDetail : {}", id);
        Mono<DeliveryDetailDTO> deliveryDetailDTO = deliveryDetailService.findOne(id);
        return ResponseUtil.wrapOrNotFound(deliveryDetailDTO);
    }

    /**
     * {@code DELETE  /delivery-details/:id} : delete the "id" deliveryDetail.
     *
     * @param id the id of the deliveryDetailDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteDeliveryDetail(@PathVariable("id") UUID id) {
        LOG.debug("REST request to delete DeliveryDetail : {}", id);
        return deliveryDetailService
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
