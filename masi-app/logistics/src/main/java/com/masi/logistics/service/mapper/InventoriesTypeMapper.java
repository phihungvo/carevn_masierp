package com.masi.logistics.service.mapper;

import com.masi.logistics.domain.InventoriesType;
import com.masi.logistics.service.dto.InventoriesTypeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link InventoriesType} and its DTO {@link InventoriesTypeDTO}.
 */
@Mapper(componentModel = "spring")
public interface InventoriesTypeMapper extends EntityMapper<InventoriesTypeDTO, InventoriesType> {}
