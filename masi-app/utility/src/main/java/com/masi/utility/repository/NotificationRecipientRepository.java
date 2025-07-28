package com.masi.utility.repository;

import com.masi.utility.domain.NotificationRecipient;
import com.masi.utility.domain.criteria.NotificationRecipientCriteria;

import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the NotificationRecipient entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NotificationRecipientRepository
    extends ReactiveCrudRepository<NotificationRecipient, UUID>, NotificationRecipientRepositoryInternal {
    Flux<NotificationRecipient> findAllBy(Pageable pageable);

    @Query("SELECT * FROM notification_recipient entity WHERE entity.notification_id = :id")
    Flux<NotificationRecipient> findByNotification(UUID id);

    @Query("SELECT * FROM notification_recipient entity WHERE entity.notification_id IS NULL")
    Flux<NotificationRecipient> findAllWhereNotificationIsNull();

    @Override
    <S extends NotificationRecipient> Mono<S> save(S entity);

    @Override
    Flux<NotificationRecipient> findAll();

    @Override
    Mono<NotificationRecipient> findById(UUID id);

    @Override
    Mono<Void> deleteById(UUID id);

    @Modifying
    @Query("UPDATE notification_recipient SET read = true , read_at=now() WHERE id IN (:ids)")
    Mono<Integer> markAsReadByIds(List<UUID> ids);
}

interface NotificationRecipientRepositoryInternal {
    <S extends NotificationRecipient> Mono<S> save(S entity);

    Flux<NotificationRecipient> findAllBy(Pageable pageable);

    Flux<NotificationRecipient> findAll();

    Mono<NotificationRecipient> findById(UUID id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<NotificationRecipient> findAllBy(Pageable pageable, Criteria criteria);
    Flux<NotificationRecipient> findByCriteria(NotificationRecipientCriteria criteria, Pageable pageable);

    Mono<Long> countByCriteria(NotificationRecipientCriteria criteria);


}
