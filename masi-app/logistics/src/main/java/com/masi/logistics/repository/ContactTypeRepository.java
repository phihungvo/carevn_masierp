package com.masi.logistics.repository;

import com.masi.logistics.domain.ContactType;
import com.masi.logistics.domain.criteria.ContactTypeCriteria;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the ContactType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ContactTypeRepository extends ReactiveCrudRepository<ContactType, UUID>, ContactTypeRepositoryInternal {
    Flux<ContactType> findAllBy(Pageable pageable);

    @Override
    <S extends ContactType> Mono<S> save(S entity);

    @Override
    Flux<ContactType> findAll();

    @Override
    Mono<ContactType> findById(UUID id);

    @Override
    Mono<Void> deleteById(UUID id);
}

interface ContactTypeRepositoryInternal {
    <S extends ContactType> Mono<S> save(S entity);

    Flux<ContactType> findAllBy(Pageable pageable);

    Flux<ContactType> findAll();

    Mono<ContactType> findById(UUID id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<ContactType> findAllBy(Pageable pageable, Criteria criteria);
    Flux<ContactType> findByCriteria(ContactTypeCriteria criteria, Pageable pageable);

    Mono<Long> countByCriteria(ContactTypeCriteria criteria);
}
