package com.masi.production.service;

import com.carevn.masi.utils.SecurityUtils;
import com.masi.production.domain.*;
import com.masi.production.domain.enumeration.MoStatus;
import com.masi.production.domain.enumeration.QcSampleStatus;
import com.masi.production.domain.enumeration.StatusEntity;
import com.masi.production.repository.*;
import com.masi.production.service.dto.*;
import com.masi.production.service.mapper.ManufactureOrderMapper;
import com.masi.production.service.mapper.ProductRoutingMapper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.masi.production.service.web.LogisticClient;
import com.masi.production.web.rest.errors.BadRequestAlertException;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.masi.production.domain.ProductRouting}.
 */
@Service
@Transactional
public class ProductRoutingService {

    private final Logger log = LoggerFactory.getLogger(ProductRoutingService.class);

    private final ProductRoutingRepository productRoutingRepository;

    private final ProductRoutingMapper productRoutingMapper;

    private final StorageService storageService;

    private final FactoryService factoryService;
    private final LogisticClient logisticClient;
    private final ProductMaintainRepository productMaintainRepository;
    private final ManufactureOrderRepository manufactureOrderRepository;
    private final ManufactureOrderMapper manufactureOrderMapper;

    public ProductRoutingService(ProductRoutingRepository productRoutingRepository, ProductRoutingMapper productRoutingMapper, StorageService storageService, FactoryService factoryService, LogisticClient logisticClient, ProductMaintainRepository productMaintainRepository, ManufactureOrderRepository manufactureOrderRepository, ManufactureOrderMapper manufactureOrderMapper) {
        this.productRoutingRepository = productRoutingRepository;
        this.productRoutingMapper = productRoutingMapper;
        this.factoryService = factoryService;
        this.storageService = storageService;
        this.logisticClient = logisticClient;
        this.productMaintainRepository = productMaintainRepository;
        this.manufactureOrderRepository = manufactureOrderRepository;
        this.manufactureOrderMapper = manufactureOrderMapper;
    }

    /**
     * Save a productRouting.
     *
     * @param productRoutingDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ProductRoutingDTO> save(ProductRoutingDTO productRoutingDTO) {
        log.debug("Request to save ProductRouting : {}", productRoutingDTO);
        return SecurityUtils.getUserJWTDetail().flatMap(login -> {
            return productRoutingRepository.save(productRoutingMapper.toEntity(productRoutingDTO))
                    .flatMap(saved -> {
                        return productRoutingRepository.findById(saved.getId(), login.getCompanyId())
                                .flatMap(e -> {
                                    if (e.getProductMaintain() == null) {
                                        return Mono.error(new BadRequestAlertException("ProductMaintain not found", "ProductRouting", "PRODUCT_MAINTAIN_NOT_FOUND"));
                                    }
                                    var manufactureId = Optional.of(e.getProductMaintain())
                                            .map(ProductMaintain::getProductPackage)
                                            .map(ProductPackage::getManufactureOrderId)
                                            .orElseThrow(() -> new BadRequestAlertException("ManufactureOrderId not found", "ProductRouting", "ORDER_NOT_FOUND"));
                                    if (manufactureId == null) {
                                        return Mono.error(new BadRequestAlertException("ManufactureOrderId not found", "ProductRouting", "ORDER_NOT_FOUND"));
                                    }
                                    return manufactureOrderRepository.findById(manufactureId)
                                            .flatMap(manufactureOrder -> {
                                                manufactureOrder.setProductionRoutingId(e.getId());
                                                manufactureOrder.setProductPackageId(e.getProductMaintain().getProductPackage().getId());
                                                manufactureOrder.setProductMaintain(e.getProductMaintain());
                                                manufactureOrder.setIsPersisted();
                                                return manufactureOrderRepository.save(manufactureOrder)
                                                        .flatMap(savedManufactureOrder -> {
                                                            return Mono.just(e).map(productRoutingMapper::toDto);
                                                        });
                                            });

                                }).doOnError(e -> e.printStackTrace());
                    });
        });
    }

    public Mono<ProductRoutingDTO> saveOrUpdate(ProductRoutingDTO productRoutingDTO) {
        log.debug("Request to saveOrUpdate ProductRouting : {}", productRoutingDTO);
        if (productRoutingDTO.getId() == null)
            productRoutingDTO.setId(UUID.randomUUID());
        if (productRoutingDTO.getName() == null) {
            productRoutingDTO.setName("ProductRouting");
        }
        return productRoutingRepository
                .findById(productRoutingDTO.getId())
                .flatMap(existingQualityCheckSample -> this.partialUpdate(productRoutingDTO))
                .switchIfEmpty(Mono.defer(() -> this.save(productRoutingDTO)));
    }

    private InventoriesDTO createInventories(ManufactureOrder manufactureOrder, ProductRoutingDTO productRoutingDTO) {
        InventoriesDTO inventoriesDTO = new InventoriesDTO();
        inventoriesDTO.setIncomingWarehouseId(productRoutingDTO.getStorageId());
        inventoriesDTO.setWarehouseGroupType(InventoriesDTO.WarehouseGroupType.WAREHOUSE_COMMERCE_IMPORT);
        //inventoriesDTO.setInventoriesTypeId(UUID.fromString("4ac506e6-b869-d664-2dfa-305284a56b83"));
        inventoriesDTO.setDateCreate(LocalDate.now());
        try {
            inventoriesDTO.setEmployeeId(UUID.fromString(productRoutingDTO.getCreatedBy()));
        } catch (Exception ignored) {
        }
        //inventoriesDTO.setNote("ghi chúuuuuuuuuuuu");
        //inventoriesDTO.setCustomerId();
        inventoriesDTO.setOrderId(manufactureOrder.getOrderId());
        inventoriesDTO.setManufacturingOrderId(manufactureOrder.getId());


        InventoriesDetailDTO detail = new InventoriesDetailDTO();
        detail.setItemId(manufactureOrder.getMaterialId());
        BigDecimal weight = manufactureOrder.getProductPackage() != null ? BigDecimal.valueOf(manufactureOrder.getProductPackage().getWeight()) : BigDecimal.ZERO;
        detail.setQuantity(weight);        //detail.setPrice(BigDecimal.valueOf());
        //detail.setNote();
        //detail.setUomId(productRoutingDTO.getUomDTO().getId());
        detail.setTotalPrice(null);
        inventoriesDTO.setInventoriesDetails(List.of(detail));


        //inventoriesDTO.setTotalAmount(new BigDecimal("15144129"));
        inventoriesDTO.setTotalQuantity(weight);
        inventoriesDTO.setIsReview(true);
        return inventoriesDTO;
    }

    /**
     * Partially update a productRouting.
     *
     * @param productRoutingDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<ProductRoutingDTO> partialUpdate(ProductRoutingDTO productRoutingDTO) {
        log.debug("Request to partially update ProductRouting : {}", productRoutingDTO);
        return SecurityUtils.getUserJWTDetail().flatMap(login -> {
            return productRoutingRepository
                    .findById(productRoutingDTO.getId(), login.getCompanyId())
                    .flatMap(existingProductRouting -> {
                        var oldManufactureId = Optional.ofNullable(existingProductRouting.getProductMaintain())
                                .map(ProductMaintain::getProductPackage)
                                .map(ProductPackage::getManufactureOrderId)
                                .orElseThrow(() -> new BadRequestAlertException("ManufactureOrderId not found", "ProductRouting", "NOT_FOUND"));
                        return manufactureOrderRepository.findByIdManufacture(oldManufactureId)
                                .flatMap(oldManufactureOrder -> {
                                    if (oldManufactureOrder == null) {
                                        return Mono.error(new BadRequestAlertException("ManufactureOrder not found", "ProductRouting", "NOT_FOUND"));
                                    }
                                    //                        if (!manufactureOrder.getStatus().equals(StatusEntity.SHIPPED)) {
                                    //                            return Mono.error(new BadRequestAlertException("ManufactureOrder is not WAREHOUSED", "ManufactureOrder", "STATUS_INVALID"));
                                    //                        }
                                    if (oldManufactureOrder.getProductMaintainId().equals(productRoutingDTO.getProductMaintainId())) {
                                        productRoutingMapper.partialUpdate(existingProductRouting, productRoutingDTO);
                                        existingProductRouting.setIsPersisted();
                                        return productRoutingRepository.save(existingProductRouting).flatMap(saved -> {
                                            return productRoutingRepository.findById(saved.getId(), login.getCompanyId())
                                                    .map(productRoutingMapper::toDto);
                                        });
                                    }
                                    oldManufactureOrder.setProductionRoutingId(null);
                                    oldManufactureOrder.setIsPersisted();
                                    return manufactureOrderRepository.save(oldManufactureOrder)
                                            .flatMap(savedOldManufactureOrder -> {
                                                return productMaintainRepository.findByIdAndIsDeletedIsFalse(productRoutingDTO.getProductMaintainId())
                                                        .flatMap(productMaintain -> {
                                                            var manufactureOrderId = productMaintain.getProductPackage().getManufactureOrderId();
                                                            if (manufactureOrderId == null) {
                                                                return Mono.error(new BadRequestAlertException("ManufactureOrderId not found", "ProductRouting", "NOT_FOUND"));
                                                            }
                                                            return manufactureOrderRepository.findByIdManufacture(manufactureOrderId)
                                                                    .flatMap(newManufactureOrder -> {
                                                                        if (newManufactureOrder == null) {
                                                                            return Mono.error(new BadRequestAlertException("ManufactureOrder not found", "ProductRouting", "NOT_FOUND"));
                                                                        }
//                                        if (!newManufactureOrder.getStatus().equals(StatusEntity.SHIPPED)) {
//                                            return Mono.error(new BadRequestAlertException("ManufactureOrder is not WAREHOUSED", "ManufactureOrder", "STATUS_INVALID"));
//                                        }
                                                                        newManufactureOrder.setProductionRoutingId(existingProductRouting.getId());
                                                                        newManufactureOrder.setIsPersisted();
                                                                        return manufactureOrderRepository.save(newManufactureOrder)
                                                                                .flatMap(savedManufactureOrder -> {
                                                                                    productRoutingMapper.partialUpdate(existingProductRouting, productRoutingDTO);
                                                                                    existingProductRouting.setIsPersisted();
                                                                                    return productRoutingRepository.save(existingProductRouting)
                                                                                            .map(productRoutingMapper::toDto).doOnError(e -> e.printStackTrace());
                                                                                });
                                                                    });
                                                        });
                                            });
                                });

                    });
        });
    }

    /**
     * Update a productRouting.
     *
     * @param productRoutingDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ProductRoutingDTO> update(ProductRoutingDTO productRoutingDTO) {
        log.debug("Request to update ProductRouting : {}", productRoutingDTO);
        return productRoutingRepository
                .save(productRoutingMapper.toEntity(productRoutingDTO).setIsPersisted())
                .map(productRoutingMapper::toDto);
    }

    /**
     * Get all the productRoutings.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<ProductRoutingDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ProductRoutings");
        return productRoutingRepository.findAllBy(pageable).map(productRoutingMapper::toDto);
    }

    /**
     * Returns the number of productRoutings available.
     *
     * @return the number of entities in the database.
     */
    public Mono<Long> countAll() {
        return productRoutingRepository.count();
    }

    /**
     * Get one productRouting by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<ProductRoutingDTO> findOne(UUID id) {
        log.debug("Request to get ProductRouting : {}", id);
        return SecurityUtils.getUserJWTDetail().flatMap(login -> {
            return productRoutingRepository.findById(id, login.getCompanyId())
                    .map(productRoutingMapper::toDto)
                    .flatMap(e -> {
                        List<UUID> materialIds = new ArrayList<>();
                        if (e.getUnit() != null) {
                            UUID materialId = UUID.fromString(e.getUnit());
                            materialIds.add(materialId);
                        }

                        return logisticClient.getUomByListIds(materialIds)
                                .collectList()
                                .flatMap(items -> {
                                    if (!items.isEmpty()) {
                                        UomDTO uomDTO = items.get(0);
                                        e.setUomDTO(uomDTO);
                                    }
                                    if(e.getStorageId() == null) {
                                        return Mono.just(e);
                                    }
                                    return logisticClient.getWarehousesByListIds(List.of(e.getStorageId()))
                                            .flatMap(storage -> {
                                                if (storage != null && storage.getData() != null && !storage.getData().isEmpty() && storage.getData().get(0) != null) {
                                                    e.setWarehouseDTO(storage.getData().get(0));
                                                }
                                                return Mono.just(e);
                                            })
                                            .switchIfEmpty(Mono.just(e));
                                });
                    })
                    .flatMap(e -> {
                        if (e.getId() != null) {
                            return manufactureOrderRepository.findByProductRoutingId(e.getId())
                                    .map(manufactureOrderMapper::toDto)
                                    .map(manuOrder -> {
                                        if (manuOrder == null) {
                                            return e;
                                        }
                                        e.setManufactureOrder(manuOrder);
                                        return e;
                                    })
                                    .switchIfEmpty(Mono.just(e));
                        }
                        return Mono.just(e);
                    });
        });
    }


    /**
     * Delete the productRouting by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(UUID id) {
        log.debug("Request to delete ProductRouting : {}", id);
        return productRoutingRepository.findById(id)
                .switchIfEmpty(Mono.error(new EntityNotFoundException("ProductRouting not found with id: " + id)))
                .flatMap(existingProductRouting -> productRoutingRepository.softDeleteById(id));
    }

    public Mono<Long> countAllByQuery(ProductRoutingQuery ro) {
        log.debug("Request to count all QualityCheckSamples by query: {}", ro);
        return productRoutingRepository.countByFilter(ro);
    }


    public Flux<ProductRoutingDTO> findAllByQuery(ProductRoutingQuery query, Pageable pageable) {
        log.debug("Request to get all ProductRouting by query: {}", query);

        // Lấy danh sách ProductRoutingDTO từ repository
        return productRoutingRepository.findAllByFilter(query, pageable)
                .map(productRoutingMapper::toDto)
                .collectList()
                .flatMapMany(dtos -> {
                    if (dtos.isEmpty()) {
                        return Flux.empty();
                    }
                    List<UUID> uomIds = dtos.stream()
                            .map(ProductRoutingDTO::getUnit)
                            .distinct()
                            .map(UUID::fromString)
                            .collect(Collectors.toList());

                    List<UUID> storageIds = dtos.stream()
                            .map(ProductRoutingDTO::getStorageId)
                            .distinct()
                            .collect(Collectors.toList());
                    return logisticClient.getUomByListIds(uomIds)
                            .collectList()
                            .flatMapMany(uomList -> {
                                if (!uomList.isEmpty()) {
                                    Map<UUID, UomDTO> uomMap = uomList.stream()
                                            .collect(Collectors.toMap(UomDTO::getId, Function.identity()));

                                    dtos.forEach(dto -> {
                                        dto.setUomDTO(uomMap.get(UUID.fromString(dto.getUnit())));
                                    });
                                }

                                return logisticClient.getWarehousesByListIds(storageIds)
                                        .flatMapMany(storageList -> {
                                            var listWarehouseDto = new ArrayList<WarehouseDTO>();
                                            if (storageList != null && storageList.getData() != null) {
                                                listWarehouseDto = new ArrayList<>(storageList.getData());

                                            }
                                            if (!listWarehouseDto.isEmpty()) {
                                                Map<UUID, WarehouseDTO> storageMap = listWarehouseDto.stream()
                                                        .collect(Collectors.toMap(WarehouseDTO::getId, Function.identity()));

                                                dtos.forEach(dto -> {
                                                    dto.setWarehouseDTO(storageMap.get((dto.getStorageId())));
                                                });
                                            }
                                            return Flux.fromIterable(dtos); // Trả về danh sách DTO đã hoàn chỉnh
                                        });
                            });
                }).doOnError(e -> e.printStackTrace());
    }


}
