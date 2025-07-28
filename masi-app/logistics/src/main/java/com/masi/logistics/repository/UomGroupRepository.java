package com.masi.logistics.repository;

import com.masi.logistics.domain.UomGroup;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the UomGroup entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UomGroupRepository extends ReactiveCrudRepository<UomGroup, UUID>, UomGroupRepositoryInternal {
    Flux<UomGroup> findAllBy(Pageable pageable);

    @Query("SELECT * FROM uom_group entity WHERE entity.base_uom_id = :id")
    Flux<UomGroup> findByBaseUom(UUID id);

    @Query("SELECT * FROM uom_group entity WHERE entity.base_uom_id IS NULL")
    Flux<UomGroup> findAllWhereBaseUomIsNull();

    @Override
    <S extends UomGroup> Mono<S> save(S entity);

    @Override
    Flux<UomGroup> findAll();

    @Override
    Mono<UomGroup> findById(UUID id);

    @Override
    Mono<Void> deleteById(UUID id);

    // delete all by uomGroupId and company and deleted at is null, update deleted at and deleted by
    @Query("UPDATE uom_group SET deleted_at = NOW(), delete_by = :deletedBy WHERE id = :id AND delete_at IS NULL AND company = :company")
    Mono<Void> deleteById(UUID id, String deletedBy, String company);
}

interface UomGroupRepositoryInternal {
    <S extends UomGroup> Mono<S> save(S entity);

    Flux<UomGroup> findAllBy(Pageable pageable);

    Flux<UomGroup> findAll();

    Mono<UomGroup> findById(UUID id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<UomGroup> findAllBy(Pageable pageable, Criteria criteria);
}
