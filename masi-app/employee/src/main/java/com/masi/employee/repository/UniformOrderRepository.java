package com.masi.employee.repository;

import com.masi.employee.domain.UniformOrder;
import com.masi.employee.service.dto.UniformOrderGetListDTO;

import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the UniformOrder entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UniformOrderRepository
        extends ReactiveCrudRepository<UniformOrder, UUID>, UniformOrderRepositoryInternal {
    Flux<UniformOrder> findAllBy(Pageable pageable);

    @Override
    <S extends UniformOrder> Mono<S> save(S entity);

    @Override
    Flux<UniformOrder> findAll();

    @Override
    Mono<UniformOrder> findById(UUID id);

    @Query("SELECT * FROM uniform_order e WHERE e.id = :id AND e.delete_at IS NULL AND e.delete_by IS NULL")
    Mono<UniformOrder> findByIdAndDeleteAtIsNullAndDeleteByIsNull(UUID id);

    @Query("UPDATE uniform_order SET delete_at = NOW(), delete_by = :deleteBy WHERE id = :id")
    Mono<UniformOrder> deleteById(UUID id, String deleteBy);

    @Override
    Mono<Void> deleteById(UUID id);
}

interface UniformOrderRepositoryInternal {
    <S extends UniformOrder> Mono<S> save(S entity);

    Flux<UniformOrder> findAllBy(Pageable pageable);

    Flux<UniformOrder> findAll();

    Mono<UniformOrder> findById(UUID id);
    // this is not supported at the moment because of
    // https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<UniformOrder> findAllBy(Pageable pageable, Criteria criteria);

    Flux<UniformOrder> findAllByQuery(Pageable pageable, UniformOrderGetListDTO uniformOrderGetListDTO);

    Mono<Long> countAllByQuery(UniformOrderGetListDTO uniformOrderGetListDTO);
}
