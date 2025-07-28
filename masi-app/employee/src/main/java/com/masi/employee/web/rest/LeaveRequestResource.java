package com.masi.employee.web.rest;

import com.carevn.masi.dto.ApiResponse;
import com.masi.employee.domain.LeaveRequest;
import com.masi.employee.domain.enumeration.LeaveRequestDayType;
import com.masi.employee.repository.LeaveRequestRepository;
import com.masi.employee.service.LeaveRequestService;
import com.masi.employee.service.dto.LeaveRequestDTO;
import com.masi.employee.service.dto.LeaveRequestQuery;
import com.masi.employee.service.dto.LeaveRequestReviewDTO;
import com.masi.employee.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cglib.core.Local;
import org.springframework.data.domain.Pageable;
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
 * REST controller for managing {@link com.masi.employee.domain.LeaveRequest}.
 */
@RestController
@RequestMapping("/api/leave-request")
public class LeaveRequestResource {

    private final Logger log = LoggerFactory.getLogger(LeaveRequestResource.class);

    private static final String ENTITY_NAME = "masiEmployeeLeaveRequest";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LeaveRequestService leaveRequestService;

    public LeaveRequestResource(LeaveRequestService leaveRequestService) {
        this.leaveRequestService = leaveRequestService;
    }

    /**
     * {@code POST  /leave-requests} : Create a new leaveRequest.
     *
     * @param leaveRequestDTO the leaveRequestDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new leaveRequestDTO, or with status {@code 400 (Bad Request)} if the leaveRequest has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<LeaveRequestDTO>> createLeaveRequest(@Valid @RequestBody LeaveRequestDTO leaveRequestDTO)
        throws URISyntaxException {
        log.debug("REST request to save LeaveRequest : {}", leaveRequestDTO);
        if (leaveRequestDTO.getId() != null) {
            throw new BadRequestAlertException("A new leaveRequest cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return leaveRequestService
            .save(leaveRequestDTO)
            .handle((result, sink) -> {
                try {
                    sink.next(ResponseEntity.created(new URI("/api/leave-requests/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result));
                } catch (URISyntaxException e) {
                    sink.error(new RuntimeException(e));
                }
            });
    }

    //    java.time.LocalDate fromDate, LocalDate toDate, String workspaceType, String company
    @GetMapping("/count-day-off")
    public Mono<Map<String, Float>> countDayOff(@RequestParam("fromDate") @NotNull LocalDate fromDate,
                                                @RequestParam("toDate") @NotNull LocalDate toDate,
                                                @RequestParam("employeeId") @NotNull UUID employeeId,
                                                @RequestParam(value = "type", required = false, defaultValue = "FULL_DAY") String type
    ) {
        if ("HALF_DAY".equals(type)) {
            return Mono.just(Map.of("count", 0.5f));
        }
        return leaveRequestService.countDayOff(fromDate, toDate, employeeId).flatMap(count -> Mono.just(Map.of("count", count)));
    }

    @GetMapping("/count-day-end-off")
    public Mono<Map<String, LocalDateTime>> countDayOff(@RequestParam("fromDate") @NotNull LocalDate fromDate,
                                                        @RequestParam("totalDay") @NotNull Float totalDay,
                                                        @RequestParam("employeeId") @NotNull UUID employeeId,
                                                        @RequestParam(value = "type", required = false, defaultValue = "FULL_DAY") String type
    ) {
        return leaveRequestService.calculateDateEnd(fromDate, totalDay, employeeId).flatMap(count -> Mono.just(Map.of("dateEnd", count)));
    }

    /**
     * {@code GET  /leave-requests} : get all the leaveRequests as a stream.
     *
     * @return the {@link Flux} of leaveRequests.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Mono<ApiResponse<LeaveRequest>> getAllLeaveRequestsAsStream(
        @ParameterObject LeaveRequestQuery query,
        @ParameterObject Pageable pageable) {
        log.debug("REST request to get all LeaveRequests as a stream");
        return leaveRequestService.countAll(query)
            .zipWith(leaveRequestService.findAll(pageable, query).collectList())
            .map(data -> new ApiResponse<>(data.getT2(), data.getT1()));
    }

    /**
     * {@code GET  /leave-requests/:id} : get the "id" leaveRequest.
     *
     * @param id the id of the leaveRequestDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the leaveRequestDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<LeaveRequestDTO>> getLeaveRequest(@PathVariable("id") UUID id) {
        log.debug("REST request to get LeaveRequest : {}", id);
        Mono<LeaveRequestDTO> leaveRequest = leaveRequestService.findOne(id);
        return ResponseUtil.wrapOrNotFound(leaveRequest);
    }

    @PatchMapping(value = "/{id}/cancel")
    public Mono<Map<String, Object>> cancelLeaveRequest(@PathVariable("id") UUID id) {
        log.debug("REST request to cancel LeaveRequest : {}", id);
        return leaveRequestService.cancelLeaveRequest(id);
    }

    @DeleteMapping(value = "/{id}")
    public Mono<Map<String, Object>> deleteLeaveRequest(@PathVariable("id") UUID id) {
        log.debug("REST request to delete LeaveRequest : {}", id);
        return leaveRequestService.delete(id);
    }
}
