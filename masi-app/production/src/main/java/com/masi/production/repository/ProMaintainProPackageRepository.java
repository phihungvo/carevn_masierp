package com.masi.production.repository;

import com.masi.production.domain.ProMaintainProPackage;
import com.masi.production.service.dto.ProductMaintainDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Spring Data R2DBC repository for the ProMaintainProPackage entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProMaintainProPackageRepository
        extends ReactiveCrudRepository<ProMaintainProPackage, UUID>, ProMaintainProPackageRepositoryInternal {
    Flux<ProMaintainProPackage> findAllBy(Pageable pageable);

    @Override
    <S extends ProMaintainProPackage> Mono<S> save(S entity);

    @Override
    Flux<ProMaintainProPackage> findAll();

    @Override
    Mono<ProMaintainProPackage> findById(UUID id);

    @Override
    Mono<Void> deleteById(UUID id);

    @Query("UPDATE pro_maintain_pro_package SET is_deleted = :isDeleted WHERE product_maintain_id = :ProductMaintainId  ")
    Mono<Void> deleteAllByProductMaintainId(UUID ProductMaintainId, Boolean isDeleted);

    Flux<ProMaintainProPackage> findAllByProductMaintainId(UUID productMaintainId);
}

interface ProMaintainProPackageRepositoryInternal {
    <S extends ProMaintainProPackage> Mono<S> save(S entity);

    Flux<ProMaintainProPackage> findAllBy(Pageable pageable);

    Flux<ProMaintainProPackage> findAll();

    Mono<ProMaintainProPackage> findById(UUID id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<ProMaintainProPackage> findAllBy(Pageable pageable, Criteria criteria);
}
