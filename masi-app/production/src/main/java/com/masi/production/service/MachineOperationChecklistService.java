package com.masi.production.service;

import com.masi.production.domain.MachineOperationChecklist;
import com.masi.production.repository.MachineOperationChecklistRepository;
import com.masi.production.repository.WorkItemRepository;
import com.masi.production.repository.WorkOrderRepository;
import com.masi.production.service.dto.MachineOperationChecklistDTO;
import com.masi.production.service.mapper.MachineOperationChecklistMapper;

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
 * Service Implementation for managing {@link com.masi.production.domain.MachineOperationChecklist}.
 */
@Service
@Transactional
public class MachineOperationChecklistService {

    private final Logger log = LoggerFactory.getLogger(MachineOperationChecklistService.class);

    private final MachineOperationChecklistRepository machineOperationChecklistRepository;

    private final MachineOperationChecklistMapper machineOperationChecklistMapper;
    private final WorkOrderRepository workOrderRepository;

    public MachineOperationChecklistService(
        MachineOperationChecklistRepository machineOperationChecklistRepository,
        MachineOperationChecklistMapper machineOperationChecklistMapper,
        WorkOrderRepository workOrderRepository
    ) {
        this.machineOperationChecklistRepository = machineOperationChecklistRepository;
        this.machineOperationChecklistMapper = machineOperationChecklistMapper;
        this.workOrderRepository = workOrderRepository;
    }

    /**
     * Save a machineOperationChecklist.
     *
     * @param dto the entity to save.
     * @return the persisted entity.
     */
    public Mono<MachineOperationChecklistDTO> save(MachineOperationChecklistDTO dto) {
        log.debug("Request to save MachineOperationChecklist : {}", dto);
        MachineOperationChecklist entity = dto.toEntity();
        entity.setCreatedAt(ZonedDateTime.now());
        entity.setIsActive(true);
        entity.setLastUpdated(ZonedDateTime.now());
        return machineOperationChecklistRepository
            .save(entity)
            .map(MachineOperationChecklist::toDto);
    }

    /**
     * Update a machineOperationChecklist.
     *
     * @param machineOperationChecklistDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<MachineOperationChecklistDTO> update(MachineOperationChecklistDTO machineOperationChecklistDTO) {
        log.debug("Request to update MachineOperationChecklist : {}", machineOperationChecklistDTO);
        return machineOperationChecklistRepository
            .save(machineOperationChecklistMapper.toEntity(machineOperationChecklistDTO).setIsPersisted())
            .map(machineOperationChecklistMapper::toDto);
    }

    /**
     * Partially update a machineOperationChecklist.
     *
     * @param machineOperationChecklistDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<MachineOperationChecklistDTO> partialUpdate(MachineOperationChecklistDTO machineOperationChecklistDTO) {
        log.debug("Request to partially update MachineOperationChecklist : {}", machineOperationChecklistDTO);

        return machineOperationChecklistRepository
            .findById(machineOperationChecklistDTO.getId())
            .map(existingMachineOperationChecklist -> {
                machineOperationChecklistDTO.applyUpdateTo(existingMachineOperationChecklist);
                existingMachineOperationChecklist.setIsPersisted();
                existingMachineOperationChecklist.setLastUpdated(ZonedDateTime.now());
                return existingMachineOperationChecklist;
            })
            .flatMap(machineOperationChecklistRepository::save)
            .map(machineOperationChecklistMapper::toDto).doFinally(signalType -> {
                workOrderRepository.updateLastUpdatedByWorkItem(machineOperationChecklistDTO.getWorkItemId()).subscribe();
            });
    }

    /**
     * Get all the machineOperationChecklists.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<MachineOperationChecklistDTO> findAll(Pageable pageable) {
        log.debug("Request to get all MachineOperationChecklists");
        return machineOperationChecklistRepository.findAllBy(pageable).map(machineOperationChecklistMapper::toDto);
    }

    /**
     * Returns the number of machineOperationChecklists available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return machineOperationChecklistRepository.count();
    }

    /**
     * Get one machineOperationChecklist by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<MachineOperationChecklistDTO> findOne(UUID id) {
        log.debug("Request to get MachineOperationChecklist : {}", id);
        return machineOperationChecklistRepository.findById(id).map(machineOperationChecklistMapper::toDto);
    }

    /**
     * Delete the machineOperationChecklist by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(UUID id) {
        log.debug("Request soft to delete MachineOperationChecklist : {}", id);
        return machineOperationChecklistRepository
            .findById(id)
            .map(existing -> {
                existing.setIsActive(false);
                existing.setLastUpdated(ZonedDateTime.now());
                existing.setIsPersisted();
                return existing;
            }).flatMap(machineOperationChecklistRepository::save).then();
    }
}
