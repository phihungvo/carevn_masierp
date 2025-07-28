package com.masi.utility.repository;

import com.masi.utility.domain.StandardWorkScheduleConfig;
import com.masi.utility.domain.criteria.StandardWorkScheduleConfigCriteria;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the StandardWorkScheduleConfig entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StandardWorkScheduleConfigRepository
    extends ReactiveCrudRepository<StandardWorkScheduleConfig, UUID>, StandardWorkScheduleConfigRepositoryInternal {
    Flux<StandardWorkScheduleConfig> findAllBy(Pageable pageable);

    @Override
    <S extends StandardWorkScheduleConfig> Mono<S> save(S entity);

    @Override
    Flux<StandardWorkScheduleConfig> findAll();

    @Override
    Mono<StandardWorkScheduleConfig> findById(UUID id);

    @Override
    Mono<Void> deleteById(UUID id);
}

interface StandardWorkScheduleConfigRepositoryInternal {
    <S extends StandardWorkScheduleConfig> Mono<S> save(S entity);

    Flux<StandardWorkScheduleConfig> findAllBy(Pageable pageable);

    Flux<StandardWorkScheduleConfig> findAll();

    Mono<StandardWorkScheduleConfig> findById(UUID id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<StandardWorkScheduleConfig> findAllBy(Pageable pageable, Criteria criteria);
    Flux<StandardWorkScheduleConfig> findByCriteria(StandardWorkScheduleConfigCriteria criteria, Pageable pageable);

    Mono<Long> countByCriteria(StandardWorkScheduleConfigCriteria criteria);
}
