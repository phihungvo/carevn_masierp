package com.masi.logistics.repository;

import com.masi.logistics.domain.ItemLiquidation;
import com.masi.logistics.domain.criteria.ItemLiquidationCriteria;
import java.util.UUID;

import com.masi.logistics.service.mapper.ContactGiftMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the ItemLiquidation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ItemLiquidationRepository extends ReactiveCrudRepository<ItemLiquidation, UUID>, ItemLiquidationRepositoryInternal {
    Flux<ItemLiquidation> findAllBy(Pageable pageable);

    @Override
    <S extends ItemLiquidation> Mono<S> save(S entity);

    @Override
    Flux<ItemLiquidation> findAll();

    @Override
    Mono<ItemLiquidation> findById(UUID id);

    @Override
    Mono<Void> deleteById(UUID id);

    @Query("UPDATE item_liquidation SET is_deleted = true WHERE id = :id")
    Mono<Void> changeIsDeletedById(UUID id);

    @Query("SELECT * FROM item_liquidation entity WHERE entity.is_deleted = false AND entity.inventories_storage_id = :inventoriesStorageId")
    Flux<ItemLiquidation> findAllByInventoriesStorageId(UUID inventoriesStorageId);
}

interface ItemLiquidationRepositoryInternal {
    <S extends ItemLiquidation> Mono<S> save(S entity);

    Flux<ItemLiquidation> findAllBy(Pageable pageable);

    Flux<ItemLiquidation> findAll();

    Mono<ItemLiquidation> findById(UUID id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<ItemLiquidation> findAllBy(Pageable pageable, Criteria criteria);
    Flux<ItemLiquidation> findByCriteria(ItemLiquidationCriteria criteria, Pageable pageable);

    Mono<Long> countByCriteria(ItemLiquidationCriteria criteria);
}
