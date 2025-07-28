package com.masi.logistics.web.rest;

import com.carevn.masi.dto.ApiResponse;
import com.carevn.masi.utils.SecurityUtils;
import com.masi.logistics.domain.criteria.VatRateCriteria;
import com.masi.logistics.repository.VatRateRepository;
import com.masi.logistics.security.AuthoritiesConstants;
import com.masi.logistics.service.VatRateService;
import com.masi.logistics.service.dto.VatRateDTO;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.ForwardedHeaderUtils;
import reactor.core.publisher.Mono;
import tech.jhipster.service.filter.StringFilter;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link com.masi.logistics.domain.VatRate}.
 */
@RestController
@RequestMapping("/api/vat-rates")
public class VatRateResource {

    private static final Logger log = LoggerFactory.getLogger(VatRateResource.class);

    private static final String ENTITY_NAME = "masiLogisticsVatRate";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VatRateService vatRateService;

    private final VatRateRepository vatRateRepository;

    public VatRateResource(VatRateService vatRateService, VatRateRepository vatRateRepository) {
        this.vatRateService = vatRateService;
        this.vatRateRepository = vatRateRepository;
    }

    /**
     * {@code POST  /vat-rates} : Create a new vatRate.
     *
     * @param vatRateDTO the vatRateDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new vatRateDTO, or with status {@code 400 (Bad Request)} if the vatRate has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<VatRateDTO>> createVatRate(@Valid @RequestBody VatRateDTO vatRateDTO) throws URISyntaxException {
        log.debug("REST request to save VatRate : {}", vatRateDTO);
        if (vatRateDTO.getId() != null) {
            throw new BadRequestAlertException("A new vatRate cannot already have an ID", ENTITY_NAME, "idexists");
        }
        vatRateDTO.setId(UUID.randomUUID());
        return vatRateService
                .save(vatRateDTO)
                .map(result -> {
                    try {
                        return ResponseEntity.created(new URI("/api/vat-rates/" + result.getId()))
                                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                                .body(result);
                    } catch (URISyntaxException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    /**
     * {@code PUT  /vat-rates/:id} : Updates an existing vatRate.
     *
     * @param id         the id of the vatRateDTO to save.
     * @param vatRateDTO the vatRateDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vatRateDTO,
     * or with status {@code 400 (Bad Request)} if the vatRateDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the vatRateDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<VatRateDTO>> updateVatRate(
            @PathVariable(value = "id", required = false) final UUID id,
            @Valid @RequestBody VatRateDTO vatRateDTO
    ) throws URISyntaxException {
        log.debug("REST request to update VatRate : {}, {}", id, vatRateDTO);
        if (vatRateDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vatRateDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return vatRateRepository
                .existsById(id)
                .flatMap(exists -> {
                    if (!exists) {
                        return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                    }

                    return vatRateService
                            .update(vatRateDTO)
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
     * {@code PATCH  /vat-rates/:id} : Partial updates given fields of an existing vatRate, field will ignore if it is null
     *
     * @param id         the id of the vatRateDTO to save.
     * @param vatRateDTO the vatRateDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vatRateDTO,
     * or with status {@code 400 (Bad Request)} if the vatRateDTO is not valid,
     * or with status {@code 404 (Not Found)} if the vatRateDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the vatRateDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = {"application/json", "application/merge-patch+json"})
    public Mono<ResponseEntity<VatRateDTO>> partialUpdateVatRate(
            @PathVariable(value = "id", required = false) final UUID id,
            @NotNull @RequestBody VatRateDTO vatRateDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update VatRate partially : {}, {}", id, vatRateDTO);
        if (vatRateDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vatRateDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return vatRateRepository
                .existsById(id)
                .flatMap(exists -> {
                    if (!exists) {
                        return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                    }

                    Mono<VatRateDTO> result = vatRateService.partialUpdate(vatRateDTO);

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
     * {@code GET  /vat-rates} : get all the vatRates.
     *
     * @param pageable the pagination information.
     * @param request  a {@link ServerHttpRequest} request.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of vatRates in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<ApiResponse<VatRateDTO>>> getAllVatRates(
            VatRateCriteria criteria,
            @org.springdoc.core.annotations.ParameterObject Pageable pageable,
            ServerHttpRequest request
    ) {
        log.debug("REST request to get VatRates by criteria: {}", criteria);
        return SecurityUtils.getUserJWTDetail().flatMap(user -> {
            if (!user.getAuthorities().contains(AuthoritiesConstants.SUPER_ADMIN)) {
                var company = (criteria.getCompany() == null ? new StringFilter() : criteria.getCompany()).getEquals();
                if (company == null) {
                    criteria.company().setEquals(user.getCompanyId());
                }
            } else {
                criteria.company().setEquals(user.getCompanyId());
            }
            return vatRateService.countByCriteria(criteria)
                    .zipWith(vatRateService.findByCriteria(criteria, pageable).collectList())
                    .map(countWithEntities ->
                            ResponseEntity.ok().body(new ApiResponse<>(countWithEntities.getT2(), countWithEntities.getT1()))
                    );
        });
    }


    /**
     * {@code GET  /vat-rates/count} : count all the vatRates.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public Mono<ResponseEntity<Long>> countVatRates(VatRateCriteria criteria) {
        log.debug("REST request to count VatRates by criteria: {}", criteria);
        return vatRateService.countByCriteria(criteria).map(count -> ResponseEntity.status(HttpStatus.OK).body(count));
    }

    /**
     * {@code GET  /vat-rates/:id} : get the "id" vatRate.
     *
     * @param id the id of the vatRateDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the vatRateDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<VatRateDTO>> getVatRate(@PathVariable("id") UUID id) {
        log.debug("REST request to get VatRate : {}", id);
        Mono<VatRateDTO> vatRateDTO = vatRateService.findOne(id);
        return ResponseUtil.wrapOrNotFound(vatRateDTO);
    }

    /**
     * {@code DELETE  /vat-rates/:id} : delete the "id" vatRate.
     *
     * @param id the id of the vatRateDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteVatRate(@PathVariable("id") UUID id) {
        log.debug("REST request to delete VatRate : {}", id);
        return vatRateService
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
