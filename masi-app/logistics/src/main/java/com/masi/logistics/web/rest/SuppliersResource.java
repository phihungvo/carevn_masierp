package com.masi.logistics.web.rest;

import com.carevn.masi.dto.ApiResponse;
import com.carevn.masi.utils.SecurityUtils;
import com.masi.logistics.repository.SuppliersRepository;
import com.masi.logistics.service.SuppliersService;
import com.masi.logistics.service.dto.ItemDTO;
import com.masi.logistics.service.dto.SuppliersDTO;
import com.masi.logistics.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link com.masi.logistics.domain.Suppliers}.
 */
@RestController
@RequestMapping("/api/suppliers")
public class SuppliersResource {

    private static final Logger log = LoggerFactory.getLogger(SuppliersResource.class);

    private static final String ENTITY_NAME = "masiLogisticsSuppliers";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SuppliersService suppliersService;

    private final SuppliersRepository suppliersRepository;

    public SuppliersResource(SuppliersService suppliersService, SuppliersRepository suppliersRepository) {
        this.suppliersService = suppliersService;
        this.suppliersRepository = suppliersRepository;
    }

    /**
     * {@code POST  /suppliers} : Create a new suppliers.
     *
     * @param suppliersDTO the suppliersDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new suppliersDTO, or with status {@code 400 (Bad Request)} if the suppliers has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<SuppliersDTO>> createSuppliers(@Valid @RequestBody SuppliersDTO suppliersDTO) throws URISyntaxException {
        log.debug("REST request to save Suppliers : {}", suppliersDTO);
        return suppliersService
            .save(suppliersDTO)
            .handle((result, sink) -> {
                try {
                    sink.next(ResponseEntity.created(new URI("/api/suppliers/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result));
                } catch (URISyntaxException e) {
                    sink.error(new RuntimeException(e));
                }
            });
    }

    /**
     * {@code PUT  /suppliers/:id} : Updates an existing suppliers.
     *
     * @param id the id of the suppliersDTO to save.
     * @param suppliersDTO the suppliersDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated suppliersDTO,
     * or with status {@code 400 (Bad Request)} if the suppliersDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the suppliersDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<SuppliersDTO>> updateSuppliers(
        @PathVariable(value = "id", required = false) final UUID id,
        @Valid @RequestBody SuppliersDTO suppliersDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Suppliers : {}, {}", id, suppliersDTO);
        if (suppliersDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, suppliersDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return suppliersRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return suppliersService
                    .update(suppliersDTO)
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
     * {@code PATCH  /suppliers/:id} : Partial updates given fields of an existing suppliers, field will ignore if it is null
     *
     * @param id the id of the suppliersDTO to save.
     * @param suppliersDTO the suppliersDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated suppliersDTO,
     * or with status {@code 400 (Bad Request)} if the suppliersDTO is not valid,
     * or with status {@code 404 (Not Found)} if the suppliersDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the suppliersDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<SuppliersDTO>> partialUpdateSuppliers(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody SuppliersDTO suppliersDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Suppliers partially : {}, {}", id, suppliersDTO);
        suppliersDTO.setId(id);
        return suppliersRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<SuppliersDTO> result = suppliersService.partialUpdate(suppliersDTO);

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
     * {@code GET  /suppliers} : get all the suppliers.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of suppliers in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<ApiResponse<SuppliersDTO>>> getAllSuppliers(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request,
        @RequestParam(value = "search", required = false) String search,
        @RequestParam(value = "status", required = false) Boolean status
    ) {
        log.debug("REST request to get a page of Suppliers");
        return suppliersService
            .countAll(search, status)
            .zipWith(suppliersService.findAll(pageable, search, status).collectList())
            .map(
                countWithEntities -> ResponseEntity.ok()
                    .headers(
                        PaginationUtil.generatePaginationHttpHeaders(
                            ForwardedHeaderUtils
                                .adaptFromForwardedHeaders(
                                    request.getURI(),
                                    request.getHeaders()),
                            new PageImpl<>(countWithEntities.getT2(), pageable,
                                countWithEntities.getT1())))
                    .body(new ApiResponse<>(countWithEntities.getT2(), countWithEntities.getT1()))
            );
    }

//    /**
//     * {@code GET  /suppliers} : get all the suppliers.
//     *
//     * @param pageable the pagination information.
//     * @param request a {@link ServerHttpRequest} request.
//     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of suppliers in body.
//     */
//    @GetMapping(value = "/{id}/items", produces = MediaType.APPLICATION_JSON_VALUE)
//    public Mono<ResponseEntity<ApiResponse<ItemDTO>>> getItemBySupplier(
//        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
//        ServerHttpRequest request
//    ) {
//        log.debug("REST request to get a page of Suppliers");
//        return suppliersService
//            .countAll()
//            .zipWith(suppliersService.findAll(pageable).collectList())
//            .map(
//                countWithEntities -> ResponseEntity.ok()
//                    .headers(
//                        PaginationUtil.generatePaginationHttpHeaders(
//                            ForwardedHeaderUtils
//                                .adaptFromForwardedHeaders(
//                                    request.getURI(),
//                                    request.getHeaders()),
//                            new PageImpl<>(countWithEntities.getT2(), pageable,
//                                countWithEntities.getT1())))
//                    .body(new ApiResponse<>(countWithEntities.getT2(), countWithEntities.getT1()))
//            );
//    }

    /**
     * {@code GET  /suppliers/:id} : get the "id" suppliers.
     *
     * @param id the id of the suppliersDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the suppliersDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<SuppliersDTO>> getSuppliers(@PathVariable("id") UUID id) {
        log.debug("REST request to get Suppliers : {}", id);
        return SecurityUtils.getUserJWTDetail().flatMap(user -> {
            Mono<SuppliersDTO> suppliersDTO = suppliersService.findOne(id, user.getCompanyId());
            return ResponseUtil.wrapOrNotFound(suppliersDTO);
        });
    }

    /**
     * {@code DELETE  /suppliers/:id} : delete the "id" suppliers.
     *
     * @param id the id of the suppliersDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteSuppliers(@PathVariable("id") UUID id) {
        log.debug("REST request to delete Suppliers : {}", id);
        return SecurityUtils.getUserJWTDetail().flatMap(user -> {
            return suppliersService.delete(id,  user.getCompanyId(), user.getUserId().toString()).then(Mono.just(ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build()));
        });
    }

    /**
     * {@code PUT  /suppliers/:id/active} : disable an existing suppliers.
     *
     * @param id the id of the suppliersDTO to active.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated suppliersDTO,
     * or with status {@code 400 (Bad Request)} if the suppliersDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the suppliersDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping("/{id}/disable")
    public Mono<ResponseEntity<SuppliersDTO>> activeSuppliers(
        @PathVariable(value = "id", required = false) final UUID id
    ) throws URISyntaxException {
        log.debug("REST request to active Suppliers : {}", id);
        return SecurityUtils.getUserJWTDetail().flatMap(login -> {
            return suppliersService
                .active(id, false)
                .then(Mono.defer(() -> {
                    var supplier = suppliersService.findOne(id, login.getCompanyId());
                    return ResponseUtil.wrapOrNotFound(supplier);
                }));

        });
    }

    // api enable supplier
    @PatchMapping("/{id}/active")
    public Mono<ResponseEntity<SuppliersDTO>> enableSuppliers(
        @PathVariable(value = "id", required = false) final UUID id
    ) throws URISyntaxException {
        log.debug("REST request to enable Suppliers : {}", id);
        return SecurityUtils.getUserJWTDetail().flatMap(login -> {
            return suppliersService
                .active(id, true)
                .then(Mono.defer(() -> {
                    var supplier = suppliersService.findOne(id, login.getCompanyId());
                    return ResponseUtil.wrapOrNotFound(supplier);
                }));

        });
    }

    @GetMapping("/export")
    public Mono<ResponseEntity<byte[]>> exportSuppliers(
        @RequestParam(value = "search", required = false) String search,
        @RequestParam(value = "status", required = false) Boolean status
    ) {
        return suppliersService.exportRecordsAsCSV(search, status).map(
            csv -> {
                return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"NCC" + ".xlsx\"")
                    .contentType(org.springframework.http.MediaType.APPLICATION_OCTET_STREAM)
                    .body(csv);
            }
        );
    }
}
