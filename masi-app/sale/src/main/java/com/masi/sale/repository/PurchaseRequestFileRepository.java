package com.masi.sale.repository;

import com.masi.sale.domain.PurchaseRequestFile;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the PurchaseRequestFile entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PurchaseRequestFileRepository
    extends ReactiveCrudRepository<PurchaseRequestFile, UUID>, PurchaseRequestFileRepositoryInternal {
    Flux<PurchaseRequestFile> findAllBy(Pageable pageable);

    @Query("SELECT * FROM purchase_request_file entity WHERE entity.purchase_request_id = :id")
    Flux<PurchaseRequestFile> findByPurchaseRequest(UUID id);

    @Query("SELECT * FROM purchase_request_file entity WHERE entity.purchase_request_id IS NULL")
    Flux<PurchaseRequestFile> findAllWherePurchaseRequestIsNull();

    Flux<PurchaseRequestFile> findAllByPurchaseRequestId(UUID id);

    @Override
    <S extends PurchaseRequestFile> Mono<S> save(S entity);

    @Override
    Flux<PurchaseRequestFile> findAll();

    @Override
    Mono<PurchaseRequestFile> findById(UUID id);

    @Override
    Mono<Void> deleteById(UUID id);
    @Modifying
    @Query("DELETE FROM purchase_request_file WHERE purchase_request_id = :id")
    Mono<Void> deleteByPurchaseRequestId(UUID id);
}

interface PurchaseRequestFileRepositoryInternal {
    <S extends PurchaseRequestFile> Mono<S> save(S entity);

    Flux<PurchaseRequestFile> findAllBy(Pageable pageable);

    Flux<PurchaseRequestFile> findAll();

    Mono<PurchaseRequestFile> findById(UUID id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<PurchaseRequestFile> findAllBy(Pageable pageable, Criteria criteria);
}
