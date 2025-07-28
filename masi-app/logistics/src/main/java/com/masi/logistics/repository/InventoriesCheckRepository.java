package com.masi.logistics.repository;

import com.masi.logistics.domain.InventoriesCheck;
import com.masi.logistics.domain.criteria.InventoriesCheckCriteria;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the InventoriesCheck entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InventoriesCheckRepository extends ReactiveCrudRepository<InventoriesCheck, UUID>, InventoriesCheckRepositoryInternal {
    Flux<InventoriesCheck> findAllBy(Pageable pageable);

    @Override
    <S extends InventoriesCheck> Mono<S> save(S entity);

    @Override
    Flux<InventoriesCheck> findAll();

    @Override
    Mono<InventoriesCheck> findById(UUID id);

    @Override
    Mono<Void> deleteById(UUID id);

    @Query("UPDATE inventories_check SET is_deleted = :isDeleted WHERE id = :id")
    Mono<Void> changeDeleted(UUID id, boolean isDeleted);
}

interface InventoriesCheckRepositoryInternal {
    <S extends InventoriesCheck> Mono<S> save(S entity);

    Flux<InventoriesCheck> findAllBy(Pageable pageable);

    Flux<InventoriesCheck> findAll();

    Mono<InventoriesCheck> findById(UUID id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<InventoriesCheck> findAllBy(Pageable pageable, Criteria criteria);
    Flux<InventoriesCheck> findByCriteria(InventoriesCheckCriteria criteria, Pageable pageable);

    Mono<Long> countByCriteria(InventoriesCheckCriteria criteria);
}
