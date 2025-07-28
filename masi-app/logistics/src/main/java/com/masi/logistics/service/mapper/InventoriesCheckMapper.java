package com.masi.logistics.service.mapper;

import com.masi.logistics.domain.InventoriesCheck;
import com.masi.logistics.domain.Warehouse;
import com.masi.logistics.service.dto.InventoriesCheckDTO;
import com.masi.logistics.service.dto.WarehouseDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link InventoriesCheck} and its DTO {@link InventoriesCheckDTO}.
 */
@Mapper(componentModel = "spring")
public interface InventoriesCheckMapper extends EntityMapper<InventoriesCheckDTO, InventoriesCheck> {

    @Mapping(target = "warehouse", source = "warehouse", qualifiedByName = "warehouseId")
//    @Mapping(target = "amountOfDifference", source = "amountOfDifference")
    InventoriesCheckDTO toDto(InventoriesCheck inventoriesCheck);

    @Named("warehouseId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "code", source = "code")
    WarehouseDTO toDtoWarehouseId(Warehouse warehouse);
}
