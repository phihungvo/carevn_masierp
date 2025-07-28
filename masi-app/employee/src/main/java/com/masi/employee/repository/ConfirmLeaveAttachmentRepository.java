package com.masi.employee.repository;

import com.masi.employee.domain.ConfirmLeaveAttachment;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the ConfirmLeaveAttachment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ConfirmLeaveAttachmentRepository
    extends ReactiveCrudRepository<ConfirmLeaveAttachment, UUID>, ConfirmLeaveAttachmentRepositoryInternal {
    Flux<ConfirmLeaveAttachment> findAllBy(Pageable pageable);

    @Query("SELECT * FROM confirm_leave_attachment entity WHERE entity.confirm_leave_id = :id")
    Flux<ConfirmLeaveAttachment> findByConfirmLeave(UUID id);

    @Query("SELECT * FROM confirm_leave_attachment entity WHERE entity.confirm_leave_id IS NULL")
    Flux<ConfirmLeaveAttachment> findAllWhereConfirmLeaveIsNull();

    @Override
    <S extends ConfirmLeaveAttachment> Mono<S> save(S entity);

    @Override
    Flux<ConfirmLeaveAttachment> findAll();

    @Override
    Mono<ConfirmLeaveAttachment> findById(UUID id);

    @Override
    Mono<Void> deleteById(UUID id);
}

interface ConfirmLeaveAttachmentRepositoryInternal {
    <S extends ConfirmLeaveAttachment> Mono<S> save(S entity);

    Flux<ConfirmLeaveAttachment> findAllBy(Pageable pageable);

    Flux<ConfirmLeaveAttachment> findAll();

    Mono<ConfirmLeaveAttachment> findById(UUID id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<ConfirmLeaveAttachment> findAllBy(Pageable pageable, Criteria criteria);
}
