package com.masi.sale.web.rest;

import com.masi.sale.repository.QuotationExportRepository;
import com.masi.sale.service.QuotationExportService;
import com.masi.sale.service.dto.QuotationExportDTO;
import com.masi.sale.web.rest.errors.BadRequestAlertException;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
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
 * REST controller for managing {@link com.masi.sale.domain.QuotationExport}.
 */
@RestController
@RequestMapping("/api/quotation-exports")
public class QuotationExportResource {

    private static final Logger log = LoggerFactory.getLogger(QuotationExportResource.class);

    private static final String ENTITY_NAME = "masiSaleQuotationExport";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final QuotationExportService quotationExportService;

    private final QuotationExportRepository quotationExportRepository;

    public QuotationExportResource(QuotationExportService quotationExportService, QuotationExportRepository quotationExportRepository) {
        this.quotationExportService = quotationExportService;
        this.quotationExportRepository = quotationExportRepository;
    }

    /**
     * {@code POST  /quotation-exports} : Create a new quotationExport.
     *
     * @param quotationExportDTO the quotationExportDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new quotationExportDTO, or with status {@code 400 (Bad Request)} if the quotationExport has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Operation(hidden = true)
    @PostMapping("")
    public Mono<ResponseEntity<QuotationExportDTO>> createQuotationExport(@Valid @RequestBody QuotationExportDTO quotationExportDTO)
        throws URISyntaxException {
        log.debug("REST request to save QuotationExport : {}", quotationExportDTO);
        if (quotationExportDTO.getId() != null) {
            throw new BadRequestAlertException("A new quotationExport cannot already have an ID", ENTITY_NAME, "idexists");
        }
        quotationExportDTO.setId(UUID.randomUUID());
        return quotationExportService
            .save(quotationExportDTO)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/quotation-exports/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /quotation-exports/:id} : Updates an existing quotationExport.
     *
     * @param id the id of the quotationExportDTO to save.
     * @param quotationExportDTO the quotationExportDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated quotationExportDTO,
     * or with status {@code 400 (Bad Request)} if the quotationExportDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the quotationExportDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Operation(hidden = true)
    @PutMapping("/{id}")
    public Mono<ResponseEntity<QuotationExportDTO>> updateQuotationExport(
        @PathVariable(value = "id", required = false) final UUID id,
        @Valid @RequestBody QuotationExportDTO quotationExportDTO
    ) throws URISyntaxException {
        log.debug("REST request to update QuotationExport : {}, {}", id, quotationExportDTO);
        if (quotationExportDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, quotationExportDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return quotationExportRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return quotationExportService
                    .update(quotationExportDTO)
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
     * {@code PATCH  /quotation-exports/:id} : Partial updates given fields of an existing quotationExport, field will ignore if it is null
     *
     * @param id the id of the quotationExportDTO to save.
     * @param quotationExportDTO the quotationExportDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated quotationExportDTO,
     * or with status {@code 400 (Bad Request)} if the quotationExportDTO is not valid,
     * or with status {@code 404 (Not Found)} if the quotationExportDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the quotationExportDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Operation(hidden = true)
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<QuotationExportDTO>> partialUpdateQuotationExport(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody QuotationExportDTO quotationExportDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update QuotationExport partially : {}, {}", id, quotationExportDTO);
        if (quotationExportDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, quotationExportDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return quotationExportRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<QuotationExportDTO> result = quotationExportService.partialUpdate(quotationExportDTO);

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
     * {@code GET  /quotation-exports} : get all the quotationExports.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of quotationExports in body.
     */
    @Operation(hidden = true)
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<QuotationExportDTO>>> getAllQuotationExports(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of QuotationExports");
        return quotationExportService
            .countAll()
            .zipWith(quotationExportService.findAll(pageable).collectList())
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
     * {@code GET  /quotation-exports/:id} : get the "id" quotationExport.
     *
     * @param id the id of the quotationExportDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the quotationExportDTO, or with status {@code 404 (Not Found)}.
     */
    @Operation(hidden = true)
    @GetMapping("/{id}")
    public Mono<ResponseEntity<QuotationExportDTO>> getQuotationExport(@PathVariable("id") UUID id) {
        log.debug("REST request to get QuotationExport : {}", id);
        Mono<QuotationExportDTO> quotationExportDTO = quotationExportService.findOne(id);
        return ResponseUtil.wrapOrNotFound(quotationExportDTO);
    }

    /**
     * {@code DELETE  /quotation-exports/:id} : delete the "id" quotationExport.
     *
     * @param id the id of the quotationExportDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @Operation(hidden = true)
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteQuotationExport(@PathVariable("id") UUID id) {
        log.debug("REST request to delete QuotationExport : {}", id);
        return quotationExportService
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
