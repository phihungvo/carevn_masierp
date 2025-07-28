package com.masi.production.service;

import com.masi.production.domain.WorkCenter;
import com.masi.production.domain.enumeration.WorkCenterStatusEnum;
import com.masi.production.repository.WorkCenterRepository;
import com.masi.production.service.dto.WorkCenterDTO;
import com.masi.production.service.mapper.WorkCenterMapper;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import com.masi.production.web.rest.errors.BadRequestAlertException;
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
 * Service Implementation for managing
 * {@link com.masi.production.domain.WorkCenter}.
 */
@Service
@Transactional
public class WorkCenterService {

    private final Logger log = LoggerFactory.getLogger(WorkCenterService.class);

    private final WorkCenterRepository workCenterRepository;

    private final WorkCenterMapper workCenterMapper;

    public WorkCenterService(WorkCenterRepository workCenterRepository, WorkCenterMapper workCenterMapper) {
        this.workCenterRepository = workCenterRepository;
        this.workCenterMapper = workCenterMapper;
    }

    /**
     * Save a workCenter.
     *
     * @param workCenterDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<WorkCenterDTO> save(WorkCenterDTO workCenterDTO) {
        log.debug("Request to save WorkCenter : {}", workCenterDTO);
        return workCenterRepository.countAllByCode(workCenterDTO.getCode())
            .flatMap(count -> {
                if (count > 0) {
                    return Mono.error(new BadRequestAlertException("A new workCenter cannot already have a code", "workCenter", "CODE_EXISTS"));
                }
                return workCenterRepository.save(workCenterMapper.toEntity(workCenterDTO)).map(workCenterMapper::toDto);
            });
    }

    /**
     * Update a workCenter.
     *
     * @param workCenterDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<WorkCenterDTO> update(WorkCenterDTO workCenterDTO) {
        log.debug("Request to update WorkCenter : {}", workCenterDTO);
        return workCenterRepository.save(workCenterMapper.toEntity(workCenterDTO).setIsPersisted())
            .map(workCenterMapper::toDto);
    }

    /**
     * Partially update a workCenter.
     *
     * @param workCenterDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<WorkCenterDTO> partialUpdate(WorkCenterDTO workCenterDTO, String updatedBy) {
        log.debug("Request to partially update WorkCenter : {}", workCenterDTO);

        return workCenterRepository
            .findById(workCenterDTO.getId())
            .map(existingWorkCenter -> {
                workCenterDTO.setCode(existingWorkCenter.getCode());
                workCenterMapper.partialUpdate(existingWorkCenter, workCenterDTO);
                return existingWorkCenter.setIsPersisted();
            })
            .flatMap(workCenterRepository::save)
            .map(workCenterMapper::toDto);
    }

    /**
     * Get all the workCenters.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<WorkCenterDTO> findAll(Pageable pageable) {
        log.debug("Request to get all WorkCenters");
        return workCenterRepository.findAllBy(pageable).map(workCenterMapper::toDto);
    }

    /**
     * Returns the number of workCenters available.
     *
     * @return the number of entities in the database.
     */
    public Mono<Long> countAll() {
        return workCenterRepository.count();
    }

    /**
     * Get one workCenter by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<WorkCenterDTO> findOne(UUID id) {
        log.debug("Request to get WorkCenter : {}", id);
        return workCenterRepository.findById(id).map(workCenterMapper::toDto);
    }

    /**
     * Delete the workCenter by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(UUID id) {
        log.debug("Request to delete WorkCenter : {}", id);
        return workCenterRepository.deleteById(id);
    }

    public Mono<Void> softDelete(UUID id, String deletedBy) {
        log.debug("Request to soft delete WorkCenter : {}", id);
        return workCenterRepository.softDeleteById(id, deletedBy, ZonedDateTime.now());
    }


    private Pageable getDefaultSortIfUnsorted(Pageable pageable) {
        Sort defaultSort = Sort.by(Sort.Direction.DESC, "created_at");
        if (Objects.isNull(pageable)) {
            pageable = PageRequest.of(0, Integer.MAX_VALUE, defaultSort);
        }
        if (pageable.getSort().equals(Sort.unsorted())) {
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), defaultSort);
        }
        return pageable;
    }

    public Flux<WorkCenterDTO> GetAllWithFilter(Pageable pageable, String search, List<WorkCenterStatusEnum> status,
                                                UUID companyId, String company, String department) {
        pageable = getDefaultSortIfUnsorted(pageable);
        return workCenterRepository.GetAllWithFilter(pageable, search, status, companyId, company, department).map(workCenterMapper::toDto);
    }

    /**
     * Count all the workCenters with filter.
     *
     * @param search
     * @param status
     * @param companyId
     * @return
     */
    public Mono<Long> CountWithFilter(String search, List<WorkCenterStatusEnum> status, UUID companyId, String company, String department) {
        return workCenterRepository.CountWithFilter(search, status, companyId, company, department);
    }
}
