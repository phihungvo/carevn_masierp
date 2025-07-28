package com.masi.logistics.service;

import com.masi.logistics.domain.DeliverySchedule;
import com.masi.logistics.domain.Warehouse;
import com.masi.logistics.domain.WarehouseType;
import com.masi.logistics.domain.criteria.WarehouseCriteria;
import com.masi.logistics.domain.enumeration.ItemCategoryCode;
import com.masi.logistics.domain.enumeration.WarehouseTypePage;
import com.masi.logistics.repository.DocumentCodeSequenceRepository;
import com.masi.logistics.repository.WarehouseRepository;
import com.masi.logistics.repository.WarehouseTypeRepository;
import com.masi.logistics.service.dto.WarehouseDTO;
import com.masi.logistics.service.mapper.WarehouseMapper;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.masi.logistics.service.mapper.WarehouseTypeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.masi.logistics.domain.Warehouse}.
 */
@Service
@Transactional
public class WarehouseService {

    private static final Logger log = LoggerFactory.getLogger(WarehouseService.class);

    private final WarehouseRepository warehouseRepository;
    private final DocumentCodeSequenceRepository documentCodeSequenceRepository;
    private final DocumentCodeSequenceService documentCodeSequenceService;
    private final WarehouseTypeRepository warehouseTypeRepository;
    private final WarehouseMapper warehouseMapper;
    private final WarehouseTypeMapper warehouseTypeMapper;

    private static final String ENTITY_NAME = "masiLogisticsWarehouse";


    public WarehouseService(WarehouseRepository warehouseRepository, DocumentCodeSequenceRepository documentCodeSequenceRepository, DocumentCodeSequenceService documentCodeSequenceService, WarehouseTypeRepository warehouseTypeRepository, WarehouseMapper warehouseMapper, WarehouseTypeMapper warehouseTypeMapper) {
        this.warehouseRepository = warehouseRepository;
        this.documentCodeSequenceRepository = documentCodeSequenceRepository;
        this.documentCodeSequenceService = documentCodeSequenceService;
        this.warehouseTypeRepository = warehouseTypeRepository;
        this.warehouseMapper = warehouseMapper;
        this.warehouseTypeMapper = warehouseTypeMapper;
        Mono.delay(Duration.ofMinutes(3)).then(documentCodeSequenceService.makeSureDocumentCodeSequenceExist(Warehouse.ENTITY_NAME,"WH-%05d")).subscribe();// code ngu vcl

    }

    /**
     * Save a warehouse.
     *
     * @param warehouseDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<WarehouseDTO> save(WarehouseDTO warehouseDTO) {
        log.debug("Request to save Warehouse : {}", warehouseDTO);

        return documentCodeSequenceService.getByDocumentType(Warehouse.ENTITY_NAME)
                .flatMap(documentCodeSequence -> {
                    warehouseDTO.setCode(documentCodeSequence.getNextAndIncrement());
                    return documentCodeSequenceService.updateSequence(documentCodeSequence)
                            .then(Mono.defer(() -> warehouseRepository.save(warehouseDTO.toEntity())
                                    .map(warehouseMapper::toDto)
                                    .onErrorResume(e -> {
                                        log.error("Failed to save warehouse entity: {}", e.getMessage());
                                        return Mono.empty(); // Bỏ qua lỗi khi lưu kho
                                    })))
                            .switchIfEmpty(Mono.error(new RuntimeException("DocumentCodeSequence update failed")));
                })
                .switchIfEmpty(Mono.error(new RuntimeException("DocumentCodeSequence not found")))
                .onErrorResume(e -> {
                    log.error("Error during save Warehouse: {}", e.getMessage());
                    return Mono.empty(); // Không rollback mà bỏ qua toàn bộ
                });
    }


    /**
     * Update a warehouse.
     *
     * @param warehouseDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<WarehouseDTO> update(WarehouseDTO warehouseDTO) {
        log.debug("Request to update Warehouse : {}", warehouseDTO);
        return warehouseRepository.save(warehouseMapper.toEntity(warehouseDTO).setIsPersisted()).map(warehouseMapper::toDto);
    }

    /**
     * Partially update a warehouse.
     *
     * @param warehouseDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<WarehouseDTO> partialUpdate(WarehouseDTO warehouseDTO) {
        log.debug("Request to partially update Warehouse : {}", warehouseDTO);

        return warehouseRepository
            .findById(warehouseDTO.getId())
            .map(existingWarehouse -> {
                warehouseMapper.partialUpdate(existingWarehouse, warehouseDTO);
                existingWarehouse.setIsPersisted();
                return existingWarehouse;
            })
            .flatMap(warehouseRepository::save)
            .map(warehouseMapper::toDto);
    }

    /**
     * Find warehouses by Criteria.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<WarehouseDTO> findByCriteria(WarehouseCriteria criteria, Pageable pageable) {
        log.debug("Request to get all Warehouses by Criteria");
        if (criteria.getIsNotGetWareHouseUniform() == null) {
            criteria.setIsNotGetWareHouseUniform(true);
        }
        return warehouseRepository.findByCriteria(criteria, pageable)
                .map(warehouseMapper::toDto);
    }


    /**
     * Find the count of warehouses by criteria.
     * @param criteria filtering criteria
     * @return the count of warehouses
     */
    public Mono<Long> countByCriteria(WarehouseCriteria criteria) {
        log.debug("Request to get the count of all Warehouses by Criteria");
        return warehouseRepository.countByCriteria(criteria);
    }

    /**
     * Returns the number of warehouses available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return warehouseRepository.count();
    }

    /**
     * Get one warehouse by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Mono<WarehouseDTO> findOne(UUID id) {
        log.debug("Request to get Warehouse : {}", id);
        return warehouseRepository.findById(id)
                .map(warehouseMapper::toDto);
    }


    /**
     * Delete the warehouse by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(UUID id) {
        log.debug("Request to delete Warehouse : {}", id);
        return warehouseRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Flux<WarehouseDTO> findByAndItemCategory(String company, Pageable pageable, UUID orderId) {
        try {
            if (pageable.isUnpaged()) {
                pageable = Pageable.unpaged();
            }
            List<String> itemCategoryCodes = new ArrayList<>();
            if (orderId != null) {
                itemCategoryCodes.add(ItemCategoryCode.FINISHED_PRODUCT.name());
            } else {
                itemCategoryCodes.add(ItemCategoryCode.SEMI_FINISHED_PRODUCT.name());
            }
            int pageSize = pageable.isUnpaged() ? Integer.MAX_VALUE : pageable.getPageSize();
            int pageNumber = pageable.isUnpaged() ? 0 : pageable.getPageNumber();
            return warehouseRepository.findByItemCategory(company, itemCategoryCodes, pageSize, pageNumber)
                .map(warehouseMapper::toDto);
        } catch (Exception e) {
            log.error("Error while fetching warehouses by item category", e);
            throw e;
        }
    }

    @Transactional(readOnly = true)
    public Mono<Long> countByItemCategory(String company, UUID orderId) {
        List<String> itemCategoryCodes = new ArrayList<>();
        if (orderId != null) {
            itemCategoryCodes.add(ItemCategoryCode.FINISHED_PRODUCT.name());
        }
        else
        {
            itemCategoryCodes.add(ItemCategoryCode.SEMI_FINISHED_PRODUCT.name());
        }
        return warehouseRepository.countByItemCategory(company, itemCategoryCodes);
    }
}
