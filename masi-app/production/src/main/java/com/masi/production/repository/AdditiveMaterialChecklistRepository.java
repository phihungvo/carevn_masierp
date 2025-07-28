package com.masi.production.repository;

import com.masi.production.domain.AdditiveMaterialChecklist;

import java.time.LocalDate;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the AdditiveMaterialChecklist entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AdditiveMaterialChecklistRepository
    extends ReactiveCrudRepository<AdditiveMaterialChecklist, UUID>, AdditiveMaterialChecklistRepositoryInternal {
    Flux<AdditiveMaterialChecklist> findAllBy(Pageable pageable);

    @Query("SELECT * FROM additive_material_checklist entity WHERE entity.work_item_id = :id")
    Flux<AdditiveMaterialChecklist> findByWorkItem(UUID id);

    @Query("SELECT * FROM additive_material_checklist entity WHERE entity.work_item_id IS NULL")
    Flux<AdditiveMaterialChecklist> findAllWhereWorkItemIsNull();


    @Query("SELECT * FROM additive_material_checklist WHERE work_item_id = :workItemId AND is_active = true and (:createdFrom is null or created_at >= :createdFrom) and (:createdTo is null or created_at <= :createdTo)")
    Flux<AdditiveMaterialChecklist> findAllByWorkItemIdAndIsActiveIsTrue(UUID workItemId,LocalDate createdFrom, LocalDate createdTo);

    @Override
    <S extends AdditiveMaterialChecklist> Mono<S> save(S entity);

    @Override
    Flux<AdditiveMaterialChecklist> findAll();



    @Override
    Mono<AdditiveMaterialChecklist> findById(UUID id);

    @Override
    Mono<Void> deleteById(UUID id);
}

interface AdditiveMaterialChecklistRepositoryInternal {
    <S extends AdditiveMaterialChecklist> Mono<S> save(S entity);

    Flux<AdditiveMaterialChecklist> findAllBy(Pageable pageable);

    Flux<AdditiveMaterialChecklist> findAll();

    Mono<AdditiveMaterialChecklist> findById(UUID id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<AdditiveMaterialChecklist> findAllBy(Pageable pageable, Criteria criteria);
}
