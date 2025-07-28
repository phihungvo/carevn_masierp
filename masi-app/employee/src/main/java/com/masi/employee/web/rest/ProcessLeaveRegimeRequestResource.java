package com.masi.employee.web.rest;

import com.carevn.masi.dto.UserJWTDetail;
import com.carevn.masi.utils.SecurityUtils;
import com.carevn.masi.utils.Utilities;
import com.masi.employee.repository.ProcessLeaveRegimeRequestRepository;
import com.masi.employee.service.EmployeeService;
import com.masi.employee.service.ProcessLeaveRegimeRequestService;
import com.masi.employee.service.dto.ProcessLeaveRegimeRequestUpdateApproversDTO;

import io.swagger.v3.oas.annotations.Operation;

import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

/**
 * REST controller for managing
 * {@link com.masi.employee.domain.ProcessLeaveRegimeRequest}.
 */
@RestController
@RequestMapping("/api/process-leave-regime-requests")
public class ProcessLeaveRegimeRequestResource {

    private static final Logger log = LoggerFactory.getLogger(ProcessLeaveRegimeRequestResource.class);

    private static final String ENTITY_NAME = "masiEmployeeProcessLeaveRegimeRequest";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProcessLeaveRegimeRequestService processLeaveRegimeRequestService;

    private final EmployeeService employeeService;

    private final ProcessLeaveRegimeRequestRepository processLeaveRegimeRequestRepository;

    public ProcessLeaveRegimeRequestResource(
        ProcessLeaveRegimeRequestService processLeaveRegimeRequestService,
        ProcessLeaveRegimeRequestRepository processLeaveRegimeRequestRepository,
        EmployeeService employeeService) {
        this.processLeaveRegimeRequestService = processLeaveRegimeRequestService;
        this.employeeService = employeeService;
        this.processLeaveRegimeRequestRepository = processLeaveRegimeRequestRepository;
    }

    // /**
    // * {@code POST /process-leave-regime-requests} : Create a new
    // processLeaveRegimeRequest.
    // *
    // * @param processLeaveRegimeRequestDTO the processLeaveRegimeRequestDTO to
    // create.
    // * @return the {@link ResponseEntity} with status {@code 201 (Created)} and
    // with body the new processLeaveRegimeRequestDTO, or with status {@code 400
    // (Bad Request)} if the processLeaveRegimeRequest has already an ID.
    // * @throws URISyntaxException if the Location URI syntax is incorrect.
    // */
    // @PostMapping("")
    // public Mono<ResponseEntity<ProcessLeaveRegimeRequestDTO>>
    // createProcessLeaveRegimeRequest(
    // @RequestBody ProcessLeaveRegimeRequestDTO processLeaveRegimeRequestDTO
    // ) throws URISyntaxException {
    // log.debug("REST request to save ProcessLeaveRegimeRequest : {}",
    // processLeaveRegimeRequestDTO);
    // if (processLeaveRegimeRequestDTO.getId() != null) {
    // throw new BadRequestAlertException("A new processLeaveRegimeRequest cannot
    // already have an ID", ENTITY_NAME, "idexists");
    // }
    // processLeaveRegimeRequestDTO.setId(UUID.randomUUID());
    // return processLeaveRegimeRequestService
    // .save(processLeaveRegimeRequestDTO)
    // .map(result -> {
    // try {
    // return ResponseEntity.created(new URI("/api/process-leave-regime-requests/" +
    // result.getId()))
    // .headers(HeaderUtil.createEntityCreationAlert(applicationName, true,
    // ENTITY_NAME, result.getId().toString()))
    // .body(result);
    // } catch (URISyntaxException e) {
    // throw new RuntimeException(e);
    // }
    // });
    // }

    // /**
    // * {@code PUT /process-leave-regime-requests/:id} : Updates an existing
    // processLeaveRegimeRequest.
    // *
    // * @param id the id of the processLeaveRegimeRequestDTO to save.
    // * @param processLeaveRegimeRequestDTO the processLeaveRegimeRequestDTO to
    // update.
    // * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with
    // body the updated processLeaveRegimeRequestDTO,
    // * or with status {@code 400 (Bad Request)} if the
    // processLeaveRegimeRequestDTO is not valid,
    // * or with status {@code 500 (Internal Server Error)} if the
    // processLeaveRegimeRequestDTO couldn't be updated.
    // * @throws URISyntaxException if the Location URI syntax is incorrect.
    // */
    // @PutMapping("/{id}")
    // public Mono<ResponseEntity<ProcessLeaveRegimeRequestDTO>>
    // updateProcessLeaveRegimeRequest(
    // @PathVariable(value = "id", required = false) final UUID id,
    // @RequestBody ProcessLeaveRegimeRequestDTO processLeaveRegimeRequestDTO
    // ) throws URISyntaxException {
    // log.debug("REST request to update ProcessLeaveRegimeRequest : {}, {}", id,
    // processLeaveRegimeRequestDTO);
    // if (processLeaveRegimeRequestDTO.getId() == null) {
    // throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
    // }
    // if (!Objects.equals(id, processLeaveRegimeRequestDTO.getId())) {
    // throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
    // }

    // return processLeaveRegimeRequestRepository
    // .existsById(id)
    // .flatMap(exists -> {
    // if (!exists) {
    // return Mono.error(new BadRequestAlertException("Entity not found",
    // ENTITY_NAME, "idnotfound"));
    // }

    // return processLeaveRegimeRequestService
    // .update(processLeaveRegimeRequestDTO)
    // .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
    // .map(
    // result ->
    // ResponseEntity.ok()
    // .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true,
    // ENTITY_NAME, result.getId().toString()))
    // .body(result)
    // );
    // });
    // }

    /**
     * {@code PATCH  /process-leave-regime-requests/:id} : Partial updates given
     * fields of an existing processLeaveRegimeRequest, field will ignore if it is
     * null
     *
     * @param leaveRegimeRequestId                           the id of the
     *                                     processLeaveRegimeRequestDTO to save.
     * @param dto the processLeaveRegimeRequestDTO to
     *                                     update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     * the updated processLeaveRegimeRequestDTO,
     * or with status {@code 400 (Bad Request)} if the
     * processLeaveRegimeRequestDTO is not valid,
     * or with status {@code 404 (Not Found)} if the
     * processLeaveRegimeRequestDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the
     * processLeaveRegimeRequestDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Operation(summary = "update list approvers for leave regime request")
    @PatchMapping(value = "/{leaveRegimeRequestId}", consumes = {"application/json", "application/merge-patch+json"})
    public Mono<ResponseEntity<Map<String, Object>>> partialUpdateProcessLeaveRegimeRequest(
        @PathVariable(value = "leaveRegimeRequestId", required = false) final UUID leaveRegimeRequestId,
        @RequestBody ProcessLeaveRegimeRequestUpdateApproversDTO dto)
        throws URISyntaxException {
        log.debug("REST request to partial update ProcessLeaveRegimeRequest partially : {}, {}", leaveRegimeRequestId,
            dto);
        dto.setApproverIds(new HashSet<>(dto.getApproverIds()));
        return SecurityUtils.getUserJWTDetail()
            .flatMap(user -> handleUpdate(leaveRegimeRequestId, dto, user));

    }

    private Mono<? extends ResponseEntity<Map<String, Object>>> handleUpdate(final UUID leaveRegimeRequestId,
                                                                             ProcessLeaveRegimeRequestUpdateApproversDTO processLeaveRegimeRequestUpdateApproversDTO,
                                                                             UserJWTDetail user) {
        return employeeService.areAllEmployeesValid(processLeaveRegimeRequestUpdateApproversDTO.getApproverIds())
            .flatMap(valid -> {
                if (!valid) {
                    return Mono.just(
                        ResponseEntity.badRequest().body(Utilities.generateResponse("INVALID_APPROVER", null)));
                }
                return processLeaveRegimeRequestService.markAsDeleted(leaveRegimeRequestId, user.getUserId())
                    .then(processLeaveRegimeRequestService
                        .saveAllRequests(
                            processLeaveRegimeRequestUpdateApproversDTO.toListDTO(leaveRegimeRequestId,
                                user))
                        .collectList().flatMap(item -> {
                            return Mono.just(
                                ResponseEntity.ok().body(Utilities.generateResponse("SUCCESS", item)));
                        }));
            });
    }

    // /**
    // * {@code GET /process-leave-regime-requests} : get all the
    // processLeaveRegimeRequests.
    // *
    // * @param pageable the pagination information.
    // * @param request a {@link ServerHttpRequest} request.
    // * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the
    // list of processLeaveRegimeRequests in body.
    // */
    // @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    // public Mono<ResponseEntity<List<ProcessLeaveRegimeRequestDTO>>>
    // getAllProcessLeaveRegimeRequests(
    // @org.springdoc.core.annotations.ParameterObject Pageable pageable,
    // ServerHttpRequest request
    // ) {
    // log.debug("REST request to get a page of ProcessLeaveRegimeRequests");
    // return processLeaveRegimeRequestService
    // .countAll()
    // .zipWith(processLeaveRegimeRequestService.findAll(pageable).collectList())
    // .map(
    // countWithEntities ->
    // ResponseEntity.ok()
    // .headers(
    // PaginationUtil.generatePaginationHttpHeaders(
    // ForwardedHeaderUtils.adaptFromForwardedHeaders(request.getURI(),
    // request.getHeaders()),
    // new PageImpl<>(countWithEntities.getT2(), pageable,
    // countWithEntities.getT1())
    // )
    // )
    // .body(countWithEntities.getT2())
    // );
    // }

    // /**
    // * {@code GET /process-leave-regime-requests/:id} : get the "id"
    // processLeaveRegimeRequest.
    // *
    // * @param id the id of the processLeaveRegimeRequestDTO to retrieve.
    // * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with
    // body the processLeaveRegimeRequestDTO, or with status {@code 404 (Not
    // Found)}.
    // */
    // @GetMapping("/{id}")
    // public Mono<ResponseEntity<ProcessLeaveRegimeRequestDTO>>
    // getProcessLeaveRegimeRequest(@PathVariable("id") UUID id) {
    // log.debug("REST request to get ProcessLeaveRegimeRequest : {}", id);
    // Mono<ProcessLeaveRegimeRequestDTO> processLeaveRegimeRequestDTO =
    // processLeaveRegimeRequestService.findOne(id);
    // return ResponseUtil.wrapOrNotFound(processLeaveRegimeRequestDTO);
    // }

    // /**
    // * {@code DELETE /process-leave-regime-requests/:id} : delete the "id"
    // processLeaveRegimeRequest.
    // *
    // * @param id the id of the processLeaveRegimeRequestDTO to delete.
    // * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
    // */
    // @DeleteMapping("/{id}")
    // public Mono<ResponseEntity<Void>>
    // deleteProcessLeaveRegimeRequest(@PathVariable("id") UUID id) {
    // log.debug("REST request to delete ProcessLeaveRegimeRequest : {}", id);
    // return processLeaveRegimeRequestService
    // .delete(id)
    // .then(
    // Mono.just(
    // ResponseEntity.noContent()
    // .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true,
    // ENTITY_NAME, id.toString()))
    // .build()
    // )
    // );
    // }
}
