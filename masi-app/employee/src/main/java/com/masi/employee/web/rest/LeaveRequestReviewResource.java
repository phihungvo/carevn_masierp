package com.masi.employee.web.rest;

import com.masi.employee.repository.LeaveRequestReviewRepository;
import com.masi.employee.service.LeaveRequestReviewService;
import com.masi.employee.service.dto.LeaveRequestReviewDTO;
import com.masi.employee.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link com.masi.employee.domain.LeaveRequestReview}.
 */
@RestController
@RequestMapping("/api/leave-request-reviews")
public class LeaveRequestReviewResource {

    private final Logger log = LoggerFactory.getLogger(LeaveRequestReviewResource.class);

    private static final String ENTITY_NAME = "masiEmployeeLeaveRequestReview";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LeaveRequestReviewService leaveRequestReviewService;

    private final LeaveRequestReviewRepository leaveRequestReviewRepository;

    public LeaveRequestReviewResource(
        LeaveRequestReviewService leaveRequestReviewService,
        LeaveRequestReviewRepository leaveRequestReviewRepository
    ) {
        this.leaveRequestReviewService = leaveRequestReviewService;
        this.leaveRequestReviewRepository = leaveRequestReviewRepository;
    }

    /**
     * {@code PATCH  /leave-request-reviews/:id} : Partial updates given fields of an existing leaveRequestReview, field will ignore if it is null
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
    public Mono<ResponseEntity<LeaveRequestReviewDTO>> partialUpdateLeaveRequestReview(
        @PathVariable(value = "id") final UUID id,
        @NotNull @RequestBody LeaveRequestReviewDTO dto
    ) throws URISyntaxException {
        log.debug("REST request to partial update LeaveRequestReview partially : {}, {}", id, dto);
        dto.setId(id);
        return leaveRequestReviewRepository
            .existsById(id)
            .flatMap(exists -> {
                if (Boolean.FALSE.equals(exists)) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }
                Mono<LeaveRequestReviewDTO> result = leaveRequestReviewService.handleReview(dto);
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
     * {@code GET  /leave-request-reviews} : get all the leaveRequestReviews.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of leaveRequestReviews in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<List<LeaveRequestReviewDTO>> getAllLeaveRequestReviews() {
        log.debug("REST request to get all LeaveRequestReviews");
        return leaveRequestReviewService.findAll().collectList();
    }

    /**
     * {@code GET  /leave-request-reviews} : get all the leaveRequestReviews as a stream.
     * @return the {@link Flux} of leaveRequestReviews.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<LeaveRequestReviewDTO> getAllLeaveRequestReviewsAsStream() {
        log.debug("REST request to get all LeaveRequestReviews as a stream");
        return leaveRequestReviewService.findAll();
    }

    /**
     * {@code GET  /leave-request-reviews/:id} : get the "id" leaveRequestReview.
     *
     * @param id the id of the leaveRequestReviewDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the leaveRequestReviewDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<LeaveRequestReviewDTO>> getLeaveRequestReview(@PathVariable("id") UUID id) {
        log.debug("REST request to get LeaveRequestReview : {}", id);
        Mono<LeaveRequestReviewDTO> leaveRequestReviewDTO = leaveRequestReviewService.findOne(id);
        return ResponseUtil.wrapOrNotFound(leaveRequestReviewDTO);
    }

    /**
     * {@code DELETE  /leave-request-reviews/:id} : delete the "id" leaveRequestReview.
     *
     * @param id the id of the leaveRequestReviewDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteLeaveRequestReview(@PathVariable("id") UUID id) {
        log.debug("REST request to delete LeaveRequestReview : {}", id);
        return leaveRequestReviewService
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
