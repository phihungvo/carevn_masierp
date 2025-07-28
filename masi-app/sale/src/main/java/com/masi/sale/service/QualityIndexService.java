package com.masi.sale.service;

import com.masi.sale.repository.QualityIndexRepository;
import com.masi.sale.service.dto.QualityIndexDTO;
import com.masi.sale.service.mapper.QualityIndexMapper;

import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing
 * {@link com.masi.sale.domain.QualityIndex}.
 */
@Service
@Transactional
public class QualityIndexService {

    private static final Logger log = LoggerFactory.getLogger(QualityIndexService.class);

    private final QualityIndexRepository qualityIndexRepository;

    private final QualityIndexMapper qualityIndexMapper;

    public QualityIndexService(QualityIndexRepository qualityIndexRepository, QualityIndexMapper qualityIndexMapper) {
        this.qualityIndexRepository = qualityIndexRepository;
        this.qualityIndexMapper = qualityIndexMapper;
    }

    /**
     * Save a qualityIndex.
     *
     * @param qualityIndexDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<QualityIndexDTO> save(QualityIndexDTO qualityIndexDTO) {
        log.debug("Request to save QualityIndex : {}", qualityIndexDTO);
        return qualityIndexRepository.save(qualityIndexMapper.toEntity(qualityIndexDTO)).map(qualityIndexMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Mono<List<String>> findAllByDistinctName() {
        return qualityIndexRepository.findAllByDistinctName().collectList();
    }

    /**
     * Update a qualityIndex.
     *
     * @param qualityIndexDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<QualityIndexDTO> update(QualityIndexDTO qualityIndexDTO) {
        log.debug("Request to update QualityIndex : {}", qualityIndexDTO);
        return qualityIndexRepository.save(qualityIndexMapper.toEntity(qualityIndexDTO).setIsPersisted())
                .map(qualityIndexMapper::toDto);
    }

    /**
     * Partially update a qualityIndex.
     *
     * @param qualityIndexDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<QualityIndexDTO> partialUpdate(QualityIndexDTO qualityIndexDTO) {
        log.debug("Request to partially update QualityIndex : {}", qualityIndexDTO);

        return qualityIndexRepository
                .findById(qualityIndexDTO.getId())
                .map(existingQualityIndex -> {
                    qualityIndexMapper.partialUpdate(existingQualityIndex, qualityIndexDTO);

                    return existingQualityIndex;
                })
                .flatMap(qualityIndexRepository::save)
                .map(qualityIndexMapper::toDto);
    }

    /**
     * Get all the qualityIndices.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<QualityIndexDTO> findAll(Pageable pageable) {
        log.debug("Request to get all QualityIndices");
        return qualityIndexRepository.findAllBy(pageable).map(qualityIndexMapper::toDto);
    }

    /**
     * Returns the number of qualityIndices available.
     *
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return qualityIndexRepository.count();
    }

    /**
     * Get one qualityIndex by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<QualityIndexDTO> findOne(UUID id) {
        log.debug("Request to get QualityIndex : {}", id);
        return qualityIndexRepository.findById(id).map(qualityIndexMapper::toDto);
    }

    /**
     * Delete the qualityIndex by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(UUID id) {
        log.debug("Request to delete QualityIndex : {}", id);
        return qualityIndexRepository.deleteById(id);
    }
}
