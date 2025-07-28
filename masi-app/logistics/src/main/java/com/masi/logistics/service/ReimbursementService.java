package com.masi.logistics.service;

import com.masi.logistics.domain.criteria.ReimbursementCriteria;
import com.masi.logistics.repository.ReimbursementRepository;
import com.masi.logistics.service.dto.ReimbursementDTO;
import com.masi.logistics.service.mapper.ReimbursementMapper;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.masi.logistics.domain.Reimbursement}.
 */
@Service
@Transactional
public class ReimbursementService {

    private static final Logger log = LoggerFactory.getLogger(ReimbursementService.class);

    private final ReimbursementRepository reimbursementRepository;

    private final ReimbursementMapper reimbursementMapper;

    public ReimbursementService(ReimbursementRepository reimbursementRepository, ReimbursementMapper reimbursementMapper) {
        this.reimbursementRepository = reimbursementRepository;
        this.reimbursementMapper = reimbursementMapper;
    }

    /**
     * Save a reimbursement.
     *
     * @param reimbursementDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ReimbursementDTO> save(ReimbursementDTO reimbursementDTO) {
        log.debug("Request to save Reimbursement : {}", reimbursementDTO);
        return reimbursementRepository.save(reimbursementMapper.toEntity(reimbursementDTO)).map(reimbursementMapper::toDto);
    }

    /**
     * Update a reimbursement.
     *
     * @param reimbursementDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ReimbursementDTO> update(ReimbursementDTO reimbursementDTO) {
        log.debug("Request to update Reimbursement : {}", reimbursementDTO);
        return reimbursementRepository
            .save(reimbursementMapper.toEntity(reimbursementDTO).setIsPersisted())
            .map(reimbursementMapper::toDto);
    }

    /**
     * Partially update a reimbursement.
     *
     * @param reimbursementDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<ReimbursementDTO> partialUpdate(ReimbursementDTO reimbursementDTO) {
        log.debug("Request to partially update Reimbursement : {}", reimbursementDTO);

        return reimbursementRepository
            .findById(reimbursementDTO.getId())
            .map(existingReimbursement -> {
                reimbursementMapper.partialUpdate(existingReimbursement, reimbursementDTO);

                return existingReimbursement;
            })
            .flatMap(reimbursementRepository::save)
            .map(reimbursementMapper::toDto);
    }

    /**
     * Find reimbursements by Criteria.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<ReimbursementDTO> findByCriteria(ReimbursementCriteria criteria, Pageable pageable) {
        log.debug("Request to get all Reimbursements by Criteria");
        return reimbursementRepository.findByCriteria(criteria, pageable).map(reimbursementMapper::toDto);
    }

    /**
     * Find the count of reimbursements by criteria.
     * @param criteria filtering criteria
     * @return the count of reimbursements
     */
    public Mono<Long> countByCriteria(ReimbursementCriteria criteria) {
        log.debug("Request to get the count of all Reimbursements by Criteria");
        return reimbursementRepository.countByCriteria(criteria);
    }

    /**
     * Returns the number of reimbursements available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return reimbursementRepository.count();
    }

    /**
     * Get one reimbursement by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<ReimbursementDTO> findOne(UUID id) {
        log.debug("Request to get Reimbursement : {}", id);
        return reimbursementRepository.findById(id).map(reimbursementMapper::toDto);
    }

    /**
     * Delete the reimbursement by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(UUID id) {
        log.debug("Request to delete Reimbursement : {}", id);
        return reimbursementRepository.deleteById(id);
    }
}
