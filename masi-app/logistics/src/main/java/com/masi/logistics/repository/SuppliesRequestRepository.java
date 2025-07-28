package com.masi.logistics.repository;

import com.masi.logistics.domain.SuppliesRequest;
import com.masi.logistics.domain.criteria.SuppliesRequestCriteria;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the SuppliesRequest entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SuppliesRequestRepository extends ReactiveCrudRepository<SuppliesRequest, UUID>, SuppliesRequestRepositoryInternal {
    Flux<SuppliesRequest> findAllBy(Pageable pageable);

    @Override
    <S extends SuppliesRequest> Mono<S> save(S entity);

    @Override
    Flux<SuppliesRequest> findAll();

    @Override
    Mono<SuppliesRequest> findById(UUID id);

    @Query("SELECT * FROM supplies_request entity WHERE entity.id = :id AND entity.company = :company AND entity.is_deleted = false")
    Mono<SuppliesRequest> findById(UUID id, String company);

    @Override
    Mono<Void> deleteById(UUID id);

    @Query("UPDATE supplies_request SET is_deleted = true, deleted_by = :deletedBy, deleted_at = NOW() WHERE id = :id AND is_deleted = false AND company = :company")
    Mono<Void> delete(UUID id, String company, String deletedBy);
}

interface SuppliesRequestRepositoryInternal {
    <S extends SuppliesRequest> Mono<S> save(S entity);

    Flux<SuppliesRequest> findAllBy(Pageable pageable);

    Flux<SuppliesRequest> findAll();

    Mono<SuppliesRequest> findById(UUID id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<SuppliesRequest> findAllBy(Pageable pageable, Criteria criteria);
    Flux<SuppliesRequest> findByCriteria(SuppliesRequestCriteria criteria, Pageable pageable);

    Mono<Long> countByCriteria(SuppliesRequestCriteria criteria);
}
