package com.masi.logistics.service;

import com.masi.logistics.domain.criteria.TransactionOutCriteria;
import com.masi.logistics.repository.TransactionOutRepository;
import com.masi.logistics.service.dto.TransactionOutDTO;
import com.masi.logistics.service.mapper.TransactionOutMapper;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.masi.logistics.domain.TransactionOut}.
 */
@Service
@Transactional
public class TransactionOutService {

    private static final Logger log = LoggerFactory.getLogger(TransactionOutService.class);

    private final TransactionOutRepository transactionOutRepository;

    private final TransactionOutMapper transactionOutMapper;

    public TransactionOutService(TransactionOutRepository transactionOutRepository, TransactionOutMapper transactionOutMapper) {
        this.transactionOutRepository = transactionOutRepository;
        this.transactionOutMapper = transactionOutMapper;
    }

    /**
     * Save a transactionOut.
     *
     * @param transactionOutDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<TransactionOutDTO> save(TransactionOutDTO transactionOutDTO) {
        log.debug("Request to save TransactionOut : {}", transactionOutDTO);
        return transactionOutRepository.save(transactionOutMapper.toEntity(transactionOutDTO)).map(transactionOutMapper::toDto);
    }

    /**
     * Update a transactionOut.
     *
     * @param transactionOutDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<TransactionOutDTO> update(TransactionOutDTO transactionOutDTO) {
        log.debug("Request to update TransactionOut : {}", transactionOutDTO);
        return transactionOutRepository
            .save(transactionOutMapper.toEntity(transactionOutDTO).setIsPersisted())
            .map(transactionOutMapper::toDto);
    }

    /**
     * Partially update a transactionOut.
     *
     * @param transactionOutDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<TransactionOutDTO> partialUpdate(TransactionOutDTO transactionOutDTO) {
        log.debug("Request to partially update TransactionOut : {}", transactionOutDTO);

        return transactionOutRepository
            .findById(transactionOutDTO.getId())
            .map(existingTransactionOut -> {
                transactionOutMapper.partialUpdate(existingTransactionOut, transactionOutDTO);

                return existingTransactionOut;
            })
            .flatMap(transactionOutRepository::save)
            .map(transactionOutMapper::toDto);
    }

    /**
     * Find transactionOuts by Criteria.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<TransactionOutDTO> findByCriteria(TransactionOutCriteria criteria, Pageable pageable) {
        log.debug("Request to get all TransactionOuts by Criteria");
        return transactionOutRepository.findByCriteria(criteria, pageable).map(transactionOutMapper::toDto);
    }

    /**
     * Find the count of transactionOuts by criteria.
     * @param criteria filtering criteria
     * @return the count of transactionOuts
     */
    public Mono<Long> countByCriteria(TransactionOutCriteria criteria) {
        log.debug("Request to get the count of all TransactionOuts by Criteria");
        return transactionOutRepository.countByCriteria(criteria);
    }

    /**
     * Returns the number of transactionOuts available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return transactionOutRepository.count();
    }

    /**
     * Get one transactionOut by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<TransactionOutDTO> findOne(UUID id) {
        log.debug("Request to get TransactionOut : {}", id);
        return transactionOutRepository.findById(id).map(transactionOutMapper::toDto);
    }

    /**
     * Delete the transactionOut by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(UUID id) {
        log.debug("Request to delete TransactionOut : {}", id);
        return transactionOutRepository.deleteById(id);
    }
}
