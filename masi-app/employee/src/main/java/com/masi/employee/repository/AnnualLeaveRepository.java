package com.masi.employee.repository;

import com.masi.employee.domain.AnnualLeave;

import java.util.UUID;

import com.masi.employee.domain.enumeration.WorkPlace;
import com.masi.employee.service.mapper.ExplanationReviewMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the AnnualLeave entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AnnualLeaveRepository extends ReactiveCrudRepository<AnnualLeave, UUID>, AnnualLeaveRepositoryInternal {
    Flux<AnnualLeave> findAllBy(Pageable pageable);

    @Override
    <S extends AnnualLeave> Mono<S> save(S entity);

    @Override
    Flux<AnnualLeave> findAll();

    @Override
    Mono<AnnualLeave> findById(UUID id);

    @Override
    Mono<Void> deleteById(UUID id);

    Mono<AnnualLeave> findByWorkPlace(WorkPlace workPlace);

    Mono<AnnualLeave> findByWorkPlaceAndCompany(WorkPlace workPlace, String company);

    Flux<AnnualLeave> findAllByCarryForwardMonth(Integer carryForwardMonth);
}

interface AnnualLeaveRepositoryInternal {
    <S extends AnnualLeave> Mono<S> save(S entity);

    Flux<AnnualLeave> findAllBy(Pageable pageable);

    Flux<AnnualLeave> findAll();

    Mono<AnnualLeave> findById(UUID id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<AnnualLeave> findAllBy(Pageable pageable, Criteria criteria);
}
