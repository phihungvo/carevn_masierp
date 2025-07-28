package com.masi.production.repository;

import com.masi.production.domain.ProductionStandard;

import java.math.BigDecimal;
import java.util.UUID;

import com.masi.production.service.dto.ProductionStandardRO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the ProductionStandard entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductionStandardRepository
    extends ReactiveCrudRepository<ProductionStandard, UUID>, ProductionStandardRepositoryInternal {
    @Override
    <S extends ProductionStandard> Mono<S> save(S entity);

    Flux<ProductionStandard> findAllByIsDeleted(Boolean isDeleted, Pageable pageable);
    Mono<Long> countAllByIsDeleted(Boolean isDeleted);

    Mono<ProductionStandard> findByIdAndIsDeleted(UUID id, Boolean isDeleted);

    @Override
    Mono<Void> deleteById(UUID id);

    @Query("UPDATE production_standard SET is_deleted = true, last_updated_at= NOW() WHERE id = :id")
    Mono<Void> softDeleteById(UUID id);

    @Query("SELECT COUNT(*) FROM production_standard entity WHERE entity.is_deleted = false AND code = :code AND company = :company AND status = 'NEW'")
    Mono<Long> findFirstByCodeAndIsDeletedAndCompany(String code, String company);

    @Query("""
        select sum(quantity) from production_standard ps
        where id in (select production_standard_id from manufacture_order mo)
    """)
    Mono<BigDecimal> countTotalQuantityInMo();
}

interface ProductionStandardRepositoryInternal {
    <S extends ProductionStandard> Mono<S> save(S entity);

    Flux<ProductionStandard> findAllBy(Pageable pageable);

    Flux<ProductionStandard> findAll();

    Mono<ProductionStandard> findById(UUID id);

    Mono<Long> countAllByFilter(ProductionStandardRO ro);

    Flux<ProductionStandard> findAllByFilter(ProductionStandardRO ro, Pageable pageable);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<ProductionStandard> findAllBy(Pageable pageable, Criteria criteria);
}
