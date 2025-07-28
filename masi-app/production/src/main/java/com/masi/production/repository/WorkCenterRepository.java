package com.masi.production.repository;

import com.masi.production.domain.WorkCenter;
import com.masi.production.domain.enumeration.WorkCenterStatusEnum;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the WorkCenter entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WorkCenterRepository extends ReactiveCrudRepository<WorkCenter, UUID>, WorkCenterRepositoryInternal {
    Flux<WorkCenter> findAllBy(Pageable pageable);

    @Override
    <S extends WorkCenter> Mono<S> save(S entity);

    @Override
    Flux<WorkCenter> findAll();

    @Override
    Mono<WorkCenter> findById(UUID id);

    @Override
    Mono<Void> deleteById(UUID id);

    @Query("UPDATE work_center SET is_deleted = true, deleted_at = :deletedAt, deleted_by = :deletedBy WHERE id = :id")
    Mono<Void> softDeleteById(UUID id, String deletedBy, ZonedDateTime deletedAt);

    @Query("SELECT COUNT(code) FROM work_center WHERE code = :code")
    Mono<Long> countAllByCode(String code);
}

interface WorkCenterRepositoryInternal {
    <S extends WorkCenter> Mono<S> save(S entity);

    Flux<WorkCenter> findAllBy(Pageable pageable);

    Flux<WorkCenter> findAll();

    Mono<WorkCenter> findById(UUID id);
    Flux<WorkCenter> GetAllWithFilter(Pageable pageable, String search, List<WorkCenterStatusEnum> status, UUID companyId,String company,String department);
    Mono<Long> CountWithFilter(String search, List<WorkCenterStatusEnum> status, UUID companyId,String company,String department);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<WorkCenter> findAllBy(Pageable pageable, Criteria criteria);
}
