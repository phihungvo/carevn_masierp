package com.masi.sale.service;

import com.carevn.masi.utils.SecurityUtils;
import com.masi.sale.domain.ContractMaterial;
import com.masi.sale.domain.DeliveryDetail;
import com.masi.sale.domain.criteria.DeliveryDetailCriteria;
import com.masi.sale.domain.enumeration.OrderStatus;
import com.masi.sale.repository.ContractMaterialRepository;
import com.masi.sale.repository.DeliveryDetailRepository;
import com.masi.sale.repository.OrderRepository;
import com.masi.sale.service.dto.DeliveryDetailCalendarDTO;
import com.masi.sale.service.dto.DeliveryDetailDTO;
import com.masi.sale.service.dto.ItemDTO;
import com.masi.sale.service.mapper.DeliveryDetailMapper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

import com.masi.sale.service.web.client.LogisticClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.service.filter.StringFilter;
import tech.jhipster.service.filter.UUIDFilter;
import tech.jhipster.service.filter.ZonedDateTimeFilter;

/**
 * Service Implementation for managing {@link com.masi.sale.domain.DeliveryDetail}.
 */
@Service
@Transactional
public class DeliveryDetailService {

    private static final Logger log = LoggerFactory.getLogger(DeliveryDetailService.class);

    private final DeliveryDetailRepository deliveryDetailRepository;

    private final DeliveryDetailMapper deliveryDetailMapper;
    private final LogisticClient logisticClient;
    private final OrderRepository orderRepository;
    private final ContractMaterialRepository contractMaterialRepository;

    public DeliveryDetailService(DeliveryDetailRepository deliveryDetailRepository, DeliveryDetailMapper deliveryDetailMapper, LogisticClient logisticClient, OrderRepository orderRepository, ContractMaterialRepository contractMaterialRepository) {
        this.deliveryDetailRepository = deliveryDetailRepository;
        this.deliveryDetailMapper = deliveryDetailMapper;
        this.logisticClient = logisticClient;
        this.orderRepository = orderRepository;
        this.contractMaterialRepository = contractMaterialRepository;
    }

    /**
     * Save a deliveryDetail.
     *
     * @param deliveryDetailDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<DeliveryDetailDTO> save(DeliveryDetailDTO deliveryDetailDTO) {
        log.debug("Request to save DeliveryDetail : {}", deliveryDetailDTO);
        return deliveryDetailRepository.save(deliveryDetailMapper.toEntity(deliveryDetailDTO)).map(deliveryDetailMapper::toDto);
    }

    // save all
    public Flux<DeliveryDetailDTO> saveAll(List<DeliveryDetailDTO> deliveryDetailDTOs) {
        log.debug("Request to saveAll DeliveryDetail : {}", deliveryDetailDTOs);
        return deliveryDetailRepository.saveAll(deliveryDetailDTOs.stream().map(deliveryDetailMapper::toEntity).collect(Collectors.toList())).map(deliveryDetailMapper::toDto);
    }

    /**
     * Update a deliveryDetail.
     *
     * @param deliveryDetailDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<DeliveryDetailDTO> update(DeliveryDetailDTO deliveryDetailDTO) {
        log.debug("Request to update DeliveryDetail : {}", deliveryDetailDTO);
        return deliveryDetailRepository
            .save(deliveryDetailMapper.toEntity(deliveryDetailDTO).setIsPersisted())
            .map(deliveryDetailMapper::toDto);
    }

    /**
     * Partially update a deliveryDetail.
     *
     * @param deliveryDetailDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<DeliveryDetailDTO> partialUpdate(DeliveryDetailDTO deliveryDetailDTO) {
        log.debug("Request to partially update DeliveryDetail : {}", deliveryDetailDTO);

        return deliveryDetailRepository
            .findById(deliveryDetailDTO.getId())
            .map(existingDeliveryDetail -> {
                deliveryDetailMapper.partialUpdate(existingDeliveryDetail, deliveryDetailDTO);
                existingDeliveryDetail.setIsPersisted();
                return existingDeliveryDetail;
            })
            .flatMap(deliveryDetailRepository::save)
            .map(deliveryDetailMapper::toDto).flatMap(dd -> {
                UUIDFilter filter = new UUIDFilter();
                filter.setEquals(dd.getOrderId());
                DeliveryDetailCriteria criteria = new DeliveryDetailCriteria();
                criteria.setOrderId(filter);
                ZonedDateTimeFilter deletedAt = new ZonedDateTimeFilter();
                deletedAt.setSpecified(false);
                criteria.setDeletedAt(deletedAt);
                StringFilter deletedBy = new StringFilter();
                deletedBy.setSpecified(false);
                criteria.setDeletedBy(deletedBy);

                return deliveryDetailRepository.findByCriteria(criteria, null)
                    .collectList()
                    .flatMap(deliveryDetails -> {
                Map<UUID, Integer> groupedData = deliveryDetails.stream()
                    .collect(Collectors.groupingBy(
                        DeliveryDetail::getContractMaterialId, // Key: item
                        Collectors.summingInt(detail -> Objects.requireNonNullElse(detail.getActualQuantity(), 0)) // Value: tổng quantity
                    ));
                        return contractMaterialRepository.findAllByOrderId(dd.getOrderId())
                            .collectList()
                            .flatMap(contractMaterials -> {
                                var isDeliveryAll = contractMaterials.stream().allMatch(contractMaterial -> {
                                    var quantity = groupedData.getOrDefault(contractMaterial.getIdMaterial(), 0);
                                    return quantity >= contractMaterial.getQuantity();
                                });
                                return orderRepository.findById(dd.getOrderId())
                                    .map(order -> {
                                        if (isDeliveryAll){
                                            order.setStatus(OrderStatus.FINISHED);
                                        }
                                        else {
                                            order.setStatus(OrderStatus.IN_PROGRESS_DELIVERED);
                                        }
                                        order.setIsPersisted();
                                        return order;
                                    })
                                    .flatMap(orderRepository::save)
                                    .map(order -> dd);
                            });
                    });
            });
    }

    /**
     * Find deliveryDetails by Criteria.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<DeliveryDetailDTO> findByCriteria(DeliveryDetailCriteria criteria, Pageable pageable) {
        log.debug("Request to get all DeliveryDetails by Criteria");
        return SecurityUtils.getUserJWTDetail().flatMapMany(user -> {
            StringFilter deletedBy = new StringFilter();
            deletedBy.setSpecified(false);
            criteria.setDeletedBy(deletedBy);
            StringFilter companyId = new StringFilter();
            companyId.setEquals(user.getCompanyId());
            criteria.setCompany(companyId);
            return deliveryDetailRepository.findByCriteria(criteria, pageable).map(deliveryDetailMapper::toDto);
        });
    }

    /**
     * Find the count of deliveryDetails by criteria.
     * @param criteria filtering criteria
     * @return the count of deliveryDetails
     */
    public Mono<Long> countByCriteria(DeliveryDetailCriteria criteria) {
        log.debug("Request to get the count of all DeliveryDetails by Criteria");
        return deliveryDetailRepository.countByCriteria(criteria);
    }

    /**
     * Returns the number of deliveryDetails available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return deliveryDetailRepository.count();
    }

    /**
     * Get one deliveryDetail by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<DeliveryDetailDTO> findOne(UUID id) {
        log.debug("Request to get DeliveryDetail : {}", id);
        return deliveryDetailRepository.findById(id).map(deliveryDetailMapper::toDto);
    }

    /**
     * Delete the deliveryDetail by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(UUID id) {
        log.debug("Request to delete DeliveryDetail : {}", id);
        return deliveryDetailRepository.deleteById(id);
    }

    public Flux<DeliveryDetailCalendarDTO> getDeliveryDetailTable(ZonedDateTime startDate, ZonedDateTime endDate) {
        if (startDate == null) {
            startDate = ZonedDateTime.now().withDayOfMonth(1);
        }
        if (endDate == null) {
            endDate = ZonedDateTime.now().plusMonths(1).minusDays(1);
        }
        ZonedDateTime finalStartDate = startDate;
        ZonedDateTime finalEndDate = endDate;
        return SecurityUtils.getUserJWTDetail().flatMapMany(login -> {
            return deliveryDetailRepository.getDeliveryDetailCalendar(finalStartDate, finalEndDate, login.getCompanyId())
                .collectList()
                .flatMapMany(this::getItemDTO);
        });
    }

    private Flux<DeliveryDetailCalendarDTO> getItemDTO(List<DeliveryDetailCalendarDTO> deliveryDetail) {
        var listItemIds = deliveryDetail.stream().filter(Objects::nonNull).map(DeliveryDetailCalendarDTO::getLogisticItem).filter(Objects::nonNull).toList();
        return logisticClient.getItemByListIds(listItemIds)
            .collectList()
            .flatMapMany(items -> {
                var itemMap = items.stream().collect(Collectors.toMap(ItemDTO::getId, item -> item));
                deliveryDetail.forEach(detail -> {
                    var item = itemMap.getOrDefault(detail.getLogisticItem(), null);
                    if (item != null) {
                        detail.setItemCode(item.getCode());
                        detail.setItemName(item.getName());
                    }
                });
                return Flux.fromIterable(deliveryDetail).doOnError(e -> log.error("Error when get item by list ids", e));
            }).doOnError(e -> log.error("Error when get item by list ids", e));
    }

    public Flux<ItemQuantity> getDeliveryDetailCalendar(
        ZonedDateTime startDate,
        ZonedDateTime endDate) {
        if (startDate == null) {
            startDate = ZonedDateTime.now().withDayOfMonth(1);
        }
        if (endDate == null) {
            endDate = ZonedDateTime.now().plusMonths(1).minusDays(1);
        }
        ZonedDateTime finalStartDate = startDate;
        ZonedDateTime finalEndDate = endDate;
        return SecurityUtils.getUserJWTDetail().flatMapMany(login -> {
            return deliveryDetailRepository.getDeliveryDetailCalendar(finalStartDate, finalEndDate, login.getCompanyId())
                .collectList()
                .flatMapMany(deliveryDetails -> {
                    return getItemDTO(deliveryDetails)
                        .collectList()
                        .flatMapMany(deliveryDetailsWithItem -> {
                            return Flux.fromIterable(processDeliveryDetails(deliveryDetailsWithItem, finalStartDate, finalEndDate));
                        });
                }).doOnError(e -> log.error("Error when get delivery detail calendar", e));
        });
    }

    public record ItemQuantity(ZonedDateTime start, ZonedDateTime deliveryDate, UUID itemId, String itemCode, String itemName, String title, BigDecimal quantity, ZonedDateTime end) {}

    public static List<ItemQuantity> processDeliveryDetails(
        List<DeliveryDetailCalendarDTO> deliveryDetails,
        ZonedDateTime startDate,
        ZonedDateTime endDate) {

        // Tạo một Map với key là ngày và value là map {itemId -> tổng quantity}
        List<ItemQuantity> dailyDataList = new ArrayList<>();
        // Duyệt qua các ngày từ startDate đến endDate
        ZonedDateTime currentDate = startDate;
        while (!currentDate.isAfter(endDate)) {
            ZonedDateTime finalCurrentDate = currentDate;
            List<DeliveryDetailCalendarDTO> filteredDetails = deliveryDetails.stream()
                .filter(detail -> detail.getDeliveryDate().toLocalDate().equals(finalCurrentDate.toLocalDate()))
                .toList();
            // current date to start of day
            var startOfDate = currentDate;
            // current date to end of day
            var endOfDate = currentDate;
            // Nhóm theo itemId và tính tổng quantity
            List<ItemQuantity> items = filteredDetails.stream()
                .filter(Objects::nonNull)
                .filter(deliveryDetailCalendarDTO -> deliveryDetailCalendarDTO.getItemId() != null)
                .collect(Collectors.groupingBy(
                    DeliveryDetailCalendarDTO::getItemId,
                    Collectors.collectingAndThen(
                        Collectors.toList(),
                        list -> {
                            String itemCode = list.get(0).getItemCode();
                            var deliveryDate = list.get(0).getDeliveryDate();
                            String itemName = list.get(0).getItemName();
                            var totalQuantity = list.stream().map(DeliveryDetailCalendarDTO::getExpectedQuantity).reduce(BigDecimal.ZERO, BigDecimal::add);
                            String title = itemName + " - " + totalQuantity;
                            return new ItemQuantity(startOfDate, deliveryDate, list.get(0).getItemId(), itemCode, itemName, title, totalQuantity, endOfDate);
                        }
                    )
                )).values().stream().toList();
            dailyDataList.addAll(items);


            // Tăng ngày hiện tại
            currentDate = currentDate.plusDays(1);
        }

        return dailyDataList;
    }
}
