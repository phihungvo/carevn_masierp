package com.masi.production.service;

import com.masi.production.domain.WorkItem;
import com.masi.production.repository.WorkItemRepository;
import com.masi.production.service.dto.WorkItemDTO;
import com.masi.production.service.mapper.WorkItemMapper;

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
 * Service Implementation for managing {@link com.masi.production.domain.WorkItem}.
 */
@Service
@Transactional
public class WorkItemService {

    private final Logger log = LoggerFactory.getLogger(WorkItemService.class);

    private final WorkItemRepository workItemRepository;

    private final WorkItemMapper workItemMapper;

    public WorkItemService(WorkItemRepository workItemRepository, WorkItemMapper workItemMapper) {
        this.workItemRepository = workItemRepository;
        this.workItemMapper = workItemMapper;
    }

    /**
     * Save a workItem.
     *
     * @param workItemDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<WorkItemDTO> save(WorkItemDTO workItemDTO) {
        log.debug("Request to save WorkItem : {}", workItemDTO);
        return workItemRepository.save(workItemMapper.toEntity(workItemDTO)).map(workItemMapper::toDto);
    }

    public Mono<WorkItemDTO> generateNew() {
        return this.generateNew(UUID.randomUUID());
    }
    public Mono<WorkItemDTO> generateNew(UUID initId) {
        log.debug("Generating new WorkItem...");
        WorkItemDTO workItemDTO = new WorkItemDTO(initId, true, ZonedDateTime.now());
        return this.save(workItemDTO);
    }

    /**
     * Update a workItem.
     *
     * @param workItemDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<WorkItemDTO> update(WorkItemDTO workItemDTO) {
        log.debug("Request to update WorkItem : {}", workItemDTO);
        return workItemRepository.save(workItemMapper.toEntity(workItemDTO).setIsPersisted()).map(WorkItem::toDto);
    }

    /**
     * Partially update a workItem.
     *
     * @param workItemDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<WorkItemDTO> partialUpdate(WorkItemDTO workItemDTO) {
        log.debug("Request to partially update WorkItem : {}", workItemDTO);

        return workItemRepository
            .findById(workItemDTO.getId())
            .map(existingWorkItem -> {
                workItemMapper.partialUpdate(existingWorkItem, workItemDTO);

                return existingWorkItem;
            })
            .flatMap(workItemRepository::save)
            .map(WorkItem::toDto);
    }

    /**
     * Get all the workItems.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<WorkItemDTO> findAll(Pageable pageable) {
        log.debug("Request to get all WorkItems");
        return workItemRepository.findAllBy(pageable).map(WorkItem::toDto);
    }

    /**
     *  Get all the workItems where WorkOrder is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<WorkItemDTO> findAllWhereWorkOrderIsNull() {
        log.debug("Request to get all workItems where WorkOrder is null");
        return workItemRepository.findAllWhereWorkOrderIsNull().map(WorkItem::toDto);
    }

    /**
     * Returns the number of workItems available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return workItemRepository.count();
    }

    /**
     * Get one workItem by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<WorkItemDTO> findOne(UUID id) {
        log.debug("Request to get WorkItem : {}", id);
        return workItemRepository.findById(id).map(WorkItem::toDto);
    }

    /**
     * Delete the workItem by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(UUID id) {
        log.debug("Request to delete WorkItem : {}", id);

        return workItemRepository
            .findById(id)
            .flatMap(existing -> {
                existing.setIsActive(false);
                existing.setIsPersisted();
                existing.setLastUpdated(ZonedDateTime.now());

                return workItemRepository.save(existing);
            })
            .then();
    }
}
