package com.masi.sale.service;

import com.masi.sale.repository.QuotationExportRepository;
import com.masi.sale.service.dto.QuotationExportDTO;
import com.masi.sale.service.mapper.QuotationExportMapper;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.masi.sale.domain.QuotationExport}.
 */
@Service
@Transactional
public class QuotationExportService {

    private static final Logger log = LoggerFactory.getLogger(QuotationExportService.class);

    private final QuotationExportRepository quotationExportRepository;

    private final QuotationExportMapper quotationExportMapper;

    public QuotationExportService(QuotationExportRepository quotationExportRepository, QuotationExportMapper quotationExportMapper) {
        this.quotationExportRepository = quotationExportRepository;
        this.quotationExportMapper = quotationExportMapper;
    }

    /**
     * Save a quotationExport.
     *
     * @param quotationExportDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<QuotationExportDTO> save(QuotationExportDTO quotationExportDTO) {
        log.debug("Request to save QuotationExport : {}", quotationExportDTO);
        return quotationExportRepository.save(quotationExportMapper.toEntity(quotationExportDTO)).map(quotationExportMapper::toDto);
    }

    /**
     * Update a quotationExport.
     *
     * @param quotationExportDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<QuotationExportDTO> update(QuotationExportDTO quotationExportDTO) {
        log.debug("Request to update QuotationExport : {}", quotationExportDTO);
        return quotationExportRepository
            .save(quotationExportMapper.toEntity(quotationExportDTO).setIsPersisted())
            .map(quotationExportMapper::toDto);
    }

    /**
     * Partially update a quotationExport.
     *
     * @param quotationExportDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<QuotationExportDTO> partialUpdate(QuotationExportDTO quotationExportDTO) {
        log.debug("Request to partially update QuotationExport : {}", quotationExportDTO);

        return quotationExportRepository
            .findById(quotationExportDTO.getId())
            .map(existingQuotationExport -> {
                quotationExportMapper.partialUpdate(existingQuotationExport, quotationExportDTO);

                return existingQuotationExport;
            })
            .flatMap(quotationExportRepository::save)
            .map(quotationExportMapper::toDto);
    }

    /**
     * Get all the quotationExports.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<QuotationExportDTO> findAll(Pageable pageable) {
        log.debug("Request to get all QuotationExports");
        return quotationExportRepository.findAllBy(pageable).map(quotationExportMapper::toDto);
    }

    /**
     * Returns the number of quotationExports available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return quotationExportRepository.count();
    }

    /**
     * Get one quotationExport by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<QuotationExportDTO> findOne(UUID id) {
        log.debug("Request to get QuotationExport : {}", id);
        return quotationExportRepository.findById(id).map(quotationExportMapper::toDto);
    }

    /**
     * Delete the quotationExport by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(UUID id) {
        log.debug("Request to delete QuotationExport : {}", id);
        return quotationExportRepository.deleteById(id);
    }
}
