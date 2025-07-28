package com.masi.logistics.service;

import com.masi.logistics.domain.Uom;
import com.masi.logistics.repository.SuppliersRepository;
import com.masi.logistics.repository.UomRepository;
import com.masi.logistics.repository.WarehouseRepository;
import com.masi.logistics.service.dto.*;
import com.masi.logistics.service.mapper.SuppliersMapper;
import com.masi.logistics.service.mapper.UomMapper;

import java.util.*;
import java.util.stream.Collectors;

import com.masi.logistics.service.mapper.WarehouseMapper;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.masi.logistics.domain.Uom}.
 */
@Service
@Transactional
public class UomService {

    private static final Logger log = LoggerFactory.getLogger(UomService.class);

    private final SuppliersRepository suppliersRepository;
    private final UomRepository uomRepository;
    private final WarehouseRepository warehouseRepository;

    // mapper
    private final UomMapper uomMapper;
    private final SuppliersMapper suppliersMapper;
    private final WarehouseMapper warehouseMapper;

    public UomService(SuppliersRepository suppliersRepository, UomRepository uomRepository, WarehouseRepository warehouseRepository, UomMapper uomMapper, SuppliersMapper suppliersMapper, WarehouseMapper warehouseMapper) {
        this.suppliersRepository = suppliersRepository;
        this.uomRepository = uomRepository;
        this.warehouseRepository = warehouseRepository;
        this.uomMapper = uomMapper;
        this.suppliersMapper = suppliersMapper;
        this.warehouseMapper = warehouseMapper;
    }

    /**
     * Save a uom.
     *
     * @param uomDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<UomDTO> save(UomDTO uomDTO) {
        log.debug("Request to save Uom : {}", uomDTO);
        return uomRepository.save(uomMapper.toEntity(uomDTO)).map(uomMapper::toDto);
    }

    /**
     * Update a uom.
     *
     * @param uomDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<UomDTO> update(UomDTO uomDTO) {
        log.debug("Request to update Uom : {}", uomDTO);
        return uomRepository.save(uomMapper.toEntity(uomDTO).setIsPersisted()).map(uomMapper::toDto);
    }

    /**
     * Partially update a uom.
     *
     * @param uomDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<UomDTO> partialUpdate(UomDTO uomDTO) {
        log.debug("Request to partially update Uom : {}", uomDTO);

        return uomRepository
            .findById(uomDTO.getId())
            .map(existingUom -> {
                uomMapper.partialUpdate(existingUom, uomDTO);
                existingUom.setIsPersisted();
                return existingUom;
            })
            .flatMap(uomRepository::save)
            .map(uomMapper::toDto);
    }

    /**
     * Get all the uoms.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<UomDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Uoms");
        return uomRepository.findAllBy(pageable).map(uomMapper::toDto);
    }

    /**
     * Returns the number of uoms available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return uomRepository.count();
    }

    /**
     * Get one uom by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<UomDTO> findOne(UUID id) {
        log.debug("Request to get Uom : {}", id);
        return uomRepository.findById(id).map(uomMapper::toDto);
    }

    /**
     * Delete the uom by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(UUID id) {
        log.debug("Request to delete Uom : {}", id);
        return uomRepository.deleteById(id);
    }

    public Flux<UomDTO> findAllByListId(List<UUID> ids) {
        return uomRepository.findAllByIdIn(ids).map(Uom::toDto);
    }

    public Flux<UomDTO> findAllByCompany(List<UUID> ids ,String company) {
        return uomRepository.findAllByIdInAndCompanyAndDeleteAtIsNullAndDeleteByIsNull(ids, company).map(uomMapper::toDto);
    }


    public Mono<UniformEntityResponse> mappingDataLogisticToOrderDTO(UniformOrderDTO uniformOrderDTO) {
        var listUomId = uniformOrderDTO.getUniformFormDetails().stream().map(UniformFormDetailDTO::getUomId).filter(Objects::nonNull).collect(Collectors.toSet());
        var lisInventoryIds = uniformOrderDTO.getUniformOrderStockDTOS().stream().map(UniformOrderStockDTO::getWarehouseId).filter(Objects::nonNull).collect(Collectors.toSet());
        try {
            return suppliersRepository.findById(uniformOrderDTO.getSupplierId(), uniformOrderDTO.getCompany())
                .flatMap(suppliers -> {
                    var res = new UniformEntityResponse();
                    res.setSupplierName(Map.of(uniformOrderDTO.getSupplierId(), suppliers.getName()));
                    return warehouseRepository.findAllById(lisInventoryIds).map(warehouseMapper::toDto).collectList().flatMap(wareHouses -> {
                        var warehouseMap = wareHouses.stream()
                            .collect(Collectors.toMap(WarehouseDTO::getId, warehouse -> warehouse));
                        res.setWarehouseMap(warehouseMap);
                        return uomRepository.findAllByIdInAndCompanyAndDeleteAtIsNullAndDeleteByIsNull(new ArrayList<>(listUomId), uniformOrderDTO.getCompany()).map(uomMapper::toDto).collectList().flatMap(uoms -> {
                            var uomMap = uoms.stream()
                                .collect(Collectors.toMap(UomDTO::getId, uom -> uom));
                            res.setUomMap(uomMap);
                            return Mono.just(res);
                        });
                    });
                });
        }
        catch (Exception e) {
            log.error("Error when mapping data logistic to order DTO: {}", e.getMessage());
            return Mono.error(e);
        }
    }

    public Mono<UniformEntityResponse> mappingDataLogisticToReleaseDTO(UniformReleaseDTO releaseDTO) {
        var listUomId = releaseDTO.getUniformFormDetails().stream().map(UniformFormDetailDTO::getUomId).filter(Objects::nonNull).collect(Collectors.toSet());
        var lisInventoryIds = releaseDTO.getWarehouseId();
        try {
            var res = new UniformEntityResponse();

            return warehouseRepository.findById(lisInventoryIds).map(warehouseMapper::toDto).flatMap(wareHouses -> {
                res.setWarehouseMap(Map.of(lisInventoryIds, wareHouses));
                return uomRepository.findAllByIdInAndCompanyAndDeleteAtIsNullAndDeleteByIsNull(new ArrayList<>(listUomId), releaseDTO.getCompany()).map(uomMapper::toDto).collectList().flatMap(uoms -> {
                    var uomMap = uoms.stream()
                        .collect(Collectors.toMap(UomDTO::getId, uom -> uom));
                    res.setUomMap(uomMap);
                    return Mono.just(res);
                });
            });
        }
        catch (Exception e) {
            log.error("Error when mapping data logistic to order DTO: {}", e.getMessage());
            return Mono.error(e);
        }
    }

    @Data
    public static class UniformEntityResponse {
        private Map<UUID, UomDTO> uomMap;
        private Map<UUID, WarehouseDTO> warehouseMap;
        private Map<UUID, String> supplierName;
    }
}
