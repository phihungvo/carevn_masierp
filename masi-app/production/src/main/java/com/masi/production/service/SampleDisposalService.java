package com.masi.production.service;

import com.carevn.masi.utils.SecurityUtils;
import com.masi.production.domain.QualityCheckSample;
import com.masi.production.domain.SampleDisposal;
import com.masi.production.domain.enumeration.QcSampleStatus;
import com.masi.production.repository.QualityCheckSampleRepository;
import com.masi.production.repository.SampleDisposalRepository;
import com.masi.production.service.dto.QualityCheckSampleDTO;
import com.masi.production.service.dto.ReviewSampleDisposalDTO;
import com.masi.production.service.dto.SampleDisposalDTO;
import com.masi.production.service.mapper.QualityCheckSampleMapper;
import com.masi.production.service.mapper.SampleDisposalMapper;
import com.masi.production.web.rest.errors.BadRequestAlertException;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.masi.production.domain.SampleDisposal}.
 */
@Service
@Transactional
public class SampleDisposalService {

    private final Logger log = LoggerFactory.getLogger(SampleDisposalService.class);

    private final SampleDisposalRepository sampleDisposalRepository;

    private final SampleDisposalMapper sampleDisposalMapper;
    private final QualityCheckSampleMapper qualityCheckSampleMapper;
    private final QualityCheckSampleService qualityCheckSampleService;
    private final QualityCheckSampleRepository qualityCheckSampleRepository;

    public SampleDisposalService(
        SampleDisposalRepository sampleDisposalRepository,
        SampleDisposalMapper sampleDisposalMapper, QualityCheckSampleMapper qualityCheckSampleMapper,
        QualityCheckSampleService qualityCheckSampleService,
        QualityCheckSampleRepository qualityCheckSampleRepository) {
        this.sampleDisposalRepository = sampleDisposalRepository;
        this.sampleDisposalMapper = sampleDisposalMapper;
        this.qualityCheckSampleMapper = qualityCheckSampleMapper;
        this.qualityCheckSampleService = qualityCheckSampleService;
        this.qualityCheckSampleRepository = qualityCheckSampleRepository;
    }

    /**
     * Save a sampleDisposal.
     *
     * @param sampleDisposalDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<SampleDisposalDTO> save(SampleDisposalDTO sampleDisposalDTO) {
        log.debug("Request to save SampleDisposal : {}", sampleDisposalDTO);
        return SecurityUtils.getUserJWTDetail().flatMap(login -> {
            sampleDisposalDTO.setCreatedAt(ZonedDateTime.now());
            sampleDisposalDTO.setLastUpdated(ZonedDateTime.now());
            sampleDisposalDTO.setIsActive(true);
            if (Objects.isNull(sampleDisposalDTO.getSamplingEmployeeId())){
                sampleDisposalDTO.setSamplingEmployeeId(login.getUserId());
            }
            return qualityCheckSampleService
                .findOne(sampleDisposalDTO.getQualitySampleCheckId())
                .switchIfEmpty(
                    Mono.error(
                        new BadRequestAlertException("Invalid qualityCheckSampleId", "qualityCheckSample", "qualityCheckSampleIdInvalid")
                    )
                )
                .flatMap(qualityCheckSample -> {
                    if (qualityCheckSample.getDisposalId() != null) {
                        return Mono.error(
                            new BadRequestAlertException(
                                "QualityCheckSample already has a disposal",
                                "qualityCheckSample",
                                "qualityCheckSampleAlreadyDisposed"
                            )
                        );
                    }
                    return Mono.just(qualityCheckSample);
                })
                .zipWith(sampleDisposalRepository.save(sampleDisposalMapper.toEntity(sampleDisposalDTO)).map(sampleDisposalMapper::toDto))
                .flatMap(tuple -> {
                    QualityCheckSampleDTO qualityCheckSample = tuple.getT1();
                    SampleDisposalDTO sampleDisposal = tuple.getT2();
                    qualityCheckSample.setDisposal(sampleDisposal);
                    qualityCheckSample.setStatus(QcSampleStatus.NEW);
                    qualityCheckSample.setDisposalId(sampleDisposal.getId());
                    return qualityCheckSampleService.partialUpdate(qualityCheckSample).then(
                        qualityCheckSampleService.setSampleStatus(qualityCheckSample.getId(),QcSampleStatus.AWAITING_DISPOSE)).thenReturn(sampleDisposal);
                });
        });
    }

    /**
     * Update a sampleDisposal.
     *
     * @param sampleDisposalDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<SampleDisposalDTO> update(SampleDisposalDTO sampleDisposalDTO) {
        log.debug("Request to update SampleDisposal : {}", sampleDisposalDTO);
        sampleDisposalDTO.setLastUpdated(ZonedDateTime.now());
        return sampleDisposalRepository.save(sampleDisposalMapper.toEntity(sampleDisposalDTO).setIsPersisted()).map(SampleDisposal::toDto);
    }

    public Mono<SampleDisposalDTO> review(ReviewSampleDisposalDTO reviewSampleDisposalDTO) {
        log.debug("Request to review SampleDisposal : {}", reviewSampleDisposalDTO);
        if (reviewSampleDisposalDTO.getReviewerApproved() && reviewSampleDisposalDTO.getReviewerSignFile() == null) {
            return Mono.error(new BadRequestAlertException("Sign is required for approval", "reviewSampleDisposal", "signRequired"));
        }
        if (!reviewSampleDisposalDTO.getReviewerApproved() && reviewSampleDisposalDTO.getReviewerNote() == null) {
            return Mono.error(new BadRequestAlertException("Note is required for rejection", "reviewSampleDisposal", "noteRequired"));
        }
        return sampleDisposalRepository
            .findById(reviewSampleDisposalDTO.getId())
            // fix lỗi khi không tìm thấy vẫn báo thành công
            .switchIfEmpty(Mono.error(new BadRequestAlertException("Invalid disposalId", "sampleDisposal", "disposalIdInvalid")))
            .map(sampleDisposal -> {
                reviewSampleDisposalDTO.applyUpdateTo(sampleDisposal);
                sampleDisposal.setLastUpdated(ZonedDateTime.now());
                sampleDisposal.setIsPersisted();
                return sampleDisposal;
            })
            .flatMap(sampleDisposalRepository::save)
            .map(sampleDisposalMapper::toDto)
            .zipWith(
                qualityCheckSampleService
                    .findFirstByDisposalId(reviewSampleDisposalDTO.getId())
                    .switchIfEmpty(
                        Mono.error(
                            new BadRequestAlertException(
                                "No qualityCheckSample found for disposal",
                                "qualityCheckSample",
                                "qualityCheckSampleNotFound"
                            )
                        )
                    )
            )
            .flatMap(tuple -> {
                SampleDisposalDTO sampleDisposal = tuple.getT1();
                QualityCheckSampleDTO qualityCheckSample = tuple.getT2();
                QcSampleStatus status = reviewSampleDisposalDTO.getReviewerApproved() ? QcSampleStatus.DISPOSED : QcSampleStatus.AWAITING_DISPOSE;
                if (status.equals(QcSampleStatus.DISPOSED)) {
                    qualityCheckSample.setStatus(QcSampleStatus.DISPOSED);
                    var entity = qualityCheckSampleMapper.toEntity(qualityCheckSample).setIsPersisted();
                    return qualityCheckSampleRepository.save(entity).thenReturn(sampleDisposal);
                }
                return qualityCheckSampleService.setSampleStatus(qualityCheckSample.getId(), status).thenReturn(sampleDisposal);
            });
    }

    /**
     * Partially update a sampleDisposal.
     *
     * @param sampleDisposalDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<SampleDisposalDTO> partialUpdate(SampleDisposalDTO sampleDisposalDTO) {
        log.debug("Request to partially update SampleDisposal : {}", sampleDisposalDTO);

        return sampleDisposalRepository
            .findById(sampleDisposalDTO.getId())
            .map(existingSampleDisposal -> {
                sampleDisposalMapper.partialUpdate(existingSampleDisposal, sampleDisposalDTO);
                existingSampleDisposal.setIsPersisted();
                return existingSampleDisposal;
            })
            .flatMap(sampleDisposalRepository::save)
            .map(SampleDisposal::toDto);
    }

    /**
     * Get all the sampleDisposals.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<SampleDisposalDTO> findAll(Pageable pageable) {
        log.debug("Request to get all SampleDisposals");
        return sampleDisposalRepository.findAllBy(pageable).map(SampleDisposal::toDto);
    }

    /**
     * Get all the sampleDisposals where Sample is {@code null}.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<SampleDisposalDTO> findAllWhereSampleIsNull() {
        log.debug("Request to get all sampleDisposals where Sample is null");
        return sampleDisposalRepository.findAllWhereSampleIsNull().map(SampleDisposal::toDto);
    }

    /**
     * Returns the number of sampleDisposals available.
     *
     * @return the number of entities in the database.
     */
    public Mono<Long> countAll() {
        return sampleDisposalRepository.count();
    }

    /**
     * Get one sampleDisposal by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<SampleDisposalDTO> findOne(UUID id) {
        log.debug("Request to get SampleDisposal : {}", id);
        return sampleDisposalRepository.findById(id).map(SampleDisposal::toDto);
    }

    /**
     * Delete the sampleDisposal by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(UUID id) {
        log.debug("Request to delete SampleDisposal : {}", id);
        return sampleDisposalRepository.deleteById(id);
    }
}
