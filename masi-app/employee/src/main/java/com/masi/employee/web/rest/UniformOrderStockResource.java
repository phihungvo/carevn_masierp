package com.masi.employee.web.rest;

import com.masi.employee.repository.UniformOrderStockRepository;
import com.masi.employee.service.UniformOrderStockService;
import com.masi.employee.service.dto.UniformOrderStockDTO;
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
 * REST controller for managing {@link com.masi.employee.domain.UniformOrderStock}.
 */
@RestController
@RequestMapping("/api/uniform-order-stocks")
public class UniformOrderStockResource {

    private static final Logger log = LoggerFactory.getLogger(UniformOrderStockResource.class);

    private static final String ENTITY_NAME = "masiEmployeeUniformOrderStock";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UniformOrderStockService uniformOrderStockService;

    private final UniformOrderStockRepository uniformOrderStockRepository;

    public UniformOrderStockResource(
        UniformOrderStockService uniformOrderStockService,
        UniformOrderStockRepository uniformOrderStockRepository
    ) {
        this.uniformOrderStockService = uniformOrderStockService;
        this.uniformOrderStockRepository = uniformOrderStockRepository;
    }

    /**
     * {@code POST  /uniform-order-stocks} : Create a new uniformOrderStock.
     *
     * @param uniformOrderStockDTO the uniformOrderStockDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new uniformOrderStockDTO, or with status {@code 400 (Bad Request)} if the uniformOrderStock has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<UniformOrderStockDTO>> createUniformOrderStock(
        @Valid @RequestBody UniformOrderStockDTO uniformOrderStockDTO
    ) throws URISyntaxException {
        log.debug("REST request to save UniformOrderStock : {}", uniformOrderStockDTO);
        if (uniformOrderStockDTO.getId() != null) {
            throw new BadRequestAlertException("A new uniformOrderStock cannot already have an ID", ENTITY_NAME, "idexists");
        }
        uniformOrderStockDTO.setId(UUID.randomUUID());
        return uniformOrderStockService
            .save(uniformOrderStockDTO)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/uniform-order-stocks/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /uniform-order-stocks/:id} : Updates an existing uniformOrderStock.
     *
     * @param id the id of the uniformOrderStockDTO to save.
     * @param uniformOrderStockDTO the uniformOrderStockDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated uniformOrderStockDTO,
     * or with status {@code 400 (Bad Request)} if the uniformOrderStockDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the uniformOrderStockDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<UniformOrderStockDTO>> updateUniformOrderStock(
        @PathVariable(value = "id", required = false) final UUID id,
        @Valid @RequestBody UniformOrderStockDTO uniformOrderStockDTO
    ) throws URISyntaxException {
        log.debug("REST request to update UniformOrderStock : {}, {}", id, uniformOrderStockDTO);
        if (uniformOrderStockDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, uniformOrderStockDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return uniformOrderStockRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return uniformOrderStockService
                    .update(uniformOrderStockDTO)
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
     * {@code PATCH  /uniform-order-stocks/:id} : Partial updates given fields of an existing uniformOrderStock, field will ignore if it is null
     *
     * @param id the id of the uniformOrderStockDTO to save.
     * @param uniformOrderStockDTO the uniformOrderStockDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated uniformOrderStockDTO,
     * or with status {@code 400 (Bad Request)} if the uniformOrderStockDTO is not valid,
     * or with status {@code 404 (Not Found)} if the uniformOrderStockDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the uniformOrderStockDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<UniformOrderStockDTO>> partialUpdateUniformOrderStock(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody UniformOrderStockDTO uniformOrderStockDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update UniformOrderStock partially : {}, {}", id, uniformOrderStockDTO);
        if (uniformOrderStockDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, uniformOrderStockDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return uniformOrderStockRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<UniformOrderStockDTO> result = uniformOrderStockService.partialUpdate(uniformOrderStockDTO);

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
     * {@code GET  /uniform-order-stocks} : get all the uniformOrderStocks.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of uniformOrderStocks in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<UniformOrderStockDTO>>> getAllUniformOrderStocks(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of UniformOrderStocks");
        return uniformOrderStockService
            .countAll()
            .zipWith(uniformOrderStockService.findAll(pageable).collectList())
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
     * {@code GET  /uniform-order-stocks/:id} : get the "id" uniformOrderStock.
     *
     * @param id the id of the uniformOrderStockDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the uniformOrderStockDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<UniformOrderStockDTO>> getUniformOrderStock(@PathVariable("id") UUID id) {
        log.debug("REST request to get UniformOrderStock : {}", id);
        Mono<UniformOrderStockDTO> uniformOrderStockDTO = uniformOrderStockService.findOne(id);
        return ResponseUtil.wrapOrNotFound(uniformOrderStockDTO);
    }

    /**
     * {@code DELETE  /uniform-order-stocks/:id} : delete the "id" uniformOrderStock.
     *
     * @param id the id of the uniformOrderStockDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteUniformOrderStock(@PathVariable("id") UUID id) {
        log.debug("REST request to delete UniformOrderStock : {}", id);
        return uniformOrderStockService
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
