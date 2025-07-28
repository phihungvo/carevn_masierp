package com.masi.logistics.service;

import com.carevn.masi.utils.CSV.CSVUtils;
import com.carevn.masi.utils.SecurityUtils;
import com.masi.logistics.domain.ItemLiquidation;
import com.masi.logistics.domain.ItemLiquidationDetail;
import com.masi.logistics.domain.criteria.ItemAssetDepreciationCriteria;
import com.masi.logistics.domain.criteria.ItemLiquidationCriteria;
import com.masi.logistics.domain.enumeration.ItemStatus;
import com.masi.logistics.domain.enumeration.StatusEntity;
import com.masi.logistics.repository.*;
import com.masi.logistics.service.dto.*;
import com.masi.logistics.service.exportDTO.ItemAssetDepreciation_DTO_Export;
import com.masi.logistics.service.exportDTO.ItemLiquidation_DTO_Export;
import com.masi.logistics.service.mapper.ContactGiftMapper;
import com.masi.logistics.service.mapper.ItemLiquidationDetailMapper;
import com.masi.logistics.service.mapper.ItemLiquidationMapper;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.masi.logistics.service.mapper.RequestApprovalMapper;
import com.masi.logistics.service.web.client.EmployeeClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.masi.logistics.domain.ItemLiquidation}.
 */
@Service
@Transactional
public class ItemLiquidationService {

    private static final Logger log = LoggerFactory.getLogger(ItemLiquidationService.class);

    private final ItemLiquidationRepository itemLiquidationRepository;

    private final ItemLiquidationMapper itemLiquidationMapper;
    private final RequestApprovalService requestApprovalService;
    private final ItemLiquidationDetailRepository itemLiquidationDetailRepository;
    private final ItemLiquidationDetailMapper itemLiquidationDetailMapper;
    private final RequestApprovalRepository requestApprovalRepository;
    private final RequestApprovalMapper requestApprovalMapper;
    private final EmployeeClient employeeClient;
    private final DocumentCodeSequenceService documentCodeSequenceService;
    private final InventoriesStorageRepository inventoriesStorageRepository;
    private final ItemInfoRepository itemInfoRepository;

    public ItemLiquidationService(ItemLiquidationRepository itemLiquidationRepository, ItemLiquidationMapper itemLiquidationMapper, RequestApprovalService requestApprovalService,
                                  ItemLiquidationDetailRepository itemLiquidationDetailRepository, ItemLiquidationDetailMapper itemLiquidationDetailMapper, RequestApprovalRepository requestApprovalRepository, RequestApprovalMapper requestApprovalMapper, EmployeeClient employeeClient, DocumentCodeSequenceService documentCodeSequenceService, InventoriesStorageRepository inventoriesStorageRepository, ItemInfoRepository itemInfoRepository) {
        this.itemLiquidationRepository = itemLiquidationRepository;
        this.itemLiquidationMapper = itemLiquidationMapper;
        this.requestApprovalService = requestApprovalService;
        this.itemLiquidationDetailRepository = itemLiquidationDetailRepository;
        this.itemLiquidationDetailMapper = itemLiquidationDetailMapper;
        this.requestApprovalRepository = requestApprovalRepository;
        this.requestApprovalMapper = requestApprovalMapper;
        this.employeeClient = employeeClient;
        this.documentCodeSequenceService = documentCodeSequenceService;
        this.inventoriesStorageRepository = inventoriesStorageRepository;
        this.itemInfoRepository = itemInfoRepository;
    }

    /**
     * Save a itemLiquidation.
     *
     * @param itemLiquidationDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ItemLiquidationDTO> save(ItemLiquidationDTO itemLiquidationDTO) {
        log.debug("Request to save ItemLiquidation : {}", itemLiquidationDTO);
        itemLiquidationDTO.setStatus(StatusEntity.NEW);
        return documentCodeSequenceService.makeSureDocumentCodeSequenceExist(ItemLiquidation.ENTITY_NAME, "%05d")
                .then(Mono.defer(() -> {
                    return documentCodeSequenceService.getByDocumentType(ItemLiquidation.ENTITY_NAME)
                            .flatMap(sequence -> {
                                LocalDate currentDate = LocalDate.now();
                                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM");
                                String formattedDate = currentDate.format(formatter);
                                var code = "DI" + formattedDate + "/" + sequence.getNextAndIncrement();
                                itemLiquidationDTO.setCode(code);
                                return documentCodeSequenceService.updateSequence(sequence)
                                        .then(Mono.defer(() -> {
                                            return itemLiquidationRepository.save(itemLiquidationMapper.toEntity(itemLiquidationDTO)) // Lưu ItemLiquidation
                                                    .map(itemLiquidationMapper::toDto)
                                                    .flatMap(dto -> {
                                                        if (itemLiquidationDTO.getItemLiquidationDTODetails() != null && !itemLiquidationDTO.getItemLiquidationDTODetails().isEmpty()) {
                                                            return Flux.fromIterable(itemLiquidationDTO.getItemLiquidationDTODetails())
                                                                    .flatMap(itemLiquidationDetailDTO -> {
                                                                        itemLiquidationDetailDTO.setId(UUID.randomUUID());
                                                                        itemLiquidationDetailDTO.setItemLiquidationId(dto.getId());
                                                                        return itemLiquidationDetailRepository.save(itemLiquidationDetailMapper.toEntity(itemLiquidationDetailDTO));
                                                                    })
                                                                    .then(Mono.just(dto));
                                                        }
                                                        return Mono.just(dto);
                                                    })
                                                    .flatMap(updatedDto -> {
                                                        if (itemLiquidationDTO.getRequestApprovals() != null && !itemLiquidationDTO.getRequestApprovals().isEmpty()) {
                                                            return Flux.fromIterable(itemLiquidationDTO.getRequestApprovals())
                                                                    .flatMap(requestApprovalDTO -> {
                                                                        CreateReviewRequest createReviewRequest = new CreateReviewRequest();
                                                                        createReviewRequest.setDocumentId(updatedDto.getId());
                                                                        createReviewRequest.setEmployeeIds(Collections.singleton(requestApprovalDTO.getEmployeeId()));
                                                                        return requestApprovalService.requestReview(createReviewRequest); // Yêu cầu phê duyệt
                                                                    })
                                                                    .then(Mono.just(updatedDto)); // Sau khi phê duyệt xong, trả về DTO đã cập nhật
                                                        }
                                                        return Mono.just(updatedDto);
                                                    });
                                        }));
                            });
                }));
    }


    /**
     * Update a itemLiquidation.
     *
     * @param itemLiquidationDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ItemLiquidationDTO> update(ItemLiquidationDTO itemLiquidationDTO) {
        log.debug("Request to update ItemLiquidation : {}", itemLiquidationDTO);
        return itemLiquidationRepository
                .save(itemLiquidationMapper.toEntity(itemLiquidationDTO).setIsPersisted())
                .flatMap(savedInventories -> {
                    if (itemLiquidationDTO.getRequestApprovals() != null && !itemLiquidationDTO.getRequestApprovals().isEmpty()) {
                        return Flux.fromIterable(itemLiquidationDTO.getRequestApprovals())
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
                .map(itemLiquidationMapper::toDto);
    }

    /**
     * Partially update a itemLiquidation.
     *
     * @param itemLiquidationDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<ItemLiquidationDTO> partialUpdate(ItemLiquidationDTO itemLiquidationDTO) {
        log.debug("Request to partially update ItemLiquidation : {}", itemLiquidationDTO);

        return itemLiquidationRepository
                .findById(itemLiquidationDTO.getId())
                .map(existingItemLiquidation -> {
                    itemLiquidationMapper.partialUpdate(existingItemLiquidation, itemLiquidationDTO);
                    existingItemLiquidation.setIsPersisted();
                    return existingItemLiquidation;
                })
                .flatMap(itemLiquidationRepository::save)
                .map(itemLiquidationMapper::toDto)
                .flatMap(dto -> {
                    return itemLiquidationDetailRepository
                            .changeIsDeletedByItemLiquidationId(dto.getId())
                            .then(Mono.just(dto));
                })
                .flatMap(dto -> {
                    if (itemLiquidationDTO.getItemLiquidationDTODetails() != null && !itemLiquidationDTO.getItemLiquidationDTODetails().isEmpty()) {
                        return Flux.fromIterable(itemLiquidationDTO.getItemLiquidationDTODetails())
                                .flatMap(itemLiquidationDetailDTO -> {
                                    itemLiquidationDetailDTO.setId(UUID.randomUUID());
                                    itemLiquidationDetailDTO.setItemLiquidationId(dto.getId());
                                    return itemLiquidationDetailRepository.save(itemLiquidationDetailMapper.toEntity(itemLiquidationDetailDTO));
                                })
                                .then(Mono.just(dto));
                    }
                    return Mono.just(dto);
                })
                .flatMap(updatedDto -> {
                    if (itemLiquidationDTO.getRequestApprovals() != null && !itemLiquidationDTO.getRequestApprovals().isEmpty()) {
                        return Flux.fromIterable(itemLiquidationDTO.getRequestApprovals())
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
    }


    /**
     * Find itemLiquidations by Criteria.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<ItemLiquidationDTO> findByCriteria(ItemLiquidationCriteria criteria, Pageable pageable) {
        log.debug("Request to get all ItemLiquidations by Criteria");
        return itemLiquidationRepository.findByCriteria(criteria, pageable).map(itemLiquidationMapper::toDto).collectList().flatMapMany(il -> {
            var listIds = il.stream().map(ItemLiquidationDTO::getId).toList();
            if (listIds.isEmpty()) {
                return Flux.fromIterable(il);
            }
            return requestApprovalRepository.findByDocumentIdIn(listIds).map(requestApprovalMapper::toDto).collectList().flatMapMany(requestApprovals -> {
                if (requestApprovals.isEmpty()) {
                    return Flux.fromIterable(il);
                }
                il.forEach(itemLiquidationDTO -> {
                    var requestApprovalDTOS = requestApprovals.stream().filter(requestApprovalDTO -> requestApprovalDTO.getDocumentId() != null && requestApprovalDTO.getDocumentId().equals(itemLiquidationDTO.getId())).collect(Collectors.toList());
                    itemLiquidationDTO.setRequestApprovals(requestApprovalDTOS);
                });
                return Flux.fromIterable(il);
            });
        });
    }

    /**
     * Find the count of itemLiquidations by criteria.
     *
     * @param criteria filtering criteria
     * @return the count of itemLiquidations
     */
    public Mono<Long> countByCriteria(ItemLiquidationCriteria criteria) {
        log.debug("Request to get the count of all ItemLiquidations by Criteria");
        return itemLiquidationRepository.countByCriteria(criteria);
    }

    /**
     * Returns the number of itemLiquidations available.
     *
     * @return the number of entities in the database.
     */
    public Mono<Long> countAll() {
        return itemLiquidationRepository.count();
    }

    /**
     * Get one itemLiquidation by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<ItemLiquidationDTO> findOne(UUID id) {
        log.debug("Request to get ItemLiquidation : {}", id);
        return itemLiquidationRepository.findById(id)
                .map(itemLiquidationMapper::toDto)
                .flatMap(dto -> itemLiquidationDetailRepository.findByItemLiquidationId(id)
                        .map(itemLiquidationDetailMapper::toDto)
                        .collectList()
                        .map(itemLiquidationDTODetails -> {
                            dto.setItemLiquidationDTODetails(itemLiquidationDTODetails);
                            return dto;
                        })
                )
                .flatMap(dto -> {
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
                .flatMap(dto -> {
                    if (dto.getCreatedBy() != null) {
                        return employeeClient.getEmployeesByListIds(Collections.singletonList(UUID.fromString(dto.getCreatedBy())))
                                .collectList()
                                .flatMap(employeeDTOList -> {
                                    if (employeeDTOList.isEmpty()) {
                                        return Mono.just(dto);
                                    }
                                    dto.setEmployee(employeeDTOList.get(0));
                                    return Mono.just(dto);
                                });
                    }
                    return Mono.just(dto);
                });
    }

    /**
     * Delete the itemLiquidation by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(UUID id) {
        log.debug("Request to delete ItemLiquidation : {}", id);
        return itemLiquidationRepository.changeIsDeletedById(id);
    }

    public Mono<Void> setStatus(UUID id, StatusEntity status) {
        return itemLiquidationRepository.findById(id)
                .flatMap(e -> {
                    if (status.equals(StatusEntity.COMPLETED) && !e.getStatus().equals(StatusEntity.APPROVED)) {
                        return Mono.empty();
                    }
                    e.setStatus(status);
                    e.setIsPersisted();
                    return itemLiquidationRepository.save(e).then();
                });
    }

    public Mono<byte[]> exportRecordsAsCSV(ItemLiquidationCriteria criteria, Pageable pageable) {
        log.debug("Request to export ItemAssetDepreciation as CSV");
        return this.findByCriteria(criteria, pageable)
                .map(ItemLiquidation_DTO_Export::new)
                .collectList()
                .flatMap(dtoList -> {
                    log.debug("Converting Inventories list to CSV");
                    return CSVUtils.convertListToExcel(dtoList);
                });
    }

    @Transactional(readOnly = true)
    public Flux<ItemLiquidationDTO> findOneByItemInfo(UUID inventoriesStorageId) {
        log.debug("Request to get ItemAssetTransfer: {}", inventoriesStorageId);
        return itemLiquidationRepository.findAllByInventoriesStorageId(inventoriesStorageId)
                .map(itemLiquidationMapper::toDto)
                .collectList()
                .flatMapMany(Flux::fromIterable);
    }

    public Mono<Void> handleConfirmItemLiquidation(UUID documentId) {
        log.debug("Request to handleConfirmItemLiquidation: {}", documentId);

        return itemLiquidationDetailRepository.findByItemLiquidationId(documentId)
                .map(ItemLiquidationDetail::getInventoriesStorageId)
                .collectList()
                .flatMap(uuids -> {
                    log.debug("InventoriesStorageIds found: {}", uuids);

                    if (uuids.isEmpty()) {
                        log.debug("No InventoriesStorageIds found for documentId: {}", documentId);
                        return Mono.empty();
                    }
                    return inventoriesStorageRepository.changeStatusByIds(uuids, ItemStatus.LIQUIDATION)
                            .then(itemInfoRepository.changeDateLiquidationByInventoriesStorageIds(uuids, ZonedDateTime.now()))
                            .doOnSuccess(unused -> log.debug("Successfully updated liquidation status and date for documentId: {}", documentId));
                })
                .then();
    }

}
