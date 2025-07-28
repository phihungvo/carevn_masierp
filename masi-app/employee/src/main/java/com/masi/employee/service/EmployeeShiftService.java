package com.masi.employee.service;

import com.masi.employee.domain.criteria.EmployeeShiftCriteria;
import com.masi.employee.repository.EmployeeShiftRepository;
import com.masi.employee.service.dto.EmployeeShiftDTO;
import com.masi.employee.service.mapper.EmployeeShiftMapper;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.masi.employee.domain.EmployeeShift}.
 */
@Service
@Transactional
public class EmployeeShiftService {

    private static final Logger log = LoggerFactory.getLogger(EmployeeShiftService.class);

    private final EmployeeShiftRepository employeeShiftRepository;

    private final EmployeeShiftMapper employeeShiftMapper;

    public EmployeeShiftService(EmployeeShiftRepository employeeShiftRepository, EmployeeShiftMapper employeeShiftMapper) {
        this.employeeShiftRepository = employeeShiftRepository;
        this.employeeShiftMapper = employeeShiftMapper;
    }

    /**
     * Save a employeeShift.
     *
     * @param employeeShiftDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<EmployeeShiftDTO> save(EmployeeShiftDTO employeeShiftDTO) {
        log.debug("Request to save EmployeeShift : {}", employeeShiftDTO);
        return employeeShiftRepository.save(employeeShiftMapper.toEntity(employeeShiftDTO)).map(employeeShiftMapper::toDto);
    }

    /**
     * Update a employeeShift.
     *
     * @param employeeShiftDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<EmployeeShiftDTO> update(EmployeeShiftDTO employeeShiftDTO) {
        log.debug("Request to update EmployeeShift : {}", employeeShiftDTO);
        return employeeShiftRepository
            .save(employeeShiftMapper.toEntity(employeeShiftDTO).setIsPersisted())
            .map(employeeShiftMapper::toDto);
    }

    /**
     * Partially update a employeeShift.
     *
     * @param employeeShiftDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<EmployeeShiftDTO> partialUpdate(EmployeeShiftDTO employeeShiftDTO) {
        log.debug("Request to partially update EmployeeShift : {}", employeeShiftDTO);

        return employeeShiftRepository
            .findById(employeeShiftDTO.getId())
            .map(existingEmployeeShift -> {
                employeeShiftMapper.partialUpdate(existingEmployeeShift, employeeShiftDTO);

                return existingEmployeeShift;
            })
            .flatMap(employeeShiftRepository::save)
            .map(employeeShiftMapper::toDto);
    }

    /**
     * Find employeeShifts by Criteria.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<EmployeeShiftDTO> findByCriteria(EmployeeShiftCriteria criteria, Pageable pageable) {
        log.debug("Request to get all EmployeeShifts by Criteria");
        return employeeShiftRepository.findByCriteria(criteria, pageable).map(employeeShiftMapper::toDto);
    }

    /**
     * Find the count of employeeShifts by criteria.
     * @param criteria filtering criteria
     * @return the count of employeeShifts
     */
    public Mono<Long> countByCriteria(EmployeeShiftCriteria criteria) {
        log.debug("Request to get the count of all EmployeeShifts by Criteria");
        return employeeShiftRepository.countByCriteria(criteria);
    }

    /**
     * Returns the number of employeeShifts available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return employeeShiftRepository.count();
    }

    /**
     * Get one employeeShift by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<EmployeeShiftDTO> findOne(UUID id) {
        log.debug("Request to get EmployeeShift : {}", id);
        return employeeShiftRepository.findById(id).map(employeeShiftMapper::toDto);
    }

    /**
     * Delete the employeeShift by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(UUID id) {
        log.debug("Request to delete EmployeeShift : {}", id);
        return employeeShiftRepository.deleteById(id);
    }
}
