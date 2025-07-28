package com.masi.production.repository;

import com.masi.production.domain.MetalDetectionChecklist;
import com.masi.production.domain.MixingReportChecklist;

import java.time.LocalDate;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the MixingReportChecklist entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MixingReportChecklistRepository
    extends ReactiveCrudRepository<MixingReportChecklist, UUID>, MixingReportChecklistRepositoryInternal {
    Flux<MixingReportChecklist> findAllBy(Pageable pageable);

    @Query("SELECT * FROM mixing_report_checklist entity WHERE entity.work_item_id = :id")
    Flux<MixingReportChecklist> findByWorkItem(UUID id);

    @Query("SELECT * FROM mixing_report_checklist entity WHERE entity.work_item_id IS NULL")
    Flux<MixingReportChecklist> findAllWhereWorkItemIsNull();

    @Query("SELECT * FROM mixing_report_checklist WHERE work_item_id = :workItemId AND  is_active = true and (:createdFrom is null or created_at >= :createdFrom) and (:createdTo is null or created_at <= :createdTo)")
    Flux<MixingReportChecklist> findAllByWorkItemIdAndIsActiveIsTrue(UUID workItemId, LocalDate createdFrom, LocalDate createdTo);

    @Override
    <S extends MixingReportChecklist> Mono<S> save(S entity);

    @Override
    Flux<MixingReportChecklist> findAll();

    @Override
    Mono<MixingReportChecklist> findById(UUID id);

    @Override
    Mono<Void> deleteById(UUID id);
}

interface MixingReportChecklistRepositoryInternal {
    <S extends MixingReportChecklist> Mono<S> save(S entity);

    Flux<MixingReportChecklist> findAllBy(Pageable pageable);

    Flux<MixingReportChecklist> findAll();

    Mono<MixingReportChecklist> findById(UUID id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<MixingReportChecklist> findAllBy(Pageable pageable, Criteria criteria);
}
