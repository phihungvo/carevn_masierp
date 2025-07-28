package com.masi.employee.web.rest;

import com.masi.employee.repository.ExplanationReviewRepository;
import com.masi.employee.service.ExplanationReviewService;
import com.masi.employee.service.dto.ExplanationReviewDTO;
import com.masi.employee.service.dto.ExplanationRequestObjectBase;
import com.masi.employee.web.rest.errors.BadRequestAlertException;
import jakarta.validation.constraints.NotNull;

import java.net.URISyntaxException;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link com.masi.employee.domain.ExplanationReview}.
 */
@RestController
@RequestMapping("/api/explanation-reviews")
public class ExplanationReviewResource {

    private final Logger log = LoggerFactory.getLogger(ExplanationReviewResource.class);

    private static final String ENTITY_NAME = "masiEmployeeExplanationReview";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ExplanationReviewService explanationReviewService;

    private final ExplanationReviewRepository explanationReviewRepository;

    public ExplanationReviewResource(
        ExplanationReviewService explanationReviewService,
        ExplanationReviewRepository explanationReviewRepository
    ) {
        this.explanationReviewService = explanationReviewService;
        this.explanationReviewRepository = explanationReviewRepository;
    }

    /**
     * {@code PATCH  /explanation-reviews/:id} : Partial updates given fields of an existing explanationReview, field will ignore if it is null
     *
     * @param id the id of the dto to save.
     * @param dto the dto to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dto,
     * or with status {@code 400 (Bad Request)} if the dto is not valid,
     * or with status {@code 404 (Not Found)} if the dto is not found,
     * or with status {@code 500 (Internal Server Error)} if the dto couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ExplanationReviewDTO> partialUpdateExplanationReview(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody ExplanationReviewDTO dto
    ) throws URISyntaxException {
        log.debug("REST request to partial update ExplanationReview partially : {}, {}", id, dto);
        dto.setId(id);
        return explanationReviewRepository
            .existsById(id)
            .flatMap(exists -> {
                if (Boolean.FALSE.equals(exists)) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }
                Mono<ExplanationReviewDTO> result = explanationReviewService.handleReview(dto);
                return result
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)));
            });
    }

    /**
     * {@code GET  /explanation-reviews} : get all the explanationReviews.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of explanationReviews in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<List<ExplanationReviewDTO>> getAllExplanationReviews(@ParameterObject ExplanationRequestObjectBase reviewRO, @ParameterObject Pageable pageable) {
        log.debug("REST request to get all ExplanationReviews");
        return explanationReviewService.find(reviewRO, pageable).collectList();
    }

    /**
     * {@code GET  /explanation-reviews/:id} : get the "id" explanationReview.
     *
     * @param id the id of the explanationReviewDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the explanationReviewDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<ExplanationReviewDTO>> getExplanationReview(@PathVariable("id") UUID id) {
        log.debug("REST request to get ExplanationReview : {}", id);
        Mono<ExplanationReviewDTO> explanationReviewDTO = explanationReviewService.findOne(id);
        return ResponseUtil.wrapOrNotFound(explanationReviewDTO);
    }
}
