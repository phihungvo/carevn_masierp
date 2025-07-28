package com.masi.sale.web.rest;

import com.carevn.masi.dto.ApiResponse;
import com.carevn.masi.utils.SecurityUtils;
import com.carevn.masi.utils.Utilities;
import com.masi.sale.repository.QuotationRepository;
import com.masi.sale.service.QuotationService;
import com.masi.sale.service.dto.CustomerProcessQuotationDTO;
import com.masi.sale.service.dto.InternalProcessQuotationDTO;
import com.masi.sale.service.dto.QuotationCreateDTO;
import com.masi.sale.service.dto.QuotationDTO;
import com.masi.sale.service.dto.QuotationGetListDTO;
import com.masi.sale.service.dto.QuotationUpdateDTO;
import com.masi.sale.utilities.HandleExportQuotation;
import com.masi.sale.web.rest.errors.BadRequestAlertException;

import feign.Param;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.io.FileInputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import org.apache.kafka.shaded.com.google.protobuf.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.annotations.ParameterObject;
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
import reactor.core.scheduler.Schedulers;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link com.masi.sale.domain.Quotation}.
 */
@RestController
@RequestMapping("/api/quotations")
public class QuotationResource {

    private static final Logger log = LoggerFactory.getLogger(QuotationResource.class);

    private static final String ENTITY_NAME = "masiSaleQuotation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final QuotationService quotationService;

    private final QuotationRepository quotationRepository;

    public QuotationResource(QuotationService quotationService, QuotationRepository quotationRepository) {
        this.quotationService = quotationService;
        this.quotationRepository = quotationRepository;
    }

    /**
     * {@code POST  /quotations} : Create a new quotation.
     *
     * @param quotationDTO the quotationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with
     *         body the new quotationDTO, or with status {@code 400 (Bad Request)}
     *         if the quotation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<Map<String, Object>>> createQuotation(
            @Valid @RequestBody QuotationCreateDTO quotationCreateDTO)
            throws URISyntaxException {
        log.debug("REST request to save Quotation : {}", quotationCreateDTO);
        return quotationService
                .createQuotation(quotationCreateDTO)
            .handle((result, sink) -> {
                try {
                    sink.next(ResponseEntity.created(new URI("/api/quotations/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME,
                            result.getId().toString()))
                        .body(Utilities.generateResponse("SUCCESS", result)));
                } catch (URISyntaxException e) {
                    sink.error(new RuntimeException(e));
                }
            });
    }

    /**
     * {@code PUT  /quotations/:id} : Updates an existing quotation.
     *
     * @param id           the id of the quotationDTO to save.
     * @param quotationDTO the quotationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the updated quotationDTO,
     *         or with status {@code 400 (Bad Request)} if the quotationDTO is not
     *         valid,
     *         or with status {@code 500 (Internal Server Error)} if the
     *         quotationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Operation(hidden = true)
    @PutMapping("/{id}")
    public Mono<ResponseEntity<QuotationDTO>> updateQuotation(
            @PathVariable(value = "id", required = false) final UUID id,
            @Valid @RequestBody QuotationDTO quotationDTO) throws URISyntaxException {
        log.debug("REST request to update Quotation : {}, {}", id, quotationDTO);
        if (quotationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, quotationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return quotationRepository
                .existsById(id)
                .flatMap(exists -> {
                    if (!exists) {
                        return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                    }

                    return quotationService
                            .update(quotationDTO)
                            .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                            .map(
                                    result -> ResponseEntity.ok()
                                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true,
                                                    ENTITY_NAME, result.getId().toString()))
                                            .body(result));
                });
    }

    /**
     * {@code PATCH  /quotations/:id} : Partial updates given fields of an existing
     * quotation, field will ignore if it is null
     *
     * @param id           the id of the quotationDTO to save.
     * @param quotationDTO the quotationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the updated quotationDTO,
     *         or with status {@code 400 (Bad Request)} if the quotationDTO is not
     *         valid,
     *         or with status {@code 404 (Not Found)} if the quotationDTO is not
     *         found,
     *         or with status {@code 500 (Internal Server Error)} if the
     *         quotationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Operation(summary = "Update quotation")
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<Map<String, Object>>> partialUpdateQuotation(
            @PathVariable(value = "id", required = false) final UUID id,
            @NotNull @RequestBody QuotationUpdateDTO quotationUpdateDTO) throws URISyntaxException {
        log.debug("REST request to partial update Quotation partially : {}, {}", id, quotationUpdateDTO);
        quotationUpdateDTO.setId(id);
        return quotationRepository
                .existsById(id)
                .flatMap(exists -> {
                    if (!exists) {
                        return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                    }

                    return SecurityUtils.getUserJWTDetail().flatMap(user -> {
                        quotationUpdateDTO.setCompany(user.getCompanyId());
                        quotationUpdateDTO.setUpdateBy(user.getUserId().toString());
                        Mono<QuotationDTO> result = quotationService.handleUpdateQuotation(quotationUpdateDTO);
                        return result
                                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                                .map(
                                        res -> ResponseEntity.ok()
                                                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true,
                                                        ENTITY_NAME, res.getId().toString()))
                                                .body(Utilities.generateResponse("SUCCESS", res)));
                    });
                });
    }

    @Operation(summary = "Gửi duyệt báo giá nội bộ")
    @PatchMapping(value = "/{id}/internal-sent")
    public Mono<ResponseEntity<Map<String, Object>>> internalSentQuotation(
            @PathVariable(value = "id", required = false) final UUID id) throws URISyntaxException {
        log.debug("REST request to partial update Quotation partially : {}", id);
        return quotationRepository
                .existsById(id)
                .flatMap(exists -> {
                    if (!exists) {
                        return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                    }

                    return SecurityUtils.getUserJWTDetail().flatMap(user -> {
                        Mono<QuotationDTO> result = quotationService.handleInternalSendQuotation(id,
                                user.getCompanyId(), user.getUserId().toString());
                        return result
                                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                                .map(
                                        res -> ResponseEntity.ok()
                                                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true,
                                                        ENTITY_NAME, res.getId().toString()))
                                                .body(Utilities.generateResponse("SUCCESS", res)));
                    });
                });
    }

    @Operation(summary = "Duyệt nội bộ báo giá nội bộ")
    @PatchMapping(value = "/{id}/internal-process", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<Map<String, Object>>> internalProcessQuotation(
            @PathVariable(value = "id", required = false) final UUID id,
         @RequestBody InternalProcessQuotationDTO dto) throws URISyntaxException {
        log.debug("REST request to partial update Quotation partially : {}", id);
        return quotationRepository
                .existsById(id)
                .flatMap(exists -> {
                    if (!exists) {
                        return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                    }

                    return SecurityUtils.getUserJWTDetail().flatMap(user -> {
                        dto.setCompany(user.getCompanyId());
                        dto.setUpdatedBy(user.getUserId().toString());
                        dto.setId(id);
                        Mono<QuotationDTO> result = quotationService.handleProcessInternalQuotation(dto);
                        return result
                                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                                .map(
                                        res -> ResponseEntity.ok()
                                                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true,
                                                        ENTITY_NAME, res.getId().toString()))
                                                .body(Utilities.generateResponse("SUCCESS", res)));
                    });
                });
    }

    @Operation(summary = "Huỷ bảng báo giá")
    @PatchMapping(value = "/{id}/cancel")
    public Mono<ResponseEntity<Map<String, Object>>> cancelQuotation(
            @PathVariable(value = "id", required = false) final UUID id) throws URISyntaxException {
        log.debug("REST request to partial update Quotation partially : {}", id);
        return quotationRepository
                .existsById(id)
                .flatMap(exists -> {
                    if (!exists) {
                        return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                    }

                    return SecurityUtils.getUserJWTDetail().flatMap(user -> {
                        Mono<QuotationDTO> result = quotationService.handleCancelQuotation(id,
                                user.getCompanyId(), user.getUserId().toString());
                        return result
                                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                                .map(
                                        res -> ResponseEntity.ok()
                                                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true,
                                                        ENTITY_NAME, res.getId().toString()))
                                                .body(Utilities.generateResponse("SUCCESS", res)));
                    });
                });
    }

    @Operation(summary = "Gửi bảng báo giá cho khách hàng")
    @PatchMapping(value = "/{id}/send-customer")
    public Mono<ResponseEntity<Map<String, Object>>> sendQuotation(
            @PathVariable(value = "id", required = false) final UUID id) throws URISyntaxException {
        log.debug("REST request to partial update Quotation partially : {}", id);
        return quotationRepository
                .existsById(id)
                .flatMap(exists -> {
                    if (!exists) {
                        return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                    }

                    return SecurityUtils.getUserJWTDetail().flatMap(user -> {
                        Mono<QuotationDTO> result = quotationService.handleSendCustomerQuotation(id,
                                user.getCompanyId(), user.getUserId().toString());
                        return result
                                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                                .map(
                                        res -> ResponseEntity.ok()
                                                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true,
                                                        ENTITY_NAME, res.getId().toString()))
                                                .body(Utilities.generateResponse("SUCCESS", res)));
                    });
                });
    }

    @Operation(summary = "Khách hàng duyệt bảng báo giá")
    @PatchMapping(value = "/{id}/customer-process", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<Map<String, Object>>> customerProcessQuotation(
            @PathVariable(value = "id", required = false) final UUID id,
            @Valid @RequestBody CustomerProcessQuotationDTO dto) throws URISyntaxException {
        log.debug("REST request to partial update Quotation partially : {}", id);
        return quotationRepository
                .existsById(id)
                .flatMap(exists -> {
                    if (!exists) {
                        return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                    }

                    return SecurityUtils.getUserJWTDetail().flatMap(user -> {
                        dto.setCompany(user.getCompanyId());
                        dto.setUpdatedBy(user.getUserId().toString());
                        dto.setId(id);
                        Mono<QuotationDTO> result = quotationService.handleCustomerProcessQuotation(dto);
                        return result
                                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                                .map(
                                        res -> ResponseEntity.ok()
                                                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true,
                                                        ENTITY_NAME, res.getId().toString()))
                                                .body(Utilities.generateResponse("SUCCESS", res)));
                    });
                });
    }

    /**
     * {@code GET  /quotations} : get all the quotations.
     *
     * @param pageable the pagination information.
     * @param request  a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
     *         of quotations in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<ApiResponse<QuotationDTO>>> getAllQuotations(
            @org.springdoc.core.annotations.ParameterObject Pageable pageable,
            @ParameterObject QuotationGetListDTO dto,
            ServerHttpRequest request) {
        log.debug("REST request to get a page of Quotations");
        return SecurityUtils.getUserJWTDetail().flatMap(user -> {
            dto.setCompany(user.getCompanyId());
            dto.setDepartment(user.getGroupId());
            dto.setAuth(user.getAuthorities());
            return quotationService
                    .countByQuery(dto)
                    .zipWith(quotationService.findAllByQuery(pageable, dto).collectList())
                    .map(
                            countWithEntities -> ResponseEntity.ok()
                                    .headers(
                                            PaginationUtil.generatePaginationHttpHeaders(
                                                    ForwardedHeaderUtils.adaptFromForwardedHeaders(request.getURI(),
                                                            request.getHeaders()),
                                                    new PageImpl<>(countWithEntities.getT2(), pageable,
                                                            countWithEntities.getT1())))
                                    .body(new ApiResponse<>(countWithEntities.getT2(), countWithEntities.getT1())));
        });
    }

    /**
     * {@code GET  /quotations/:id} : get the "id" quotation.
     *
     * @param id the id of the quotationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the quotationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<QuotationDTO>> getQuotation(@PathVariable("id") UUID id) {
        log.debug("REST request to get Quotation : {}", id);
        return SecurityUtils.getUserJWTDetail().flatMap(user -> {
            return quotationService.findOne(id, user.getCompanyId())
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(quotation -> {
                        if (quotation.getCompany().equals(user.getCompanyId())) {
                            return ResponseEntity.ok(quotation);
                        } else {
                            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
                        }
                    });
        });
    }

    @GetMapping("/{id}/export")
    public Mono<ResponseEntity<InputStreamResource>> getFileAttachment(
            @RequestParam(value = "download", required = false, defaultValue = "false") boolean download,
            @RequestParam(value = "option", required = false, defaultValue = "2") Integer option,
            @PathVariable("id") UUID id) {
        log.debug("REST request to get FileAttachment : {}", id);
        if (option == null) {
            option = 2;
        }
        int value = option;
        return SecurityUtils.getUserJWTDetail().flatMap(user -> quotationService.findOne(id, user.getCompanyId())
                .handle((quotation, sink) -> {
                    try {
                        var data = HandleExportQuotation.exportQuotation(user.getCompanyId(), quotation, value);
                        FileInputStream fileInputStream = new FileInputStream(data.getT1());
                        InputStreamResource inputStreamResource = new InputStreamResource(fileInputStream);
                        HttpHeaders headers = new HttpHeaders();
                        String contentType = Files.probeContentType(Path.of(data.getT1()));

                        if (contentType == null) {
                            contentType = "application/octet-stream"; // Set a default content type if probeContentType
                            // fails
                        }
                        if (download) {
                            headers.add(HttpHeaders.CONTENT_DISPOSITION,
                                    String.format("attachment; filename=\"%s\"", data.getT2() + ".pdf"));
                        } else {
                            headers.add(HttpHeaders.CONTENT_TYPE, contentType);
                            headers.add("content-name", data.getT2() + ".pdf");
                        }

                        sink.next(new ResponseEntity<>(inputStreamResource, headers, HttpStatus.OK));
                    } catch (Exception e) {
                        sink.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage()));
                    }
                }));
    }

    /**
     * {@code DELETE  /quotations/:id} : delete the "id" quotation.
     *
     * @param id the id of the quotationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteQuotation(@PathVariable("id") UUID id) {
        log.debug("REST request to delete Quotation : {}", id);
        return SecurityUtils.getUserJWTDetail().flatMap(user -> {
            return quotationService
                    .removeQuotation(id, user.getCompanyId(), user.getUserId().toString())
                    .map(
                            result -> ResponseEntity.noContent()
                                    .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME,
                                            id.toString()))
                                    .build());
        });
    }
}
