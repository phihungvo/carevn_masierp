package com.masi.sale.repository;

import com.masi.sale.domain.ContractFile;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import com.masi.sale.domain.enumeration.ContractStatus;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the ContractFile entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ContractFileRepository extends ReactiveCrudRepository<ContractFile, UUID>, ContractFileRepositoryInternal {
    Flux<ContractFile> findAllBy(Pageable pageable);

    @Query("SELECT * FROM contract_file entity WHERE entity.contract_id = :id")
    Flux<ContractFile> findByContract(UUID id);

    @Query("SELECT * FROM contract_file entity WHERE entity.contract_id IS NULL")
    Flux<ContractFile> findAllWhereContractIsNull();

    @Override
    <S extends ContractFile> Mono<S> save(S entity);

    @Override
    Flux<ContractFile> findAll();

    @Override
    Mono<ContractFile> findById(UUID id);

    @Override
    Mono<Void> deleteById(UUID id);

    //    @Query("SELECT * FROM contract_file WHERE contract_id = :id AND is_deleted = false ORDER BY last_updated DESC LIMIT 3")
    @Query("SELECT * FROM contract_file WHERE contract_id = :id AND is_deleted = false ORDER BY last_updated")
    Flux<ContractFile> findTop3ByContractIdOrderByLastUpdatedDesc(UUID id);

    Mono<Boolean> existsByContractFilePath(String contractFilePath);

    @Query("UPDATE contract_file SET is_deleted = true WHERE contract_id = :contractId AND is_deleted = false")
    Mono<Void> deleteByContractId(UUID contractId);
}

interface ContractFileRepositoryInternal {
    <S extends ContractFile> Mono<S> save(S entity);

    Flux<ContractFile> findAllBy(Pageable pageable);

    Flux<ContractFile> findAll();

    Mono<ContractFile> findById(UUID id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<ContractFile> findAllBy(Pageable pageable, Criteria criteria);
}
