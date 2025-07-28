package com.masi.logistics.service;

import com.masi.logistics.domain.criteria.WarehouseRoleCriteria;
import com.masi.logistics.repository.WarehouseRoleRepository;
import com.masi.logistics.service.dto.WarehouseRoleDTO;
import com.masi.logistics.service.mapper.WarehouseRoleMapper;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.masi.logistics.domain.WarehouseRole}.
 */
@Service
@Transactional
public class WarehouseRoleService {

    private static final Logger log = LoggerFactory.getLogger(WarehouseRoleService.class);

    private final WarehouseRoleRepository warehouseRoleRepository;

    private final WarehouseRoleMapper warehouseRoleMapper;

    public WarehouseRoleService(WarehouseRoleRepository warehouseRoleRepository, WarehouseRoleMapper warehouseRoleMapper) {
        this.warehouseRoleRepository = warehouseRoleRepository;
        this.warehouseRoleMapper = warehouseRoleMapper;
    }

    /**
     * Save a warehouseRole.
     *
     * @param warehouseRoleDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<WarehouseRoleDTO> save(WarehouseRoleDTO warehouseRoleDTO) {
        log.debug("Request to save WarehouseRole : {}", warehouseRoleDTO);
        return warehouseRoleRepository.save(warehouseRoleMapper.toEntity(warehouseRoleDTO)).map(warehouseRoleMapper::toDto);
    }

    /**
     * Update a warehouseRole.
     *
     * @param warehouseRoleDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<WarehouseRoleDTO> update(WarehouseRoleDTO warehouseRoleDTO) {
        log.debug("Request to update WarehouseRole : {}", warehouseRoleDTO);
        return warehouseRoleRepository
            .save(warehouseRoleMapper.toEntity(warehouseRoleDTO).setIsPersisted())
            .map(warehouseRoleMapper::toDto);
    }

    /**
     * Partially update a warehouseRole.
     *
     * @param warehouseRoleDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<WarehouseRoleDTO> partialUpdate(WarehouseRoleDTO warehouseRoleDTO) {
        log.debug("Request to partially update WarehouseRole : {}", warehouseRoleDTO);

        return warehouseRoleRepository
            .findById(warehouseRoleDTO.getId())
            .map(existingWarehouseRole -> {
                warehouseRoleMapper.partialUpdate(existingWarehouseRole, warehouseRoleDTO);

                return existingWarehouseRole;
            })
            .flatMap(warehouseRoleRepository::save)
            .map(warehouseRoleMapper::toDto);
    }

    /**
     * Find warehouseRoles by Criteria.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<WarehouseRoleDTO> findByCriteria(WarehouseRoleCriteria criteria, Pageable pageable) {
        log.debug("Request to get all WarehouseRoles by Criteria");
        return warehouseRoleRepository.findByCriteria(criteria, pageable).map(warehouseRoleMapper::toDto);
    }

    /**
     * Find the count of warehouseRoles by criteria.
     * @param criteria filtering criteria
     * @return the count of warehouseRoles
     */
    public Mono<Long> countByCriteria(WarehouseRoleCriteria criteria) {
        log.debug("Request to get the count of all WarehouseRoles by Criteria");
        return warehouseRoleRepository.countByCriteria(criteria);
    }

    /**
     * Returns the number of warehouseRoles available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return warehouseRoleRepository.count();
    }

    /**
     * Get one warehouseRole by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<WarehouseRoleDTO> findOne(UUID id) {
        log.debug("Request to get WarehouseRole : {}", id);
        return warehouseRoleRepository.findById(id).map(warehouseRoleMapper::toDto);
    }

    /**
     * Delete the warehouseRole by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(UUID id) {
        log.debug("Request to delete WarehouseRole : {}", id);
        return warehouseRoleRepository.deleteById(id);
    }
}
