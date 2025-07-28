package com.masi.logistics.service;

import com.masi.logistics.domain.criteria.TypeOfFGenerationCriteria;
import com.masi.logistics.repository.TypeOfFGenerationRepository;
import com.masi.logistics.service.dto.TypeOfFGenerationDTO;
import com.masi.logistics.service.mapper.TypeOfFGenerationMapper;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.masi.logistics.domain.TypeOfFGeneration}.
 */
@Service
@Transactional
public class TypeOfFGenerationService {

    private static final Logger log = LoggerFactory.getLogger(TypeOfFGenerationService.class);

    private final TypeOfFGenerationRepository typeOfFGenerationRepository;

    private final TypeOfFGenerationMapper typeOfFGenerationMapper;

    public TypeOfFGenerationService(
        TypeOfFGenerationRepository typeOfFGenerationRepository,
        TypeOfFGenerationMapper typeOfFGenerationMapper
    ) {
        this.typeOfFGenerationRepository = typeOfFGenerationRepository;
        this.typeOfFGenerationMapper = typeOfFGenerationMapper;
    }

    /**
     * Save a typeOfFGeneration.
     *
     * @param typeOfFGenerationDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<TypeOfFGenerationDTO> save(TypeOfFGenerationDTO typeOfFGenerationDTO) {
        log.debug("Request to save TypeOfFGeneration : {}", typeOfFGenerationDTO);
        return typeOfFGenerationRepository.save(typeOfFGenerationMapper.toEntity(typeOfFGenerationDTO)).map(typeOfFGenerationMapper::toDto);
    }

    /**
     * Update a typeOfFGeneration.
     *
     * @param typeOfFGenerationDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<TypeOfFGenerationDTO> update(TypeOfFGenerationDTO typeOfFGenerationDTO) {
        log.debug("Request to update TypeOfFGeneration : {}", typeOfFGenerationDTO);
        return typeOfFGenerationRepository
            .save(typeOfFGenerationMapper.toEntity(typeOfFGenerationDTO).setIsPersisted())
            .map(typeOfFGenerationMapper::toDto);
    }

    /**
     * Partially update a typeOfFGeneration.
     *
     * @param typeOfFGenerationDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<TypeOfFGenerationDTO> partialUpdate(TypeOfFGenerationDTO typeOfFGenerationDTO) {
        log.debug("Request to partially update TypeOfFGeneration : {}", typeOfFGenerationDTO);

        return typeOfFGenerationRepository
            .findById(typeOfFGenerationDTO.getId())
            .map(existingTypeOfFGeneration -> {
                typeOfFGenerationMapper.partialUpdate(existingTypeOfFGeneration, typeOfFGenerationDTO);

                return existingTypeOfFGeneration;
            })
            .flatMap(typeOfFGenerationRepository::save)
            .map(typeOfFGenerationMapper::toDto);
    }

    /**
     * Find typeOfFGenerations by Criteria.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<TypeOfFGenerationDTO> findByCriteria(TypeOfFGenerationCriteria criteria, Pageable pageable) {
        log.debug("Request to get all TypeOfFGenerations by Criteria");
        return typeOfFGenerationRepository.findByCriteria(criteria, pageable).map(typeOfFGenerationMapper::toDto);
    }

    /**
     * Find the count of typeOfFGenerations by criteria.
     * @param criteria filtering criteria
     * @return the count of typeOfFGenerations
     */
    public Mono<Long> countByCriteria(TypeOfFGenerationCriteria criteria) {
        log.debug("Request to get the count of all TypeOfFGenerations by Criteria");
        return typeOfFGenerationRepository.countByCriteria(criteria);
    }

    /**
     * Returns the number of typeOfFGenerations available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return typeOfFGenerationRepository.count();
    }

    /**
     * Get one typeOfFGeneration by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<TypeOfFGenerationDTO> findOne(UUID id) {
        log.debug("Request to get TypeOfFGeneration : {}", id);
        return typeOfFGenerationRepository.findById(id).map(typeOfFGenerationMapper::toDto);
    }

    /**
     * Delete the typeOfFGeneration by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(UUID id) {
        log.debug("Request to delete TypeOfFGeneration : {}", id);
        return typeOfFGenerationRepository.deleteById(id);
    }
}
