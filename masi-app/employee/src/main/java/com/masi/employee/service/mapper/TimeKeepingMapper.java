package com.masi.employee.service.mapper;

import com.masi.employee.domain.Employee;
import com.masi.employee.domain.TimeKeeping;
import com.masi.employee.service.dto.EmployeeDTO;
import com.masi.employee.service.dto.TimeKeepingDTO;
import java.util.Objects;
import java.util.UUID;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TimeKeeping} and its DTO {@link TimeKeepingDTO}.
 */
@Mapper(componentModel = "spring")
public interface TimeKeepingMapper extends EntityMapper<TimeKeepingDTO, TimeKeeping> {
    @Mapping(target = "employee", source = "employee", qualifiedByName = "employeeId")
    TimeKeepingDTO toDto(TimeKeeping s);

    @Named("employeeId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EmployeeDTO toDtoEmployeeId(Employee employee);

    default String map(UUID value) {
        return Objects.toString(value, null);
    }
}
