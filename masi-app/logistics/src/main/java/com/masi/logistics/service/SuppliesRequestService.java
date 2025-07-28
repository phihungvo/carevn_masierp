package com.masi.logistics.service;

import com.carevn.masi.dto.EmployeeDTO;
import com.carevn.masi.utils.CSV.CSVUtils;
import com.carevn.masi.utils.SecurityUtils;
import com.masi.logistics.domain.SuppliesRequest;
import com.masi.logistics.domain.criteria.SupplierContractCriteria;
import com.masi.logistics.domain.criteria.SuppliesItemCriteria;
import com.masi.logistics.domain.criteria.SuppliesRequestCriteria;
import com.masi.logistics.domain.enumeration.RequestStatus;
import com.masi.logistics.repository.*;
import com.masi.logistics.service.dto.RequestApprovalDTO;
import com.masi.logistics.service.dto.SuppliesItemDTO;
import com.masi.logistics.service.dto.SuppliesRequestDTO;
import com.masi.logistics.service.mapper.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import com.masi.logistics.service.web.client.EmployeeClient;
import com.masi.logistics.web.rest.errors.BadRequestAlertException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.service.filter.StringFilter;
import tech.jhipster.service.filter.UUIDFilter;
import tech.jhipster.service.filter.ZonedDateTimeFilter;

/**
 * Service Implementation for managing {@link com.masi.logistics.domain.SuppliesRequest}.
 */
@Service
@Transactional
public class SuppliesRequestService {

    private static final Logger log = LoggerFactory.getLogger(SuppliesRequestService.class);

    private final SuppliesRequestRepository suppliesRequestRepository;
    private final SupplierDetailRepository supplierDetailRepository;
    private final SuppliesItemRepository suppliesItemRepository;
    private RequestApprovalService requestApprovalService;
    private final RequestApprovalRepository requestApprovalRepository;
    private final EmployeeClient employeeClient;
    private final DocumentCodeSequenceService documentCodeSequenceService;

    private final SuppliesRequestMapper suppliesRequestMapper;
    private final SupplierDetailMapper supplierDetailMapper;
    private final RequestApprovalMapper requestApprovalMapper;
    private final SuppliesItemMapper suppliesItemMapper;
    private final SupplierContractMapper supplierContractMapper;
    private final SupplierContractRepository supplierContractRepository;

    @Autowired
    public void setRequestApprovalService(RequestApprovalService requestApprovalService) {
        this.requestApprovalService = requestApprovalService;
    }

    @Lazy
    public SuppliesRequestService(SuppliesRequestRepository suppliesRequestRepository, RequestApprovalRepository requestApprovalRepository, EmployeeClient employeeClient,
                                  DocumentCodeSequenceService documentCodeSequenceService,
                                  SuppliesRequestMapper suppliesRequestMapper,
                                  SuppliesItemRepository suppliesItemService,
                                  SuppliesItemMapper suppliesItemMapper,
                                  SupplierDetailRepository supplierDetailRepository,
                                  RequestApprovalService requestApprovalService,
                                  SupplierDetailMapper supplierDetailMapper,
                                  RequestApprovalMapper requestApprovalMapper, SupplierContractMapper supplierContractMapper, SupplierContractRepository supplierContractRepository) {
        this.suppliesRequestRepository = suppliesRequestRepository;
        this.requestApprovalRepository = requestApprovalRepository;
        this.employeeClient = employeeClient;
        this.documentCodeSequenceService = documentCodeSequenceService;
        this.suppliesRequestMapper = suppliesRequestMapper;
        this.suppliesItemRepository = suppliesItemService;
        this.suppliesItemMapper = suppliesItemMapper;
        this.requestApprovalService = requestApprovalService;
        this.supplierDetailMapper = supplierDetailMapper;
        this.requestApprovalMapper = requestApprovalMapper;
        this.supplierContractMapper = supplierContractMapper;
        this.supplierContractRepository = supplierContractRepository;
        Mono.delay(Duration.ofMinutes(3)).then(documentCodeSequenceService.makeSureDocumentCodeSequenceExist(SuppliesRequest.ENTITY_NAME, "%04d")).subscribe();
        this.supplierDetailRepository = supplierDetailRepository;
    }

    /**
     * Save a suppliesRequest.
     *
     * @param suppliesRequestDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<SuppliesRequestDTO> save(SuppliesRequestDTO suppliesRequestDTO) {
        log.debug("Request to save SuppliesRequest : {}", suppliesRequestDTO);
        var suppliesItemDTO = suppliesRequestDTO.getSuppliesItemDTO();
        var suppliesItemEntity = suppliesItemMapper.toEntity(new ArrayList<>(suppliesItemDTO));
        return documentCodeSequenceService.getByDocumentType(SuppliesRequest.ENTITY_NAME).flatMap(documentCodeSequence -> {
            var code = documentCodeSequence.getNextAndIncrement();
            String prefix = "ĐXMH";
            Date now = new Date();
            if (suppliesRequestDTO.getIsReview()){
                suppliesRequestDTO.setRequestStatus(RequestStatus.NEW);
            }
            else
            {
                suppliesRequestDTO.setRequestStatus(RequestStatus.APPROVED);
            }
            SimpleDateFormat monthFormat = new SimpleDateFormat("MM");
            SimpleDateFormat yearFormat = new SimpleDateFormat("yy");
            String month = monthFormat.format(now);
            String year = yearFormat.format(now);
            var requestNumber = String.format(prefix + "%s%s/%s", month, year,code );
            suppliesRequestDTO.setCode(code);
            suppliesRequestDTO.setRequestNumber(requestNumber);
            return documentCodeSequenceService.updateSequence(documentCodeSequence).then(Mono.defer(()->{
                return suppliesRequestRepository.save(suppliesRequestMapper.toEntity(suppliesRequestDTO))
                    .map(suppliesRequestMapper::toDto)
                    .flatMap(s -> {
                        suppliesItemEntity.forEach(suppliesItem -> suppliesItem.setIdSuppliesRequest(s.getId()));
                        return suppliesItemRepository.saveAll(suppliesItemEntity)
                            .map(suppliesItemMapper::toDto)
                            .collectList()
                            .flatMap(suppliesItemDTOList -> {
                                s.setSuppliesItemDTO(new ArrayList<>(suppliesItemDTOList));
                                var requestApprovalEntity = requestApprovalMapper.toEntity(new ArrayList<>(suppliesRequestDTO.getRequestApprovals()));
                                requestApprovalEntity.forEach(requestApproval -> requestApproval.setDocumentId(s.getId()));
                                return requestApprovalRepository.saveAll(requestApprovalEntity)
                                    .map(requestApprovalMapper::toDto)
                                    .collectList()
                                    .flatMap(requestApprovalDTOList -> {
                                        s.setRequestApprovals(new ArrayList<>(requestApprovalDTOList));
                                        return Mono.just(s);
                                    });
                            });
                    });
            }));
        });
    }

    /**
     * Update a suppliesRequest.
     *
     * @param suppliesRequestDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<SuppliesRequestDTO> update(SuppliesRequestDTO suppliesRequestDTO) {
        log.debug("Request to update SuppliesRequest : {}", suppliesRequestDTO);
        return suppliesRequestRepository
            .save(suppliesRequestMapper.toEntity(suppliesRequestDTO).setIsPersisted())
            .map(suppliesRequestMapper::toDto);
    }

    /**
     * Partially update a suppliesRequest.
     *
     * @param suppliesRequestDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<SuppliesRequestDTO> partialUpdate(SuppliesRequestDTO suppliesRequestDTO) {
        log.debug("Request to partially update SuppliesRequest : {}", suppliesRequestDTO);

        return SecurityUtils.getUserJWTDetail().flatMap(login -> {
            return suppliesRequestRepository
                .findById(suppliesRequestDTO.getId())
                .flatMap(existingSuppliesRequest -> {

                    if ((existingSuppliesRequest.getRequestStatus().equals(RequestStatus.WAITING_APPROVE)
                        || existingSuppliesRequest.getRequestStatus().equals(RequestStatus.CANCELLED)))
                    {
                        return Mono.error(new BadRequestAlertException("Can not update because request is in WAITING_APPROVE or CANCELLED status", "SuppliesRequest", "INVALID_STATUS"));
                    }
                    if (existingSuppliesRequest.getRequestStatus().equals(RequestStatus.APPROVED))
                    {
                        existingSuppliesRequest.setRemainingQuantity(suppliesRequestDTO.getRemainingQuantity());
                        existingSuppliesRequest.setDeliveredQuantity(suppliesRequestDTO.getDeliveredQuantity());
                    }
                    else{
                        suppliesRequestMapper.partialUpdate(existingSuppliesRequest, suppliesRequestDTO);
                    }
                    if ( existingSuppliesRequest.getRequestStatus() != null  && (existingSuppliesRequest.getRequestStatus().equals(RequestStatus.REJECTED))) {
                        if (existingSuppliesRequest.getIsReview()){
                            existingSuppliesRequest.setRequestStatus(RequestStatus.REJECTED);
                        }
                        else
                        {
                            existingSuppliesRequest.setRequestStatus(RequestStatus.APPROVED);
                        }
                    }
                    if (existingSuppliesRequest.getRequestStatus() != null && (existingSuppliesRequest.getRequestStatus().equals(RequestStatus.NEW))) {
                        if (existingSuppliesRequest.getIsReview()){
                            existingSuppliesRequest.setRequestStatus(RequestStatus.NEW);
                        }
                        else
                        {
                            existingSuppliesRequest.setRequestStatus(RequestStatus.APPROVED);
                        }
                    }
                    existingSuppliesRequest.setIsPersisted();

                    return Mono.just(existingSuppliesRequest);
                })
                .flatMap(suppliesRequestRepository::save)
                .map(suppliesRequestMapper::toDto).flatMap(s -> {
                    if (s.getRequestStatus().equals(RequestStatus.APPROVED)){
                        return Mono.just(s);
                    }
                    else {
                        return suppliesItemRepository.deleteBySuppliesRequestId(s.getId(), s.getCompany(), suppliesRequestDTO.getUpdatedBy())
                            .then(Mono.defer(() -> {
                                var suppliesItemDTO = suppliesRequestDTO.getSuppliesItemDTO();
                                var suppliesItemEntity = suppliesItemMapper.toEntity(new ArrayList<>(suppliesItemDTO));
                                suppliesItemEntity.forEach(suppliesItem -> suppliesItem.setIdSuppliesRequest(s.getId()));
                                return suppliesItemRepository.saveAll(suppliesItemEntity)
                                    .map(suppliesItemMapper::toDto)
                                    .collectList()
                                    .flatMap(suppliesItemDTOList -> {
                                        s.setSuppliesItemDTO(new ArrayList<>(suppliesItemDTOList));
                                        var entityRequestApproval = requestApprovalMapper.toEntity(new ArrayList<>(suppliesRequestDTO.getRequestApprovals()));
                                        return requestApprovalRepository.deleteAllByDocumentId(s.getId())
                                            .then(Mono.defer(() -> {
                                                    entityRequestApproval.forEach(requestApproval -> requestApproval.setDocumentId(s.getId()));
                                                    return requestApprovalRepository.saveAll(entityRequestApproval)
                                                        .map(requestApprovalMapper::toDto)
                                                        .collectList()
                                                        .flatMap(requestApprovalDTOList -> {
                                                            s.setRequestApprovals(new ArrayList<>(requestApprovalDTOList));
                                                            return Mono.just(s);
                                                        });
                                                })
                                            );
                                    });
                            }));
                    }
                });
        });
    }

    /**
     * Find suppliesRequests by Criteria.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<SuppliesRequestDTO> findByCriteria(SuppliesRequestCriteria criteria, Pageable pageable) {
        log.debug("Request to get all SuppliesRequests by Criteria");
        StringFilter deletedByFilter = new StringFilter();
        deletedByFilter.setSpecified(false); // Lọc các record có deleted_by IS NULL
        criteria.setDeletedBy(deletedByFilter);
        // Kiểm tra và xử lý criteria.getCode()
        Mono<SuppliesRequestCriteria> updatedCriteriaMono;
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
        return SecurityUtils.getUserJWTDetail().flatMapMany(login -> {

            return updatedCriteriaMono.flatMapMany(updatedCriteria -> {
            StringFilter companyFilter = new StringFilter();
            companyFilter.setEquals(login.getCompanyId());
                updatedCriteria.setCompany(companyFilter);
                return suppliesRequestRepository.findByCriteria(updatedCriteria, pageable)
                    .map(suppliesRequestMapper::toDto)
                    .collectList()
                    .flatMapMany(suppliesRequestDTO -> {
                        var listSuppliesRequestIds = suppliesRequestDTO.stream().map(SuppliesRequestDTO::getId).toList();
                        if (listSuppliesRequestIds.isEmpty()) {
                            listSuppliesRequestIds = new ArrayList<>();
                            listSuppliesRequestIds.add(UUID.fromString("00000000-0000-0000-0000-000000000000"));
                        }
                        List<UUID> finalListSuppliesRequestIds = listSuppliesRequestIds;
                        return requestApprovalRepository.findByDocumentIdIn(listSuppliesRequestIds)
                            .map(requestApprovalMapper::toDto).collectList()
                            .flatMapMany(ra -> {
                                suppliesRequestDTO.forEach(paymentRequestDTO -> {
                                    var requestApprovals = ra.stream()
                                        .filter(requestApprovalDTO -> requestApprovalDTO.getDocumentId().equals(paymentRequestDTO.getId()))
                                        .toList();
                                    paymentRequestDTO.setRequestApprovals(requestApprovals);
                                });

                                HashSet<UUID> listEmployeeIds = new HashSet<>();
                                suppliesRequestDTO.forEach(sr -> {
                                    try {
                                        UUID createdByUUID = UUID.fromString(sr.getCreatedBy());
                                        listEmployeeIds.add(createdByUUID);
                                    } catch (IllegalArgumentException e) {
                                        log.error("Error while parsing UUID from createdBy: {}", sr.getCreatedBy());
                                    }
                                    if (sr.getRequestApprovals() != null) {
                                        sr.getRequestApprovals()
                                            .forEach(requestApprovalDTO -> listEmployeeIds.add(requestApprovalDTO.getEmployeeId()));
                                    }
                                });

                                return employeeClient.getEmployeesByListIds(new ArrayList<>(listEmployeeIds))
                                    .collectList()
                                    .defaultIfEmpty(new ArrayList<>())
                                    .flatMapMany(employees -> {
                                        var mapEmployee = employees.stream()
                                            .collect(Collectors.toMap(EmployeeDTO::getId, x -> x));
                                        suppliesRequestDTO.forEach(srDTO -> {
                                            try {
                                                UUID createdByUUID = UUID.fromString(srDTO.getCreatedBy());
                                                srDTO.setCreatedByEmployeeId(createdByUUID);
                                                srDTO.setCreatedByEmployee(mapEmployee.get(createdByUUID));
                                            } catch (IllegalArgumentException e) {
                                                log.error("Error while set parsing UUID from createdBy: {}", srDTO.getCreatedBy());
                                            }

                                            if (srDTO.getRequestApprovals() != null) {
                                                srDTO.getRequestApprovals()
                                                    .forEach(requestApprovalDTO -> {
                                                        requestApprovalDTO.setEmployee(
                                                            mapEmployee.get(requestApprovalDTO.getEmployeeId()));
                                                    });
                                            }
                                        });
                                        UUIDFilter suppliesRequestIds = new UUIDFilter();
                                        suppliesRequestIds.setIn(new ArrayList<>(finalListSuppliesRequestIds));
                                        ZonedDateTimeFilter deletedAtFilter = new ZonedDateTimeFilter();
                                        deletedAtFilter.setSpecified(false);
                                        SuppliesItemCriteria suppliesItemCriteria = new SuppliesItemCriteria();
                                        suppliesItemCriteria.setIdSuppliesRequest(suppliesRequestIds);
                                        suppliesItemCriteria.setDeletedAt(deletedAtFilter);
                                        return suppliesItemRepository.findByCriteria(suppliesItemCriteria, null)
                                            .map(suppliesItemMapper::toDto)
                                            .collectList()
                                            .flatMapMany(details -> {
                                                var mapDetails = details.stream().collect(Collectors.toMap(
                                                    SuppliesItemDTO::getIdSuppliesRequest,
                                                    x -> new ArrayList<>(Collections.singletonList(x)),
                                                    (existing, replacement) -> {
                                                        existing.addAll(replacement);
                                                        return existing;
                                                    }
                                                ));
                                                suppliesRequestDTO.forEach(srDTO -> {
                                                    var detailsList = mapDetails.get(srDTO.getId());
                                                    if (detailsList != null) {
                                                        srDTO.setSuppliesItemDTO(detailsList);
                                                    }
                                                });
                                                return Flux.fromIterable(suppliesRequestDTO);
                                            });
                                    });
                            });
                    });
            });
        });
    }

    /**
     * Find the count of suppliesRequests by criteria.
     * @param criteria filtering criteria
     * @return the count of suppliesRequests
     */
    public Mono<Long> countByCriteria(SuppliesRequestCriteria criteria) {
        log.debug("Request to get the count of all SuppliesRequests by Criteria");
        Mono<SuppliesRequestCriteria> updatedCriteriaMono;
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
        return updatedCriteriaMono.flatMap(suppliesRequestRepository::countByCriteria
        );
    }

    /**
     * Returns the number of suppliesRequests available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return suppliesRequestRepository.count();
    }

    /**
     * Get one suppliesRequest by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<SuppliesRequestDTO> findOne(UUID id, String company) {
        log.debug("Request to get SuppliesRequest : {}", id);
        return SecurityUtils.getUserJWTDetail().flatMap(login -> {
            return suppliesRequestRepository.findById(id)
                .map(suppliesRequestMapper::toDto)
                .flatMap(suppliesRequestDTO -> suppliesItemRepository.findAllByIdSuppliesRequestAndCompanyAndIsDeletedIsFalse(suppliesRequestDTO.getId(), suppliesRequestDTO.getCompany())
                    .map(suppliesItemMapper::toDto)
                    .collectList()
                    .flatMap(suppliesItemDTOList -> Flux.fromIterable(suppliesItemDTOList)
                        .flatMap(suppliesItemDTO -> supplierDetailRepository.findAllBySupplierIdAndCompany(suppliesItemDTO.getSuppliesId(), suppliesRequestDTO.getCompany())
                            .map(supplierDetailMapper::toDto)
                            .collectList()
                            .flatMap(supplierDetailDTOList -> {
                                suppliesItemDTO.getSuppliers().setSuppliesDetails(new ArrayList<>(supplierDetailDTOList));
                                return Mono.just(suppliesItemDTO);
                            }).then(Mono.just(suppliesItemDTO))
                        )
                        .collectList()
                    )
                    .flatMap(updatedSuppliesItemDTOList -> {
                        suppliesRequestDTO.setSuppliesItemDTO(new ArrayList<>(updatedSuppliesItemDTOList));
                        return requestApprovalRepository.findAllByDocumentIdAndIsDeletedIsFalse(id).map(requestApprovalMapper::toDto)
                            .collectList()
                            .flatMap(requestApprovalDTOList -> {
                                suppliesRequestDTO.setRequestApprovals(new ArrayList<>(requestApprovalDTOList));
                                SupplierContractCriteria supplierContractCriteria = new SupplierContractCriteria();
                                UUIDFilter suppliesRequestIdFilter = new UUIDFilter();
                                suppliesRequestIdFilter.setEquals(id);
                                supplierContractCriteria.setSuppliesRequestId(suppliesRequestIdFilter);
                                ZonedDateTimeFilter deletedAtFilter = new ZonedDateTimeFilter();
                                deletedAtFilter.setSpecified(false);
                                supplierContractCriteria.setDeletedAt(deletedAtFilter);
                                StringFilter companyFilter = new StringFilter();
                                companyFilter.setEquals(company);
                                supplierContractCriteria.setCompany(companyFilter);
                                StringFilter deletedByFilter = new StringFilter();
                                deletedByFilter.setSpecified(false);
                                supplierContractCriteria.setDeletedBy(deletedByFilter);
                                return supplierContractRepository.findByCriteria(supplierContractCriteria, null)
                                    .map(supplierContractMapper::toDto)
                                    .collectList()
                                    .flatMap(supplierContractDTOList -> {
                                        suppliesRequestDTO.setSupplierContracts(new ArrayList<>(supplierContractDTOList));
                                        return Mono.just(suppliesRequestDTO);
                                    });
                            });
                    })
                );
        });
    }

    /**
     * Delete the suppliesRequest by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(UUID id) {
        log.debug("Request to delete SuppliesRequest : {}", id);
        return suppliesRequestRepository.deleteById(id);
    }

    /**
     * Delete the suppliesRequest by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(UUID id, String company, String deletedBy) {
        log.debug("Request to delete SuppliesRequest by company : {}", id);
        return suppliesRequestRepository.delete(id, company, deletedBy).then(suppliesItemRepository.deleteBySuppliesRequestId(id, company, deletedBy));
    }

    public Mono<SuppliesRequestDTO> proposeReview(UUID id) {
        log.debug("Request to propose review contract SuppliesRequest : {}", id);
        return SecurityUtils.getUserJWTDetail().flatMap(login -> {
            return suppliesRequestRepository.findById(id)
            .flatMap(suppliesRequest -> {
                if (!(suppliesRequest.getCreatedByEmployeeId().equals(login.getUserId()))){
                    return Mono.error(new BadRequestAlertException("Can not send for approval because not created by you", "paymentRequest", "INVALID_USER"));
                }
                if (!(suppliesRequest.getRequestStatus().equals(RequestStatus.NEW)
                    || suppliesRequest.getRequestStatus().equals(RequestStatus.REJECTED)) ) {
                    return Mono.error(new BadRequestAlertException("Request is not in NEW or REJECT status", "SuppliesRequest", "INVALID_STATUS"));
                }

                suppliesRequest.setRequestStatus(RequestStatus.WAITING_APPROVE);
                suppliesRequest.setUpdatedBy(login.getUserId().toString());
                suppliesRequest.setUpdatedAt(ZonedDateTime.now());
                suppliesRequest.setIsPersisted();
                return suppliesRequestRepository.save(suppliesRequest)
                    .map(suppliesRequestMapper::toDto).flatMap(s -> {
                        return requestApprovalRepository.findByDocumentIdIn(List.of(id))
                            .switchIfEmpty(Mono.error(new BadRequestAlertException("Request approval list is empty", "SuppliesRequest", "requestapprovalempty")))
                            .map(requestApprovalMapper::toDto)
                            .switchIfEmpty(Mono.error(new BadRequestAlertException("Request approval list is empty", "SuppliesRequest", "requestapprovalempty")))
                            .collectList()
                            .doOnError(e -> {
                                log.error("Error while get request approval list: {}", e.getMessage());
                            })
                            .flatMap(requestApprovalDTOList -> {
                                return requestApprovalRepository.removeResultByDocumentIdAndCompanyAndDeleted(id, s.getCompany())
                                    .then(Mono.just(s));
                            });
                    });
            });
        });
    }

    /**
     * Approves or rejects a contract for a SuppliesRequest.
     *
     * @param id the ID of the SuppliesRequest.
     * @param company the company associated with the SuppliesRequest.
     * @param request the request containing approval details.
     * @param updatedBy the ID of the employee updating the request.
     * @return a Mono containing the updated SuppliesRequestDTO.
     */
    public Mono<SuppliesRequestDTO> approved(UUID id, String company, RequestApprovalDTO request, String updatedBy) {
        return suppliesRequestRepository.findById(id, company).flatMap(suppliesRequest -> {
            if (!suppliesRequest.getRequestStatus().equals(RequestStatus.WAITING_APPROVE)) {
                return Mono.error(new BadRequestAlertException("Request is not in WAITING_APPROVE status", "SuppliesRequest", "requestnotwaitingapprove"));
            }
            return requestApprovalRepository.findByDocumentIdIn(List.of(id))
                .switchIfEmpty(Mono.error(new BadRequestAlertException("Request approval list is empty", "SuppliesRequest", "requestapprovalempty")))
                .collectList()
                .flatMap(requestApprovalDTOList -> {
                    if (requestApprovalDTOList.isEmpty()) {
                        return Mono.error(new BadRequestAlertException("Request approval list is empty", "SuppliesRequest", "requestapprovalempty"));
                    }

                    // Lấy requestApproval của người hiện tại (updatedBy)
                    var myRequestApproval = requestApprovalDTOList.stream()
                        .filter(requestApprovalDTO -> requestApprovalDTO.getEmployeeId().equals(UUID.fromString(updatedBy)))
                        .findFirst()
                        .orElse(null);

                    if (myRequestApproval == null) {
                        return Mono.error(new BadRequestAlertException("You are not in request approval list", "SuppliesRequest", "requestapprovalnotfound"));
                    }

                    // Cập nhật trạng thái duyệt cho người hiện tại
                    myRequestApproval.setResult(true);
                    myRequestApproval.setUpdatedAt(ZonedDateTime.now());
                    myRequestApproval.setUpdatedBy(updatedBy);
                    myRequestApproval.setApprovedSign(request.getApprovedSign());
                    myRequestApproval.setApprovedSignName(request.getApprovedSignName());
                    myRequestApproval.setIsPersisted();

                    // Lọc ra danh sách không bao gồm người hiện tại
                    var othersApprovalList = requestApprovalDTOList.stream()
                        .filter(requestApprovalDTO -> !requestApprovalDTO.getEmployeeId().equals(UUID.fromString(updatedBy)))
                        .toList();

                    // Kiểm tra nếu tất cả các bản ghi còn lại đã được duyệt
                    if (othersApprovalList.isEmpty() || othersApprovalList.stream().allMatch(requestApprovalDTO -> requestApprovalDTO.getApprovedSign() != null)) {
                        // Nếu danh sách trống (chỉ có duy nhất bản thân) hoặc tất cả đã duyệt
                        suppliesRequest.setRequestStatus(RequestStatus.APPROVED);
                        suppliesRequest.setUpdatedBy(updatedBy);
                        suppliesRequest.setUpdatedAt(ZonedDateTime.now());
                        suppliesRequest.setIsPersisted(); // Đánh dấu trạng thái đã được cập nhật
                        return requestApprovalRepository.save(myRequestApproval)
                            .map(requestApprovalMapper::toDto)
                            .flatMap(requestApprovalDTO -> {
                                return suppliesRequestRepository.save(suppliesRequest)
                                    .map(suppliesRequestMapper::toDto);
                            });
                    } else {
                        // Nếu chưa, trả về trạng thái hiện tại mà không thay đổi
                        return requestApprovalRepository.save(myRequestApproval)
                            .map(requestApprovalMapper::toDto)
                            .flatMap(requestApprovalDTO -> {
                                return Mono.just(suppliesRequestMapper.toDto(suppliesRequest));
                            });
                    }


                });
        });
    }

    //reject
    public Mono<SuppliesRequestDTO> reject(UUID id, String company, RequestApprovalDTO approvedRequest, String updatedBy){
        if (approvedRequest.getRejectNote() == null) {
            return Mono.error(new BadRequestAlertException("Reject note is required", "SuppliesRequest", "rejectnotenull"));
        }
        return findOne(id, company).flatMap(suppliesRequestDTO -> {
            if (suppliesRequestDTO.getRequestStatus() != RequestStatus.WAITING_APPROVE) {
                return Mono.error(new BadRequestAlertException("Request is not in WAITING_APPROVE status", "SuppliesRequest", "requestnotwaitingapprove"));
            }
            suppliesRequestDTO.setRequestStatus(RequestStatus.REJECTED);
            return requestApprovalRepository.findByDocumentIdIn(List.of(id))
                .switchIfEmpty(Mono.error(new BadRequestAlertException("Request approval list is empty", "SuppliesRequest", "requestapprovalempty")))
                .collectList().flatMap(requestApprovalDTOList -> {
                if (requestApprovalDTOList.isEmpty()) {
                    return Mono.error(new BadRequestAlertException("Request approval list is empty", "SuppliesRequest", "requestapprovalempty"));
                }
                var myRequestApproval = requestApprovalDTOList.
                    stream()
                    .filter(requestApprovalDTO ->
                        requestApprovalDTO.getEmployeeId().equals(UUID.fromString(updatedBy))).findFirst().orElse(null);
                if (myRequestApproval == null) {
                    return Mono.error(new BadRequestAlertException("You are not in request approval list", "SuppliesRequest", "requestapprovalnotfound"));
                }
                myRequestApproval.setResult(false);
                myRequestApproval.setRejectNote(approvedRequest.getRejectNote());
                myRequestApproval.setUpdatedAt(ZonedDateTime.now());
                myRequestApproval.setUpdatedBy(updatedBy);
                myRequestApproval.setIsPersisted();
                return requestApprovalRepository.save(myRequestApproval)
                    .map(requestApprovalMapper::toDto)
                    .flatMap(requestApprovalDTO -> {
                        return suppliesRequestRepository.save(suppliesRequestMapper.toEntity(suppliesRequestDTO).setIsPersisted())
                            .map(suppliesRequestMapper::toDto);
                    });
            });
        });
    }

    public Mono<SuppliesRequestDTO> cancel(UUID id){
        log.debug("Request to cancel SuppliesRequest : {}", id);
        return SecurityUtils.getUserJWTDetail().flatMap(login -> {
            return findOne(id, login.getCompanyId()).flatMap(suppliesRequestDTO -> {
                if (!(suppliesRequestDTO.getRequestStatus().equals(RequestStatus.NEW))) {
                    return Mono.error(new BadRequestAlertException("Request is not in NEW or REJECTED status", "SuppliesRequest", "INVALID_STATUS"));
                }
                suppliesRequestDTO.setRequestStatus(RequestStatus.CANCELLED);
                var entity = suppliesRequestMapper.toEntity(suppliesRequestDTO);
                entity.setIsPersisted();
                return suppliesRequestRepository.save(entity)
                    .map(suppliesRequestMapper::toDto);
            });
        });
    }


    public Mono<byte[]> exportRecordsAsCSV(SuppliesRequestCriteria criteria, Pageable pageable) {
        log.debug("Request to export TimeKeepingRecords as CSV");

        return this.findByCriteria(criteria, pageable)
                .collectList()
                .flatMap(dto -> {
                            log.debug("Request to export TimeKeepingRecords as CSV");
                            return CSVUtils.convertListToExcel(dto);
                        }
                );
    }

    public Mono<String> exportSuppliesRequest(List<SuppliesRequestDTO> suppliesRequestDTOList) {
        try {
            Workbook workbook = new XSSFWorkbook();
            var path = "uploaded-files/supplies-request/";
            var folder = new File(path);
            if (!folder.exists()) {
                folder.mkdirs();
            }
            var orderSheet = workbook.createSheet("Danh sách đề xuất mua hàng");

            Row headerRow = orderSheet.createRow(0);
            String[] headers = {"STT", "Mã đề xuất", "Loại đề xuất", "Ngày lập", "Người lập", "Số lượng", "Số lượng đã giao", "Số lượng còn lại", "Thành tiền", "Nội dung", "Trạng thái"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
            }

            var headerData = Flux.fromIterable(suppliesRequestDTOList).collectList();
            AtomicInteger rowNum = new AtomicInteger(1);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            return headerData.flatMap(data -> {
                data.forEach(suppliesRequestDTO -> {
                    var createdByName = "SYSTEM";
                    var createdByCode = "SYSTEM";
                    if (suppliesRequestDTO.getCreatedByEmployee() != null) {
                        createdByName = suppliesRequestDTO.getCreatedByEmployee().getFullName() == null ? "" : suppliesRequestDTO.getCreatedByEmployee().getFullName();
                        createdByCode = suppliesRequestDTO.getCreatedByEmployee().getEmployeeCode() == null ? "" : suppliesRequestDTO.getCreatedByEmployee().getEmployeeCode();
                    }
                    var employee = createdByCode + " - " + createdByName;
                    Row row = orderSheet.createRow(rowNum.get());
                    row.createCell(0).setCellValue(rowNum.getAndIncrement());
                    row.createCell(1).setCellValue(suppliesRequestDTO.getRequestNumber());
                    var requestType = suppliesRequestDTO.getRequestType() == null ? "" : suppliesRequestDTO.getRequestType().getName();
                    row.createCell(2).setCellValue(requestType);
                    var requestDate = suppliesRequestDTO.getRequestDate() != null ? suppliesRequestDTO.getRequestDate().format(formatter) : suppliesRequestDTO.getCreatedDate().format(formatter);
                    row.createCell(3).setCellValue(requestDate);
                    row.createCell(4).setCellValue(createdByName);
                    var totalQuantity = suppliesRequestDTO.getTotalQuantity() == null ? "0" : (suppliesRequestDTO.getTotalQuantity().stripTrailingZeros().scale() <= 0 ? String.valueOf(suppliesRequestDTO.getTotalQuantity().intValue()) : String.valueOf(suppliesRequestDTO.getTotalQuantity()));
                    row.createCell(5).setCellValue(totalQuantity);
                    var deliveredQuantity = suppliesRequestDTO.getDeliveredQuantity() == null ? "0" : (suppliesRequestDTO.getDeliveredQuantity().stripTrailingZeros().scale() <= 0 ? String.valueOf(suppliesRequestDTO.getDeliveredQuantity().intValue()) : String.valueOf(suppliesRequestDTO.getDeliveredQuantity()));
                    row.createCell(6).setCellValue(deliveredQuantity);
                    var remainingQuantity = suppliesRequestDTO.getRemainingQuantity() == null ? "0" : (suppliesRequestDTO.getRemainingQuantity().stripTrailingZeros().scale() <= 0 ? String.valueOf(suppliesRequestDTO.getRemainingQuantity().intValue()) : String.valueOf(suppliesRequestDTO.getRemainingQuantity()));
                    row.createCell(7).setCellValue(remainingQuantity);
                    var totalAmountAfterVat = suppliesRequestDTO.getTotalAmountAfterVat() == null ? "0" : (suppliesRequestDTO.getTotalAmountAfterVat().stripTrailingZeros().scale() <= 0 ? String.valueOf(suppliesRequestDTO.getTotalAmountAfterVat().intValue()) : String.valueOf(suppliesRequestDTO.getTotalAmountAfterVat()));
                    var totalAmount = suppliesRequestDTO.getTotalAmount() == null ? "0" : (suppliesRequestDTO.getTotalAmount().stripTrailingZeros().scale() <= 0 ? String.valueOf(suppliesRequestDTO.getTotalAmount().intValue()) : String.valueOf(suppliesRequestDTO.getTotalAmount()));
                    row.createCell(8).setCellValue(totalAmount);
                    row.createCell(9).setCellValue(suppliesRequestDTO.getNote() == null ? "" : suppliesRequestDTO.getNote());
                    var requestStatus = suppliesRequestDTO.getRequestStatus() == null ? "" : suppliesRequestDTO.getRequestStatus().toVietnameseName();
                    row.createCell(10).setCellValue(requestStatus);
                });

                for (int i = 0; i < headers.length; i++) {
                    orderSheet.autoSizeColumn(i);
                }

                var uuid = UUID.randomUUID().toString();

                try (FileOutputStream out = new FileOutputStream(path + uuid + ".xlsx")) {
                    workbook.write(out);
                    workbook.close();
                    return Mono.just(path + uuid + ".xlsx");
                } catch (IOException e) {
                    return Mono.error(e);
                }
            });
        } catch (Exception e) {
            return Mono.error(e);
        }
    }
}
