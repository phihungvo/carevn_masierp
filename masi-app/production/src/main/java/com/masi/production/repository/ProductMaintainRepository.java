package com.masi.production.repository;

import com.masi.production.domain.ProductMaintain;
import java.util.UUID;

import com.masi.production.service.dto.ProductMaintainQuery;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the ProductMaintain entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductMaintainRepository extends ReactiveCrudRepository<ProductMaintain, UUID>, ProductMaintainRepositoryInternal {
    Flux<ProductMaintain> findAllBy(Pageable pageable);

    @Override
    <S extends ProductMaintain> Mono<S> save(S entity);

    @Override
    Flux<ProductMaintain> findAll();

    @Override
    Mono<ProductMaintain> findByIdAndIsDeletedIsFalse(UUID id);

    @Query("SELECT * FROM product_maintain entity WHERE entity.is_deleted = false AND entity.id = :id")
    Mono<ProductMaintain> findByIdNotDeleted(UUID id);

    @Override
    Mono<Void> deleteById(UUID id);

    @Query("UPDATE product_maintain SET is_deleted = true WHERE id = :id")
    Mono<Void> softDeleteById(@Param("id") UUID id);


    @Query("SELECT COUNT(*) FROM product_maintain entity WHERE entity.is_deleted = false AND entity.product_batch_code = :productBatchCode")
    Mono<Long> countByProductBatchCode(String productBatchCode);

    @Query("UPDATE product_maintain SET is_deleted = :b WHERE product_package_id = :id")
    Mono<Void> changeIsDeletedByProductPackageId(UUID id, boolean b);
}
interface ProductMaintainRepositoryInternal {
    <S extends ProductMaintain> Mono<S> save(S entity);

    Flux<ProductMaintain> findAllBy(Pageable pageable);

    Flux<ProductMaintain> findAll();

    Mono<ProductMaintain> findByIdAndIsDeletedIsFalse(UUID id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<ProductMaintain> findAllBy(Pageable pageable, Criteria criteria);

    Mono<Long> countByFilter(ProductMaintainQuery query);

    Flux<ProductMaintain> findAllByFilter(ProductMaintainQuery query, Pageable pageable);
}
