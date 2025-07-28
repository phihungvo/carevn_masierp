package com.masi.utility.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.masi.utility.domain.Notification;
import com.masi.utility.domain.NotificationRecipient;
import com.masi.utility.domain.criteria.NotificationCriteria;
import com.masi.utility.repository.NotificationRecipientRepository;
import com.masi.utility.repository.NotificationRepository;
import com.masi.utility.service.dto.NotificationAddedEvent;
import com.masi.utility.service.dto.NotificationDTO;
import com.masi.utility.service.mapper.NotificationMapper;
import com.masi.utility.web.rest.MasiUtilityKafkaResource;
import com.masi.utility.web.rest.MasiUtilityKafkaResource.Payload;

import io.r2dbc.postgresql.codec.Json;

import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing
 * {@link com.masi.utility.domain.Notification}.
 */
@Service
public class NotificationService {

    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);
    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;
    private final NotificationRecipientRepository notificationRecipientRepository;
    private final ObjectMapper objectMapper;

    public NotificationService(NotificationRepository notificationRepository, NotificationMapper notificationMapper,
                               NotificationRecipientRepository notificationRecipientRepository, ObjectMapper objectMapper) {
        this.notificationRepository = notificationRepository;
        this.notificationMapper = notificationMapper;
        this.notificationRecipientRepository = notificationRecipientRepository;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(id = "masi-utility", topics = "notification-added", containerFactory = "kafkaListenerContainerFactory")
    public void listen(ConsumerRecord<String, Map> data) {
        log.info("Received notification-added event: {}", data);
        final NotificationAddedEvent payload = objectMapper.convertValue(data.value(), NotificationAddedEvent.class);
        ObjectWriter ow = objectMapper.writer().withDefaultPrettyPrinter();
        String jsonStr = "{}";
        try {
            jsonStr = ow.writeValueAsString(payload.getData());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        Json json = Json.of(jsonStr);
        Notification notification = Notification.builder()
            .id(payload.getId())
            .content(payload.getContent())
            .title(payload.getTitle())
            .entityName(payload.getEntityName())
            .entityId(payload.getEntityId())
            .data(json)
            .sentBy(payload.getSentBy())
            .category(payload.getCategory())
            .entityType(payload.getEntityType())
            .createdAt(ZonedDateTime.now())
            .createdBy(payload.getCreatedBy())
            .build();
        notificationRepository.save(notification).flatMap(
            notification1 -> {
                var uniqueRecipients = new HashSet<String>(payload.getRecipients());
                Collection<NotificationRecipient> recipients = uniqueRecipients.stream()
                    .map(recipient -> NotificationRecipient.builder()
                        .notificationId(notification1.getId())
                        .recipientId(UUID.fromString(recipient))
                        .id(UUID.randomUUID())
                        .read(false)
                        .build())
                    .toList();
                return notificationRecipientRepository.saveAll(recipients).then(Mono.just(notification1));
            }).subscribe();
    }

    /**
     * Save a notification.
     *
     * @param notificationDTO the entity to save.
     * @return the persisted entity.
     */
    @Transactional
    public Mono<NotificationDTO> save(NotificationDTO notificationDTO) {
        log.debug("Request to save Notification : {}", notificationDTO);
        return notificationRepository.save(notificationMapper.toEntity(notificationDTO)).map(notificationMapper::toDto);
    }

    /**
     * Update a notification.
     *
     * @param notificationDTO the entity to save.
     * @return the persisted entity.
     */
    @Transactional
    public Mono<NotificationDTO> update(NotificationDTO notificationDTO) {
        log.debug("Request to update Notification : {}", notificationDTO);
        return notificationRepository.save(notificationMapper.toEntity(notificationDTO).setIsPersisted())
            .map(notificationMapper::toDto);
    }

    /**
     * Partially update a notification.
     *
     * @param notificationDTO the entity to update partially.
     * @return the persisted entity.
     */
    @Transactional
    public Mono<NotificationDTO> partialUpdate(NotificationDTO notificationDTO) {
        log.debug("Request to partially update Notification : {}", notificationDTO);

        return notificationRepository
            .findById(notificationDTO.getId())
            .map(existingNotification -> {
                notificationMapper.partialUpdate(existingNotification, notificationDTO);

                return existingNotification;
            })
            .flatMap(notificationRepository::save)
            .map(notificationMapper::toDto);
    }

    /**
     * Find notifications by Criteria.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<NotificationDTO> findByCriteria(NotificationCriteria criteria, Pageable pageable) {
        log.debug("Request to get all Notifications by Criteria");
        return notificationRepository.findByCriteria(criteria, pageable).map(notificationMapper::toDto);
    }

    /**
     * Find the count of notifications by criteria.
     *
     * @param criteria filtering criteria
     * @return the count of notifications
     */
    public Mono<Long> countByCriteria(NotificationCriteria criteria) {
        log.debug("Request to get the count of all Notifications by Criteria");
        return notificationRepository.countByCriteria(criteria);
    }

    /**
     * Returns the number of notifications available.
     *
     * @return the number of entities in the database.
     */
    public Mono<Long> countAll() {
        return notificationRepository.count();
    }

    /**
     * Get one notification by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<NotificationDTO> findOne(UUID id) {
        log.debug("Request to get Notification : {}", id);
        return notificationRepository.findById(id).map(notificationMapper::toDto);
    }

    /**
     * Delete the notification by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(UUID id) {
        log.debug("Request to delete Notification : {}", id);
        return notificationRepository.deleteById(id);
    }
}
