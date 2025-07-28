package com.masi.logistics.repository;

import com.masi.logistics.domain.Reimbursement;
import com.masi.logistics.domain.criteria.ReimbursementCriteria;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the Reimbursement entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReimbursementRepository extends ReactiveCrudRepository<Reimbursement, UUID>, ReimbursementRepositoryInternal {
    Flux<Reimbursement> findAllBy(Pageable pageable);

    @Override
    <S extends Reimbursement> Mono<S> save(S entity);

    @Override
    Flux<Reimbursement> findAll();

    @Override
    Mono<Reimbursement> findById(UUID id);

    @Override
    Mono<Void> deleteById(UUID id);

    @Query("SELECT * FROM reimbursement entity WHERE entity.reimbursement_id = :id AND entity.is_deleted = false AND entity.company = :company")
    Flux<Reimbursement> findByReimbursementId(UUID id, String company);

    @Modifying
    @Query("UPDATE reimbursement SET is_deleted = true, deleted_at = NOW(), deleted_by = :deletedBy WHERE reimbursement_id = :id AND company = :company")
    Mono<Void> deleteByReimbursementId(UUID id, String deletedBy, String company);
}

interface ReimbursementRepositoryInternal {
    <S extends Reimbursement> Mono<S> save(S entity);

    Flux<Reimbursement> findAllBy(Pageable pageable);

    Flux<Reimbursement> findAll();

    Mono<Reimbursement> findById(UUID id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Reimbursement> findAllBy(Pageable pageable, Criteria criteria);
    Flux<Reimbursement> findByCriteria(ReimbursementCriteria criteria, Pageable pageable);

    Mono<Long> countByCriteria(ReimbursementCriteria criteria);
}
