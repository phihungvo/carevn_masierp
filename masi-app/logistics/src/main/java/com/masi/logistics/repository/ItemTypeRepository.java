package com.masi.logistics.repository;

import com.masi.logistics.domain.ItemType;
import com.masi.logistics.domain.criteria.ItemTypeCriteria;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the ItemType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ItemTypeRepository extends ReactiveCrudRepository<ItemType, UUID>, ItemTypeRepositoryInternal {
    Flux<ItemType> findAllBy(Pageable pageable);

    @Override
    <S extends ItemType> Mono<S> save(S entity);

    @Override
    Flux<ItemType> findAll();

    @Override
    Mono<ItemType> findById(UUID id);

    @Override
    Mono<Void> deleteById(UUID id);
}

interface ItemTypeRepositoryInternal {
    <S extends ItemType> Mono<S> save(S entity);

    Flux<ItemType> findAllBy(Pageable pageable);

    Flux<ItemType> findAll();

    Mono<ItemType> findById(UUID id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<ItemType> findAllBy(Pageable pageable, Criteria criteria);
    Flux<ItemType> findByCriteria(ItemTypeCriteria criteria, Pageable pageable);

    Mono<Long> countByCriteria(ItemTypeCriteria criteria);
}
