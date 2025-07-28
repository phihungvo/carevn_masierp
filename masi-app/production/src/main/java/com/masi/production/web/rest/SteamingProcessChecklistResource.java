package com.masi.production.web.rest;

import com.masi.production.repository.SteamingProcessChecklistRepository;
import com.masi.production.service.SteamingProcessChecklistService;
import com.masi.production.service.dto.SteamingProcessChecklistDTO;
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
 * REST controller for managing {@link com.masi.production.domain.SteamingProcessChecklist}.
 */
@RestController
@RequestMapping("/api/steaming-process-checklists")
public class SteamingProcessChecklistResource {

    private final Logger log = LoggerFactory.getLogger(SteamingProcessChecklistResource.class);

    private static final String ENTITY_NAME = "masiProductionSteamingProcessChecklist";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SteamingProcessChecklistService steamingProcessChecklistService;

    private final SteamingProcessChecklistRepository steamingProcessChecklistRepository;

    public SteamingProcessChecklistResource(
        SteamingProcessChecklistService steamingProcessChecklistService,
        SteamingProcessChecklistRepository steamingProcessChecklistRepository
    ) {
        this.steamingProcessChecklistService = steamingProcessChecklistService;
        this.steamingProcessChecklistRepository = steamingProcessChecklistRepository;
    }

    /**
     * {@code POST  /steaming-process-checklists} : Create a new steamingProcessChecklist.
     *
     * @param dto the steamingProcessChecklistDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new steamingProcessChecklistDTO, or with status {@code 400 (Bad Request)} if the steamingProcessChecklist has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<SteamingProcessChecklistDTO> createSteamingProcessChecklist(
        @Valid @RequestBody SteamingProcessChecklistDTO dto
    ) throws URISyntaxException {
        log.debug("REST request to save SteamingProcessChecklist : {}", dto);
        if (dto.getId() != null) {
            throw new BadRequestAlertException("A new steamingProcessChecklist cannot already have an ID", ENTITY_NAME, "idexists");
        }
        dto.setId(UUID.randomUUID());
        return steamingProcessChecklistService
            .save(dto)
            .doOnError(
                error -> {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, error.getMessage());
                }
            );
    }

    /**
     * {@code PATCH  /steaming-process-checklists/:id} : Partial updates given fields of an existing steamingProcessChecklist, field will ignore if it is null
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
    public Mono<ResponseEntity<SteamingProcessChecklistDTO>> partialUpdateSteamingProcessChecklist(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody SteamingProcessChecklistDTO dto
    ) throws URISyntaxException {
        log.debug("REST request to partial update SteamingProcessChecklist partially : {}, {}", id, dto);
        dto.setId(id);
        return steamingProcessChecklistRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<SteamingProcessChecklistDTO> result = steamingProcessChecklistService.partialUpdate(dto);

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
     * {@code GET  /steaming-process-checklists/:id} : get the "id" steamingProcessChecklist.
     *
     * @param id the id of the steamingProcessChecklistDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the steamingProcessChecklistDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<SteamingProcessChecklistDTO>> getSteamingProcessChecklist(@PathVariable("id") UUID id) {
        log.debug("REST request to get SteamingProcessChecklist : {}", id);
        Mono<SteamingProcessChecklistDTO> steamingProcessChecklistDTO = steamingProcessChecklistService.findOne(id);
        return ResponseUtil.wrapOrNotFound(steamingProcessChecklistDTO);
    }

    /**
     * {@code DELETE  /steaming-process-checklists/:id} : delete the "id" steamingProcessChecklist.
     *
     * @param id the id of the steamingProcessChecklistDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<Void> deleteSteamingProcessChecklist(@PathVariable("id") UUID id) {
        log.debug("REST request to delete SteamingProcessChecklist : {}", id);
        return steamingProcessChecklistService
            .delete(id);
    }
}
