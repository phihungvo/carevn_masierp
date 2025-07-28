package com.masi.logistics.repository;

import com.masi.logistics.domain.WarehouseType;
import com.masi.logistics.domain.criteria.WarehouseTypeCriteria;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the WarehouseType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WarehouseTypeRepository extends ReactiveCrudRepository<WarehouseType, UUID>, WarehouseTypeRepositoryInternal {
    Flux<WarehouseType> findAllBy(Pageable pageable);

    @Override
    <S extends WarehouseType> Mono<S> save(S entity);

    @Override
    Flux<WarehouseType> findAll();

    @Override
    Mono<WarehouseType> findById(UUID id);


    @Override
    Mono<Void> deleteById(UUID id);
}

interface WarehouseTypeRepositoryInternal {
    <S extends WarehouseType> Mono<S> save(S entity);

    Flux<WarehouseType> findAllBy(Pageable pageable);

    Flux<WarehouseType> findAll();

    Mono<WarehouseType> findById(UUID id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<WarehouseType> findAllBy(Pageable pageable, Criteria criteria);
    Flux<WarehouseType> findByCriteria(WarehouseTypeCriteria criteria, Pageable pageable);

    Mono<Long> countByCriteria(WarehouseTypeCriteria criteria);
}
