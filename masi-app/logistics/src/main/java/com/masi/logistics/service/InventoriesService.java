package com.masi.logistics.service;

import com.carevn.masi.dto.WorkspaceDTO;
import com.carevn.masi.utils.CSV.CSVUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.masi.logistics.domain.*;
import com.masi.logistics.domain.criteria.InventoriesCriteria;
import com.masi.logistics.domain.criteria.InventoriesDetailCriteria;
import com.masi.logistics.domain.enumeration.*;
import com.masi.logistics.domain.enumeration.ItemType;
import com.masi.logistics.domain.event.NotificationAddedEvent;
import com.masi.logistics.repository.*;
import com.masi.logistics.service.dto.CreateReviewRequest;
import com.masi.logistics.service.dto.InventoriesDTO;
import com.masi.logistics.service.dto.InventoriesDetailDTO;
import com.masi.logistics.service.dto.RequestApprovalDTO;
import com.masi.logistics.service.exportDTO.invertories_CE_DTO_Export;
import com.masi.logistics.service.exportDTO.invertories_CI_DTO_Export;
import com.masi.logistics.service.exportDTO.invertories_DE_DTO_Export;
import com.masi.logistics.service.exportDTO.invertories_DI_DTO_Export;
import com.masi.logistics.service.mapper.InventoriesDetailMapper;
import com.masi.logistics.service.mapper.InventoriesMapper;

import java.math.BigDecimal;
import java.net.URI;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

import com.masi.logistics.service.mapper.RequestApprovalMapper;
import com.masi.logistics.service.web.client.EmployeeClient;
import com.masi.logistics.service.web.client.SaleClient;
import com.masi.logistics.web.rest.errors.BadRequestAlertException;
import io.r2dbc.postgresql.codec.Json;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.StringFilter;
import tech.jhipster.service.filter.UUIDFilter;

/**
 * Service Implementation for managing {@link com.masi.logistics.domain.Inventories}.
 */
@Service
@Transactional
public class InventoriesService {

    private static final Logger log = LoggerFactory.getLogger(InventoriesService.class);

    private final InventoriesRepository inventoriesRepository;
    private final RequestApprovalMapper requestApprovalMapper;

    private final InventoriesMapper inventoriesMapper;

    private final InventoriesDetailRepository inventoriesDetailRepository;
    private final InventoriesDetailMapper inventoriesDetailMapper;
    private final StreamBridge streamBridge;
    private final RequestApprovalRepository requestApprovalRepository;
    private final EmployeeClient employeeClient;
    private final RequestApprovalService requestApprovalService;
    private final DocumentCodeSequenceService documentCodeSequenceService;
    private final IncomingInvoiceRepository incomingInvoiceRepository;
    private final SaleClient saleClient;
    private final InventoriesStorageRepository inventoriesStorageRepository;
    private final InventoriesStorageService inventoriesStorageService;
    private final ItemService itemService;
    private final InventoriesTypeService inventoriesTypeService;
    private final SuppliersService suppliersService;
    private final WarehouseRepository warehouseRepository;


    public InventoriesService(InventoriesRepository inventoriesRepository, RequestApprovalMapper requestApprovalMapper, InventoriesMapper inventoriesMapper, InventoriesDetailRepository inventoriesDetailRepository, InventoriesDetailMapper inventoriesDetailMapper, StreamBridge streamBridge, RequestApprovalRepository requestApprovalRepository, EmployeeClient employeeClient, RequestApprovalService requestApprovalService, DocumentCodeSequenceService documentCodeSequenceService, IncomingInvoiceRepository incomingInvoiceRepository, SaleClient saleClient, InventoriesStorageRepository inventoriesStorageRepository, InventoriesStorageService inventoriesStorageService, ItemService itemService, InventoriesTypeService inventoriesTypeService, SuppliersService suppliersService, WarehouseRepository warehouseRepository) {
        this.inventoriesRepository = inventoriesRepository;
        this.requestApprovalMapper = requestApprovalMapper;
        this.inventoriesMapper = inventoriesMapper;
        this.inventoriesDetailRepository = inventoriesDetailRepository;
        this.inventoriesDetailMapper = inventoriesDetailMapper;
        this.streamBridge = streamBridge;
        this.requestApprovalRepository = requestApprovalRepository;
        this.employeeClient = employeeClient;
        this.requestApprovalService = requestApprovalService;
        this.documentCodeSequenceService = documentCodeSequenceService;
        this.incomingInvoiceRepository = incomingInvoiceRepository;
        this.saleClient = saleClient;
        this.inventoriesStorageRepository = inventoriesStorageRepository;
        this.inventoriesStorageService = inventoriesStorageService;
        this.itemService = itemService;
        this.inventoriesTypeService = inventoriesTypeService;
        this.suppliersService = suppliersService;
        this.warehouseRepository = warehouseRepository;
    }

    /**
     * Save a inventories.
     *
     * @param inventoriesDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<InventoriesDTO> save(InventoriesDTO inventoriesDTO) {
        log.debug("Request to save Inventories : {}", inventoriesDTO);

        // Set status based on review flag
        inventoriesDTO.setStatus(
                inventoriesDTO.getIsReview() != null && !inventoriesDTO.getIsReview()
                        ? StatusEntity.APPROVED
                        : StatusEntity.NEW
        );

        return Mono.just(inventoriesDTO)

                .flatMap(dto -> {
                    if (dto.getInventoriesTypeId() == null) {
                        return inventoriesTypeService.getInventoriesTypeWrap(inventoriesDTO.getCompanyImport())
                                .map(typeDTO -> {
                                    dto.setInventoriesTypeId(typeDTO.getId());
                                    if (typeDTO.getId() == null) {
                                        dto.setInventoriesTypeId(UUID.fromString("00000000-0000-0000-0000-000000000000"));
                                    }
                                    return dto;
                                })
                                .onErrorResume(e -> {
                                    log.error("Failed to fetch inventories type", e);
                                    dto.setInventoriesTypeId(UUID.fromString("00000000-0000-0000-0000-000000000000"));
                                    return Mono.just(dto);
                                });
                    }
                    return Mono.just(dto);
                })
                .flatMap(dto -> {
                    if (dto.getCustomerId() == null) {
                        return suppliersService.getSuplierWrap(inventoriesDTO.getCompanyImport())
                                .map(customerDTO -> {
                                    dto.setCustomerId(customerDTO.getId());
                                    if (customerDTO.getId() == null) {
                                        dto.setCustomerId(UUID.fromString("00000000-0000-0000-0000-000000000000"));
                                    }
                                    return dto;
                                })
                                .onErrorResume(e -> {
                                    log.error("Failed to fetch supplier", e);
                                    dto.setCustomerId(UUID.fromString("00000000-0000-0000-0000-000000000000"));
                                    return Mono.just(dto);
                                });
                    }
                    return Mono.just(dto);
                })
                .flatMap(dto -> inventoriesRepository.save(inventoriesMapper.toEntity(dto))
                        .map(inventoriesMapper::toDto)
                        .onErrorResume(e -> {
                            log.error("Failed to save Inventories entity", e);
                            return Mono.just(inventoriesDTO);
                        }))
                .flatMap(savedDto -> {
                    // Process inventory details
                    if (inventoriesDTO.getInventoriesDetails() != null && !inventoriesDTO.getInventoriesDetails().isEmpty()) {
                        return Flux.fromIterable(inventoriesDTO.getInventoriesDetails())
                                .flatMap(detail -> {
                                    detail.setId(UUID.randomUUID());
                                    if(detail.getCode() == null || detail.getCode().isEmpty())
                                        detail.setCode(savedDto.getCode());

                                    detail.setInventoriesId(savedDto.getId());
                                    String itemType = "";
                                    if( (detail.getIsDefaultItem() == null || detail.getIsDefaultItem()) ){
                                        if(inventoriesDTO.getIsOrderManu() != null && inventoriesDTO.getIsOrderManu())
                                            itemType = "SYSTEM_THANH_PHAM";
                                        else
                                            itemType = "SYSTEM_BAN_THANH_PHAM";
                                    }
                                    else
                                        return itemService.findByIdOrCreateWhenIsExist(detail.getProteinPercentageApply())
                                                .map(item -> {
                                                    detail.setItemId(item.getId());
                                                    return detail;
                                                })
                                                .map(inventoriesDetailMapper::toEntity)
                                                .flatMap(inventoriesDetailRepository::save)
                                                .onErrorResume(e -> {
                                                    log.error("Failed to save Inventory Detail", e);
                                                    return Mono.empty();
                                                });

                                    // Fetch item if itemId is null
                                    return (detail.getItemId() == null || detail.getItemId().equals(UUID.fromString("00000000-0000-0000-0000-000000000000"))
                                            ? itemService.getItemWrap(savedDto.getCompany(),itemType)
                                            .map(item -> {
                                                detail.setItemId(item != null
                                                        ? item.getId()
                                                        : UUID.fromString("00000000-0000-0000-0000-000000000000"));
                                                return detail;
                                            })
                                            : Mono.just(detail))
                                            .map(inventoriesDetailMapper::toEntity)
                                            .flatMap(inventoriesDetailRepository::save)
                                            .onErrorResume(e -> {
                                                log.error("Failed to save Inventory Detail", e);
                                                return Mono.empty();
                                            });
                                })
                                .then(Mono.just(savedDto))
                                .onErrorResume(e -> {
                                    log.error("Error processing inventory details", e);
                                    return Mono.just(savedDto);
                                });
                    }
                    return Mono.just(savedDto);
                })
                .flatMap(updatedDto -> {
                    // Process request approvals
                    if (inventoriesDTO.getRequestApprovals() != null && !inventoriesDTO.getRequestApprovals().isEmpty()) {
                        return Flux.fromIterable(inventoriesDTO.getRequestApprovals())
                                .flatMap(approval -> {
                                    CreateReviewRequest createReviewRequest = new CreateReviewRequest();
                                    createReviewRequest.setDocumentId(updatedDto.getId());
                                    createReviewRequest.setEmployeeIds(Collections.singleton(approval.getEmployeeId()));
                                    return requestApprovalService.requestReview(createReviewRequest)
                                            .onErrorResume(e -> {
                                                log.error("Failed to request review", e);
                                                return Mono.empty();
                                            });
                                })
                                .then(Mono.just(updatedDto))
                                .onErrorResume(e -> {
                                    log.error("Error processing request approvals", e);
                                    return Mono.just(updatedDto);
                                });
                    }
                    return Mono.just(updatedDto);
                })
                .flatMap(finalDto -> {
                    log.info("Request to create Review Document Inventories : {}", finalDto);
                    return documentCodeSequenceService.updateCurrentSequence(Inventories.ENTITY_NAME)
                            .thenReturn(finalDto)
                            .onErrorResume(e -> {
                                log.error("Failed to update document sequence", e);
                                return Mono.just(finalDto);
                            });
                });
    }



    /**
     * Partially update a inventories.
     *
     * @param inventoriesDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<InventoriesDTO> partialUpdate(InventoriesDTO inventoriesDTO) {
        log.debug("Request to partially update Inventories : {}", inventoriesDTO);

        return inventoriesRepository
                .findById(inventoriesDTO.getId())
                .flatMap(existingInventories -> {
                    log.debug("Found existing inventory: {}", existingInventories);
                    if (existingInventories.getStatus() != null && (existingInventories.getStatus().equals(StatusEntity.NEW) || existingInventories.getStatus().equals(StatusEntity.REJECTED))) {
                        if (inventoriesDTO.getIsReview() != null && !inventoriesDTO.getIsReview()) {
                            inventoriesDTO.setStatus(StatusEntity.APPROVED);
                            inventoriesDTO.setRequestApprovals(new ArrayList<>());
                        } else {
                            inventoriesDTO.setStatus(StatusEntity.NEW);
                        }
                    }
                    // Update the existing inventory with partial fields
                    inventoriesMapper.partialUpdate(existingInventories, inventoriesDTO);
                    existingInventories.setIsPersisted();

                    // Save the updated inventory
                    return inventoriesRepository.save(existingInventories)
                            .flatMap(savedInventories -> {
                                // Delete existing inventory details
                                return inventoriesDetailRepository
                                        .deleteAllByInventoriesId(savedInventories.getId())
                                        .thenReturn(savedInventories);
                            })
                            .flatMap(savedInventories -> {
                                // Add new inventory details if provided
                                if (inventoriesDTO.getInventoriesDetails() != null && !inventoriesDTO.getInventoriesDetails().isEmpty()) {
                                    return Flux.fromIterable(inventoriesDTO.getInventoriesDetails())
                                            .map(detailDTO -> {
                                                detailDTO.setId(UUID.randomUUID());
                                                detailDTO.setCode(savedInventories.getCode());
                                                detailDTO.setInventoriesId(savedInventories.getId());
                                                return inventoriesDetailMapper.toEntity(detailDTO);
                                            })
                                            .flatMap(inventoriesDetailRepository::save)
                                            .then(Mono.just(savedInventories));
                                }
                                return Mono.just(savedInventories);
                            })
                            .flatMap(savedInventories -> {
                                if (inventoriesDTO.getRequestApprovals() != null && !inventoriesDTO.getRequestApprovals().isEmpty()) {
                                    return Flux.fromIterable(inventoriesDTO.getRequestApprovals())
                                            .flatMap(requestApprovalDTO -> {
                                                CreateReviewRequest createReviewRequest = new CreateReviewRequest();
                                                createReviewRequest.setDocumentId(savedInventories.getId());
                                                createReviewRequest.setEmployeeIds(Collections.singleton(requestApprovalDTO.getEmployeeId()));
                                                return requestApprovalService.requestReview(createReviewRequest);
                                            })
                                            .then(Mono.just(savedInventories));
                                }
                                return Mono.just(savedInventories);
                            })
                            .map(inventoriesMapper::toDto);
                });
    }


    /**
     * Find inventories by Criteria.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<InventoriesDTO> findByCriteria(InventoriesCriteria criteria, Pageable pageable) {
        log.debug("Request to get all Inventories by Criteria");
        StringFilter deletedByFilter = new StringFilter();
        deletedByFilter.setSpecified(false); // Lọc các record có deleted_by IS NULL
        criteria.setDeletedBy(deletedByFilter);
        // Kiểm tra và xử lý criteria.getCode()
        Mono<InventoriesCriteria> updatedCriteriaMono;
        if (criteria.getSearch() != null && !criteria.getSearch().isEmpty()) {
            updatedCriteriaMono = employeeClient.getEmployeesBySearch(criteria.getSearch())
                    .collectList()
                    .flatMap(employeeIds -> {
                        if (employeeIds.isEmpty()) {
                            return Mono.just(criteria);
                        }

                        criteria.setEmployeeIds(new ArrayList<>(employeeIds));
                        return Mono.just(criteria);
                    });
        } else {
            updatedCriteriaMono = Mono.just(criteria);
        }
        return updatedCriteriaMono.flatMapMany(inventoriesCriteria -> {
            return inventoriesRepository.findByCriteria(criteria, pageable)
                    .map(inventoriesMapper::toDto)
                    .map(dto -> {
                        if (dto.getPurchaseContractId() == null) {
                            dto.setPurchaseContract(null);
                        }
                        return dto;
                    })
                    .collectList()
                    .flatMapMany(dtos -> {
                        List<UUID> inventoryIds = dtos.stream()
                                .map(InventoriesDTO::getId)
                                .distinct()
                                .toList();

                        return fetchRequestApprovalsWithEmployees(inventoryIds)
                                .map(requestApprovals -> {
                                    dtos.forEach(dto -> {
                                        List<RequestApprovalDTO> approvalsForDto = requestApprovals.stream()
                                                .filter(request -> request.getDocumentId().equals(dto.getId()))
                                                .toList();
                                        dto.setRequestApprovals(approvalsForDto);
                                    });
                                    return dtos;
                                })
                                .flatMapMany(Flux::fromIterable);
                    });
        });
    }

    private Mono<List<RequestApprovalDTO>> fetchRequestApprovalsWithEmployees(List<UUID> inventoryIds) {
        if (inventoryIds.isEmpty()) {
            return Mono.just(Collections.emptyList());
        }
        return requestApprovalRepository.findAllByDocumentIdsAndIsDeletedIsFalse(inventoryIds)
                .map(requestApprovalMapper::toDto)
                .collectList()
                .flatMap(requestApprovals -> {
                    List<UUID> employeeIds = requestApprovals.stream()
                            .map(RequestApprovalDTO::getEmployeeId)
                            .distinct()
                            .toList();

                    return employeeClient.getEmployeesByListIds(employeeIds)
                            .collectList()
                            .map(employees -> {
                                requestApprovals.forEach(requestApproval ->
                                        employees.stream()
                                                .filter(employee -> employee.getId().equals(requestApproval.getEmployeeId()))
                                                .findFirst()
                                                .ifPresent(requestApproval::setEmployee));
                                return requestApprovals;
                            });
                });


//                .collectList()
//                .flatMapMany(dtos -> {
//                    if (dtos.isEmpty()) {
//                        return Flux.empty();
//                    }
//                    Collection<UUID> ids = dtos.stream().map(InventoriesDTO::getId).distinct().toList();
//
//                    return inventoriesDetailRepository.findAllByInventoriesIdIn(ids)
//                            .map(inventoriesDetailMapper::toDto)
//                            .collectList()
//                            .flatMapMany(inventoriesDetails -> {
//                                dtos.forEach(dto -> {
//                                    dto.setInventoriesDetails(
//                                            inventoriesDetails.stream()
//                                                    .filter(inventoriesDetailDTO -> inventoriesDetailDTO.getInventoriesId().equals(dto.getId()))
//                                                    .toList()
//                                    );
//                                });
//                                return Flux.fromIterable(dtos);
//                            });
//                });
    }

    /**
     * Find the count of inventories by criteria.
     *
     * @param criteria filtering criteria
     * @return the count of inventories
     */
    public Mono<Long> countByCriteria(InventoriesCriteria criteria) {
        log.debug("Request to get the count of all Inventories by Criteria");
        StringFilter deletedByFilter = new StringFilter();
        deletedByFilter.setSpecified(false); // Lọc các record có deleted_by IS NULL
        criteria.setDeletedBy(deletedByFilter);
        // Kiểm tra và xử lý criteria.getCode()
        Mono<InventoriesCriteria> updatedCriteriaMono;
        if (criteria.getSearch() != null && !criteria.getSearch().isEmpty()) {
            updatedCriteriaMono = employeeClient.getEmployeesBySearch(criteria.getSearch())
                    .collectList()
                    .flatMap(employeeIds -> {
                        if (employeeIds.isEmpty()) {
                            return Mono.just(criteria);
                        }

                        criteria.setEmployeeIds(new ArrayList<>(employeeIds));
                        return Mono.just(criteria);
                    });
        } else {
            updatedCriteriaMono = Mono.just(criteria);
        }
        return updatedCriteriaMono.flatMap(inventoriesRepository::countByCriteria);
    }

    /**
     * Returns the number of inventories available.
     *
     * @return the number of entities in the database.
     */
    public Mono<Long> countAll() {
        return inventoriesRepository.count();
    }

    /**
     * Get one inventories by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<InventoriesDTO> findOne(UUID id) {
        log.debug("Request to get Inventories : {}", id);

        return inventoriesRepository.findById(id)
                .map(inventoriesMapper::toDto)
                .flatMap(dto -> {
                    UUIDFilter filter = new UUIDFilter();
                    filter.setEquals(id);
                    BooleanFilter booleanFilter = new BooleanFilter();
                    booleanFilter.setEquals(false);
                    InventoriesDetailCriteria criteria = new InventoriesDetailCriteria();
                    criteria.setInventoriesId(filter);
                    criteria.setIsDeleted(booleanFilter);
                    return inventoriesDetailRepository.findByCriteria(criteria, null).collectList()
                            .map(inventoriesDetailMapper::toDto)
                            .doOnNext(dto::setInventoriesDetails)
                            .flatMap(ignored -> Mono.just(dto));
                })
                .flatMap(dto -> {
                    if (dto.getIsReview() != null && !dto.getIsReview()) {
                        return Mono.just(dto);
                    }
                    return requestApprovalRepository.findAllByDocumentIdAndIsDeletedIsFalse(dto.getId())
                            .map(requestApprovalMapper::toDto)
                            .collectList()
                            .flatMap(requestApprovalDTOS -> {
                                List<UUID> employeeIds = requestApprovalDTOS.stream()
                                        .map(RequestApprovalDTO::getEmployeeId)
                                        .distinct()
                                        .collect(Collectors.toList());
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
                })
                .flatMap(dto ->
                        incomingInvoiceRepository.findAllByInventoryInId(dto.getId())
                                .collectList()
                                .doOnNext(dto::setInvoices)
                                .flatMap(ignored -> Mono.just(dto))
                )
                .flatMap(dtos -> {
                    List<UUID> inventoriesDetailIds = dtos.getInventoriesDetails().stream()
                            .map(detail -> {
                                try {
                                    return UUID.fromString(detail.getCreatedBy());
                                } catch (IllegalArgumentException | NullPointerException e) {
                                    return null;
                                }
                            })
                            .filter(Objects::nonNull)
                            .distinct()
                            .collect(Collectors.toList());

                    inventoriesDetailIds.add(dtos.getEmployeeId());

                    return employeeClient.getEmployeesByListIds(inventoriesDetailIds)
                            .collectList()
                            .doOnNext(employees -> {
                                dtos.setCreatedByEmployee(
                                        employees.stream()
                                                .filter(employeeDTO -> employeeDTO.getId().equals(UUID.fromString(dtos.getCreatedBy())))
                                                .findFirst()
                                                .orElse(null)
                                );

                                dtos.setEmployee(
                                        employees.stream()
                                                .filter(employeeDTO -> employeeDTO.getId().equals(dtos.getEmployeeId()))
                                                .findFirst()
                                                .orElse(null)
                                );
                            })
                            .thenReturn(dtos);
                });
    }


    /**
     * Delete the inventories by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(UUID id) {
        log.debug("Request to delete Inventories : {}", id);
        return inventoriesRepository.changeSortIsDeleted(id, true);
    }

//    public Mono<InventoriesDTO> reviewInventories(UUID id, Collection<UUID> employeeReviewIds) {
//        log.debug("Request to confirm Inventories : {}", id);
//        return inventoriesRepository
//                .findById(id)
//                .flatMap(existingInventories -> {
////                    if (existingInventories.getStatus().getValue() >= StatusEntity.WAITING_APPROVED.getValue()) {
////                        return Mono.just(existingInventories).map(inventoriesMapper::toDto);
////                    }
//                    log.debug("Request to confirm update Inventories : {}", existingInventories);
//                    existingInventories.setStatus(StatusEntity.WAITING_APPROVED);
//                    existingInventories.setIsPersisted();
//                    return inventoriesRepository.save(existingInventories)
//                            .flatMap(entityInventory -> {
//
//                                return Mono.just(entityInventory);
//                            })
//                            .map(inventoriesMapper::toDto)
//                            .flatMap(dto -> {
//                                log.info("Request to create Review Document Inventories : {}", dto);
//                                return createReviewDocumentInventories(employeeReviewIds, dto.getId())
//                                        .then(Mono.just(dto));
//                            });
//                });
//    }

    private Mono<Void> createReviewDocumentInventories(Collection<UUID> employeeReviewIds, UUID inventoriesId) {
        log.debug("Request to create Review Document Inventories : {}", inventoriesId);
        return Flux.fromIterable(employeeReviewIds)
                .flatMap(employeeId -> {
                    log.debug("Request to create Review Document Inventories : {}", inventoriesId);
                    RequestApproval requestApproval = new RequestApproval();
                    requestApproval.setIndex(1);
                    requestApproval.setId(UUID.randomUUID());
                    requestApproval.setDocumentId(inventoriesId);
                    requestApproval.setEmployeeId(employeeId);
                    requestApproval.setIsDeleted(false);
                    return requestApprovalRepository.save(requestApproval);
                })
                .then();
    }

//    public Mono<InventoriesDTO> rejectInventories(UUID id, String reason) {
//        log.debug("Request to reject Inventories : {}", id);
//        return inventoriesRepository.findById(id)
//                .flatMap(existingInventories -> {
//                    log.debug("Request to update Inventories status to REJECTED: {}", existingInventories);
//                    existingInventories.setStatus(StatusEntity.REJECTED);
//                    existingInventories.setIsPersisted();
//                    Map<String, Object> additionalFields = new HashMap<>();
//                    additionalFields.put("reasonNote", reason);
//                    Json attribute = existingInventories.getAttribute();
//                    try {
//                        Json updatedAttribute = JsonMapperService.putFieldsToJson(attribute, additionalFields);
//                        existingInventories.setAttribute(updatedAttribute); // Cập nhật attribute
//                    } catch (JsonProcessingException e) {
//                        throw new RuntimeException("Failed to update JSON attribute", e);
//                    }
//
//                    return inventoriesRepository.save(existingInventories)
//                            .map(inventoriesMapper::toDto);
//                });
//    }
//
//
//    public Mono<InventoriesDTO> confirmInventories(UUID id, UUID attachmentId) {
//        log.debug("Request to confirm Inventories : {}", id);
//        return inventoriesRepository
//                .findById(id)
//                .flatMap(existingInventories -> {
//                    log.debug("Request to confirm update Inventories : {}", existingInventories);
////                    if (existingInventories.getStatus().getValue() >= StatusEntity.WAITING_APPROVED.getValue()) {
////                        return Mono.just(existingInventories).map(inventoriesMapper::toDto);
////                    }
//                    existingInventories.setStatus(StatusEntity.APPROVED);
//                    existingInventories.setIsPersisted();
//                    return inventoriesRepository.save(existingInventories)
//                            .flatMap(entityInventory -> {
//                                return inventoriesDetailRepository.findAllByInventoriesIdAndIsDeleted(entityInventory.getId(), false)
//                                        .flatMap(inventoriesDetail -> {
//                                            BigDecimal quantity = inventoriesDetail.getQuantity();
//                                            ZonedDateTime now = ZonedDateTime.now();
//                                            UUID inventoriesDetailId = inventoriesDetail.getId();
//                                            String company = inventoriesDetail.getCompany();
//                                            UUID item = inventoriesDetail.getItemId();
//
//                                            List<InventoriesStorage> storageList = new ArrayList<>();
//                                            for (int i = 0; i < quantity.intValue(); i++) {
//                                                InventoriesStorage inventoriesStorage = new InventoriesStorage();
//                                                inventoriesStorage.setId(UUID.randomUUID());
//                                                inventoriesStorage.setCode(genCodeInputWareHouse(inventoriesDetailId, BigDecimal.valueOf(i)));
//                                                inventoriesStorage.setImportDate(now);
//                                                inventoriesStorage.setCompany(company);
//                                                inventoriesStorage.setItemId(item);
//                                                inventoriesStorage.setCreatedAt(now);
//                                                inventoriesStorage.setInventoriesDetailId(inventoriesDetailId);
//                                                inventoriesStorage.setIsDeleted(false);
//                                                storageList.add(inventoriesStorage);
//                                            }
//
//                                            return inventoriesDetailRepository.saveAll(storageList).then(Mono.just(entityInventory));
//                                        });
//                            })
//                            .map(inventoriesMapper::toDto);
//                });
//    }

//    private String genCodeInputWareHouse(UUID id, BigDecimal code) {
//        String formattedCode = code.stripTrailingZeros().toPlainString();
//        return formattedCode + "-" + id;
//    }

    public Mono<byte[]> exportRecordsAsCSV(InventoriesCriteria criteria, Pageable pageable) {
        log.debug("Request to export Inventories as CSV");

        // Determine the appropriate DTO type based on WarehouseGroupType
        if (Objects.equals(criteria.getWarehouseGroupType().getEquals(), WarehouseGroupType.WAREHOUSE_DEPRECIATION_IMPORT.toString())) {
            return this.findByCriteria(criteria, pageable)
                    .map(inventoriesDTO -> new invertories_DI_DTO_Export(
                            inventoriesDTO.getCode(),
                            inventoriesDTO.getDateCreate(),
                            inventoriesDTO.getInventoriesType(),
                            inventoriesDTO.getCustomer(),
                            inventoriesDTO.getIncomingWarehouse(),
                            inventoriesDTO.getTotalQuantity(),
                            inventoriesDTO.getTotalAmount(),
                            inventoriesDTO.getPurchaseContract(),
                            inventoriesDTO.getStatus()
                    ))
                    .collectList()
                    .flatMap(dtoList -> {
                        log.debug("Converting Inventories list to CSV");
                        return CSVUtils.convertListToExcel(dtoList);
                    });
        }

        if (Objects.equals(criteria.getWarehouseGroupType().getEquals(), WarehouseGroupType.WAREHOUSE_COMMERCE_IMPORT.toString())) {
            return this.findByCriteria(criteria, pageable)
                    .map(inventoriesDTO -> new invertories_CI_DTO_Export(
                            inventoriesDTO.getCode(),
                            inventoriesDTO.getDateCreate(),
                            inventoriesDTO.getInventoriesType(),
                            inventoriesDTO.getCustomer(),
                            inventoriesDTO.getIncomingWarehouse(),
                            inventoriesDTO.getPurchasePrice(),
                            inventoriesDTO.getTotalAmount(),
                            inventoriesDTO.getPurchaseContract(),
                            inventoriesDTO.getStatus(),
                            inventoriesDTO.getTotalQuantity()
                    ))
                    .collectList()
                    .flatMap(dtoList -> {
                        log.debug("Converting Inventories list to CSV");
                        return CSVUtils.convertListToExcel(dtoList);
                    });
        }

        if (Objects.equals(criteria.getWarehouseGroupType().getEquals(), WarehouseGroupType.WAREHOUSE_DEPRECIATION_EXPORT.toString())) {
            return this.findByCriteria(criteria, null)
                    .collectList()
                    .flatMapMany(inventoriesDTOList -> {
                        return employeeClient.getWorkspaces()
                                .flatMap(data -> {
                                    var workspaces = data.getData();
                                    LinkedHashMap<UUID, WorkspaceDTO> workspaceMap = workspaces.stream()
                                            .collect(Collectors.toMap(
                                                    WorkspaceDTO::getId,
                                                    workspace -> workspace,
                                                    (a, b) -> a,
                                                    LinkedHashMap::new
                                            ));
                                    inventoriesDTOList.forEach(inventoriesDTO -> {
                                        var workspaceName = "";
                                        try {
                                            var uuid = UUID.fromString(inventoriesDTO.getAttributeValue("workspaceId"));
                                            workspaceName = workspaceMap.get(uuid).getName();
                                        } catch (Exception e) {
                                            //log.error("Error while getting workspace name: ", e);
                                        }
                                        inventoriesDTO.setReceivedWorkspace(workspaceName);
                                    });
                                    return Mono.just(inventoriesDTOList);
                                })
                                .flatMapMany(Flux::fromIterable);
                    })
                    .map(inventoriesDTO -> new invertories_DE_DTO_Export(
                            inventoriesDTO.getCode(),
                            inventoriesDTO.getDateCreate(),
                            inventoriesDTO.getIncomingWarehouse(),
                            inventoriesDTO.getTotalQuantity(),
                            inventoriesDTO.getTotalAmount(),
                            inventoriesDTO.getStatus(),
                            inventoriesDTO.getReceivedWorkspace(),
                            inventoriesDTO.getNote(),
                            inventoriesDTO.getCreatedAt()
                    ))
                    .collectList()
                    .flatMap(dtoList -> {
                        log.debug("Converting Inventories list to CSV");
                        return CSVUtils.convertListToExcel(dtoList);
                    });
        }

        // Default case for other WarehouseGroupType
        return this.findByCriteria(criteria, pageable)
                .map(inventoriesDTO -> new invertories_CE_DTO_Export(
                        inventoriesDTO.getCode(),
                        inventoriesDTO.getDateCreate(),
                        inventoriesDTO.getInventoriesType(),
                        inventoriesDTO.getCustomer(),
                        inventoriesDTO.getIncomingWarehouse(),
                        inventoriesDTO.getTotalQuantity(),
                        inventoriesDTO.getTotalAmount(),
                        inventoriesDTO.getPurchaseContract(),
                        inventoriesDTO.getStatus(),
                        inventoriesDTO.getAttributeValue("receiverName"),
                        inventoriesDTO.getAttributeValue("shipperAddress"),
                        inventoriesDTO.getNote()
                ))
                .collectList()
                .flatMap(dtoList -> {
                    log.debug("Converting Inventories list to CSV");
                    return CSVUtils.convertListToExcel(dtoList);
                });
    }


    public Mono<Void> NotificationInventories() {
        return inventoriesRepository.findAll()
                .flatMap(inventories -> {
                    log.debug("Request to Notification Inventories : {}", inventories);
                    var title = "Inventories Notification Updated Item";

                    NotificationAddedEvent notificationAddedEvent = NotificationAddedEvent.builder()
                            .content("Inventories Notification Updated Item")
                            .title(title)
                            .createdAt(ZonedDateTime.now())
                            .createdBy("system")
//                            .data(data)
                            .sentBy("system")
                            .action("RecruitmentRequestUpdated")
//                            .entityId(existingRecruitmentRequest.getId().toString())
                            .entityType("RecruitmentRequest")
//                            .recipients(Collections.singleton(existingRecruitmentRequest.getCreatedBy()))
                            .id(UUID.randomUUID())
                            .build();

                    boolean sent = streamBridge.send("notification-added", notificationAddedEvent, org.springframework.http.MediaType.APPLICATION_JSON);
                    if (!sent) {
                        log.error("Failed to send notification for inventory: {}", inventories);
                    }

                    return Mono.empty();
                })
                .onErrorResume(e -> {
                    log.error("Error while sending notification: ", e);
                    return Mono.empty();
                }).then();
    }


    public Mono<Void> setStatus(UUID id, StatusEntity status) {
        return inventoriesRepository.findById(id)
                .flatMap(e -> {
                    if (status.equals(StatusEntity.COMPLETED) && !e.getStatus().equals(StatusEntity.APPROVED)) {
                        return Mono.empty();
                    }
                    e.setStatus(status);
                    e.setIsPersisted();
                    return inventoriesRepository.save(e).then();
                });
    }

    // handle export-import warehouse //

    public Mono<Void> createItemInWareHouse(UUID id) {
        return inventoriesRepository.findById(id)
                .flatMapMany(inventories -> {
                    if (inventories == null) {
                        return Mono.empty();
                    }

                    return inventoriesDetailRepository.findAllByInventoriesIdAndIsDeleted(id, false)
                            .map(InventoriesDetail::toDto)
                            .collectList()
                            .flatMap(details -> processInventoriesDetails(details, inventories));
                })
                .concatWith(Mono.fromRunnable(this::runBackgroundJob))
                .then();
    }

    private void runBackgroundJob() {
        log.info("Running background job...");
    }


    private Mono<Void> processInventoriesDetails(List<InventoriesDetailDTO> details, Inventories inventories) {
        if (details.isEmpty()) {
            return Mono.empty();
        }

        if (inventories.getWarehouseGroupType().equals(WarehouseGroupType.WAREHOUSE_COMMERCE_EXPORT)
                || inventories.getWarehouseGroupType().equals(WarehouseGroupType.WAREHOUSE_DEPRECIATION_EXPORT)) {
            // handle export warehouse
            return processExportWarehouse(details , inventories);
        } else if (inventories.getWarehouseGroupType().equals(WarehouseGroupType.WAREHOUSE_COMMERCE_IMPORT)
                || inventories.getWarehouseGroupType().equals(WarehouseGroupType.WAREHOUSE_DEPRECIATION_IMPORT)) {
            // handle import warehouse
            return processImportWarehouse(details, inventories);
        }

        return Mono.empty();
    }

    private Mono<Void> processExportWarehouse(List<InventoriesDetailDTO> details , Inventories inventories) {
        log.debug("Processing export warehouse");

        return inventoriesStorageRepository.findAllByItemId(
                        details.stream().map(InventoriesDetailDTO::getItemId).collect(Collectors.toList()) ,inventories.getIncomingWarehouseId()
                )
                .collectList()
                .flatMap(items -> {
                    for (InventoriesStorageTotal item : items) {
                        BigDecimal quantityNeed = details.stream()
                                .filter(detail -> detail.getItemId().equals(item.getItemId()))
                                .map(InventoriesDetailDTO::getQuantity)
                                .findFirst()
                                .orElse(BigDecimal.ZERO);

                        BigDecimal totalQuantity = items.stream()
                                .filter(storage -> storage.getItemId().equals(item.getItemId()))
                                .map(storage -> storage.getTotalQuantity() == null ? BigDecimal.ZERO : storage.getTotalQuantity())
                                .reduce(BigDecimal.ZERO, BigDecimal::add);

                        if (totalQuantity.compareTo(quantityNeed) < 0) {
                            return Mono.error(new BadRequestAlertException("Vật phẩm " + item.getItemCode() + " - " + item.getItemName() + " (Hiện có: " + totalQuantity + ") Không đủ số lượng. Số lượng cần: " + quantityNeed, "INVENTORIES", "QUANTITY_NOT_ENOUGH"));
                        }
                    }
                    return Mono.just(items);
                })
                .flatMapMany(items -> Flux.fromIterable(details))
                .flatMap(detail -> {
                    UUID itemId = detail.getItemId();
                    BigDecimal quantityNeed = detail.getQuantity() == null ? BigDecimal.ZERO : detail.getQuantity();

                    return inventoriesStorageRepository.findAllByItemIdAndIsDeletedOrderByCreatedAtAsc(itemId, false)
                            .collectList()
                            .flatMap(storageList -> {
                                BigDecimal remainingQuantity = quantityNeed;
                                List<InventoriesStorage> updatedStorages = new ArrayList<>();
                                ZonedDateTime now = ZonedDateTime.now();
                                for (InventoriesStorage storage : storageList) {
                                    if (remainingQuantity.compareTo(BigDecimal.ZERO) <= 0) {
                                        break;
                                    }

                                    BigDecimal availableQuantity = storage.getQuantity() == null ? BigDecimal.ZERO : storage.getQuantity();
                                    if (availableQuantity.compareTo(remainingQuantity) <= 0) {
                                        remainingQuantity = remainingQuantity.subtract(availableQuantity);
                                        storage.setQuantity(BigDecimal.ZERO);
                                        storage.setIsDeleted(true);
                                    } else {
                                        storage.setQuantity(availableQuantity.subtract(remainingQuantity));
                                        remainingQuantity = BigDecimal.ZERO;
                                    }
                                    storage.setExportDate(now);
                                    updatedStorages.add(storage);
                                }
                                if (remainingQuantity.compareTo(BigDecimal.ZERO) > 0) {
                                    return Mono.error(new RuntimeException("Not enough quantity in storage"));
                                }
                                return inventoriesStorageRepository.saveAll(updatedStorages).then();
                            });
                })
                .then();
    }


//    private Mono<Void> processImportWarehouse(List<InventoriesDetailDTO> details, Inventories inventories) {
//        log.debug("Processing import warehouse");
//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.registerModule(new JavaTimeModule());
//        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
//        return Flux.fromIterable(details)
//                .flatMap(detail -> findCodeItemNext(detail.getItemId())
//                        .flatMap(codeNext -> {
//                            if (inventories.getWarehouseGroupType().equals(WarehouseGroupType.WAREHOUSE_COMMERCE_IMPORT) || ((detail.getItem().getPercentProtein() != null) && detail.getItem().getPercentProtein() > 0))
//                            {
////                            if (!detail.getItem().getItemCategory().getItemTypeCategory().equals(ItemTypeCategory.STACK)) {
//                                InventoriesStorage storage = new InventoriesStorage();
//                                storage.setId(UUID.randomUUID());
//                                storage.setCode(genCodeInputWareHouse(detail.getItem().getCode(), codeNext));
//                                storage.setImportDate(ZonedDateTime.now());
//                                storage.setCompany(detail.getCompany());
//                                storage.setItemId(detail.getItemId());
//                                storage.setCreatedAt(ZonedDateTime.now());
//                                storage.setQuantity(detail.getQuantity());
//                                storage.setInventoriesDetailId(detail.getId());
//                                storage.setItemType(ItemType.MATERIAL);
//                                storage.setIsDeleted(false);
//                                storage.setPrice(detail.getPrice());
//                                storage.setNotes(detail.getNote());
//                                storage.setSupplierId(inventories.getCustomerId());
//
//                                storage.setStatus(ItemStatus.DEPRECIATION);
//                                storage.setRemainingPrice(detail.getPrice());
//                                storage.setWarehouseId(inventories.getIncomingWarehouseId());
//
//
//                                InventoriesStorage.Transaction transaction = new InventoriesStorage.Transaction(InventoriesStorage.TransactionEnum.ADD, ZonedDateTime.now(), storage.getCode(), null, storage.getQuantity(), storage.getPrice(), null, null, storage.getNotes());
//                                InventoriesStorage.AssetLog assetLog = new InventoriesStorage.AssetLog(Collections.singletonList(transaction));
//
//                                try {
//                                    String json = objectMapper.writeValueAsString(assetLog);
//                                    storage.setAssetLogs(Json.of(json));
//                                } catch (JsonProcessingException ignore) {
//                                }
//
//                                return inventoriesStorageRepository.save(storage)
//                                        .then(inventoriesStorageService.saveItemInfo(Collections.singletonList(storage.getId())))
//                                        .then();
//                            }
//                            else {
//                                BigDecimal quantity = detail.getQuantity();
//                                if (quantity == null || quantity.compareTo(BigDecimal.ZERO) <= 0) {
//                                    return Mono.empty();
//                                }
//
//                                ZonedDateTime now = ZonedDateTime.now();
//                                List<InventoriesStorage> storages = new ArrayList<>();
//                                for (int i = 0; i < quantity.intValue(); i++) {
//                                    InventoriesStorage storage = new InventoriesStorage();
//                                    storage.setId(UUID.randomUUID());
//                                    storage.setCode(genCodeInputWareHouse(detail.getItem().getCode(), codeNext));
//                                    storage.setImportDate(now);
//                                    storage.setCompany(detail.getCompany());
//                                    storage.setItemId(detail.getItemId());
//                                    storage.setCreatedAt(now);
//                                    storage.setQuantity(BigDecimal.ONE);
//                                    storage.setInventoriesDetailId(detail.getId());
//                                    storage.setItemType(ItemType.MATERIAL);
//                                    storage.setIsDeleted(false);
//                                    storage.setPrice(detail.getPrice());
//                                    storage.setNotes(detail.getNote());
//                                    storage.setStatus(ItemStatus.DEPRECIATION);
//                                    storage.setRemainingPrice(detail.getPrice());
//                                    storage.setWarehouseId(inventories.getIncomingWarehouseId());
//                                    storage.setSupplierId(inventories.getCustomerId());
//
//                                    codeNext = codeNext.add(BigDecimal.ONE);
//
//                                    InventoriesStorage.Transaction transaction = new InventoriesStorage.Transaction(
//                                            InventoriesStorage.TransactionEnum.ADD,
//                                            now,
//                                            storage.getCode(),
//                                            "",
//                                            storage.getQuantity(),
//                                            storage.getPrice(),
//                                            "",
//                                            "",
//                                            storage.getNotes()
//                                    );
//                                    InventoriesStorage.AssetLog assetLog = new InventoriesStorage.AssetLog(Collections.singletonList(transaction));
//                                    try {
//                                        String json = objectMapper.writeValueAsString(assetLog);
//                                        storage.setAssetLogs(Json.of(json));
//                                    } catch (JsonProcessingException e) {
//                                        log.debug(e.getMessage());
//                                    }
//                                    storages.add(storage);
//                                }
//                                int batchSize = 100;
//
//                                List<List<InventoriesStorage>> batches = new ArrayList<>();
//
//                                for (int i = 0; i < storages.size(); i += batchSize) {
//                                    int end = Math.min(i + batchSize, storages.size());
//                                    batches.add(storages.subList(i, end));
//                                }
//                                return Flux.fromIterable(batches)
//                                        .flatMap(batch -> inventoriesStorageRepository.saveAll(batch)
//                                                .then(inventoriesStorageService.saveItemInfo(
//                                                        batch.stream().map(InventoriesStorage::getId).toList()
//                                                ))
//                                        )
//                                        .then();
//                            }
//
//                        }))
//                .then();
//    }

    private Mono<Void> processImportWarehouse(List<InventoriesDetailDTO> details, Inventories inventories) {
        log.debug("Processing import warehouse");
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return Flux.fromIterable(details)
                .flatMap(detail -> findCodeItemNext(detail.getItemId())
                        .flatMap(codeNext -> {
                            if (inventories.getWarehouseGroupType().equals(WarehouseGroupType.WAREHOUSE_COMMERCE_IMPORT)
                                    && ((detail.getItem().getIsSeparation() != null) && !detail.getItem().getIsSeparation())
                            ) {
//                            if (!detail.getItem().getItemCategory().getItemTypeCategory().equals(ItemTypeCategory.STACK)) {
                                InventoriesStorage storage = new InventoriesStorage();
                                storage.setId(UUID.randomUUID());
                                if(inventories.getProductionId() != null || detail.getCode() != null)
                                    storage.setCode(detail.getCode());
                                else
                                    storage.setCode(genCodeInputWareHouse(detail.getItem().getCode(), codeNext));

                                storage.setImportDate(ZonedDateTime.now());
                                storage.setCompany(detail.getCompany());
                                storage.setItemId(detail.getItemId());
                                storage.setCreatedAt(ZonedDateTime.now());
                                storage.setQuantity(detail.getQuantity());
                                storage.setInventoriesDetailId(detail.getId());
                                storage.setItemType(ItemType.MATERIAL);
                                storage.setIsDeleted(false);
                                storage.setPrice(detail.getPrice());
                                storage.setNotes(detail.getNote());
                                storage.setSupplierId(inventories.getCustomerId());

                                storage.setStatus(ItemStatus.DEPRECIATION);
                                storage.setRemainingPrice(detail.getPrice());
                                storage.setWarehouseId(inventories.getIncomingWarehouseId());


                                InventoriesStorage.Transaction transaction = new InventoriesStorage.Transaction(InventoriesStorage.TransactionEnum.ADD, ZonedDateTime.now(), storage.getCode(), null, storage.getQuantity(), storage.getPrice(), null, null, storage.getNotes());
                                InventoriesStorage.AssetLog assetLog = new InventoriesStorage.AssetLog(Collections.singletonList(transaction));

                                try {
                                    String json = objectMapper.writeValueAsString(assetLog);
                                    storage.setAssetLogs(Json.of(json));
                                } catch (JsonProcessingException ignore) {
                                }

                                return inventoriesStorageRepository.save(storage)
                                        .then(inventoriesStorageService.saveItemInfo(Collections.singletonList(storage.getId())))
                                        .then();
                            } else {
                                BigDecimal quantity = detail.getQuantity();
                                if (quantity == null || quantity.compareTo(BigDecimal.ZERO) <= 0) {
                                    return Mono.empty();
                                }

                                List<InventoriesStorage> storages = new ArrayList<>();
                                ZonedDateTime now = ZonedDateTime.now();

                                for (int i = 0; i < quantity.intValue(); i++) {
                                    InventoriesStorage storage = new InventoriesStorage();
                                    storage.setId(UUID.randomUUID());
                                    storage.setCode(genCodeInputWareHouse(detail.getItem().getCode(), codeNext));
                                    storage.setImportDate(now);
                                    storage.setCompany(detail.getCompany());
                                    storage.setItemId(detail.getItemId());
                                    storage.setCreatedAt(now);
                                    storage.setQuantity(BigDecimal.ONE);
                                    storage.setInventoriesDetailId(detail.getId());
                                    storage.setItemType(ItemType.MATERIAL);
                                    storage.setIsDeleted(false);
                                    storage.setPrice(detail.getPrice());
                                    storage.setNotes(detail.getNote());
                                    storage.setStatus(ItemStatus.DEPRECIATION);
                                    storage.setRemainingPrice(detail.getPrice());
                                    storage.setWarehouseId(inventories.getIncomingWarehouseId());
                                    storage.setSupplierId(inventories.getCustomerId());

                                    codeNext = codeNext.add(BigDecimal.ONE);
                                    InventoriesStorage.Transaction transaction = new InventoriesStorage.Transaction(InventoriesStorage.TransactionEnum.ADD, ZonedDateTime.now(), storage.getCode(), "", storage.getQuantity(), storage.getPrice(), "", "", storage.getNotes());
                                    InventoriesStorage.AssetLog assetLog = new InventoriesStorage.AssetLog(Collections.singletonList(transaction));

                                    try {

                                        String json = objectMapper.writeValueAsString(assetLog);
                                        storage.setAssetLogs(Json.of(json));
                                    } catch (JsonProcessingException e) {
                                        log.debug(e.getMessage());
                                    }
                                    storages.add(storage);
                                }

                                List<UUID> uuid = storages.stream().map(InventoriesStorage::getId)
                                        .toList();

                                return inventoriesStorageRepository.saveAll(storages)
                                        .then(inventoriesStorageService.saveItemInfo(uuid))
                                        .then();
                            }
                        }))
                .then();
    }


    // handle export-import warehouse //


    private String genCodeInputWareHouse(String codeId, BigDecimal code) {
        String formattedCode = code.stripTrailingZeros().toPlainString();
        return codeId + "-" + formattedCode;
    }

    Mono<BigDecimal> findCodeItemNext(UUID id) {
        return inventoriesStorageRepository.findByItemId(id, false)
                .flatMap(dto -> {
                    if (dto.getCode() == null) {
                        return Mono.just(BigDecimal.ZERO);
                    }
                    String code = dto.getCode();
                    try {
                        String[] parts = code.split("-");
                        if (parts.length > 1) {
                            return Mono.just(new BigDecimal(parts[1].trim()).add(BigDecimal.ONE));
                        } else {
                            return Mono.just(BigDecimal.ZERO);
                        }
                    } catch (NumberFormatException e) {
                        return Mono.just(BigDecimal.ZERO);
                    }
                })
                .defaultIfEmpty(BigDecimal.ZERO);
    }


    Mono<String> genCodeItemNext(UUID id) {
        return inventoriesStorageRepository.findByItemId(id, false)
                .flatMap(dto -> {
                    String code = dto.getCode();
                    if (code == null || code.isEmpty()) {
                        return Mono.just("ITEM-" + BigDecimal.ZERO);
                    }
                    try {
                        String[] parts = code.split("-");
                        BigDecimal numberPart = (parts.length > 1)
                                ? new BigDecimal(parts[1].trim())
                                : BigDecimal.ZERO;
                        return Mono.just(parts[0] + "-" + numberPart.add(BigDecimal.ONE));
                    } catch (NumberFormatException e) {
                        return Mono.just(code + "-" + BigDecimal.ZERO);
                    }
                })
                .defaultIfEmpty("ITEM-" + BigDecimal.ZERO);
    }

}
