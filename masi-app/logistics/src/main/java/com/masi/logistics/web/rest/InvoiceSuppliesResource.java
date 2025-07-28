package com.masi.logistics.web.rest;

import com.masi.logistics.domain.criteria.InvoiceSuppliesCriteria;
import com.masi.logistics.repository.InvoiceSuppliesRepository;
import com.masi.logistics.service.InvoiceSuppliesService;
import com.masi.logistics.service.dto.InvoiceSuppliesDTO;
import com.masi.logistics.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
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
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link com.masi.logistics.domain.InvoiceSupplies}.
 */
@RestController
@RequestMapping("/api/invoice-supplies")
public class InvoiceSuppliesResource {

    private static final Logger log = LoggerFactory.getLogger(InvoiceSuppliesResource.class);

    private static final String ENTITY_NAME = "masiLogisticsInvoiceSupplies";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InvoiceSuppliesService invoiceSuppliesService;

    private final InvoiceSuppliesRepository invoiceSuppliesRepository;

    public InvoiceSuppliesResource(InvoiceSuppliesService invoiceSuppliesService, InvoiceSuppliesRepository invoiceSuppliesRepository) {
        this.invoiceSuppliesService = invoiceSuppliesService;
        this.invoiceSuppliesRepository = invoiceSuppliesRepository;
    }

    /**
     * {@code POST  /invoice-supplies} : Create a new invoiceSupplies.
     *
     * @param invoiceSuppliesDTO the invoiceSuppliesDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new invoiceSuppliesDTO, or with status {@code 400 (Bad Request)} if the invoiceSupplies has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<InvoiceSuppliesDTO>> createInvoiceSupplies(@Valid @RequestBody InvoiceSuppliesDTO invoiceSuppliesDTO)
        throws URISyntaxException {
        log.debug("REST request to save InvoiceSupplies : {}", invoiceSuppliesDTO);
        if (invoiceSuppliesDTO.getId() != null) {
            throw new BadRequestAlertException("A new invoiceSupplies cannot already have an ID", ENTITY_NAME, "idexists");
        }
        invoiceSuppliesDTO.setId(UUID.randomUUID());
        return invoiceSuppliesService
            .save(invoiceSuppliesDTO)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/invoice-supplies/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /invoice-supplies/:id} : Updates an existing invoiceSupplies.
     *
     * @param id the id of the invoiceSuppliesDTO to save.
     * @param invoiceSuppliesDTO the invoiceSuppliesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated invoiceSuppliesDTO,
     * or with status {@code 400 (Bad Request)} if the invoiceSuppliesDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the invoiceSuppliesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<InvoiceSuppliesDTO>> updateInvoiceSupplies(
        @PathVariable(value = "id", required = false) final UUID id,
        @Valid @RequestBody InvoiceSuppliesDTO invoiceSuppliesDTO
    ) throws URISyntaxException {
        log.debug("REST request to update InvoiceSupplies : {}, {}", id, invoiceSuppliesDTO);
        if (invoiceSuppliesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, invoiceSuppliesDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return invoiceSuppliesRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return invoiceSuppliesService
                    .update(invoiceSuppliesDTO)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(
                        result ->
                            ResponseEntity.ok()
                                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                                .body(result)
                    );
            });
    }

    /**
     * {@code PATCH  /invoice-supplies/:id} : Partial updates given fields of an existing invoiceSupplies, field will ignore if it is null
     *
     * @param id the id of the invoiceSuppliesDTO to save.
     * @param invoiceSuppliesDTO the invoiceSuppliesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated invoiceSuppliesDTO,
     * or with status {@code 400 (Bad Request)} if the invoiceSuppliesDTO is not valid,
     * or with status {@code 404 (Not Found)} if the invoiceSuppliesDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the invoiceSuppliesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<InvoiceSuppliesDTO>> partialUpdateInvoiceSupplies(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody InvoiceSuppliesDTO invoiceSuppliesDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update InvoiceSupplies partially : {}, {}", id, invoiceSuppliesDTO);
        if (invoiceSuppliesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, invoiceSuppliesDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return invoiceSuppliesRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<InvoiceSuppliesDTO> result = invoiceSuppliesService.partialUpdate(invoiceSuppliesDTO);

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
     * {@code GET  /invoice-supplies} : get all the invoiceSupplies.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of invoiceSupplies in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<InvoiceSuppliesDTO>>> getAllInvoiceSupplies(
        InvoiceSuppliesCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get InvoiceSupplies by criteria: {}", criteria);
        return invoiceSuppliesService
            .countByCriteria(criteria)
            .zipWith(invoiceSuppliesService.findByCriteria(criteria, pageable).collectList())
            .map(
                countWithEntities ->
                    ResponseEntity.ok()
                        .headers(
                            PaginationUtil.generatePaginationHttpHeaders(
                                ForwardedHeaderUtils.adaptFromForwardedHeaders(request.getURI(), request.getHeaders()),
                                new PageImpl<>(countWithEntities.getT2(), pageable, countWithEntities.getT1())
                            )
                        )
                        .body(countWithEntities.getT2())
            );
    }

    /**
     * {@code GET  /invoice-supplies/count} : count all the invoiceSupplies.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public Mono<ResponseEntity<Long>> countInvoiceSupplies(InvoiceSuppliesCriteria criteria) {
        log.debug("REST request to count InvoiceSupplies by criteria: {}", criteria);
        return invoiceSuppliesService.countByCriteria(criteria).map(count -> ResponseEntity.status(HttpStatus.OK).body(count));
    }

    /**
     * {@code GET  /invoice-supplies/:id} : get the "id" invoiceSupplies.
     *
     * @param id the id of the invoiceSuppliesDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the invoiceSuppliesDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<InvoiceSuppliesDTO>> getInvoiceSupplies(@PathVariable("id") UUID id) {
        log.debug("REST request to get InvoiceSupplies : {}", id);
        Mono<InvoiceSuppliesDTO> invoiceSuppliesDTO = invoiceSuppliesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(invoiceSuppliesDTO);
    }

    /**
     * {@code DELETE  /invoice-supplies/:id} : delete the "id" invoiceSupplies.
     *
     * @param id the id of the invoiceSuppliesDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteInvoiceSupplies(@PathVariable("id") UUID id) {
        log.debug("REST request to delete InvoiceSupplies : {}", id);
        return invoiceSuppliesService
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
