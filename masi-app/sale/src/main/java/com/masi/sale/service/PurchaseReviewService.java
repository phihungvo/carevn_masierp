package com.masi.sale.service;

import com.masi.sale.domain.PurchaseRequest;
import com.masi.sale.domain.PurchaseReview;
import com.masi.sale.domain.enumeration.PurchaseRequestStatus;
import com.masi.sale.domain.enumeration.PurchaseReviewStatus;
import com.masi.sale.repository.PurchaseRequestRepository;
import com.masi.sale.repository.PurchaseReviewRepository;
import com.masi.sale.service.dto.PurchaseRequestDTO;
import com.masi.sale.service.dto.PurchaseReviewDTO;
import com.masi.sale.service.dto.RequestReviewDTO;
import com.masi.sale.service.mapper.PurchaseReviewMapper;

import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import com.masi.sale.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.masi.sale.domain.PurchaseReview}.
 */
@Service
@Transactional
public class PurchaseReviewService {

    private static final Logger log = LoggerFactory.getLogger(PurchaseReviewService.class);

    private final PurchaseReviewRepository purchaseReviewRepository;

    private final PurchaseReviewMapper purchaseReviewMapper;
    private final PurchaseRequestRepository purchaseRequestRepository;

    public PurchaseReviewService(PurchaseReviewRepository purchaseReviewRepository, PurchaseReviewMapper purchaseReviewMapper, PurchaseRequestRepository purchaseRequestRepository) {
        this.purchaseReviewRepository = purchaseReviewRepository;
        this.purchaseReviewMapper = purchaseReviewMapper;
        this.purchaseRequestRepository = purchaseRequestRepository;
    }

    public Mono<Void> requestReview(RequestReviewDTO dto) {
        Collection<UUID> reviewers = dto.getEmployeeIds();
        if (reviewers.size() > 4) {
            return Mono.error(new BadRequestAlertException("Too many reviewers", "orderReview", "tooManyReviewers"));
        }
        return purchaseRequestRepository.findById(dto.getDocumentId())
            .switchIfEmpty(Mono.error(new BadRequestAlertException("Order not found", "orderReview", "orderNotFound")))
            .flatMap(entity -> {
                entity.setRequestStatus(PurchaseRequestStatus.WAITING_APPROVAL);

                return purchaseRequestRepository.save(entity.setIsPersisted());
            })
            .flatMap(orderDTO -> Flux.fromIterable(reviewers)
                .flatMap(reviewerId -> {
                    PurchaseReview orderReview = new PurchaseReview()
                        .id(UUID.randomUUID())
                        .lastUpdated(ZonedDateTime.now())
                        .createdDate(ZonedDateTime.now())
                        .employeeId(reviewerId)
                        .status(PurchaseReviewStatus.PENDING)
                        .purchaseRequestId(orderDTO.getId());
                    return purchaseReviewRepository.save(orderReview);
                }).then());
    }


    public Mono<PurchaseReviewDTO> review(PurchaseReviewDTO dto) {
        return purchaseReviewRepository.findById(dto.getId())
            .switchIfEmpty(Mono.error(new BadRequestAlertException("PurchaseReview not found", "purchaseReview", "purchaseReviewNotFound")))
            .<PurchaseReview>handle((purchaseReview, sink) -> {
                if (!PurchaseRequestStatus.WAITING_APPROVAL.equals(purchaseReview.getPurchaseRequest().getRequestStatus())) {
                    sink.error(new BadRequestAlertException("PurchaseRequest is not waiting for approval", "purchaseReview",
                        "purchaseRequestNotWaiting"));
                    return;
                }

                sink.next(dto.applyReview(purchaseReview).setIsPersisted());
            })
            .flatMap(purchaseReviewRepository::save)
            .flatMap(entity -> autoUpdateStatus(entity.getPurchaseRequestId()).thenReturn(entity))
            .map(purchaseReviewMapper::toDto);
    }

    public Mono<PurchaseRequestDTO> autoUpdateStatus(UUID purchaseRequestId) {
        Mono<PurchaseRequest> purchaseRequestDto = purchaseRequestRepository.getFirstByIdAndIsDeletedFalse(purchaseRequestId);
        return purchaseRequestDto.zipWith(purchaseReviewRepository.findByPurchaseRequest(purchaseRequestId).collectList())
            .map(tuple -> {
                PurchaseRequest purchaseRequest = tuple.getT1();
                List<PurchaseReview> reviews = tuple.getT2();
                if (reviews.stream().anyMatch(review -> PurchaseReviewStatus.PENDING.equals(review.getStatus()))) {
                    purchaseRequest.setRequestStatus(PurchaseRequestStatus.WAITING_APPROVAL);
                } else if (reviews.stream().anyMatch(review -> PurchaseReviewStatus.REJECTED.equals(review.getStatus()))) {
                    purchaseRequest.setRequestStatus(PurchaseRequestStatus.REJECTED);
                } else {
                    purchaseRequest.setRequestStatus(PurchaseRequestStatus.APPROVED);
                }
                return purchaseRequest.setIsPersisted();
            })
            .flatMap(purchaseRequestRepository::save)
            .map(PurchaseRequest::toDTO);
    }

    public Flux<PurchaseReviewDTO> findAllByPurchaseRequest(UUID purchaseRequestId) {
        return purchaseReviewRepository.findByPurchaseRequest(purchaseRequestId).map(purchaseReviewMapper::toDto);
    }

    public Mono<Void> deleteByPurchaseRequestId(UUID purchaseRequestId) {
        return purchaseReviewRepository.deleteByPurchaseRequest(purchaseRequestId);
    }
    /**
     * Update a purchaseReview.
     *
     * @param purchaseReviewDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<PurchaseReviewDTO> update(PurchaseReviewDTO purchaseReviewDTO) {
        log.debug("Request to update PurchaseReview : {}", purchaseReviewDTO);
        return purchaseReviewRepository
            .save(purchaseReviewMapper.toEntity(purchaseReviewDTO).setIsPersisted())
            .map(purchaseReviewMapper::toDto);
    }

    /**
     * Partially update a purchaseReview.
     *
     * @param purchaseReviewDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<PurchaseReviewDTO> partialUpdate(PurchaseReviewDTO purchaseReviewDTO) {
        log.debug("Request to partially update PurchaseReview : {}", purchaseReviewDTO);

        return purchaseReviewRepository
            .findById(purchaseReviewDTO.getId())
            .map(existingPurchaseReview -> {
                purchaseReviewMapper.partialUpdate(existingPurchaseReview, purchaseReviewDTO);
                existingPurchaseReview.setIsPersisted();
                return existingPurchaseReview;
            })
            .flatMap(purchaseReviewRepository::save)
            .map(purchaseReviewMapper::toDto);
    }

    /**
     * Get all the purchaseReviews.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<PurchaseReviewDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PurchaseReviews");
        return purchaseReviewRepository.findAllBy(pageable).map(purchaseReviewMapper::toDto);
    }


    /**
     * Get one purchaseReview by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<PurchaseReviewDTO> findOne(UUID id) {
        log.debug("Request to get PurchaseReview : {}", id);
        return purchaseReviewRepository.findById(id).map(purchaseReviewMapper::toDto);
    }

    /**
     * Delete the purchaseReview by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(UUID id) {
        log.debug("Request to delete PurchaseReview : {}", id);
        return purchaseReviewRepository.deleteById(id);
    }
}
