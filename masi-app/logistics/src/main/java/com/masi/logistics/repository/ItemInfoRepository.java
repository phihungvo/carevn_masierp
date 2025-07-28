package com.masi.logistics.repository;

import com.masi.logistics.domain.ItemInfo;
import com.masi.logistics.domain.criteria.ItemInfoCriteria;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

import com.masi.logistics.service.dto.InventoriesStorageDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the ItemInfo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ItemInfoRepository extends ReactiveCrudRepository<ItemInfo, UUID>, ItemInfoRepositoryInternal {
    Flux<ItemInfo> findAllBy(Pageable pageable);

    @Override
    <S extends ItemInfo> Mono<S> save(S entity);

    @Override
    Flux<ItemInfo> findAll();

    @Override
    Mono<ItemInfo> findById(UUID id);

    @Override
    Mono<Void> deleteById(UUID id);

    Mono<ItemInfo> findByInventoryStorageIdAndIsDeleted(UUID inventoryStorageId, Boolean isDeleted);

    @Query("UPDATE item_info SET is_deleted = :b WHERE inventory_storage_id = :id")
    Mono<Void> changeIsDeletedByInventoriesId(UUID id, boolean b);

    @Query("SELECT * FROM item_info WHERE inventory_storage_id IN (:inventoryIds) AND is_deleted = :b")
    Flux<ItemInfo> findByInventoryStorageIdInAndIsDeleted(List<UUID> inventoryIds, boolean b);

    @Query("UPDATE item_info SET user_id = :userId WHERE inventory_storage_id = :id")
    Mono<Void> changeUserByIds(UUID id, UUID userId);

    @Modifying
    @Query("UPDATE item_info SET liquidation_date = :now WHERE inventory_storage_id IN (:uuids)")
    Mono<Integer> changeDateLiquidationByInventoriesStorageIds(List<UUID> uuids, ZonedDateTime now);

}

interface ItemInfoRepositoryInternal {
    <S extends ItemInfo> Mono<S> save(S entity);

    Flux<ItemInfo> findAllBy(Pageable pageable);

    Flux<ItemInfo> findAll();

    Mono<ItemInfo> findById(UUID id);

    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<ItemInfo> findAllBy(Pageable pageable, Criteria criteria);
    Flux<ItemInfo> findByCriteria(ItemInfoCriteria criteria, Pageable pageable);

    Mono<Long> countByCriteria(ItemInfoCriteria criteria);
}
