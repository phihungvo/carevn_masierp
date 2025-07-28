package com.masi.employee.web.rest;

import com.carevn.masi.utils.SecurityUtils;
import com.masi.employee.repository.UniformReturnRepository;
import com.masi.employee.service.UniformReturnService;
import com.masi.employee.service.dto.UniformReturnDTO;
import com.masi.employee.web.rest.errors.BadRequestAlertException;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
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
 * REST controller for managing {@link com.masi.employee.domain.UniformReturn}.
 */
@Hidden
@RestController
@RequestMapping("/api/uniform-returns")
public class UniformReturnResource {

    private static final Logger log = LoggerFactory.getLogger(UniformReturnResource.class);

    private static final String ENTITY_NAME = "masiEmployeeUniformReturn";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UniformReturnService uniformReturnService;

    public UniformReturnResource(UniformReturnService uniformReturnService) {
        this.uniformReturnService = uniformReturnService;
    }

    @PostMapping("")
    public Mono<ResponseEntity<UniformReturnDTO>> createUniformReturn(
        @Valid @RequestBody UniformReturnDTO uniformReturnDTO)
        throws URISyntaxException {
        log.debug("REST request to save UniformReturn : {}", uniformReturnDTO);

        uniformReturnDTO.setId(UUID.randomUUID());
        return SecurityUtils.getUserJWTDetail().flatMap(userJWTDetail -> {
            uniformReturnDTO.setCreateBy(userJWTDetail.getUserId().toString());
            uniformReturnDTO.setUpdateBy(userJWTDetail.getUserId().toString());
            uniformReturnDTO.setCompany(userJWTDetail.getCompanyId());
            return uniformReturnService
                .saveV3(uniformReturnDTO)
                .map(result -> {
                    return ResponseEntity.ok()
                        .body(result);
                });
        });
    }

    @GetMapping("remaining")
    public Mono<ResponseEntity<Map<String, Long>>> countTotalRemaining(
        @RequestParam("employeeId") UUID employeeId,
        @RequestParam("uniformId") UUID uniformId) {
        return uniformReturnService.countTotalRemainingUniform(employeeId, uniformId)
            .map(result -> {
                return ResponseEntity.ok()
                    .body(Map.of("totalRemaining", result));
            }).switchIfEmpty(Mono.just(ResponseEntity.ok()
                .body(Map.of("totalRemaining", 0L))));
    }
}
