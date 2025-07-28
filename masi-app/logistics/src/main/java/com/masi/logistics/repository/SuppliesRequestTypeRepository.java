package com.masi.logistics.repository;

import com.masi.logistics.domain.SuppliesRequestType;
import com.masi.logistics.domain.criteria.SuppliesRequestTypeCriteria;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the SuppliesRequestType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SuppliesRequestTypeRepository
    extends ReactiveCrudRepository<SuppliesRequestType, UUID>, SuppliesRequestTypeRepositoryInternal {
    Flux<SuppliesRequestType> findAllBy(Pageable pageable);

    @Override
    <S extends SuppliesRequestType> Mono<S> save(S entity);

    @Override
    Flux<SuppliesRequestType> findAll();

    @Override
    Mono<SuppliesRequestType> findById(UUID id);

    @Override
    Mono<Void> deleteById(UUID id);
}

interface SuppliesRequestTypeRepositoryInternal {
    <S extends SuppliesRequestType> Mono<S> save(S entity);

    Flux<SuppliesRequestType> findAllBy(Pageable pageable);

    Flux<SuppliesRequestType> findAll();

    Mono<SuppliesRequestType> findById(UUID id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<SuppliesRequestType> findAllBy(Pageable pageable, Criteria criteria);
    Flux<SuppliesRequestType> findByCriteria(SuppliesRequestTypeCriteria criteria, Pageable pageable);

    Mono<Long> countByCriteria(SuppliesRequestTypeCriteria criteria);
}
