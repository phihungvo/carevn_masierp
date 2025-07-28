package com.masi.employee.repository;

import com.masi.employee.domain.TypeRequestApprovalPages;
import com.masi.employee.domain.criteria.TypeRequestApprovalPagesCriteria;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the TypeRequestApprovalPages entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TypeRequestApprovalPagesRepository
    extends ReactiveCrudRepository<TypeRequestApprovalPages, UUID>, TypeRequestApprovalPagesRepositoryInternal {
    Flux<TypeRequestApprovalPages> findAllBy(Pageable pageable);

    @Override
    <S extends TypeRequestApprovalPages> Mono<S> save(S entity);

    @Override
    Flux<TypeRequestApprovalPages> findAll();

    @Override
    Mono<TypeRequestApprovalPages> findById(UUID id);

    @Override
    Mono<Void> deleteById(UUID id);
}

interface TypeRequestApprovalPagesRepositoryInternal {
    <S extends TypeRequestApprovalPages> Mono<S> save(S entity);

    Flux<TypeRequestApprovalPages> findAllBy(Pageable pageable);

    Flux<TypeRequestApprovalPages> findAll();

    Mono<TypeRequestApprovalPages> findById(UUID id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<TypeRequestApprovalPages> findAllBy(Pageable pageable, Criteria criteria);
    Flux<TypeRequestApprovalPages> findByCriteria(TypeRequestApprovalPagesCriteria criteria, Pageable pageable);

    Mono<Long> countByCriteria(TypeRequestApprovalPagesCriteria criteria);
}
