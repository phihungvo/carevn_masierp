package com.masi.utility.service.mapper;

import com.masi.utility.domain.HolidayConfig;
import com.masi.utility.service.dto.HolidayConfigDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link HolidayConfig} and its DTO {@link HolidayConfigDTO}.
 */
@Mapper(componentModel = "spring")
public interface HolidayConfigMapper extends EntityMapper<HolidayConfigDTO, HolidayConfig> {}
