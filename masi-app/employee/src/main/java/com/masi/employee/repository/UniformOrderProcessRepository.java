package com.masi.employee.repository;

import com.masi.employee.domain.UniformOrderProcess;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the UniformOrderProcess entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UniformOrderProcessRepository
        extends ReactiveCrudRepository<UniformOrderProcess, UUID>, UniformOrderProcessRepositoryInternal {
    Flux<UniformOrderProcess> findAllBy(Pageable pageable);

    @Query("SELECT * FROM uniform_order_process entity WHERE entity.uniform_order_id = :id AND delete_at IS NULL AND delete_by IS NULL")
    Flux<UniformOrderProcess> findByUniformOrder(UUID id);

    @Query("SELECT * FROM uniform_order_process entity WHERE entity.uniform_order_id IS NULL")
    Flux<UniformOrderProcess> findAllWhereUniformOrderIsNull();

    @Query("SELECT * FROM uniform_order_process entity WHERE entity.uniform_order_id = :id AND entity.approver_id = :approverId AND delete_at IS NULL AND delete_by IS NULL")
    Mono<UniformOrderProcess> findByUniformOrderAndApprover(UUID id, UUID approverId);

    @Query("SELECT * FROM uniform_order_process entity WHERE entity.uniform_order_id = :id AND entity.status = :status AND delete_at IS NULL AND delete_by IS NULL")
    Flux<UniformOrderProcess> findByUniformOrderAndStatus(UUID id, String status);

    // remove by uniform_order_id and set delete_at and delete_by
    @Modifying
    @Query("UPDATE uniform_order_process SET delete_at = NOW(), delete_by = :deleteBy WHERE uniform_order_id = :id")
    Mono<UniformOrderProcess> deleteByUniformOrder(UUID id, String deleteBy);

    @Override
    <S extends UniformOrderProcess> Mono<S> save(S entity);

    @Override
    Flux<UniformOrderProcess> findAll();

    @Override
    Mono<UniformOrderProcess> findById(UUID id);

    @Override
    Mono<Void> deleteById(UUID id);

}

interface UniformOrderProcessRepositoryInternal {
    <S extends UniformOrderProcess> Mono<S> save(S entity);

    Flux<UniformOrderProcess> findAllBy(Pageable pageable);

    Flux<UniformOrderProcess> findAll();

    Mono<UniformOrderProcess> findById(UUID id);

    Flux<UniformOrderProcess> findAllByOrderId(UUID id);
    // this is not supported at the moment because of
    // https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<UniformOrderProcess> findAllBy(Pageable pageable, Criteria criteria);
}
