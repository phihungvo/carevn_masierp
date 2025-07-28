package com.masi.sale.repository;

import com.masi.sale.domain.PurchaseReview;

import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the PurchaseReview entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PurchaseReviewRepository extends ReactiveCrudRepository<PurchaseReview, UUID>, PurchaseReviewRepositoryInternal {
    Flux<PurchaseReview> findAllBy(Pageable pageable);

    @Query("SELECT * FROM purchase_review entity WHERE entity.purchase_request_id = :id")
    Flux<PurchaseReview> findByPurchaseRequest(UUID id);

    @Query("SELECT * FROM purchase_review entity WHERE entity.purchase_request_id IS NULL")
    Flux<PurchaseReview> findAllWherePurchaseRequestIsNull();

    @Query("DELETE FROM purchase_review entity WHERE entity.purchase_request_id = :id")
    Mono<Void> deleteByPurchaseRequest(UUID id);

    @Override
    <S extends PurchaseReview> Mono<S> save(S entity);

    @Override
    Flux<PurchaseReview> findAll();

    @Override
    Mono<PurchaseReview> findById(UUID id);

    @Override
    Mono<Void> deleteById(UUID id);
}

interface PurchaseReviewRepositoryInternal {
    <S extends PurchaseReview> Mono<S> save(S entity);

    Flux<PurchaseReview> findAllBy(Pageable pageable);

    Flux<PurchaseReview> findAll();

    Mono<PurchaseReview> findById(UUID id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<PurchaseReview> findAllBy(Pageable pageable, Criteria criteria);
}
