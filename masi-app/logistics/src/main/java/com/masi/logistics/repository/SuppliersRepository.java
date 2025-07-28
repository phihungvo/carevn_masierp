package com.masi.logistics.repository;

import com.masi.logistics.domain.Suppliers;

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
 * Spring Data R2DBC repository for the Suppliers entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SuppliersRepository extends ReactiveCrudRepository<Suppliers, UUID>, SuppliersRepositoryInternal {
    Flux<Suppliers> findAllBy(Pageable pageable);

    @Query("SELECT * FROM suppliers entity WHERE entity.supplier_group_id = :id")
    Flux<Suppliers> findBySupplierGroup(UUID id);

    @Query("SELECT * FROM suppliers entity WHERE entity.supplier_group_id IS NULL")
    Flux<Suppliers> findAllWhereSupplierGroupIsNull();

    @Override
    <S extends Suppliers> Mono<S> save(S entity);

    @Override
    Flux<Suppliers> findAll();

    @Override
    Mono<Suppliers> findById(UUID id);

    @Query("SELECT * FROM suppliers entity WHERE entity.id = :id AND entity.company = :company AND entity.delete_at IS NULL AND entity.delete_by IS NULL")
    Mono<Suppliers> findById(UUID id, String company);

    @Override
    Mono<Void> deleteById(UUID id);

    @Query("UPDATE suppliers SET delete_at = NOW(), delete_by = :deleteBy WHERE id = :id AND company = :company AND delete_at IS NULL AND delete_by IS NULL")
    Mono<Void> deleteById(UUID id, String deleteBy, String company);

    @Query("UPDATE suppliers SET delete_at = NOW(), delete_by = :deleteBy WHERE id IN (:ids) AND company = :company AND delete_at IS NULL AND delete_by IS NULL")
    Mono<Void> deleteByIdIn(List<UUID> ids, String deleteBy, String company);

    @Modifying
    @Query("UPDATE suppliers SET is_active = :status, update_by = :activeBy, update_at = NOW() WHERE id = :id AND company = :company AND delete_at IS NULL AND delete_by IS NULL")
    Mono<Void> updateStatus(UUID id, Boolean status, UUID activeBy , String company);

    @Query("SELECT COUNT(*) FROM suppliers WHERE company = :company AND delete_at IS NULL AND delete_by IS NULL AND LOWER(code) = LOWER(:code)")
    Mono<Long> countByCode(String code, String company);

    @Query("SELECT * FROM suppliers WHERE create_by = :system AND company = :companyImport LIMIT 1")
    Mono<Suppliers> findByCreatedBy(String system, String companyImport);
}

interface SuppliersRepositoryInternal {
    <S extends Suppliers> Mono<S> save(S entity);

    Flux<Suppliers> findAllBy(Pageable pageable);

    Flux<Suppliers> findAllBy(Pageable pageable, String search, Boolean status, String company);

    Mono<Long> countByCriteria(String search, Boolean status, String company);

    Flux<Suppliers> findAll();

    Mono<Suppliers> findById(UUID id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Suppliers> findAllBy(Pageable pageable, Criteria criteria);
}
