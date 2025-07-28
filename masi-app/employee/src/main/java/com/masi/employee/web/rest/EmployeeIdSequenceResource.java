package com.masi.employee.web.rest;

import com.carevn.masi.utils.SecurityUtils;
import com.masi.employee.domain.EmployeeIdSequence;
import com.masi.employee.domain.enumeration.Gender;
import com.masi.employee.repository.EmployeeIdSequenceRepository;
import com.masi.employee.service.EmployeeIdSequenceService;
import com.masi.employee.service.dto.EmployeeIdSequenceDTO;
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
 * REST controller for managing {@link com.masi.employee.domain.EmployeeIdSequence}.
 */
@RestController
@RequestMapping("/api/employee-id-sequences")
public class EmployeeIdSequenceResource {

    private static final Logger log = LoggerFactory.getLogger(EmployeeIdSequenceResource.class);

    private static final String ENTITY_NAME = "masiEmployeeEmployeeIdSequence";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EmployeeIdSequenceService employeeIdSequenceService;


    public EmployeeIdSequenceResource(
        EmployeeIdSequenceService employeeIdSequenceService
    ) {
        this.employeeIdSequenceService = employeeIdSequenceService;
    }


    @GetMapping(value = "")
    public Mono<List<EmployeeIdSequenceDTO>> getAllEmployeeIdSequences(
        @RequestParam(name = "gender", required = false) Gender gender
    ) {
        log.debug("REST request to get all EmployeeIdSequences");
        return
            SecurityUtils.getUserJWTDetail()
                .flatMap(user -> {
                    return employeeIdSequenceService.findAll(gender, user.getCompanyId()).collectList();
                });

    }

}
