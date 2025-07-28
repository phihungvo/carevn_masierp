package com.masi.production.service;

import com.masi.production.domain.Factory;
import com.masi.production.domain.Storage;
import com.masi.production.repository.StorageRepository;
import com.masi.production.service.dto.StorageDTO;
import com.masi.production.service.mapper.StorageMapper;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.masi.production.domain.Storage}.
 */
@Service
@Transactional
public class StorageService {

    private final Logger log = LoggerFactory.getLogger(StorageService.class);

    private final StorageRepository storageRepository;

    private final StorageMapper storageMapper;

    public StorageService(StorageRepository storageRepository, StorageMapper storageMapper) {
        this.storageRepository = storageRepository;
        this.storageMapper = storageMapper;
    }

    /**
     * Save a storage.
     *
     * @param storageDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<StorageDTO> save(StorageDTO storageDTO) {
        log.debug("Request to save Storage : {}", storageDTO);
        return storageRepository.save(storageMapper.toEntity(storageDTO)).map(storageMapper::toDto);
    }

    /**
     * Update a storage.
     *
     * @param storageDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<StorageDTO> update(StorageDTO storageDTO) {
        log.debug("Request to update Storage : {}", storageDTO);
        return storageRepository.save(storageMapper.toEntity(storageDTO).setIsPersisted()).map(storageMapper::toDto);
    }

    /**
     * Partially update a storage.
     *
     * @param storageDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<StorageDTO> partialUpdate(StorageDTO storageDTO) {
        log.debug("Request to partially update Storage : {}", storageDTO);

        return storageRepository
            .findById(storageDTO.getId())
            .map(existingStorage -> {
                storageMapper.partialUpdate(existingStorage, storageDTO);

                return existingStorage;
            })
            .flatMap(storageRepository::save)
            .map(storageMapper::toDto);
    }

    /**
     * Get all the storages.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<StorageDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Storages");
        return storageRepository.findAllBy(pageable).map(storageMapper::toDto);
    }

    /**
     * Returns the number of storages available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return storageRepository.count();
    }

    /**
     * Get one storage by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<StorageDTO> findOne(UUID id) {
        log.debug("Request to get Storage : {}", id);
        return storageRepository.findById(id).map(storageMapper::toDto);
    }

    /**
     * Delete the storage by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(UUID id) {
        log.debug("Request to delete Storage : {}", id);
        return storageRepository.deleteById(id);
    }


}
