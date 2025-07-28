package com.masi.production.repository;

import com.masi.production.domain.QualityCheckSample;
import com.masi.production.domain.enumeration.QcSampleStatus;
import com.masi.production.service.dto.QuanlityCheckSampleRO;
import com.masi.production.service.dto.QuantityCheckFilter;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the QualityCheckSample entity.
 */
@SuppressWarnings("unused")
@Repository
public interface QualityCheckSampleRepository
    extends ReactiveCrudRepository<QualityCheckSample, UUID>, QualityCheckSampleRepositoryInternal {
    Flux<QualityCheckSample> findAllBy(Pageable pageable);

    @Query("SELECT * FROM quality_check_sample entity WHERE entity.disposal_id = :id")
    Flux<QualityCheckSample> findByDisposal(UUID id);

    @Query("SELECT * FROM quality_check_sample entity WHERE entity.disposal_id IS NULL")
    Flux<QualityCheckSample> findAllWhereDisposalIsNull();

    @Override
    <S extends QualityCheckSample> Mono<S> save(S entity);

    @Override
    Flux<QualityCheckSample> findAll();

    @Override
    Mono<QualityCheckSample> findById(UUID id);

    @Override
    Mono<Void> deleteById(UUID id);

    @Query("SELECT * FROM quality_check_sample entity WHERE entity.is_active = true AND id = :id AND manufacture_order_id = :manufactureOrderId")
    Mono<QualityCheckSample> findByIdAndManufactureOrderId(UUID id, UUID manufactureOrderId);


    @Query(
        "SELECT count(*) FROM quality_check_sample entity WHERE entity.is_active = true and (:status is null or entity.status = :status) "
    )
    Mono<Long> findAllByIsActiveIsTrueAndStatus(QcSampleStatus status);

    @Query("UPDATE quality_check_sample SET is_active = false WHERE id = :id")
    Mono<Void> softDeleteById(UUID id);

    Mono<QualityCheckSample> findFirstByDisposalId(UUID disposalId);

    @Modifying
    @Query("UPDATE quality_check_sample SET status = :status WHERE id = :id")
    Mono<Void> updateStatus(UUID id, QcSampleStatus status);

    @Query("UPDATE quality_check_sample SET is_active = :b WHERE manufacture_order_id = :id")
    Mono<Void> changeIsDeletedByManufactureOrderId(UUID id, boolean b);
}



interface QualityCheckSampleRepositoryInternal {
    <S extends QualityCheckSample> Mono<S> save(S entity);

    Flux<QualityCheckSample> findAllBy(Pageable pageable);

    Flux<QualityCheckSample> findAll();

    Mono<QualityCheckSample> findById(UUID id);

    Flux<QualityCheckSample> findAllByIsActiveIsTrueAndStatus(Pageable pageable, QcSampleStatus status);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<QualityCheckSample> findAllBy(Pageable pageable, Criteria criteria);

    Flux<QualityCheckSample> findAllByFilter(Pageable pageable, QuanlityCheckSampleRO ro);
    Mono<Long> countByFilter(QuanlityCheckSampleRO ro);
}
