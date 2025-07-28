package com.masi.logistics.service;

import com.carevn.masi.dto.EmployeeDTO;
import com.carevn.masi.dto.WorkspaceDTO;
import com.carevn.masi.utils.CSV.CSVUtils;
import com.masi.logistics.domain.InventoriesStorage;
import com.masi.logistics.domain.ItemAssetDepreciationDetail;
import com.masi.logistics.domain.criteria.InventoriesCriteria;
import com.masi.logistics.domain.criteria.ItemAssetDepreciationCriteria;
import com.masi.logistics.domain.criteria.ItemAssetDepreciationDetailCriteria;
import com.masi.logistics.domain.enumeration.StatusEntity;
import com.masi.logistics.domain.enumeration.WarehouseGroupType;
import com.masi.logistics.repository.InventoriesStorageRepository;
import com.masi.logistics.repository.ItemAssetDepreciationDetailRepository;
import com.masi.logistics.repository.ItemAssetDepreciationRepository;
import com.masi.logistics.repository.RequestApprovalRepository;
import com.masi.logistics.service.dto.*;
import com.masi.logistics.service.exportDTO.*;
import com.masi.logistics.service.mapper.ContactGiftMapper;
import com.masi.logistics.service.mapper.ItemAssetDepreciationDetailMapper;
import com.masi.logistics.service.mapper.ItemAssetDepreciationMapper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.masi.logistics.service.mapper.RequestApprovalMapper;
import com.masi.logistics.service.web.client.EmployeeClient;
import com.masi.logistics.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.UUIDFilter;

/**
 * Service Implementation for managing {@link com.masi.logistics.domain.ItemAssetDepreciation}.
 */
@Service
@Transactional
public class ItemAssetDepreciationService {

    private static final Logger log = LoggerFactory.getLogger(ItemAssetDepreciationService.class);

    private final ItemAssetDepreciationRepository itemAssetDepreciationRepository;

    private final ItemAssetDepreciationMapper itemAssetDepreciationMapper;
    private final EmployeeClient employeeClient;
    private final ItemAssetDepreciationDetailRepository itemAssetDepreciationDetailRepository;
    private final ItemAssetDepreciationDetailMapper itemAssetDepreciationDetailMapper;
    private final InventoriesStorageService inventoriesStorageService;
    private final RequestApprovalRepository requestApprovalRepository;
    private final RequestApprovalMapper requestApprovalMapper;
    private final RequestApprovalService requestApprovalService;
    private final InventoriesStorageRepository inventoriesStorageRepository;

    public ItemAssetDepreciationService(
            ItemAssetDepreciationRepository itemAssetDepreciationRepository,
            ItemAssetDepreciationMapper itemAssetDepreciationMapper,
            EmployeeClient employeeClient, ItemAssetDepreciationDetailRepository itemAssetDepreciationDetailRepository, ItemAssetDepreciationDetailMapper itemAssetDepreciationDetailMapper, InventoriesStorageService inventoriesStorageService, RequestApprovalRepository requestApprovalRepository, RequestApprovalMapper requestApprovalMapper, RequestApprovalService requestApprovalService, InventoriesStorageRepository inventoriesStorageRepository) {
        this.itemAssetDepreciationRepository = itemAssetDepreciationRepository;
        this.itemAssetDepreciationMapper = itemAssetDepreciationMapper;
        this.employeeClient = employeeClient;
        this.itemAssetDepreciationDetailRepository = itemAssetDepreciationDetailRepository;
        this.itemAssetDepreciationDetailMapper = itemAssetDepreciationDetailMapper;
        this.inventoriesStorageService = inventoriesStorageService;
        this.requestApprovalRepository = requestApprovalRepository;
        this.requestApprovalMapper = requestApprovalMapper;
        this.requestApprovalService = requestApprovalService;
        this.inventoriesStorageRepository = inventoriesStorageRepository;
    }

    /**
     * Save a itemAssetDepreciation.
     *
     * @return the persisted entity.
     */
    private Mono<Boolean> checkInventoriesStorage(String code, LocalDate depreciationDate) {
        return itemAssetDepreciationRepository.findByCodeAndDepreciationDate(code, depreciationDate)
                .map(Objects::nonNull)
                .defaultIfEmpty(false);
    }


    public Mono<ItemAssetDepreciationDTO> save(ItemAssetDepreciationDTO itemAssetDepreciationDTO) {
        log.debug("Request to save ItemAssetDepreciation : {}", itemAssetDepreciationDTO);
        itemAssetDepreciationDTO.setStatus(StatusEntity.NEW);

        return this.checkInventoriesStorage(itemAssetDepreciationDTO.getCode(), itemAssetDepreciationDTO.getDepreciationDate())
                .flatMap(exists -> {
                    if (exists) {
                        return Mono.error(new BadRequestAlertException(
                                "Code and Depreciation Date already exists",
                                "ItemAssetDepreciation", "ItemAssetDepreciationExists"));
                    }

                    return itemAssetDepreciationRepository
                            .save(itemAssetDepreciationMapper.toEntity(itemAssetDepreciationDTO))
                            .map(itemAssetDepreciationMapper::toDto)
                            .flatMap(savedDTO -> inventoriesStorageService.findByCriteriaDepreciation()
                                    .map(InventoriesStorageDTO::toItemAssetDepreciationDTO)
                                    .collectList()
                                    .flatMap(defaultDetails -> {
                                        List<ItemAssetDepreciationDetailDTO> userDetails = itemAssetDepreciationDTO.getItemAssetDepreciationDetails();

                                        return Flux.fromIterable(defaultDetails)
                                                .flatMap(defaultDetail -> {
                                                    // Kiểm tra nếu có `id` trùng trong `userDetails`
                                                    ItemAssetDepreciationDetailDTO matchingUserDetail = userDetails.stream()
                                                            .filter(userDetail -> userDetail.getInventoriesStorageId() != null &&
                                                                    userDetail.getInventoriesStorageId().equals(defaultDetail.getInventoriesStorageId()))
                                                            .findFirst()
                                                            .orElse(null);

                                                    // Nếu tìm thấy, cập nhật dữ liệu từ `matchingUserDetail`
                                                    if (matchingUserDetail != null) {
                                                        defaultDetail.setAmortizedCostInformation(matchingUserDetail.getAmortizedCostInformation());
                                                        defaultDetail.setCostInformation(matchingUserDetail.getCostInformation());
                                                        defaultDetail.setAmortizationRate(matchingUserDetail.getAmortizationRate());
                                                        defaultDetail.setAccumulatedAmortizationAmount(matchingUserDetail.getAccumulatedAmortizationAmount());
                                                        defaultDetail.setRecipe(matchingUserDetail.getRecipe());
                                                        defaultDetail.setAmortizationAmount(matchingUserDetail.getAmortizationAmount());
                                                    }

                                                    // Gán `id` mới nếu `id` chưa tồn tại
                                                    if (defaultDetail.getId() == null) {
                                                        defaultDetail.setId(UUID.randomUUID());
                                                    }

                                                    defaultDetail.setItemAssetDepreciationId(savedDTO.getId());

                                                    // Lưu dữ liệu vào repository
                                                    return itemAssetDepreciationDetailRepository.save(
                                                            itemAssetDepreciationDetailMapper.toEntity(defaultDetail)
                                                    );
                                                })
                                                .then(Mono.just(savedDTO));
                                    }))
                            .flatMap(updatedDto -> {
                                if (itemAssetDepreciationDTO.getRequestApprovals() != null && !itemAssetDepreciationDTO.getRequestApprovals().isEmpty()) {
                                    return Flux.fromIterable(itemAssetDepreciationDTO.getRequestApprovals())
                                            .flatMap(requestApprovalDTO -> {
                                                CreateReviewRequest createReviewRequest = new CreateReviewRequest();
                                                createReviewRequest.setDocumentId(updatedDto.getId());
                                                createReviewRequest.setEmployeeIds(Collections.singleton(requestApprovalDTO.getEmployeeId()));
                                                return requestApprovalService.requestReview(createReviewRequest);
                                            })
                                            .then(Mono.just(updatedDto));
                                }
                                return Mono.just(updatedDto);
                            });
                });
    }


    /**
     * Update a itemAssetDepreciation.
     *
     * @param itemAssetDepreciationDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ItemAssetDepreciationDTO> update(ItemAssetDepreciationDTO itemAssetDepreciationDTO) {
        log.debug("Request to update ItemAssetDepreciation : {}", itemAssetDepreciationDTO);
        return itemAssetDepreciationRepository
                .save(itemAssetDepreciationMapper.toEntity(itemAssetDepreciationDTO).setIsPersisted())
                .map(itemAssetDepreciationMapper::toDto);
    }

    /**
     * Partially update a itemAssetDepreciation.
     *
     * @param itemAssetDepreciationDTO the entity to update partially.
     * @return the persisted entity.
     */
//    public Mono<ItemAssetDepreciationDTO> partialUpdate(ItemAssetDepreciationDTO itemAssetDepreciationDTO) {
//        log.debug("Request to partially update ItemAssetDepreciation : {}", itemAssetDepreciationDTO);
//        return itemAssetDepreciationRepository
//                .findById(itemAssetDepreciationDTO.getId())
//                .map(existingItemAssetDepreciation -> {
//                    itemAssetDepreciationMapper.partialUpdate(existingItemAssetDepreciation, itemAssetDepreciationDTO);
//                    existingItemAssetDepreciation.setIsPersisted();
//                    return existingItemAssetDepreciation;
//                })
//                .flatMap(itemAssetDepreciationRepository::save)
//                .flatMap(updatedItemAssetDepreciation -> {
//                    return itemAssetDepreciationDetailRepository
//                            .changeIsDeletedByItemAssetDepreciationId(updatedItemAssetDepreciation.getId())
//                            .then(Mono.just(updatedItemAssetDepreciation));
//                })
//                .flatMap(itemAssetDepreciation -> {
//                    if (itemAssetDepreciationDTO.getItemAssetDepreciationDetails() != null && !itemAssetDepreciationDTO.getItemAssetDepreciationDetails().isEmpty()) {
//                        return Flux.fromIterable(itemAssetDepreciationDTO.getItemAssetDepreciationDetails())
//                                .flatMap(itemAssetDepreciationDetailDTO -> {
//                                    itemAssetDepreciationDetailDTO.setId(UUID.randomUUID());
//                                    itemAssetDepreciationDetailDTO.setItemAssetDepreciationId(itemAssetDepreciation.getId());
//                                    return itemAssetDepreciationDetailRepository.save(itemAssetDepreciationDetailMapper.toEntity(itemAssetDepreciationDetailDTO));
//                                })
//                                .then(Mono.just(itemAssetDepreciation));
//                    }
//                    return Mono.just(itemAssetDepreciation);
//                })
//                .flatMap(savedInventories -> {
//                    if (itemAssetDepreciationDTO.getRequestApprovals() != null && !itemAssetDepreciationDTO.getRequestApprovals().isEmpty()) {
//                        return Flux.fromIterable(itemAssetDepreciationDTO.getRequestApprovals())
//                                .flatMap(requestApprovalDTO -> {
//                                    CreateReviewRequest createReviewRequest = new CreateReviewRequest();
//                                    createReviewRequest.setDocumentId(savedInventories.getId());
//                                    createReviewRequest.setEmployeeIds(Collections.singleton(requestApprovalDTO.getEmployeeId()));
//                                    return requestApprovalService.requestReview(createReviewRequest);
//                                })
//                                .then(Mono.just(savedInventories));
//                    }
//                    return Mono.just(savedInventories);
//                })
//                .map(itemAssetDepreciationMapper::toDto);
//    }
    public Mono<ItemAssetDepreciationDTO> partialUpdate(ItemAssetDepreciationDTO itemAssetDepreciationDTO) {
        log.debug("Request to partially update ItemAssetDepreciation : {}", itemAssetDepreciationDTO);

        return this.checkInventoriesStorage(itemAssetDepreciationDTO.getCode(), itemAssetDepreciationDTO.getDepreciationDate())
                .flatMap(isValid -> {
                    if (!isValid) {
                        return Mono.error(new BadRequestAlertException(
                                "Invalid InventoriesStorage",
                                "ItemAssetDepreciation",
                                "inventoriesStorageInvalid"));
                    }

                    return itemAssetDepreciationRepository.findById(itemAssetDepreciationDTO.getId())
                            .flatMap(existingItemAssetDepreciation -> {
                                itemAssetDepreciationMapper.partialUpdate(existingItemAssetDepreciation, itemAssetDepreciationDTO);
                                existingItemAssetDepreciation.setIsPersisted();

                                return itemAssetDepreciationRepository.save(existingItemAssetDepreciation)
                                        .flatMap(updatedItemAssetDepreciation -> {
                                            List<UUID> detailIds = itemAssetDepreciationDTO.getItemAssetDepreciationDetails().stream()
                                                    .map(ItemAssetDepreciationDetailDTO::getId)
                                                    .toList();
                                            List<UUID> inventoriesIds = itemAssetDepreciationDTO.getItemAssetDepreciationDetails().stream()
                                                    .map(ItemAssetDepreciationDetailDTO::getInventoriesStorageId)
                                                    .toList();

                                            return inventoriesStorageRepository.findAllById(inventoriesIds)
                                                    .collectList()
                                                    .flatMap(inventoriesStorageDtos -> {
                                                        if (inventoriesStorageDtos == null || inventoriesStorageDtos.isEmpty()) {
                                                            return Mono.error(new BadRequestAlertException(
                                                                    "Invalid InventoriesStorage",
                                                                    "ItemAssetDepreciation",
                                                                    "inventoriesStorageInvalid"));
                                                        }
                                                        return Flux.fromIterable(detailIds)
                                                                .flatMap(detailId -> itemAssetDepreciationDetailRepository.findById(detailId)
                                                                        .flatMap(existingDetail -> {
                                                                            ItemAssetDepreciationDetailDTO updatedDetailDTO = itemAssetDepreciationDTO
                                                                                    .getItemAssetDepreciationDetails()
                                                                                    .stream()
                                                                                    .filter(dto -> dto.getId().equals(detailId))
                                                                                    .findFirst()
                                                                                    .orElse(null);
                                                                            if (updatedDetailDTO != null) {
                                                                                itemAssetDepreciationDetailMapper.partialUpdate(existingDetail, updatedDetailDTO);

                                                                                InventoriesStorage storage = inventoriesStorageDtos.stream()
                                                                                        .filter(inventoriesStorage -> inventoriesStorage.getId().equals(existingDetail.getInventoriesStorageId()))
                                                                                        .findFirst()
                                                                                        .orElse(null);

                                                                                if (storage == null) {
                                                                                    return Mono.error(new BadRequestAlertException(
                                                                                            "Missing InventoriesStorage for Detail",
                                                                                            "ItemAssetDepreciation",
                                                                                            "storageNotFound"));
                                                                                }

                                                                                this.handleCalculateAmortization(storage, existingDetail);
                                                                                existingDetail.setIsPersisted();
                                                                                return itemAssetDepreciationDetailRepository.save(existingDetail);
                                                                            }
                                                                            return Mono.empty();
                                                                        })
                                                                )
                                                                .then(Mono.just(updatedItemAssetDepreciation));
                                                    });
                                        });
                            })
                            .flatMap(updatedInventories -> {
                                if (itemAssetDepreciationDTO.getRequestApprovals() != null && !itemAssetDepreciationDTO.getRequestApprovals().isEmpty()) {
                                    return Flux.fromIterable(itemAssetDepreciationDTO.getRequestApprovals())
                                            .flatMap(requestApprovalDTO -> {
                                                CreateReviewRequest createReviewRequest = new CreateReviewRequest();
                                                createReviewRequest.setDocumentId(updatedInventories.getId());
                                                createReviewRequest.setEmployeeIds(Collections.singleton(requestApprovalDTO.getEmployeeId()));
                                                return requestApprovalService.requestReview(createReviewRequest);
                                            })
                                            .then(Mono.just(updatedInventories));
                                }
                                return Mono.just(updatedInventories);
                            })
                            .map(itemAssetDepreciationMapper::toDto);
                });
    }

    private ItemAssetDepreciationDetail handleCalculateAmortization(InventoriesStorage inventoriesStorage, ItemAssetDepreciationDetail updated) {

        if(inventoriesStorage == null) {
            return updated;
        }

        BigDecimal accumulatedAmortizationAmount = inventoriesStorage.getPrice()
                .subtract(inventoriesStorage.getRemainingPrice()); // lũy kế khấu hao (chưa tính tháng này)

        // Tỷ lệ khấu hao hiện tại
        BigDecimal amortizationRate = accumulatedAmortizationAmount.divide(inventoriesStorage.getPrice(), 6, RoundingMode.HALF_UP);

        // Tỷ lệ khấu hao sau khi tính tháng này
        amortizationRate = amortizationRate.add(updated.getAmortizationAmount().multiply(BigDecimal.valueOf(100)).divide(inventoriesStorage.getPrice()));

        // Kiểm tra: Tỷ lệ khấu hao không vượt quá 100%
        if (amortizationRate.compareTo(BigDecimal.valueOf(100)) > 0) { // 1 = 100%
            amortizationRate = BigDecimal.valueOf(100);
        }

        // Lũy kế khấu hao (đã tính tháng này)
        accumulatedAmortizationAmount = inventoriesStorage.getPrice().multiply(amortizationRate).divide(BigDecimal.valueOf(100), 6, RoundingMode.HALF_UP);

        // Kiểm tra: Lũy kế khấu hao không vượt giá gốc
        if (accumulatedAmortizationAmount.compareTo(inventoriesStorage.getPrice()) > 0) {
            accumulatedAmortizationAmount = inventoriesStorage.getPrice();
        }

        // Tính lại giá trị còn lại
        BigDecimal remainingPrice = inventoriesStorage.getPrice().subtract(accumulatedAmortizationAmount);
        if (remainingPrice.compareTo(BigDecimal.ZERO) < 0) { // Giá trị còn lại không được âm
            remainingPrice = BigDecimal.ZERO;
        }

        // Cập nhật thông tin
        updated.setAccumulatedAmortizationAmount(accumulatedAmortizationAmount);
        updated.setAmortizationRate(amortizationRate); // Đổi sang %

        return updated;
    }



    /**
     * Find itemAssetDepreciations by Criteria.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<ItemAssetDepreciationDTO> findByCriteria(ItemAssetDepreciationCriteria criteria, Pageable pageable) {
        log.debug("Request to get all ItemAssetDepreciations by Criteria");

        return itemAssetDepreciationRepository.findByCriteria(criteria, pageable)
                .map(itemAssetDepreciationMapper::toDto)
                .collectList()
                .flatMapMany(itemAssetDepreciationDTOS -> {

                    var listIds = itemAssetDepreciationDTOS.stream()
                            .map(ItemAssetDepreciationDTO::getId)
                            .toList();
                    if (listIds.isEmpty()) {
                        listIds = new ArrayList<>(List.of(UUID.fromString("00000000-0000-0000-0000-000000000000")));
                    }
                    return requestApprovalRepository.findByDocumentIdIn(listIds).map(requestApprovalMapper::toDto).collectList()
                            .defaultIfEmpty(new ArrayList<>())
                            .flatMapMany(requestApprovalDTOS -> {
                                if (itemAssetDepreciationDTOS.isEmpty()) {
                                    return Flux.empty();
                                }

                                List<UUID> employeeIds = itemAssetDepreciationDTOS.stream()
                                        .map(ItemAssetDepreciationDTO::getEmployeeId)
                                        .filter(Objects::nonNull)
                                        .distinct()
                                        .collect(Collectors.toList());
                                var listRequestApprovalEmployeeIds = requestApprovalDTOS.stream()
                                        .map(RequestApprovalDTO::getEmployeeId)
                                        .distinct()
                                        .toList();
                                employeeIds.addAll(listRequestApprovalEmployeeIds);
                                // Fetch employees by IDs and return a Flux
                                return employeeClient.getEmployeesByListIds(employeeIds) // This now returns Flux<EmployeeDTO>
                                        .collectList()
                                        .flatMapMany(employeeDTOS -> {
                                            // Create a map for quick lookup of employees by ID
                                            Map<UUID, EmployeeDTO> employeeMap = employeeDTOS.stream()
                                                    .collect(Collectors.toMap(EmployeeDTO::getId, Function.identity()));
                                            // Assign employee data to each ItemAssetDepreciationDTO
                                            requestApprovalDTOS.forEach(requestApprovalDTO -> {
                                                if (requestApprovalDTO.getEmployeeId() != null) {
                                                    requestApprovalDTO.setEmployee(employeeMap.get(requestApprovalDTO.getEmployeeId()));
                                                }
                                            });
                                            itemAssetDepreciationDTOS.forEach(item -> {
                                                if (item.getEmployeeId() != null) {
                                                    item.setEmployee(employeeMap.get(item.getEmployeeId()));
                                                }
                                                var requestApproval = requestApprovalDTOS.stream()
                                                        .filter(Objects::nonNull)
                                                        .filter(requestApprovalDTO -> requestApprovalDTO.getDocumentId() != null && requestApprovalDTO.getDocumentId().equals(item.getId())).toList();
                                                item.setRequestApprovals(requestApproval);
                                            });

                                            return Flux.fromIterable(itemAssetDepreciationDTOS);
                                        });
                            });
                });
    }


    /**
     * Find the count of itemAssetDepreciations by criteria.
     *
     * @param criteria filtering criteria
     * @return the count of itemAssetDepreciations
     */
    public Mono<Long> countByCriteria(ItemAssetDepreciationCriteria criteria) {
        log.debug("Request to get the count of all ItemAssetDepreciations by Criteria");
        return itemAssetDepreciationRepository.countByCriteria(criteria);
    }

    /**
     * Returns the number of itemAssetDepreciations available.
     *
     * @return the number of entities in the database.
     */
    public Mono<Long> countAll() {
        return itemAssetDepreciationRepository.count();
    }

    /**
     * Get one itemAssetDepreciation by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<ItemAssetDepreciationDTO> findOne(UUID id) {
        log.debug("Request to get ItemAssetDepreciation : {}", id);
        return itemAssetDepreciationRepository.findById(id)
                .map(itemAssetDepreciationMapper::toDto)
//                .flatMap(dto -> itemAssetDepreciationDetailRepository.findByItemAssetDepreciationId(id)
//                        .map(itemAssetDepreciationDetailMapper::toDto)
//                        .collectList()
//                        .map(details -> {
//                            dto.setItemAssetDepreciationDetails(details);
//                            return dto;
//                        })
//                )
                .flatMap(dto -> {
                    return requestApprovalRepository.findAllByDocumentIdAndIsDeletedIsFalse(dto.getId())
                            .map(requestApprovalMapper::toDto)
                            .collectList()
                            .flatMap(requestApprovalDTOS -> {
                                List<UUID> employeeIds = requestApprovalDTOS.stream()
                                        .map(RequestApprovalDTO::getEmployeeId)
                                        .distinct()
                                        .collect(Collectors.toList());

//                                if (dto.getEmployeeId() != null) {
//                                    employeeIds.add(dto.getEmployeeId());
//                                }

                                return employeeClient.getEmployeesByListIds(employeeIds)
                                        .collectList()
                                        .flatMap(employees -> {
                                            requestApprovalDTOS.forEach(requestApprovalDTO ->
                                                    employees.stream()
                                                            .filter(employeeDTO -> employeeDTO.getId().equals(requestApprovalDTO.getEmployeeId()))
                                                            .findFirst()
                                                            .ifPresent(requestApprovalDTO::setEmployee)
                                            );
                                            dto.setRequestApprovals(requestApprovalDTOS);
                                            return Mono.just(dto);
                                        });
                            });
                });

    }

    /**
     * Delete the itemAssetDepreciation by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(UUID id) {
        log.debug("Request to delete ItemAssetDepreciation : {}", id);
        return itemAssetDepreciationRepository.changeIsDeletedById(id);
    }

    public Mono<Void> setStatus(UUID id, StatusEntity status) {
        return itemAssetDepreciationRepository.findById(id)
                .flatMap(e -> {
                    if (status.equals(StatusEntity.COMPLETED) && !e.getStatus().equals(StatusEntity.APPROVED)) {
                        return Mono.empty();
                    }
                    e.setStatus(status);
                    e.setIsPersisted();
                    return itemAssetDepreciationRepository.save(e).then();
                });
    }

    public Mono<byte[]> exportRecordsAsCSV(ItemAssetDepreciationCriteria criteria, Pageable pageable) {
        log.debug("Request to export ItemAssetDepreciation as CSV");
        return this.findByCriteria(criteria, pageable)
                .map(ItemAssetDepreciation_DTO_Export::new)
                .collectList()
                .flatMap(dtoList -> {
                    log.debug("Converting Inventories list to CSV");
                    return CSVUtils.convertListToExcel(dtoList);
                });
    }

    @Transactional(readOnly = true)
    public Flux<ItemAssetDepreciationDTO> findOneByItemInfo(UUID inventoriesStorageId) {
        log.debug("Request to get ItemAssetTransfer: {}", inventoriesStorageId);
        return itemAssetDepreciationRepository.findAllByInventoriesStorageId(inventoriesStorageId)
                .map(itemAssetDepreciationMapper::toDto)
                .collectList()
                .flatMapMany(Flux::fromIterable);
    }

    public Mono<Long> countDetailById(UUID id) {
        log.debug("Request to get the count of all InventoriesStorages by Criteria");
        ItemAssetDepreciationDetailCriteria itemAssetDepreciationDetailCriteria = new ItemAssetDepreciationDetailCriteria();
        UUIDFilter inventoriesStorageId = new UUIDFilter();
        inventoriesStorageId.setEquals(id);
        itemAssetDepreciationDetailCriteria.setId(inventoriesStorageId);
        BooleanFilter isDeleted = new BooleanFilter();
        isDeleted.setEquals(false);
        itemAssetDepreciationDetailCriteria.setIsDeleted(isDeleted);

        return itemAssetDepreciationDetailRepository.countByCriteria(itemAssetDepreciationDetailCriteria);
    }

    public Flux<ItemAssetDepreciationDetailDTO> findDetailById(UUID id, Pageable pageable) {
        log.debug("Request to get all InventoriesStorages by Criteria");

        ItemAssetDepreciationDetailCriteria itemAssetDepreciationDetailCriteria = new ItemAssetDepreciationDetailCriteria();
        UUIDFilter inventoriesStorageId = new UUIDFilter();
        inventoriesStorageId.setEquals(id);

        // fake field item_asset_depreciation_id == code
        itemAssetDepreciationDetailCriteria.setId(inventoriesStorageId);
        BooleanFilter isDeleted = new BooleanFilter();
        isDeleted.setEquals(false);
        itemAssetDepreciationDetailCriteria.setIsDeleted(isDeleted);

        return itemAssetDepreciationDetailRepository.findByCriteria(itemAssetDepreciationDetailCriteria, pageable)
                .map(itemAssetDepreciationDetailMapper::toDto);
    }

    public Mono<Void> handleDepreciation(UUID id) {
        // Run background job in a separate task
        return itemAssetDepreciationDetailRepository.findByItemAssetDepreciationId(id)
                .collectList()
                .flatMapMany(Flux::fromIterable)
                .concatMap(itemAssetDepreciation ->
                        inventoriesStorageService.changeRemainingPrice(
                                itemAssetDepreciation.getInventoriesStorageId(),
                                itemAssetDepreciation.getAmortizationAmount()
                        )
                )
                .then(Mono.fromRunnable(this::runBackgroundJob));
    }

    private void runBackgroundJob() {
        log.info("Running background job...");
    }

}
