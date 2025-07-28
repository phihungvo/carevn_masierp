package com.masi.employee.service;

import com.carevn.masi.dto.ApiResponse;
import com.carevn.masi.utils.SecurityUtils;
import com.carevn.masi.utils.Utilities;
import com.masi.employee.domain.ExplanationReview;
import com.masi.employee.domain.LeaveRequest;
import com.masi.employee.domain.TimeKeepingExplanation;
import com.masi.employee.domain.TimeKeepingViolation;
import com.masi.employee.domain.enumeration.ExplanationStatus;
import com.masi.employee.domain.enumeration.LeaveRequestStatus;
import com.masi.employee.repository.ExplanationReviewRepository;
import com.masi.employee.repository.TimeKeepingExplanationRepository;
import com.masi.employee.repository.TimeKeepingViolationRepository;
import com.masi.employee.service.dto.*;
import com.masi.employee.service.mapper.TimeKeepingExplanationMapper;

import java.time.ZonedDateTime;
import java.util.*;

import com.masi.employee.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.masi.employee.domain.TimeKeepingExplanation}.
 */
@Service
@Transactional
public class TimeKeepingExplanationService {

    private final Logger log = LoggerFactory.getLogger(TimeKeepingExplanationService.class);

    private final TimeKeepingExplanationRepository explanationRepository;
    private final TimeKeepingViolationRepository violationRepository;
    private final ExplanationReviewRepository reviewRepository;
    private final ExplanationReviewService reviewService;
    private final TimeKeepingViolationService violationService;
    private final TimeKeepingExplanationMapper timeKeepingExplanationMapper;

    public TimeKeepingExplanationService(
        TimeKeepingExplanationRepository explanationRepository,
        TimeKeepingViolationRepository violationRepository,
        TimeKeepingExplanationMapper timeKeepingExplanationMapper,
        ExplanationReviewRepository reviewRepository,
        ExplanationReviewService reviewService,
        TimeKeepingViolationService violationService
    ) {
        this.explanationRepository = explanationRepository;
        this.violationRepository = violationRepository;
        this.timeKeepingExplanationMapper = timeKeepingExplanationMapper;
        this.reviewRepository = reviewRepository;
        this.reviewService = reviewService;
        this.violationService = violationService;
    }

    public Mono<Void> saveV2(Collection<ExplanationRequest> list) {
        return SecurityUtils.getUserJWTDetail().flatMap(e -> {
            return Flux.fromIterable(list)
                .flatMap(explanationRequest -> {
                    TimeKeepingExplanationDTO dto = new TimeKeepingExplanationDTO();
                    dto.setId(UUID.randomUUID());
                    dto.setExplanation(explanationRequest.getExplanation());
                    dto.setReason(explanationRequest.getReason());
                    dto.setCreatedAt(ZonedDateTime.now());
                    dto.setViolationIds(Set.of(explanationRequest.getId()));
                    dto.setLastUpdated(dto.getCreatedAt());
                    dto.setStatus(ExplanationStatus.PENDING);
                    dto.setIsActive(true);
                    dto.setEmployeeId(e.getUserId());
                    return save(dto);
                })
                .then();
        });
    }

    /**
     * Save a timeKeepingExplanation.
     *
     * @param timeKeepingExplanationDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<TimeKeepingExplanationDTO> save(TimeKeepingExplanationDTO timeKeepingExplanationDTO) {
        log.debug("Request to save TimeKeepingExplanation : {}", timeKeepingExplanationDTO);
        //set default values
        timeKeepingExplanationDTO.setCreatedAt(ZonedDateTime.now());
        timeKeepingExplanationDTO.setLastUpdated(timeKeepingExplanationDTO.getCreatedAt());
        timeKeepingExplanationDTO.setIsActive(true);
        return explanationRepository
            .save(timeKeepingExplanationMapper.toEntity(timeKeepingExplanationDTO))
            .flatMap(savedExplanation -> {
                UUID explanationId = savedExplanation.getId();
                Flux<TimeKeepingViolation> violations;
                if (timeKeepingExplanationDTO.getFromDate() != null && timeKeepingExplanationDTO.getToDate() != null) {
                    violations = violationRepository.findAllByDateBetweenAndEmployeeId(timeKeepingExplanationDTO.getFromDate(), timeKeepingExplanationDTO.getToDate(), timeKeepingExplanationDTO.getEmployeeId());

                } else {
                    violations = violationRepository.findByIdIn(timeKeepingExplanationDTO.getViolationIds());
                }

                return violations
                    .flatMap(violation -> {
                        violation.setExplanationId(explanationId);
                        violation.setIsPersisted();
                        return violationRepository.save(violation);
                    })
                    .map(violation -> {
                        savedExplanation.getViolations().add(violation);
                        return violation;
                    })
                    .then(Mono.just(savedExplanation));
            })
            .doOnError(e -> log.error("Failed to save explanation: ", e))
            .flatMap(savedExplanation -> {
                UUID explanationId = savedExplanation.getId();
                Set<UUID> reviewers = timeKeepingExplanationDTO.getReviewerIds();
                Flux<ExplanationReviewDTO> reviewSaves = Flux.fromIterable(reviewers)
                    .flatMap(reviewerId -> {
                        ExplanationReviewDTO dto = new ExplanationReviewDTO(reviewerId, explanationId);
                        return reviewService.save(dto);
                    })
                    .map(review -> {
                        savedExplanation.toDto().addReview(review);
                        return review;
                    });
                return reviewSaves.then(Mono.just(savedExplanation));
            })
            .map(TimeKeepingExplanation::toDto);
    }

    /**
     * Partially update a timeKeepingExplanation.
     *
     * @param dto the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<TimeKeepingExplanationDTO> partialUpdate(TimeKeepingExplanationDTO dto) {
        log.debug("Request to partially update TimeKeepingExplanation : {}", dto);

        return explanationRepository
            .findById(dto.getId())
            .hasElement().flatMap(hasElement -> {
                if (Boolean.TRUE.equals(hasElement)) {
                    return explanationRepository.findById(dto.getId())
                        .map(existingTimeKeepingExplanation -> {
                            existingTimeKeepingExplanation.partialUpdate(dto);
                            existingTimeKeepingExplanation.setLastUpdated(ZonedDateTime.now());
                            existingTimeKeepingExplanation.setIsPersisted();
                            return existingTimeKeepingExplanation;
                        })
                        .flatMap(explanationRepository::save)
                        .map(TimeKeepingExplanation::toDto);
                } else
                    return Mono.error(new BadRequestAlertException("TimeKeepingExplanation not found", "TimeKeepingExplanation", "idnotfound"));
            });
    }

    /**
     * Get all the timeKeepingExplanations.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<TimeKeepingExplanationDTO> findAll(Pageable pageable, ExplanationRO ro) {
        log.debug("Request to get all TimeKeepingExplanations");
        return SecurityUtils.getUserJWTDetail().flatMapMany(user -> {
            if (ro != null) {
                ro.setCompany(String.valueOf(user.getCompanyId()));
            }
            if (ro == null) {
                return explanationRepository.findAllByIsActive(true, pageable)
                        .flatMap(explanations -> violationRepository.findAllByExplanationId(explanations.getId()).collectList().map(violations -> {
                            explanations.setViolations(new HashSet<>(violations));
                            return explanations;
                        })).map(TimeKeepingExplanation::toDto);
            }

            return explanationRepository.findAllByFilter(pageable, ro)
                    .flatMap(explanations -> violationRepository.findAllByExplanationId(explanations.getId()).collectList().map(violations -> {
                        explanations.setViolations(new HashSet<>(violations));
                        return explanations;
                    }))
                    .flatMap(explanation -> reviewRepository.findAllByExplanationId(explanation.getId())
                            .collectList()
                            .map(reviews -> {
                                explanation.setReviews(new HashSet<>(reviews));
                                return explanation;
                            }))
                    .map(TimeKeepingExplanation::toDto);
        });
    }

    /**
     * Returns the number of timeKeepingExplanations available.
     *
     * @return the number of entities in the database.
     */
    public Mono<Long> countAll(ExplanationRO ro) {
        if (ro == null) {
            log.debug("Request to count all active TimeKeepingExplanations");
            return explanationRepository.countAllByIsActive(true);
        }
        log.debug("Count - TimeKeepViolation - query = {}", ro);
        return explanationRepository.countAllByFilter(ro);
    }

    /**
     * Get one timeKeepingExplanation by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<TimeKeepingExplanationDTO> findOne(UUID id) {
        log.debug("Request to get TimeKeepingExplanation : {}", id);
        return explanationRepository.findByIdAndIsActive(id, true)
            .flatMap(explanation -> violationRepository.findAllByExplanationId(explanation.getId()).collectList().map(violations -> {
                explanation.setViolations(new HashSet<>(violations));
                return explanation;
            })).flatMap(explanation -> reviewRepository.findAllByExplanationId(explanation.getId())
                .collectList()
                .map(reviews -> {
                    explanation.setReviews(new HashSet<>(reviews));
                    return explanation;
                }))
            .map(TimeKeepingExplanation::toDto);
    }

    /**
     * Delete the timeKeepingExplanation by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(UUID id) {
        log.debug("Request to delete TimeKeepingExplanation : {}", id);
        return explanationRepository.findById(id).hasElement().flatMap(hasElement -> {
            if (Boolean.TRUE.equals(hasElement)) {
                return explanationRepository.findById(id).flatMap(explanation -> {
                    explanation.setIsActive(false);
                    explanation.setIsPersisted();
                    explanation.setLastUpdated(ZonedDateTime.now());
                    return explanationRepository.save(explanation)
                        //unlink violations
                        .flatMap(deleted -> violationService.unlinkExplanation(deleted.getId()).then(Mono.just(deleted)))
                        //delete associated reviews
                        .flatMap(deleted -> reviewService.deleteReviewByExplanationId(deleted.getId()).then(Mono.just(deleted)))
                        .then();
                });
            } else
                return Mono.error(new BadRequestAlertException("TimeKeepingExplanation not found", "TimeKeepingExplanation", "idnotfound"));
        });
    }

    public Mono<Map<String, Object>> cancelLeaveRequest(UUID id) {
        log.debug("Request to cancel explanation: {}", id);
        return explanationRepository.findByIdAndIsActive(id, true)
            .<TimeKeepingExplanation>handle((explanation, sink) -> {
                if (!ExplanationStatus.PENDING.equals(explanation.getStatus())) {
                    sink.error(new BadRequestAlertException("Only PENDING explanation can be cancelled", "TimekeepingExplanation", "explanationNotPending"));
                    return;
                }
                explanation.setStatus(ExplanationStatus.CANCELLED);
                explanation.setIsPersisted();
                sink.next(explanation);
            })
            .flatMap(explanationRepository::save)
            .flatMap(explanation -> reviewService.deleteReviewByExplanationId(explanation.getId()))
            .then(Mono.fromCallable(() -> Utilities.generateResponse("Explanation cancelled successfully", null)))
            .switchIfEmpty(Mono.error(new BadRequestAlertException("Explanation not found", "TimekeepingExplanation", "explanationNotFound")))
            .onErrorResume(e -> {
                log.error("Failed to cancel explanation: ", e);
                return Mono.error(e);
            });
    }
}
