package com.masi.production.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.masi.production.domain.ProductPackage;
import com.masi.production.domain.QualityCheckSample;
import com.masi.production.domain.enumeration.ProductPackageStatus;
import com.masi.production.domain.enumeration.QcSampleStatus;
import com.masi.production.repository.ManufactureOrderRepository;
import com.masi.production.repository.ProductPackageRepository;
import com.masi.production.repository.QualityCheckSampleRepository;
import com.masi.production.service.dto.QualityCheckSampleDTO;
import com.masi.production.service.dto.QuanlityCheckSampleRO;
import com.masi.production.service.dto.QuantityCheckFilter;
import com.masi.production.service.dto.QuantityCheckQuery;
import com.masi.production.service.mapper.ProductPackageMapper;
import com.masi.production.service.mapper.QualityCheckSampleMapper;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import com.masi.production.service.web.LogisticClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.masi.production.domain.QualityCheckSample}.
 */
@Service
@Transactional
public class QualityCheckSampleService {

    private final Logger log = LoggerFactory.getLogger(QualityCheckSampleService.class);

    private final QualityCheckSampleRepository qualityCheckSampleRepository;
    private final ProductPackageRepository productPackageRepository;

    private final QualityCheckSampleMapper qualityCheckSampleMapper;
    private final LogisticClient logisticClient;

    public QualityCheckSampleService(
            QualityCheckSampleRepository qualityCheckSampleRepository, ProductPackageRepository productPackageRepository,
            QualityCheckSampleMapper qualityCheckSampleMapper, ProductPackageMapper productPackageMapper,
            LogisticClient logisticClient) {
        this.qualityCheckSampleRepository = qualityCheckSampleRepository;
        this.productPackageRepository = productPackageRepository;
        this.qualityCheckSampleMapper = qualityCheckSampleMapper;
        this.logisticClient = logisticClient;
    }

    public Mono<QualityCheckSampleDTO> findFirstByDisposalId(UUID disposalId) {
        log.debug("Request to get QualityCheckSample by disposalId : {}", disposalId);
        return qualityCheckSampleRepository.findFirstByDisposalId(disposalId).map(qualityCheckSampleMapper::toDto);
    }

    /**
     * Save a qualityCheckSample.
     *
     * @param qualityCheckSampleDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<QualityCheckSampleDTO> saveOrUpdate(QualityCheckSampleDTO qualityCheckSampleDTO) {
        log.debug("Request to save QualityCheckSample : {}", qualityCheckSampleDTO);
        if (qualityCheckSampleDTO.getId() == null)
            qualityCheckSampleDTO.setId(UUID.randomUUID());
        return qualityCheckSampleRepository
                .findById(qualityCheckSampleDTO.getId())
                .flatMap(existingQualityCheckSample -> this.partialUpdate(qualityCheckSampleDTO))
                .switchIfEmpty(Mono.defer(() -> this.save(qualityCheckSampleDTO)));
    }

    public Mono<QualityCheckSampleDTO> save(QualityCheckSampleDTO qualityCheckSampleDTO) {
        log.debug("Request to save QualityCheckSampleDTO : {}", qualityCheckSampleDTO);

        qualityCheckSampleDTO.setIsActive(true);
        qualityCheckSampleDTO.setCreatedAt(ZonedDateTime.now());
        qualityCheckSampleDTO.setLastUpdated(ZonedDateTime.now());
        qualityCheckSampleDTO.setStatus(QcSampleStatus.NEW);
        return qualityCheckSampleRepository
                .save(qualityCheckSampleMapper.toEntity(qualityCheckSampleDTO))
                .flatMap(savedEntity -> {
                    try {
                        return handleSaveManufactureImportWareHouse(qualityCheckSampleDTO, qualityCheckSampleDTO.getManufactureOrderId(), qualityCheckSampleDTO.getItemId())
                                .thenReturn(savedEntity);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .map(qualityCheckSampleMapper::toDto);
    }

    /**
     * Partially update a qualityCheckSample.
     *
     * @param qualityCheckSampleDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<QualityCheckSampleDTO> partialUpdate(QualityCheckSampleDTO qualityCheckSampleDTO) {
        log.debug("Request to partially update QualityCheckSample : {}", qualityCheckSampleDTO);

        return qualityCheckSampleRepository
                .findById(qualityCheckSampleDTO.getId())
                .map(existingQualityCheckSample -> {
                    qualityCheckSampleMapper.partialUpdate(existingQualityCheckSample, qualityCheckSampleDTO);
                    if (existingQualityCheckSample.getItemId() != null)
                        existingQualityCheckSample.setItemId(qualityCheckSampleDTO.getItemId());

                    existingQualityCheckSample.setIsPersisted();
                    return existingQualityCheckSample;
                })
                .flatMap(qualityCheckSampleRepository::save)
                .flatMap(savedEntity -> {
                    try {
                        return handleSaveManufactureImportWareHouse(qualityCheckSampleDTO, qualityCheckSampleDTO.getManufactureOrderId(), qualityCheckSampleDTO.getItemId())
                                .thenReturn(savedEntity);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .map(qualityCheckSampleMapper::toDto);
    }

    private Mono<Void> handleSaveManufactureImportWareHouse(QualityCheckSampleDTO qualityCheckSampleDTO, UUID manufactureOrderId, UUID itemId) throws IOException {
        if (manufactureOrderId == null && (itemId != null || itemId.equals(UUID.fromString("00000000-0000-0000-0000-000000000000")))) {
            return Mono.empty();
        }

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode attributes = objectMapper.readTree(qualityCheckSampleDTO.getAttributes().toString());
            if (attributes.has("isDone")) {
                boolean isDone = attributes.get("isDone").asBoolean();
                if (isDone) {
                    return Mono.empty();
                }
            }
        } catch (Exception e) {
            if (qualityCheckSampleDTO.getIsDone() == null || !qualityCheckSampleDTO.getIsDone())
                return Mono.empty().then();
        }

        Float code = qualityCheckSampleDTO.getProteinPercentageApply();

        return logisticClient.updateInventoriesStorage(manufactureOrderId, itemId, code)
                .then();
    }

    ;


    public Flux<QualityCheckSampleDTO> findAllByQuery(Pageable pageable, QuanlityCheckSampleRO ro) {
        log.debug("Request to get all QualityCheckSamples by query: {}", ro);
        if (pageable.getSort().isUnsorted()) {
            log.debug("Sorting by createdAt desc");
            Sort sort = Sort.by(Sort.Order.desc("createdAt"));
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        }
        return qualityCheckSampleRepository.findAllByFilter(pageable, ro).map(QualityCheckSample::toDto);
    }

    public Mono<Long> countAllByQuery(QuanlityCheckSampleRO ro) {
        log.debug("Request to count all QualityCheckSamples by query: {}", ro);
        return qualityCheckSampleRepository.countByFilter(ro);
    }

    public Mono<QualityCheckSampleDTO> handleReviewPass(UUID id) {
        log.debug("Request to review approved QualityCheckSample : {}", id);
        return qualityCheckSampleRepository.findById(id)
                .flatMap(qualityCheckSample -> {
                    qualityCheckSample.setStatus(QcSampleStatus.PASS);
                    qualityCheckSample.setLastUpdated(ZonedDateTime.now());
                    qualityCheckSample.setIsPersisted();
                    return Mono.just(qualityCheckSample).map(QualityCheckSample::toDto);
                });
    }

    public Mono<Void> setSampleStatus(UUID id, QcSampleStatus status) {
        log.debug("Request to set QualityCheckSample status : {}", id);
        return qualityCheckSampleRepository.updateStatus(id, status);
    }

    public Mono<QualityCheckSampleDTO> handleFailQualityCheck(UUID id) {
        log.debug("Request to handle fail QualityCheckSample : {}", id);
        return qualityCheckSampleRepository.findById(id)
                .map(qualityCheckSample -> {
                    qualityCheckSample.setStatus(QcSampleStatus.REJECTED);
                    qualityCheckSample.setLastUpdated(ZonedDateTime.now());
                    return qualityCheckSample.setIsPersisted();
                })
                .flatMap(qualityCheckSampleRepository::save)
                .map(QualityCheckSample::toDto);
    }


    /**
     * Get all the qualityCheckSamples.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<QualityCheckSampleDTO> findAll(Pageable pageable) {
        log.debug("Request to get all QualityCheckSamples");
        return qualityCheckSampleRepository.findAllBy(pageable).map(QualityCheckSample::toDto);
    }

    /**
     * Returns the number of qualityCheckSamples available.
     *
     * @return the number of entities in the database.
     */
    public Mono<Long> countAll() {
        return qualityCheckSampleRepository.count();
    }

    /**
     * Get one qualityCheckSample by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<QualityCheckSampleDTO> findOne(UUID id) {
        log.debug("Request to get QualityCheckSample : {}", id);
        return qualityCheckSampleRepository.findById(id).map(QualityCheckSample::toDto);
    }

    /**
     * Delete the qualityCheckSample by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(UUID id) {
        log.debug("Request to delete QualityCheckSample : {}", id);
        return qualityCheckSampleRepository.softDeleteById(id);
    }
}
