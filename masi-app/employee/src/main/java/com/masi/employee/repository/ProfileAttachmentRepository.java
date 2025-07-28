package com.masi.employee.repository;

import com.masi.employee.domain.ProfileAttachment;

import java.util.UUID;

import com.masi.employee.domain.enumeration.ProfileAttachmentType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the ProfileAttachment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProfileAttachmentRepository extends ReactiveCrudRepository<ProfileAttachment, UUID>, ProfileAttachmentRepositoryInternal {
    Flux<ProfileAttachment> findAllBy(Pageable pageable);

    @Override
    <S extends ProfileAttachment> Mono<S> save(S entity);

    @Override
    Flux<ProfileAttachment> findAll();

    @Override
    Mono<ProfileAttachment> findById(UUID id);

    @Override
    Mono<Void> deleteById(UUID id);

    // by profile id and type
    @Query("SELECT * FROM profile_attachment entity WHERE entity.employee_profile_id = :profileId and entity.type = :attachmentType limit 1")
    Mono<ProfileAttachment> findAllByProfileIdAndAttachmentType(UUID profileId, ProfileAttachmentType attachmentType);

    @Query("SELECT * FROM profile_attachment entity WHERE entity.employee_profile_id = :profileId and entity.is_deleted = false")
    Flux<ProfileAttachment> findAllByProfileId(UUID profileId);

    @Query("UPDATE profile_attachment SET is_deleted = true WHERE employee_profile_id = :profileId and type = :attachmentType")
    Mono<Void> deleteAllByType(UUID profileId, ProfileAttachmentType attachmentType);

    @Query("UPDATE profile_attachment SET is_deleted = true WHERE employee_profile_id = :profileId")
    Mono<Void> deleteAllByProfileId(UUID profileId);
}

interface ProfileAttachmentRepositoryInternal {
    <S extends ProfileAttachment> Mono<S> save(S entity);

    Flux<ProfileAttachment> findAllBy(Pageable pageable);

    Flux<ProfileAttachment> findAll();

    Mono<ProfileAttachment> findById(UUID id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<ProfileAttachment> findAllBy(Pageable pageable, Criteria criteria);
}
