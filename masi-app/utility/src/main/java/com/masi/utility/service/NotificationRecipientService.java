package com.masi.utility.service;

import com.carevn.masi.utils.SecurityUtils;
import com.masi.utility.domain.criteria.NotificationRecipientCriteria;
import com.masi.utility.repository.NotificationRecipientRepository;
import com.masi.utility.service.dto.NotificationRecipientDTO;
import com.masi.utility.service.mapper.NotificationRecipientMapper;

import java.util.List;
import java.util.UUID;

import javax.management.NotificationFilter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.converters.models.Sort;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing
 * {@link com.masi.utility.domain.NotificationRecipient}.
 */
@Service
@Transactional
public class NotificationRecipientService {

    private static final Logger log = LoggerFactory.getLogger(NotificationRecipientService.class);

    private final NotificationRecipientRepository notificationRecipientRepository;

    private final NotificationRecipientMapper notificationRecipientMapper;

    public NotificationRecipientService(
        NotificationRecipientRepository notificationRecipientRepository,
        NotificationRecipientMapper notificationRecipientMapper) {
        this.notificationRecipientRepository = notificationRecipientRepository;
        this.notificationRecipientMapper = notificationRecipientMapper;
    }

    /**
     * Save a notificationRecipient.
     *
     * @param notificationRecipientDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<NotificationRecipientDTO> save(NotificationRecipientDTO notificationRecipientDTO) {
        log.debug("Request to save NotificationRecipient : {}", notificationRecipientDTO);
        return notificationRecipientRepository
            .save(notificationRecipientMapper.toEntity(notificationRecipientDTO))
            .map(notificationRecipientMapper::toDto);
    }

    public Flux<NotificationRecipientDTO> getMyNotifications(Pageable pageable) {
        log.debug("Request to get all NotificationRecipients by recipientId");
        NotificationRecipientCriteria criteria = new NotificationRecipientCriteria();

        return SecurityUtils.getUserJWTDetail()
            .flatMapMany(user -> {
                criteria.recipientId().setEquals(user.getUserId());
                return notificationRecipientRepository
                    .findByCriteria(criteria, pageable)
                    .map(notificationRecipientMapper::toDto);
            });
    }

    public Mono<Long> countMyNotifications() {
        log.debug("Request to get the count of all NotificationRecipients by recipientId");
        NotificationRecipientCriteria criteria = new NotificationRecipientCriteria();
        return SecurityUtils.getUserJWTDetail()
            .flatMap(user -> {
                criteria.recipientId().setEquals(user.getUserId());
                return notificationRecipientRepository.countByCriteria(criteria);
            });
    }

    /**
     * Update a notificationRecipient.
     *
     * @param notificationRecipientDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<NotificationRecipientDTO> update(NotificationRecipientDTO notificationRecipientDTO) {
        log.debug("Request to update NotificationRecipient : {}", notificationRecipientDTO);
        return notificationRecipientRepository
            .save(notificationRecipientMapper.toEntity(notificationRecipientDTO).setIsPersisted())
            .map(notificationRecipientMapper::toDto);
    }

    /**
     * Partially update a notificationRecipient.
     *
     * @param notificationRecipientDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<NotificationRecipientDTO> partialUpdate(NotificationRecipientDTO notificationRecipientDTO) {
        log.debug("Request to partially update NotificationRecipient : {}", notificationRecipientDTO);

        return notificationRecipientRepository
            .findById(notificationRecipientDTO.getId())
            .map(existingNotificationRecipient -> {
                notificationRecipientMapper.partialUpdate(existingNotificationRecipient, notificationRecipientDTO);

                return existingNotificationRecipient;
            })
            .flatMap(notificationRecipientRepository::save)
            .map(notificationRecipientMapper::toDto);
    }

    /**
     * Find notificationRecipients by Criteria.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<NotificationRecipientDTO> findByCriteria(NotificationRecipientCriteria criteria, Pageable pageable) {
        log.debug("Request to get all NotificationRecipients by Criteria");
        return notificationRecipientRepository.findByCriteria(criteria, pageable)
            .map(notificationRecipientMapper::toDto);
    }

    /**
     * Find the count of notificationRecipients by criteria.
     *
     * @param criteria filtering criteria
     * @return the count of notificationRecipients
     */
    public Mono<Long> countByCriteria(NotificationRecipientCriteria criteria) {
        log.debug("Request to get the count of all NotificationRecipients by Criteria");
        return notificationRecipientRepository.countByCriteria(criteria);
    }

    /**
     * Returns the number of notificationRecipients available.
     *
     * @return the number of entities in the database.
     */
    public Mono<Long> countAll() {
        return notificationRecipientRepository.count();
    }

    /**
     * Get one notificationRecipient by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<NotificationRecipientDTO> findOne(UUID id) {
        log.debug("Request to get NotificationRecipient : {}", id);
        return notificationRecipientRepository.findById(id).map(notificationRecipientMapper::toDto);
    }

    /**
     * Delete the notificationRecipient by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(UUID id) {
        log.debug("Request to delete NotificationRecipient : {}", id);
        return notificationRecipientRepository.deleteById(id);
    }

    public Mono<Void> markAsRead(List<UUID> ids) {
        log.debug("Request to mark NotificationRecipient as read : {}", ids);
        return notificationRecipientRepository.markAsReadByIds(ids).then();
    }

    @Transactional(readOnly = true)
    public Flux<NotificationRecipientDTO> findMyUnread(Pageable pageable) {
        log.debug("Request to get all unread NotificationRecipients by notificationId");
        NotificationRecipientCriteria criteria = new NotificationRecipientCriteria();
        criteria.read().setEquals(false);
        return SecurityUtils.getUserJWTDetail()
            .flatMapMany(user -> {
                criteria.recipientId().setEquals(user.getUserId());
                return notificationRecipientRepository
                    .findByCriteria(criteria, pageable)
                    .map(notificationRecipientMapper::toDto);
            });
    }

    @Transactional(readOnly = true)
    public Mono<Long> countMyUnreadNotifications() {
        log.debug("Request to get the count of all unread NotificationRecipients by recipientId");
        NotificationRecipientCriteria criteria = new NotificationRecipientCriteria();
        return SecurityUtils.getUserJWTDetail()
            .flatMap(user -> {
                criteria.recipientId().setEquals(user.getUserId());
                criteria.read().setEquals(false);
                return notificationRecipientRepository.countByCriteria(criteria);
            });
    }
}
