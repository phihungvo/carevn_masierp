package com.masi.logistics.service;

import com.carevn.masi.dto.EmbedFile;
import com.carevn.masi.dto.UserJWTDetail;
import com.carevn.masi.utils.SecurityUtils;
import com.masi.logistics.domain.DeliveryDetail;
import com.masi.logistics.domain.RequestApproval;
import com.masi.logistics.domain.SupplierContract;
import com.masi.logistics.domain.SupplierContractDetail;
import com.masi.logistics.domain.criteria.SupplierContractCriteria;
import com.masi.logistics.domain.criteria.SupplierContractDetailCriteria;
import com.masi.logistics.domain.enumeration.ContractStatus;
import com.masi.logistics.repository.*;
import com.masi.logistics.service.dto.*;
import com.masi.logistics.service.mapper.*;

import java.time.Duration;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import com.masi.logistics.web.rest.errors.BadRequestAlertException;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.StringFilter;
import tech.jhipster.service.filter.UUIDFilter;
import tech.jhipster.service.filter.ZonedDateTimeFilter;

/**
 * Service Implementation for managing {@link com.masi.logistics.domain.SupplierContract}.
 */
@Service
@Transactional
@AllArgsConstructor
public class SupplierContractService {

    private static final Logger LOG = LoggerFactory.getLogger(SupplierContractService.class);

    private final SupplierContractRepository supplierContractRepository;

    private final SupplierContractDetailRepository supplierContractDetailRepository;
    private final SupplierContractMapper supplierContractMapper;
    private final DeliveryDetailMapper deliveryDetailMapper;
    private final IncomingInvoiceMapper incomingInvoiceMapper;
    private final SupplierContractDetailMapper supplierContractDetailMapper;
    private final RequestApprovalMapper requestApprovalMapper;
    private final DocumentCodeSequenceService documentCodeSequenceService;
    private final RequestApprovalRepository requestApprovalRepository;
    private final IncomingInvoiceRepository incomingInvoiceRepository;
    private final IncomingInvoiceService incomingInvoiceService;
    private final DeliveryScheduleService deliveryScheduleService;
    private final DeliveryDetailRepository deliveryDetailRepository;

    /**
     * Save a supplierContract.
     *
     * @param supplierContractDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<SupplierContractDTO> save(SupplierContractDTO supplierContractDTO) {
        LOG.debug("Request to save SupplierContract : {}", supplierContractDTO);
        if (Objects.isNull(supplierContractDTO.getStatus())){
            supplierContractDTO.setStatus(ContractStatus.NEW);
        }
        supplierContractDTO.setDeliveryStatus(ContractStatus.DeliveryStatus.NOT_DELIVERED);
        return documentCodeSequenceService.makeSureDocumentCodeSequenceExist(SupplierContract.ENTITY_NAME, "%04d")
            .then(Mono.defer(() -> documentCodeSequenceService.getByDocumentType(SupplierContract.ENTITY_NAME)
                .flatMap(sequence -> {
                    LocalDate currentDate = LocalDate.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMyy");
                    currentDate.format(formatter);
                    var nextCode = sequence.getNextAndIncrement();
                    var formatedDate = currentDate.format(formatter);

                    var code = "HDM" + formatedDate + "/" + nextCode;
                    supplierContractDTO.setContractCode(code);
                    if (supplierContractDTO.getSupplierContractDetails() == null) {
                        supplierContractDTO.setSupplierContractDetails(new ArrayList<>());
                    }
                    supplierContractDTO.setIsDeleted(false);
                    return documentCodeSequenceService.updateSequence(sequence).then(
                        supplierContractRepository.save(supplierContractMapper.toEntity(supplierContractDTO))
                        .map(supplierContractMapper::toDto)
                        .flatMap(savedSupplierContract -> {
                            var entityDetails = supplierContractDetailMapper.toEntity(new ArrayList<>(supplierContractDTO.getSupplierContractDetails()));
                            entityDetails.forEach(detail -> detail.setSupplierContractId(savedSupplierContract.getId()));
                            return supplierContractDetailRepository.saveAll(entityDetails).collectList()
                                .flatMap(details -> {
                                    savedSupplierContract.setSupplierContractDetails(supplierContractDetailMapper.toDto(details));
                                    if (supplierContractDTO.getRequestApprovals() == null){
                                        supplierContractDTO.setRequestApprovals(new ArrayList<>());
                                    }
                                    var requestApprovalEntity = requestApprovalMapper.toEntity(new ArrayList<>(supplierContractDTO.getRequestApprovals()));
                                    requestApprovalEntity.forEach(requestApproval -> requestApproval.setDocumentId(savedSupplierContract.getId()));
                                    return requestApprovalRepository.saveAll(requestApprovalEntity)
                                        .collectList()
                                        .flatMap(requestApprovals -> {
                                            supplierContractDTO.setRequestApprovals(requestApprovalMapper.toDto(requestApprovals));
                                            if (supplierContractDTO.getLiquidationRequestApprovals() == null){
                                                supplierContractDTO.setLiquidationRequestApprovals(new ArrayList<>());
                                            }
                                            var liquidationEntity = requestApprovalMapper.toEntity(new ArrayList<>());
                                            liquidationEntity.forEach(lr -> {
                                                lr.setDocumentId(savedSupplierContract.getId());
                                                lr.setGroupRequest(SupplierContract.DEFAULT_LIQUIDATION_REQUEST_APPROVAL_GROUP);
                                            });
                                            return requestApprovalRepository.saveAll(liquidationEntity)
                                                .collectList()
                                                .defaultIfEmpty(new ArrayList<>())
                                                .flatMap(liquidationRequestApprovals -> {
                                                    supplierContractDTO.setLiquidationRequestApprovals(requestApprovalMapper.toDto(liquidationRequestApprovals));
                                                    return autoCreateDeliverySchedule(
                                                        savedSupplierContract.getId(),
                                                        savedSupplierContract.getStatus(),
                                                        supplierContractMapper.toEntity(savedSupplierContract)
                                                    ).flatMap(Mono::just);
                                                });
                                        });
                                });
                        }));
                }))
            );
    }

    public Mono<Void> setStatus(UUID id, ContractStatus status) {
        return supplierContractRepository.findById(id)
                .flatMap(deliverySchedule -> {
                    deliverySchedule.setStatus(status);
                    return supplierContractRepository.save(deliverySchedule.setIsPersisted()).then();
                });
    }

    public Mono<SupplierContractDTO> sendForApproval(UUID id) {
        return SecurityUtils.getUserJWTDetail().flatMap(login -> {
            return supplierContractRepository.findById(id)
                .flatMap(supplierContract -> {
                    if (supplierContract.getStatus() != null) {
                        if (!(supplierContract.getStatus().equals(ContractStatus.NEW) || supplierContract.getStatus().equals(ContractStatus.REJECTED))) {
                            return Mono.error(new BadRequestAlertException("Can not send for approval because not in new status", "supplierContract", "INVALID_STATUS"));
                        }
                        if (!supplierContract.getCreatedBy().equals(login.getUserId().toString())) {
                            return Mono.error(new BadRequestAlertException("Can not send for approval because not created by you", "supplierContract", "INVALID_USER"));
                        }
                    }
                    supplierContract.setStatus(ContractStatus.WAITING_APPROVE);
                    return supplierContractRepository.save(supplierContract.setIsPersisted())
                        .map(supplierContractMapper::toDto);
                });
        });
    }

    public Mono<SupplierContractDTO> requestLiquidation(UUID id) {
        return supplierContractRepository.findById(id)
            .flatMap(supplierContract -> {
                if (supplierContract.getStatus() != null) {
                    if (supplierContract.getStatus().equals(ContractStatus.NEW)
                        || supplierContract.getStatus().equals(ContractStatus.REJECTED)
                        || supplierContract.getStatus().equals(ContractStatus.CANCELLED)
                        || supplierContract.getStatus().equals(ContractStatus.WAITING_APPROVE)
                        || supplierContract.getStatus().equals(ContractStatus.WAITING_LIQUIDATION)) {
                        return Mono.error(new BadRequestAlertException("Contract is not in a valid state", "supplierContract", "INVALID_CONTRACT_STATE"));
                    }
                }
                supplierContract.setStatus(ContractStatus.WAITING_LIQUIDATION);
                return supplierContractRepository.save(supplierContract.setIsPersisted()).map(supplierContractMapper::toDto);
            });
    }

    // approve
    public Mono<SupplierContractDTO> approve(UUID id, RequestApprovalDTO requestApprovalDTO) {
        if (requestApprovalDTO.getApprovedSign() == null) {
            return Mono.error(new BadRequestAlertException("Approved sign is required", "supplierContract", "APPROVED_SIGN_REQUIRED"));
        }
        return SecurityUtils.getUserJWTDetail().flatMap(login -> {
            return supplierContractRepository.findById(id)
                .flatMap(supplierContract -> {
                    // if (supplierContract.getStatus() != null) {
                    //     if (!supplierContract.getStatus().equals(ContractStatus.WAITING_APPROVE)) {
                    //         return Mono.error(new BadRequestAlertException("Can not approve because not in waiting approve status", "supplierContract", "INVALID_STATUS"));
                    //     }
                    // }
                    return requestApprovalRepository.findByDocumentIdAndIsDeletedAndGroup(id, false, RequestApproval.DEFAULT_GROUP)
                        .collectList()
                        .defaultIfEmpty(new ArrayList<>())
                        .flatMap(requestApprovals -> {
                            if (requestApprovals.isEmpty()) {
                                return Mono.error(new BadRequestAlertException("Can not approve because not found request approval", "supplierContract", "NOT_FOUND_REQUEST_APPROVAL"));
                            }
                            var myRequest = requestApprovals.stream().filter(r -> r.getEmployeeId().equals(login.getUserId())).findFirst().orElse(null);
                            if (myRequest == null) {
                                return Mono.error(new BadRequestAlertException("Can not approve because not found your request approval", "supplierContract", "NOT_FOUND_YOUR_REQUEST_APPROVAL"));
                            }
                            requestApprovals.remove(myRequest);
                            myRequest.setApprovedSign(requestApprovalDTO.getApprovedSign());
                            myRequest.setApprovedSignName(requestApprovalDTO.getApprovedSignName());
                            myRequest.setResult(true);
                            myRequest.setIsPersisted();
                            var isAllApproved = requestApprovals.stream().allMatch(r -> r.getApprovedSign() != null);
                            if (!isAllApproved) {
                                return requestApprovalRepository.save(myRequest).then(Mono.just(supplierContract).map(supplierContractMapper::toDto));
                            }
                            supplierContract.setStatus(ContractStatus.APPROVED);
                            supplierContract.setIsPersisted();
                            return requestApprovalRepository.save(myRequest).then(supplierContractRepository.save(supplierContract))
                                .map(supplierContractMapper::toDto).flatMap(approvedContract -> {
                                    List<SupplierContractDetailDTO> contractDetails =
                                        approvedContract.getSupplierContractDetails() == null
                                            ? new ArrayList<>()
                                            : new ArrayList<>(approvedContract.getSupplierContractDetails());

                                    List<DeliveryDetailDTO> deliveryDetails = new ArrayList<>();

                                    contractDetails.forEach(detail -> {
                                        var deliveryDetail = new DeliveryDetailDTO();
                                        deliveryDetail.setQuantity(detail.getQuantity().intValue());
                                        deliveryDetail.setPrice(detail.getPrice());
                                        deliveryDetail.setUomId(detail.getUnitId());
                                        deliveryDetail.setContractMaterialId(detail.getSupplyItemId());
                                        deliveryDetail.setDeliveryDate(approvedContract.getDeliveryEstDate());
                                        deliveryDetail.setSupplierContractId(approvedContract.getId());
                                        deliveryDetails.add(deliveryDetail);
                                    });
                                    var entity = deliveryDetailMapper.toEntity(deliveryDetails);
                                    return deliveryDetailRepository.saveAll(entity).collectList().map(deliveryDetailMapper::toDto)
                                        .flatMap(deliveryDetailDTOS -> {
                                            return Mono.just(approvedContract);
                                        });
                                });
                        });
                });
        });
    }

    // reject
    public Mono<SupplierContractDTO> reject(UUID id, RequestApprovalDTO requestApprovalDTO) {
        if (requestApprovalDTO.getRejectNote() == null || requestApprovalDTO.getRejectNote().isEmpty()) {
            return Mono.error(new BadRequestAlertException("Reject note is required", "supplierContract", "REJECT_NOTE_REQUIRED"));
        }
        return SecurityUtils.getUserJWTDetail().flatMap(login -> {
            return supplierContractRepository.findById(id)
                .flatMap(supplierContract -> {
                    // if (supplierContract.getStatus() != null) {
                    //     if (!supplierContract.getStatus().equals(ContractStatus.WAITING_APPROVE)) {
                    //         return Mono.error(new BadRequestAlertException("Can not reject because not in waiting approve status", "supplierContract", "INVALID_STATUS"));
                    //     }
                    // }
                    return requestApprovalRepository.findByDocumentIdAndIsDeletedAndGroup(id, false, RequestApproval.DEFAULT_GROUP)
                        .collectList()
                        .defaultIfEmpty(new ArrayList<>())
                        .flatMap(requestApprovals -> {
                            if (requestApprovals.isEmpty()) {
                                return Mono.error(new BadRequestAlertException("Can not reject because not found request approval", "supplierContract", "NOT_FOUND_REQUEST_APPROVAL"));
                            }
                            var myRequest = requestApprovals.stream().filter(r -> r.getEmployeeId().equals(login.getUserId())).findFirst().orElse(null);
                            if (myRequest == null) {
                                return Mono.error(new BadRequestAlertException("Can not reject because not found your request approval", "supplierContract", "NOT_FOUND_YOUR_REQUEST_APPROVAL"));
                            }
                            requestApprovals.remove(myRequest);
                            myRequest.setRejectNote(requestApprovalDTO.getRejectNote());
                            myRequest.setResult(false);
                            myRequest.setIsPersisted();
                            supplierContract.setStatus(ContractStatus.REJECTED);
                            supplierContract.setIsPersisted();
                            return requestApprovalRepository.save(myRequest).then(supplierContractRepository.save(supplierContract))
                                .map(supplierContractMapper::toDto);
                        });
                });
        });
    }

    /**
     * Update a supplierContract.
     *
     * @param supplierContractDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<SupplierContractDTO> update(SupplierContractDTO supplierContractDTO) {
        LOG.debug("Request to update SupplierContract : {}", supplierContractDTO);
        return supplierContractRepository
                .save(supplierContractMapper.toEntity(supplierContractDTO).setIsPersisted())
                .map(supplierContractMapper::toDto);
    }

    /**
     * Partially update a supplierContract.
     *
     * @param supplierContractDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<SupplierContractDTO> partialUpdate(SupplierContractDTO supplierContractDTO) {
    LOG.debug("Request to partially update SupplierContract : {}", supplierContractDTO);

    return SecurityUtils.getUserJWTDetail().flatMap(login -> supplierContractRepository
        .findById(supplierContractDTO.getId(), login.getCompanyId())
        .flatMap(existingSupplierContract -> {
            supplierContractMapper.partialUpdate(existingSupplierContract, supplierContractDTO);
            return supplierContractRepository.save(existingSupplierContract.setIsPersisted())
                .flatMap(savedSupplierContract -> {
                    List<SupplierContractDetail> newDetails = supplierContractDTO.getSupplierContractDetails().stream().map(dto -> {
                        var entity = supplierContractDetailMapper.toEntity(dto);
                        entity.setSupplierContractId(savedSupplierContract.getId());
                        entity.setId(UUID.randomUUID());
                        return entity;
                    }).toList();
                    return supplierContractDetailRepository.deleteBySupplierContract(savedSupplierContract.getId(), login.getUserId().toString(), login.getCompanyId())
                        .then(Mono.defer(() -> {
                            return supplierContractDetailRepository.saveAll(newDetails)
                                .collectList()
                                .flatMap(details -> {
                                    savedSupplierContract.setSupplierContractDetails(new HashSet<>(details));
                                    return autoCreateDeliverySchedule(
                                        savedSupplierContract.getId(),
                                        savedSupplierContract.getStatus(),
                                        savedSupplierContract
                                    ).flatMap(scd -> {
                                        if (savedSupplierContract.getStatus().equals(ContractStatus.NEW) || savedSupplierContract.getStatus().equals(ContractStatus.REJECTED)) {
                                            return requestApprovalRepository.deleteAllByDocumentIdAndCompany(savedSupplierContract.getId(), login.getCompanyId(), login.getUserId().toString())
                                                .then(Mono.defer(() -> {
                                                    if (supplierContractDTO.getRequestApprovals() == null) {
                                                        supplierContractDTO.setRequestApprovals(new ArrayList<>());
                                                    }
                                                    var requestApprovalEntity = requestApprovalMapper.toEntity(new ArrayList<>(supplierContractDTO.getRequestApprovals()));
                                                    requestApprovalEntity.forEach(requestApproval -> requestApproval.setDocumentId(scd.getId()));
                                                    return requestApprovalRepository.saveAll(requestApprovalEntity)
                                                        .collectList()
                                                        .flatMap(requestApprovals -> {
                                                            supplierContractDTO.setRequestApprovals(requestApprovalMapper.toDto(requestApprovals));
                                                            return updateLiquidationRequests(supplierContractDTO, scd.getId(), login);
                                                        });

                                                }));
                                        } else {
                                            return updateLiquidationRequests(supplierContractDTO, scd.getId(), login);
                                        }
                                    });
                                });
                        }));
                });
        }));
    }

    private Mono<SupplierContractDTO> updateLiquidationRequests(SupplierContractDTO supplierContractDTO, UUID contractId, UserJWTDetail login) {
        return requestApprovalRepository
            .deleteAllByDocumentIdAndGroupAndCompany(
                contractId,
                SupplierContract.DEFAULT_LIQUIDATION_REQUEST_APPROVAL_GROUP,
                login.getUserId().toString(),
                login.getCompanyId()
            )
            .then(Mono.defer(() -> {
                var liquidationRequest = supplierContractDTO.getLiquidationRequestApprovals();
                if (liquidationRequest == null) {
                    liquidationRequest = new ArrayList<>();
                }
                var liquidationRequestEntity = requestApprovalMapper.toEntity(new ArrayList<>(liquidationRequest));
                liquidationRequestEntity.forEach(lr -> {
                    lr.setDocumentId(contractId);
                    lr.setGroupRequest(SupplierContract.DEFAULT_LIQUIDATION_REQUEST_APPROVAL_GROUP);
                });
                return requestApprovalRepository.saveAll(liquidationRequestEntity)
                    .collectList()
                    .flatMap(liquidationRequestApprovals -> {
                        supplierContractDTO.setLiquidationRequestApprovals(requestApprovalMapper.toDto(liquidationRequestApprovals));
                        return Mono.just(supplierContractDTO);
                    });
            }));
    }

    public Mono<SupplierContractDTO> approveLiquidation(UUID id, RequestApprovalDTO requestApprovalDTO){
        if (requestApprovalDTO.getApprovedSign() == null) {
            return Mono.error(new BadRequestAlertException("Approved sign is required", "supplierContract", "APPROVED_SIGN_REQUIRED"));
        }
        return SecurityUtils.getUserJWTDetail().flatMap(login -> {
            return supplierContractRepository.findById(id)
                .flatMap(supplierContract -> {
                    // if (supplierContract.getStatus() != null) {
                    //     if (!supplierContract.getStatus().equals(ContractStatus.WAITING_LIQUIDATION)) {
                    //         return Mono.error(new BadRequestAlertException("Can not approve because not in waiting liquidation status", "supplierContract", "INVALID_STATUS"));
                    //     }
                    // }
                    return requestApprovalRepository.findByDocumentIdAndIsDeletedAndGroup(id, false, SupplierContract.DEFAULT_LIQUIDATION_REQUEST_APPROVAL_GROUP)
                        .collectList()
                        .defaultIfEmpty(new ArrayList<>())
                        .flatMap(requestApprovals -> {
                            if (requestApprovals.isEmpty()) {
                                return Mono.error(new BadRequestAlertException("Can not approve because not found request approval", "supplierContract", "NOT_FOUND_REQUEST_APPROVAL"));
                            }
                            var myRequest = requestApprovals.stream().filter(r -> r.getEmployeeId().equals(login.getUserId())).findFirst().orElse(null);
                            if (myRequest == null) {
                                return Mono.error(new BadRequestAlertException("Can not approve because not found your request approval", "supplierContract", "NOT_FOUND_YOUR_REQUEST_APPROVAL"));
                            }
                            requestApprovals.remove(myRequest);
                            myRequest.setApprovedSign(requestApprovalDTO.getApprovedSign());
                            myRequest.setApprovedSignName(requestApprovalDTO.getApprovedSignName());
                            myRequest.setResult(true);
                            myRequest.setIsPersisted();
                            var isAllApproved = requestApprovals.stream().allMatch(r -> r.getApprovedSign() != null);
                            if (!isAllApproved) {
                                return requestApprovalRepository.save(myRequest).then(Mono.just(supplierContract).map(supplierContractMapper::toDto));
                            }
                            supplierContract.setStatus(ContractStatus.LIQUIDATED);
                            supplierContract.setIsPersisted();
                            return requestApprovalRepository.save(myRequest).then(supplierContractRepository.save(supplierContract))
                                .map(supplierContractMapper::toDto);
                        });
                });
        });
    }

    public Mono<SupplierContractDTO> rejectLiquidation(UUID id, RequestApprovalDTO requestApprovalDTO){
        if (requestApprovalDTO.getRejectNote() == null || requestApprovalDTO.getRejectNote().isEmpty()) {
            return Mono.error(new BadRequestAlertException("Reject note is required", "supplierContract", "REJECT_NOTE_REQUIRED"));
        }
        return SecurityUtils.getUserJWTDetail().flatMap(login -> {
            return supplierContractRepository.findById(id)
                .flatMap(supplierContract -> {
                    // if (supplierContract.getStatus() != null) {
                    //     if (!supplierContract.getStatus().equals(ContractStatus.WAITING_LIQUIDATION)) {
                    //         return Mono.error(new BadRequestAlertException("Can not reject because not in waiting liquidation status", "supplierContract", "INVALID_STATUS"));
                    //     }
                    // }
                    return requestApprovalRepository.findByDocumentIdAndIsDeletedAndGroup(id, false, SupplierContract.DEFAULT_LIQUIDATION_REQUEST_APPROVAL_GROUP)
                        .collectList()
                        .defaultIfEmpty(new ArrayList<>())
                        .flatMap(requestApprovals -> {
                            if (requestApprovals.isEmpty()) {
                                return Mono.error(new BadRequestAlertException("Can not reject because not found request approval", "supplierContract", "NOT_FOUND_REQUEST_APPROVAL"));
                            }
                            var myRequest = requestApprovals.stream().filter(r -> r.getEmployeeId().equals(login.getUserId())).findFirst().orElse(null);
                            if (myRequest == null) {
                                return Mono.error(new BadRequestAlertException("Can not reject because not found your request approval", "supplierContract", "NOT_FOUND_YOUR_REQUEST_APPROVAL"));
                            }
                            requestApprovals.remove(myRequest);
                            myRequest.setRejectNote(requestApprovalDTO.getRejectNote());
                            myRequest.setResult(false);
                            myRequest.setIsPersisted();
                            supplierContract.setStatus(ContractStatus.REJECTED_LIQUIDATION);
                            supplierContract.setIsPersisted();
                            return requestApprovalRepository.save(myRequest).then(supplierContractRepository.save(supplierContract))
                                .map(supplierContractMapper::toDto);
                        });
                });
        });
    }

    public Mono<SupplierContractCriteria> calculateCriteria(SupplierContractCriteria criteria) {
        return SecurityUtils.getUserJWTDetail().map(user -> {
//
//            if (user.isHasAbove(AuthoritiesConstants.DIRECTOR) || user.getGroupId().equals("SALE")) {
//                return criteria;
//            }
//            if (user.isHasPermission("PERMISSION.SUPPLIER_CONTRACT.READ_ALL")) {
//                return criteria;
//            }
//            if (user.isHasAbove(AuthoritiesConstants.DEPARTMENT_MANAGER)) {
//                //Trưởng phòng chỉ thấy được chứng từ của phòng mình
//                criteria.department().setContains(user.getGroupId());
//                return criteria;
//            }
//            //Nhân viên chỉ thấy được chứng từ của mình
//            criteria.createdBy().setEquals(user.getUserId().toString());
            return criteria;
        });
    }

    /**
     * Find supplierContracts by Criteria.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<SupplierContractDTO> findByCriteria(SupplierContractCriteria criteria, Pageable pageable) {
        LOG.debug("Request to get all SupplierContracts by Criteria");
        StringFilter deletedByFilter = new StringFilter();
        deletedByFilter.setSpecified(false);
        criteria.setDeletedBy(deletedByFilter);
        ZonedDateTimeFilter deletedAtFilter = new ZonedDateTimeFilter();
        deletedAtFilter.setSpecified(false);
        criteria.setDeletedAt(deletedAtFilter);
        return SecurityUtils.getUserJWTDetail().flatMapMany(login -> {
            StringFilter companyFilter = new StringFilter();
            companyFilter.setEquals(login.getCompanyId());
            criteria.setCompany(companyFilter);
            return this.calculateCriteria(criteria)
                .flatMapMany(c -> supplierContractRepository.findByCriteria(c, pageable).map(supplierContractMapper::toDto))
                .collectList()
                .flatMapMany(supplierContractDTOS -> {
                    var listIds = supplierContractDTOS.stream().map(SupplierContractDTO::getId).toList();
                    if (listIds.isEmpty()) {
                        return Flux.fromIterable(supplierContractDTOS);
                    }
                    return supplierContractDetailRepository.findBySupplierContract(new ArrayList<>(listIds), login.getCompanyId(), null)
                        .collectList()
                        .flatMapMany(details -> {
                            supplierContractDTOS.forEach(supplierContractDTO -> {
                                var detailList = details.stream().filter(d -> d.getSupplierContractId().equals(supplierContractDTO.getId())).toList();
                                supplierContractDTO.setSupplierContractDetails(supplierContractDetailMapper.toDto(detailList));
                            });
                            return requestApprovalRepository.findByDocumentIdIn(listIds)
                                .collectList()
                                .map(requestApprovalMapper::toDto)
                                .flatMapMany(requestApprovalDTOS -> {
                                       Map<UUID, List<RequestApprovalDTO>> groupedApprovals = requestApprovalDTOS.stream()
                                           .collect(Collectors.groupingBy(RequestApprovalDTO::getDocumentId));
                                   supplierContractDTOS.forEach(sc -> {
                                       var thisRequest = groupedApprovals.getOrDefault(sc.getId(), new ArrayList<>());
                                       var groupedRequest = thisRequest.stream().collect(Collectors.groupingBy(RequestApprovalDTO::getGroupRequest));
                                       List<RequestApprovalDTO> defaultGroupApprovals = groupedRequest.getOrDefault(RequestApproval.DEFAULT_GROUP, new ArrayList<>());
                                       List<RequestApprovalDTO> otherGroupApprovals = groupedRequest.entrySet().stream()
                                           .filter(entry -> !entry.getKey().equals(RequestApproval.DEFAULT_GROUP))
                                           .flatMap(entry -> entry.getValue().stream())
                                           .collect(Collectors.toList());
                                       sc.setRequestApprovals(defaultGroupApprovals);
                                       sc.setLiquidationRequestApprovals(otherGroupApprovals);
                                   });
                                      return Flux.fromIterable(supplierContractDTOS);
                                });
                        });
                });
        });
    }

    /**
     * Find the count of supplierContracts by criteria.
     *
     * @param criteria filtering criteria
     * @return the count of supplierContracts
     */
    public Mono<Long> countByCriteria(SupplierContractCriteria criteria) {
        LOG.debug("Request to get the count of all SupplierContracts by Criteria");
        return supplierContractRepository.countByCriteria(criteria);
    }

    /**
     * Returns the number of supplierContracts available.
     *
     * @return the number of entities in the database.
     */
    public Mono<Long> countAll() {
        return supplierContractRepository.count();
    }

    /**
     * Get one supplierContract by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<SupplierContractDTO> findOne(UUID id) {
        LOG.debug("Request to get SupplierContract : {}", id);
        return SecurityUtils.getUserJWTDetail().flatMap(login -> {
            return supplierContractRepository.findById(id, login.getCompanyId()).map(supplierContractMapper::toDto)
                .flatMap(supplierContract ->{
                    return supplierContractDetailRepository.findByListSupplierContractId(List.of(id), login.getCompanyId(), null)
                        .collectList()
                        .flatMap(details -> {
                            supplierContract.setSupplierContractDetails(supplierContractDetailMapper.toDto(details));
                            return requestApprovalRepository.findAllByDocumentIdAndIsDeletedIsFalse(id)
                                .collectList()
                                .defaultIfEmpty(new ArrayList<>())
                                .flatMap(requestApprovals -> {

                                    Map<String, List<RequestApproval>> groupedApprovals = requestApprovals.stream()
                                        .collect(Collectors.groupingBy(RequestApproval::getGroupRequest));

                                    List<RequestApproval> defaultGroupApprovals = groupedApprovals.getOrDefault(RequestApproval.DEFAULT_GROUP, new ArrayList<>());
                                    List<RequestApproval> otherGroupApprovals = groupedApprovals.entrySet().stream()
                                        .filter(entry -> !entry.getKey().equals(RequestApproval.DEFAULT_GROUP))
                                        .flatMap(entry -> entry.getValue().stream())
                                        .collect(Collectors.toList());

                                    supplierContract.setRequestApprovals(requestApprovalMapper.toDto(defaultGroupApprovals));
                                    supplierContract.setLiquidationRequestApprovals(requestApprovalMapper.toDto(otherGroupApprovals));

                                    IncomingInvoiceQuery query = new IncomingInvoiceQuery();
                                    query.setSupplierContractId(id);
                                    query.setCompanyId(login.getCompanyId());
                                    return incomingInvoiceRepository.findAllBy(query, null)
                                        .map(incomingInvoiceMapper::toDto)
                                        .collectList()
                                        .defaultIfEmpty(new ArrayList<>())
                                        .doOnError(e -> LOG.error("Error when get incoming invoices", e))
                                        .flatMap(incomingInvoices -> {
                                            supplierContract.setIncomingInvoices(incomingInvoices);
                                            return Mono.just(supplierContract);
                                        });
                                });
                        });
                });
        });
    }

    /**
     * Delete the supplierContract by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(UUID id) {
        LOG.debug("Request to delete SupplierContract : {}", id);
        return supplierContractRepository.findById(id).flatMap(supplierContract -> supplierContract.deleteAsync().then(supplierContractRepository.save(supplierContract.setIsPersisted())).then());
    }

    public  Mono<Void> updateExpiredContracts() {
        return supplierContractRepository.updateExpiredContracts();
    }

    public Mono<SupplierContractDTO> cancel(UUID id) {
        return SecurityUtils.getUserJWTDetail().flatMap(login -> {
            return supplierContractRepository.findById(id)
                .flatMap(supplierContract -> {
                    // if (supplierContract.getStatus() != null) {
                    //     if (!supplierContract.getStatus().equals(ContractStatus.NEW)) {
                    //         return Mono.error(new BadRequestAlertException("Can not cancel because not in approved status", "supplierContract", "INVALID_STATUS"));
                    //     }
                    // }
                    if (!supplierContract.getCreatedBy().equals(login.getUserId().toString())) {
                        return Mono.error(new BadRequestAlertException("Can not cancel because not created by you", "supplierContract", "INVALID_USER"));
                    }
                    supplierContract.setStatus(ContractStatus.CANCELLED);
                    return supplierContractRepository.save(supplierContract.setIsPersisted()).map(supplierContractMapper::toDto);
                });
        });
    }

    public Mono<SupplierContractDTO> updateStatus(UUID id, ContractStatus status) {
        return SecurityUtils.getUserJWTDetail().flatMap(login -> {
            return supplierContractRepository.findById(id, login.getCompanyId()).flatMap(supplierContract -> {
                supplierContract.setStatus(status);
                supplierContract.setIsPersisted();
                return supplierContractRepository.save(supplierContract).flatMap(sc -> {
                    if (sc.getId() != null) {
                        return this.findOne(sc.getId()).flatMap(savedContract -> {
                            if (status.equals(ContractStatus.APPROVED)) {
                                return deliveryDetailRepository.countBySupplierContractId(id).flatMap(count -> {
                                    if (count > 0) {
                                        return Mono.just(savedContract);
                                    }
                                    List<SupplierContractDetailDTO> contractDetails = savedContract.getSupplierContractDetails() == null
                                        ? new ArrayList<>()
                                        : new ArrayList<>(savedContract.getSupplierContractDetails());

                                    List<DeliveryDetailDTO> deliveryDetails = new ArrayList<>();

                                    contractDetails.forEach(detail -> {
                                        LOG.info("Create delivery detail for contract detail {}", detail);
                                        var deliveryDetail = new DeliveryDetailDTO();
                                        deliveryDetail.setQuantity(detail.getQuantity().intValue());
                                        deliveryDetail.setPrice(detail.getPrice());
                                        deliveryDetail.setUomId(detail.getUnitId());
                                        deliveryDetail.setContractMaterialId(detail.getSupplyItemId());
                                        deliveryDetail.setDeliveryDate(supplierContract.getDeliveryEstDate());
                                        deliveryDetail.setSupplierContractId(supplierContract.getId());
                                        deliveryDetail.setContractDetailId(detail.getId());
                                        deliveryDetail.setNote(detail.getNote());
                                        deliveryDetails.add(deliveryDetail);
                                    });

                                    return deliveryDetailRepository.saveAll(deliveryDetailMapper.toEntity(deliveryDetails))
                                        .collectList()
                                        .map(deliveryDetailMapper::toDto)
                                        .flatMap(deliveryDetailDTOS -> {
                                            return Mono.just(supplierContract).map(supplierContractMapper::toDto);
                                        });
                                });
                            }
                        return Mono.just(supplierContract).map(supplierContractMapper::toDto);
                        });
                    }
                    return Mono.just(supplierContractMapper.toDto(supplierContract));
                });
            });
        });
    }

    private Mono<SupplierContractDTO> autoCreateDeliverySchedule(UUID id, ContractStatus status, SupplierContract supplierContract) {
        if (status.equals(ContractStatus.APPROVED)) {
            return deliveryDetailRepository.countBySupplierContractId(id).flatMap(count -> {
                if (count > 0) {
                    return Mono.just(supplierContract).map(supplierContractMapper::toDto);
                }
                List<SupplierContractDetail> contractDetails = supplierContract.getSupplierContractDetails() == null
                    ? new ArrayList<>()
                    : new ArrayList<>(supplierContract.getSupplierContractDetails());

                List<DeliveryDetail> deliveryDetails = new ArrayList<>();

                contractDetails.forEach(detail -> {
                    LOG.info("Create delivery detail for contract detail {}", detail);
                    var deliveryDetail = new DeliveryDetail();
                    deliveryDetail.setQuantity(detail.getQuantity().intValue());
                    deliveryDetail.setPrice(detail.getPrice());
                    deliveryDetail.setUomId(detail.getUnitId());
                    deliveryDetail.setContractMaterialId(detail.getSupplyItemId());
                    deliveryDetail.setDeliveryDate(supplierContract.getDeliveryEstDate());
                    deliveryDetail.setSupplierContractId(supplierContract.getId());
                    deliveryDetail.setContractDetailId(detail.getId());
                    deliveryDetail.setNote(detail.getNote());
                    deliveryDetails.add(deliveryDetail);
                });
                return deliveryDetailRepository.saveAll(deliveryDetails)
                    .collectList()
                    .map(deliveryDetailMapper::toDto)
                    .flatMap(deliveryDetailDTOS -> {
                        return Mono.just(supplierContract).map(supplierContractMapper::toDto);
                    });
            });
        }
        return Mono.just(supplierContract).map(supplierContractMapper::toDto);
    }

}
