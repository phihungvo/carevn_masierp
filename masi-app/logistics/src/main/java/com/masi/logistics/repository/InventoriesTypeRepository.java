package com.masi.logistics.repository;

import com.masi.logistics.domain.InventoriesType;
import com.masi.logistics.domain.criteria.InventoriesTypeCriteria;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the InventoriesType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InventoriesTypeRepository extends ReactiveCrudRepository<InventoriesType, UUID>, InventoriesTypeRepositoryInternal {
    Flux<InventoriesType> findAllBy(Pageable pageable);

    @Override
    <S extends InventoriesType> Mono<S> save(S entity);

    @Override
    Flux<InventoriesType> findAll();

    @Override
    Mono<InventoriesType> findById(UUID id);

    @Override
    Mono<Void> deleteById(UUID id);

    @Query("SELECT * FROM inventories_type entity WHERE entity.created_by = :system AND company = :companyImport LIMIT 1")
    Mono<InventoriesType>  findByCreatedBy(String system, String companyImport);
}

interface InventoriesTypeRepositoryInternal {
    <S extends InventoriesType> Mono<S> save(S entity);

    Flux<InventoriesType> findAllBy(Pageable pageable);

    Flux<InventoriesType> findAll();

    Mono<InventoriesType> findById(UUID id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<InventoriesType> findAllBy(Pageable pageable, Criteria criteria);
    Flux<InventoriesType> findByCriteria(InventoriesTypeCriteria criteria, Pageable pageable);

    Mono<Long> countByCriteria(InventoriesTypeCriteria criteria);
}
