package com.masi.employee.service.mapper;

import com.masi.employee.domain.Employee;
import com.masi.employee.domain.TimeKeeping;
import com.masi.employee.domain.TimeKeepingExplanation;
import com.masi.employee.domain.TimeKeepingViolation;
import com.masi.employee.service.dto.EmployeeDTO;
import com.masi.employee.service.dto.TimeKeepingDTO;
import com.masi.employee.service.dto.TimeKeepingExplanationDTO;
import com.masi.employee.service.dto.TimeKeepingViolationDTO;
import java.util.Objects;
import java.util.UUID;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TimeKeepingViolation} and its DTO {@link TimeKeepingViolationDTO}.
 */
@Mapper(componentModel = "spring")
public interface TimeKeepingViolationMapper extends EntityMapper<TimeKeepingViolationDTO, TimeKeepingViolation> {
    @Mapping(target = "timeKeeping", source = "timeKeeping", qualifiedByName = "timeKeepingId")
    @Mapping(target = "employee", source = "employee", qualifiedByName = "employeeId")
    @Mapping(target = "explanation", source = "explanation", qualifiedByName = "timeKeepingExplanationId")
    TimeKeepingViolationDTO toDto(TimeKeepingViolation s);

    @Named("timeKeepingId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TimeKeepingDTO toDtoTimeKeepingId(TimeKeeping timeKeeping);

    @Named("employeeId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EmployeeDTO toDtoEmployeeId(Employee employee);

    @Named("timeKeepingExplanationId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TimeKeepingExplanationDTO toDtoTimeKeepingExplanationId(TimeKeepingExplanation timeKeepingExplanation);

    default String map(UUID value) {
        return Objects.toString(value, null);
    }
}
