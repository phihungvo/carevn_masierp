package com.masi.employee.service;

import com.masi.employee.domain.*;
import com.masi.employee.domain.enumeration.*;
import com.masi.employee.repository.LeaveRequestRepository;
import com.masi.employee.repository.LeaveRequestReviewRepository;
import com.masi.employee.service.dto.*;
import com.masi.employee.service.mapper.LeaveRequestReviewMapper;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import com.masi.employee.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.masi.employee.domain.LeaveRequestReview}.
 */
@Service
@Transactional
public class LeaveRequestReviewService {

    private final Logger log = LoggerFactory.getLogger(LeaveRequestReviewService.class);

    private final LeaveRequestReviewRepository reviewRepository;
    private final LeaveRequestRepository leaveRequestRepository;
    private final LeaveRequestReviewMapper reviewMapper;
    private final PersonalMonthlyTimesheetService personalMonthlyTimesheetService;
    private final TimeKeepingService timeKeepingService;
    private final AnnualLeaveService annualLeaveService;


    private LeaveRequestService leaveRequestService;

    @Autowired
    public void setLeaveRequestService(@Lazy LeaveRequestService leaveRequestService) {
        this.leaveRequestService = leaveRequestService;
    }

    public LeaveRequestReviewService(
        LeaveRequestReviewRepository reviewRepository,
        LeaveRequestRepository leaveRequestRepository,
        LeaveRequestReviewMapper reviewMapper,
        PersonalMonthlyTimesheetService personalMonthlyTimesheetService,
        TimeKeepingService timeKeepingService, AnnualLeaveService annualLeaveService
    ) {
        this.reviewRepository = reviewRepository;
        this.leaveRequestRepository = leaveRequestRepository;
        this.reviewMapper = reviewMapper;
        this.personalMonthlyTimesheetService = personalMonthlyTimesheetService;
        this.timeKeepingService = timeKeepingService;
        this.annualLeaveService = annualLeaveService;
    }

    /**
     * Save a leaveRequestReview.
     *
     * @param dto the entity to save.
     * @return the persisted entity.
     */
    public Mono<LeaveRequestReviewDTO> save(LeaveRequestReviewDTO dto) {
        log.debug("Request to save LeaveRequestReview : {}", dto);
        dto.setId(UUID.randomUUID());
        return reviewRepository
            .save(reviewMapper.toEntity(dto))
            .map(reviewMapper::toDto);
    }

    public Flux<LeaveRequestReviewDTO> getByLeaveRequestId(UUID leaveRequestId) {
        return reviewRepository.findAllByLeaveRequestIdAndIsActive(leaveRequestId, true).map(reviewMapper::toDto);
    }

    /**
     * Update a leaveRequestReview.
     *
     * @param leaveRequestReviewDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<LeaveRequestReviewDTO> update(LeaveRequestReviewDTO leaveRequestReviewDTO) {
        log.debug("Request to update LeaveRequestReview : {}", leaveRequestReviewDTO);
        return reviewRepository
            .save(reviewMapper.toEntity(leaveRequestReviewDTO).setIsPersisted())
            .map(reviewMapper::toDto);
    }

    public Mono<LeaveRequestReviewDTO> handleReview(LeaveRequestReviewDTO dto) {
        log.debug("Request to partially update leave request review : {}", dto);

        return reviewRepository
            .findById(dto.getId())
            .<LeaveRequestReview>handle((existing, sink) -> {
                //if the review is already approved or rejected, throw an error and do not update the review

                if (ReviewStatus.APPROVED.equals(existing.getStatus())) {
                    sink.error(new BadRequestAlertException("Review is already APPROVED and cannot be updated", "LeaveRequestReview", "alreadyApproved"));
                    return;
                }
                sink.next(existing);
            })
            .flatMap(review -> {
                return leaveRequestRepository.findById(review.getLeaveRequestId())
                    .flatMap(leaveRequest -> {
//                        if (LeaveRequestStatus.REJECTED.equals(leaveRequest.getStatus())) {
//                            return Mono.error(new BadRequestAlertException("Cannot approve the leave request as it is already cancelled", "LeaveRequestReview", "alreadyRejected"));
//                        }
                        if (LeaveRequestStatus.CANCELLED.equals(leaveRequest.getStatus())) {
                            return Mono.error(new BadRequestAlertException("Cannot approve the leave request as it is already cancelled", "LeaveRequestReview", "alreadyCancelled"));
                        }
                        if (ReviewStatus.APPROVED.equals(dto.getStatus())) {
                            return personalMonthlyTimesheetService.isLocked(leaveRequest.getEmployeeId(), leaveRequest.getFromDate().withDayOfMonth(1), TimeKeepingType.HOUR) // todo: change it
                                .flatMap(locked -> {
                                    if (Boolean.TRUE.equals(locked)) {
                                        return Mono.error(new BadRequestAlertException("Cannot approve the leave request as the timesheet is already approved", "LeaveRequestReview", "timesheetLocked"));
                                    } else {
                                        return Mono.just(review);
                                    }
                                });
                        } else {
                            return Mono.just(review);
                        }
                    });
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
                    return checkIfAllReviewsAreApproved(saved.getLeaveRequestId())
                        .then(Mono.just(saved));
                } else if (ReviewStatus.REJECTED.equals(saved.getStatus())) {
                    //if the review is rejected, reject the explanation
                    return rejectExplanation(saved.getLeaveRequestId())
                        .then(Mono.just(saved));
                } else {
                    return Mono.just(saved);
                }
            })
            .map(reviewMapper::toDto);
    }

    private Mono<Void> checkIfAllReviewsAreApproved(UUID leaveRequestId) {
        return reviewRepository.findByLeaveRequest(leaveRequestId)
            .all(review -> ReviewStatus.APPROVED.equals(review.getStatus()))
            .flatMap(allApproved -> {
                if (Boolean.TRUE.equals(allApproved)) {
                    return leaveRequestRepository.findById(leaveRequestId)
                        .flatMap(leaveRequest -> {
                            leaveRequest.setStatus(LeaveRequestStatus.APPROVED);
                            leaveRequest.setIsPersisted();
                            leaveRequest.setLastUpdated(ZonedDateTime.now());

                            float dayOff = leaveRequest.getTotalDayOff();
                            if (LeaveType.UNPAID_LEAVE.equals(leaveRequest.getLeaveRequestType())) {
                                dayOff = 0;
                            }
                            if (LeaveType.COMPENSATION_LEAVE.equals(leaveRequest.getLeaveRequestType())) {
                                dayOff = 0;
                            }
                            final float finalDayOff = dayOff;
                            return
                                timeKeepingService.createLeaveDayForRequest(leaveRequest).flatMap(isOke -> {
                                        log.info("Timekeeping created {}", isOke);
                                        if (isOke) {
                                            return annualLeaveService.findOneById(leaveRequest.getEmployeeId()).flatMap(dayOffDTO -> {
                                                dayOffDTO.setUseDaysOff((dayOffDTO.getUseDaysOff() + finalDayOff));
                                                return annualLeaveService.setUseDayOff(dayOffDTO.getId(), dayOffDTO.getUseDaysOff()).then(Mono.just(leaveRequest));
                                            });
                                        }
                                        return Mono.just(leaveRequest);
                                    })
                                    .then(Mono.just(leaveRequest));
                        })
                        .flatMap(leaveRequestRepository::save)
                        .flatMap(e -> {
                            log.info("Leave request approved{}", e);
                            return Mono.just(e);
                        })

                        .then();
                } else {
                    return Mono.empty();
                }
            });
    }

    private Mono<Void> rejectExplanation(UUID explanationId) {
        return leaveRequestRepository.findById(explanationId)
            .map(leaveRequest -> {
                leaveRequest.setStatus(LeaveRequestStatus.REJECTED);
                leaveRequest.setIsPersisted();
                leaveRequest.setLastUpdated(ZonedDateTime.now());
                return leaveRequest;
            })
            .flatMap(leaveRequestRepository::save)
            .then();
    }

    /**
     * Get all the leaveRequestReviews.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<LeaveRequestReviewDTO> findAll() {
        log.debug("Request to get all LeaveRequestReviews");
        return reviewRepository.findAll().map(reviewMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Flux<LeaveRequestReviewDTO> find(LeaveRequestRequestObjectBase reviewRO, Pageable pageable) {
        log.debug("Request to get leave request review : {}", reviewRO);
        return reviewRepository.findAllBy(reviewRO, pageable).map(reviewMapper::toDto);
    }

    /**
     * Returns the number of leaveRequestReviews available.
     *
     * @return the number of entities in the database.
     */
    public Mono<Long> countAll() {
        return reviewRepository.count();
    }

    /**
     * Get one leaveRequestReview by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<LeaveRequestReviewDTO> findOne(UUID id) {
        log.debug("Request to get LeaveRequestReview : {}", id);
        return reviewRepository.findById(id).map(reviewMapper::toDto);
    }

    /**
     * Delete the leaveRequestReview by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(UUID id) {
        log.debug("Request to delete LeaveRequestReview : {}", id);
        return reviewRepository.deleteById(id);
    }

    public Mono<Void> deleteReviewByRequestId(UUID leaveRequestId) {
        return reviewRepository.findAllByLeaveRequestId(leaveRequestId)
            .flatMap(review -> {
                review.setIsActive(false);
                review.setIsPersisted();
                review.setLastUpdated(ZonedDateTime.now());
                return reviewRepository.save(review);
            })
            .then();
    }
}
