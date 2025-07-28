package com.masi.logistics.service;

import com.masi.logistics.domain.criteria.ItemInfoCriteria;
import com.masi.logistics.repository.ItemInfoRepository;
import com.masi.logistics.service.dto.ItemInfoDTO;
import com.masi.logistics.service.mapper.ItemInfoMapper;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.masi.logistics.domain.ItemInfo}.
 */
@Service
@Transactional
public class ItemInfoService {

    private static final Logger log = LoggerFactory.getLogger(ItemInfoService.class);

    private final ItemInfoRepository itemInfoRepository;

    private final ItemInfoMapper itemInfoMapper;

    public ItemInfoService(ItemInfoRepository itemInfoRepository, ItemInfoMapper itemInfoMapper) {
        this.itemInfoRepository = itemInfoRepository;
        this.itemInfoMapper = itemInfoMapper;
    }

    /**
     * Save a itemInfo.
     *
     * @param itemInfoDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ItemInfoDTO> save(ItemInfoDTO itemInfoDTO) {
        log.debug("Request to save ItemInfo : {}", itemInfoDTO);
        return itemInfoRepository.save(itemInfoMapper.toEntity(itemInfoDTO)).map(itemInfoMapper::toDto);
    }

    /**
     * Update a itemInfo.
     *
     * @param itemInfoDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ItemInfoDTO> update(ItemInfoDTO itemInfoDTO) {
        log.debug("Request to update ItemInfo : {}", itemInfoDTO);
        return itemInfoRepository.save(itemInfoMapper.toEntity(itemInfoDTO).setIsPersisted()).map(itemInfoMapper::toDto);
    }

    /**
     * Partially update a itemInfo.
     *
     * @param itemInfoDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<ItemInfoDTO> partialUpdate(ItemInfoDTO itemInfoDTO) {
        log.debug("Request to partially update ItemInfo : {}", itemInfoDTO);

        return itemInfoRepository
            .findById(itemInfoDTO.getId())
            .map(existingItemInfo -> {
                itemInfoMapper.partialUpdate(existingItemInfo, itemInfoDTO);

                return existingItemInfo;
            })
            .flatMap(itemInfoRepository::save)
            .map(itemInfoMapper::toDto);
    }

    /**
     * Find itemInfos by Criteria.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<ItemInfoDTO> findByCriteria(ItemInfoCriteria criteria, Pageable pageable) {
        log.debug("Request to get all ItemInfos by Criteria");
        return itemInfoRepository.findByCriteria(criteria, pageable).map(itemInfoMapper::toDto);
    }

    /**
     * Find the count of itemInfos by criteria.
     * @param criteria filtering criteria
     * @return the count of itemInfos
     */
    public Mono<Long> countByCriteria(ItemInfoCriteria criteria) {
        log.debug("Request to get the count of all ItemInfos by Criteria");
        return itemInfoRepository.countByCriteria(criteria);
    }

    /**
     * Returns the number of itemInfos available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return itemInfoRepository.count();
    }

    /**
     * Get one itemInfo by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<ItemInfoDTO> findOne(UUID id) {
        log.debug("Request to get ItemInfo : {}", id);
        return itemInfoRepository.findById(id).map(itemInfoMapper::toDto);
    }

    /**
     * Delete the itemInfo by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(UUID id) {
        log.debug("Request to delete ItemInfo : {}", id);
        return itemInfoRepository.deleteById(id);
    }
}
