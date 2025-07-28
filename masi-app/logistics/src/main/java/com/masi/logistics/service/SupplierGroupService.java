package com.masi.logistics.service;

import com.masi.logistics.repository.SupplierGroupRepository;
import com.masi.logistics.service.dto.SupplierGroupDTO;
import com.masi.logistics.service.mapper.SupplierGroupMapper;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.masi.logistics.domain.SupplierGroup}.
 */
@Service
@Transactional
public class SupplierGroupService {

    private static final Logger log = LoggerFactory.getLogger(SupplierGroupService.class);

    private final SupplierGroupRepository supplierGroupRepository;

    private final SupplierGroupMapper supplierGroupMapper;

    public SupplierGroupService(SupplierGroupRepository supplierGroupRepository, SupplierGroupMapper supplierGroupMapper) {
        this.supplierGroupRepository = supplierGroupRepository;
        this.supplierGroupMapper = supplierGroupMapper;
    }

    /**
     * Save a supplierGroup.
     *
     * @param supplierGroupDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<SupplierGroupDTO> save(SupplierGroupDTO supplierGroupDTO) {
        log.debug("Request to save SupplierGroup : {}", supplierGroupDTO);
        return supplierGroupRepository.save(supplierGroupMapper.toEntity(supplierGroupDTO)).map(supplierGroupMapper::toDto);
    }

    /**
     * Update a supplierGroup.
     *
     * @param supplierGroupDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<SupplierGroupDTO> update(SupplierGroupDTO supplierGroupDTO) {
        log.debug("Request to update SupplierGroup : {}", supplierGroupDTO);
        return supplierGroupRepository
            .save(supplierGroupMapper.toEntity(supplierGroupDTO).setIsPersisted())
            .map(supplierGroupMapper::toDto);
    }

    /**
     * Partially update a supplierGroup.
     *
     * @param supplierGroupDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<SupplierGroupDTO> partialUpdate(SupplierGroupDTO supplierGroupDTO) {
        log.debug("Request to partially update SupplierGroup : {}", supplierGroupDTO);

        return supplierGroupRepository
            .findById(supplierGroupDTO.getId())
            .map(existingSupplierGroup -> {
                supplierGroupMapper.partialUpdate(existingSupplierGroup, supplierGroupDTO);

                return existingSupplierGroup;
            })
            .flatMap(supplierGroupRepository::save)
            .map(supplierGroupMapper::toDto);
    }

    /**
     * Get all the supplierGroups.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<SupplierGroupDTO> findAll(Pageable pageable) {
        log.debug("Request to get all SupplierGroups");
        return supplierGroupRepository.findAllBy(pageable).map(supplierGroupMapper::toDto);
    }

    /**
     * Returns the number of supplierGroups available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return supplierGroupRepository.count();
    }

    /**
     * Get one supplierGroup by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<SupplierGroupDTO> findOne(UUID id) {
        log.debug("Request to get SupplierGroup : {}", id);
        return supplierGroupRepository.findById(id).map(supplierGroupMapper::toDto);
    }

    /**
     * Delete the supplierGroup by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(UUID id) {
        log.debug("Request to delete SupplierGroup : {}", id);
        return supplierGroupRepository.deleteById(id);
    }
}
