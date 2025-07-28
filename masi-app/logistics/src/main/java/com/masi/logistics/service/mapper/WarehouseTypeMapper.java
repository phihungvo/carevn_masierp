package com.masi.logistics.service.mapper;

import com.masi.logistics.domain.WarehouseType;
import com.masi.logistics.service.dto.WarehouseTypeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link WarehouseType} and its DTO {@link WarehouseTypeDTO}.
 */
@Mapper(componentModel = "spring")
public interface WarehouseTypeMapper extends EntityMapper<WarehouseTypeDTO, WarehouseType> {}
