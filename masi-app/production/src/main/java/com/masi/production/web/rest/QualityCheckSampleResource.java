package com.masi.production.web.rest;

import com.carevn.masi.dto.ApiResponse;
import com.carevn.masi.utils.SecurityUtils;
import com.carevn.masi.utils.Utilities;
import com.masi.production.repository.QualityCheckSampleRepository;
import com.masi.production.service.QualityCheckSampleService;
import com.masi.production.service.web.EmployeeClient;
import com.masi.production.service.dto.QualityCheckSampleDTO;
import com.masi.production.service.dto.QuanlityCheckSampleRO;
import com.masi.production.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

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
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link com.masi.production.domain.QualityCheckSample}.
 */
@RestController
@RequestMapping("/api/quality-check-samples")
public class QualityCheckSampleResource {

    private final Logger log = LoggerFactory.getLogger(QualityCheckSampleResource.class);

    private static final String ENTITY_NAME = "masiProductionQualityCheckSample";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final QualityCheckSampleService qualityCheckSampleService;

    private final QualityCheckSampleRepository qualityCheckSampleRepository;

    private final EmployeeClient employeeClient;

    public QualityCheckSampleResource(
        QualityCheckSampleService qualityCheckSampleService,
        QualityCheckSampleRepository qualityCheckSampleRepository, EmployeeClient employeeClient
    ) {
        this.qualityCheckSampleService = qualityCheckSampleService;
        this.qualityCheckSampleRepository = qualityCheckSampleRepository;
        this.employeeClient = employeeClient;
    }

    /**
     * {@code POST  /quality-check-samples} : Create a new qualityCheckSample.
     *
     * @param qualityCheckSampleDTO the qualityCheckSampleDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new qualityCheckSampleDTO, or with status {@code 400 (Bad Request)} if the qualityCheckSample has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<Map>> createQualityCheckSample(@Valid @RequestBody QualityCheckSampleDTO qualityCheckSampleDTO)
        throws URISyntaxException {
        log.debug("REST request to save QualityCheckSample : {}", qualityCheckSampleDTO);
        qualityCheckSampleDTO.setId(UUID.randomUUID());
        return qualityCheckSampleService
            .save(qualityCheckSampleDTO)
            .handle((result, sink) -> {
                try {
                    sink.next(ResponseEntity.created(new URI("/api/quality-check-samples/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(Utilities.generateResponse("success", result)));
                } catch (URISyntaxException e) {
                    sink.error(new RuntimeException(e));
                }
            });
    }

    /**
     * {@code PATCH  /quality-check-samples/:id} : Partial updates given fields of an existing qualityCheckSample, field will ignore if it is null
     *
     * @param id                    the id of the qualityCheckSampleDTO to save.
     * @param qualityCheckSampleDTO the qualityCheckSampleDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated qualityCheckSampleDTO,
     * or with status {@code 400 (Bad Request)} if the qualityCheckSampleDTO is not valid,
     * or with status {@code 404 (Not Found)} if the qualityCheckSampleDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the qualityCheckSampleDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = {"application/json", "application/merge-patch+json"})
    public Mono<ResponseEntity<Map>> partialUpdateQualityCheckSample(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody QualityCheckSampleDTO qualityCheckSampleDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update QualityCheckSample partially : {}, {}", id, qualityCheckSampleDTO);
        qualityCheckSampleDTO.setId(id);

        return qualityCheckSampleRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<QualityCheckSampleDTO> result = qualityCheckSampleService.partialUpdate(qualityCheckSampleDTO);

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
     * {@code GET  /quality-check-samples} : get all the qualityCheckSamples.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of qualityCheckSamples in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<ApiResponse<QualityCheckSampleDTO>>> getAllQualityCheckSamples(
        @ParameterObject QuanlityCheckSampleRO ro,
        @ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of QualityCheckSamples");
        return SecurityUtils.getUserJWTDetail().flatMap(user -> {
            ro.setCompany(String.valueOf(user.getCompanyId()));
            ro.setDepartment(user.getGroupId());
            return qualityCheckSampleService
                .countAllByQuery(ro)
                .zipWith(qualityCheckSampleService.findAllByQuery(pageable, ro).collectList()
                    .flatMap(qualityCheckSampleDTOS -> {
                        Map<UUID, LinkedList<QualityCheckSampleDTO>> map = new HashMap<>();
                        qualityCheckSampleDTOS.forEach(qualityCheckSampleDTO -> {
                            if (qualityCheckSampleDTO.getDisposal() == null)
                                return;
                            if (!map.containsKey(qualityCheckSampleDTO.getDisposal().getReviewerId()))
                                map.put(qualityCheckSampleDTO.getDisposal().getReviewerId(), new LinkedList<>());
                            map.get(qualityCheckSampleDTO.getDisposal().getReviewerId()).add(qualityCheckSampleDTO);

                        });

                        return employeeClient.getEmployeesByListIds(new ArrayList<>(map.keySet()))
                            .collectList()
                            .map(employeeDTOS -> {
                                employeeDTOS.forEach(employeeDTO -> {
                                    if (map.containsKey(employeeDTO.getId()))
                                        map.get(employeeDTO.getId()).forEach(qualityCheckSampleDTO -> {
                                            if (qualityCheckSampleDTO.getDisposal() != null)
                                                qualityCheckSampleDTO.getDisposal().setReviewer(employeeDTO);
                                        });
                                });
                                return Mono.just(qualityCheckSampleDTOS);
                            }).then(Mono.just(qualityCheckSampleDTOS));
                    })
                )
                .map(countWithEntities -> ResponseEntity.ok().body(new ApiResponse<>(countWithEntities.getT2(), countWithEntities.getT1())));
        });

    }

    // api handle revew pass:

    @PatchMapping("/{id}/review-pass")
    public Mono<ResponseEntity<Map>> reviewPass(@Valid @PathVariable UUID id) {
        log.debug("REST request to review pass QualityCheckSample : {}", id);
        return qualityCheckSampleService
            .handleReviewPass(id)
            .handle((result, sink) -> {
                sink.next(ResponseEntity.ok().body(Utilities.generateResponse("success", result)));
            });
    }

    @PatchMapping("/{id}/review-fail")
    public Mono<ResponseEntity<Map>> reviewFail(@Valid @PathVariable UUID id) {
        log.debug("REST request to review fail QualityCheckSample : {}", id);
        return qualityCheckSampleService
            .handleFailQualityCheck(id)
            .handle((result, sink) -> {
                sink.next(ResponseEntity.ok().body(Utilities.generateResponse("success", result)));
            });
    }

    /**
     * {@code GET  /quality-check-samples/:id} : get the "id" qualityCheckSample.
     *
     * @param id the id of the qualityCheckSampleDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the qualityCheckSampleDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<QualityCheckSampleDTO>> getQualityCheckSample(@PathVariable("id") UUID id) {
        log.debug("REST request to get QualityCheckSample : {}", id);
        Mono<QualityCheckSampleDTO> qualityCheckSampleDTO = qualityCheckSampleService.findOne(id);
        return ResponseUtil.wrapOrNotFound(qualityCheckSampleDTO);
    }

    /**
     * {@code DELETE  /quality-check-samples/:id} : delete the "id" qualityCheckSample.
     *
     * @param id the id of the qualityCheckSampleDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteQualityCheckSample(@PathVariable("id") UUID id) {
        log.debug("REST request to delete QualityCheckSample : {}", id);
        return qualityCheckSampleService
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
