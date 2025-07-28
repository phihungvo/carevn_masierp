package com.masi.employee.service;

import com.masi.employee.domain.LeaveRegimeRequest;
import com.masi.employee.domain.ProcessLeaveRegimeRequest;
import com.masi.employee.domain.enumeration.LeaveRegimeRequestStatus;
import com.masi.employee.repository.LeaveRegimeRequestRepository;
import com.masi.employee.repository.ProcessLeaveRegimeRequestRepository;
import com.masi.employee.service.dto.LeaveRegimeRequestDTO;
import com.masi.employee.service.dto.ProcessLeaveRegimeRequestDTO;
import com.masi.employee.service.mapper.ProcessLeaveRegimeRequestMapper;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing
 * {@link com.masi.employee.domain.ProcessLeaveRegimeRequest}.
 */
@Service
@Transactional
public class ProcessLeaveRegimeRequestService {

    private static final Logger log = LoggerFactory.getLogger(ProcessLeaveRegimeRequestService.class);

    private final ProcessLeaveRegimeRequestRepository processLeaveRegimeRequestRepository;

    private final ProcessLeaveRegimeRequestMapper processLeaveRegimeRequestMapper;
    private final LeaveRegimeRequestRepository leaveRegimeRequestRepository;

    public ProcessLeaveRegimeRequestService(
        ProcessLeaveRegimeRequestRepository processLeaveRegimeRequestRepository,
        ProcessLeaveRegimeRequestMapper processLeaveRegimeRequestMapper, LeaveRegimeRequestRepository leaveRegimeRequestRepository) {
        this.processLeaveRegimeRequestRepository = processLeaveRegimeRequestRepository;
        this.processLeaveRegimeRequestMapper = processLeaveRegimeRequestMapper;
        this.leaveRegimeRequestRepository = leaveRegimeRequestRepository;
    }

    /**
     * Save a processLeaveRegimeRequest.
     *
     * @param processLeaveRegimeRequestDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ProcessLeaveRegimeRequestDTO> save(ProcessLeaveRegimeRequestDTO processLeaveRegimeRequestDTO) {
        log.debug("Request to save ProcessLeaveRegimeRequest : {}", processLeaveRegimeRequestDTO);
        return processLeaveRegimeRequestRepository
            .save(processLeaveRegimeRequestMapper.toEntity(processLeaveRegimeRequestDTO))
            .map(processLeaveRegimeRequestMapper::toDto);
    }

    /**
     * Update a processLeaveRegimeRequest.
     *
     * @param processLeaveRegimeRequestDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ProcessLeaveRegimeRequestDTO> update(ProcessLeaveRegimeRequestDTO processLeaveRegimeRequestDTO) {
        log.debug("Request to update ProcessLeaveRegimeRequest : {}", processLeaveRegimeRequestDTO);
        return processLeaveRegimeRequestRepository
            .save(processLeaveRegimeRequestMapper.toEntity(processLeaveRegimeRequestDTO).setIsPersisted())
            .map(processLeaveRegimeRequestMapper::toDto);
    }

    /**
     * Partially update a processLeaveRegimeRequest.
     *
     * @param processLeaveRegimeRequestDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<ProcessLeaveRegimeRequestDTO> partialUpdate(ProcessLeaveRegimeRequestDTO processLeaveRegimeRequestDTO) {
        log.debug("Request to partially update ProcessLeaveRegimeRequest : {}", processLeaveRegimeRequestDTO);

        return processLeaveRegimeRequestRepository
            .findById(processLeaveRegimeRequestDTO.getId())
            .map(existingProcessLeaveRegimeRequest -> {
                processLeaveRegimeRequestMapper.partialUpdate(existingProcessLeaveRegimeRequest,
                    processLeaveRegimeRequestDTO);

                return existingProcessLeaveRegimeRequest;
            })
            .flatMap(processLeaveRegimeRequestRepository::save)
            .map(processLeaveRegimeRequestMapper::toDto);
    }

    /**
     * Get all the processLeaveRegimeRequests.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<ProcessLeaveRegimeRequestDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ProcessLeaveRegimeRequests");
        return processLeaveRegimeRequestRepository.findAllBy(pageable).map(processLeaveRegimeRequestMapper::toDto);
    }

    /**
     * Returns the number of processLeaveRegimeRequests available.
     *
     * @return the number of entities in the database.
     */
    public Mono<Long> countAll() {
        return processLeaveRegimeRequestRepository.count();
    }

    /**
     * Get one processLeaveRegimeRequest by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<ProcessLeaveRegimeRequestDTO> findOne(UUID id) {
        log.debug("Request to get ProcessLeaveRegimeRequest : {}", id);
        return processLeaveRegimeRequestRepository.findById(id).map(processLeaveRegimeRequestMapper::toDto);
    }

    /**
     * Delete the processLeaveRegimeRequest by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(UUID id) {
        log.debug("Request to delete ProcessLeaveRegimeRequest : {}", id);
        return processLeaveRegimeRequestRepository.deleteById(id);
    }

    // create function to save all entities
    public Flux<ProcessLeaveRegimeRequestDTO> saveAllRequests(ArrayList<ProcessLeaveRegimeRequestDTO> a) {
        return
            leaveRegimeRequestRepository.updateStatusById(a.get(0).getLeaveRegimeRequestId(), LeaveRegimeRequestStatus.WAITING_APPROVAL)
                .thenMany(
                    processLeaveRegimeRequestRepository.saveAll(processLeaveRegimeRequestMapper.toEntity(a))
                        .map(processLeaveRegimeRequestMapper::toDto)
                );
    }

    // public Flux<LeaveRegimeRequestDTO> findAllByLeaveRegimeRequestId(UUID id) {
    // // TODO Auto-generated method stub
    // return
    // processLeaveRegimeRequestRepository.findByLeaveRegimeRequestIdAndIsDeletedIsFalse(id)
    // .map(processLeaveRegimeRequestMapper::toDto)
    // .cast(LeaveRegimeRequestDTO.class);
    // }

    public Flux<ProcessLeaveRegimeRequestDTO> findAllProcessByLeaveRegimeRequestId(UUID id) {
        // TODO Auto-generated method stub
        return processLeaveRegimeRequestRepository.findByLeaveRegimeRequestIdAndIsDeletedIsFalseQuery(id)
            .map(processLeaveRegimeRequestMapper::toDto);
    }

    public Mono<Void> markAsDeleted(UUID id, UUID updatedBy) {
        // TODO Auto-generated method stub

        return processLeaveRegimeRequestRepository.findByLeaveRegimeRequestIdAndIsDeletedIsFalse(id)
            .flatMap(exist -> {
                exist.setIsDeleted(true);
                exist.setDeletedAt(ZonedDateTime.now());
                exist.setDeletedBy(updatedBy);
                return processLeaveRegimeRequestRepository.save(exist);
            }).then();
    }
}
