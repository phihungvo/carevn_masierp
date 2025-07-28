package com.masi.utility.repository;

import com.masi.utility.domain.HolidayConfig;
import com.masi.utility.domain.criteria.HolidayConfigCriteria;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the HolidayConfig entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HolidayConfigRepository extends ReactiveCrudRepository<HolidayConfig, UUID>, HolidayConfigRepositoryInternal {
    Flux<HolidayConfig> findAllBy(Pageable pageable);

    @Override
    <S extends HolidayConfig> Mono<S> save(S entity);

    @Override
    Flux<HolidayConfig> findAll();

    @Override
    Mono<HolidayConfig> findById(UUID id);

    @Override
    Mono<Void> deleteById(UUID id);
}

interface HolidayConfigRepositoryInternal {
    <S extends HolidayConfig> Mono<S> save(S entity);

    Flux<HolidayConfig> findAllBy(Pageable pageable);

    Flux<HolidayConfig> findAll();

    Mono<HolidayConfig> findById(UUID id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<HolidayConfig> findAllBy(Pageable pageable, Criteria criteria);
    Flux<HolidayConfig> findByCriteria(HolidayConfigCriteria criteria, Pageable pageable);

    Mono<Long> countByCriteria(HolidayConfigCriteria criteria);
}
