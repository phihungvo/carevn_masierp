package com.masi.employee.service;

import com.masi.employee.repository.UniformOrderProcessRepository;
import com.masi.employee.service.dto.UniformOrderProcessDTO;
import com.masi.employee.service.mapper.UniformOrderProcessMapper;

import java.util.List;
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
 * {@link com.masi.employee.domain.UniformOrderProcess}.
 */
@Service
@Transactional
public class UniformOrderProcessService {

    private static final Logger log = LoggerFactory.getLogger(UniformOrderProcessService.class);

    private final UniformOrderProcessRepository uniformOrderProcessRepository;

    private final UniformOrderProcessMapper uniformOrderProcessMapper;

    public UniformOrderProcessService(
            UniformOrderProcessRepository uniformOrderProcessRepository,
            UniformOrderProcessMapper uniformOrderProcessMapper) {
        this.uniformOrderProcessRepository = uniformOrderProcessRepository;
        this.uniformOrderProcessMapper = uniformOrderProcessMapper;
    }

    /**
     * Save a uniformOrderProcess.
     *
     * @param uniformOrderProcessDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<UniformOrderProcessDTO> save(UniformOrderProcessDTO uniformOrderProcessDTO) {
        log.debug("Request to save UniformOrderProcess : {}", uniformOrderProcessDTO);
        return uniformOrderProcessRepository
                .save(uniformOrderProcessMapper.toEntity(uniformOrderProcessDTO))
                .map(uniformOrderProcessMapper::toDto);
    }

    /**
     * Update a uniformOrderProcess.
     *
     * @param uniformOrderProcessDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<UniformOrderProcessDTO> update(UniformOrderProcessDTO uniformOrderProcessDTO) {
        log.debug("Request to update UniformOrderProcess : {}", uniformOrderProcessDTO);
        return uniformOrderProcessRepository
                .save(uniformOrderProcessMapper.toEntity(uniformOrderProcessDTO).setIsPersisted())
                .map(uniformOrderProcessMapper::toDto);
    }

    /**
     * Partially update a uniformOrderProcess.
     *
     * @param uniformOrderProcessDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<UniformOrderProcessDTO> partialUpdate(UniformOrderProcessDTO uniformOrderProcessDTO) {
        log.debug("Request to partially update UniformOrderProcess : {}", uniformOrderProcessDTO);

        return uniformOrderProcessRepository
                .findById(uniformOrderProcessDTO.getId())
                .map(existingUniformOrderProcess -> {
                    uniformOrderProcessMapper.partialUpdate(existingUniformOrderProcess, uniformOrderProcessDTO);

                    return existingUniformOrderProcess;
                })
                .flatMap(uniformOrderProcessRepository::save)
                .map(uniformOrderProcessMapper::toDto);
    }

    /**
     * Get all the uniformOrderProcesses.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<UniformOrderProcessDTO> findAll(Pageable pageable) {
        log.debug("Request to get all UniformOrderProcesses");
        return uniformOrderProcessRepository.findAllBy(pageable).map(uniformOrderProcessMapper::toDto);
    }

    /**
     * Returns the number of uniformOrderProcesses available.
     *
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return uniformOrderProcessRepository.count();
    }

    /**
     * Get one uniformOrderProcess by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<UniformOrderProcessDTO> findOne(UUID id) {
        log.debug("Request to get UniformOrderProcess : {}", id);
        return uniformOrderProcessRepository.findById(id).map(uniformOrderProcessMapper::toDto);
    }

    /**
     * Delete the uniformOrderProcess by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(UUID id) {
        log.debug("Request to delete UniformOrderProcess : {}", id);
        return uniformOrderProcessRepository.deleteById(id);
    }

    // save all uniformOrderProcess
    public Flux<UniformOrderProcessDTO> saveAll(List<UniformOrderProcessDTO> uniformOrderProcessDTO) {
        log.debug("Request to saveAll UniformOrderProcess : {}", uniformOrderProcessDTO);
        return uniformOrderProcessRepository
                .saveAll(uniformOrderProcessMapper.toEntity(uniformOrderProcessDTO))
                .map(uniformOrderProcessMapper::toDto);
    }

    public Flux<UniformOrderProcessDTO> deletedAndCreateList(List<UniformOrderProcessDTO> uniformOrderProcessDTO,
            UUID orderId, String deleteBy) {
        return uniformOrderProcessRepository
                .deleteByUniformOrder(orderId, deleteBy)
                .thenMany(uniformOrderProcessRepository
                        .saveAll(uniformOrderProcessMapper.toEntity(uniformOrderProcessDTO)))
                .map(uniformOrderProcessMapper::toDto);
    }
}
