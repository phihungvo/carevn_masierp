package com.masi.logistics.repository;

import com.masi.logistics.domain.UomGroupDetails;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the UomGroupDetails entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UomGroupDetailsRepository
        extends ReactiveCrudRepository<UomGroupDetails, UUID>, UomGroupDetailsRepositoryInternal {
    Flux<UomGroupDetails> findAllBy(Pageable pageable);

    @Query("SELECT * FROM uom_group_details entity WHERE entity.base_uom_id = :id")
    Flux<UomGroupDetails> findByBaseUom(UUID id);

    @Query("SELECT * FROM uom_group_details entity WHERE entity.base_uom_id IS NULL")
    Flux<UomGroupDetails> findAllWhereBaseUomIsNull();

    @Query("SELECT * FROM uom_group_details entity WHERE entity.alt_uom_id = :id")
    Flux<UomGroupDetails> findByAltUom(UUID id);

    @Query("SELECT * FROM uom_group_details entity WHERE entity.alt_uom_id IS NULL")
    Flux<UomGroupDetails> findAllWhereAltUomIsNull();

    @Query("SELECT * FROM uom_group_details entity WHERE entity.uom_group_id = :id")
    Flux<UomGroupDetails> findByUomGroup(UUID id);

    @Query("SELECT * FROM uom_group_details entity WHERE entity.uom_group_id IS NULL")
    Flux<UomGroupDetails> findAllWhereUomGroupIsNull();

    @Override
    <S extends UomGroupDetails> Mono<S> save(S entity);

    @Override
    Flux<UomGroupDetails> findAll();

    @Override
    Mono<UomGroupDetails> findById(UUID id);

    @Override
    Mono<Void> deleteById(UUID id);

    // delete all by uomGroupId and company and deleted at is null, update deleted at and deleted by
    @Query("UPDATE uom_group_details SET delete_at = NOW(), delete_by = :deletedBy WHERE uom_group_id = :uomGroupId AND company = :company AND delete_at IS NULL AND delete_by IS NULL")
    Mono<Void> deleteAllByUomGroupId(UUID uomGroupId, String company, String deletedBy);

}

interface UomGroupDetailsRepositoryInternal {
    <S extends UomGroupDetails> Mono<S> save(S entity);

    Flux<UomGroupDetails> findAllBy(Pageable pageable);

    Flux<UomGroupDetails> findAll();

    Mono<UomGroupDetails> findById(UUID id);

    Flux<UomGroupDetails> findAllByUomGroupId(UUID uomGroupId, String company);
    // this is not supported at the moment because of
    // https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<UomGroupDetails> findAllBy(Pageable pageable, Criteria criteria);
}
