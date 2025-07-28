package com.masi.logistics.repository;

import com.masi.logistics.domain.RelatedCosts;
import com.masi.logistics.domain.criteria.RelatedCostsCriteria;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the RelatedCosts entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RelatedCostsRepository extends ReactiveCrudRepository<RelatedCosts, UUID>, RelatedCostsRepositoryInternal {
    Flux<RelatedCosts> findAllBy(Pageable pageable);

    @Override
    <S extends RelatedCosts> Mono<S> save(S entity);

    @Override
    Flux<RelatedCosts> findAll();

    @Override
    Mono<RelatedCosts> findById(UUID id);

    @Override
    Mono<Void> deleteById(UUID id);

    @Modifying
    @Query("UPDATE related_costs SET is_deleted = true, deleted_by = :deletedBy, deleted_at = NOW() WHERE invoice_id = :invoiceId AND company = :company AND is_deleted = false")
    Mono<Void> removeAllByInvoiceIdAndCompanyAndIsDeletedIsFalse(UUID invoiceId, String company, String deletedBy);
}

interface RelatedCostsRepositoryInternal {
    <S extends RelatedCosts> Mono<S> save(S entity);

    Flux<RelatedCosts> findAllBy(Pageable pageable);

    Flux<RelatedCosts> findAll();

    Mono<RelatedCosts> findById(UUID id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<RelatedCosts> findAllBy(Pageable pageable, Criteria criteria);
    Flux<RelatedCosts> findByCriteria(RelatedCostsCriteria criteria, Pageable pageable);

    Mono<Long> countByCriteria(RelatedCostsCriteria criteria);
}
