package com.masi.employee.service;

import com.carevn.masi.dto.EmbedFile;
import com.carevn.masi.utils.SecurityUtils;
import com.masi.employee.domain.Documentary;
import com.masi.employee.domain.enumeration.DocumentaryStatus;
import com.masi.employee.repository.DocumentaryRepository;
import com.masi.employee.service.dto.DocumentaryDTO;
import com.masi.employee.service.dto.DocumentaryRO;
import com.masi.employee.service.dto.FileAttachmentDTO;
import com.masi.employee.service.dto.request.ConsentToReview;
import com.masi.employee.service.dto.request.RefusalOfReview;
import com.masi.employee.service.mapper.DocumentaryMapper;
import com.masi.employee.service.web.client.FileClient;
import com.masi.employee.web.rest.errors.BadRequestAlertException;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.masi.employee.domain.Documentary}.
 */
@Service
@Transactional
public class DocumentaryService {

    private static final Logger log = LoggerFactory.getLogger(DocumentaryService.class);

    private final DocumentaryRepository documentaryRepository;

    private final DocumentaryMapper documentaryMapper;
    private final FileClient fileClient;

    public DocumentaryService(DocumentaryRepository documentaryRepository, DocumentaryMapper documentaryMapper, FileClient fileClient) {
        this.documentaryRepository = documentaryRepository;
        this.documentaryMapper = documentaryMapper;
        this.fileClient = fileClient;
    }

    /**
     * Save a documentary.
     *
     * @param documentaryDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<DocumentaryDTO> save(DocumentaryDTO documentaryDTO) {
        log.debug("Request to save Documentary : {}", documentaryDTO);
        documentaryDTO.setStatus(DocumentaryStatus.NEW);
        documentaryDTO.setIdGroup("HCNS");
        var entity = documentaryMapper.toEntity(documentaryDTO);
        entity.setAttachments(documentaryDTO.getAttachments());
        return documentaryRepository.save(entity).map(documentaryMapper::toDto);
    }

    /**
     * Update a documentary.
     *
     * @param documentaryDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<DocumentaryDTO> update(DocumentaryDTO documentaryDTO) {
        log.debug("Request to update Documentary : {}", documentaryDTO);
        return documentaryRepository.save(documentaryMapper.toEntity(documentaryDTO).setIsPersisted()).map(documentaryMapper::toDto);
    }

    /**
     * Partially update a documentary.
     *
     * @param documentaryDTO the entity to update partially.
     * @return the persisted entity.
     */
    @Transactional
    public Mono<DocumentaryDTO> partialUpdate(DocumentaryDTO documentaryDTO) {
        log.debug("Request to partially update Documentary : {}", documentaryDTO);
        return documentaryRepository
            .findByIdAndIsDeleted(documentaryDTO.getId(), false)
            .flatMap(existingDocumentary -> {
                documentaryDTO.applyUpdate(existingDocumentary);
                existingDocumentary.setIsPersisted();
                return documentaryRepository.save(existingDocumentary).map(documentaryMapper::toDto);
            });
    }

    /**
     * Get all the documentaries.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<DocumentaryDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Documentaries");
        return documentaryRepository.findAllBy(pageable).map(documentaryMapper::toDto);
    }

    /**
     * Returns the number of documentaries available.
     *
     * @return the number of entities in the database.
     */
    public Mono<Long> countAll() {
        return documentaryRepository.count();
    }

    /**
     * Get one documentary by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<DocumentaryDTO> findOne(UUID id) {
        log.debug("Request to get Documentary : {}", id);
        return documentaryRepository
            .findById(id)
            .flatMap(e -> {
                var dto = documentaryMapper.toDto(e);
                List<UUID> listIds = Stream.of(dto.getApprovalSignFile(), dto.getAttachmentsContentFile())
                    .filter(StringUtils::isNotBlank)
                    .map(UUID::fromString)
                    .toList();
                return fileClient
                    .getFileAttachmentsByListIds(listIds)
                    .collectList()
                    .map(fileAttachmentDTOS -> {
                        dto.setSignFile(
                            fileAttachmentDTOS.stream().filter(f -> f.getId().toString().equals(dto.getApprovalSignFile())).findFirst().orElse(null)
                        );
                        dto.setAttachmentsFile(
                            fileAttachmentDTOS
                                .stream()
                                .filter(f -> f.getId().toString().equals(dto.getAttachmentsContentFile()))
                                .findFirst()
                                .orElse(null)
                        );
                        return dto;
                    });
            });
    }

    /**
     * Delete the documentary by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(UUID id) {
        log.debug("Request to delete Documentary : {}", id);
        return documentaryRepository
            .findByIdAndIsDeleted(id, false)
            .flatMap(documentary -> {
                if (DocumentaryStatus.APPROVED.equals(documentary.getStatus())) return Mono.error(
                    new BadRequestAlertException("Entity can not delelted by status", documentary.getStatus().toString(), "idnotfound")
                );
                return SecurityUtils.getUserJWTDetail()
                    .flatMap(login -> {
                        documentary.setIsDeleted(true);
                        documentary.setDeletedAt(ZonedDateTime.now());
                        documentary.setDeletedBy(login.getUserId().toString());
                        documentary.setIsPersisted();
                        return documentaryRepository.save(documentary).then();
                    });
            });
    }

    public Mono<Long> countAllWithQuery(DocumentaryRO ro) {
        return documentaryRepository.countByFilter(ro);
    }

    public Flux<DocumentaryDTO> findAllWithQuery(Pageable pageable, DocumentaryRO ro) {
        log.debug("Request to query Documentary");
        return documentaryRepository.findAllByFilter(ro, pageable).map(Documentary::toDto);
    }

    public Mono<Integer> approveReviewDocumentary(UUID id) {
        log.debug("Request to Review Documentary");
        return documentaryRepository
            .findById(id)
            .flatMap(documentary -> {
                return documentaryRepository.changeStatusByIdAndStatus(id).thenReturn(1);
            });
    }

    public Mono<Integer> approveConsentDocumentary(UUID id, ConsentToReview consentToReview) {
        log.debug("Request to Approve Consent Documentary : {}", id);
        return documentaryRepository
            .findById(id)
            .flatMap(documentary -> {
                if (documentary.getStatus().equals(DocumentaryStatus.PENDING_APPROVAL)) {
                    try {
                        return documentaryRepository
                            .consentToReviewContract(id, consentToReview.getApprovalSignFile())
                            .thenReturn(1);
                    } catch (Exception e) {
                        return Mono.error(new BadRequestAlertException("Cannot upload file: " + e.getMessage(), "file", "uploaderror"));
                    }
                }
                return Mono.error(
                    new BadRequestAlertException(
                        "Cannot Approve Consent Documentary with status: " + documentary.getStatus() + " or Cannot upload file",
                        "",
                        ""
                    )
                );
            });
    }

    public Mono<Integer> approveRefusalDocumentary(UUID id, RefusalOfReview refusalOfReview) {
        log.debug("Request to Refusal Consent Documentary : {}", id);
        return documentaryRepository
            .findById(id)
            .flatMap(contract -> {
                if (contract.getStatus().equals(DocumentaryStatus.PENDING_APPROVAL)) {
                    return documentaryRepository.refusalOfReviewContract(id, refusalOfReview.getRejectNote()).thenReturn(1);
                }
                return Mono.error(
                    new BadRequestAlertException(
                        "Cannot Liqidated Review contract with status: " + contract.getStatus() + "OR Not Time ContractValid",
                        "",
                        ""
                    )
                );
            });
    }
}
