package com.masi.logistics.web.rest;

import com.carevn.masi.dto.ApiResponse;
import com.carevn.masi.utils.SecurityUtils;
import com.masi.logistics.domain.criteria.InventoriesCriteria;
import com.masi.logistics.domain.criteria.InventoriesTypeCriteria;
import com.masi.logistics.repository.InventoriesTypeRepository;
import com.masi.logistics.security.AuthoritiesConstants;
import com.masi.logistics.service.InventoriesTypeService;
import com.masi.logistics.service.dto.InventoriesDTO;
import com.masi.logistics.service.dto.InventoriesTypeDTO;
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
 * REST controller for managing {@link com.masi.logistics.domain.InventoriesType}.
 */
@RestController
@RequestMapping("/api/inventories-types")
public class InventoriesTypeResource {

    private static final Logger log = LoggerFactory.getLogger(InventoriesTypeResource.class);

    private static final String ENTITY_NAME = "masiLogisticsInventoriesType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InventoriesTypeService inventoriesTypeService;

    private final InventoriesTypeRepository inventoriesTypeRepository;

    public InventoriesTypeResource(InventoriesTypeService inventoriesTypeService, InventoriesTypeRepository inventoriesTypeRepository) {
        this.inventoriesTypeService = inventoriesTypeService;
        this.inventoriesTypeRepository = inventoriesTypeRepository;
    }

    /**
     * {@code POST  /inventories-types} : Create a new inventoriesType.
     *
     * @param inventoriesTypeDTO the inventoriesTypeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new inventoriesTypeDTO, or with status {@code 400 (Bad Request)} if the inventoriesType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<InventoriesTypeDTO>> createInventoriesType(@Valid @RequestBody InventoriesTypeDTO inventoriesTypeDTO)
        throws URISyntaxException {
        log.debug("REST request to save InventoriesType : {}", inventoriesTypeDTO);

        inventoriesTypeDTO.setId(UUID.randomUUID());
        return inventoriesTypeService
            .save(inventoriesTypeDTO)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/inventories-types/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }


    /**
     * {@code PATCH  /inventories-types/:id} : Partial updates given fields of an existing inventoriesType, field will ignore if it is null
     *
     * @param id the id of the inventoriesTypeDTO to save.
     * @param inventoriesTypeDTO the inventoriesTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated inventoriesTypeDTO,
     * or with status {@code 400 (Bad Request)} if the inventoriesTypeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the inventoriesTypeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the inventoriesTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<InventoriesTypeDTO>> partialUpdateInventoriesType(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody InventoriesTypeDTO inventoriesTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update InventoriesType partially : {}, {}", id, inventoriesTypeDTO);
        if (inventoriesTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, inventoriesTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return inventoriesTypeRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<InventoriesTypeDTO> result = inventoriesTypeService.partialUpdate(inventoriesTypeDTO);

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
     * {@code GET  /inventories-types} : get all the inventoriesTypes.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of inventoriesTypes in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<ApiResponse<InventoriesTypeDTO>>> getAllInventoriesTypes(
            InventoriesTypeCriteria criteria,
            @org.springdoc.core.annotations.ParameterObject Pageable pageable,
            ServerHttpRequest request
    ) {
        log.debug("REST request to get InventoriesTypes by criteria: {}", criteria);
        return SecurityUtils.getUserJWTDetail().flatMap(user ->{
            if (!user.getAuthorities().contains(AuthoritiesConstants.SUPER_ADMIN)){
                var company = (criteria.getCompany() == null ? new StringFilter(): criteria.getCompany()).getEquals();
                if (company == null){
                    criteria.company().setEquals(user.getCompanyId());
                }
            }
            else {
                criteria.company().setEquals(user.getCompanyId());
            }
            return inventoriesTypeService
                    .countByCriteria(criteria)
                    .zipWith(inventoriesTypeService.findByCriteria(criteria, pageable).collectList())
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
     * {@code GET  /inventories-types/count} : count all the inventoriesTypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public Mono<ResponseEntity<Long>> countInventoriesTypes(InventoriesTypeCriteria criteria) {
        log.debug("REST request to count InventoriesTypes by criteria: {}", criteria);
        return inventoriesTypeService.countByCriteria(criteria).map(count -> ResponseEntity.status(HttpStatus.OK).body(count));
    }

    /**
     * {@code GET  /inventories-types/:id} : get the "id" inventoriesType.
     *
     * @param id the id of the inventoriesTypeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the inventoriesTypeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<InventoriesTypeDTO>> getInventoriesType(@PathVariable("id") UUID id) {
        log.debug("REST request to get InventoriesType : {}", id);
        Mono<InventoriesTypeDTO> inventoriesTypeDTO = inventoriesTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(inventoriesTypeDTO);
    }

    /**
     * {@code DELETE  /inventories-types/:id} : delete the "id" inventoriesType.
     *
     * @param id the id of the inventoriesTypeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteInventoriesType(@PathVariable("id") UUID id) {
        log.debug("REST request to delete InventoriesType : {}", id);
        return inventoriesTypeService
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
