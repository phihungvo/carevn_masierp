package com.masi.sale.repository;

import com.masi.sale.domain.OrderReview;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the OrderReview entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrderReviewRepository extends ReactiveCrudRepository<OrderReview, UUID>, OrderReviewRepositoryInternal {
    Flux<OrderReview> findAllBy(Pageable pageable);

    @Query("SELECT * FROM order_review entity WHERE entity.order_id = :id")
    Flux<OrderReview> findByOrder(UUID id);

    @Query("SELECT * FROM order_review entity WHERE entity.order_id IS NULL")
    Flux<OrderReview> findAllWhereOrderIsNull();

    @Override
    <S extends OrderReview> Mono<S> save(S entity);

    @Override
    Flux<OrderReview> findAll();


    @Query("DELETE FROM order_review entity WHERE entity.order_id = :id")
    Mono<Void> deleteByOrderId(UUID id);

    @Query("SELECT entity.awaiting_date FROM order_review entity WHERE entity.order_id = :orderId and entity.awaiting_date is not null and entity.approval_solution='WAITING' ORDER BY entity.awaiting_date DESC LIMIT 1")
    Mono<LocalDate> findLatestAwaitingDate(UUID orderId);

    @Override
    Mono<OrderReview> findById(UUID id);

    @Override
    Mono<Void> deleteById(UUID id);

    @Query("SELECT * FROM order_review WHERE order_id IN (:orderIds)")
    Flux<OrderReview> findOrderReviewByOrderIds(List<UUID> orderIds);
}

interface OrderReviewRepositoryInternal {
    <S extends OrderReview> Mono<S> save(S entity);

    Flux<OrderReview> findAllBy(Pageable pageable);

    Flux<OrderReview> findAll();

    Mono<OrderReview> findById(UUID id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<OrderReview> findAllBy(Pageable pageable, Criteria criteria);
}
