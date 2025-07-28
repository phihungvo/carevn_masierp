package com.masi.logistics.repository;

import com.masi.logistics.domain.SuppliesItem;
import com.masi.logistics.domain.criteria.SuppliesItemCriteria;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the SuppliesItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SuppliesItemRepository extends ReactiveCrudRepository<SuppliesItem, UUID>, SuppliesItemRepositoryInternal {
    Flux<SuppliesItem> findAllBy(Pageable pageable);

    @Override
    <S extends SuppliesItem> Mono<S> save(S entity);

    @Override
    Flux<SuppliesItem> findAll();

    @Override
    Mono<SuppliesItem> findById(UUID id);

    @Override
    Mono<Void> deleteById(UUID id);

    @Modifying
    @Query("UPDATE supplies_item SET is_deleted = true, deleted_by = :deletedBy, deleted_at = NOW() WHERE id_supplies_request = :id AND is_deleted = false AND company = :company")
    Mono<Void> deleteBySuppliesRequestId(UUID id, String company, String deletedBy);
}

interface SuppliesItemRepositoryInternal {
    <S extends SuppliesItem> Mono<S> save(S entity);

    Flux<SuppliesItem> findAllBy(Pageable pageable);

    Flux<SuppliesItem> findAll();

    Mono<SuppliesItem> findById(UUID id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<SuppliesItem> findAllBy(Pageable pageable, Criteria criteria);
    Flux<SuppliesItem> findByCriteria(SuppliesItemCriteria criteria, Pageable pageable);

    Mono<Long> countByCriteria(SuppliesItemCriteria criteria);

    Flux<SuppliesItem> findAllByIdSuppliesRequestAndCompanyAndIsDeletedIsFalse(UUID idSuppliesRequest, String company);
}
