package com.masi.logistics.service;

import com.masi.logistics.domain.criteria.ItemLiquidationDetailCriteria;
import com.masi.logistics.repository.ItemLiquidationDetailRepository;
import com.masi.logistics.service.dto.ItemLiquidationDetailDTO;
import com.masi.logistics.service.mapper.ItemLiquidationDetailMapper;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.masi.logistics.domain.ItemLiquidationDetail}.
 */
@Service
@Transactional
public class ItemLiquidationDetailService {

    private static final Logger log = LoggerFactory.getLogger(ItemLiquidationDetailService.class);

    private final ItemLiquidationDetailRepository itemLiquidationDetailRepository;

    private final ItemLiquidationDetailMapper itemLiquidationDetailMapper;

    public ItemLiquidationDetailService(
        ItemLiquidationDetailRepository itemLiquidationDetailRepository,
        ItemLiquidationDetailMapper itemLiquidationDetailMapper
    ) {
        this.itemLiquidationDetailRepository = itemLiquidationDetailRepository;
        this.itemLiquidationDetailMapper = itemLiquidationDetailMapper;
    }

    /**
     * Save a itemLiquidationDetail.
     *
     * @param itemLiquidationDetailDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ItemLiquidationDetailDTO> save(ItemLiquidationDetailDTO itemLiquidationDetailDTO) {
        log.debug("Request to save ItemLiquidationDetail : {}", itemLiquidationDetailDTO);
        return itemLiquidationDetailRepository
            .save(itemLiquidationDetailMapper.toEntity(itemLiquidationDetailDTO))
            .map(itemLiquidationDetailMapper::toDto);
    }

    /**
     * Update a itemLiquidationDetail.
     *
     * @param itemLiquidationDetailDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ItemLiquidationDetailDTO> update(ItemLiquidationDetailDTO itemLiquidationDetailDTO) {
        log.debug("Request to update ItemLiquidationDetail : {}", itemLiquidationDetailDTO);
        return itemLiquidationDetailRepository
            .save(itemLiquidationDetailMapper.toEntity(itemLiquidationDetailDTO).setIsPersisted())
            .map(itemLiquidationDetailMapper::toDto);
    }

    /**
     * Partially update a itemLiquidationDetail.
     *
     * @param itemLiquidationDetailDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<ItemLiquidationDetailDTO> partialUpdate(ItemLiquidationDetailDTO itemLiquidationDetailDTO) {
        log.debug("Request to partially update ItemLiquidationDetail : {}", itemLiquidationDetailDTO);

        return itemLiquidationDetailRepository
            .findById(itemLiquidationDetailDTO.getId())
            .map(existingItemLiquidationDetail -> {
                itemLiquidationDetailMapper.partialUpdate(existingItemLiquidationDetail, itemLiquidationDetailDTO);

                return existingItemLiquidationDetail;
            })
            .flatMap(itemLiquidationDetailRepository::save)
            .map(itemLiquidationDetailMapper::toDto);
    }

    /**
     * Find itemLiquidationDetails by Criteria.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<ItemLiquidationDetailDTO> findByCriteria(ItemLiquidationDetailCriteria criteria, Pageable pageable) {
        log.debug("Request to get all ItemLiquidationDetails by Criteria");
        return itemLiquidationDetailRepository.findByCriteria(criteria, pageable).map(itemLiquidationDetailMapper::toDto);
    }

    /**
     * Find the count of itemLiquidationDetails by criteria.
     * @param criteria filtering criteria
     * @return the count of itemLiquidationDetails
     */
    public Mono<Long> countByCriteria(ItemLiquidationDetailCriteria criteria) {
        log.debug("Request to get the count of all ItemLiquidationDetails by Criteria");
        return itemLiquidationDetailRepository.countByCriteria(criteria);
    }

    /**
     * Returns the number of itemLiquidationDetails available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return itemLiquidationDetailRepository.count();
    }

    /**
     * Get one itemLiquidationDetail by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<ItemLiquidationDetailDTO> findOne(UUID id) {
        log.debug("Request to get ItemLiquidationDetail : {}", id);
        return itemLiquidationDetailRepository.findById(id).map(itemLiquidationDetailMapper::toDto);
    }

    /**
     * Delete the itemLiquidationDetail by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(UUID id) {
        log.debug("Request to delete ItemLiquidationDetail : {}", id);
        return itemLiquidationDetailRepository.deleteById(id);
    }
}
