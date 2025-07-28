package com.masi.employee.web.rest;

import com.masi.employee.repository.ProfileAttachmentRepository;
import com.masi.employee.service.ProfileAttachmentService;
import com.masi.employee.service.dto.ProfileAttachmentDTO;
import com.masi.employee.web.rest.errors.BadRequestAlertException;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
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
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.ForwardedHeaderUtils;
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link com.masi.employee.domain.ProfileAttachment}.
 */
@RestController
@RequestMapping("/api/profile-attachments")
public class ProfileAttachmentResource {

    private static final Logger log = LoggerFactory.getLogger(ProfileAttachmentResource.class);

    private static final String ENTITY_NAME = "masiEmployeeProfileAttachment";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProfileAttachmentService profileAttachmentService;


    public ProfileAttachmentResource(
        ProfileAttachmentService profileAttachmentService
    ) {
        this.profileAttachmentService = profileAttachmentService;

    }



    @Operation(summary = "Tải lên tệp đính kèm cho hồ sơ nhân viên, truyền Id của profile")
    @PatchMapping(value = "profile/{id}")
    public Mono<ResponseEntity<List<ProfileAttachmentDTO>>> partialUpdateProfileAttachment(
        @PathVariable(value = "id") final UUID id,
        @RequestParam(value = "clear-old", required = false, defaultValue = "false") boolean clearOld,
        @NotNull @RequestBody List<ProfileAttachmentDTO> dto
    ) {
        if (dto.isEmpty()) {
            return Mono.just(ResponseEntity.noContent().build());
        }
        dto.forEach(profileAttachmentDTO -> {
            profileAttachmentDTO.setEmployeeProfileId(id);
        });
        return profileAttachmentService.uploadProfileAttachment(dto, clearOld)
            .map(result -> ResponseEntity.ok()
                .body(result)
            );
    }

    @Operation(summary = "Lấy tất cả tệp đính kèm của hồ sơ nhân viên, truyền Id của profile")
    @GetMapping(value = "profile/{id}")
    public Mono<ResponseEntity<List<ProfileAttachmentDTO>>> getAllProfileAttachments(
        @PathVariable(value = "id") final UUID id
    ) {
        return profileAttachmentService.getByEmployeeProfileId(id)
            .map(ResponseEntity.ok()::body);
    }


}
