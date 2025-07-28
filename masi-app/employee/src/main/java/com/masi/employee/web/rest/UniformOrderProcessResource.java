package com.masi.employee.web.rest;

import com.masi.employee.repository.UniformOrderProcessRepository;
import com.masi.employee.service.UniformOrderProcessService;
import com.masi.employee.service.dto.UniformOrderProcessDTO;
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
 * REST controller for managing {@link com.masi.employee.domain.UniformOrderProcess}.
 */
@RestController
@RequestMapping("/api/uniform-order-processes")
public class UniformOrderProcessResource {

    private static final Logger log = LoggerFactory.getLogger(UniformOrderProcessResource.class);

    private static final String ENTITY_NAME = "masiEmployeeUniformOrderProcess";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UniformOrderProcessService uniformOrderProcessService;

    private final UniformOrderProcessRepository uniformOrderProcessRepository;

    public UniformOrderProcessResource(
        UniformOrderProcessService uniformOrderProcessService,
        UniformOrderProcessRepository uniformOrderProcessRepository
    ) {
        this.uniformOrderProcessService = uniformOrderProcessService;
        this.uniformOrderProcessRepository = uniformOrderProcessRepository;
    }

    /**
     * {@code POST  /uniform-order-processes} : Create a new uniformOrderProcess.
     *
     * @param uniformOrderProcessDTO the uniformOrderProcessDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new uniformOrderProcessDTO, or with status {@code 400 (Bad Request)} if the uniformOrderProcess has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<UniformOrderProcessDTO>> createUniformOrderProcess(
        @Valid @RequestBody UniformOrderProcessDTO uniformOrderProcessDTO
    ) throws URISyntaxException {
        log.debug("REST request to save UniformOrderProcess : {}", uniformOrderProcessDTO);
        if (uniformOrderProcessDTO.getId() != null) {
            throw new BadRequestAlertException("A new uniformOrderProcess cannot already have an ID", ENTITY_NAME, "idexists");
        }
        uniformOrderProcessDTO.setId(UUID.randomUUID());
        return uniformOrderProcessService
            .save(uniformOrderProcessDTO)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/uniform-order-processes/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /uniform-order-processes/:id} : Updates an existing uniformOrderProcess.
     *
     * @param id the id of the uniformOrderProcessDTO to save.
     * @param uniformOrderProcessDTO the uniformOrderProcessDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated uniformOrderProcessDTO,
     * or with status {@code 400 (Bad Request)} if the uniformOrderProcessDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the uniformOrderProcessDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<UniformOrderProcessDTO>> updateUniformOrderProcess(
        @PathVariable(value = "id", required = false) final UUID id,
        @Valid @RequestBody UniformOrderProcessDTO uniformOrderProcessDTO
    ) throws URISyntaxException {
        log.debug("REST request to update UniformOrderProcess : {}, {}", id, uniformOrderProcessDTO);
        if (uniformOrderProcessDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, uniformOrderProcessDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return uniformOrderProcessRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return uniformOrderProcessService
                    .update(uniformOrderProcessDTO)
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
     * {@code PATCH  /uniform-order-processes/:id} : Partial updates given fields of an existing uniformOrderProcess, field will ignore if it is null
     *
     * @param id the id of the uniformOrderProcessDTO to save.
     * @param uniformOrderProcessDTO the uniformOrderProcessDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated uniformOrderProcessDTO,
     * or with status {@code 400 (Bad Request)} if the uniformOrderProcessDTO is not valid,
     * or with status {@code 404 (Not Found)} if the uniformOrderProcessDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the uniformOrderProcessDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<UniformOrderProcessDTO>> partialUpdateUniformOrderProcess(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody UniformOrderProcessDTO uniformOrderProcessDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update UniformOrderProcess partially : {}, {}", id, uniformOrderProcessDTO);
        if (uniformOrderProcessDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, uniformOrderProcessDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return uniformOrderProcessRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<UniformOrderProcessDTO> result = uniformOrderProcessService.partialUpdate(uniformOrderProcessDTO);

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
     * {@code GET  /uniform-order-processes} : get all the uniformOrderProcesses.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of uniformOrderProcesses in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<UniformOrderProcessDTO>>> getAllUniformOrderProcesses(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of UniformOrderProcesses");
        return uniformOrderProcessService
            .countAll()
            .zipWith(uniformOrderProcessService.findAll(pageable).collectList())
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
     * {@code GET  /uniform-order-processes/:id} : get the "id" uniformOrderProcess.
     *
     * @param id the id of the uniformOrderProcessDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the uniformOrderProcessDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<UniformOrderProcessDTO>> getUniformOrderProcess(@PathVariable("id") UUID id) {
        log.debug("REST request to get UniformOrderProcess : {}", id);
        Mono<UniformOrderProcessDTO> uniformOrderProcessDTO = uniformOrderProcessService.findOne(id);
        return ResponseUtil.wrapOrNotFound(uniformOrderProcessDTO);
    }

    /**
     * {@code DELETE  /uniform-order-processes/:id} : delete the "id" uniformOrderProcess.
     *
     * @param id the id of the uniformOrderProcessDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteUniformOrderProcess(@PathVariable("id") UUID id) {
        log.debug("REST request to delete UniformOrderProcess : {}", id);
        return uniformOrderProcessService
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
