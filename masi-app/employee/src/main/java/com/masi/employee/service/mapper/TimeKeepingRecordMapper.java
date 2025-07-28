package com.masi.employee.service.mapper;

import com.masi.employee.domain.Employee;
import com.masi.employee.domain.TimeKeepingRecord;
import com.masi.employee.service.dto.EmployeeDTO;
import com.masi.employee.service.dto.TimeKeepingRecordDTO;
import java.util.Objects;
import java.util.UUID;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TimeKeepingRecord} and its DTO {@link TimeKeepingRecordDTO}.
 */
@Mapper(componentModel = "spring")
public interface TimeKeepingRecordMapper extends EntityMapper<TimeKeepingRecordDTO, TimeKeepingRecord> {
    @Mapping(target = "employee", source = "employee", qualifiedByName = "employeeId")
    TimeKeepingRecordDTO toDto(TimeKeepingRecord s);

    @Named("employeeId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EmployeeDTO toDtoEmployeeId(Employee employee);

    default String map(UUID value) {
        return Objects.toString(value, null);
    }
}
