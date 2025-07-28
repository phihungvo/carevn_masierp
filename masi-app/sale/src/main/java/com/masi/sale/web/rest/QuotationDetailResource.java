package com.masi.sale.web.rest;

import com.carevn.masi.utils.SecurityUtils;
import com.carevn.masi.utils.Utilities;
import com.masi.sale.repository.QuotationDetailRepository;
import com.masi.sale.service.QuotationDetailService;
import com.masi.sale.service.dto.QuotationDetailCreateDTO;
import com.masi.sale.service.dto.QuotationDetailDTO;
import com.masi.sale.service.dto.QuotationDetailUpdateDTO;
import com.masi.sale.web.rest.errors.BadRequestAlertException;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import org.mapstruct.ap.shaded.freemarker.template.utility.SecurityUtilities;
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
 * REST controller for managing {@link com.masi.sale.domain.QuotationDetail}.
 */
@RestController
@RequestMapping("/api/quotation-details")
public class QuotationDetailResource {

    private static final Logger log = LoggerFactory.getLogger(QuotationDetailResource.class);

    private static final String ENTITY_NAME = "masiSaleQuotationDetail";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final QuotationDetailService quotationDetailService;

    private final QuotationDetailRepository quotationDetailRepository;

    public QuotationDetailResource(QuotationDetailService quotationDetailService,
            QuotationDetailRepository quotationDetailRepository) {
        this.quotationDetailService = quotationDetailService;
        this.quotationDetailRepository = quotationDetailRepository;
    }

    /**
     * {@code POST  /quotation-details} : Create a new quotationDetail.
     *
     * @param quotationDetailDTO the quotationDetailDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with
     *         body the new quotationDetailDTO, or with status
     *         {@code 400 (Bad Request)} if the quotationDetail has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<Map<String, Object>>> createQuotationDetail(
            @Valid @RequestBody QuotationDetailCreateDTO quotationDetailCreateDTO)
            throws URISyntaxException {
        log.debug("REST request to save QuotationDetail : {}", quotationDetailCreateDTO);
        var quotationDetailDTO = quotationDetailCreateDTO.toDto();
        return quotationDetailService
                .save(quotationDetailDTO)
                .map(result -> {
                    try {
                        return ResponseEntity.created(new URI("/api/quotation-details/" + result.getId()))
                                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME,
                                        result.getId().toString()))
                                .body(Utilities.generateResponse("SUCCESS", result));
                    } catch (URISyntaxException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    /**
     * {@code PUT  /quotation-details/:id} : Updates an existing quotationDetail.
     *
     * @param id                 the id of the quotationDetailDTO to save.
     * @param quotationDetailDTO the quotationDetailDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the updated quotationDetailDTO,
     *         or with status {@code 400 (Bad Request)} if the quotationDetailDTO is
     *         not valid,
     *         or with status {@code 500 (Internal Server Error)} if the
     *         quotationDetailDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Operation(hidden = true)
    @PutMapping("/{id}")
    public Mono<ResponseEntity<QuotationDetailDTO>> updateQuotationDetail(
            @PathVariable(value = "id", required = false) final UUID id,
            @Valid @RequestBody QuotationDetailDTO quotationDetailDTO) throws URISyntaxException {
        log.debug("REST request to update QuotationDetail : {}, {}", id, quotationDetailDTO);
        if (quotationDetailDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, quotationDetailDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return quotationDetailRepository
                .existsById(id)
                .flatMap(exists -> {
                    if (!exists) {
                        return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                    }

                    return quotationDetailService
                            .update(quotationDetailDTO)
                            .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                            .map(
                                    result -> ResponseEntity.ok()
                                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true,
                                                    ENTITY_NAME, result.getId().toString()))
                                            .body(result));
                });
    }

    /**
     * {@code PATCH  /quotation-details/:id} : Partial updates given fields of an
     * existing quotationDetail, field will ignore if it is null
     *
     * @param id                 the id of the quotationDetailDTO to save.
     * @param quotationDetailDTO the quotationDetailDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the updated quotationDetailDTO,
     *         or with status {@code 400 (Bad Request)} if the quotationDetailDTO is
     *         not valid,
     *         or with status {@code 404 (Not Found)} if the quotationDetailDTO is
     *         not found,
     *         or with status {@code 500 (Internal Server Error)} if the
     *         quotationDetailDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Operation(summary = "Cập nhật chi tiết báo giá")
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<QuotationDetailDTO>> partialUpdateQuotationDetail(
            @PathVariable(value = "id", required = false) final UUID id,
            @NotNull @RequestBody QuotationDetailUpdateDTO quotationDetailUpdateDTO) throws URISyntaxException {
        QuotationDetailDTO quotationDetailDTO = quotationDetailUpdateDTO.toDto();
        log.debug("REST request to partial update QuotationDetail partially : {}, {}", id, quotationDetailDTO);
        if (quotationDetailDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, quotationDetailDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return SecurityUtils.getUserJWTDetail().flatMap(user -> {
            return quotationDetailRepository
                    .existsById(id)
                    .flatMap(exists -> {
                        if (!exists) {
                            return Mono
                                    .error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                        }
                        quotationDetailDTO.setUpdatedBy(user.getUserId().toString());
                        quotationDetailDTO.setLastUpdated(ZonedDateTime.now());
                        quotationDetailDTO.setCompany(user.getCompanyId());
                        Mono<QuotationDetailDTO> result = quotationDetailService.partialUpdate(quotationDetailDTO);

                        return result
                                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                                .map(
                                        res -> ResponseEntity.ok()
                                                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true,
                                                        ENTITY_NAME, res.getId().toString()))
                                                .body(res));
                    });
        });
    }

    /**
     * {@code GET  /quotation-details} : get all the quotationDetails.
     *
     * @param pageable the pagination information.
     * @param request  a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
     *         of quotationDetails in body.
     */
    @Operation(hidden = true)
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<QuotationDetailDTO>>> getAllQuotationDetails(
            @org.springdoc.core.annotations.ParameterObject Pageable pageable,
            ServerHttpRequest request) {
        log.debug("REST request to get a page of QuotationDetails");
        return quotationDetailService
                .countAll()
                .zipWith(quotationDetailService.findAll(pageable).collectList())
                .map(
                        countWithEntities -> ResponseEntity.ok()
                                .headers(
                                        PaginationUtil.generatePaginationHttpHeaders(
                                                ForwardedHeaderUtils.adaptFromForwardedHeaders(request.getURI(),
                                                        request.getHeaders()),
                                                new PageImpl<>(countWithEntities.getT2(), pageable,
                                                        countWithEntities.getT1())))
                                .body(countWithEntities.getT2()));
    }

    /**
     * {@code GET  /quotation-details/:id} : get the "id" quotationDetail.
     *
     * @param id the id of the quotationDetailDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the quotationDetailDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<QuotationDetailDTO>> getQuotationDetail(@PathVariable("id") UUID id) {
        log.debug("REST request to get QuotationDetail : {}", id);
        return SecurityUtils.getUserJWTDetail().flatMap(user -> {
            return quotationDetailService
                    .findOne(id, user.getCompanyId())
                    .map(quotationDetailDTO -> {
                        return ResponseEntity.ok().body(quotationDetailDTO);
                    });
        });
    }

    /**
     * {@code DELETE  /quotation-details/:id} : delete the "id" quotationDetail.
     *
     * @param id the id of the quotationDetailDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteQuotationDetail(@PathVariable("id") UUID id) {
        log.debug("REST request to delete QuotationDetail : {}", id);
        return SecurityUtils.getUserJWTDetail().flatMap(user -> {
            return quotationDetailService
                    .removeById(id, user.getCompanyId(), user.getUserId().toString())
                    .map(
                            result -> ResponseEntity.noContent()
                                    .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME,
                                            id.toString()))
                                    .build());
        });
    }
}
