package com.masi.logistics.repository;

import com.masi.logistics.domain.Inventories;
import com.masi.logistics.domain.criteria.InventoriesCriteria;
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
 * Spring Data R2DBC repository for the Inventories entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InventoriesRepository extends ReactiveCrudRepository<Inventories, UUID>, InventoriesRepositoryInternal {
    Flux<Inventories> findAllBy(Pageable pageable);

    @Override
    <S extends Inventories> Mono<S> save(S entity);

    @Override
    Flux<Inventories> findAll();

    @Override
    Mono<Inventories> findById(UUID id);

    @Override
    Mono<Void> deleteById(UUID id);

    @Query("UPDATE inventories SET is_deleted = :isDeleted WHERE id = :id")
    Mono<Void> changeSortIsDeleted(UUID id, Boolean isDeleted);

    @Modifying
    @Query("UPDATE inventories SET invoice_id = NULL WHERE invoice_id IN (:invoiceIds) AND is_deleted = FALSE")
    Mono<Void> clearInvoiceIdByInvoiceIds(Iterable<UUID> invoiceIds);

    @Query("SELECT * FROM inventories WHERE incoming_warehouse_id = :idWarehouse AND company = :companyId AND is_deleted = FALSE")
    Flux<Inventories> findAllByIncomingWarehouseIdAndCompanyId(UUID idWarehouse, String companyId);
}

interface InventoriesRepositoryInternal {
    <S extends Inventories> Mono<S> save(S entity);

    Flux<Inventories> findAllBy(Pageable pageable);

    Flux<Inventories> findAll();

    Mono<Inventories> findById(UUID id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Inventories> findAllBy(Pageable pageable, Criteria criteria);
    Flux<Inventories> findByCriteria(InventoriesCriteria criteria, Pageable pageable);

    Mono<Long> countByCriteria(InventoriesCriteria criteria);
}
