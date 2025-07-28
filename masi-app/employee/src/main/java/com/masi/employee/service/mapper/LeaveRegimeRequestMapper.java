package com.masi.employee.service.mapper;

import com.masi.employee.domain.Employee;
import com.masi.employee.domain.LeaveRegimeRequest;
import com.masi.employee.domain.LeaveRequest;
import com.masi.employee.service.dto.EmployeeDTO;
import com.masi.employee.service.dto.LeaveRegimeRequestDTO;
import com.masi.employee.service.dto.LeaveRequestDTO;
import org.mapstruct.*;

import java.util.Objects;
import java.util.UUID;

/**
 * Mapper for the entity {@link LeaveRegimeRequest} and its DTO
 * {@link LeaveRegimeRequestDTO}.
 */
@Mapper(componentModel = "spring")
public interface LeaveRegimeRequestMapper extends EntityMapper<LeaveRegimeRequestDTO, LeaveRegimeRequest> {
    @Mapping(target = "employee", source = "employee", qualifiedByName = "employeeId")
    @Mapping(target = "substitute", source = "substitute", qualifiedByName = "employeeId")
    LeaveRegimeRequestDTO toDto(LeaveRegimeRequest s);

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
