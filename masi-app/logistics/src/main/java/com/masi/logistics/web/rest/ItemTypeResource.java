package com.masi.logistics.web.rest;

import com.carevn.masi.dto.ApiResponse;
import com.carevn.masi.utils.SecurityUtils;
import com.masi.logistics.domain.criteria.ItemTypeCriteria;
import com.masi.logistics.repository.ItemTypeRepository;
import com.masi.logistics.security.AuthoritiesConstants;
import com.masi.logistics.service.ItemTypeService;
import com.masi.logistics.service.dto.ItemTypeDTO;
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
 * REST controller for managing {@link com.masi.logistics.domain.ItemType}.
 */
@RestController
@RequestMapping("/api/item-types")
public class ItemTypeResource {

    private static final Logger log = LoggerFactory.getLogger(ItemTypeResource.class);

    private static final String ENTITY_NAME = "masiLogisticsItemType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ItemTypeService itemTypeService;

    private final ItemTypeRepository itemTypeRepository;

    public ItemTypeResource(ItemTypeService itemTypeService, ItemTypeRepository itemTypeRepository) {
        this.itemTypeService = itemTypeService;
        this.itemTypeRepository = itemTypeRepository;
    }

    /**
     * {@code POST  /item-types} : Create a new itemType.
     *
     * @param itemTypeDTO the itemTypeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new itemTypeDTO, or with status {@code 400 (Bad Request)} if the itemType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<ItemTypeDTO>> createItemType(@RequestBody ItemTypeDTO itemTypeDTO) throws URISyntaxException {
        log.debug("REST request to save ItemType : {}", itemTypeDTO);

        itemTypeDTO.setId(UUID.randomUUID());
        return itemTypeService
            .save(itemTypeDTO)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/item-types/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PATCH  /item-types/:id} : Partial updates given fields of an existing itemType, field will ignore if it is null
     *
     * @param id the id of the itemTypeDTO to save.
     * @param itemTypeDTO the itemTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated itemTypeDTO,
     * or with status {@code 400 (Bad Request)} if the itemTypeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the itemTypeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the itemTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<ItemTypeDTO>> partialUpdateItemType(
        @PathVariable(value = "id", required = false) final UUID id,
        @RequestBody ItemTypeDTO itemTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ItemType partially : {}, {}", id, itemTypeDTO);
        if (itemTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, itemTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return itemTypeRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<ItemTypeDTO> result = itemTypeService.partialUpdate(itemTypeDTO);

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
     * {@code GET  /item-types} : get all the itemTypes.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of itemTypes in body.
     */

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<ApiResponse<ItemTypeDTO>>> getAllItemTypes(
            ItemTypeCriteria criteria,
            @org.springdoc.core.annotations.ParameterObject Pageable pageable,
            ServerHttpRequest request
    ) {
        log.debug("REST request to get ItemTypes by criteria: {}", criteria);
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
            return itemTypeService
                    .countByCriteria(criteria)
                    .zipWith(itemTypeService.findByCriteria(criteria, pageable).collectList())
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
     * {@code GET  /item-types/count} : count all the itemTypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public Mono<ResponseEntity<Long>> countItemTypes(ItemTypeCriteria criteria) {
        log.debug("REST request to count ItemTypes by criteria: {}", criteria);
        return itemTypeService.countByCriteria(criteria).map(count -> ResponseEntity.status(HttpStatus.OK).body(count));
    }

    /**
     * {@code GET  /item-types/:id} : get the "id" itemType.
     *
     * @param id the id of the itemTypeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the itemTypeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<ItemTypeDTO>> getItemType(@PathVariable("id") UUID id) {
        log.debug("REST request to get ItemType : {}", id);
        Mono<ItemTypeDTO> itemTypeDTO = itemTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(itemTypeDTO);
    }

    /**
     * {@code DELETE  /item-types/:id} : delete the "id" itemType.
     *
     * @param id the id of the itemTypeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteItemType(@PathVariable("id") UUID id) {
        log.debug("REST request to delete ItemType : {}", id);
        return itemTypeService
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
