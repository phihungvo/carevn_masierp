package com.masi.logistics.repository;

import com.masi.logistics.domain.SupplierDetail;
import com.masi.logistics.domain.criteria.SupplierDetailCriteria;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the SupplierDetail entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SupplierDetailRepository extends ReactiveCrudRepository<SupplierDetail, UUID>, SupplierDetailRepositoryInternal {
    Flux<SupplierDetail> findAllBy(Pageable pageable);

    @Override
    <S extends SupplierDetail> Mono<S> save(S entity);

    @Override
    Flux<SupplierDetail> findAll();

    @Override
    Mono<SupplierDetail> findById(UUID id);

    @Override
    Mono<Void> deleteById(UUID id);

    @Query("UPDATE supplier_detail SET delete_at = NOW(), delete_by = :deleteBy WHERE id = :id AND company = :company AND delete_at IS NULL AND delete_by IS NULL")
    Mono<Void> deleteByIdAndCompany(UUID id, String company, String deleteBy);

    // deleted by supplierId and company
    @Query("UPDATE supplier_detail SET delete_at = NOW(), delete_by = :deleteBy WHERE supplier_id = :supplierId AND company = :company AND delete_at IS NULL AND delete_by IS NULL")
    Mono<Void> deleteBySupplierIdAndCompany(UUID supplierId, String company, String deleteBy);
}

interface SupplierDetailRepositoryInternal {
    <S extends SupplierDetail> Mono<S> save(S entity);

    Flux<SupplierDetail> findAllBy(Pageable pageable);

    Flux<SupplierDetail> findAll();

    Mono<SupplierDetail> findById(UUID id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<SupplierDetail> findAllBy(Pageable pageable, Criteria criteria);
    Flux<SupplierDetail> findByCriteria(SupplierDetailCriteria criteria, Pageable pageable);

    Mono<Long> countByCriteria(SupplierDetailCriteria criteria);

    Flux<SupplierDetail> findAllBySupplierIdAndCompany(UUID supplierId, String company);

    Mono<Long> countBySupplierIdAndCompany(UUID supplierId, String company);
}
