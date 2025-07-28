package com.masi.employee.service.mapper;

import com.masi.employee.domain.Employee;
import com.masi.employee.domain.TimeKeepingExplanation;
import com.masi.employee.service.dto.EmployeeDTO;
import com.masi.employee.service.dto.TimeKeepingExplanationDTO;
import java.util.Objects;
import java.util.UUID;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TimeKeepingExplanation} and its DTO {@link TimeKeepingExplanationDTO}.
 */
@Mapper(componentModel = "spring")
public interface TimeKeepingExplanationMapper extends EntityMapper<TimeKeepingExplanationDTO, TimeKeepingExplanation> {
    @Mapping(target = "employee", source = "employee", qualifiedByName = "employeeId")
    TimeKeepingExplanationDTO toDto(TimeKeepingExplanation s);

    @Named("employeeId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EmployeeDTO toDtoEmployeeId(Employee employee);

    default String map(UUID value) {
        return Objects.toString(value, null);
    }
}
