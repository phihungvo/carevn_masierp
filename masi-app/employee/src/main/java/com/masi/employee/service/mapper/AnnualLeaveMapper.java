package com.masi.employee.service.mapper;

import com.masi.employee.domain.AnnualLeave;
import com.masi.employee.service.dto.AnnualLeaveDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AnnualLeave} and its DTO {@link AnnualLeaveDTO}.
 */
@Mapper(componentModel = "spring")
public interface AnnualLeaveMapper extends EntityMapper<AnnualLeaveDTO, AnnualLeave> {}