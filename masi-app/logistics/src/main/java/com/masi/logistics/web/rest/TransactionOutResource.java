package com.masi.logistics.web.rest;

import com.masi.logistics.domain.criteria.TransactionOutCriteria;
import com.masi.logistics.repository.TransactionOutRepository;
import com.masi.logistics.service.TransactionOutService;
import com.masi.logistics.service.dto.TransactionOutDTO;
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
 * REST controller for managing {@link com.masi.logistics.domain.TransactionOut}.
 */
@RestController
@RequestMapping("/api/transaction-outs")
public class TransactionOutResource {

    private static final Logger log = LoggerFactory.getLogger(TransactionOutResource.class);

    private static final String ENTITY_NAME = "masiLogisticsTransactionOut";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TransactionOutService transactionOutService;

    private final TransactionOutRepository transactionOutRepository;

    public TransactionOutResource(TransactionOutService transactionOutService, TransactionOutRepository transactionOutRepository) {
        this.transactionOutService = transactionOutService;
        this.transactionOutRepository = transactionOutRepository;
    }

    /**
     * {@code POST  /transaction-outs} : Create a new transactionOut.
     *
     * @param transactionOutDTO the transactionOutDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new transactionOutDTO, or with status {@code 400 (Bad Request)} if the transactionOut has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<TransactionOutDTO>> createTransactionOut(@RequestBody TransactionOutDTO transactionOutDTO)
        throws URISyntaxException {
        log.debug("REST request to save TransactionOut : {}", transactionOutDTO);
        if (transactionOutDTO.getId() != null) {
            throw new BadRequestAlertException("A new transactionOut cannot already have an ID", ENTITY_NAME, "idexists");
        }
        transactionOutDTO.setId(UUID.randomUUID());
        return transactionOutService
            .save(transactionOutDTO)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/transaction-outs/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /transaction-outs/:id} : Updates an existing transactionOut.
     *
     * @param id the id of the transactionOutDTO to save.
     * @param transactionOutDTO the transactionOutDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated transactionOutDTO,
     * or with status {@code 400 (Bad Request)} if the transactionOutDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the transactionOutDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<TransactionOutDTO>> updateTransactionOut(
        @PathVariable(value = "id", required = false) final UUID id,
        @RequestBody TransactionOutDTO transactionOutDTO
    ) throws URISyntaxException {
        log.debug("REST request to update TransactionOut : {}, {}", id, transactionOutDTO);
        if (transactionOutDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, transactionOutDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return transactionOutRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return transactionOutService
                    .update(transactionOutDTO)
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
     * {@code PATCH  /transaction-outs/:id} : Partial updates given fields of an existing transactionOut, field will ignore if it is null
     *
     * @param id the id of the transactionOutDTO to save.
     * @param transactionOutDTO the transactionOutDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated transactionOutDTO,
     * or with status {@code 400 (Bad Request)} if the transactionOutDTO is not valid,
     * or with status {@code 404 (Not Found)} if the transactionOutDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the transactionOutDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<TransactionOutDTO>> partialUpdateTransactionOut(
        @PathVariable(value = "id", required = false) final UUID id,
        @RequestBody TransactionOutDTO transactionOutDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update TransactionOut partially : {}, {}", id, transactionOutDTO);
        if (transactionOutDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, transactionOutDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return transactionOutRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<TransactionOutDTO> result = transactionOutService.partialUpdate(transactionOutDTO);

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
     * {@code GET  /transaction-outs} : get all the transactionOuts.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of transactionOuts in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<TransactionOutDTO>>> getAllTransactionOuts(
        TransactionOutCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get TransactionOuts by criteria: {}", criteria);
        return transactionOutService
            .countByCriteria(criteria)
            .zipWith(transactionOutService.findByCriteria(criteria, pageable).collectList())
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
     * {@code GET  /transaction-outs/count} : count all the transactionOuts.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public Mono<ResponseEntity<Long>> countTransactionOuts(TransactionOutCriteria criteria) {
        log.debug("REST request to count TransactionOuts by criteria: {}", criteria);
        return transactionOutService.countByCriteria(criteria).map(count -> ResponseEntity.status(HttpStatus.OK).body(count));
    }

    /**
     * {@code GET  /transaction-outs/:id} : get the "id" transactionOut.
     *
     * @param id the id of the transactionOutDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the transactionOutDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<TransactionOutDTO>> getTransactionOut(@PathVariable("id") UUID id) {
        log.debug("REST request to get TransactionOut : {}", id);
        Mono<TransactionOutDTO> transactionOutDTO = transactionOutService.findOne(id);
        return ResponseUtil.wrapOrNotFound(transactionOutDTO);
    }

    /**
     * {@code DELETE  /transaction-outs/:id} : delete the "id" transactionOut.
     *
     * @param id the id of the transactionOutDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteTransactionOut(@PathVariable("id") UUID id) {
        log.debug("REST request to delete TransactionOut : {}", id);
        return transactionOutService
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
