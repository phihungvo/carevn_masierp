package com.masi.employee.repository;

import com.masi.employee.domain.Uniform;
import com.masi.employee.domain.UniformRelease;
import com.masi.employee.service.dto.UniformReleaseGetListDTO;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the UniformRelease entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UniformReleaseRepository
    extends ReactiveCrudRepository<UniformRelease, UUID>, UniformReleaseRepositoryInternal {
    Flux<UniformRelease> findAllBy(Pageable pageable);

    @Override
    <S extends UniformRelease> Mono<S> save(S entity);

    @Override
    Flux<UniformRelease> findAll();

    @Override
    Mono<UniformRelease> findById(UUID id);

    @Query("SELECT * FROM uniform_release WHERE employee_id = :employeeId and is_returned = false and delete_at is null and type='SUPPORT' ORDER BY create_at DESC LIMIT 1")
    Mono<UniformRelease> findLatestReleaseForEmployee(UUID employeeId);

    @Query("SELECT * FROM uniform_release WHERE id = :id AND company = :company AND is_returned = false AND delete_at is null AND type='SUPPORT'")
    Mono<UniformRelease> findNotDeletedById(UUID id, String company);


    @Query("SELECT uniform_release.* FROM uniform_release JOIN  uniform_form_detail ud on ud.uniform_release_id = uniform_release.id WHERE ud.uniform_id = :uniformId and employee_id = :employeeId and is_returned = false and uniform_release.delete_at is null and uniform_release.type='SUPPORT' ORDER BY uniform_release.date, uniform_release.create_at")
    Flux<UniformRelease> findOldestReleaseForEmployee(UUID employeeId,UUID uniformId);

    @Override
    Mono<Void> deleteById(UUID id);
    // sum tâất ca realease ma co type la SUPPORT va is_returned = false
    @Query("SELECT SUM(uniform_release.remaining) FROM uniform_release JOIN uniform_form_detail ON uniform_release.id = uniform_form_detail.uniform_release_id WHERE employee_id = :employeeId and uniform_release.is_returned = false and uniform_release.delete_at is null and uniform_release.type='SUPPORT' and uniform_form_detail.uniform_id = :uniformId")
    Mono<Long> countTotalRemaining(UUID employeeId,UUID uniformId);
}

interface UniformReleaseRepositoryInternal {
    <S extends UniformRelease> Mono<S> save(S entity);

    Flux<UniformRelease> findAllBy(Pageable pageable);

    Flux<UniformRelease> findAll();

    Mono<UniformRelease> findById(UUID id);

    Mono<Long> countAllByQuery(UniformReleaseGetListDTO uniformReleaseGetListDTO,
                               List<Uniform> listUniform);

    Flux<UniformRelease> findAllWithQuery(Pageable pageable, UniformReleaseGetListDTO uniformReleaseGetListDTO,
                                          List<Uniform> listUniform);
    // this is not supported at the moment because of
    // https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<UniformRelease> findAllBy(Pageable pageable, Criteria criteria);
}
