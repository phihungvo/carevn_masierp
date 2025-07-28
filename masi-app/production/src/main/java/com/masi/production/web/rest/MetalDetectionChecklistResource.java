package com.masi.production.web.rest;

import com.masi.production.repository.MetalDetectionChecklistRepository;
import com.masi.production.service.MetalDetectionChecklistService;
import com.masi.production.service.dto.MetalDetectionChecklistDTO;
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
 * REST controller for managing {@link com.masi.production.domain.MetalDetectionChecklist}.
 */
@RestController
@RequestMapping("/api/metal-detection-checklists")
public class MetalDetectionChecklistResource {

    private final Logger log = LoggerFactory.getLogger(MetalDetectionChecklistResource.class);

    private static final String ENTITY_NAME = "masiProductionMetalDetectionChecklist";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MetalDetectionChecklistService metalDetectionChecklistService;

    private final MetalDetectionChecklistRepository metalDetectionChecklistRepository;

    public MetalDetectionChecklistResource(
        MetalDetectionChecklistService metalDetectionChecklistService,
        MetalDetectionChecklistRepository metalDetectionChecklistRepository
    ) {
        this.metalDetectionChecklistService = metalDetectionChecklistService;
        this.metalDetectionChecklistRepository = metalDetectionChecklistRepository;
    }

    /**
     * {@code POST  /metal-detection-checklists} : Create a new metalDetectionChecklist.
     *
     * @param dto the dto to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new dto, or with status {@code 400 (Bad Request)} if the metalDetectionChecklist has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<MetalDetectionChecklistDTO>> createMetalDetectionChecklist(
        @Valid @RequestBody MetalDetectionChecklistDTO dto
    ) throws URISyntaxException {
        log.debug("REST request to save MetalDetectionChecklist : {}", dto);
        dto.setId(UUID.randomUUID());
        return metalDetectionChecklistService
            .save(dto)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/metal-detection-checklists/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PATCH  /metal-detection-checklists/:id} : Partial updates given fields of an existing metalDetectionChecklist, field will ignore if it is null
     *
     * @param id the id of the dto to save.
     * @param dto the dto to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dto,
     * or with status {@code 400 (Bad Request)} if the dto is not valid,
     * or with status {@code 404 (Not Found)} if the dto is not found,
     * or with status {@code 500 (Internal Server Error)} if the dto couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<MetalDetectionChecklistDTO>> partialUpdateMetalDetectionChecklist(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody MetalDetectionChecklistDTO dto
    ) throws URISyntaxException {
        log.debug("REST request to partial update MetalDetectionChecklist partially : {}, {}", id, dto);
        dto.setId(id);
        return metalDetectionChecklistRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<MetalDetectionChecklistDTO> result = metalDetectionChecklistService.partialUpdate(dto);

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
     * {@code GET  /metal-detection-checklists/:id} : get the "id" metalDetectionChecklist.
     *
     * @param id the id of the metalDetectionChecklistDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the metalDetectionChecklistDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<MetalDetectionChecklistDTO>> getMetalDetectionChecklist(@PathVariable("id") UUID id) {
        log.debug("REST request to get MetalDetectionChecklist : {}", id);
        Mono<MetalDetectionChecklistDTO> metalDetectionChecklistDTO = metalDetectionChecklistService.findOne(id);
        return ResponseUtil.wrapOrNotFound(metalDetectionChecklistDTO);
    }

    /**
     * {@code DELETE  /metal-detection-checklists/:id} : delete the "id" metalDetectionChecklist.
     *
     * @param id the id of the metalDetectionChecklistDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteMetalDetectionChecklist(@PathVariable("id") UUID id) {
        log.debug("REST request to delete MetalDetectionChecklist : {}", id);
        return metalDetectionChecklistService
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
