package com.masi.production.web.rest;

import com.masi.production.repository.MixingReportChecklistRepository;
import com.masi.production.service.MixingReportChecklistService;
import com.masi.production.service.dto.MixingReportChecklistDTO;
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
 * REST controller for managing {@link com.masi.production.domain.MixingReportChecklist}.
 */
@RestController
@RequestMapping("/api/mixing-report-checklists")
public class MixingReportChecklistResource {

    private final Logger log = LoggerFactory.getLogger(MixingReportChecklistResource.class);

    private static final String ENTITY_NAME = "masiProductionMixingReportChecklist";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MixingReportChecklistService mixingReportChecklistService;

    private final MixingReportChecklistRepository mixingReportChecklistRepository;

    public MixingReportChecklistResource(
        MixingReportChecklistService mixingReportChecklistService,
        MixingReportChecklistRepository mixingReportChecklistRepository
    ) {
        this.mixingReportChecklistService = mixingReportChecklistService;
        this.mixingReportChecklistRepository = mixingReportChecklistRepository;
    }

    /**
     * {@code POST  /mixing-report-checklists} : Create a new mixingReportChecklist.
     *
     * @param mixingReportChecklistDTO the mixingReportChecklistDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new mixingReportChecklistDTO, or with status {@code 400 (Bad Request)} if the mixingReportChecklist has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<MixingReportChecklistDTO>> createMixingReportChecklist(
        @Valid @RequestBody MixingReportChecklistDTO mixingReportChecklistDTO
    ) throws URISyntaxException {
        log.debug("REST request to save MixingReportChecklist : {}", mixingReportChecklistDTO);

        mixingReportChecklistDTO.setId(UUID.randomUUID());
        return mixingReportChecklistService
            .save(mixingReportChecklistDTO)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/mixing-report-checklists/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PATCH  /mixing-report-checklists/:id} : Partial updates given fields of an existing mixingReportChecklist, field will ignore if it is null
     *
     * @param id the id of the mixingReportChecklistDTO to save.
     * @param mixingReportChecklistDTO the mixingReportChecklistDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated mixingReportChecklistDTO,
     * or with status {@code 400 (Bad Request)} if the mixingReportChecklistDTO is not valid,
     * or with status {@code 404 (Not Found)} if the mixingReportChecklistDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the mixingReportChecklistDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<MixingReportChecklistDTO>> partialUpdateMixingReportChecklist(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody MixingReportChecklistDTO mixingReportChecklistDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update MixingReportChecklist partially : {}, {}", id, mixingReportChecklistDTO);
        mixingReportChecklistDTO.setId(id);
        return mixingReportChecklistRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<MixingReportChecklistDTO> result = mixingReportChecklistService.partialUpdate(mixingReportChecklistDTO);

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
     * {@code GET  /mixing-report-checklists/:id} : get the "id" mixingReportChecklist.
     *
     * @param id the id of the mixingReportChecklistDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the mixingReportChecklistDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<MixingReportChecklistDTO>> getMixingReportChecklist(@PathVariable("id") UUID id) {
        log.debug("REST request to get MixingReportChecklist : {}", id);
        Mono<MixingReportChecklistDTO> mixingReportChecklistDTO = mixingReportChecklistService.findOne(id);
        return ResponseUtil.wrapOrNotFound(mixingReportChecklistDTO);
    }

    /**
     * {@code DELETE  /mixing-report-checklists/:id} : delete the "id" mixingReportChecklist.
     *
     * @param id the id of the mixingReportChecklistDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteMixingReportChecklist(@PathVariable("id") UUID id) {
        log.debug("REST request to delete MixingReportChecklist : {}", id);
        return mixingReportChecklistService
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
