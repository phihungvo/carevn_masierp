package com.masi.employee.service.mapper;

import com.masi.employee.domain.ConfirmLeave;
import com.masi.employee.service.dto.ConfirmLeaveDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ConfirmLeave} and its DTO {@link ConfirmLeaveDTO}.
 */
@Mapper(componentModel = "spring")
public interface ConfirmLeaveMapper extends EntityMapper<ConfirmLeaveDTO, ConfirmLeave> {}
