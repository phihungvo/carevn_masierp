package com.masi.production.service;

import com.masi.production.domain.AdditiveMaterialChecklist;
import com.masi.production.domain.ReleaseWarehouse;
import com.masi.production.repository.AdditiveMaterialChecklistRepository;
import com.masi.production.repository.ReleaseWarehouseRepository;
import com.masi.production.repository.WorkItemRepository;
import com.masi.production.repository.WorkOrderRepository;
import com.masi.production.service.dto.AdditiveMaterialChecklistDTO;
import com.masi.production.service.dto.ReleaseWarehouseDTO;
import com.masi.production.service.mapper.AdditiveMaterialChecklistMapper;

import java.time.ZonedDateTime;
import java.util.UUID;

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
 * Service Implementation for managing {@link com.masi.production.domain.AdditiveMaterialChecklist}.
 */
@Service
@Transactional
public class AdditiveMaterialChecklistService {

    private final Logger log = LoggerFactory.getLogger(AdditiveMaterialChecklistService.class);

    private final AdditiveMaterialChecklistRepository additiveMaterialChecklistRepository;
    private final WorkOrderRepository workOrderRepository;
    private final AdditiveMaterialChecklistMapper additiveMaterialChecklistMapper;
    private final ReleaseWarehouseRepository releaseWarehouseRepository;

    public AdditiveMaterialChecklistService(
        AdditiveMaterialChecklistRepository additiveMaterialChecklistRepository,
        AdditiveMaterialChecklistMapper additiveMaterialChecklistMapper,
        WorkOrderRepository workItemRepository, ReleaseWarehouseRepository releaseWarehouseRepository
    ) {
        this.additiveMaterialChecklistRepository = additiveMaterialChecklistRepository;
        this.additiveMaterialChecklistMapper = additiveMaterialChecklistMapper;
        this.workOrderRepository = workItemRepository;
        this.releaseWarehouseRepository = releaseWarehouseRepository;
    }

    /**
     * Save a additiveMaterialChecklist.
     *
     * @param dto the entity to save.
     * @return the persisted entity.
     */
    public Mono<AdditiveMaterialChecklistDTO> save(AdditiveMaterialChecklistDTO dto) {
        log.debug("Request to save AdditiveMaterialChecklist : {}", dto);
        AdditiveMaterialChecklist entity = dto.toEntity();
        entity.setCreatedAt(ZonedDateTime.now());
        entity.setIsActive(true);
        entity.setLastUpdated(ZonedDateTime.now());
        return additiveMaterialChecklistRepository
            .save(entity)
            .map(AdditiveMaterialChecklist::toDto);
    }

    /**
     * Update a additiveMaterialChecklist.
     *
     * @param additiveMaterialChecklistDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<AdditiveMaterialChecklistDTO> update(AdditiveMaterialChecklistDTO additiveMaterialChecklistDTO) {
        log.debug("Request to update AdditiveMaterialChecklist : {}", additiveMaterialChecklistDTO);
        return additiveMaterialChecklistRepository
            .save(additiveMaterialChecklistMapper.toEntity(additiveMaterialChecklistDTO).setIsPersisted())
            .map(AdditiveMaterialChecklist::toDto);
    }

    /**
     * Partially update a additiveMaterialChecklist.
     *
     * @param dto the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<AdditiveMaterialChecklistDTO> partialUpdate(AdditiveMaterialChecklistDTO dto) {
        log.debug("Request to partially update AdditiveMaterialChecklist : {}", dto);
        return additiveMaterialChecklistRepository
            .findById(dto.getId())
            .map(existingAdditiveMaterialChecklist -> {
                dto.applyUpdate(existingAdditiveMaterialChecklist);
                existingAdditiveMaterialChecklist.setIsPersisted();
                existingAdditiveMaterialChecklist.setLastUpdated(ZonedDateTime.now());
                return existingAdditiveMaterialChecklist;
            })
            .flatMap(additiveMaterialChecklistRepository::save)
            .map(AdditiveMaterialChecklist::toDto).doFinally(signalType -> {
                workOrderRepository.updateLastUpdatedByWorkItem(dto.getWorkItemId()).subscribe();
            }).flatMap(additiveMaterialChecklistDTO -> {
                return releaseWarehouseRepository.changeIsDeletedAllByMaterialAdditiveId(dto.getId(), false).then(
                    Mono.defer(() ->{
                        return releaseWarehouseRepository.saveAll(dto.toReleaseWarehouseEntity()).map(ReleaseWarehouse::toDto).collectList().map(releaseWarehouseDTOS -> {
                            additiveMaterialChecklistDTO.setReleaseWarehouseDTO(releaseWarehouseDTOS);
                            return additiveMaterialChecklistDTO;
                        });
                    })
                );
            });

    }



    /**
     * Get all the additiveMaterialChecklists.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<AdditiveMaterialChecklistDTO> findAll(Pageable pageable) {
        log.debug("Request to get all AdditiveMaterialChecklists");
        return additiveMaterialChecklistRepository.findAllBy(pageable).map(AdditiveMaterialChecklist::toDto);
    }

    /**
     * Returns the number of additiveMaterialChecklists available.
     *
     * @return the number of entities in the database.
     */
    public Mono<Long> countAll() {
        return additiveMaterialChecklistRepository.count();
    }

    /**
     * Get one additiveMaterialChecklist by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<AdditiveMaterialChecklistDTO> findOne(UUID id) {
        log.debug("Request to get AdditiveMaterialChecklist : {}", id);
        return additiveMaterialChecklistRepository.findById(id)
            .map(e -> {
                return e.toDto();
            });
    }

    /**
     * Delete the additiveMaterialChecklist by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(UUID id) {
        log.debug("Request to delete AdditiveMaterialChecklist : {}", id);
        return additiveMaterialChecklistRepository.findById(id).map(existing -> {
            existing.setIsActive(false);
            existing.setLastUpdated(ZonedDateTime.now());
            existing.setIsPersisted();
            return existing;
        }).flatMap(additiveMaterialChecklistRepository::save).then();
    }

    public Flux<ReleaseWarehouseDTO> getReleaseWarehouseByMaterialAdditiveChecklistId(Pageable pageable, UUID materialAdditiveChecklistId) {
        // default sort by created date
        if (pageable.getSort().isUnsorted()) {
            log.debug("Sorting by createdAt desc");
            Sort sort = Sort.by(Sort.Order.desc("createdDate"));
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        }
        return releaseWarehouseRepository.findAllByMaterialAdditiveChecklistIdAndIsDeleted(pageable.getPageNumber(), pageable.getPageSize(),materialAdditiveChecklistId, false).map(ReleaseWarehouse::toDto);
    }

    public Mono<Long> countReleaseWarehouseByMaterialAdditiveChecklistId(UUID materialAdditiveChecklistId) {
        return releaseWarehouseRepository.countAllByMaterialAdditiveChecklistIdAndIsDeleted(materialAdditiveChecklistId, false);
    }
}
