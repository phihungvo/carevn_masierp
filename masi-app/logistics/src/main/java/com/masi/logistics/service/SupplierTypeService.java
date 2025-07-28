package com.masi.logistics.service;

import com.masi.logistics.domain.criteria.SupplierTypeCriteria;
import com.masi.logistics.repository.SupplierTypeRepository;
import com.masi.logistics.service.dto.SupplierTypeDTO;
import com.masi.logistics.service.mapper.SupplierTypeMapper;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.masi.logistics.domain.SupplierType}.
 */
@Service
@Transactional
public class SupplierTypeService {

    private static final Logger LOG = LoggerFactory.getLogger(SupplierTypeService.class);

    private final SupplierTypeRepository supplierTypeRepository;

    private final SupplierTypeMapper supplierTypeMapper;

    public SupplierTypeService(SupplierTypeRepository supplierTypeRepository, SupplierTypeMapper supplierTypeMapper) {
        this.supplierTypeRepository = supplierTypeRepository;
        this.supplierTypeMapper = supplierTypeMapper;
    }

    /**
     * Save a supplierType.
     *
     * @param supplierTypeDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<SupplierTypeDTO> save(SupplierTypeDTO supplierTypeDTO) {
        LOG.debug("Request to save SupplierType : {}", supplierTypeDTO);
        return supplierTypeRepository.save(supplierTypeMapper.toEntity(supplierTypeDTO)).map(supplierTypeMapper::toDto);
    }

    /**
     * Update a supplierType.
     *
     * @param supplierTypeDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<SupplierTypeDTO> update(SupplierTypeDTO supplierTypeDTO) {
        LOG.debug("Request to update SupplierType : {}", supplierTypeDTO);
        return supplierTypeRepository.save(supplierTypeMapper.toEntity(supplierTypeDTO).setIsPersisted()).map(supplierTypeMapper::toDto);
    }

    /**
     * Partially update a supplierType.
     *
     * @param supplierTypeDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<SupplierTypeDTO> partialUpdate(SupplierTypeDTO supplierTypeDTO) {
        LOG.debug("Request to partially update SupplierType : {}", supplierTypeDTO);

        return supplierTypeRepository
            .findById(supplierTypeDTO.getId())
            .map(existingSupplierType -> {
                supplierTypeMapper.partialUpdate(existingSupplierType, supplierTypeDTO);

                return existingSupplierType;
            })
            .flatMap(supplierTypeRepository::save)
            .map(supplierTypeMapper::toDto);
    }

    /**
     * Find supplierTypes by Criteria.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<SupplierTypeDTO> findByCriteria(SupplierTypeCriteria criteria, Pageable pageable) {
        LOG.debug("Request to get all SupplierTypes by Criteria");
        return supplierTypeRepository.findByCriteria(criteria, pageable).map(supplierTypeMapper::toDto);
    }

    /**
     * Find the count of supplierTypes by criteria.
     * @param criteria filtering criteria
     * @return the count of supplierTypes
     */
    public Mono<Long> countByCriteria(SupplierTypeCriteria criteria) {
        LOG.debug("Request to get the count of all SupplierTypes by Criteria");
        return supplierTypeRepository.countByCriteria(criteria);
    }

    /**
     * Returns the number of supplierTypes available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return supplierTypeRepository.count();
    }

    /**
     * Get one supplierType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<SupplierTypeDTO> findOne(UUID id) {
        LOG.debug("Request to get SupplierType : {}", id);
        return supplierTypeRepository.findById(id).map(supplierTypeMapper::toDto);
    }

    /**
     * Delete the supplierType by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(UUID id) {
        LOG.debug("Request to delete SupplierType : {}", id);
        return supplierTypeRepository.deleteById(id);
    }
}
