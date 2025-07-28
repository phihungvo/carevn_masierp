package com.masi.logistics.service;

import com.masi.logistics.domain.criteria.ItemSubCategoryCriteria;
import com.masi.logistics.repository.ItemSubCategoryRepository;
import com.masi.logistics.service.dto.ItemSubCategoryDTO;
import com.masi.logistics.service.mapper.ItemSubCategoryMapper;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.masi.logistics.domain.ItemSubCategory}.
 */
@Service
@Transactional
public class ItemSubCategoryService {

    private static final Logger log = LoggerFactory.getLogger(ItemSubCategoryService.class);

    private final ItemSubCategoryRepository itemSubCategoryRepository;

    private final ItemSubCategoryMapper itemSubCategoryMapper;

    public ItemSubCategoryService(ItemSubCategoryRepository itemSubCategoryRepository, ItemSubCategoryMapper itemSubCategoryMapper) {
        this.itemSubCategoryRepository = itemSubCategoryRepository;
        this.itemSubCategoryMapper = itemSubCategoryMapper;
    }

    /**
     * Save a itemSubCategory.
     *
     * @param itemSubCategoryDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ItemSubCategoryDTO> save(ItemSubCategoryDTO itemSubCategoryDTO) {
        log.debug("Request to save ItemSubCategory : {}", itemSubCategoryDTO);
        return itemSubCategoryRepository.save(itemSubCategoryMapper.toEntity(itemSubCategoryDTO)).map(itemSubCategoryMapper::toDto);
    }

    /**
     * Update a itemSubCategory.
     *
     * @param itemSubCategoryDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ItemSubCategoryDTO> update(ItemSubCategoryDTO itemSubCategoryDTO) {
        log.debug("Request to update ItemSubCategory : {}", itemSubCategoryDTO);
        return itemSubCategoryRepository
            .save(itemSubCategoryMapper.toEntity(itemSubCategoryDTO).setIsPersisted())
            .map(itemSubCategoryMapper::toDto);
    }

    /**
     * Partially update a itemSubCategory.
     *
     * @param itemSubCategoryDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<ItemSubCategoryDTO> partialUpdate(ItemSubCategoryDTO itemSubCategoryDTO) {
        log.debug("Request to partially update ItemSubCategory : {}", itemSubCategoryDTO);

        return itemSubCategoryRepository
            .findById(itemSubCategoryDTO.getId())
            .map(existingItemSubCategory -> {
                itemSubCategoryMapper.partialUpdate(existingItemSubCategory, itemSubCategoryDTO);

                return existingItemSubCategory;
            })
            .flatMap(itemSubCategoryRepository::save)
            .map(itemSubCategoryMapper::toDto);
    }

    /**
     * Find itemSubCategories by Criteria.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<ItemSubCategoryDTO> findByCriteria(ItemSubCategoryCriteria criteria, Pageable pageable) {
        log.debug("Request to get all ItemSubCategories by Criteria");
        return itemSubCategoryRepository.findByCriteria(criteria, pageable).map(itemSubCategoryMapper::toDto);
    }

    /**
     * Find the count of itemSubCategories by criteria.
     * @param criteria filtering criteria
     * @return the count of itemSubCategories
     */
    public Mono<Long> countByCriteria(ItemSubCategoryCriteria criteria) {
        log.debug("Request to get the count of all ItemSubCategories by Criteria");
        return itemSubCategoryRepository.countByCriteria(criteria);
    }

    /**
     * Returns the number of itemSubCategories available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return itemSubCategoryRepository.count();
    }

    /**
     * Get one itemSubCategory by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<ItemSubCategoryDTO> findOne(UUID id) {
        log.debug("Request to get ItemSubCategory : {}", id);
        return itemSubCategoryRepository.findById(id).map(itemSubCategoryMapper::toDto);
    }

    /**
     * Delete the itemSubCategory by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(UUID id) {
        log.debug("Request to delete ItemSubCategory : {}", id);
        return itemSubCategoryRepository.deleteById(id);
    }
}
