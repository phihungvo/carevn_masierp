package com.masi.logistics.repository;

import com.masi.logistics.domain.ItemCategory;
import com.masi.logistics.domain.criteria.ItemCategoryCriteria;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the ItemCategory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ItemCategoryRepository extends ReactiveCrudRepository<ItemCategory, UUID>, ItemCategoryRepositoryInternal {
    Flux<ItemCategory> findAllBy(Pageable pageable);

    @Query("SELECT * FROM item_category entity WHERE entity.warehouse_type_id = :id")
    Flux<ItemCategory> findByWarehouseType(UUID id);

    @Query("SELECT * FROM item_category entity WHERE entity.warehouse_type_id IS NULL")
    Flux<ItemCategory> findAllWhereWarehouseTypeIsNull();

    @Override
    <S extends ItemCategory> Mono<S> save(S entity);

    @Override
    Flux<ItemCategory> findAll();

    @Override
    Mono<ItemCategory> findById(UUID id);

    @Override
    Mono<Void> deleteById(UUID id);
}

interface ItemCategoryRepositoryInternal {
    <S extends ItemCategory> Mono<S> save(S entity);

    Flux<ItemCategory> findAllBy(Pageable pageable);

    Flux<ItemCategory> findAll();

    Mono<ItemCategory> findById(UUID id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<ItemCategory> findAllBy(Pageable pageable, Criteria criteria);
    Flux<ItemCategory> findByCriteria(ItemCategoryCriteria criteria, Pageable pageable);

    Mono<Long> countByCriteria(ItemCategoryCriteria criteria);
}
