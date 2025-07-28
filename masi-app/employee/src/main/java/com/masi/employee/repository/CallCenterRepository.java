package com.masi.employee.repository;

import com.masi.employee.domain.CallCenter;
import com.masi.employee.domain.criteria.CallCenterCriteria;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the CallCenter entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CallCenterRepository extends ReactiveCrudRepository<CallCenter, UUID>, CallCenterRepositoryInternal {
    Flux<CallCenter> findAllBy(Pageable pageable);

    @Override
    <S extends CallCenter> Mono<S> save(S entity);

    @Override
    Flux<CallCenter> findAll();

    @Override
    Mono<CallCenter> findById(UUID id);

    @Override
    Mono<Void> deleteById(UUID id);

    @Query("UPDATE call_center SET is_deleted = true WHERE id = :id")
    Mono<Void> changeIsDeletedById(UUID id);

}

interface CallCenterRepositoryInternal {
    <S extends CallCenter> Mono<S> save(S entity);

    Flux<CallCenter> findAllBy(Pageable pageable);

    Flux<CallCenter> findAll();

    Mono<CallCenter> findById(UUID id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<CallCenter> findAllBy(Pageable pageable, Criteria criteria);
    Flux<CallCenter> findByCriteria(CallCenterCriteria criteria, Pageable pageable);

    Mono<Long> countByCriteria(CallCenterCriteria criteria);
}
