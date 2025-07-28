package com.masi.logistics.repository;

import com.masi.logistics.domain.ItemAssetDepreciation;
import com.masi.logistics.domain.criteria.ItemAssetDepreciationCriteria;

import java.time.LocalDate;
import java.util.UUID;

import com.masi.logistics.service.mapper.ContactGiftMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the ItemAssetDepreciation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ItemAssetDepreciationRepository
    extends ReactiveCrudRepository<ItemAssetDepreciation, UUID>, ItemAssetDepreciationRepositoryInternal {
    Flux<ItemAssetDepreciation> findAllBy(Pageable pageable);

    @Override
    <S extends ItemAssetDepreciation> Mono<S> save(S entity);

    @Override
    Flux<ItemAssetDepreciation> findAll();

    @Override
    Mono<ItemAssetDepreciation> findById(UUID id);

    @Override
    Mono<Void> deleteById(UUID id);

    @Query("UPDATE item_asset_depreciation SET is_deleted = true WHERE id = :id")
    Mono<Void> changeIsDeletedById(UUID id);

    @Query("SELECT * FROM item_asset_depreciation entity WHERE entity.is_deleted = false and entity.inventories_storage_id = :inventoriesStorageId")
    Flux<ItemAssetDepreciation> findAllByInventoriesStorageId(UUID inventoriesStorageId);


    @Query("SELECT * FROM item_asset_depreciation WHERE ( code = :code OR depreciation_date = :depreciationDate ) AND is_deleted = false AND status != 'CANCELLED' LIMIT 1")
    Mono<ItemAssetDepreciation> findByCodeAndDepreciationDate(String code,LocalDate depreciationDate);


}

interface ItemAssetDepreciationRepositoryInternal {
    <S extends ItemAssetDepreciation> Mono<S> save(S entity);

    Flux<ItemAssetDepreciation> findAllBy(Pageable pageable);

    Flux<ItemAssetDepreciation> findAll();

    Mono<ItemAssetDepreciation> findById(UUID id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<ItemAssetDepreciation> findAllBy(Pageable pageable, Criteria criteria);
    Flux<ItemAssetDepreciation> findByCriteria(ItemAssetDepreciationCriteria criteria, Pageable pageable);

    Mono<Long> countByCriteria(ItemAssetDepreciationCriteria criteria);
}
