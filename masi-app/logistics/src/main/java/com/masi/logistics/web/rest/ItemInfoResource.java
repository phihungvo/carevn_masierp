package com.masi.logistics.web.rest;

import com.masi.logistics.domain.criteria.ItemInfoCriteria;
import com.masi.logistics.repository.ItemInfoRepository;
import com.masi.logistics.service.ItemInfoService;
import com.masi.logistics.service.dto.ItemInfoDTO;
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
 * REST controller for managing {@link com.masi.logistics.domain.ItemInfo}.
 */
@RestController
@RequestMapping("/api/item-infos")
public class ItemInfoResource {

    private static final Logger log = LoggerFactory.getLogger(ItemInfoResource.class);

    private static final String ENTITY_NAME = "masiLogisticsItemInfo";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ItemInfoService itemInfoService;

    private final ItemInfoRepository itemInfoRepository;

    public ItemInfoResource(ItemInfoService itemInfoService, ItemInfoRepository itemInfoRepository) {
        this.itemInfoService = itemInfoService;
        this.itemInfoRepository = itemInfoRepository;
    }

    /**
     * {@code POST  /item-infos} : Create a new itemInfo.
     *
     * @param itemInfoDTO the itemInfoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new itemInfoDTO, or with status {@code 400 (Bad Request)} if the itemInfo has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<ItemInfoDTO>> createItemInfo(@RequestBody ItemInfoDTO itemInfoDTO) throws URISyntaxException {
        log.debug("REST request to save ItemInfo : {}", itemInfoDTO);
        if (itemInfoDTO.getId() != null) {
            throw new BadRequestAlertException("A new itemInfo cannot already have an ID", ENTITY_NAME, "idexists");
        }
        itemInfoDTO.setId(UUID.randomUUID());
        return itemInfoService
            .save(itemInfoDTO)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/item-infos/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /item-infos/:id} : Updates an existing itemInfo.
     *
     * @param id the id of the itemInfoDTO to save.
     * @param itemInfoDTO the itemInfoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated itemInfoDTO,
     * or with status {@code 400 (Bad Request)} if the itemInfoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the itemInfoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<ItemInfoDTO>> updateItemInfo(
        @PathVariable(value = "id", required = false) final UUID id,
        @RequestBody ItemInfoDTO itemInfoDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ItemInfo : {}, {}", id, itemInfoDTO);
        if (itemInfoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, itemInfoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return itemInfoRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return itemInfoService
                    .update(itemInfoDTO)
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
     * {@code PATCH  /item-infos/:id} : Partial updates given fields of an existing itemInfo, field will ignore if it is null
     *
     * @param id the id of the itemInfoDTO to save.
     * @param itemInfoDTO the itemInfoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated itemInfoDTO,
     * or with status {@code 400 (Bad Request)} if the itemInfoDTO is not valid,
     * or with status {@code 404 (Not Found)} if the itemInfoDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the itemInfoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<ItemInfoDTO>> partialUpdateItemInfo(
        @PathVariable(value = "id", required = false) final UUID id,
        @RequestBody ItemInfoDTO itemInfoDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ItemInfo partially : {}, {}", id, itemInfoDTO);
        if (itemInfoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, itemInfoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return itemInfoRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<ItemInfoDTO> result = itemInfoService.partialUpdate(itemInfoDTO);

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
     * {@code GET  /item-infos} : get all the itemInfos.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of itemInfos in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<ItemInfoDTO>>> getAllItemInfos(
        ItemInfoCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get ItemInfos by criteria: {}", criteria);
        return itemInfoService
            .countByCriteria(criteria)
            .zipWith(itemInfoService.findByCriteria(criteria, pageable).collectList())
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
     * {@code GET  /item-infos/count} : count all the itemInfos.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public Mono<ResponseEntity<Long>> countItemInfos(ItemInfoCriteria criteria) {
        log.debug("REST request to count ItemInfos by criteria: {}", criteria);
        return itemInfoService.countByCriteria(criteria).map(count -> ResponseEntity.status(HttpStatus.OK).body(count));
    }

    /**
     * {@code GET  /item-infos/:id} : get the "id" itemInfo.
     *
     * @param id the id of the itemInfoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the itemInfoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<ItemInfoDTO>> getItemInfo(@PathVariable("id") UUID id) {
        log.debug("REST request to get ItemInfo : {}", id);
        Mono<ItemInfoDTO> itemInfoDTO = itemInfoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(itemInfoDTO);
    }

    /**
     * {@code DELETE  /item-infos/:id} : delete the "id" itemInfo.
     *
     * @param id the id of the itemInfoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteItemInfo(@PathVariable("id") UUID id) {
        log.debug("REST request to delete ItemInfo : {}", id);
        return itemInfoService
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
