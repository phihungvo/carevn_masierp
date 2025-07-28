package com.masi.utility.repository;

import com.masi.utility.domain.Notification;
import com.masi.utility.domain.criteria.NotificationCriteria;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the Notification entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NotificationRepository extends ReactiveCrudRepository<Notification, UUID>, NotificationRepositoryInternal {
    Flux<Notification> findAllBy(Pageable pageable);

    @Override
    <S extends Notification> Mono<S> save(S entity);

    @Override
    Flux<Notification> findAll();

    @Override
    Mono<Notification> findById(UUID id);

    @Override
    Mono<Void> deleteById(UUID id);
}

interface NotificationRepositoryInternal {
    <S extends Notification> Mono<S> save(S entity);

    Flux<Notification> findAllBy(Pageable pageable);

    Flux<Notification> findAll();

    Mono<Notification> findById(UUID id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Notification> findAllBy(Pageable pageable, Criteria criteria);
    Flux<Notification> findByCriteria(NotificationCriteria criteria, Pageable pageable);

    Mono<Long> countByCriteria(NotificationCriteria criteria);
}
