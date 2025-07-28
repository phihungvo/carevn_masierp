package com.masi.production.service.mapper;

import com.masi.production.domain.ReleaseWarehouse;
import com.masi.production.service.dto.ReleaseWarehouseDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link ReleaseWarehouse} and its DTO {@link ReleaseWarehouseDTO}.
 */
@Mapper(componentModel = "spring")
public interface ReleaseWarehouseMapper extends EntityMapper<ReleaseWarehouseDTO, ReleaseWarehouse> {}
