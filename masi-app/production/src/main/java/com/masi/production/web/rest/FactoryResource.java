package com.masi.production.web.rest;

import com.masi.production.repository.FactoryRepository;
import com.masi.production.service.FactoryService;
import com.masi.production.service.dto.FactoryDTO;
import com.masi.production.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link com.masi.production.domain.Factory}.
 */
@RestController
@RequestMapping("/api/factories")
public class FactoryResource {

    private final Logger log = LoggerFactory.getLogger(FactoryResource.class);

    private static final String ENTITY_NAME = "masiProductionFactory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FactoryService factoryService;

    private final FactoryRepository factoryRepository;

    public FactoryResource(FactoryService factoryService, FactoryRepository factoryRepository) {
        this.factoryService = factoryService;
        this.factoryRepository = factoryRepository;
    }

    /**
     * {@code POST  /factories} : Create a new factory.
     *
     * @param factoryDTO the factoryDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new factoryDTO, or with status {@code 400 (Bad Request)} if the factory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<FactoryDTO>> createFactory(@Valid @RequestBody FactoryDTO factoryDTO) throws URISyntaxException {
        log.debug("REST request to save Factory : {}", factoryDTO);
        if (factoryDTO.getId() != null) {
            throw new BadRequestAlertException("A new factory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        factoryDTO.setId(UUID.randomUUID());
        return factoryService
            .save(factoryDTO)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/factories/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /factories/:id} : Updates an existing factory.
     *
     * @param id the id of the factoryDTO to save.
     * @param factoryDTO the factoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated factoryDTO,
     * or with status {@code 400 (Bad Request)} if the factoryDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the factoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<FactoryDTO>> updateFactory(
        @PathVariable(value = "id", required = false) final UUID id,
        @Valid @RequestBody FactoryDTO factoryDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Factory : {}, {}", id, factoryDTO);
        if (factoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, factoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return factoryRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return factoryService
                    .update(factoryDTO)
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
     * {@code PATCH  /factories/:id} : Partial updates given fields of an existing factory, field will ignore if it is null
     *
     * @param id the id of the factoryDTO to save.
     * @param factoryDTO the factoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated factoryDTO,
     * or with status {@code 400 (Bad Request)} if the factoryDTO is not valid,
     * or with status {@code 404 (Not Found)} if the factoryDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the factoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<FactoryDTO>> partialUpdateFactory(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody FactoryDTO factoryDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Factory partially : {}, {}", id, factoryDTO);
        if (factoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, factoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return factoryRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<FactoryDTO> result = factoryService.partialUpdate(factoryDTO);

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
     * {@code GET  /factories} : get all the factories.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of factories in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<FactoryDTO>>> getAllFactories(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of Factories");
        return factoryService
            .countAll()
            .zipWith(factoryService.findAll(pageable).collectList())
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
     * {@code GET  /factories/:id} : get the "id" factory.
     *
     * @param id the id of the factoryDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the factoryDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<FactoryDTO>> getFactory(@PathVariable("id") UUID id) {
        log.debug("REST request to get Factory : {}", id);
        Mono<FactoryDTO> factoryDTO = factoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(factoryDTO);
    }

    /**
     * {@code DELETE  /factories/:id} : delete the "id" factory.
     *
     * @param id the id of the factoryDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteFactory(@PathVariable("id") UUID id) {
        log.debug("REST request to delete Factory : {}", id);
        return factoryService
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
