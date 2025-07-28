package com.masi.logistics.service;

import com.masi.logistics.domain.criteria.AssetTransferDetailsCriteria;
import com.masi.logistics.repository.AssetTransferDetailsRepository;
import com.masi.logistics.service.dto.AssetTransferDetailsDTO;
import com.masi.logistics.service.mapper.AssetTransferDetailsMapper;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.masi.logistics.domain.AssetTransferDetails}.
 */
@Service
@Transactional
public class AssetTransferDetailsService {

    private static final Logger log = LoggerFactory.getLogger(AssetTransferDetailsService.class);

    private final AssetTransferDetailsRepository assetTransferDetailsRepository;

    private final AssetTransferDetailsMapper assetTransferDetailsMapper;

    public AssetTransferDetailsService(
        AssetTransferDetailsRepository assetTransferDetailsRepository,
        AssetTransferDetailsMapper assetTransferDetailsMapper
    ) {
        this.assetTransferDetailsRepository = assetTransferDetailsRepository;
        this.assetTransferDetailsMapper = assetTransferDetailsMapper;
    }

    /**
     * Save a assetTransferDetails.
     *
     * @param assetTransferDetailsDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<AssetTransferDetailsDTO> save(AssetTransferDetailsDTO assetTransferDetailsDTO) {
        log.debug("Request to save AssetTransferDetails : {}", assetTransferDetailsDTO);
        return assetTransferDetailsRepository
            .save(assetTransferDetailsMapper.toEntity(assetTransferDetailsDTO))
            .map(assetTransferDetailsMapper::toDto);
    }

    /**
     * Update a assetTransferDetails.
     *
     * @param assetTransferDetailsDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<AssetTransferDetailsDTO> update(AssetTransferDetailsDTO assetTransferDetailsDTO) {
        log.debug("Request to update AssetTransferDetails : {}", assetTransferDetailsDTO);
        return assetTransferDetailsRepository
            .save(assetTransferDetailsMapper.toEntity(assetTransferDetailsDTO).setIsPersisted())
            .map(assetTransferDetailsMapper::toDto);
    }

    /**
     * Partially update a assetTransferDetails.
     *
     * @param assetTransferDetailsDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<AssetTransferDetailsDTO> partialUpdate(AssetTransferDetailsDTO assetTransferDetailsDTO) {
        log.debug("Request to partially update AssetTransferDetails : {}", assetTransferDetailsDTO);

        return assetTransferDetailsRepository
            .findById(assetTransferDetailsDTO.getId())
            .map(existingAssetTransferDetails -> {
                assetTransferDetailsMapper.partialUpdate(existingAssetTransferDetails, assetTransferDetailsDTO);

                return existingAssetTransferDetails;
            })
            .flatMap(assetTransferDetailsRepository::save)
            .map(assetTransferDetailsMapper::toDto);
    }

    /**
     * Find assetTransferDetails by Criteria.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<AssetTransferDetailsDTO> findByCriteria(AssetTransferDetailsCriteria criteria, Pageable pageable) {
        log.debug("Request to get all AssetTransferDetails by Criteria");
        return assetTransferDetailsRepository.findByCriteria(criteria, pageable).map(assetTransferDetailsMapper::toDto);
    }

    /**
     * Find the count of assetTransferDetails by criteria.
     * @param criteria filtering criteria
     * @return the count of assetTransferDetails
     */
    public Mono<Long> countByCriteria(AssetTransferDetailsCriteria criteria) {
        log.debug("Request to get the count of all AssetTransferDetails by Criteria");
        return assetTransferDetailsRepository.countByCriteria(criteria);
    }

    /**
     * Returns the number of assetTransferDetails available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return assetTransferDetailsRepository.count();
    }

    /**
     * Get one assetTransferDetails by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<AssetTransferDetailsDTO> findOne(UUID id) {
        log.debug("Request to get AssetTransferDetails : {}", id);
        return assetTransferDetailsRepository.findById(id).map(assetTransferDetailsMapper::toDto);
    }

    /**
     * Delete the assetTransferDetails by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(UUID id) {
        log.debug("Request to delete AssetTransferDetails : {}", id);
        return assetTransferDetailsRepository.deleteById(id);
    }
}
