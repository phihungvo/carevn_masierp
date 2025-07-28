package com.masi.utility.service;

import com.carevn.masi.utils.FileManager;
import com.carevn.masi.utils.SecurityUtils;
import com.masi.utility.domain.FileAttachment;
import com.masi.utility.domain.criteria.FileAttachmentCriteria;
import com.masi.utility.repository.FileAttachmentRepository;
import com.masi.utility.service.dto.FileAttachmentDTO;
import com.masi.utility.service.mapper.FileAttachmentMapper;

import java.io.File;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import com.masi.utility.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.masi.utility.domain.FileAttachment}.
 */
@Service
@Transactional
public class FileAttachmentService {

    private static final Logger log = LoggerFactory.getLogger(FileAttachmentService.class);

    private final FileAttachmentRepository fileAttachmentRepository;

    private final FileAttachmentMapper fileAttachmentMapper;
    private final FileManager fileManager;

    public FileAttachmentService(FileAttachmentRepository fileAttachmentRepository, FileAttachmentMapper fileAttachmentMapper) {
        this.fileAttachmentRepository = fileAttachmentRepository;
        this.fileAttachmentMapper = fileAttachmentMapper;
        try {
            this.fileManager = new FileManager("file-attachments");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Flux<FileAttachmentDTO> findAllByListIds(List<UUID> ids) {
        return fileAttachmentRepository.findAllByIdIn(ids).map(fileAttachmentMapper::toDto);
    }

    public Mono<FileAttachmentDTO> uploadFile(Mono<FilePart> filePartMono, UUID requestId) {
        var id = requestId == null ? UUID.randomUUID() : requestId;

        return filePartMono.flatMap(filePart -> {
            FileAttachment fileAttachment = new FileAttachment();
            fileAttachment.setId(id);
            fileAttachment.setName(filePart.filename());
            fileAttachment.setPath(fileManager.getFilePath(filePart.filename()).toString());
            fileAttachment.setFileSize(filePart.headers().getContentLength());
            fileAttachment.setMimeType(filePart.headers().getContentType() != null ? filePart.headers().getContentType().toString() : "");
            fileAttachment.setCreatedBy("system");
            fileAttachment.setCreatedAt(ZonedDateTime.now());
            fileAttachment.setDeletedAt(null);
            return filePart.transferTo(new File(fileAttachment.getPath())).then(fileAttachmentRepository.save(fileAttachment));
        }).flatMap(fileAttachmentRepository::save).map(entity -> {
            var dto = fileAttachmentMapper.toDto(entity);
            dto.setBase64(fileManager.getFileBase64(dto.getPath()));
            return dto;
        });
    }

    public Flux<FileAttachmentDTO> uploadFiles(Flux<FilePart> filePartFlux) {
        return filePartFlux.flatMap(filePart -> this.uploadFile(Mono.just(filePart), null));
    }

    public Mono<FileAttachmentDTO> save(FileAttachmentDTO fileAttachmentDTO) {
        log.debug("Request to save FileAttachment : {}", fileAttachmentDTO);
        return fileAttachmentRepository.save(fileAttachmentMapper.toEntity(fileAttachmentDTO)).map(fileAttachmentMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Mono<FileAttachmentDTO> findOne(UUID id) {
        log.debug("Request to get FileAttachment : {}", id);
        return fileAttachmentRepository.findFirstByIdAndDeletedAtIsNull(id).map(fileAttachmentMapper::toDto);
    }

    public Mono<Void> delete(UUID id) {

        return SecurityUtils.getUserJWTDetail().zipWith(fileAttachmentRepository.findById(id)).flatMap(tuple -> {
            if (tuple.getT1().getCompanyId().equals(tuple.getT2().getCompany())) {
                var entity = tuple.getT2();
                entity.setIsPersisted();
                entity.setUpdatedAt(ZonedDateTime.now());
                entity.setUpdatedBy(tuple.getT1().getUserId().toString());
                entity.setDeletedBy(tuple.getT1().getUserId().toString());
                entity.setDeletedAt(ZonedDateTime.now());
                return fileAttachmentRepository.save(entity).then();
            } else {
                return Mono.error(new BadRequestAlertException("You are not authorized to delete this file", "fileAttachment", "notAuthorized"));
            }
        });
    }
}
