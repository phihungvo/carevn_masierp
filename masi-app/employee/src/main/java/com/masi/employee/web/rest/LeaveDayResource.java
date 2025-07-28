package com.masi.employee.web.rest;

import com.masi.employee.repository.LeaveDayRepository;
import com.masi.employee.service.LeaveDayService;
import com.masi.employee.service.dto.LeaveDayDTO;
import com.masi.employee.web.rest.errors.BadRequestAlertException;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link com.masi.employee.domain.LeaveDay}.
 */
@RestController
@RequestMapping("/api/leave-days")
public class LeaveDayResource {

    private final Logger log = LoggerFactory.getLogger(LeaveDayResource.class);

    private static final String ENTITY_NAME = "masiEmployeeLeaveDay";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LeaveDayService leaveDayService;

    private final LeaveDayRepository leaveDayRepository;

    public LeaveDayResource(LeaveDayService leaveDayService, LeaveDayRepository leaveDayRepository) {
        this.leaveDayService = leaveDayService;
        this.leaveDayRepository = leaveDayRepository;
    }

    /**
     * {@code POST  /leave-days} : Create a new leaveDay.
     *
     * @param leaveDayDTO the leaveDayDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new leaveDayDTO, or with status {@code 400 (Bad Request)} if the leaveDay has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<LeaveDayDTO>> createLeaveDay(@Valid @RequestBody LeaveDayDTO leaveDayDTO) throws URISyntaxException {
        log.debug("REST request to save LeaveDay : {}", leaveDayDTO);
        if (leaveDayDTO.getId() != null) {
            throw new BadRequestAlertException("A new leaveDay cannot already have an ID", ENTITY_NAME, "idexists");
        }
        leaveDayDTO.setId(UUID.randomUUID());
        return leaveDayService
            .save(leaveDayDTO)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/leave-days/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /leave-days/:id} : Updates an existing leaveDay.
     *
     * @param id the id of the leaveDayDTO to save.
     * @param leaveDayDTO the leaveDayDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated leaveDayDTO,
     * or with status {@code 400 (Bad Request)} if the leaveDayDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the leaveDayDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<LeaveDayDTO>> updateLeaveDay(
        @PathVariable(value = "id", required = false) final UUID id,
        @Valid @RequestBody LeaveDayDTO leaveDayDTO
    ) throws URISyntaxException {
        log.debug("REST request to update LeaveDay : {}, {}", id, leaveDayDTO);
        if (leaveDayDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, leaveDayDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return leaveDayRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return leaveDayService
                    .update(leaveDayDTO)
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
     * {@code PATCH  /leave-days/:id} : Partial updates given fields of an existing leaveDay, field will ignore if it is null
     *
     * @param id the id of the leaveDayDTO to save.
     * @param leaveDayDTO the leaveDayDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated leaveDayDTO,
     * or with status {@code 400 (Bad Request)} if the leaveDayDTO is not valid,
     * or with status {@code 404 (Not Found)} if the leaveDayDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the leaveDayDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<LeaveDayDTO>> partialUpdateLeaveDay(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody LeaveDayDTO leaveDayDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update LeaveDay partially : {}, {}", id, leaveDayDTO);
        if (leaveDayDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, leaveDayDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return leaveDayRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<LeaveDayDTO> result = leaveDayService.partialUpdate(leaveDayDTO);

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
     * {@code GET  /leave-days} : get all the leaveDays.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of leaveDays in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<List<LeaveDayDTO>> getAllLeaveDays() {
        log.debug("REST request to get all LeaveDays");
        return leaveDayService.findAll().collectList();
    }

    /**
     * {@code GET  /leave-days} : get all the leaveDays as a stream.
     * @return the {@link Flux} of leaveDays.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<LeaveDayDTO> getAllLeaveDaysAsStream() {
        log.debug("REST request to get all LeaveDays as a stream");
        return leaveDayService.findAll();
    }

    /**
     * {@code GET  /leave-days/:id} : get the "id" leaveDay.
     *
     * @param id the id of the leaveDayDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the leaveDayDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<LeaveDayDTO>> getLeaveDay(@PathVariable("id") UUID id) {
        log.debug("REST request to get LeaveDay : {}", id);
        Mono<LeaveDayDTO> leaveDayDTO = leaveDayService.findOne(id);
        return ResponseUtil.wrapOrNotFound(leaveDayDTO);
    }

    /**
     * {@code DELETE  /leave-days/:id} : delete the "id" leaveDay.
     *
     * @param id the id of the leaveDayDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteLeaveDay(@PathVariable("id") UUID id) {
        log.debug("REST request to delete LeaveDay : {}", id);
        return leaveDayService
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
