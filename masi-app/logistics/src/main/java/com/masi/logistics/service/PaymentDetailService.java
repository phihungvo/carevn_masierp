package com.masi.logistics.service;

import com.masi.logistics.domain.criteria.PaymentDetailCriteria;
import com.masi.logistics.repository.PaymentDetailRepository;
import com.masi.logistics.service.dto.PaymentDetailDTO;
import com.masi.logistics.service.mapper.PaymentDetailMapper;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.masi.logistics.domain.PaymentDetail}.
 */
@Service
@Transactional
public class PaymentDetailService {

    private static final Logger LOG = LoggerFactory.getLogger(PaymentDetailService.class);

    private final PaymentDetailRepository paymentDetailRepository;

    private final PaymentDetailMapper paymentDetailMapper;

    public PaymentDetailService(PaymentDetailRepository paymentDetailRepository, PaymentDetailMapper paymentDetailMapper) {
        this.paymentDetailRepository = paymentDetailRepository;
        this.paymentDetailMapper = paymentDetailMapper;
    }

    /**
     * Save a paymentDetail.
     *
     * @param paymentDetailDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<PaymentDetailDTO> save(PaymentDetailDTO paymentDetailDTO) {
        LOG.debug("Request to save PaymentDetail : {}", paymentDetailDTO);
        return paymentDetailRepository.save(paymentDetailDTO.toEntity()).map(paymentDetailMapper::toDto);
    }

    /**
     * Update a paymentDetail.
     *
     * @param paymentDetailDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<PaymentDetailDTO> update(PaymentDetailDTO paymentDetailDTO) {
        LOG.debug("Request to update PaymentDetail : {}", paymentDetailDTO);
        return paymentDetailRepository
            .save(paymentDetailMapper.toEntity(paymentDetailDTO).setIsPersisted())
            .map(paymentDetailMapper::toDto);
    }

    /**
     * Partially update a paymentDetail.
     *
     * @param paymentDetailDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<PaymentDetailDTO> partialUpdate(PaymentDetailDTO paymentDetailDTO) {
        LOG.debug("Request to partially update PaymentDetail : {}", paymentDetailDTO);

        return paymentDetailRepository
            .findById(paymentDetailDTO.getId())
            .map(existingPaymentDetail -> {
                paymentDetailMapper.partialUpdate(existingPaymentDetail, paymentDetailDTO);

                return existingPaymentDetail;
            })
            .flatMap(paymentDetailRepository::save)
            .map(paymentDetailMapper::toDto);
    }

    /**
     * Find paymentDetails by Criteria.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<PaymentDetailDTO> findByCriteria(PaymentDetailCriteria criteria, Pageable pageable) {
        LOG.debug("Request to get all PaymentDetails by Criteria");
        return paymentDetailRepository.findByCriteria(criteria, pageable).map(paymentDetailMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Flux<PaymentDetailDTO> findByRequestId(UUID requestId, Pageable pageable) {
        LOG.debug("Request to get all PaymentDetails by requestId");
        return paymentDetailRepository.findByPaymentRequestId(requestId, pageable).map(paymentDetailMapper::toDto);
    }

    /**
     * Find the count of paymentDetails by criteria.
     * @param criteria filtering criteria
     * @return the count of paymentDetails
     */
    public Mono<Long> countByCriteria(PaymentDetailCriteria criteria) {
        LOG.debug("Request to get the count of all PaymentDetails by Criteria");
        return paymentDetailRepository.countByCriteria(criteria);
    }

    public Mono<Long> countByRequestId(UUID requestId) {
        LOG.debug("Request to get the count of all PaymentDetails by Criteria");
        return paymentDetailRepository.countByPaymentRequestId(requestId);
    }

    /**
     * Returns the number of paymentDetails available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return paymentDetailRepository.count();
    }

    /**
     * Get one paymentDetail by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<PaymentDetailDTO> findOne(UUID id) {
        LOG.debug("Request to get PaymentDetail : {}", id);
        return paymentDetailRepository.findById(id).map(paymentDetailMapper::toDto);
    }

    /**
     * Delete the paymentDetail by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(UUID id) {
        LOG.debug("Request to delete PaymentDetail : {}", id);
        return paymentDetailRepository.deleteById(id);
    }
}
