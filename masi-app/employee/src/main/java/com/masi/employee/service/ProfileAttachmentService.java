package com.masi.employee.service;

import com.masi.employee.domain.EmployeeProfile;
import com.masi.employee.domain.ProfileAttachment;
import com.masi.employee.domain.enumeration.ProfileAttachmentType;
import com.masi.employee.repository.EmployeeProfileRepository;
import com.masi.employee.repository.ProfileAttachmentRepository;
import com.masi.employee.service.dto.ProfileAttachmentDTO;
import com.masi.employee.service.mapper.ProfileAttachmentMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;

import com.masi.employee.service.web.client.FileClient;
import com.masi.employee.web.rest.errors.BadRequestAlertException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.service.filter.LocalDateFilter;

/**
 * Service Implementation for managing {@link com.masi.employee.domain.ProfileAttachment}.
 */
@Service
@Transactional
public class ProfileAttachmentService {

    private static final Logger log = LoggerFactory.getLogger(ProfileAttachmentService.class);

    private final ProfileAttachmentRepository profileAttachmentRepository;

    private final ProfileAttachmentMapper profileAttachmentMapper;
    private final EmployeeProfileRepository employeeProfileRepository;
    private final FileClient fileClient;

    public ProfileAttachmentService(
        ProfileAttachmentRepository profileAttachmentRepository,
        ProfileAttachmentMapper profileAttachmentMapper, EmployeeProfileRepository employeeProfileRepository, FileClient fileClient) {
        this.profileAttachmentRepository = profileAttachmentRepository;
        this.employeeProfileRepository = employeeProfileRepository;
        this.profileAttachmentMapper = profileAttachmentMapper;
        this.fileClient = fileClient;
    }


    /**
     * Save a profileAttachment.
     *
     * @param profileAttachmentDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ProfileAttachmentDTO> save(ProfileAttachmentDTO profileAttachmentDTO) {
        log.debug("Request to save ProfileAttachment : {}", profileAttachmentDTO);
        return profileAttachmentRepository.save(profileAttachmentMapper.toEntity(profileAttachmentDTO)).map(profileAttachmentMapper::toDto);
    }

    /**
     * Update a profileAttachment.
     *
     * @param profileAttachmentDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ProfileAttachmentDTO> update(ProfileAttachmentDTO profileAttachmentDTO) {
        log.debug("Request to update ProfileAttachment : {}", profileAttachmentDTO);
        return profileAttachmentRepository
            .save(profileAttachmentMapper.toEntity(profileAttachmentDTO).setIsPersisted())
            .map(profileAttachmentMapper::toDto);
    }

    /**
     * Partially update a profileAttachment.
     *
     * @param profileAttachmentDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<ProfileAttachmentDTO> partialUpdate(ProfileAttachmentDTO profileAttachmentDTO) {
        log.debug("Request to partially update ProfileAttachment : {}", profileAttachmentDTO);

        return profileAttachmentRepository
            .findById(profileAttachmentDTO.getId())
            .map(existingProfileAttachment -> {
                profileAttachmentMapper.partialUpdate(existingProfileAttachment, profileAttachmentDTO);

                return existingProfileAttachment;
            })
            .flatMap(profileAttachmentRepository::save)
            .map(profileAttachmentMapper::toDto);
    }

    /**
     * Get all the profileAttachments.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<ProfileAttachmentDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ProfileAttachments");
        return profileAttachmentRepository.findAllBy(pageable).map(profileAttachmentMapper::toDto);
    }

    /**
     * Returns the number of profileAttachments available.
     *
     * @return the number of entities in the database.
     */
    public Mono<Long> countAll() {
        return profileAttachmentRepository.count();
    }

    /**
     * Get one profileAttachment by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<ProfileAttachmentDTO> findOne(UUID id) {
        log.debug("Request to get ProfileAttachment : {}", id);
        return profileAttachmentRepository.findById(id).map(profileAttachmentMapper::toDto);
    }

    /**
     * Delete the profileAttachment by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(UUID id) {
        log.debug("Request to delete ProfileAttachment : {}", id);
        return profileAttachmentRepository.deleteById(id);
    }

    public Flux<ProfileAttachmentDTO> getAllProfileAttachments(UUID employeeProfileId) {
        return profileAttachmentRepository.findAllByProfileId(employeeProfileId)
            .map(profileAttachmentMapper::toDto);
    }

    public Mono<Void> deleteByEmployeeProfileId(UUID employeeProfileId) {
        return profileAttachmentRepository.deleteAllByProfileId(employeeProfileId);
    }

    public Mono<List<ProfileAttachmentDTO>> uploadProfileAttachment(List<ProfileAttachmentDTO> attachments, boolean clearOld) {
        final UUID employeeProfileId = attachments.get(0).getEmployeeProfileId();

        Mono<Void> promise = Mono.empty();
        if (clearOld) {
            promise = this.profileAttachmentRepository.deleteAllByProfileId(employeeProfileId);
        }
        return promise.then(employeeProfileRepository.findByIdAndNotDelete(attachments.get(0).getEmployeeProfileId())
            .flatMap(employeeProfile -> {
                return Flux.fromIterable(attachments)
                    .flatMap(attachment -> {
                        if (attachment.getType() == ProfileAttachmentType.OTHER) {
                            attachment.setPath(attachment.getPath());
                            return profileAttachmentRepository.deleteAllByType(employeeProfileId, ProfileAttachmentType.OTHER).then(profileAttachmentRepository.save(profileAttachmentMapper.toEntity(attachment)).map(profileAttachmentMapper::toDto));
                        }

                        // cứ gửi lên thì lưu vào db ( cho phép gửi lên nhiều lần)
                        attachment.setPath(attachment.getPath());
                        return profileAttachmentRepository.save(profileAttachmentMapper.toEntity(attachment))
//                        return profileAttachmentRepository.findAllByProfileIdAndAttachmentType(employeeProfileId, attachment.getType())
//                            .flatMap(existingAttachment -> {
//                                existingAttachment.setPath(attachment.getPath());
//                                return profileAttachmentRepository.save(existingAttachment.setIsPersisted());
//                            })
//                            .switchIfEmpty(
//                                Mono.defer(() -> {
//                                        attachment.setPath(attachment.getPath());
//
//                                    }
//                                ))
                            .map(profileAttachmentMapper::toDto);
                    }).collectList();
            }).switchIfEmpty(Mono.error(new BadRequestAlertException("Employee profile not found", "EmployeeProfile", "idnotfound"))));

    }

    public Mono<List<ProfileAttachmentDTO>> uploadProfileAttachment(List<ProfileAttachmentDTO> attachments) {
        return this.uploadProfileAttachment(attachments, false);
    }


    public Mono<List<ProfileAttachmentDTO>> getByEmployeeProfileId(UUID employeeProfileId) {
        return profileAttachmentRepository.findAllByProfileId(employeeProfileId)
            .collectList()
            .map(profileAttachmentMapper::toDto)
            .flatMap(attachments -> {
                Map<UUID, ProfileAttachmentDTO> attachmentMap = new HashMap<>();
                attachments.forEach(attachment -> {
                    attachmentMap.put(tryParseUUid(attachment.getPath()), attachment);
                });
                return fileClient.getFileAttachmentsByListIds(attachmentMap.keySet())
                    .map(fileAttachmentDTO -> {
                        attachmentMap.get(fileAttachmentDTO.getId()).setFileAttachment(fileAttachmentDTO);
                        return attachmentMap.get(fileAttachmentDTO.getId());
                    }).collectList().then(Mono.just(attachments));
            });
    }

    private UUID tryParseUUid(String id) {
        if (StringUtils.isBlank(id)) {
            return UUID.randomUUID();
        }
        try {
            return UUID.fromString(id);
        } catch (Exception e) {
            return UUID.randomUUID();
        }
    }
}
