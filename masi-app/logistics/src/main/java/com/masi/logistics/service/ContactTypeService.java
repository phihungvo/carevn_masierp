package com.masi.logistics.service;

import com.masi.logistics.domain.criteria.ContactTypeCriteria;
import com.masi.logistics.repository.ContactTypeRepository;
import com.masi.logistics.service.dto.ContactTypeDTO;
import com.masi.logistics.service.mapper.ContactTypeMapper;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.masi.logistics.domain.ContactType}.
 */
@Service
@Transactional
public class ContactTypeService {

    private static final Logger log = LoggerFactory.getLogger(ContactTypeService.class);

    private final ContactTypeRepository contactTypeRepository;

    private final ContactTypeMapper contactTypeMapper;

    public ContactTypeService(ContactTypeRepository contactTypeRepository, ContactTypeMapper contactTypeMapper) {
        this.contactTypeRepository = contactTypeRepository;
        this.contactTypeMapper = contactTypeMapper;
    }

    /**
     * Save a contactType.
     *
     * @param contactTypeDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ContactTypeDTO> save(ContactTypeDTO contactTypeDTO) {
        log.debug("Request to save ContactType : {}", contactTypeDTO);
        return contactTypeRepository.save(contactTypeMapper.toEntity(contactTypeDTO)).map(contactTypeMapper::toDto);
    }

    /**
     * Update a contactType.
     *
     * @param contactTypeDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ContactTypeDTO> update(ContactTypeDTO contactTypeDTO) {
        log.debug("Request to update ContactType : {}", contactTypeDTO);
        return contactTypeRepository.save(contactTypeMapper.toEntity(contactTypeDTO).setIsPersisted()).map(contactTypeMapper::toDto);
    }

    /**
     * Partially update a contactType.
     *
     * @param contactTypeDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<ContactTypeDTO> partialUpdate(ContactTypeDTO contactTypeDTO) {
        log.debug("Request to partially update ContactType : {}", contactTypeDTO);

        return contactTypeRepository
            .findById(contactTypeDTO.getId())
            .map(existingContactType -> {
                contactTypeMapper.partialUpdate(existingContactType, contactTypeDTO);

                return existingContactType;
            })
            .flatMap(contactTypeRepository::save)
            .map(contactTypeMapper::toDto);
    }

    /**
     * Find contactTypes by Criteria.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<ContactTypeDTO> findByCriteria(ContactTypeCriteria criteria, Pageable pageable) {
        log.debug("Request to get all ContactTypes by Criteria");
        return contactTypeRepository.findByCriteria(criteria, pageable).map(contactTypeMapper::toDto);
    }

    /**
     * Find the count of contactTypes by criteria.
     * @param criteria filtering criteria
     * @return the count of contactTypes
     */
    public Mono<Long> countByCriteria(ContactTypeCriteria criteria) {
        log.debug("Request to get the count of all ContactTypes by Criteria");
        return contactTypeRepository.countByCriteria(criteria);
    }

    /**
     * Returns the number of contactTypes available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return contactTypeRepository.count();
    }

    /**
     * Get one contactType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<ContactTypeDTO> findOne(UUID id) {
        log.debug("Request to get ContactType : {}", id);
        return contactTypeRepository.findById(id).map(contactTypeMapper::toDto);
    }

    /**
     * Delete the contactType by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(UUID id) {
        log.debug("Request to delete ContactType : {}", id);
        return contactTypeRepository.deleteById(id);
    }
}
