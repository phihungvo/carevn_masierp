package com.masi.logistics.repository;

import com.masi.logistics.domain.ItemLiquidationDetail;
import com.masi.logistics.domain.criteria.ItemLiquidationDetailCriteria;
import java.util.UUID;

import com.masi.logistics.service.dto.ItemLiquidationDTO;
import com.masi.logistics.service.mapper.ContactGiftMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the ItemLiquidationDetail entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ItemLiquidationDetailRepository
    extends ReactiveCrudRepository<ItemLiquidationDetail, UUID>, ItemLiquidationDetailRepositoryInternal {
    Flux<ItemLiquidationDetail> findAllBy(Pageable pageable);

    @Override
    <S extends ItemLiquidationDetail> Mono<S> save(S entity);

    @Override
    Flux<ItemLiquidationDetail> findAll();

    @Override
    Mono<ItemLiquidationDetail> findById(UUID id);

    @Override
    Mono<Void> deleteById(UUID id);

    @Query("UPDATE item_liquidation_detail SET is_deleted = true WHERE item_liquidation_id = :id")
    Mono<Void> changeIsDeletedByItemLiquidationId(UUID id);

    Flux<ItemLiquidationDetail> findByItemLiquidationId(UUID itemLiquidationId);

    @Query("SELECT * FROM item_liquidation_detail entity WHERE entity.inventories_storage_id = :id AND entity.is_deleted = :b")
    Flux<ItemLiquidationDetail> findByInventoryStorageIdAndIsDeleted(UUID id, boolean b);
}

interface ItemLiquidationDetailRepositoryInternal {
    <S extends ItemLiquidationDetail> Mono<S> save(S entity);

    Flux<ItemLiquidationDetail> findAllBy(Pageable pageable);

    Flux<ItemLiquidationDetail> findAll();

    Mono<ItemLiquidationDetail> findById(UUID id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<ItemLiquidationDetail> findAllBy(Pageable pageable, Criteria criteria);
    Flux<ItemLiquidationDetail> findByCriteria(ItemLiquidationDetailCriteria criteria, Pageable pageable);

    Mono<Long> countByCriteria(ItemLiquidationDetailCriteria criteria);
}
