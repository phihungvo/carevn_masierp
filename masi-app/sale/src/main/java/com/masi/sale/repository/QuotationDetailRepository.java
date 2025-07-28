package com.masi.sale.repository;

import com.masi.sale.domain.QuotationDetail;
import com.masi.sale.service.dto.QuotationDetailGetListDTO;

import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the QuotationDetail entity.
 */
@SuppressWarnings("unused")
@Repository
public interface QuotationDetailRepository
        extends ReactiveCrudRepository<QuotationDetail, UUID>, QuotationDetailRepositoryInternal {
    Flux<QuotationDetail> findAllBy(Pageable pageable);

    @Query("SELECT * FROM quotation_detail entity WHERE entity.quotation_id = :id AND entity.company = :company AND entity.is_deleted = false AND entity.deleted_by IS NULL AND entity.deleted_date IS NULL")
    Flux<QuotationDetail> findByQuotation(UUID id, String company);

    @Query("SELECT * FROM quotation_detail entity WHERE entity.id = :id AND entity.company = :company AND entity.is_deleted = false AND entity.deleted_by IS NULL AND entity.deleted_date IS NULL")
    Mono<QuotationDetail> findOneById(UUID id, String company);

    @Query("SELECT * FROM quotation_detail entity WHERE entity.quotation_id IS NULL")
    Flux<QuotationDetail> findAllWhereQuotationIsNull();

    @Override
    <S extends QuotationDetail> Mono<S> save(S entity);

    @Override
    Flux<QuotationDetail> findAll();

    @Override
    Mono<QuotationDetail> findById(UUID id);

    @Override
    Mono<Void> deleteById(UUID id);

    @Modifying
    @Query("UPDATE quotation_detail SET is_deleted = true, deleted_by = :deletedBy, deleted_date = NOW() WHERE quotation_id = :quotationId AND company = :company AND is_deleted = false AND deleted_by IS NULL AND deleted_date IS NULL")
    Mono<Void> removeByQuotation(UUID quotationId, String company, String deletedBy);

    @Modifying
    @Query("UPDATE quotation_detail SET is_deleted = true, deleted_by = :deletedBy, deleted_date = NOW() WHERE id = :id AND company = :company AND is_deleted = false AND deleted_by IS NULL AND deleted_date IS NULL")
    Mono<Void> removeById(UUID id, String company, String deletedBy);
}

interface QuotationDetailRepositoryInternal {
    <S extends QuotationDetail> Mono<S> save(S entity);

    Flux<QuotationDetail> findAllBy(Pageable pageable);

    Flux<QuotationDetail> findAll();

    Mono<QuotationDetail> findById(UUID id);

    Flux<QuotationDetail> findAllByQuery(Pageable pageable, QuotationDetailGetListDTO dto);

    Mono<Long> countByQuery(QuotationDetailGetListDTO dto);
    // this is not supported at the moment because of
    // https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<QuotationDetail> findAllBy(Pageable pageable, Criteria criteria);
}
