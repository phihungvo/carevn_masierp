package com.masi.utility.web.rest;

import com.carevn.masi.dto.ApiResponse;
import com.masi.utility.repository.CronJobRepository;
import com.masi.utility.service.CronJobService;
import com.masi.utility.service.dto.CronJobDTO;
import com.masi.utility.web.rest.errors.BadRequestAlertException;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;

import io.swagger.v3.oas.annotations.Operation;
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
 * REST controller for managing {@link com.masi.utility.domain.CronJob}.
 */
@RestController
@RequestMapping("/api/cron-jobs")
public class CronJobResource {

    private static final Logger log = LoggerFactory.getLogger(CronJobResource.class);

    private static final String ENTITY_NAME = "masiUtilityCronJob";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CronJobService cronJobService;

    private final CronJobRepository cronJobRepository;

    public CronJobResource(CronJobService cronJobService, CronJobRepository cronJobRepository) {
        this.cronJobService = cronJobService;
        this.cronJobRepository = cronJobRepository;
    }


    @PatchMapping(value = "/{id}", consumes = {"application/json", "application/merge-patch+json"})
    @Operation(summary = "Update the cronJob by id")
    public Mono<ResponseEntity<CronJobDTO>> partialUpdateCronJob(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CronJobDTO cronJobDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update CronJob partially : {}, {}", id, cronJobDTO);
        if (cronJobDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cronJobDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return cronJobRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<CronJobDTO> result = cronJobService.partialUpdate(cronJobDTO);

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


    @Operation(summary = "Get all the cronJobs")
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ApiResponse<CronJobDTO>> getAllCronJobs(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of CronJobs");
        return ApiResponse.from(cronJobService.findAll(pageable), cronJobService.countAll());

    }

    /**
     * {@code GET  /cron-jobs/:id} : get the "id" cronJob.
     *
     * @param id the id of the cronJobDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cronJobDTO, or with status {@code 404 (Not Found)}.
     */
    @Operation(summary = "Get the cronJob by id")
    @GetMapping("/{id}")
    public Mono<ResponseEntity<CronJobDTO>> getCronJob(@PathVariable("id") Long id) {
        log.debug("REST request to get CronJob : {}", id);
        Mono<CronJobDTO> cronJobDTO = cronJobService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cronJobDTO);
    }

    /**
     * {@code DELETE  /cron-jobs/:id} : delete the "id" cronJob.
     *
     * @param id the id of the cronJobDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @Operation(summary = "Disable the cronJob by id")
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteCronJob(@PathVariable("id") Long id) {
        log.debug("REST request to delete CronJob : {}", id);
        return cronJobService
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
