package com.masi.production.service;

import com.masi.production.domain.ReceiveMaterialChecklist;
import com.masi.production.domain.SteamingProcessChecklist;
import com.masi.production.repository.SteamingProcessChecklistRepository;
import com.masi.production.repository.WorkItemRepository;
import com.masi.production.repository.WorkOrderRepository;
import com.masi.production.service.dto.SteamingProcessChecklistDTO;
import com.masi.production.service.mapper.SteamingProcessChecklistMapper;

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
 * Service Implementation for managing {@link com.masi.production.domain.SteamingProcessChecklist}.
 */
@Service
@Transactional
public class SteamingProcessChecklistService {

    private final Logger log = LoggerFactory.getLogger(SteamingProcessChecklistService.class);

    private final SteamingProcessChecklistRepository steamingProcessChecklistRepository;
    private final WorkOrderRepository workOrderRepository;
    private final SteamingProcessChecklistMapper steamingProcessChecklistMapper;

    public SteamingProcessChecklistService(
        SteamingProcessChecklistRepository steamingProcessChecklistRepository,
        SteamingProcessChecklistMapper steamingProcessChecklistMapper,
        WorkOrderRepository workItemRepository
    ) {
        this.steamingProcessChecklistRepository = steamingProcessChecklistRepository;
        this.steamingProcessChecklistMapper = steamingProcessChecklistMapper;
        this.workOrderRepository = workItemRepository;
    }

    /**
     * Save a steamingProcessChecklist.
     *
     * @param dto the entity to save.
     * @return the persisted entity.
     */
    public Mono<SteamingProcessChecklistDTO> save(SteamingProcessChecklistDTO dto) {
        log.debug("Request to save SteamingProcessChecklist : {}", dto);
        SteamingProcessChecklist entity = dto.toEntity();
        entity.setCreatedAt(ZonedDateTime.now());
        entity.setIsActive(true);
        entity.setLastUpdated(ZonedDateTime.now());

        return steamingProcessChecklistRepository
            .save(entity)
            .map(SteamingProcessChecklist::toDto);
    }

    /**
     * Partially update a steamingProcessChecklist.
     *
     * @param steamingProcessChecklistDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<SteamingProcessChecklistDTO> partialUpdate(SteamingProcessChecklistDTO steamingProcessChecklistDTO) {
        log.debug("Request to partially update SteamingProcessChecklist : {}", steamingProcessChecklistDTO);

        return steamingProcessChecklistRepository
            .findById(steamingProcessChecklistDTO.getId())
            .map(existingSteamingProcessChecklist -> {
                steamingProcessChecklistDTO.applyUpdate(existingSteamingProcessChecklist);
                existingSteamingProcessChecklist.setIsPersisted();
                existingSteamingProcessChecklist.setLastUpdated(ZonedDateTime.now());
                return existingSteamingProcessChecklist;
            })
            .flatMap(steamingProcessChecklistRepository::save)
            .map(SteamingProcessChecklist::toDto).doFinally(signalType -> {
                workOrderRepository.updateLastUpdatedByWorkItem(steamingProcessChecklistDTO.getWorkItemId()).subscribe();
            });
    }

    /**
     * Get all the steamingProcessChecklists.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<SteamingProcessChecklistDTO> findAll(Pageable pageable) {
        log.debug("Request to get all SteamingProcessChecklists");
        return steamingProcessChecklistRepository.findAllBy(pageable).map(SteamingProcessChecklist::toDto);
    }

    /**
     * Returns the number of steamingProcessChecklists available.
     *
     * @return the number of entities in the database.
     */
    public Mono<Long> countAll() {
        return steamingProcessChecklistRepository.count();
    }

    /**
     * Get one steamingProcessChecklist by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<SteamingProcessChecklistDTO> findOne(UUID id) {
        log.debug("Request to get SteamingProcessChecklist : {}", id);
        return steamingProcessChecklistRepository.findById(id).map(steamingProcessChecklistMapper::toDto);
    }

    /**
     * Delete the steamingProcessChecklist by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(UUID id) {
        log.debug("Request to delete SteamingProcessChecklist : {}", id);
        return steamingProcessChecklistRepository.findById(id).map(existing -> {
            existing.setIsActive(false);
            existing.setLastUpdated(ZonedDateTime.now());
            existing.setIsPersisted();
            return existing;
        }).flatMap(steamingProcessChecklistRepository::save).then();
    }
}
