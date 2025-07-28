package com.masi.production.service;

import com.masi.production.domain.ManufactureOrder;
import com.masi.production.domain.ProductPackage;
import com.masi.production.domain.enumeration.StatusEntity;
import com.masi.production.repository.ManufactureOrderRepository;
import com.masi.production.repository.ProductPackageRepository;
import com.masi.production.repository.WorkOrderRepository;
import com.masi.production.service.dto.*;
import com.masi.production.service.mapper.ManufactureOrderMapper;
import com.masi.production.service.mapper.ProductPackageMapper;
import com.masi.production.service.web.EmployeeClient;
import com.masi.production.service.web.LogisticClient;
import com.masi.production.web.rest.errors.BadRequestAlertException;

import java.time.ZonedDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing
 * {@link com.masi.production.domain.ProductPackage}.
 */
@Service
@Transactional
public class ProductPackageService {

    private final Logger log = LoggerFactory.getLogger(ProductPackageService.class);

    private final ProductPackageRepository productPackageRepository;

    private final ProductPackageMapper productPackageMapper;
    private final WorkOrderRepository workOrderRepository;
    private final LogisticClient logisticClient;
    private final ManufactureOrderRepository manufactureOrderRepository;
    private final ManufactureOrderMapper manufactureOrderMapper;
    private final EmployeeClient employeeClient;

    public ProductPackageService(ProductPackageRepository productPackageRepository,
                                 ProductPackageMapper productPackageMapper, WorkOrderRepository workOrderRepository, LogisticClient logisticClient, ManufactureOrderRepository manufactureOrderRepository, ManufactureOrderMapper manufactureOrderMapper, EmployeeClient employeeClient) {
        this.productPackageRepository = productPackageRepository;
        this.productPackageMapper = productPackageMapper;
        this.workOrderRepository = workOrderRepository;
        this.logisticClient = logisticClient;
        this.manufactureOrderRepository = manufactureOrderRepository;
        this.manufactureOrderMapper = manufactureOrderMapper;
        this.employeeClient = employeeClient;
    }

    /**
     * Save a productPackage.
     *
     * @param productPackageDTO the entity to save.
     * @return the persisted entity.
     */

    public Mono<ProductPackageDTO> saveOrUpdate(ProductPackageDTO productPackageDTO) {
        log.debug("Request to save QualityCheckSample : {}", productPackageDTO);
        if (productPackageDTO.getId() == null)
            productPackageDTO.setId(UUID.randomUUID());
        return productPackageRepository
                .findById(productPackageDTO.getId())
                .flatMap(existingQualityCheckSample -> this.partialUpdate(productPackageDTO))
                .switchIfEmpty(Mono.defer(() -> this.save(productPackageDTO)));
    }

    public Mono<ProductPackageDTO> save(ProductPackageDTO productPackageDTO) {
        log.debug("Request to save ProductPackage : {}", productPackageDTO);
        return productPackageRepository.countAllByPackageCode(productPackageDTO.getPackageCode(),productPackageDTO.getId()).flatMap(count -> {
            if (count > 0) {
                return Mono.error(new BadRequestAlertException("A new productPackage cannot already have a packageCode", "productPackage", "packageCodeExists"));
            }
            productPackageDTO.setCreatedAt(ZonedDateTime.now());
            productPackageDTO.setLastUpdatedAt(ZonedDateTime.now());
            productPackageDTO.setIsDeleted(false);
            productPackageDTO.setIsSew(false);
            var entity = productPackageMapper.toEntity(productPackageDTO);
            return productPackageRepository
                .save(entity)
                .map(productPackageMapper::toDto)
                .flatMap(pp -> {
                    if (pp.getManufactureOrderId() != null) {
                        return manufactureOrderRepository.findById(pp.getManufactureOrderId())
                            .flatMap(mo -> {
                                if (!(mo.getStatus().equals(StatusEntity.PACKAGING) ||
                                    mo.getStatus().equals(StatusEntity.ADDITIVES) ||
                                    mo.getStatus().equals(StatusEntity.PRODUCTION)))
                                {
                                    return Mono.error(new BadRequestAlertException("Manufacture order is not in packaging status", "manufactureOrder", "notInPackagingStatus"));
                                }
                                mo.setProductPackageId(pp.getId());
                                mo.setIsPersisted();
                                return manufactureOrderRepository.save(mo).map(m -> pp);
                            });
                    }
                    return Mono.just(pp);
                });
        });
    }

    /**
     * Update a productPackage.
     *
     * @param productPackageDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ProductPackageDTO> update(ProductPackageDTO productPackageDTO) {
        log.debug("Request to update ProductPackage : {}", productPackageDTO);
        return productPackageRepository
                .save(productPackageMapper.toEntity(productPackageDTO).setIsPersisted())
                .map(productPackageMapper::toDto);
    }

    /**
     * Partially update a productPackage.
     *
     * @param productPackageDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<ProductPackageDTO> partialUpdate(ProductPackageDTO productPackageDTO) {
        log.debug("Request to partially update ProductPackage : {}", productPackageDTO);
        return productPackageRepository.countAllByPackageCode(productPackageDTO.getPackageCode(),productPackageDTO.getId()).flatMap(count -> {
            if (count > 0) {
                return Mono.error(new BadRequestAlertException("A new productPackage cannot already have a packageCode", "productPackage", "packageCodeExists"));
            }
            return productPackageRepository
                    .findByIdAndIsDeletedIsFalse(productPackageDTO.getId())
                    .flatMap(existingProductPackage -> {
                        if (existingProductPackage.getManufactureOrderId() != null
                                && !existingProductPackage.getManufactureOrderId().equals(productPackageDTO.getManufactureOrderId())) {
                            return manufactureOrderRepository.findById(existingProductPackage.getManufactureOrderId())
                                    .flatMap(mo -> {
                                        mo.setProductPackageId(null);
                                        mo.setIsPersisted();
                                        return manufactureOrderRepository.save(mo).map(m -> existingProductPackage)
                                                .flatMap(savedOldMo -> {
                                                    existingProductPackage.setManufactureOrderId(productPackageDTO.getManufactureOrderId());
                                                    return manufactureOrderRepository.findById(productPackageDTO.getManufactureOrderId())
                                                            .flatMap(newMo -> {
                                                                if (!(mo.getStatus().equals(StatusEntity.PACKAGING) ||
                                                                    mo.getStatus().equals(StatusEntity.ADDITIVES) ||
                                                                    mo.getStatus().equals(StatusEntity.PRODUCTION))) {
                                                                    return Mono.error(new BadRequestAlertException("Manufacture order is not in packaging status", "manufactureOrder", "notInPackagingStatus"));
                                                                }
                                                                newMo.setProductPackageId(existingProductPackage.getId());
                                                                newMo.setIsPersisted();
                                                                return manufactureOrderRepository.save(newMo).flatMap(savedNewMo -> {
                                                                    return productPackageRepository.save(existingProductPackage).map(productPackageMapper::toDto);
                                                                });
                                                            });
                                                });
                                    });
                        }
                        productPackageMapper.partialUpdate(existingProductPackage, productPackageDTO);
                        existingProductPackage.setLastUpdatedAt(ZonedDateTime.now());
                        existingProductPackage.setIsPersisted();
                        return productPackageRepository.save(existingProductPackage).map(productPackageMapper::toDto);
                    });
        });
    }


    public Mono<ProductPackageDTO> updateSew(UUID id) {
        log.debug("Request to partially update ProductPackage : {}", id);
        return productPackageRepository
                .findByIdAndIsDeletedIsFalse(id)
                .map(existingProductPackage -> {
                    existingProductPackage.setIsSew(true);
                    existingProductPackage.setLastUpdatedAt(ZonedDateTime.now());
                    existingProductPackage.setIsPersisted();
                    return existingProductPackage;
                })
                .flatMap(productPackageRepository::save)
                .map(productPackageMapper::toDto);
    }


    @Transactional(readOnly = true)
    public Flux<ProductPackageDTO> findByQuery(ProductPackageQuery query, Pageable pageable) {
        return productPackageRepository.findAllByQuery(query, pageable)
                .map(ProductPackage::toDto)
                .collectList()
                .flatMap(dtos -> {
                    List<UUID> materialIds = dtos.stream()
                            .map(e -> UUID.fromString(e.getUnit()))
                            .distinct()
                            .collect(Collectors.toList());
                    if (materialIds.isEmpty()) {
                        return Mono.just(dtos);
                    }
                    return logisticClient.getUomByListIds(materialIds)
                            .collectList()
                            .flatMap(items -> {
                                if (!items.isEmpty()) {
                                    Map<UUID, UomDTO> itemMap = items.stream()
                                            .collect(Collectors.toMap(UomDTO::getId, Function.identity()));

                                    dtos.forEach(dto -> {
                                        UomDTO uomDTO = itemMap.get(UUID.fromString(dto.getUnit()));
                                        dto.setUomDTO(uomDTO);
                                    });
                                }
                                return Mono.just(dtos);
                            })
                            .switchIfEmpty(Mono.just(dtos));
                })
                .flatMapMany(dtos -> {
                    List<UUID> manufactureOrderIds = dtos.stream()
                        .filter(Objects::nonNull)
                        .map(ProductPackageDTO::getManufactureOrderId)
                        .filter(Objects::nonNull)
                        .distinct()
                        .collect(Collectors.toList());
                    List<UUID> packageByIds = dtos.stream()
                        .filter(Objects::nonNull)
                        .map(ProductPackageDTO::getPackageBy)
                        .filter(Objects::nonNull)
                        .distinct()
                        .toList();
                    if (manufactureOrderIds.isEmpty()) {
                        return Flux.fromIterable(dtos);
                    }

                    return manufactureOrderRepository.findAllByIds(manufactureOrderIds)
                        .collectList()
                        .doOnNext(orders -> log.info("Retrieved orders: {}", orders))
                        .map(manufactureOrderMapper::toDto)
                        .doOnNext(dtos1 -> log.info("Mapped DTOs: {}", dtos1))
                        .defaultIfEmpty(new ArrayList<>())
                        .flatMapMany(orders -> {
                                Map<UUID, ManufactureOrderDTO> mos = orders.stream()
                                        .collect(Collectors.toMap(ManufactureOrderDTO::getId, Function.identity()));

                                dtos.forEach(dto -> {
                                    dto.setManufactureOrder(mos.get(dto.getManufactureOrderId()));
                                });
                                return employeeClient.getEmployeesByListIdsWithPost(packageByIds)
                                        .collectList()
                                    .defaultIfEmpty(new ArrayList<>())
                                        .map(employees -> {
                                            if (!employees.isEmpty()) {
                                                Map<UUID, com.carevn.masi.dto.EmployeeDTO> employeeMap = employees.stream()
                                                        .collect(Collectors.toMap(com.carevn.masi.dto.EmployeeDTO::getId, Function.identity()));
                                                dtos.forEach(dto -> {
                                                    if (dto.getPackageBy() != null) {
                                                        com.carevn.masi.dto.EmployeeDTO employee = employeeMap.get(dto.getPackageBy());
                                                        if (employee != null) {
                                                            dto.setPackageByEmployee(employee);
                                                        }
                                                    }
                                                });
                                            }
                                            return dtos;
                                        })
                                        .flatMapMany(Flux::fromIterable);
                            });
                });

    }


    @Transactional(readOnly = true)
    public Mono<Long> countByQuery(ProductPackageQuery query) {
        return productPackageRepository.countByQuery(query);
    }

    /**
     * Get all the productPackages.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<ProductPackageDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ProductPackages");
        return productPackageRepository.findAllBy(pageable)
                .map(productPackageMapper::toDto);
    }

    /**
     * Returns the number of productPackages available.
     *
     * @return the number of entities in the database.
     */
    public Mono<Long> countAll() {
        return productPackageRepository.count();
    }

    /**
     * Get one productPackage by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<ProductPackageDTO> findOne(UUID id) {
        log.debug("Request to get ProductPackage : {}", id);
        return productPackageRepository.findById(id)
                .map(ProductPackage::toDto)
                .flatMap(e -> {
                    List<UUID> materialIds = new ArrayList<>();
                    UUID materialId = UUID.fromString(e.getUnit());
                    materialIds.add(materialId);
                    return logisticClient.getUomByListIds(materialIds)
                            .collectList()
                            .flatMap(items -> {
                                if (!items.isEmpty()) {
                                    UomDTO uomDTO = items.get(0);
                                    e.setUomDTO(uomDTO);
                                }
                                return manufactureOrderRepository.findById(e.getManufactureOrderId())
                                    .flatMap(mo -> {
                                        e.setManufactureOrder(manufactureOrderMapper.toDto(mo));
                                        if (e.getPackageBy() != null)
                                        {
                                            return employeeClient.getEmployeesByListIdsWithPost(List.of(e.getPackageBy()))
                                                    .collectList()
                                                    .map(employees -> {
                                                        if (!employees.isEmpty()) {
                                                            var employee = employees.get(0);
                                                            if (employee != null) {
                                                                e.setPackageByEmployee(employee);
                                                            }
                                                        }
                                                        return e;
                                                    });
                                        }
                                        return Mono.just(e);
                                    });
                            });
                });

    }


    /**
     * Delete the productPackage by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(UUID id) {
        log.debug("Request to delete ProductPackage : {}", id);
        return productPackageRepository.findById(id)
                .flatMap(productPackage -> {
                    productPackage.setIsDeleted(true);
                    productPackage.setLastUpdatedAt(ZonedDateTime.now());
                    productPackage.setIsPersisted();
                    return productPackageRepository.save(productPackage);
                })
                .then();
    }
}
