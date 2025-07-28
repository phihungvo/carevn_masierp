package com.masi.employee.web.rest;

import com.carevn.masi.dto.ApiResponse;
import com.carevn.masi.utils.SecurityUtils;
import com.masi.employee.repository.WorkspaceRepository;
import com.masi.employee.service.WorkspaceService;
import com.masi.employee.service.dto.WorkspaceDTO;
import com.masi.employee.service.dto.WorkspaceRO;
import com.masi.employee.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link com.masi.employee.domain.Workspace}.
 */
@RestController
@RequestMapping("/api/workspaces")
public class WorkspaceResource {

    private static final Logger log = LoggerFactory.getLogger(WorkspaceResource.class);

    private static final String ENTITY_NAME = "masiEmployeeWorkspace";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WorkspaceService workspaceService;

    public WorkspaceResource(WorkspaceService workspaceService) {
        this.workspaceService = workspaceService;
    }

    /**
     * {@code POST  /workspaces} : Create a new workspace.
     *
     * @param workspaceDTO the workspaceDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new workspaceDTO, or with status {@code 400 (Bad Request)} if the workspace has already an ID.
     */
    @PostMapping("")
    public Mono<ResponseEntity<WorkspaceDTO>> createWorkspace(@Valid @RequestBody WorkspaceDTO workspaceDTO) {
        log.debug("REST request to save Workspace : {}", workspaceDTO);
        return
            SecurityUtils.getUserJWTDetail().flatMap(user -> {
                workspaceDTO.setCompany(String.valueOf(user.getCompanyId()));
                return workspaceService
                    .create(workspaceDTO)
                    .map(result -> ResponseEntity.ok()
                        .body(result));
            });

    }

    /**
     * {@code PATCH  /workspaces/:id} : Partial updates given fields of an existing workspace, field will ignore if it is null
     *
     * @param id           the id of the workspaceDTO to save.
     * @param workspaceDTO the workspaceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated workspaceDTO,
     * or with status {@code 400 (Bad Request)} if the workspaceDTO is not valid,
     * or with status {@code 404 (Not Found)} if the workspaceDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the workspaceDTO couldn't be updated.
     */
    @PatchMapping(value = "/{id}", consumes = {"application/json", "application/merge-patch+json"})
    public Mono<ResponseEntity<WorkspaceDTO>> partialUpdateWorkspace(
        @PathVariable(value = "id") final UUID id,
        @NotNull @RequestBody WorkspaceDTO workspaceDTO
    ) {
        workspaceDTO.setId(id);
        log.debug("REST request to partial update Workspace partially : {}, {}", id, workspaceDTO);

        return workspaceService
            .partialUpdate(workspaceDTO)
            .map(result -> {
                return ResponseEntity.ok()
                    .body(result);

            });
    }

    /**
     * {@code GET  /workspaces} : get all the workspaces.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of workspaces in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ApiResponse<WorkspaceDTO>> getAllWorkspaces(@ParameterObject Pageable pageable,
                                                            @ParameterObject WorkspaceRO workspaceRO) {
        log.debug("REST request to get all Workspaces");
        return
            SecurityUtils.getUserJWTDetail().flatMap(user -> {
                workspaceRO.setCompany(String.valueOf(user.getCompanyId()));
                return workspaceService.getAllByQueryAndPaginate(pageable, workspaceRO)
                    .collectList()
                    .zipWith(workspaceService.countAllActive(workspaceRO))
                    .map(list -> new ApiResponse<>(list.getT1(), list.getT2()));
            });


    }

    /**
     * {@code GET  /workspaces/:id} : get the "id" workspace.
     *
     * @param id the id of the workspaceDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the workspaceDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<WorkspaceDTO>> getWorkspace(@PathVariable("id") UUID id) {
        log.debug("REST request to get Workspace : {}", id);
        Mono<WorkspaceDTO> workspaceDTO = workspaceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(workspaceDTO);
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteWorkspace(@PathVariable("id") UUID id) {
        log.debug("REST request to delete Workspace : {}", id);
        return workspaceService.deactivate(id)
            .then(Mono.fromCallable(() -> ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                .build()));
    }
}
