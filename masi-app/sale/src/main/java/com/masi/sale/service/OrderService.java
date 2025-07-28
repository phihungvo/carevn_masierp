package com.masi.sale.service;

import com.carevn.masi.utils.DocxUtils;
import com.carevn.masi.utils.FileManager;
import com.deepoove.poi.data.RowRenderData;
import com.deepoove.poi.data.Tables;
import com.deepoove.poi.data.Rows;
import com.deepoove.poi.data.style.RowStyle;
import com.masi.sale.domain.ContractMaterialFull;
import com.masi.sale.domain.Order;
import com.masi.sale.domain.OrderReview;
import com.masi.sale.domain.QualityIndex;
import com.masi.sale.domain.enumeration.OrderStatus;
import com.masi.sale.repository.*;
import com.masi.sale.service.dto.ContractMaterialDTO;
import com.masi.sale.service.dto.OrderDTO;
import com.masi.sale.service.dto.OrderQueryDTO;
import com.masi.sale.service.dto.OrderReviewDTO;
import com.masi.sale.service.mapper.OrderMapper;
import com.masi.sale.service.web.client.EmployeeClient;
import com.masi.sale.service.web.client.FileClient;
import com.masi.sale.service.web.client.ManufactureOrderClient;
import com.masi.sale.web.rest.errors.BadRequestAlertException;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.spire.doc.Document;
import com.spire.doc.FileFormat;
import com.spire.pdf.PdfDocument;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.masi.sale.domain.Order}.
 */
@Service
@Transactional
public class OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository orderRepository;
    private final FileManager fileManager;

    private final OrderMapper orderMapper;
    private final ContractRepository contractRepository;
    private final OrderReviewRepository orderReviewRepository;
    private final FileClient fileClient;
    private final EmployeeClient employeeClient;
    private final Map<String, String> cache = new ConcurrentHashMap<>();
    private final ContractService contractService;
    private final QualityIndexRepository qualityIndexRepository;
    private final ManufactureOrderClient manufactureOrderClient;
    private final ContractMaterialRepository contractMaterialRepository;

    @Scheduled(fixedRate = 15 * 60 * 1000)
    public Mono<Void> clearCache() {
        cache.clear();
        return Mono.empty();
    }


    public OrderService(OrderRepository orderRepository, OrderMapper orderMapper, ContractRepository contractRepository,
                        OrderReviewRepository orderReviewRepository, FileClient fileClient, EmployeeClient employeeClient,
                        ContractService contractService, QualityIndexRepository qualityIndexRepository,
                        ManufactureOrderClient manufactureOrderClient, ContractMaterialRepository contractMaterialRepository) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
        this.contractRepository = contractRepository;
        this.orderReviewRepository = orderReviewRepository;
        this.fileClient = fileClient;
        this.employeeClient = employeeClient;
        this.contractService = contractService;
        try {
            fileManager = new FileManager("order_export");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.qualityIndexRepository = qualityIndexRepository;
        this.manufactureOrderClient = manufactureOrderClient;
        this.contractMaterialRepository = contractMaterialRepository;
    }

    public Flux<ContractMaterialDTO> getContractMaterialByOrderId(UUID orderId) {
        return contractMaterialRepository.findAllByOrderId(orderId).map(ContractMaterialFull::toDto);
    }

    private String formatFullDate(LocalDate date) {
        if (date == null) {
            return "";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("/// dd // MM / yyyy");
        return date.format(formatter).replace("///", "Ngày").replace("//", "tháng").replace("/", "năm");
    }

    private String formatNumber(double number) {
        if (number % 1 == 0) {
            return String.format("%,.0f", number).replaceAll(",", ".");
        }
        return String.format("%,.1f", number).replaceAll(",", ".");
    }

    public Mono<String> exportOrder(UUID orderId) {
        // if (cache.containsKey(orderId.toString())) {
        // return Mono.just(cache.get(orderId.toString()));
        // }
        return orderRepository.findByIdAndIsDeletedIsFalse(orderId)
            .flatMap(order -> {
                return contractService.findOne(order.getContractId()).map(contractDTO -> {
                    order.setContractDTO(contractDTO);
                    return order;
                });
            })
            .flatMap(order -> {
                return contractService.findOne(order.getContractId()).map(contractDTO -> {
                        order.setContractDTO(contractDTO);
                        return order;
                    }).then(Mono.just(order))
                    .flatMap(order1 -> employeeClient.getEmployee(order1.getCreatedBy()).map(employeeDTO -> {
                        order1.setCreatedByDTO(employeeDTO);
                        return order1;
                    })).then(Mono.just(order));
            })
            .flatMap(order -> qualityIndexRepository.findAllByOrderId(orderId).collectList().map(qualityIndexes -> {
                order.setQualityIndexes(qualityIndexes);
                return order;
            }))
            .flatMap(order -> {
                String resourcePath = "/templates/docx/order_requirement.docx";
                var templateStream = getClass().getResourceAsStream(resourcePath);
                HashMap<String, Object> map = new HashMap<>();
                if (order.getDateOrder() == null) {
                    order.setDateOrder(LocalDate.now());
                }
//                map.put("code", order.getOrderCode());
                map.put("code", "BM.GMP.06.03"); //stupid hard code
                if (order.getContract().getCustomer() == null) {
                    map.put("contract_name", String.format("%s", order.getContract().getContractName()));
                } else {
                    map.put("contract_name", String.format("%s - %s", order.getContract().getCustomer().getCompanyName(), order.getContract().getContractName()));
                }
                map.put("code_number", order.getOrderCode());
                map.put("package_type", order.getPackageType());
//                map.put("start_date", order.getDateOrder() != null ? order.getDateOrder().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "");
                map.put("start_date", "30/06/2023"); //stupid hard code
                map.put("full_start_date", formatFullDate(order.getDateOrder()));
                map.put("full_created_dated", formatFullDate(order.getDateOrder()));
                map.put("end_date", order.getFinishDate() != null ? order.getFinishDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "");
                map.put("full_today", formatFullDate(LocalDate.now()));
                map.put("create_name",
                    order.getCreatedByDTO() != null ? order.getCreatedByDTO().getLastName() + " " + order.getCreatedByDTO().getFirstName() : "");
//                map.put("number", order.getNumberOrder());
                map.put("number", "01");    //stupid hard code

                if (order.getContractDTO() != null) {
                    var listProduct = order.getContractDTO().getContractMaterialDTOS().stream().filter(contractMaterialDTO -> orderId.equals(contractMaterialDTO.getOrderId())).toList();
                    var arr = new ArrayList<Map<String, String>>();
                    listProduct.forEach(product -> {
                        var newMap = new HashMap<String, String>();
                        var line = String.format(" –     %s %s %s.", formatNumber(product.getQuantity()), product.getUnit(),
                            product.getMaterialName());
                        newMap.put("content", line);
                        arr.add(newMap);
                    });
                    map.put("quantity_details", arr);
                } else {
                    map.put("quantity_details", new ArrayList<>());
                }

                if (!StringUtils.isBlank(order.getNote())) {
                    var arr = new ArrayList<Map<String, String>>();
                    Arrays.stream(order.getNote().split("\n")).forEach(note -> {
                        var newMap = new HashMap<String, String>();
                        // vai ca dai ????
                        newMap.put("content", String.format(" –     %s", note.trim()));
                        arr.add(newMap);
                    });
                    map.put("note", arr);
                } else {
                    map.put("note", new ArrayList<>());
                }
                var table = Tables.of(Rows.of("CHỈ TIÊU KIỂM NGHIỆM", "CHẤP NHẬN").center()
                    .textFontFamily("Times New Roman")
                    .textFontSize(13)
                    .rowHeight(1)
                    .textBold().create()).center().create();
                order.getQualityIndexes().forEach(qualityIndex -> {
                    table.addRow(Rows.of(qualityIndex.getName(), qualityIndex.getValue()).textFontSize(13)
                        .rowHeight(0.8).center().create());
                });
                map.put("table", table);

                try {
                    var docPath = DocxUtils.renderTemplate(templateStream, map, fileManager);
                    Document doc = new Document(docPath);
                    PdfDocument.setCustomFontsFolders("/fonts");
                    doc.saveToFile(docPath.replace(".docx", ".pdf"), FileFormat.PDF);
                    cache.put(orderId.toString(), docPath.replace(".docx", ".pdf"));
                    return Mono.just(docPath.replace(".docx", ".pdf"));
                } catch (IOException e) {
                    return Mono.error(e);
                }
            }).doOnError(e -> {
                log.error("Error while exporting order", e);
            });
    }

    public Mono<OrderDTO> save(OrderDTO orderDTO) {
        log.debug("Request to save Order : {}", orderDTO);
        Order order = orderDTO.toEntity();
        order.setStatus(OrderStatus.NEW);
        return contractRepository.findById(orderDTO.getContractId())
            .flatMap(contract -> orderRepository.save(order).map(Order::toDTO))
            .flatMap(savedOrder -> contractMaterialRepository
                .updateOrderById(orderDTO.getContractMaterialUse(), savedOrder.getId())
                .doOnError(e -> log.error("Failed to update contract materials", e))
                .thenReturn(savedOrder))
            .flatMap(entity -> {
                if (orderDTO.getQualityIndexes() == null || orderDTO.getQualityIndexes().isEmpty()) {
                    return Mono.error(new BadRequestAlertException("Quality indexes are required", "Order",
                        "qualityindexesrequired"));
                }
                var listQualityIndex = orderDTO.getQualityIndexes();
                var listEntity = listQualityIndex.stream().map(dto -> dto.toEntity(entity.getId()))
                    .collect(Collectors.toList());
                return qualityIndexRepository.saveAll(listEntity).collectList()
                    .map(qualityIndexes -> {
                        entity.setQualityIndexes(listQualityIndex);
                        return entity;
                    });
            }).switchIfEmpty(
                Mono.error(new BadRequestAlertException("Contract not found", "Order", "contractnotfound")));

    }

    public Mono<OrderDTO> update(OrderDTO orderDTO) {
        log.debug("Request to update Order : {}", orderDTO);
        return orderRepository.save(orderMapper.toEntity(orderDTO).setIsPersisted()).map(Order::toDTO);
    }

    /**
     * Partially update a order.
     *
     * @param orderDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<OrderDTO> partialUpdate(OrderDTO orderDTO) {
        log.debug("Request to partially update Order : {}", orderDTO);
        cache.remove(orderDTO.getId().toString());
        return orderRepository.findByIdAndIsDeletedIsFalse(orderDTO.getId())
            .switchIfEmpty(Mono.error(new BadRequestAlertException("Entity not found", "Order", "idnotfound")))
            .flatMap(existingOrder -> {
                if (OrderStatus.APPROVED.equals(existingOrder.getStatus())
                    || OrderStatus.REJECTED.equals(existingOrder.getStatus())) {
                    return Mono.error(new BadRequestAlertException("Order is already approved or rejected", "Order",
                        "orderisapproved"));
                }
                orderDTO.applyChanges(existingOrder);
                return Mono.just(existingOrder.setIsPersisted());
            }).flatMap(orderRepository::save)
            .flatMap(savedOrder -> contractMaterialRepository
                .updateOrderById(orderDTO.getContractMaterialUse(), savedOrder.getId())
                .doOnError(e -> log.error("Failed to update contract materials", e))
                .thenReturn(savedOrder))
            .flatMap(order -> {
                var listQualityIndex = orderDTO.getQualityIndexes();
                var listEntity = listQualityIndex.stream().map(dto -> dto.toEntity(order.getId()))
                    .toList();
                return qualityIndexRepository.deleteByOrderId(order.getId())
                    .then(qualityIndexRepository.saveAll(listEntity).collectList().map(qualityIndexes -> {
                        return order;
                    }));
            }).map(Order::toDTO);
    }

    @Transactional(readOnly = true)
    public Flux<OrderDTO> findAllByQuery(OrderQueryDTO dto, Pageable pageable) {
        log.debug("Request to get all Orders by query");
        return orderRepository.findAllByQuery(dto, pageable).map(Order::toDTO)
            .flatMap(orderDTO -> orderReviewRepository.findLatestAwaitingDate(orderDTO.getId())
                .map(dto1 -> {
                    orderDTO.setWaitUntil(dto1);
                    return orderDTO;
                }).switchIfEmpty(Mono.just(orderDTO)));
    }

    @Transactional(readOnly = true)
    public Mono<List<OrderDTO>> findAllByQueryJoinWithManufacture(OrderQueryDTO dto, Pageable pageable) {
        return orderRepository.findAllByQuery(dto, pageable).map(Order::toDTO)
            .collectList()
            .flatMap(orderDTO -> {
                Map<UUID, OrderDTO> orderDTOMap = orderDTO.stream()
                    .collect(Collectors.toMap(OrderDTO::getId, Function.identity()));
                var listId = orderDTO.stream().map(OrderDTO::getId).collect(Collectors.toList());
                return manufactureOrderClient.getByListIds(listId).map(manufactureOrderDTOS -> {
                    manufactureOrderDTOS.forEach(manufactureOrderDTO -> {
                        if (dto.getStartDate() != null && dto.getEndDate() != null) {
                            if (manufactureOrderDTO.getFromDate().isBefore(dto.getStartDate())
                                || manufactureOrderDTO.getFromDate().isAfter(dto.getEndDate())) {
                                return;
                            }
                        }
                        var order = orderDTOMap.get(manufactureOrderDTO.getOrderId());
                        order.addManufactureOrder(manufactureOrderDTO);
                    });
                    return orderDTO;
                });
            });
    }

    @Transactional(readOnly = true)
    public Mono<Long> countAllByQuery(OrderQueryDTO dto) {
        log.debug("Request to count all Orders by query");
        return orderRepository.countAllByQuery(dto);
    }

    /**
     * Get one order by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<OrderDTO> findOne(UUID id) {
        log.debug("Request to get Order : {}", id);
        return orderRepository.findByIdAndIsDeletedIsFalse(id).map(Order::toDTO)
            .flatMap(orderDTO -> orderReviewRepository.findLatestAwaitingDate(id).map(dto -> {
                orderDTO.setWaitUntil(dto);
                return orderDTO;
            }).switchIfEmpty(Mono.just(orderDTO)))

            .flatMap(orderDTO -> orderReviewRepository.findByOrder(id).map(OrderReview::toDto).collectList()
                .flatMap(orderReviews -> {
                    orderDTO.setOrderReviews(orderReviews);
                    Map<UUID, OrderReviewDTO> orderReviewDTOMap = orderReviews.stream()
                        .collect(Collectors.toMap((e) -> {
                            if (e.getApprovalStatusSignFile() == null) {
                                return UUID.randomUUID();
                            }
                            return UUID.fromString(e.getApprovalStatusSignFile());
                        }, Function.identity()));
                    var listFileId = new ArrayList<>(orderReviewDTOMap.keySet());
                    return fileClient.getFileAttachmentsByListIds(listFileId).collectList().map(files -> {
                        files.forEach(file -> {
                            orderReviewDTOMap.get(file.getId()).setApprovalSignFile(file);
                        });
                        return orderDTO;
                    });
                }))
            .flatMap(orderDTO -> {
                if (orderDTO.getContractId() != null) {
                    return contractService.findOne(orderDTO.getContractId()).map(contractDTO -> {
                        orderDTO.setContract(contractDTO);
                        return orderDTO;
                    });
                }
                return Mono.just(orderDTO);
            })
            .flatMap(orderDTO -> {
                return qualityIndexRepository.findAllByOrderId(id).collectList().map(qualityIndexes -> {
                    orderDTO.setQualityIndexes(qualityIndexes.stream().map(QualityIndex::toDTO).toList());
                    return orderDTO;
                }).then(Mono.just(orderDTO));
            })
            .doOnError(e -> {
                log.error("Error while getting order", e);
            });
    }

    /**
     * Delete the order by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(UUID id) {
        log.debug("Request to delete Order : {}", id);
        return orderRepository.findByIdAndIsDeletedIsFalse(id)
            .switchIfEmpty(Mono.error(new BadRequestAlertException("Entity not found", "Order", "idnotfound")))
            .flatMap(order -> {
                if (OrderStatus.APPROVED.equals(order.getStatus())
                    || OrderStatus.REJECTED.equals(order.getStatus())) {
                    return Mono.error(new BadRequestAlertException("Order is already approved or rejected", "Order",
                        "orderisapproved"));
                }
                order.setIsDeleted(true);
                order.setLastUpdated(ZonedDateTime.now());
                order.setIsPersisted();
                return orderRepository.save(order).then();
            });
    }

    public Mono<OrderDTO> cancel(UUID id) {
        log.debug("Request to cancel Order : {}", id);
        return orderRepository.findById(id)
            .switchIfEmpty(Mono.error(new BadRequestAlertException("Entity not found", "Order", "idnotfound")))
            .flatMap(order -> {
                if (OrderStatus.APPROVED.equals(order.getStatus())
                    || OrderStatus.REJECTED.equals(order.getStatus())) {
                    return Mono.error(new BadRequestAlertException("Order is already approved or rejected", "Order",
                        "orderisapproved"));
                }
                order.setStatus(OrderStatus.CANCELLED);
                order.setLastUpdated(ZonedDateTime.now());
                return orderRepository.save(order.setIsPersisted()).map(Order::toDTO)
                    .doOnTerminate(() -> contractMaterialRepository.removeOrderIdByOrderId(id).subscribe());
            });
    }

    public Mono<Integer> updateContractMaterial(UUID idOrder, UUID idManufacture,
                                                Collection<UUID> listIdContractMaterial) {

        log.debug("Request to partially update Order : {}", idOrder);
        return orderRepository.findByIdAndIsDeletedIsFalse(idOrder)
            .switchIfEmpty(Mono.error(new BadRequestAlertException("Entity not found", "Order", "idnotfound")))
            .flatMap(exist -> {
                return contractMaterialRepository.deleteAllByManufactureById(idManufacture)
                    .flatMap(t -> {
                        if (idOrder != null && idManufacture != null) {
                            return contractMaterialRepository.updateManufactureById(listIdContractMaterial, idManufacture, idOrder)
                                .doOnError(e -> log.error("Failed to update contract materials", e));
                        } else if (idOrder != null) {
                            return contractMaterialRepository.updateManufactureById(listIdContractMaterial, idOrder)
                                .doOnError(e -> log.error("Failed to update contract materials (Order only)", e));
                        } else if (idManufacture != null) {
                            return contractMaterialRepository.updateManufactureById(listIdContractMaterial, idManufacture)
                                .doOnError(e -> log.error("Failed to update contract materials (Manufacture only)", e));
                        }
                        return Mono.error(new BadRequestAlertException("Both idOrder and idManufacture are empty", "Order", "emptyids"));
                    });
            })
            .map(updatedRows -> 1); // Trả về kết quả cuối cùng
    }

    public Flux<OrderDTO> findAllById(List<UUID> ids) {
        log.debug("Request to get all Orders by ids");
        return orderRepository.findAllById(ids).map(Order::toDTO);
    }
}
