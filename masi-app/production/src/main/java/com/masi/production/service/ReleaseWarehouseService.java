package com.masi.production.service;

import com.masi.production.repository.ReleaseWarehouseRepository;
import com.masi.production.service.dto.ReleaseWarehouseDTO;
import com.masi.production.service.mapper.ReleaseWarehouseMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Service Implementation for managing {@link com.masi.sale.domain.ReleaseWarehouse}.
 */
@Service
@Transactional
public class ReleaseWarehouseService {

    private static final Logger log = LoggerFactory.getLogger(ReleaseWarehouseService.class);

    private final ReleaseWarehouseRepository releaseWarehouseRepository;

    private final ReleaseWarehouseMapper releaseWarehouseMapper;

    public ReleaseWarehouseService(ReleaseWarehouseRepository releaseWarehouseRepository, ReleaseWarehouseMapper releaseWarehouseMapper) {
        this.releaseWarehouseRepository = releaseWarehouseRepository;
        this.releaseWarehouseMapper = releaseWarehouseMapper;
    }

    /**
     * Save a releaseWarehouse.
     *
     * @param releaseWarehouseDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ReleaseWarehouseDTO> save(ReleaseWarehouseDTO releaseWarehouseDTO) {
        log.debug("Request to save ReleaseWarehouse : {}", releaseWarehouseDTO);
        return releaseWarehouseRepository.save(releaseWarehouseMapper.toEntity(releaseWarehouseDTO)).map(releaseWarehouseMapper::toDto);
    }

    /**
     * Update a releaseWarehouse.
     *
     * @param releaseWarehouseDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ReleaseWarehouseDTO> update(ReleaseWarehouseDTO releaseWarehouseDTO) {
        log.debug("Request to update ReleaseWarehouse : {}", releaseWarehouseDTO);
        return releaseWarehouseRepository
            .save(releaseWarehouseMapper.toEntity(releaseWarehouseDTO).setIsPersisted())
            .map(releaseWarehouseMapper::toDto);
    }

    /**
     * Partially update a releaseWarehouse.
     *
     * @param releaseWarehouseDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<ReleaseWarehouseDTO> partialUpdate(ReleaseWarehouseDTO releaseWarehouseDTO) {
        log.debug("Request to partially update ReleaseWarehouse : {}", releaseWarehouseDTO);

        return releaseWarehouseRepository
            .findById(releaseWarehouseDTO.getId())
            .map(existingReleaseWarehouse -> {
                releaseWarehouseMapper.partialUpdate(existingReleaseWarehouse, releaseWarehouseDTO);

                return existingReleaseWarehouse;
            })
            .flatMap(releaseWarehouseRepository::save)
            .map(releaseWarehouseMapper::toDto);
    }

    /**
     * Get all the releaseWarehouses.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<ReleaseWarehouseDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ReleaseWarehouses");
        return releaseWarehouseRepository.findAllBy(pageable).map(releaseWarehouseMapper::toDto);
    }

    /**
     * Returns the number of releaseWarehouses available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return releaseWarehouseRepository.count();
    }

    /**
     * Get one releaseWarehouse by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<ReleaseWarehouseDTO> findOne(UUID id) {
        log.debug("Request to get ReleaseWarehouse : {}", id);
        return releaseWarehouseRepository.findById(id).map(releaseWarehouseMapper::toDto);
    }

    /**
     * Delete the releaseWarehouse by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(UUID id) {
        log.debug("Request to delete ReleaseWarehouse : {}", id);
        return releaseWarehouseRepository.deleteById(id);
    }
}
