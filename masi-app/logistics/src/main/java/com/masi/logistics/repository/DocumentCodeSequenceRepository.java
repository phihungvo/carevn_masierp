package com.masi.logistics.repository;

import com.masi.logistics.domain.DocumentCodeSequence;

import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the DocumentCodeSequence entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DocumentCodeSequenceRepository
    extends ReactiveCrudRepository<DocumentCodeSequence, UUID>, DocumentCodeSequenceRepositoryInternal {
    @Override
    <S extends DocumentCodeSequence> Mono<S> save(S entity);

    @Override
    Flux<DocumentCodeSequence> findAll();

    @Override
    Mono<DocumentCodeSequence> findById(UUID id);

    @Override
    Mono<Void> deleteById(UUID id);

    Mono<DocumentCodeSequence> findFirstByCompanyAndDocumentType(String company, String documentType);
}

interface DocumentCodeSequenceRepositoryInternal {
    <S extends DocumentCodeSequence> Mono<S> save(S entity);

    Flux<DocumentCodeSequence> findAllBy(Pageable pageable);

    Flux<DocumentCodeSequence> findAll();

    Mono<DocumentCodeSequence> findById(UUID id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<DocumentCodeSequence> findAllBy(Pageable pageable, Criteria criteria);
}
