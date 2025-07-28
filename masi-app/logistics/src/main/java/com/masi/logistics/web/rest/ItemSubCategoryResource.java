package com.masi.logistics.web.rest;

import com.carevn.masi.dto.ApiResponse;
import com.carevn.masi.utils.SecurityUtils;
import com.masi.logistics.domain.criteria.ItemSubCategoryCriteria;
import com.masi.logistics.repository.ItemSubCategoryRepository;
import com.masi.logistics.security.AuthoritiesConstants;
import com.masi.logistics.service.ItemSubCategoryService;
import com.masi.logistics.service.dto.ItemSubCategoryDTO;
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
 * REST controller for managing {@link com.masi.logistics.domain.ItemSubCategory}.
 */
@RestController
@RequestMapping("/api/item-sub-categories")
public class ItemSubCategoryResource {

    private static final Logger log = LoggerFactory.getLogger(ItemSubCategoryResource.class);

    private static final String ENTITY_NAME = "masiLogisticsItemSubCategory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ItemSubCategoryService itemSubCategoryService;

    private final ItemSubCategoryRepository itemSubCategoryRepository;

    public ItemSubCategoryResource(ItemSubCategoryService itemSubCategoryService, ItemSubCategoryRepository itemSubCategoryRepository) {
        this.itemSubCategoryService = itemSubCategoryService;
        this.itemSubCategoryRepository = itemSubCategoryRepository;
    }

    /**
     * {@code POST  /item-sub-categories} : Create a new itemSubCategory.
     *
     * @param itemSubCategoryDTO the itemSubCategoryDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new itemSubCategoryDTO, or with status {@code 400 (Bad Request)} if the itemSubCategory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<ItemSubCategoryDTO>> createItemSubCategory(@Valid @RequestBody ItemSubCategoryDTO itemSubCategoryDTO)
        throws URISyntaxException {
        log.debug("REST request to save ItemSubCategory : {}", itemSubCategoryDTO);
        if (itemSubCategoryDTO.getId() != null) {
            throw new BadRequestAlertException("A new itemSubCategory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        itemSubCategoryDTO.setId(UUID.randomUUID());
        return itemSubCategoryService
            .save(itemSubCategoryDTO)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/item-sub-categories/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PATCH  /item-sub-categories/:id} : Partial updates given fields of an existing itemSubCategory, field will ignore if it is null
     *
     * @param id the id of the itemSubCategoryDTO to save.
     * @param itemSubCategoryDTO the itemSubCategoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated itemSubCategoryDTO,
     * or with status {@code 400 (Bad Request)} if the itemSubCategoryDTO is not valid,
     * or with status {@code 404 (Not Found)} if the itemSubCategoryDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the itemSubCategoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<ItemSubCategoryDTO>> partialUpdateItemSubCategory(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody ItemSubCategoryDTO itemSubCategoryDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ItemSubCategory partially : {}, {}", id, itemSubCategoryDTO);
        if (itemSubCategoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, itemSubCategoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return itemSubCategoryRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<ItemSubCategoryDTO> result = itemSubCategoryService.partialUpdate(itemSubCategoryDTO);

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
     * {@code GET  /item-sub-categories} : get all the itemSubCategories.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of itemSubCategories in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<ApiResponse<ItemSubCategoryDTO>>> getAllItemSubCategories(
        ItemSubCategoryCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get ItemSubCategories by criteria: {}", criteria);
        return SecurityUtils.getUserJWTDetail().flatMap(user -> {
            if (!user.getAuthorities().contains(AuthoritiesConstants.SUPER_ADMIN)) {
                var company = (criteria.getCompany() == null ? new StringFilter() : criteria.getCompany()).getEquals();
                if (company == null) {
                    criteria.company().setEquals(user.getCompanyId());
                }
            } else {
                criteria.company().setEquals(user.getCompanyId());
            }
            return itemSubCategoryService
                    .countByCriteria(criteria)
                    .zipWith(itemSubCategoryService.findByCriteria(criteria, pageable).collectList())
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
     * {@code GET  /item-sub-categories/count} : count all the itemSubCategories.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public Mono<ResponseEntity<Long>> countItemSubCategories(ItemSubCategoryCriteria criteria) {
        log.debug("REST request to count ItemSubCategories by criteria: {}", criteria);
        return itemSubCategoryService.countByCriteria(criteria).map(count -> ResponseEntity.status(HttpStatus.OK).body(count));
    }

    /**
     * {@code GET  /item-sub-categories/:id} : get the "id" itemSubCategory.
     *
     * @param id the id of the itemSubCategoryDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the itemSubCategoryDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<ItemSubCategoryDTO>> getItemSubCategory(@PathVariable("id") UUID id) {
        log.debug("REST request to get ItemSubCategory : {}", id);
        Mono<ItemSubCategoryDTO> itemSubCategoryDTO = itemSubCategoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(itemSubCategoryDTO);
    }

    /**
     * {@code DELETE  /item-sub-categories/:id} : delete the "id" itemSubCategory.
     *
     * @param id the id of the itemSubCategoryDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteItemSubCategory(@PathVariable("id") UUID id) {
        log.debug("REST request to delete ItemSubCategory : {}", id);
        return itemSubCategoryService
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
