package com.masi.employee.web.rest;

import com.carevn.masi.dto.ApiResponse;
import com.carevn.masi.utils.Utilities;
import com.masi.employee.domain.RecruitmentRequest;
import com.masi.employee.domain.enumeration.RecruitmentStatus;
import com.masi.employee.repository.RecruitmentRequestRepository;
import com.masi.employee.service.InterviewScheduleService;
import com.masi.employee.service.RecruitmentRequestService;
import com.masi.employee.service.dto.*;
import com.masi.employee.service.dto.request.ConsentToApproveRecruitmentRequest;
import com.masi.employee.service.dto.request.RefusalOfApproveRecruitmentRequest;
import com.masi.employee.web.rest.errors.BadRequestAlertException;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.HeaderUtil;

/**
 * REST controller for managing {@link RecruitmentRequest}.
 */
@RestController
@RequestMapping("/api/recruitment-requests")
public class RecruitmentRequestResource {

    private static final Logger log = LoggerFactory.getLogger(RecruitmentRequestResource.class);

    private static final String ENTITY_NAME = "masiEmployeeRecruitmentRequest";
    private final InterviewScheduleService interviewScheduleService;

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RecruitmentRequestService recruitmentRequestService;

    private final RecruitmentRequestRepository recruitmentRequestRepository;

    public RecruitmentRequestResource(
        InterviewScheduleService interviewScheduleService, RecruitmentRequestService recruitmentRequestService,
        RecruitmentRequestRepository recruitmentRequestRepository
    ) {
        this.interviewScheduleService = interviewScheduleService;
        this.recruitmentRequestService = recruitmentRequestService;
        this.recruitmentRequestRepository = recruitmentRequestRepository;
    }

    /**
     * {@code POST  /recruitment-requests} : Create a new recruitmentRequest.
     *
     * @param recruitmentRequestDTO the recruitmentRequestDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new recruitmentRequestDTO, or with status {@code 400 (Bad Request)} if the recruitmentRequest has already an ID.
     */
    @Operation(summary = "create RecruitmentRequest")
    @PostMapping("/recruitment")
    public Mono<ResponseEntity<Map>> createRecruitmentRequest(
        @Valid @RequestBody RecruitmentRequestDTO recruitmentRequestDTO
    ) {
        log.debug("REST request to save RecruitmentRequest : {}", recruitmentRequestDTO);
        recruitmentRequestDTO.setId(UUID.randomUUID());
        recruitmentRequestDTO.setOldStatus(RecruitmentStatus.WAITING_APPROVAL);
        return recruitmentRequestService
            .save(recruitmentRequestDTO)
            .map(result -> {
                return ResponseEntity.ok()
                    .body(Utilities.generateResponse("success", result));


            });
    }


    @GetMapping(value = "/recruitment-change-logs/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<ApiResponse<RecruitmentChangeLogsDTO>>> getRecruitmentChangeLogs(
        @PathVariable(value = "id", required = false) final UUID id,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable

    ) {
        log.debug("REST request to get RecruitmentChangeLogs : {}", id);
        return recruitmentRequestService
            .countAllChangeLogsByRecruitmentId(id)
            .zipWith(recruitmentRequestService.findAllChangeLogsByRecruitmentId(id, pageable).collectList())
            .map(
                tuple -> {
                    List<RecruitmentChangeLogsDTO> employeeChangeLogDTOList = tuple.getT2();
                    return ResponseEntity.ok()
                        .body(new ApiResponse<>(employeeChangeLogDTOList, tuple.getT1()));
                }
            );
    }


    @Operation(summary = "adjourn Recruitment Request")
    @PatchMapping(value = "/adjourn/{id}", consumes = {"application/json", "application/merge-patch+json"})
    public Mono<ResponseEntity<Map>> adjournRecruitmentRequest(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody RecruitmentRequestDTO recruitmentRequestDTO

    ) throws URISyntaxException {
        log.debug("REST request to partial update RecruitmentRequest partially : {}", id);
        recruitmentRequestDTO.setId(id);
        recruitmentRequestDTO.setAdjourn(true);
        if (recruitmentRequestDTO.getStatus() != null && !RecruitmentStatus.WAITING_RENEW.equals(recruitmentRequestDTO.getStatus())) {
            recruitmentRequestDTO.setOldStatus(recruitmentRequestDTO.getStatus());
        }

        List<UUID> empIds = recruitmentRequestDTO.getEmpIds();
        try {
            if (empIds == null) {
                empIds = new ArrayList<>();
            }
            if (empIds.isEmpty()) {
                empIds.add(UUID.fromString("999e1416-a92a-459a-add7-611278b61b35"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        List<UUID> finalEmpIds = empIds;
        return recruitmentRequestRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }
                Mono<RecruitmentRequestDTO> result = recruitmentRequestService.partialUpdate(recruitmentRequestDTO, finalEmpIds);

                return result
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(
                        res ->
                            ResponseEntity.ok()
                                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, res.getId().toString()))
                                .body(Utilities.generateResponse("success", res))
                    );
            });
    }

    @Operation(summary = "Update Recruitment Request")
    @PatchMapping(value = "/{id}", consumes = {"application/json", "application/merge-patch+json"})
    public Mono<ResponseEntity<Map>> partialUpdateRecruitmentRequest(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody RecruitmentRequestDTO recruitmentRequestDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update RecruitmentRequest partially : {}, {}", id, recruitmentRequestDTO);
        recruitmentRequestDTO.setId(id);
        recruitmentRequestDTO.setUpdate(true);
        return recruitmentRequestRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<RecruitmentRequestDTO> result = recruitmentRequestService.partialUpdate(recruitmentRequestDTO, null);

                return result
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(
                        res ->
                            ResponseEntity.ok()
                                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, res.getId().toString()))
                                .body(Utilities.generateResponse("success", res))
                    );
            });
    }

    /**
     * {@code GET  /recruitment-requests/:id} : get the "id" recruitmentRequest.
     *
     * @param id the id of the recruitmentRequestDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the recruitmentRequestDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<RecruitmentRequestDTO>> getRecruitmentRequest(@PathVariable("id") UUID id) {
        log.debug("REST request to get RecruitmentRequest : {}", id);

        return recruitmentRequestService.findOne(id)
            .flatMap(recruitmentRequestDTO -> {
                return recruitmentRequestService.findRecruimentReviewRequest(recruitmentRequestDTO.getId())
                    .map(recruitmentReviewRequestDTOS -> {
                        recruitmentRequestDTO.setListRecruitmentReviews(recruitmentReviewRequestDTOS);
                        return recruitmentRequestDTO;
                    })
                    .map(ResponseEntity::ok)
                    .defaultIfEmpty(ResponseEntity.notFound().build());
            })
            .defaultIfEmpty(ResponseEntity.notFound().build());
    }


    /**
     * {@code DELETE  /recruitment-requests/:id} : delete the "id" recruitmentRequest.
     *
     * @param id the id of the recruitmentRequestDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @Operation(summary = "Delete Recruitment Request")
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteRecruitmentRequest(@PathVariable("id") UUID id) {
        log.debug("REST request to delete RecruitmentRequest : {}", id);
        return recruitmentRequestService
            .delete(id)
            .then(
                Mono.just(
                    ResponseEntity.noContent()
                        .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                        .build()
                )
            );
    }


    @Operation(summary = "Approve Recruitment Request if success")
    @PatchMapping("/Approve-consent/{id}")
    public Mono<ResponseEntity<Map<String, Object>>> Approveconsent(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody ConsentToApproveRecruitmentRequest consentToReview
    ) throws URISyntaxException {
        log.debug("REST request to Consent Contract : {}", id);
        return recruitmentRequestService
            .approveConsentRecruitment(id, consentToReview)
            .map(result -> {
                Map<String, Object> responseBody = new HashMap<>();
                responseBody.put("message", "Approve Recruitment Request Consent successfully");
                responseBody.put("RecruitmentId", id);
                responseBody.put("status", "success");
                return ResponseEntity.ok().body(responseBody);
            }).onErrorResume(e -> {
                throw new BadRequestAlertException("Failed to Approve Recruitment Request: " + e.getMessage(), "Recruitment", "Recruitmenterror");
            });
    }


    @Operation(summary = "Approve Recruitment Request if fail")
    @PatchMapping("/Approve-refusal/{id}")
    public Mono<ResponseEntity<Map<String, Object>>> Approverefusal(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody RefusalOfApproveRecruitmentRequest refusalOfReview
    ) throws URISyntaxException {
        log.debug("REST request to Refusal Contract : {}", id);
        return recruitmentRequestService
            .refusalRecruitment(id, refusalOfReview)
            .map(result -> {
                Map<String, Object> responseBody = new HashMap<>();
                responseBody.put("message", "Approve Recruitment Request refusal successfully");
                responseBody.put("RecruitmentId", id);      responseBody.put("status", "success");
                return ResponseEntity.ok().body(responseBody);
            });


    }

    @Operation(summary = "create RecruitmentRequest ( id recruitment )")
    @PostMapping("interview/{id}")
    public Mono<ResponseEntity<InterviewScheduleDTO>> createInterviewRequest(
        @Valid @RequestBody InterviewScheduleDTO interviewScheduleDTO,
        @PathVariable(value = "id", required = true) final UUID id
    ) {
        log.debug("REST request to save interviewScheduleDTO : {}", interviewScheduleDTO);
        interviewScheduleDTO.setId(UUID.randomUUID());
        interviewScheduleDTO.setRecruitmentRequestId(id);
        return interviewScheduleService
            .saveInterview(interviewScheduleDTO)
            .map(
                result ->
                    ResponseEntity.ok().body(result)
            );
    }

/////////////////////////


    /**
     * {@code GET  /recruitment-requests} : get all the recruitmentRequests.
     *
     * @param pageable the pagination information.
     * @param moRO     a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of recruitmentRequests in body.
     */
/*    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<ApiResponse<RecruitmentRequestDTO>>> getAllRecruitmentRequests(
        @org.springdoc.core.annotations.ParameterObject RecruitmentRequestRO moRO,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of RecruitmentRequests");
        return recruitmentRequestService
            .countAllByFilter(moRO)
            .zipWith(recruitmentRequestService.findAllByFilter(pageable, moRO).collectList())
            .map(countWithEntities -> ResponseEntity.ok().body(new ApiResponse<>(countWithEntities.getT2(), countWithEntities.getT1())));
    }*/
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<ApiResponse<RecruitmentRequestDTO>>> getAllRecruitmentRequests(
        @ParameterObject RecruitmentRequestRO moRO,
        @ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of RecruitmentRequests");

        return recruitmentRequestService
            .countAllByFilter(moRO)
            .zipWith(
                recruitmentRequestService.findAllByFilter(pageable, moRO)
                    .collectList()
                    .flatMap(recruitmentRequests -> {
                        List<UUID> requestIds = recruitmentRequests.stream()
                            .map(RecruitmentRequestDTO::getId)
                            .collect(Collectors.toList());

                        return recruitmentRequestService.getRequestIdByListIds(requestIds)
                            .collectList()
                            .map(reviewRequests -> {
                                Map<UUID, List<RecruitmentReviewRequestDTO>> reviewMap = reviewRequests.stream()
                                    .collect(Collectors.groupingBy(RecruitmentReviewRequestDTO::getRequestId));

                                recruitmentRequests.forEach(request -> {
                                    List<RecruitmentReviewRequestDTO> reviews = reviewMap.getOrDefault(request.getId(), Collections.emptyList());
                                    request.setListRecruitmentReviews(reviews);
                                });

                                return recruitmentRequests;
                            });
                    })
                    .flatMapMany(Flux::fromIterable)
                    .flatMap(request -> interviewScheduleService.countByRecruitmentPass(request.getId())
                        .map(count -> {
                            request.setNumberOfCandidates(count);
                            return request;
                        })
                    )
                    .collectList()
            )
            .map(countWithEntities -> {
                var list = countWithEntities.getT2();
                // toi viet cai  gi the nay
                list.sort((o1, o2) -> {
                    if (o1.getCreatedDate().isEqual(o2.getCreatedDate())) {
                        return 0;
                    }
                    if (o1.getCreatedDate().isAfter(o2.getCreatedDate())) {
                        return -1;
                    } else {
                        return 1;
                    }
                });
                return ResponseEntity.ok().body(
                    new ApiResponse<>(list
                        , countWithEntities.getT1()));
            });
    }


}
