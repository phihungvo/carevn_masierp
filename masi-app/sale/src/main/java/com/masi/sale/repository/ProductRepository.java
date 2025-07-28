package com.masi.sale.repository;

import com.masi.sale.domain.Material;
import com.masi.sale.domain.Product;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the Product entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductRepository extends ReactiveCrudRepository<Product, UUID>, ProductRepositoryInternal {
    Flux<Product> findAllBy(Pageable pageable);

    @Override
    <S extends Product> Mono<S> save(S entity);

    @Override
    Flux<Product> findAll();

    @Override
    Mono<Product> findById(UUID id);

    @Override
    Mono<Void> deleteById(UUID id);

    @Query("SELECT COUNT(c) FROM product c WHERE c.is_deleted = false AND c.company = :company")
    Mono<Long> countProductsByFilter( String company);

    @Query("SELECT * FROM product C WHERE C.is_deleted = false AND C.company = :company")
    Flux<Product> findAllProductsByFilter(String company);

    @Query("SELECT * FROM product C WHERE C.id = :id AND C.is_deleted = false AND C.company = :company")
    Mono<Product> findNotRemoveById(UUID id, String company);
}

interface ProductRepositoryInternal {
    <S extends Product> Mono<S> save(S entity);

    Flux<Product> findAllBy(Pageable pageable);

    Flux<Product> findAll();

    Mono<Product> findById(UUID id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Product> findAllBy(Pageable pageable, Criteria criteria);
}
