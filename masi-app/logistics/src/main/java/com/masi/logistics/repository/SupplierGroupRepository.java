package com.masi.logistics.repository;

import com.masi.logistics.domain.SupplierGroup;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the SupplierGroup entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SupplierGroupRepository extends ReactiveCrudRepository<SupplierGroup, UUID>, SupplierGroupRepositoryInternal {
    Flux<SupplierGroup> findAllBy(Pageable pageable);

    @Override
    <S extends SupplierGroup> Mono<S> save(S entity);

    @Override
    Flux<SupplierGroup> findAll();

    @Override
    Mono<SupplierGroup> findById(UUID id);

    @Query("SELECT * FROM supplier_group entity WHERE entity.id = :id AND entity.company = :company AND entity.delete_at IS NULL AND entity.delete_by IS NULL")
    Mono<SupplierGroup> findById(UUID id, String company);

    @Query("UPDATE supplier_group SET delete_at = NOW(), delete_by = :username WHERE id = :id AND company = :company AND delete_at IS NULL AND delete_by IS NULL")
    Mono<SupplierGroup> deleteById(UUID id, String company, String username);

    @Override
    Mono<Void> deleteById(UUID id);
}

interface SupplierGroupRepositoryInternal {
    <S extends SupplierGroup> Mono<S> save(S entity);

    Flux<SupplierGroup> findAllBy(Pageable pageable);

    Flux<SupplierGroup> findAll();

    Mono<SupplierGroup> findById(UUID id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<SupplierGroup> findAllBy(Pageable pageable, Criteria criteria);
}
