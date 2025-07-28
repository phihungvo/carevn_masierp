package com.masi.logistics.repository;

import com.masi.logistics.domain.AssetTransferDetails;
import com.masi.logistics.domain.criteria.AssetTransferDetailsCriteria;
import java.util.UUID;

import com.masi.logistics.service.mapper.ContactGiftMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the AssetTransferDetails entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AssetTransferDetailsRepository
    extends ReactiveCrudRepository<AssetTransferDetails, UUID>, AssetTransferDetailsRepositoryInternal {
    Flux<AssetTransferDetails> findAllBy(Pageable pageable);

    @Override
    <S extends AssetTransferDetails> Mono<S> save(S entity);

    @Override
    Flux<AssetTransferDetails> findAll();

    @Override
    Mono<AssetTransferDetails> findById(UUID id);

    @Override
    Mono<Void> deleteById(UUID id);

    @Modifying
    @Query("UPDATE asset_transfer_details SET is_deleted = true WHERE item_asset_transfer_id = :itemAssetTransferId AND is_deleted = false AND company = :company")
    Mono<Integer> deleteByItemAssetTransferId(UUID itemAssetTransferId, String company);

    @Query("SELECT * FROM asset_transfer_details entity WHERE entity.item_asset_transfer_id = :id AND entity.is_deleted = false AND company = :company ORDER BY entity.created_at DESC")
    Flux<AssetTransferDetails> findByItemAssetTransferId(UUID id, String company);

    @Query("SELECT * FROM asset_transfer_details entity WHERE entity.item_asset_transfer_id = :id AND entity.is_deleted = false")
    Flux<AssetTransferDetails> findByItemAssetTransferId(UUID id);

    @Query("SELECT * FROM asset_transfer_details entity WHERE entity.inventories_storage_id = :id AND entity.is_deleted = :b")
    Flux<AssetTransferDetails> findByInventoryStorageIdAndIsDeleted(UUID id, boolean b);
}

interface AssetTransferDetailsRepositoryInternal {
    <S extends AssetTransferDetails> Mono<S> save(S entity);

    Flux<AssetTransferDetails> findAllBy(Pageable pageable);

    Flux<AssetTransferDetails> findAll();

    Mono<AssetTransferDetails> findById(UUID id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<AssetTransferDetails> findAllBy(Pageable pageable, Criteria criteria);
    Flux<AssetTransferDetails> findByCriteria(AssetTransferDetailsCriteria criteria, Pageable pageable);

    Mono<Long> countByCriteria(AssetTransferDetailsCriteria criteria);
}
