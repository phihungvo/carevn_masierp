package com.masi.logistics.service;

import com.carevn.masi.dto.EmployeeDTO;
import com.masi.logistics.domain.criteria.FactoriesCriteria;
import com.masi.logistics.repository.FactoriesRepository;
import com.masi.logistics.service.dto.FactoriesDTO;
import com.masi.logistics.service.mapper.FactoriesMapper;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.masi.logistics.service.web.client.EmployeeClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.masi.logistics.domain.Factories}.
 */
@Service
@Transactional
public class FactoriesService {

    private static final Logger log = LoggerFactory.getLogger(FactoriesService.class);

    private final FactoriesRepository factoriesRepository;

    private final FactoriesMapper factoriesMapper;
    private final EmployeeClient employeeClient;

    public FactoriesService(FactoriesRepository factoriesRepository, FactoriesMapper factoriesMapper, EmployeeClient employeeClient) {
        this.factoriesRepository = factoriesRepository;
        this.factoriesMapper = factoriesMapper;
        this.employeeClient = employeeClient;
    }

    /**
     * Save a factories.
     *
     * @param factoriesDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<FactoriesDTO> save(FactoriesDTO factoriesDTO) {
        log.debug("Request to save Factories : {}", factoriesDTO);
        return factoriesRepository.save(factoriesMapper.toEntity(factoriesDTO)).map(factoriesMapper::toDto);
    }

    /**
     * Update a factories.
     *
     * @param factoriesDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<FactoriesDTO> update(FactoriesDTO factoriesDTO) {
        log.debug("Request to update Factories : {}", factoriesDTO);
        return factoriesRepository.save(factoriesMapper.toEntity(factoriesDTO).setIsPersisted()).map(factoriesMapper::toDto);
    }

    /**
     * Partially update a factories.
     *
     * @param factoriesDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<FactoriesDTO> partialUpdate(FactoriesDTO factoriesDTO) {
        log.debug("Request to partially update Factories : {}", factoriesDTO);

        return factoriesRepository
            .findById(factoriesDTO.getId())
            .map(existingFactories -> {
                factoriesMapper.partialUpdate(existingFactories, factoriesDTO);
                existingFactories.setIsPersisted();
                return existingFactories;
            })
            .flatMap(factoriesRepository::save)
            .map(factoriesMapper::toDto);
    }

    /**
     * Find factories by Criteria.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<FactoriesDTO> findByCriteria(FactoriesCriteria criteria, Pageable pageable) {
        log.debug("Request to get all Factories by Criteria");

        return factoriesRepository.findByCriteria(criteria, pageable)
                .map(factoriesMapper::toDto)
                .collectList()
                .flatMapMany(dtos -> {
                    List<UUID> ids = dtos.stream()
                            .map(FactoriesDTO::getEmployeeOwnerId)
                            .filter(Objects::nonNull)
                            .distinct()
                            .toList();

                    if (ids.isEmpty()) {
                        return Flux.fromIterable(dtos);
                    }

                    return employeeClient.getEmployeesByListIds(ids)
                            .collectList()
                            .flatMapMany(employeeDTOS -> {
                                Map<UUID, EmployeeDTO> employeeMap = employeeDTOS.stream()
                                        .collect(Collectors.toMap(EmployeeDTO::getId, Function.identity()));

                                dtos.forEach(factoriesDTO -> {
                                    if (factoriesDTO.getEmployeeOwnerId() != null) {
                                        factoriesDTO.setEmployeeOwner(employeeMap.get(factoriesDTO.getEmployeeOwnerId()));
                                    }
                                });

                                return Flux.fromIterable(dtos);
                            });
                });
    }


    /**
     * Find the count of factories by criteria.
     * @param criteria filtering criteria
     * @return the count of factories
     */
    public Mono<Long> countByCriteria(FactoriesCriteria criteria) {
        log.debug("Request to get the count of all Factories by Criteria");
        return factoriesRepository.countByCriteria(criteria);
    }

    /**
     * Returns the number of factories available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return factoriesRepository.count();
    }

    /**
     * Get one factories by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<FactoriesDTO> findOne(UUID id) {
        log.debug("Request to get Factories : {}", id);
        return factoriesRepository.findById(id)
                .map(factoriesMapper::toDto)
                .flatMap(dto -> {
                    return employeeClient.getEmployeesByListIds(Collections.singletonList(dto.getEmployeeOwnerId()))
                            .collectList()
                            .flatMap(employeeDTOs -> {
                                if (employeeDTOs.isEmpty()) {
                                    return Mono.just(dto);
                                }
                                dto.setEmployeeOwner(employeeDTOs.get(0));
                                return Mono.just(dto);
                            });
                });
    }


    /**
     * Delete the factories by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(UUID id) {
        log.debug("Request to delete Factories : {}", id);
        return factoriesRepository.deleteById(id);
    }

    public Mono<FactoriesDTO> changeIsActive(UUID id, boolean b) {
        log.debug("Request to change isActive Factories : {}", id);
        return factoriesRepository.findById(id)
                .map(factories -> {
                    factories.setIsActive(b);
                    factories.setIsPersisted();
                    return factories;
                })
                .flatMap(factoriesRepository::save)
                .map(factoriesMapper::toDto);
    }
}
