package com.masi.logistics.service;

import com.masi.logistics.domain.criteria.RelatedCostsCriteria;
import com.masi.logistics.repository.RelatedCostsRepository;
import com.masi.logistics.service.dto.RelatedCostsDTO;
import com.masi.logistics.service.mapper.RelatedCostsMapper;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.masi.logistics.domain.RelatedCosts}.
 */
@Service
@Transactional
public class RelatedCostsService {

    private static final Logger log = LoggerFactory.getLogger(RelatedCostsService.class);

    private final RelatedCostsRepository relatedCostsRepository;

    private final RelatedCostsMapper relatedCostsMapper;

    public RelatedCostsService(RelatedCostsRepository relatedCostsRepository, RelatedCostsMapper relatedCostsMapper) {
        this.relatedCostsRepository = relatedCostsRepository;
        this.relatedCostsMapper = relatedCostsMapper;
    }

    /**
     * Save a relatedCosts.
     *
     * @param relatedCostsDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<RelatedCostsDTO> save(RelatedCostsDTO relatedCostsDTO) {
        log.debug("Request to save RelatedCosts : {}", relatedCostsDTO);
        return relatedCostsRepository.save(relatedCostsMapper.toEntity(relatedCostsDTO)).map(relatedCostsMapper::toDto);
    }

    /**
     * Update a relatedCosts.
     *
     * @param relatedCostsDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<RelatedCostsDTO> update(RelatedCostsDTO relatedCostsDTO) {
        log.debug("Request to update RelatedCosts : {}", relatedCostsDTO);
        return relatedCostsRepository.save(relatedCostsMapper.toEntity(relatedCostsDTO).setIsPersisted()).map(relatedCostsMapper::toDto);
    }

    /**
     * Partially update a relatedCosts.
     *
     * @param relatedCostsDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<RelatedCostsDTO> partialUpdate(RelatedCostsDTO relatedCostsDTO) {
        log.debug("Request to partially update RelatedCosts : {}", relatedCostsDTO);

        return relatedCostsRepository
            .findById(relatedCostsDTO.getId())
            .map(existingRelatedCosts -> {
                relatedCostsMapper.partialUpdate(existingRelatedCosts, relatedCostsDTO);

                return existingRelatedCosts;
            })
            .flatMap(relatedCostsRepository::save)
            .map(relatedCostsMapper::toDto);
    }

    /**
     * Find relatedCosts by Criteria.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<RelatedCostsDTO> findByCriteria(RelatedCostsCriteria criteria, Pageable pageable) {
        log.debug("Request to get all RelatedCosts by Criteria");
        return relatedCostsRepository.findByCriteria(criteria, pageable).map(relatedCostsMapper::toDto);
    }

    /**
     * Find the count of relatedCosts by criteria.
     * @param criteria filtering criteria
     * @return the count of relatedCosts
     */
    public Mono<Long> countByCriteria(RelatedCostsCriteria criteria) {
        log.debug("Request to get the count of all RelatedCosts by Criteria");
        return relatedCostsRepository.countByCriteria(criteria);
    }

    /**
     * Returns the number of relatedCosts available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return relatedCostsRepository.count();
    }

    /**
     * Get one relatedCosts by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<RelatedCostsDTO> findOne(UUID id) {
        log.debug("Request to get RelatedCosts : {}", id);
        return relatedCostsRepository.findById(id).map(relatedCostsMapper::toDto);
    }

    /**
     * Delete the relatedCosts by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(UUID id) {
        log.debug("Request to delete RelatedCosts : {}", id);
        return relatedCostsRepository.deleteById(id);
    }
}
