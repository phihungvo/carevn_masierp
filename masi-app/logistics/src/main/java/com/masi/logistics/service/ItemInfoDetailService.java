package com.masi.logistics.service;

import com.masi.logistics.domain.criteria.ItemInfoDetailCriteria;
import com.masi.logistics.repository.ItemInfoDetailRepository;
import com.masi.logistics.service.dto.ItemInfoDetailDTO;
import com.masi.logistics.service.mapper.ItemInfoDetailMapper;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.masi.logistics.domain.ItemInfoDetail}.
 */
@Service
@Transactional
public class ItemInfoDetailService {

    private static final Logger log = LoggerFactory.getLogger(ItemInfoDetailService.class);

    private final ItemInfoDetailRepository itemInfoDetailRepository;

    private final ItemInfoDetailMapper itemInfoDetailMapper;

    public ItemInfoDetailService(ItemInfoDetailRepository itemInfoDetailRepository, ItemInfoDetailMapper itemInfoDetailMapper) {
        this.itemInfoDetailRepository = itemInfoDetailRepository;
        this.itemInfoDetailMapper = itemInfoDetailMapper;
    }

    /**
     * Save a itemInfoDetail.
     *
     * @param itemInfoDetailDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ItemInfoDetailDTO> save(ItemInfoDetailDTO itemInfoDetailDTO) {
        log.debug("Request to save ItemInfoDetail : {}", itemInfoDetailDTO);
        return itemInfoDetailRepository.save(itemInfoDetailMapper.toEntity(itemInfoDetailDTO)).map(itemInfoDetailMapper::toDto);
    }

    /**
     * Update a itemInfoDetail.
     *
     * @param itemInfoDetailDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ItemInfoDetailDTO> update(ItemInfoDetailDTO itemInfoDetailDTO) {
        log.debug("Request to update ItemInfoDetail : {}", itemInfoDetailDTO);
        return itemInfoDetailRepository
            .save(itemInfoDetailMapper.toEntity(itemInfoDetailDTO).setIsPersisted())
            .map(itemInfoDetailMapper::toDto);
    }

    /**
     * Partially update a itemInfoDetail.
     *
     * @param itemInfoDetailDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<ItemInfoDetailDTO> partialUpdate(ItemInfoDetailDTO itemInfoDetailDTO) {
        log.debug("Request to partially update ItemInfoDetail : {}", itemInfoDetailDTO);

        return itemInfoDetailRepository
            .findById(itemInfoDetailDTO.getId())
            .map(existingItemInfoDetail -> {
                itemInfoDetailMapper.partialUpdate(existingItemInfoDetail, itemInfoDetailDTO);

                return existingItemInfoDetail;
            })
            .flatMap(itemInfoDetailRepository::save)
            .map(itemInfoDetailMapper::toDto);
    }

    /**
     * Find itemInfoDetails by Criteria.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<ItemInfoDetailDTO> findByCriteria(ItemInfoDetailCriteria criteria, Pageable pageable) {
        log.debug("Request to get all ItemInfoDetails by Criteria");
        return itemInfoDetailRepository.findByCriteria(criteria, pageable).map(itemInfoDetailMapper::toDto);
    }

    /**
     * Find the count of itemInfoDetails by criteria.
     * @param criteria filtering criteria
     * @return the count of itemInfoDetails
     */
    public Mono<Long> countByCriteria(ItemInfoDetailCriteria criteria) {
        log.debug("Request to get the count of all ItemInfoDetails by Criteria");
        return itemInfoDetailRepository.countByCriteria(criteria);
    }

    /**
     * Returns the number of itemInfoDetails available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return itemInfoDetailRepository.count();
    }

    /**
     * Get one itemInfoDetail by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<ItemInfoDetailDTO> findOne(UUID id) {
        log.debug("Request to get ItemInfoDetail : {}", id);
        return itemInfoDetailRepository.findById(id).map(itemInfoDetailMapper::toDto);
    }

    /**
     * Delete the itemInfoDetail by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(UUID id) {
        log.debug("Request to delete ItemInfoDetail : {}", id);
        return itemInfoDetailRepository.deleteById(id);
    }
}
