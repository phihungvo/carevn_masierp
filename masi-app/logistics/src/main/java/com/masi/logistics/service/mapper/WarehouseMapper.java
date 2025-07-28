package com.masi.logistics.service.mapper;

import com.masi.logistics.domain.Warehouse;
import com.masi.logistics.domain.WarehouseType;
import com.masi.logistics.service.dto.WarehouseDTO;
import com.masi.logistics.service.dto.WarehouseTypeDTO;
import java.util.Objects;
import java.util.UUID;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Warehouse} and its DTO {@link WarehouseDTO}.
 */
@Mapper(componentModel = "spring")
public interface WarehouseMapper extends EntityMapper<WarehouseDTO, Warehouse> {
    @Mapping(target = "warehouseType", source = "warehouseType", qualifiedByName = "warehouseTypeId")
    WarehouseDTO toDto(Warehouse s);

    @Named("warehouseTypeId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "code", source = "code")
    @Mapping(target = "name", source = "name")
    WarehouseTypeDTO toDtoWarehouseTypeId(WarehouseType warehouseType);

    default String map(UUID value) {
        return Objects.toString(value, null);
    }
}
