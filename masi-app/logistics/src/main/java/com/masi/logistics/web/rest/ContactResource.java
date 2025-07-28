package com.masi.logistics.web.rest;

import com.carevn.masi.dto.ApiResponse;
import com.masi.logistics.domain.criteria.ContactCriteria;
import com.masi.logistics.repository.ContactRepository;
import com.masi.logistics.service.ContactService;
import com.masi.logistics.service.dto.ContactDTO;
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
 * REST controller for managing {@link com.masi.logistics.domain.Contact}.
 */
@RestController
@RequestMapping("/api/contacts")
public class ContactResource {

    private static final Logger log = LoggerFactory.getLogger(ContactResource.class);

    private static final String ENTITY_NAME = "masiLogisticsContact";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ContactService contactService;

    private final ContactRepository contactRepository;

    public ContactResource(ContactService contactService, ContactRepository contactRepository) {
        this.contactService = contactService;
        this.contactRepository = contactRepository;
    }

    /**
     * {@code POST  /contacts} : Create a new contact.
     *
     * @param contactDTO the contactDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new contactDTO, or with status {@code 400 (Bad Request)} if the contact has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<ContactDTO>> createContact(@Valid @RequestBody ContactDTO contactDTO) throws URISyntaxException {
        log.debug("REST request to save Contact : {}", contactDTO);
        if (contactDTO.getId() != null) {
            throw new BadRequestAlertException("A new contact cannot already have an ID", ENTITY_NAME, "idexists");
        }
        contactDTO.setId(UUID.randomUUID());
        return contactService
            .save(contactDTO)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/contacts/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /contacts/:id} : Updates an existing contact.
     *
     * @param id the id of the contactDTO to save.
     * @param contactDTO the contactDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated contactDTO,
     * or with status {@code 400 (Bad Request)} if the contactDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the contactDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<ContactDTO>> updateContact(
        @PathVariable(value = "id", required = false) final UUID id,
        @Valid @RequestBody ContactDTO contactDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Contact : {}, {}", id, contactDTO);
        if (contactDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, contactDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return contactRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return contactService
                    .update(contactDTO)
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
     * {@code PATCH  /contacts/:id} : Partial updates given fields of an existing contact, field will ignore if it is null
     *
     * @param id the id of the contactDTO to save.
     * @param contactDTO the contactDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated contactDTO,
     * or with status {@code 400 (Bad Request)} if the contactDTO is not valid,
     * or with status {@code 404 (Not Found)} if the contactDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the contactDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<ContactDTO>> partialUpdateContact(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody ContactDTO contactDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Contact partially : {}, {}", id, contactDTO);
        contactDTO.setId(id);

        return contactRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<ContactDTO> result = contactService.partialUpdate(contactDTO);

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
     * {@code GET  /contacts} : get all the contacts.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of contacts in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<ApiResponse<ContactDTO>>> getAllContacts(
        ContactCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get Contacts by criteria: {}", criteria);
        return contactService
            .countByCriteria(criteria)
            .zipWith(contactService.findByCriteria(criteria, pageable).collectList())
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
     * {@code GET  /contacts/count} : count all the contacts.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public Mono<ResponseEntity<Long>> countContacts(ContactCriteria criteria) {
        log.debug("REST request to count Contacts by criteria: {}", criteria);
        return contactService.countByCriteria(criteria).map(count -> ResponseEntity.status(HttpStatus.OK).body(count));
    }

    /**
     * {@code GET  /contacts/:id} : get the "id" contact.
     *
     * @param id the id of the contactDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the contactDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<ContactDTO>> getContact(@PathVariable("id") UUID id) {
        log.debug("REST request to get Contact : {}", id);
        Mono<ContactDTO> contactDTO = contactService.findOne(id);
        return ResponseUtil.wrapOrNotFound(contactDTO);
    }

    /**
     * {@code DELETE  /contacts/:id} : delete the "id" contact.
     *
     * @param id the id of the contactDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteContact(@PathVariable("id") UUID id) {
        log.debug("REST request to delete Contact : {}", id);
        return contactService
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
