package com.masi.production.repository;

import com.masi.production.domain.ReceiveMaterialChecklist;
import com.masi.production.domain.SteamingProcessChecklist;

import java.time.LocalDate;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the SteamingProcessChecklist entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SteamingProcessChecklistRepository
    extends ReactiveCrudRepository<SteamingProcessChecklist, UUID>, SteamingProcessChecklistRepositoryInternal {
    Flux<SteamingProcessChecklist> findAllBy(Pageable pageable);

    @Query("SELECT * FROM steaming_process_checklist entity WHERE entity.work_item_id = :id")
    Flux<SteamingProcessChecklist> findByWorkItem(UUID id);

    @Query("SELECT * FROM steaming_process_checklist entity WHERE entity.work_item_id IS NULL")
    Flux<SteamingProcessChecklist> findAllWhereWorkItemIsNull();

    @Query("SELECT * FROM steaming_process_checklist WHERE work_item_id = :workItemId AND  is_active = true and (:createdFrom is null or created_at >= :createdFrom) and (:createdTo is null or created_at <= :createdTo)")
    Flux<SteamingProcessChecklist> findAllByWorkItemIdAndIsActiveIsTrue(UUID workItemId, LocalDate createdFrom, LocalDate createdTo);

    @Override
    <S extends SteamingProcessChecklist> Mono<S> save(S entity);

    @Override
    Flux<SteamingProcessChecklist> findAll();

    @Override
    Mono<SteamingProcessChecklist> findById(UUID id);

    @Override
    Mono<Void> deleteById(UUID id);
}

interface SteamingProcessChecklistRepositoryInternal {
    <S extends SteamingProcessChecklist> Mono<S> save(S entity);

    Flux<SteamingProcessChecklist> findAllBy(Pageable pageable);

    Flux<SteamingProcessChecklist> findAll();

    Mono<SteamingProcessChecklist> findById(UUID id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<SteamingProcessChecklist> findAllBy(Pageable pageable, Criteria criteria);
}
