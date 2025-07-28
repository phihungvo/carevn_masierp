package com.masi.employee.repository;

import com.masi.employee.domain.Documentary;

import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

import com.masi.employee.domain.enumeration.DocumentaryStatus;
import com.masi.employee.service.dto.DocumentaryRO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the Documentary entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DocumentaryRepository extends ReactiveCrudRepository<Documentary, UUID>, DocumentaryRepositoryInternal {
    Flux<Documentary> findAllBy(Pageable pageable);

    @Override
    <S extends Documentary> Mono<S> save(S entity);

    @Override
    Flux<Documentary> findAll();

    @Override
    Mono<Documentary> findById(UUID id);

    @Override
    Mono<Void> deleteById(UUID id);

    @Query("UPDATE documentary SET is_deleted = true WHERE id = :id")
    Mono<Void> changeIsActiveByIdAndUpdateLast(UUID id);


    Mono<Documentary> findByIdAndIsDeleted(UUID id, boolean isDeleted);

    @Query("UPDATE documentary SET status = 'PENDING_APPROVAL' WHERE id = :id")
    Mono<Void> changeStatusByIdAndStatus(UUID id);

    @Query("UPDATE documentary SET status = 'APPROVED', approval_sign_file = :approvalSign WHERE id = :id")
    Mono<Void> consentToReviewContract(UUID id, String approvalSign);

    @Query("UPDATE documentary SET status = 'REQUEST_EDIT',reject_note =:rejectNote WHERE id = :id")
    Mono<Void> refusalOfReviewContract(UUID id, String rejectNote);
}

interface DocumentaryRepositoryInternal {
    <S extends Documentary> Mono<S> save(S entity);

    Flux<Documentary> findAllBy(Pageable pageable);

    Flux<Documentary> findAll();

    Mono<Documentary> findById(UUID id);

    Mono<Long> countByFilter(DocumentaryRO ro);

    Flux<Documentary> findAllByFilter(DocumentaryRO ro, Pageable pageable);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Documentary> findAllBy(Pageable pageable, Criteria criteria);
}
