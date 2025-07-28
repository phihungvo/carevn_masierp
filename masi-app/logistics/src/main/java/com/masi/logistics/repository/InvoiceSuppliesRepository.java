package com.masi.logistics.repository;

import com.masi.logistics.domain.InvoiceSupplies;
import com.masi.logistics.domain.criteria.InvoiceSuppliesCriteria;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the InvoiceSupplies entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InvoiceSuppliesRepository extends ReactiveCrudRepository<InvoiceSupplies, UUID>, InvoiceSuppliesRepositoryInternal {
    Flux<InvoiceSupplies> findAllBy(Pageable pageable);

    @Override
    <S extends InvoiceSupplies> Mono<S> save(S entity);

    @Override
    Flux<InvoiceSupplies> findAll();

    @Override
    Mono<InvoiceSupplies> findById(UUID id);

    @Override
    Mono<Void> deleteById(UUID id);

    @Modifying
    @Query("UPDATE invoice_supplies SET is_deleted = true, deleted_at = NOW(), deleted_by = :deletedBy WHERE invoice_id = :invoiceId AND is_deleted = false AND company = :company")
    Mono<Void> deleteByInvoiceId(UUID invoiceId, String company, UUID deletedBy);

}

interface InvoiceSuppliesRepositoryInternal {
    <S extends InvoiceSupplies> Mono<S> save(S entity);

    Flux<InvoiceSupplies> findAllBy(Pageable pageable);

    Flux<InvoiceSupplies> findAll();

    Mono<InvoiceSupplies> findById(UUID id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<InvoiceSupplies> findAllBy(Pageable pageable, Criteria criteria);
    Flux<InvoiceSupplies> findByCriteria(InvoiceSuppliesCriteria criteria, Pageable pageable);

    Mono<Long> countByCriteria(InvoiceSuppliesCriteria criteria);
}
