package com.masi.logistics.service;

import com.masi.logistics.domain.criteria.ContactCriteria;
import com.masi.logistics.repository.ContactGiftRepository;
import com.masi.logistics.repository.ContactRepository;
import com.masi.logistics.service.dto.ContactDTO;
import com.masi.logistics.service.mapper.ContactGiftMapper;
import com.masi.logistics.service.mapper.ContactMapper;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.masi.logistics.domain.Contact}.
 */
@Service
@Transactional
public class ContactService {

    private static final Logger log = LoggerFactory.getLogger(ContactService.class);

    private final ContactRepository contactRepository;

    private final ContactMapper contactMapper;
    private final ContactGiftMapper contactGiftsMapper;
    private final ContactGiftRepository contactGiftRepository;

    public ContactService(ContactRepository contactRepository, ContactMapper contactMapper, ContactGiftMapper contactGiftsMapper, ContactGiftRepository contactGiftRepository) {
        this.contactRepository = contactRepository;
        this.contactMapper = contactMapper;
        this.contactGiftsMapper = contactGiftsMapper;
        this.contactGiftRepository = contactGiftRepository;
    }

    /**
     * Save a contact.
     *
     * @param contactDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ContactDTO> save(ContactDTO contactDTO) {
        log.debug("Request to save Contact : {}", contactDTO);
        return contactRepository.save(contactMapper.toEntity(contactDTO)).map(contactMapper::toDto);
    }

    /**
     * Update a contact.
     *
     * @param contactDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ContactDTO> update(ContactDTO contactDTO) {
        log.debug("Request to update Contact : {}", contactDTO);
        return contactRepository.save(contactMapper.toEntity(contactDTO).setIsPersisted()).map(contactMapper::toDto);
    }

    /**
     * Partially update a contact.
     *
     * @param contactDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<ContactDTO> partialUpdate(ContactDTO contactDTO) {
        log.debug("Request to partially update Contact : {}", contactDTO);

        return contactRepository
            .findById(contactDTO.getId())
            .map(existingContact -> {
                contactMapper.partialUpdate(existingContact, contactDTO);

                return existingContact;
            })
            .flatMap(contactRepository::save)
            .map(contactMapper::toDto);
    }

    /**
     * Find contacts by Criteria.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<ContactDTO> findByCriteria(ContactCriteria criteria, Pageable pageable) {
        log.debug("Request to get all Contacts by Criteria");
        return contactRepository.findByCriteria(criteria, pageable).map(contactMapper::toDto);
    }

    /**
     * Find the count of contacts by criteria.
     * @param criteria filtering criteria
     * @return the count of contacts
     */
    public Mono<Long> countByCriteria(ContactCriteria criteria) {
        log.debug("Request to get the count of all Contacts by Criteria");
        return contactRepository.countByCriteria(criteria);
    }

    /**
     * Returns the number of contacts available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return contactRepository.count();
    }

    /**
     * Get one contact by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<ContactDTO> findOne(UUID id) {
        log.debug("Request to get Contact : {}", id);
        return contactRepository.findById(id).map(contactMapper::toDto);
    }

    /**
     * Delete the contact by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(UUID id) {
        log.debug("Request to delete Contact : {}", id);
        return contactRepository.deleteById(id);
    }

    public Flux<ContactDTO> getContactBySupplierId(UUID supplierId, String company) {
        log.debug("Request to get all Contacts by SupplierId");
        return contactRepository.findAllBySupplierId(supplierId, company).map(contactMapper::toDto).flatMap(contact -> {
            return contactGiftRepository.findAllByContactId(contact.getId(), company)
                .collectList().map(contactGiftsMapper::toDto)
                .map(contactGifts -> {
                    contact.setContactGifts(contactGifts);
                    return contact;
            });
        });
    }

    public Mono<Void> removeAllBySupplierId(UUID supplierId, UUID deletedBy ,String company) {
        log.debug("Request to delete all Contacts by SupplierId");
        return contactRepository.findAllBySupplierId(supplierId, company).flatMap(contact -> {
            return contactGiftRepository.deletedByContactId(contact.getId(), deletedBy, company );
        }).then(contactRepository.deleteBySupplierId(supplierId, deletedBy, company));
    }
}
