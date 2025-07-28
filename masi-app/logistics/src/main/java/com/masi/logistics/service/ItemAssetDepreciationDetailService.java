package com.masi.logistics.service;

import com.masi.logistics.domain.criteria.ItemAssetDepreciationDetailCriteria;
import com.masi.logistics.repository.ItemAssetDepreciationDetailRepository;
import com.masi.logistics.service.dto.ItemAssetDepreciationDetailDTO;
import com.masi.logistics.service.mapper.ItemAssetDepreciationDetailMapper;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.masi.logistics.domain.ItemAssetDepreciationDetail}.
 */
@Service
@Transactional
public class ItemAssetDepreciationDetailService {

    private static final Logger log = LoggerFactory.getLogger(ItemAssetDepreciationDetailService.class);

    private final ItemAssetDepreciationDetailRepository itemAssetDepreciationDetailRepository;

    private final ItemAssetDepreciationDetailMapper itemAssetDepreciationDetailMapper;

    public ItemAssetDepreciationDetailService(
        ItemAssetDepreciationDetailRepository itemAssetDepreciationDetailRepository,
        ItemAssetDepreciationDetailMapper itemAssetDepreciationDetailMapper
    ) {
        this.itemAssetDepreciationDetailRepository = itemAssetDepreciationDetailRepository;
        this.itemAssetDepreciationDetailMapper = itemAssetDepreciationDetailMapper;
    }

    /**
     * Save a itemAssetDepreciationDetail.
     *
     * @param itemAssetDepreciationDetailDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ItemAssetDepreciationDetailDTO> save(ItemAssetDepreciationDetailDTO itemAssetDepreciationDetailDTO) {
        log.debug("Request to save ItemAssetDepreciationDetail : {}", itemAssetDepreciationDetailDTO);
        return itemAssetDepreciationDetailRepository
            .save(itemAssetDepreciationDetailMapper.toEntity(itemAssetDepreciationDetailDTO))
            .map(itemAssetDepreciationDetailMapper::toDto);
    }

    /**
     * Update a itemAssetDepreciationDetail.
     *
     * @param itemAssetDepreciationDetailDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ItemAssetDepreciationDetailDTO> update(ItemAssetDepreciationDetailDTO itemAssetDepreciationDetailDTO) {
        log.debug("Request to update ItemAssetDepreciationDetail : {}", itemAssetDepreciationDetailDTO);
        return itemAssetDepreciationDetailRepository
            .save(itemAssetDepreciationDetailMapper.toEntity(itemAssetDepreciationDetailDTO).setIsPersisted())
            .map(itemAssetDepreciationDetailMapper::toDto);
    }

    /**
     * Partially update a itemAssetDepreciationDetail.
     *
     * @param itemAssetDepreciationDetailDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<ItemAssetDepreciationDetailDTO> partialUpdate(ItemAssetDepreciationDetailDTO itemAssetDepreciationDetailDTO) {
        log.debug("Request to partially update ItemAssetDepreciationDetail : {}", itemAssetDepreciationDetailDTO);

        return itemAssetDepreciationDetailRepository
            .findById(itemAssetDepreciationDetailDTO.getId())
            .map(existingItemAssetDepreciationDetail -> {
                itemAssetDepreciationDetailMapper.partialUpdate(existingItemAssetDepreciationDetail, itemAssetDepreciationDetailDTO);

                return existingItemAssetDepreciationDetail;
            })
            .flatMap(itemAssetDepreciationDetailRepository::save)
            .map(itemAssetDepreciationDetailMapper::toDto);
    }

    /**
     * Find itemAssetDepreciationDetails by Criteria.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<ItemAssetDepreciationDetailDTO> findByCriteria(ItemAssetDepreciationDetailCriteria criteria, Pageable pageable) {
        log.debug("Request to get all ItemAssetDepreciationDetails by Criteria");
        return itemAssetDepreciationDetailRepository.findByCriteria(criteria, pageable).map(itemAssetDepreciationDetailMapper::toDto);
    }

    /**
     * Find the count of itemAssetDepreciationDetails by criteria.
     * @param criteria filtering criteria
     * @return the count of itemAssetDepreciationDetails
     */
    public Mono<Long> countByCriteria(ItemAssetDepreciationDetailCriteria criteria) {
        log.debug("Request to get the count of all ItemAssetDepreciationDetails by Criteria");
        return itemAssetDepreciationDetailRepository.countByCriteria(criteria);
    }

    /**
     * Returns the number of itemAssetDepreciationDetails available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return itemAssetDepreciationDetailRepository.count();
    }

    /**
     * Get one itemAssetDepreciationDetail by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<ItemAssetDepreciationDetailDTO> findOne(UUID id) {
        log.debug("Request to get ItemAssetDepreciationDetail : {}", id);
        return itemAssetDepreciationDetailRepository.findById(id).map(itemAssetDepreciationDetailMapper::toDto);
    }

    /**
     * Delete the itemAssetDepreciationDetail by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(UUID id) {
        log.debug("Request to delete ItemAssetDepreciationDetail : {}", id);
        return itemAssetDepreciationDetailRepository.deleteById(id);
    }
}
