package com.masi.logistics.repository;

import com.masi.logistics.domain.ItemAssetDepreciation;
import com.masi.logistics.domain.ItemAssetDepreciationDetail;
import com.masi.logistics.domain.criteria.ItemAssetDepreciationDetailCriteria;
import java.util.UUID;
import java.util.function.Function;

import com.masi.logistics.service.mapper.ContactGiftMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the ItemAssetDepreciationDetail entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ItemAssetDepreciationDetailRepository
    extends ReactiveCrudRepository<ItemAssetDepreciationDetail, UUID>, ItemAssetDepreciationDetailRepositoryInternal {
    Flux<ItemAssetDepreciationDetail> findAllBy(Pageable pageable);

    @Override
    <S extends ItemAssetDepreciationDetail> Mono<S> save(S entity);

    @Override
    Flux<ItemAssetDepreciationDetail> findAll();

    @Override
    Mono<ItemAssetDepreciationDetail> findById(UUID id);

    @Override
    Mono<Void> deleteById(UUID id);

    @Query("UPDATE item_asset_depreciation_detail SET is_deleted = true WHERE item_asset_depreciation_id = :id")
    Mono<Void>changeIsDeletedByItemAssetDepreciationId(UUID id);

    Flux<ItemAssetDepreciationDetail> findByItemAssetDepreciationId(UUID itemAssetDepreciationId);

    @Query("SELECT * FROM item_asset_depreciation_detail entity WHERE entity.inventories_storage_id = :id AND entity.is_deleted = :b")
    Flux<ItemAssetDepreciationDetail> findByInventoryStorageIdAndIsDeleted(UUID id, boolean b);
}

interface ItemAssetDepreciationDetailRepositoryInternal {
    <S extends ItemAssetDepreciationDetail> Mono<S> save(S entity);

    Flux<ItemAssetDepreciationDetail> findAllBy(Pageable pageable);

    Flux<ItemAssetDepreciationDetail> findAll();

    Mono<ItemAssetDepreciationDetail> findById(UUID id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<ItemAssetDepreciationDetail> findAllBy(Pageable pageable, Criteria criteria);
    Flux<ItemAssetDepreciationDetail> findByCriteria(ItemAssetDepreciationDetailCriteria criteria, Pageable pageable);

    Mono<Long> countByCriteria(ItemAssetDepreciationDetailCriteria criteria);
}
