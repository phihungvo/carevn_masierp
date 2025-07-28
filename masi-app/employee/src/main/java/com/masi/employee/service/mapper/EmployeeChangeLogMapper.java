package com.masi.employee.service.mapper;

import com.masi.employee.domain.EmployeeChangeLog;
import com.masi.employee.service.dto.EmployeeChangeLogDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link EmployeeChangeLog} and its DTO {@link EmployeeChangeLogDTO}.
 */
@Mapper(componentModel = "spring")
public interface EmployeeChangeLogMapper extends EntityMapper<EmployeeChangeLogDTO, EmployeeChangeLog> {}
