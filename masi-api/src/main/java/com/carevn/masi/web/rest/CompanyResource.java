package com.carevn.masi.web.rest;

import com.carevn.masi.dto.ApiResponse;
import com.carevn.masi.repository.CompanyRepository;
import com.carevn.masi.service.CompanyService;
import com.carevn.masi.service.dto.CompanyDTO;
import com.carevn.masi.service.dto.CompanyQuery;
import com.carevn.masi.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.ForwardedHeaderUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link com.carevn.masi.domain.Company}.
 */
@RestController
@RequestMapping("/api/companies")
public class CompanyResource {

    private static final Logger log = LoggerFactory.getLogger(CompanyResource.class);

    private static final String ENTITY_NAME = "company";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CompanyService companyService;

    private final CompanyRepository companyRepository;

    public CompanyResource(CompanyService companyService, CompanyRepository companyRepository) {
        this.companyService = companyService;
        this.companyRepository = companyRepository;
    }


    @PostMapping("/{companyId}/users")
    public Mono<ResponseEntity<Void>> addUserToCompany(@PathVariable("companyId") String companyId, @RequestBody UUID userId) {
        return companyRepository.addUserToCompany(companyId, userId)
            .then(Mono.just(ResponseEntity.noContent().build()));
    }

    @GetMapping("/{identifier}/director")
    @Transactional(readOnly = true)
    public Mono<ResponseEntity<Map<String,Object>>> getDepartmentManager(@PathVariable("identifier") String groupIdentifier) {
        return  companyRepository.findCompanyDirector(groupIdentifier)
            .map(user -> {
                Map<String,Object> map = new HashMap<>();
                map.put("id", user.getId());
                map.put("fullName", user.getLastName() + " " + user.getFirstName());
                return ResponseEntity.ok().body(map);
            })
            .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

    /**
     * {@code POST  /companies} : Create a new company.
     *
     * @param companyDTO the companyDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new companyDTO, or with status {@code 400 (Bad Request)} if the company has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<CompanyDTO>> createCompany(@Valid @RequestBody CompanyDTO companyDTO) throws URISyntaxException {
        log.debug("REST request to save Company : {}", companyDTO);
        if (companyDTO.getId() != null) {
            throw new BadRequestAlertException("A new company cannot already have an ID", ENTITY_NAME, "idexists");
        }
        companyDTO.setId(UUID.randomUUID());
        return companyService
            .save(companyDTO)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/companies/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /companies/:id} : Updates an existing company.
     *
     * @param id the id of the companyDTO to save.
     * @param companyDTO the companyDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated companyDTO,
     * or with status {@code 400 (Bad Request)} if the companyDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the companyDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<CompanyDTO>> updateCompany(
        @PathVariable(value = "id", required = false) final UUID id,
        @Valid @RequestBody CompanyDTO companyDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Company : {}, {}", id, companyDTO);
        if (companyDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, companyDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return companyRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return companyService
                    .update(companyDTO)
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
     * {@code PATCH  /companies/:id} : Partial updates given fields of an existing company, field will ignore if it is null
     *
     * @param id the id of the companyDTO to save.
     * @param companyDTO the companyDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated companyDTO,
     * or with status {@code 400 (Bad Request)} if the companyDTO is not valid,
     * or with status {@code 404 (Not Found)} if the companyDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the companyDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<CompanyDTO>> partialUpdateCompany(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody CompanyDTO companyDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Company partially : {}, {}", id, companyDTO);
        companyDTO.setId(id);
        return companyRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<CompanyDTO> result = companyService.partialUpdate(companyDTO);

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
     * {@code GET  /companies} : get all the companies.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of companies in body.
     */
//    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<ApiResponse<CompanyDTO>>> getAllCompanies(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request,
        @RequestParam(required = false) String search,
        @RequestParam(required = false) String parentId
    ) {
        log.debug("REST request to get a page of Companies");
        return companyService
            .countAll()
            .zipWith(companyService.findAll(pageable).collectList())
            .map(
                countWithEntities ->
                    ResponseEntity.ok()
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

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<ApiResponse<CompanyDTO>>> getAllCompanies(Pageable pageable,
                                                                         ServerHttpRequest request,
                                                                         @ParameterObject CompanyQuery query) {
        return companyService.findAllByQuery(pageable, query)
            .collectList()
            .zipWith(companyService.countByQuery(query))
            .map(
                countWithEntities ->
                    ResponseEntity.ok()
                        .headers(
                            PaginationUtil.generatePaginationHttpHeaders(
                                ForwardedHeaderUtils
                                    .adaptFromForwardedHeaders(
                                        request.getURI(),
                                        request.getHeaders()),
                                new PageImpl<>(countWithEntities.getT1(), pageable,
                                    countWithEntities.getT2())))
                        .body(new ApiResponse<>(countWithEntities.getT1(), countWithEntities.getT2()))
            );
    }

    /**
     * {@code GET  /companies/:id} : get the "id" company.
     *
     * @param id the id of the companyDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the companyDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<CompanyDTO>> getCompany(@PathVariable("id") UUID id) {
        log.debug("REST request to get Company : {}", id);
        Mono<CompanyDTO> companyDTO = companyService.findOne(id);
        return ResponseUtil.wrapOrNotFound(companyDTO);
    }

    /**
     * {@code DELETE  /companies/:id} : delete the "id" company.
     *
     * @param id the id of the companyDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteCompany(@PathVariable("id") UUID id) {
        log.debug("REST request to delete Company : {}", id);
        return companyService
            .delete(id)
            .then(
                Mono.just(
                    ResponseEntity.noContent()
                        .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                        .build()
                )
            );
    }

    @PatchMapping("/{id}/activate")
    public Mono<ResponseEntity<CompanyDTO>> activateCompany(@PathVariable("id") UUID id) {
        log.debug("REST request để kích hoạt Company : {}", id);
        return companyService
            .findOne(id)
            .flatMap(existingCompany -> companyService
                .activate(id)
                .map(result -> ResponseEntity.ok()
                    .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                    .body(result)))
            .switchIfEmpty(Mono.error(new BadRequestAlertException("Không tìm thấy công ty", ENTITY_NAME, "idnotfound")));
    }

    @PatchMapping("/{id}/deactivate")
    public Mono<ResponseEntity<CompanyDTO>> deactivateCompany(@PathVariable("id") UUID id) {
        log.debug("REST request để vô hiệu hóa Company : {}", id);
        return companyService
            .findOne(id)
            .flatMap(existingCompany -> companyService
                .deactivate(id)
                .map(result -> ResponseEntity.ok()
                    .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                    .body(result)))
            .switchIfEmpty(Mono.error(new BadRequestAlertException("Không tìm thấy công ty", ENTITY_NAME, "idnotfound")));
    }

}
