package com.masi.logistics.service;

import com.carevn.masi.dto.ApiResponse;
import com.carevn.masi.utils.CSV.CSVUtils;
import com.carevn.masi.utils.JsonMapperService;
import com.carevn.masi.utils.SecurityUtils;
import com.masi.logistics.domain.*;
import com.masi.logistics.domain.criteria.AssetTransferDetailsCriteria;
import com.masi.logistics.domain.criteria.InventoriesStorageCriteria;
import com.masi.logistics.domain.criteria.ItemAssetDepreciationDetailCriteria;
import com.masi.logistics.domain.enumeration.ItemStatus;
import com.masi.logistics.repository.*;
import com.masi.logistics.security.AuthoritiesConstants;
import com.masi.logistics.service.dto.*;
import com.masi.logistics.service.exportDTO.Inventories_storage_DTO_Export;
import com.masi.logistics.service.mapper.*;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

import com.masi.logistics.service.request.MaterialManuFactureRequest;
import com.masi.logistics.web.rest.errors.BadRequestAlertException;
import io.r2dbc.postgresql.codec.Json;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.StringFilter;
import tech.jhipster.service.filter.UUIDFilter;

/**
 * Service Implementation for managing {@link InventoriesStorage}.
 */
@Service
@Transactional
public class InventoriesStorageService {

    private static final Logger log = LoggerFactory.getLogger(InventoriesStorageService.class);

    private final InventoriesStorageRepository inventoriesStorageRepository;

    private final InventoriesStorageMapper inventoriesStorageMapper;
    private final InventoriesRepository inventoriesRepository;
    private final InventoriesDetailRepository inventoriesDetailRepository;
    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;
    private final ItemInfoRepository itemInfoRepository;
    private final ItemInfoMapper itemInfoMapper;
    private final WarehouseRepository warehouseRepository;
    private final WarehouseMapper warehouseMapper;
    private final AssetTransferDetailsRepository assetTransferDetailsRepository;
    private final AssetTransferDetailsMapper assetTransferDetailsMapper;
    private final ItemSubCategoryRepository itemSubCategoryRepository;
    private final ItemSubCategoryMapper itemSubCategoryMapper;
    private final ItemAssetDepreciationDetailRepository itemAssetDepreciationDetailRepository;
    private final ItemAssetDepreciationDetailMapper itemAssetDepreciationDetailMapper;
    private final ItemAssetTransferRepository itemAssetTransferRepository;
    private final ItemLiquidationDetailRepository itemLiquidationDetailRepository;
    private final ItemLiquidationDetailMapper itemLiquidationDetailMapper;
    private final ItemLiquidationRepository itemLiquidationRepository;
    private final ItemLiquidationMapper itemLiquidationMapper;
    private final ItemAssetDepreciationRepository itemAssetDepreciationRepository;
    private final ItemAssetDepreciationMapper itemAssetDepreciationMapper;
    private final ItemAssetTransferMapper itemAssetTransferMapper;
    private final InventoriesService inventoriesService;
    private final ItemService itemService;

    public InventoriesStorageService(
            InventoriesStorageRepository inventoriesStorageRepository,
            InventoriesStorageMapper inventoriesStorageMapper,
            InventoriesRepository inventoriesRepository, InventoriesDetailRepository inventoriesDetailRepository, ItemRepository itemRepository, ItemMapper itemMapper, ItemInfoRepository itemInfoRepository, ItemInfoMapper itemInfoMapper, WarehouseRepository warehouseRepository, WarehouseMapper warehouseMapper, AssetTransferDetailsRepository assetTransferDetailsRepository, AssetTransferDetailsMapper assetTransferDetailsMapper, ItemSubCategoryRepository itemSubCategoryRepository, ItemSubCategoryMapper itemSubCategoryMapper, ItemAssetDepreciationDetailRepository itemAssetDepreciationDetailRepository, ItemAssetDepreciationDetailMapper itemAssetDepreciationDetailMapper, ItemAssetTransferRepository itemAssetTransferRepository, ItemLiquidationDetailRepository itemLiquidationDetailRepository, ItemLiquidationDetailMapper itemLiquidationDetailMapper,
            ItemLiquidationRepository itemLiquidationRepository, ItemLiquidationMapper itemLiquidationMapper,
            ItemAssetDepreciationRepository itemAssetDepreciationRepository, ItemAssetDepreciationMapper itemAssetDepreciationMapper, ItemAssetTransferMapper itemAssetTransferMapper,
            @Lazy InventoriesService inventoriesService, ItemService itemService) {
        this.inventoriesStorageRepository = inventoriesStorageRepository;
        this.inventoriesStorageMapper = inventoriesStorageMapper;
        this.inventoriesRepository = inventoriesRepository;
        this.inventoriesDetailRepository = inventoriesDetailRepository;
        this.itemRepository = itemRepository;
        this.itemMapper = itemMapper;
        this.itemInfoRepository = itemInfoRepository;
        this.itemInfoMapper = itemInfoMapper;
        this.warehouseRepository = warehouseRepository;
        this.warehouseMapper = warehouseMapper;
        this.assetTransferDetailsRepository = assetTransferDetailsRepository;
        this.assetTransferDetailsMapper = assetTransferDetailsMapper;
        this.itemSubCategoryRepository = itemSubCategoryRepository;
        this.itemSubCategoryMapper = itemSubCategoryMapper;
        this.itemAssetDepreciationDetailRepository = itemAssetDepreciationDetailRepository;
        this.itemAssetDepreciationDetailMapper = itemAssetDepreciationDetailMapper;
        this.itemAssetTransferRepository = itemAssetTransferRepository;
        this.itemLiquidationDetailRepository = itemLiquidationDetailRepository;
        this.itemLiquidationDetailMapper = itemLiquidationDetailMapper;
        this.itemLiquidationRepository = itemLiquidationRepository;
        this.itemLiquidationMapper = itemLiquidationMapper;
        this.itemAssetDepreciationRepository = itemAssetDepreciationRepository;
        this.itemAssetDepreciationMapper = itemAssetDepreciationMapper;
        this.itemAssetTransferMapper = itemAssetTransferMapper;
        this.inventoriesService = inventoriesService;
        this.itemService = itemService;
    }

    /**
     * Save a inventoriesStorage.
     *
     * @param inventoriesStorageDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<InventoriesStorageDTO> save(InventoriesStorageDTO inventoriesStorageDTO) {
        log.debug("Request to save InventoriesStorage : {}", inventoriesStorageDTO);

        return inventoriesStorageRepository
                .save(inventoriesStorageMapper.toEntity(inventoriesStorageDTO))
                .map(inventoriesStorageMapper::toDto)
                .flatMap(dto -> {
                    if (inventoriesStorageDTO.getItemInfo() == null) {
                        return Mono.just(dto);
                    }
                    inventoriesStorageDTO.getItemInfo().setId(UUID.randomUUID());
                    inventoriesStorageDTO.getItemInfo().setInventoryStorageId(dto.getId());

                    if (inventoriesStorageDTO.getItemInfo().getIncludedAccessories() != null) {
                        try {
                            Json attribute = inventoriesStorageDTO.getItemInfo().getAttribute();

                            Json includedAccessoriesJson = JsonMapperService.convertListToJson(inventoriesStorageDTO.getItemInfo().getIncludedAccessories(), IncludedAccessoriesDTO.class);

                            Json mergedJson = (attribute != null)
                                    ? JsonMapperService.mergeJson(includedAccessoriesJson, attribute)
                                    : includedAccessoriesJson;
                            inventoriesStorageDTO.getItemInfo().setAttribute(mergedJson);
                        } catch (Exception e) {
                        }
                    }
                    return itemInfoRepository
                            .save(itemInfoMapper.toEntity(inventoriesStorageDTO.getItemInfo()))
                            .thenReturn(dto);
                });
    }


    /**
     * Update a inventoriesStorage.
     *
     * @param inventoriesStorageDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<InventoriesStorageDTO> update(InventoriesStorageDTO inventoriesStorageDTO) {
        log.debug("Request to update InventoriesStorage : {}", inventoriesStorageDTO);
        return inventoriesStorageRepository
                .save(inventoriesStorageMapper.toEntity(inventoriesStorageDTO).setIsPersisted())
                .map(inventoriesStorageMapper::toDto);
    }

    /**
     * Partially update a inventoriesStorage.
     *
     * @return the persisted entity.
     */


    public Mono<Void> saveItemInfo(List<UUID> inventoriesStorageId) {
        log.debug("Request to save ItemInfo for InventoriesStorage : {}", inventoriesStorageId);
        return Flux.fromIterable(inventoriesStorageId)
                .flatMap(id -> {
                    ItemInfo itemInfo = new ItemInfo();
                    itemInfo.setId(UUID.randomUUID());
                    itemInfo.setInventoryStorageId(id);
                    return itemInfoRepository.save(itemInfo);
                })
                .then();
    }


    public Mono<InventoriesStorageDTO> partialUpdate(InventoriesStorageDTO inventoriesStorageDTO) {
        log.debug("Request to partially update InventoriesStorage : {}", inventoriesStorageDTO);

        return inventoriesStorageRepository
                .findById(inventoriesStorageDTO.getId())
                .map(existingInventoriesStorage -> {
                    if (inventoriesStorageDTO.getItemInfo() != null &&
                            inventoriesStorageDTO.getItemInfo().getUsageDate() != null &&
                            existingInventoriesStorage.getPrice() != null &&
                            !(existingInventoriesStorage.getPrice().compareTo(inventoriesStorageDTO.getPrice()) == 0)) {
                        throw new BadRequestAlertException("Price cannot be updated for items with a usage date",
                                "InventoriesStorage",
                                "price_update_not_allowed");
                    }

                    inventoriesStorageMapper.partialUpdate(existingInventoriesStorage, inventoriesStorageDTO);
                    var oldLogs = existingInventoriesStorage.getAssetLogs();
                    var newLogs = new InventoriesStorage.Transaction(
                            InventoriesStorage.TransactionEnum.UPDATE,
                            ZonedDateTime.now(),
                            inventoriesStorageDTO.getCode(),
                            "",
                            BigDecimal.ZERO,
                            inventoriesStorageDTO.getPrice(),
                            "",
                            "",
                            inventoriesStorageDTO.getNotes()
                    );
                    try {
                        InventoriesStorageDTO.addTransaction(oldLogs.asString(), newLogs);
                    } catch (Exception e) {
                        log.error("Error adding transaction to asset logs: {}", e.getMessage(), e);
                    }

                    existingInventoriesStorage.setIsPersisted();
                    return existingInventoriesStorage;
                })
                .flatMap(inventoriesStorageRepository::save)
                .map(inventoriesStorageMapper::toDto)
                .flatMap(dto -> {
                    if (inventoriesStorageDTO.getItemInfo() == null) {
                        return Mono.just(dto);
                    }

                    return itemInfoRepository
                            .changeIsDeletedByInventoriesId(inventoriesStorageDTO.getId(), true)
                            .then(Mono.defer(() -> {
                                inventoriesStorageDTO.getItemInfo().setId(UUID.randomUUID());
                                inventoriesStorageDTO.getItemInfo().setInventoryStorageId(dto.getId());
                                if (inventoriesStorageDTO.getItemInfo().getIncludedAccessories() != null) {
                                    try {
                                        Json attribute = inventoriesStorageDTO.getItemInfo().getAttribute();
                                        Json includedAccessoriesJson = JsonMapperService.convertListToJson(
                                                inventoriesStorageDTO.getItemInfo().getIncludedAccessories(),
                                                IncludedAccessoriesDTO.class
                                        );
                                        Json mergedJson = (attribute != null)
                                                ? JsonMapperService.mergeJson(includedAccessoriesJson, attribute)
                                                : includedAccessoriesJson;

                                        inventoriesStorageDTO.getItemInfo().setAttribute(mergedJson);
                                    } catch (Exception e) {
                                        log.debug("Error merging JSON for item info: {}", e.getMessage(), e);
                                    }
                                }
                                return itemInfoRepository
                                        .save(itemInfoMapper.toEntity(inventoriesStorageDTO.getItemInfo()))
                                        .thenReturn(dto);
                            }));
                });
    }


    /**
     * Find inventoriesStorages by Criteria.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<InventoriesStorageDTO> findByCriteria(InventoriesStorageCriteria criteria, Pageable pageable) {
        log.debug("Request to get all InventoriesStorages by Criteria");

        return inventoriesStorageRepository.findByCriteria(criteria, pageable)
                .map(inventoriesStorageMapper::toDto)
//                .collectList()
//                .flatMapMany(inventoriesStorageDTOS -> {
//                    if (inventoriesStorageDTOS.isEmpty()) {
//                        return Flux.empty();
//                    }
//                    List<UUID> ids = inventoriesStorageDTOS.stream()
//                            .map(InventoriesStorageDTO::getItemId)
//                            .distinct()
//                            .toList();
//                    return itemRepository.findAllById(ids)
//                            .map(itemMapper::toDto)
//                            .collectMap(ItemDTO::getId, Function.identity())
//                            .flatMapMany(itemDTOMap -> {
//                                inventoriesStorageDTOS.forEach(inventoriesStorageDTO -> {
//                                    ItemDTO itemDTO = itemDTOMap.get(inventoriesStorageDTO.getItemId());
//                                    inventoriesStorageDTO.setItem(itemDTO);
//                                });
//                                return Flux.fromIterable(inventoriesStorageDTOS);
//                            });
//                })
                ;
    }

    public Flux<InventoriesStorageDTO> findByCriteriaDepreciation() {
        InventoriesStorageCriteria criteria = new InventoriesStorageCriteria();
        criteria.setCheckDepreciation(true);

        return SecurityUtils.getUserJWTDetail().flatMapMany(user -> {
            if (!user.getAuthorities().contains(AuthoritiesConstants.SUPER_ADMIN)) {
                var company = (criteria.getCompany() == null ? new StringFilter() : criteria.getCompany()).getEquals();
                if (company == null) {
                    criteria.company().setEquals(user.getCompanyId());
                }
            } else {
                criteria.company().setEquals(user.getCompanyId());
            }
            StringFilter statusFilter = new StringFilter();
            statusFilter.setDoesNotContain(ItemStatus.DEPRECIATION.toString());
            criteria.setStatusDepreciation(statusFilter);

            return this.findByCriteriaDepreciation(criteria, PageRequest.of(0, Integer.MAX_VALUE), true);
        });
    }

    public Flux<InventoriesStorageDTO> findByCriteriaFishMeal(InventoriesStorageCriteria criteria, Pageable pageable, Boolean calculateDepreciationThisMonth) {
        log.debug("Request to get all InventoriesStorages by Criteria");
        StringFilter statusFilter = new StringFilter();
        statusFilter.setDoesNotContain(ItemStatus.DEPRECIATION.toString());
        criteria.setStatusDepreciation(statusFilter);
        criteria.setCheckFishMeal(true);
        return SecurityUtils.getUserJWTDetail().flatMapMany(user -> {
            if (!user.getAuthorities().contains(AuthoritiesConstants.SUPER_ADMIN)) {
                StringFilter companyFilter = criteria.getCompany() == null ? new StringFilter() : criteria.getCompany();
                if (companyFilter.getEquals() == null) {
                    companyFilter.setEquals(user.getCompanyId());
                    criteria.setCompany(companyFilter);
                }
            } else if (criteria.getCompany() == null || criteria.getCompany().getEquals() == null) {
                criteria.company().setEquals(user.getCompanyId());
            }
            return inventoriesStorageRepository.findByCriteria(criteria, pageable)
                    .map(inventoriesStorageMapper::toDto)
                    .collectList()
                    .flatMapMany(dtoList -> {
                        if (dtoList.isEmpty()) {
                            return Flux.empty();
                        }
                        List<UUID> inventoryIds = dtoList.stream()
                                .map(InventoriesStorageDTO::getId)
                                .toList();

                        return itemInfoRepository.findByInventoryStorageIdInAndIsDeleted(inventoryIds, false)
                                .map(itemInfoMapper::toDto)
                                .collectMultimap(ItemInfoDTO::getInventoryStorageId)
                                .flatMapMany(itemInfoMap -> {
                                    dtoList.forEach(dto -> {
                                        Collection<ItemInfoDTO> itemInfos = itemInfoMap.getOrDefault(dto.getId(), List.of());
                                        dto.setItemInfo(itemInfos.stream().findFirst().orElse(null)); // Chọn `ItemInfo` đầu tiên
                                    });
                                    return Flux.fromIterable(dtoList);
                                });
                    })
                    ;
        });
    }


    public Flux<InventoriesStorageDTO> findByCriteriaDepreciation(InventoriesStorageCriteria criteria, Pageable pageable, Boolean calculateDepreciationThisMonth) {
        log.debug("Request to get all InventoriesStorages by Criteria");
        StringFilter statusFilter = new StringFilter();
        statusFilter.setDoesNotContain(ItemStatus.DEPRECIATION.toString());
        criteria.setStatusDepreciation(statusFilter);

        return inventoriesStorageRepository.findByCriteria(criteria, pageable)
                .map(inventoriesStorageMapper::toDto)
                .collectList()
                .flatMapMany(dtoList -> {
                    if (dtoList.isEmpty()) {
                        return Flux.empty();
                    }
                    List<UUID> inventoryIds = dtoList.stream()
                            .map(InventoriesStorageDTO::getId)
                            .toList();

                    return itemInfoRepository.findByInventoryStorageIdInAndIsDeleted(inventoryIds, false)
                            .map(itemInfoMapper::toDto)
                            .collectMultimap(ItemInfoDTO::getInventoryStorageId)
                            .flatMapMany(itemInfoMap -> {
                                dtoList.forEach(dto -> {
                                    Collection<ItemInfoDTO> itemInfos = itemInfoMap.getOrDefault(dto.getId(), List.of());
                                    dto.setItemInfo(itemInfos.stream().findFirst().orElse(null));
                                });
                                return Flux.fromIterable(dtoList);
                            });
                })
                .map(dto -> {
                    if (calculateDepreciationThisMonth) {
                        return this.handleDepreciationThisMonth(dto);
                    }
                    return this.handleDepreciation(dto);
                });
    }

    private InventoriesStorageDTO handleDepreciationThisMonth(InventoriesStorageDTO dto) {
        if (dto.getItemInfo() == null ||
                dto.getItemInfo().getMonthOfUse() == null ||
                dto.getItemInfo().getMonthOfUse() <= 0) {
            dto.setDepreciationValue(BigDecimal.ZERO);
            dto.setDepreciationRate(BigDecimal.ZERO);
            dto.setAccumulated(BigDecimal.ZERO);
            return dto;
        }
        BigDecimal price = dto.getPrice() != null ? dto.getPrice() : BigDecimal.ZERO;
        BigDecimal remainingPrice = dto.getRemainingPrice() != null ? dto.getRemainingPrice() : BigDecimal.ZERO;


        // Giá trị khấu hao: Giá ban đầu / Số tháng sử dụng
        BigDecimal depreciation = price.divide(BigDecimal.valueOf(dto.getItemInfo().getMonthOfUse()), MathContext.DECIMAL128);

        // Giá trị lũy kế: Giá ban đầu - Giá còn lại +  giá trị khấu hao
        BigDecimal accumulated = price.subtract(remainingPrice).add(depreciation);

        // Tỉ lệ khấu hao: accumulated / price * 100 (nếu price > 0)
        BigDecimal depreciationRate = price.compareTo(BigDecimal.ZERO) > 0
                ? accumulated.divide(price, MathContext.DECIMAL128).multiply(BigDecimal.valueOf(100))
                : BigDecimal.ZERO;

        // Gán kết quả vào DTO
        dto.setDepreciationValue(depreciation);
        dto.setDepreciationRate(depreciationRate);

        dto.setAccumulated(accumulated);

        return dto;
    }

    private InventoriesStorageDTO handleDepreciation(InventoriesStorageDTO dto) {
        if (dto.getItemInfo() == null ||
                dto.getItemInfo().getMonthOfUse() == null ||
                dto.getItemInfo().getMonthOfUse() <= 0) {
            dto.setDepreciationValue(BigDecimal.ZERO);
            dto.setDepreciationRate(BigDecimal.ZERO);
            dto.setAccumulated(BigDecimal.ZERO);
            return dto;
        }
        BigDecimal price = dto.getPrice() != null ? dto.getPrice() : BigDecimal.ZERO;
        BigDecimal remainingPrice = dto.getRemainingPrice() != null ? dto.getRemainingPrice() : BigDecimal.ZERO;
        // Giá trị lũy kế: Giá ban đầu - Giá còn lại
        BigDecimal accumulated = price.subtract(remainingPrice);

        // Giá trị khấu hao: Giá ban đầu / Số tháng sử dụng
        BigDecimal depreciation = dto.getItemInfo().getMonthOfUse() > 0
                ? price.divide(BigDecimal.valueOf(dto.getItemInfo().getMonthOfUse()), MathContext.DECIMAL128)
                : BigDecimal.ZERO;

        // Tỉ lệ khấu hao: accumulated / price * 100 (nếu price > 0)
        BigDecimal depreciationRate = price.compareTo(BigDecimal.ZERO) > 0
                ? accumulated.divide(price, MathContext.DECIMAL128).multiply(BigDecimal.valueOf(100))
                : BigDecimal.ZERO;

        // Gán kết quả vào DTO
        dto.setDepreciationValue(depreciation);
        dto.setDepreciationRate(depreciationRate);
        dto.setAccumulated(accumulated);

        return dto;
    }


    /**
     * Find the count of inventoriesStorages by criteria.
     *
     * @param criteria filtering criteria
     * @return the count of inventoriesStorages
     */
    public Mono<Long> countByCriteria(InventoriesStorageCriteria criteria) {
        log.debug("Request to get the count of all InventoriesStorages by Criteria");
        return inventoriesStorageRepository.countByCriteria(criteria);
    }

    public Mono<Long> fakeCountByCriteria(InventoriesStorageCriteria criteria) {
        return Mono.just(10L);
    }

    /**
     * Returns the number of inventoriesStorages available.
     *
     * @return the number of entities in the database.
     */
    public Mono<Long> countAll() {
        return inventoriesStorageRepository.count();
    }

    private ItemAssetDepreciationDetailCriteria getItemAssetDepreciationDetailCriteria(InventoriesStorageDTO dto) {
        ItemAssetDepreciationDetailCriteria itemAssetDepreciationDetailCriteria = new ItemAssetDepreciationDetailCriteria();
        UUIDFilter inventoriesStorageId = new UUIDFilter();
        inventoriesStorageId.setEquals(dto.getId());
        itemAssetDepreciationDetailCriteria.setInventoriesStorageId(inventoriesStorageId);
        BooleanFilter isDeleted = new BooleanFilter();
        isDeleted.setEquals(false);
        itemAssetDepreciationDetailCriteria.setIsDeleted(isDeleted);
        return itemAssetDepreciationDetailCriteria;
    }

    private AssetTransferDetailsCriteria getAssetTransferDetailsCriteria(InventoriesStorageDTO dto) {
        AssetTransferDetailsCriteria assetTransferDetailsCriteria = new AssetTransferDetailsCriteria();
        UUIDFilter inventoriesStorageId = new UUIDFilter();
        inventoriesStorageId.setEquals(dto.getId());
        assetTransferDetailsCriteria.setInventoriesStorageId(inventoriesStorageId);
        BooleanFilter isDeleted = new BooleanFilter();
        isDeleted.setEquals(false);
        assetTransferDetailsCriteria.setIsDeleted(isDeleted);
        return assetTransferDetailsCriteria;
    }

    /**
     * Get one inventoriesStorage by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<InventoriesStorageDTO> findOne(UUID id) {
        log.debug("Request to get InventoriesStorage : {}", id);

        return inventoriesStorageRepository
                .findById(id)
                .map(inventoriesStorageMapper::toDto)
                .flatMap(is -> {
                    AssetTransferDetailsCriteria assetTransferDetailsCriteria = getAssetTransferDetailsCriteria(is);
                    return assetTransferDetailsRepository.findByCriteria(assetTransferDetailsCriteria, null)
                            .collectList()
                            .map(assetTransferDetailsMapper::toDto)
                            .map(assetTransferDetails -> {
                                Map<ItemAssetTransferDTO, List<AssetTransferDetailsDTO>> groupedByItemAssetTransfer =
                                        assetTransferDetails.stream()
                                                .filter(detail -> detail.getItemAssetTransfer() != null) // Lọc bỏ các mục không có itemAssetTransfer
                                                .collect(Collectors.groupingBy(AssetTransferDetailsDTO::getItemAssetTransfer));
                                is.setItemAssetTransfers(groupedByItemAssetTransfer.keySet());
                                return is;
                            });
                })
                .flatMap(dto -> itemInfoRepository.findByInventoryStorageIdAndIsDeleted(dto.getId(), false)
                        .map(itemInfo -> {
                            dto.setItemInfo(itemInfoMapper.toDto(itemInfo));
                            return dto;
                        })
                        .flatMap(dtoInfo -> {
                            return itemSubCategoryRepository.findById(dtoInfo.getItemInfo().getItemSubCategoryId() == null ? UUID.fromString("00000000-0000-0000-0000-000000000000") : dtoInfo.getItemInfo().getItemSubCategoryId())
                                    .map(itemSubCategoryMapper::toDto)
                                    .map(itemSubCategory -> {
                                        dtoInfo.getItemInfo().setItemSubCategory(itemSubCategory);
                                        return dtoInfo;
                                    });
                        })
                        .flatMap(dtoInfo -> {
                            Json attribute = dtoInfo.getItemInfo().getAttribute();
                            if (attribute != null) {
                                try {
                                    // Convert JSON to IncludedAccessoriesDTO list
                                    List<IncludedAccessoriesDTO> includedAccessories = JsonMapperService.convertJsonToList(
                                            attribute, IncludedAccessoriesDTO.class, "IncludedAccessoriesDTO");
                                    dtoInfo.getItemInfo().setIncludedAccessories(includedAccessories);

                                    // Extract item and warehouse IDs
                                    List<UUID> itemIds = includedAccessories.stream()
                                            .map(IncludedAccessoriesDTO::getItemId)
                                            .distinct()
                                            .toList();
                                    List<UUID> warehouseIds = includedAccessories.stream()
                                            .map(IncludedAccessoriesDTO::getWarehouseId)
                                            .distinct()
                                            .toList();

                                    // Fetch related items and warehouses
                                    return Mono.zip(
                                            itemRepository.findAllByIdIn(itemIds)
                                                    .map(itemMapper::toDto)
                                                    .collectList(),
                                            warehouseRepository.findAllByIdIn(warehouseIds)
                                                    .map(warehouseMapper::toDto)
                                                    .collectList()
                                    ).map(tuple -> {
                                        List<ItemDTO> items = tuple.getT1();
                                        List<WarehouseDTO> warehouses = tuple.getT2();

                                        // Map items and warehouses to includedAccessories
                                        includedAccessories.forEach(accessory -> {
                                            ItemDTO item = items.stream()
                                                    .filter(i -> i.getId().equals(accessory.getItemId()))
                                                    .findFirst()
                                                    .orElse(null);
                                            accessory.setItem(item);

                                            WarehouseDTO warehouse = warehouses.stream()
                                                    .filter(w -> w.getId().equals(accessory.getWarehouseId()))
                                                    .findFirst()
                                                    .orElse(null);
                                            accessory.setWarehouse(warehouse);
                                        });

                                        return dtoInfo;
                                    });
                                } catch (Exception e) {
                                    log.error("Error converting JSON to list for item info: {}", e.getMessage(), e);
                                }
                            }
                            return Mono.just(dtoInfo);
                        })
                        .defaultIfEmpty(dto)
                )
                .flatMap(is -> {
                    ItemAssetDepreciationDetailCriteria itemAssetDepreciationDetailCriteria = getItemAssetDepreciationDetailCriteria(is);
                    return itemAssetDepreciationDetailRepository.findByCriteria(itemAssetDepreciationDetailCriteria, null)
                            .collectList()
                            .map(itemAssetDepreciationDetailMapper::toDto)
                            .map(itemAssetDepreciationDetailDTOS -> {
                                is.setItemAssetDepreciationDetails(itemAssetDepreciationDetailDTOS);
                                return is;
                            });
                });
    }


    /**
     * Delete the inventoriesStorage by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(UUID id) {
        log.debug("Request to delete InventoriesStorage : {}", id);
        return inventoriesStorageRepository.changeIsDeletedById(id, true);
    }

    public Mono<List<InventoriesStorageDTO>> getAllItem(List<UUID> documentIds, UUID warehouseId) {
        if (documentIds.isEmpty()) {
            documentIds = new ArrayList<>(List.of(UUID.fromString("00000000-0000-0000-0000-000000000000")));
        }
        return inventoriesStorageRepository.findAllByItemIdAndWarehouseId(documentIds, warehouseId)
                .map(InventoriesStorage::toDto)
                .collectList();
    }

    public record InventoriesStorageTotalResponse(UUID itemCategoryId, String itemCategoryCode, String itemCategoryName,
                                                  List<InventoriesStorageTotal> inventoriesStorageTotals) {
    }

    public Mono<ApiResponse<InventoriesStorageTotal>> getAllItems(UUID idWarehouse, String companyId, Pageable pageable) {
        return inventoriesRepository.findAllByIncomingWarehouseIdAndCompanyId(idWarehouse, companyId)
                .map(Inventories::getId)
                .collectList()
                .flatMap(ids -> {
                    if (ids.isEmpty()) {
                        Mono<List<InventoriesStorageTotal>> data = Mono.just(new ArrayList<>());
                        Mono<Long> totalRecord = Mono.just(0L);

                        return ApiResponse.from(data, totalRecord);
                    }
                    return inventoriesDetailRepository.findAllByInventoriesIdIn(ids)
                            .map(InventoriesDetail::getId)
                            .distinct()
                            .collectList()
                            .flatMap(detailIds -> {
                                if (detailIds.isEmpty()) {
                                    Mono<List<InventoriesStorageTotal>> data = Mono.just(new ArrayList<>());
                                    Mono<Long> totalRecord = Mono.just(0L);
                                    return ApiResponse.from(data, totalRecord);
                                }
                                Mono<List<InventoriesStorageTotal>> data = inventoriesStorageRepository.findAllItem(detailIds, companyId, pageable)
                                        .collectList();

                                Mono<Long> totalRecord = inventoriesStorageRepository.findAllItemCount(detailIds, companyId);
                                return ApiResponse.from(data, totalRecord);
                            });
                });
    }


    public Mono<byte[]> exportRecordsAsCSV(InventoriesStorageCriteria criteria, Pageable pageable) {
        log.debug("Request to export Inventories as CSV");

        return this.findByCriteria(criteria, null)
                .map(Inventories_storage_DTO_Export::new)
                .collectList()
                .flatMap(dtoList -> {
                    log.debug("Converting Inventories list to CSV");

                    return CSVUtils.convertListToExcel(dtoList);
                });
    }

//    public Mono<Void> changeRemainingPrice(UUID inventoriesStorageId, BigDecimal amortizationAmount) {
//        return inventoriesStorageRepository.findById(inventoriesStorageId)
//                .map(inventoriesStorage -> {
//                    BigDecimal remainingPrice = inventoriesStorage.getRemainingPrice() != null
//                            ? inventoriesStorage.getRemainingPrice()
//                            : BigDecimal.ZERO;
//
//                    BigDecimal newRemainingPrice = remainingPrice.subtract(amortizationAmount);
//                    inventoriesStorage.setRemainingPrice(newRemainingPrice);
//                    inventoriesStorage.setIsPersisted();
//                    return inventoriesStorage;
//                })
//                .flatMap(inventoriesStorageRepository::save)
//                .then();
//    }

    public Mono<Void> changeRemainingPrice(UUID inventoriesStorageId, BigDecimal amortizationAmount) {
        return inventoriesStorageRepository.updateRemainingPriceById(inventoriesStorageId, amortizationAmount)
                .then();
    }

    public Mono<InventoriesStorageDTO> updateVolume(List<MaterialManuFactureRequest> materialManuFactureRequests) {
        return Flux.fromIterable(materialManuFactureRequests)
                .flatMap(request -> inventoriesStorageRepository.findByIdMapBrier(UUID.fromString(request.getProductionMaintainId()))
                        .flatMap(inventoriesStorage -> {
                            BigDecimal quantityUse = request.getQuantityUse() != null ? request.getQuantityUse() : BigDecimal.ZERO;

                            if (quantityUse.compareTo(inventoriesStorage.getQuantity()) > 0) {
                                return Mono.error(new IllegalArgumentException("Quantity use exceeds available inventory"));
                            }
                            BigDecimal updatedQuantity = inventoriesStorage.getQuantity().subtract(quantityUse);
                            inventoriesStorage.setQuantity(updatedQuantity);
                            inventoriesStorage.setIsPersisted();

                            return inventoriesStorageRepository.save(inventoriesStorage)
                                    .map(inventoriesStorageMapper::toDto);
                        }))
                .collectList()
                .mapNotNull(list -> list.stream().findFirst().orElse(null));
    }


    public Mono<InventoriesStorageDTO> updateProtein(UUID productId, UUID itemId, Float code) {
        log.debug("Request to update protein: {}", code);
        return itemService.findByIdOrCreateWhenIsExist(code)
                .flatMap(itemDTO -> {
                    return inventoriesStorageRepository.updateItemIdByProductId(productId, itemDTO.getId())
                            .then(Mono.just(new InventoriesStorageDTO()));
                })
                .then(Mono.just(new InventoriesStorageDTO()));

    }


    public Mono<Map<String, Object>> getArises(UUID id) {
        return inventoriesStorageRepository.findById(id)
                .switchIfEmpty(Mono.error(new BadRequestAlertException(
                        "InventoriesStorage not found",
                        "InventoriesStorage",
                        "notfound"
                )))
                .flatMap(inventoriesStorage -> {
                    Mono<List<ItemAssetDepreciationDetailDTO>> itemAssetDepreciationDetailMono =
                            itemAssetDepreciationDetailRepository.findByInventoryStorageIdAndIsDeleted(id, false)
                                    .map(itemAssetDepreciationDetailMapper::toDto)
                                    .collectList()
                                    .flatMap(dtos -> {
                                        List<UUID> itemIds = dtos.stream()
                                                .map(ItemAssetDepreciationDetailDTO::getItemAssetDepreciationId)
                                                .toList();
                                        return itemAssetDepreciationRepository.findAllById(itemIds)
                                                .collectList()
                                                .map(itemAssetDepreciationList -> itemAssetDepreciationList.stream()
                                                        .map(itemAssetDepreciationMapper::toDto)
                                                        .collect(Collectors.toList()))
                                                .flatMap(itemAsset -> {
                                                    dtos.forEach(dto -> dto.setItemAssetDepreciation(itemAsset.stream().filter(item -> item.getId().equals(dto.getItemAssetDepreciationId())).findFirst().orElse(null)));
                                                    return Mono.just(dtos);
                                                });
                                    });

                    Mono<List<AssetTransferDetailsDTO>> assetTransferDetailsMono =
                            assetTransferDetailsRepository.findByInventoryStorageIdAndIsDeleted(id, false)
                                    .map(assetTransferDetailsMapper::toDto)
                                    .collectList()
                                    .flatMap(dtos -> {
                                        List<UUID> itemIds = dtos.stream()
                                                .map(AssetTransferDetailsDTO::getItemAssetTransferId)
                                                .toList();

                                        return itemAssetTransferRepository.findAllById(itemIds)
                                                .collectList()
                                                .map(itemAssetDepreciationList -> itemAssetDepreciationList.stream()
                                                        .map(itemAssetTransferMapper::toDto)
                                                        .collect(Collectors.toList()))
                                                .flatMap(itemAsset -> {
                                                    dtos.forEach(dto -> dto.setItemAssetTransfer(itemAsset.stream().filter(item -> item.getId().equals(dto.getItemAssetTransferId())).findFirst().orElse(null)));
                                                    return Mono.just(dtos);
                                                });
                                    });

                    Mono<ItemLiquidationDetailDTO> itemLiquidationDetailMono =
                            itemLiquidationDetailRepository.findByInventoryStorageIdAndIsDeleted(id, false)
                                    .switchIfEmpty(Mono.error(new BadRequestAlertException(
                                            "ItemLiquidationDetail not found",
                                            "ItemLiquidationDetail",
                                            "notfound"
                                    )))
                                    .map(itemLiquidationDetailMapper::toDto)
                                    .flatMap(dto -> {
                                        if (dto == null) {
                                            return Mono.just(dto);
                                        }
                                        return itemLiquidationRepository.findById(dto.getItemLiquidationId())
                                                .mapNotNull(itemLiquidationMapper::toDto)
                                                .map(itemLiquidationDTO -> {
                                                    dto.setItemLiquidation(itemLiquidationDTO);
                                                    return dto;
                                                });
                                    })
                                    .doOnNext(e -> log.debug("ItemLiquidationDetail: {}", e))
                                    .onErrorResume(e -> {
                                        log.error("Error fetching ItemLiquidationDetail: {}", e.getMessage(), e);
                                        return Mono.empty();
                                    })
                                    .switchIfEmpty(Mono.just(new ItemLiquidationDetailDTO()))
                                    .collectList()
                                    .mapNotNull(list -> list.stream().findFirst().orElse(null));

                    return Mono.zip(itemAssetDepreciationDetailMono, assetTransferDetailsMono, itemLiquidationDetailMono)
                            .map(tuple -> {
                                ArisesDTO arisesDTO = new ArisesDTO();
                                arisesDTO.setInventoriesStorage(inventoriesStorageMapper.toDto(inventoriesStorage));
                                arisesDTO.setItemAssetDepreciationDetail(tuple.getT1());
                                arisesDTO.setAssetTransferDetails(tuple.getT2());
                                arisesDTO.setItemLiquidationDetail(tuple.getT3());

                                Map<String, Object> result = new HashMap<>();
                                if (arisesDTO.getInventoriesStorage() != null) {
                                    result.put("InventoriesStorage", arisesDTO.getInventoriesStorage());
                                }
                                if (arisesDTO.getItemAssetDepreciationDetail() != null) {
                                    result.put("ItemAssetDepreciationDetail", arisesDTO.getItemAssetDepreciationDetail());
                                }
                                if (arisesDTO.getAssetTransferDetails() != null) {
                                    result.put("AssetTransferDetails", arisesDTO.getAssetTransferDetails());
                                }
                                if (arisesDTO.getItemLiquidationDetail() != null) {
                                    result.put("ItemLiquidationDetail", arisesDTO.getItemLiquidationDetail());
                                }

                                return result;
                            });
                });
    }


}
