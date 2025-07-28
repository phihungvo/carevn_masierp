package com.masi.production.service;

import com.masi.production.domain.MixingReportChecklist;
import com.masi.production.repository.MixingReportChecklistRepository;
import com.masi.production.repository.WorkItemRepository;
import com.masi.production.repository.WorkOrderRepository;
import com.masi.production.service.dto.MixingReportChecklistDTO;
import com.masi.production.service.mapper.MixingReportChecklistMapper;

import java.time.ZonedDateTime;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.masi.production.domain.MixingReportChecklist}.
 */
@Service
@Transactional
public class MixingReportChecklistService {

    private final Logger log = LoggerFactory.getLogger(MixingReportChecklistService.class);

    private final MixingReportChecklistRepository mixingReportChecklistRepository;

    private final MixingReportChecklistMapper mixingReportChecklistMapper;
    private final WorkOrderRepository workOrderRepository;

    public MixingReportChecklistService(
        MixingReportChecklistRepository mixingReportChecklistRepository,
        MixingReportChecklistMapper mixingReportChecklistMapper,
        WorkOrderRepository workOrderRepository
    ) {
        this.mixingReportChecklistRepository = mixingReportChecklistRepository;
        this.mixingReportChecklistMapper = mixingReportChecklistMapper;
        this.workOrderRepository = workOrderRepository;

    }

    /**
     * Save a mixingReportChecklist.
     *
     * @param dto the entity to save.
     * @return the persisted entity.
     */
    public Mono<MixingReportChecklistDTO> save(MixingReportChecklistDTO dto) {
        log.debug("Request to save MixingReportChecklist : {}", dto);
        MixingReportChecklist entity = dto.toEntity();
        entity.setCreatedAt(ZonedDateTime.now());
        entity.setIsActive(true);
        entity.setLastUpdated(ZonedDateTime.now());
        return mixingReportChecklistRepository
            .save(entity)
            .map(mixingReportChecklistMapper::toDto);
    }

    /**
     * Update a mixingReportChecklist.
     *
     * @param mixingReportChecklistDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<MixingReportChecklistDTO> update(MixingReportChecklistDTO mixingReportChecklistDTO) {
        log.debug("Request to update MixingReportChecklist : {}", mixingReportChecklistDTO);
        return mixingReportChecklistRepository
            .save(mixingReportChecklistMapper.toEntity(mixingReportChecklistDTO).setIsPersisted())
            .map(MixingReportChecklist::toDto);
    }

    /**
     * Partially update a mixingReportChecklist.
     *
     * @param mixingReportChecklistDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<MixingReportChecklistDTO> partialUpdate(MixingReportChecklistDTO mixingReportChecklistDTO) {
        log.debug("Request to partially update MixingReportChecklist : {}", mixingReportChecklistDTO);

        return mixingReportChecklistRepository
            .findById(mixingReportChecklistDTO.getId())
            .map(existingMixingReportChecklist -> {
                mixingReportChecklistDTO.applyUpdateTo(existingMixingReportChecklist);
                existingMixingReportChecklist.setIsPersisted();
                existingMixingReportChecklist.setLastUpdated(ZonedDateTime.now());
                return existingMixingReportChecklist;
            })
            .flatMap(mixingReportChecklistRepository::save)
            .map(MixingReportChecklist::toDto).doFinally(signalType -> {
                log.debug("Updating last updated by work item for work item id: {}", mixingReportChecklistDTO.getWorkItemId());
                workOrderRepository.updateLastUpdatedByWorkItem(mixingReportChecklistDTO.getWorkItemId()).subscribe();
            });
    }

    /**
     * Get all the mixingReportChecklists.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<MixingReportChecklistDTO> findAll(Pageable pageable) {
        log.debug("Request to get all MixingReportChecklists");
        return mixingReportChecklistRepository.findAllBy(pageable).map(mixingReportChecklistMapper::toDto);
    }

    /**
     * Returns the number of mixingReportChecklists available.
     *
     * @return the number of entities in the database.
     */
    public Mono<Long> countAll() {
        return mixingReportChecklistRepository.count();
    }

    /**
     * Get one mixingReportChecklist by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<MixingReportChecklistDTO> findOne(UUID id) {
        log.debug("Request to get MixingReportChecklist : {}", id);
        return mixingReportChecklistRepository.findById(id).map(MixingReportChecklist::toDto);
    }

    /**
     * Delete the mixingReportChecklist by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(UUID id) {
        log.debug("Request to delete MixingReportChecklist : {}", id);

        return mixingReportChecklistRepository
            .findById(id)
            .map(existing -> {
                existing.setIsActive(false);
                existing.setLastUpdated(ZonedDateTime.now());
                existing.setIsPersisted();
                return existing;
            }).flatMap(mixingReportChecklistRepository::save).then();
    }
}
