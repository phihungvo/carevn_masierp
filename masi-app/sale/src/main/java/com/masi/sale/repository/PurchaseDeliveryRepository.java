package com.masi.sale.repository;

import com.masi.sale.domain.PurchaseDelivery;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the PurchaseDelivery entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PurchaseDeliveryRepository extends ReactiveCrudRepository<PurchaseDelivery, UUID>, PurchaseDeliveryRepositoryInternal {
    Flux<PurchaseDelivery> findAllBy(Pageable pageable);

    @Query("SELECT * FROM purchase_delivery entity WHERE entity.purchase_request_id = :id order by entity.created_at desc")
    Flux<PurchaseDelivery> findByPurchaseRequest(UUID id);

    Mono<PurchaseDelivery> findFirstByPurchaseRequestId(UUID id);

    @Query("SELECT * FROM purchase_delivery entity WHERE entity.purchase_request_id IS NULL")
    Flux<PurchaseDelivery> findAllWherePurchaseRequestIsNull();

    @Override
    <S extends PurchaseDelivery> Mono<S> save(S entity);

    @Override
    Flux<PurchaseDelivery> findAll();

    @Override
    Mono<PurchaseDelivery> findById(UUID id);

    @Override
    Mono<Void> deleteById(UUID id);
}

interface PurchaseDeliveryRepositoryInternal {
    <S extends PurchaseDelivery> Mono<S> save(S entity);

    Flux<PurchaseDelivery> findAllBy(Pageable pageable);

    Flux<PurchaseDelivery> findAll();

    Mono<PurchaseDelivery> findById(UUID id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<PurchaseDelivery> findAllBy(Pageable pageable, Criteria criteria);
}
