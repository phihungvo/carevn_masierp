package com.masi.employee.web.rest;

import com.carevn.masi.dto.ApiResponse;
import com.carevn.masi.utils.SecurityUtils;
import com.carevn.masi.utils.Utilities;
import com.masi.employee.domain.UniformOrder;
import com.masi.employee.domain.enumeration.UniformOrderStatus;
import com.masi.employee.repository.UniformOrderRepository;
import com.masi.employee.service.*;
import com.masi.employee.service.dto.*;
import com.masi.employee.service.reports.ReportService;
import com.masi.employee.web.rest.errors.BadRequestAlertException;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
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

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuples;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

import static java.util.List.*;

/**
 * REST controller for managing {@link UniformOrder}.
 */
@RestController
@RequestMapping("/api/uniform-orders")
public class UniformOrderResource {

    private static final Logger log = LoggerFactory.getLogger(UniformOrderResource.class);

    private static final String ENTITY_NAME = "masiEmployeeUniformOrder";
    private final ReportService reportService;
    private final EmployeeProfileService employeeProfileService;

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UniformOrderService uniformOrderService;

    private final UniformFormDetailService uniformFormDetailService;

    private final UniformOrderStockService uniformOrderStockService;

    private final UniformOrderRepository uniformOrderRepository;

    private final UniformOrderProcessService uniformOrderProcessService;

    private final LogisticClient logisticClient;

    public UniformOrderResource(UniformOrderService uniformOrderService,
                                UniformOrderRepository uniformOrderRepository,
                                UniformFormDetailService uniformFormDetailService,
                                UniformOrderProcessService uniformOrderProcessService, ReportService reportService, UniformOrderStockService uniformOrderStockService, LogisticClient logisticClient, EmployeeProfileService employeeProfileService) {
        this.uniformOrderService = uniformOrderService;
        this.uniformFormDetailService = uniformFormDetailService;
        this.uniformOrderRepository = uniformOrderRepository;
        this.uniformOrderProcessService = uniformOrderProcessService;
        this.reportService = reportService;
        this.uniformOrderStockService = uniformOrderStockService;
        this.logisticClient = logisticClient;
        this.employeeProfileService = employeeProfileService;
    }

    @PostMapping("")
    public Mono<ResponseEntity<Map<String, Object>>> createUniformOrder(
            @RequestBody UniformOrderCreateDTO uniformOrderCreateDTO) {
        log.debug("REST request to save UniformOrder : {}", uniformOrderCreateDTO);

        return uniformOrderService
                .save(uniformOrderCreateDTO.toDto())
                .flatMap(result -> {
                        return uniformOrderProcessService
                                .saveAll(uniformOrderCreateDTO.toProcessDto(result))
                                .collectList()
                                .flatMap(process -> {

                                    return uniformFormDetailService.saveAll(uniformOrderCreateDTO.toDetailDto(result))
                                            .collectList()
                                            .map(details -> Tuples.of(result, details, process));
                                });

                })

                .map(tuple -> {
                    return ResponseEntity
                            .ok()
                            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true,
                                    ENTITY_NAME, tuple.getT1().getId().toString()))
                            .body(Map.of("uniformOrder", tuple.getT1(), "details", tuple.getT2(), "process",
                                    !Objects.equals(tuple.getT3().toString(), "false") ? tuple.getT3()
                                            : new ArrayList<>()));
                });
    }

    @Operation(hidden = true)
    @PutMapping("/{id}")
    public Mono<ResponseEntity<UniformOrderDTO>> updateUniformOrder(
            @PathVariable(value = "id", required = false) final UUID id,
            @Valid @RequestBody UniformOrderDTO uniformOrderDTO) throws URISyntaxException {
        log.debug("REST request to update UniformOrder : {}, {}", id,
                uniformOrderDTO);
        uniformOrderDTO.setId(id);

        return uniformOrderRepository
                .existsById(id)
                .flatMap(exists -> {
                    if (!exists) {
                        return Mono.error(new BadRequestAlertException("Entity not found",
                                ENTITY_NAME, "idnotfound"));
                    }

                    return uniformOrderService
                            .update(uniformOrderDTO)
                            .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                            .map(
                                    result -> ResponseEntity.ok()
                                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true,
                                                    ENTITY_NAME, result.getId().toString()))
                                            .body(result));
                });
    }

    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<Map<String, Object>>> partialUpdateUniformOrder(
            @PathVariable(value = "id", required = false) final UUID id,
            @NotNull @RequestBody UniformOrderUpdateDTO uniformOrderUpdateDTO) throws URISyntaxException {
        log.debug("REST request to partial update UniformOrder partially : {}, {}", id, uniformOrderUpdateDTO);

        var uniformOrderDTO = uniformOrderUpdateDTO.toDto(id);
        return SecurityUtils.getUserJWTDetail().flatMap(user -> {
            uniformOrderDTO.setUpdateAt(ZonedDateTime.now());
            uniformOrderDTO.setUpdateBy(user.getUserId().toString());
            return uniformOrderService
                    .handlePartialUpdate(uniformOrderDTO, uniformOrderUpdateDTO.toDetailDto(uniformOrderDTO)).map(
                            result -> ResponseEntity.ok()
                                    .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME,
                                            result.getId().toString()))
                                    .body(Utilities.generateResponse("SUCCESS", result)));
        });
    }

    // gen java doc:

    /**
     * Handles a partial update of a {@link UniformOrder} process.
     *
     * @param id                           The unique identifier of the
     *                                     {@link UniformOrder} to be updated. Can
     *                                     be {@code null} if not specified.
     * @param uniformOrderHandleProcessDTO The data transfer object containing the
     *                                     updates to be applied to the
     *                                     {@link UniformOrder}.
     * @return A {@link Mono} containing the {@link ResponseEntity} with the status
     *         of the update operation.
     * @throws URISyntaxException If the URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}/process", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<Map<String, Object>>> processUniformOrder(
            @PathVariable(value = "id", required = false) final UUID id,
            @NotNull @RequestBody UniformOrderHandleProcessDTO uniformOrderHandleProcessDTO) throws URISyntaxException {

        log.debug("REST request to partially update UniformOrder process: {}, {}", id, uniformOrderHandleProcessDTO);

        return SecurityUtils.getUserJWTDetail()
                .flatMap(user -> {
                    // Set the update details
                    uniformOrderHandleProcessDTO.setUpdateAt(ZonedDateTime.now());
                    uniformOrderHandleProcessDTO.setUpdateBy(user.getUserId().toString());
                    uniformOrderHandleProcessDTO.setApproverId(user.getUserId());
                    return uniformOrderService.findOne(id)
                            .flatMap(uniformOrder -> {
                                // Set the existing order data to the DTO
                                uniformOrderHandleProcessDTO.setUniformOrderDTO(uniformOrder);

                                return uniformOrderService.handleProcessUniformOrder(uniformOrderHandleProcessDTO)
                                        .map(result -> ResponseEntity.ok()
                                                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true,
                                                        ENTITY_NAME, result.getId().toString()))
                                                .body(Utilities.generateResponse("SUCCESS", result)));
                            });
                });
    }

    @PatchMapping(value = "/{id}/cancel")
    public Mono<ResponseEntity<Map<String, Object>>> cancelUniformOrder(
            @PathVariable(value = "id", required = false) final UUID id) throws URISyntaxException {

        log.debug("REST request to cancelUniformOrder: {},", id);

        return SecurityUtils.getUserJWTDetail()
                .flatMap(user -> {
                    // Set the update details
                    return uniformOrderService.findOne(id)
                            .flatMap(uniformOrder -> {
                                if (uniformOrder.getStatus().equals(UniformOrderStatus.APPROVED)) {
                                    return Mono.error(new BadRequestAlertException("uniform order approved",
                                            "UNIFORM_ORDER", "UNIFORM_ORDER_APPROVED"));
                                }
                                uniformOrder.setStatus(UniformOrderStatus.CANCELLED);
                                // Set the existing order data to the DTO
                                uniformOrder.setUpdateAt(ZonedDateTime.now());
                                uniformOrder.setUpdateBy(user.getUserId().toString());
                                return uniformOrderService.partialUpdate(uniformOrder)
                                        .map(result -> ResponseEntity.ok()
                                                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true,
                                                        ENTITY_NAME, result.getId().toString()))
                                                .body(Utilities.generateResponse("SUCCESS", result)));
                            });
                });
    }

    @PatchMapping(value = "/{id}/stock")
    @Operation(summary = "Nhập kho", description = "Nhập kho")
    public Mono<ResponseEntity<Map<String, Object>>> stockUniformOrder(
        @PathVariable(value = "id", required = false) final UUID id,
        @RequestBody UniformOrderStockDTO uniformOrderStockDTO) throws URISyntaxException {

        log.debug("REST request to stockUniformOrder: {}", id);

        return SecurityUtils.getUserJWTDetail()
                .flatMap(user -> {
                    uniformOrderStockDTO.setUniformOrderId(id);
                    uniformOrderStockDTO.setCompany(user.getCompanyId());
                    uniformOrderStockDTO.setCreateBy(user.getUserId().toString());
                    // Set the update details
                    return uniformOrderService.findOne(id)
                            .flatMap(uniformOrder -> {
                                return uniformOrderService.createUniformOrderStockV2(uniformOrderStockDTO)
                                .map(result -> ResponseEntity.ok()
                                        .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true,
                                                ENTITY_NAME, result.getId().toString()))
                                        .body(Utilities.generateResponse("SUCCESS", result)));
                            });
                });
    }

    /**
     * {@code GET  /uniform-orders} : get all the uniformOrders.
     *
     * @param pageable the pagination information.
     * @param request  a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
     *         of uniformOrders in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<ApiResponse<UniformOrderDTO>>> getAllUniformOrders(
            @ParameterObject Pageable pageable,
            @ParameterObject UniformOrderGetListDTO uniformOrderGetListDTO,
            ServerHttpRequest request) {
        log.debug("REST request to get a page of UniformOrders");
        return SecurityUtils.getUserJWTDetail().flatMap(user -> {
            if (user.getCompanyId() == null) {
                return Mono.error(new ResponseStatusException(HttpStatus.FORBIDDEN));
            }
            uniformOrderGetListDTO.setCompany(user.getCompanyId());
            return uniformOrderService
                    .countAllByQuery(
                            uniformOrderGetListDTO)
                    .zipWith(uniformOrderService.findAllByQuery(pageable, uniformOrderGetListDTO)
                            .collectList())
                    .map(countWithEntities ->
                        ResponseEntity.ok()
                            .headers(
                                PaginationUtil.generatePaginationHttpHeaders(
                                    ForwardedHeaderUtils
                                        .adaptFromForwardedHeaders(
                                            request.getURI(),
                                            request.getHeaders()),
                                    new PageImpl<>(countWithEntities.getT2(), pageable,
                                        countWithEntities.getT1())))
                            .body(new ApiResponse<>(countWithEntities.getT2(), countWithEntities.getT1())));

        });
    }

    /**
     *
     *
     * @param download the id of the uniformOrderDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the uniformOrderDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/export-order-xlsx")
    public Mono<ResponseEntity<InputStreamResource>> exportUniformOrder(
       @RequestParam(value = "download", defaultValue = "true") boolean download,
       @RequestParam(value = "startDate",required = false) LocalDate startDate,
       @RequestParam(value = "endDate",required = false) LocalDate endDate) {
        var uniformOrderGetlistDTO = new UniformOrderGetListDTO();
        ZonedDateTime zdtStart = null;
        ZonedDateTime zdtEnd = null;
        if (startDate != null) {
            zdtStart = startDate.atStartOfDay(ZoneOffset.UTC);
        }
        if (endDate != null) {
            zdtEnd = endDate.atStartOfDay(ZoneOffset.UTC);
        }
        uniformOrderGetlistDTO.setStartDate(zdtStart);
        uniformOrderGetlistDTO.setEndDate(zdtEnd);
        return SecurityUtils.getUserJWTDetail().flatMap(user -> {
            uniformOrderGetlistDTO.setCompany(user.getCompanyId());
//            uniformOrderGetlistDTO.setStatus(new ArrayList<>(of(UniformOrderStatus.STOCKED)));
            return uniformOrderService.findAllByQuery(null, uniformOrderGetlistDTO)
                .flatMap(uniformOrder -> {
                    return logisticClient.getSuppliers(0, Integer.MAX_VALUE - 1).collectList().flatMap(res -> {

                        if (!res.isEmpty()) {
                            var mapSupplier = res.stream().filter(s -> s.getId() != null && s.getId().equals(uniformOrder.getSupplierId())).findFirst().orElse(null);
                            if (mapSupplier != null) {
                                uniformOrder.setSupplierName(mapSupplier.getName());
                            }
                        }
                        return Mono.just(uniformOrder);
                    }).then(Mono.just(uniformOrder));
                })
                .collectList().flatMap(uniformOrders -> {
                    if (uniformOrders.isEmpty()) {
                        return Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND));
                    }
                    var listEmployee = uniformOrders.stream().map(UniformOrderDTO::getCreateBy).toList();
                    return employeeProfileService.findAllByListIdString(listEmployee, user.getCompanyId()).collectList().flatMap(emp -> {
                        emp.forEach(e -> {
                            uniformOrders.forEach(u -> {
                                if (u.getCreateBy().equals(e.getId().toString())) {
                                    u.setCreateByDTO(e);
                                }
                            });
                        });
                        return reportService.exportOrdersToExcel(uniformOrders)
                            .handle((file, sink) -> {
                                try {
                                    FileInputStream fileInputStream = new FileInputStream(file);
                                    InputStreamResource inputStreamResource = new InputStreamResource(fileInputStream);
                                    HttpHeaders headers = new HttpHeaders();
                                    String contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

                                    if (download) {
                                        headers.add(HttpHeaders.CONTENT_DISPOSITION,
                                            String.format("attachment; filename=\"%s\"", "uniform_order" + ".xlsx"));
                                    } else {
                                        headers.add(HttpHeaders.CONTENT_TYPE, contentType);
                                        headers.add("content-name", "uniform_order" + ".xlsx");
                                    }
                                    sink.next(new ResponseEntity<>(inputStreamResource, headers, HttpStatus.OK));
                                } catch (IOException e) {
                                    sink.error(e);
                                }
                            });
                    });
                });
        });
    }

    /**
     *
     *
     * @param download the id of the uniformOrderDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the uniformOrderDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/export-order-stock-xlsx")
    public Mono<ResponseEntity<InputStreamResource>> exportUniformOrderStock(
        @RequestParam(value = "download", defaultValue = "true") boolean download,
        @RequestParam(value = "startDate",required = false) LocalDate startDate,
        @RequestParam(value = "endDate",required = false) LocalDate endDate) {
        ZonedDateTime zdtStart;
        ZonedDateTime zdtEnd;
        if (startDate != null) {
            zdtStart = startDate.atStartOfDay(ZoneOffset.UTC);
        } else {
            zdtStart = null;
        }
        if (endDate != null) {
            zdtEnd = endDate.atStartOfDay(ZoneOffset.UTC);
        } else {
            zdtEnd = null;
        }

        return SecurityUtils.getUserJWTDetail().flatMap(user -> {
            return uniformOrderStockService.findAllBy(null, zdtStart, zdtEnd, user.getCompanyId())
                .collectList().flatMap(uniformOrderStockDTOS -> {
                    if (uniformOrderStockDTOS.isEmpty()) {
                        return Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND));
                    }
                    return reportService.exportUniformOrderStock(uniformOrderStockDTOS)
                        .handle((file, sink) -> {
                            try {
                                FileInputStream fileInputStream = new FileInputStream(file);
                                InputStreamResource inputStreamResource = new InputStreamResource(fileInputStream);
                                HttpHeaders headers = new HttpHeaders();
                                String contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

                                if (download) {
                                    headers.add(HttpHeaders.CONTENT_DISPOSITION,
                                        String.format("attachment; filename=\"%s\"", "uniform_order_stock" + ".xlsx"));
                                } else {
                                    headers.add(HttpHeaders.CONTENT_TYPE, contentType);
                                    headers.add("content-name", "uniform_order_stock" + ".xlsx");
                                }
                                sink.next(new ResponseEntity<>(inputStreamResource, headers, HttpStatus.OK));
                            } catch (IOException e) {
                                sink.error(e);
                            }
                        });
                });
        });
    }


    /**
     * {@code GET  /uniform-orders/:id} : get the "id" uniformOrder.
     *
     * @param id the id of the uniformOrderDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the uniformOrderDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<UniformOrderDTO>> getUniformOrder(@PathVariable("id") UUID id) {
        log.debug("REST request to get UniformOrder : {}", id);
        Mono<UniformOrderDTO> uniformOrderDTO = uniformOrderService.findOne(id);
        return ResponseUtil.wrapOrNotFound(uniformOrderDTO);
    }

    /**
     * {@code DELETE  /uniform-orders/:id} : delete the "id" uniformOrder.
     *
     * @param id the id of the uniformOrderDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteUniformOrder(@PathVariable("id") UUID id) {
        log.debug("REST request to delete UniformOrder : {}", id);
        return SecurityUtils.getUserJWTDetail()
                .flatMap(user -> {
                    return uniformOrderService.deleteByUniformOrder(id, user.getUserId().toString())
                            .then(Mono.fromCallable(() -> ResponseEntity.noContent()
                                    .headers(HeaderUtil.createEntityDeletionAlert(applicationName,
                                            true, ENTITY_NAME, id.toString()))
                                    .build()));
                });
    }

}
