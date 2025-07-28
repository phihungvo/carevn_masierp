package com.masi.employee.service.mapper;

import com.masi.employee.domain.Employee;
import com.masi.employee.domain.LeaveRequest;
import com.masi.employee.service.dto.EmployeeDTO;
import com.masi.employee.service.dto.LeaveRequestDTO;
import java.util.Objects;
import java.util.UUID;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link LeaveRequest} and its DTO {@link LeaveRequestDTO}.
 */
@Mapper(componentModel = "spring")
public interface LeaveRequestMapper extends EntityMapper<LeaveRequestDTO, LeaveRequest> {
    @Mapping(target = "employee", source = "employee", qualifiedByName = "employeeId")
    @Mapping(target = "substitute", source = "substitute", qualifiedByName = "employeeId")
    LeaveRequestDTO toDto(LeaveRequest s);

    @Named("employeeId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EmployeeDTO toDtoEmployeeId(Employee employee);

    default String map(UUID value) {
        return Objects.toString(value, null);
    }
}
