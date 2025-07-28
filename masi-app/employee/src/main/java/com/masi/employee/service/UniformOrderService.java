package com.masi.employee.service;

import com.masi.employee.domain.UniformFormDetail;
import com.masi.employee.domain.UniformOrder;
import com.masi.employee.domain.enumeration.UniformOrderProcessStatus;
import com.masi.employee.domain.enumeration.UniformOrderStatus;
import com.masi.employee.repository.*;
import com.masi.employee.service.dto.*;
import com.masi.employee.service.mapper.UniformFormDetailMapper;
import com.masi.employee.service.mapper.UniformOrderMapper;
import com.masi.employee.service.mapper.UniformOrderProcessMapper;
import com.masi.employee.service.mapper.UniformOrderStockMapper;
import com.masi.employee.service.web.client.FileClient;
import com.masi.employee.web.rest.errors.BadRequestAlertException;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing
 * {@link com.masi.employee.domain.UniformOrder}.
 */
@Service
@Transactional
public class UniformOrderService {

    private static final Logger log = LoggerFactory.getLogger(UniformOrderService.class);
    private static final String ENTITY_NAME = "masiEmployeeUniformOrder";

    private final UniformOrderRepository uniformOrderRepository;
    private final UniformOrderProcessRepository uniformOrderProcessRepository;
    private final UniformFormDetailRepository uniformFormDetailRepository;

    private final UniformOrderMapper uniformOrderMapper;
    private final UniformOrderProcessMapper uniformOrderProcessMapper;
    private final UniformFormDetailMapper uniformFormDetailMapper;
    private final UniformOrderStockMapper uniformOrderStockMapper;

    private final UniformOrderStockRepository uniformOrderStockRepository;

    private DocumentSequenceService documentSequenceService;

    private final FileClient fileClient;
    private final LogisticClient logisticClient;
    private UniformFormDetailService uniformFormDetailService;

    private UniformStockService uniformStockService;
    private final UniformRepository uniformRepository;

    @Autowired
    public void setDocumentSequenceService(DocumentSequenceService documentSequenceService) {
        this.documentSequenceService = documentSequenceService;
    }

    @Autowired
    public void setUniformStockService(UniformStockService uniformStockService) {
        this.uniformStockService = uniformStockService;
    }

    @Autowired
    public void setUniformFormDetailService(UniformFormDetailService uniformFormDetailService) {
        this.uniformFormDetailService = uniformFormDetailService;
    }

    @Lazy
    public UniformOrderService(UniformOrderRepository uniformOrderRepository,
                               UniformOrderMapper uniformOrderMapper,
                               UniformOrderProcessRepository uniformOrderProcessRepository,
                               UniformOrderProcessMapper uniformOrderProcessMapper,
                               UniformFormDetailService uniformFormDetailService,
                               UniformFormDetailRepository uniformFormDetailRepository, LogisticClient logisticClient,
                               UniformStockService uniformStockService,
                               UniformFormDetailMapper uniformFormDetailMapper, UniformOrderStockMapper uniformOrderStockMapper, UniformOrderStockRepository uniformOrderStockRepository, FileClient fileClient,
                               DocumentSequenceService documentSequenceService, UniformRepository uniformRepositoty) {
        this.uniformOrderRepository = uniformOrderRepository;
        this.uniformOrderProcessRepository = uniformOrderProcessRepository;
        this.uniformFormDetailRepository = uniformFormDetailRepository;
        this.uniformOrderMapper = uniformOrderMapper;
        this.uniformOrderProcessMapper = uniformOrderProcessMapper;
        this.uniformFormDetailService = uniformFormDetailService;
        this.logisticClient = logisticClient;
        this.uniformStockService = uniformStockService;
        this.uniformFormDetailMapper = uniformFormDetailMapper;
        this.uniformOrderStockMapper = uniformOrderStockMapper;
        this.uniformOrderStockRepository = uniformOrderStockRepository;
        this.fileClient = fileClient;
        this.documentSequenceService = documentSequenceService;
        this.uniformRepository = uniformRepositoty;
    }

    /**
     * Save a uniformOrder.
     *
     * @param uniformOrderDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<UniformOrderDTO> save(UniformOrderDTO uniformOrderDTO) {
        log.debug("Request to save UniformOrder : {}", uniformOrderDTO);
        return documentSequenceService.findOrInitSequence(ENTITY_NAME, uniformOrderDTO.getCompany())
            .switchIfEmpty(Mono.error(new BadRequestAlertException("Document sequence not found", ENTITY_NAME, "NOT_FOUND")))
            .flatMap(sequence -> {
                System.out.println("sequence: " + sequence);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMyy");
                LocalDate date = LocalDate.now(); // or any other LocalDate instance
                String formattedDate = date.format(formatter);
                var seqFormat = String.format("/%d", sequence);
                if (sequence < 10000) {
                    seqFormat = String.format("/%04d", sequence);
                }
                var code = "UO" + formattedDate + seqFormat;
                uniformOrderDTO.setCode(code);
                return uniformOrderRepository.save(uniformOrderMapper.toEntity(uniformOrderDTO))
                    .map(uniformOrderMapper::toDto);
            });
    }

    /**
     * Update a uniformOrder.
     *
     * @param uniformOrderDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<UniformOrderDTO> update(UniformOrderDTO uniformOrderDTO) {
        log.debug("Request to update UniformOrder : {}", uniformOrderDTO);
        return uniformOrderRepository.save(uniformOrderMapper.toEntity(uniformOrderDTO).setIsPersisted())
            .map(uniformOrderMapper::toDto);
    }

    /**
     * Partially update a uniformOrder.
     *
     * @param uniformOrderDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<UniformOrderDTO> partialUpdate(UniformOrderDTO uniformOrderDTO) {
        log.debug("Request to partially update UniformOrder : {}", uniformOrderDTO);

        return uniformOrderRepository
            .findById(uniformOrderDTO.getId())
            .map(existingUniformOrder -> {
                uniformOrderMapper.partialUpdate(existingUniformOrder, uniformOrderDTO);
                existingUniformOrder.setIsPersisted();
                return existingUniformOrder;
            })
            .flatMap(uniformOrderRepository::save)
            .map(uniformOrderMapper::toDto);
    }

    public Mono<UniformOrderDTO> handlePartialUpdate(UniformOrderDTO uniformOrderDTO,
                                                     List<UniformFormDetailDTO> uniformFormDetailDTOs) {
        log.debug("Request to handlePartialUpdate UniformOrder : {}", uniformOrderDTO);

        return uniformOrderRepository.findById(uniformOrderDTO.getId())
            .switchIfEmpty(Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, null)))
            .flatMap(existingUniformOrder -> handleUpdateOrderUniform(existingUniformOrder, uniformOrderDTO,
                uniformFormDetailDTOs));
    }

    private Mono<UniformOrderDTO> handleUpdateOrderUniform(UniformOrder existingUniformOrder,
                                                           UniformOrderDTO uniformOrderDTO,
                                                           List<UniformFormDetailDTO> uniformFormDetailDTOs) {
        log.info("Request to handleUpdateOrderUniform UniformOrder : {}", uniformFormDetailDTOs);
        return uniformOrderProcessRepository.findByUniformOrder(existingUniformOrder.getId())
            .collectList()
            .flatMap(uniformOrderProcess -> {
                boolean isCanUpdate = false;
                if ((uniformOrderProcess.isEmpty() || uniformOrderProcess == null)
                    && !existingUniformOrder.getStatus().equals(UniformOrderStatus.WAITING)) {
                    isCanUpdate = true;
                } else {
                    isCanUpdate = uniformOrderProcess.stream()
                        .allMatch(process -> process.getStatus().toString()
                            .equals(UniformOrderProcessStatus.WAITING.toString()));
                }

                if (!isCanUpdate) {
                    return Mono.error(new BadRequestAlertException("process status invalid", "uniformOrderProcess",
                        "INVALID_STATUS"));
                }

                uniformOrderMapper.partialUpdate(existingUniformOrder, uniformOrderDTO);
                return uniformFormDetailService.deleteAndCreateByUniformOrderId(existingUniformOrder.getId(),
                        uniformOrderDTO.getUpdateBy(), uniformFormDetailDTOs)
                    .then(uniformOrderRepository.save(existingUniformOrder.setIsPersisted()))
                    .map(uniformOrderMapper::toDto);
            });
    }

    public Mono<UniformOrderDTO> handleProcessUniformOrder(UniformOrderHandleProcessDTO uniformOrderHandleProcessDTO) {
        log.debug("Request to handleProcessUniformOrder UniformOrder : {}", uniformOrderHandleProcessDTO);
        return switch (uniformOrderHandleProcessDTO.getStatus()) {
            case APPROVED -> handleApproveProcessUniformOrder(uniformOrderHandleProcessDTO);
            case REJECTED -> handleRejectProcessUniformOrder(uniformOrderHandleProcessDTO);
            default ->
                throw new BadRequestAlertException("process status invalid", "uniformOrderProcess", "INVALID_STATUS");
        };
    }

    // method handle case approve:

    /**
     * Handle approve process uniform order.
     *
     * @param uniformOrderHandleProcessDTO the uniform order handle process DTO
     * @return the uniform order DTO
     */
    private Mono<UniformOrderDTO> handleApproveProcessUniformOrder(
        UniformOrderHandleProcessDTO uniformOrderHandleProcessDTO) {
        if (uniformOrderHandleProcessDTO.getFileId().isBlank()) {
            return Mono.error(
                new BadRequestAlertException("fileId is required", "uniformOrderProcess", "FILE_ID_REQUIRED"));
        }

        var orderId = uniformOrderHandleProcessDTO.getUniformOrderDTO().getId();
        return uniformOrderRepository.findByIdAndDeleteAtIsNullAndDeleteByIsNull(orderId).flatMap(orderEntity -> {
            return uniformOrderProcessRepository
                .findByUniformOrderAndStatus(orderId, UniformOrderProcessStatus.WAITING.toString())
                .collectList()
                .flatMap(approver -> {
                    log.info(ENTITY_NAME + " handleApproveProcessUniformOrder approver: {}", approver);
                    var isNull = approver.stream().anyMatch(app -> app.getApproverId() == null);
                    if (isNull && approver.size() == 1) {
                        // Trường hợp này sẽ không xảy ra vì switchIfEmpty đã xử lý, nhưng để an toàn
                        // thêm logic này.
                        var thisProcessEntity = approver.get(0);
                        orderEntity.setUpdateBy(uniformOrderHandleProcessDTO.getUpdateBy());
                        orderEntity.setUpdateAt(uniformOrderHandleProcessDTO.getUpdateAt());
                        orderEntity.setStatus(UniformOrderStatus.APPROVED);
                        thisProcessEntity.setStatus(UniformOrderProcessStatus.APPROVED);
                        thisProcessEntity.setFileId(uniformOrderHandleProcessDTO.getFileId());
                        thisProcessEntity.setFileName(uniformOrderHandleProcessDTO.getFileName());
                        thisProcessEntity.setUpdateAt(ZonedDateTime.now());
                        thisProcessEntity.setUpdateBy(uniformOrderHandleProcessDTO.getUpdateBy());
                        var a = uniformOrderProcessRepository.save(thisProcessEntity.setIsPersisted());
                        return a.flatMap(
                            x -> uniformOrderRepository.save(orderEntity).map(uniformOrderMapper::toDto));

                    }

                    var thisProcessEntity = approver.stream().filter(app -> app.getApproverId()
                        .equals(uniformOrderHandleProcessDTO.getApproverId())).findFirst().orElse(null);
                    if (thisProcessEntity == null) {
                        return Mono.error(new BadRequestAlertException("approver not found", "uniformOrderProcess",
                            "APPROVER_NOT_FOUND"));
                    }
                    thisProcessEntity.setStatus(UniformOrderProcessStatus.APPROVED);
                    thisProcessEntity.setFileId(uniformOrderHandleProcessDTO.getFileId());
                    thisProcessEntity.setFileName(uniformOrderHandleProcessDTO.getFileName());
                    thisProcessEntity.setUpdateAt(ZonedDateTime.now());
                    thisProcessEntity.setUpdateBy(uniformOrderHandleProcessDTO.getUpdateBy());
                    var a = uniformOrderProcessRepository.save(thisProcessEntity.setIsPersisted());
                    return a.flatMap(thisProcess -> {
                        return uniformOrderProcessRepository.findByUniformOrder(orderId)
                            .collectList()
                            .flatMap(processes -> {
                                if (processes.isEmpty()) {
                                    return Mono.error(new BadRequestAlertException("process not found",
                                        "uniformOrderProcess", "NOT_FOUND"));
                                }
                                var isAllApproved = processes.stream()
                                    .allMatch(process -> process.getStatus()
                                        .equals(UniformOrderProcessStatus.APPROVED));
                                if (isAllApproved) {
                                    orderEntity.setUpdateBy(uniformOrderHandleProcessDTO.getUpdateBy());
                                    orderEntity.setUpdateAt(uniformOrderHandleProcessDTO.getUpdateAt());
                                    orderEntity.setStatus(UniformOrderStatus.APPROVED);
                                    return uniformOrderRepository.save(
                                            orderEntity)
                                        .map(uniformOrderMapper::toDto);
                                }
                                return Mono.just(uniformOrderMapper.toDto(orderEntity));
                            });
                    });
                })
                .switchIfEmpty(Mono.error(new BadRequestAlertException("approver not found", "uniformOrderProcess",
                    "APPROVER_NOT_FOUND")));
        });

    }

    private Mono<UniformOrderDTO> handleRejectProcessUniformOrder(
        UniformOrderHandleProcessDTO uniformOrderHandleProcessDTO) {
        if (uniformOrderHandleProcessDTO.getReason().isBlank()) {
            return Mono.error(
                new BadRequestAlertException("reason is required", "uniformOrderProcess", "REASON_REQUIRED"));
        }
        var orderId = uniformOrderHandleProcessDTO.getUniformOrderDTO().getId();
        return uniformOrderRepository.findByIdAndDeleteAtIsNullAndDeleteByIsNull(orderId).flatMap(orderEntity -> {
            return uniformOrderProcessRepository
                .findByUniformOrderAndStatus(orderId, UniformOrderProcessStatus.WAITING.toString())
                .collectList()
                .flatMap(approver -> {
                    var isNull = approver.stream().anyMatch(app -> app.getApproverId() == null);
                    if (isNull && approver.size() == 1) {
                        // Trường hợp này sẽ không xảy ra vì switchIfEmpty đã xử lý, nhưng để an toàn
                        // thêm logic này.
                        var thisProcessEntity = approver.get(0);
                        orderEntity.setUpdateBy(uniformOrderHandleProcessDTO.getUpdateBy());
                        orderEntity.setUpdateAt(uniformOrderHandleProcessDTO.getUpdateAt());
                        orderEntity.setStatus(UniformOrderStatus.REJECTED);
                        thisProcessEntity.setStatus(UniformOrderProcessStatus.REJECTED);
                        thisProcessEntity.setReason(uniformOrderHandleProcessDTO.getReason());
                        thisProcessEntity.setUpdateAt(ZonedDateTime.now());
                        thisProcessEntity.setUpdateBy(uniformOrderHandleProcessDTO.getUpdateBy());
                        var a = uniformOrderProcessRepository.save(thisProcessEntity.setIsPersisted());
                        return a.flatMap(
                            x -> uniformOrderRepository.save(orderEntity).map(uniformOrderMapper::toDto));

                    }
                    var thisProcessEntity = approver.stream().filter(app -> app.getApproverId()
                        .equals(uniformOrderHandleProcessDTO.getApproverId())).findFirst().orElse(null);
                    if (thisProcessEntity == null) {
                        return Mono.error(new BadRequestAlertException("approver not found", "uniformOrderProcess",
                            "APPROVER_NOT_FOUND"));
                    }
                    thisProcessEntity.setStatus(UniformOrderProcessStatus.REJECTED);
                    thisProcessEntity.setReason(uniformOrderHandleProcessDTO.getReason());
                    thisProcessEntity.setUpdateAt(ZonedDateTime.now());
                    thisProcessEntity.setUpdateBy(uniformOrderHandleProcessDTO.getUpdateBy());
                    var a = uniformOrderProcessRepository.save(thisProcessEntity.setIsPersisted());
                    return a.flatMap(x -> uniformOrderRepository.save(
                            orderEntity)
                        .map(uniformOrderMapper::toDto));
                }).switchIfEmpty(Mono.defer(() -> {
                    // Cập nhật thông tin đơn hàng và lưu lại nếu không tìm thấy approver
                    return Mono.error(new BadRequestAlertException("approver not found", "uniformOrderProcess",
                        "APPROVER_NOT_FOUND"));
                }));
        });
    }

    /**
     * Get all the uniformOrders.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<UniformOrderDTO> findAll(Pageable pageable) {
        log.debug("Request to get all UniformOrders");
        return uniformOrderRepository.findAllBy(pageable).map(uniformOrderMapper::toDto);
    }

    /**
     * Returns the number of uniformOrders available.
     *
     * @return the number of entities in the database.
     */
    public Mono<Long> countAll() {
        return uniformOrderRepository.count();
    }

    /**
     * Get one uniformOrder by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<UniformOrderDTO> findOne(UUID id) {
        log.debug("Request to get UniformOrder : {}", id);
        return uniformOrderRepository.findByIdAndDeleteAtIsNullAndDeleteByIsNull(id).map(uniformOrderMapper::toDto)
            .flatMap(uniformOrder -> {
                return uniformFormDetailService.findAllByUniformOrderId(uniformOrder.getId())
                    .collectList()
                    .flatMap(uniformFormDetailDTOs -> {
                        uniformOrder.setUniformFormDetails(uniformFormDetailDTOs);
                        return uniformOrderProcessRepository.findAllByOrderId(uniformOrder.getId())
                            .collectList().map(uniformOrderProcessMapper::toDto)
                            .flatMap(uniformOrderProcesses -> {
                                Function<String, UUID> tryParseUUidFunc = (String idStr) -> {
                                    try {
                                        return UUID.fromString(idStr);
                                    } catch (Exception e) {
                                        return UUID.randomUUID();
                                    }
                                };
                                Map<UUID, UniformOrderProcessDTO> processStatusMap = new HashMap<>();
                                uniformOrderProcesses.forEach(process -> {
                                    processStatusMap.put(tryParseUUidFunc.apply(process.getFileId()),
                                        process);
                                });
                                return fileClient
                                    .getFileAttachmentsByListIds(
                                        new HashSet<>(processStatusMap.keySet()))
                                    .collectList()
                                    .flatMap(fileAttachments -> {
                                        fileAttachments.forEach(fileAttachment -> {
                                            processStatusMap.get(fileAttachment.getId())
                                                .setFile(fileAttachment);
                                        });
                                        return Mono.just(uniformOrderProcesses);
                                    }).then(Mono.just(uniformOrderProcesses));
                            })
                            .flatMap(uniformOrderProcesses -> {
                                uniformOrder.setUniformOrderProcesses(uniformOrderProcesses);
                                return uniformRepository.findAllByCompanyAndDeleteAtIsNull(null, uniformOrder.getCompany()).collectList().flatMap(uniforms -> {
                                    return uniformOrderStockRepository.createQueryCondition(uniformOrder.getId(), uniformOrder.getCompany(), uniforms)
                                        .collectList()
                                        .map(uniformOrderStockMapper::toDto)
                                        .flatMap(uniformOrderStocks -> {
                                            uniformOrder.setUniformOrderStockDTOS(uniformOrderStocks);
                                            log.info("Request to get UniformOrder : {}", uniformOrder);
                                            log.debug("Request to get UniformOrder : {}", uniformOrder);
                                            return logisticClient.getUniformOrderResponse(uniformOrder)
                                                .flatMap(res -> {
                                                    log.debug("\n\n\n\n\n\nRequest to get UniformOrder :\n\n\n\n\n {}\n\n\n\n\n\n\n", res);
                                                    log.info("\n\n\n\n\n\nRequest to get UniformOrder :\n\n\n\n\n {}\n\n\n\n\n\n\n", res);
                                                    if (res.getSupplierName().containsKey(uniformOrder.getSupplierId())) {
                                                        uniformOrder.setSupplierName(res.getSupplierName().get(uniformOrder.getSupplierId()));
                                                        log.info("Request to get getSupplierName : {}", uniformOrder.getSupplierName());
                                                    }
                                                    uniformOrder.getUniformOrderStockDTOS().forEach(uniformOrderStockDTO -> {
                                                        if (res.getWarehouseMap().containsKey(uniformOrderStockDTO.getWarehouseId())) {
                                                            uniformOrderStockDTO.setWareHouseDTO(res.getWarehouseMap().get(uniformOrderStockDTO.getWarehouseId()));
                                                        }
                                                        uniformOrderStockDTO.getUniformFormDetail().forEach(uniformFormDetailDTO -> {
                                                            if (res.getUomMap().containsKey(uniformFormDetailDTO.getUomId())) {
                                                                uniformFormDetailDTO.setUomName(res.getUomMap().get(uniformFormDetailDTO.getUomId()).getName());
                                                            }
                                                        });
                                                    });

                                                    uniformOrder.getUniformFormDetails().forEach(uniformFormDetailDTO -> {
                                                        if (res.getUomMap().containsKey(uniformFormDetailDTO.getUomId())) {
                                                            uniformFormDetailDTO.setUomName(res.getUomMap().get(uniformFormDetailDTO.getUomId()).getName());
                                                        }
                                                    });
                                                    return Mono.just(uniformOrder);
                                                });
                                        });
                                });
                            });
                    });
            });
    }

    /**
     * Delete the uniformOrder by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(UUID id) {
        log.debug("Request to delete UniformOrder : {}", id);
        return uniformOrderRepository.deleteById(id);
    }

    /**
     * @param pageable
     * @param uniformOrderGetListDTO
     * @return
     */
    public Flux<UniformOrderDTO> findAllByQuery(Pageable pageable, UniformOrderGetListDTO uniformOrderGetListDTO) {
        return uniformOrderRepository.findAllByQuery(pageable, uniformOrderGetListDTO).map(uniformOrderMapper::toDto)
            .flatMap(uniform -> {
                return uniformFormDetailService.findAllByUniformOrderId(uniform.getId()).collectList()
                    .flatMap(uniformFormDetailDTOS -> {
                        uniform.setUniformFormDetails(uniformFormDetailDTOS);
                        return uniformRepository.findAllByCompanyAndDeleteAtIsNull(null, uniform.getCompany())
                            .collectList()
                            .flatMap(uniforms -> {
                                return uniformOrderStockRepository.createQueryCondition(uniform.getId(), uniform.getCompany(), uniforms)
                                    .collectList()
                                    .map(uniformOrderStockMapper::toDto)
                                    .handle((uniformOrderStocks, sink) -> {
                                        try {
//                                            uniformOrders.forEach(uniformOrderDTO -> {
//                                                var importedQuantity = 0;
//                                                importedQuantity = uniformOrderDTO.getUniformFormDetails().stream().mapToInt(UniformFormDetailDTO::getQuantityChange).sum();
//                                                var remainQuantity = uniformOrderDTO.getQuantity() - importedQuantity;
//                                                uniformOrderDTO.setRemainQuantity(remainQuantity);
//                                            });
                                            var importedQuantity = uniform.getUniformFormDetails().stream().mapToInt(x -> x.getQuantityChange() == null ? 0 : x.getQuantityChange()).sum();
                                            var remainQuantity = uniform.getQuantity() == null ? 0 : uniform.getQuantity() - importedQuantity;
                                            uniform.setRemainQuantity(remainQuantity);
                                            sink.next(uniform);
                                        } catch (Exception e) {
                                            sink.error(e);
                                        }
                                    });
                        });
                    });
            });
    }

    /**
     * Count all by query.
     *
     * @param uniformOrderGetListDTO the uniform order get list DTO
     * @return the mono
     */
    public Mono<Long> countAllByQuery(UniformOrderGetListDTO uniformOrderGetListDTO) {
        return uniformOrderRepository.countAllByQuery(uniformOrderGetListDTO);
    }

    public Mono<Void> deleteByUniformOrder(UUID id, String deleteBy) {
        return uniformFormDetailRepository.deleteByUniformOrder(id, deleteBy)
            .then(uniformFormDetailRepository.deleteByUniformOrder(id, deleteBy)
                .then(uniformOrderRepository.deleteById(id, deleteBy)))
            .then();
    }


    // write java doc:
    /**
     * Create uniform order stock v2 mono.
     *
     * @param uniformOrderStockDTO the uniform order stock DTO
     * @return the mono
     */
    public Mono<UniformOrderStockDTO> createUniformOrderStockV2(UniformOrderStockDTO uniformOrderStockDTO) {
        log.debug("Request to create UniformOrder stock v2 : {}", uniformOrderStockDTO);
        if (uniformOrderStockDTO.getUniformFormDetailDTO().isEmpty()) {
            return Mono.error(new BadRequestAlertException("uniformFormDetailDTO is required", ENTITY_NAME, "UNIFORM_FORM_DETAIL_REQUIRED"));
        }
        return uniformOrderRepository.findByIdAndDeleteAtIsNullAndDeleteByIsNull(uniformOrderStockDTO.getUniformOrderId())
            .flatMap(existingUniformOrder -> {
                if (!existingUniformOrder.getStatus().equals(UniformOrderStatus.APPROVED) && !existingUniformOrder.getStatus().equals(UniformOrderStatus.PROCESSING)) {
                    return Mono
                        .error(new BadRequestAlertException("order not approved", ENTITY_NAME, "NOT_APPROVED"));
                }
                return uniformFormDetailRepository.findAllByUniformOrderId(existingUniformOrder.getId())
                    .collectList()
                    .flatMap(orderDetail -> {
                                var listId = orderDetail.stream().map(UniformFormDetail::getUniformId).toList();
                                HashSet<UUID> setDetail = new HashSet<>(listId);
                                // tìm xem có uniform nào không có trong order không
                                HashSet<UniformFormDetail> updateList = new HashSet<>();
                                for (var item : uniformOrderStockDTO.getUniformFormDetailDTO()) {
                                    // find old value
                                    var oldValue = orderDetail.stream().filter(x -> x.getUniformId().equals(item.getUniformId())).findFirst().orElse(null);
                                    if (oldValue != null) {
                                        // nếu không có trong order thì không cho tạo
                                        if (!setDetail.contains(item.getUniformId())) {
                                            return Mono.error(new BadRequestAlertException("Order not have uniform", ENTITY_NAME, "NOT_FOUND"));
                                        }
                                        var oldReturned = oldValue.getQuantityChange() == null ? 0 : oldValue.getQuantityChange();
                                        // nếu có trong order thì cập nhật lại số lượng trả về
                                        var newReturned = item.getQuantity() == null ? 0 : item.getQuantity();
                                        if (newReturned > oldValue.getQuantity() || newReturned + oldReturned > oldValue.getQuantity()) {
                                            return Mono.error(new BadRequestAlertException("Invalid import quantity", ENTITY_NAME, "INVALID_RETURNED"));
                                        }
                                        oldValue.setQuantityChange(oldReturned + newReturned);
                                        oldValue.setUpdateAt(ZonedDateTime.now());
                                        oldValue.setUpdateBy(uniformOrderStockDTO.getCreateBy());
                                        oldValue.setIsPersisted();
                                        updateList.add(oldValue);
                                    }

                                }
                                var changeStatus = orderDetail.stream().allMatch(x -> x.getQuantity().equals(x.getQuantityChange()));
                                if (changeStatus) {
                                    existingUniformOrder.setStatus(UniformOrderStatus.STOCKED);
                                }
                                else
                                {
                                    existingUniformOrder.setStatus(UniformOrderStatus.PROCESSING);
                                }
                                existingUniformOrder.setUpdateBy(uniformOrderStockDTO.getCreateBy());
                                existingUniformOrder.setUpdateAt(ZonedDateTime.now());
                                return uniformOrderRepository.save(existingUniformOrder.setIsPersisted()).flatMap(order -> {
                                    return uniformFormDetailRepository.saveAll(updateList)
                                        .collectList()
                                        .then(
                                            uniformOrderStockRepository.save(uniformOrderStockDTO.toEntity())
                                                .flatMap(orderStock -> {
                                                    return uniformFormDetailRepository
                                                        .saveAll(uniformFormDetailMapper.toEntity(uniformOrderStockDTO.toDetailDto(orderStock.getId())))
                                                        .collectList()
                                                        .flatMap(orderStockDetail -> {
                                                            return uniformStockService
                                                                .calculateUniformStockCreateWithOrderStock(
                                                                    orderStock.getCompany(),
                                                                    orderStock.getId(),
                                                                    uniformOrderStockDTO.getWarehouseId()
                                                                ).thenReturn(uniformOrderStockMapper.toDto(orderStock)).flatMap(this::setUniformOrderStockCode);
                                                        });
                                                }));
                                });

                    });
            });

    }

    private Mono<UniformOrderStockDTO> setUniformOrderStockCode(UniformOrderStockDTO uniformOrderstockDTO) {
        return uniformOrderStockRepository.findByIdAndCompany(uniformOrderstockDTO.getId(), uniformOrderstockDTO.getCompany())
            .flatMap(uniformOrderStock -> {
                if (uniformOrderStock.getId() == null) {
                    return Mono.error(new BadRequestAlertException("Uniform order not found", ENTITY_NAME, "NOT_FOUND"));
                }
                return documentSequenceService.findOrInitSequence("ORDER_STOCK", uniformOrderStock.getCompany())
                    .switchIfEmpty(Mono.error(new BadRequestAlertException("Document sequence not found", ENTITY_NAME, "NOT_FOUND")))
                    .flatMap(sequence -> {
                        System.out.println("sequence: " + sequence);
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMyy");
                        LocalDate date = LocalDate.now(); // or any other LocalDate instance
                        String formattedDate = date.format(formatter);
                        var seqFormat = String.format("%d", sequence);
                        if (sequence < 10000) {
                            seqFormat = String.format("%04d", sequence);
                        }
                        var code = "NK" + formattedDate + "/" + seqFormat;
                        uniformOrderStock.setCode(code);
                        uniformOrderStock.setUpdateAt(uniformOrderstockDTO.getUpdateAt());
                        uniformOrderStock.setUpdateBy(uniformOrderstockDTO.getUpdateBy());
                        uniformOrderStock.setIsPersisted();
                        return uniformOrderStockRepository.save(uniformOrderStock).map(uniformOrderStockMapper::toDto);
                    });
            });
    }

    @Data
    public static class UniformOrderResponse {
        private Map<UUID, UomDTO> uomMap;
        private Map<UUID, WarehouseDTO> warehouseMap;
        private Map<UUID, String> supplierName;
    }
}
