package com.masi.sale.service;

import com.masi.sale.domain.ContractMaterialFull;
import com.masi.sale.domain.DeliveryDetail;
import com.masi.sale.domain.DeliverySchedule;
import com.masi.sale.domain.criteria.DeliveryScheduleCriteria;
import com.masi.sale.domain.enumeration.StatusEntity;
import com.masi.sale.repository.*;
import com.masi.sale.service.dto.*;
import com.masi.sale.service.dto.reponse.DeliveryScheduleResponse;
import com.masi.sale.service.dto.request.DeliveryScheduleRequest;
import com.masi.sale.service.mapper.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

import com.masi.sale.service.web.client.LogisticClient;
import org.eclipse.angus.mail.imap.protocol.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.masi.sale.domain.DeliverySchedule}.
 */
@Service
@Transactional
public class DeliveryScheduleService {

    private static final Logger log = LoggerFactory.getLogger(DeliveryScheduleService.class);

    private final DeliveryScheduleRepository deliveryScheduleRepository;

    private final DeliveryScheduleMapper deliveryScheduleMapper;
    private final OrderRepository orderRepository;
    private final ContractMaterialRepository contractMaterialRepository;
    private final ContractMaterialMapper contractMaterialMapper;
    private final DeliveryDetailRepository deliveryDetailRepository;
    private final ContractRepository contractRepository;
    private final ContractMapper contractMapper;
    private final LogisticClient logisticClient;
    private final DeliveryDetailMapper deliveryDetailMapper;
    private final OrderMapper orderMapper;

    public DeliveryScheduleService(DeliveryScheduleRepository deliveryScheduleRepository, DeliveryScheduleMapper deliveryScheduleMapper, OrderRepository orderRepository, ContractMaterialRepository contractMaterialRepository, ContractMaterialMapper contractMaterialMapper, DeliveryDetailRepository deliveryDetailRepository, ContractRepository contractRepository, ContractMapper contractMapper, LogisticClient logisticClient, DeliveryDetailMapper deliveryDetailMapper, OrderMapper orderMapper) {
        this.deliveryScheduleRepository = deliveryScheduleRepository;
        this.deliveryScheduleMapper = deliveryScheduleMapper;
        this.orderRepository = orderRepository;
        this.contractMaterialRepository = contractMaterialRepository;
        this.contractMaterialMapper = contractMaterialMapper;
        this.deliveryDetailRepository = deliveryDetailRepository;
        this.contractRepository = contractRepository;
        this.contractMapper = contractMapper;
        this.logisticClient = logisticClient;
        this.deliveryDetailMapper = deliveryDetailMapper;
        this.orderMapper = orderMapper;
    }

    /**
     * Save a deliverySchedule.
     *
     * @param deliveryScheduleDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<DeliveryScheduleDTO> save(DeliveryScheduleDTO deliveryScheduleDTO) {
        log.debug("Request to save DeliverySchedule : {}", deliveryScheduleDTO);
        return deliveryScheduleRepository
                .save(deliveryScheduleMapper.toEntity(deliveryScheduleDTO))
                .map(deliveryScheduleMapper::toDto)
                .flatMap(savedSchedule -> {
                    if (deliveryScheduleDTO.getDeliveryDetail() != null && !deliveryScheduleDTO.getDeliveryDetail().isEmpty()) {
                        return Flux.fromIterable(deliveryScheduleDTO.getDeliveryDetail())
                                .map(deliveryDetailMapper::toEntity)
                                .flatMap(deliveryDetail -> {
                                    deliveryDetail.setId(UUID.randomUUID());
                                    deliveryDetail.setDeliveryId(savedSchedule.getId());
                                    return deliveryDetailRepository.save(deliveryDetail);
                                })
                                .then(Mono.just(savedSchedule));
                    }
                    return Mono.just(savedSchedule);
                });
    }


    /**
     * Update a deliverySchedule.
     *
     * @param deliveryScheduleDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<DeliveryScheduleDTO> update(DeliveryScheduleDTO deliveryScheduleDTO) {
        log.debug("Request to update DeliverySchedule : {}", deliveryScheduleDTO);
        return deliveryScheduleRepository
            .save(deliveryScheduleMapper.toEntity(deliveryScheduleDTO).setIsPersisted())
            .map(deliveryScheduleMapper::toDto);
    }

    /**
     * Partially update a deliverySchedule.
     *
     * @param deliveryScheduleDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<DeliveryScheduleDTO> partialUpdate(DeliveryScheduleDTO deliveryScheduleDTO) {
        log.debug("Request to partially update DeliverySchedule : {}", deliveryScheduleDTO);

        return deliveryScheduleRepository
            .findById(deliveryScheduleDTO.getId())
            .map(existingDeliverySchedule -> {
                deliveryScheduleMapper.partialUpdate(existingDeliverySchedule, deliveryScheduleDTO);

                return existingDeliverySchedule;
            })
            .flatMap(deliveryScheduleRepository::save)
            .map(deliveryScheduleMapper::toDto);
    }

    /**
     * Find deliverySchedules by Criteria.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<DeliveryScheduleResponse> findByCriteriaList(DeliveryScheduleRequest criteria, Pageable pageable) {
        log.debug("Request to get all DeliverySchedules by Criteria");

        // find all order by date
        return orderRepository.findAllByDate(criteria.getDeliveryDate(), criteria.getExpectedReceiveDate())
                .map(orderMapper::toDto)
                .collectList()
                .flatMapMany(orderDTOList -> {
                    if (orderDTOList.isEmpty()) {
                        return Flux.empty();
                    }

                    List<UUID> orderIds = orderDTOList.stream()
                            .map(OrderDTO::getId)
                            .distinct()
                            .toList();

                    // find all order materials by order ids
                    Flux<ContractMaterialDTO> contractMaterials = contractMaterialRepository.findAllByIdContractIn(orderIds)
                            .map(ContractMaterialFull::toDtoMapOrderAndContract)
                            .collectList()
                            .flatMapMany(materials -> {
                                if (materials.isEmpty()) {
                                    return Flux.empty();
                                }

                                // Dummy Data Item
                                ItemDTO itemTest = new ItemDTO();
                                itemTest.setId(UUID.randomUUID());
                                itemTest.setName("Test");
                                itemTest.setCode("T");

                                // find all items by material ids
                                return logisticClient.getItemByListIds(materials.stream().map(ContractMaterialDTO::getItemId).toList())
                                        .collectList()
                                        .flatMapMany(items -> {
                                            materials.forEach(material -> {
                                                material.setItemDTO(items.stream()
                                                        .filter(item -> item.getId().equals(material.getItemId()))
                                                        .findFirst()
                                                        .orElse(itemTest));
                                            });
                                            return Flux.fromIterable(materials);
                                        });
                            });

                    // find all delivery schedules by contract ids
                    Flux<DeliveryDetailDTO> deliverySchedules = deliveryScheduleRepository.findAllByOrderIdIn(orderIds)
                            .map(deliveryScheduleMapper::toDto)
                            .collectList()
                            .flatMapMany(deliveryScheduleDTOs -> {
                                List<UUID> scheduleIds = deliveryScheduleDTOs.stream()
                                        .map(DeliveryScheduleDTO::getId)
                                        .distinct()
                                        .toList();

                                if (scheduleIds.isEmpty()) {
                                    return Flux.empty();
                                }
                                return deliveryDetailRepository.findAllByDeliveryIdIn(scheduleIds)
                                        .map(deliveryDetailMapper::toDto)
                                        .map(dto -> {
                                            Optional<DeliveryScheduleDTO> matchingSchedule = deliveryScheduleDTOs.stream()
                                                    .filter(ds -> ds.getId().equals(dto.getDeliveryId()))
                                                    .findFirst();
                                            matchingSchedule.ifPresent(ds -> dto.setDeliveryDate(ds.getDeliveryDate().atStartOfDay().atZone(ZoneId.of("UTC"))));
                                            matchingSchedule.ifPresent(ds -> dto.setContractId(ds.getContractId()));
                                            return dto;
                                        });
                            });


                    return contractMaterials.collectList()
                            .zipWith(deliverySchedules.collectList(), (materials, schedules) -> {
                                List<DeliveryScheduleResponse> responses = new ArrayList<>();

                                for (ContractMaterialDTO material : materials) {
                                    DeliveryScheduleResponse response = new DeliveryScheduleResponse();
                                    response.setContract(material.getContract());
                                    response.setItem(material.getItemDTO());
                                    response.setOrder(material.getOrder());
                                    response.setNeedQuantity(BigDecimal.valueOf(material.getQuantity()));

                                    List<DeliveryDetailDTO> relatedSchedules = schedules.stream()
                                            .filter(schedule ->
                                                    schedule.getContractId() != null &&
                                                    schedule.getContractId().equals(material.getIdContract()) &&
                                                            schedule.getContractMaterialId().equals(material.getItemId())
                                            )
                                            .toList();



                                    response.setDeliverySchedules(relatedSchedules);

                                    responses.add(response);
                                }
                                return responses;
                            })
                            .flatMapMany(Flux::fromIterable);
                })
                .flatMap(response -> {
                    BigDecimal receivedQuantity = response.getDeliverySchedules().stream()
                            .map(DeliveryDetailDTO::getPrice)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    BigDecimal remainingQuantity = response.getNeedQuantity()
                            .subtract(Optional.ofNullable(response.getReceivedQuantity()).orElse(BigDecimal.ZERO))
                            .subtract(receivedQuantity);

                    if (remainingQuantity.compareTo(BigDecimal.ZERO) < 0) {
                        remainingQuantity = BigDecimal.ZERO;
                    }

                    response.setReceivedQuantity(receivedQuantity); // đã nhận
                    response.setRemainingQuantity(remainingQuantity); // còn lại

                    response.setImportPlan(BigDecimal.ZERO); // kế hoạch nhập
                    response.setOweQuantity(BigDecimal.ZERO); // nợ

                    return Mono.just(response);
                });

    }


    @Transactional(readOnly = true)
    public Flux<DeliveryScheduleDTO> findByCriteriaCalendar(DeliveryScheduleRequest criteria, Pageable pageable) {
        log.debug("Request to get all DeliverySchedules by Criteria");
       return deliveryScheduleRepository.findAll()
                .map(deliveryScheduleMapper::toDto);
    }



    /**
     * Find the count of deliverySchedules by criteria.
     * @param criteria filtering criteria
     * @return the count of deliverySchedules
     */
    public Mono<Long> countByCriteria(DeliveryScheduleRequest criteria) {
        log.debug("Request to get the count of all DeliverySchedules by Criteria");
        return Mono.just(0L);
    }

    /**
     * Returns the number of deliverySchedules available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return deliveryScheduleRepository.count();
    }

    /**
     * Get one deliverySchedule by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<DeliveryScheduleDTO> findOne(UUID id) {
        log.debug("Request to get DeliverySchedule : {}", id);

        return deliveryScheduleRepository
                .findById(id)
                .map(deliveryScheduleMapper::toDto)
                .flatMap(deliveryScheduleDTO ->
                        deliveryDetailRepository.findAllByDeliveryIdIn(Collections.singletonList(deliveryScheduleDTO.getId()))
                                .map(deliveryDetailMapper::toDto)
                                .collectList()
                                .map(deliveryDetails -> {
                                    deliveryScheduleDTO.setDeliveryDetail(deliveryDetails);
                                    return deliveryScheduleDTO;
                                })
                );
    }

    /**
     * Delete the deliverySchedule by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(UUID id) {
        log.debug("Request to delete DeliverySchedule : {}", id);
        return deliveryScheduleRepository.deleteById(id);
    }

    Mono<Void> createDeliveryOrder(UUID orderId) {
        log.debug("Request to create DeliverySchedule for Order : {}", orderId);
        return orderRepository.findById(orderId)
                .flatMap(order -> {
                    DeliverySchedule deliverySchedule = new DeliverySchedule();
                    deliverySchedule.setId(UUID.randomUUID());
                    deliverySchedule.setOrderId(orderId);
                    deliverySchedule.setExpectedReceiveDate(order.getDeliveryTermTo());
                    deliverySchedule.setDeliveryLocation(order.getDeliveryLocation());

                    return deliveryScheduleRepository.save(deliverySchedule)
                            .thenMany(contractMaterialRepository.findAllByOrderId(orderId)
                                    .map(contractMaterialMapper::toDto)
                                    .flatMap(contractMaterialDTO -> {
                                        DeliveryDetail deliveryDetail = new DeliveryDetail();
                                        deliveryDetail.setId(UUID.randomUUID());
                                        deliveryDetail.setContractMaterialId(contractMaterialDTO.getItemId());
                                        deliveryDetail.setDeliveryId(deliverySchedule.getId());
//                                        deliveryDetail.setOrderId(orderId);
                                        deliveryDetail.setContractMaterialId(contractMaterialDTO.getId());
                                        return deliveryDetailRepository.save(deliveryDetail);
                                    }))
                            .then();
                });
    }

}
