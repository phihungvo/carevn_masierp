package com.masi.logistics.service.mapper;

import com.masi.logistics.domain.Item;
import com.masi.logistics.domain.ItemType;
import com.masi.logistics.service.dto.ItemDTO;
import com.masi.logistics.service.dto.ItemTypeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ItemType} and its DTO {@link ItemTypeDTO}.
 */
@Mapper(componentModel = "spring")
public interface ItemTypeMapper extends EntityMapper<ItemTypeDTO, ItemType> {

    @Mapping(target = "itemType", source = "itemType")
    ItemTypeDTO toDto(ItemType s);
}
