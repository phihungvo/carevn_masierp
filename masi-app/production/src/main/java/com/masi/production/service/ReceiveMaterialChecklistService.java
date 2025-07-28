package com.masi.production.service;

import com.masi.production.domain.MixingReportChecklist;
import com.masi.production.domain.ReceiveMaterialChecklist;
import com.masi.production.domain.WorkOrder;
import com.masi.production.repository.ReceiveMaterialChecklistRepository;
import com.masi.production.repository.WorkItemRepository;
import com.masi.production.repository.WorkOrderRepository;
import com.masi.production.service.dto.ReceiveMaterialChecklistDTO;
import com.masi.production.service.mapper.ReceiveMaterialChecklistMapper;

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
 * Service Implementation for managing {@link com.masi.production.domain.ReceiveMaterialChecklist}.
 */
@Service
@Transactional
public class ReceiveMaterialChecklistService {

    private final Logger log = LoggerFactory.getLogger(ReceiveMaterialChecklistService.class);

    private final ReceiveMaterialChecklistRepository receiveMaterialChecklistRepository;
    private final WorkOrderRepository workOrderRepository;
    private final ReceiveMaterialChecklistMapper receiveMaterialChecklistMapper;

    public ReceiveMaterialChecklistService(
        ReceiveMaterialChecklistRepository receiveMaterialChecklistRepository,
        ReceiveMaterialChecklistMapper receiveMaterialChecklistMapper,
        WorkOrderRepository workItemRepository
    ) {
        this.receiveMaterialChecklistRepository = receiveMaterialChecklistRepository;
        this.receiveMaterialChecklistMapper = receiveMaterialChecklistMapper;
        this.workOrderRepository = workItemRepository;
    }

    /**
     * Save a receiveMaterialChecklist.
     *
     * @param dto the entity to save.
     * @return the persisted entity.
     */
    public Mono<ReceiveMaterialChecklistDTO> save(ReceiveMaterialChecklistDTO dto) {
        log.debug("Request to save ReceiveMaterialChecklist : {}", dto);
        ReceiveMaterialChecklist entity = dto.toEntity();
        entity.setCreatedAt(ZonedDateTime.now());
        entity.setIsActive(true);
        entity.setLastUpdated(ZonedDateTime.now());
        return receiveMaterialChecklistRepository
            .save(entity)
            .map(ReceiveMaterialChecklist::toDto);
    }

    /**
     * Partially update a receiveMaterialChecklist.
     *
     * @param receiveMaterialChecklistDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<ReceiveMaterialChecklistDTO> partialUpdate(ReceiveMaterialChecklistDTO receiveMaterialChecklistDTO) {
        log.debug("Request to partially update ReceiveMaterialChecklist : {}", receiveMaterialChecklistDTO);

        return receiveMaterialChecklistRepository
            .findById(receiveMaterialChecklistDTO.getId())
            .map(existingReceiveMaterialChecklist -> {
                existingReceiveMaterialChecklist.setIsPersisted();
                receiveMaterialChecklistDTO.applyUpdate(existingReceiveMaterialChecklist);
                existingReceiveMaterialChecklist.setLastUpdated(ZonedDateTime.now());
                return existingReceiveMaterialChecklist;
            })
            .flatMap(receiveMaterialChecklistRepository::save)
            .map(ReceiveMaterialChecklist::toDto).doFinally(signalType -> {
                workOrderRepository.updateLastUpdatedByWorkItem(receiveMaterialChecklistDTO.getWorkItemId()).subscribe();
            });
    }

    /**
     * Get all the receiveMaterialChecklists.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<ReceiveMaterialChecklistDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ReceiveMaterialChecklists");
        return receiveMaterialChecklistRepository.findAllBy(pageable).map(receiveMaterialChecklistMapper::toDto);
    }

    /**
     * Returns the number of receiveMaterialChecklists available.
     *
     * @return the number of entities in the database.
     */
    public Mono<Long> countAll() {
        return receiveMaterialChecklistRepository.count();
    }

    /**
     * Get one receiveMaterialChecklist by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<ReceiveMaterialChecklistDTO> findOne(UUID id) {
        log.debug("Request to get ReceiveMaterialChecklist : {}", id);
        return receiveMaterialChecklistRepository.findById(id).map(ReceiveMaterialChecklist::toDto);
    }

    /**
     * Delete the receiveMaterialChecklist by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(UUID id) {
        log.debug("Request to delete ReceiveMaterialChecklist : {}", id);
        return receiveMaterialChecklistRepository.findById(id).map(existing -> {
            existing.setIsActive(false);
            existing.setLastUpdated(ZonedDateTime.now());
            existing.setIsPersisted();
            return existing;
        }).flatMap(receiveMaterialChecklistRepository::save).then();
    }
}
