package com.masi.production.repository;

import com.masi.production.domain.MachineOperationChecklist;
import com.masi.production.domain.MetalDetectionChecklist;

import java.time.LocalDate;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the MetalDetectionChecklist entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MetalDetectionChecklistRepository
    extends ReactiveCrudRepository<MetalDetectionChecklist, UUID>, MetalDetectionChecklistRepositoryInternal {
    Flux<MetalDetectionChecklist> findAllBy(Pageable pageable);

    @Query("SELECT * FROM metal_detection_checklist entity WHERE entity.work_item_id = :id")
    Flux<MetalDetectionChecklist> findByWorkItem(UUID id);

    @Query("SELECT * FROM metal_detection_checklist entity WHERE entity.work_item_id IS NULL")
    Flux<MetalDetectionChecklist> findAllWhereWorkItemIsNull();

    @Query("SELECT * FROM metal_detection_checklist WHERE work_item_id = :workItemId AND  is_active = true and (:createdFrom is null or created_at >= :createdFrom) and (:createdTo is null or created_at <= :createdTo)")
    Flux<MetalDetectionChecklist> findAllByWorkItemIdAndIsActiveIsTrue(UUID workItemId, LocalDate createdFrom, LocalDate createdTo);

    @Override
    <S extends MetalDetectionChecklist> Mono<S> save(S entity);

    @Override
    Flux<MetalDetectionChecklist> findAll();

    @Override
    Mono<MetalDetectionChecklist> findById(UUID id);

    @Override
    Mono<Void> deleteById(UUID id);
}

interface MetalDetectionChecklistRepositoryInternal {
    <S extends MetalDetectionChecklist> Mono<S> save(S entity);

    Flux<MetalDetectionChecklist> findAllBy(Pageable pageable);

    Flux<MetalDetectionChecklist> findAll();

    Mono<MetalDetectionChecklist> findById(UUID id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<MetalDetectionChecklist> findAllBy(Pageable pageable, Criteria criteria);
}
