package com.masi.production.service;

import com.carevn.masi.utils.CSV.CSVUtils;
import com.carevn.masi.utils.JsonMapperService;
import com.carevn.masi.utils.SecurityUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.masi.production.domain.ManufactureOrder;
import com.masi.production.domain.enumeration.*;
import com.masi.production.repository.*;
import com.masi.production.service.dto.*;
import com.masi.production.service.exportDto.ManuOrderExportDTO;
import com.masi.production.service.exportDto.ManuStandardExportDTO;
import com.masi.production.service.mapper.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import com.masi.production.service.web.LogisticClient;
import com.masi.production.service.web.SaleClient;
import com.masi.production.web.rest.ProductRoutingResource;
import com.masi.production.web.rest.QualityCheckSampleResource;
import com.masi.production.web.rest.errors.BadRequestAlertException;
import io.r2dbc.postgresql.codec.Json;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing
 * {@link ManufactureOrder}.
 */
@Service
@Transactional
@AllArgsConstructor
public class ManufactureOrderService {

    private final Logger log = LoggerFactory.getLogger(ManufactureOrderService.class);

    private final ManufactureOrderRepository manufactureOrderRepository;
    private final WorkOrderRepository workOrderRepository;
    private final ManufactureOrderMapper manufactureOrderMapper;
    private final SaleClient orderClient;
    private final LogisticClient logisticClient;
    private final ReleaseWarehouseRepository releaseWarehouseRepository;
    private final ReleaseWarehouseMapper releaseWarehouseMapper;
    private final Map<UUID, Boolean> PROCESSING_MO = new ConcurrentHashMap<>();
    private final SaleClient saleClient;
    private final QualityCheckSampleService qualityCheckSampleService;
    private final ProductionStandardRepository productionStandardRepository;
    private final QualityCheckSampleResource qualityCheckSampleResource;
    private final ProductMaintainRepository productMaintainRepository;
    private final ProductRoutingResource productRoutingResource;
    private final QualityCheckSampleRepository qualityCheckSampleRepository;
    private final ProductRoutingRepository productRoutingRepository;
    private WorkOrderService workOrderService;
    private ProductPackageRepository productPackageRepository;
    private ProductPackageMapper productPackageMapper;
    private ProductRoutingService productRoutingService;
    private ProductMaintainService productMaintainService;
    private ProductMaintainMapper productMaintainMapper;
    private ProductPackageService productPackageService;
    private final ProductionStandardMapper productionStandardMapper;

    /**
     * Save a manufactureOrder.
     *
     * @return the persisted entity.
     */
    public Mono<Long> countAllByCriteria(ManufactureOrderRO criteria) {
        log.debug("Request to count ManufactureOrders by criteria: {}", criteria);
        return manufactureOrderRepository.countAllByFilter(criteria);
    }

    public Mono<byte[]> exportRecordsAsCSV(ManufactureOrderRO criteria, Pageable pageable) {
        log.debug("Request to export Inventories as CSV");

        return this.findByCriteria(criteria, pageable) // Fetch data by criteria
                .collectList()
                .flatMap(dtoList -> {
                    log.debug("Processing DTO list");

                    List<UUID> orderIds = dtoList.stream()
                            .map(ManuFactureDTO::getOrderId)
                            .filter(Objects::nonNull)
                            .toList();

                    List<UUID> standardIds = dtoList.stream()
                            .map(ManuFactureDTO::getProductionStandardId)
                            .filter(Objects::nonNull)
                            .toList();

                    Mono<List<ManuFactureDTO>> processedDtoList = Mono.just(dtoList);

                    // Fetch additional data for order IDs if available
                    if (!orderIds.isEmpty()) {
                        processedDtoList = processedDtoList.flatMap(currentList ->
                                orderClient.findAllOrderById(orderIds)
                                        .collectList()
                                        .map(orderDTOS -> {
                                            currentList.forEach(dto -> orderDTOS.stream()
                                                    .filter(orderDTO -> orderDTO.getId().equals(dto.getOrderId()))
                                                    .findFirst()
                                                    .ifPresent(dto::setOrder));
                                            return currentList;
                                        })
                        );
                    }

                    // Fetch additional data for production standard IDs if available
                    if (!standardIds.isEmpty()) {
                        processedDtoList = processedDtoList.flatMap(currentList ->
                                productionStandardRepository.findAllById(standardIds)
                                        .map(productionStandardMapper::toDto)
                                        .collectList()
                                        .map(standardDTOS -> {
                                            currentList.forEach(dto -> standardDTOS.stream()
                                                    .filter(standardDTO -> standardDTO.getId().equals(dto.getProductionStandardId()))
                                                    .findFirst()
                                                    .ifPresent(dto::setProductionStandards));
                                            return currentList;
                                        })
                        );
                    }

                    // Determine the type of DTO for exporting
                    return processedDtoList.flatMap(dtoList1 -> {
                        log.debug("Mapping DTOs to export format");
                        List<?> exportDTOs;
                        if (!orderIds.isEmpty()) {
                            exportDTOs = dtoList1.stream()
                                    .map(dto -> {
                                        try {
                                            return new ManuOrderExportDTO(dto);
                                        } catch (IOException e) {
                                            throw new RuntimeException(e);
                                        }
                                    })
                                    .collect(Collectors.toList());
                        } else if (!standardIds.isEmpty()) {
                            exportDTOs = dtoList1.stream()
                                    .map(dto -> {
                                        try {
                                            return new ManuStandardExportDTO(dto);
                                        } catch (IOException e) {
                                            throw new RuntimeException(e);
                                        }
                                    })
                                    .collect(Collectors.toList());
                        } else {
                            return Mono.error(new BadRequestAlertException("No data found", "", ""));
                        }

                        log.debug("Converting export DTOs to CSV");
                        return CSVUtils.convertListToExcel(exportDTOs);
                    });
                });
    }


    public Flux<ManuFactureDTO> findByCriteria(ManufactureOrderRO criteria, Pageable pageable) {
        log.debug("Request to get ManufactureOrders by criteria: {}", criteria);
        return manufactureOrderRepository.findAllByFilter(criteria, pageable)
                .map(ManufactureOrder::toDtoManuFactureDTO);
    }


    public Mono<BigDecimal> countTotalQuantityByStandardId(UUID standardId) {
        return manufactureOrderRepository.countTotalQuantityByStandardId(standardId);
    }

    @Transactional(readOnly = true)
    public Mono<ManuFactureDTO> findOne(UUID id) {
        log.debug("Request to get ManufactureOrder : {}", id);

        return manufactureOrderRepository.findByIdManufacture(id)
                .map(ManufactureOrder::toDtoManuFactureDTO)
                .map(manuFactureDTO -> {
                    Json attribute = manuFactureDTO.getAttributes();
                    List<MaterialManuFactureDTO> materialManuFacture = JsonMapperService.convertJsonToList(attribute, MaterialManuFactureDTO.class, "MaterialManuFactureDTO");
                    AdditivesDTO additives = JsonMapperService.convertJsonToObject(attribute, AdditivesDTO.class, "AdditivesDTO");
                    ProductionManufactureDTO productionManufacture = JsonMapperService.convertJsonToObject(attribute, ProductionManufactureDTO.class, "ProductionManufactureDTO");

                    manuFactureDTO.setMaterialManuFacture(materialManuFacture);
                    manuFactureDTO.setAdditives(additives);
                    manuFactureDTO.setProductionManufacture(productionManufacture);
                    return manuFactureDTO;
                })
//                .flatMap(manuFactureDTO -> {
//                    return saleClient.findOrderById(id)
//                            .doOnNext(manuFactureDTO::setReleaseWarehouseDTOS)
//                            .map(orderDTO -> {
//                                manuFactureDTO.setOrderId(orderDTO.getId());
//                                return manuFactureDTO;
//                            })
//                            .thenReturn(manuFactureDTO);
//                })
                ;
    }

    public Mono<ManuFactureDTO> partialUpdate(ManuFactureDTO manufactureOrderDTO) {
        log.debug("Request to partially update ManufactureOrder : {}", manufactureOrderDTO);
        return this.handleSaveManufacture(manufactureOrderDTO)
                .map(savedMo -> {
                    return new ManuFactureDTO();
                });
    }

    private boolean isObjectEmpty(Object obj) {
        if (obj == null) {
            return true;
        }
        return Arrays.stream(obj.getClass().getDeclaredFields())
                .allMatch(field -> {
                    field.setAccessible(true);
                    try {
                        Object value = field.get(obj);
                        return value == null || (value instanceof Collection && ((Collection<?>) value).isEmpty());
                    } catch (IllegalAccessException e) {
                        log.error("Error accessing field: {}", field.getName(), e);
                        return true;
                    }
                });
    }


    public Mono<Integer> handleSaveManufacture(ManuFactureDTO manuFactureDTO) {
        return manufactureOrderRepository.findById(manuFactureDTO.getId())
                .flatMap(existingManufactureOrder -> {
                    // Cập nhật thời gian chỉnh sửa và lưu
                    existingManufactureOrder.setLastUpdated(ZonedDateTime.now());
                    existingManufactureOrder.setIsPersisted();
                    existingManufactureOrder.toUpdate(manuFactureDTO);

                    // Xử lý theo trạng thái
                    Mono<ManufactureOrder> processStatus = switch (existingManufactureOrder.getStatus()) {
                        case NEW, ADDITIVES, PRODUCTION, PACKAGING-> {
                            try {
                                Json mergedJson = manuFactureDTO.mergeJson();
                                existingManufactureOrder.setAttributes(mergedJson);
                            } catch (Exception e) {
                                log.error("Error merging JSON for manufacture order: {}", e.getMessage(), e);
                                yield Mono.just(existingManufactureOrder);
                            }

                            if (manuFactureDTO.getProductPackageDTO() != null){
                                yield productPackageService.saveOrUpdate(manuFactureDTO.getProductPackageDTO())
                                    .flatMap(productPackageDTO -> {
                                        existingManufactureOrder.setProductPackageId(productPackageDTO.getId());
                                        return Mono.just(existingManufactureOrder);
                                    });
                            }
                            yield Mono.just(existingManufactureOrder);
                        }
                        case PACKED_COMPLETED -> {
                            if (manuFactureDTO.getProductMaintainDTO() == null || isObjectEmpty(manuFactureDTO.getProductMaintainDTO())) {
                                log.error("MaterialManuFacture data is required for PACKED_COMPLETED status");
                                yield Mono.just(existingManufactureOrder);
                            }
                            yield productMaintainService.saveOrUpdate(manuFactureDTO.getProductMaintainDTO())
                                    .flatMap(productMaintainDTO -> {
                                        existingManufactureOrder.setProductMaintainId(productMaintainDTO.getId());
                                        return Mono.just(existingManufactureOrder);
                                    });
                        }
                        case SHIPPED -> {
                            if (manuFactureDTO.getProductRoutingDTO() == null || isObjectEmpty(manuFactureDTO.getProductRoutingDTO())) {
                                log.error("MaterialManuFacture data is required for MATERIAL SHIPPED");
                                yield Mono.just(existingManufactureOrder);
                            }
                            manuFactureDTO.getProductRoutingDTO().setQuantity(manuFactureDTO.getProductPackageDTO().getQuantity());
                            yield productRoutingService.saveOrUpdate(manuFactureDTO.getProductRoutingDTO())
                                    .flatMap(productRoutingDTO -> {
                                        existingManufactureOrder.setProductionRoutingId(productRoutingDTO.getId());
                                        return Mono.just(existingManufactureOrder);
                                    });
                        }
                        default -> Mono.just(existingManufactureOrder);
                    };

                    return processStatus
                            .flatMap(order -> handleQualityCheckSample(manuFactureDTO, order)
                                    .then(handleProductPackage(manuFactureDTO, order))
                                    .then(Mono.just(order)));
                })
                .flatMap(manufactureOrderRepository::save)
                .thenReturn(1);
    }


    private Mono<Void> handleQualityCheckSample(ManuFactureDTO manuFactureDTO, ManufactureOrder existingManufactureOrder) {
        if (manuFactureDTO.getQualityCheckSampleDTO() != null) {
            return qualityCheckSampleService.saveOrUpdate(manuFactureDTO.getQualityCheckSampleDTO())
                    .flatMap(qualityCheckSampleDTO -> {
                        existingManufactureOrder.setQualityCheckSampleId(manuFactureDTO.getQualityCheckSampleDTO().getId());
                        return manufactureOrderRepository.save(existingManufactureOrder).then();
                    });
        }
        return Mono.empty();
    }

    private Mono<Void> handleProductPackage(ManuFactureDTO manuFactureDTO, ManufactureOrder existingManufactureOrder) {
        if (manuFactureDTO.getProductPackageDTO() != null) {
            if (manuFactureDTO.getProductPackageDTO().getStatus() == null) {
                log.error("MaterialManuFacture data is required for PACKAGING status");
            } else {
                ProductPackageDTO manufactureOrderDTO = new ProductPackageDTO();
                manufactureOrderDTO.setId(manuFactureDTO.getProductPackageDTO().getId());
                manufactureOrderDTO.setStatus(manuFactureDTO.getProductPackageDTO().getStatus());
                return productPackageService.partialUpdate(manuFactureDTO.getProductPackageDTO()).then();
            }
        }
        return Mono.empty();
    }


    public Mono<Void> completeManuFacture(UUID id, Integer isSkip) {
        return manufactureOrderRepository.findById(id)
                .flatMap(manufactureOrder -> {
                    StatusEntity currentStatus = manufactureOrder.getStatus();
                    return StatusEntity.getNextStatus(currentStatus, isSkip)
                            .map(nextStatus -> {
                                manufactureOrder.setStatus(nextStatus);
                                manufactureOrder.setLastUpdated(ZonedDateTime.now());
                                if (nextStatus.equals(StatusEntity.COMPLETED)) {
                                    LocalDate localNowHCM = LocalDate.now(ZoneId.of("Asia/Ho_Chi_Minh"));
                                    manufactureOrder.setToDate(localNowHCM);
                                }
                                if (manufactureOrder.getStatus().equals(StatusEntity.ADDITIVES) || manufactureOrder.getStatus().equals(StatusEntity.PRODUCTION)) {
                                    return manufactureOrderRepository.save(manufactureOrder)
                                            .then(this.completeStateMaterial(manufactureOrder))
                                            .then();

                                }
                                if (manufactureOrder.getStatus().equals(StatusEntity.COMPLETED)) {
                                    return manufactureOrderRepository.save(manufactureOrder)
                                            .then(this.completeStateQuantity(id))
                                            .then();
                                }
                                return manufactureOrderRepository.save(manufactureOrder).then();
                            })
                            .orElseGet(() -> Mono.error(new BadRequestAlertException("No next status available", "ManufactureOrder", "statusNotAccessible")));
                });
    }

    public Mono<Void> completeStateQuantity(UUID id) {
        return this.findOne(id)
                .flatMap(manufactureOrder -> {
                    if (manufactureOrder.getProductRoutingDTO() == null) {
                        log.error("MaterialManuFacture data is required for PACKAGING status. ManufactureOrder ID: {}", id);
                        return Mono.empty();
                    }

                    return buildInventoriesRequest(manufactureOrder, manufactureOrder.getProductRoutingDTO())
                            .flatMap(inventoriesRequest -> logisticClient.createInventoriesStorage(inventoriesRequest)
                                    .doOnSuccess(response -> log.info("Successfully created inventory storage for ManufactureOrder ID: {}", id))
                                    .onErrorResume(ex -> {
                                        log.error("Failed to create inventory storage for ManufactureOrder ID: {}", id, ex);
                                        return Mono.empty();
                                    }));
                })
                .then();
    }

    private Mono<InventoriesRequest> buildInventoriesRequest(ManuFactureDTO manufactureOrder, ProductRoutingDTO productRoutingDTO) {
        return SecurityUtils.getUserJWTDetail().map(user -> {
            InventoriesRequest inventoriesRequest = new InventoriesRequest();
            inventoriesRequest.setIncomingWarehouseId(productRoutingDTO.getStorageId());
            inventoriesRequest.setDateCreate(productRoutingDTO.getWarehouseDate());
            inventoriesRequest.setWorkspaceName(productRoutingDTO.getName());
            inventoriesRequest.setIsNoReview(true);
            inventoriesRequest.setIsReview(false);
            inventoriesRequest.setCode(manufactureOrder.getProductPackageDTO().getPackageCode());
            inventoriesRequest.setTotalAmount(BigDecimal.ZERO);
            inventoriesRequest.setTotalQuantity(productRoutingDTO.getQuantity() * 50f);
            inventoriesRequest.setWarehouseGroupType("WAREHOUSE_COMMERCE_IMPORT");
            inventoriesRequest.setEmployeeId(user.getUserId());
            inventoriesRequest.setCompanyImport(user.getCompanyId());
            inventoriesRequest.setProductionId(manufactureOrder.getId());
            if (manufactureOrder.getOrderId() != null)
                inventoriesRequest.setIsOrderManu(true);
            else
                inventoriesRequest.setIsProductStanding(true);

            InventoriesRequest.InventoryDetail inventoryDetail = new InventoriesRequest.InventoryDetail();
            inventoryDetail.setItemId(manufactureOrder.getQualityCheckSampleDTO().getItemId());
            inventoryDetail.setQuantity(productRoutingDTO.getQuantity() * 50f);
            inventoryDetail.setCode(manufactureOrder.getProductMaintainDTO().getProductBatchCode());
            inventoryDetail.setPrice(BigDecimal.ZERO);
            inventoryDetail.setNote("SẢN XUẤT TRONG LỆNH SẢN XUẤT");
            inventoryDetail.setTotalPrice(BigDecimal.ZERO);

            try {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode attributesNode = objectMapper.readTree(manufactureOrder.getQualityCheckSampleDTO().getAttributes().asArray());

                if (attributesNode.has("isDone") && attributesNode.get("isDone").asBoolean()) {
                    inventoryDetail.setIsDefaultItem(false);
                }
                else
                    inventoryDetail.setIsDefaultItem(true);
            } catch (Exception e) {
                e.printStackTrace();
            }

            inventoryDetail.setProteinPercentageApply(manufactureOrder.getQualityCheckSampleDTO().getProteinPercentageApply());

            inventoriesRequest.setInventoriesDetails(Collections.singletonList(inventoryDetail));
            return inventoriesRequest;
        });
    }


    public Mono<Void> completeStateMaterial(ManufactureOrder manufactureOrder) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();

            JsonNode attributes = objectMapper.readTree(manufactureOrder.getAttributes().asArray());

            JsonNode rawMaterial2 = attributes.path("rawMaterial2");
            JsonNode items = rawMaterial2.path("items");

            if (!items.isArray()) {
                return Mono.error(new IllegalArgumentException("Invalid items format in attributes"));
            }

            List<MaterialManuFactureDTO> materialManuFactureDTOS = new ArrayList<>();
            for (JsonNode item : items) {
                MaterialManuFactureDTO materialManuFactureDTO = new MaterialManuFactureDTO();
                // Thiết lập từng thuộc tính từ JsonNode
                materialManuFactureDTO.setQuantity(BigDecimal.valueOf(item.path("quantity").asDouble()));
                materialManuFactureDTO.setQuantityUse(BigDecimal.valueOf(item.path("quantityUse").asDouble()));
                materialManuFactureDTO.setProductionMaintainId(item.path("productionMaintainId").asText());

                materialManuFactureDTOS.add(materialManuFactureDTO);
            }


            log.info("Extracted MaterialManuFactureDTOs: {}", materialManuFactureDTOS);

            return logisticClient.updateVolumeInventoriesStorage(materialManuFactureDTOS)
                    .then();
        } catch (Exception e) {
            log.error("Error processing completeStateMaterial", e);
            return Mono.error(e);
        }
    }


    @Transactional
    public Mono<ManuFactureDTO> save(ManuFactureDTO manufactureOrderDTO) {
        log.debug("Request to save ManufactureOrder : {}", manufactureOrderDTO);

        return SecurityUtils.getUserJWTDetail().flatMap(user -> {
            // Khởi tạo đối tượng ManufactureOrder
            ManufactureOrder manufactureOrder = new ManufactureOrder();
            manufactureOrder.setId(UUID.randomUUID());
            manufactureOrder.setCode(manufactureOrderDTO.getCode());
            manufactureOrder.setName(manufactureOrderDTO.getName());
            manufactureOrder.setFromDate(manufactureOrderDTO.getFromDate());
            manufactureOrder.setStatus(StatusEntity.NEW);

            if (manufactureOrderDTO.getOrderId() != null && !StringUtils.isBlank(manufactureOrderDTO.getOrderId().toString())) {
                manufactureOrder.setOrderId(manufactureOrderDTO.getOrderId());
                manufactureOrder.setManufactureOrderType(ManufactureOrderType.MANUFACTURE_ORDER_BY_ORDER.toString());
                manufactureOrder.setProductionStandardId(null);
            } else if (manufactureOrderDTO.getProductionStandardId() != null && !StringUtils.isBlank(manufactureOrderDTO.getProductionStandardId().toString())) {
                manufactureOrder.setProductionStandardId(manufactureOrderDTO.getProductionStandardId());
                manufactureOrder.setManufactureOrderType(ManufactureOrderType.MANUFACTURE_ORDER_BY_STANDARD.toString());
                manufactureOrder.setOrderId(null);
            } else
                throw new BadRequestAlertException("ManufactureOrderType Not Found", "", "");

            manufactureOrder.setMaterialId(UUID.fromString("00000000-0000-0000-0000-000000000000"));
            manufactureOrder.setToDate(manufactureOrderDTO.getToDate());
            manufactureOrder.setProductionQuantity(manufactureOrderDTO.getProductionQuantity());
            manufactureOrder.setCreatedAt(ZonedDateTime.now());
            manufactureOrder.setCreatedBy(user.getUserId());
            manufactureOrder.setOrderItemId(manufactureOrderDTO.getOrderItemId());
            manufactureOrder.setIsActive(true);

            // Xử lý merge JSON
            try {
                Json mergedJson = manufactureOrderDTO.mergeJson();
                manufactureOrder.setAttributes(mergedJson);
            } catch (Exception e) {
                log.error("Error merging JSON for manufacture order: {}", e.getMessage(), e);
            }

            // Lưu vào repository và trả về DTO
            return manufactureOrderRepository.save(manufactureOrder)
                    .map(ManufactureOrder::toDtoManuFactureDTO)
                    .doOnError(error -> log.error("Error saving ManufactureOrder: {}", error.getMessage(), error));
        });
    }


//    public Mono<ManufactureOrderDTO> save(ManufactureOrderDTO manufactureOrderDTO, boolean autoCreateWorkOrder) {
//        log.debug("Request to save ManufactureOrder : {}", manufactureOrderDTO);
//        manufactureOrderDTO.setCreatedAt(ZonedDateTime.now());
//        manufactureOrderDTO.setLastUpdated(ZonedDateTime.now());
//        manufactureOrderDTO.setStatus(MoStatus.NEW);
//        manufactureOrderDTO.setIsActive(true);
//        if (manufactureOrderDTO.getOrderId() != null && !StringUtils.isBlank(manufactureOrderDTO.getOrderId().toString())) {
//            manufactureOrderDTO.setManufactureOrderType(ManufactureOrderType.MANUFACTURE_ORDER_BY_ORDER.toString());
//            manufactureOrderDTO.setProductionStandardId(null);
//        } else if (manufactureOrderDTO.getProductionStandardId() != null && !StringUtils.isBlank(manufactureOrderDTO.getProductionStandardId().toString())) {
//            manufactureOrderDTO.setManufactureOrderType(ManufactureOrderType.MANUFACTURE_ORDER_BY_STANDARD.toString());
//            manufactureOrderDTO.setOrderId(null);
//        } else
//            throw new BadRequestAlertException("ManufactureOrderType Not Found", "", "");
//
//        return manufactureOrderRepository
//            .save(manufactureOrderDTO.toEntity())
//            .map(order -> {
//                log.debug("ManufactureOrder saved: {}", order);
//                return order.toDto();
//            })
//            .flatMap(dto -> orderClient
//                .updateContractMaterial(dto.getId(), manufactureOrderDTO.getListIdContractMaterial(), manufactureOrderDTO.getOrderId())  // Update contract material after saving
//                .then(Mono.just(dto)))
//            .flatMap(dto -> Flux.fromIterable(manufactureOrderDTO.getReleaseWarehouseDTOS())
//                .flatMap(releaseWarehouseDTO -> {
//                    releaseWarehouseDTO.setId(UUID.randomUUID());
//
//                    if (releaseWarehouseDTO.getOrderId() == null || StringUtils.isBlank(releaseWarehouseDTO.getOrderId().toString())) {
//                        releaseWarehouseDTO.setOrderId(manufactureOrderDTO.getOrderId());
//                    }
//
//                    if (releaseWarehouseDTO.getProductionStandardId() == null || StringUtils.isBlank(releaseWarehouseDTO.getProductionStandardId().toString())) {
//                        releaseWarehouseDTO.setProductionStandardId(manufactureOrderDTO.getProductionStandardId());
//                    }
//
//                    releaseWarehouseDTO.setManufactureOrderId(manufactureOrderDTO.getId());
//                    return releaseWarehouseRepository.save(releaseWarehouseMapper.toEntity(releaseWarehouseDTO));
//                })
//                .then(Mono.just(dto))
//            );
//    }


/*
    public Mono<ManufactureOrderDTO> startProduction(UUID manufactureOrderId) {
        if (PROCESSING_MO.containsKey(manufactureOrderId)) {
            log.error("Manufacture Order is processing: {}", manufactureOrderId);
            return Mono.empty();
        }
        PROCESSING_MO.putIfAbsent(manufactureOrderId, true);
        return manufactureOrderRepository
                .findById(manufactureOrderId)
                .flatMap(existingManufactureOrder -> { // Changed from map to flatMap
                    if (!existingManufactureOrder.getStatus().equals(StatusEntity.NEW))
                        return Mono.error(new BadRequestAlertException("Status no access", "", ""));

                    existingManufactureOrder.setStatus(StatusEntity.PENDING);
                    existingManufactureOrder.setIsPersisted();
                    List<WorkOrderDTO> newWorkOrders;


                    if (existingManufactureOrder.getManufactureOrderType() != null
                            && existingManufactureOrder.getManufactureOrderType().equals(ManufactureOrderType.MANUFACTURE_ORDER_BY_ORDER.toString())) {
                        Set<WorkOrderType> workOrderTypes = WorkOrderType.WORK_ORDER_TYPE_ORDER.keySet();
                        newWorkOrders = workOrderTypes.stream()
                                .map(workOrderType -> {
                                    var workOrder = new WorkOrderDTO();
                                    workOrder.setChecklistType(workOrderType);
                                    workOrder.setMoId(existingManufactureOrder.getId());
                                    workOrder.setIsActive(true);
                                    workOrder.setId(UUID.randomUUID());
                                    workOrder.setCreatedAt(ZonedDateTime.now());
                                    // workOrder.setLastUpdated(ZonedDateTime.now());
//                                    workOrder.setManufactureOrderType(ManufactureOrderType.MANUFACTURE_ORDER_BY_ORDER);
                                    workOrder.setStatus(WoStatus.NEW);
                                    workOrder.setFromDate(existingManufactureOrder.getFromDate());
                                    workOrder.setToDate(existingManufactureOrder.getToDate());
                                    workOrder.setWithNewWorkItemSameType(true);
                                    return workOrder;
                                })
                                .toList();
                    } else if (existingManufactureOrder.getManufactureOrderType() != null
                            && existingManufactureOrder.getManufactureOrderType().equals(ManufactureOrderType.MANUFACTURE_ORDER_BY_STANDARD.toString())) {
                        Set<WorkOrderType> workOrderTypes = WorkOrderType.WORK_ORDER_TYPE_STANDARD.keySet();
                        newWorkOrders = workOrderTypes.stream()
                                .map(workOrderType -> {
                                    var workOrder = new WorkOrderDTO();
                                    workOrder.setChecklistType(workOrderType);
                                    workOrder.setMoId(existingManufactureOrder.getId());
                                    workOrder.setIsActive(true);
                                    workOrder.setId(UUID.randomUUID());
                                    workOrder.setCreatedAt(ZonedDateTime.now());
//                                    workOrder.setManufactureOrderType(ManufactureOrderType.MANUFACTURE_ORDER_BY_STANDARD);
                                    // workOrder.setLastUpdated(ZonedDateTime.now());
                                    workOrder.setStatus(WoStatus.NEW);
                                    workOrder.setFromDate(existingManufactureOrder.getFromDate());
                                    workOrder.setToDate(existingManufactureOrder.getToDate());
                                    workOrder.setWithNewWorkItemSameType(true);
                                    return workOrder;
                                })
                                .toList();
                    } else {
                        newWorkOrders = null;
                        return Mono.error(new BadRequestAlertException("Type not null", "", ""));
                    }


                    return workOrderService.saveAll(newWorkOrders) // Moved saveAll outside
                            .then(manufactureOrderRepository.save(existingManufactureOrder)) // Save after work orders
                            .map(ManufactureOrder::toDto)
                            .flatMap(dto -> {
                                return releaseWarehouseRepository.findAllByManufactureOrderIdAndIsDeleted(manufactureOrderId, false)
                                        .map(releaseWarehouseMapper::toDto)
                                        .collectList()
                                        .flatMap(releaseWarehouses -> {
                                            return logisticClient.createRequestWarehouseRelease(releaseWarehouses)
                                                    .then(Mono.just(dto));
                                        });
                            });
                }).doOnTerminate(() -> {
                    PROCESSING_MO.remove(manufactureOrderId);
                });
    }
*/

    public Mono<List<ManufactureOrderDTO>> findByListOrderId(Collection<UUID> orderIds) {
        return manufactureOrderRepository.findAllByOrderIdInAndIsActiveIsTrue(orderIds)
                .map(ManufactureOrder::toDto)
                .collectList();
    }

    /**
     * Cancel a manufactureOrder.
     *
     * @param id the id of the ManufactureOrder to cancel
     * @return the persisted entity.
     */

    public Mono<ManufactureOrderDTO> cancel(UUID id) {
        log.debug("Request to cancel ManufactureOrder : {}", id);
//        return manufactureOrderRepository
//                .findById(id)
//                .map(manufactureOrder -> {
//                    if (!manufactureOrder.getStatus().equals(MoStatus.NEW))
//                        throw new BadRequestAlertException("Status not access", "", "");
//                    manufactureOrder.setStatus(StatusEntity.CANCELLED);
//                    manufactureOrder.setLastUpdated(ZonedDateTime.now());
//                    return manufactureOrder.setIsPersisted();
//                })
//                .flatMap(manufactureOrderRepository::save)
//                .map(ManufactureOrder::toDto)
//                .switchIfEmpty(Mono.error(new BadRequestAlertException("ManufactureOrder not found", "", "")));
//
//    }
        return manufactureOrderRepository.findById(id)
                .flatMap(manufactureOrder -> {
                    if (manufactureOrder == null) {
                        return Mono.error(new BadRequestAlertException("ManufactureOrder not found", "", ""));
                    }
                    manufactureOrder.setStatus(StatusEntity.CANCELLED);
                    return manufactureOrderRepository.save(manufactureOrder)
                            .map(ManufactureOrder::toDto);
                })
                .switchIfEmpty(Mono.error(new BadRequestAlertException("ManufactureOrder not found", "", "")));
    }


    /**
     * Partially update a manufactureOrder.
     *
     * @param manufactureOrderDTO the entity to update partially.
     * @return the persisted entity.
     */
//    public Mono<ManufactureOrderDTO> partialUpdate(ManufactureOrderDTO manufactureOrderDTO) {
//        log.debug("Request to partially update ManufactureOrder : {}", manufactureOrderDTO);
//
//        return manufactureOrderRepository
//                .findById(manufactureOrderDTO.getId())
//                .map(existingManufactureOrder -> {
//                    manufactureOrderDTO.applyUpdateTo(existingManufactureOrder);
//                    existingManufactureOrder.setIsPersisted();
//                    existingManufactureOrder.setLastUpdated(ZonedDateTime.now());
//
//                    if (!existingManufactureOrder.getStatus().equals(MoStatus.NEW))
//                        throw new BadRequestAlertException("Status not access", "", "");
//
//                    return existingManufactureOrder.setIsPersisted();
//                })
//                .flatMap(manufactureOrderRepository::save)
//                .map(ManufactureOrder::toDto)
//                .flatMap(dto -> {
//                    return releaseWarehouseRepository.changeIsDeletedAllByManufactureOrderId(manufactureOrderDTO.getId(), true)
//                            .thenMany(Flux.fromIterable(manufactureOrderDTO.getReleaseWarehouseDTOS())
//                                    .flatMap(releaseWarehouseDTO -> {
//                                        releaseWarehouseDTO.setId(UUID.randomUUID());
//
//                                        // Cập nhật orderId nếu null
//                                        if (releaseWarehouseDTO.getOrderId() == null || StringUtils.isBlank(releaseWarehouseDTO.getOrderId().toString())) {
//                                            releaseWarehouseDTO.setOrderId(manufactureOrderDTO.getOrderId());
//                                        }
//
//                                        // Cập nhật productionStandardId nếu null
//                                        if (releaseWarehouseDTO.getProductionStandardId() == null || StringUtils.isBlank(releaseWarehouseDTO.getProductionStandardId().toString())) {
//                                            releaseWarehouseDTO.setProductionStandardId(manufactureOrderDTO.getProductionStandardId());
//                                        }
//
//                                        releaseWarehouseDTO.setManufactureOrderId(manufactureOrderDTO.getId());
//
//                                        return releaseWarehouseRepository.save(releaseWarehouseMapper.toEntity(releaseWarehouseDTO))
//                                                .then(orderClient.updateContractMaterial(manufactureOrderDTO.getId(), manufactureOrderDTO.getListIdContractMaterial(), manufactureOrderDTO.getOrderId()));
//
//                                    }))
//                            .then(Mono.just(dto)); // Trả về DTO sau khi hoàn tất
//                });
//    }


    /**
     * Get all the manufactureOrders.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<ManufactureOrderDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ManufactureOrders");
        return manufactureOrderRepository.findAllByIsActiveIsTrue(pageable).map(ManufactureOrder::toDto);
    }

    /**
     * Get all the manufactureOrders.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<ManufactureOrderDTO> findAll(Pageable pageable, ManufactureOrderRO moRO) {
        if (moRO == null) {
            return manufactureOrderRepository.findAllByIsActiveIsTrue(pageable).map(manufactureOrderMapper::toDto);
        }
        log.debug("Request to query ManufactureOrders");
        return manufactureOrderRepository.findAllByFilter(moRO, pageable)
                // map work orders to manufacture order
                .flatMap(manufactureOrder -> workOrderRepository
                        .findAllByManufactureOrderIdAndIsActive(manufactureOrder.getId(), true)
                        .collectList().flatMap(workOrders -> {
                            manufactureOrder.setWorkOrders(workOrders);
                            return Mono.just(manufactureOrder);
                        }))
                .map(ManufactureOrder::toDto);
    }

    public Mono<Long> countAll(ManufactureOrderRO moRO) {
        if (moRO == null) {
            return manufactureOrderRepository.countAllByIsActiveIsTrue();
        }
        log.debug("Request to count ManufactureOrders");
        return manufactureOrderRepository.countAllByFilter(moRO);
    }

    /**
     * Returns the number of manufactureOrders available.
     *
     * @return the number of entities in the database.
     */
    public Mono<Long> countAll() {
        return manufactureOrderRepository.countAllByIsActiveIsTrue();
    }

    /**
     * Get one manufactureOrder by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */


//    @Transactional(readOnly = true)
//    public Mono<ManufactureOrderDTO> findOne(UUID id) {
//        log.debug("Request to get ManufactureOrder : {}", id);
//
//        return manufactureOrderRepository.findById(id)
//                .flatMap(manufactureOrder -> workOrderRepository.findAllByManufactureOrderIdAndIsActive(id, true)
//                        .collectList()
//                        .flatMap(workOrders -> {
//                            manufactureOrder.setWorkOrders(new HashSet<>(workOrders));
//                            return Mono.just(manufactureOrder);
//                        })
//                )
//                .map(ManufactureOrder::toDto)
//                .flatMap(manufactureOrderDTO -> releaseWarehouseRepository.findAllByManufactureOrderIdAndIsDeleted(id, false)
//                        .map(releaseWarehouseMapper::toDto)
//                        .collectList()
//                        .doOnNext(manufactureOrderDTO::setReleaseWarehouseDTOS)
//                        .thenReturn(manufactureOrderDTO)
//                );
//    }


    /**
     * Delete the manufactureOrder by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<ManufactureOrderDTO> delete(UUID id) {
        log.debug("Request to delete ManufactureOrder : {}", id);

        return manufactureOrderRepository.findById(id)
                .flatMap(manufactureOrder -> {
                    if (manufactureOrder == null) {
                        return Mono.error(new BadRequestAlertException("ManufactureOrder not found", "", ""));
                    }
                    manufactureOrder.setStatus(StatusEntity.CANCELLED);
                    return manufactureOrderRepository.save(manufactureOrder)
                            .map(ManufactureOrder::toDto);
                })
                .switchIfEmpty(Mono.error(new BadRequestAlertException("ManufactureOrder not found", "", "")));
    }


    /**
     * Get all the manufactureOrders.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<ManufactureOrderDTO> findAllWithWorkOrder(Pageable pageable, ManufactureWorkOrdersRO moRO) {
        if (moRO == null) {
            return manufactureOrderRepository.findAllByIsActiveIsTrue(pageable).map(manufactureOrderMapper::toDto);
        }
        return findDataByFilter(moRO, pageable).map(ManufactureOrder::toDto);
    }

    public Mono<Long> countAllWithWorkOrder(ManufactureWorkOrdersRO moRO) {
        if (moRO == null) {
            return manufactureOrderRepository.countAllByIsActiveIsTrue();
        }
        return findDataByFilter(moRO, null).count();
    }

    private Flux<ManufactureOrder> findDataByFilter(ManufactureWorkOrdersRO moRO, Pageable pageable) {
        return manufactureOrderRepository.findAllWithWorkOrdersByFilter(moRO, pageable)
                .flatMap(manufactureOrder -> workOrderRepository
                        .findAllByMoIdAndStatuses(moRO, manufactureOrder.getId(), true)
                        .collectList().flatMap(workOrders -> {
                            boolean hasStatuses = moRO.getStatuses() != null;
                            boolean hasValidDateRange = moRO.getFromDate() != null && moRO.getToDate() != null;
                            boolean noWorkOrders = workOrders == null || workOrders.isEmpty();

                            if ((hasStatuses || hasValidDateRange) && noWorkOrders) {
                                return Mono.empty();
                            }
                            assert workOrders != null;
                            var list = new LinkedList<>(workOrders);
                            manufactureOrder.setWorkOrders(list);
                            return Mono.just(manufactureOrder);
                        }));
    }

    public Mono<ManufactureOrderDTO> findByMaintainId(UUID productMaintainId, String company) {
        return manufactureOrderRepository.findByMaintainIdAndCompany(productMaintainId, company).map(ManufactureOrder::toDto);
    }

}
