package com.masi.production.service;

import com.carevn.masi.utils.SecurityUtils;
import com.masi.production.domain.ManufactureOrder;
import com.masi.production.domain.ProductionStandard;
import com.masi.production.repository.ManufactureOrderRepository;
import com.masi.production.repository.ProductionStandardRepository;
import com.masi.production.service.dto.ManufactureOrderDTO;
import com.masi.production.service.dto.ProductionStandardDTO;
import com.masi.production.service.dto.ProductionStandardRO;
import com.masi.production.service.mapper.ManufactureOrderMapper;
import com.masi.production.service.mapper.ProductionStandardMapper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import com.masi.production.service.web.LogisticClient;
import com.masi.production.web.rest.errors.BadRequestAlertException;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.masi.production.domain.ProductionStandard}.
 */
@Service
@Transactional
public class ProductionStandardService {

    private final Logger log = LoggerFactory.getLogger(ProductionStandardService.class);

    private final ProductionStandardRepository productionStandardRepository;

    private final ProductionStandardMapper productionStandardMapper;
    private final ManufactureOrderRepository manufactureOrderRepository;
    private final ManufactureOrderMapper manufactureOrderMapper;
    private final LogisticClient logisticClient;

    public ProductionStandardService(
        ProductionStandardRepository productionStandardRepository,
        ProductionStandardMapper productionStandardMapper, ManufactureOrderRepository manufactureOrderRepository, ManufactureOrderMapper manufactureOrderMapper, LogisticClient logisticClient
    ) {
        this.productionStandardRepository = productionStandardRepository;
        this.productionStandardMapper = productionStandardMapper;
        this.manufactureOrderRepository = manufactureOrderRepository;
        this.manufactureOrderMapper = manufactureOrderMapper;
        this.logisticClient = logisticClient;
    }

    /**
     * Save a productionStandard.
     *
     * @param productionStandardDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ProductionStandardDTO> save(ProductionStandardDTO productionStandardDTO) {
        log.debug("Request to save ProductionStandard : {}", productionStandardDTO);
        return SecurityUtils.getUserJWTDetail().flatMap(login -> {
            productionStandardDTO.setCreatedAt(ZonedDateTime.now());
            productionStandardDTO.setLastUpdatedAt(ZonedDateTime.now());
            productionStandardDTO.setIsDeleted(false);
            productionStandardDTO.setUnit(UUID.randomUUID().toString());
            productionStandardDTO.setStartDate(LocalDate.now());
            productionStandardDTO.setWorkspace(login.getGroupId());
            productionStandardDTO.setStatus(String.valueOf(ProductionStandard.Status.NEW));
            LocalDate date = productionStandardDTO.getDueDate().withDayOfMonth(1);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd_MM_yyyy");
            String formattedDate = date.format(formatter);
            productionStandardDTO.setCode(formattedDate);
            var entity = productionStandardMapper.toEntity(productionStandardDTO);
            return productionStandardRepository.findFirstByCodeAndIsDeletedAndCompany(productionStandardDTO.getCode(), login.getCompanyId())
                .flatMap(count -> {
                    if (count > 0) {
                        return Mono.error(new BadRequestAlertException("A new production standard with the same code already exists", "productionStandard", "codeexists"));
                    }
                    return productionStandardRepository.save(entity).map(ProductionStandard::toDto);
                });

        });
    }

    /**
     * Update a productionStandard.
     *
     * @param productionStandardDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ProductionStandardDTO> update(ProductionStandardDTO productionStandardDTO) {
        log.debug("Request to update ProductionStandard : {}", productionStandardDTO);
        productionStandardDTO.setLastUpdatedAt(ZonedDateTime.now());
        return productionStandardRepository
            .save(productionStandardMapper.toEntity(productionStandardDTO).setIsPersisted())
            .map(ProductionStandard::toDto);
    }

    /**
     * Partially update a productionStandard.
     *
     * @param productionStandardDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<ProductionStandardDTO> partialUpdate(ProductionStandardDTO productionStandardDTO) {
        log.debug("Request to partially update ProductionStandard : {}", productionStandardDTO);

        return productionStandardRepository
            .findByIdAndIsDeleted(productionStandardDTO.getId(), false)
            .map(existingProductionStandard -> {
                existingProductionStandard.setLastUpdatedAt(ZonedDateTime.now());
                productionStandardMapper.partialUpdate(existingProductionStandard, productionStandardDTO);
                return existingProductionStandard;
            })
            .flatMap(productionStandardRepository::save)
            .map(ProductionStandard::toDto);
    }

    public Mono<Void> cancel(UUID id) {
        return productionStandardRepository.findByIdAndIsDeleted(id, false)
            .flatMap(productionStandard -> {
                productionStandard.setStatus(ProductionStandard.Status.CANCELED.name());
                productionStandard.setLastUpdatedAt(ZonedDateTime.now());
                return productionStandardRepository.save(productionStandard).then();
            });
    }

    /**
     * Get all the productionStandards.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<ProductionStandardDTO> findAll(Boolean isDeleted, Pageable pageable, ProductionStandardRO ro) {
        if (ro == null) {
            return productionStandardRepository.findAllByIsDeleted(isDeleted, pageable).map(ProductionStandard::toDto);
        }
        return productionStandardRepository.findAllByFilter(ro, pageable).map(ProductionStandard::toDto);
    }

    public Flux<ProductionStandardDTO> findAllFull(boolean isDeleted, Pageable pageable, ProductionStandardRO ro) {
        log.debug("Request to get all ProductionStandards");
        Sort defaultSort = Sort.by(Sort.Direction.DESC, "created_at");
        if (Objects.isNull(pageable)) {
            pageable = PageRequest.of(0, Integer.MAX_VALUE, defaultSort);
        }
        if (pageable.getSort().equals(Sort.unsorted())) {
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), defaultSort);
        }
        if (ro == null) {
            return productionStandardRepository.findAllByIsDeleted(isDeleted, pageable).map(ProductionStandard::toDto);
        }
        return productionStandardRepository.findAllByFilter(ro, pageable)
            .map(ProductionStandard::toDto)
            .collectList()
            .flatMapMany(dtoList -> {
                Collection<UUID> listId = dtoList.stream().map(ProductionStandardDTO::getId).toList();
                Flux<ManufactureOrder> manufactureOrderFlux;
                if(CollectionUtils.isNotEmpty(listId)){
                  manufactureOrderFlux = manufactureOrderRepository.findAllByProductionStandardIds(listId);
                }else {
                    List<ManufactureOrder> manufactureOrderDTOS = new ArrayList<>();
                    manufactureOrderFlux = Flux.fromIterable(manufactureOrderDTOS);
                }
                return manufactureOrderFlux
                    .map(ManufactureOrder::toDto)
                    .collectList()
                    .flatMapMany(manufactureOrders -> {
                        Map<UUID, List<ManufactureOrderDTO>> manufactureOrderMap = manufactureOrders.stream()
                            .collect(Collectors.groupingBy(ManufactureOrderDTO::getProductionStandardId));
                        dtoList.forEach(dto -> {
                            List<ManufactureOrderDTO> orders = manufactureOrderMap.get(dto.getId());
                            dto.setManufactureOrderDTOS(orders != null ? orders : new ArrayList<>());
                        });

                        return Flux.fromIterable(dtoList);
                    });
            });
    }

    @Transactional(readOnly = true)
    public Mono<Long> countAllByIsDeleted(Boolean isDeleted, ProductionStandardRO ro) {
        if (ro == null) {
            return productionStandardRepository.countAllByIsDeleted(isDeleted);
        }
        return productionStandardRepository.countAllByFilter(ro);
    }

    /**
     * Returns the number of productionStandards available.
     *
     * @return the number of entities in the database.
     */
    public Mono<Long> countAll() {
        return productionStandardRepository.count();
    }

    /**
     * Get one productionStandard by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<ProductionStandardDTO> findOne(UUID id) {
        log.debug("Request to get ProductionStandard : {}", id);
        return productionStandardRepository.findByIdAndIsDeleted(id, false)
            .flatMap(e -> {
                List<UUID> itemIds = new ArrayList<>();
                if (e.getMaterialId() != null) {
                    itemIds.add(e.getMaterialId());
                }

                if (itemIds.isEmpty()) {
                    return Mono.just(productionStandardMapper.toDto(e));
                }

                return logisticClient.getItemByListIds(itemIds).collectList()
                    .flatMap(item -> {
                        ProductionStandardDTO dto = productionStandardMapper.toDto(e);
                        if (!item.isEmpty()) {
                            dto.setMaterial(item.get(0));
                        }
                        return Mono.just(dto);
                    });
            });
    }


    /**
     * Delete the productionStandard by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(UUID id) {
        log.debug("Request to delete ProductionStandard : {}", id);
        return productionStandardRepository.softDeleteById(id);
    }

    public Mono<BigDecimal> countTotalQuantityInMo() {
        return productionStandardRepository.countTotalQuantityInMo();
    }
}
