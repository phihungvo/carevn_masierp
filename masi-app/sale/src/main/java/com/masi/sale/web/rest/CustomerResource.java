package com.masi.sale.web.rest;

import static com.masi.sale.domain.enumeration.CustomerStatus.DISABLED;
import static com.masi.sale.domain.enumeration.CustomerStatus.ENABLED;

import com.carevn.masi.dto.ApiResponse;
import com.carevn.masi.utils.SecurityUtils;
import com.carevn.masi.utils.Utilities;
import com.masi.sale.domain.Customer;
import com.masi.sale.repository.CustomerRepository;
import com.masi.sale.service.CustomerService;
import com.masi.sale.service.dto.CustomerDTO;
import com.masi.sale.service.dto.CustomerRO;
import com.masi.sale.web.rest.errors.BadRequestAlertException;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link com.masi.sale.domain.Customer}.
 */
@RestController
@RequestMapping("/api/customers")
public class CustomerResource {

    private final Logger log = LoggerFactory.getLogger(CustomerResource.class);

    private static final String ENTITY_NAME = "saleCustomer";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CustomerService customerService;

    private final CustomerRepository customerRepository;

    public CustomerResource(CustomerService customerService, CustomerRepository customerRepository) {
        this.customerService = customerService;
        this.customerRepository = customerRepository;
    }

    /**
     * {@code POST  /customers} : Create a new customer.
     *
     * @param customerDTO the customerDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with
     * body the new customerDTO, or with status {@code 400 (Bad Request)} if
     * the customer has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */

    @PostMapping("")
    public Mono<ResponseEntity<Map>> createCustomer(@Valid @RequestBody CustomerDTO customerDTO) throws URISyntaxException {
        log.debug("REST request to save Customer : {}", customerDTO);
        if (customerDTO.getId() != null) {
            throw new BadRequestAlertException("A new customer cannot already have an ID", ENTITY_NAME, "idexists");
        }
        customerDTO.setId(UUID.randomUUID());

        return customerRepository
            .countByIsDeletedAndCustomerCode(customerDTO.getCustomerCode())
            .flatMap(count -> {
                if (count > 0) {
                    return Mono.error(new BadRequestAlertException("Customer Code exists", customerDTO.getCustomerCode(), "codeexists"));
                }
                return customerService
                    .save(customerDTO)
                    .handle((result, sink) -> {
                        try {
                            sink.next(
                                ResponseEntity.created(new URI("/api/customers/" + result.getId()))
                                    .headers(
                                        HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString())
                                    )
                                    .body(Utilities.generateResponse("success", result))
                            );
                        } catch (URISyntaxException e) {
                            sink.error(new RuntimeException(e));
                        }
                    });
            });
    }

    /**
     * {@code PATCH  /customers/:id} : Partial updates given fields of an existing
     * customer, field will ignore if it is null
     *
     * @param id          the id of the customerDTO to save.
     * @param customerDTO the customerDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     * the updated customerDTO,
     * or with status {@code 400 (Bad Request)} if the customerDTO is not
     * valid,
     * or with status {@code 404 (Not Found)} if the customerDTO is not
     * found,
     * or with status {@code 500 (Internal Server Error)} if the customerDTO
     * couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = {"application/json", "application/merge-patch+json"})
    public Mono<ResponseEntity<Map>> partialUpdateCustomer(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody CustomerDTO customerDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Customer partially : {}, {}", id, customerDTO);
        customerDTO.setId(id);

        return customerRepository
            .findById(id)
            .flatMap(exists -> {
                Mono<CustomerDTO> result = customerService.partialUpdate(customerDTO);
                return result
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(
                        res ->
                            ResponseEntity.ok()
                                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, res.getId().toString()))
                                .body(Utilities.generateResponse("success", res))
                    );
            });
    }

    /**
     * {@code GET  /customers} : get all the customers.
     *
     * @param pageable the pagination information.
     * @param request  a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
     * of customers in body.
     */
    @GetMapping(value = "/enabled", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<ApiResponse<CustomerDTO>>> getAllCustomersENABLED(
        @ParameterObject CustomerRO ro,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        ro.setCustomerStatus(ENABLED);
        log.debug("REST request to get a page of Customers");
        return SecurityUtils.getUserJWTDetail()
            .flatMap(user -> {
                ro.setCompany(String.valueOf(user.getCompanyId()));
                ro.setDepartment(user.getGroupId());
                return customerService
                    .countAllByQuery(ro)
                    .zipWith(customerService.findAllByQuery(pageable, ro).collectList())
                    .map(
                        countWithEntities ->
                            ResponseEntity.ok().body(new ApiResponse<>(countWithEntities.getT2(), countWithEntities.getT1()))
                    );
            });
    }

    @GetMapping(value = "/disabled", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<ApiResponse<CustomerDTO>>> getAllCustomersDISABLED(
        @ParameterObject CustomerRO ro,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        ro.setCustomerStatus(DISABLED);
        log.debug("REST request to get a page of Customers");
        return SecurityUtils.getUserJWTDetail()
            .flatMap(user -> {
                ro.setCompany(String.valueOf(user.getCompanyId()));
                ro.setDepartment(user.getGroupId().toString());
                return customerService
                    .countAllByQuery(ro)
                    .zipWith(customerService.findAllByQuery(pageable, ro).collectList())
                    .map(
                        countWithEntities ->
                            ResponseEntity.ok().body(new ApiResponse<>(countWithEntities.getT2(), countWithEntities.getT1()))
                    );
            });
    }

    /**
     * {@code GET  /customers/:id} : get the "id" customer.
     *
     * @param id the id of the customerDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     * the customerDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<CustomerDTO>> getCustomer(@PathVariable("id") UUID id) {
        log.debug("REST request to get Customer : {}", id);
        Mono<CustomerDTO> customerDTO = customerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(customerDTO);
    }

    @PatchMapping("/customer-birthday")
    public Mono<ResponseEntity<Void>> getCustomer() {
        log.debug("REST request to send customer-birthday at localtime: {}", ZonedDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));
        var send = customerService.sendBirthday();
        return send.then(Mono.just(ResponseEntity.noContent().build()));
    }

    /**
     * {@code DELETE  /customers/:id} : delete the "id" customer.
     *
     * @param id the id of the customerDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Map<String, Object>>> deleteCustomer(@PathVariable("id") UUID id) {
        log.debug("REST request to delete Customer : {}", id);

        return customerService
            .delete(id)
            .then(
                Mono.just(
                    ResponseEntity.noContent()
                        .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                        .build()
                )
            )
            .map(responseEntity -> {
                Map<String, Object> responseBody = new HashMap<>();
                responseBody.put("message", "Customer deleted successfully");
                return ResponseEntity.ok().body(responseBody);
            })
            .onErrorResume(IllegalStateException.class, ex -> {
                Map<String, Object> responseBody = new HashMap<>();
                responseBody.put("error", "Customer is enabled and cannot be deleted");
                responseBody.put("message", ex.getMessage());
                return Mono.just(ResponseEntity.badRequest().body(responseBody));
            });
    }

    @DeleteMapping("{id}/disable")
    public Mono<ResponseEntity<Map>> disabledCustomer(@PathVariable("id") UUID id) {
        log.debug("REST request to disabled Customer : {}", id);
        return customerService
            .disabled(id)
            .then(
                Mono.just(
                    ResponseEntity.ok()
                        .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, id.toString()))
                        .body(Collections.singletonMap("message", "Customer successfully disabled"))
                )
            );
    }

    @PatchMapping("{id}/activate")
    public Mono<ResponseEntity<Map>> activatedCustomer(@PathVariable("id") UUID id) {
        log.debug("REST request to activated Customer : {}", id);
        return customerService
            .activated(id)
            .then(
                Mono.just(
                    ResponseEntity.ok()
                        .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, id.toString()))
                        .body(Collections.singletonMap("message", "Customer successfully activated"))
                )
            );
    }

    @PatchMapping(value = "{id}/transfer/{newOwnerId}")
    public Mono<ResponseEntity<Map>> changeCustomer(
        @PathVariable(value = "id", required = false) final UUID id,
        @PathVariable(value = "newOwnerId", required = false) final UUID CustomerOwnerId
    ) throws URISyntaxException {
        log.debug("REST request to change Customer owner : {}", id);
        return customerService
            .change(id, CustomerOwnerId)
            .then(
                Mono.just(
                    ResponseEntity.ok()
                        .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, id.toString()))
                        .body(Collections.singletonMap("message", "Customer owner successfully changed"))
                )
            );
    }

    @Operation(summary = "Get Customer by code")
    @GetMapping("/customer-code/{id}")
    public Mono<ResponseEntity<CustomerDTO>> getCustomerId(@PathVariable("id") String id) {
        log.debug("REST request to check if Customer exists with id : {}", id);
        return customerService.findByCustomerId(id).map(ResponseEntity::ok).defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Lấy theo mã số thuế, không có trả 404")
    @GetMapping("/tax-code/{taxCode}")
    public Mono<ResponseEntity<? extends Object>> getCustomerByTaxCode(
        @RequestParam(value = "ownerCode", required = false) UUID ownerCode,
        @PathVariable("taxCode") String taxCode
    ) {
        log.debug("REST request to get Customer by tax code : {}", taxCode);
        return customerService
            .findByTaxCode(taxCode)
            .map(cus -> {
                if (cus.getId().equals(ownerCode)) {
                    return ResponseEntity.notFound().build();
                }
                return ResponseEntity.ok(cus);
            })
            .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Xuất file csv")
    @GetMapping("/export/enabled")
    public Mono<ResponseEntity<InputStreamResource>> exportCustomerEnabled(
        @ParameterObject CustomerRO ro,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        ro.setCustomerStatus(ENABLED);
        log.debug("REST request to export Customers");
        return SecurityUtils.getUserJWTDetail().flatMap(user -> {
            ro.setCompany(String.valueOf(user.getCompanyId()));
            return Mono.just(ro);
        }).then(customerService
            .export(ro, pageable)
            .flatMap(csv -> {
                try {
                    FileInputStream fileInputStream = new FileInputStream(csv);
                    InputStreamResource inputStreamResource = new InputStreamResource(fileInputStream);
                    HttpHeaders headers = new HttpHeaders();
                    headers.add(HttpHeaders.CONTENT_DISPOSITION, String.format("attachment; filename=\"%s\"", "Danh sach KH.xlsx"));
                    return Mono.just(new ResponseEntity<>(inputStreamResource, headers, HttpStatus.OK));
                } catch (FileNotFoundException e) {
                    return Mono.error(new RuntimeException(e));
                }
            }));
    }

    @Operation(summary = "Xuất file csv")
    @GetMapping("/export/disabled")
    public Mono<ResponseEntity<byte[]>> exportCustomerDisabled(
        @ParameterObject CustomerRO ro,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        ro.setCustomerStatus(DISABLED);
        return SecurityUtils.getUserJWTDetail().flatMap(user -> {
            ro.setCompany(String.valueOf(user.getCompanyId()));
            return Mono.just(ro);
        }).then(customerService
            .export(ro, pageable)
            .flatMap(csv -> {
                try {
                    FileInputStream fileInputStream = new FileInputStream(csv);
                    byte[] bytes = fileInputStream.readAllBytes();
                    HttpHeaders headers = new HttpHeaders();
                    headers.add(HttpHeaders.CONTENT_TYPE, "application/octet-stream");
                    headers.add(HttpHeaders.CONTENT_DISPOSITION, String.format("attachment; filename=%s", "Danh sach KH.xlsx"));
                    return Mono.just(new ResponseEntity<>(bytes, headers, HttpStatus.OK));
                } catch (Exception e) {
                    return Mono.error(new RuntimeException(e));
                }
            }));
    }

    @Operation(summary = "Mã khách hàng tiếp theo")
    @GetMapping("/nextCustomerCode")
    public Mono<ResponseEntity<Map<String, String>>> getNextCustomerCode() {
        return SecurityUtils.getUserJWTDetail()
            .flatMap(user -> {
                return customerService
                    .getNextCustomerCode(String.valueOf(user.getCompanyId()))
                    .map(result -> ResponseEntity.ok(Map.of("nextCustomerCode", result)))
                    .defaultIfEmpty(
                        ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                            Map.of("message", "No customer code templates found for company: " + String.valueOf(user.getCompanyId()))
                        )
                    );
            });
    }

    @GetMapping("/birthday")
    public Mono<ResponseEntity<ApiResponse<CustomerDTO>>> getEmployeeBirthdayInMonth(
        @RequestParam(value = "fromDate", required = false, defaultValue = "2020-01-01") LocalDate fromDate,
        @RequestParam(value = "toDate", required = false, defaultValue = "2050-12-31") LocalDate toDate) {
        return SecurityUtils.getUserJWTDetail()
            .flatMap(user -> {
                return customerService
                    .countAllBirthday(fromDate, toDate, user.getUserId())
                    .zipWith(customerService.findAllBirthday(fromDate, toDate, user.getUserId()).collectList())
                    .map(
                        countWithEntities ->
                            ResponseEntity.ok().body(new ApiResponse<>(countWithEntities.getT2(), countWithEntities.getT1()))
                    );
            });
    }
}
