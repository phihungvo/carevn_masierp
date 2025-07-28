package com.masi.employee.service.mapper;

import com.masi.employee.domain.EmployeeShiftDetail;
import com.masi.employee.service.dto.EmployeeShiftDetailDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link EmployeeShiftDetail} and its DTO {@link EmployeeShiftDetailDTO}.
 */
@Mapper(componentModel = "spring")
public interface EmployeeShiftDetailMapper extends EntityMapper<EmployeeShiftDetailDTO, EmployeeShiftDetail> {}
