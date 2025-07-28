package com.masi.sale.repository;

import java.util.UUID;

import com.masi.sale.domain.Material;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the Material entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MaterialRepository extends ReactiveCrudRepository<Material, UUID>, MaterialRepositoryInternal {
    Flux<Material> findAllBy(Pageable pageable);

    @Query("SELECT * FROM product entity WHERE entity.contract_product_id = :id")
    Flux<Material> findByContractProduct(UUID id);

    @Query("SELECT * FROM product entity WHERE entity.contract_product_id IS NULL")
    Flux<Material> findAllWhereContractProductIsNull();

    @Override
    <S extends Material> Mono<S> save(S entity);

    @Override
    Flux<Material> findAll();

    @Override
    Mono<Material> findById(UUID id);

    @Override
    Mono<Void> deleteById(UUID id);

    @Query("SELECT COUNT(c) FROM material c WHERE c.is_deleted = false AND c.company = :company")
    Mono<Long> countProductsByFilter(String company);

    @Query("SELECT * FROM material c WHERE c.is_deleted = false AND c.company = :company AND c.id = :id")
    Mono<Material> findNotDeletedById(UUID id, String company);

    @Query("SELECT * FROM material C WHERE C.is_deleted = false AND C.company = :company")
    Flux<Material> findAllProductsByFilter(String company);
}

interface MaterialRepositoryInternal {
    <S extends Material> Mono<S> save(S entity);

    Flux<Material> findAllBy(Pageable pageable);

    Flux<Material> findAll();

    Mono<Material> findById(UUID id);
    // this is not supported at the moment because of
    // https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Material> findAllBy(Pageable pageable, Criteria criteria);
}
