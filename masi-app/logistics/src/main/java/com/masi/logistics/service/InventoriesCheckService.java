package com.masi.logistics.service;

import com.carevn.masi.dto.EmployeeDTO;
import com.carevn.masi.utils.CSV.CSVUtils;
import com.masi.logistics.domain.Inventories;
import com.masi.logistics.domain.InventoriesCheck;
import com.masi.logistics.domain.InventoriesCheckDetail;
import com.masi.logistics.domain.criteria.InventoriesCheckCriteria;
import com.masi.logistics.domain.criteria.ItemCriteria;
import com.masi.logistics.domain.enumeration.StatusEntity;
import com.masi.logistics.repository.InventoriesCheckDetailRepository;
import com.masi.logistics.repository.InventoriesCheckRepository;
import com.masi.logistics.repository.RequestApprovalRepository;
import com.masi.logistics.service.dto.*;
import com.masi.logistics.service.exportDTO.inventories_DTO_Export;
import com.masi.logistics.service.exportDTO.items_DTO_Export;
import com.masi.logistics.service.mapper.ContactGiftMapper;
import com.masi.logistics.service.mapper.InventoriesCheckDetailMapper;
import com.masi.logistics.service.mapper.InventoriesCheckMapper;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
 * Service Implementation for managing {@link com.masi.logistics.domain.InventoriesCheck}.
 */
@Service
@Transactional
public class InventoriesCheckService {

    private static final Logger log = LoggerFactory.getLogger(InventoriesCheckService.class);

    private final InventoriesCheckRepository inventoriesCheckRepository;

    private final InventoriesCheckMapper inventoriesCheckMapper;
    private final InventoriesCheckDetailRepository inventoriesCheckDetailRepository;
    private final InventoriesCheckDetailMapper inventoriesCheckDetailMapper;
    private final EmployeeClient employeeClient;
    private final RequestApprovalService requestApprovalService;
    private final RequestApprovalRepository requestApprovalRepository;
    private final RequestApprovalMapper requestApprovalMapper;
    private final DocumentCodeSequenceService documentCodeSequenceService;

    public InventoriesCheckService(InventoriesCheckRepository inventoriesCheckRepository, InventoriesCheckMapper inventoriesCheckMapper, InventoriesCheckDetailRepository inventoriesCheckDetailRepository, InventoriesCheckDetailMapper inventoriesCheckDetailMapper, EmployeeClient employeeClient, RequestApprovalService requestApprovalService, RequestApprovalRepository requestApprovalRepository, RequestApprovalMapper requestApprovalMapper, DocumentCodeSequenceService documentCodeSequenceService) {
        this.inventoriesCheckRepository = inventoriesCheckRepository;
        this.inventoriesCheckMapper = inventoriesCheckMapper;
        this.inventoriesCheckDetailRepository = inventoriesCheckDetailRepository;
        this.inventoriesCheckDetailMapper = inventoriesCheckDetailMapper;
        this.employeeClient = employeeClient;
        this.requestApprovalService = requestApprovalService;
        this.requestApprovalRepository = requestApprovalRepository;
        this.requestApprovalMapper = requestApprovalMapper;
        this.documentCodeSequenceService = documentCodeSequenceService;
    }

    /**
     * Save a inventoriesCheck.
     *
     * @param inventoriesCheckDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<InventoriesCheckDTO> save(InventoriesCheckDTO inventoriesCheckDTO) {
        log.debug("Request to save InventoriesCheck : {}", inventoriesCheckDTO);
        inventoriesCheckDTO.setStatus(StatusEntity.NEW);

        return documentCodeSequenceService.getByDocumentType(InventoriesCheck.ENTITY_NAME)
                .flatMap(code -> {
                    String formattedDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd_MM_yyyy"));
                    String newCode = formattedDate + "/" + String.format("%04d", code.getCurrentSequence());

                    inventoriesCheckDTO.setCode(newCode);

                    return inventoriesCheckRepository
                            .save(inventoriesCheckMapper.toEntity(inventoriesCheckDTO))
                            .flatMap(savedEntity -> {
                                InventoriesCheckDTO savedInventoriesCheckDTO = inventoriesCheckMapper.toDto(savedEntity);

                                if (inventoriesCheckDTO.getListInventoriesCheckDetail() != null) {
                                    return Flux.fromIterable(inventoriesCheckDTO.getListInventoriesCheckDetail())
                                            .flatMap(detail -> {
                                                detail.setId(UUID.randomUUID());
                                                detail.setInventoriesCheckId(savedInventoriesCheckDTO.getId());
                                                return inventoriesCheckDetailRepository.save(inventoriesCheckDetailMapper.toEntity(detail));
                                            })
                                            .then(Mono.just(savedInventoriesCheckDTO));
                                }

                                return Mono.just(savedInventoriesCheckDTO);
                            });
                })
                .flatMap(dto -> {
                    if (inventoriesCheckDTO.getRequestApprovals() != null && !inventoriesCheckDTO.getRequestApprovals().isEmpty()) {
                        return Flux.fromIterable(inventoriesCheckDTO.getRequestApprovals())
                                .flatMap(requestApprovalDTO -> {
                                    CreateReviewRequest createReviewRequest = new CreateReviewRequest();
                                    createReviewRequest.setDocumentId(dto.getId());
                                    createReviewRequest.setEmployeeIds(Collections.singleton(requestApprovalDTO.getEmployeeId()));
                                    return requestApprovalService.requestReview(createReviewRequest);
                                })
                                .then(Mono.just(dto));
                    }
                    return Mono.just(dto);
                })
                .flatMap(dto -> {
                    log.info("Request to create Review Document Inventories : {}", dto);
                    return documentCodeSequenceService.updateCurrentSequence(InventoriesCheck.ENTITY_NAME)
                            .then(Mono.just(dto));
                });
    }




    /**
     * Partially update a inventoriesCheck.
     *
     * @param inventoriesCheckDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<InventoriesCheckDTO> partialUpdate(InventoriesCheckDTO inventoriesCheckDTO) {
        log.debug("Request to partially update InventoriesCheck : {}", inventoriesCheckDTO);

        return inventoriesCheckRepository
                .findById(inventoriesCheckDTO.getId())
                .map(existingInventoriesCheck -> {
                    inventoriesCheckMapper.partialUpdate(existingInventoriesCheck, inventoriesCheckDTO);
                    existingInventoriesCheck.setIsPersisted();
                    return existingInventoriesCheck;
                })
                .flatMap(inventoriesCheckRepository::save)
                .map(inventoriesCheckMapper::toDto)
                .flatMap(updatedInventoriesCheckDTO -> {
                    if (inventoriesCheckDTO.getListInventoriesCheckDetail() != null) {
                        return inventoriesCheckDetailRepository
                                .deleteByInventoriesCheckId(updatedInventoriesCheckDTO.getId(),true)
                                .thenMany(Flux.fromIterable(inventoriesCheckDTO.getListInventoriesCheckDetail()).flatMap(detail -> {
                                    detail.setId(UUID.randomUUID());
                                    detail.setInventoriesCheckId(updatedInventoriesCheckDTO.getId());
                                    return inventoriesCheckDetailRepository.save(inventoriesCheckDetailMapper.toEntity(detail));
                                }))

                                .then(Mono.just(updatedInventoriesCheckDTO));
                    }
                    return Mono.just(updatedInventoriesCheckDTO);
                })
                .flatMap(savedInventories -> {
                    if (inventoriesCheckDTO.getRequestApprovals() != null && !inventoriesCheckDTO.getRequestApprovals().isEmpty()) {
                        return Flux.fromIterable(inventoriesCheckDTO.getRequestApprovals())
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
                .flatMap(dto -> {
                    log.info("Request to create Review Document Inventories : {}", dto);
                    return documentCodeSequenceService.updateCurrentSequence(InventoriesCheck.ENTITY_NAME)
                            .then(Mono.just(dto));
                });
    }

    /**
     * Find inventoriesChecks by Criteria.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<InventoriesCheckDTO> findByCriteria(InventoriesCheckCriteria criteria, Pageable pageable) {
        log.debug("Request to get all InventoriesChecks by Criteria");
        return inventoriesCheckRepository.findByCriteria(criteria, pageable)
                .map(inventoriesCheckMapper::toDto)
                .collectList()
                .flatMapMany(dtos -> {
                    List<UUID> inventoryIds = dtos.stream()
                            .map(InventoriesCheckDTO::getId)
                            .distinct()
                            .toList();
                    return requestApprovalService.fetchRequestApprovalsWithEmployees(inventoryIds)
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
    }

    /**
     * Find the count of inventoriesChecks by criteria.
     *
     * @param criteria filtering criteria
     * @return the count of inventoriesChecks
     */
    public Mono<Long> countByCriteria(InventoriesCheckCriteria criteria) {
        log.debug("Request to get the count of all InventoriesChecks by Criteria");
        return inventoriesCheckRepository.countByCriteria(criteria);
    }


    /**
     * Get one inventoriesCheck by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<InventoriesCheckDTO> findOne(UUID id) {
        log.debug("Request to get InventoriesCheck : {}", id);

        return inventoriesCheckRepository.findById(id)
                .map(inventoriesCheckMapper::toDto)
                .flatMap(inventoriesCheckDTO ->
                        inventoriesCheckDetailRepository.findAllByInventoriesCheckId(id)
                                .map(InventoriesCheckDetail::toDto)
                                .collectList()
                                .flatMap( dto -> {
                                    inventoriesCheckDTO.setListInventoriesCheckDetail(dto);
                                    return Mono.just(inventoriesCheckDTO);
                                })
                )
                .flatMap(inventoriesCheckDTO -> {
                    List<UUID> employeeClientIds = Stream.of(
                                    inventoriesCheckDTO.getApprover1(),
                                    inventoriesCheckDTO.getApprover2(),
                                    inventoriesCheckDTO.getApprover3()
                            )
                            .filter(Objects::nonNull)
                            .distinct()
                            .collect(Collectors.toList());

                    if (employeeClientIds.isEmpty()) {
                        return Mono.just(inventoriesCheckDTO);
                    }

                    return employeeClient.getEmployeesByListIds(employeeClientIds)
                            .collectList()
                            .map(employeeList -> {
                                Map<UUID, EmployeeDTO> employeeMap = employeeList.stream()
                                        .collect(Collectors.toMap(EmployeeDTO::getId, Function.identity()));
                                inventoriesCheckDTO.setApprover1Employee(employeeMap.get(inventoriesCheckDTO.getApprover1()));
                                inventoriesCheckDTO.setApprover2Employee(employeeMap.get(inventoriesCheckDTO.getApprover2()));
                                inventoriesCheckDTO.setApprover3Employee(employeeMap.get(inventoriesCheckDTO.getApprover3()));

                                return inventoriesCheckDTO;
                            });
                })
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
                });
    }



    /**
     * Delete the inventoriesCheck by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(UUID id) {
        log.debug("Request to delete InventoriesCheck : {}", id);
        return inventoriesCheckRepository.changeDeleted(id,true);
    }

    public Mono<Void> setStatus(UUID id, StatusEntity status) {
        return inventoriesCheckRepository.findById(id)
                .flatMap(e -> {
                    if (status.equals(StatusEntity.COMPLETED) && !e.getStatus().equals(StatusEntity.APPROVED)) {
                        return Mono.empty();
                    }
                    e.setStatus(status);
                    e.setIsPersisted();
                    return inventoriesCheckRepository.save(e).then();
                });
    }

    public Mono<byte[]> exportRecordsAsCSV(InventoriesCheckCriteria criteria, Pageable pageable) {
        log.debug("Request to export Inventories as CSV");

        return this.findByCriteria(criteria, null)
                .map(inventories_DTO_Export::new)
                .collectList()
                .flatMap(dtoList -> {
                    log.debug("Converting Inventories list to CSV");

                    return CSVUtils.convertListToExcel(dtoList);
                });
    }
}
