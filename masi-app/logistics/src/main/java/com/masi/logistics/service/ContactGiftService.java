package com.masi.logistics.service;

import com.masi.logistics.domain.criteria.ContactGiftCriteria;
import com.masi.logistics.repository.ContactGiftRepository;
import com.masi.logistics.service.dto.ContactGiftDTO;
import com.masi.logistics.service.mapper.ContactGiftMapper;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.masi.logistics.domain.ContactGift}.
 */
@Service
@Transactional
public class ContactGiftService {

    private static final Logger log = LoggerFactory.getLogger(ContactGiftService.class);

    private final ContactGiftRepository contactGiftRepository;

    private final ContactGiftMapper contactGiftMapper;

    public ContactGiftService(ContactGiftRepository contactGiftRepository, ContactGiftMapper contactGiftMapper) {
        this.contactGiftRepository = contactGiftRepository;
        this.contactGiftMapper = contactGiftMapper;
    }

    /**
     * Save a contactGift.
     *
     * @param contactGiftDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ContactGiftDTO> save(ContactGiftDTO contactGiftDTO) {
        log.debug("Request to save ContactGift : {}", contactGiftDTO);
        return contactGiftRepository.save(contactGiftMapper.toEntity(contactGiftDTO)).map(contactGiftMapper::toDto);
    }

    /**
     * Update a contactGift.
     *
     * @param contactGiftDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ContactGiftDTO> update(ContactGiftDTO contactGiftDTO) {
        log.debug("Request to update ContactGift : {}", contactGiftDTO);
        return contactGiftRepository.save(contactGiftMapper.toEntity(contactGiftDTO).setIsPersisted()).map(contactGiftMapper::toDto);
    }

    /**
     * Partially update a contactGift.
     *
     * @param contactGiftDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<ContactGiftDTO> partialUpdate(ContactGiftDTO contactGiftDTO) {
        log.debug("Request to partially update ContactGift : {}", contactGiftDTO);

        return contactGiftRepository
            .findById(contactGiftDTO.getId())
            .map(existingContactGift -> {
                contactGiftMapper.partialUpdate(existingContactGift, contactGiftDTO);

                return existingContactGift;
            })
            .flatMap(contactGiftRepository::save)
            .map(contactGiftMapper::toDto);
    }

    /**
     * Find contactGifts by Criteria.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<ContactGiftDTO> findByCriteria(ContactGiftCriteria criteria, Pageable pageable) {
        log.debug("Request to get all ContactGifts by Criteria");
        return contactGiftRepository.findByCriteria(criteria, pageable).map(contactGiftMapper::toDto);
    }

    /**
     * Find the count of contactGifts by criteria.
     * @param criteria filtering criteria
     * @return the count of contactGifts
     */
    public Mono<Long> countByCriteria(ContactGiftCriteria criteria) {
        log.debug("Request to get the count of all ContactGifts by Criteria");
        return contactGiftRepository.countByCriteria(criteria);
    }

    /**
     * Returns the number of contactGifts available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return contactGiftRepository.count();
    }

    /**
     * Get one contactGift by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<ContactGiftDTO> findOne(UUID id) {
        log.debug("Request to get ContactGift : {}", id);
        return contactGiftRepository.findById(id).map(contactGiftMapper::toDto);
    }

    /**
     * Delete the contactGift by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(UUID id) {
        log.debug("Request to delete ContactGift : {}", id);
        return contactGiftRepository.deleteById(id);
    }
}
