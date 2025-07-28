package com.masi.sale.web.rest;

import com.carevn.masi.dto.ApiResponse;
import com.carevn.masi.utils.SecurityUtils;
import com.masi.sale.domain.ContractMaterialFull;
import com.masi.sale.domain.Order;
import com.masi.sale.domain.enumeration.OrderReviewStatus;
import com.masi.sale.domain.enumeration.OrderStatus;
import com.masi.sale.repository.OrderRepository;
import com.masi.sale.service.OrderReviewService;
import com.masi.sale.service.OrderService;
import com.masi.sale.service.dto.*;
import com.masi.sale.service.web.client.EmployeeClient;
import com.masi.sale.web.rest.errors.BadRequestAlertException;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

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
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * REST controller for managing {@link com.masi.sale.domain.Order}.
 */
@RestController
@RequestMapping("/api/orders")
public class OrderResource {

    private static final Logger log = LoggerFactory.getLogger(OrderResource.class);

    private static final String ENTITY_NAME = "masiSaleOrder";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OrderService orderService;

    private final OrderRepository orderRepository;

    private final OrderReviewService orderReviewService;

    private final EmployeeClient employeeClient;

    public OrderResource(OrderService orderService, OrderRepository orderRepository,
            OrderReviewService orderReviewService, EmployeeClient employeeClient) {
        this.orderService = orderService;
        this.orderRepository = orderRepository;
        this.orderReviewService = orderReviewService;
        this.employeeClient = employeeClient;
    }

    @GetMapping("/{id}/contract-material")
    public Mono<ResponseEntity<List<ContractMaterialDTO>>> getContractMaterial(@PathVariable("id") UUID id) {
        log.debug("REST request to get Contract Material of Order : {}", id);
        return orderService.getContractMaterialByOrderId(id)
                .collectList()
                .map(ResponseEntity::ok);
    }

    @GetMapping("{id}/export")
    public Mono<ResponseEntity<InputStreamResource>> getOrderDocx(@PathVariable("id") UUID id,
            @RequestParam(defaultValue = "false") boolean download) {
        log.debug("REST request to get contract of EmployeeProfile : {}", id);
        return orderService.exportOrder(id).flatMap(filePath -> {
            try {
                FileInputStream fileInputStream = new FileInputStream(filePath);
                InputStreamResource inputStreamResource = new InputStreamResource(fileInputStream);
                HttpHeaders headers = new HttpHeaders();
                String contentType = Files.probeContentType(Path.of(filePath));

                if (contentType == null) {
                    contentType = "application/octet-stream"; // Set a default content type if probeContentType
                    // fails
                }
                if (download) {
                    headers.add(HttpHeaders.CONTENT_DISPOSITION,
                            String.format("attachment; filename=\"%s\"", "order_requirement.pdf"));
                } else {
                    headers.add(HttpHeaders.CONTENT_TYPE, contentType);
                    headers.add("content-name", "order_requirement.pdf");
                }
                return Mono.just(new ResponseEntity<>(inputStreamResource, headers, HttpStatus.OK));

            } catch (Exception e) {
                return Mono.error(new RuntimeException(e));
            }
        }).switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)));

    }

    @Operation(summary = "Create a new order", description = "Return a new order with new Id")
    @PostMapping("")
    public Mono<ResponseEntity<OrderDTO>> createOrder(@Valid @RequestBody OrderDTO orderDTO) {
        log.debug("REST request to save Order : {}", orderDTO);
        orderDTO.setId(UUID.randomUUID());
        return SecurityUtils.getCompanyId().flatMap(
                companyId -> {
                    orderDTO.setCompany(companyId);
                    return Mono.just(orderDTO);
                }).then(orderService
                        .save(orderDTO)
                        .handle((result, sink) -> {
                            try {
                                sink.next(ResponseEntity.created(new URI("/api/orders/" + result.getId()))
                                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true,
                                                ENTITY_NAME, result.getId().toString()))
                                        .body(result));
                            } catch (URISyntaxException e) {
                                sink.error(new RuntimeException(e));
                            }
                        }));
    }

    @Operation(summary = "Update an existing order", description = "Update an existing order with new data")
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<OrderDTO>> partialUpdateOrder(
            @PathVariable(value = "id", required = false) final UUID id,
            @NotNull @RequestBody OrderDTO orderDTO) {
        log.debug("REST request to partial update Order partially : {}, {}", id, orderDTO);
        orderDTO.setId(id);

        return orderRepository
                .existsById(id)
                .flatMap(exists -> {
                    if (!exists) {
                        return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                    }

                    Mono<OrderDTO> result = orderService.partialUpdate(orderDTO);

                    return result
                            .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                            .map(
                                    res -> ResponseEntity.ok()
                                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true,
                                                    ENTITY_NAME, res.getId().toString()))
                                            .body(res));
                });
    }

    @GetMapping("join-with-manufacture-order")
    public Mono<ResponseEntity<ApiResponse<OrderDTO>>> getAllOrdersJoinWithManufactureOrder(
            @ParameterObject Pageable pageable,
            OrderQueryDTO dto) {
        log.debug("REST request to get a page of Orders query: {}", dto);
        return SecurityUtils.getCompanyId().flatMap(
                companyId -> {
                    dto.setCompany(companyId);
                    return Mono.just(dto);
                }).then(
                        orderService
                                .countAllByQuery(dto)
                                .zipWith(orderService.findAllByQueryJoinWithManufacture(dto, pageable))
                                .map(
                                        countWithEntities -> ResponseEntity.ok()
                                                .body(
                                                        new ApiResponse<>(
                                                                countWithEntities.getT2(),
                                                                countWithEntities.getT1()))));
    }

    @Operation(summary = "Get all the orders with pagination", description = "Get all the orders with pagination")
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<ApiResponse<OrderDTO>>> getAllOrders(
            @ParameterObject Pageable pageable,
            OrderQueryDTO dto) {
        log.debug("REST request to get a page of Orders query: {}", dto);
        return SecurityUtils.getCompanyId().flatMap(
                companyId -> {
                    dto.setCompany(companyId);
                    return Mono.just(dto);
                }).then(orderService
                        .countAllByQuery(dto)
                        .zipWith(orderService.findAllByQuery(dto, pageable).collectList().flatMap(dtoOrders -> {
                            // Get order IDs
                            List<UUID> requestIdsOrder = dtoOrders.stream()
                                    .map(OrderDTO::getId)
                                    .collect(Collectors.toList());
                            // Fetch order reviews for each order
                            return orderReviewService.findOrderReviewById(requestIdsOrder)
                                    .collectList()
                                    .flatMap(orderReviews -> {
                                        Map<UUID, List<OrderReviewDTO>> reviewMap = orderReviews.stream()
                                                .collect(Collectors.groupingBy(OrderReviewDTO::getOrderId));
                                        dtoOrders.forEach(order -> {
                                            List<OrderReviewDTO> reviews = reviewMap.getOrDefault(order.getId(),
                                                    new LinkedList<>());
                                            order.setOrderReviews(reviews);
                                        });

                                        Map<UUID, List<OrderReviewDTO>> reviewMapEmployee = orderReviews.stream()
                                                .collect(Collectors.groupingBy(OrderReviewDTO::getEmployeeId));

                                        // Fetch employee IDs from reviews that have not signed off
                                        List<UUID> employeeIds = new LinkedList<>(reviewMapEmployee.keySet());

                                        // Fetch employee details for those IDs
                                        return employeeClient.getEmployeesByListIds(employeeIds)
                                                .collectList()
                                                .map(employees -> {
                                                    // Map employee data and signature status to orders
                                                    employees.forEach(employee -> {
                                                        List<OrderReviewDTO> reviews = reviewMapEmployee
                                                                .get(employee.getId());
                                                        if (reviews != null) {
                                                            reviews.forEach(review -> {
                                                                review.setEmployee(employee);
                                                            });
                                                        }
                                                    });
                                                    return dtoOrders;
                                                });
                                    });
                        }))
                        .map(countWithEntities -> ResponseEntity.ok()
                                .body(new ApiResponse<>(
                                        countWithEntities.getT2(),
                                        countWithEntities.getT1()))));
    }

    @Operation(summary = "Get an order by id include list order reviews")
    @GetMapping("/{id}")
    public Mono<ResponseEntity<OrderDTO>> getOrder(@PathVariable("id") UUID id) {
        log.debug("REST request to get Order : {}", id);

        Mono<OrderDTO> orderDTO = orderService.findOne(id);

        return ResponseUtil.wrapOrNotFound(orderDTO);
    }

    @Operation(summary = "Soft delete an order by id")
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteOrder(@PathVariable("id") UUID id) {
        log.debug("REST request to delete Order : {}", id);
        return orderService
                .delete(id)
                .then(
                        Mono.just(
                                ResponseEntity.noContent()
                                        .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true,
                                                ENTITY_NAME, id.toString()))
                                        .build()));
    }

    @PostMapping("/list-id")
    public Mono<ResponseEntity<List<OrderDTO>>> findAllOrderById(@RequestBody List<UUID> ids) {
        log.debug("REST request to get Orders by ids: {}", ids);
        return orderService.findAllById(ids)
                .collectList()
                .map(ResponseEntity::ok);
    }

    @Operation(summary = "Cancel an order by id")
    @PatchMapping("/{id}/cancel")
    public Mono<ResponseEntity<OrderDTO>> cancelOrder(@PathVariable("id") UUID id) {
        log.debug("REST request to cancel Order : {}", id);
        return orderService
                .cancel(id)
                .map(
                        orderDTO -> ResponseEntity.ok()
                                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME,
                                        orderDTO.getId().toString()))
                                .body(orderDTO));
    }

    @Operation(summary = "update contract material order by id")
    @PatchMapping("update-contract-material/{idOrder}/{idManufacture}/")
    public Mono<ResponseEntity<Integer>> updateContractMaterial(
            @PathVariable(value = "idManufacture", required = false) final UUID idManufacture,
            @PathVariable(value = "idOrder", required = false) final UUID idOrder,
            @NotNull @RequestBody Collection<UUID> listIdContractMaterial) {
        return orderRepository
                .existsById(idOrder)
                .flatMap(exists -> {
                    if (!exists) {
                        return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                    }

                    Mono<Integer> result = orderService.updateContractMaterial(idOrder, idManufacture,
                            listIdContractMaterial);

                    return result
                            .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                            .map(
                                    res -> ResponseEntity.ok()
                                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true,
                                                    ENTITY_NAME, idOrder.toString()))
                                            .body(res > 0 ? 1 : 0));
                });

    }

}
