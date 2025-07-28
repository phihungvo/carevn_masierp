package com.masi.employee.service;

import com.carevn.masi.dto.ApiResponse;
import com.carevn.masi.utils.SecurityUtils;
import com.carevn.masi.utils.Utilities;
import com.masi.employee.domain.EmployeeProfile;
import com.masi.employee.domain.LeaveRequest;
import com.masi.employee.domain.enumeration.*;
import com.masi.employee.repository.EmployeeProfileRepository;
import com.masi.employee.repository.LeaveRequestRepository;
import com.masi.employee.repository.LeaveRequestReviewRepository;
import com.masi.employee.service.dto.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import com.masi.employee.service.web.client.ConfigClient;
import com.masi.employee.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.service.filter.UUIDFilter;

/**
 * Service Implementation for managing
 * {@link com.masi.employee.domain.LeaveRequest}.
 */
@Service
@Transactional
public class LeaveRequestService {

    private final Logger log = LoggerFactory.getLogger(LeaveRequestService.class);

    private final LeaveRequestRepository leaveRequestRepository;
    private final LeaveRequestReviewRepository reviewRepository;
    private final LeaveRequestReviewService reviewService;
    private final AnnualLeaveService annualLeaveService;
    private final TimeKeepingService timeKeepingService;
    private final ConfigClient configClient;
    private final EmployeeProfileRepository employeeProfileRepository;
    public static final int TOTAL_SHIFT_OF_DAY = 2;

    public LeaveRequestService(LeaveRequestRepository leaveRequestRepository,
            LeaveRequestReviewRepository reviewRepository, LeaveRequestReviewService reviewService,
            AnnualLeaveService annualLeaveService, TimeKeepingService timeKeepingService, ConfigClient configClient,
            EmployeeProfileRepository employeeProfileRepository) {
        this.leaveRequestRepository = leaveRequestRepository;
        this.reviewRepository = reviewRepository;
        this.reviewService = reviewService;
        this.annualLeaveService = annualLeaveService;
        this.timeKeepingService = timeKeepingService;
        this.configClient = configClient;
        this.employeeProfileRepository = employeeProfileRepository;
    }

    /**
     * Save a leaveRequest.
     *
     * @param dto the entity to save.
     * @return the persisted entity.
     */
    public Mono<LeaveRequestDTO> save(LeaveRequestDTO dto) {
        log.debug("Request to save LeaveRequest : {}", dto);
        if (LeaveRequestDayType.HALF_DAY.equals(dto.getLeaveRequestDayType())
                && (Objects.isNull(dto.getFromTime()) || Objects.isNull(dto.getToTime()))) {
            return Mono
                    .error(new BadRequestAlertException("From time and to time are required for half day leave request",
                            "LeaveRequest", "startTimeEndTimeRequired"));
        }
        LeaveRequest leaveRequest = dto.toEntity();
        leaveRequest.setId(UUID.randomUUID());
        leaveRequest.setCreatedAt(ZonedDateTime.now());
        leaveRequest.setLastUpdated(ZonedDateTime.now());
        leaveRequest.setIsActive(true);
        leaveRequest.setFiles(dto.getFiles());
        return leaveRequestRepository.save(leaveRequest)
                .flatMap(request -> createAndSetReviews(request, dto.getReviewerIds())).map(LeaveRequest::toDto);
    }

    public Mono<Float> countDayOff(LocalDate fromDate, LocalDate toDate, String workspaceType, String company) {

        return configClient.getStandardWorkScheduleConfig(workspaceType, company).flatMap(configDTO -> {
            float count = 0;
            var start = fromDate.plusDays(1);
            for (LocalDate date = start; date.isBefore(toDate); date = date.plusDays(1)) {
                var dayOfWeek = date.getDayOfWeek().getValue();
                var thatDaySchedule = configDTO.stream().filter(config -> config.getDayOfWeek() == dayOfWeek)
                        .findFirst().orElse(null);
                var numberOfShifts = thatDaySchedule != null ? thatDaySchedule.getNumberOfShifts() : 0;
                // 2 ca => 1 ngày nghỉ (nghỉ 1 ngày)
                // 1 ca => 0.5 ngày nghỉ
                count += (numberOfShifts * 1.0f / TOTAL_SHIFT_OF_DAY /* 2 */);
            }
            return Mono.just(count);
        });
    }

    public Mono<LocalDateTime> calculateDateEnd(LocalDate fromDate, float totalDayOff, String workspaceType,
            String company) {
        // only allow x.0 / x.5
        var afterFloat = totalDayOff - (int) totalDayOff;
        if (afterFloat != 0 && afterFloat != 0.5) {
            return Mono
                    .error(new BadRequestAlertException("Invalid total day off", "LeaveRequest", "invalidTotalDayOff"));
        }
        return configClient.getStandardWorkScheduleConfig(workspaceType, company).flatMap(configDTO -> {
            float count = 0;

            var date = fromDate.plusDays(1);
            while (count < totalDayOff) {
                var dayOfWeek = date.getDayOfWeek().getValue();
                var thatDaySchedule = configDTO.stream().filter(config -> config.getDayOfWeek() == dayOfWeek)
                        .findFirst().orElse(null);
                var numberOfShifts = thatDaySchedule != null ? thatDaySchedule.getNumberOfShifts() : 0;
                count += (numberOfShifts * 1.0f / TOTAL_SHIFT_OF_DAY /* 2 */);
                date = date.plusDays(1);
            }
            // kiếm ngày làm việc dau tien
            LocalDate dayReturn = date;
            while (true) {
                LocalDate finalDayReturn = dayReturn;
                if (configDTO.stream().filter(c -> c.getNumberOfShifts() <= 0)
                        .anyMatch(c -> c.getDayOfWeek() == finalDayReturn.getDayOfWeek().getValue())) {
                    dayReturn = dayReturn.plusDays(1);
                } else {
                    break;
                }
            }
            if (count > totalDayOff) {
                return Mono.just(dayReturn.atStartOfDay().minusHours(12));
            }
            return Mono.just(dayReturn.atStartOfDay());
        });
    }

    public Mono<Float> countDayOff(LocalDate fromDate, LocalDate toDate, UUID employeeId) {
        return employeeProfileRepository.findFirstById(employeeId).flatMap(employeeProfile -> countDayOff(fromDate,
                toDate, employeeProfile.getWorkspace().getWorkspaceType().name(), employeeProfile.getCompany()));
    }

    public Mono<LocalDateTime> calculateDateEnd(LocalDate fromDate, float totalDayOff, UUID employeeId) {
        return employeeProfileRepository.findFirstById(employeeId).flatMap(employeeProfile -> calculateDateEnd(fromDate,
                totalDayOff, employeeProfile.getWorkspace().getWorkspaceType().name(), employeeProfile.getCompany()));
    }

    public Mono<LeaveRequest> createAndSetReviews(LeaveRequest request, Set<UUID> reviewerIds) {
        UUID requestId = request.getId();
        Flux<LeaveRequestReviewDTO> reviewSaves = Flux.fromIterable(reviewerIds).flatMap(reviewerId -> {
            LeaveRequestReviewDTO reviewDto = new LeaveRequestReviewDTO(reviewerId, requestId);
            return reviewService.save(reviewDto);
        }).map(review -> {
            request.toDto().addReview(review);
            return review;
        });
        return reviewSaves.then(Mono.just(request));
    }

    public Mono<LeaveRequestDTO> saveLeaveRequestWithApprove(LeaveRequestDTO dto) {
        log.debug("Request to save LeaveRequest with approved : {}", dto);
        if (LeaveRequestDayType.HALF_DAY.equals(dto.getLeaveRequestDayType())
                && (Objects.isNull(dto.getFromTime()) || Objects.isNull(dto.getToTime()))) {
            return Mono
                    .error(new BadRequestAlertException("From time and to time are required for half day leave request",
                            "LeaveRequest", "startTimeEndTimeRequired"));
        }
        LeaveRequest leaveRequest = dto.toEntity();
        leaveRequest.setId(dto.getId());
        leaveRequest.setFiles(dto.getFiles());
        log.debug("Request to save LeaveRequest 123 : {}", leaveRequest);

        return leaveRequestRepository.save(leaveRequest)
                .flatMap(request -> createAndSetReviewsWithApprove(request, dto.getReviews())).flatMap(tuple2 -> {
                    float totalDayOff = tuple2.getTotalDayOff();
                    final float totalDayOff1 = totalDayOff;
                    if (LeaveType.UNPAID_LEAVE.equals(leaveRequest.getLeaveRequestType())) {
                        totalDayOff = 0;
                    }
                    if (LeaveType.COMPENSATION_LEAVE.equals(leaveRequest.getLeaveRequestType())) {
                        totalDayOff = 0;
                    }
                    final float finalTotalDayOff = totalDayOff;
                    return annualLeaveService.findOneById(leaveRequest.getEmployeeId()).flatMap(dayOffDTO -> {
                        // dayOffDTO.setNumberDaysOff(dayOffDTO.getNumberDaysOff() - finalTotalDayOff);
                        dayOffDTO.setUseDaysOff(dayOffDTO.getUseDaysOff() + finalTotalDayOff);

                        // if (dayOffDTO.getNumberDaysOff() < 0)
                        // return Mono.error(new BadRequestAlertException("Ngày nghỉ không đủ",
                        // "LeaveRequest", "notEnoughDayOff"));
                        return annualLeaveService.setUseDayOff(dayOffDTO.getId(), dayOffDTO.getUseDaysOff())
                                .then(Mono.just(leaveRequest));
                    }).flatMap(t -> {
                        // leaveRequest.setFromDate(leaveRequest.getFromDate().plusDays(-1)); // test
                        return timeKeepingService.createLeaveDayForRequest(leaveRequest);
                    }).then(Mono.just(leaveRequest.toDto())).doOnTerminate(() -> {
                        leaveRequest.setTotalDayOff(dto.getTotalDayOff() > 0 ? dto.getTotalDayOff() : totalDayOff1);
                        leaveRequestRepository.save(leaveRequest.setIsPersisted()).subscribe();
                    });
                });
    }

    public Mono<LeaveRequest> createAndSetReviewsWithApprove(LeaveRequest request,
            Set<LeaveRequestReviewDTO> reviewerIds) {
        log.debug("createAndSetReviewsWithApprove : {}, {}", request, reviewerIds);
        if (reviewerIds == null || reviewerIds.isEmpty()) {
            return Mono.just(request);
        }
        Flux<LeaveRequestReviewDTO> reviewSaves = Flux.fromIterable(reviewerIds).flatMap(reviewService::save)
                .map(review -> {
                    request.toDto().addReview(review);
                    return review;
                });
        return reviewSaves.flatMap(review -> timeKeepingService.createLeaveDayForRequest(request))
                .then(Mono.just(request));
    }

    /**
     * Partially update a leaveRequest.
     *
     * @param leaveRequestDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<LeaveRequestDTO> partialUpdate(LeaveRequestDTO leaveRequestDTO) {
        log.debug("Request to partially update LeaveRequest : {}", leaveRequestDTO);

        return leaveRequestRepository.findById(leaveRequestDTO.getId()).map(existingLeaveRequest -> {
            existingLeaveRequest.partialUpdate(leaveRequestDTO);
            existingLeaveRequest.setFiles(leaveRequestDTO.getFiles());
            return existingLeaveRequest.setIsPersisted();
        }).flatMap(leaveRequestRepository::save).map(LeaveRequest::toDto);
    }

    /**
     * Get all the leaveRequests.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<LeaveRequest> findAll(Pageable pageable, LeaveRequestQuery query) {
        log.debug("Request to get all LeaveRequests");
        if (query == null) {
            return leaveRequestRepository.findAllByIsActive(true, pageable).flatMap(this::mapWithReviews);
        }
        LeaveRequestFilter filter = createFilter(query);
        log.debug("LeaveRequestFilter : {}", filter);
        return leaveRequestRepository.findAllByFilter(pageable, filter).flatMap(this::mapWithReviews);
    }

    private Mono<LeaveRequest> mapWithReviews(LeaveRequest leaveRequest) {
        return reviewRepository.findAllByLeaveRequestId(leaveRequest.getId()).collectList().map(reviews -> {
            leaveRequest.setReviews(new HashSet<>(reviews));
            return leaveRequest;
        });
    }

    @Transactional
    public Mono<ApiResponse<LeaveRequestDTO>> findAndCount(Pageable pageable, LeaveRequestQuery query) {
        return findAll(pageable, query).map(LeaveRequest::toDto).collectList().zipWith(countAll(query))
                .map(tuple -> new ApiResponse<LeaveRequestDTO>(tuple.getT1(), tuple.getT2()));
    }

    /**
     * Returns the number of leaveRequests available.
     *
     * @return the number of entities in the database.
     */
    public Mono<Long> countAll(LeaveRequestQuery query) {
        if (query == null) {
            return leaveRequestRepository.countAllByIsActive(true);
        }
        LeaveRequestFilter filter = createFilter(query);
        log.debug("CountAll - LeaveRequest - filter = {}", filter);
        return leaveRequestRepository.countAllByFilter(filter);
    }

    /**
     * Get one leaveRequest by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<LeaveRequestDTO> findOne(UUID id) {
        log.debug("Request to get LeaveRequest : {}", id);
        return leaveRequestRepository.findByIdAndIsActive(id, true).flatMap(leaveRequest -> reviewRepository
                .findByLeaveRequest(leaveRequest.getId()).collectList().map(leaveRequestReviews -> {
                    leaveRequest.setReviews(new HashSet<>(leaveRequestReviews));
                    return leaveRequest;
                })).flatMap(explanation -> reviewRepository.findAllByLeaveRequestId(explanation.getId()).collectList()
                        .map(reviews -> {
                            explanation.setReviews(new HashSet<>(reviews));
                            return explanation;
                        }))
                .map(LeaveRequest::toDto);
    }

    /**
     * Delete the leaveRequest by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Map<String, Object>> delete(UUID id) {
        log.debug("Request to delete LeaveRequest : {}", id);
        return leaveRequestRepository.findByIdAndIsActive(id, true).flatMap(leaveRequest -> {
            leaveRequest.setIsActive(false);
            leaveRequest.setIsPersisted();
            return leaveRequestRepository.save(leaveRequest);
        }).flatMap(leaveRequest -> reviewService.deleteReviewByRequestId(leaveRequest.getId()))
                .then(Mono.fromCallable(() -> Utilities.generateResponse("Leave request deleted successfully", null)))
                .switchIfEmpty(Mono.error(new BadRequestAlertException("Leave request not found", "LeaveRequest",
                        "leaveRequestNotFound")));
    }

    public Mono<LeaveRequest> getByEmployeeIdAndDate(UUID employeeId, LocalDate date) {
        return leaveRequestRepository.findByEmployeeIdDateRangeInclude(employeeId, date);
    }

    public Mono<Map<String, Object>> cancelLeaveRequest(UUID leaveRequestId) {
        log.debug("Request to cancel leave request: {}", leaveRequestId);
        return leaveRequestRepository.findByIdAndIsActive(leaveRequestId, true)
                .<LeaveRequest>handle((leaveRequest, sink) -> {
                    if (LeaveRequestStatus.CANCELLED.equals(leaveRequest.getStatus())) {
                        sink.error(new BadRequestAlertException("CANCELLED leave request can not be cancelled",
                                "LeaveRequest", "alreadyCancelled"));
                        return;
                    }
                    if (LeaveRequestStatus.APPROVED.equals(leaveRequest.getStatus())) {
                        sink.error(new BadRequestAlertException("APPROVED leave request can not be cancelled",
                                "LeaveRequest", "alreadyApproved"));
                        return;
                    }
                    leaveRequest.setStatus(LeaveRequestStatus.CANCELLED);
                    leaveRequest.setIsPersisted();
                    sink.next(leaveRequest);
                }).flatMap(leaveRequestRepository::save)
                .flatMap(request -> reviewService.deleteReviewByRequestId(request.getId()))
                .then(Mono.fromCallable(() -> Utilities.generateResponse("Leave request cancelled successfully", null)))
                .switchIfEmpty(Mono.error(new BadRequestAlertException("Leave request not found", "LeaveRequest",
                        "leaveRequestNotFound")))
                .onErrorResume(e -> {
                    log.error("Failed to cancel leave request: ", e);
                    return Mono.error(e);
                });
    }

    private LeaveRequestFilter createFilter(LeaveRequestQuery query) {
        LeaveRequestFilter filter = new LeaveRequestFilter();
        filter.setIsActive(true);
        if (query.getType() != null) {
            filter.type().setIn(query.getType());
        }
        if (query.getStatus() != null) {
            filter.status().setIn(query.getStatus());
        }
        if (query.getWorkspaceIds() != null) {
            filter.setWorkspaceIds(new UUIDFilter());
            filter.getWorkspaceIds().setIn(query.getWorkspaceIds());
        }
        if (query.getEmployeeIds() != null) {
            filter.setEmployeeIds(new UUIDFilter());
            filter.getEmployeeIds().setIn(query.getEmployeeIds());
        }
        filter.setWorkspaceType(query.getWorkspaceType());
        return filter;
    }
}
