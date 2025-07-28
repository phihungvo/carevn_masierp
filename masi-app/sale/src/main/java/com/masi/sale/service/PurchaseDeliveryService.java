package com.masi.sale.service;

import com.masi.sale.domain.PurchaseDelivery;
import com.masi.sale.domain.enumeration.PurchaseRequestStatus;
import com.masi.sale.repository.PurchaseDeliveryRepository;
import com.masi.sale.repository.PurchaseRequestRepository;
import com.masi.sale.service.dto.PurchaseDeliveryDTO;
import com.masi.sale.service.mapper.PurchaseDeliveryMapper;

import java.time.ZonedDateTime;
import java.util.UUID;

import com.masi.sale.web.rest.errors.BadRequestAlertException;
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
 * Service Implementation for managing
 * {@link com.masi.sale.domain.PurchaseDelivery}.
 */
@Service
@Transactional
public class PurchaseDeliveryService {

    private static final Logger log = LoggerFactory.getLogger(PurchaseDeliveryService.class);

    private final PurchaseDeliveryRepository purchaseDeliveryRepository;

    private final PurchaseDeliveryMapper purchaseDeliveryMapper;
    private final PurchaseRequestRepository purchaseRequestRepository;

    public PurchaseDeliveryService(PurchaseDeliveryRepository purchaseDeliveryRepository,
            PurchaseDeliveryMapper purchaseDeliveryMapper, PurchaseRequestRepository purchaseRequestRepository) {
        this.purchaseDeliveryRepository = purchaseDeliveryRepository;
        this.purchaseDeliveryMapper = purchaseDeliveryMapper;
        this.purchaseRequestRepository = purchaseRequestRepository;
    }

    /**
     * Save a purchaseDelivery.
     *
     * @param purchaseDeliveryDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<PurchaseDeliveryDTO> save(PurchaseDeliveryDTO purchaseDeliveryDTO) {
        var entity = purchaseDeliveryDTO.toEntity();

        return purchaseRequestRepository.getFirstByIdAndIsDeletedFalse(entity.getPurchaseRequestId())
                .switchIfEmpty(Mono.error(
                        new BadRequestAlertException("Invalid purchase request id", "purchaseRequest", "notfound")))
                .flatMap(purchaseRequestDTO -> {
                    entity.setPurchaseRequestId(purchaseRequestDTO.getId());
                    return purchaseDeliveryRepository.save(entity).map(purchaseDeliveryMapper::toDto);
                });
    }

    @Transactional(readOnly = true)
    public Mono<PurchaseDeliveryDTO> findOneByPurchaseRequestId(UUID purchaseRequestId) {
        return purchaseDeliveryRepository.findFirstByPurchaseRequestId(purchaseRequestId)
                .map(purchaseDeliveryMapper::toDto);
    }

    public Flux<PurchaseDeliveryDTO> findAllByPurchaseRequestId(UUID purchaseRequestId) {
        return purchaseDeliveryRepository.findByPurchaseRequest(purchaseRequestId).map(purchaseDeliveryMapper::toDto);
    }

    /**
     * Update a purchaseDelivery.
     *
     * @param purchaseDeliveryDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<PurchaseDeliveryDTO> update(PurchaseDeliveryDTO purchaseDeliveryDTO) {
        log.debug("Request to update PurchaseDelivery : {}", purchaseDeliveryDTO);
        return purchaseDeliveryRepository
                .save(purchaseDeliveryMapper.toEntity(purchaseDeliveryDTO).setIsPersisted())
                .map(purchaseDeliveryMapper::toDto);
    }

    /**
     * Partially update a purchaseDelivery.
     *
     * @param purchaseDeliveryDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<PurchaseDeliveryDTO> partialUpdate(PurchaseDeliveryDTO purchaseDeliveryDTO) {
        log.debug("Request to partially update PurchaseDelivery : {}", purchaseDeliveryDTO);

        return purchaseDeliveryRepository
                .findById(purchaseDeliveryDTO.getId())
                .switchIfEmpty(
                        Mono.error(new BadRequestAlertException("Entity not found", "PurchaseDelivery", "idnotfound")))
                .flatMap(existingPurchaseDelivery -> {
                    existingPurchaseDelivery.setDeliveried(purchaseDeliveryDTO.getDeliveried());
                    existingPurchaseDelivery.setUpdatedDate(ZonedDateTime.now());
                    return purchaseRequestRepository.findById(existingPurchaseDelivery.getPurchaseRequestId())
                            .flatMap(purchaseRequestDTO -> {
                                if (purchaseRequestDTO.getTotalPrice() < existingPurchaseDelivery.getDeliveried()) {
                                    return Mono.error(new BadRequestAlertException(
                                            "Deliveried quantity is greater than total price", "PurchaseDelivery",
                                            "invalidquantity"));
                                }
                                existingPurchaseDelivery.setWaitingDelivery(
                                        purchaseRequestDTO.getQuantity() - existingPurchaseDelivery.getDeliveried());
                                if (existingPurchaseDelivery.getWaitingDelivery() <= 0) {
                                    purchaseRequestDTO.setRequestStatus(PurchaseRequestStatus.FINISHED);
                                } else {
                                    purchaseRequestDTO.setRequestStatus(PurchaseRequestStatus.DELIVERING);
                                }
                                return purchaseRequestRepository.save(purchaseRequestDTO.setIsPersisted())
                                        .then(Mono.just(existingPurchaseDelivery.setIsPersisted()));
                            });
                })
                .flatMap(purchaseDeliveryRepository::save)
                .map(purchaseDeliveryMapper::toDto);
    }

    /**
     * Get all the purchaseDeliveries.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<PurchaseDeliveryDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PurchaseDeliveries");
        return purchaseDeliveryRepository.findAllBy(pageable).map(purchaseDeliveryMapper::toDto);
    }

    /**
     * Returns the number of purchaseDeliveries available.
     *
     * @return the number of entities in the database.
     */
    public Mono<Long> countAll() {
        return purchaseDeliveryRepository.count();
    }

    /**
     * Get one purchaseDelivery by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<PurchaseDeliveryDTO> findOne(UUID id) {
        log.debug("Request to get PurchaseDelivery : {}", id);
        return purchaseDeliveryRepository.findById(id).map(purchaseDeliveryMapper::toDto);
    }

    /**
     * Delete the purchaseDelivery by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(UUID id) {
        log.debug("Request to delete PurchaseDelivery : {}", id);
        return purchaseDeliveryRepository.deleteById(id);
    }
}
