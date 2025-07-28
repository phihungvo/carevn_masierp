package com.masi.employee.service;

import com.masi.employee.domain.MonthlyTimeSheetReview;
import com.masi.employee.domain.enumeration.TimeKeepingType;
import com.masi.employee.domain.enumeration.TimesheetReviewStatus;
import com.masi.employee.domain.enumeration.WorkspaceType;
import com.masi.employee.repository.MonthlyTimeSheetReviewRepository;
import com.masi.employee.service.dto.MonthlyTimeSheetReviewDTO;
import com.masi.employee.service.dto.ReviewTimeSheetDTO;
import com.masi.employee.service.mapper.MonthlyTimeSheetReviewMapper;
import com.masi.employee.web.rest.errors.BadRequestAlertException;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing
 * {@link MonthlyTimeSheetReview}.
 */
@Service
@Transactional
public class MonthlyTimeSheetReviewService {

    private final Logger log = LoggerFactory.getLogger(MonthlyTimeSheetReviewService.class);

    private final MonthlyTimeSheetReviewRepository monthlyTimeSheetReviewRepository;

    private final MonthlyTimeSheetReviewMapper monthlyTimeSheetReviewMapper;

    private final PersonalMonthlyTimesheetService personalMonthlyTimesheetService;

    public MonthlyTimeSheetReviewService(
        MonthlyTimeSheetReviewRepository monthlyTimeSheetReviewRepository,
        MonthlyTimeSheetReviewMapper monthlyTimeSheetReviewMapper,
        PersonalMonthlyTimesheetService personalMonthlyTimesheetService) {
        this.monthlyTimeSheetReviewRepository = monthlyTimeSheetReviewRepository;
        this.monthlyTimeSheetReviewMapper = monthlyTimeSheetReviewMapper;
        this.personalMonthlyTimesheetService = personalMonthlyTimesheetService;
    }

    public Mono<MonthlyTimeSheetReviewDTO> review(ReviewTimeSheetDTO dto) {
        if (TimesheetReviewStatus.APPROVED.equals(dto.getStatus()) && StringUtils.isBlank(dto.getSignatureFile())) {
            return Mono.error(new BadRequestAlertException("Signature is required for approval", "MonthlyTimeSheetReview",
                "signatureRequired"));
        }
        if (TimesheetReviewStatus.REJECTED.equals(dto.getStatus()) && dto.getNote() == null) {
            return Mono.error(new BadRequestAlertException("Note is required for rejection", "MonthlyTimeSheetReview",
                "noteRequired"));
        }
        return monthlyTimeSheetReviewRepository.findByMonth(dto.getMonth().withDayOfMonth(1), dto.getWorkspaceType(), dto.getTimeKeepingType())
            .flatMap(existingReview -> {
                if (existingReview.getStatus().equals(TimesheetReviewStatus.APPROVED)) {
                    return Mono.error(new BadRequestAlertException("Timesheet already approved cannot be modified",
                        "MonthlyTimeSheetReview", "timesheetApproved"));
                }
                return Mono.just(monthlyTimeSheetReviewMapper.toDto(existingReview));
            })
            .switchIfEmpty(save(dto))
            .flatMap(review -> {
                if (TimesheetReviewStatus.APPROVED.equals(dto.getStatus())) {
                    return personalMonthlyTimesheetService.bulkApprove(dto.getMonth(), review.getId(), dto.getTimeKeepingType()).then(Mono.just(review));
                } else {
                    return personalMonthlyTimesheetService.bulkReject(dto.getMonth(), review.getId(), dto.getTimeKeepingType()).then(Mono.just(review));
                }
            });
    }

    /**
     * Save a monthlyTimeSheetReview.
     *
     * @param dto the entity to save.
     * @return the persisted entity.
     */
    public Mono<MonthlyTimeSheetReviewDTO> save(ReviewTimeSheetDTO dto) {
        MonthlyTimeSheetReviewDTO monthlyTimeSheetReviewDTO = new MonthlyTimeSheetReviewDTO();
        monthlyTimeSheetReviewDTO.setCreatedDate(ZonedDateTime.now());
        monthlyTimeSheetReviewDTO.setId(UUID.randomUUID());
        monthlyTimeSheetReviewDTO.setLastUpdated(ZonedDateTime.now());
        monthlyTimeSheetReviewDTO.setStatus(dto.getStatus());
        monthlyTimeSheetReviewDTO.setNote(dto.getNote());
        monthlyTimeSheetReviewDTO.setSignatureFile(dto.getSignatureFile());
        return monthlyTimeSheetReviewRepository
            .save(monthlyTimeSheetReviewMapper.toEntity(monthlyTimeSheetReviewDTO))
            .map(monthlyTimeSheetReviewMapper::toDto);
    }

    public Mono<MonthlyTimeSheetReviewDTO> findByMonth(LocalDate month, WorkspaceType workspaceType, TimeKeepingType timeKeepingType) {
        return monthlyTimeSheetReviewRepository.findByMonth(month.withDayOfMonth(1), workspaceType, timeKeepingType).map(monthlyTimeSheetReviewMapper::toDto);
    }

    /**
     * Update a monthlyTimeSheetReview.
     *
     * @param monthlyTimeSheetReviewDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<MonthlyTimeSheetReviewDTO> update(MonthlyTimeSheetReviewDTO monthlyTimeSheetReviewDTO) {
        log.debug("Request to update MonthlyTimeSheetReview : {}", monthlyTimeSheetReviewDTO);
        return monthlyTimeSheetReviewRepository
            .save(monthlyTimeSheetReviewMapper.toEntity(monthlyTimeSheetReviewDTO).setIsPersisted())
            .map(monthlyTimeSheetReviewMapper::toDto);
    }

    /**
     * Partially update a monthlyTimeSheetReview.
     *
     * @param monthlyTimeSheetReviewDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<MonthlyTimeSheetReviewDTO> partialUpdate(MonthlyTimeSheetReviewDTO monthlyTimeSheetReviewDTO) {
        log.debug("Request to partially update MonthlyTimeSheetReview : {}", monthlyTimeSheetReviewDTO);

        return monthlyTimeSheetReviewRepository
            .findById(monthlyTimeSheetReviewDTO.getId())
            .map(existingMonthlyTimeSheetReview -> {
                monthlyTimeSheetReviewMapper.partialUpdate(existingMonthlyTimeSheetReview,
                    monthlyTimeSheetReviewDTO);

                return existingMonthlyTimeSheetReview;
            })
            .flatMap(monthlyTimeSheetReviewRepository::save)
            .map(monthlyTimeSheetReviewMapper::toDto);
    }

    /**
     * Get all the monthlyTimeSheetReviews.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<MonthlyTimeSheetReviewDTO> findAll(Pageable pageable) {
        log.debug("Request to get all MonthlyTimeSheetReviews");
        return monthlyTimeSheetReviewRepository.findAllBy(pageable).map(monthlyTimeSheetReviewMapper::toDto);
    }

    /**
     * Returns the number of monthlyTimeSheetReviews available.
     *
     * @return the number of entities in the database.
     */
    public Mono<Long> countAll() {
        return monthlyTimeSheetReviewRepository.count();
    }

    /**
     * Get one monthlyTimeSheetReview by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<MonthlyTimeSheetReviewDTO> findOne(UUID id) {
        log.debug("Request to get MonthlyTimeSheetReview : {}", id);
        return monthlyTimeSheetReviewRepository.findById(id).map(monthlyTimeSheetReviewMapper::toDto);
    }

    /**
     * Delete the monthlyTimeSheetReview by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(UUID id) {
        log.debug("Request to delete MonthlyTimeSheetReview : {}", id);
        return monthlyTimeSheetReviewRepository.deleteById(id);
    }
}
