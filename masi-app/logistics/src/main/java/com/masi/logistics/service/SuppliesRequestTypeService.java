package com.masi.logistics.service;

import com.masi.logistics.domain.criteria.SuppliesRequestTypeCriteria;
import com.masi.logistics.repository.SuppliesRequestTypeRepository;
import com.masi.logistics.service.dto.SuppliesRequestTypeDTO;
import com.masi.logistics.service.mapper.SuppliesRequestTypeMapper;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.masi.logistics.domain.SuppliesRequestType}.
 */
@Service
@Transactional
public class SuppliesRequestTypeService {

    private static final Logger log = LoggerFactory.getLogger(SuppliesRequestTypeService.class);

    private final SuppliesRequestTypeRepository suppliesRequestTypeRepository;

    private final SuppliesRequestTypeMapper suppliesRequestTypeMapper;

    public SuppliesRequestTypeService(
        SuppliesRequestTypeRepository suppliesRequestTypeRepository,
        SuppliesRequestTypeMapper suppliesRequestTypeMapper
    ) {
        this.suppliesRequestTypeRepository = suppliesRequestTypeRepository;
        this.suppliesRequestTypeMapper = suppliesRequestTypeMapper;
    }

    /**
     * Save a suppliesRequestType.
     *
     * @param suppliesRequestTypeDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<SuppliesRequestTypeDTO> save(SuppliesRequestTypeDTO suppliesRequestTypeDTO) {
        log.debug("Request to save SuppliesRequestType : {}", suppliesRequestTypeDTO);
        return suppliesRequestTypeRepository
            .save(suppliesRequestTypeMapper.toEntity(suppliesRequestTypeDTO))
            .map(suppliesRequestTypeMapper::toDto);
    }

    /**
     * Update a suppliesRequestType.
     *
     * @param suppliesRequestTypeDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<SuppliesRequestTypeDTO> update(SuppliesRequestTypeDTO suppliesRequestTypeDTO) {
        log.debug("Request to update SuppliesRequestType : {}", suppliesRequestTypeDTO);
        return suppliesRequestTypeRepository
            .save(suppliesRequestTypeMapper.toEntity(suppliesRequestTypeDTO).setIsPersisted())
            .map(suppliesRequestTypeMapper::toDto);
    }

    /**
     * Partially update a suppliesRequestType.
     *
     * @param suppliesRequestTypeDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<SuppliesRequestTypeDTO> partialUpdate(SuppliesRequestTypeDTO suppliesRequestTypeDTO) {
        log.debug("Request to partially update SuppliesRequestType : {}", suppliesRequestTypeDTO);

        return suppliesRequestTypeRepository
            .findById(suppliesRequestTypeDTO.getId())
            .map(existingSuppliesRequestType -> {
                suppliesRequestTypeMapper.partialUpdate(existingSuppliesRequestType, suppliesRequestTypeDTO);

                return existingSuppliesRequestType;
            })
            .flatMap(suppliesRequestTypeRepository::save)
            .map(suppliesRequestTypeMapper::toDto);
    }

    /**
     * Find suppliesRequestTypes by Criteria.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<SuppliesRequestTypeDTO> findByCriteria(SuppliesRequestTypeCriteria criteria, Pageable pageable) {
        log.debug("Request to get all SuppliesRequestTypes by Criteria");
        return suppliesRequestTypeRepository.findByCriteria(criteria, pageable).map(suppliesRequestTypeMapper::toDto);
    }

    /**
     * Find the count of suppliesRequestTypes by criteria.
     * @param criteria filtering criteria
     * @return the count of suppliesRequestTypes
     */
    public Mono<Long> countByCriteria(SuppliesRequestTypeCriteria criteria) {
        log.debug("Request to get the count of all SuppliesRequestTypes by Criteria");
        return suppliesRequestTypeRepository.countByCriteria(criteria);
    }

    /**
     * Returns the number of suppliesRequestTypes available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return suppliesRequestTypeRepository.count();
    }

    /**
     * Get one suppliesRequestType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<SuppliesRequestTypeDTO> findOne(UUID id) {
        log.debug("Request to get SuppliesRequestType : {}", id);
        return suppliesRequestTypeRepository.findById(id).map(suppliesRequestTypeMapper::toDto);
    }

    /**
     * Delete the suppliesRequestType by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(UUID id) {
        log.debug("Request to delete SuppliesRequestType : {}", id);
        return suppliesRequestTypeRepository.deleteById(id);
    }
}
