package com.masi.logistics.repository;

import com.masi.logistics.domain.InventoriesDetail;
import com.masi.logistics.domain.InventoriesDetailMapItem;
import com.masi.logistics.domain.InventoriesStorage;
import com.masi.logistics.domain.criteria.InventoriesDetailCriteria;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import com.masi.logistics.service.mapper.InventoriesMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the InventoriesDetail entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InventoriesDetailRepository extends ReactiveCrudRepository<InventoriesDetail, UUID>, InventoriesDetailRepositoryInternal {
    Flux<InventoriesDetail> findAllBy(Pageable pageable);

    @Override
    <S extends InventoriesDetail> Mono<S> save(S entity);

    @Override
    Flux<InventoriesDetail> findAll();

    @Override
    Mono<InventoriesDetail> findById(UUID id);

    @Override
    Mono<Void> deleteById(UUID id);

    @Query("SELECT * FROM inventories_detail entity WHERE entity.inventories_id IN (:ids) AND entity.is_deleted = false")
    Flux<InventoriesDetail> findAllByInventoriesIdIn(Collection<UUID> ids);

    @Query("UPDATE inventories_detail SET is_deleted = true WHERE inventories_id = :id")
    Mono<Void> deleteAllByInventoriesId(UUID id);

    @Query("""
    SELECT entity.*, 
           it.code AS item_code, 
           it.name AS item_name,
           it.percent_protein AS item_percent_protein,
           it.is_separation AS item_is_separation
    FROM inventories_detail entity
    JOIN public.item it ON entity.item_id = it.id
    WHERE entity.inventories_id = :id 
    AND entity.is_deleted = :isDeleted
""")
    Flux<InventoriesDetailMapItem> findAllByInventoriesIdAndIsDeleted(UUID id, Boolean isDeleted);



}

interface InventoriesDetailRepositoryInternal {
    <S extends InventoriesDetail> Mono<S> save(S entity);

    Flux<InventoriesDetail> findAllBy(Pageable pageable);

    Flux<InventoriesDetail> findAll();

    Mono<InventoriesDetail> findById(UUID id);

    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<InventoriesDetail> findAllBy(Pageable pageable, Criteria criteria);
    Flux<InventoriesDetail> findByCriteria(InventoriesDetailCriteria criteria, Pageable pageable);

    Mono<Long> countByCriteria(InventoriesDetailCriteria criteria);
}
