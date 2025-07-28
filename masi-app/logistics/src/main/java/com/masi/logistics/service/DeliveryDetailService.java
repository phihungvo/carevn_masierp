package com.masi.logistics.service;

import com.carevn.masi.utils.SecurityUtils;
import com.masi.logistics.domain.DeliveryDetail;
import com.masi.logistics.domain.criteria.DeliveryDetailCriteria;
import com.masi.logistics.domain.criteria.SupplierContractDetailCriteria;
import com.masi.logistics.repository.DeliveryDetailRepository;
import com.masi.logistics.repository.SupplierContractDetailRepository;
import com.masi.logistics.repository.SupplierContractRepository;
import com.masi.logistics.service.dto.DeliveryDetailCalendarDTO;
import com.masi.logistics.service.dto.DeliveryDetailDTO;
import com.masi.logistics.service.mapper.DeliveryDetailMapper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

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

import static com.masi.logistics.domain.enumeration.ContractStatus.DeliveryStatus.DELIVERED;
import static com.masi.logistics.domain.enumeration.ContractStatus.DeliveryStatus.DELIVERING;

/**
 * Service Implementation for managing {@link com.masi.logistics.domain.DeliveryDetail}.
 */
@Service
@Transactional
public class DeliveryDetailService {

    private static final Logger LOG = LoggerFactory.getLogger(DeliveryDetailService.class);

    private final DeliveryDetailRepository deliveryDetailRepository;

    private final DeliveryDetailMapper deliveryDetailMapper;
    private final SupplierContractDetailRepository supplierContractDetailRepository;
    private final SupplierContractRepository supplierContractRepository;

    public DeliveryDetailService(DeliveryDetailRepository deliveryDetailRepository, DeliveryDetailMapper deliveryDetailMapper, SupplierContractDetailRepository supplierContractDetailRepository, SupplierContractRepository supplierContractRepository) {
        this.deliveryDetailRepository = deliveryDetailRepository;
        this.deliveryDetailMapper = deliveryDetailMapper;
        this.supplierContractDetailRepository = supplierContractDetailRepository;
        this.supplierContractRepository = supplierContractRepository;
    }

    /**
     * Save a deliveryDetail.
     *
     * @param deliveryDetailDTO the entity to save.
     * @return the persisted entity.
     */
    public Flux<DeliveryDetailDTO> saveAll(List<DeliveryDetailDTO> deliveryDetailDTO) {
        LOG.debug("Request to saveAll DeliveryDetail : {}", deliveryDetailDTO);
        return deliveryDetailRepository.saveAll(deliveryDetailMapper.toEntity(deliveryDetailDTO)).map(deliveryDetailMapper::toDto);
    }

    /**
     * Save a deliveryDetail.
     *
     * @param deliveryDetailDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<DeliveryDetailDTO> save(DeliveryDetailDTO deliveryDetailDTO) {
        LOG.debug("Request to save DeliveryDetail : {}", deliveryDetailDTO);
        return deliveryDetailRepository
            .save(deliveryDetailMapper.toEntity(deliveryDetailDTO))
            .map(deliveryDetailMapper::toDto);
    }

    /**
     * Update a deliveryDetail.
     *
     * @param deliveryDetailDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<DeliveryDetailDTO> update(DeliveryDetailDTO deliveryDetailDTO) {
        LOG.debug("Request to update DeliveryDetail : {}", deliveryDetailDTO);
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
        LOG.debug("Request to partially update DeliveryDetail : {}", deliveryDetailDTO);

        return deliveryDetailRepository
            .findById(deliveryDetailDTO.getId())
            .map(existingDeliveryDetail -> {
                deliveryDetailMapper.partialUpdate(existingDeliveryDetail, deliveryDetailDTO);
                existingDeliveryDetail.setIsPersisted();
                return existingDeliveryDetail;
            })
            .flatMap(deliveryDetailRepository::save)
            .map(deliveryDetailMapper::toDto).flatMap(dd -> {
                DeliveryDetailCriteria criteria = getDeliveryDetailCriteria(dd);
                return deliveryDetailRepository.findByCriteria(criteria, null)
                    .collectList()
                    .flatMap(deliveryDetails -> {
                        Map<UUID, Integer> groupedData = deliveryDetails.stream()
                            .collect(Collectors.groupingBy(
                                DeliveryDetail::getContractMaterialId, // Key: item
                                Collectors.summingInt(x -> x.getActualQuantity() == null ? 0 : x.getActualQuantity().intValue()) // Value: tổng quantity
                            ));
                        var contractDetailCriteria = getSupplierContractDetailCriteria(dd);
                        return supplierContractDetailRepository.findByCriteria(contractDetailCriteria, null)
                            .collectList()
                            .flatMap(contractDetails -> {
                                var isDeliveryAll = contractDetails.stream().allMatch(contractDetail -> {
                                    var totalQuantity = groupedData.get(contractDetail.getSupplyItemId());
                                    return totalQuantity != null && totalQuantity >= contractDetail.getQuantity().intValue();
                                });
                                return supplierContractRepository.findById(dd.getSupplierContractId())
                                    .flatMap(supplierContract -> {
                                        if (isDeliveryAll) {
                                            supplierContract.setDeliveryStatus(DELIVERED);
                                        } else {
                                            supplierContract.setDeliveryStatus(DELIVERING);
                                        }
                                        supplierContract.setIsPersisted();
                                        return supplierContractRepository.save(supplierContract).flatMap(s -> {
                                            return Mono.just(dd);
                                        });
                                    });
                            });
                    });
            });
    }

    private DeliveryDetailCriteria getDeliveryDetailCriteria(DeliveryDetailDTO dd) {
        UUIDFilter filter = new UUIDFilter();
        filter.setEquals(dd.getSupplierContractId());
        DeliveryDetailCriteria criteria = new DeliveryDetailCriteria();
        criteria.setSupplierContractId(filter);
        ZonedDateTimeFilter deletedAt = new ZonedDateTimeFilter();
        deletedAt.setSpecified(false);
        criteria.setDeletedAt(deletedAt);
        StringFilter deletedBy = new StringFilter();
        deletedBy.setSpecified(false);
        criteria.setDeletedBy(deletedBy);
        return criteria;
    }

    private SupplierContractDetailCriteria getSupplierContractDetailCriteria(DeliveryDetailDTO dd) {
        var contractDetailCriteria = new SupplierContractDetailCriteria();
        UUIDFilter contractIdFilter = new UUIDFilter();
        contractIdFilter.setEquals(dd.getSupplierContractId());
        contractDetailCriteria.setSupplierContractId(contractIdFilter);
        BooleanFilter isDeletedFilter = new BooleanFilter();
        isDeletedFilter.setEquals(false);
        contractDetailCriteria.setIsDeleted(isDeletedFilter);
        return contractDetailCriteria;
    }

    /**
     * Find deliveryDetails by Criteria.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<DeliveryDetailDTO> findByCriteria(DeliveryDetailCriteria criteria, Pageable pageable) {
        LOG.debug("Request to get all DeliveryDetails by Criteria");
        return SecurityUtils.getUserJWTDetail().flatMapMany(login -> {
            StringFilter companyId = new StringFilter();
            companyId.setEquals(login.getCompanyId());
            criteria.setCompany(companyId);
            return deliveryDetailRepository.findByCriteria(criteria, pageable).map(deliveryDetailMapper::toDto);
        });
    }

    public Flux<DeliveryDetailCalendarDTO> getDeliveryDetailTable(LocalDate startDate, LocalDate endDate) {
        if (startDate == null) {
            startDate = LocalDate.now().withDayOfMonth(1);
        }
        if (endDate == null) {
            endDate = YearMonth.now().atEndOfMonth();
        }
        LocalDate finalStartDate = startDate;
        LocalDate finalEndDate = endDate;
        return SecurityUtils.getUserJWTDetail().flatMapMany(login -> {
            return deliveryDetailRepository.getDeliveryDetailCalendar(finalStartDate, finalEndDate, login.getCompanyId());
        });
    }

    public Flux<ItemQuantity> getDeliveryDetailCalendar(
        LocalDate startDate,
        LocalDate endDate) {
        if (startDate == null) {
            startDate = LocalDate.now().withDayOfMonth(1);
        }
        if (endDate == null) {
            endDate = YearMonth.now().atEndOfMonth();
        }
        LocalDate finalStartDate = startDate;
        LocalDate finalEndDate = endDate;
       return SecurityUtils.getUserJWTDetail().flatMapMany(login -> {
           return deliveryDetailRepository.getDeliveryDetailCalendar(finalStartDate, finalEndDate, login.getCompanyId())
               .collectList()
               .flatMapMany(deliveryDetails -> {
                   var result = processDeliveryDetails(deliveryDetails, finalStartDate, finalEndDate);
                   return Flux.fromIterable(result);
               });
       });
    }

    public record ItemQuantity(LocalDate start, UUID itemId, String itemCode, String itemName, String title, BigDecimal quantity, LocalDate end) {}

    public static List<ItemQuantity> processDeliveryDetails(
        List<DeliveryDetailCalendarDTO> deliveryDetails,
        LocalDate startDate,
        LocalDate endDate) {

        // Tạo một Map với key là ngày và value là map {itemId -> tổng quantity}
        List<ItemQuantity> dailyDataList = new ArrayList<>();
        // Duyệt qua các ngày từ startDate đến endDate
        LocalDate currentDate = startDate;
        while (!currentDate.isAfter(endDate)) {
            LocalDate finalCurrentDate = currentDate;
            List<DeliveryDetailCalendarDTO> filteredDetails = deliveryDetails.stream()
                .filter(detail -> detail.getDeliveryDate().equals(finalCurrentDate))
                .toList();
            // current date to start of day
            var startOfDate = currentDate;
            // current date to end of day
            var endOfDate = currentDate;
            // Nhóm theo itemId và tính tổng quantity
            List<ItemQuantity> items = filteredDetails.stream()
                .filter(deliveryDetailCalendarDTO -> deliveryDetailCalendarDTO.getItemId() != null)
                .collect(Collectors.groupingBy(
                    DeliveryDetailCalendarDTO::getItemId,
                    Collectors.collectingAndThen(
                        Collectors.toList(),
                        list -> {
                            String itemCode = list.get(0).getItemCode();
                            String itemName = list.get(0).getItemName();
                            var totalQuantity = list.stream().map(DeliveryDetailCalendarDTO::getExpectedQuantity).reduce(BigDecimal.ZERO, BigDecimal::add);
                            String title = itemName + " - " + totalQuantity;
                            return new ItemQuantity(startOfDate, list.get(0).getItemId(), itemCode, itemName, title, totalQuantity, endOfDate);
                        }
                    )
                )).values().stream().toList();
            dailyDataList.addAll(items);


            // Tăng ngày hiện tại
            currentDate = currentDate.plusDays(1);
        }

        return dailyDataList;
    }


    /**
     * Find the count of deliveryDetails by criteria.
     * @param criteria filtering criteria
     * @return the count of deliveryDetails
     */
    public Mono<Long> countByCriteria(DeliveryDetailCriteria criteria) {
        LOG.debug("Request to get the count of all DeliveryDetails by Criteria");
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
        LOG.debug("Request to get DeliveryDetail : {}", id);
        return SecurityUtils.getUserJWTDetail().flatMap(user -> deliveryDetailRepository.findByIdAndCompany(id, user.getCompanyId()))
            .map(deliveryDetailMapper::toDto);
    }

    /**
     * Delete the deliveryDetail by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(UUID id) {
        LOG.debug("Request to delete DeliveryDetail : {}", id);
        return deliveryDetailRepository.deleteById(id);
    }
}
