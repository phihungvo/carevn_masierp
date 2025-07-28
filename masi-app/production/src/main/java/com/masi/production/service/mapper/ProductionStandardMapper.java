package com.masi.production.service.mapper;

import com.masi.production.domain.ProductionStandard;
import com.masi.production.service.dto.ProductionStandardDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ProductionStandard} and its DTO {@link ProductionStandardDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProductionStandardMapper extends EntityMapper<ProductionStandardDTO, ProductionStandard> {}
