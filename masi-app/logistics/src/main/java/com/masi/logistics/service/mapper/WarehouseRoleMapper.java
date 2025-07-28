package com.masi.logistics.service.mapper;

import com.masi.logistics.domain.WarehouseRole;
import com.masi.logistics.domain.WarehouseType;
import com.masi.logistics.service.dto.WarehouseRoleDTO;
import com.masi.logistics.service.dto.WarehouseTypeDTO;
import java.util.Objects;
import java.util.UUID;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link WarehouseRole} and its DTO {@link WarehouseRoleDTO}.
 */
@Mapper(componentModel = "spring")
public interface WarehouseRoleMapper extends EntityMapper<WarehouseRoleDTO, WarehouseRole> {
    @Mapping(target = "warehouseType", source = "warehouseType", qualifiedByName = "warehouseTypeId")
    WarehouseRoleDTO toDto(WarehouseRole s);

    @Named("warehouseTypeId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    WarehouseTypeDTO toDtoWarehouseTypeId(WarehouseType warehouseType);

    default String map(UUID value) {
        return Objects.toString(value, null);
    }
}
