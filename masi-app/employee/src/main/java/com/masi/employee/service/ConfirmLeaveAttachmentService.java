package com.masi.employee.service;

import com.masi.employee.repository.ConfirmLeaveAttachmentRepository;
import com.masi.employee.service.dto.ConfirmLeaveAttachmentDTO;
import com.masi.employee.service.mapper.ConfirmLeaveAttachmentMapper;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.masi.employee.domain.ConfirmLeaveAttachment}.
 */
@Service
@Transactional
public class ConfirmLeaveAttachmentService {

    private static final Logger log = LoggerFactory.getLogger(ConfirmLeaveAttachmentService.class);

    private final ConfirmLeaveAttachmentRepository confirmLeaveAttachmentRepository;

    private final ConfirmLeaveAttachmentMapper confirmLeaveAttachmentMapper;

    public ConfirmLeaveAttachmentService(
        ConfirmLeaveAttachmentRepository confirmLeaveAttachmentRepository,
        ConfirmLeaveAttachmentMapper confirmLeaveAttachmentMapper
    ) {
        this.confirmLeaveAttachmentRepository = confirmLeaveAttachmentRepository;
        this.confirmLeaveAttachmentMapper = confirmLeaveAttachmentMapper;
    }

    /**
     * Save a confirmLeaveAttachment.
     *
     * @param confirmLeaveAttachmentDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ConfirmLeaveAttachmentDTO> save(ConfirmLeaveAttachmentDTO confirmLeaveAttachmentDTO) {
        log.debug("Request to save ConfirmLeaveAttachment : {}", confirmLeaveAttachmentDTO);
        confirmLeaveAttachmentDTO.setId(confirmLeaveAttachmentDTO.getFileId());
        return confirmLeaveAttachmentRepository
            .save(confirmLeaveAttachmentMapper.toEntity(confirmLeaveAttachmentDTO))
            .map(confirmLeaveAttachmentMapper::toDto);
    }

    /**
     * Update a confirmLeaveAttachment.
     *
     * @param confirmLeaveAttachmentDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ConfirmLeaveAttachmentDTO> update(ConfirmLeaveAttachmentDTO confirmLeaveAttachmentDTO) {
        log.debug("Request to update ConfirmLeaveAttachment : {}", confirmLeaveAttachmentDTO);
        return confirmLeaveAttachmentRepository
            .save(confirmLeaveAttachmentMapper.toEntity(confirmLeaveAttachmentDTO).setIsPersisted())
            .map(confirmLeaveAttachmentMapper::toDto);
    }

    /**
     * Partially update a confirmLeaveAttachment.
     *
     * @param confirmLeaveAttachmentDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<ConfirmLeaveAttachmentDTO> partialUpdate(ConfirmLeaveAttachmentDTO confirmLeaveAttachmentDTO) {
        log.debug("Request to partially update ConfirmLeaveAttachment : {}", confirmLeaveAttachmentDTO);

        return confirmLeaveAttachmentRepository
            .findById(confirmLeaveAttachmentDTO.getId())
            .map(existingConfirmLeaveAttachment -> {
                confirmLeaveAttachmentMapper.partialUpdate(existingConfirmLeaveAttachment, confirmLeaveAttachmentDTO);

                return existingConfirmLeaveAttachment;
            })
            .flatMap(confirmLeaveAttachmentRepository::save)
            .map(confirmLeaveAttachmentMapper::toDto);
    }

    /**
     * Get all the confirmLeaveAttachments.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<ConfirmLeaveAttachmentDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ConfirmLeaveAttachments");
        return confirmLeaveAttachmentRepository.findAllBy(pageable).map(confirmLeaveAttachmentMapper::toDto);
    }

    /**
     * Returns the number of confirmLeaveAttachments available.
     *
     * @return the number of entities in the database.
     */
    public Mono<Long> countAll() {
        return confirmLeaveAttachmentRepository.count();
    }

    /**
     * Get one confirmLeaveAttachment by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<ConfirmLeaveAttachmentDTO> findOne(UUID id) {
        log.debug("Request to get ConfirmLeaveAttachment : {}", id);
        return confirmLeaveAttachmentRepository.findById(id).map(confirmLeaveAttachmentMapper::toDto);
    }

    /**
     * Delete the confirmLeaveAttachment by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(UUID id) {
        log.debug("Request to delete ConfirmLeaveAttachment : {}", id);
        return confirmLeaveAttachmentRepository.deleteById(id);
    }
}
