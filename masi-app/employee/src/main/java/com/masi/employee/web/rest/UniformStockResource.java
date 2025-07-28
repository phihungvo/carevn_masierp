package com.masi.employee.web.rest;

import com.carevn.masi.dto.ApiResponse;
import com.carevn.masi.utils.SecurityUtils;
import com.masi.employee.repository.UniformStockRepository;
import com.masi.employee.service.UniformStockService;
import com.masi.employee.service.dto.UniformOrderDTO;
import com.masi.employee.service.dto.UniformStockDTO;
import com.masi.employee.service.dto.UniformStockReleaseDTO;
import com.masi.employee.web.rest.errors.BadRequestAlertException;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.annotations.ParameterObject;
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
import reactor.util.function.Tuple2;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link com.masi.employee.domain.UniformStock}.
 */
@RestController
@RequestMapping("/api/uniform-stocks")
public class UniformStockResource {

    private static final Logger log = LoggerFactory.getLogger(UniformStockResource.class);

    private static final String ENTITY_NAME = "masiEmployeeUniformStock";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UniformStockService uniformStockService;

    private final UniformStockRepository uniformStockRepository;

    public UniformStockResource(UniformStockService uniformStockService,
            UniformStockRepository uniformStockRepository) {
        this.uniformStockService = uniformStockService;
        this.uniformStockRepository = uniformStockRepository;
    }

    /**
     * {@code POST  /uniform-stocks} : Create a new uniformStock.
     *
     * @param uniformStockDTO the uniformStockDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with
     *         body the new uniformStockDTO, or with status
     *         {@code 400 (Bad Request)} if the uniformStock has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Operation(hidden = true)
    @PostMapping("")
    public Mono<ResponseEntity<UniformStockDTO>> createUniformStock(@Valid @RequestBody UniformStockDTO uniformStockDTO)
            throws URISyntaxException {
        log.debug("REST request to save UniformStock : {}", uniformStockDTO);
        if (uniformStockDTO.getId() != null) {
            throw new BadRequestAlertException("A new uniformStock cannot already have an ID", ENTITY_NAME, "idexists");
        }
        uniformStockDTO.setId(UUID.randomUUID());
        return uniformStockService
                .save(uniformStockDTO)
                .map(result -> {
                    try {
                        return ResponseEntity.created(new URI("/api/uniform-stocks/" + result.getId()))
                                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME,
                                        result.getId().toString()))
                                .body(result);
                    } catch (URISyntaxException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    /**
     * {@code PUT  /uniform-stocks/:id} : Updates an existing uniformStock.
     *
     * @param id              the id of the uniformStockDTO to save.
     * @param uniformStockDTO the uniformStockDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the updated uniformStockDTO,
     *         or with status {@code 400 (Bad Request)} if the uniformStockDTO is
     *         not valid,
     *         or with status {@code 500 (Internal Server Error)} if the
     *         uniformStockDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Operation(hidden = true)
    @PutMapping("/{id}")
    public Mono<ResponseEntity<UniformStockDTO>> updateUniformStock(
            @PathVariable(value = "id", required = false) final UUID id,
            @Valid @RequestBody UniformStockDTO uniformStockDTO) throws URISyntaxException {
        log.debug("REST request to update UniformStock : {}, {}", id, uniformStockDTO);
        if (uniformStockDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, uniformStockDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return uniformStockRepository
                .existsById(id)
                .flatMap(exists -> {
                    if (!exists) {
                        return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                    }

                    return uniformStockService
                            .update(uniformStockDTO)
                            .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                            .map(
                                    result -> ResponseEntity.ok()
                                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true,
                                                    ENTITY_NAME, result.getId().toString()))
                                            .body(result));
                });
    }

    /**
     * {@code PATCH  /uniform-stocks/:id} : Partial updates given fields of an
     * existing uniformStock, field will ignore if it is null
     *
     * @param id              the id of the uniformStockDTO to save.
     * @param uniformStockDTO the uniformStockDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the updated uniformStockDTO,
     *         or with status {@code 400 (Bad Request)} if the uniformStockDTO is
     *         not valid,
     *         or with status {@code 404 (Not Found)} if the uniformStockDTO is not
     *         found,
     *         or with status {@code 500 (Internal Server Error)} if the
     *         uniformStockDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Operation(hidden = true)
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<UniformStockDTO>> partialUpdateUniformStock(
            @PathVariable(value = "id", required = false) final UUID id,
            @NotNull @RequestBody UniformStockDTO uniformStockDTO) throws URISyntaxException {
        log.debug("REST request to partial update UniformStock partially : {}, {}", id, uniformStockDTO);
        if (uniformStockDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, uniformStockDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return uniformStockRepository
                .existsById(id)
                .flatMap(exists -> {
                    if (!exists) {
                        return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                    }

                    Mono<UniformStockDTO> result = uniformStockService.partialUpdate(uniformStockDTO);

                    return result
                            .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                            .map(
                                    res -> ResponseEntity.ok()
                                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true,
                                                    ENTITY_NAME, res.getId().toString()))
                                            .body(res));
                });
    }

    /**
     * {@code GET  /uniform-stocks} : get all the uniformStocks.
     *
     * @param pageable the pagination information.
     * @param request  a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
     *         of uniformStocks in body.
     */
    @Operation(summary = "Get all the uniformStocks")
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<ApiResponse<UniformStockDTO>>> getAllUniformStocks(
            @org.springdoc.core.annotations.ParameterObject Pageable pageable,
            @RequestParam(required = false) String status,
            ServerHttpRequest request) {
        log.debug("REST request to get a page of UniformStocks");
        return SecurityUtils.getUserJWTDetail().flatMap(user -> {
            if (user.getCompanyId() == null) {
                return Mono.error(new ResponseStatusException(HttpStatus.FORBIDDEN));
            }
            return uniformStockService
                    .countAllByQuery(user.getCompanyId(), status)
                    .zipWith(uniformStockService.findAllByQuery(pageable, user.getCompanyId(), status).collectList())
                    .map(countWithEntities -> handleGetListStockLambda(pageable, request, countWithEntities));
        });
    }

    private ResponseEntity<ApiResponse<UniformStockDTO>> handleGetListStockLambda(Pageable pageable,
            ServerHttpRequest request,
            Tuple2<Long, List<UniformStockDTO>> countWithEntities) {
        return ResponseEntity.ok()
                .headers(
                        PaginationUtil.generatePaginationHttpHeaders(
                                ForwardedHeaderUtils
                                        .adaptFromForwardedHeaders(
                                                request.getURI(),
                                                request.getHeaders()),
                                new PageImpl<>(countWithEntities.getT2(), pageable,
                                        countWithEntities.getT1())))
                .body(new ApiResponse<>(countWithEntities.getT2(), countWithEntities.getT1()));
    }

    /**
     * {@code GET  /uniform-stocks/:id} : get the "id" uniformStock.
     *
     * @param id the id of the uniformStockDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the uniformStockDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<UniformStockDTO>> getUniformStock(@PathVariable("id") UUID id) {
        log.debug("REST request to get UniformStock : {}", id);
        Mono<UniformStockDTO> uniformStockDTO = uniformStockService.findOne(id);
        return ResponseUtil.wrapOrNotFound(uniformStockDTO);
    }

    /**
     * {@code DELETE  /uniform-stocks/:id} : delete the "id" uniformStock.
     *
     * @param id the id of the uniformStockDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @Operation(hidden = true)
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteUniformStock(@PathVariable("id") UUID id) {
        log.debug("REST request to delete UniformStock : {}", id);
        return uniformStockService
                .delete(id)
                .then(
                        Mono.just(
                                ResponseEntity.noContent()
                                        .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true,
                                                ENTITY_NAME, id.toString()))
                                        .build()));
    }

}
