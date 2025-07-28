package com.masi.logistics.web.rest;

import com.masi.logistics.repository.UomGroupDetailsRepository;
import com.masi.logistics.service.UomGroupDetailsService;
import com.masi.logistics.service.dto.UomGroupDetailsDTO;
import com.masi.logistics.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link com.masi.logistics.domain.UomGroupDetails}.
 */
@RestController
@RequestMapping("/api/uom-group-details")
public class UomGroupDetailsResource {

    private static final Logger log = LoggerFactory.getLogger(UomGroupDetailsResource.class);

    private static final String ENTITY_NAME = "masiLogisticsUomGroupDetails";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UomGroupDetailsService uomGroupDetailsService;

    private final UomGroupDetailsRepository uomGroupDetailsRepository;

    public UomGroupDetailsResource(UomGroupDetailsService uomGroupDetailsService, UomGroupDetailsRepository uomGroupDetailsRepository) {
        this.uomGroupDetailsService = uomGroupDetailsService;
        this.uomGroupDetailsRepository = uomGroupDetailsRepository;
    }

    /**
     * {@code POST  /uom-group-details} : Create a new uomGroupDetails.
     *
     * @param uomGroupDetailsDTO the uomGroupDetailsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new uomGroupDetailsDTO, or with status {@code 400 (Bad Request)} if the uomGroupDetails has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<UomGroupDetailsDTO>> createUomGroupDetails(@Valid @RequestBody UomGroupDetailsDTO uomGroupDetailsDTO)
        throws URISyntaxException {
        log.debug("REST request to save UomGroupDetails : {}", uomGroupDetailsDTO);
        if (uomGroupDetailsDTO.getId() != null) {
            throw new BadRequestAlertException("A new uomGroupDetails cannot already have an ID", ENTITY_NAME, "idexists");
        }
        //uomGroupDetailsDTO.setId(UUID.randomUUID());
        return uomGroupDetailsService
            .save(uomGroupDetailsDTO)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/uom-group-details/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /uom-group-details/:id} : Updates an existing uomGroupDetails.
     *
     * @param id the id of the uomGroupDetailsDTO to save.
     * @param uomGroupDetailsDTO the uomGroupDetailsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated uomGroupDetailsDTO,
     * or with status {@code 400 (Bad Request)} if the uomGroupDetailsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the uomGroupDetailsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<UomGroupDetailsDTO>> updateUomGroupDetails(
        @PathVariable(value = "id", required = false) final UUID id,
        @Valid @RequestBody UomGroupDetailsDTO uomGroupDetailsDTO
    ) throws URISyntaxException {
        log.debug("REST request to update UomGroupDetails : {}, {}", id, uomGroupDetailsDTO);
        if (uomGroupDetailsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, uomGroupDetailsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return uomGroupDetailsRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return uomGroupDetailsService
                    .update(uomGroupDetailsDTO)
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
     * {@code PATCH  /uom-group-details/:id} : Partial updates given fields of an existing uomGroupDetails, field will ignore if it is null
     *
     * @param id the id of the uomGroupDetailsDTO to save.
     * @param uomGroupDetailsDTO the uomGroupDetailsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated uomGroupDetailsDTO,
     * or with status {@code 400 (Bad Request)} if the uomGroupDetailsDTO is not valid,
     * or with status {@code 404 (Not Found)} if the uomGroupDetailsDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the uomGroupDetailsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<UomGroupDetailsDTO>> partialUpdateUomGroupDetails(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody UomGroupDetailsDTO uomGroupDetailsDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update UomGroupDetails partially : {}, {}", id, uomGroupDetailsDTO);
        if (uomGroupDetailsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, uomGroupDetailsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return uomGroupDetailsRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<UomGroupDetailsDTO> result = uomGroupDetailsService.partialUpdate(uomGroupDetailsDTO);

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
     * {@code GET  /uom-group-details} : get all the uomGroupDetails.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of uomGroupDetails in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<UomGroupDetailsDTO>>> getAllUomGroupDetails(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of UomGroupDetails");
        return uomGroupDetailsService
            .countAll()
            .zipWith(uomGroupDetailsService.findAll(pageable).collectList())
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
     * {@code GET  /uom-group-details/:id} : get the "id" uomGroupDetails.
     *
     * @param id the id of the uomGroupDetailsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the uomGroupDetailsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<UomGroupDetailsDTO>> getUomGroupDetails(@PathVariable("id") UUID id) {
        log.debug("REST request to get UomGroupDetails : {}", id);
        Mono<UomGroupDetailsDTO> uomGroupDetailsDTO = uomGroupDetailsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(uomGroupDetailsDTO);
    }

    /**
     * {@code DELETE  /uom-group-details/:id} : delete the "id" uomGroupDetails.
     *
     * @param id the id of the uomGroupDetailsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteUomGroupDetails(@PathVariable("id") UUID id) {
        log.debug("REST request to delete UomGroupDetails : {}", id);
        return uomGroupDetailsService
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
