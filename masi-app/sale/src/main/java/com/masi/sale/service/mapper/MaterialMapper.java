package com.masi.sale.service.mapper;

import com.masi.sale.domain.Material;
import com.masi.sale.service.dto.MaterialDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Material} and its DTO {@link MaterialDTO}.
 */
@Mapper(componentModel = "spring")
public interface MaterialMapper extends EntityMapper<MaterialDTO, Material> {
}
