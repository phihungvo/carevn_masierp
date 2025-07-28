package com.masi.sale.repository;

import com.masi.sale.domain.Contract;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.UUID;

import com.masi.sale.domain.enumeration.ContractStatus;
import com.masi.sale.service.dto.ContractRO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the Contract entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ContractRepository extends ReactiveCrudRepository<Contract, UUID>, ContractRepositoryInternal {
    Flux<Contract> findAllBy(Pageable pageable);

    @Override
    <S extends Contract> Mono<S> save(S entity);

    @Override
    Flux<Contract> findAll();

    @Override
    Mono<Contract> findById(UUID id);

    Mono<Contract> findByIdAndIsActive(UUID id, boolean isActive);


    @Override
    Mono<Void> deleteById(UUID id);

    @Query("UPDATE contract SET is_active = false WHERE id = :id")
    Mono<Void> softDeleteById(UUID id);

    @Query("UPDATE contract SET is_active = :isActive, last_updated = :lastUpdate WHERE id = :id")
    Mono<Void> changeIsActiveByIdAndUpdateLast(UUID id, ZonedDateTime lastUpdate, Boolean isActive);


    @Query("UPDATE contract SET status = :contractStatus, last_updated = :lastUpdate WHERE id = :id")
    Mono<Void> changeStatusByIdAndStatus(UUID id, ContractStatus contractStatus, ZonedDateTime lastUpdate);

    @Query("UPDATE contract SET status = 'LIQUIDATED', approval_sign_file = :approvalSign, last_updated = :lastUpdate WHERE id = :id")
    Mono<Void> consentToReviewContract(UUID id, String approvalSign, ZonedDateTime lastUpdate);

    @Query("UPDATE contract SET status = 'APPROVED',reject_note =:rejectNote, last_updated = :lastUpdate WHERE id = :id")
    Mono<Void> refusalOfReviewContract(UUID id, String rejectNote, ZonedDateTime lastUpdate);

    @Modifying
    @Query("UPDATE contract SET is_deleted = true WHERE is_active = false AND last_updated <= :thirtyDaysAgo")
    Flux<Contract> updateInactiveContracts(ZonedDateTime thirtyDaysAgo);


    @Query("""
                SELECT c.* 
                FROM contract c 
                WHERE c.is_active = true 
                  AND c.is_deleted = false 
                  AND (
                      (:deliveryDate IS NULL OR (:deliveryDate BETWEEN c.contract_valid_from AND c.contract_valid_to)) 
                      OR (:expectedReceiveDate IS NULL OR (:expectedReceiveDate BETWEEN c.contract_valid_from AND c.contract_valid_to))
                  )
            """)
    Flux<Contract> findAllByDate(LocalDate deliveryDate, LocalDate expectedReceiveDate);
}

interface ContractRepositoryInternal {
    <S extends Contract> Mono<S> save(S entity);

    Flux<Contract> findAllBy(Pageable pageable);

    Flux<Contract> findAll();

    Mono<Contract> findById(UUID id);

    Flux<Contract> findAllByFilter(Pageable pageable, ContractRO ro);

    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Contract> findAllBy(Pageable pageable, Criteria criteria);
    Mono<Long> countByFilter(ContractRO ro);
}
