package com.masi.logistics.web.rest;

import com.carevn.masi.dto.ApiResponse;
import com.carevn.masi.utils.SecurityUtils;
import com.masi.logistics.domain.criteria.InventoriesCriteria;
import com.masi.logistics.domain.criteria.ItemCriteria;
import com.masi.logistics.repository.ItemRepository;
import com.masi.logistics.security.AuthoritiesConstants;
import com.masi.logistics.service.ItemService;
import com.masi.logistics.service.dto.FactoriesDTO;
import com.masi.logistics.service.dto.ItemDTO;
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
import org.springframework.http.HttpHeaders;
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
 * REST controller for managing {@link com.masi.logistics.domain.Item}.
 */
@RestController
@RequestMapping("/api/items")
public class ItemResource {

    private static final Logger log = LoggerFactory.getLogger(ItemResource.class);

    private static final String ENTITY_NAME = "masiLogisticsItem";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ItemService itemService;

    private final ItemRepository itemRepository;

    public ItemResource(ItemService itemService, ItemRepository itemRepository) {
        this.itemService = itemService;
        this.itemRepository = itemRepository;
    }

    /**
     * {@code POST  /items} : Create a new item.
     *
     * @param itemDTO the itemDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new itemDTO, or with status {@code 400 (Bad Request)} if the item has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<ItemDTO>> createItem(@Valid @RequestBody ItemDTO itemDTO) throws URISyntaxException {
        log.debug("REST request to save Item : {}", itemDTO);
        return itemService
            .save(itemDTO)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/items/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /items/:id} : Updates an existing item.
     *
     * @param id the id of the itemDTO to save.
     * @param itemDTO the itemDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated itemDTO,
     * or with status {@code 400 (Bad Request)} if the itemDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the itemDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<ItemDTO>> updateItem(
        @PathVariable(value = "id", required = false) final UUID id,
        @Valid @RequestBody ItemDTO itemDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Item : {}, {}", id, itemDTO);
        if (itemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, itemDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return itemRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return itemService
                    .update(itemDTO)
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
     * {@code PATCH  /items/:id} : Partial updates given fields of an existing item, field will ignore if it is null
     *
     * @param id the id of the itemDTO to save.
     * @param itemDTO the itemDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated itemDTO,
     * or with status {@code 400 (Bad Request)} if the itemDTO is not valid,
     * or with status {@code 404 (Not Found)} if the itemDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the itemDTO couldn't be updated.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<ItemDTO>> partialUpdateItem(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody ItemDTO itemDTO
    ) {
        log.debug("REST request to partial update Item partially : {}, {}", id, itemDTO);
        itemDTO.setId(id);
        return itemRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<ItemDTO> result = itemService.partialUpdate(itemDTO);

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
     * {@code GET  /items} : get all the items.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of items in body.
     */




    /**
     * {@code GET  /items/count} : count all the items.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public Mono<ResponseEntity<Long>> countItems(ItemCriteria criteria) {
        log.debug("REST request to count Items by criteria: {}", criteria);
        return itemService.countByCriteria(criteria).map(count -> ResponseEntity.status(HttpStatus.OK).body(count));
    }

    /**
     * {@code GET  /items/:id} : get the "id" item.
     *
     * @param id the id of the itemDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the itemDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<ItemDTO>> getItem(@PathVariable("id") UUID id) {
        log.debug("REST request to get Item : {}", id);
        return SecurityUtils.getUserJWTDetail().flatMap(user ->{
            Mono<ItemDTO> itemDTO = itemService.findOne(id, user.getCompanyId());
            return ResponseUtil.wrapOrNotFound(itemDTO);
        });
    }

    /**
     * {@code DELETE  /items/:id} : delete the "id" item.
     *
     * @param id the id of the itemDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteItem(@PathVariable("id") UUID id) {
        log.debug("REST request to delete Item : {}", id);
        return SecurityUtils.getUserJWTDetail().flatMap(user -> {
            return itemService
                .delete(id, user.getUserId().toString(), user.getCompanyId())
                .then(
                    Mono.just(
                        ResponseEntity.noContent()
                            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                            .build()
                    )
                );
        });
    }

    @GetMapping(value = "/percent-protein", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<ApiResponse<ItemDTO>>> getAllItemsByPerProtein(
            ItemCriteria criteria,
            @org.springdoc.core.annotations.ParameterObject Pageable pageable,
            ServerHttpRequest request
    ) {
        log.debug("REST request to get Items by criteria: {}", criteria);
        criteria.setIsFindPerProtein(true);
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
            return itemService
                    .countByCriteria(criteria)
                    .zipWith(itemService.findByCriteria(criteria, pageable).collectList())
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

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<ApiResponse<ItemDTO>>> getAllItems(
            ItemCriteria criteria,
            @org.springdoc.core.annotations.ParameterObject Pageable pageable,
            ServerHttpRequest request
    ) {
        log.debug("REST request to get Items by criteria: {}", criteria);
        var name =  (criteria.getName() == null ? new StringFilter(): criteria.getName()).getContains() == null ? "" : criteria.getName().getContains();
        name = "%" + name + "%";
        criteria.name().setContains(name);
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
            return itemService
                    .countByCriteria(criteria)
                    .zipWith(itemService.findByCriteria(criteria, pageable).collectList())
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

    @PatchMapping(value = "/{id}/active")
    public Mono<ResponseEntity<ItemDTO>> activeFactories(
            @PathVariable(value = "id", required = false) final UUID id) throws URISyntaxException {
        return itemRepository
                .existsById(id)
                .flatMap(exists -> {
                    if (!exists) {
                        return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                    }

                    Mono<ItemDTO> result = itemService.changeIsActive(id, true);

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
    public Mono<ResponseEntity<ItemDTO>> disableFactories(
            @PathVariable(value = "id", required = false) final UUID id) throws URISyntaxException {
        return itemRepository
                .existsById(id)
                .flatMap(exists -> {
                    if (!exists) {
                        return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                    }

                    Mono<ItemDTO> result = itemService.changeIsActive(id, false);

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

    @GetMapping("/export")
    public Mono<ResponseEntity<byte[]>> exportInventories(
            ItemCriteria criteria,
            @org.springdoc.core.annotations.ParameterObject Pageable pageable,
            ServerHttpRequest request
    ) {
        return itemService.exportRecordsAsCSV(criteria, pageable)
                .map(csvBytes -> {
                    log.debug("REST request to export Inventories as CSV");

                    return ResponseEntity.ok()
                            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"Item" + ".csv\"")
                            .contentType(org.springframework.http.MediaType.APPLICATION_OCTET_STREAM)
                            .body(csvBytes);
                });
    }
}
