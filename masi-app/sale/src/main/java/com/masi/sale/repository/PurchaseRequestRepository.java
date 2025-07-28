package com.masi.sale.repository;

import com.masi.sale.domain.PurchaseRequest;
import java.util.UUID;

import com.masi.sale.service.dto.PurchaseRequestQueryDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.Fuseable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the PurchaseRequest entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PurchaseRequestRepository extends ReactiveCrudRepository<PurchaseRequest, UUID>, PurchaseRequestRepositoryInternal {
    Flux<PurchaseRequest> findAllBy(Pageable pageable);

    @Override
    <S extends PurchaseRequest> Mono<S> save(S entity);

    @Override
    Flux<PurchaseRequest> findAll();

    @Override
    Mono<PurchaseRequest> findById(UUID id);

    @Override
    @Query("UPDATE purchase_request set is_deleted = true where id = :id")
    Mono<Void> deleteById(UUID id);

    Mono<PurchaseRequest> getFirstByIdAndIsDeletedFalse(UUID id);
}

interface PurchaseRequestRepositoryInternal {
    <S extends PurchaseRequest> Mono<S> save(S entity);

    Flux<PurchaseRequest> findAllBy(Pageable pageable);

    Flux<PurchaseRequest> findAll();

    Mono<PurchaseRequest> findById(UUID id);

    Flux<PurchaseRequest> findAllByQuery(PurchaseRequestQueryDTO queryDTO, Pageable pageable);
    Mono<Long> countAllByQuery(PurchaseRequestQueryDTO queryDTO);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<PurchaseRequest> findAllBy(Pageable pageable, Criteria criteria);
}
