package com.masi.logistics.repository;

import com.masi.logistics.domain.SupplierContractDetail;
import com.masi.logistics.domain.criteria.SupplierContractDetailCriteria;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the SupplierContractDetail entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SupplierContractDetailRepository
    extends ReactiveCrudRepository<SupplierContractDetail, UUID>, SupplierContractDetailRepositoryInternal {
    Flux<SupplierContractDetail> findAllBy(Pageable pageable);

    @Query("SELECT * FROM supplier_contract_detail entity WHERE entity.supplier_contract_id = :id AND entity.is_deleted = false AND entity.deleted_by IS NULL AND entity.deleted_at IS NULL")
    Flux<SupplierContractDetail> findBySupplierContract(UUID id);

    @Query("SELECT * FROM supplier_contract_detail entity WHERE entity.supplier_contract_id IN (:id) AND entity.is_deleted = false AND entity.deleted_by IS NULL AND entity.deleted_at IS NULL AND company = :company")
    default Flux<SupplierContractDetail> findBySupplierContract(List<UUID> id, String company, Pageable pageable) {
        if (id == null || id.isEmpty()) {
            return Flux.empty();
        }
        return findBySupplierContractInternal(id, company, pageable);
    }

    @Query("SELECT * FROM supplier_contract_detail entity WHERE entity.supplier_contract_id IN (:id) AND entity.is_deleted = false AND entity.deleted_by IS NULL AND entity.deleted_at IS NULL AND company = :company")
    Flux<SupplierContractDetail> findBySupplierContractInternal(List<UUID> id, String company, Pageable pageable);

    @Query("SELECT * FROM supplier_contract_detail entity WHERE entity.supplier_contract_id IS NULL")
    Flux<SupplierContractDetail> findAllWhereSupplierContractIsNull();

    @Override
    <S extends SupplierContractDetail> Mono<S> save(S entity);

    @Override
    Flux<SupplierContractDetail> findAll();

    @Override
    Mono<SupplierContractDetail> findById(UUID id);

    @Override
    Mono<Void> deleteById(UUID id);

    @Modifying
    @Query("DELETE FROM supplier_contract_detail WHERE contract_id = :contractId")
    Mono<Void> deleteByContract(UUID contractId);

    @Modifying
    @Query("UPDATE supplier_contract_detail SET is_deleted = TRUE, deleted_at = NOW(), deleted_by = :deletedBy WHERE supplier_contract_id = :id AND is_deleted = FALSE AND deleted_at IS NULL AND deleted_by IS NULL AND company = :company")
    Mono<Integer> deleteBySupplierContract(UUID id, String deletedBy, String company);
}

interface SupplierContractDetailRepositoryInternal {
    <S extends SupplierContractDetail> Mono<S> save(S entity);

    Flux<SupplierContractDetail> findAllBy(Pageable pageable);

    Flux<SupplierContractDetail> findAll();

    Mono<SupplierContractDetail> findById(UUID id);

    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<SupplierContractDetail> findAllBy(Pageable pageable, Criteria criteria);
    Flux<SupplierContractDetail> findByCriteria(SupplierContractDetailCriteria criteria, Pageable pageable);

    Flux<SupplierContractDetail> findByListSupplierContractId(List<UUID> id, String company, Pageable pageable);


    Mono<Long> countByCriteria(SupplierContractDetailCriteria criteria);
}
