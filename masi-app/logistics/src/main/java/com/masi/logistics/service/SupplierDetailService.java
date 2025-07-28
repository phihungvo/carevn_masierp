package com.masi.logistics.service;

import com.masi.logistics.domain.criteria.SupplierDetailCriteria;
import com.masi.logistics.repository.SupplierDetailRepository;
import com.masi.logistics.service.dto.SupplierDetailDTO;
import com.masi.logistics.service.mapper.SupplierDetailMapper;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.masi.logistics.domain.SupplierDetail}.
 */
@Service
@Transactional
public class SupplierDetailService {

    private static final Logger log = LoggerFactory.getLogger(SupplierDetailService.class);

    private final SupplierDetailRepository supplierDetailRepository;

    private final SupplierDetailMapper supplierDetailMapper;

    public SupplierDetailService(SupplierDetailRepository supplierDetailRepository, SupplierDetailMapper supplierDetailMapper) {
        this.supplierDetailRepository = supplierDetailRepository;
        this.supplierDetailMapper = supplierDetailMapper;
    }

    /**
     * Save a supplierDetail.
     *
     * @param supplierDetailDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<SupplierDetailDTO> save(SupplierDetailDTO supplierDetailDTO) {
        log.debug("Request to save SupplierDetail : {}", supplierDetailDTO);
        return supplierDetailRepository.save(supplierDetailMapper.toEntity(supplierDetailDTO)).map(supplierDetailMapper::toDto);
    }

    /**
     * Update a supplierDetail.
     *
     * @param supplierDetailDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<SupplierDetailDTO> update(SupplierDetailDTO supplierDetailDTO) {
        log.debug("Request to update SupplierDetail : {}", supplierDetailDTO);
        return supplierDetailRepository
            .save(supplierDetailMapper.toEntity(supplierDetailDTO).setIsPersisted())
            .map(supplierDetailMapper::toDto);
    }

    /**
     * Partially update a supplierDetail.
     *
     * @param supplierDetailDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<SupplierDetailDTO> partialUpdate(SupplierDetailDTO supplierDetailDTO) {
        log.debug("Request to partially update SupplierDetail : {}", supplierDetailDTO);

        return supplierDetailRepository
            .findById(supplierDetailDTO.getId())
            .map(existingSupplierDetail -> {
                supplierDetailMapper.partialUpdate(existingSupplierDetail, supplierDetailDTO);
                existingSupplierDetail.setIsPersisted();
                return existingSupplierDetail;
            })
            .flatMap(supplierDetailRepository::save)
            .map(supplierDetailMapper::toDto);
    }

    /**
     * Find supplierDetails by Criteria.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<SupplierDetailDTO> findByCriteria(SupplierDetailCriteria criteria, Pageable pageable) {
        log.debug("Request to get all SupplierDetails by Criteria");
        return supplierDetailRepository.findByCriteria(criteria, pageable).map(supplierDetailMapper::toDto);
    }

    /**
     * Find the count of supplierDetails by criteria.
     * @param criteria filtering criteria
     * @return the count of supplierDetails
     */
    public Mono<Long> countByCriteria(SupplierDetailCriteria criteria) {
        log.debug("Request to get the count of all SupplierDetails by Criteria");
        return supplierDetailRepository.countByCriteria(criteria);
    }

    /**
     * Returns the number of supplierDetails available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return supplierDetailRepository.count();
    }

    /**
     * Get one supplierDetail by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<SupplierDetailDTO> findOne(UUID id) {
        log.debug("Request to get SupplierDetail : {}", id);
        return supplierDetailRepository.findById(id).map(supplierDetailMapper::toDto);
    }

    /**
     * Delete the supplierDetail by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(UUID id) {
        log.debug("Request to delete SupplierDetail : {}", id);
        return supplierDetailRepository.deleteById(id);
    }
}
