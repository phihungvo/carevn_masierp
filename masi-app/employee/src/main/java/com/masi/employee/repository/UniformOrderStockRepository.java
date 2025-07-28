package com.masi.employee.repository;

import com.masi.employee.domain.Uniform;
import com.masi.employee.domain.UniformOrderStock;

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
 * Spring Data R2DBC repository for the UniformOrderStock entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UniformOrderStockRepository extends ReactiveCrudRepository<UniformOrderStock, UUID>, UniformOrderStockRepositoryInternal {
    Flux<UniformOrderStock> findAllBy(Pageable pageable);

    @Override
    <S extends UniformOrderStock> Mono<S> save(S entity);

    @Override
    Flux<UniformOrderStock> findAll();

    @Override
    Mono<UniformOrderStock> findById(UUID id);

    @Override
    Mono<Void> deleteById(UUID id);

    @Query("SELECT * FROM uniform_order_stock entity WHERE entity.uniform_order_id = :uniformOrderId AND entity.company = :company AND entity.delete_at IS NULL AND entity.delete_by IS NULL")
    Flux<UniformOrderStock> findAllByUniformOrderId(UUID uniformOrderId, String company);

    @Query("SELECT * FROM uniform_order_stock entity WHERE entity.id = :id AND entity.company = :company AND entity.delete_at IS NULL AND entity.delete_by IS NULL")
    Mono<UniformOrderStock> findByIdAndCompany(UUID id, String company);
}

interface UniformOrderStockRepositoryInternal {
    <S extends UniformOrderStock> Mono<S> save(S entity);

    Flux<UniformOrderStock> findAllBy(Pageable pageable);

    Flux<UniformOrderStock> findAll();

    Mono<UniformOrderStock> findById(UUID id);

    Flux<UniformOrderStock> createQueryCondition(UUID uniformOrderId, String company, List<Uniform> uniforms);

    Flux<UniformOrderStock> findAllBy(Pageable pageable, ZonedDateTime startDate, ZonedDateTime endDate, String company);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<UniformOrderStock> findAllBy(Pageable pageable, Criteria criteria);
}
