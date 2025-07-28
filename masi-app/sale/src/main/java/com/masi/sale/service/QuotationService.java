package com.masi.sale.service;

import com.masi.sale.domain.Quotation;
import com.masi.sale.domain.enumeration.QuotationStatus;
import com.masi.sale.repository.CustomerRepository;
import com.masi.sale.repository.QuotationDetailRepository;
import com.masi.sale.repository.QuotationRepository;
import com.masi.sale.service.dto.CustomerProcessQuotationDTO;
import com.masi.sale.service.dto.CustomerProcessQuotationDTO.CustomerProcessQuotationStatus;
import com.masi.sale.service.dto.InternalProcessQuotationDTO;
import com.masi.sale.service.dto.InternalProcessQuotationStatus;
import com.masi.sale.service.dto.QuotationCreateDTO;
import com.masi.sale.service.dto.QuotationDTO;
import com.masi.sale.service.dto.QuotationDetailGetListDTO;
import com.masi.sale.service.dto.QuotationGetListDTO;
import com.masi.sale.service.dto.QuotationUpdateDTO;
import com.masi.sale.service.event.NotificationAddedEvent;
import com.masi.sale.service.mapper.CustomerMapper;
import com.masi.sale.service.mapper.QuotationDetailMapper;
import com.masi.sale.service.mapper.QuotationMapper;
import com.masi.sale.service.web.client.EmployeeClient;
import com.masi.sale.utilities.HandleExportQuotation;
import com.masi.sale.web.rest.errors.BadRequestAlertException;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

/**
 * Service Implementation for managing {@link com.masi.sale.domain.Quotation}.
 */
@Service
@Transactional
public class QuotationService {

    private static final Logger log = LoggerFactory.getLogger(QuotationService.class);

    private final QuotationRepository quotationRepository;

    private final QuotationDetailRepository quotationDetailRepository;

    private final QuotationMapper quotationMapper;
    private final QuotationDetailMapper quotationDetailMapper;

    // web client
    private final EmployeeClient employeeClient;

    private final StreamBridge streamBridge;
    private final CustomerRepository customerRepository;

    private final CustomerMapper customerMapper;

    public QuotationService(QuotationRepository quotationRepository, QuotationMapper quotationMapper,
                            QuotationDetailMapper quotationDetailMapper, QuotationDetailRepository quotationDetailRepository,
                            EmployeeClient employeeClient, StreamBridge streamBridge, CustomerRepository customerRepository, CustomerRepository customerRepository1, CustomerMapper customerMapper) {
        this.quotationRepository = quotationRepository;
        this.quotationDetailRepository = quotationDetailRepository;
        this.quotationMapper = quotationMapper;
        this.quotationDetailMapper = quotationDetailMapper;
        this.employeeClient = employeeClient;
        this.streamBridge = streamBridge;
        this.customerRepository = customerRepository1;
        this.customerMapper = customerMapper;
    }

    /**
     * Save a quotation.
     *
     * @param quotationDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<QuotationDTO> save(QuotationDTO quotationDTO) {
        log.debug("Request to save Quotation : {}", quotationDTO);
        return quotationRepository.save(quotationMapper.toEntity(quotationDTO)).map(quotationMapper::toDto);
    }

    public Mono<QuotationDTO> createQuotation(QuotationCreateDTO quotationCreateDTO) {
        log.debug("Request to createQuotation : {}", quotationCreateDTO);
        return quotationRepository.save(quotationMapper.toEntity(quotationCreateDTO
                .toDto())).map(quotationMapper::toDto)
            .flatMap(quotationDTO -> {
                return Flux.fromIterable(quotationCreateDTO.toListDetail(quotationDTO.getId()))
                    .map(quotationDetailMapper::toEntity)
                    .flatMap(quotationDetailRepository::save)
                    .collectList()
                    .map(quotationDetails -> {
                        quotationDTO.setQuotationDetails(new HashSet<>(quotationDetails));
                        return quotationDTO;
                    });
            });
    }

    /**
     * Update a quotation.
     *
     * @param quotationDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<QuotationDTO> update(QuotationDTO quotationDTO) {
        log.debug("Request to update Quotation : {}", quotationDTO);
        return quotationRepository.save(quotationMapper.toEntity(quotationDTO).setIsPersisted())
            .map(quotationMapper::toDto);
    }

    /**
     * Partially update a quotation.
     *
     * @param quotationDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<QuotationDTO> partialUpdate(QuotationDTO quotationDTO) {
        log.debug("Request to partially update Quotation : {}", quotationDTO);

        return quotationRepository
            .findById(quotationDTO.getId())
            .map(existingQuotation -> {
                quotationMapper.partialUpdate(existingQuotation, quotationDTO);

                return existingQuotation;
            })
            .flatMap(quotationRepository::save)
            .map(quotationMapper::toDto);
    }

    public Mono<QuotationDTO> handleUpdateQuotation(QuotationUpdateDTO quotationUpdateDTO) {
        log.debug("handle update quotation: {}", quotationUpdateDTO);
        return quotationRepository.findById(quotationUpdateDTO.getId(), quotationUpdateDTO.getCompany())
            .flatMap(existingQuotation -> {
                if (existingQuotation == null) {
                    return Mono
                        .error(new BadRequestAlertException("Quotation not found", "QUOTATION", "NOT_FOUND"));
                }
                if (existingQuotation.getStatus().equals(QuotationStatus.CUSTOMER_APPROVED) || existingQuotation
                    .getStatus().equals(QuotationStatus.REJECTED)) {
                    return Mono.error(new BadRequestAlertException("Quotation is not editable", "QUOTATION",
                        "NOT_EDITABLE"));
                }
                quotationMapper.partialUpdate(existingQuotation, quotationUpdateDTO.toDto());
                existingQuotation.updatedBy(quotationUpdateDTO.getUpdateBy());
                existingQuotation.setLastUpdated(ZonedDateTime.now());
                return quotationDetailRepository.removeByQuotation(existingQuotation.getId(),
                        quotationUpdateDTO.getCompany(), quotationUpdateDTO.getUpdateBy())
                    .then(quotationDetailRepository.saveAll(quotationDetailMapper.toEntity(quotationUpdateDTO
                            .toListDetail(
                                existingQuotation.getId())))
                        .collectList())
                    .flatMap(savedDetails -> {
                        existingQuotation.setQuotationDetails(new HashSet<>(savedDetails));
                        return quotationRepository.save(existingQuotation.setIsPersisted())
                            .map(quotationMapper::toDto);
                    });
            });
    }

    public Mono<QuotationDTO> handleInternalSendQuotation(UUID id, String company, String updatedBy) {
        log.debug("handle internal sent quotation: {}", id);
        return quotationRepository.findById(id, company).flatMap(existingQuotation -> {
            existingQuotation.setStatus(QuotationStatus.WAITING_APPROVAL);
            existingQuotation.updatedBy(updatedBy);
            existingQuotation.setLastUpdated(ZonedDateTime.now());
            return quotationRepository.save(existingQuotation.setIsPersisted()).map(quotationMapper::toDto);
        });
    }

    public Mono<QuotationDTO> handleCancelQuotation(UUID id, String company, String updatedBy) {
        log.debug("handle internal sent quotation: {}", id);
        return quotationRepository.findById(id, company).flatMap(existingQuotation -> {
            if (existingQuotation.getStatus() == QuotationStatus.SENT
                || existingQuotation.getStatus() == QuotationStatus.CUSTOMER_APPROVED
                || existingQuotation.getStatus() == QuotationStatus.REJECTED) {
                return Mono
                    .error(new BadRequestAlertException("Quotation is not cancellable, status invalid", "QUOTATION",
                        "NOT_CANCELLABLE"));
            }
            existingQuotation.setStatus(QuotationStatus.CANCELLED);
            existingQuotation.updatedBy(updatedBy);
            existingQuotation.setLastUpdated(ZonedDateTime.now());
            return quotationRepository.save(existingQuotation.setIsPersisted()).map(quotationMapper::toDto);
        });
    }

    public Mono<QuotationDTO> handleSendCustomerQuotation(UUID id, String company, String updatedBy) {
        log.debug("handle internal sent quotation: {}", id);
        return quotationRepository.findById(id, company).flatMap(existingQuotation -> {
            if (existingQuotation.getStatus() != QuotationStatus.APPROVED) {
                return Mono
                    .error(new BadRequestAlertException("Quotation is not approved internal", "QUOTATION",
                        "NOT_APPROVED"));
            }
            existingQuotation.setStatus(QuotationStatus.SENT);
            existingQuotation.updatedBy(updatedBy);
            existingQuotation.setLastUpdated(ZonedDateTime.now());
            return quotationRepository.save(existingQuotation.setIsPersisted()).map(quotationMapper::toDto);
        });
    }

    public Mono<QuotationDTO> handleProcessInternalQuotation(InternalProcessQuotationDTO dto) {
        log.debug("handle process internal sent quotation: {}", dto);
        return quotationRepository.findById(dto.getId(), dto.getCompany())
            .switchIfEmpty(Mono.error(new BadRequestAlertException("Quotation not found", "QUOTATION", "NOT_FOUND")))
            .flatMap(existingQuotation -> {
                if (existingQuotation.getStatus() != QuotationStatus.WAITING_APPROVAL) {
                    return Mono.error(new BadRequestAlertException("Quotation is not waiting for approval", "QUOTATION", "NOT_WAITING"));
                }

                existingQuotation.setProcessAt(ZonedDateTime.now());
                existingQuotation.updatedBy(dto.getUpdatedBy());
                existingQuotation.setLastUpdated(ZonedDateTime.now());

                if (dto.getStatus() == InternalProcessQuotationStatus.APPROVED) {
                    updateQuotationForApproval(existingQuotation, dto);
                } else {
                    if (dto.getRejectNote().isBlank()) {
                        return Mono.error(new BadRequestAlertException("Reject note is required", "QUOTATION", "REJECT_NOTE_REQUIRED"));
                    }
                    updateQuotationForRejection(existingQuotation, dto);
                }

                return quotationRepository.save(existingQuotation.setIsPersisted())
                    .doOnSuccess(savedQuotation -> sendNotification(existingQuotation, dto))
                    .map(quotationMapper::toDto);
            });
    }

    private void updateQuotationForApproval(Quotation existingQuotation, InternalProcessQuotationDTO dto) {
        existingQuotation.setStatus(QuotationStatus.APPROVED);
        existingQuotation.setApproverId(UUID.fromString(dto.getUpdatedBy()));
        existingQuotation.setApprovalSignFile(dto.getApprovalSignFile());
        existingQuotation.setApprovalSignName(dto.getApprovalSignName());
    }

    private void updateQuotationForRejection(Quotation existingQuotation, InternalProcessQuotationDTO dto) {
        existingQuotation.setStatus(QuotationStatus.NEED_UPDATE);
        existingQuotation.setRejectNote(dto.getRejectNote());
    }

    private void sendNotification(Quotation existingQuotation, InternalProcessQuotationDTO dto) {
        var map = new HashMap<String, Object>();
        map.put("entity", existingQuotation);

        var eventBuilder = NotificationAddedEvent.builder()
            .id(UUID.randomUUID())
            .content(dto.getStatus() == InternalProcessQuotationStatus.APPROVED
                ? "Bảng báo giá " + existingQuotation.getName() + " đã được duyệt nội bộ"
                : "Bảng báo giá " + existingQuotation.getName() + " cần cập nhật")
            .title("Thông báo duyệt báo giá")
            .createdAt(ZonedDateTime.now())
            .entityId(existingQuotation.getId().toString())
            .entityType("Quotation")
            .entityName("Quotation")
            .createdBy("system")
            .recipients(List.of(existingQuotation.getCreatedBy()))
            .data(map)
            .action(dto.getStatus() == InternalProcessQuotationStatus.APPROVED
                ? "INTERNAL_APPROVED_QUOTATION"
                : "INTERNAL_REJECTED_QUOTATION")
            .sentBy(dto.getUpdatedBy());

        Mono.fromRunnable(() -> {
            try {
                streamBridge.send("notification-added", eventBuilder.build(), MediaType.APPLICATION_JSON);
            } catch (Exception e) {
                log.warn("Failed to send Kafka notification: {}", e.getMessage());
            }
        }).subscribeOn(Schedulers.boundedElastic()).subscribe();
    }


    /**
     * Get all the quotations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<QuotationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Quotations");
        return quotationRepository.findAllBy(pageable).map(quotationMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Flux<QuotationDTO> findAllByQuery(Pageable pageable, QuotationGetListDTO dto) {
        log.debug("Request to get all Quotations by query");
        return quotationRepository.findAllByQuery(pageable, dto)
            .map(quotationMapper::toDto)
            .flatMap(quotationDTO -> {
                var detailDto = new QuotationDetailGetListDTO();
                detailDto.setQuotationId(quotationDTO.getId());
                return quotationDetailRepository.findAllByQuery(null,
                        detailDto)
                    .collectList()
                    .map(quotationDetails -> {
                        quotationDTO.setQuotationDetails(new HashSet<>(quotationDetails));
                        return quotationDTO;
                    });
            });
    }

    /**
     * Returns the number of quotations available.
     *
     * @return the number of entities in the database.
     */
    public Mono<Long> countAll() {
        return quotationRepository.count();
    }

    public Mono<Long> countByQuery(QuotationGetListDTO dto) {
        return quotationRepository.countByQuery(dto);
    }


//    @Scheduled(cron = "0 16 15 * * *")
//    public Mono<Void> temp() {
////        75877540-c0ba-484b-b1bc-6b3f963733cd
//        log.debug("Request to get Quotation : {}", "75877540-c0ba-484b-b1bc-6b3f963733cd");
//        return findOne(UUID.fromString("75877540-c0ba-484b-b1bc-6b3f963733cd"), "KIM_LONG")
//            .flatMap(existingQuotation -> {
//                try {
//                    log.debug("Request to get Quotation 2 : {}", existingQuotation);
//                    FileOutputStream fos = new FileOutputStream("C:\\Users\\Laffy\\AppData\\Local\\Temp\\quotation131212.bin");
//                    ObjectOutputStream oos = new ObjectOutputStream(fos);
//                    oos.writeObject(existingQuotation);
//                    oos.close();
//                    fos.close();
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                return Mono.empty();
//            }).then();
//    }

    /**
     * Get one quotation by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<QuotationDTO> findOne(UUID id, String company) {
        log.debug("Request to get Quotation : {}", id);
        return quotationRepository.findById(id, company).map(quotationMapper::toDto).flatMap(rs -> {
            var listEmployee = new ArrayList<UUID>();
            listEmployee.add(UUID.fromString(rs.getCreatedBy()));
            if (rs.getApproverId() != null)
                listEmployee.add(rs.getApproverId());
            if (rs.getUpdatedBy() != null)
                listEmployee.add(UUID.fromString(rs.getUpdatedBy()));
            if (rs.getCustomerApproverId() != null)
                listEmployee.add(rs.getCustomerApproverId());
            var setEmployee = new HashSet<>(listEmployee);
            return customerRepository.findByIdAndIsDeleted(rs.getCustomerId(), company).map(customerMapper::toDto).flatMap(customer -> {
                rs.setCustomer(customer);
                return employeeClient.getEmployeesByListIds(new ArrayList<>(setEmployee))
                    .collectList()
                    .flatMap(employees -> {
                        employees.forEach(x -> {
                            if (x.getId().equals(UUID.fromString(rs.getCreatedBy()))) {
                                rs.setCreatedByEmployee(x);
                            }

                            if (x.getId() == rs.getApproverId())
                                rs.setApprover(x);

                            if (x.getId() == rs.getCustomerApproverId()) {
                                rs.setCustomerApprover(x);
                            }
                            if (Objects.equals(x.getId().toString(), rs.getUpdatedBy())) {
                                rs.setUpdatedByEmployee(x);
                            }
                        });
                        var detailDto = new QuotationDetailGetListDTO();
                        detailDto.setQuotationId(rs.getId());
                        return quotationDetailRepository.findAllByQuery(null, detailDto).collectList()
                            .flatMap(detail -> {
                                rs.setQuotationDetails(new HashSet<>(detail));
                                return Mono.just(rs);
                            });
                    }).then(Mono.just(rs));
            });
        });
    }

    /**
     * Delete the quotation by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(UUID id) {
        log.debug("Request to delete Quotation : {}", id);
        return quotationRepository.deleteById(id);
    }

    public Mono<Void> removeQuotation(UUID id, String company, String deletedBy) {
        log.debug("Request to remove Quotation : {}", id);
        return quotationRepository.findById(id, company)
            .flatMap(existingQuotation -> {
                if (existingQuotation == null) {
                    return Mono
                        .error(new BadRequestAlertException("Quotation not found", "QUOTATION", "NOT_FOUND"));
                }
                if (existingQuotation.getStatus().equals(QuotationStatus.CUSTOMER_APPROVED) || existingQuotation
                    .getStatus().equals(QuotationStatus.REJECTED)) {
                    return Mono.error(new BadRequestAlertException("Quotation is not editable", "QUOTATION",
                        "NOT_EDITABLE"));
                }
                return quotationRepository.removeById(id, company, deletedBy).then(
                    quotationDetailRepository.removeByQuotation(id, company, deletedBy).then());
            });

    }

    public Mono<QuotationDTO> handleCustomerProcessQuotation(CustomerProcessQuotationDTO dto) {
        log.debug("handle process internal sent quotation: {}", dto);
        return quotationRepository.findById(dto.getId(), dto.getCompany()).flatMap(existingQuotation -> {
//            if (existingQuotation.getStatus() != QuotationStatus.SENT) {
//                return Mono.error(new BadRequestAlertException("Quotation is not SENT", "QUOTATION",
//                    "NOT_SENT"));
//            }

            if (dto.getStatus() == CustomerProcessQuotationStatus.CUSTOMER_APPROVED) {
//                if (dto.getFileId() == null || dto.getFileId().isBlank()) {
//                    return Mono.error(new BadRequestAlertException("File is required", "QUOTATION",
//                        "FILE_REQUIRED"));
//                }
                existingQuotation.setStatus(QuotationStatus.CUSTOMER_APPROVED);
                existingQuotation.updatedBy(dto.getUpdatedBy());
                existingQuotation.setLastUpdated(ZonedDateTime.now());
                existingQuotation.setFileId(dto.getFileId());
                existingQuotation.setFileName(dto.getFileName());
                existingQuotation.setCustomerApproverId(UUID.fromString(dto.getUpdatedBy()));
                // var map = new HashMap<String, Object>();
                // map.put("entity", existingQuotation);
                // var eventBuilder = NotificationAddedEvent.builder()
                // .id(UUID.randomUUID())
                // .content("Bảng báo giá đã được khách hàng duyệt")
                // .title("Thông báo duyệt báo giá")
                // .createdAt(ZonedDateTime.now())
                // .entityId(existingQuotation.getId().toString())
                // .entityType("Quotation")
                // .entityName("Quotation")
                // .createdBy("system")
                // .createdAt(ZonedDateTime.now())
                // .recipients(List.of(existingQuotation.getCreatedBy()))
                // .data(map)
                // .action("CUSTOMER_APPROVED_QUOTATION")
                // .sentBy(existingQuotation.getApproverId().toString());
                // streamBridge.send("notification-added", eventBuilder.build(),
                // MediaType.APPLICATION_JSON);

            } else {
//                if (StringUtils.isBlank(dto.getRejectNote())) {
//                    return Mono.error(new BadRequestAlertException("Reject note is required", "QUOTATION",
//                        "REJECT_NOTE_REQUIRED"));
//                }
                existingQuotation.setStatus(QuotationStatus.REJECTED);
                existingQuotation.setCustomerRejectNote(dto.getRejectNote());
                existingQuotation.updatedBy(dto.getUpdatedBy());
                existingQuotation.setLastUpdated(ZonedDateTime.now());
                // var map = new HashMap<String, Object>();
                // map.put("entity", existingQuotation);
                // var eventBuilder = NotificationAddedEvent.builder()
                // .id(UUID.randomUUID())
                // .content("Bảng báo giá đã cần cập nhật")
                // .title("Thông báo duyệt báo giá")
                // .createdAt(ZonedDateTime.now())
                // .entityId(existingQuotation.getId().toString())
                // .entityType("Quotation")
                // .entityName("Quotation")
                // .createdBy("system")
                // .createdAt(ZonedDateTime.now())
                // .recipients(List.of(existingQuotation.getCreatedBy()))
                // .data(map)
                // .action("INTERNAL_REJECTED_QUOTATION")
                // .sentBy(existingQuotation.getApproverId().toString());
                // streamBridge.send("notification-added", eventBuilder.build(),
                // MediaType.APPLICATION_JSON);
            }

            return quotationRepository.save(existingQuotation.setIsPersisted()).map(quotationMapper::toDto);
        });
    }
}
