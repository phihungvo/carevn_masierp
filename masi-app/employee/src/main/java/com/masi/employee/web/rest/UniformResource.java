package com.masi.employee.web.rest;

import com.carevn.masi.dto.ApiResponse;
import com.carevn.masi.utils.SecurityUtils;
import com.masi.employee.repository.EmployeeRepository;
import com.masi.employee.repository.UniformRepository;
import com.masi.employee.service.EmployeeProfileService;
import com.masi.employee.service.EmployeeService;
import com.masi.employee.service.LogisticClient;
import com.masi.employee.service.UniformService;
import com.masi.employee.service.dto.*;
import com.masi.employee.service.reports.ReportService;
import com.masi.employee.web.rest.errors.BadRequestAlertException;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

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
 * REST controller for managing {@link com.masi.employee.domain.Uniform}.
 */
@RestController
@RequestMapping("/api/uniforms")
public class UniformResource {

    private static final Logger log = LoggerFactory.getLogger(UniformResource.class);

    private static final String ENTITY_NAME = "masiEmployeeUniform";
    private final EmployeeRepository employeeRepository;

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UniformService uniformService;

    private final UniformRepository uniformRepository;

    private final LogisticClient logisticClient;

    private final ReportService reportService;

    private final EmployeeService employeeService;

    private final EmployeeProfileService employeeProfileService;

    public UniformResource(EmployeeRepository employeeRepository, UniformService uniformService, UniformRepository uniformRepository, LogisticClient logisticClient, ReportService reportService, EmployeeService employeeService, EmployeeProfileService employeeProfileService) {
        this.employeeRepository = employeeRepository;
        this.uniformService = uniformService;
        this.uniformRepository = uniformRepository;
        this.logisticClient = logisticClient;
        this.reportService = reportService;
        this.employeeService = employeeService;
        this.employeeProfileService = employeeProfileService;
    }

    @PostMapping("")
    public Mono<ResponseEntity<UniformDTO>> createUniform(@Valid @RequestBody UniformDTO uniformDTO)
        throws URISyntaxException {
        log.debug("REST request to save Uniform : {}", uniformDTO);
        if (uniformDTO.getId() != null) {
            throw new BadRequestAlertException("A new uniform cannot already have an ID", ENTITY_NAME, "idexists");
        }
        uniformDTO.setId(UUID.randomUUID());
        return SecurityUtils.getUserJWTDetail().flatMap(user -> {
            return uniformService
                .save(uniformDTO, user.getCompanyId())
                .map(result -> {
                    try {
                        return ResponseEntity.created(new URI("/api/uniforms/" + result.getId()))
                            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME,
                                result.getId().toString()))
                            .body(result);
                    } catch (URISyntaxException e) {
                        throw new RuntimeException(e);
                    }
                });
        });
    }


    @PatchMapping(value = "/{id}", consumes = {"application/json", "application/merge-patch+json"})
    public Mono<ResponseEntity<UniformDTO>> partialUpdateUniform(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody UniformDTO uniformDTO) throws URISyntaxException {
        log.debug("REST request to partial update Uniform partially : {}, {}", id, uniformDTO);

        uniformDTO.setId(id);
        return uniformRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<UniformDTO> result = uniformService.partialUpdate(uniformDTO);

                return result
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(
                        res -> ResponseEntity.ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true,
                                ENTITY_NAME, res.getId().toString()))
                            .body(res));
            });
    }


    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<ApiResponse<UniformDTO>>> getAllUniforms(
        @ParameterObject UniformQuery query,
        @ParameterObject Pageable pageable,
        ServerHttpRequest request) {

        log.debug("REST request to get a page of Uniforms");

        return SecurityUtils.getUserJWTDetail()
            .flatMap(user -> {
                query.setCompany(user.getCompanyId());
                return uniformService.countAll(query)
                    .zipWith(uniformService.findAllByCompanyAndIsDeletedIsFalse(pageable, query)
                        .collectList().flatMap(t -> {
                            var uomIds = t.stream().map(UniformDTO::getUomId).toList();
                            return logisticClient.getUomByListIds(uomIds).collectList().map(emps -> {
                                var empMap = emps.stream().collect(Collectors.toMap(UomDTO::getId, Function.identity()));
                                t.forEach(c -> {
                                    c.setUomDTO(empMap.get(c.getUomId()));
                                });
                                return t;
                            });
                        }))
                    .map(countWithEntities -> ResponseEntity.ok()
                        .body(new ApiResponse<>(countWithEntities.getT2(), countWithEntities.getT1())));
            });
    }


    @GetMapping("/{id}")
    public Mono<ResponseEntity<UniformDTO>> getUniform(@PathVariable("id") UUID id) {
        log.debug("REST request to get Uniform : {}", id);

        return uniformService.findOne(id)
            .flatMap(uniformDTO ->
                logisticClient.getUomById(uniformDTO.getUomId())
                    .map(uomDTO -> {
                        uniformDTO.setUomDTO(uomDTO);
                        return uniformDTO;
                    })
            )
            .flatMap(uniformDTO -> ResponseUtil.wrapOrNotFound(Mono.just(uniformDTO)));
    }


    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Map>> deleteUniform(@PathVariable("id") UUID id) {
        log.debug("REST request to delete Uniform : {}", id);
        return SecurityUtils.getUserJWTDetail()
            .flatMap(user -> uniformService
                .delete(id, user.getUserId())
                .then(
                    Mono.just(
                        ResponseEntity.ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, id.toString()))
                            .body(Collections.singletonMap("message", "Customer successfully disabled"))
                    )
                ));
    }

    @PatchMapping("/{id}/status/{status}")
    public Mono<ResponseEntity<Void>> updateStatus(@PathVariable("id") UUID id, @PathVariable("status") String status) {

        return uniformService.setStatus(id, status)
            .then(Mono.just(ResponseEntity.noContent().headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, id.toString())).build()));
    }



    /**
     *
     *
     * @param download the id of the uniformOrderDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the uniformOrderDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/export-xlsx")
    public Mono<ResponseEntity<InputStreamResource>> exportUniformOrder(
        @RequestParam(value = "download", defaultValue = "true") boolean download,
        @RequestParam(value = "startDate", required = false) LocalDate startDate,
        @RequestParam(value = "endDate", required = false) LocalDate endDate) {
        var uniformQuery = new UniformQuery();
        ZonedDateTime zdtStart = null;
        ZonedDateTime zdtEnd = null;
        if (startDate != null) {
            zdtStart = startDate.atStartOfDay(ZoneOffset.UTC);
        }
        if (endDate != null) {
            zdtEnd = endDate.atStartOfDay(ZoneOffset.UTC);
        }
        uniformQuery.setStartDate(zdtStart);
        uniformQuery.setEndDate(zdtEnd);

        return SecurityUtils.getUserJWTDetail().flatMap(user -> {
            uniformQuery.setCompany(user.getCompanyId());
//            uniformOrderGetlistDTO.setStatus(new ArrayList<>(of(UniformOrderStatus.STOCKED)));
            return uniformService.findAllByCompanyAndIsDeletedIsFalse(null, uniformQuery)
                .collectList().flatMap(uniform -> {
                    if (uniform.isEmpty()) {
                        return Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND));
                    }
                    var uomIds = uniform.stream().map(UniformDTO::getUomId).toList();
                    var listEmp = uniform.stream()
                        .map(UniformDTO::getCreateBy)  // Lấy giá trị createBy
                        .distinct()
                        .toList();

                    return logisticClient.getUomByListIds(uomIds).collectList().flatMap(uoms -> {
                        var empMap = uoms.stream().collect(Collectors.toMap(UomDTO::getId, Function.identity()));
                        return employeeProfileService.findAllByListIdString(listEmp, user.getCompanyId()).collectList().flatMap(emps -> {
                            uniform.forEach(c -> {
                                c.setUomDTO(empMap.get(c.getUomId()));
                                c.setCreateByDTO(emps.stream().filter(e -> e.getId().toString().equals(c.getCreateBy())).findFirst().orElse(null));
                            });

                            return reportService.getUniformExport(uniform)
                                .handle((file, sink) -> {
                                    try {
                                        FileInputStream fileInputStream = new FileInputStream(file);
                                        InputStreamResource inputStreamResource = new InputStreamResource(fileInputStream);
                                        HttpHeaders headers = new HttpHeaders();
                                        String contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

                                        if (download) {
                                            headers.add(HttpHeaders.CONTENT_DISPOSITION,
                                                String.format("attachment; filename=\"%s\"", "uniform" + ".xlsx"));
                                        } else {
                                            headers.add(HttpHeaders.CONTENT_TYPE, contentType);
                                            headers.add("content-name", "uniform" + ".xlsx");
                                        }
                                        sink.next(new ResponseEntity<>(inputStreamResource, headers, HttpStatus.OK));
                                    } catch (IOException e) {
                                        sink.error(e);
                                    }
                                });
                        });
                    });

                });
        });
    }

}
