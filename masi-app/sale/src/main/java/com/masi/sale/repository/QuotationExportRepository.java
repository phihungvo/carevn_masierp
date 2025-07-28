package com.masi.sale.repository;

import com.masi.sale.domain.QuotationExport;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the QuotationExport entity.
 */
@SuppressWarnings("unused")
@Repository
public interface QuotationExportRepository extends ReactiveCrudRepository<QuotationExport, UUID>, QuotationExportRepositoryInternal {
    Flux<QuotationExport> findAllBy(Pageable pageable);

    @Query("SELECT * FROM quotation_export entity WHERE entity.quotation_id = :id")
    Flux<QuotationExport> findByQuotation(UUID id);

    @Query("SELECT * FROM quotation_export entity WHERE entity.quotation_id IS NULL")
    Flux<QuotationExport> findAllWhereQuotationIsNull();

    @Override
    <S extends QuotationExport> Mono<S> save(S entity);

    @Override
    Flux<QuotationExport> findAll();

    @Override
    Mono<QuotationExport> findById(UUID id);

    @Override
    Mono<Void> deleteById(UUID id);
}

interface QuotationExportRepositoryInternal {
    <S extends QuotationExport> Mono<S> save(S entity);

    Flux<QuotationExport> findAllBy(Pageable pageable);

    Flux<QuotationExport> findAll();

    Mono<QuotationExport> findById(UUID id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<QuotationExport> findAllBy(Pageable pageable, Criteria criteria);
}
