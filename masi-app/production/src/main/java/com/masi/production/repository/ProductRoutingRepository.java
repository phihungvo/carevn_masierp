package com.masi.production.repository;

import com.masi.production.domain.ProductRouting;

import java.time.ZonedDateTime;
import java.util.UUID;

import com.masi.production.domain.QualityCheckSample;
import com.masi.production.service.dto.ProductRoutingQuery;
import com.masi.production.service.dto.QuanlityCheckSampleRO;
import org.reactivestreams.Publisher;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactivefeign.client.ReactiveHttpExchangeFilterFunction;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the ProductRouting entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductRoutingRepository extends ReactiveCrudRepository<ProductRouting, UUID>, ProductRoutingRepositoryInternal {
    Flux<ProductRouting> findAllBy(Pageable pageable);

    @Query("SELECT * FROM product_routing entity WHERE entity.factory_id = :id")
    Flux<ProductRouting> findByFactory(UUID id);

    @Query("SELECT * FROM product_routing entity WHERE entity.factory_id IS NULL")
    Flux<ProductRouting> findAllWhereFactoryIsNull();

    @Query("SELECT * FROM product_routing entity WHERE entity.storage_id = :id")
    Flux<ProductRouting> findByStorage(UUID id);

    @Query("SELECT * FROM product_routing entity WHERE entity.storage_id IS NULL")
    Flux<ProductRouting> findAllWhereStorageIsNull();

    @Override
    <S extends ProductRouting> Mono<S> save(S entity);

    @Override
    Flux<ProductRouting> findAll();

    Mono<ProductRouting> findById(UUID id);

    @Override
    Mono<Void> deleteById(UUID id);

    @Query("UPDATE product_routing SET is_active = false WHERE id = :id")
    Mono<Void> softDeleteById(@Param("id") UUID id);

    Mono<ProductRouting> findByIdAndIsActiveIsTrue(UUID id);

    @Query("UPDATE product_routing SET is_active = :b WHERE product_maintain_id = :id")
    Mono<Void> changeIsDeletedByProductMaintainId(UUID id, boolean b);
}

interface ProductRoutingRepositoryInternal {
    <S extends ProductRouting> Mono<S> save(S entity);

    Flux<ProductRouting> findAllBy(Pageable pageable);

    Flux<ProductRouting> findAll();

    Mono<Long> countByFilter(ProductRoutingQuery ro);

    Mono<ProductRouting> findById(UUID id, String company);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<ProductRouting> findAllBy(Pageable pageable, Criteria criteria);
//    Mono<ProductRouting> findOne(UUID id);
    Flux<ProductRouting> findAllByFilter( ProductRoutingQuery query,Pageable pageable);
}
