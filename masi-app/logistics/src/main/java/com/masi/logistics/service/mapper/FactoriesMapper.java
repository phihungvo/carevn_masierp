package com.masi.logistics.service.mapper;

import com.masi.logistics.domain.Factories;
import com.masi.logistics.service.dto.FactoriesDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Factories} and its DTO {@link FactoriesDTO}.
 */
@Mapper(componentModel = "spring")
public interface FactoriesMapper extends EntityMapper<FactoriesDTO, Factories> {}
