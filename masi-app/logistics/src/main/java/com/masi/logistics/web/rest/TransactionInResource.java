package com.masi.logistics.web.rest;

import com.masi.logistics.domain.criteria.TransactionInCriteria;
import com.masi.logistics.repository.TransactionInRepository;
import com.masi.logistics.service.TransactionInService;
import com.masi.logistics.service.dto.TransactionInDTO;
import com.masi.logistics.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link com.masi.logistics.domain.TransactionIn}.
 */
@RestController
@RequestMapping("/api/transaction-ins")
public class TransactionInResource {

    private static final Logger log = LoggerFactory.getLogger(TransactionInResource.class);

    private static final String ENTITY_NAME = "masiLogisticsTransactionIn";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TransactionInService transactionInService;

    private final TransactionInRepository transactionInRepository;

    public TransactionInResource(TransactionInService transactionInService, TransactionInRepository transactionInRepository) {
        this.transactionInService = transactionInService;
        this.transactionInRepository = transactionInRepository;
    }

    /**
     * {@code POST  /transaction-ins} : Create a new transactionIn.
     *
     * @param transactionInDTO the transactionInDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new transactionInDTO, or with status {@code 400 (Bad Request)} if the transactionIn has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<TransactionInDTO>> createTransactionIn(@RequestBody TransactionInDTO transactionInDTO)
        throws URISyntaxException {
        log.debug("REST request to save TransactionIn : {}", transactionInDTO);
        if (transactionInDTO.getId() != null) {
            throw new BadRequestAlertException("A new transactionIn cannot already have an ID", ENTITY_NAME, "idexists");
        }
        transactionInDTO.setId(UUID.randomUUID());
        return transactionInService
            .save(transactionInDTO)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/transaction-ins/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /transaction-ins/:id} : Updates an existing transactionIn.
     *
     * @param id the id of the transactionInDTO to save.
     * @param transactionInDTO the transactionInDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated transactionInDTO,
     * or with status {@code 400 (Bad Request)} if the transactionInDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the transactionInDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<TransactionInDTO>> updateTransactionIn(
        @PathVariable(value = "id", required = false) final UUID id,
        @RequestBody TransactionInDTO transactionInDTO
    ) throws URISyntaxException {
        log.debug("REST request to update TransactionIn : {}, {}", id, transactionInDTO);
        if (transactionInDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, transactionInDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return transactionInRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return transactionInService
                    .update(transactionInDTO)
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
     * {@code PATCH  /transaction-ins/:id} : Partial updates given fields of an existing transactionIn, field will ignore if it is null
     *
     * @param id the id of the transactionInDTO to save.
     * @param transactionInDTO the transactionInDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated transactionInDTO,
     * or with status {@code 400 (Bad Request)} if the transactionInDTO is not valid,
     * or with status {@code 404 (Not Found)} if the transactionInDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the transactionInDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<TransactionInDTO>> partialUpdateTransactionIn(
        @PathVariable(value = "id", required = false) final UUID id,
        @RequestBody TransactionInDTO transactionInDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update TransactionIn partially : {}, {}", id, transactionInDTO);
        if (transactionInDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, transactionInDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return transactionInRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<TransactionInDTO> result = transactionInService.partialUpdate(transactionInDTO);

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
     * {@code GET  /transaction-ins} : get all the transactionIns.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of transactionIns in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<TransactionInDTO>>> getAllTransactionIns(
        TransactionInCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get TransactionIns by criteria: {}", criteria);
        return transactionInService
            .countByCriteria(criteria)
            .zipWith(transactionInService.findByCriteria(criteria, pageable).collectList())
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
     * {@code GET  /transaction-ins/count} : count all the transactionIns.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public Mono<ResponseEntity<Long>> countTransactionIns(TransactionInCriteria criteria) {
        log.debug("REST request to count TransactionIns by criteria: {}", criteria);
        return transactionInService.countByCriteria(criteria).map(count -> ResponseEntity.status(HttpStatus.OK).body(count));
    }

    /**
     * {@code GET  /transaction-ins/:id} : get the "id" transactionIn.
     *
     * @param id the id of the transactionInDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the transactionInDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<TransactionInDTO>> getTransactionIn(@PathVariable("id") UUID id) {
        log.debug("REST request to get TransactionIn : {}", id);
        Mono<TransactionInDTO> transactionInDTO = transactionInService.findOne(id);
        return ResponseUtil.wrapOrNotFound(transactionInDTO);
    }

    /**
     * {@code DELETE  /transaction-ins/:id} : delete the "id" transactionIn.
     *
     * @param id the id of the transactionInDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteTransactionIn(@PathVariable("id") UUID id) {
        log.debug("REST request to delete TransactionIn : {}", id);
        return transactionInService
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
