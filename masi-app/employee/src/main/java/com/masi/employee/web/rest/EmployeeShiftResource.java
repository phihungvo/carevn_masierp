package com.masi.employee.web.rest;

import com.masi.employee.domain.criteria.EmployeeShiftCriteria;
import com.masi.employee.repository.EmployeeShiftRepository;
import com.masi.employee.service.EmployeeShiftService;
import com.masi.employee.service.dto.EmployeeShiftDTO;
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
 * REST controller for managing {@link com.masi.employee.domain.EmployeeShift}.
 */
@RestController
@RequestMapping("/api/employee-shifts")
public class EmployeeShiftResource {

    private static final Logger log = LoggerFactory.getLogger(EmployeeShiftResource.class);

    private static final String ENTITY_NAME = "masiEmployeeEmployeeShift";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EmployeeShiftService employeeShiftService;

    private final EmployeeShiftRepository employeeShiftRepository;

    public EmployeeShiftResource(EmployeeShiftService employeeShiftService, EmployeeShiftRepository employeeShiftRepository) {
        this.employeeShiftService = employeeShiftService;
        this.employeeShiftRepository = employeeShiftRepository;
    }

    /**
     * {@code POST  /employee-shifts} : Create a new employeeShift.
     *
     * @param employeeShiftDTO the employeeShiftDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new employeeShiftDTO, or with status {@code 400 (Bad Request)} if the employeeShift has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<EmployeeShiftDTO>> createEmployeeShift(@Valid @RequestBody EmployeeShiftDTO employeeShiftDTO)
        throws URISyntaxException {
        log.debug("REST request to save EmployeeShift : {}", employeeShiftDTO);
        if (employeeShiftDTO.getId() != null) {
            throw new BadRequestAlertException("A new employeeShift cannot already have an ID", ENTITY_NAME, "idexists");
        }
        employeeShiftDTO.setId(UUID.randomUUID());
        return employeeShiftService
            .save(employeeShiftDTO)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/employee-shifts/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /employee-shifts/:id} : Updates an existing employeeShift.
     *
     * @param id the id of the employeeShiftDTO to save.
     * @param employeeShiftDTO the employeeShiftDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated employeeShiftDTO,
     * or with status {@code 400 (Bad Request)} if the employeeShiftDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the employeeShiftDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<EmployeeShiftDTO>> updateEmployeeShift(
        @PathVariable(value = "id", required = false) final UUID id,
        @Valid @RequestBody EmployeeShiftDTO employeeShiftDTO
    ) throws URISyntaxException {
        log.debug("REST request to update EmployeeShift : {}, {}", id, employeeShiftDTO);
        if (employeeShiftDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, employeeShiftDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return employeeShiftRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return employeeShiftService
                    .update(employeeShiftDTO)
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
     * {@code PATCH  /employee-shifts/:id} : Partial updates given fields of an existing employeeShift, field will ignore if it is null
     *
     * @param id the id of the employeeShiftDTO to save.
     * @param employeeShiftDTO the employeeShiftDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated employeeShiftDTO,
     * or with status {@code 400 (Bad Request)} if the employeeShiftDTO is not valid,
     * or with status {@code 404 (Not Found)} if the employeeShiftDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the employeeShiftDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<EmployeeShiftDTO>> partialUpdateEmployeeShift(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody EmployeeShiftDTO employeeShiftDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update EmployeeShift partially : {}, {}", id, employeeShiftDTO);
        if (employeeShiftDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, employeeShiftDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return employeeShiftRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<EmployeeShiftDTO> result = employeeShiftService.partialUpdate(employeeShiftDTO);

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
     * {@code GET  /employee-shifts} : get all the employeeShifts.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of employeeShifts in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<EmployeeShiftDTO>>> getAllEmployeeShifts(
        EmployeeShiftCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get EmployeeShifts by criteria: {}", criteria);
        return employeeShiftService
            .countByCriteria(criteria)
            .zipWith(employeeShiftService.findByCriteria(criteria, pageable).collectList())
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
     * {@code GET  /employee-shifts/count} : count all the employeeShifts.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public Mono<ResponseEntity<Long>> countEmployeeShifts(EmployeeShiftCriteria criteria) {
        log.debug("REST request to count EmployeeShifts by criteria: {}", criteria);
        return employeeShiftService.countByCriteria(criteria).map(count -> ResponseEntity.status(HttpStatus.OK).body(count));
    }

    /**
     * {@code GET  /employee-shifts/:id} : get the "id" employeeShift.
     *
     * @param id the id of the employeeShiftDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the employeeShiftDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<EmployeeShiftDTO>> getEmployeeShift(@PathVariable("id") UUID id) {
        log.debug("REST request to get EmployeeShift : {}", id);
        Mono<EmployeeShiftDTO> employeeShiftDTO = employeeShiftService.findOne(id);
        return ResponseUtil.wrapOrNotFound(employeeShiftDTO);
    }

    /**
     * {@code DELETE  /employee-shifts/:id} : delete the "id" employeeShift.
     *
     * @param id the id of the employeeShiftDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteEmployeeShift(@PathVariable("id") UUID id) {
        log.debug("REST request to delete EmployeeShift : {}", id);
        return employeeShiftService
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
