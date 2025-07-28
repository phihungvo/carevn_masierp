package com.masi.employee.web.rest;

import com.masi.employee.repository.UniformFormDetailRepository;
import com.masi.employee.service.UniformFormDetailService;
import com.masi.employee.service.dto.UniformFormDetailDTO;
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
 * REST controller for managing {@link com.masi.employee.domain.UniformFormDetail}.
 */
@RestController
@RequestMapping("/api/uniform-form-details")
public class UniformFormDetailResource {

    private static final Logger log = LoggerFactory.getLogger(UniformFormDetailResource.class);

    private static final String ENTITY_NAME = "masiEmployeeUniformFormDetail";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UniformFormDetailService uniformFormDetailService;

    private final UniformFormDetailRepository uniformFormDetailRepository;

    public UniformFormDetailResource(
        UniformFormDetailService uniformFormDetailService,
        UniformFormDetailRepository uniformFormDetailRepository
    ) {
        this.uniformFormDetailService = uniformFormDetailService;
        this.uniformFormDetailRepository = uniformFormDetailRepository;
    }

    /**
     * {@code POST  /uniform-form-details} : Create a new uniformFormDetail.
     *
     * @param uniformFormDetailDTO the uniformFormDetailDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new uniformFormDetailDTO, or with status {@code 400 (Bad Request)} if the uniformFormDetail has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<UniformFormDetailDTO>> createUniformFormDetail(
        @Valid @RequestBody UniformFormDetailDTO uniformFormDetailDTO
    ) throws URISyntaxException {
        log.debug("REST request to save UniformFormDetail : {}", uniformFormDetailDTO);
        if (uniformFormDetailDTO.getId() != null) {
            throw new BadRequestAlertException("A new uniformFormDetail cannot already have an ID", ENTITY_NAME, "idexists");
        }
        uniformFormDetailDTO.setId(UUID.randomUUID());
        return uniformFormDetailService
            .save(uniformFormDetailDTO)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/uniform-form-details/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /uniform-form-details/:id} : Updates an existing uniformFormDetail.
     *
     * @param id the id of the uniformFormDetailDTO to save.
     * @param uniformFormDetailDTO the uniformFormDetailDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated uniformFormDetailDTO,
     * or with status {@code 400 (Bad Request)} if the uniformFormDetailDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the uniformFormDetailDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<UniformFormDetailDTO>> updateUniformFormDetail(
        @PathVariable(value = "id", required = false) final UUID id,
        @Valid @RequestBody UniformFormDetailDTO uniformFormDetailDTO
    ) throws URISyntaxException {
        log.debug("REST request to update UniformFormDetail : {}, {}", id, uniformFormDetailDTO);
        if (uniformFormDetailDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, uniformFormDetailDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return uniformFormDetailRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return uniformFormDetailService
                    .update(uniformFormDetailDTO)
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
     * {@code PATCH  /uniform-form-details/:id} : Partial updates given fields of an existing uniformFormDetail, field will ignore if it is null
     *
     * @param id the id of the uniformFormDetailDTO to save.
     * @param uniformFormDetailDTO the uniformFormDetailDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated uniformFormDetailDTO,
     * or with status {@code 400 (Bad Request)} if the uniformFormDetailDTO is not valid,
     * or with status {@code 404 (Not Found)} if the uniformFormDetailDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the uniformFormDetailDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<UniformFormDetailDTO>> partialUpdateUniformFormDetail(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody UniformFormDetailDTO uniformFormDetailDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update UniformFormDetail partially : {}, {}", id, uniformFormDetailDTO);
        if (uniformFormDetailDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, uniformFormDetailDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return uniformFormDetailRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<UniformFormDetailDTO> result = uniformFormDetailService.partialUpdate(uniformFormDetailDTO);

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
     * {@code GET  /uniform-form-details} : get all the uniformFormDetails.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of uniformFormDetails in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<UniformFormDetailDTO>>> getAllUniformFormDetails(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of UniformFormDetails");
        return uniformFormDetailService
            .countAll()
            .zipWith(uniformFormDetailService.findAll(pageable).collectList())
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
     * {@code GET  /uniform-form-details/:id} : get the "id" uniformFormDetail.
     *
     * @param id the id of the uniformFormDetailDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the uniformFormDetailDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<UniformFormDetailDTO>> getUniformFormDetail(@PathVariable("id") UUID id) {
        log.debug("REST request to get UniformFormDetail : {}", id);
        Mono<UniformFormDetailDTO> uniformFormDetailDTO = uniformFormDetailService.findOne(id);
        return ResponseUtil.wrapOrNotFound(uniformFormDetailDTO);
    }

    /**
     * {@code DELETE  /uniform-form-details/:id} : delete the "id" uniformFormDetail.
     *
     * @param id the id of the uniformFormDetailDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteUniformFormDetail(@PathVariable("id") UUID id) {
        log.debug("REST request to delete UniformFormDetail : {}", id);
        return uniformFormDetailService
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
