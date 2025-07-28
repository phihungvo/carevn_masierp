package com.masi.logistics.web.rest;

import com.masi.logistics.domain.criteria.ItemInfoDetailCriteria;
import com.masi.logistics.repository.ItemInfoDetailRepository;
import com.masi.logistics.service.ItemInfoDetailService;
import com.masi.logistics.service.dto.ItemInfoDetailDTO;
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
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link com.masi.logistics.domain.ItemInfoDetail}.
 */
@RestController
@RequestMapping("/api/item-info-details")
public class ItemInfoDetailResource {

    private static final Logger log = LoggerFactory.getLogger(ItemInfoDetailResource.class);

    private static final String ENTITY_NAME = "masiLogisticsItemInfoDetail";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ItemInfoDetailService itemInfoDetailService;

    private final ItemInfoDetailRepository itemInfoDetailRepository;

    public ItemInfoDetailResource(ItemInfoDetailService itemInfoDetailService, ItemInfoDetailRepository itemInfoDetailRepository) {
        this.itemInfoDetailService = itemInfoDetailService;
        this.itemInfoDetailRepository = itemInfoDetailRepository;
    }

    /**
     * {@code POST  /item-info-details} : Create a new itemInfoDetail.
     *
     * @param itemInfoDetailDTO the itemInfoDetailDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new itemInfoDetailDTO, or with status {@code 400 (Bad Request)} if the itemInfoDetail has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<ItemInfoDetailDTO>> createItemInfoDetail(@RequestBody ItemInfoDetailDTO itemInfoDetailDTO)
        throws URISyntaxException {
        log.debug("REST request to save ItemInfoDetail : {}", itemInfoDetailDTO);
        if (itemInfoDetailDTO.getId() != null) {
            throw new BadRequestAlertException("A new itemInfoDetail cannot already have an ID", ENTITY_NAME, "idexists");
        }
        itemInfoDetailDTO.setId(UUID.randomUUID());
        return itemInfoDetailService
            .save(itemInfoDetailDTO)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/item-info-details/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /item-info-details/:id} : Updates an existing itemInfoDetail.
     *
     * @param id the id of the itemInfoDetailDTO to save.
     * @param itemInfoDetailDTO the itemInfoDetailDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated itemInfoDetailDTO,
     * or with status {@code 400 (Bad Request)} if the itemInfoDetailDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the itemInfoDetailDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<ItemInfoDetailDTO>> updateItemInfoDetail(
        @PathVariable(value = "id", required = false) final UUID id,
        @RequestBody ItemInfoDetailDTO itemInfoDetailDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ItemInfoDetail : {}, {}", id, itemInfoDetailDTO);
        if (itemInfoDetailDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, itemInfoDetailDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return itemInfoDetailRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return itemInfoDetailService
                    .update(itemInfoDetailDTO)
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
     * {@code PATCH  /item-info-details/:id} : Partial updates given fields of an existing itemInfoDetail, field will ignore if it is null
     *
     * @param id the id of the itemInfoDetailDTO to save.
     * @param itemInfoDetailDTO the itemInfoDetailDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated itemInfoDetailDTO,
     * or with status {@code 400 (Bad Request)} if the itemInfoDetailDTO is not valid,
     * or with status {@code 404 (Not Found)} if the itemInfoDetailDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the itemInfoDetailDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<ItemInfoDetailDTO>> partialUpdateItemInfoDetail(
        @PathVariable(value = "id", required = false) final UUID id,
        @RequestBody ItemInfoDetailDTO itemInfoDetailDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ItemInfoDetail partially : {}, {}", id, itemInfoDetailDTO);
        if (itemInfoDetailDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, itemInfoDetailDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return itemInfoDetailRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<ItemInfoDetailDTO> result = itemInfoDetailService.partialUpdate(itemInfoDetailDTO);

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
     * {@code GET  /item-info-details} : get all the itemInfoDetails.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of itemInfoDetails in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<ItemInfoDetailDTO>>> getAllItemInfoDetails(
        ItemInfoDetailCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get ItemInfoDetails by criteria: {}", criteria);
        return itemInfoDetailService
            .countByCriteria(criteria)
            .zipWith(itemInfoDetailService.findByCriteria(criteria, pageable).collectList())
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
     * {@code GET  /item-info-details/count} : count all the itemInfoDetails.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public Mono<ResponseEntity<Long>> countItemInfoDetails(ItemInfoDetailCriteria criteria) {
        log.debug("REST request to count ItemInfoDetails by criteria: {}", criteria);
        return itemInfoDetailService.countByCriteria(criteria).map(count -> ResponseEntity.status(HttpStatus.OK).body(count));
    }

    /**
     * {@code GET  /item-info-details/:id} : get the "id" itemInfoDetail.
     *
     * @param id the id of the itemInfoDetailDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the itemInfoDetailDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<ItemInfoDetailDTO>> getItemInfoDetail(@PathVariable("id") UUID id) {
        log.debug("REST request to get ItemInfoDetail : {}", id);
        Mono<ItemInfoDetailDTO> itemInfoDetailDTO = itemInfoDetailService.findOne(id);
        return ResponseUtil.wrapOrNotFound(itemInfoDetailDTO);
    }

    /**
     * {@code DELETE  /item-info-details/:id} : delete the "id" itemInfoDetail.
     *
     * @param id the id of the itemInfoDetailDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteItemInfoDetail(@PathVariable("id") UUID id) {
        log.debug("REST request to delete ItemInfoDetail : {}", id);
        return itemInfoDetailService
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
