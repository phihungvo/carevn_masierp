package com.masi.logistics.service;

import com.masi.logistics.domain.criteria.CurrencyCriteria;
import com.masi.logistics.repository.CurrencyRepository;
import com.masi.logistics.service.dto.CurrencyDTO;
import com.masi.logistics.service.mapper.CurrencyMapper;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.masi.logistics.domain.Currency}.
 */
@Service
@Transactional
public class CurrencyService {

    private static final Logger log = LoggerFactory.getLogger(CurrencyService.class);

    private final CurrencyRepository currencyRepository;

    private final CurrencyMapper currencyMapper;

    public CurrencyService(CurrencyRepository currencyRepository, CurrencyMapper currencyMapper) {
        this.currencyRepository = currencyRepository;
        this.currencyMapper = currencyMapper;
    }

    /**
     * Save a currency.
     *
     * @param currencyDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<CurrencyDTO> save(CurrencyDTO currencyDTO) {
        log.debug("Request to save Currency : {}", currencyDTO);
        return currencyRepository.save(currencyMapper.toEntity(currencyDTO)).map(currencyMapper::toDto);
    }

    /**
     * Update a currency.
     *
     * @param currencyDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<CurrencyDTO> update(CurrencyDTO currencyDTO) {
        log.debug("Request to update Currency : {}", currencyDTO);
        return currencyRepository.save(currencyMapper.toEntity(currencyDTO).setIsPersisted()).map(currencyMapper::toDto);
    }

    /**
     * Partially update a currency.
     *
     * @param currencyDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<CurrencyDTO> partialUpdate(CurrencyDTO currencyDTO) {
        log.debug("Request to partially update Currency : {}", currencyDTO);

        return currencyRepository
            .findById(currencyDTO.getId())
            .map(existingCurrency -> {
                currencyMapper.partialUpdate(existingCurrency, currencyDTO);

                return existingCurrency;
            })
            .flatMap(currencyRepository::save)
            .map(currencyMapper::toDto);
    }

    /**
     * Find currencies by Criteria.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<CurrencyDTO> findByCriteria(CurrencyCriteria criteria, Pageable pageable) {
        log.debug("Request to get all Currencies by Criteria");
        return currencyRepository.findByCriteria(criteria, pageable).map(currencyMapper::toDto);
    }

    /**
     * Find the count of currencies by criteria.
     * @param criteria filtering criteria
     * @return the count of currencies
     */
    public Mono<Long> countByCriteria(CurrencyCriteria criteria) {
        log.debug("Request to get the count of all Currencies by Criteria");
        return currencyRepository.countByCriteria(criteria);
    }

    /**
     * Returns the number of currencies available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return currencyRepository.count();
    }

    /**
     * Get one currency by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<CurrencyDTO> findOne(UUID id) {
        log.debug("Request to get Currency : {}", id);
        return currencyRepository.findById(id).map(currencyMapper::toDto);
    }

    /**
     * Delete the currency by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(UUID id) {
        log.debug("Request to delete Currency : {}", id);
        return currencyRepository.deleteById(id);
    }
}
