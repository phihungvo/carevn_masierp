package com.masi.logistics.web.rest;

import com.masi.logistics.domain.criteria.TypeOfFGenerationCriteria;
import com.masi.logistics.repository.TypeOfFGenerationRepository;
import com.masi.logistics.service.TypeOfFGenerationService;
import com.masi.logistics.service.dto.TypeOfFGenerationDTO;
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
 * REST controller for managing {@link com.masi.logistics.domain.TypeOfFGeneration}.
 */
@RestController
@RequestMapping("/api/type-of-f-generations")
public class TypeOfFGenerationResource {

    private static final Logger log = LoggerFactory.getLogger(TypeOfFGenerationResource.class);

    private static final String ENTITY_NAME = "masiLogisticsTypeOfFGeneration";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TypeOfFGenerationService typeOfFGenerationService;

    private final TypeOfFGenerationRepository typeOfFGenerationRepository;

    public TypeOfFGenerationResource(
        TypeOfFGenerationService typeOfFGenerationService,
        TypeOfFGenerationRepository typeOfFGenerationRepository
    ) {
        this.typeOfFGenerationService = typeOfFGenerationService;
        this.typeOfFGenerationRepository = typeOfFGenerationRepository;
    }

    /**
     * {@code POST  /type-of-f-generations} : Create a new typeOfFGeneration.
     *
     * @param typeOfFGenerationDTO the typeOfFGenerationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new typeOfFGenerationDTO, or with status {@code 400 (Bad Request)} if the typeOfFGeneration has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<TypeOfFGenerationDTO>> createTypeOfFGeneration(@RequestBody TypeOfFGenerationDTO typeOfFGenerationDTO)
        throws URISyntaxException {
        log.debug("REST request to save TypeOfFGeneration : {}", typeOfFGenerationDTO);
        if (typeOfFGenerationDTO.getId() != null) {
            throw new BadRequestAlertException("A new typeOfFGeneration cannot already have an ID", ENTITY_NAME, "idexists");
        }
        typeOfFGenerationDTO.setId(UUID.randomUUID());
        return typeOfFGenerationService
            .save(typeOfFGenerationDTO)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/type-of-f-generations/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /type-of-f-generations/:id} : Updates an existing typeOfFGeneration.
     *
     * @param id the id of the typeOfFGenerationDTO to save.
     * @param typeOfFGenerationDTO the typeOfFGenerationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated typeOfFGenerationDTO,
     * or with status {@code 400 (Bad Request)} if the typeOfFGenerationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the typeOfFGenerationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<TypeOfFGenerationDTO>> updateTypeOfFGeneration(
        @PathVariable(value = "id", required = false) final UUID id,
        @RequestBody TypeOfFGenerationDTO typeOfFGenerationDTO
    ) throws URISyntaxException {
        log.debug("REST request to update TypeOfFGeneration : {}, {}", id, typeOfFGenerationDTO);
        if (typeOfFGenerationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, typeOfFGenerationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return typeOfFGenerationRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return typeOfFGenerationService
                    .update(typeOfFGenerationDTO)
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
     * {@code PATCH  /type-of-f-generations/:id} : Partial updates given fields of an existing typeOfFGeneration, field will ignore if it is null
     *
     * @param id the id of the typeOfFGenerationDTO to save.
     * @param typeOfFGenerationDTO the typeOfFGenerationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated typeOfFGenerationDTO,
     * or with status {@code 400 (Bad Request)} if the typeOfFGenerationDTO is not valid,
     * or with status {@code 404 (Not Found)} if the typeOfFGenerationDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the typeOfFGenerationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<TypeOfFGenerationDTO>> partialUpdateTypeOfFGeneration(
        @PathVariable(value = "id", required = false) final UUID id,
        @RequestBody TypeOfFGenerationDTO typeOfFGenerationDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update TypeOfFGeneration partially : {}, {}", id, typeOfFGenerationDTO);
        if (typeOfFGenerationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, typeOfFGenerationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return typeOfFGenerationRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<TypeOfFGenerationDTO> result = typeOfFGenerationService.partialUpdate(typeOfFGenerationDTO);

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
     * {@code GET  /type-of-f-generations} : get all the typeOfFGenerations.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of typeOfFGenerations in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<TypeOfFGenerationDTO>>> getAllTypeOfFGenerations(
        TypeOfFGenerationCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get TypeOfFGenerations by criteria: {}", criteria);
        return typeOfFGenerationService
            .countByCriteria(criteria)
            .zipWith(typeOfFGenerationService.findByCriteria(criteria, pageable).collectList())
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
     * {@code GET  /type-of-f-generations/count} : count all the typeOfFGenerations.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public Mono<ResponseEntity<Long>> countTypeOfFGenerations(TypeOfFGenerationCriteria criteria) {
        log.debug("REST request to count TypeOfFGenerations by criteria: {}", criteria);
        return typeOfFGenerationService.countByCriteria(criteria).map(count -> ResponseEntity.status(HttpStatus.OK).body(count));
    }

    /**
     * {@code GET  /type-of-f-generations/:id} : get the "id" typeOfFGeneration.
     *
     * @param id the id of the typeOfFGenerationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the typeOfFGenerationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<TypeOfFGenerationDTO>> getTypeOfFGeneration(@PathVariable("id") UUID id) {
        log.debug("REST request to get TypeOfFGeneration : {}", id);
        Mono<TypeOfFGenerationDTO> typeOfFGenerationDTO = typeOfFGenerationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(typeOfFGenerationDTO);
    }

    /**
     * {@code DELETE  /type-of-f-generations/:id} : delete the "id" typeOfFGeneration.
     *
     * @param id the id of the typeOfFGenerationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteTypeOfFGeneration(@PathVariable("id") UUID id) {
        log.debug("REST request to delete TypeOfFGeneration : {}", id);
        return typeOfFGenerationService
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
