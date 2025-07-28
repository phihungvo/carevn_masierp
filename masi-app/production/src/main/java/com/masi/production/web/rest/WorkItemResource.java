package com.masi.production.web.rest;

import com.masi.production.repository.WorkItemRepository;
import com.masi.production.service.WorkItemService;
import com.masi.production.service.dto.WorkItemDTO;
import com.masi.production.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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
 * REST controller for managing {@link com.masi.production.domain.WorkItem}.
 */
@RestController
@RequestMapping("/api/work-items")
public class WorkItemResource {

    private final Logger log = LoggerFactory.getLogger(WorkItemResource.class);

    private static final String ENTITY_NAME = "masiProductionWorkItem";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WorkItemService workItemService;

    private final WorkItemRepository workItemRepository;

    public WorkItemResource(WorkItemService workItemService, WorkItemRepository workItemRepository) {
        this.workItemService = workItemService;
        this.workItemRepository = workItemRepository;
    }

    /**
     * {@code POST  /work-items} : Create a new workItem.
     *
     * @param workItemDTO the workItemDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new workItemDTO, or with status {@code 400 (Bad Request)} if the workItem has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<WorkItemDTO>> createWorkItem(@Valid @RequestBody WorkItemDTO workItemDTO) throws URISyntaxException {
        log.debug("REST request to save WorkItem : {}", workItemDTO);
        if (workItemDTO.getId() != null) {
            throw new BadRequestAlertException("A new workItem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        workItemDTO.setId(UUID.randomUUID());
        return workItemService
            .save(workItemDTO)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/work-items/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /work-items/:id} : Updates an existing workItem.
     *
     * @param id the id of the workItemDTO to save.
     * @param workItemDTO the workItemDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated workItemDTO,
     * or with status {@code 400 (Bad Request)} if the workItemDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the workItemDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<WorkItemDTO>> updateWorkItem(
        @PathVariable(value = "id", required = false) final UUID id,
        @Valid @RequestBody WorkItemDTO workItemDTO
    ) throws URISyntaxException {
        log.debug("REST request to update WorkItem : {}, {}", id, workItemDTO);
        if (workItemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, workItemDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return workItemRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return workItemService
                    .update(workItemDTO)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(
                        result ->
                            ResponseEntity.ok()
                                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                                .body(result)
                    );
            });
    }

    /**
     * {@code PATCH  /work-items/:id} : Partial updates given fields of an existing workItem, field will ignore if it is null
     *
     * @param id the id of the workItemDTO to save.
     * @param workItemDTO the workItemDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated workItemDTO,
     * or with status {@code 400 (Bad Request)} if the workItemDTO is not valid,
     * or with status {@code 404 (Not Found)} if the workItemDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the workItemDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<WorkItemDTO>> partialUpdateWorkItem(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody WorkItemDTO workItemDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update WorkItem partially : {}, {}", id, workItemDTO);
        if (workItemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, workItemDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return workItemRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<WorkItemDTO> result = workItemService.partialUpdate(workItemDTO);

                return result
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(
                        res ->
                            ResponseEntity.ok()
                                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, res.getId().toString()))
                                .body(res)
                    );
            });
    }

    /**
     * {@code GET  /work-items} : get all the workItems.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of workItems in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<WorkItemDTO>>> getAllWorkItems(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request,
        @RequestParam(name = "filter", required = false) String filter
    ) {
        if ("workorder-is-null".equals(filter)) {
            log.debug("REST request to get all WorkItems where workOrder is null");
            return workItemService.findAllWhereWorkOrderIsNull().collectList().map(ResponseEntity::ok);
        }
        log.debug("REST request to get a page of WorkItems");
        return workItemService
            .countAll()
            .zipWith(workItemService.findAll(pageable).collectList())
            .map(
                countWithEntities ->
                    ResponseEntity.ok()
                        .headers(
                            PaginationUtil.generatePaginationHttpHeaders(
                                ForwardedHeaderUtils.adaptFromForwardedHeaders(request.getURI(), request.getHeaders()),
                                new PageImpl<>(countWithEntities.getT2(), pageable, countWithEntities.getT1())
                            )
                        )
                        .body(countWithEntities.getT2())
            );
    }

    /**
     * {@code GET  /work-items/:id} : get the "id" workItem.
     *
     * @param id the id of the workItemDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the workItemDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<WorkItemDTO>> getWorkItem(@PathVariable("id") UUID id) {
        log.debug("REST request to get WorkItem : {}", id);
        Mono<WorkItemDTO> workItemDTO = workItemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(workItemDTO);
    }

    /**
     * {@code DELETE  /work-items/:id} : delete the "id" workItem.
     *
     * @param id the id of the workItemDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteWorkItem(@PathVariable("id") UUID id) {
        log.debug("REST request to delete WorkItem : {}", id);
        return workItemService
            .delete(id)
            .then(
                Mono.just(
                    ResponseEntity.noContent()
                        .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                        .build()
                )
            );
    }
}
