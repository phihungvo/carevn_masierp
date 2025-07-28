package com.masi.logistics.service.mapper;

import com.masi.logistics.domain.TypeOfFGeneration;
import com.masi.logistics.service.dto.TypeOfFGenerationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TypeOfFGeneration} and its DTO {@link TypeOfFGenerationDTO}.
 */
@Mapper(componentModel = "spring")
public interface TypeOfFGenerationMapper extends EntityMapper<TypeOfFGenerationDTO, TypeOfFGeneration> {}
