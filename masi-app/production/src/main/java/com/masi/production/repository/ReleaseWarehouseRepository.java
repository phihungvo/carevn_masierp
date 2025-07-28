package com.masi.production.repository;

import com.masi.production.domain.ReleaseWarehouse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Spring Data R2DBC repository for the ReleaseWarehouse entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReleaseWarehouseRepository extends ReactiveCrudRepository<ReleaseWarehouse, UUID>, ReleaseWarehouseRepositoryInternal {
    Flux<ReleaseWarehouse> findAllBy(Pageable pageable);

    @Override
    <S extends ReleaseWarehouse> Mono<S> save(S entity);

    @Override
    Flux<ReleaseWarehouse> findAll();

    @Override
    Mono<ReleaseWarehouse> findById(UUID id);

    @Override
    Mono<Void> deleteById(UUID id);

    Flux<ReleaseWarehouse> findAllByManufactureOrderIdAndIsDeleted(UUID id, boolean b);

    @Modifying
    @Query("UPDATE release_warehouse SET is_deleted = :isDeleted WHERE manufacture_order_id = :ManufactureOrderId")
    Mono<Void> changeIsDeletedAllByManufactureOrderId(UUID ManufactureOrderId, boolean isDeleted);

    @Modifying
    @Query("UPDATE release_warehouse SET is_deleted = :isDeleted WHERE additive_material_checklist_id = :additive_material_checklist_id")
    Mono<Void> changeIsDeletedAllByMaterialAdditiveId(UUID additive_material_checklist_id, boolean isDeleted);

    @Query("SELECT * FROM release_warehouse WHERE additive_material_checklist_id = :additive_material_checklist_id AND is_deleted = :isDeleted ORDER BY created_date DESC LIMIT :pageSize OFFSET :page")
    Flux<ReleaseWarehouse> findAllByMaterialAdditiveChecklistIdAndIsDeleted(Integer page, Integer pageSize, UUID materialAdditiveChecklistId, boolean isDeleted);

    @Query("SELECT COUNT(*) FROM release_warehouse WHERE additive_material_checklist_id = :additive_material_checklist_id AND is_deleted = :isDeleted ")
    Mono<Long> countAllByMaterialAdditiveChecklistIdAndIsDeleted(UUID additive_material_checklist_id, boolean isDeleted);

}

interface ReleaseWarehouseRepositoryInternal {
    <S extends ReleaseWarehouse> Mono<S> save(S entity);

    Flux<ReleaseWarehouse> findAllBy(Pageable pageable);

    Flux<ReleaseWarehouse> findAll();

    Mono<ReleaseWarehouse> findById(UUID id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<ReleaseWarehouse> findAllBy(Pageable pageable, Criteria criteria);
}
