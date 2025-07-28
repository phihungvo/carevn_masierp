package com.masi.employee.web.rest;

import com.carevn.masi.dto.ApiResponse;
import com.carevn.masi.utils.SecurityUtils;
import com.carevn.masi.utils.Utilities;
import com.masi.employee.service.InterviewScheduleService;
import com.masi.employee.service.dto.InterviewScheduleDTO;
import com.masi.employee.service.dto.InterviewScheduleQuery;
import com.masi.employee.service.dto.request.ResultInterviewRequest;
import com.masi.employee.web.rest.errors.BadRequestAlertException;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@AllArgsConstructor
@Controller
@RequestMapping("/api/interview-schedule")
public class InterviewScheduleResource {
    private final InterviewScheduleService recruitmentRequestService;
    private static final Logger log = LoggerFactory.getLogger(InterviewScheduleResource.class);


    @Operation(summary = "create RecruitmentRequest ( id recruitment )")
    @PostMapping("")
    public Mono<ResponseEntity<InterviewScheduleDTO>> createInterviewRequest(
        @Valid @RequestBody InterviewScheduleDTO interviewScheduleDTO
    ) {
        log.debug("REST request to save interviewScheduleDTO : {}", interviewScheduleDTO);
        interviewScheduleDTO.setId(UUID.randomUUID());
        return recruitmentRequestService
            .saveInterview(interviewScheduleDTO)
            .map(
                result ->
                    ResponseEntity.ok().body(result)
            );
    }


    @Operation(summary = "Update Result Interview")
    @PatchMapping("/{id}/result")
    public Mono<ResponseEntity<Map<String, Object>>> updateResultInterview(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody ResultInterviewRequest refusalOfReview
    ) throws URISyntaxException {
        log.debug("REST request to Result Interview : {}", id);
        return recruitmentRequestService
            .updateResultInterview(id, refusalOfReview)
            .map(result -> {
                Map<String, Object> responseBody = new HashMap<>();
                responseBody.put("message", "Approve Recruitment Request refusal successfully");
                responseBody.put("RecruitmentId", id);
                responseBody.put("status", "success");
                return ResponseEntity.ok().body(responseBody);
            }).onErrorResume(e -> {
                throw new BadRequestAlertException("Failed to Approve Recruitment Request: " + e.getMessage(), "Recruitment", "Recruitmentnerror");
            });

    }

    @Operation(summary = "Update  Interview")
    @PatchMapping("{id}")
    public Mono<ResponseEntity<Map<String, Object>>> updateInterview(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody InterviewScheduleDTO interviewScheduleDTO
    ) {
        log.debug("REST request to Update Interview : {}", id);
        interviewScheduleDTO.setId(id);
        return recruitmentRequestService
            .partialUpdate(interviewScheduleDTO)
            .map(result -> {
                Map<String, Object> responseBody = new HashMap<>();
                responseBody.put("message", "Update Interview successfully");
                responseBody.put("RecruitmentId", id);
                responseBody.put("status", "success");
                return ResponseEntity.ok().body(responseBody);
            }).onErrorResume(e -> {
                throw new BadRequestAlertException("Failed to Update Interview: " + e.getMessage(), "Recruitment", "Recruitmentnerror");
            });
    }

    @Operation(summary = "Delete Interview")
    @DeleteMapping("{id}")
    public Mono<ResponseEntity<Void>> deleteInterview(@PathVariable("id") UUID id) {
        log.debug("REST request to delete Interview : {}", id);
        return recruitmentRequestService
            .delete(id)
            .then(
                Mono.just(
                    ResponseEntity.noContent()
                        .build()
                )
            );
    }

    @Operation(summary = "Get One Interview")
    @GetMapping("/{id}")
    public Mono<ResponseEntity<InterviewScheduleDTO>> getInterview(@PathVariable("id") UUID id) {
        log.debug("REST request to get Interview : {}", id);
        Mono<InterviewScheduleDTO> interviewScheduleDTO = recruitmentRequestService.findById(id);
        return ResponseUtil.wrapOrNotFound(interviewScheduleDTO);
    }

    @Operation(summary = "Get All Interview")
    @GetMapping("")
    public Mono<ResponseEntity<ApiResponse<InterviewScheduleDTO>>> getAllInterview(
        @ParameterObject  InterviewScheduleQuery query,
        @ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get all Interview");
        return SecurityUtils.getUserJWTDetail().flatMap(user -> {
            query.setCompany(String.valueOf(user.getCompanyId()));
            return recruitmentRequestService
                .findAllByQuery(query, pageable)
                .collectList()
                .zipWith(recruitmentRequestService.countByQuery(query))
                .map(data -> {
                    return ResponseEntity.ok().body(new ApiResponse<>(data.getT1(), data.getT2()));
                });
        });
    }
}
