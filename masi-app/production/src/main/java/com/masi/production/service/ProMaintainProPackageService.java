package com.masi.production.service;

import com.masi.production.repository.ProMaintainProPackageRepository;
import com.masi.production.service.dto.ProMaintainProPackageDTO;
import com.masi.production.service.mapper.ProMaintainProPackageMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Service Implementation for managing {@link com.masi.production.domain.ProMaintainProPackage}.
 */
@Service
@Transactional
public class ProMaintainProPackageService {

    private static final Logger log = LoggerFactory.getLogger(ProMaintainProPackageService.class);

    private final ProMaintainProPackageRepository proMaintainProPackageRepository;

    private final ProMaintainProPackageMapper proMaintainProPackageMapper;

    public ProMaintainProPackageService(
        ProMaintainProPackageRepository proMaintainProPackageRepository,
        ProMaintainProPackageMapper proMaintainProPackageMapper
    ) {
        this.proMaintainProPackageRepository = proMaintainProPackageRepository;
        this.proMaintainProPackageMapper = proMaintainProPackageMapper;
    }

    /**
     * Save a proMaintainProPackage.
     *
     * @param proMaintainProPackageDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ProMaintainProPackageDTO> save(ProMaintainProPackageDTO proMaintainProPackageDTO) {
        log.debug("Request to save ProMaintainProPackage : {}", proMaintainProPackageDTO);
        return proMaintainProPackageRepository
            .save(proMaintainProPackageMapper.toEntity(proMaintainProPackageDTO))
            .map(proMaintainProPackageMapper::toDto);
    }

    /**
     * Update a proMaintainProPackage.
     *
     * @param proMaintainProPackageDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ProMaintainProPackageDTO> update(ProMaintainProPackageDTO proMaintainProPackageDTO) {
        log.debug("Request to update ProMaintainProPackage : {}", proMaintainProPackageDTO);
        return proMaintainProPackageRepository
            .save(proMaintainProPackageMapper.toEntity(proMaintainProPackageDTO).setIsPersisted())
            .map(proMaintainProPackageMapper::toDto);
    }

    /**
     * Partially update a proMaintainProPackage.
     *
     * @param proMaintainProPackageDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<ProMaintainProPackageDTO> partialUpdate(ProMaintainProPackageDTO proMaintainProPackageDTO) {
        log.debug("Request to partially update ProMaintainProPackage : {}", proMaintainProPackageDTO);

        return proMaintainProPackageRepository
            .findById(proMaintainProPackageDTO.getId())
            .map(existingProMaintainProPackage -> {
                proMaintainProPackageMapper.partialUpdate(existingProMaintainProPackage, proMaintainProPackageDTO);

                return existingProMaintainProPackage;
            })
            .flatMap(proMaintainProPackageRepository::save)
            .map(proMaintainProPackageMapper::toDto);
    }

    /**
     * Get all the proMaintainProPackages.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<ProMaintainProPackageDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ProMaintainProPackages");
        return proMaintainProPackageRepository.findAllBy(pageable).map(proMaintainProPackageMapper::toDto);
    }

    /**
     * Returns the number of proMaintainProPackages available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return proMaintainProPackageRepository.count();
    }

    /**
     * Get one proMaintainProPackage by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<ProMaintainProPackageDTO> findOne(UUID id) {
        log.debug("Request to get ProMaintainProPackage : {}", id);
        return proMaintainProPackageRepository.findById(id).map(proMaintainProPackageMapper::toDto);
    }

    /**
     * Delete the proMaintainProPackage by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(UUID id) {
        log.debug("Request to delete ProMaintainProPackage : {}", id);
        return proMaintainProPackageRepository.deleteById(id);
    }
}
