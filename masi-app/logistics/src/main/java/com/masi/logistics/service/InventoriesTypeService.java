package com.masi.logistics.service;

import com.masi.logistics.domain.criteria.InventoriesTypeCriteria;
import com.masi.logistics.repository.InventoriesTypeRepository;
import com.masi.logistics.service.dto.InventoriesTypeDTO;
import com.masi.logistics.service.mapper.InventoriesTypeMapper;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.masi.logistics.domain.InventoriesType}.
 */
@Service
@Transactional
public class InventoriesTypeService {

    private static final Logger log = LoggerFactory.getLogger(InventoriesTypeService.class);

    private final InventoriesTypeRepository inventoriesTypeRepository;

    private final InventoriesTypeMapper inventoriesTypeMapper;

    public InventoriesTypeService(InventoriesTypeRepository inventoriesTypeRepository, InventoriesTypeMapper inventoriesTypeMapper) {
        this.inventoriesTypeRepository = inventoriesTypeRepository;
        this.inventoriesTypeMapper = inventoriesTypeMapper;
    }

    /**
     * Save a inventoriesType.
     *
     * @param inventoriesTypeDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<InventoriesTypeDTO> save(InventoriesTypeDTO inventoriesTypeDTO) {
        log.debug("Request to save InventoriesType : {}", inventoriesTypeDTO);
        return inventoriesTypeRepository.save(inventoriesTypeMapper.toEntity(inventoriesTypeDTO)).map(inventoriesTypeMapper::toDto);
    }

    /**
     * Update a inventoriesType.
     *
     * @param inventoriesTypeDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<InventoriesTypeDTO> update(InventoriesTypeDTO inventoriesTypeDTO) {
        log.debug("Request to update InventoriesType : {}", inventoriesTypeDTO);
        return inventoriesTypeRepository
            .save(inventoriesTypeMapper.toEntity(inventoriesTypeDTO).setIsPersisted())
            .map(inventoriesTypeMapper::toDto);
    }

    /**
     * Partially update a inventoriesType.
     *
     * @param inventoriesTypeDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<InventoriesTypeDTO> partialUpdate(InventoriesTypeDTO inventoriesTypeDTO) {
        log.debug("Request to partially update InventoriesType : {}", inventoriesTypeDTO);

        return inventoriesTypeRepository
            .findById(inventoriesTypeDTO.getId())
            .map(existingInventoriesType -> {
                inventoriesTypeMapper.partialUpdate(existingInventoriesType, inventoriesTypeDTO);

                return existingInventoriesType;
            })
            .flatMap(inventoriesTypeRepository::save)
            .map(inventoriesTypeMapper::toDto);
    }

    public Mono<InventoriesTypeDTO> getInventoriesTypeWrap(String companyImport) {
        return inventoriesTypeRepository.findByCreatedBy("SYSTEM" , companyImport)
                .map(inventoriesTypeMapper::toDto);
    }

    /**
     * Find inventoriesTypes by Criteria.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<InventoriesTypeDTO> findByCriteria(InventoriesTypeCriteria criteria, Pageable pageable) {
        log.debug("Request to get all InventoriesTypes by Criteria");
        return inventoriesTypeRepository.findByCriteria(criteria, pageable).map(inventoriesTypeMapper::toDto);
    }

    /**
     * Find the count of inventoriesTypes by criteria.
     * @param criteria filtering criteria
     * @return the count of inventoriesTypes
     */
    public Mono<Long> countByCriteria(InventoriesTypeCriteria criteria) {
        log.debug("Request to get the count of all InventoriesTypes by Criteria");
        return inventoriesTypeRepository.countByCriteria(criteria);
    }

    /**
     * Returns the number of inventoriesTypes available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return inventoriesTypeRepository.count();
    }

    /**
     * Get one inventoriesType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<InventoriesTypeDTO> findOne(UUID id) {
        log.debug("Request to get InventoriesType : {}", id);
        return inventoriesTypeRepository.findById(id).map(inventoriesTypeMapper::toDto);
    }

    /**
     * Delete the inventoriesType by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(UUID id) {
        log.debug("Request to delete InventoriesType : {}", id);
        return inventoriesTypeRepository.deleteById(id);
    }


}
