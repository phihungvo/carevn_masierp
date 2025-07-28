package com.masi.logistics.service;

import com.masi.logistics.domain.criteria.InventoriesDetailCriteria;
import com.masi.logistics.repository.InventoriesDetailRepository;
import com.masi.logistics.service.dto.InventoriesDetailDTO;
import com.masi.logistics.service.mapper.InventoriesDetailMapper;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.masi.logistics.domain.InventoriesDetail}.
 */
@Service
@Transactional
public class InventoriesDetailService {

    private static final Logger log = LoggerFactory.getLogger(InventoriesDetailService.class);

    private final InventoriesDetailRepository inventoriesDetailRepository;

    private final InventoriesDetailMapper inventoriesDetailMapper;

    public InventoriesDetailService(
        InventoriesDetailRepository inventoriesDetailRepository,
        InventoriesDetailMapper inventoriesDetailMapper
    ) {
        this.inventoriesDetailRepository = inventoriesDetailRepository;
        this.inventoriesDetailMapper = inventoriesDetailMapper;
    }

    /**
     * Save a inventoriesDetail.
     *
     * @param inventoriesDetailDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<InventoriesDetailDTO> save(InventoriesDetailDTO inventoriesDetailDTO) {
        log.debug("Request to save InventoriesDetail : {}", inventoriesDetailDTO);
        return inventoriesDetailRepository.save(inventoriesDetailMapper.toEntity(inventoriesDetailDTO)).map(inventoriesDetailMapper::toDto);
    }

    /**
     * Update a inventoriesDetail.
     *
     * @param inventoriesDetailDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<InventoriesDetailDTO> update(InventoriesDetailDTO inventoriesDetailDTO) {
        log.debug("Request to update InventoriesDetail : {}", inventoriesDetailDTO);
        return inventoriesDetailRepository
            .save(inventoriesDetailMapper.toEntity(inventoriesDetailDTO).setIsPersisted())
            .map(inventoriesDetailMapper::toDto);
    }

    /**
     * Partially update a inventoriesDetail.
     *
     * @param inventoriesDetailDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<InventoriesDetailDTO> partialUpdate(InventoriesDetailDTO inventoriesDetailDTO) {
        log.debug("Request to partially update InventoriesDetail : {}", inventoriesDetailDTO);

        return inventoriesDetailRepository
            .findById(inventoriesDetailDTO.getId())
            .map(existingInventoriesDetail -> {
                inventoriesDetailMapper.partialUpdate(existingInventoriesDetail, inventoriesDetailDTO);

                return existingInventoriesDetail;
            })
            .flatMap(inventoriesDetailRepository::save)
            .map(inventoriesDetailMapper::toDto);
    }

    /**
     * Find inventoriesDetails by Criteria.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<InventoriesDetailDTO> findByCriteria(InventoriesDetailCriteria criteria, Pageable pageable) {
        log.debug("Request to get all InventoriesDetails by Criteria");
        return inventoriesDetailRepository.findByCriteria(criteria, pageable)
                .map(inventoriesDetailMapper::toDto);
    }

    /**
     * Find the count of inventoriesDetails by criteria.
     * @param criteria filtering criteria
     * @return the count of inventoriesDetails
     */
    public Mono<Long> countByCriteria(InventoriesDetailCriteria criteria) {
        log.debug("Request to get the count of all InventoriesDetails by Criteria");
        return inventoriesDetailRepository.countByCriteria(criteria);
    }

    /**
     * Returns the number of inventoriesDetails available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return inventoriesDetailRepository.count();
    }

    /**
     * Get one inventoriesDetail by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<InventoriesDetailDTO> findOne(UUID id) {
        log.debug("Request to get InventoriesDetail : {}", id);
        return inventoriesDetailRepository.findById(id).map(inventoriesDetailMapper::toDto);
    }

    /**
     * Delete the inventoriesDetail by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(UUID id) {
        log.debug("Request to delete InventoriesDetail : {}", id);
        return inventoriesDetailRepository.deleteById(id);
    }
}
