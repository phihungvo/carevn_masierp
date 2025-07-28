package com.masi.logistics.service;

import com.masi.logistics.domain.criteria.VatRateCriteria;
import com.masi.logistics.repository.VatRateRepository;
import com.masi.logistics.service.dto.VatRateDTO;
import com.masi.logistics.service.mapper.VatRateMapper;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.masi.logistics.domain.VatRate}.
 */
@Service
@Transactional
public class VatRateService {

    private static final Logger log = LoggerFactory.getLogger(VatRateService.class);

    private final VatRateRepository vatRateRepository;

    private final VatRateMapper vatRateMapper;

    public VatRateService(VatRateRepository vatRateRepository, VatRateMapper vatRateMapper) {
        this.vatRateRepository = vatRateRepository;
        this.vatRateMapper = vatRateMapper;
    }

    /**
     * Save a vatRate.
     *
     * @param vatRateDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<VatRateDTO> save(VatRateDTO vatRateDTO) {
        log.debug("Request to save VatRate : {}", vatRateDTO);
        return vatRateRepository.save(vatRateMapper.toEntity(vatRateDTO)).map(vatRateMapper::toDto);
    }

    /**
     * Update a vatRate.
     *
     * @param vatRateDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<VatRateDTO> update(VatRateDTO vatRateDTO) {
        log.debug("Request to update VatRate : {}", vatRateDTO);
        return vatRateRepository.save(vatRateMapper.toEntity(vatRateDTO).setIsPersisted()).map(vatRateMapper::toDto);
    }

    /**
     * Partially update a vatRate.
     *
     * @param vatRateDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<VatRateDTO> partialUpdate(VatRateDTO vatRateDTO) {
        log.debug("Request to partially update VatRate : {}", vatRateDTO);

        return vatRateRepository
            .findById(vatRateDTO.getId())
            .map(existingVatRate -> {
                vatRateMapper.partialUpdate(existingVatRate, vatRateDTO);

                return existingVatRate;
            })
            .flatMap(vatRateRepository::save)
            .map(vatRateMapper::toDto);
    }

    /**
     * Find vatRates by Criteria.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<VatRateDTO> findByCriteria(VatRateCriteria criteria, Pageable pageable) {
        log.debug("Request to get all VatRates by Criteria");
        return vatRateRepository.findByCriteria(criteria, pageable).map(vatRateMapper::toDto);
    }

    /**
     * Find the count of vatRates by criteria.
     * @param criteria filtering criteria
     * @return the count of vatRates
     */
    public Mono<Long> countByCriteria(VatRateCriteria criteria) {
        log.debug("Request to get the count of all VatRates by Criteria");
        return vatRateRepository.countByCriteria(criteria);
    }

    /**
     * Returns the number of vatRates available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return vatRateRepository.count();
    }

    /**
     * Get one vatRate by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<VatRateDTO> findOne(UUID id) {
        log.debug("Request to get VatRate : {}", id);
        return vatRateRepository.findById(id).map(vatRateMapper::toDto);
    }

    /**
     * Delete the vatRate by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(UUID id) {
        log.debug("Request to delete VatRate : {}", id);
        return vatRateRepository.deleteById(id);
    }
}
