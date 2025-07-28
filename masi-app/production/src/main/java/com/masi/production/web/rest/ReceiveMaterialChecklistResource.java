package com.masi.production.web.rest;

import com.masi.production.repository.ReceiveMaterialChecklistRepository;
import com.masi.production.service.ReceiveMaterialChecklistService;
import com.masi.production.service.dto.ReceiveMaterialChecklistDTO;
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
 * REST controller for managing {@link com.masi.production.domain.ReceiveMaterialChecklist}.
 */
@RestController
@RequestMapping("/api/receive-material-checklists")
public class ReceiveMaterialChecklistResource {

    private final Logger log = LoggerFactory.getLogger(ReceiveMaterialChecklistResource.class);

    private static final String ENTITY_NAME = "masiProductionReceiveMaterialChecklist";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ReceiveMaterialChecklistService receiveMaterialChecklistService;

    private final ReceiveMaterialChecklistRepository receiveMaterialChecklistRepository;

    public ReceiveMaterialChecklistResource(
        ReceiveMaterialChecklistService receiveMaterialChecklistService,
        ReceiveMaterialChecklistRepository receiveMaterialChecklistRepository
    ) {
        this.receiveMaterialChecklistService = receiveMaterialChecklistService;
        this.receiveMaterialChecklistRepository = receiveMaterialChecklistRepository;
    }

    /**
     * {@code POST  /receive-material-checklists} : Create a new receiveMaterialChecklist.
     *
     * @param receiveMaterialChecklistDTO the receiveMaterialChecklistDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new receiveMaterialChecklistDTO, or with status {@code 400 (Bad Request)} if the receiveMaterialChecklist has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ReceiveMaterialChecklistDTO> createReceiveMaterialChecklist(
        @Valid @RequestBody ReceiveMaterialChecklistDTO receiveMaterialChecklistDTO
    ) throws URISyntaxException {
        log.debug("REST request to save ReceiveMaterialChecklist : {}", receiveMaterialChecklistDTO);
        if (receiveMaterialChecklistDTO.getId() != null) {
            throw new BadRequestAlertException("A new receiveMaterialChecklist cannot already have an ID", ENTITY_NAME, "idexists");
        }
        receiveMaterialChecklistDTO.setId(UUID.randomUUID());
        return receiveMaterialChecklistService
            .save(receiveMaterialChecklistDTO)
            .doOnError(
                throwable -> {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, throwable.getMessage());
                }
            );
    }

    /**
     * {@code PATCH  /receive-material-checklists/:id} : Partial updates given fields of an existing receiveMaterialChecklist, field will ignore if it is null
     *
     * @param id the id of the receiveMaterialChecklistDTO to save.
     * @param receiveMaterialChecklistDTO the receiveMaterialChecklistDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated receiveMaterialChecklistDTO,
     * or with status {@code 400 (Bad Request)} if the receiveMaterialChecklistDTO is not valid,
     * or with status {@code 404 (Not Found)} if the receiveMaterialChecklistDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the receiveMaterialChecklistDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<ReceiveMaterialChecklistDTO>> partialUpdateReceiveMaterialChecklist(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody ReceiveMaterialChecklistDTO receiveMaterialChecklistDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ReceiveMaterialChecklist partially : {}, {}", id, receiveMaterialChecklistDTO);
        receiveMaterialChecklistDTO.setId(id);
        return receiveMaterialChecklistRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<ReceiveMaterialChecklistDTO> result = receiveMaterialChecklistService.partialUpdate(receiveMaterialChecklistDTO);

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

    @GetMapping("/{id}")
    public Mono<ResponseEntity<ReceiveMaterialChecklistDTO>> getReceiveMaterialChecklist(@PathVariable UUID id) {
        log.debug("REST request to get ReceiveMaterialChecklist : {}", id);
        Mono<ReceiveMaterialChecklistDTO> receiveMaterialChecklistDTO = receiveMaterialChecklistService.findOne(id);
        return ResponseUtil.wrapOrNotFound(receiveMaterialChecklistDTO);
    }

    /**
     * {@code DELETE  /receive-material-checklists/:id} : delete the "id" receiveMaterialChecklist.
     *
     * @param id the id of the receiveMaterialChecklistDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteReceiveMaterialChecklist(@PathVariable("id") UUID id) {
        log.debug("REST request to delete ReceiveMaterialChecklist : {}", id);
        return receiveMaterialChecklistService
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
