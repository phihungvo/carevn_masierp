package com.masi.production.service;

import com.masi.production.domain.ManufactureOrder;
import com.masi.production.domain.MetalDetectionChecklist;
import com.masi.production.repository.MetalDetectionChecklistRepository;
import com.masi.production.repository.WorkItemRepository;
import com.masi.production.repository.WorkOrderRepository;
import com.masi.production.service.dto.MetalDetectionChecklistDTO;
import com.masi.production.service.mapper.MetalDetectionChecklistMapper;

import java.time.ZonedDateTime;
import java.util.UUID;

import com.masi.production.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.masi.production.domain.MetalDetectionChecklist}.
 */
@Service
@Transactional
public class MetalDetectionChecklistService {

    private final Logger log = LoggerFactory.getLogger(MetalDetectionChecklistService.class);

    private final MetalDetectionChecklistRepository metalDetectionChecklistRepository;
    private final WorkItemRepository workItemRepository;
    private final MetalDetectionChecklistMapper metalDetectionChecklistMapper;
    private final WorkOrderRepository workOrderRepository;
    public MetalDetectionChecklistService(
        MetalDetectionChecklistRepository metalDetectionChecklistRepository,
        MetalDetectionChecklistMapper metalDetectionChecklistMapper,
        WorkItemRepository workItemRepository, WorkOrderRepository workOrderRepository
    ) {
        this.metalDetectionChecklistRepository = metalDetectionChecklistRepository;
        this.metalDetectionChecklistMapper = metalDetectionChecklistMapper;
        this.workItemRepository = workItemRepository;
        this.workOrderRepository = workOrderRepository;
    }

    /**
     * Save a metalDetectionChecklist.
     *
     * @param dto the entity to save.
     * @return the persisted entity.
     */
    public Mono<MetalDetectionChecklistDTO> save(MetalDetectionChecklistDTO dto) {
        log.debug("Request to save MetalDetectionChecklist : {}", dto);
        MetalDetectionChecklist entity = dto.toEntity();
        entity.setCreatedAt(ZonedDateTime.now());
        entity.setIsActive(true);
        entity.setLastUpdated(ZonedDateTime.now());
            return metalDetectionChecklistRepository
                .save(entity)
                .map(MetalDetectionChecklist::toDto);
    }

    /**
     * Partially update a metalDetectionChecklist.
     *
     * @param dto the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<MetalDetectionChecklistDTO> partialUpdate(MetalDetectionChecklistDTO dto) {
        log.debug("Request to partially update MetalDetectionChecklist : {}", dto);

        return metalDetectionChecklistRepository
            .findById(dto.getId())
            .map(existingMetalDetectionChecklist -> {
                dto.applyUpdate(existingMetalDetectionChecklist);
                existingMetalDetectionChecklist.setIsPersisted();
                existingMetalDetectionChecklist.setLastUpdated(ZonedDateTime.now());
                return existingMetalDetectionChecklist;
            })
            .flatMap(metalDetectionChecklistRepository::save)
            .map(MetalDetectionChecklist::toDto).doFinally(signalType -> {
                workOrderRepository.updateLastUpdatedByWorkItem(dto.getWorkItemId()).subscribe();
            });
    }

    /**
     * Get all the metalDetectionChecklists.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<MetalDetectionChecklistDTO> findAll(Pageable pageable) {
        log.debug("Request to get all MetalDetectionChecklists");
        return metalDetectionChecklistRepository.findAllBy(pageable).map(MetalDetectionChecklist::toDto);
    }

    /**
     * Returns the number of metalDetectionChecklists available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return metalDetectionChecklistRepository.count();
    }

    /**
     * Get one metalDetectionChecklist by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<MetalDetectionChecklistDTO> findOne(UUID id) {
        log.debug("Request to get MetalDetectionChecklist : {}", id);
        return metalDetectionChecklistRepository.findById(id).map(MetalDetectionChecklist::toDto);
    }

    /**
     * Delete the metalDetectionChecklist by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(UUID id) {
        log.debug("Request to delete MetalDetectionChecklist : {}", id);
        return metalDetectionChecklistRepository.findById(id).map(existing -> {
            existing.setIsActive(false);
            existing.setLastUpdated(ZonedDateTime.now());
            existing.setIsPersisted();
            return existing;
        }).flatMap(metalDetectionChecklistRepository::save).then();
    }
}
