package com.masi.sale.repository;

import com.masi.sale.domain.ContractProduct;

import java.util.UUID;

import com.masi.sale.domain.ContractProductFull;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the ContractProduct entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ContractProductRepository extends ReactiveCrudRepository<ContractProduct, UUID>, ContractProductRepositoryInternal {
    Flux<ContractProduct> findAllBy(Pageable pageable);

    @Override
    <S extends ContractProduct> Mono<S> save(S entity);

    @Override
    Flux<ContractProduct> findAll();

    @Override
    Mono<ContractProduct> findById(UUID id);

    @Override
    Mono<Void> deleteById(UUID id);

    @Query("SELECT c.*, m.name AS product_name " +
            "FROM product m " +
            "JOIN contract_product c ON c.id_product = m.id " +
            "WHERE c.id_contract = :idContract AND c.is_deleted = false")
    Flux<ContractProductFull> findAllByIdContract(UUID idContract);

    @Query("UPDATE contract_product SET is_deleted = true WHERE id_contract = :idContract")
    Mono<Void> deleteByIdContract(UUID idContract);
}

interface ContractProductRepositoryInternal {
    <S extends ContractProduct> Mono<S> save(S entity);

    Flux<ContractProduct> findAllBy(Pageable pageable);

    Flux<ContractProduct> findAll();

    Mono<ContractProduct> findById(UUID id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<ContractProduct> findAllBy(Pageable pageable, Criteria criteria);
}
