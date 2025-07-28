package com.masi.logistics.service;

import com.masi.logistics.domain.criteria.TransactionTypeCriteria;
import com.masi.logistics.repository.TransactionTypeRepository;
import com.masi.logistics.service.dto.TransactionTypeDTO;
import com.masi.logistics.service.mapper.TransactionTypeMapper;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.masi.logistics.domain.TransactionType}.
 */
@Service
@Transactional
public class TransactionTypeService {

    private static final Logger log = LoggerFactory.getLogger(TransactionTypeService.class);

    private final TransactionTypeRepository transactionTypeRepository;

    private final TransactionTypeMapper transactionTypeMapper;

    public TransactionTypeService(TransactionTypeRepository transactionTypeRepository, TransactionTypeMapper transactionTypeMapper) {
        this.transactionTypeRepository = transactionTypeRepository;
        this.transactionTypeMapper = transactionTypeMapper;
    }

    /**
     * Save a transactionType.
     *
     * @param transactionTypeDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<TransactionTypeDTO> save(TransactionTypeDTO transactionTypeDTO) {
        log.debug("Request to save TransactionType : {}", transactionTypeDTO);
        return transactionTypeRepository.save(transactionTypeMapper.toEntity(transactionTypeDTO)).map(transactionTypeMapper::toDto);
    }

    /**
     * Update a transactionType.
     *
     * @param transactionTypeDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<TransactionTypeDTO> update(TransactionTypeDTO transactionTypeDTO) {
        log.debug("Request to update TransactionType : {}", transactionTypeDTO);
        return transactionTypeRepository
            .save(transactionTypeMapper.toEntity(transactionTypeDTO).setIsPersisted())
            .map(transactionTypeMapper::toDto);
    }

    /**
     * Partially update a transactionType.
     *
     * @param transactionTypeDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<TransactionTypeDTO> partialUpdate(TransactionTypeDTO transactionTypeDTO) {
        log.debug("Request to partially update TransactionType : {}", transactionTypeDTO);

        return transactionTypeRepository
            .findById(transactionTypeDTO.getId())
            .map(existingTransactionType -> {
                transactionTypeMapper.partialUpdate(existingTransactionType, transactionTypeDTO);

                return existingTransactionType;
            })
            .flatMap(transactionTypeRepository::save)
            .map(transactionTypeMapper::toDto);
    }

    /**
     * Find transactionTypes by Criteria.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<TransactionTypeDTO> findByCriteria(TransactionTypeCriteria criteria, Pageable pageable) {
        log.debug("Request to get all TransactionTypes by Criteria");
        return transactionTypeRepository.findByCriteria(criteria, pageable).map(transactionTypeMapper::toDto);
    }

    /**
     * Find the count of transactionTypes by criteria.
     * @param criteria filtering criteria
     * @return the count of transactionTypes
     */
    public Mono<Long> countByCriteria(TransactionTypeCriteria criteria) {
        log.debug("Request to get the count of all TransactionTypes by Criteria");
        return transactionTypeRepository.countByCriteria(criteria);
    }

    /**
     * Returns the number of transactionTypes available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return transactionTypeRepository.count();
    }

    /**
     * Get one transactionType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<TransactionTypeDTO> findOne(UUID id) {
        log.debug("Request to get TransactionType : {}", id);
        return transactionTypeRepository.findById(id).map(transactionTypeMapper::toDto);
    }

    /**
     * Delete the transactionType by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(UUID id) {
        log.debug("Request to delete TransactionType : {}", id);
        return transactionTypeRepository.deleteById(id);
    }
}
