package com.masi.production.service;

import com.carevn.masi.dto.ApiResponse;
import com.carevn.masi.utils.SecurityUtils;
import com.masi.production.domain.*;
import com.masi.production.domain.enumeration.ChecklistType;
import com.masi.production.domain.enumeration.MoStatus;
import com.masi.production.domain.enumeration.WoStatus;
import com.masi.production.repository.*;
import com.masi.production.service.dto.*;
import com.masi.production.service.dto.WorkOrderRO;
import com.masi.production.service.mapper.WorkOrderMapper;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import com.masi.production.web.rest.errors.BadRequestAlertException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuples;

/**
 * Service Implementation for managing {@link com.masi.production.domain.WorkOrder}.
 */
@Service
@Transactional
public class WorkOrderService {

    private final Logger log = LoggerFactory.getLogger(WorkOrderService.class);

    private final WorkOrderRepository workOrderRepository;
    private final WorkItemService workItemService;
    private final WorkOrderMapper workOrderMapper;
    private final AdditiveMaterialChecklistRepository additiveMaterialChecklistRepository;
    private final MachineOperationChecklistRepository machineOperationChecklistRepository;
    private final MetalDetectionChecklistRepository metalDetectionChecklistRepository;
    private final MixingReportChecklistRepository mixingReportChecklistRepository;
    private final ReceiveMaterialChecklistRepository receiveMaterialChecklistRepository;
    private final SteamingProcessChecklistRepository steamingProcessChecklistRepository;
    private final ManufactureOrderRepository manufactureOrderRepository;

    public WorkOrderService(WorkOrderRepository workOrderRepository, WorkOrderMapper workOrderMapper, WorkItemService workItemService, AdditiveMaterialChecklistRepository additiveMaterialChecklistRepository, MachineOperationChecklistRepository machineOperationChecklistRepository, MetalDetectionChecklistRepository metalDetectionChecklistRepository, MixingReportChecklistRepository mixingReportChecklistRepository, ReceiveMaterialChecklistRepository receiveMaterialChecklistRepository, SteamingProcessChecklistRepository steamingProcessChecklistRepository, ManufactureOrderRepository manufactureOrderRepository) {
        this.workOrderRepository = workOrderRepository;
        this.workOrderMapper = workOrderMapper;
        this.workItemService = workItemService;
        this.additiveMaterialChecklistRepository = additiveMaterialChecklistRepository;
        this.machineOperationChecklistRepository = machineOperationChecklistRepository;
        this.metalDetectionChecklistRepository = metalDetectionChecklistRepository;
        this.mixingReportChecklistRepository = mixingReportChecklistRepository;
        this.receiveMaterialChecklistRepository = receiveMaterialChecklistRepository;
        this.steamingProcessChecklistRepository = steamingProcessChecklistRepository;
        this.manufactureOrderRepository = manufactureOrderRepository;
    }

    /**
     * Save a workOrder.
     *
     * @param workOrderDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<WorkOrderDTO> save(WorkOrderDTO workOrderDTO) {
        log.debug("Request to save WorkOrder : {}", workOrderDTO);
        return workItemService.generateNew().flatMap(workItemDTO -> {
            //set workItemDto to workOrderDto
            workOrderDTO.setId(UUID.randomUUID());
            workOrderDTO.setCreatedAt(ZonedDateTime.now());
//            workOrderDTO.setLastUpdated(workItemDTO.getCreatedAt());
            workOrderDTO.setIsActive(true);
            workOrderDTO.setStatus(WoStatus.NEW);
            workOrderDTO.setWorkItem(workItemDTO);
            workOrderDTO.setWorkItemId(workItemDTO.getId());
            WorkOrder workOrder = workOrderDTO.toEntity();
            workOrder.setWorkItem(workItemDTO.toEntity());
            workOrder.getWorkItem().setLastUpdated(null);
            workOrder.setLastUpdated(null);
            return workOrderRepository.save(workOrder).map(workOrderMapper::toDto).flatMap(savedWorkOrderDTO -> {
                if (workOrderDTO.isWithNewWorkItemSameType()) {
                    BaseCheckListDto baseCheckListDto = workOrderDTO.getChecklist();
                    baseCheckListDto.setWorkItemId(workItemDTO.getId());
                    Mono<BaseCheckListDto> promise = switch (workOrderDTO.getChecklistType()) {
                        case RECEIVE_MATERIAL_CHECKLIST -> {
                            ReceiveMaterialChecklistDTO receiveMaterialChecklistDTO = (ReceiveMaterialChecklistDTO) baseCheckListDto;
                            yield receiveMaterialChecklistRepository.save(receiveMaterialChecklistDTO.toEntity()).map(ReceiveMaterialChecklist::toDto);
                        }
                        case STEAMING_PROCESS_CHECKLIST -> {
                            SteamingProcessChecklistDTO steamingProcessChecklistDTO = (SteamingProcessChecklistDTO) baseCheckListDto;
                            yield steamingProcessChecklistRepository.save(steamingProcessChecklistDTO.toEntity()).map(SteamingProcessChecklist::toDto);
                        }
                        case ADDITIVE_MATERIAL_CHECKLIST -> {
                            AdditiveMaterialChecklistDTO additiveMaterialChecklistDTO = (AdditiveMaterialChecklistDTO) baseCheckListDto;
                            yield additiveMaterialChecklistRepository.save(additiveMaterialChecklistDTO.toEntity()).map(AdditiveMaterialChecklist::toDto);
                        }
                        case MACHINE_OPERATION_CHECKLIST -> {
                            MachineOperationChecklistDTO machineOperationChecklistDTO = (MachineOperationChecklistDTO) baseCheckListDto;
                            yield machineOperationChecklistRepository.save(machineOperationChecklistDTO.toEntity()).map(MachineOperationChecklist::toDto);
                        }
                        case METAL_DETECTION_CHECKLIST -> {
                            MetalDetectionChecklistDTO metalDetectionChecklistDTO = (MetalDetectionChecklistDTO) baseCheckListDto;
                            yield metalDetectionChecklistRepository.save(metalDetectionChecklistDTO.toEntity()).map(MetalDetectionChecklist::toDto);
                        }
                        case MIXING_REPORT_CHECKLIST -> {
                            MixingReportChecklistDTO mixingReportChecklistDTO = (MixingReportChecklistDTO) baseCheckListDto;
                            yield mixingReportChecklistRepository.save(mixingReportChecklistDTO.toEntity()).map(MixingReportChecklist::toDto);
                        }
                    };
                    return promise.then(Mono.just(savedWorkOrderDTO));
                }
                return Mono.just(savedWorkOrderDTO);
            });
        });
    }

    public Mono<Void> saveAll(List<WorkOrderDTO> workOrderDTOs) {
        log.debug("Request to save WorkOrders : {}", workOrderDTOs);
        if (workOrderDTOs.isEmpty()) {
            return Mono.empty();
        }
        var moId = workOrderDTOs.get(0).getMoId();
        return workOrderRepository.deleteByManufactureOrder(moId).then(Flux.fromIterable(workOrderDTOs).flatMap(workOrderDTO -> {
            return this.save(workOrderDTO).then();
        }).collectList().then());
    }


    /**
     * Partially update a workOrder.
     *
     * @param workOrderDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<WorkOrderDTO> partialUpdate(WorkOrderDTO workOrderDTO) {
        log.debug("Request to partially update WorkOrder : {}", workOrderDTO);

        return workOrderRepository
            .findById(workOrderDTO.getId())
            .map(existingWorkOrder -> {
                existingWorkOrder.partialUpdate(workOrderDTO);
                existingWorkOrder.setIsPersisted();
                existingWorkOrder.setLastUpdated(ZonedDateTime.now());
                return existingWorkOrder;
            })
            .flatMap(workOrderRepository::save)
            .map(WorkOrder::toDto);
    }

    /**
     * Get all the workOrders.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<WorkOrderDTO> findAll(Pageable pageable) {
        log.debug("Request to get all WorkOrders");
        return workOrderRepository.findAllBy(pageable).map(WorkOrder::toDto);
    }

    @Transactional(readOnly = true)
    public Flux<WorkOrderDTO> findAllActive(Pageable pageable) {
        log.debug("Request to get all WorkOrders");
        return SecurityUtils.getUserJWTDetail().flatMap(user -> {
            String company = String.valueOf(user.getCompanyId());
            String department = String.valueOf(user.getGroupId());
            return Mono.just(Tuples.of(company, department));
        }).flatMapMany(tuple -> {
            String company = tuple.getT1();
            String department = tuple.getT2();
            return workOrderRepository.findAllByIsActive(pageable, true, company, department).map(WorkOrder::toDto);
        });
    }


    /**
     * Returns the number of workOrders available.
     *
     * @return the number of entities in the database.
     */
    public Mono<Long> countAllActive() {
        return workOrderRepository.countIsActive(true);
    }

    /**
     * Get one workOrder by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<WorkOrderDTO> findOne(WorkOrderRO ro, UUID id, Pageable pageable) {
        log.debug("Request to get WorkOrder : {}", id);
        return workOrderRepository.findByIdAndIsActive(id, true).map(WorkOrder::toDto)
            .switchIfEmpty(Mono.error(new BadRequestAlertException("WorkOrder not found", "workOrder", "idnotfound")))
            .flatMap(workOrderDTO -> {
                if (Objects.nonNull(workOrderDTO.getWorkItem())) {
                    List<Mono<List<BaseCheckListDto>>> promises = new ArrayList<>();
                    if (StringUtils.isBlank(ro.getName()) || ChecklistType.RECEIVE_MATERIAL_CHECKLIST.isLike(ro.getName())) {
                        Mono<List<BaseCheckListDto>> promise = receiveMaterialChecklistRepository.findAllByWorkItemIdAndIsActiveIsTrue(workOrderDTO.getWorkItem().getId(), ro.getStartDate(), ro.getEndDate())
                            .map(e -> {
                                return (BaseCheckListDto) e.toDto();
                            }).collectList();
                        promises.add(promise);
                    }
                    if (StringUtils.isBlank(ro.getName()) || ChecklistType.MACHINE_OPERATION_CHECKLIST.isLike(ro.getName())) {
                        Mono<List<BaseCheckListDto>> promise = machineOperationChecklistRepository.findAllByWorkItemIdAndIsActiveIsTrue(workOrderDTO.getWorkItem().getId(), ro.getStartDate(), ro.getEndDate())
                            .map(e -> {
                                return (BaseCheckListDto) e.toDto();
                            }).collectList();
                        promises.add(promise);
                    }

                    if (StringUtils.isBlank(ro.getName()) || ChecklistType.METAL_DETECTION_CHECKLIST.isLike(ro.getName())) {
                        Mono<List<BaseCheckListDto>> promise = metalDetectionChecklistRepository.findAllByWorkItemIdAndIsActiveIsTrue(workOrderDTO.getWorkItem().getId(), ro.getStartDate(), ro.getEndDate())
                            .map(e -> {
                                return (BaseCheckListDto) e.toDto();
                            }).collectList();
                        promises.add(promise);
                    }

                    if (StringUtils.isBlank(ro.getName()) || ChecklistType.MIXING_REPORT_CHECKLIST.isLike(ro.getName())) {
                        Mono<List<BaseCheckListDto>> promise = mixingReportChecklistRepository.findAllByWorkItemIdAndIsActiveIsTrue(workOrderDTO.getWorkItem().getId(), ro.getStartDate(), ro.getEndDate())
                            .map(e -> {
                                return (BaseCheckListDto) e.toDto();
                            }).collectList();
                        promises.add(promise);
                    }

                    if (StringUtils.isBlank(ro.getName()) || ChecklistType.ADDITIVE_MATERIAL_CHECKLIST.isLike(ro.getName())) {
                        Mono<List<BaseCheckListDto>> promise = additiveMaterialChecklistRepository.findAllByWorkItemIdAndIsActiveIsTrue(workOrderDTO.getWorkItem().getId(), ro.getStartDate(), ro.getEndDate())
                            .map(e -> {
                                return (BaseCheckListDto) e.toDto();
                            }).collectList();
                        promises.add(promise);
                    }
                    if (StringUtils.isBlank(ro.getName()) || ChecklistType.STEAMING_PROCESS_CHECKLIST.isLike(ro.getName())) {
                        Mono<List<BaseCheckListDto>> promise = steamingProcessChecklistRepository.findAllByWorkItemIdAndIsActiveIsTrue(workOrderDTO.getWorkItem().getId(), ro.getStartDate(), ro.getEndDate())
                            .map(e -> {
                                return (BaseCheckListDto) e.toDto();
                            }).collectList();
                        promises.add(promise);
                    }
                    log.info("Promises: {}", promises.size());
                    return Flux.concat(promises).collectList().flatMap(listCheckLists -> {
                        List<BaseCheckListDto> checkLists = listCheckLists.stream().flatMap(Collection::stream).toList();
                        checkLists = new ArrayList<>(checkLists);
                        workOrderDTO.getWorkItem().setCheckListsCount(checkLists.size());
                        checkLists.sort(Comparator.comparing(BaseCheckListDto::getCreatedAt).reversed());
                        checkLists = checkLists.stream().skip(pageable.getOffset()).limit(pageable.getPageSize()).toList();
                        workOrderDTO.getWorkItem().setCheckLists(checkLists);
                        return Mono.just(workOrderDTO);
                    }).doOnError(e -> {
                        log.error("Error: {}", e);
                    });
                }
                return Mono.just(workOrderDTO);

            });
    }

    /**
     * Delete the workOrder by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(UUID id) {
        log.debug("Request to delete WorkOrder : {}", id);
        return workOrderRepository.findById(id)
            .map(workOrder -> {
                workOrder.setIsActive(false);
                workOrder.setIsPersisted();
                workOrder.setLastUpdated(ZonedDateTime.now());
                return workOrder;
            })
            .flatMap(workOrderRepository::save)
            .then();
    }

    public Mono<Void> startWorkOrder(UUID id) {
        log.debug("Request to start WorkOrder : {}", id);
        return workOrderRepository.findById(id)
            .flatMap(workOrder -> {
                workOrder.setStatus(WoStatus.RUNNING);
                workOrder.setIsPersisted();
                workOrder.setLastUpdated(ZonedDateTime.now());
                return manufactureOrderRepository.updateStatusMo(workOrder.getManufactureOrderId(), MoStatus.PENDING).then(Mono.just(workOrder));
            })
            .flatMap(workOrderRepository::save)
            .then();
    }

    public Mono<Void> stopWorkOrder(UUID id) {
        log.debug("Request to stop WorkOrder : {}", id);
        return workOrderRepository.findById(id)
            .flatMap(workOrder -> {
                workOrder.setStatus(WoStatus.STOP);
                workOrder.setIsPersisted();
                workOrder.setLastUpdated(ZonedDateTime.now());
                return manufactureOrderRepository.updateStatusMo(workOrder.getManufactureOrderId(), MoStatus.PAUSED).then(Mono.just(workOrder));
            })
            .flatMap(workOrderRepository::save)
            .then();
    }

    public Mono<Void> completeWorkOrder(UUID id) {
        log.debug("Request to complete WorkOrder : {}", id);

        return workOrderRepository.findById(id)
            .flatMap(workOrder -> {
                workOrder.setStatus(WoStatus.COMPLETED);
                workOrder.setIsPersisted();
                workOrder.setLastUpdated(ZonedDateTime.now());
                return workOrderRepository.save(workOrder)
                    .then(checkCompleteWorkOrder(workOrder.getManufactureOrderId())
                        .flatMap(check -> {
                            if (check) {
                                return Mono.empty(); // Nếu không có WorkOrder hoàn thành, trả về Mono rỗng
                            }
                            return manufactureOrderRepository.updateStatusMo(workOrder.getManufactureOrderId(), MoStatus.COMPLETE_PRODUCTION);

                        })
                    );
            })
            .then();
    }


    public Mono<Boolean> checkCompleteWorkOrder(UUID moId) {
        log.debug("Request to check completed WorkOrder : {}", moId);
        return workOrderRepository
            .findByManufactureOrderAndStatus(moId, WoStatus.COMPLETED.toString())
            .hasElements()
            .defaultIfEmpty(false);
    }


}
