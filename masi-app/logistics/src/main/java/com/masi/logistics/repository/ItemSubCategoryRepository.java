package com.masi.logistics.repository;

import com.masi.logistics.domain.ItemSubCategory;
import com.masi.logistics.domain.criteria.ItemSubCategoryCriteria;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the ItemSubCategory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ItemSubCategoryRepository extends ReactiveCrudRepository<ItemSubCategory, UUID>, ItemSubCategoryRepositoryInternal {
    Flux<ItemSubCategory> findAllBy(Pageable pageable);

    @Override
    <S extends ItemSubCategory> Mono<S> save(S entity);

    @Override
    Flux<ItemSubCategory> findAll();

    @Override
    Mono<ItemSubCategory> findById(UUID id);

    @Override
    Mono<Void> deleteById(UUID id);
}

interface ItemSubCategoryRepositoryInternal {
    <S extends ItemSubCategory> Mono<S> save(S entity);

    Flux<ItemSubCategory> findAllBy(Pageable pageable);

    Flux<ItemSubCategory> findAll();

    Mono<ItemSubCategory> findById(UUID id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<ItemSubCategory> findAllBy(Pageable pageable, Criteria criteria);
    Flux<ItemSubCategory> findByCriteria(ItemSubCategoryCriteria criteria, Pageable pageable);

    Mono<Long> countByCriteria(ItemSubCategoryCriteria criteria);
}
