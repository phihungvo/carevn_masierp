package com.masi.employee.service;

import com.masi.employee.domain.UniformFormDetail;
import com.masi.employee.domain.UniformOrderStock;
import com.masi.employee.repository.EmployeeRepository;
import com.masi.employee.repository.UniformFormDetailRepository;
import com.masi.employee.repository.UniformOrderStockRepository;
import com.masi.employee.service.dto.UniformOrderStockDTO;
import com.masi.employee.service.dto.WarehouseDTO;
import com.masi.employee.service.mapper.UniformFormDetailMapper;
import com.masi.employee.service.mapper.UniformOrderStockMapper;

import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link UniformOrderStock}.
 */
@Service
@Transactional
public class UniformOrderStockService {

    private static final Logger log = LoggerFactory.getLogger(UniformOrderStockService.class);

    private final UniformOrderStockRepository uniformOrderStockRepository;
    private final UniformFormDetailRepository uniformFormDetailRepository;

    private final UniformOrderStockMapper uniformOrderStockMapper;
    private final UniformFormDetailMapper uniformFormDetailMapper;
    private final EmployeeRepository employeeRepository;
    private final LogisticClient logisticClient;


    public UniformOrderStockService(
        UniformOrderStockRepository uniformOrderStockRepository, UniformFormDetailRepository uniformFormDetailRepository,
        UniformOrderStockMapper uniformOrderStockMapper, UniformFormDetailMapper uniformFormDetailMapper, EmployeeRepository employeeRepository, LogisticClient logisticClient
    ) {
        this.uniformOrderStockRepository = uniformOrderStockRepository;
        this.uniformFormDetailRepository = uniformFormDetailRepository;
        this.uniformOrderStockMapper = uniformOrderStockMapper;
        this.uniformFormDetailMapper = uniformFormDetailMapper;
        this.employeeRepository = employeeRepository;
        this.logisticClient = logisticClient;
    }

    /**
     * Save a uniformOrderStock.
     *
     * @param uniformOrderStockDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<UniformOrderStockDTO> save(UniformOrderStockDTO uniformOrderStockDTO) {
        log.debug("Request to save UniformOrderStock : {}", uniformOrderStockDTO);
        return uniformOrderStockRepository.save(uniformOrderStockMapper.toEntity(uniformOrderStockDTO)).map(uniformOrderStockMapper::toDto);
    }

    /**
     * Update a uniformOrderStock.
     *
     * @param uniformOrderStockDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<UniformOrderStockDTO> update(UniformOrderStockDTO uniformOrderStockDTO) {
        log.debug("Request to update UniformOrderStock : {}", uniformOrderStockDTO);
        return uniformOrderStockRepository
            .save(uniformOrderStockMapper.toEntity(uniformOrderStockDTO).setIsPersisted())
            .map(uniformOrderStockMapper::toDto);
    }

    /**
     * Partially update a uniformOrderStock.
     *
     * @param uniformOrderStockDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<UniformOrderStockDTO> partialUpdate(UniformOrderStockDTO uniformOrderStockDTO) {
        log.debug("Request to partially update UniformOrderStock : {}", uniformOrderStockDTO);

        return uniformOrderStockRepository
            .findById(uniformOrderStockDTO.getId())
            .map(existingUniformOrderStock -> {
                uniformOrderStockMapper.partialUpdate(existingUniformOrderStock, uniformOrderStockDTO);

                return existingUniformOrderStock;
            })
            .flatMap(uniformOrderStockRepository::save)
            .map(uniformOrderStockMapper::toDto);
    }

    /**
     * Get all the uniformOrderStocks.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<UniformOrderStockDTO> findAll(Pageable pageable) {
        log.debug("Request to get all UniformOrderStocks");
        return uniformOrderStockRepository.findAllBy(pageable).map(uniformOrderStockMapper::toDto);
    }

    /**
     * Returns the number of uniformOrderStocks available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return uniformOrderStockRepository.count();
    }

    /**
     * Get one uniformOrderStock by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<UniformOrderStockDTO> findOne(UUID id) {
        log.debug("Request to get UniformOrderStock : {}", id);
        return uniformOrderStockRepository.findById(id).map(uniformOrderStockMapper::toDto);
    }

    /**
     * Delete the uniformOrderStock by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(UUID id) {
        log.debug("Request to delete UniformOrderStock : {}", id);
        return uniformOrderStockRepository.deleteById(id);
    }

    /**
     * Get all the uniformOrderStocks by uniformOrderId.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<UniformOrderStockDTO> findAllBy(Pageable pageable, ZonedDateTime startDate, ZonedDateTime endDate, String company) {
        log.debug("Request to get all UniformOrderStocks by uniformOrderId");
        return uniformOrderStockRepository.findAllBy(pageable, startDate, endDate, company)
            .map(uniformOrderStockMapper::toDto)
            .flatMap(u -> {
                System.out.println(u);
                return uniformFormDetailRepository.findAllByUniformOrderStock(u.getId(), company)
                    .collectList()
                    .map(uniformFormDetailMapper::toDto)
                    .flatMap(details -> {
                        u.setUniformFormDetail(new HashSet<>(details));
                        return logisticClient.getWarehouseById(u.getWarehouseId()).defaultIfEmpty(new WarehouseDTO())
                            .map(warehouse -> {
                                u.setWareHouseDTO(warehouse);
                                return u;
                            }).defaultIfEmpty(u);
                    }).defaultIfEmpty(u);
            });
    }
}
