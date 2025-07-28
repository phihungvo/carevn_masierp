package com.masi.sale.repository;

import com.masi.sale.domain.Quotation;
import com.masi.sale.service.dto.QuotationGetListDTO;

import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the Quotation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface QuotationRepository extends ReactiveCrudRepository<Quotation, UUID>, QuotationRepositoryInternal {
    Flux<Quotation> findAllBy(Pageable pageable);

    @Override
    <S extends Quotation> Mono<S> save(S entity);

    @Override
    Flux<Quotation> findAll();

    @Override
    Mono<Quotation> findById(UUID id);

    @Query("SELECT * FROM quotation WHERE id = :id AND company = :company AND is_deleted = false AND deleted_by IS NULL AND deleted_date IS NULL")
    Mono<Quotation> findById(UUID id, String company);

    @Override
    Mono<Void> deleteById(UUID id);

    @Modifying
    @Query("UPDATE quotation SET is_deleted = true, deleted_by = :deletedBy, deleted_date = NOW() WHERE id = :id AND company = :company AND is_deleted = false AND deleted_by IS NULL AND deleted_date IS NULL")
    Mono<Void> removeById(UUID id, String company, String deletedBy);
}

interface QuotationRepositoryInternal {
    <S extends Quotation> Mono<S> save(S entity);

    Flux<Quotation> findAllBy(Pageable pageable);

    Flux<Quotation> findAll();

    Mono<Quotation> findById(UUID id);

    Flux<Quotation> findAllByQuery(Pageable pageable, QuotationGetListDTO dto);

    Mono<Long> countByQuery(QuotationGetListDTO dto);
    // this is not supported at the moment because of
    // https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Quotation> findAllBy(Pageable pageable, Criteria criteria);
}
