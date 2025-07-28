package com.masi.utility.repository;

import com.masi.utility.domain.FileAttachment;
import com.masi.utility.domain.criteria.FileAttachmentCriteria;

import java.util.Collection;
import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the FileAttachment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FileAttachmentRepository extends ReactiveCrudRepository<FileAttachment, UUID>, FileAttachmentRepositoryInternal {
    Flux<FileAttachment> findAllBy(Pageable pageable);

    @Override
    <S extends FileAttachment> Mono<S> save(S entity);

    @Override
    Flux<FileAttachment> findAll();

    @Override
    Mono<FileAttachment> findById(UUID id);

    Mono<FileAttachment> findFirstByIdAndDeletedAtIsNull(UUID id);

    @Override
    Mono<Void> deleteById(UUID id);

    Flux<FileAttachment> findAllByIdIn(Collection<@NotNull(message = "must not be null") UUID> id);
}

interface FileAttachmentRepositoryInternal {
    <S extends FileAttachment> Mono<S> save(S entity);

    Flux<FileAttachment> findAllBy(Pageable pageable);

    Flux<FileAttachment> findAll();

    Mono<FileAttachment> findById(UUID id);

    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<FileAttachment> findAllBy(Pageable pageable, Criteria criteria);
    Flux<FileAttachment> findByCriteria(FileAttachmentCriteria criteria, Pageable pageable);

    Mono<Long> countByCriteria(FileAttachmentCriteria criteria);
}
