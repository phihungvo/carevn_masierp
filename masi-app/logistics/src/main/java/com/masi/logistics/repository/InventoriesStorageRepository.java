package com.masi.logistics.repository;

import com.masi.logistics.domain.InventoriesStorage;
import com.masi.logistics.domain.InventoriesStorageTotal;
import com.masi.logistics.domain.criteria.InventoriesStorageCriteria;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import com.masi.logistics.domain.enumeration.ItemStatus;
import com.masi.logistics.domain.enumeration.StatusEntity;
import com.masi.logistics.service.mapper.InventoriesMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the InventoriesStorage entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InventoriesStorageRepository
        extends ReactiveCrudRepository<InventoriesStorage, UUID>, InventoriesStorageRepositoryInternal {
    Flux<InventoriesStorage> findAllBy(Pageable pageable);

    @Override
    <S extends InventoriesStorage> Mono<S> save(S entity);

    @Override
    Flux<InventoriesStorage> findAll();

    @Override
    Mono<InventoriesStorage> findById(UUID id);

    @Query("SELECT * FROM inventories_storage entity WHERE id = :id")
    Mono<InventoriesStorage> findByIdMapBrier(UUID id);

    @Override
    Mono<Void> deleteById(UUID id);

    @Query("""

            SELECT
                           i.item_id,
                it.code AS item_code,
                it.name AS item_name,
                SUM(i.quantity) AS total_count
                        FROM public.inventories_storage i
                        JOIN public.item it ON i.item_id = it.id
                        WHERE i.item_id IN (:documentIds) AND i.warehouse_id = :warehouseId
                        AND i.is_deleted = false
                        GROUP BY i.item_id, it.code, it.name;

            """)
    Flux<InventoriesStorageTotal> findAllByItemId(List<UUID> documentIds , UUID warehouseId);

    @Query("""

            SELECT
                i.item_id,
                it.code AS item_code,
                it.name AS item_name,
                SUM(i.quantity) AS total_count,
                w.name AS warehouse_name,
                w.id AS warehouse_id
            FROM public.inventories_storage i
            JOIN public.item it ON i.item_id = it.id
            JOIN warehouse w ON i.warehouse_id = w.id
            and (w.id = :warehouseId or :warehouseId is NULL)
            WHERE i.item_id IN (:documentIds)
            AND i.is_deleted = false
            GROUP BY i.item_id, it.code, it.name, w.name, w.id;

            """)
    Flux<InventoriesStorageTotal> findAllByItemIdAndWarehouseId(List<UUID> documentIds, UUID warehouseId);

    @Query("""
                SELECT
                    i.item_id,
                    it.code AS item_code,
                    it.name AS item_name,
                    uom.name AS uom_name,
                    ic.name as item_category_name,
                    ic.code as item_category_code,
                    ic.id as item_category_id,
                    SUM(i.quantity) AS total_count
                FROM public.inventories_storage i
                JOIN public.item it ON i.item_id = it.id
                JOIN public.uom uom ON it.uom_id = uom.id
                JOIN item_category ic on ic.id = it.item_category_id
                WHERE
                    i.is_deleted = false
                    AND i.inventories_detail_id IN (:ids)
                    AND i.company = :company
                GROUP BY i.item_id, it.code, it.name, uom.name, ic.name, ic.code, ic.id

                ORDER BY it.name
                LIMIT :#{#pageable.pageSize}
                OFFSET :#{#pageable.offset}

            """)
    Flux<InventoriesStorageTotal> findAllItem(List<UUID> ids, String company, Pageable pageable);


    @Query("""
                SELECT
                    COUNT(DISTINCT i.item_id)
                FROM public.inventories_storage i
                JOIN public.item it ON i.item_id = it.id
                JOIN public.uom uom ON it.uom_id = uom.id
                join item_category ic on ic.id = it.item_category_id
                WHERE
                    i.is_deleted = false
                    AND i.inventories_detail_id IN (:ids)
                    AND i.company = :company
            """)
    Mono<Long> findAllItemCount(List<UUID> ids, String company);


    Flux<InventoriesStorage> saveAll(List<InventoriesStorage> storageList);

    @Query("""
                SELECT * FROM inventories_storage entity
                WHERE entity.item_id = :id
                AND entity.is_deleted = :b
                ORDER BY entity.created_at DESC
                LIMIT 1
            """)
    Mono<InventoriesStorage> findByItemId(UUID id, boolean b);

    Flux<InventoriesStorage> findAllByItemIdAndIsDeletedOrderByCreatedAtAsc(UUID itemId, boolean isDeleted);

    @Modifying
    @Query("UPDATE inventories_storage SET is_deleted = :b WHERE id = :id")
    Mono<Void> changeIsDeletedById(UUID id, boolean b);

    @Modifying
    @Query("UPDATE inventories_storage SET status = :status WHERE id IN (:uuids)")
    Mono<Integer> changeStatusByIds(List<UUID> uuids, ItemStatus status);

    @Modifying
    @Query("UPDATE inventories_storage SET department = :departmentId WHERE id = :uuids")
    Mono<Integer> changeDepartmentByIds(UUID uuids, String departmentId);

    @Modifying
    @Query("UPDATE inventories_storage " +
            "SET remaining_price = remaining_price - :amortizationAmount " +
            "WHERE id = :inventoriesStorageId")
    Mono<Integer> updateRemainingPriceById(UUID inventoriesStorageId, BigDecimal amortizationAmount);

    @Modifying
    @Query("""
            UPDATE inventories_storage 
            SET item_id = :itemId 
            WHERE inventories_detail_id IN (
                SELECT id FROM inventories_detail 
                WHERE inventories_id IN (
                    SELECT id FROM inventories WHERE production_id = :productId
                )
            )
            """)
    Flux<InventoriesStorage> updateItemIdByProductId( UUID productId, UUID itemId);

}

interface InventoriesStorageRepositoryInternal {
    <S extends InventoriesStorage> Mono<S> save(S entity);

    Flux<InventoriesStorage> findAllBy(Pageable pageable);

    Flux<InventoriesStorage> findAll();

//    Flux<InventoriesStorage> saveAll(List<InventoriesStorage> storageList);

    Mono<InventoriesStorage> findById(UUID id);

    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<InventoriesStorage> findAllBy(Pageable pageable, Criteria criteria);
    Flux<InventoriesStorage> findByCriteria(InventoriesStorageCriteria criteria, Pageable pageable);

    Mono<Long> countByCriteria(InventoriesStorageCriteria criteria);
}
