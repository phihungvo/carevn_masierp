package com.masi.employee.service;

import com.carevn.masi.dto.ApiResponse;
import com.carevn.masi.utils.SecurityUtils;
import com.masi.employee.domain.Employee;
import com.masi.employee.repository.EmployeeRepository;
import com.masi.employee.service.dto.EmployeeDTO;
import com.masi.employee.service.dto.WorkspaceDTO;
import com.masi.employee.service.mapper.EmployeeMapper;

import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing
 * {@link com.masi.employee.domain.Employee}.
 */
@Service
@Transactional
public class EmployeeService {

    private final Logger log = LoggerFactory.getLogger(EmployeeService.class);

    private final EmployeeRepository employeeRepository;

    private final EmployeeMapper employeeMapper;

    public EmployeeService(EmployeeRepository employeeRepository, EmployeeMapper employeeMapper) {
        this.employeeRepository = employeeRepository;
        this.employeeMapper = employeeMapper;
    }

    /**
     * Save a employee.
     *
     * @param employeeDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<EmployeeDTO> save(EmployeeDTO employeeDTO) {
        log.debug("Request to save Employee : {}", employeeDTO);
        return employeeRepository.save(employeeMapper.toEntity(employeeDTO)).map(employeeMapper::toDto);
    }

    /**
     * Update a employee.
     *
     * @param employeeDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<EmployeeDTO> update(EmployeeDTO employeeDTO) {
        log.debug("Request to update Employee : {}", employeeDTO);
        return employeeRepository.save(employeeMapper.toEntity(employeeDTO).setIsPersisted())
            .map(employeeMapper::toDto);
    }

    /**
     * Partially update a employee.
     *
     * @param employeeDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<EmployeeDTO> partialUpdate(EmployeeDTO employeeDTO) {
        log.debug("Request to partially update Employee : {}", employeeDTO);

        return employeeRepository
            .findById(employeeDTO.getId())
            .map(existingEmployee -> {
                employeeMapper.partialUpdate(existingEmployee, employeeDTO);

                return existingEmployee;
            })
            .flatMap(employeeRepository::save)
            .map(employeeMapper::toDto);
    }

    public Flux<EmployeeDTO> findAllByListId(List<UUID> ids) {
        return employeeRepository.findAllByIdIns(ids)
                .map(Employee::toDto);
    }

    /**
     * Get all the employees.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<EmployeeDTO> findAll() {

        return

            employeeRepository.findAllActive()
                .map(employeeMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Mono<ApiResponse<EmployeeDTO>> findAndCount() {
        log.debug("Request to get all Employees");
        return SecurityUtils.getCompanyId()
            .flatMap(company -> employeeRepository.countAllActive(company)
                .flatMap(total -> employeeRepository.findAllActive(company)
                    .map(Employee::toDto)
                    .collectList()
                    .map(employees -> new ApiResponse<>(employees, total))));

    }

    /**
     * Returns the number of employees available.
     *
     * @return the number of entities in the database.
     */
    public Mono<Long> countAll() {
        return employeeRepository.count();
    }

    /**
     * Get one employee by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<EmployeeDTO> findOne(UUID id) {
        log.debug("Request to get Employee : {}", id);
        return employeeRepository.findById(id).map(e -> {
            var dto = employeeMapper.toDto(e);
            if (dto.getWorkspaceId() == null) {
                dto.setWorkspaceId(UUID.fromString("00000000-0000-0000-0000-000000000000"));
            }
            if (e.getWorkspace() != null) {
                dto.setWorkspace(e.getWorkspace().toDTO());
            } else if (dto.getWorkspaceId() != null && dto.getWorkspace() == null) {
                var newWorkspace = new WorkspaceDTO();
                newWorkspace.setId(dto.getWorkspaceId());
                newWorkspace.setName("UNDEFINED");
                dto.setWorkspace(newWorkspace);
            }
            return dto;
        });
    }

    /**
     * Delete the employee by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(UUID id) {
        log.debug("Request to delete Employee : {}", id);
        return employeeRepository.deleteById(id);
    }


    public Mono<Boolean> areAllEmployeesValid(Collection<UUID> employeeIds) {
        employeeIds.removeIf(Objects::isNull);
        HashSet<UUID> employeeIdsSet = new HashSet<>(employeeIds);
        return employeeRepository.countAllByIdInAndIsActiveTrue(employeeIds)
            .map(employees -> employees == employeeIdsSet.size());
    }

    @Transactional(readOnly = true)
    public Flux<EmployeeDTO> findAllByIdInListString(List<String> listEmp) {
        return employeeRepository.findAllByIdInListString(listEmp).map(employeeMapper::toDto);
    }
}
