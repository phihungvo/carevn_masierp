package com.masi.production.service.mapper;

import com.masi.production.domain.Factory;
import com.masi.production.service.dto.FactoryDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Factory} and its DTO {@link FactoryDTO}.
 */
@Mapper(componentModel = "spring")
public interface FactoryMapper extends EntityMapper<FactoryDTO, Factory> {}
