package com.masi.utility.web.rest;

import com.masi.utility.domain.criteria.StandardWorkScheduleConfigCriteria;
import com.masi.utility.repository.StandardWorkScheduleConfigRepository;
import com.masi.utility.service.StandardWorkScheduleConfigService;
import com.masi.utility.service.dto.StandardWorkScheduleConfigDTO;
import com.masi.utility.service.dto.WorkScheduleQuery;
import com.masi.utility.web.rest.errors.BadRequestAlertException;

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
 * REST controller for managing {@link com.masi.utility.domain.StandardWorkScheduleConfig}.
 */
@RestController
@RequestMapping("/api/configs/standard-work-schedule-configs")
public class StandardWorkScheduleConfigResource {

    private static final Logger LOG = LoggerFactory.getLogger(StandardWorkScheduleConfigResource.class);

    private static final String ENTITY_NAME = "masiUtilityStandardWorkScheduleConfig";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final StandardWorkScheduleConfigService standardWorkScheduleConfigService;

    private final StandardWorkScheduleConfigRepository standardWorkScheduleConfigRepository;

    public StandardWorkScheduleConfigResource(
        StandardWorkScheduleConfigService standardWorkScheduleConfigService,
        StandardWorkScheduleConfigRepository standardWorkScheduleConfigRepository
    ) {
        this.standardWorkScheduleConfigService = standardWorkScheduleConfigService;
        this.standardWorkScheduleConfigRepository = standardWorkScheduleConfigRepository;
    }

    /**
     * {@code PATCH  /standard-work-schedule-configs/:id} : Partial updates given fields of an existing standardWorkScheduleConfig, field will ignore if it is null
     *
     * @param id                            the id of the standardWorkScheduleConfigDTO to save.
     * @param standardWorkScheduleConfigDTO the standardWorkScheduleConfigDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated standardWorkScheduleConfigDTO,
     * or with status {@code 400 (Bad Request)} if the standardWorkScheduleConfigDTO is not valid,
     * or with status {@code 404 (Not Found)} if the standardWorkScheduleConfigDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the standardWorkScheduleConfigDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = {"application/json", "application/merge-patch+json"})
    public Mono<ResponseEntity<StandardWorkScheduleConfigDTO>> partialUpdateStandardWorkScheduleConfig(
        @PathVariable(value = "id", required = false) final UUID id,
        @RequestBody StandardWorkScheduleConfigDTO standardWorkScheduleConfigDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update StandardWorkScheduleConfig partially : {}, {}", id, standardWorkScheduleConfigDTO);
        if (standardWorkScheduleConfigDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, standardWorkScheduleConfigDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return standardWorkScheduleConfigRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<StandardWorkScheduleConfigDTO> result = standardWorkScheduleConfigService.partialUpdate(standardWorkScheduleConfigDTO);

                return result
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(res ->
                        ResponseEntity.ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, res.getId().toString()))
                            .body(res)
                    );
            });
    }


    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<List<StandardWorkScheduleConfigDTO>> getAllStandardWorkScheduleConfigs(
        WorkScheduleQuery query
    ) {
        return standardWorkScheduleConfigService
            .findByQuery(query);
    }
}
