package com.masi.sale.service;

import com.masi.sale.repository.MaterialRepository;
import com.masi.sale.repository.QuotationDetailRepository;
import com.masi.sale.service.dto.QuotationDetailDTO;
import com.masi.sale.service.mapper.MaterialMapper;
import com.masi.sale.service.mapper.QuotationDetailMapper;
import com.masi.sale.web.rest.errors.BadRequestAlertException;

import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing
 * {@link com.masi.sale.domain.QuotationDetail}.
 */
@Service
@Transactional
public class QuotationDetailService {

    private static final Logger log = LoggerFactory.getLogger(QuotationDetailService.class);

    private final QuotationDetailRepository quotationDetailRepository;
    private final MaterialRepository materialRepository;

    private final QuotationDetailMapper quotationDetailMapper;
    private final MaterialMapper materialMapper;

    public QuotationDetailService(QuotationDetailRepository quotationDetailRepository,
            QuotationDetailMapper quotationDetailMapper, MaterialRepository materialRepository,
            MaterialMapper materialMapper) {
        this.quotationDetailRepository = quotationDetailRepository;
        this.quotationDetailMapper = quotationDetailMapper;
        this.materialRepository = materialRepository;
        this.materialMapper = materialMapper;
    }

    /**
     * Save a quotationDetail.
     *
     * @param quotationDetailDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<QuotationDetailDTO> save(QuotationDetailDTO quotationDetailDTO) {
        log.debug("Request to save QuotationDetail : {}", quotationDetailDTO);
        return quotationDetailRepository.save(quotationDetailMapper.toEntity(quotationDetailDTO))
                .map(quotationDetailMapper::toDto);
    }

    /**
     * Update a quotationDetail.
     *
     * @param quotationDetailDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<QuotationDetailDTO> update(QuotationDetailDTO quotationDetailDTO) {
        log.debug("Request to update QuotationDetail : {}", quotationDetailDTO);
        return quotationDetailRepository
                .save(quotationDetailMapper.toEntity(quotationDetailDTO).setIsPersisted())
                .map(quotationDetailMapper::toDto);
    }

    /**
     * Partially update a quotationDetail.
     *
     * @param quotationDetailDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<QuotationDetailDTO> partialUpdate(QuotationDetailDTO quotationDetailDTO) {
        log.debug("Request to partially update QuotationDetail : {}", quotationDetailDTO);

        return quotationDetailRepository
                .findById(quotationDetailDTO.getId())
                .map(existingQuotationDetail -> {
                    quotationDetailMapper.partialUpdate(existingQuotationDetail, quotationDetailDTO);
                    existingQuotationDetail.setIsPersisted();
                    return existingQuotationDetail;
                })
                .flatMap(quotationDetailRepository::save)
                .map(quotationDetailMapper::toDto)
                .flatMap(result -> {
                    if (result.getMaterialId() != null) {
                        return materialRepository.findNotDeletedById(result.getMaterialId(), quotationDetailDTO.getCompany())
                                .map(materialMapper::toDto)
                                .map(materialDTO -> {
                                    result.setMaterial(materialDTO);
                                    return result;
                                });
                    }
                    return Mono.just(result);
                });
    }

    /**
     * Get all the quotationDetails.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<QuotationDetailDTO> findAll(Pageable pageable) {
        log.debug("Request to get all QuotationDetails");
        return quotationDetailRepository.findAllBy(pageable).map(quotationDetailMapper::toDto);
    }

    /**
     * Returns the number of quotationDetails available.
     *
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return quotationDetailRepository.count();
    }

    /**
     * Get one quotationDetail by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<QuotationDetailDTO> findOne(UUID id, String company) {
        log.debug("Request to get QuotationDetail : {}", id);
        return quotationDetailRepository.findOneById(id, company).map(quotationDetailMapper::toDto).flatMap(result -> {
            if (result.getMaterialId() != null) {
                return materialRepository.findNotDeletedById(result.getMaterialId(), company)
                        .switchIfEmpty(Mono.error(
                                new BadRequestAlertException("material not found", "QUOTATION_DETAIL", "NOT_FOUND")))
                        .map(materialMapper::toDto)
                        .map(materialDTO -> {
                            result.setMaterial(materialDTO);
                            return result;
                        });
            }
            return Mono.just(result);
        });
    }

    /**
     * Delete the quotationDetail by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(UUID id) {
        log.debug("Request to delete QuotationDetail : {}", id);
        return quotationDetailRepository.deleteById(id);
    }

    /**
     * Get all the quotationDetails by quotation.
     *
     * @param id the id of the quotation.
     * @return the list of entities.
     */
    public Mono<Void> removeById(UUID id, String company, String deletedBy) {
        log.debug("Request to remove QuotationDetail by Quotation : {}", id);
        return quotationDetailRepository.removeById(id, company, deletedBy);
    }
}
