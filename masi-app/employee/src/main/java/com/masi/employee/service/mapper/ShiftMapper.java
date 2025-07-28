package com.masi.employee.service.mapper;

import com.masi.employee.domain.Shift;
import com.masi.employee.service.dto.ShiftDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Shift} and its DTO {@link ShiftDTO}.
 */
@Mapper(componentModel = "spring")
public interface ShiftMapper extends EntityMapper<ShiftDTO, Shift> {}
