package com.masi.employee.web.rest;

import com.masi.employee.repository.ConfirmLeaveRepository;
import com.masi.employee.service.ConfirmLeaveService;
import com.masi.employee.service.dto.ConfirmLeaveDTO;
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
 * REST controller for managing {@link com.masi.employee.domain.ConfirmLeave}.
 */
@RestController
@RequestMapping("/api/confirm-leaves")
public class ConfirmLeaveResource {

    private static final Logger log = LoggerFactory.getLogger(ConfirmLeaveResource.class);

    private static final String ENTITY_NAME = "masiEmployeeConfirmLeave";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ConfirmLeaveService confirmLeaveService;

    private final ConfirmLeaveRepository confirmLeaveRepository;

    public ConfirmLeaveResource(ConfirmLeaveService confirmLeaveService,
            ConfirmLeaveRepository confirmLeaveRepository) {
        this.confirmLeaveService = confirmLeaveService;
        this.confirmLeaveRepository = confirmLeaveRepository;
    }

    /**
     * {@code POST  /confirm-leaves} : Create a new confirmLeave.
     *
     * @param confirmLeaveDTO the confirmLeaveDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with
     *         body the new confirmLeaveDTO, or with status
     *         {@code 400 (Bad Request)} if the confirmLeave has already an ID.
     */
    @PostMapping("")
    public Mono<ResponseEntity<ConfirmLeaveDTO>> createConfirmLeave(
            @Valid @RequestBody ConfirmLeaveDTO confirmLeaveDTO) {
        log.debug("REST request to save ConfirmLeave : {}", confirmLeaveDTO);

        return confirmLeaveService
                .save(confirmLeaveDTO)
                .handle((result, sink) -> {
                    try {
                        sink.next(ResponseEntity.created(new URI("/api/confirm-leaves/" + result.getId()))
                                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME,
                                        result.getId().toString()))
                                .body(result));
                    } catch (URISyntaxException e) {
                        sink.error(new RuntimeException(e));
                    }
                });
    }

    @GetMapping("{id}")
    public Mono<ResponseEntity<ConfirmLeaveDTO>> getConfirmLeave(@PathVariable UUID id) {
        log.debug("REST request to get ConfirmLeave : {}", id);
        Mono<ConfirmLeaveDTO> confirmLeaveDTO = confirmLeaveService.findOne(id);
        return ResponseUtil.wrapOrNotFound(confirmLeaveDTO);
    }
}
