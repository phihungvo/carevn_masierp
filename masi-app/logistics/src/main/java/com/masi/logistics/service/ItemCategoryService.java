package com.masi.logistics.service;

import com.masi.logistics.domain.criteria.ItemCategoryCriteria;
import com.masi.logistics.repository.ItemCategoryRepository;
import com.masi.logistics.service.dto.ItemCategoryDTO;
import com.masi.logistics.service.mapper.ItemCategoryMapper;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.masi.logistics.domain.ItemCategory}.
 */
@Service
@Transactional
public class ItemCategoryService {

    private static final Logger log = LoggerFactory.getLogger(ItemCategoryService.class);

    private final ItemCategoryRepository itemCategoryRepository;

    private final ItemCategoryMapper itemCategoryMapper;

    public ItemCategoryService(ItemCategoryRepository itemCategoryRepository, ItemCategoryMapper itemCategoryMapper) {
        this.itemCategoryRepository = itemCategoryRepository;
        this.itemCategoryMapper = itemCategoryMapper;
    }

    /**
     * Save a itemCategory.
     *
     * @param itemCategoryDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ItemCategoryDTO> save(ItemCategoryDTO itemCategoryDTO) {
        log.debug("Request to save ItemCategory : {}", itemCategoryDTO);
        return itemCategoryRepository.save(itemCategoryMapper.toEntity(itemCategoryDTO)).map(itemCategoryMapper::toDto);
    }

    /**
     * Update a itemCategory.
     *
     * @param itemCategoryDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ItemCategoryDTO> update(ItemCategoryDTO itemCategoryDTO) {
        log.debug("Request to update ItemCategory : {}", itemCategoryDTO);
        return itemCategoryRepository.save(itemCategoryMapper.toEntity(itemCategoryDTO).setIsPersisted()).map(itemCategoryMapper::toDto);
    }

    /**
     * Partially update a itemCategory.
     *
     * @param itemCategoryDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<ItemCategoryDTO> partialUpdate(ItemCategoryDTO itemCategoryDTO) {
        log.debug("Request to partially update ItemCategory : {}", itemCategoryDTO);

        return itemCategoryRepository
            .findById(itemCategoryDTO.getId())
            .map(existingItemCategory -> {
                itemCategoryMapper.partialUpdate(existingItemCategory, itemCategoryDTO);

                return existingItemCategory;
            })
            .flatMap(itemCategoryRepository::save)
            .map(itemCategoryMapper::toDto);
    }

    /**
     * Find itemCategories by Criteria.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<ItemCategoryDTO> findByCriteria(ItemCategoryCriteria criteria, Pageable pageable) {
        log.debug("Request to get all ItemCategories by Criteria");
        return itemCategoryRepository.findByCriteria(criteria, pageable).map(itemCategoryMapper::toDto);
    }

    /**
     * Find the count of itemCategories by criteria.
     * @param criteria filtering criteria
     * @return the count of itemCategories
     */
    public Mono<Long> countByCriteria(ItemCategoryCriteria criteria) {
        log.debug("Request to get the count of all ItemCategories by Criteria");
        return itemCategoryRepository.countByCriteria(criteria);
    }

    /**
     * Returns the number of itemCategories available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return itemCategoryRepository.count();
    }

    /**
     * Get one itemCategory by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<ItemCategoryDTO> findOne(UUID id) {
        log.debug("Request to get ItemCategory : {}", id);
        return itemCategoryRepository.findById(id).map(itemCategoryMapper::toDto);
    }

    /**
     * Delete the itemCategory by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(UUID id) {
        log.debug("Request to delete ItemCategory : {}", id);
        return itemCategoryRepository.deleteById(id);
    }
}
