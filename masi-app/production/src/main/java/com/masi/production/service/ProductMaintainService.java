package com.masi.production.service;

import com.masi.production.domain.ManufactureOrder;
import com.masi.production.domain.ProductMaintain;
import com.masi.production.domain.enumeration.StatusEntity;
import com.masi.production.repository.ManufactureOrderRepository;
import com.masi.production.repository.ProMaintainProPackageRepository;
import com.masi.production.repository.ProductMaintainRepository;
import com.masi.production.repository.ProductPackageRepository;
import com.masi.production.service.dto.ProductMaintainDTO;
import com.masi.production.service.dto.ProductMaintainQuery;
import com.masi.production.service.mapper.ProMaintainProPackageMapper;
import com.masi.production.service.mapper.ProductMaintainMapper;

import java.sql.Array;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.masi.production.web.rest.errors.BadRequestAlertException;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.masi.production.domain.ProductMaintain}.
 */
@Service
@Transactional
public class ProductMaintainService {

    private final Logger log = LoggerFactory.getLogger(ProductMaintainService.class);

    private final ProductMaintainRepository productMaintainRepository;

    private final ProductMaintainMapper productMaintainMapper;
    private final ProMaintainProPackageRepository proMaintainProPackageRepository;
    private final ProductPackageRepository productPackageRepository;
    private final ProMaintainProPackageMapper productPackageMapper;
    private final ManufactureOrderRepository manufactureOrderRepository;

    public ProductMaintainService(ProductMaintainRepository productMaintainRepository, ProductMaintainMapper productMaintainMapper, ProMaintainProPackageRepository proMaintainProPackageRepository, ProductPackageRepository productPackageRepository, ProMaintainProPackageMapper productPackageMapper, ManufactureOrderRepository manufactureOrderRepository) {
        this.productMaintainRepository = productMaintainRepository;
        this.productMaintainMapper = productMaintainMapper;
        this.proMaintainProPackageRepository = proMaintainProPackageRepository;
        this.productPackageRepository = productPackageRepository;
        this.productPackageMapper = productPackageMapper;
        this.manufactureOrderRepository = manufactureOrderRepository;
    }

    /**
     * Save a productMaintain.
     *
     * @param productMaintainDTO the entity to save.
     * @return the persisted entity.
     */

    public Mono<ProductMaintainDTO> saveOrUpdate(ProductMaintainDTO productMaintainDTO) {
        log.debug("Request to save QualityCheckSample : {}", productMaintainDTO);
        if (productMaintainDTO.getId() == null)
            productMaintainDTO.setId(UUID.randomUUID());
        return productMaintainRepository.findByIdAndIsDeletedIsFalse(productMaintainDTO.getId())
            .switchIfEmpty(save(productMaintainDTO).flatMap(savedDTO -> productMaintainRepository.findByIdAndIsDeletedIsFalse(savedDTO.getId())))
            .flatMap(existingProductMaintain -> updateProductMaintain(existingProductMaintain, productMaintainDTO));
    }


    public Mono<ProductMaintainDTO> save(ProductMaintainDTO productMaintainDTO) {
        log.debug("Request to save ProductMaintain : {}", productMaintainDTO);
        productMaintainDTO.setIsDeleted(false);
        productMaintainDTO.setCreatedAt(ZonedDateTime.now());
        productMaintainDTO.setLastUpdatedAt(ZonedDateTime.now());

        return productMaintainRepository.countByProductBatchCode(productMaintainDTO.getProductBatchCode())
            .flatMap(count -> {
                if (count > 0) {
                    return Mono.error(new BadRequestAlertException("A new productMaintain cannot already have a code", "productMaintain", "CODE_EXISTS"));
                }
                return productMaintainRepository.save(productMaintainMapper.toEntity(productMaintainDTO))
                    .flatMap(savedMaintain -> productMaintainRepository.findByIdAndIsDeletedIsFalse(savedMaintain.getId()))
                    .map(productMaintainMapper::toDto)
                    .flatMap(maintain -> {
                        if (productMaintainDTO.getProductPackageId() != null) {
                            return productPackageRepository.findByIdAndIsDeletedIsFalse(productMaintainDTO.getProductPackageId())
                                .flatMap(productPackage -> {
                                    log.info("Product Package: {}", productPackage);
                                    return manufactureOrderRepository.findByIdManufacture(productPackage.getManufactureOrderId())
                                        .flatMap(manufactureOrder -> {
                                            if (manufactureOrder == null){
                                                return Mono.error(new BadRequestAlertException("Manufacture Order not found", "productMaintain", "ORDER_NOT_FOUND"));
                                            }
                                            if (!manufactureOrder.getStatus().equals(StatusEntity.PACKED_COMPLETED)) {
                                                return Mono.error(new BadRequestAlertException("Manufacture Order not packed", "productMaintain", "ORDER_NOT_PACKED"));
                                            }
                                            manufactureOrder.setProductMaintainId(maintain.getId());
                                            manufactureOrder.setProductPackageId(productMaintainDTO.getProductPackageId());
                                            manufactureOrder.setIsPersisted();
                                            return manufactureOrderRepository.save(manufactureOrder)
                                                .flatMap(order -> {
                                                    log.info("Manufacture Order: {}", order);
                                                    return Mono.just(maintain);
                                                });
                                        });
                                });
                        }
                        return Mono.error(new BadRequestAlertException("Product Package not found", "productMaintain", "PACKAGE_NOT_FOUND"));
                    });
            });
    }


    /**
     * Partially update a productMaintain.
     *
     * @param productMaintainDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<ProductMaintainDTO> partialUpdate(ProductMaintainDTO productMaintainDTO) {
    log.debug("Request to partially update ProductMaintain : {}", productMaintainDTO);

    return productMaintainRepository.findByIdAndIsDeletedIsFalse(productMaintainDTO.getId())
        .flatMap(existingProductMaintain -> {
            if (!existingProductMaintain.getProductPackageId().equals(productMaintainDTO.getProductPackageId())) {
                return updateManufactureOrder(existingProductMaintain, productMaintainDTO);
            }
            return updateProductMaintain(existingProductMaintain, productMaintainDTO);
        });
}

private Mono<ProductMaintainDTO> updateManufactureOrder(ProductMaintain existingProductMaintain, ProductMaintainDTO productMaintainDTO) {
    return productPackageRepository.findByIdAndIsDeletedIsFalse(existingProductMaintain.getProductPackageId())
        .switchIfEmpty(Mono.error(new BadRequestAlertException("Product Package not found", "productMaintain", "PACKAGE_NOT_FOUND")))
        .flatMap(oldProductPackage -> {
            return productPackageRepository.findByIdAndIsDeletedIsFalse(productMaintainDTO.getProductPackageId())
                .flatMap(newProductPackage -> {
                    var oldManufactureOrderId = oldProductPackage.getManufactureOrderId();
                    if (oldManufactureOrderId == null) {
                        return Mono.error(new BadRequestAlertException("Manufacture Order not found", "productMaintain", "ORDER_NOT_FOUND"));
                    }
                    var newManufactureOrderId = newProductPackage.getManufactureOrderId();
                    if (newManufactureOrderId == null) {
                        return Mono.error(new BadRequestAlertException("Manufacture Order not found", "productMaintain", "ORDER_NOT_FOUND"));
                    }
                return manufactureOrderRepository.findAllByIds(List.of(oldManufactureOrderId, newManufactureOrderId)).collectList()
                    .flatMap(manufactureOrders -> {
                        var mapManufactureOrder = manufactureOrders.stream().filter(Objects::nonNull).collect(Collectors.toMap(ManufactureOrder::getId, Function.identity()));
                        var listManufactureOrder = new ArrayList<ManufactureOrder>();
                        if (mapManufactureOrder.containsKey(newManufactureOrderId)){
                            var newManufactureOrder = mapManufactureOrder.get(newManufactureOrderId);
                            if (!newManufactureOrder.getStatus().equals(StatusEntity.PACKED_COMPLETED)) {
                                return Mono.error(new BadRequestAlertException("Manufacture Order not packed", "productMaintain", "ORDER_NOT_PACKED"));
                            }
                            newManufactureOrder.setProductMaintainId(existingProductMaintain.getId());
                            //newManufactureOrder.setProductPackageId(productMaintainDTO.getProductPackageId());
                            newManufactureOrder.setIsPersisted();
                            listManufactureOrder.add(newManufactureOrder);
                        }
                        if (mapManufactureOrder.containsKey(oldManufactureOrderId)){
                            var oldManufactureOrder = mapManufactureOrder.get(oldManufactureOrderId);
                            oldManufactureOrder.setProductMaintainId(null);
                            //oldManufactureOrder.setProductPackageId(null);
                            oldManufactureOrder.setIsPersisted();
                            listManufactureOrder.add(oldManufactureOrder);
                        }
                        return manufactureOrderRepository.saveAll(listManufactureOrder).collectList()
                            .flatMap(orders -> {
                                return updateProductMaintain(existingProductMaintain, productMaintainDTO);
                            });
                    });
            });
        });
}

private Mono<ProductMaintainDTO> updateProductMaintain(ProductMaintain existingProductMaintain, ProductMaintainDTO productMaintainDTO) {
    productMaintainMapper.partialUpdate(existingProductMaintain, productMaintainDTO);
    existingProductMaintain.setIsPersisted();
    return productMaintainRepository.save(existingProductMaintain)
        .map(productMaintainMapper::toDto);
}


    /**
     * Get all the productMaintains.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<ProductMaintainDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ProductMaintains");
        return productMaintainRepository.findAllBy(pageable).map(productMaintainMapper::toDto);
    }

    /**
     * Returns the number of productMaintains available.
     *
     * @return the number of entities in the database.
     */
    public Mono<Long> countAll() {
        return productMaintainRepository.count();
    }

    /**
     * Get one productMaintain by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<ProductMaintainDTO> findOne(UUID id) {
        log.debug("Request to get ProductMaintain : {}", id);

        return productMaintainRepository.findByIdAndIsDeletedIsFalse(id)
                .map(productMaintainMapper::toDto);
    }


    /**
     * Delete the productMaintain by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(UUID id) {
        log.debug("Request to delete ProductMaintain : {}", id);
        return productMaintainRepository.findByIdAndIsDeletedIsFalse(id)
                .switchIfEmpty(Mono.error(new EntityNotFoundException("ProductMaintain not found with id: " + id)))
                .flatMap(existingProductRouting -> productMaintainRepository.softDeleteById(id))
                .flatMap(dto ->
                        proMaintainProPackageRepository.deleteAllByProductMaintainId(id, true)
                                .then(Mono.just(dto))
                );
    }


    public Mono<Long> countAllByQuery(ProductMaintainQuery query) {
        log.debug("Request to count all ProductMaintain by query: {}", query);
        return productMaintainRepository.countByFilter(query);
    }


    public Flux<ProductMaintainDTO> findAllByQuery(ProductMaintainQuery query, Pageable pageable) {
        log.debug("Request to get all ProductMaintain by query: {}", query);
        return productMaintainRepository.findAllByFilter(query, pageable).map(productMaintainMapper::toDto);
    }

}
