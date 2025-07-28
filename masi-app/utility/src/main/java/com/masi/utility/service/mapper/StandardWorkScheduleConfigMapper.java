package com.masi.utility.service.mapper;

import com.masi.utility.domain.StandardWorkScheduleConfig;
import com.masi.utility.service.dto.StandardWorkScheduleConfigDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link StandardWorkScheduleConfig} and its DTO {@link StandardWorkScheduleConfigDTO}.
 */
@Mapper(componentModel = "spring")
public interface StandardWorkScheduleConfigMapper extends EntityMapper<StandardWorkScheduleConfigDTO, StandardWorkScheduleConfig> {}
