package com.masi.logistics.web.rest;

import com.carevn.masi.dto.ApiResponse;
import com.carevn.masi.utils.SecurityUtils;
import com.masi.logistics.domain.InventoriesStorageTotal;
import com.masi.logistics.domain.criteria.InventoriesCheckCriteria;
import com.masi.logistics.domain.criteria.InventoriesStorageCriteria;
import com.masi.logistics.domain.enumeration.ItemStatus;
import com.masi.logistics.repository.InventoriesStorageRepository;
import com.masi.logistics.security.AuthoritiesConstants;
import com.masi.logistics.service.InventoriesStorageService;
import com.masi.logistics.service.dto.ArisesDTO;
import com.masi.logistics.service.dto.InventoriesStorageDTO;
import com.masi.logistics.service.request.ListDocumentIdRequest;
import com.masi.logistics.service.request.MaterialManuFactureRequest;
import com.masi.logistics.web.rest.errors.BadRequestAlertException;

import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.ForwardedHeaderUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.service.filter.StringFilter;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link com.masi.logistics.domain.InventoriesStorage}.
 */
@RestController
@RequestMapping("/api/inventories-storages")
public class InventoriesStorageResource {

    private static final Logger log = LoggerFactory.getLogger(InventoriesStorageResource.class);

    private static final String ENTITY_NAME = "masiLogisticsInventoriesStorage";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InventoriesStorageService inventoriesStorageService;

    private final InventoriesStorageRepository inventoriesStorageRepository;

    public InventoriesStorageResource(
            InventoriesStorageService inventoriesStorageService,
            InventoriesStorageRepository inventoriesStorageRepository
    ) {
        this.inventoriesStorageService = inventoriesStorageService;
        this.inventoriesStorageRepository = inventoriesStorageRepository;
    }

    /**
     * {@code POST  /inventories-storages} : Create a new inventoriesStorage.
     *
     * @param inventoriesStorageDTO the inventoriesStorageDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new inventoriesStorageDTO, or with status {@code 400 (Bad Request)} if the inventoriesStorage has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<InventoriesStorageDTO>> createInventoriesStorage(@RequestBody InventoriesStorageDTO inventoriesStorageDTO)
            throws URISyntaxException {
        log.debug("REST request to save InventoriesStorage : {}", inventoriesStorageDTO);
        inventoriesStorageDTO.setId(UUID.randomUUID());
        return inventoriesStorageService
                .save(inventoriesStorageDTO)
                .map(result -> {
                    try {
                        return ResponseEntity.created(new URI("/api/inventories-storages/" + result.getId()))
                                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                                .body(result);
                    } catch (URISyntaxException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    @PatchMapping("/protein/{id}/{itemId}")
    public Mono<ResponseEntity<InventoriesStorageDTO>> updateProtein(
            @PathVariable UUID id,
            @PathVariable UUID itemId,
            @RequestBody Float code) {
        log.debug("REST request to update protein for InventoriesStorage with id: {}, itemId: {}, code: {}", id, itemId, code);

        return inventoriesStorageService.updateProtein(id, itemId, code)
                .map(result -> ResponseEntity.ok()
                        .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result))
                .doOnError(e -> log.error("Error updating protein: {}", e.getMessage()))
                .switchIfEmpty(Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "notfound")));
    }


    @PatchMapping("/update-volume")
    public Mono<ResponseEntity<InventoriesStorageDTO>> updateVolume(
            @RequestBody List<MaterialManuFactureRequest> materialManuFactureRequest) {
        log.debug("REST request to update volume for InventoriesStorage : {}", materialManuFactureRequest);
        return inventoriesStorageService.updateVolume(materialManuFactureRequest)
                .map(result -> ResponseEntity.ok()
                        .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result)
                )
                .switchIfEmpty(Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "notfound")));
    }




    /**
     * {@code PATCH  /inventories-storages/:id} : Partial updates given fields of an existing inventoriesStorage, field will ignore if it is null
     *
     * @param id                    the id of the inventoriesStorageDTO to save.
     * @param inventoriesStorageDTO the inventoriesStorageDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated inventoriesStorageDTO,
     * or with status {@code 400 (Bad Request)} if the inventoriesStorageDTO is not valid,
     * or with status {@code 404 (Not Found)} if the inventoriesStorageDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the inventoriesStorageDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = {"application/json", "application/merge-patch+json"})
    public Mono<ResponseEntity<InventoriesStorageDTO>> partialUpdateInventoriesStorage(
            @PathVariable(value = "id", required = false) final UUID id,
            @RequestBody InventoriesStorageDTO inventoriesStorageDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update InventoriesStorage partially : {}, {}", id, inventoriesStorageDTO);
        inventoriesStorageDTO.setId(id);
        return inventoriesStorageRepository
                .existsById(id)
                .flatMap(exists -> {
                    if (!exists) {
                        return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                    }

                    Mono<InventoriesStorageDTO> result = inventoriesStorageService.partialUpdate(inventoriesStorageDTO);

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
     * {@code GET  /inventories-storages} : get all the inventoriesStorages.
     *
     * @param pageable the pagination information.
     * @param request  a {@link ServerHttpRequest} request.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of inventoriesStorages in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<ApiResponse<InventoriesStorageDTO>>> getAllInventoriesStorages(
            InventoriesStorageCriteria criteria,
            @org.springdoc.core.annotations.ParameterObject Pageable pageable,
            ServerHttpRequest request
    ) {
        log.debug("REST request to get InventoriesStorages by criteria: {}", criteria);

        return SecurityUtils.getUserJWTDetail().flatMap(user -> {
            if (!user.getAuthorities().contains(AuthoritiesConstants.SUPER_ADMIN)) {
                var company = (criteria.getCompany() == null ? new StringFilter() : criteria.getCompany()).getEquals();
                if (company == null) {
                    criteria.company().setEquals(user.getCompanyId());
                }
            } else {
                criteria.company().setEquals(user.getCompanyId());
            }
            return inventoriesStorageService
                    .countByCriteria(criteria)
                    .zipWith(inventoriesStorageService.findByCriteria(criteria, pageable).collectList())
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
     * {@code GET  /inventories-storages/count} : count all the inventoriesStorages.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public Mono<ResponseEntity<Long>> countInventoriesStorages(InventoriesStorageCriteria criteria) {
        log.debug("REST request to count InventoriesStorages by criteria: {}", criteria);
        return inventoriesStorageService.countByCriteria(criteria).map(count -> ResponseEntity.status(HttpStatus.OK).body(count));
    }

    @GetMapping(value = "/depreciation", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<ApiResponse<InventoriesStorageDTO>>> getAllInventoriesStoragesDepreciation(
            InventoriesStorageCriteria criteria,
            @org.springdoc.core.annotations.ParameterObject Pageable pageable,
            ServerHttpRequest request
    ) {
        log.debug("REST request to get InventoriesStorages by criteria: {}", criteria);
        if(criteria.getCheckDepreciation() == null){
            criteria.setCheckDepreciation(false);
        }

        return SecurityUtils.getUserJWTDetail().flatMap(user -> {
            if (!user.getAuthorities().contains(AuthoritiesConstants.SUPER_ADMIN)) {
                var company = (criteria.getCompany() == null ? new StringFilter() : criteria.getCompany()).getEquals();
                if (company == null) {
                    criteria.company().setEquals(user.getCompanyId());
                }
            } else {
                criteria.company().setEquals(user.getCompanyId());
            }
            StringFilter statusFilter = new StringFilter();
            statusFilter.setDoesNotContain(ItemStatus.DEPRECIATION.toString());
            criteria.setStatusDepreciation(statusFilter);
            return inventoriesStorageService
                    .countByCriteria(criteria)
                    .zipWith(inventoriesStorageService.findByCriteriaDepreciation(criteria, pageable, false).collectList())
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


    @GetMapping(value = "/fish-meal", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<ApiResponse<InventoriesStorageDTO>>> getAllInventoriesStoragesFishMeal(
            InventoriesStorageCriteria criteria,
            @org.springdoc.core.annotations.ParameterObject Pageable pageable,
            ServerHttpRequest request
    ) {
        log.debug("REST request to get InventoriesStorages by criteria: {}", criteria);

        return SecurityUtils.getUserJWTDetail().flatMap(user -> {
            if (!user.getAuthorities().contains(AuthoritiesConstants.SUPER_ADMIN)) {
                var company = (criteria.getCompany() == null ? new StringFilter() : criteria.getCompany()).getEquals();
                if (company == null) {
                    criteria.company().setEquals(user.getCompanyId());
                }
            } else {
                criteria.company().setEquals(user.getCompanyId());
            }
            return inventoriesStorageService
                    .fakeCountByCriteria(criteria)
                    .zipWith(inventoriesStorageService.findByCriteriaFishMeal(criteria, pageable, false).collectList())
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
     * {@code GET  /inventories-storages/:id} : get the "id" inventoriesStorage.
     *
     * @param id the id of the inventoriesStorageDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the inventoriesStorageDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<InventoriesStorageDTO>> getInventoriesStorage(@PathVariable("id") UUID id) {
        log.debug("REST request to get InventoriesStorage : {}", id);
        Mono<InventoriesStorageDTO> inventoriesStorageDTO = inventoriesStorageService.findOne(id);
        return ResponseUtil.wrapOrNotFound(inventoriesStorageDTO);
    }

    @PostMapping("/items")
    public Mono<ResponseEntity<List<InventoriesStorageDTO>>> getItemInInventoriesStorage(@RequestBody ListDocumentIdRequest listDocumentIdRequest) {
        log.debug("REST request to get InventoriesStorage by item id : {}", listDocumentIdRequest);
        return inventoriesStorageService.getAllItem(listDocumentIdRequest.getDocumentIds(), listDocumentIdRequest.getWarehouseId())
                .map(ResponseEntity.ok()::body);
    }

    @GetMapping("/items/{idWarehouse}/all")
    public Mono<ResponseEntity<ApiResponse<InventoriesStorageTotal>>> getItemInInventoriesStorageAll(
            @PathVariable("idWarehouse") UUID idWarehouse,
            @org.springdoc.core.annotations.ParameterObject Pageable pageable

    ) {
        log.debug("REST request to get InventoriesStorage by item");

        return SecurityUtils.getUserJWTDetail()
                .flatMap(user -> inventoriesStorageService.getAllItems(idWarehouse,user.getCompanyId(),pageable)
                        .map(ResponseEntity::ok));
    }


    @GetMapping("/arises/{id}")
    public Mono<ResponseEntity<Map<String,Object>>> getArisesInInventoriesStorage(
            @PathVariable("id") UUID id
    ) {
        log.debug("REST request to get ArisesDTO for InventoriesStorage by id: {}", id);

        return inventoriesStorageService.getArises(id)
                .flatMap(arisesDTO -> ResponseUtil.wrapOrNotFound(Mono.just(arisesDTO)))
                .switchIfEmpty(Mono.error(new BadRequestAlertException("Entity not found", "InventoriesStorage", "notfound")));
    }


    /**
     * {@code DELETE  /inventories-storages/:id} : delete the "id" inventoriesStorage.
     *
     * @param id the id of the inventoriesStorageDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteInventoriesStorage(@PathVariable("id") UUID id) {
        log.debug("REST request to delete InventoriesStorage : {}", id);
        return inventoriesStorageService
                .delete(id)
                .then(
                        Mono.just(
                                ResponseEntity.noContent()
                                        .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                                        .build()
                        )
                );
    }

    @GetMapping("/export")
    public Mono<ResponseEntity<byte[]>> exportInventories(
            InventoriesStorageCriteria criteria,
            @org.springdoc.core.annotations.ParameterObject Pageable pageable,
            ServerHttpRequest request
    ) {
        return inventoriesStorageService.exportRecordsAsCSV(criteria, pageable)
                .map(csvBytes -> {
                    log.debug("REST request to export Inventories as CSV");

                    return ResponseEntity.ok()
                            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"InventoriesCheck" + ".xlsx\"")
                            .contentType(org.springframework.http.MediaType.APPLICATION_OCTET_STREAM)
                            .body(csvBytes);
                });
    }
}
