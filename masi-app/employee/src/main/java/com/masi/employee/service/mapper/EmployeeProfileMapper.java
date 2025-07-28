package com.masi.employee.service.mapper;

import com.masi.employee.domain.EmployeeProfile;
import com.masi.employee.service.dto.EmployeeProfileDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link EmployeeProfile} and its DTO {@link EmployeeProfileDTO}.
 */
@Mapper(componentModel = "spring")
public interface EmployeeProfileMapper extends EntityMapper<EmployeeProfileDTO, EmployeeProfile> {}
