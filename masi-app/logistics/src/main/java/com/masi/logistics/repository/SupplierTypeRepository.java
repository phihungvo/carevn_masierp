package com.masi.logistics.repository;

import com.masi.logistics.domain.SupplierType;
import com.masi.logistics.domain.criteria.SupplierTypeCriteria;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the SupplierType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SupplierTypeRepository extends ReactiveCrudRepository<SupplierType, UUID>, SupplierTypeRepositoryInternal {
    Flux<SupplierType> findAllBy(Pageable pageable);

    @Override
    <S extends SupplierType> Mono<S> save(S entity);

    @Override
    Flux<SupplierType> findAll();

    @Override
    Mono<SupplierType> findById(UUID id);

    @Override
    Mono<Void> deleteById(UUID id);
}

interface SupplierTypeRepositoryInternal {
    <S extends SupplierType> Mono<S> save(S entity);

    Flux<SupplierType> findAllBy(Pageable pageable);

    Flux<SupplierType> findAll();

    Mono<SupplierType> findById(UUID id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<SupplierType> findAllBy(Pageable pageable, Criteria criteria);
    Flux<SupplierType> findByCriteria(SupplierTypeCriteria criteria, Pageable pageable);

    Mono<Long> countByCriteria(SupplierTypeCriteria criteria);
}
