package com.masi.logistics.repository;

import com.masi.logistics.domain.ContactGift;
import com.masi.logistics.domain.criteria.ContactGiftCriteria;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the ContactGift entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ContactGiftRepository extends ReactiveCrudRepository<ContactGift, UUID>, ContactGiftRepositoryInternal {
    Flux<ContactGift> findAllBy(Pageable pageable);

    @Query("SELECT * FROM contact_gift entity WHERE entity.contact_id = :id")
    Flux<ContactGift> findByContact(UUID id);

    @Query("SELECT * FROM contact_gift entity WHERE entity.contact_id IS NULL")
    Flux<ContactGift> findAllWhereContactIsNull();

    @Override
    <S extends ContactGift> Mono<S> save(S entity);

    @Override
    Flux<ContactGift> findAll();

    @Override
    Mono<ContactGift> findById(UUID id);

    @Override
    Mono<Void> deleteById(UUID id);

    @Modifying
    @Query("UPDATE contact_gift set deleted_at = NOW(), deleted_by = :deletedBy WHERE contact_id = :id")
    Mono<Void> deletedByContactId(UUID id, UUID deletedBy, String company);

    @Query("SELECT * FROM contact_gift entity WHERE entity.contact_id = :id AND entity.company = :company AND entity.deleted_at IS NULL AND entity.deleted_by IS NULL")
    Flux<ContactGift> findAllByContactId(UUID id, String company);
}

interface ContactGiftRepositoryInternal {
    <S extends ContactGift> Mono<S> save(S entity);

    Flux<ContactGift> findAllBy(Pageable pageable);

    Flux<ContactGift> findAll();

    Mono<ContactGift> findById(UUID id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<ContactGift> findAllBy(Pageable pageable, Criteria criteria);
    Flux<ContactGift> findByCriteria(ContactGiftCriteria criteria, Pageable pageable);

    Mono<Long> countByCriteria(ContactGiftCriteria criteria);
}
