package com.masi.production.repository;

import com.masi.production.domain.WorkItem;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the WorkItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WorkItemRepository extends ReactiveCrudRepository<WorkItem, UUID>, WorkItemRepositoryInternal {
    Flux<WorkItem> findAllBy(Pageable pageable);
    

    @Query("SELECT * FROM work_item entity WHERE entity.id not in (select work_order_id from work_order)")
    Flux<WorkItem> findAllWhereWorkOrderIsNull();

    @Override
    <S extends WorkItem> Mono<S> save(S entity);

    @Override
    Flux<WorkItem> findAll();

    @Override
    Mono<WorkItem> findById(UUID id);

    @Override
    Mono<Void> deleteById(UUID id);
}

interface WorkItemRepositoryInternal {
    <S extends WorkItem> Mono<S> save(S entity);

    Flux<WorkItem> findAllBy(Pageable pageable);

    Flux<WorkItem> findAll();

    Mono<WorkItem> findById(UUID id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<WorkItem> findAllBy(Pageable pageable, Criteria criteria);
}
