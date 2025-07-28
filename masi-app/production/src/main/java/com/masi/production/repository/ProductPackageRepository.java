package com.masi.production.repository;

import com.masi.production.domain.ProductPackage;
import com.masi.production.service.dto.ProductPackageQuery;

import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the ProductPackage entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductPackageRepository extends ReactiveCrudRepository<ProductPackage, UUID>, ProductPackageRepositoryInternal {
    Flux<ProductPackage> findAllBy(Pageable pageable);

    @Query("SELECT * FROM product_package entity WHERE entity.work_order_id = :id")
    Flux<ProductPackage> findByWorkOrder(UUID id);

    @Query("SELECT * FROM product_package entity WHERE entity.work_order_id IS NULL")
    Flux<ProductPackage> findAllWhereWorkOrderIsNull();

    Mono<ProductPackage> findFirstByIdAndIsDeletedIsFalse(UUID id);

    @Override
    <S extends ProductPackage> Mono<S> save(S entity);

    @Override
    Flux<ProductPackage> findAll();

    @Override
    Mono<ProductPackage> findById(UUID id);

    @Query("SELECT * FROM product_package entity WHERE entity.id = :id AND entity.is_deleted = false")
    Mono<ProductPackage> findByIdAndIsDeletedIsFalse(UUID id);

    @Override
    Mono<Void> deleteById(UUID id);

    @Query("SELECT COUNT(package_code) FROM product_package WHERE package_code = :packageCode AND id != :id")
    Mono<Long> countAllByPackageCode(String packageCode,UUID id);

    @Query("UPDATE product_package SET is_deleted = :b WHERE manufacture_order_id = :id")
    Mono<Void> changeIsDeletedByManufactureOrderId(UUID id, boolean b);
}

interface ProductPackageRepositoryInternal {
    <S extends ProductPackage> Mono<S> save(S entity);

    Flux<ProductPackage> findAllBy(Pageable pageable);

    Flux<ProductPackage> findAll();

    Mono<ProductPackage> findById(UUID id);

    Flux<ProductPackage> findAllByQuery(ProductPackageQuery query, Pageable pageable);

    Mono<Long> countByQuery(ProductPackageQuery query);

    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<ProductPackage> findAllBy(Pageable pageable, Criteria criteria);
}
