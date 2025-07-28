package com.masi.employee.web.rest;

import com.masi.employee.domain.criteria.EmployeeShiftDetailCriteria;
import com.masi.employee.repository.EmployeeShiftDetailRepository;
import com.masi.employee.service.EmployeeShiftDetailService;
import com.masi.employee.service.dto.EmployeeShiftDetailDTO;
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
 * REST controller for managing {@link com.masi.employee.domain.EmployeeShiftDetail}.
 */
@RestController
@RequestMapping("/api/employee-shift-details")
public class EmployeeShiftDetailResource {

    private static final Logger log = LoggerFactory.getLogger(EmployeeShiftDetailResource.class);

    private static final String ENTITY_NAME = "masiEmployeeEmployeeShiftDetail";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EmployeeShiftDetailService employeeShiftDetailService;

    private final EmployeeShiftDetailRepository employeeShiftDetailRepository;

    public EmployeeShiftDetailResource(
        EmployeeShiftDetailService employeeShiftDetailService,
        EmployeeShiftDetailRepository employeeShiftDetailRepository
    ) {
        this.employeeShiftDetailService = employeeShiftDetailService;
        this.employeeShiftDetailRepository = employeeShiftDetailRepository;
    }

    /**
     * {@code POST  /employee-shift-details} : Create a new employeeShiftDetail.
     *
     * @param employeeShiftDetailDTO the employeeShiftDetailDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new employeeShiftDetailDTO, or with status {@code 400 (Bad Request)} if the employeeShiftDetail has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<EmployeeShiftDetailDTO>> createEmployeeShiftDetail(
        @Valid @RequestBody EmployeeShiftDetailDTO employeeShiftDetailDTO
    ) throws URISyntaxException {
        log.debug("REST request to save EmployeeShiftDetail : {}", employeeShiftDetailDTO);
        if (employeeShiftDetailDTO.getId() != null) {
            throw new BadRequestAlertException("A new employeeShiftDetail cannot already have an ID", ENTITY_NAME, "idexists");
        }
        employeeShiftDetailDTO.setId(UUID.randomUUID());
        return employeeShiftDetailService
            .save(employeeShiftDetailDTO)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/employee-shift-details/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /employee-shift-details/:id} : Updates an existing employeeShiftDetail.
     *
     * @param id the id of the employeeShiftDetailDTO to save.
     * @param employeeShiftDetailDTO the employeeShiftDetailDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated employeeShiftDetailDTO,
     * or with status {@code 400 (Bad Request)} if the employeeShiftDetailDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the employeeShiftDetailDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<EmployeeShiftDetailDTO>> updateEmployeeShiftDetail(
        @PathVariable(value = "id", required = false) final UUID id,
        @Valid @RequestBody EmployeeShiftDetailDTO employeeShiftDetailDTO
    ) throws URISyntaxException {
        log.debug("REST request to update EmployeeShiftDetail : {}, {}", id, employeeShiftDetailDTO);
        if (employeeShiftDetailDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, employeeShiftDetailDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return employeeShiftDetailRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return employeeShiftDetailService
                    .update(employeeShiftDetailDTO)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(
                        result ->
                            ResponseEntity.ok()
                                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                                .body(result)
                    );
            });
    }

    /**
     * {@code PATCH  /employee-shift-details/:id} : Partial updates given fields of an existing employeeShiftDetail, field will ignore if it is null
     *
     * @param id the id of the employeeShiftDetailDTO to save.
     * @param employeeShiftDetailDTO the employeeShiftDetailDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated employeeShiftDetailDTO,
     * or with status {@code 400 (Bad Request)} if the employeeShiftDetailDTO is not valid,
     * or with status {@code 404 (Not Found)} if the employeeShiftDetailDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the employeeShiftDetailDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<EmployeeShiftDetailDTO>> partialUpdateEmployeeShiftDetail(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody EmployeeShiftDetailDTO employeeShiftDetailDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update EmployeeShiftDetail partially : {}, {}", id, employeeShiftDetailDTO);
        if (employeeShiftDetailDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, employeeShiftDetailDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return employeeShiftDetailRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<EmployeeShiftDetailDTO> result = employeeShiftDetailService.partialUpdate(employeeShiftDetailDTO);

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

    /**
     * {@code GET  /employee-shift-details} : get all the employeeShiftDetails.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of employeeShiftDetails in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<EmployeeShiftDetailDTO>>> getAllEmployeeShiftDetails(
        EmployeeShiftDetailCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get EmployeeShiftDetails by criteria: {}", criteria);
        return employeeShiftDetailService
            .countByCriteria(criteria)
            .zipWith(employeeShiftDetailService.findByCriteria(criteria, pageable).collectList())
            .map(
                countWithEntities ->
                    ResponseEntity.ok()
                        .headers(
                            PaginationUtil.generatePaginationHttpHeaders(
                                ForwardedHeaderUtils.adaptFromForwardedHeaders(request.getURI(), request.getHeaders()),
                                new PageImpl<>(countWithEntities.getT2(), pageable, countWithEntities.getT1())
                            )
                        )
                        .body(countWithEntities.getT2())
            );
    }

    /**
     * {@code GET  /employee-shift-details/count} : count all the employeeShiftDetails.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public Mono<ResponseEntity<Long>> countEmployeeShiftDetails(EmployeeShiftDetailCriteria criteria) {
        log.debug("REST request to count EmployeeShiftDetails by criteria: {}", criteria);
        return employeeShiftDetailService.countByCriteria(criteria).map(count -> ResponseEntity.status(HttpStatus.OK).body(count));
    }

    /**
     * {@code GET  /employee-shift-details/:id} : get the "id" employeeShiftDetail.
     *
     * @param id the id of the employeeShiftDetailDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the employeeShiftDetailDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<EmployeeShiftDetailDTO>> getEmployeeShiftDetail(@PathVariable("id") UUID id) {
        log.debug("REST request to get EmployeeShiftDetail : {}", id);
        Mono<EmployeeShiftDetailDTO> employeeShiftDetailDTO = employeeShiftDetailService.findOne(id);
        return ResponseUtil.wrapOrNotFound(employeeShiftDetailDTO);
    }

    /**
     * {@code DELETE  /employee-shift-details/:id} : delete the "id" employeeShiftDetail.
     *
     * @param id the id of the employeeShiftDetailDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteEmployeeShiftDetail(@PathVariable("id") UUID id) {
        log.debug("REST request to delete EmployeeShiftDetail : {}", id);
        return employeeShiftDetailService
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
