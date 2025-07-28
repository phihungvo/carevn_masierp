package com.masi.employee.web.rest;

import com.carevn.masi.dto.ApiResponse;
import com.masi.employee.domain.criteria.TypeRequestApprovalPagesCriteria;
import com.masi.employee.repository.TypeRequestApprovalPagesRepository;
import com.masi.employee.service.TypeRequestApprovalPagesService;
import com.masi.employee.service.dto.TypeRequestApprovalPagesDTO;
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
 * REST controller for managing {@link com.masi.employee.domain.TypeRequestApprovalPages}.
 */
@RestController
@RequestMapping("/api/type-request-approval-pages")
public class TypeRequestApprovalPagesResource {

    private static final Logger log = LoggerFactory.getLogger(TypeRequestApprovalPagesResource.class);

    private static final String ENTITY_NAME = "masiEmployeeTypeRequestApprovalPages";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TypeRequestApprovalPagesService typeRequestApprovalPagesService;

    private final TypeRequestApprovalPagesRepository typeRequestApprovalPagesRepository;

    public TypeRequestApprovalPagesResource(
        TypeRequestApprovalPagesService typeRequestApprovalPagesService,
        TypeRequestApprovalPagesRepository typeRequestApprovalPagesRepository
    ) {
        this.typeRequestApprovalPagesService = typeRequestApprovalPagesService;
        this.typeRequestApprovalPagesRepository = typeRequestApprovalPagesRepository;
    }

    /**
     * {@code POST  /type-request-approval-pages} : Create a new typeRequestApprovalPages.
     *
     * @param typeRequestApprovalPagesDTO the typeRequestApprovalPagesDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new typeRequestApprovalPagesDTO, or with status {@code 400 (Bad Request)} if the typeRequestApprovalPages has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<TypeRequestApprovalPagesDTO>> createTypeRequestApprovalPages(
        @Valid @RequestBody TypeRequestApprovalPagesDTO typeRequestApprovalPagesDTO
    ) throws URISyntaxException {
        log.debug("REST request to save TypeRequestApprovalPages : {}", typeRequestApprovalPagesDTO);
        if (typeRequestApprovalPagesDTO.getId() != null) {
            throw new BadRequestAlertException("A new typeRequestApprovalPages cannot already have an ID", ENTITY_NAME, "idexists");
        }
        typeRequestApprovalPagesDTO.setId(UUID.randomUUID());
        return typeRequestApprovalPagesService
            .save(typeRequestApprovalPagesDTO)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/type-request-approval-pages/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new BadRequestAlertException("Invalid URI", ENTITY_NAME, "invaliduri");
                }
            });
    }

    /**
     * {@code PATCH  /type-request-approval-pages/:id} : Partial updates given fields of an existing typeRequestApprovalPages, field will ignore if it is null
     *
     * @param id the id of the typeRequestApprovalPagesDTO to save.
     * @param typeRequestApprovalPagesDTO the typeRequestApprovalPagesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated typeRequestApprovalPagesDTO,
     * or with status {@code 400 (Bad Request)} if the typeRequestApprovalPagesDTO is not valid,
     * or with status {@code 404 (Not Found)} if the typeRequestApprovalPagesDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the typeRequestApprovalPagesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<TypeRequestApprovalPagesDTO>> partialUpdateTypeRequestApprovalPages(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody TypeRequestApprovalPagesDTO typeRequestApprovalPagesDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update TypeRequestApprovalPages partially : {}, {}", id, typeRequestApprovalPagesDTO);
        return typeRequestApprovalPagesRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }
                Mono<TypeRequestApprovalPagesDTO> result = typeRequestApprovalPagesService.partialUpdate(typeRequestApprovalPagesDTO);
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
     * {@code GET  /type-request-approval-pages} : get all the typeRequestApprovalPages.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of typeRequestApprovalPages in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ApiResponse<TypeRequestApprovalPagesDTO>> getAllTypeRequestApprovalPages(
            TypeRequestApprovalPagesCriteria criteria,
            @org.springdoc.core.annotations.ParameterObject Pageable pageable,
            ServerHttpRequest request
    ) {
        log.debug("REST request to get TypeRequestApprovalPages by criteria: {}", criteria);
        return typeRequestApprovalPagesService
                .countByCriteria(criteria)
                .zipWith(typeRequestApprovalPagesService.findByCriteria(criteria, pageable).collectList())
                .map(
                        countWithEntities -> new ApiResponse<>(countWithEntities.getT2(), countWithEntities.getT1())
                );
    }

    /**
     * {@code GET  /type-request-approval-pages/:id} : get the "id" typeRequestApprovalPages.
     *
     * @param id the id of the typeRequestApprovalPagesDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the typeRequestApprovalPagesDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<TypeRequestApprovalPagesDTO>> getTypeRequestApprovalPages(@PathVariable("id") UUID id) {
        log.debug("REST request to get TypeRequestApprovalPages : {}", id);
        Mono<TypeRequestApprovalPagesDTO> typeRequestApprovalPagesDTO = typeRequestApprovalPagesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(typeRequestApprovalPagesDTO);
    }

}
