package com.masi.logistics.service;

import com.masi.logistics.domain.criteria.InventoriesCheckDetailCriteria;
import com.masi.logistics.repository.InventoriesCheckDetailRepository;
import com.masi.logistics.service.dto.InventoriesCheckDetailDTO;
import com.masi.logistics.service.mapper.InventoriesCheckDetailMapper;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.masi.logistics.domain.InventoriesCheckDetail}.
 */
@Service
@Transactional
public class InventoriesCheckDetailService {

    private static final Logger log = LoggerFactory.getLogger(InventoriesCheckDetailService.class);

    private final InventoriesCheckDetailRepository inventoriesCheckDetailRepository;

    private final InventoriesCheckDetailMapper inventoriesCheckDetailMapper;

    public InventoriesCheckDetailService(
        InventoriesCheckDetailRepository inventoriesCheckDetailRepository,
        InventoriesCheckDetailMapper inventoriesCheckDetailMapper
    ) {
        this.inventoriesCheckDetailRepository = inventoriesCheckDetailRepository;
        this.inventoriesCheckDetailMapper = inventoriesCheckDetailMapper;
    }

    /**
     * Save a inventoriesCheckDetail.
     *
     * @param inventoriesCheckDetailDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<InventoriesCheckDetailDTO> save(InventoriesCheckDetailDTO inventoriesCheckDetailDTO) {
        log.debug("Request to save InventoriesCheckDetail : {}", inventoriesCheckDetailDTO);
        return inventoriesCheckDetailRepository
            .save(inventoriesCheckDetailMapper.toEntity(inventoriesCheckDetailDTO))
            .map(inventoriesCheckDetailMapper::toDto);
    }

    /**
     * Update a inventoriesCheckDetail.
     *
     * @param inventoriesCheckDetailDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<InventoriesCheckDetailDTO> update(InventoriesCheckDetailDTO inventoriesCheckDetailDTO) {
        log.debug("Request to update InventoriesCheckDetail : {}", inventoriesCheckDetailDTO);
        return inventoriesCheckDetailRepository
            .save(inventoriesCheckDetailMapper.toEntity(inventoriesCheckDetailDTO).setIsPersisted())
            .map(inventoriesCheckDetailMapper::toDto);
    }

    /**
     * Partially update a inventoriesCheckDetail.
     *
     * @param inventoriesCheckDetailDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<InventoriesCheckDetailDTO> partialUpdate(InventoriesCheckDetailDTO inventoriesCheckDetailDTO) {
        log.debug("Request to partially update InventoriesCheckDetail : {}", inventoriesCheckDetailDTO);

        return inventoriesCheckDetailRepository
            .findById(inventoriesCheckDetailDTO.getId())
            .map(existingInventoriesCheckDetail -> {
                inventoriesCheckDetailMapper.partialUpdate(existingInventoriesCheckDetail, inventoriesCheckDetailDTO);

                return existingInventoriesCheckDetail;
            })
            .flatMap(inventoriesCheckDetailRepository::save)
            .map(inventoriesCheckDetailMapper::toDto);
    }

    /**
     * Find inventoriesCheckDetails by Criteria.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<InventoriesCheckDetailDTO> findByCriteria(InventoriesCheckDetailCriteria criteria, Pageable pageable) {
        log.debug("Request to get all InventoriesCheckDetails by Criteria");
        return inventoriesCheckDetailRepository.findByCriteria(criteria, pageable).map(inventoriesCheckDetailMapper::toDto);
    }

    /**
     * Find the count of inventoriesCheckDetails by criteria.
     * @param criteria filtering criteria
     * @return the count of inventoriesCheckDetails
     */
    public Mono<Long> countByCriteria(InventoriesCheckDetailCriteria criteria) {
        log.debug("Request to get the count of all InventoriesCheckDetails by Criteria");
        return inventoriesCheckDetailRepository.countByCriteria(criteria);
    }

    /**
     * Returns the number of inventoriesCheckDetails available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return inventoriesCheckDetailRepository.count();
    }

    /**
     * Get one inventoriesCheckDetail by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<InventoriesCheckDetailDTO> findOne(UUID id) {
        log.debug("Request to get InventoriesCheckDetail : {}", id);
        return inventoriesCheckDetailRepository.findById(id).map(inventoriesCheckDetailMapper::toDto);
    }

    /**
     * Delete the inventoriesCheckDetail by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(UUID id) {
        log.debug("Request to delete InventoriesCheckDetail : {}", id);
        return inventoriesCheckDetailRepository.deleteById(id);
    }
}
