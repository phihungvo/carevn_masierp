package com.masi.logistics.web.rest;

import com.carevn.masi.dto.ApiResponse;
import com.masi.logistics.domain.criteria.ContactTypeCriteria;
import com.masi.logistics.repository.ContactTypeRepository;
import com.masi.logistics.service.ContactTypeService;
import com.masi.logistics.service.dto.ContactTypeDTO;
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
 * REST controller for managing {@link com.masi.logistics.domain.ContactType}.
 */
@RestController
@RequestMapping("/api/contact-types")
public class ContactTypeResource {

    private static final Logger log = LoggerFactory.getLogger(ContactTypeResource.class);

    private static final String ENTITY_NAME = "masiLogisticsContactType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ContactTypeService contactTypeService;

    private final ContactTypeRepository contactTypeRepository;

    public ContactTypeResource(ContactTypeService contactTypeService, ContactTypeRepository contactTypeRepository) {
        this.contactTypeService = contactTypeService;
        this.contactTypeRepository = contactTypeRepository;
    }

    /**
     * {@code POST  /contact-types} : Create a new contactType.
     *
     * @param contactTypeDTO the contactTypeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new contactTypeDTO, or with status {@code 400 (Bad Request)} if the contactType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<ContactTypeDTO>> createContactType(@Valid @RequestBody ContactTypeDTO contactTypeDTO)
        throws URISyntaxException {
        log.debug("REST request to save ContactType : {}", contactTypeDTO);
        if (contactTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new contactType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        contactTypeDTO.setId(UUID.randomUUID());
        return contactTypeService
            .save(contactTypeDTO)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/contact-types/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /contact-types/:id} : Updates an existing contactType.
     *
     * @param id the id of the contactTypeDTO to save.
     * @param contactTypeDTO the contactTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated contactTypeDTO,
     * or with status {@code 400 (Bad Request)} if the contactTypeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the contactTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<ContactTypeDTO>> updateContactType(
        @PathVariable(value = "id", required = false) final UUID id,
        @Valid @RequestBody ContactTypeDTO contactTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ContactType : {}, {}", id, contactTypeDTO);
        if (contactTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, contactTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return contactTypeRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return contactTypeService
                    .update(contactTypeDTO)
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
     * {@code PATCH  /contact-types/:id} : Partial updates given fields of an existing contactType, field will ignore if it is null
     *
     * @param id the id of the contactTypeDTO to save.
     * @param contactTypeDTO the contactTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated contactTypeDTO,
     * or with status {@code 400 (Bad Request)} if the contactTypeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the contactTypeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the contactTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<ContactTypeDTO>> partialUpdateContactType(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody ContactTypeDTO contactTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ContactType partially : {}, {}", id, contactTypeDTO);
        contactTypeDTO.setId(id);

        return contactTypeRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<ContactTypeDTO> result = contactTypeService.partialUpdate(contactTypeDTO);

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
     * {@code GET  /contact-types} : get all the contactTypes.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of contactTypes in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<ApiResponse<ContactTypeDTO>>> getAllContactTypes(
        ContactTypeCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get ContactTypes by criteria: {}", criteria);
        return contactTypeService
            .countByCriteria(criteria)
            .zipWith(contactTypeService.findByCriteria(criteria, pageable).collectList())
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
     * {@code GET  /contact-types/count} : count all the contactTypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public Mono<ResponseEntity<Long>> countContactTypes(ContactTypeCriteria criteria) {
        log.debug("REST request to count ContactTypes by criteria: {}", criteria);
        return contactTypeService.countByCriteria(criteria).map(count -> ResponseEntity.status(HttpStatus.OK).body(count));
    }

    /**
     * {@code GET  /contact-types/:id} : get the "id" contactType.
     *
     * @param id the id of the contactTypeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the contactTypeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<ContactTypeDTO>> getContactType(@PathVariable("id") UUID id) {
        log.debug("REST request to get ContactType : {}", id);
        Mono<ContactTypeDTO> contactTypeDTO = contactTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(contactTypeDTO);
    }

    /**
     * {@code DELETE  /contact-types/:id} : delete the "id" contactType.
     *
     * @param id the id of the contactTypeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteContactType(@PathVariable("id") UUID id) {
        log.debug("REST request to delete ContactType : {}", id);
        return contactTypeService
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
