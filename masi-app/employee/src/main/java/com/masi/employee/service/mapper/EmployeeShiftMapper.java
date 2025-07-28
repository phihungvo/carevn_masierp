package com.masi.employee.service.mapper;

import com.masi.employee.domain.EmployeeShift;
import com.masi.employee.service.dto.EmployeeShiftDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link EmployeeShift} and its DTO {@link EmployeeShiftDTO}.
 */
@Mapper(componentModel = "spring")
public interface EmployeeShiftMapper extends EntityMapper<EmployeeShiftDTO, EmployeeShift> {}
