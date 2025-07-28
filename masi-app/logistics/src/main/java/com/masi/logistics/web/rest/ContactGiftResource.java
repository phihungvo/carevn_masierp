package com.masi.logistics.web.rest;

import com.carevn.masi.dto.ApiResponse;
import com.masi.logistics.domain.criteria.ContactGiftCriteria;
import com.masi.logistics.repository.ContactGiftRepository;
import com.masi.logistics.service.ContactGiftService;
import com.masi.logistics.service.dto.ContactGiftDTO;
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
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link com.masi.logistics.domain.ContactGift}.
 */
@RestController
@RequestMapping("/api/contact-gifts")
public class ContactGiftResource {

    private static final Logger log = LoggerFactory.getLogger(ContactGiftResource.class);

    private static final String ENTITY_NAME = "masiLogisticsContactGift";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ContactGiftService contactGiftService;

    private final ContactGiftRepository contactGiftRepository;

    public ContactGiftResource(ContactGiftService contactGiftService, ContactGiftRepository contactGiftRepository) {
        this.contactGiftService = contactGiftService;
        this.contactGiftRepository = contactGiftRepository;
    }

    /**
     * {@code POST  /contact-gifts} : Create a new contactGift.
     *
     * @param contactGiftDTO the contactGiftDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new contactGiftDTO, or with status {@code 400 (Bad Request)} if the contactGift has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<ContactGiftDTO>> createContactGift(@Valid @RequestBody ContactGiftDTO contactGiftDTO)
        throws URISyntaxException {
        log.debug("REST request to save ContactGift : {}", contactGiftDTO);
        if (contactGiftDTO.getId() != null) {
            throw new BadRequestAlertException("A new contactGift cannot already have an ID", ENTITY_NAME, "idexists");
        }
        contactGiftDTO.setId(UUID.randomUUID());
        return contactGiftService
            .save(contactGiftDTO)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/contact-gifts/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /contact-gifts/:id} : Updates an existing contactGift.
     *
     * @param id the id of the contactGiftDTO to save.
     * @param contactGiftDTO the contactGiftDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated contactGiftDTO,
     * or with status {@code 400 (Bad Request)} if the contactGiftDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the contactGiftDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<ContactGiftDTO>> updateContactGift(
        @PathVariable(value = "id", required = false) final UUID id,
        @Valid @RequestBody ContactGiftDTO contactGiftDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ContactGift : {}, {}", id, contactGiftDTO);
        if (contactGiftDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, contactGiftDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return contactGiftRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return contactGiftService
                    .update(contactGiftDTO)
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
     * {@code PATCH  /contact-gifts/:id} : Partial updates given fields of an existing contactGift, field will ignore if it is null
     *
     * @param id the id of the contactGiftDTO to save.
     * @param contactGiftDTO the contactGiftDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated contactGiftDTO,
     * or with status {@code 400 (Bad Request)} if the contactGiftDTO is not valid,
     * or with status {@code 404 (Not Found)} if the contactGiftDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the contactGiftDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<ContactGiftDTO>> partialUpdateContactGift(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody ContactGiftDTO contactGiftDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ContactGift partially : {}, {}", id, contactGiftDTO);
        contactGiftDTO.setId(id);
        return contactGiftRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<ContactGiftDTO> result = contactGiftService.partialUpdate(contactGiftDTO);

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
     * {@code GET  /contact-gifts} : get all the contactGifts.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of contactGifts in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<ApiResponse<ContactGiftDTO>>> getAllContactGifts(
        ContactGiftCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get ContactGifts by criteria: {}", criteria);
        return contactGiftService
            .countByCriteria(criteria)
            .zipWith(contactGiftService.findByCriteria(criteria, pageable).collectList())
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
    }

    /**
     * {@code GET  /contact-gifts/count} : count all the contactGifts.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public Mono<ResponseEntity<Long>> countContactGifts(ContactGiftCriteria criteria) {
        log.debug("REST request to count ContactGifts by criteria: {}", criteria);
        return contactGiftService.countByCriteria(criteria).map(count -> ResponseEntity.status(HttpStatus.OK).body(count));
    }

    /**
     * {@code GET  /contact-gifts/:id} : get the "id" contactGift.
     *
     * @param id the id of the contactGiftDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the contactGiftDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<ContactGiftDTO>> getContactGift(@PathVariable("id") UUID id) {
        log.debug("REST request to get ContactGift : {}", id);
        Mono<ContactGiftDTO> contactGiftDTO = contactGiftService.findOne(id);
        return ResponseUtil.wrapOrNotFound(contactGiftDTO);
    }

    /**
     * {@code DELETE  /contact-gifts/:id} : delete the "id" contactGift.
     *
     * @param id the id of the contactGiftDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteContactGift(@PathVariable("id") UUID id) {
        log.debug("REST request to delete ContactGift : {}", id);
        return contactGiftService
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
