package com.masi.logistics.repository;

import com.masi.logistics.domain.SupplierContract;
import com.masi.logistics.domain.criteria.SupplierContractCriteria;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the SupplierContract entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SupplierContractRepository extends ReactiveCrudRepository<SupplierContract, UUID>, SupplierContractRepositoryInternal {
    Flux<SupplierContract> findAllBy(Pageable pageable);

    @Override
    <S extends SupplierContract> Mono<S> save(S entity);

    @Override
    Flux<SupplierContract> findAll();

    @Override
    Mono<SupplierContract> findById(UUID id);

    @Override
    Mono<Void> deleteById(UUID id);

    @Modifying
    @Query("UPDATE supplier_contract SET status = 'EXPIRED', updated_at = NOW(), updated_by = 'SYSTEM' WHERE end_date < CURRENT_DATE AND status = 'NEW' AND deleted_at IS NULL AND deleted_by IS NULL")
    Mono<Void> updateExpiredContracts();

    @Query("SELECT * FROM supplier_contract WHERE end_date < CURRENT_DATE AND status = 'NEW' AND deleted_at IS NULL AND deleted_by IS NULL")
    Flux<SupplierContract> findSupplierContractByEndDateIsBeforeCurrentDateAndStatusIsNew();
}

interface SupplierContractRepositoryInternal {
    <S extends SupplierContract> Mono<S> save(S entity);

    Flux<SupplierContract> findAllBy(Pageable pageable);

    Flux<SupplierContract> findAll();

    Mono<SupplierContract> findById(UUID id, String company);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<SupplierContract> findAllBy(Pageable pageable, Criteria criteria);
    Flux<SupplierContract> findByCriteria(SupplierContractCriteria criteria, Pageable pageable);

    Mono<Long> countByCriteria(SupplierContractCriteria criteria);
}
