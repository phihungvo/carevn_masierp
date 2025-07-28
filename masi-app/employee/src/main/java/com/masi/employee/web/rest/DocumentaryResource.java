package com.masi.employee.web.rest;

import com.carevn.masi.dto.ApiResponse;
import com.carevn.masi.utils.SecurityUtils;
import com.carevn.masi.utils.Utilities;
import com.masi.employee.domain.Documentary;
import com.masi.employee.domain.EmployeeProfile;
import com.masi.employee.domain.enumeration.DocumentaryStatus;
import com.masi.employee.repository.DocumentaryRepository;
import com.masi.employee.repository.EmployeeProfileRepository;
import com.masi.employee.service.DocumentaryService;
import com.masi.employee.service.FileService;
import com.masi.employee.service.dto.DocumentaryDTO;
import com.masi.employee.service.dto.DocumentaryRO;
import com.masi.employee.service.dto.request.ConsentToReview;
import com.masi.employee.service.dto.request.RefusalOfReview;
import com.masi.employee.web.rest.errors.BadRequestAlertException;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.HeaderUtil;

/**
 * REST controller for managing {@link Documentary}.
 */
@RestController
@RequestMapping("/api/documentaries")
public class DocumentaryResource {

    private static final Logger log = LoggerFactory.getLogger(DocumentaryResource.class);

    private static final String ENTITY_NAME = "masiEmployeeDocumentary";
    private final FileService fileService;

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DocumentaryService documentaryService;
    private final EmployeeProfileRepository employeeProfileRepository;

    private final DocumentaryRepository documentaryRepository;

    public DocumentaryResource(DocumentaryService documentaryService, EmployeeProfileRepository employeeProfileRepository, DocumentaryRepository documentaryRepository, FileService fileService) {
        this.documentaryService = documentaryService;
        this.employeeProfileRepository = employeeProfileRepository;
        this.documentaryRepository = documentaryRepository;
        this.fileService = fileService;
    }

    /**
     * {@code POST  /documentaries} : Create a new documentary.
     *
     * @param documentaryDTO the documentaryDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new documentaryDTO, or with status {@code 400 (Bad Request)} if the documentary has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<Map<String, Object>>> createDocumentary(@Valid @RequestBody DocumentaryDTO documentaryDTO)
        throws URISyntaxException {
        log.debug("REST request to save Documentary : {}", documentaryDTO);
        documentaryDTO.setId(UUID.randomUUID());

        return documentaryService
            .save(documentaryDTO)
            .handle((result, sink) -> {
                try {
                    Map<String, Object> response = new HashMap<>();
                    response.put("message", "Document created successfully");
                    response.put("Document", result);
                    sink.next(ResponseEntity.created(new URI("/api/document/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(response));

                } catch (URISyntaxException e) {
                    sink.error(new RuntimeException(e));
                }
            });
    }

    @PatchMapping(value = "/{id}", consumes = {"application/json", "application/merge-patch+json"})
    public Mono<ResponseEntity<Map<String, Object>>> partialUpdateDocumentary(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody DocumentaryDTO documentaryDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Documentary partially : {}, {}", id, documentaryDTO);
        documentaryDTO.setId(id);

        return documentaryRepository
            .findByIdAndIsDeleted(id, false)
            .flatMap(exists -> {

                try {
                    if (exists.getStatus().equals(DocumentaryStatus.APPROVED)) {
                        return Mono.error(new BadRequestAlertException("Entity can not delelted by status", documentaryDTO.getStatus().toString(), "idnotfound"));
                    }
                    Mono<DocumentaryDTO> result = documentaryService.partialUpdate(documentaryDTO);

                    return result
                        .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                        .map(
                            res ->
                                ResponseEntity.ok()
                                    .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, res.getId().toString()))
                                    .body(Utilities.generateResponse("success", res))
                        );
                } catch (Exception e) {
                    return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage()));
                }
            });
    }

    /**
     * {@code GET  /documentaries} : get all the documentaries.
     *
     * @param pageable the pagination information.
     * @param ro       a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of documentaries in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<ApiResponse<DocumentaryDTO>>> getAllDocumentaries(
        @ParameterObject Pageable pageable,
        @ParameterObject DocumentaryRO ro) {
        log.debug("REST request to get a page of Documentaries");

        return SecurityUtils.getUserJWTDetail().flatMap(user -> {
            ro.setCompanyId(String.valueOf(user.getCompanyId()));
//            ro.setDepartment(user.getGroupId().toString());
            ro.setEmployeeId(user.getUserId());

            Mono<Long> countMono = documentaryService.countAllWithQuery(ro);

            Mono<List<DocumentaryDTO>> documentariesMono = documentaryService.findAllWithQuery(pageable, ro).collectList();

            Mono<Map<UUID, EmployeeProfile>> employeeProfilesMono = documentariesMono
                .flatMap(documentaries -> {
                    List<UUID> uuids = documentaries.stream()
                        .flatMap(documentary -> Stream.of(documentary.getSenderOrReceiver(), documentary.getSigner()))
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());
                    return employeeProfileRepository.findByIdIn(uuids)
                        .collectMap(EmployeeProfile::getId);
                });


            Mono<List<DocumentaryDTO>> documentaryDTOsMono = documentariesMono.zipWith(employeeProfilesMono)
                .map(tuple -> {
                    List<DocumentaryDTO> documentaries = tuple.getT1();
                    Map<UUID, EmployeeProfile> employeeProfileMap = tuple.getT2();

                    return documentaries.stream()
                        .map(dto -> {
                            UUID senderOrReceiverId = dto.getSenderOrReceiver();
                            if (senderOrReceiverId != null && employeeProfileMap.containsKey(senderOrReceiverId)) {
                                dto.setEmployeeProfileSender(employeeProfileMap.get(senderOrReceiverId).toDTO());
                            }

                            UUID signerId = dto.getSigner();
                            if (signerId != null && employeeProfileMap.containsKey(signerId)) {
                                dto.setEmployeeProfileSigner(employeeProfileMap.get(signerId).toDTO());
                            }

                            return dto;
                        })
                        .collect(Collectors.toList());
                });
            return countMono.zipWith(documentaryDTOsMono)
                .map(tuple -> {
                    Long count = tuple.getT1();
                    List<DocumentaryDTO> documentaryDTOs = tuple.getT2();
                    return ResponseEntity.ok().body(new ApiResponse<>(documentaryDTOs, count));
                });
        });

    }

    /**
     * {@code GET  /documentaries/:id} : get the "id" documentary.
     *
     * @param id the id of the documentaryDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the documentaryDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<DocumentaryDTO>> getDocumentary(@PathVariable("id") UUID id) {
        log.debug("REST request to get Documentary : {}", id);
        Mono<DocumentaryDTO> documentaryMono = documentaryService.findOne(id);

        Mono<Map<UUID, EmployeeProfile>> employeeProfilesMono = documentaryMono
            .flatMap(documentary -> {
                UUID senderOrReceiverId = documentary.getSenderOrReceiver();
                UUID signerId = documentary.getSigner();
                List<UUID> uuids = Stream.of(senderOrReceiverId, signerId)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

                return employeeProfileRepository.findByIdIn(uuids)
                    .collectMap(EmployeeProfile::getId);
            });

        return documentaryMono.zipWith(employeeProfilesMono)
            .map(tuple -> {
                DocumentaryDTO documentary = tuple.getT1();
                Map<UUID, EmployeeProfile> employeeProfileMap = tuple.getT2();

                UUID senderOrReceiverId = documentary.getSenderOrReceiver();
                if (senderOrReceiverId != null && employeeProfileMap.containsKey(senderOrReceiverId)) {
                    documentary.setEmployeeProfileSender(employeeProfileMap.get(senderOrReceiverId).toDTO());
                }

                UUID signerId = documentary.getSigner();
                if (signerId != null && employeeProfileMap.containsKey(signerId)) {
                    documentary.setEmployeeProfileSigner(employeeProfileMap.get(signerId).toDTO());
                }

                return ResponseEntity.ok(documentary);
            })
            .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    /**
     * {@code DELETE  /documentaries/:id} : delete the "id" documentary.
     *
     * @param id the id of the documentaryDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Map<String, Object>>> deleteDocumentary(@PathVariable("id") UUID id) {
        log.debug("REST request to delete Documentary : {}", id);
        return documentaryService.delete(id)
            .then(
                Mono.just(
                    ResponseEntity.noContent()
                        .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                        .build()
                )
            )
            .map(responseEntity -> {
                Map<String, Object> responseBody = new HashMap<>();
                responseBody.put("message", "Documentary deleted successfully");
                return ResponseEntity.ok().body(responseBody);
            })
            .onErrorResume(IllegalStateException.class, ex -> {
                Map<String, Object> responseBody = new HashMap<>();
                responseBody.put("error", "Cannot delete Documentary");
                responseBody.put("message", ex.getMessage());
                responseBody.put("status", "success");
                return Mono.just(
                    ResponseEntity.badRequest().body(responseBody)
                );
            });
    }


    @Operation(summary = "Approve review Documentary ")
    @PatchMapping("/documentary-review/{id}")
    public Mono<ResponseEntity<Map<String, Object>>> documentaryReviewContract(@PathVariable("id") UUID id) {
        log.debug("REST request to Approve review Documentary : {}", id);
        return documentaryService
            .approveReviewDocumentary(id)
            .map(result -> {
                Map<String, Object> responseBody = new HashMap<>();
                responseBody.put("message", "Documentary Approve review successfully");
                responseBody.put("Documentary", id);
                responseBody.put("status", "success");
                return ResponseEntity.ok().body(responseBody);
            }).onErrorResume(e -> {
                throw new BadRequestAlertException("Failed to Approve Documentary: " + e.getMessage(), "Documentary", "Documentarynerror");
            });
    }

    @Operation(summary = "Approve Documentary if success")
    @PatchMapping("/documentary-consent/{id}")
    public Mono<ResponseEntity<Map<String, Object>>> documentaryContract(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody ConsentToReview consentToReview
    ) throws URISyntaxException {
        log.debug("REST request to Consent Documentary : {}", id);
        return documentaryService
            .approveConsentDocumentary(id, consentToReview)
            .map(result -> {
                Map<String, Object> responseBody = new HashMap<>();
                responseBody.put("message", "Documentary Approve Consent successfully");
                responseBody.put("DocumentaryId", id);
                responseBody.put("status", "success");
                return ResponseEntity.ok().body(responseBody);
            }).onErrorResume(e -> {
                throw new BadRequestAlertException("Failed to Consent Documentary: " + e.getMessage(), "Documentary", "Documentarynerror");
            });
    }

    @Operation(summary = "Approve Documentary if fail")
    @PatchMapping("/documentary-refusal/{id}")
    public Mono<ResponseEntity<Map<String, Object>>> documentaryContract(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody RefusalOfReview refusalOfReview
    ) throws URISyntaxException {
        log.debug("REST request to Refusal Documentary : {}", id);
        return documentaryService
            .approveRefusalDocumentary(id, refusalOfReview)
            .map(result -> {
                Map<String, Object> responseBody = new HashMap<>();
                responseBody.put("message", "Documentary Approve refusal successfully");
                responseBody.put("DocumentaryId", id);
                responseBody.put("status", "success");
                return ResponseEntity.ok().body(responseBody);
            }).onErrorResume(e -> {
                throw new BadRequestAlertException("Failed to Approve Documentary: " + e.getMessage(), "Documentary", "Documentaryerror");
            });

    }

    @Operation(summary = "Input NameFile")
    @GetMapping("/files/{fileName}")
    public ResponseEntity<?> downloadFile(@PathVariable String fileName) {
        if (!fileService.checkFileExists(fileName)) {
            throw new BadRequestAlertException("File not found", "file", "notfound");
        }

        Path path = fileService.getFilePath(fileName);

        try {
            byte[] data = Files.readAllBytes(path);
            ByteArrayResource resource = new ByteArrayResource(data);

            String userFriendlyFileName = fileName.substring(fileName.lastIndexOf('_') + 1);

            return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + userFriendlyFileName + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(data.length)
                .body(resource);
        } catch (IOException e) {
            throw new BadRequestAlertException("Error reading file: " + e.getMessage(), "file", "readerror");
        }
    }
}
