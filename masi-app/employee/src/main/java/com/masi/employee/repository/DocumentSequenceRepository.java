package com.masi.employee.repository;

import com.masi.employee.domain.DocumentSequence;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the DocumentSequence entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DocumentSequenceRepository
        extends ReactiveCrudRepository<DocumentSequence, Long>, DocumentSequenceRepositoryInternal {
    @Override
    <S extends DocumentSequence> Mono<S> save(S entity);

    @Override
    Flux<DocumentSequence> findAll();

    @Override
    Mono<DocumentSequence> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);

    Mono<DocumentSequence> findByEntityNameAndCompanyAndIsActiveIsTrue(
            String entityName, String company);

    @Query("UPDATE document_sequence ds set current_sequence = (current_sequence + 1) WHERE entity_name =:entityName AND company =:company")
    Mono<Void> UpdateByEntityNameAndCompanyAndIsActiveIsTrue(String entityName, String company);
}

interface DocumentSequenceRepositoryInternal {
    <S extends DocumentSequence> Mono<S> save(S entity);

    Flux<DocumentSequence> findAllBy(Pageable pageable);

    Flux<DocumentSequence> findAll();

    Mono<DocumentSequence> findById(Long id);
    // this is not supported at the moment because of
    // https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<DocumentSequence> findAllBy(Pageable pageable, Criteria criteria);
}
