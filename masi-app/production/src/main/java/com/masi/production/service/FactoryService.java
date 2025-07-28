package com.masi.production.service;

import com.masi.production.domain.Factory;
import com.masi.production.repository.FactoryRepository;
import com.masi.production.service.dto.FactoryDTO;
import com.masi.production.service.mapper.FactoryMapper;
import java.util.UUID;

import com.masi.production.service.mapper.ProductRoutingMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.masi.production.domain.Factory}.
 */
@Service
@Transactional
public class FactoryService {

    private final Logger log = LoggerFactory.getLogger(FactoryService.class);

    private final FactoryRepository factoryRepository;

    private final FactoryMapper factoryMapper;

    public FactoryService(FactoryRepository factoryRepository, FactoryMapper factoryMapper) {
        this.factoryRepository = factoryRepository;
        this.factoryMapper = factoryMapper;
    }

    /**
     * Save a factory.
     *
     * @param factoryDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<FactoryDTO> save(FactoryDTO factoryDTO) {
        log.debug("Request to save Factory : {}", factoryDTO);
        return factoryRepository.save(factoryMapper.toEntity(factoryDTO)).map(factoryMapper::toDto);
    }

    /**
     * Update a factory.
     *
     * @param factoryDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<FactoryDTO> update(FactoryDTO factoryDTO) {
        log.debug("Request to update Factory : {}", factoryDTO);
        return factoryRepository.save(factoryMapper.toEntity(factoryDTO).setIsPersisted()).map(factoryMapper::toDto);
    }

    /**
     * Partially update a factory.
     *
     * @param factoryDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<FactoryDTO> partialUpdate(FactoryDTO factoryDTO) {
        log.debug("Request to partially update Factory : {}", factoryDTO);

        return factoryRepository
            .findById(factoryDTO.getId())
            .map(existingFactory -> {
                factoryMapper.partialUpdate(existingFactory, factoryDTO);

                return existingFactory;
            })
            .flatMap(factoryRepository::save)
            .map(factoryMapper::toDto);
    }

    /**
     * Get all the factories.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<FactoryDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Factories");
        return factoryRepository.findAllBy(pageable).map(factoryMapper::toDto);
    }

    /**
     * Returns the number of factories available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return factoryRepository.count();
    }

    /**
     * Get one factory by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<FactoryDTO> findOne(UUID id) {
        log.debug("Request to get Factory : {}", id);
        return factoryRepository.findById(id).map(factoryMapper::toDto);
    }

    /**
     * Delete the factory by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(UUID id) {
        log.debug("Request to delete Factory : {}", id);
        return factoryRepository.deleteById(id);
    }

}
