package com.masi.logistics.service.mapper;

import com.masi.logistics.domain.ItemCategory;
import com.masi.logistics.domain.WarehouseType;
import com.masi.logistics.service.dto.ItemCategoryDTO;
import com.masi.logistics.service.dto.WarehouseTypeDTO;
import java.util.Objects;
import java.util.UUID;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ItemCategory} and its DTO {@link ItemCategoryDTO}.
 */
@Mapper(componentModel = "spring")
public interface ItemCategoryMapper extends EntityMapper<ItemCategoryDTO, ItemCategory> {
    @Mapping(target = "warehouseType", source = "warehouseType", qualifiedByName = "warehouseTypeId")
    ItemCategoryDTO toDto(ItemCategory s);

    @Named("warehouseTypeId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    WarehouseTypeDTO toDtoWarehouseTypeId(WarehouseType warehouseType);

    default String map(UUID value) {
        return Objects.toString(value, null);
    }
}
