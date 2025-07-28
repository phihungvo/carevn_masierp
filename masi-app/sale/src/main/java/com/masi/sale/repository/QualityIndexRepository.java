package com.masi.sale.repository;

import com.masi.sale.domain.QualityIndex;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the QualityIndex entity.
 */
@SuppressWarnings("unused")
@Repository
public interface QualityIndexRepository
        extends ReactiveCrudRepository<QualityIndex, UUID>, QualityIndexRepositoryInternal {
    Flux<QualityIndex> findAllBy(Pageable pageable);

    @Query("SELECT * FROM quality_index entity WHERE entity.order_id = :orderId")
    Flux<QualityIndex> findAllByOrderId(UUID orderId);

    @Override
    <S extends QualityIndex> Mono<S> save(S entity);

    @Override
    Flux<QualityIndex> findAll();

    @Override
    Mono<QualityIndex> findById(UUID id);

    @Override
    Mono<Void> deleteById(UUID id);

    @Modifying
    @Query("DELETE FROM quality_index WHERE order_id = :orderId")
    Mono<Void> deleteByOrderId(UUID orderId);

    @Query("SELECT distinct entity.name FROM quality_index entity")
    Flux<String> findAllByDistinctName();
}

interface QualityIndexRepositoryInternal {
    <S extends QualityIndex> Mono<S> save(S entity);

    Flux<QualityIndex> findAllBy(Pageable pageable);

    Flux<QualityIndex> findAll();

    Mono<QualityIndex> findById(UUID id);
    // this is not supported at the moment because of
    // https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<QualityIndex> findAllBy(Pageable pageable, Criteria criteria);
}
