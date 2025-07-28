package com.masi.logistics.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import com.masi.logistics.service.dto.UniformOrderDTO;
import com.masi.logistics.service.dto.UniformReleaseDTO;
import io.swagger.v3.oas.annotations.Operation;
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

import com.carevn.masi.dto.ApiResponse;
import com.masi.logistics.repository.UomRepository;
import com.masi.logistics.service.UomService;
import com.masi.logistics.service.dto.UomDTO;
import com.masi.logistics.web.rest.errors.BadRequestAlertException;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link com.masi.logistics.domain.Uom}.
 */
@RestController
@RequestMapping("/api/uoms")
public class UomResource {

    private static final Logger log = LoggerFactory.getLogger(UomResource.class);

    private static final String ENTITY_NAME = "masiLogisticsUom";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UomService uomService;

    private final UomRepository uomRepository;

    public UomResource(UomService uomService, UomRepository uomRepository) {
        this.uomService = uomService;
        this.uomRepository = uomRepository;
    }

    /**
     * {@code POST  /uoms} : Create a new uom.
     *
     * @param uomDTO the uomDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new uomDTO, or with status {@code 400 (Bad Request)} if the uom has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<UomDTO>> createUom(@Valid @RequestBody UomDTO uomDTO) throws URISyntaxException {
        log.debug("REST request to save Uom : {}", uomDTO);
        if (uomDTO.getId() != null) {
            throw new BadRequestAlertException("A new uom cannot already have an ID", ENTITY_NAME, "idexists");
        }
        //uomDTO.setId(UUID.randomUUID());
        return uomService
            .save(uomDTO)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/uoms/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /uoms/:id} : Updates an existing uom.
     *
     * @param id     the id of the uomDTO to save.
     * @param uomDTO the uomDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated uomDTO,
     * or with status {@code 400 (Bad Request)} if the uomDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the uomDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<UomDTO>> updateUom(
        @PathVariable(value = "id", required = false) final UUID id,
        @Valid @RequestBody UomDTO uomDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Uom : {}, {}", id, uomDTO);
        if (uomDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, uomDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return uomRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return uomService
                    .update(uomDTO)
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
     * {@code PATCH  /uoms/:id} : Partial updates given fields of an existing uom, field will ignore if it is null
     *
     * @param id     the id of the uomDTO to save.
     * @param uomDTO the uomDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated uomDTO,
     * or with status {@code 400 (Bad Request)} if the uomDTO is not valid,
     * or with status {@code 404 (Not Found)} if the uomDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the uomDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = {"application/json", "application/merge-patch+json"})
    public Mono<ResponseEntity<UomDTO>> partialUpdateUom(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody UomDTO uomDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Uom partially : {}, {}", id, uomDTO);
        if (uomDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, uomDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return uomRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<UomDTO> result = uomService.partialUpdate(uomDTO);

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
     * {@code GET  /uoms} : get all the uoms.
     *
     * @param pageable the pagination information.
     * @param request  a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of uoms in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<ApiResponse<UomDTO>>> getAllUoms(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of Uoms");
        return uomService
            .countAll()
            .zipWith(uomService.findAll(pageable).collectList())
            .map(countWithEntities ->
                ResponseEntity.ok()
                            .headers(
                                PaginationUtil.generatePaginationHttpHeaders(
                                    ForwardedHeaderUtils
                                        .adaptFromForwardedHeaders(
                                            request.getURI(),
                                            request.getHeaders()),
                                    new PageImpl<>(countWithEntities.getT2(), pageable,
                                        countWithEntities.getT1())))
                            .body(new ApiResponse<>(countWithEntities.getT2(), countWithEntities.getT1()))
            );
    }

    /**
     * {@code GET  /uoms/:id} : get the "id" uom.
     *
     * @param id the id of the uomDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the uomDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<UomDTO>> getUom(@PathVariable("id") UUID id) {
        log.debug("REST request to get Uom : {}", id);
        Mono<UomDTO> uomDTO = uomService.findOne(id);
        return ResponseUtil.wrapOrNotFound(uomDTO);
    }

    /**
     * {@code DELETE  /uoms/:id} : delete the "id" uom.
     *
     * @param id the id of the uomDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteUom(@PathVariable("id") UUID id) {
        log.debug("REST request to delete Uom : {}", id);
        return uomService
            .delete(id)
            .then(
                Mono.just(
                    ResponseEntity.noContent()
                        .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                        .build()
                )
            );
    }

    @Operation(summary = "Get all Employees by list of ids")
    @GetMapping("/list")
    public Mono<ResponseEntity<List<UomDTO>>> getAllEmployeesByIds(@RequestParam List<UUID> ids) {
        log.debug("REST request to get all Employees by list of ids");
        return uomService.findAllByListId(ids).collectList().map(ResponseEntity::ok);
    }


    @Operation(summary = "uniform-order-mapping")
    @PostMapping("/uniform-order-mapping")
    public Mono<ResponseEntity<UomService.UniformEntityResponse>> mappingDataOrder(@RequestBody UniformOrderDTO uniformOrderDTO) {
        log.debug("REST request to get all uniform-order-mapping by list of ids");
        return uomService.mappingDataLogisticToOrderDTO(uniformOrderDTO).map(ResponseEntity::ok);
    }

    @Operation(summary = "uniform-release-mapping")
    @PostMapping("/uniform-release-mapping")
    public Mono<ResponseEntity<UomService.UniformEntityResponse>> mappingDataRelease(@RequestBody UniformReleaseDTO releaseDTO) {
        log.debug("REST request to get all uniform-release-mapping by list of ids");
        return uomService.mappingDataLogisticToReleaseDTO(releaseDTO).map(ResponseEntity::ok);
    }
}
