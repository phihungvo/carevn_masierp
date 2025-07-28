package com.masi.logistics.service;

import com.masi.logistics.domain.criteria.ItemTypeCriteria;
import com.masi.logistics.repository.ItemTypeRepository;
import com.masi.logistics.service.dto.ItemTypeDTO;
import com.masi.logistics.service.mapper.ItemTypeMapper;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.masi.logistics.domain.ItemType}.
 */
@Service
@Transactional
public class ItemTypeService {

    private static final Logger log = LoggerFactory.getLogger(ItemTypeService.class);

    private final ItemTypeRepository itemTypeRepository;

    private final ItemTypeMapper itemTypeMapper;

    public ItemTypeService(ItemTypeRepository itemTypeRepository, ItemTypeMapper itemTypeMapper) {
        this.itemTypeRepository = itemTypeRepository;
        this.itemTypeMapper = itemTypeMapper;
    }

    /**
     * Save a itemType.
     *
     * @param itemTypeDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ItemTypeDTO> save(ItemTypeDTO itemTypeDTO) {
        log.debug("Request to save ItemType : {}", itemTypeDTO);
        return itemTypeRepository.save(itemTypeMapper.toEntity(itemTypeDTO)).map(itemTypeMapper::toDto);
    }

    /**
     * Update a itemType.
     *
     * @param itemTypeDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ItemTypeDTO> update(ItemTypeDTO itemTypeDTO) {
        log.debug("Request to update ItemType : {}", itemTypeDTO);
        return itemTypeRepository.save(itemTypeMapper.toEntity(itemTypeDTO).setIsPersisted()).map(itemTypeMapper::toDto);
    }

    /**
     * Partially update a itemType.
     *
     * @param itemTypeDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<ItemTypeDTO> partialUpdate(ItemTypeDTO itemTypeDTO) {
        log.debug("Request to partially update ItemType : {}", itemTypeDTO);

        return itemTypeRepository
            .findById(itemTypeDTO.getId())
            .map(existingItemType -> {
                itemTypeMapper.partialUpdate(existingItemType, itemTypeDTO);

                return existingItemType;
            })
            .flatMap(itemTypeRepository::save)
            .map(itemTypeMapper::toDto);
    }

    /**
     * Find itemTypes by Criteria.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<ItemTypeDTO> findByCriteria(ItemTypeCriteria criteria, Pageable pageable) {
        log.debug("Request to get all ItemTypes by Criteria");
        return itemTypeRepository.findByCriteria(criteria, pageable).map(itemTypeMapper::toDto);
    }

    /**
     * Find the count of itemTypes by criteria.
     * @param criteria filtering criteria
     * @return the count of itemTypes
     */
    public Mono<Long> countByCriteria(ItemTypeCriteria criteria) {
        log.debug("Request to get the count of all ItemTypes by Criteria");
        return itemTypeRepository.countByCriteria(criteria);
    }

    /**
     * Returns the number of itemTypes available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return itemTypeRepository.count();
    }

    /**
     * Get one itemType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<ItemTypeDTO> findOne(UUID id) {
        log.debug("Request to get ItemType : {}", id);
        return itemTypeRepository.findById(id).map(itemTypeMapper::toDto);
    }

    /**
     * Delete the itemType by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(UUID id) {
        log.debug("Request to delete ItemType : {}", id);
        return itemTypeRepository.deleteById(id);
    }
}
