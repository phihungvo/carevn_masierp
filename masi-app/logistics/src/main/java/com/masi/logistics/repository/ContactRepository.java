package com.masi.logistics.repository;

import com.masi.logistics.domain.Contact;
import com.masi.logistics.domain.criteria.ContactCriteria;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the Contact entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ContactRepository extends ReactiveCrudRepository<Contact, UUID>, ContactRepositoryInternal {
    Flux<Contact> findAllBy(Pageable pageable);

    @Query("SELECT * FROM contact entity WHERE entity.contact_type_id = :id")
    Flux<Contact> findByContactType(UUID id);

    @Query("SELECT * FROM contact entity WHERE entity.contact_type_id IS NULL")
    Flux<Contact> findAllWhereContactTypeIsNull();

    @Override
    <S extends Contact> Mono<S> save(S entity);

    @Override
    Flux<Contact> findAll();

    @Override
    Mono<Contact> findById(UUID id);

    @Override
    Mono<Void> deleteById(UUID id);

    @Modifying
    @Query("UPDATE contact set deleted_at = NOW(), deleted_by = :deletedBy WHERE supplier_id = :id AND company = :company AND deleted_at IS NULL AND deleted_by IS NULL")
    Mono<Void> deleteBySupplierId(UUID id, UUID deletedBy, String company);


}

interface ContactRepositoryInternal {
    <S extends Contact> Mono<S> save(S entity);

    Flux<Contact> findAllBy(Pageable pageable);

    Flux<Contact> findAll();

    Mono<Contact> findById(UUID id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Contact> findAllBy(Pageable pageable, Criteria criteria);
    Flux<Contact> findByCriteria(ContactCriteria criteria, Pageable pageable);

    Mono<Long> countByCriteria(ContactCriteria criteria);

    Flux<Contact> findAllBySupplierId(UUID supplierId, String company);

}
