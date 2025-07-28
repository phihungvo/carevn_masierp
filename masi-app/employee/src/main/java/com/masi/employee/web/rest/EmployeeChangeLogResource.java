package com.masi.employee.web.rest;

import com.carevn.masi.dto.ApiResponse;
import com.masi.employee.repository.EmployeeChangeLogRepository;
import com.masi.employee.service.EmployeeChangeLogService;
import com.masi.employee.service.dto.EmployeeChangeLogDTO;
import com.masi.employee.web.rest.errors.BadRequestAlertException;

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
 * REST controller for managing {@link com.masi.employee.domain.EmployeeChangeLog}.
 */
@RestController
@RequestMapping("/api/employee-change-logs")
public class EmployeeChangeLogResource {

    private static final Logger log = LoggerFactory.getLogger(EmployeeChangeLogResource.class);

    private static final String ENTITY_NAME = "masiEmployeeEmployeeChangeLog";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EmployeeChangeLogService employeeChangeLogService;


    public EmployeeChangeLogResource(
        EmployeeChangeLogService employeeChangeLogService
    ) {
        this.employeeChangeLogService = employeeChangeLogService;
    }

    /**
     * {@code GET  /employee-change-logs} : get all the employeeChangeLogs.
     *
     * @param pageable the pagination information.
     * @param request  a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of employeeChangeLogs in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<ApiResponse<EmployeeChangeLogDTO>>> getAllEmployeeChangeLogs(
        @RequestParam(value = "employeeId", required = true) UUID employeeId,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of EmployeeChangeLogs");
        return employeeChangeLogService
            .countAllByEmployeeId(employeeId)
            .zipWith(employeeChangeLogService.findAllByEmployeeId(employeeId, pageable).collectList())
            .map(
                tuple -> {
                    List<EmployeeChangeLogDTO> employeeChangeLogDTOList = tuple.getT2();
                    return ResponseEntity.ok()
                        .body(new ApiResponse<>(employeeChangeLogDTOList, tuple.getT1()));
                }
            );
    }


    @GetMapping("/{id}")
    public Mono<ResponseEntity<EmployeeChangeLogDTO>> getEmployeeChangeLog(@PathVariable("id") UUID id) {
        log.debug("REST request to get EmployeeChangeLog : {}", id);
        Mono<EmployeeChangeLogDTO> employeeChangeLogDTO = employeeChangeLogService.findById(id);
        return ResponseUtil.wrapOrNotFound(employeeChangeLogDTO);
    }


}
