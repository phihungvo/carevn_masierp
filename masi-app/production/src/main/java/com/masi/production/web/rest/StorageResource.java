package com.masi.production.web.rest;

import com.masi.production.repository.StorageRepository;
import com.masi.production.service.StorageService;
import com.masi.production.service.dto.StorageDTO;
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
 * REST controller for managing {@link com.masi.production.domain.Storage}.
 */
@RestController
@RequestMapping("/api/storages")
public class StorageResource {

    private final Logger log = LoggerFactory.getLogger(StorageResource.class);

    private static final String ENTITY_NAME = "masiProductionStorage";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final StorageService storageService;

    private final StorageRepository storageRepository;

    public StorageResource(StorageService storageService, StorageRepository storageRepository) {
        this.storageService = storageService;
        this.storageRepository = storageRepository;
    }

    /**
     * {@code POST  /storages} : Create a new storage.
     *
     * @param storageDTO the storageDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new storageDTO, or with status {@code 400 (Bad Request)} if the storage has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<StorageDTO>> createStorage(@Valid @RequestBody StorageDTO storageDTO) throws URISyntaxException {
        log.debug("REST request to save Storage : {}", storageDTO);
        if (storageDTO.getId() != null) {
            throw new BadRequestAlertException("A new storage cannot already have an ID", ENTITY_NAME, "idexists");
        }
        storageDTO.setId(UUID.randomUUID());
        return storageService
            .save(storageDTO)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/storages/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /storages/:id} : Updates an existing storage.
     *
     * @param id the id of the storageDTO to save.
     * @param storageDTO the storageDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated storageDTO,
     * or with status {@code 400 (Bad Request)} if the storageDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the storageDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<StorageDTO>> updateStorage(
        @PathVariable(value = "id", required = false) final UUID id,
        @Valid @RequestBody StorageDTO storageDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Storage : {}, {}", id, storageDTO);
        if (storageDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, storageDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return storageRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return storageService
                    .update(storageDTO)
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
     * {@code PATCH  /storages/:id} : Partial updates given fields of an existing storage, field will ignore if it is null
     *
     * @param id the id of the storageDTO to save.
     * @param storageDTO the storageDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated storageDTO,
     * or with status {@code 400 (Bad Request)} if the storageDTO is not valid,
     * or with status {@code 404 (Not Found)} if the storageDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the storageDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<StorageDTO>> partialUpdateStorage(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody StorageDTO storageDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Storage partially : {}, {}", id, storageDTO);
        if (storageDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, storageDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return storageRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<StorageDTO> result = storageService.partialUpdate(storageDTO);

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
     * {@code GET  /storages} : get all the storages.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of storages in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<StorageDTO>>> getAllStorages(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of Storages");
        return storageService
            .countAll()
            .zipWith(storageService.findAll(pageable).collectList())
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
     * {@code GET  /storages/:id} : get the "id" storage.
     *
     * @param id the id of the storageDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the storageDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<StorageDTO>> getStorage(@PathVariable("id") UUID id) {
        log.debug("REST request to get Storage : {}", id);
        Mono<StorageDTO> storageDTO = storageService.findOne(id);
        return ResponseUtil.wrapOrNotFound(storageDTO);
    }

    /**
     * {@code DELETE  /storages/:id} : delete the "id" storage.
     *
     * @param id the id of the storageDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteStorage(@PathVariable("id") UUID id) {
        log.debug("REST request to delete Storage : {}", id);
        return storageService
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
