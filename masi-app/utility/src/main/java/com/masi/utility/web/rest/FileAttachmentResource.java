package com.masi.utility.web.rest;

import com.masi.utility.domain.criteria.FileAttachmentCriteria;
import com.masi.utility.repository.FileAttachmentRepository;
import com.masi.utility.service.FileAttachmentService;
import com.masi.utility.service.dto.FileAttachmentDTO;
import com.masi.utility.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.ForwardedHeaderUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link com.masi.utility.domain.FileAttachment}.
 */
@RestController
@RequestMapping("/api/file-attachments")
public class FileAttachmentResource {

    private static final Logger log = LoggerFactory.getLogger(FileAttachmentResource.class);

    private static final String ENTITY_NAME = "masiUtilityFileAttachment";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FileAttachmentService fileAttachmentService;

    private final FileAttachmentRepository fileAttachmentRepository;

    public FileAttachmentResource(FileAttachmentService fileAttachmentService,
                                  FileAttachmentRepository fileAttachmentRepository) {
        this.fileAttachmentService = fileAttachmentService;
        this.fileAttachmentRepository = fileAttachmentRepository;
    }

    @PostMapping(value = "/upload-many", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Mono<ResponseEntity<List<FileAttachmentDTO>>> createFileAttachmentMany(
        @RequestPart("files") Flux<FilePart> filePartFlux) {
        return fileAttachmentService.uploadFiles(filePartFlux).collectList().map(
            result -> {
                return ResponseEntity.ok().body(result);
            });
    }

    @GetMapping("/list")
    public Mono<ResponseEntity<List<FileAttachmentDTO>>> getAllFileAttachmentsByListIds(@RequestParam List<UUID> ids) {
        log.debug("REST request to get FileAttachments by list of ids : {}", ids);
        return fileAttachmentService.findAllByListIds(ids).collectList().map(ResponseEntity.ok()::body);
    }

    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Mono<ResponseEntity<FileAttachmentDTO>> createFileAttachment(
        @RequestPart("files") Mono<FilePart> filePartFlux,
        @RequestParam(value = "requestId", required = false) UUID requestId) {
        return fileAttachmentService
            .uploadFile(filePartFlux, requestId)
            .handle((result, sink) -> {
                try {
                    sink.next(ResponseEntity
                        .created(new URI("/api/file-attachments/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME,
                            result.getId().toString()))
                        .body(result));
                } catch (URISyntaxException e) {
                    sink.error(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()));
                }
            });
    }

    @GetMapping("/{id}/detail")
    public Mono<ResponseEntity<FileAttachmentDTO>> getFileAttachmentDetail(@PathVariable("id") UUID id) {
        log.debug("REST request to get FileAttachment details : {}", id);
        return fileAttachmentService.findOne(id).map(ResponseEntity.ok()::body);
    }




    @GetMapping("/{id}")
    public Mono<ResponseEntity<InputStreamResource>> getFileAttachment(
        @RequestParam(value = "download", required = false, defaultValue = "false") boolean download,
        @PathVariable("id") UUID id) {
        log.debug("REST request to get FileAttachment : {}", id);
        Mono<ResponseEntity<InputStreamResource>> fileAttachment = fileAttachmentService
            .findOne(id)
            .handle((fileAttachmentDTO, sink) -> {
                try {
                    FileInputStream fileInputStream = new FileInputStream(fileAttachmentDTO.getPath());
                    InputStreamResource inputStreamResource = new InputStreamResource(fileInputStream);
                    HttpHeaders headers = new HttpHeaders();
                    String contentType = Files.probeContentType(Path.of(fileAttachmentDTO.getPath()));
                    if (contentType == null) {
                        contentType = "application/octet-stream"; // Set a default content type if probeContentType
                        // fails
                    }
                    if (download) {
                        headers.add(HttpHeaders.CONTENT_DISPOSITION, String.format("attachment; filename=\"%s\"", fileAttachmentDTO.getName()));
                    } else {
                        headers.add(HttpHeaders.CONTENT_TYPE, contentType);
                        headers.add("content-name", fileAttachmentDTO.getName());
                    }
                    // headers.add(HttpHeaders.CONTENT_DISPOSITION, String.format("attachment;
                    // filename=\"%s\"", fileAttachmentDTO.getName()));

                    sink.next(new ResponseEntity<>(inputStreamResource, headers, HttpStatus.OK));

                } catch (IOException e) {
                    sink.error(new BadRequestAlertException("File not found", ENTITY_NAME, "fileNotFound"));
                }
            });
        return fileAttachment
            .switchIfEmpty(Mono.error(new BadRequestAlertException("File not found", ENTITY_NAME, "fileNotFound")));
    }
    @GetMapping("/{id}/{fileName}")
    public Mono<ResponseEntity<InputStreamResource>> getFileAttachmentWithFileName(
        @RequestParam(value = "download", required = false, defaultValue = "false") boolean download,
        @PathVariable("id") UUID id,
        @PathVariable("fileName") String fileName) {
        Mono<ResponseEntity<InputStreamResource>> fileAttachment = fileAttachmentService
            .findOne(id)
            .handle((fileAttachmentDTO, sink) -> {
                try {
                    FileInputStream fileInputStream = new FileInputStream(fileAttachmentDTO.getPath());
                    InputStreamResource inputStreamResource = new InputStreamResource(fileInputStream);
                    HttpHeaders headers = new HttpHeaders();
                    String contentType = Files.probeContentType(Path.of(fileAttachmentDTO.getPath()));
                    if (contentType == null) {
                        contentType = "application/octet-stream"; // Set a default content type if probeContentType
                    }
                    if (download) {
                        headers.add(HttpHeaders.CONTENT_DISPOSITION, String.format("attachment; filename=\"%s\"", fileName));
                    } else {
                        headers.add(HttpHeaders.CONTENT_TYPE, contentType);
                        headers.add("content-name", fileName);
                    }
                    sink.next(new ResponseEntity<>(inputStreamResource, headers, HttpStatus.OK));

                } catch (IOException e) {
                    sink.error(new BadRequestAlertException("File not found", ENTITY_NAME, "fileNotFound"));
                }
            });
        return fileAttachment
            .switchIfEmpty(Mono.error(new BadRequestAlertException("File not found", ENTITY_NAME, "fileNotFound")));
    }

    @GetMapping("/{id}/base64")
    public Mono<ResponseEntity<FileAttachmentDTO>> getFileAttachmentBase64(@PathVariable("id") UUID id) {
        log.debug("REST request to get base64 FileAttachment : {}", id);
        return fileAttachmentService
            .findOne(id)
            .handle((fileAttachmentDTO, sink) -> {
                try {
                    FileInputStream fileInputStream = new FileInputStream(fileAttachmentDTO.getPath());
                    byte[] fileContent = fileInputStream.readAllBytes();
                    fileAttachmentDTO.setBase64(fileContent);
                    fileInputStream.close();
                    sink.next(ResponseEntity.ok().body(fileAttachmentDTO));
                } catch (Exception e) {
                    sink.error(new BadRequestAlertException("File not found", ENTITY_NAME, "fileNotFound"));
                }
            });
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteFileAttachment(@PathVariable("id") UUID id) {
        log.debug("REST request to delete FileAttachment : {}", id);
        return fileAttachmentService
            .delete(id)
            .then(
                Mono.just(
                    ResponseEntity.noContent()
                        .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true,
                            ENTITY_NAME, id.toString()))
                        .build()));
    }
}
