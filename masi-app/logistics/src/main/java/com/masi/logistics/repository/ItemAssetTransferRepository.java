package com.masi.logistics.repository;

import com.masi.logistics.domain.ItemAssetTransfer;
import com.masi.logistics.domain.criteria.ItemAssetTransferCriteria;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the ItemAssetTransfer entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ItemAssetTransferRepository extends ReactiveCrudRepository<ItemAssetTransfer, UUID>, ItemAssetTransferRepositoryInternal {
    Flux<ItemAssetTransfer> findAllBy(Pageable pageable);

    @Override
    <S extends ItemAssetTransfer> Mono<S> save(S entity);

    @Override
    Flux<ItemAssetTransfer> findAll();

    @Override
    Mono<ItemAssetTransfer> findById(UUID id);

//    @Query("SELECT * FROM item_asset_transfer entity WHERE entity.id = :id AND entity.company = :company AND entity.is_deleted = false")
//    Mono<ItemAssetTransfer> findById(UUID id, String company);

    @Query("SELECT * FROM item_asset_transfer entity WHERE entity.inventories_storage_id = :inventoriesStorageId AND entity.is_deleted = false")
    Flux<ItemAssetTransfer> findAllByInventoriesStorageId(UUID inventoriesStorageId);

    @Override
    Mono<Void> deleteById(UUID id);

    @Query("SELECT * FROM item_asset_transfer entity WHERE entity.id = :id  AND entity.is_deleted = false")
    Mono<ItemAssetTransfer> findByIdBrier(UUID id);

    @Modifying
    @Query("UPDATE item_asset_transfer SET is_deleted = true WHERE id = :id AND is_deleted = false AND company = :company")
    Mono<Integer> deleteById(UUID id, String company);
}

interface ItemAssetTransferRepositoryInternal {
    <S extends ItemAssetTransfer> Mono<S> save(S entity);

    Flux<ItemAssetTransfer> findAllBy(Pageable pageable);

    Flux<ItemAssetTransfer> findAll();

    Mono<ItemAssetTransfer> findById(UUID id);

    Mono<ItemAssetTransfer> findByIdAndCompany(UUID id, String company);

    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<ItemAssetTransfer> findAllBy(Pageable pageable, Criteria criteria);
    Flux<ItemAssetTransfer> findByCriteria(ItemAssetTransferCriteria criteria, Pageable pageable);

    Mono<Long> countByCriteria(ItemAssetTransferCriteria criteria);
}
