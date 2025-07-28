package com.masi.logistics.web.rest;

import com.masi.logistics.domain.criteria.RelatedCostsCriteria;
import com.masi.logistics.repository.RelatedCostsRepository;
import com.masi.logistics.service.RelatedCostsService;
import com.masi.logistics.service.dto.RelatedCostsDTO;
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
 * REST controller for managing {@link com.masi.logistics.domain.RelatedCosts}.
 */
@RestController
@RequestMapping("/api/related-costs")
public class RelatedCostsResource {

    private static final Logger log = LoggerFactory.getLogger(RelatedCostsResource.class);

    private static final String ENTITY_NAME = "masiLogisticsRelatedCosts";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RelatedCostsService relatedCostsService;

    private final RelatedCostsRepository relatedCostsRepository;

    public RelatedCostsResource(RelatedCostsService relatedCostsService, RelatedCostsRepository relatedCostsRepository) {
        this.relatedCostsService = relatedCostsService;
        this.relatedCostsRepository = relatedCostsRepository;
    }

    /**
     * {@code POST  /related-costs} : Create a new relatedCosts.
     *
     * @param relatedCostsDTO the relatedCostsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new relatedCostsDTO, or with status {@code 400 (Bad Request)} if the relatedCosts has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<RelatedCostsDTO>> createRelatedCosts(@Valid @RequestBody RelatedCostsDTO relatedCostsDTO)
        throws URISyntaxException {
        log.debug("REST request to save RelatedCosts : {}", relatedCostsDTO);
        if (relatedCostsDTO.getId() != null) {
            throw new BadRequestAlertException("A new relatedCosts cannot already have an ID", ENTITY_NAME, "idexists");
        }
        relatedCostsDTO.setId(UUID.randomUUID());
        return relatedCostsService
            .save(relatedCostsDTO)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/related-costs/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /related-costs/:id} : Updates an existing relatedCosts.
     *
     * @param id the id of the relatedCostsDTO to save.
     * @param relatedCostsDTO the relatedCostsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated relatedCostsDTO,
     * or with status {@code 400 (Bad Request)} if the relatedCostsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the relatedCostsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<RelatedCostsDTO>> updateRelatedCosts(
        @PathVariable(value = "id", required = false) final UUID id,
        @Valid @RequestBody RelatedCostsDTO relatedCostsDTO
    ) throws URISyntaxException {
        log.debug("REST request to update RelatedCosts : {}, {}", id, relatedCostsDTO);
        if (relatedCostsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, relatedCostsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return relatedCostsRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return relatedCostsService
                    .update(relatedCostsDTO)
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
     * {@code PATCH  /related-costs/:id} : Partial updates given fields of an existing relatedCosts, field will ignore if it is null
     *
     * @param id the id of the relatedCostsDTO to save.
     * @param relatedCostsDTO the relatedCostsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated relatedCostsDTO,
     * or with status {@code 400 (Bad Request)} if the relatedCostsDTO is not valid,
     * or with status {@code 404 (Not Found)} if the relatedCostsDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the relatedCostsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<RelatedCostsDTO>> partialUpdateRelatedCosts(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody RelatedCostsDTO relatedCostsDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update RelatedCosts partially : {}, {}", id, relatedCostsDTO);
        if (relatedCostsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, relatedCostsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return relatedCostsRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<RelatedCostsDTO> result = relatedCostsService.partialUpdate(relatedCostsDTO);

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
     * {@code GET  /related-costs} : get all the relatedCosts.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of relatedCosts in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<RelatedCostsDTO>>> getAllRelatedCosts(
        RelatedCostsCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get RelatedCosts by criteria: {}", criteria);
        return relatedCostsService
            .countByCriteria(criteria)
            .zipWith(relatedCostsService.findByCriteria(criteria, pageable).collectList())
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
     * {@code GET  /related-costs/count} : count all the relatedCosts.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public Mono<ResponseEntity<Long>> countRelatedCosts(RelatedCostsCriteria criteria) {
        log.debug("REST request to count RelatedCosts by criteria: {}", criteria);
        return relatedCostsService.countByCriteria(criteria).map(count -> ResponseEntity.status(HttpStatus.OK).body(count));
    }

    /**
     * {@code GET  /related-costs/:id} : get the "id" relatedCosts.
     *
     * @param id the id of the relatedCostsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the relatedCostsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<RelatedCostsDTO>> getRelatedCosts(@PathVariable("id") UUID id) {
        log.debug("REST request to get RelatedCosts : {}", id);
        Mono<RelatedCostsDTO> relatedCostsDTO = relatedCostsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(relatedCostsDTO);
    }

    /**
     * {@code DELETE  /related-costs/:id} : delete the "id" relatedCosts.
     *
     * @param id the id of the relatedCostsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteRelatedCosts(@PathVariable("id") UUID id) {
        log.debug("REST request to delete RelatedCosts : {}", id);
        return relatedCostsService
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
