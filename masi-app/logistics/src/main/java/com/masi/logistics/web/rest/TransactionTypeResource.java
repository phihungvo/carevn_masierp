package com.masi.logistics.web.rest;

import com.carevn.masi.dto.ApiResponse;
import com.masi.logistics.domain.criteria.TransactionTypeCriteria;
import com.masi.logistics.repository.TransactionTypeRepository;
import com.masi.logistics.service.TransactionTypeService;
import com.masi.logistics.service.dto.TransactionTypeDTO;
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
 * REST controller for managing {@link com.masi.logistics.domain.TransactionType}.
 */
@RestController
@RequestMapping("/api/transaction-types")
public class TransactionTypeResource {

    private static final Logger log = LoggerFactory.getLogger(TransactionTypeResource.class);

    private static final String ENTITY_NAME = "masiLogisticsTransactionType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TransactionTypeService transactionTypeService;

    private final TransactionTypeRepository transactionTypeRepository;

    public TransactionTypeResource(TransactionTypeService transactionTypeService, TransactionTypeRepository transactionTypeRepository) {
        this.transactionTypeService = transactionTypeService;
        this.transactionTypeRepository = transactionTypeRepository;
    }

    /**
     * {@code POST  /transaction-types} : Create a new transactionType.
     *
     * @param transactionTypeDTO the transactionTypeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new transactionTypeDTO, or with status {@code 400 (Bad Request)} if the transactionType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<TransactionTypeDTO>> createTransactionType(@RequestBody TransactionTypeDTO transactionTypeDTO)
        throws URISyntaxException {
        log.debug("REST request to save TransactionType : {}", transactionTypeDTO);
        if (transactionTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new transactionType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        transactionTypeDTO.setId(UUID.randomUUID());
        return transactionTypeService
            .save(transactionTypeDTO)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/transaction-types/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /transaction-types/:id} : Updates an existing transactionType.
     *
     * @param id the id of the transactionTypeDTO to save.
     * @param transactionTypeDTO the transactionTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated transactionTypeDTO,
     * or with status {@code 400 (Bad Request)} if the transactionTypeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the transactionTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<TransactionTypeDTO>> updateTransactionType(
        @PathVariable(value = "id", required = false) final UUID id,
        @RequestBody TransactionTypeDTO transactionTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update TransactionType : {}, {}", id, transactionTypeDTO);
        if (transactionTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, transactionTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return transactionTypeRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return transactionTypeService
                    .update(transactionTypeDTO)
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
     * {@code PATCH  /transaction-types/:id} : Partial updates given fields of an existing transactionType, field will ignore if it is null
     *
     * @param id the id of the transactionTypeDTO to save.
     * @param transactionTypeDTO the transactionTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated transactionTypeDTO,
     * or with status {@code 400 (Bad Request)} if the transactionTypeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the transactionTypeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the transactionTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<TransactionTypeDTO>> partialUpdateTransactionType(
        @PathVariable(value = "id", required = false) final UUID id,
        @RequestBody TransactionTypeDTO transactionTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update TransactionType partially : {}, {}", id, transactionTypeDTO);
        if (transactionTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, transactionTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return transactionTypeRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<TransactionTypeDTO> result = transactionTypeService.partialUpdate(transactionTypeDTO);

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
     * {@code GET  /transaction-types} : get all the transactionTypes.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of transactionTypes in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<ApiResponse<TransactionTypeDTO>>> getAllTransactionTypes(
        TransactionTypeCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get TransactionTypes by criteria: {}", criteria);
        return transactionTypeService
            .countByCriteria(criteria)
            .zipWith(transactionTypeService.findByCriteria(criteria, pageable).collectList())
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
     * {@code GET  /transaction-types/count} : count all the transactionTypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public Mono<ResponseEntity<Long>> countTransactionTypes(TransactionTypeCriteria criteria) {
        log.debug("REST request to count TransactionTypes by criteria: {}", criteria);
        return transactionTypeService.countByCriteria(criteria).map(count -> ResponseEntity.status(HttpStatus.OK).body(count));
    }

    /**
     * {@code GET  /transaction-types/:id} : get the "id" transactionType.
     *
     * @param id the id of the transactionTypeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the transactionTypeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<TransactionTypeDTO>> getTransactionType(@PathVariable("id") UUID id) {
        log.debug("REST request to get TransactionType : {}", id);
        Mono<TransactionTypeDTO> transactionTypeDTO = transactionTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(transactionTypeDTO);
    }

    /**
     * {@code DELETE  /transaction-types/:id} : delete the "id" transactionType.
     *
     * @param id the id of the transactionTypeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteTransactionType(@PathVariable("id") UUID id) {
        log.debug("REST request to delete TransactionType : {}", id);
        return transactionTypeService
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
