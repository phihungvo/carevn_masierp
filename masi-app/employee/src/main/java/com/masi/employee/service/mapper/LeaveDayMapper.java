package com.masi.employee.service.mapper;

import com.masi.employee.domain.LeaveDay;
import com.masi.employee.service.dto.LeaveDayDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link LeaveDay} and its DTO {@link LeaveDayDTO}.
 */
@Mapper(componentModel = "spring")
public interface LeaveDayMapper extends EntityMapper<LeaveDayDTO, LeaveDay> {}
