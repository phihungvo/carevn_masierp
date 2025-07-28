package com.masi.employee.repository;

import com.masi.employee.domain.Uniform;

import java.util.List;
import java.util.UUID;

import com.masi.employee.service.dto.UniformQuery;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the Uniform entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UniformRepository extends ReactiveCrudRepository<Uniform, UUID>, UniformRepositoryInternal {
    Flux<Uniform> findAllBy(Pageable pageable);

    @Override
    <S extends Uniform> Mono<S> save(S entity);

    @Override
    Flux<Uniform> findAll();

    @Override
    Mono<Uniform> findById(UUID id);

    @Override
    Mono<Void> deleteById(UUID id);


    Flux<Uniform> findAllByCompanyAndDeleteAtIsNull(Pageable pageable, String company);

    @Query("SELECT * FROM uniform WHERE id IN (:listUniformId) AND company = :company AND delete_at IS NULL ORDER BY name ASC")
    Flux<Uniform> getIdAndNameUniformByCompanyAndDeleteAtIsNull(String company, List<UUID> listUniformId);

    @Query("SELECT * FROM uniform WHERE id = :id AND company = :company AND delete_at IS NULL ORDER BY name ASC")
    Mono<Uniform> getUniformByIdAndCompanyAndDeleteAtIsNull(String company, UUID id);

    @Query("SELECT * FROM uniform WHERE company = :company AND delete_at IS NULL ORDER BY name ASC")
    Flux<Uniform> getUniformByCompanyAndDeleteAtIsNull(String company);

}

interface UniformRepositoryInternal {
    <S extends Uniform> Mono<S> save(S entity);

    Flux<Uniform> findAllByFilter(Pageable pageable,UniformQuery query);

    Mono<Long> countByFilter(UniformQuery ro);

    Mono<Uniform> findById(UUID id);
    // this is not supported at the moment because of
    // https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Uniform> findAllBy(Pageable pageable, Criteria criteria);
}
