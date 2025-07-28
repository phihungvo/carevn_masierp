package com.masi.production.web.rest;

import com.masi.production.repository.MachineOperationChecklistRepository;
import com.masi.production.service.MachineOperationChecklistService;
import com.masi.production.service.dto.MachineOperationChecklistDTO;
import com.masi.production.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link com.masi.production.domain.MachineOperationChecklist}.
 */
@RestController
@RequestMapping("/api/machine-operation-checklists")
public class MachineOperationChecklistResource {

    private final Logger log = LoggerFactory.getLogger(MachineOperationChecklistResource.class);

    private static final String ENTITY_NAME = "masiProductionMachineOperationChecklist";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MachineOperationChecklistService machineOperationChecklistService;

    private final MachineOperationChecklistRepository machineOperationChecklistRepository;

    public MachineOperationChecklistResource(
        MachineOperationChecklistService machineOperationChecklistService,
        MachineOperationChecklistRepository machineOperationChecklistRepository
    ) {
        this.machineOperationChecklistService = machineOperationChecklistService;
        this.machineOperationChecklistRepository = machineOperationChecklistRepository;
    }

    /**
     * {@code POST  /machine-operation-checklists} : Create a new machineOperationChecklist.
     *
     * @param machineOperationChecklistDTO the machineOperationChecklistDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new machineOperationChecklistDTO, or with status {@code 400 (Bad Request)} if the machineOperationChecklist has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<MachineOperationChecklistDTO>> createMachineOperationChecklist(
        @Valid @RequestBody MachineOperationChecklistDTO machineOperationChecklistDTO
    ) throws URISyntaxException {
        log.debug("REST request to save MachineOperationChecklist : {}", machineOperationChecklistDTO);

        machineOperationChecklistDTO.setId(UUID.randomUUID());
        return machineOperationChecklistService
            .save(machineOperationChecklistDTO)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/machine-operation-checklists/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PATCH  /machine-operation-checklists/:id} : Partial updates given fields of an existing machineOperationChecklist, field will ignore if it is null
     *
     * @param id the id of the machineOperationChecklistDTO to save.
     * @param machineOperationChecklistDTO the machineOperationChecklistDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated machineOperationChecklistDTO,
     * or with status {@code 400 (Bad Request)} if the machineOperationChecklistDTO is not valid,
     * or with status {@code 404 (Not Found)} if the machineOperationChecklistDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the machineOperationChecklistDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<MachineOperationChecklistDTO>> partialUpdateMachineOperationChecklist(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody MachineOperationChecklistDTO machineOperationChecklistDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update MachineOperationChecklist partially : {}, {}", id, machineOperationChecklistDTO);
        machineOperationChecklistDTO.setId(id);

        return machineOperationChecklistRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<MachineOperationChecklistDTO> result = machineOperationChecklistService.partialUpdate(machineOperationChecklistDTO);

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
     * {@code GET  /machine-operation-checklists/:id} : get the "id" machineOperationChecklist.
     *
     * @param id the id of the machineOperationChecklistDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the machineOperationChecklistDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<MachineOperationChecklistDTO>> getMachineOperationChecklist(@PathVariable("id") UUID id) {
        log.debug("REST request to get MachineOperationChecklist : {}", id);
        Mono<MachineOperationChecklistDTO> machineOperationChecklistDTO = machineOperationChecklistService.findOne(id);
        return ResponseUtil.wrapOrNotFound(machineOperationChecklistDTO);
    }

    /**
     * {@code DELETE  /machine-operation-checklists/:id} : delete the "id" machineOperationChecklist.
     *
     * @param id the id of the machineOperationChecklistDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteMachineOperationChecklist(@PathVariable("id") UUID id) {
        log.debug("REST request to delete MachineOperationChecklist : {}", id);
        return machineOperationChecklistService
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
