package com.masi.production.web.rest;

import com.carevn.masi.utils.Utilities;
import com.masi.production.repository.SampleDisposalRepository;
import com.masi.production.service.SampleDisposalService;
import com.masi.production.service.dto.ReviewSampleDisposalDTO;
import com.masi.production.service.dto.SampleDisposalDTO;
import com.masi.production.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link com.masi.production.domain.SampleDisposal}.
 */
@RestController
@RequestMapping("/api/sample-disposals")
public class SampleDisposalResource {

    private final Logger log = LoggerFactory.getLogger(SampleDisposalResource.class);

    private static final String ENTITY_NAME = "masiProductionSampleDisposal";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SampleDisposalService sampleDisposalService;

    private final SampleDisposalRepository sampleDisposalRepository;

    public SampleDisposalResource(SampleDisposalService sampleDisposalService, SampleDisposalRepository sampleDisposalRepository) {
        this.sampleDisposalService = sampleDisposalService;
        this.sampleDisposalRepository = sampleDisposalRepository;
    }

    /**
     * {@code POST  /sample-disposals} : Create a new sampleDisposal.
     *
     * @param sampleDisposalDTO the sampleDisposalDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new sampleDisposalDTO, or with status {@code 400 (Bad Request)} if the sampleDisposal has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<Map>> createSampleDisposal(@Valid @RequestBody SampleDisposalDTO sampleDisposalDTO)
        throws URISyntaxException {
        log.debug("REST request to save SampleDisposal : {}", sampleDisposalDTO);
        sampleDisposalDTO.setRequesterId(UUID.randomUUID());
        sampleDisposalDTO.setId(UUID.randomUUID());
        return sampleDisposalService
            .save(sampleDisposalDTO)
            .handle((result, sink) -> {
                try {
                    sink.next(ResponseEntity.created(new URI("/api/sample-disposals/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(Utilities.generateResponse("success", result)));
                } catch (URISyntaxException e) {
                    sink.error(new RuntimeException(e));
                }
            });
    }

    /**
     * {@code PATCH  /sample-disposals/:id/reviews} : review the sampleDisposal.
     *
     * @param id                      the id of the sampleDisposalDTO to review.
     * @param reviewSampleDisposalDTO the reviewSampleDisposalDTO to review.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sampleDisposalDTO,
     */

    @PatchMapping("/{id}/reviews")
    public Mono<ResponseEntity<Map>> reviewSampleDisposal(
        @PathVariable("id") UUID id,
        @Valid @RequestBody ReviewSampleDisposalDTO reviewSampleDisposalDTO
    ) {
        reviewSampleDisposalDTO.setId(id);
        log.debug("REST request to review SampleDisposal : {}", reviewSampleDisposalDTO);
        return sampleDisposalService
            .review(reviewSampleDisposalDTO)
            .map(
                result ->
                    ResponseEntity.ok()
                        .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(Utilities.generateResponse("success", result))
            );
    }

    /**
     * {@code PATCH  /sample-disposals/:id} : Partial updates given fields of an existing sampleDisposal, field will ignore if it is null
     *
     * @param id                the id of the sampleDisposalDTO to save.
     * @param sampleDisposalDTO the sampleDisposalDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sampleDisposalDTO,
     * or with status {@code 400 (Bad Request)} if the sampleDisposalDTO is not valid,
     * or with status {@code 404 (Not Found)} if the sampleDisposalDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the sampleDisposalDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<Map>> partialUpdateSampleDisposal(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody SampleDisposalDTO sampleDisposalDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update SampleDisposal partially : {}, {}", id, sampleDisposalDTO);
        sampleDisposalDTO.setId(id);

        return sampleDisposalRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<SampleDisposalDTO> result = sampleDisposalService.partialUpdate(sampleDisposalDTO);

                return result
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(
                        res ->
                            ResponseEntity.ok()
                                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, res.getId().toString()))
                                .body(Utilities.generateResponse("success", res))
                    );
            });
    }

    /**
     * {@code GET  /sample-disposals/:id} : get the "id" sampleDisposal.
     *
     * @param id the id of the sampleDisposalDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the sampleDisposalDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<SampleDisposalDTO>> getSampleDisposal(@PathVariable("id") UUID id) {
        log.debug("REST request to get SampleDisposal : {}", id);
        Mono<SampleDisposalDTO> sampleDisposalDTO = sampleDisposalService.findOne(id);
        return ResponseUtil.wrapOrNotFound(sampleDisposalDTO);
    }
}
