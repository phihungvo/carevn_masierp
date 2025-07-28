package com.masi.logistics.repository;

import com.masi.logistics.domain.PaymentDetail;
import com.masi.logistics.domain.criteria.PaymentDetailCriteria;

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
 * Spring Data R2DBC repository for the PaymentDetail entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PaymentDetailRepository extends ReactiveCrudRepository<PaymentDetail, UUID>, PaymentDetailRepositoryInternal {
    Flux<PaymentDetail> findAllBy(Pageable pageable);

    @Query("SELECT * FROM payment_detail entity WHERE entity.payment_request_id = :id")
    Flux<PaymentDetail> findByPaymentRequest(UUID id);

    @Query("SELECT * FROM payment_detail entity WHERE entity.payment_request_id IS NULL")
    Flux<PaymentDetail> findAllWherePaymentRequestIsNull();

    @Override
    <S extends PaymentDetail> Mono<S> save(S entity);

    @Override
    Flux<PaymentDetail> findAll();

    @Override
    Mono<PaymentDetail> findById(UUID id);

    @Override
    Mono<Void> deleteById(UUID id);

    Mono<Long> countByPaymentRequestId(UUID id);

    @Modifying
    @Query("UPDATE payment_detail SET deleted_at = NOW(), deleted_by = :deletedBy WHERE payment_request_id IN (:id) AND company = :company AND deleted_at IS NULL AND deleted_by IS NULL")
    Mono<Void> deleteByPaymentRequestId(List<UUID> id, String deletedBy, String company);

    @Modifying
    @Query("UPDATE payment_detail SET deleted_at = NOW(), deleted_by = :deletedBy WHERE id IN (:id) AND company = :company AND deleted_at IS NULL AND deleted_by IS NULL")
    Mono<Void> deleteBytId(List<UUID> id, String deletedBy, String company);
}

interface PaymentDetailRepositoryInternal {
    <S extends PaymentDetail> Mono<S> save(S entity);

    Flux<PaymentDetail> findAllBy(Pageable pageable);

    Flux<PaymentDetail> findAll();

    Mono<PaymentDetail> findById(UUID id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<PaymentDetail> findAllBy(Pageable pageable, Criteria criteria);
    Flux<PaymentDetail> findByCriteria(PaymentDetailCriteria criteria, Pageable pageable);

    Flux<PaymentDetail> findByPaymentRequestId(UUID id,Pageable pageable);

    Mono<Long> countByCriteria(PaymentDetailCriteria criteria);

    Mono<Long> countByRequestId(String requestId);
}
