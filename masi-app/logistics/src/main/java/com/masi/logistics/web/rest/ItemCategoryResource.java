package com.masi.logistics.web.rest;

import com.carevn.masi.dto.ApiResponse;
import com.carevn.masi.utils.SecurityUtils;
import com.masi.logistics.domain.criteria.ItemCategoryCriteria;
import com.masi.logistics.domain.criteria.VatRateCriteria;
import com.masi.logistics.repository.ItemCategoryRepository;
import com.masi.logistics.security.AuthoritiesConstants;
import com.masi.logistics.service.ItemCategoryService;
import com.masi.logistics.service.dto.ItemCategoryDTO;
import com.masi.logistics.service.dto.VatRateDTO;
import com.masi.logistics.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link com.masi.logistics.domain.ItemCategory}.
 */
@RestController
@RequestMapping("/api/item-categories")
public class ItemCategoryResource {

    private static final Logger log = LoggerFactory.getLogger(ItemCategoryResource.class);

    private static final String ENTITY_NAME = "masiLogisticsItemCategory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ItemCategoryService itemCategoryService;

    private final ItemCategoryRepository itemCategoryRepository;

    public ItemCategoryResource(ItemCategoryService itemCategoryService, ItemCategoryRepository itemCategoryRepository) {
        this.itemCategoryService = itemCategoryService;
        this.itemCategoryRepository = itemCategoryRepository;
    }

    /**
     * {@code POST  /item-categories} : Create a new itemCategory.
     *
     * @param itemCategoryDTO the itemCategoryDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new itemCategoryDTO, or with status {@code 400 (Bad Request)} if the itemCategory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<ItemCategoryDTO>> createItemCategory(@RequestBody ItemCategoryDTO itemCategoryDTO)
        throws URISyntaxException {
        log.debug("REST request to save ItemCategory : {}", itemCategoryDTO);
        return itemCategoryService
            .save(itemCategoryDTO)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/item-categories/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /item-categories/:id} : Updates an existing itemCategory.
     *
     * @param id the id of the itemCategoryDTO to save.
     * @param itemCategoryDTO the itemCategoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated itemCategoryDTO,
     * or with status {@code 400 (Bad Request)} if the itemCategoryDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the itemCategoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<ItemCategoryDTO>> updateItemCategory(
        @PathVariable(value = "id", required = false) final UUID id,
        @RequestBody ItemCategoryDTO itemCategoryDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ItemCategory : {}, {}", id, itemCategoryDTO);
        if (itemCategoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, itemCategoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return itemCategoryRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return itemCategoryService
                    .update(itemCategoryDTO)
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
     * {@code PATCH  /item-categories/:id} : Partial updates given fields of an existing itemCategory, field will ignore if it is null
     *
     * @param id the id of the itemCategoryDTO to save.
     * @param itemCategoryDTO the itemCategoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated itemCategoryDTO,
     * or with status {@code 400 (Bad Request)} if the itemCategoryDTO is not valid,
     * or with status {@code 404 (Not Found)} if the itemCategoryDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the itemCategoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<ItemCategoryDTO>> partialUpdateItemCategory(
        @PathVariable(value = "id", required = false) final UUID id,
        @RequestBody ItemCategoryDTO itemCategoryDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ItemCategory partially : {}, {}", id, itemCategoryDTO);
        itemCategoryDTO.setId(id);
        return itemCategoryRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }
                Mono<ItemCategoryDTO> result = itemCategoryService.partialUpdate(itemCategoryDTO);
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
     * {@code GET  /item-categories} : get all the itemCategories.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of itemCategories in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<ApiResponse<ItemCategoryDTO>>> getAllItemCategories(
            ItemCategoryCriteria criteria,
            @org.springdoc.core.annotations.ParameterObject Pageable pageable,
            ServerHttpRequest request
    ) {
        log.debug("REST request to get ItemCategories by criteria: {}", criteria);
        return SecurityUtils.getUserJWTDetail().flatMap(user -> {
            if (!user.getAuthorities().contains(AuthoritiesConstants.SUPER_ADMIN)) {
                var company = (criteria.getCompany() == null ? new StringFilter() : criteria.getCompany()).getEquals();
                if (company == null) {
                    criteria.company().setEquals(user.getCompanyId());
                }
            } else {
                criteria.company().setEquals(user.getCompanyId());
            }
            return itemCategoryService
                    .countByCriteria(criteria)
                    .zipWith(itemCategoryService.findByCriteria(criteria, pageable).collectList())
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
     * {@code GET  /item-categories/count} : count all the itemCategories.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public Mono<ResponseEntity<Long>> countItemCategories(ItemCategoryCriteria criteria) {
        log.debug("REST request to count ItemCategories by criteria: {}", criteria);
        return itemCategoryService.countByCriteria(criteria).map(count -> ResponseEntity.status(HttpStatus.OK).body(count));
    }

    /**
     * {@code GET  /item-categories/:id} : get the "id" itemCategory.
     *
     * @param id the id of the itemCategoryDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the itemCategoryDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<ItemCategoryDTO>> getItemCategory(@PathVariable("id") UUID id) {
        log.debug("REST request to get ItemCategory : {}", id);
        Mono<ItemCategoryDTO> itemCategoryDTO = itemCategoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(itemCategoryDTO);
    }

    /**
     * {@code DELETE  /item-categories/:id} : delete the "id" itemCategory.
     *
     * @param id the id of the itemCategoryDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteItemCategory(@PathVariable("id") UUID id) {
        log.debug("REST request to delete ItemCategory : {}", id);
        return itemCategoryService
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
