package com.masi.logistics.service;

import com.masi.logistics.domain.criteria.WarehouseTypeCriteria;
import com.masi.logistics.repository.WarehouseTypeRepository;
import com.masi.logistics.service.dto.WarehouseTypeDTO;
import com.masi.logistics.service.mapper.WarehouseTypeMapper;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.masi.logistics.domain.WarehouseType}.
 */
@Service
@Transactional
public class WarehouseTypeService {

    private static final Logger log = LoggerFactory.getLogger(WarehouseTypeService.class);

    private final WarehouseTypeRepository warehouseTypeRepository;

    private final WarehouseTypeMapper warehouseTypeMapper;

    public WarehouseTypeService(WarehouseTypeRepository warehouseTypeRepository, WarehouseTypeMapper warehouseTypeMapper) {
        this.warehouseTypeRepository = warehouseTypeRepository;
        this.warehouseTypeMapper = warehouseTypeMapper;
    }

    /**
     * Save a warehouseType.
     *
     * @param warehouseTypeDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<WarehouseTypeDTO> save(WarehouseTypeDTO warehouseTypeDTO) {
        log.debug("Request to save WarehouseType : {}", warehouseTypeDTO);
        return warehouseTypeRepository.save(warehouseTypeMapper.toEntity(warehouseTypeDTO)).map(warehouseTypeMapper::toDto);
    }

    /**
     * Update a warehouseType.
     *
     * @param warehouseTypeDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<WarehouseTypeDTO> update(WarehouseTypeDTO warehouseTypeDTO) {
        log.debug("Request to update WarehouseType : {}", warehouseTypeDTO);
        return warehouseTypeRepository
            .save(warehouseTypeMapper.toEntity(warehouseTypeDTO).setIsPersisted())
            .map(warehouseTypeMapper::toDto);
    }

    /**
     * Partially update a warehouseType.
     *
     * @param warehouseTypeDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<WarehouseTypeDTO> partialUpdate(WarehouseTypeDTO warehouseTypeDTO) {
        log.debug("Request to partially update WarehouseType : {}", warehouseTypeDTO);

        return warehouseTypeRepository
            .findById(warehouseTypeDTO.getId())
            .map(existingWarehouseType -> {
                warehouseTypeMapper.partialUpdate(existingWarehouseType, warehouseTypeDTO);

                return existingWarehouseType;
            })
            .flatMap(warehouseTypeRepository::save)
            .map(warehouseTypeMapper::toDto);
    }

    /**
     * Find warehouseTypes by Criteria.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<WarehouseTypeDTO> findByCriteria(WarehouseTypeCriteria criteria, Pageable pageable) {
        log.debug("Request to get all WarehouseTypes by Criteria");
        return warehouseTypeRepository.findByCriteria(criteria, pageable).map(warehouseTypeMapper::toDto);
    }

    /**
     * Find the count of warehouseTypes by criteria.
     * @param criteria filtering criteria
     * @return the count of warehouseTypes
     */
    public Mono<Long> countByCriteria(WarehouseTypeCriteria criteria) {
        log.debug("Request to get the count of all WarehouseTypes by Criteria");
        return warehouseTypeRepository.countByCriteria(criteria);
    }

    /**
     * Returns the number of warehouseTypes available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return warehouseTypeRepository.count();
    }

    /**
     * Get one warehouseType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<WarehouseTypeDTO> findOne(UUID id) {
        log.debug("Request to get WarehouseType : {}", id);
        return warehouseTypeRepository.findById(id).map(warehouseTypeMapper::toDto);
    }

    /**
     * Delete the warehouseType by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(UUID id) {
        log.debug("Request to delete WarehouseType : {}", id);
        return warehouseTypeRepository.deleteById(id);
    }
}
