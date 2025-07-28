package com.masi.employee.service;

import com.masi.employee.domain.ConfirmLeave;
import com.masi.employee.domain.ConfirmLeaveAttachment;
import com.masi.employee.repository.ConfirmLeaveAttachmentRepository;
import com.masi.employee.repository.ConfirmLeaveRepository;
import com.masi.employee.service.dto.ConfirmLeaveDTO;
import com.masi.employee.service.mapper.ConfirmLeaveMapper;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import com.masi.employee.service.web.client.FileClient;
import com.masi.employee.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.masi.employee.domain.ConfirmLeave}.
 */
@Service
@Transactional
public class ConfirmLeaveService {

    private static final Logger log = LoggerFactory.getLogger(ConfirmLeaveService.class);

    private final ConfirmLeaveRepository confirmLeaveRepository;

    private final ConfirmLeaveMapper confirmLeaveMapper;
    private final EmployeeProfileService employeeProfileService;


    private final ConfirmLeaveAttachmentRepository confirmLeaveAttachmentRepository;
    private final FileClient fileClient;

    public ConfirmLeaveService(ConfirmLeaveRepository confirmLeaveRepository, ConfirmLeaveMapper confirmLeaveMapper, EmployeeProfileService employeeProfileService, ConfirmLeaveAttachmentRepository confirmLeaveAttachmentRepository, FileClient fileClient) {
        this.confirmLeaveRepository = confirmLeaveRepository;
        this.confirmLeaveMapper = confirmLeaveMapper;
        this.employeeProfileService = employeeProfileService;
        this.confirmLeaveAttachmentRepository = confirmLeaveAttachmentRepository;
        this.fileClient = fileClient;
    }


    public Mono<ConfirmLeaveDTO> save(ConfirmLeaveDTO confirmLeaveDTO) {
        log.debug("Request to save ConfirmLeave : {}", confirmLeaveDTO);
        var entity = confirmLeaveMapper.toEntity(confirmLeaveDTO);
        return
            employeeProfileService.findOne(confirmLeaveDTO.getId())
                .switchIfEmpty(Mono.error(new BadRequestAlertException("Not found profile", "confirmLeave", "idnull")))
                .zipWith(Mono.defer(() ->
                    findOne(confirmLeaveDTO.getId())
                        .switchIfEmpty(Mono.just(ConfirmLeaveDTO.builder().id(null).build()))
                ))
                .flatMap(tuple -> {
                        var employeeProfileDTO = tuple.getT1();
                        var confirmLeaveDTO1 = tuple.getT2();
                        if (confirmLeaveDTO1.getId() != null) {
                            entity.setIsPersisted();
                        }
                        return confirmLeaveRepository.save(entity).map(confirmLeaveMapper::toDto)
                            .flatMap(e -> employeeProfileService.confirmLeave(employeeProfileDTO.getId()))
                            .zipWith(Mono.defer(() -> {
                                if (confirmLeaveDTO.getFileAttachmentIds() != null && !confirmLeaveDTO.getFileAttachmentIds().isEmpty()) {
                                    Collection<ConfirmLeaveAttachment> confirmLeaveAttachments = confirmLeaveDTO
                                        .getFileAttachmentIds()
                                        .stream()
                                        .map(id -> ConfirmLeaveAttachment
                                            .builder()
                                            .id(id)
                                            .confirmLeaveId(entity.getId())
                                            .fileId(id)
                                            .build())
                                        .toList();
                                    return confirmLeaveAttachmentRepository
                                        .saveAll(confirmLeaveAttachments)
                                        .then(Mono.just(confirmLeaveAttachments));
                                }
                                return Mono.just(List.of());
                            }))
                            .then(Mono.just(confirmLeaveDTO));
                    }
                );
    }

    /**
     * Update a confirmLeave.
     *
     * @param confirmLeaveDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ConfirmLeaveDTO> update(ConfirmLeaveDTO confirmLeaveDTO) {
        log.debug("Request to update ConfirmLeave : {}", confirmLeaveDTO);
        return confirmLeaveRepository.save(confirmLeaveMapper.toEntity(confirmLeaveDTO).setIsPersisted()).map(confirmLeaveMapper::toDto);
    }

    /**
     * Partially update a confirmLeave.
     *
     * @param confirmLeaveDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<ConfirmLeaveDTO> partialUpdate(ConfirmLeaveDTO confirmLeaveDTO) {
        log.debug("Request to partially update ConfirmLeave : {}", confirmLeaveDTO);

        return confirmLeaveRepository
            .findById(confirmLeaveDTO.getId())
            .map(existingConfirmLeave -> {
                confirmLeaveMapper.partialUpdate(existingConfirmLeave, confirmLeaveDTO);

                return existingConfirmLeave;
            })
            .flatMap(confirmLeaveRepository::save)
            .map(confirmLeaveMapper::toDto);
    }

    /**
     * Get all the confirmLeaves.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<ConfirmLeaveDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ConfirmLeaves");
        return confirmLeaveRepository.findAllBy(pageable).map(confirmLeaveMapper::toDto);
    }

    /**
     * Returns the number of confirmLeaves available.
     *
     * @return the number of entities in the database.
     */
    public Mono<Long> countAll() {
        return confirmLeaveRepository.count();
    }

    /**
     * Get one confirmLeave by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<ConfirmLeaveDTO> findOne(UUID id) {
        log.debug("Request to get ConfirmLeave : {}", id);
        return confirmLeaveRepository.findWithDetailById(id).map(ConfirmLeave::toDTO).flatMap(confirmLeaveDTO -> fileClient.getFileAttachmentsByListIds(confirmLeaveDTO.getFileAttachmentIds())
            .collectList()
            .map(fileAttachments -> {
                confirmLeaveDTO.setFileAttachments(fileAttachments);
                return confirmLeaveDTO;
            }));
    }

    /**
     * Delete the confirmLeave by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(UUID id) {
        log.debug("Request to delete ConfirmLeave : {}", id);
        return confirmLeaveRepository.deleteById(id);
    }
}
