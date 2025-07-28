package com.masi.employee.service.mapper;

import com.masi.employee.domain.Employee;
import com.masi.employee.domain.ProcessLeaveRegimeRequest;
import com.masi.employee.service.dto.EmployeeDTO;
import com.masi.employee.service.dto.ProcessLeaveRegimeRequestDTO;

import java.util.Objects;
import java.util.UUID;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link ProcessLeaveRegimeRequest} and its DTO
 * {@link ProcessLeaveRegimeRequestDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProcessLeaveRegimeRequestMapper
        extends EntityMapper<ProcessLeaveRegimeRequestDTO, ProcessLeaveRegimeRequest> {

    @Mapping(target = "employee", source = "employee", qualifiedByName = "employeeId")
    @Mapping(target = "approver", source = "approver", qualifiedByName = "employeeId")
    ProcessLeaveRegimeRequestDTO toDto(ProcessLeaveRegimeRequest s);

    @Named("employeeId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "firstName", source = "firstName")
    @Mapping(target = "lastName", source = "lastName")
    EmployeeDTO toDtoEmployeeId(Employee employee);

    default String map(UUID value) {
        return Objects.toString(value, null);
    }

}
