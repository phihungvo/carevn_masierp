package com.masi.logistics.repository;

import com.masi.logistics.domain.WarehouseRole;
import com.masi.logistics.domain.criteria.WarehouseRoleCriteria;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the WarehouseRole entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WarehouseRoleRepository extends ReactiveCrudRepository<WarehouseRole, UUID>, WarehouseRoleRepositoryInternal {
    Flux<WarehouseRole> findAllBy(Pageable pageable);

    @Query("SELECT * FROM warehouse_role entity WHERE entity.warehouse_type_id = :id")
    Flux<WarehouseRole> findByWarehouseType(UUID id);

    @Query("SELECT * FROM warehouse_role entity WHERE entity.warehouse_type_id IS NULL")
    Flux<WarehouseRole> findAllWhereWarehouseTypeIsNull();

    @Override
    <S extends WarehouseRole> Mono<S> save(S entity);

    @Override
    Flux<WarehouseRole> findAll();

    @Override
    Mono<WarehouseRole> findById(UUID id);

    @Override
    Mono<Void> deleteById(UUID id);
}

interface WarehouseRoleRepositoryInternal {
    <S extends WarehouseRole> Mono<S> save(S entity);

    Flux<WarehouseRole> findAllBy(Pageable pageable);

    Flux<WarehouseRole> findAll();

    Mono<WarehouseRole> findById(UUID id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<WarehouseRole> findAllBy(Pageable pageable, Criteria criteria);
    Flux<WarehouseRole> findByCriteria(WarehouseRoleCriteria criteria, Pageable pageable);

    Mono<Long> countByCriteria(WarehouseRoleCriteria criteria);
}
