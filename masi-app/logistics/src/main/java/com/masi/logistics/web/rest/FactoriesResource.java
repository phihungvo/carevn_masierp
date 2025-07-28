package com.masi.logistics.web.rest;

import com.carevn.masi.dto.ApiResponse;
import com.carevn.masi.utils.SecurityUtils;
import com.masi.logistics.domain.criteria.FactoriesCriteria;
import com.masi.logistics.repository.FactoriesRepository;
import com.masi.logistics.security.AuthoritiesConstants;
import com.masi.logistics.service.FactoriesService;
import com.masi.logistics.service.dto.FactoriesDTO;
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
import tech.jhipster.service.filter.StringFilter;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link com.masi.logistics.domain.Factories}.
 */
@RestController
@RequestMapping("/api/factories")
public class FactoriesResource {

    private static final Logger log = LoggerFactory.getLogger(FactoriesResource.class);

    private static final String ENTITY_NAME = "masiLogisticsFactories";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FactoriesService factoriesService;

    private final FactoriesRepository factoriesRepository;

    public FactoriesResource(FactoriesService factoriesService, FactoriesRepository factoriesRepository) {
        this.factoriesService = factoriesService;
        this.factoriesRepository = factoriesRepository;
    }

    /**
     * {@code POST  /factories} : Create a new factories.
     *
     * @param factoriesDTO the factoriesDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new factoriesDTO, or with status {@code 400 (Bad Request)} if the factories has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<FactoriesDTO>> createFactories(@Valid @RequestBody FactoriesDTO factoriesDTO) throws URISyntaxException {
        log.debug("REST request to save Factories : {}", factoriesDTO);

        factoriesDTO.setId(UUID.randomUUID());
        return factoriesService
            .save(factoriesDTO)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/factories/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }


    /**
     * {@code PATCH  /factories/:id} : Partial updates given fields of an existing factories, field will ignore if it is null
     *
     * @param id the id of the factoriesDTO to save.
     * @param factoriesDTO the factoriesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated factoriesDTO,
     * or with status {@code 400 (Bad Request)} if the factoriesDTO is not valid,
     * or with status {@code 404 (Not Found)} if the factoriesDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the factoriesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<FactoriesDTO>> partialUpdateFactories(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody FactoriesDTO factoriesDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Factories partially : {}, {}", id, factoriesDTO);
        factoriesDTO.setId(id);
        return factoriesRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<FactoriesDTO> result = factoriesService.partialUpdate(factoriesDTO);

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

    @PatchMapping(value = "/{id}/active")
    public Mono<ResponseEntity<FactoriesDTO>> activeFactories(
            @PathVariable(value = "id", required = false) final UUID id) throws URISyntaxException {
        return factoriesRepository
                .existsById(id)
                .flatMap(exists -> {
                    if (!exists) {
                        return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                    }

                    Mono<FactoriesDTO> result = factoriesService.changeIsActive(id, true);

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

    @PatchMapping(value = "/{id}/disable")
    public Mono<ResponseEntity<FactoriesDTO>> disableFactories(
            @PathVariable(value = "id", required = false) final UUID id) throws URISyntaxException {
        return factoriesRepository
                .existsById(id)
                .flatMap(exists -> {
                    if (!exists) {
                        return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                    }

                    Mono<FactoriesDTO> result = factoriesService.changeIsActive(id, false);

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
     * {@code GET  /factories} : get all the factories.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of factories in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<ApiResponse<FactoriesDTO>>> getAllFactories(
            FactoriesCriteria criteria,
            @org.springdoc.core.annotations.ParameterObject Pageable pageable,
            ServerHttpRequest request
    ) {
        log.debug("REST request to get Factories by criteria: {}", criteria);
        return SecurityUtils.getUserJWTDetail().flatMap(user ->{
//            if (!user.getAuthorities().contains(AuthoritiesConstants.SUPER_ADMIN)){
//                var company = (criteria.getCompany() == null ? new StringFilter(): criteria.getCompany()).getEquals();
//                if (company == null){
//                    criteria.company().setEquals(user.getCompanyId());
//                }
//            }
//            else {
//                criteria.company().setEquals(user.getCompanyId());
//            }
            return factoriesService
                    .countByCriteria(criteria)
                    .zipWith(factoriesService.findByCriteria(criteria, pageable).collectList())
                    .map(
                            countWithEntities ->
                                    ResponseEntity.ok()
                                            .headers(
                                                    PaginationUtil.generatePaginationHttpHeaders(
                                                            ForwardedHeaderUtils.adaptFromForwardedHeaders(request.getURI(), request.getHeaders()),
                                                            new PageImpl<>(countWithEntities.getT2(), pageable, countWithEntities.getT1())
                                                    )
                                            )
                                            .body(new ApiResponse<>(countWithEntities.getT2(), countWithEntities.getT1()))
                    );
        });
    }

    /**
     * {@code GET  /factories/count} : count all the factories.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public Mono<ResponseEntity<Long>> countFactories(FactoriesCriteria criteria) {
        log.debug("REST request to count Factories by criteria: {}", criteria);
        return factoriesService.countByCriteria(criteria).map(count -> ResponseEntity.status(HttpStatus.OK).body(count));
    }

    /**
     * {@code GET  /factories/:id} : get the "id" factories.
     *
     * @param id the id of the factoriesDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the factoriesDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<FactoriesDTO>> getFactories(@PathVariable("id") UUID id) {
        log.debug("REST request to get Factories : {}", id);
        Mono<FactoriesDTO> factoriesDTO = factoriesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(factoriesDTO);
    }

    /**
     * {@code DELETE  /factories/:id} : delete the "id" factories.
     *
     * @param id the id of the factoriesDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteFactories(@PathVariable("id") UUID id) {
        log.debug("REST request to delete Factories : {}", id);
        return factoriesService
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
