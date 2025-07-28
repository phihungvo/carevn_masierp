package com.masi.employee.service;

import com.masi.employee.domain.ExplanationReview;
import com.masi.employee.domain.enumeration.ExplanationStatus;
import com.masi.employee.domain.enumeration.ReviewStatus;
import com.masi.employee.repository.ExplanationReviewRepository;
import com.masi.employee.repository.TimeKeepingExplanationRepository;
import com.masi.employee.service.dto.ExplanationReviewDTO;
import com.masi.employee.service.dto.ExplanationRequestObjectBase;
import com.masi.employee.service.mapper.ExplanationReviewMapper;

import java.time.ZonedDateTime;
import java.util.UUID;

import com.masi.employee.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.masi.employee.domain.ExplanationReview}.
 */
@Service
@Transactional
public class ExplanationReviewService {

    private final Logger log = LoggerFactory.getLogger(ExplanationReviewService.class);

    private final ExplanationReviewRepository reviewRepository;
    private final TimeKeepingExplanationRepository explanationRepository;
    private final ExplanationReviewMapper reviewMapper;

    public ExplanationReviewService(
        ExplanationReviewRepository reviewRepository,
        TimeKeepingExplanationRepository explanationRepository,
        ExplanationReviewMapper reviewMapper
    ) {
        this.reviewRepository = reviewRepository;
        this.explanationRepository = explanationRepository;
        this.reviewMapper = reviewMapper;
    }

    /**
     * Save a explanationReview.
     *
     * @param dto the entity to save.
     * @return the persisted entity.
     */
    public Mono<ExplanationReviewDTO> save(ExplanationReviewDTO dto) {
        log.debug("Request to save ExplanationReview : {}", dto);
        dto.setId(UUID.randomUUID());
        return reviewRepository.save(reviewMapper.toEntity(dto)).map(reviewMapper::toDto)
            .doOnError(throwable -> {
                log.error("Error while saving explanation review", throwable);
            });
    }

    /**
     * Update a explanationReview.
     *
     * @param dto the entity to save.
     * @return the persisted entity.
     */
    public Mono<ExplanationReviewDTO> update(ExplanationReviewDTO dto) {
        log.debug("Request to update ExplanationReview : {}", dto);
        return reviewRepository
            .save(reviewMapper.toEntity(dto).setIsPersisted())
            .map(reviewMapper::toDto);
    }

    public Mono<ExplanationReviewDTO> handleReview(ExplanationReviewDTO dto) {
        log.debug("Request to partially update ExplanationReview : {}", dto);

        return reviewRepository
            .findById(dto.getId())
            .<ExplanationReview>handle((existing, sink) -> {

                //if the review is already approved or rejected, throw an error and do not update the review
                if (ReviewStatus.APPROVED.equals(existing.getStatus()) || ReviewStatus.REJECTED.equals(existing.getStatus())) {
                    sink.error(new BadRequestAlertException("Review is already APPROVED/REJECTED and cannot be updated", "ExplanationReview", "reviewstatuserror"));
                    return;
                }
                sink.next(existing);
            })
            .map(existing -> {
                reviewMapper.partialUpdate(existing, dto);
                existing.setIsPersisted();
                existing.setLastUpdated(ZonedDateTime.now());
                return existing;
            })
            .flatMap(reviewRepository::save)
            .flatMap(saved -> {
                //after the review is submitted, check if all reviews are approved and update the explanation status
                if (ReviewStatus.APPROVED.equals(saved.getStatus())) {
                    return checkIfAllReviewsAreApproved(saved.getExplanationId())
                        .then(Mono.just(saved));
                } else if (ReviewStatus.REJECTED.equals(saved.getStatus())) {
                    //if the review is rejected, reject the explanation
                    return rejectExplanation(saved.getExplanationId())
                        .then(Mono.just(saved));
                } else {
                    return Mono.just(saved);
                }
            })
            .map(reviewMapper::toDto);
    }

    private Mono<Void> checkIfAllReviewsAreApproved(UUID explanationId) {
        return reviewRepository.findByExplanationId(explanationId)
            .all(review -> ReviewStatus.APPROVED.equals(review.getStatus()))
            .flatMap(allApproved -> {
                if (Boolean.TRUE.equals(allApproved)) {
                    return explanationRepository.findById(explanationId)
                        .map(explanation -> {
                            explanation.setStatus(ExplanationStatus.APPROVED);
                            explanation.setIsPersisted();
                            explanation.setLastUpdated(ZonedDateTime.now());
                            return explanation;
                        })
                        .flatMap(explanationRepository::save)
                        .then();
                } else {
                    return Mono.empty();
                }
            });
    }

    private Mono<Void> rejectExplanation(UUID explanationId) {
        return explanationRepository.findById(explanationId)
            .map(explanation -> {
                explanation.setStatus(ExplanationStatus.REJECTED);
                explanation.setIsPersisted();
                explanation.setLastUpdated(ZonedDateTime.now());
                return explanation;
            })
            .flatMap(explanationRepository::save)
            .then();
    }

    /**
     * Get all the explanationReviews.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<ExplanationReviewDTO> findAll() {
        log.debug("Request to get all ExplanationReviews");
        return reviewRepository.findAll().map(reviewMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Flux<ExplanationReviewDTO> find(ExplanationRequestObjectBase reviewRO, Pageable pageable) {
        log.debug("Request to get ExplanationReviews : {}", reviewRO);
        return reviewRepository.findAllBy(reviewRO, pageable).map(reviewMapper::toDto);
    }

    /**
     * Returns the number of explanationReviews available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return reviewRepository.count();
    }

    /**
     * Get one explanationReview by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<ExplanationReviewDTO> findOne(UUID id) {
        log.debug("Request to get ExplanationReview : {}", id);
        return reviewRepository.findByIdAndIsActive(id, true).map(reviewMapper::toDto);
    }

    /**
     * Delete the explanationReview by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(UUID id) {
        log.debug("Request to delete ExplanationReview : {}", id);
        return reviewRepository.deleteById(id);
    }

    public Mono<Void> deleteReviewByExplanationId(UUID explanationId) {
        return reviewRepository.findByExplanationId(explanationId)
            .flatMap(review -> {
                review.setIsActive(false);
                review.setIsPersisted();
                review.setLastUpdated(ZonedDateTime.now());
                return reviewRepository.save(review);
            })
            .then();
    }
}
