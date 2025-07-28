package com.masi.production.repository;

import com.masi.production.domain.AdditiveMaterialChecklist;
import com.masi.production.domain.MachineOperationChecklist;

import java.time.LocalDate;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the MachineOperationChecklist entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MachineOperationChecklistRepository
    extends ReactiveCrudRepository<MachineOperationChecklist, UUID>, MachineOperationChecklistRepositoryInternal {
    Flux<MachineOperationChecklist> findAllBy(Pageable pageable);

    @Query("SELECT * FROM machine_operation_checklist entity WHERE entity.work_item_id = :id")
    Flux<MachineOperationChecklist> findByWorkItem(UUID id);

    @Query("SELECT * FROM machine_operation_checklist entity WHERE entity.work_item_id IS NULL")
    Flux<MachineOperationChecklist> findAllWhereWorkItemIsNull();

    @Query("SELECT * FROM machine_operation_checklist WHERE work_item_id = :workItemId AND  is_active = true and (:createdFrom is null or created_at >= :createdFrom) and (:createdTo is null or created_at <= :createdTo)")
    Flux<MachineOperationChecklist> findAllByWorkItemIdAndIsActiveIsTrue(UUID workItemId, LocalDate createdFrom, LocalDate createdTo);

    @Override
    <S extends MachineOperationChecklist> Mono<S> save(S entity);

    @Override
    Flux<MachineOperationChecklist> findAll();

    @Override
    Mono<MachineOperationChecklist> findById(UUID id);

    @Override
    Mono<Void> deleteById(UUID id);
}

interface MachineOperationChecklistRepositoryInternal {
    <S extends MachineOperationChecklist> Mono<S> save(S entity);

    Flux<MachineOperationChecklist> findAllBy(Pageable pageable);

    Flux<MachineOperationChecklist> findAll();

    Mono<MachineOperationChecklist> findById(UUID id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<MachineOperationChecklist> findAllBy(Pageable pageable, Criteria criteria);
}
