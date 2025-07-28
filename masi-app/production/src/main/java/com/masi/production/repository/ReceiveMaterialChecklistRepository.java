package com.masi.production.repository;

import com.masi.production.domain.MixingReportChecklist;
import com.masi.production.domain.ReceiveMaterialChecklist;

import java.time.LocalDate;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the ReceiveMaterialChecklist entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReceiveMaterialChecklistRepository
    extends ReactiveCrudRepository<ReceiveMaterialChecklist, UUID>, ReceiveMaterialChecklistRepositoryInternal {
    Flux<ReceiveMaterialChecklist> findAllBy(Pageable pageable);

    @Query("SELECT * FROM receive_material_checklist entity WHERE entity.work_item_id = :id")
    Flux<ReceiveMaterialChecklist> findByWorkItem(UUID id);

    @Query("SELECT * FROM receive_material_checklist entity WHERE entity.work_item_id IS NULL")
    Flux<ReceiveMaterialChecklist> findAllWhereWorkItemIsNull();

    @Query("SELECT * FROM receive_material_checklist WHERE work_item_id = :workItemId AND  is_active = true and (:createdFrom is null or created_at >= :createdFrom) and (:createdTo is null or created_at <= :createdTo)")
    Flux<ReceiveMaterialChecklist> findAllByWorkItemIdAndIsActiveIsTrue(UUID workItemId, LocalDate createdFrom, LocalDate createdTo);

    @Override
    <S extends ReceiveMaterialChecklist> Mono<S> save(S entity);

    @Override
    Flux<ReceiveMaterialChecklist> findAll();

    @Override
    Mono<ReceiveMaterialChecklist> findById(UUID id);

    @Override
    Mono<Void> deleteById(UUID id);
}

interface ReceiveMaterialChecklistRepositoryInternal {
    <S extends ReceiveMaterialChecklist> Mono<S> save(S entity);

    Flux<ReceiveMaterialChecklist> findAllBy(Pageable pageable);

    Flux<ReceiveMaterialChecklist> findAll();

    Mono<ReceiveMaterialChecklist> findById(UUID id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<ReceiveMaterialChecklist> findAllBy(Pageable pageable, Criteria criteria);
}
