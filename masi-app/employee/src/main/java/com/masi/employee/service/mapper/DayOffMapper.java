package com.masi.employee.service.mapper;

import com.masi.employee.domain.DayOff;
import com.masi.employee.service.dto.DayOffDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link DayOff} and its DTO {@link DayOffDTO}.
 */
@Mapper(componentModel = "spring")
public interface DayOffMapper extends EntityMapper<DayOffDTO, DayOff> {}
