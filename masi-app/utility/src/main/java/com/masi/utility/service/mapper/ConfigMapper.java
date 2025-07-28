package com.masi.utility.service.mapper;

import com.masi.utility.domain.Config;
import com.masi.utility.service.dto.ConfigDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Config} and its DTO {@link ConfigDTO}.
 */
@Mapper(componentModel = "spring")
public interface ConfigMapper extends EntityMapper<ConfigDTO, Config> {}
