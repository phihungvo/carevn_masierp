package com.masi.utility.web.rest;

import com.carevn.masi.dto.ApiResponse;
import com.masi.utility.domain.criteria.ConfigCriteria;
import com.masi.utility.domain.enumeration.DataType;
import com.masi.utility.repository.ConfigRepository;
import com.masi.utility.service.ConfigService;
import com.masi.utility.service.dto.ConfigDTO;
import com.masi.utility.web.rest.errors.BadRequestAlertException;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
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
 * REST controller for managing {@link com.masi.utility.domain.Config}.
 */
@RestController
@RequestMapping("/api/configs")
public class ConfigResource {

    private static final Logger LOG = LoggerFactory.getLogger(ConfigResource.class);

    private static final String ENTITY_NAME = "masiUtilityConfig";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ConfigService configService;

    private final ConfigRepository configRepository;

    public ConfigResource(ConfigService configService, ConfigRepository configRepository) {
        this.configService = configService;
        this.configRepository = configRepository;
    }


    @PatchMapping(value = "/{id}", consumes = {"application/json", "application/merge-patch+json"})
    public Mono<ResponseEntity<ConfigDTO>> partialUpdateConfig(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ConfigDTO configDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Config partially : {}, {}", id, configDTO);
        if (configDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, configDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return configRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<ConfigDTO> result = configService.partialUpdate(configDTO);

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
    public Mono<ResponseEntity<ApiResponse<ConfigDTO>>> getAllConfigs(
        ConfigCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get Configs by criteria: {}", criteria);
        return ApiResponse.from(configService.findByCriteria(criteria, pageable), configService.countByCriteria(criteria))
            .map(response -> ResponseEntity.ok().body(response));
    }

    @Data
    public static class ConfigDefault {
        private String value;
        private DataType type;
        private String description;
    }

    @Operation(summary = "Get config by key, if not found create with default value")
    @PostMapping("/{key}")
    public Mono<ResponseEntity<ConfigDTO>> getConfig(@PathVariable("key") String key, @RequestBody ConfigDefault body) {
        Mono<ConfigDTO> configDTO = configService.findByKey(key).switchIfEmpty(Mono.defer(() -> {
            var defaultValue = body.getValue();
            var dataType = body.getType();
            var description = body.getDescription();
            if (defaultValue == null || dataType == null) {
                return Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND));
            }
            var newConfig = new ConfigDTO();
            newConfig.setKey(key);
            newConfig.setValue(defaultValue);
            newConfig.setType(dataType);
            newConfig.setDescription(description);
            return configService.createConfig(newConfig);
        }));
        return ResponseUtil.wrapOrNotFound(configDTO);
    }

}
