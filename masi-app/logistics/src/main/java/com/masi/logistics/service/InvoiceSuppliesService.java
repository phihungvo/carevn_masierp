package com.masi.logistics.service;

import com.masi.logistics.domain.criteria.InvoiceSuppliesCriteria;
import com.masi.logistics.repository.InvoiceSuppliesRepository;
import com.masi.logistics.service.dto.InvoiceSuppliesDTO;
import com.masi.logistics.service.mapper.InvoiceSuppliesMapper;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.masi.logistics.domain.InvoiceSupplies}.
 */
@Service
@Transactional
public class InvoiceSuppliesService {

    private static final Logger log = LoggerFactory.getLogger(InvoiceSuppliesService.class);

    private final InvoiceSuppliesRepository invoiceSuppliesRepository;

    private final InvoiceSuppliesMapper invoiceSuppliesMapper;

    public InvoiceSuppliesService(InvoiceSuppliesRepository invoiceSuppliesRepository, InvoiceSuppliesMapper invoiceSuppliesMapper) {
        this.invoiceSuppliesRepository = invoiceSuppliesRepository;
        this.invoiceSuppliesMapper = invoiceSuppliesMapper;
    }

    /**
     * Save a invoiceSupplies.
     *
     * @param invoiceSuppliesDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<InvoiceSuppliesDTO> save(InvoiceSuppliesDTO invoiceSuppliesDTO) {
        log.debug("Request to save InvoiceSupplies : {}", invoiceSuppliesDTO);
        return invoiceSuppliesRepository.save(invoiceSuppliesMapper.toEntity(invoiceSuppliesDTO)).map(invoiceSuppliesMapper::toDto);
    }

    /**
     * Update a invoiceSupplies.
     *
     * @param invoiceSuppliesDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<InvoiceSuppliesDTO> update(InvoiceSuppliesDTO invoiceSuppliesDTO) {
        log.debug("Request to update InvoiceSupplies : {}", invoiceSuppliesDTO);
        return invoiceSuppliesRepository
            .save(invoiceSuppliesMapper.toEntity(invoiceSuppliesDTO).setIsPersisted())
            .map(invoiceSuppliesMapper::toDto);
    }

    /**
     * Partially update a invoiceSupplies.
     *
     * @param invoiceSuppliesDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<InvoiceSuppliesDTO> partialUpdate(InvoiceSuppliesDTO invoiceSuppliesDTO) {
        log.debug("Request to partially update InvoiceSupplies : {}", invoiceSuppliesDTO);

        return invoiceSuppliesRepository
            .findById(invoiceSuppliesDTO.getId())
            .map(existingInvoiceSupplies -> {
                invoiceSuppliesMapper.partialUpdate(existingInvoiceSupplies, invoiceSuppliesDTO);

                return existingInvoiceSupplies;
            })
            .flatMap(invoiceSuppliesRepository::save)
            .map(invoiceSuppliesMapper::toDto);
    }

    /**
     * Find invoiceSupplies by Criteria.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<InvoiceSuppliesDTO> findByCriteria(InvoiceSuppliesCriteria criteria, Pageable pageable) {
        log.debug("Request to get all InvoiceSupplies by Criteria");
        return invoiceSuppliesRepository.findByCriteria(criteria, pageable).map(invoiceSuppliesMapper::toDto);
    }

    /**
     * Find the count of invoiceSupplies by criteria.
     * @param criteria filtering criteria
     * @return the count of invoiceSupplies
     */
    public Mono<Long> countByCriteria(InvoiceSuppliesCriteria criteria) {
        log.debug("Request to get the count of all InvoiceSupplies by Criteria");
        return invoiceSuppliesRepository.countByCriteria(criteria);
    }

    /**
     * Returns the number of invoiceSupplies available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return invoiceSuppliesRepository.count();
    }

    /**
     * Get one invoiceSupplies by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<InvoiceSuppliesDTO> findOne(UUID id) {
        log.debug("Request to get InvoiceSupplies : {}", id);
        return invoiceSuppliesRepository.findById(id).map(invoiceSuppliesMapper::toDto);
    }

    /**
     * Delete the invoiceSupplies by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(UUID id) {
        log.debug("Request to delete InvoiceSupplies : {}", id);
        return invoiceSuppliesRepository.deleteById(id);
    }
}
