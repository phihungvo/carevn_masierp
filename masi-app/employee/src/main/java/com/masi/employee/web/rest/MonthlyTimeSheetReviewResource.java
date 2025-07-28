package com.masi.employee.web.rest;

import com.masi.employee.domain.enumeration.TimeKeepingType;
import com.masi.employee.domain.enumeration.WorkspaceType;
import com.masi.employee.repository.MonthlyTimeSheetReviewRepository;
import com.masi.employee.service.MonthlyTimeSheetReviewService;
import com.masi.employee.service.dto.MonthlyTimeSheetReviewDTO;
import com.masi.employee.service.dto.ReviewTimeSheetDTO;
import com.masi.employee.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
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
 * REST controller for managing {@link com.masi.employee.domain.MonthlyTimeSheetReview}.
 */
@RestController
@RequestMapping("/api/monthly-time-sheet-reviews")
public class MonthlyTimeSheetReviewResource {

    private final Logger log = LoggerFactory.getLogger(MonthlyTimeSheetReviewResource.class);

    private static final String ENTITY_NAME = "masiEmployeeMonthlyTimeSheetReview";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MonthlyTimeSheetReviewService monthlyTimeSheetReviewService;

    private final MonthlyTimeSheetReviewRepository monthlyTimeSheetReviewRepository;

    public MonthlyTimeSheetReviewResource(
        MonthlyTimeSheetReviewService monthlyTimeSheetReviewService,
        MonthlyTimeSheetReviewRepository monthlyTimeSheetReviewRepository
    ) {
        this.monthlyTimeSheetReviewService = monthlyTimeSheetReviewService;
        this.monthlyTimeSheetReviewRepository = monthlyTimeSheetReviewRepository;
    }


    @PostMapping("")
    public Mono<ResponseEntity<MonthlyTimeSheetReviewDTO>> createMonthlyTimeSheetReview(
        @Valid @RequestBody ReviewTimeSheetDTO dto
    ) throws URISyntaxException {
        log.debug("REST request to save MonthlyTimeSheetReview : {}", dto);
        return monthlyTimeSheetReviewService
            .review(dto)
            .handle((result, sink) -> {
                try {
                    sink.next(ResponseEntity.created(new URI("/api/monthly-time-sheet-reviews/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result));
                } catch (URISyntaxException e) {
                    sink.error(new RuntimeException(e));
                }
            });
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<MonthlyTimeSheetReviewDTO>> getMonthlyTimeSheetReview(@PathVariable("id") UUID id) {
        log.debug("REST request to get MonthlyTimeSheetReview : {}", id);
        Mono<MonthlyTimeSheetReviewDTO> monthlyTimeSheetReviewDTO = monthlyTimeSheetReviewService.findOne(id);
        return ResponseUtil.wrapOrNotFound(monthlyTimeSheetReviewDTO);
    }

    @GetMapping("/month/{month}")
    public Mono<ResponseEntity<MonthlyTimeSheetReviewDTO>> getMonthlyTimeSheetReview(
        @RequestParam(value = "workspaceType",required = false) WorkspaceType workspaceType,
        @RequestParam(value = "type",required = false) TimeKeepingType type,
        @PathVariable("month") LocalDate month) {
        log.debug("REST request to get MonthlyTimeSheetReview : {}", month);
        Mono<MonthlyTimeSheetReviewDTO> monthlyTimeSheetReviewDTO = monthlyTimeSheetReviewService.findByMonth(month, workspaceType, type);
        return ResponseUtil.wrapOrNotFound(monthlyTimeSheetReviewDTO);
    }
}
