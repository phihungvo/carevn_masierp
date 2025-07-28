package com.masi.employee.web.rest;

import com.carevn.masi.dto.ApiResponse;
import com.carevn.masi.utils.SecurityUtils;
import com.masi.employee.repository.UniformReleaseRepository;
import com.masi.employee.service.UniformReleaseService;
import com.masi.employee.service.dto.EmployeeDTO;
import com.masi.employee.service.dto.UniformReleaseDTO;
import com.masi.employee.service.dto.UniformReleaseGetListDTO;
import com.masi.employee.service.dto.UniformStockReleaseDTO;
import com.masi.employee.service.reports.ReportService;
import com.masi.employee.service.web.client.AuthClient;
import com.masi.employee.web.rest.errors.BadRequestAlertException;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
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
 * REST controller for managing {@link com.masi.employee.domain.UniformRelease}.
 */
@RestController
@RequestMapping("/api/uniform-releases")
public class UniformReleaseResource {

    private static final Logger log = LoggerFactory.getLogger(UniformReleaseResource.class);

    private static final String ENTITY_NAME = "masiEmployeeUniformRelease";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UniformReleaseService uniformReleaseService;

    private final UniformReleaseRepository uniformReleaseRepository;

    private final ReportService reportService;

    private final AuthClient authClient;

    public UniformReleaseResource(UniformReleaseService uniformReleaseService,
                                  UniformReleaseRepository uniformReleaseRepository, ReportService reportService, AuthClient authClient) {
        this.uniformReleaseService = uniformReleaseService;
        this.uniformReleaseRepository = uniformReleaseRepository;
        this.reportService = reportService;
        this.authClient = authClient;
    }

    @GetMapping("test-auth")
    public Mono<Map> testAuth() {
        return authClient.getEmployeeDirectorCompany("KIM_LONG");
    }

    @Operation(summary = "Xuất hàng")
    @PostMapping("")
    public Mono<ResponseEntity<UniformReleaseDTO>> createUniformRelease(
            @Valid @RequestBody UniformStockReleaseDTO uniformStockReleaseDTO)
            throws URISyntaxException {
        log.debug("REST request to save UniformRelease : {}", uniformStockReleaseDTO);
        return SecurityUtils.getUserJWTDetail().flatMap(user -> {
            uniformStockReleaseDTO.setCompany(user.getCompanyId());
            return uniformReleaseService
                .handleCreateReleaseStock(uniformStockReleaseDTO)
                .handle((result, sink) -> {
                    try {
                        sink.next(ResponseEntity.created(
                                new URI("/api/uniform-releases/" + result.getId()))
                            .headers(HeaderUtil.createEntityCreationAlert(
                                applicationName, true, ENTITY_NAME,
                                result.getId().toString()))
                            .body(result));
                    } catch (URISyntaxException e) {
                        sink.error(new RuntimeException(e));
                    }
                });
        });
    }


    @Operation(hidden = true)
    @PutMapping("/{id}")
    public Mono<ResponseEntity<UniformReleaseDTO>> updateUniformRelease(
            @PathVariable(value = "id", required = false) final UUID id,
            @Valid @RequestBody UniformReleaseDTO uniformReleaseDTO) {
        log.debug("REST request to update UniformRelease : {}, {}", id, uniformReleaseDTO);
        if (uniformReleaseDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, uniformReleaseDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return uniformReleaseRepository
                .existsById(id)
                .flatMap(exists -> {
                    if (!exists) {
                        return Mono.error(new BadRequestAlertException("Entity not found",
                                ENTITY_NAME, "idnotfound"));
                    }

                    return uniformReleaseService
                            .update(uniformReleaseDTO)
                            .switchIfEmpty(Mono.error(new ResponseStatusException(
                                    HttpStatus.NOT_FOUND)))
                            .map(
                                    result -> ResponseEntity.ok()
                                            .headers(HeaderUtil
                                                    .createEntityUpdateAlert(
                                                            applicationName,
                                                            true,
                                                            ENTITY_NAME,
                                                            result.getId().toString()))
                                            .body(result));
                });
    }

    @Operation(hidden = true)
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<UniformReleaseDTO>> partialUpdateUniformRelease(
            @PathVariable(value = "id", required = false) final UUID id,
            @NotNull @RequestBody UniformReleaseDTO uniformReleaseDTO) throws URISyntaxException {
        log.debug("REST request to partial update UniformRelease partially : {}, {}", id, uniformReleaseDTO);
        if (uniformReleaseDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, uniformReleaseDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return uniformReleaseRepository
                .existsById(id)
                .flatMap(exists -> {
                    if (!exists) {
                        return Mono.error(new BadRequestAlertException("Entity not found",
                                ENTITY_NAME, "idnotfound"));
                    }

                    Mono<UniformReleaseDTO> result = uniformReleaseService
                            .partialUpdate(uniformReleaseDTO);

                    return result
                            .switchIfEmpty(Mono.error(new ResponseStatusException(
                                    HttpStatus.NOT_FOUND)))
                            .map(
                                    res -> ResponseEntity.ok()
                                            .headers(HeaderUtil
                                                    .createEntityUpdateAlert(
                                                            applicationName,
                                                            true,
                                                            ENTITY_NAME,
                                                            res.getId().toString()))
                                            .body(res));
                });
    }

    /**
     * {@code GET  /uniform-releases} : get all the uniformReleases.
     *
     * @param pageable the pagination information.
     * @param request  a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
     *         of uniformReleases in body.
     */
    @Operation(summary = "Lấy danh sách xuất hàng")
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<ApiResponse<UniformReleaseDTO>>> getAllUniformReleases(
            @org.springdoc.core.annotations.ParameterObject Pageable pageable,
            @ParameterObject UniformReleaseGetListDTO uniformReleaseGetListDTO,
            ServerHttpRequest request) {
        log.debug("REST request to get a page of UniformReleases");
        return SecurityUtils.getCompanyId().flatMap(company -> {
            uniformReleaseGetListDTO.setCompany(company);
            return uniformReleaseService
                    .countAllByQuery(uniformReleaseGetListDTO)
                    .zipWith(uniformReleaseService.findAll(pageable,
                            uniformReleaseGetListDTO).collectList())
                    .map(
                            countWithEntities -> ResponseEntity.ok()
                                    .headers(
                                            PaginationUtil.generatePaginationHttpHeaders(
                                                    ForwardedHeaderUtils
                                                            .adaptFromForwardedHeaders(
                                                                    request.getURI(),
                                                                    request.getHeaders()),
                                                    new PageImpl<>(countWithEntities
                                                            .getT2(),
                                                            pageable,
                                                            countWithEntities
                                                                    .getT1())))
                                    .body(new ApiResponse<>(
                                            countWithEntities.getT2(),
                                            countWithEntities.getT1())));
        });
    }

    /**
     * {@code GET  /uniform-releases} : get all the uniformReleases.
     *
     * @param pageable the pagination information.
     * @param request  a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
     *         of uniformReleases in body.
     */
    @Operation(summary = "Lấy danh sách xuất hàng")
    @GetMapping(value = "/export-xlsx")
    public Mono<ResponseEntity<InputStreamResource>> exportReleases(
        @RequestParam(value = "download", defaultValue = "true") boolean download,
        @ParameterObject UniformReleaseGetListDTO uniformReleaseGetListDTO,
        ServerHttpRequest request) {
        log.debug("REST request to export a page of UniformReleases");
        return SecurityUtils.getCompanyId().flatMap(company -> {
            uniformReleaseGetListDTO.setCompany(company);
            Pageable pageable = Pageable.ofSize(Integer.MAX_VALUE);
            return uniformReleaseService.findAll(pageable, uniformReleaseGetListDTO)
                .collectList().flatMap(releaseDTOS -> {
                    if (releaseDTOS.isEmpty()) {
                        return Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND));
                    }
                    return reportService.exportUniformReleaseToExcel(releaseDTOS)
                        .handle((file, sink) -> {
                            try {
                                FileInputStream fileInputStream = new FileInputStream(file);
                                InputStreamResource inputStreamResource = new InputStreamResource(fileInputStream);
                                HttpHeaders headers = new HttpHeaders();
                                String contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

                                if (download) {
                                    headers.add(HttpHeaders.CONTENT_DISPOSITION,
                                        String.format("attachment; filename=\"%s\"", "uniform_release" + ".xlsx"));
                                } else {
                                    headers.add(HttpHeaders.CONTENT_TYPE, contentType);
                                    headers.add("content-name", "uniform_release" + ".xlsx");
                                }
                                sink.next(new ResponseEntity<>(inputStreamResource, headers, HttpStatus.OK));
                            } catch (IOException e) {
                                sink.error(e);
                            }
                        });
                });
        });
    }

    /**
     * {@code GET  /uniform-releases/:id} : get the "id" uniformRelease.
     *
     * @param id the id of the uniformReleaseDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the uniformReleaseDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<UniformReleaseDTO>> getUniformRelease(@PathVariable("id") UUID id) {
        log.debug("REST request to get UniformRelease : {}", id);
        Mono<UniformReleaseDTO> uniformReleaseDTO = uniformReleaseService.findOne(id);
        return ResponseUtil.wrapOrNotFound(uniformReleaseDTO);
    }

    /**
     * {@code DELETE  /uniform-releases/:id} : delete the "id" uniformRelease.
     *
     * @param id the id of the uniformReleaseDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @Operation(hidden = true)
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteUniformRelease(@PathVariable("id") UUID id) {
        log.debug("REST request to delete UniformRelease : {}", id);
        return uniformReleaseService
                .delete(id)
                .then(
                        Mono.just(
                                ResponseEntity.noContent()
                                        .headers(HeaderUtil
                                                .createEntityDeletionAlert(
                                                        applicationName,
                                                        true,
                                                        ENTITY_NAME,
                                                        id.toString()))
                                        .build()));
    }
}
