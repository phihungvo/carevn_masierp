package com.masi.logistics.service;

import com.masi.logistics.repository.UomGroupDetailsRepository;
import com.masi.logistics.service.dto.UomGroupDetailsDTO;
import com.masi.logistics.service.mapper.UomGroupDetailsMapper;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.masi.logistics.domain.UomGroupDetails}.
 */
@Service
@Transactional
public class UomGroupDetailsService {

    private static final Logger log = LoggerFactory.getLogger(UomGroupDetailsService.class);

    private final UomGroupDetailsRepository uomGroupDetailsRepository;

    private final UomGroupDetailsMapper uomGroupDetailsMapper;

    public UomGroupDetailsService(UomGroupDetailsRepository uomGroupDetailsRepository, UomGroupDetailsMapper uomGroupDetailsMapper) {
        this.uomGroupDetailsRepository = uomGroupDetailsRepository;
        this.uomGroupDetailsMapper = uomGroupDetailsMapper;
    }

    /**
     * Save a uomGroupDetails.
     *
     * @param uomGroupDetailsDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<UomGroupDetailsDTO> save(UomGroupDetailsDTO uomGroupDetailsDTO) {
        log.debug("Request to save UomGroupDetails : {}", uomGroupDetailsDTO);
        return uomGroupDetailsRepository.save(uomGroupDetailsMapper.toEntity(uomGroupDetailsDTO)).map(uomGroupDetailsMapper::toDto);
    }

    /**
     * Update a uomGroupDetails.
     *
     * @param uomGroupDetailsDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<UomGroupDetailsDTO> update(UomGroupDetailsDTO uomGroupDetailsDTO) {
        log.debug("Request to update UomGroupDetails : {}", uomGroupDetailsDTO);
        return uomGroupDetailsRepository
            .save(uomGroupDetailsMapper.toEntity(uomGroupDetailsDTO).setIsPersisted())
            .map(uomGroupDetailsMapper::toDto);
    }

    /**
     * Partially update a uomGroupDetails.
     *
     * @param uomGroupDetailsDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<UomGroupDetailsDTO> partialUpdate(UomGroupDetailsDTO uomGroupDetailsDTO) {
        log.debug("Request to partially update UomGroupDetails : {}", uomGroupDetailsDTO);

        return uomGroupDetailsRepository
            .findById(uomGroupDetailsDTO.getId())
            .map(existingUomGroupDetails -> {
                uomGroupDetailsMapper.partialUpdate(existingUomGroupDetails, uomGroupDetailsDTO);

                return existingUomGroupDetails;
            })
            .flatMap(uomGroupDetailsRepository::save)
            .map(uomGroupDetailsMapper::toDto);
    }

    /**
     * Get all the uomGroupDetails.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<UomGroupDetailsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all UomGroupDetails");
        return uomGroupDetailsRepository.findAllBy(pageable).map(uomGroupDetailsMapper::toDto);
    }

    /**
     * Returns the number of uomGroupDetails available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return uomGroupDetailsRepository.count();
    }

    /**
     * Get one uomGroupDetails by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<UomGroupDetailsDTO> findOne(UUID id) {
        log.debug("Request to get UomGroupDetails : {}", id);
        return uomGroupDetailsRepository.findById(id).map(uomGroupDetailsMapper::toDto);
    }

    /**
     * Delete the uomGroupDetails by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(UUID id) {
        log.debug("Request to delete UomGroupDetails : {}", id);
        return uomGroupDetailsRepository.deleteById(id);
    }
}
