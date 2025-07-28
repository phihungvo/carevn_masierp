package com.masi.employee.repository;

import com.masi.employee.domain.Workspace;
import com.masi.employee.service.dto.WorkspaceRO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Spring Data R2DBC repository for the Workspace entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WorkspaceRepository extends ReactiveCrudRepository<Workspace, UUID>, WorkspaceRepositoryInternal {
    @Override
    <S extends Workspace> Mono<S> save(S entity);

    @Override
    Flux<Workspace> findAll();


    Mono<Workspace> findById(UUID id);

    Mono<Workspace> findByIdAndIsActiveTrue(UUID id);

    Mono<Workspace> findByName(String name);

    Mono<Workspace> findByNameAndIsActiveTrue(String name);

    Mono<Void> deleteByName(String name);

    Mono<Boolean> existsByIdAndIsActiveTrue(UUID id);

    Mono<Boolean> existsById(UUID id);

    Mono<Boolean> existsByNameAndCompanyAndIsActiveTrue(String name,String company);

    Mono<Boolean> existsByName(String name);

    Mono<Workspace> findFirstByNameAndCompany(String name, String company);
}

interface WorkspaceRepositoryInternal {
    <S extends Workspace> Mono<S> save(S entity);

    Flux<Workspace> findAllBy(Pageable pageable);

    Flux<Workspace> findAll();

    Flux<Workspace> findAllActiveBy(Pageable pageable, WorkspaceRO ro);

    Mono<Long> countAllActiveBy(WorkspaceRO ro);
}
