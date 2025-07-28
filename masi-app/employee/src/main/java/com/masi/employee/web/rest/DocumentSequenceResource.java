package com.masi.employee.web.rest;

import com.masi.employee.repository.DocumentSequenceRepository;
import com.masi.employee.service.DocumentSequenceService;
import com.masi.employee.service.dto.DocumentSequenceDTO;
import com.masi.employee.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link com.masi.employee.domain.DocumentSequence}.
 */
@RestController
@RequestMapping("/api/document-sequences")
public class DocumentSequenceResource {

    private static final Logger log = LoggerFactory.getLogger(DocumentSequenceResource.class);

    private static final String ENTITY_NAME = "masiEmployeeDocumentSequence";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DocumentSequenceService documentSequenceService;

    private final DocumentSequenceRepository documentSequenceRepository;

    public DocumentSequenceResource(
        DocumentSequenceService documentSequenceService,
        DocumentSequenceRepository documentSequenceRepository
    ) {
        this.documentSequenceService = documentSequenceService;
        this.documentSequenceRepository = documentSequenceRepository;
    }

    /**
     * {@code POST  /document-sequences} : Create a new documentSequence.
     *
     * @param documentSequenceDTO the documentSequenceDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new documentSequenceDTO, or with status {@code 400 (Bad Request)} if the documentSequence has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<DocumentSequenceDTO>> createDocumentSequence(@RequestBody DocumentSequenceDTO documentSequenceDTO)
        throws URISyntaxException {
        log.debug("REST request to save DocumentSequence : {}", documentSequenceDTO);
        if (documentSequenceDTO.getId() != null) {
            throw new BadRequestAlertException("A new documentSequence cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return documentSequenceService
            .save(documentSequenceDTO)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/document-sequences/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /document-sequences/:id} : Updates an existing documentSequence.
     *
     * @param id the id of the documentSequenceDTO to save.
     * @param documentSequenceDTO the documentSequenceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated documentSequenceDTO,
     * or with status {@code 400 (Bad Request)} if the documentSequenceDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the documentSequenceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<DocumentSequenceDTO>> updateDocumentSequence(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DocumentSequenceDTO documentSequenceDTO
    ) throws URISyntaxException {
        log.debug("REST request to update DocumentSequence : {}, {}", id, documentSequenceDTO);
        if (documentSequenceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, documentSequenceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return documentSequenceRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return documentSequenceService
                    .update(documentSequenceDTO)
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
     * {@code PATCH  /document-sequences/:id} : Partial updates given fields of an existing documentSequence, field will ignore if it is null
     *
     * @param id the id of the documentSequenceDTO to save.
     * @param documentSequenceDTO the documentSequenceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated documentSequenceDTO,
     * or with status {@code 400 (Bad Request)} if the documentSequenceDTO is not valid,
     * or with status {@code 404 (Not Found)} if the documentSequenceDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the documentSequenceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<DocumentSequenceDTO>> partialUpdateDocumentSequence(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DocumentSequenceDTO documentSequenceDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update DocumentSequence partially : {}, {}", id, documentSequenceDTO);
        if (documentSequenceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, documentSequenceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return documentSequenceRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<DocumentSequenceDTO> result = documentSequenceService.partialUpdate(documentSequenceDTO);

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
     * {@code GET  /document-sequences} : get all the documentSequences.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of documentSequences in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<List<DocumentSequenceDTO>> getAllDocumentSequences() {
        log.debug("REST request to get all DocumentSequences");
        return documentSequenceService.findAll().collectList();
    }

    /**
     * {@code GET  /document-sequences} : get all the documentSequences as a stream.
     * @return the {@link Flux} of documentSequences.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<DocumentSequenceDTO> getAllDocumentSequencesAsStream() {
        log.debug("REST request to get all DocumentSequences as a stream");
        return documentSequenceService.findAll();
    }

    /**
     * {@code GET  /document-sequences/:id} : get the "id" documentSequence.
     *
     * @param id the id of the documentSequenceDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the documentSequenceDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<DocumentSequenceDTO>> getDocumentSequence(@PathVariable("id") Long id) {
        log.debug("REST request to get DocumentSequence : {}", id);
        Mono<DocumentSequenceDTO> documentSequenceDTO = documentSequenceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(documentSequenceDTO);
    }

    /**
     * {@code DELETE  /document-sequences/:id} : delete the "id" documentSequence.
     *
     * @param id the id of the documentSequenceDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteDocumentSequence(@PathVariable("id") Long id) {
        log.debug("REST request to delete DocumentSequence : {}", id);
        return documentSequenceService
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
