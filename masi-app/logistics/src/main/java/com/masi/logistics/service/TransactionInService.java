package com.masi.logistics.service;

import com.masi.logistics.domain.criteria.TransactionInCriteria;
import com.masi.logistics.repository.TransactionInRepository;
import com.masi.logistics.service.dto.TransactionInDTO;
import com.masi.logistics.service.mapper.TransactionInMapper;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.masi.logistics.domain.TransactionIn}.
 */
@Service
@Transactional
public class TransactionInService {

    private static final Logger log = LoggerFactory.getLogger(TransactionInService.class);

    private final TransactionInRepository transactionInRepository;

    private final TransactionInMapper transactionInMapper;

    public TransactionInService(TransactionInRepository transactionInRepository, TransactionInMapper transactionInMapper) {
        this.transactionInRepository = transactionInRepository;
        this.transactionInMapper = transactionInMapper;
    }

    /**
     * Save a transactionIn.
     *
     * @param transactionInDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<TransactionInDTO> save(TransactionInDTO transactionInDTO) {
        log.debug("Request to save TransactionIn : {}", transactionInDTO);
        return transactionInRepository.save(transactionInMapper.toEntity(transactionInDTO)).map(transactionInMapper::toDto);
    }

    /**
     * Update a transactionIn.
     *
     * @param transactionInDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<TransactionInDTO> update(TransactionInDTO transactionInDTO) {
        log.debug("Request to update TransactionIn : {}", transactionInDTO);
        return transactionInRepository
            .save(transactionInMapper.toEntity(transactionInDTO).setIsPersisted())
            .map(transactionInMapper::toDto);
    }

    /**
     * Partially update a transactionIn.
     *
     * @param transactionInDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<TransactionInDTO> partialUpdate(TransactionInDTO transactionInDTO) {
        log.debug("Request to partially update TransactionIn : {}", transactionInDTO);

        return transactionInRepository
            .findById(transactionInDTO.getId())
            .map(existingTransactionIn -> {
                transactionInMapper.partialUpdate(existingTransactionIn, transactionInDTO);

                return existingTransactionIn;
            })
            .flatMap(transactionInRepository::save)
            .map(transactionInMapper::toDto);
    }

    /**
     * Find transactionIns by Criteria.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<TransactionInDTO> findByCriteria(TransactionInCriteria criteria, Pageable pageable) {
        log.debug("Request to get all TransactionIns by Criteria");
        return transactionInRepository.findByCriteria(criteria, pageable).map(transactionInMapper::toDto);
    }

    /**
     * Find the count of transactionIns by criteria.
     * @param criteria filtering criteria
     * @return the count of transactionIns
     */
    public Mono<Long> countByCriteria(TransactionInCriteria criteria) {
        log.debug("Request to get the count of all TransactionIns by Criteria");
        return transactionInRepository.countByCriteria(criteria);
    }

    /**
     * Returns the number of transactionIns available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return transactionInRepository.count();
    }

    /**
     * Get one transactionIn by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<TransactionInDTO> findOne(UUID id) {
        log.debug("Request to get TransactionIn : {}", id);
        return transactionInRepository.findById(id).map(transactionInMapper::toDto);
    }

    /**
     * Delete the transactionIn by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(UUID id) {
        log.debug("Request to delete TransactionIn : {}", id);
        return transactionInRepository.deleteById(id);
    }
}
