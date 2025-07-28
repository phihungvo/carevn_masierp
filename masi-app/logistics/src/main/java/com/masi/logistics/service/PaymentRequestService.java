package com.masi.logistics.service;

import com.carevn.masi.dto.EmployeeDTO;
import com.carevn.masi.dto.UserJWTDetail;
import com.carevn.masi.utils.SecurityUtils;
import com.masi.logistics.domain.PaymentDetail;
import com.masi.logistics.domain.PaymentRequest;
import com.masi.logistics.domain.Reimbursement;
import com.masi.logistics.domain.criteria.PaymentDetailCriteria;
import com.masi.logistics.domain.criteria.PaymentRequestCriteria;
import com.masi.logistics.domain.criteria.ReimbursementCriteria;
import com.masi.logistics.domain.enumeration.RequestStatus;
import com.masi.logistics.domain.enumeration.RequestTypeEnum;
import com.masi.logistics.repository.*;
import com.masi.logistics.service.dto.*;
import com.masi.logistics.service.mapper.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import com.masi.logistics.service.web.client.EmployeeClient;
import com.masi.logistics.web.rest.errors.BadRequestAlertException;
import lombok.AllArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.StringFilter;
import tech.jhipster.service.filter.UUIDFilter;
import tech.jhipster.service.filter.ZonedDateTimeFilter;

/**
 * Service Implementation for managing {@link com.masi.logistics.domain.PaymentRequest}.
 */
@Service
@Transactional
@AllArgsConstructor
public class PaymentRequestService {

    private static final Logger LOG = LoggerFactory.getLogger(PaymentRequestService.class);

    private final PaymentRequestRepository paymentRequestRepository;

    private final PaymentRequestMapper paymentRequestMapper;
    private final RequestApprovalMapper requestApprovalMapper;
    private final PaymentDetailMapper paymentDetailMapper;
    private final ReimbursementMapper reimbursementMapper;
    private final IncomingInvoiceMapper incomingInvoiceMapper;
    private final RequestApprovalRepository requestApprovalRepository;
    private final PaymentDetailRepository paymentDetailRepository;
    private final ReimbursementRepository reimbursementRepository;
    private final DocumentCodeSequenceService documentCodeSequenceService;
    private final IncomingInvoiceService incomingInvoiceService;
    private final IncomingInvoiceRepository incomingInvoiceRepository;
    private final EmployeeClient employeeClient;
    private final SuppliersRepository suppliersRepository;
    private final SuppliersMapper suppliersMapper;

    /**
     * Save a paymentRequest.
     *
     * @param paymentRequestDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<PaymentRequestDTO> save(PaymentRequestDTO paymentRequestDTO) {
        LOG.debug("Request to save PaymentRequest : {}", paymentRequestDTO);
        paymentRequestDTO.setStatus(RequestStatus.NEW);
        return SecurityUtils.getUserJWTDetail().flatMap(login -> {
            paymentRequestDTO.setCreatedBy(login.getUserId());
            paymentRequestDTO.setCreatedDate(ZonedDateTime.now());
            paymentRequestDTO.setCompany(login.getCompanyId());
            return switch (paymentRequestDTO.getType()) {
                case PAYMENT -> documentCodeSequenceService.makeSureDocumentCodeSequenceExist(PaymentRequest.ENTITY_NAME + "_" + "PAYMENT",  "%04d")// thanh toán
                    .then(documentCodeSequenceService.getByDocumentType(PaymentRequest.ENTITY_NAME + "_" + "PAYMENT"))
                    .flatMap(code -> {
                        var currentDate = getCurrentMonthDayString();
                        var codeNext = code.getNextAndIncrement();
                        var fullCode = "TT" + currentDate + "/" + codeNext;
                        paymentRequestDTO.setCode(fullCode);
                        paymentRequestDTO.setType(RequestTypeEnum.PAYMENT);
                        return documentCodeSequenceService.updateSequence(code).then(Mono.defer(() -> {

                            return paymentRequestRepository.save(paymentRequestMapper.toEntity(paymentRequestDTO))
                                .map(paymentRequestMapper::toDto).flatMap(pr -> {
                                    var entityApprovals = requestApprovalMapper.toEntity(new ArrayList<>(paymentRequestDTO.getRequestApprovals()));
                                    entityApprovals.forEach(requestApproval -> requestApproval.setDocumentId(pr.getId()));
                                    return requestApprovalRepository.saveAll(entityApprovals)
                                        .map(requestApprovalMapper::toDto)
                                        .collectList()
                                        .defaultIfEmpty(new ArrayList<>())
                                        .flatMap(requestApprovals -> {
                                            pr.setRequestApprovals(requestApprovals);
                                            return savePaymentRequestWithDetails(paymentRequestDTO, pr);

                                        });
                                });
                        }));
                    });
                case REIMBURSEMENT -> documentCodeSequenceService.makeSureDocumentCodeSequenceExist(PaymentRequest.ENTITY_NAME + "_" +"REIMBURSEMENT",  "%04d")// Hoàn tạm ứng
                    .then(documentCodeSequenceService.getByDocumentType(PaymentRequest.ENTITY_NAME + "_" + "REIMBURSEMENT"))
                    .flatMap(code -> {
                        var currentDate = getCurrentMonthDayString();
                        var codeNext = code.getNextAndIncrement();
                        var fullCode = "HU" + currentDate + "/" + codeNext;
                        paymentRequestDTO.setCode(fullCode);
                        paymentRequestDTO.setType(RequestTypeEnum.REIMBURSEMENT);
                        return documentCodeSequenceService.updateSequence(code).then(Mono.defer(() -> {
                            return paymentRequestRepository.save(paymentRequestMapper.toEntity(paymentRequestDTO))
                                .map(paymentRequestMapper::toDto)
                                .flatMap(pr -> {
                                    var entityReimbursements = reimbursementMapper.toEntity(new ArrayList<>(paymentRequestDTO.getReimbursementDTOS()));
                                    entityReimbursements.forEach(reimbursement -> reimbursement.setReimbursementId(pr.getId()));
                                    return reimbursementRepository
                                        .saveAll(entityReimbursements)
                                        .map(reimbursementMapper::toDto)
                                        .collectList()
                                        .defaultIfEmpty(new ArrayList<>())
                                        .flatMap(reimbursements -> {
                                            pr.setReimbursementDTOS(reimbursements);
                                            var entityApprovals = requestApprovalMapper.toEntity(new ArrayList<>(paymentRequestDTO.getRequestApprovals()));
                                            entityApprovals.forEach(requestApproval -> requestApproval.setDocumentId(pr.getId()));
                                            return requestApprovalRepository.saveAll(entityApprovals)
                                                .map(requestApprovalMapper::toDto)
                                                .collectList()
                                                .defaultIfEmpty(new ArrayList<>())
                                                .flatMap(requestApprovals -> {
                                                    pr.setRequestApprovals(requestApprovals);
                                                    return savePaymentRequestWithDetails(paymentRequestDTO, pr);
                                                });
                                        });
                                });
                        }));
                    });
                case ADVANCEMENT -> documentCodeSequenceService.makeSureDocumentCodeSequenceExist(PaymentRequest.ENTITY_NAME + "_" +"ADVANCEMENT",  "%04d")// tạm ứng
                    .then(documentCodeSequenceService.getByDocumentType(PaymentRequest.ENTITY_NAME + "_" + "ADVANCEMENT"))
                    .flatMap(code -> {
                        var currentDate = getCurrentMonthDayString();
                        var codeNext = code.getNextAndIncrement();
                        var fullCode = "TU" + currentDate + "/" + codeNext;
                        paymentRequestDTO.setCode(fullCode);
                        paymentRequestDTO.setType(RequestTypeEnum.ADVANCEMENT);
                        return documentCodeSequenceService.updateSequence(code).then(Mono.defer(() -> {
                            return paymentRequestRepository.save(paymentRequestMapper.toEntity(paymentRequestDTO))
                                .map(paymentRequestMapper::toDto)
                                .flatMap(pr -> {
                                    var entityApprovals = requestApprovalMapper.toEntity(new ArrayList<>(paymentRequestDTO.getRequestApprovals()));
                                    entityApprovals.forEach(requestApproval -> requestApproval.setDocumentId(pr.getId()));
                                    return requestApprovalRepository.saveAll(entityApprovals)
                                        .map(requestApprovalMapper::toDto)
                                        .collectList()
                                        .defaultIfEmpty(new ArrayList<>())
                                        .flatMap(requestApprovals -> {
                                            pr.setRequestApprovals(requestApprovals);
                                            return Mono.just(pr);
                                        });
                                });
                        }));
                    });
                default ->
                    Mono.error(new BadRequestAlertException("Invalid request type", "paymentRequest", "INVALID_REQUEST_TYPE"));
            };
        });
    }

    /**
     * Save a paymentRequest.
     *
     * @param paymentRequestDTO the entity to save.
     * @return the persisted entity.
     * kiểm tra trong list payment detail có invoice nào không có id không
     * nếu có thì xoá khỏi list và tạo mới invoice đó bằng invoice DTO
     * những item có invoiceId thì update documentId = paymentRequestId
     * sau đó set invoiceId cho payment detail đó
     */
    private Mono<PaymentRequestDTO> savePaymentRequestWithDetails(PaymentRequestDTO paymentRequestDTO, PaymentRequestDTO paymentRequestAfterSave) {
        var listPaymentDetail = paymentRequestDTO.getPaymentDetails().stream().toList();

        List<IncomingInvoiceDTO> invoiceWithoutId = listPaymentDetail.stream()
            .filter(paymentDetailDTO -> Objects.isNull(paymentDetailDTO.getInvoiceId()))
            .map(PaymentDetailDTO::getIncomingInvoice)
            .filter(Objects::nonNull)
            .collect(Collectors.toList());

        listPaymentDetail = listPaymentDetail.stream()
            .filter(paymentDetailDTO -> Objects.nonNull(paymentDetailDTO.getInvoiceId()))
            .toList();
        List<UUID> listInvoiceId = listPaymentDetail.stream().map(PaymentDetailDTO::getInvoiceId).toList();

        listPaymentDetail.forEach(paymentDetailDTO -> paymentDetailDTO.setPaymentRequestId(paymentRequestAfterSave.getId()));
        var isPayment = paymentRequestAfterSave.getType().equals(RequestTypeEnum.PAYMENT);
        List<PaymentDetailDTO> finalListPaymentDetail = new ArrayList<>(listPaymentDetail);
        // nếu có invoice không có id thì tạo invoice mới
        if (!invoiceWithoutId.isEmpty()) {
            return incomingInvoiceService.saveAll(invoiceWithoutId, paymentRequestAfterSave.getId(), paymentRequestAfterSave.getSupplierId(), isPayment)
                .map(IncomingInvoiceDTO::getId)
                .collectList()
                .flatMap(incomingInvoiceIds -> {
                    for (var item: incomingInvoiceIds) {
                        var paymentDetailDTO = new PaymentDetailDTO();
                        paymentDetailDTO.setPaymentRequestId(paymentRequestAfterSave.getId());
                        paymentDetailDTO.setInvoiceId(item);
                        finalListPaymentDetail.add(paymentDetailDTO);
                    }
                    return paymentDetailRepository
                        .saveAll(paymentDetailMapper.toEntity(finalListPaymentDetail))
                        .map(paymentDetailMapper::toDto)
                        .collectList()
                        .defaultIfEmpty(new ArrayList<>())
                        .flatMap(paymentDetails -> {
                            paymentRequestAfterSave.setPaymentDetails(paymentDetails);
                            if (!listInvoiceId.isEmpty())
                            {
                                if (isPayment) {
                                    return incomingInvoiceRepository.updateDocumentId(listInvoiceId, paymentRequestAfterSave.getId(), paymentRequestAfterSave.getCompany())
                                        .then(Mono.just(paymentRequestAfterSave));
                                }
                                else{
                                    return incomingInvoiceRepository.updateReimbursementId(listInvoiceId, paymentRequestAfterSave.getId(), paymentRequestAfterSave.getCompany())
                                        .then(Mono.just(paymentRequestAfterSave));
                                }
                            }
                            return Mono.just(paymentRequestAfterSave);
                        });
                });
        } else {
            // nếu không có invoice nào không có id thì update tất cả documentId = paymentRequestId

            return paymentDetailRepository.saveAll(paymentDetailMapper.toEntity(finalListPaymentDetail))
                .map(paymentDetailMapper::toDto)
                .collectList()
                .defaultIfEmpty(new ArrayList<>())
                .flatMap(paymentDetails -> {
                    paymentRequestAfterSave.setPaymentDetails(paymentDetails);
                    if (!listInvoiceId.isEmpty())
                    {
                        if (isPayment){
                            return incomingInvoiceRepository.updateDocumentId(listInvoiceId, paymentRequestAfterSave.getId(), paymentRequestAfterSave.getCompany())
                                .then(Mono.just(paymentRequestAfterSave));
                        }
                        else{
                            return incomingInvoiceRepository.updateReimbursementId(listInvoiceId, paymentRequestAfterSave.getId(), paymentRequestAfterSave.getCompany())
                                .then(Mono.just(paymentRequestAfterSave));
                        }
                    }
                    return Mono.just(paymentRequestAfterSave);
                });
        }
    }



    public static String getCurrentMonthDayString() {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMyy");
        return currentDate.format(formatter);
    }

    /**
     * Update a paymentRequest.
     *
     * @param paymentRequestDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<PaymentRequestDTO> update(PaymentRequestDTO paymentRequestDTO) {
        LOG.debug("Request to update PaymentRequest : {}", paymentRequestDTO);
        return paymentRequestRepository
            .save(paymentRequestMapper.toEntity(paymentRequestDTO).setIsPersisted())
            .map(paymentRequestMapper::toDto);
    }

    /**
     * Partially update a paymentRequest.
     *
     * @param paymentRequestDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<PaymentRequestDTO> partialUpdate(PaymentRequestDTO paymentRequestDTO) {
        LOG.debug("Request to partially update PaymentRequest : {}", paymentRequestDTO);

        return paymentRequestRepository
            .findById(paymentRequestDTO.getId())
            .map(existingPaymentRequest -> {
                paymentRequestMapper.partialUpdate(existingPaymentRequest, paymentRequestDTO);
                existingPaymentRequest.setIsPersisted();
                return existingPaymentRequest;
            })
            .flatMap(paymentRequestRepository::save)
            .map(paymentRequestMapper::toDto);
    }

    public Mono<PaymentRequestDTO> partialUpdateV2(PaymentRequestDTO paymentRequestDTO) {
        LOG.debug("Request to partially update PaymentRequest v2: {}", paymentRequestDTO);
        return paymentRequestRepository.findById(paymentRequestDTO.getId()).flatMap(pr -> {
//            if (!pr.getStatus().equals(RequestStatus.NEW)) {
//                return Mono.error(new BadRequestAlertException("Can not update because not in new status", "paymentRequest", "INVALID_STATUS"));
//            }
            paymentRequestMapper.partialUpdate(pr, paymentRequestDTO);
            pr.setIsPersisted();
            return SecurityUtils.getUserJWTDetail().flatMap(login -> {
                return paymentRequestRepository
                    .save(pr)
                    .map(paymentRequestMapper::toDto)
                    .flatMap(savedPr -> {
                        return requestApprovalRepository.findAllByDocumentIdAndIsDeletedIsFalse(savedPr.getId())
                            .map(requestApprovalMapper::toDto)
                            .collectList()
                            .flatMap(requestApprovals -> {
                                return requestApprovalRepository.deleteAllByDocumentIdAndCompany(savedPr.getId(), login.getCompanyId(), login.getUserId().toString())
                                    .then(Mono.defer(() -> {
                                        var entityApprovals = requestApprovalMapper.toEntity(new ArrayList<>(paymentRequestDTO.getRequestApprovals()));
                                        entityApprovals.forEach(requestApproval -> requestApproval.setDocumentId(savedPr.getId()));
                                        return requestApprovalRepository.saveAll(entityApprovals)
                                            .map(requestApprovalMapper::toDto)
                                            .collectList()
                                            .defaultIfEmpty(new ArrayList<>())
                                            .flatMap(ra -> {
                                                savedPr.setRequestApprovals(ra);
                                                return handleUpdatePaymentRequest(paymentRequestDTO, login, savedPr);
                                            });
                                    }));
                            });
                    });
            });
        });
    }

    private Mono<PaymentRequestDTO> handleUpdatePaymentRequest(PaymentRequestDTO paymentRequestDTO, UserJWTDetail login, PaymentRequestDTO savedPr) {
        if (Objects.requireNonNull(savedPr.getType()).equals(RequestTypeEnum.PAYMENT)) {
            return updateCasePayment(paymentRequestDTO, login, savedPr);
        }
        if (Objects.requireNonNull(savedPr.getType()).equals(RequestTypeEnum.REIMBURSEMENT)) {
            return updateCaseRei(paymentRequestDTO, login, savedPr);
        }

        return Mono.just(savedPr);
    }

    private Mono<PaymentRequestDTO> updateCasePayment(PaymentRequestDTO paymentRequestDTO, UserJWTDetail login, PaymentRequestDTO savedPr) {
        return paymentDetailRepository.findByPaymentRequestId(savedPr.getId(), null)
            .collectList()
            .flatMap(oldPaymentDetail -> {
                // find ids not in DTO list
                List<PaymentDetailDTO> newPaymentDetails = new ArrayList<>(paymentRequestDTO.getPaymentDetails());
                // co id nhung khác id trong list

                // find ids in newPaymentDetails
                List<UUID> newPaymentDetailIds = paymentRequestDTO.getPaymentDetails().stream()
                    .map(PaymentDetailDTO::getId)
                    .filter(Objects::nonNull)
                    .toList();

                // find items in oldPaymentDetails but not in newPaymentDetails
                List<PaymentDetail> removedList = oldPaymentDetail.stream()
                    .filter(item -> !newPaymentDetailIds.contains(item.getId()))
                    .toList();

                List<UUID> removedIds = removedList.stream().map(PaymentDetail::getId).toList();

                var entityNewPaymentDetails = paymentDetailMapper.toEntity(new ArrayList<>(newPaymentDetails));
                if (!removedList.isEmpty()) {
                    return paymentDetailRepository
                        .deleteBytId(removedIds, login.getUserId().toString(), login.getCompanyId())
                        .then(Mono.just(savedPr)).flatMap(x -> {
                            if (paymentRequestDTO.getType().equals(RequestTypeEnum.PAYMENT)) {
                                return incomingInvoiceRepository.setNullDocumentId(paymentRequestDTO.getId(), login.getCompanyId())
                                    .then(Mono.just(savedPr))
                                    .flatMap(y -> handleCreateNewInvoice(savedPr, entityNewPaymentDetails));
                            }
                            else{
                                return incomingInvoiceRepository.setNullReimbursementId(paymentRequestDTO.getId(), login.getCompanyId())
                                    .then(Mono.just(savedPr))
                                    .flatMap(y -> handleCreateNewInvoice(savedPr, entityNewPaymentDetails));
                            }
                        });
                }
                return handleCreateNewInvoice(savedPr, entityNewPaymentDetails);
            });
    }

    private Mono<? extends PaymentRequestDTO> handleCreateNewInvoice(PaymentRequestDTO savedPr, List<PaymentDetail> paymentDetailsCreate) {
        var invoiceIdsNull = paymentDetailsCreate.stream()
            .filter(filterList -> Objects.isNull(filterList.getInvoiceId()))
            .map(PaymentDetail::getIncomingInvoice)
            .toList();
        var listIdNullButHasInvoice = paymentDetailsCreate.stream()
            .filter(filterList -> Objects.isNull(filterList.getId()))
            .filter(filterList -> Objects.nonNull(filterList.getInvoiceId()))
            .toList();
        var isPayment = savedPr.getType().equals(RequestTypeEnum.PAYMENT);
        return incomingInvoiceService
            .saveAll(
                incomingInvoiceMapper.toDto(new ArrayList<>(invoiceIdsNull)),
                savedPr.getId(),
                savedPr.getSupplierId(),
                isPayment
            )
            .map(IncomingInvoiceDTO::getId)
            .collectList()
            .flatMap(incomingInvoiceIds -> {
                List<PaymentDetailDTO> savedList = new ArrayList<>();
                for (var item : incomingInvoiceIds) {
                    var paymentDetailDTO = new PaymentDetailDTO();
                    paymentDetailDTO.setPaymentRequestId(savedPr.getId());
                    paymentDetailDTO.setInvoiceId(item);
                    savedList.add(paymentDetailDTO);
                }
                for (var item : listIdNullButHasInvoice) {
                    item.setPaymentRequestId(savedPr.getId());
                    savedList.add(paymentDetailMapper.toDto(item));
                }
                return paymentDetailRepository
                    .saveAll(paymentDetailMapper.toEntity(savedList))
                    .map(paymentDetailMapper::toDto)
                    .collectList()
                    .defaultIfEmpty(new ArrayList<>())
                    .flatMap(paymentDetails -> {
                        savedPr.setPaymentDetails(paymentDetails);
                        return Mono.just(savedPr);
                    });
            });
    }

    private Mono<PaymentRequestDTO> updateCaseRei(PaymentRequestDTO paymentRequestDTO, UserJWTDetail login, PaymentRequestDTO savedPr) {
        return reimbursementRepository.findByReimbursementId(savedPr.getId(), login.getCompanyId())
            .collectList()
            .flatMap(reimbursement -> {
                return reimbursementRepository.deleteByReimbursementId(savedPr.getId(), login.getUserId().toString(), login.getCompanyId())
                    .then(Mono.defer(() -> {
                        paymentRequestDTO.getReimbursementDTOS().forEach(reimbursementDTO -> {
                            reimbursementDTO.setReimbursementId(savedPr.getId());
                        });
                        return reimbursementRepository.saveAll(reimbursementMapper.toEntity(new ArrayList<>(paymentRequestDTO.getReimbursementDTOS())))
                            .map(reimbursementMapper::toDto)
                            .collectList()
                            .defaultIfEmpty(new ArrayList<>())
                            .flatMap(reimbursements -> {
                                savedPr.setReimbursementDTOS(reimbursements);
                                return updateCasePayment(paymentRequestDTO, login, savedPr);
                            });
                    }));
            });
    }

    /**
     * Find paymentRequests by Criteria.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<PaymentRequestDTO> findByCriteria(PaymentRequestCriteria criteria, Pageable pageable) {
        LOG.debug("Request to get all PaymentRequests by Criteria");
        StringFilter deletedByFilter = new StringFilter();
        deletedByFilter.setSpecified(false); // Lọc các record có deleted_by IS NULL
        criteria.setDeletedBy(deletedByFilter);
        // Kiểm tra và xử lý criteria.getCode()
        Mono<PaymentRequestCriteria> updatedCriteriaMono;
        if (criteria.getSearch() != null && !criteria.getSearch().isEmpty()) {
            updatedCriteriaMono = employeeClient.getEmployeesBySearch(criteria.getSearch())
                    .collectList()
                    .flatMap(employeeIds -> {
                        if (employeeIds.isEmpty()) {
                            LOG.warn("No employees found for the given code, skipping filter.");
                            return Mono.just(criteria);
                        }

                        criteria.setEmployeeIds(new ArrayList<>(employeeIds));
                        return Mono.just(criteria);
                    });
        } else {
            updatedCriteriaMono = Mono.just(criteria);
        }

        return SecurityUtils.getUserJWTDetail().flatMapMany(login -> {
            return updatedCriteriaMono.flatMapMany(updatedCriteria ->{
                StringFilter companyFilter = new StringFilter();
                companyFilter.setEquals(login.getCompanyId());
                updatedCriteria.setCompany(companyFilter);
                return paymentRequestRepository.findByCriteria(updatedCriteria, pageable)
                    .map(paymentRequestMapper::toDto)
                    .collectList()
                    .flatMapMany(pr -> {
                        LOG.debug("Request to get all PaymentRequests by Criteria");
                        var listPaymentRequestIds = pr.stream().map(PaymentRequestDTO::getId).toList();
                        if (listPaymentRequestIds.isEmpty()) {
                            listPaymentRequestIds = new ArrayList<>();
                            listPaymentRequestIds.add(UUID.fromString("00000000-0000-0000-0000-000000000000"));
                        }
                        return requestApprovalRepository.findByDocumentIdIn(listPaymentRequestIds)
                            .map(requestApprovalMapper::toDto).collectList()
                            .flatMapMany(ra -> {
                                pr.forEach(paymentRequestDTO -> {
                                    var requestApprovals = ra.stream()
                                        .filter(requestApprovalDTO -> requestApprovalDTO.getDocumentId().equals(paymentRequestDTO.getId()))
                                        .toList();
                                    paymentRequestDTO.setRequestApprovals(requestApprovals);
                                });

                                HashSet<UUID> listEmployeeIds = new HashSet<>();
                                pr.forEach(paymentRequestDTO -> {
                                    listEmployeeIds.add(paymentRequestDTO.getEmployeeId());
                                    listEmployeeIds.add(paymentRequestDTO.getCreatedBy());
                                    if (paymentRequestDTO.getRequestApprovals() != null) {
                                        paymentRequestDTO.getRequestApprovals()
                                            .forEach(requestApprovalDTO -> listEmployeeIds.add(requestApprovalDTO.getEmployeeId()));
                                    }
                                });

                                return employeeClient.getEmployeesByListIds(new ArrayList<>(listEmployeeIds))
                                    .collectList()
                                    .defaultIfEmpty(new ArrayList<>())
                                    .flatMapMany(employees -> {
                                        var mapEmployee = employees.stream()
                                            .collect(Collectors.toMap(EmployeeDTO::getId, x -> x));
                                        pr.forEach(paymentRequestDTO -> {
                                            paymentRequestDTO.setEmployee(mapEmployee.get(paymentRequestDTO.getEmployeeId()));
                                            paymentRequestDTO.setCreatedByEmployee(mapEmployee.get(paymentRequestDTO.getCreatedBy()));

                                            if (paymentRequestDTO.getRequestApprovals() != null) {
                                                paymentRequestDTO.getRequestApprovals()
                                                    .forEach(requestApprovalDTO -> {
                                                        requestApprovalDTO.setEmployee(
                                                            mapEmployee.get(requestApprovalDTO.getEmployeeId()));
                                                    });
                                            }
                                        });

                                        return Flux.fromIterable(pr);
                                    });
                            });
                    });
            });
        });
    }


    /**
     * Find the count of paymentRequests by criteria.
     * @param criteria filtering criteria
     * @return the count of paymentRequests
     */
    public Mono<Long> countByCriteria(PaymentRequestCriteria criteria) {
        LOG.debug("Request to get the count of all PaymentRequests by Criteria");
        // add update criteria here
        Mono<PaymentRequestCriteria> updatedCriteriaMono;
        if (criteria.getSearch() != null && !criteria.getSearch().isEmpty()) {
            updatedCriteriaMono = employeeClient.getEmployeesBySearch(criteria.getSearch())
                .collectList()
                .flatMap(employeeIds -> {
                    if (employeeIds.isEmpty()) {
                        LOG.warn("No employees found for the given code, skipping filter.");
                        return Mono.just(criteria);
                    }

                    criteria.setEmployeeIds(new ArrayList<>(employeeIds));
                    return Mono.just(criteria);
                });
        } else {
            updatedCriteriaMono = Mono.just(criteria);
        }
        return updatedCriteriaMono.flatMap(paymentRequestRepository::countByCriteria);
    }

    /**
     * Returns the number of paymentRequests available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return paymentRequestRepository.count();
    }

    /**
     * Get one paymentRequest by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<PaymentRequestDTO> findOne(UUID id) {
        LOG.debug("Request to get PaymentRequest : {}", id);
        return paymentRequestRepository.findById(id)
            .map(paymentRequestMapper::toDto)
            .flatMap(pr -> {
                return requestApprovalRepository.findAllByDocumentIdAndIsDeletedIsFalse(id)
                    .map(requestApprovalMapper::toDto)
                    .collectList()
                    .defaultIfEmpty(new ArrayList<>())
                    .flatMap(requestApprovals -> {
                        pr.setRequestApprovals(requestApprovals);
                        HashSet<UUID> listEmployeeIds = new HashSet<>();
                        listEmployeeIds.add(pr.getEmployeeId());
                        listEmployeeIds.add(pr.getCreatedBy());
                        requestApprovals.forEach(requestApprovalDTO -> listEmployeeIds.add(requestApprovalDTO.getEmployeeId()));
                        return employeeClient.getEmployeesByListIds(new ArrayList<>(listEmployeeIds))
                            .collectList()
                            .defaultIfEmpty(new ArrayList<>())
                            .flatMap(employees -> {
                                var mapEmployee = employees.stream().collect(Collectors.toMap(EmployeeDTO::getId, x -> x));
                                var employee = mapEmployee.get(pr.getEmployeeId());
                                var createdBy = mapEmployee.get(pr.getCreatedBy());
                                if (employee != null) {
                                    pr.setEmployee(employee);
                                }
                                if (createdBy != null) {
                                    pr.setCreatedByEmployee(createdBy);
                                }
                                if (pr.getRequestApprovals() != null)
                                {
                                    pr.getRequestApprovals().forEach(requestApprovalDTO -> {
                                        var employee1 = mapEmployee.get(requestApprovalDTO.getEmployeeId());
                                        if (employee1 != null) {
                                            requestApprovalDTO.setEmployee(employee1);
                                        }
                                    });
                                }
                                if (pr.getType().equals(RequestTypeEnum.PAYMENT)) {
                                    return getPaymentRequestDetailsDTOMono(pr);
                                }
                                if (pr.getType().equals(RequestTypeEnum.REIMBURSEMENT)) {
                                    ReimbursementCriteria reimbursementCriteria = createConditionFilterReimbursement(pr);
                                    return reimbursementRepository.findByCriteria(reimbursementCriteria, null)
                                        .map(reimbursementMapper::toDto)
                                        .collectList()
                                        .defaultIfEmpty(new ArrayList<>())
                                        .flatMap(reimbursements -> {
                                            pr.setReimbursementDTOS(reimbursements);
                                            return getPaymentRequestDetailsDTOMono(pr).then(Mono.just(pr));
                                        });
                                }
                                return Mono.just(pr);
                            }).flatMap(reqA -> {
                                return SecurityUtils.getUserJWTDetail().flatMap(login -> {
                                    return suppliersRepository.findById(pr.getSupplierId(), login.getCompanyId())
                                        .map(suppliersMapper::toDto)
                                        .defaultIfEmpty(new SuppliersDTO())
                                        .flatMap(sup -> {
                                            reqA.setSuppliers(sup);
                                            return Mono.just(reqA);
                                        });
                                });
                            });
                    });
            });
    }

    private ReimbursementCriteria createConditionFilterReimbursement(PaymentRequestDTO pr) {
        ReimbursementCriteria reimbursementCriteria = new ReimbursementCriteria();
        var uuidFilter = new UUIDFilter();
        uuidFilter.setEquals(pr.getId());
        reimbursementCriteria.setReimbursementId(uuidFilter);
        var companyFilter = new StringFilter();
        companyFilter.setEquals(pr.getCompany());
        var booleanFilter = new BooleanFilter();
        booleanFilter.setEquals(false);
        reimbursementCriteria.setIsDeleted(booleanFilter);
        return reimbursementCriteria;
    }

    private Mono<PaymentRequestDTO> getPaymentRequestDetailsDTOMono(PaymentRequestDTO pr) {
        var uuidFilter = new UUIDFilter();
        uuidFilter.setEquals(pr.getId());

        PaymentDetailCriteria paymentDetailCriteria = getDetailCriteria(uuidFilter);

        return paymentDetailRepository.findByCriteria(paymentDetailCriteria, null)
            .map(paymentDetailMapper::toDto)
            .collectList()
            .defaultIfEmpty(new ArrayList<>())
            .flatMap(paymentDetails -> {
                pr.setPaymentDetails(paymentDetails);
                return Mono.just(pr);
            });
    }

    private static @NotNull PaymentDetailCriteria getDetailCriteria(UUIDFilter uuidFilter) {
        ZonedDateTimeFilter deletedAtFilter = new ZonedDateTimeFilter();
        deletedAtFilter.setSpecified(false); // Lọc các record có deleted_at IS NULL

        StringFilter deletedByFilter = new StringFilter();
        deletedByFilter.setSpecified(false); // Lọc các record có deleted_by IS NULL

        PaymentDetailCriteria paymentDetailCriteria = new PaymentDetailCriteria();
        paymentDetailCriteria.setPaymentRequestId(uuidFilter);
        paymentDetailCriteria.setDeletedAt(deletedAtFilter);
        paymentDetailCriteria.setDeletedBy(deletedByFilter);
        return paymentDetailCriteria;
    }


    /**
     * Delete the paymentRequest by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(UUID id) {
        LOG.debug("Request to delete PaymentRequest : {}", id);
        return paymentRequestRepository.deleteById(id);
    }

    // api request approve

    public Mono<PaymentRequestDTO> sendForApproval(UUID id) {
        LOG.debug("Request to sendForApproval PaymentRequest : {}", id);
        return SecurityUtils.getUserJWTDetail().flatMap(login -> {
            return paymentRequestRepository.findById(id)
                .flatMap(paymentRequest -> {
                    if (!(paymentRequest.getStatus().equals(RequestStatus.NEW) || paymentRequest.getStatus().equals(RequestStatus.REJECTED))) {
                        return Mono.error(new BadRequestAlertException("Can not send for approval because not in new status", "paymentRequest", "INVALID_STATUS"));
                    }
                    if (!paymentRequest.getCreatedBy().equals(login.getUserId())) {
                        return Mono.error(new BadRequestAlertException("Can not send for approval because not created by you", "paymentRequest", "INVALID_USER"));
                    }
                    paymentRequest.setStatus(RequestStatus.WAITING_APPROVE);
                    paymentRequest.setIsPersisted();
                    return paymentRequestRepository
                        .save(paymentRequest)
                        .map(paymentRequestMapper::toDto)
                        .flatMap(pr -> {
                            return requestApprovalRepository.findAllByDocumentIdAndIsDeletedIsFalse(id)
                                .switchIfEmpty(Mono.error(new BadRequestAlertException("Not found", "requestApproval", "notFound")))
                                .map(requestApprovalMapper::toDto)
                                .collectList()
                                .flatMap(requestApprovals -> {
                                    return requestApprovalRepository.removeResultByDocumentIdAndCompanyAndDeleted(id, login.getCompanyId())
                                        .then(Mono.just(pr));
                                });

                    });
                });
        });
    }

    // approve
    // api duyệt hoá đơn đầu vào:
    public Mono<PaymentRequestDTO> approve(UUID invoiceId, RequestApprovalDTO requestApprovalDTO) {
        return SecurityUtils.getUserJWTDetail().flatMap(login -> {
            return paymentRequestRepository.findById(invoiceId)
                .switchIfEmpty(Mono.error(new BadRequestAlertException("Not found", "paymentRequest", "notFound")))
                .flatMap(paymentRequest -> {
                    if (!paymentRequest.getStatus().equals(RequestStatus.WAITING_APPROVE)) {
                        return Mono.error(new BadRequestAlertException("can not approve because not in waiting", "paymentRequest", "INVALID_STATUS"));
                    }
                    paymentRequest.setIsPersisted();

                    return requestApprovalRepository.findAllByDocumentIdAndIsDeletedIsFalse(invoiceId)
                        .collectList()
                        .flatMap(requestApprovals -> {
                            var myRequestApproval = requestApprovals.stream()
                                .filter(requestApproval -> requestApproval.getEmployeeId().equals(login.getUserId()))
                                .findFirst()
                                .orElse(null);
                            if (myRequestApproval == null) {
                                return Mono.error(new BadRequestAlertException("Can not approve because not in approver list", "paymentRequest", "INVALID_APPROVER"));
                            }
                            requestApprovals.remove(myRequestApproval);

                            myRequestApproval.approvedSign(requestApprovalDTO.getApprovedSign());
                            myRequestApproval.approvedSignName(requestApprovalDTO.getApprovedSignName());
                            myRequestApproval.result(true);
                            myRequestApproval.setUpdatedAt(ZonedDateTime.now());
                            myRequestApproval.setUpdatedBy(login.getUserId().toString());
                            myRequestApproval.setIsPersisted();
                            var isAllApproved = requestApprovals.stream().allMatch(x -> x.getApprovedSign() != null);
                            if (isAllApproved) {
                                paymentRequest.setStatus(RequestStatus.APPROVED);
                                if (paymentRequest.getType().equals(RequestTypeEnum.REIMBURSEMENT)) {
                                    return reimbursementRepository.findByReimbursementId(paymentRequest.getId(), login.getCompanyId())
                                        .collectList()
                                        .flatMap(reimbursements -> {
                                            var listAdvanceIds = reimbursements.stream().map(Reimbursement::getAdvanceId).toList();
                                            UUIDFilter uuidFilter = new UUIDFilter();
                                            uuidFilter.setIn(listAdvanceIds);
                                            PaymentRequestCriteria paymentRequestCriteria = new PaymentRequestCriteria();
                                            paymentRequestCriteria.setId(uuidFilter);
                                            return paymentRequestRepository.findByCriteria(paymentRequestCriteria, null)
                                                .collectList()
                                                .flatMap(advancement -> {
                                                    advancement.forEach(adv -> {
                                                        adv.setReimbursementDate(ZonedDateTime.now());
                                                        adv.setIsPersisted();
                                                    });
                                                    return paymentRequestRepository.saveAll(advancement)
                                                        .collectList()
                                                        .flatMap(adv -> {
                                                            return requestApprovalRepository
                                                                .save(myRequestApproval)
                                                                .then(
                                                                    paymentRequestRepository.save(paymentRequest)
                                                                );
                                                        });
                                                });
                                        });
                                }
                                return requestApprovalRepository.save(myRequestApproval).then(paymentRequestRepository.save(paymentRequest));
                            }
                            return requestApprovalRepository.save(myRequestApproval).then(paymentRequestRepository.save(paymentRequest));
                        });
                }).map(paymentRequestMapper::toDto);
        });
    }

    // api từ chối hoá đơn đầu vào:
    public Mono<PaymentRequestDTO> reject(UUID invoiceId, RequestApprovalDTO requestApprovalDTO) {
        return SecurityUtils.getUserJWTDetail().flatMap(login -> {
            return paymentRequestRepository.findById(invoiceId)
                .flatMap(paymentRequest -> {
                    if (!paymentRequest.getStatus().equals(RequestStatus.WAITING_APPROVE)) {
                        return Mono.error(new BadRequestAlertException("canNotReject because not in waiting", "paymentRequest", "INVALID_STATUS"));
                    }

                    return requestApprovalRepository.findAllByDocumentIdAndIsDeletedIsFalse(invoiceId).collectList().flatMap(docs -> {
                        var myRequestApproval = docs.stream().filter(requestApproval -> requestApproval.getEmployeeId().equals(login.getUserId())).findFirst().orElse(null);
                        if (myRequestApproval == null) {
                            return Mono.error(new BadRequestAlertException("Can not reject because not in approver list", "paymentRequest", "INVALID_APPROVER"));
                        }

                        myRequestApproval.rejectNote(requestApprovalDTO.getRejectNote());
                        myRequestApproval.result(false);
                        myRequestApproval.setUpdatedAt(ZonedDateTime.now());
                        myRequestApproval.setUpdatedBy(login.getUserId().toString());
                        myRequestApproval.setIsPersisted();
                        paymentRequest.setStatus(RequestStatus.REJECTED);
                        paymentRequest.setIsPersisted();
                        return requestApprovalRepository.save(myRequestApproval).then(paymentRequestRepository.save(paymentRequest));
                    });
                }).map(paymentRequestMapper::toDto);
        });
    }

    public Mono<PaymentRequestDTO> cancel(UUID invoiceId) {
        return SecurityUtils.getUserJWTDetail().flatMap(login -> {
            return paymentRequestRepository.findById(invoiceId)
                .flatMap(paymentRequest -> {
                    if (!(paymentRequest.getStatus().equals(RequestStatus.NEW))) {
                        return Mono.error(new BadRequestAlertException("Can not cancel not rejected invoice", "paymentRequest", "INVALID_STATUS"));
                    }
                    if (!paymentRequest.getCreatedBy().equals(login.getUserId())) {
                        return Mono.error(new BadRequestAlertException("Can not cancel because not created by you", "paymentRequest", "INVALID_USER"));
                    }
                    paymentRequest.setStatus(RequestStatus.CANCELLED);
                    paymentRequest.setIsPersisted();
                    return paymentRequestRepository.save(paymentRequest);
                }).map(paymentRequestMapper::toDto);
        });
    }

    // xuất excel danh sách payment request
    public Mono<String> exportPaymentRequest(List<PaymentRequestDTO> paymentRequestDTOS){
        try {
            Workbook workbook = new XSSFWorkbook();
            var path = "uploaded-files/uniform/";
            // check if folder exists
            var folder = new File(path);
            if (!folder.exists()) {
                var s = folder.mkdirs();
            }
            // Sheet 1: Thông tin đơn hàng
            var orderSheet = workbook.createSheet("Danh sách đề nghị");

            Row headerRow = orderSheet.createRow(0);
            String[] headers = {"Số ĐN", "Ngày ĐN", "Người ĐN", "Nội dung", "Số tiền", "Số tiền đã chi", "Số tiền còn lại", "Trạng thái"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
            }

            var headerData = Flux.fromIterable(paymentRequestDTOS).collectList();
            AtomicInteger rowNum = new AtomicInteger(1);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            return headerData.flatMap(data -> {
                data.forEach(payment -> {
                    var createdByName = "SYSTEM";
                    var createdByCode = "SYSTEM";
                    if(payment.getEmployee() != null) {
                        createdByName = payment.getEmployee().getFullName() == null ? "" : payment.getEmployee().getFullName();
                        createdByCode = payment.getEmployee().getEmployeeCode() == null ? "" : payment.getEmployee().getEmployeeCode();
                    }
                    var employee = createdByCode + " - " + createdByName;
                    Row row = orderSheet.createRow(rowNum.getAndIncrement());
                    row.createCell(0).setCellValue(payment.getCode());
                    var paymentDate = payment.getPaymentDate() != null? payment.getPaymentDate().format(formatter): payment.getCreatedDate().format(formatter);
                    row.createCell(1).setCellValue(paymentDate);
                    row.createCell(1).setCellValue(payment.getPaymentDate().format(formatter));
                    row.createCell(2).setCellValue(createdByName);
                    row.createCell(3).setCellValue(payment.getContent());
                    var totalAmount = getTotalAmount(payment);
                    var amount = getAmount(payment);
                    row.createCell(4).setCellValue(totalAmount);
                    row.createCell(5).setCellValue(amount);
                    row.createCell(6).setCellValue(payment.getRemainingAmount() == null ? "0" : (payment.getRemainingAmount().stripTrailingZeros().scale() <= 0 ? String.valueOf(payment.getRemainingAmount().intValue()) : String.valueOf(payment.getRemainingAmount())));
                    row.createCell(7).setCellValue(payment.getStatus().toVietnameseName());

                });
                for(int i = 0; i < headers.length; i++) {
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

    private static String getAmount(PaymentRequestDTO payment) {
        var amount = payment.getPaidAmount() == null ? "0" : (payment.getPaidAmount().stripTrailingZeros().scale() <= 0 ? String.valueOf(payment.getPaidAmount().intValue()) : String.valueOf(payment.getPaidAmount()));
        if (payment.getType().equals(RequestTypeEnum.ADVANCEMENT))
        {
            amount = payment.getPaymentVoucherAmount() == null ? "0" : (payment.getPaymentVoucherAmount().stripTrailingZeros().scale() <= 0 ? String.valueOf(payment.getPaymentVoucherAmount().intValue()) : String.valueOf(payment.getPaymentVoucherAmount()));
        }
        return amount;
    }

    private static String getTotalAmount(PaymentRequestDTO uniform) {
        var totalAmount = uniform.getTotalAmount() == null ? "0" : (uniform.getTotalAmount().stripTrailingZeros().scale() <= 0 ? String.valueOf(uniform.getTotalAmount().intValue()) : String.valueOf(uniform.getTotalAmount()));
        if (uniform.getType().equals(RequestTypeEnum.REIMBURSEMENT)){
            totalAmount = uniform.getPaymentVoucherAmount() == null ? "0" : (uniform.getPaymentVoucherAmount().stripTrailingZeros().scale() <= 0 ? String.valueOf(uniform.getPaymentVoucherAmount().intValue()) : String.valueOf(uniform.getPaymentVoucherAmount()));
        }
        return totalAmount;
    }

}
