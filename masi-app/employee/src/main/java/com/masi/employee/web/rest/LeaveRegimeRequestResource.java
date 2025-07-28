package com.masi.employee.web.rest;

import com.carevn.masi.dto.ApiResponse;
import com.carevn.masi.dto.UserJWTDetail;
import com.carevn.masi.utils.Utilities;
import com.carevn.masi.utils.SecurityUtils;
import com.masi.employee.domain.LeaveRegimeRequest;
import com.masi.employee.domain.enumeration.LeaveRegimeRequestStatus;
import com.masi.employee.repository.LeaveRegimeRequestRepository;
import com.masi.employee.service.EmployeeService;
import com.masi.employee.service.LeaveRegimeRequestService;
import com.masi.employee.service.dto.LeaveRegimeRequestCreateDTO;
import com.masi.employee.service.dto.LeaveRegimeRequestDTO;
import com.masi.employee.service.dto.LeaveRegimeRequestGetListDTO;
import com.masi.employee.service.dto.LeaveRegimeRequestProcessDTO;
import com.masi.employee.service.dto.LeaveRegimeRequestUpdateDTO;
import com.masi.employee.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.annotations.ParameterObject;
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
 * REST controller for managing
 * {@link LeaveRegimeRequest}.
 */
@RestController
@RequestMapping("/api/leave-regime-requests")
public class LeaveRegimeRequestResource {

    private static final Logger log = LoggerFactory.getLogger(LeaveRegimeRequestResource.class);

    private static final String ENTITY_NAME = "masiEmployeeLeaveRegimeRequest";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LeaveRegimeRequestService leaveRegimeRequestService;

    private final LeaveRegimeRequestRepository leaveRegimeRequestRepository;

    public LeaveRegimeRequestResource(
        LeaveRegimeRequestService leaveRegimeRequestService,
        LeaveRegimeRequestRepository leaveRegimeRequestRepository) {
        this.leaveRegimeRequestService = leaveRegimeRequestService;
        this.leaveRegimeRequestRepository = leaveRegimeRequestRepository;
    }

    @PostMapping("")
    public Mono<ResponseEntity<Map<String, Object>>> createLeaveRegimeRequest(
        @Valid @RequestBody LeaveRegimeRequestCreateDTO leaveRegimeRequestCreateDTO)  {
        log.debug("REST request to save LeaveRegimeRequest : {}", leaveRegimeRequestCreateDTO);
        if (leaveRegimeRequestCreateDTO.getApproverIds().size() > 4) {
            return Mono.error(new BadRequestAlertException("Approver max 4", ENTITY_NAME,
                "INVALID_APPROVER_NUMBER"));
        }
        var leaveRegimeRequestDTO = leaveRegimeRequestCreateDTO.toDto();

        return validateStartEndDate(leaveRegimeRequestCreateDTO.getLastWorkDate(),
            leaveRegimeRequestCreateDTO.getReturnWorkDate()).flatMap(isValid -> {
            if (!isValid) {
                return Mono.error(new BadRequestAlertException("Invalid start date and end date", ENTITY_NAME,
                    "invaliddate"));
            }
            return SecurityUtils.getUserJWTDetail().flatMap(user -> {
                if (Objects.isNull(leaveRegimeRequestCreateDTO.getEmployeeId())) {
                    leaveRegimeRequestDTO.setEmployeeId(user.getUserId());
                }
                return leaveRegimeRequestService
                    .createLeaveRegimeRequest(leaveRegimeRequestDTO, leaveRegimeRequestCreateDTO.getApproverIds())
                    .handle((result, sink) -> {
                        try {
                            sink.next(ResponseEntity
                                .created(new URI("/api/leave-regime-requests/" + result.getId()))
                                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true,
                                    ENTITY_NAME,
                                    result.getId().toString()))
                                .body(Utilities.generateResponse("SUCCESS", result)));
                        } catch (URISyntaxException e) {
                            sink.error(new RuntimeException(e));
                        }
                    });
            });
        });

    }

    @PatchMapping(value = "/{id}", consumes = {"application/json", "application/merge-patch+json"})
    public Mono<ResponseEntity<LeaveRegimeRequestDTO>> partialUpdateLeaveRegimeRequest(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody LeaveRegimeRequestUpdateDTO leaveRegimeRequestUpdateDTO) throws URISyntaxException {
        var leaveRegimeRequestDTO = leaveRegimeRequestUpdateDTO.toDto(id);

        // Validate ID
        if (id == null || !id.equals(leaveRegimeRequestDTO.getId())) {
            return Mono.error(
                new BadRequestAlertException("Invalid ID", ENTITY_NAME, id == null ? "idnull" : "idinvalid"));
        }

        // Validate dates
        return validateStartEndDate(leaveRegimeRequestDTO.getLastWorkDate(), leaveRegimeRequestDTO.getReturnWorkDate())
            .flatMap(isValid -> {
                if (!isValid) {
                    return Mono.error(new BadRequestAlertException("Invalid start date and end date", ENTITY_NAME,
                        "invaliddate"));
                }
                return updateLeaveRegimeRequest(id, leaveRegimeRequestDTO,
                    leaveRegimeRequestUpdateDTO);
            });
    }


    @PatchMapping(value = "/{id}/process", consumes = {"application/json", "application/merge-patch+json"})
    public Mono<ResponseEntity<Map<String, Object>>> processLeaveRegimeRequest(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody LeaveRegimeRequestProcessDTO leaveRegimeRequestProcessDTO) throws URISyntaxException {
        log.debug("REST request to partial update LeaveRegimeRequest partially : {}", id);
        LeaveRegimeRequestDTO leaveRegimeRequestDTO = new LeaveRegimeRequestDTO();
        leaveRegimeRequestDTO.setId(id);
        leaveRegimeRequestDTO
            .setStatus(LeaveRegimeRequestStatus.valueOf(leaveRegimeRequestProcessDTO.getStatus().toString()));
        leaveRegimeRequestDTO.setUpdatedAt(ZonedDateTime.now());
        return SecurityUtils.getUserJWTDetail().flatMap(user -> {
            leaveRegimeRequestDTO.setUpdatedBy(user.getUserId());
            leaveRegimeRequestDTO.setUpdatedAt(ZonedDateTime.now());
            return leaveRegimeRequestService.processLeaveRequestRegime(leaveRegimeRequestDTO,
                    leaveRegimeRequestProcessDTO)
                .handle((result, sink) -> {
                    try {
                        sink.next(ResponseEntity
                            .ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME,
                                result.getId().toString()))
                            .body(Utilities.generateResponse("SUCCESS", result)));
                    } catch (RuntimeException e) {
                        sink.error(new RuntimeException(e));
                    }
                });
        });
    }

    private Mono<ResponseEntity<LeaveRegimeRequestDTO>> updateLeaveRegimeRequest(UUID id,
                                                                                 LeaveRegimeRequestDTO leaveRegimeRequestDTO, LeaveRegimeRequestUpdateDTO leaveRegimeRequestUpdateDTO) {
        return SecurityUtils.getUserJWTDetail()
            .flatMap(user -> {
                leaveRegimeRequestDTO.setUpdatedAt(ZonedDateTime.now());
                leaveRegimeRequestDTO.setUpdatedBy(user.getUserId());
                return leaveRegimeRequestRepository.findFirstByIsDeletedIsFalseAndId(id)
                    .flatMap(exists -> leaveRegimeRequestService.partialUpdate(leaveRegimeRequestDTO,
                        leaveRegimeRequestUpdateDTO))
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(res -> ResponseEntity.ok()
                        .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME,
                            res.getId().toString()))
                        .body(res));
            });
    }

    public Mono<Boolean> validateStartEndDate(ZonedDateTime startDate, ZonedDateTime endDate) {
        return Mono.just(startDate.isBefore(endDate));
    }

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<ApiResponse<LeaveRegimeRequestDTO>>> getAllLeaveRegimeRequests(
        @ParameterObject Pageable pageable,
        @ParameterObject LeaveRegimeRequestGetListDTO filter) {
        log.debug("REST request to get a page of LeaveRegimeRequests");
        return SecurityUtils.getUserJWTDetail().flatMap(user -> {
            filter.setCompanyId(String.valueOf(user.getCompanyId()));
            filter.setDepartment(user.getGroupId());
            filter.setEmployeeId(user.getUserId());
            return leaveRegimeRequestService
                .countAllWithQuery(filter, user.getAuthorities())
                .flatMap(total -> leaveRegimeRequestService
                    .findAllWithQuery(pageable, filter, user.getAuthorities()).collectList()
                    .map(
                        leave -> ResponseEntity.ok().body(new ApiResponse<>(leave, total))));
        });
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<LeaveRegimeRequestDTO>> getLeaveRegimeRequest(@PathVariable("id") UUID id) {
        log.debug("REST request to get LeaveRegimeRequest : {}", id);
        Mono<LeaveRegimeRequestDTO> leaveRegimeRequestDTO = leaveRegimeRequestService
            .findFirstIsDeletedIsFalseAndId(id);
        return ResponseUtil.wrapOrNotFound(leaveRegimeRequestDTO);
    }

    @PatchMapping("/{id}/cancel")
    public Mono<ResponseEntity<Map<String, Object>>> cancelLeaveRegimeRequest(@PathVariable("id") UUID id) {
        log.debug("REST request to delete LeaveRegimeRequest : {}", id);
        return SecurityUtils.getCurrentUserLogin().flatMap(user -> {
            return leaveRegimeRequestService.cancelRequest(id, UUID.fromString(user)).map(res -> {
                return ResponseEntity.ok()
                    .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME,
                        id.toString()))
                    .body(Utilities.generateResponse("SUCCESS", res));
            });
        });
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteLeaveRegimeRequest(@PathVariable("id") UUID id) {
        log.debug("REST request to delete LeaveRegimeRequest : {}", id);
        return SecurityUtils.getCurrentUserLogin().flatMap(user -> leaveRegimeRequestService
            .softDelete(id, UUID.fromString(user))
            .then(Mono.fromCallable(() -> ResponseEntity.noContent()
                .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME,
                    id.toString()))
                .build())));
    }

}
