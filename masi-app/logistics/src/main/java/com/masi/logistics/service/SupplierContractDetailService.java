package com.masi.logistics.service;

import com.masi.logistics.domain.criteria.SupplierContractDetailCriteria;
import com.masi.logistics.repository.SupplierContractDetailRepository;
import com.masi.logistics.service.dto.SupplierContractDetailDTO;
import com.masi.logistics.service.mapper.SupplierContractDetailMapper;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.masi.logistics.domain.SupplierContractDetail}.
 */
@Service
@Transactional
public class SupplierContractDetailService {

    private static final Logger LOG = LoggerFactory.getLogger(SupplierContractDetailService.class);

    private final SupplierContractDetailRepository supplierContractDetailRepository;

    private final SupplierContractDetailMapper supplierContractDetailMapper;

    public SupplierContractDetailService(
        SupplierContractDetailRepository supplierContractDetailRepository,
        SupplierContractDetailMapper supplierContractDetailMapper
    ) {
        this.supplierContractDetailRepository = supplierContractDetailRepository;
        this.supplierContractDetailMapper = supplierContractDetailMapper;
    }

    /**
     * Save a supplierContractDetail.
     *
     * @param supplierContractDetailDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<SupplierContractDetailDTO> save(SupplierContractDetailDTO supplierContractDetailDTO) {
        LOG.debug("Request to save SupplierContractDetail : {}", supplierContractDetailDTO);
        return supplierContractDetailRepository
            .save(supplierContractDetailMapper.toEntity(supplierContractDetailDTO))
            .map(supplierContractDetailMapper::toDto);
    }

    /**
     * Update a supplierContractDetail.
     *
     * @param supplierContractDetailDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<SupplierContractDetailDTO> update(SupplierContractDetailDTO supplierContractDetailDTO) {
        LOG.debug("Request to update SupplierContractDetail : {}", supplierContractDetailDTO);
        return supplierContractDetailRepository
            .save(supplierContractDetailMapper.toEntity(supplierContractDetailDTO).setIsPersisted())
            .map(supplierContractDetailMapper::toDto);
    }

    /**
     * Partially update a supplierContractDetail.
     *
     * @param supplierContractDetailDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<SupplierContractDetailDTO> partialUpdate(SupplierContractDetailDTO supplierContractDetailDTO) {
        LOG.debug("Request to partially update SupplierContractDetail : {}", supplierContractDetailDTO);

        return supplierContractDetailRepository
            .findById(supplierContractDetailDTO.getId())
            .map(existingSupplierContractDetail -> {
                supplierContractDetailMapper.partialUpdate(existingSupplierContractDetail, supplierContractDetailDTO);

                return existingSupplierContractDetail;
            })
            .flatMap(supplierContractDetailRepository::save)
            .map(supplierContractDetailMapper::toDto);
    }

    /**
     * Find supplierContractDetails by Criteria.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<SupplierContractDetailDTO> findByCriteria(SupplierContractDetailCriteria criteria, Pageable pageable) {
        LOG.debug("Request to get all SupplierContractDetails by Criteria");
        return supplierContractDetailRepository.findByCriteria(criteria, pageable).map(supplierContractDetailMapper::toDto);
    }

    /**
     * Find the count of supplierContractDetails by criteria.
     * @param criteria filtering criteria
     * @return the count of supplierContractDetails
     */
    public Mono<Long> countByCriteria(SupplierContractDetailCriteria criteria) {
        LOG.debug("Request to get the count of all SupplierContractDetails by Criteria");
        return supplierContractDetailRepository.countByCriteria(criteria);
    }

    /**
     * Returns the number of supplierContractDetails available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return supplierContractDetailRepository.count();
    }

    /**
     * Get one supplierContractDetail by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<SupplierContractDetailDTO> findOne(UUID id) {
        LOG.debug("Request to get SupplierContractDetail : {}", id);
        return supplierContractDetailRepository.findById(id).map(supplierContractDetailMapper::toDto);
    }

    /**
     * Delete the supplierContractDetail by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(UUID id) {
        LOG.debug("Request to delete SupplierContractDetail : {}", id);
        return supplierContractDetailRepository.deleteById(id);
    }
}
