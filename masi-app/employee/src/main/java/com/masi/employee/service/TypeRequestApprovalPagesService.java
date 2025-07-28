package com.masi.employee.service;

import com.masi.employee.domain.criteria.TypeRequestApprovalPagesCriteria;
import com.masi.employee.repository.TypeRequestApprovalPagesRepository;
import com.masi.employee.service.dto.TypeRequestApprovalPagesDTO;
import com.masi.employee.service.mapper.TypeRequestApprovalPagesMapper;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.masi.employee.domain.TypeRequestApprovalPages}.
 */
@Service
@Transactional
public class TypeRequestApprovalPagesService {

    private static final Logger log = LoggerFactory.getLogger(TypeRequestApprovalPagesService.class);

    private final TypeRequestApprovalPagesRepository typeRequestApprovalPagesRepository;

    private final TypeRequestApprovalPagesMapper typeRequestApprovalPagesMapper;

    public TypeRequestApprovalPagesService(
        TypeRequestApprovalPagesRepository typeRequestApprovalPagesRepository,
        TypeRequestApprovalPagesMapper typeRequestApprovalPagesMapper
    ) {
        this.typeRequestApprovalPagesRepository = typeRequestApprovalPagesRepository;
        this.typeRequestApprovalPagesMapper = typeRequestApprovalPagesMapper;
    }

    /**
     * Save a typeRequestApprovalPages.
     *
     * @param typeRequestApprovalPagesDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<TypeRequestApprovalPagesDTO> save(TypeRequestApprovalPagesDTO typeRequestApprovalPagesDTO) {
        log.debug("Request to save TypeRequestApprovalPages : {}", typeRequestApprovalPagesDTO);
        return typeRequestApprovalPagesRepository
            .save(typeRequestApprovalPagesMapper.toEntity(typeRequestApprovalPagesDTO))
            .map(typeRequestApprovalPagesMapper::toDto);
    }

    /**
     * Update a typeRequestApprovalPages.
     *
     * @param typeRequestApprovalPagesDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<TypeRequestApprovalPagesDTO> update(TypeRequestApprovalPagesDTO typeRequestApprovalPagesDTO) {
        log.debug("Request to update TypeRequestApprovalPages : {}", typeRequestApprovalPagesDTO);
        return typeRequestApprovalPagesRepository
            .save(typeRequestApprovalPagesMapper.toEntity(typeRequestApprovalPagesDTO).setIsPersisted())
            .map(typeRequestApprovalPagesMapper::toDto);
    }

    /**
     * Partially update a typeRequestApprovalPages.
     *
     * @param typeRequestApprovalPagesDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<TypeRequestApprovalPagesDTO> partialUpdate(TypeRequestApprovalPagesDTO typeRequestApprovalPagesDTO) {
        log.debug("Request to partially update TypeRequestApprovalPages : {}", typeRequestApprovalPagesDTO);

        return typeRequestApprovalPagesRepository
            .findById(typeRequestApprovalPagesDTO.getId())
            .map(existingTypeRequestApprovalPages -> {
                typeRequestApprovalPagesMapper.partialUpdate(existingTypeRequestApprovalPages, typeRequestApprovalPagesDTO);

                return existingTypeRequestApprovalPages;
            })
            .flatMap(typeRequestApprovalPagesRepository::save)
            .map(typeRequestApprovalPagesMapper::toDto);
    }

    /**
     * Find typeRequestApprovalPages by Criteria.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<TypeRequestApprovalPagesDTO> findByCriteria(TypeRequestApprovalPagesCriteria criteria, Pageable pageable) {
        log.debug("Request to get all TypeRequestApprovalPages by Criteria");
        return typeRequestApprovalPagesRepository.findByCriteria(criteria, pageable).map(typeRequestApprovalPagesMapper::toDto);
    }

    /**
     * Find the count of typeRequestApprovalPages by criteria.
     * @param criteria filtering criteria
     * @return the count of typeRequestApprovalPages
     */
    public Mono<Long> countByCriteria(TypeRequestApprovalPagesCriteria criteria) {
        log.debug("Request to get the count of all TypeRequestApprovalPages by Criteria");
        return typeRequestApprovalPagesRepository.countByCriteria(criteria);
    }

    /**
     * Returns the number of typeRequestApprovalPages available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return typeRequestApprovalPagesRepository.count();
    }

    /**
     * Get one typeRequestApprovalPages by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<TypeRequestApprovalPagesDTO> findOne(UUID id) {
        log.debug("Request to get TypeRequestApprovalPages : {}", id);
        return typeRequestApprovalPagesRepository.findById(id).map(typeRequestApprovalPagesMapper::toDto);
    }

    /**
     * Delete the typeRequestApprovalPages by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(UUID id) {
        log.debug("Request to delete TypeRequestApprovalPages : {}", id);
        return typeRequestApprovalPagesRepository.deleteById(id);
    }
}
