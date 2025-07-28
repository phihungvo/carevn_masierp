package com.masi.logistics.service.mapper;

import com.masi.logistics.domain.InventoriesDetail;
import com.masi.logistics.domain.Item;
import com.masi.logistics.service.dto.InventoriesDetailDTO;
import com.masi.logistics.service.dto.ItemDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link InventoriesDetail} and its DTO {@link InventoriesDetailDTO}.
 */
@Mapper(componentModel = "spring")
public interface InventoriesDetailMapper extends EntityMapper<InventoriesDetailDTO, InventoriesDetail> {
    @Mapping(target = "item", source = "item", qualifiedByName = "itemId")
    InventoriesDetailDTO toDto(InventoriesDetail s);

    @Named("itemId")
    ItemDTO toDtoId(Item item);
}
