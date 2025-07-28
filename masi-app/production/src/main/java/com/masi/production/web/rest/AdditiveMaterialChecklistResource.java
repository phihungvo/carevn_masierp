package com.masi.production.web.rest;

import com.carevn.masi.dto.ApiResponse;
import com.carevn.masi.utils.SecurityUtils;
import com.masi.production.repository.AdditiveMaterialChecklistRepository;
import com.masi.production.service.AdditiveMaterialChecklistService;
import com.masi.production.service.dto.AdditiveMaterialChecklistDTO;
import com.masi.production.service.dto.ReleaseWarehouseDTO;
import com.masi.production.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.annotations.ParameterObject;
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
 * REST controller for managing {@link com.masi.production.domain.AdditiveMaterialChecklist}.
 */
@RestController
@RequestMapping("/api/additive-material-checklists")
public class AdditiveMaterialChecklistResource {

    private final Logger log = LoggerFactory.getLogger(AdditiveMaterialChecklistResource.class);

    private static final String ENTITY_NAME = "masiProductionAdditiveMaterialChecklist";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AdditiveMaterialChecklistService additiveMaterialChecklistService;

    private final AdditiveMaterialChecklistRepository additiveMaterialChecklistRepository;

    public AdditiveMaterialChecklistResource(
        AdditiveMaterialChecklistService additiveMaterialChecklistService,
        AdditiveMaterialChecklistRepository additiveMaterialChecklistRepository
    ) {
        this.additiveMaterialChecklistService = additiveMaterialChecklistService;
        this.additiveMaterialChecklistRepository = additiveMaterialChecklistRepository;
    }

    /**
     * {@code POST  /additive-material-checklists} : Create a new additiveMaterialChecklist.
     *
     * @param additiveMaterialChecklistDTO the additiveMaterialChecklistDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new additiveMaterialChecklistDTO, or with status {@code 400 (Bad Request)} if the additiveMaterialChecklist has already an ID.
     */
    @PostMapping("")
    public Mono<AdditiveMaterialChecklistDTO> createAdditiveMaterialChecklist(
        @Valid @RequestBody AdditiveMaterialChecklistDTO additiveMaterialChecklistDTO
    ) {
        log.debug("REST request to save AdditiveMaterialChecklist : {}", additiveMaterialChecklistDTO);
        if (additiveMaterialChecklistDTO.getId() != null) {
            throw new BadRequestAlertException("A new additiveMaterialChecklist cannot already have an ID", ENTITY_NAME, "idexists");
        }
        additiveMaterialChecklistDTO.setId(UUID.randomUUID());
        return additiveMaterialChecklistService
            .save(additiveMaterialChecklistDTO)
            .doOnError(
                throwable -> {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, throwable.getMessage());
                }
            );
    }

    /**
     * {@code PATCH  /additive-material-checklists/:id} : Partial updates given fields of an existing additiveMaterialChecklist, field will ignore if it is null
     *
     * @param id the id of the additiveMaterialChecklistDTO to save.
     * @param additiveMaterialChecklistDTO the additiveMaterialChecklistDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated additiveMaterialChecklistDTO,
     * or with status {@code 400 (Bad Request)} if the additiveMaterialChecklistDTO is not valid,
     * or with status {@code 404 (Not Found)} if the additiveMaterialChecklistDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the additiveMaterialChecklistDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<AdditiveMaterialChecklistDTO>> partialUpdateAdditiveMaterialChecklist(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody AdditiveMaterialChecklistDTO additiveMaterialChecklistDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update AdditiveMaterialChecklist partially : {}, {}", id, additiveMaterialChecklistDTO);
        additiveMaterialChecklistDTO.setId(id);
        return additiveMaterialChecklistRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<AdditiveMaterialChecklistDTO> result = additiveMaterialChecklistService.partialUpdate(additiveMaterialChecklistDTO);

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

    @GetMapping("/{id}")
    public Mono<ResponseEntity<AdditiveMaterialChecklistDTO>> getAdditiveMaterialChecklist(@PathVariable UUID id) {
        log.debug("REST request to get AdditiveMaterialChecklist : {}", id);
        Mono<AdditiveMaterialChecklistDTO> additiveMaterialChecklistDTO = additiveMaterialChecklistService.findOne(id);
        return ResponseUtil.wrapOrNotFound(additiveMaterialChecklistDTO);
    }

    @GetMapping("/{id}/release-warehouse")
    public Mono<ResponseEntity<ApiResponse<ReleaseWarehouseDTO>>> getReleaseWarehouse(@ParameterObject Pageable pageable,
                                                                                      @PathVariable UUID id,
                                                                                      ServerHttpRequest request) {
        return SecurityUtils.getUserJWTDetail()
            .flatMap(user -> {
                return additiveMaterialChecklistService.countReleaseWarehouseByMaterialAdditiveChecklistId(id)
                    .zipWith(additiveMaterialChecklistService.getReleaseWarehouseByMaterialAdditiveChecklistId(pageable, id)
                        .collectList())
                    .map(countWithEntities -> ResponseEntity.ok()
                        .body(new ApiResponse<>(countWithEntities.getT2(), countWithEntities.getT1())));
            });
    }

    /**
     * {@code DELETE  /additive-material-checklists/:id} : delete the "id" additiveMaterialChecklist.
     *
     * @param id the id of the additiveMaterialChecklistDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<Void> deleteAdditiveMaterialChecklist(@PathVariable("id") UUID id) {
        log.debug("REST request to delete AdditiveMaterialChecklist : {}", id);
        return additiveMaterialChecklistService
            .delete(id);
    }




}
