package com.masi.logistics.repository;

import com.masi.logistics.domain.Item;
import com.masi.logistics.domain.criteria.ItemCriteria;

import java.util.List;
import java.util.UUID;

import com.masi.logistics.service.dto.InventoriesStorageDTO;
import com.masi.logistics.service.mapper.ContactGiftMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the Item entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ItemRepository extends ReactiveCrudRepository<Item, UUID>, ItemRepositoryInternal {
    Flux<Item> findAllBy(Pageable pageable);

    @Query("SELECT * FROM item entity WHERE entity.item_category_id = :id")
    Flux<Item> findByItemCategory(UUID id);

    @Query("SELECT * FROM item entity WHERE entity.item_category_id IS NULL")
    Flux<Item> findAllWhereItemCategoryIsNull();

    @Override
    <S extends Item> Mono<S> save(S entity);

    @Override
    Flux<Item> findAll();

    @Override
    Mono<Item> findById(UUID id);

    @Override
    Mono<Void> deleteById(UUID id);

    // delete with id by update deleted_at
    @Query("UPDATE item SET deleted_at = NOW(), deleted_by = :updatedBy, is_deleted = true WHERE id = :id")
    Mono<Void> deleteById(UUID id, String updatedBy, String company);

    @Query("SELECT COUNT(*) FROM item WHERE code = :code AND company = :company AND is_deleted = false ")
    Mono<Long> countByCodeAndCompanyAndIsDeletedIsFalse(String code, String company);

    @Query("SELECT * FROM item WHERE id IN (:ids) AND is_deleted = false")
    Flux<Item> findAllByIdIn(List<UUID> ids);

    @Query("SELECT * FROM item WHERE created_by = :system AND company = :company LIMIT 1")
    Mono<Item>  findByCreatedByAndCompany(String system, String company);

    @Query("SELECT * FROM item WHERE percent_protein = :proteinPercentageApplyFormat")
    Mono<Item>findByProteinPercentageApply(Float proteinPercentageApplyFormat);
}

interface ItemRepositoryInternal {
    <S extends Item> Mono<S> save(S entity);

    Flux<Item> findAllBy(Pageable pageable);

    Flux<Item> findAll();

    Mono<Item> findById(UUID id);
    Mono<Item> findById(UUID id, String company);

    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Item> findAllBy(Pageable pageable, Criteria criteria);
    Flux<Item> findByCriteria(ItemCriteria criteria, Pageable pageable);

    Mono<Long> countByCriteria(ItemCriteria criteria);
}
