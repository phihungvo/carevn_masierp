package com.masi.logistics.repository;

import com.masi.logistics.domain.ItemInfoDetail;
import com.masi.logistics.domain.criteria.ItemInfoDetailCriteria;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the ItemInfoDetail entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ItemInfoDetailRepository extends ReactiveCrudRepository<ItemInfoDetail, UUID>, ItemInfoDetailRepositoryInternal {
    Flux<ItemInfoDetail> findAllBy(Pageable pageable);

    @Override
    <S extends ItemInfoDetail> Mono<S> save(S entity);

    @Override
    Flux<ItemInfoDetail> findAll();

    @Override
    Mono<ItemInfoDetail> findById(UUID id);

    @Override
    Mono<Void> deleteById(UUID id);
}

interface ItemInfoDetailRepositoryInternal {
    <S extends ItemInfoDetail> Mono<S> save(S entity);

    Flux<ItemInfoDetail> findAllBy(Pageable pageable);

    Flux<ItemInfoDetail> findAll();

    Mono<ItemInfoDetail> findById(UUID id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<ItemInfoDetail> findAllBy(Pageable pageable, Criteria criteria);
    Flux<ItemInfoDetail> findByCriteria(ItemInfoDetailCriteria criteria, Pageable pageable);

    Mono<Long> countByCriteria(ItemInfoDetailCriteria criteria);
}
