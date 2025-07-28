package com.masi.logistics.service.mapper;

import com.masi.logistics.domain.InventoriesStorage;
import com.masi.logistics.domain.Item;
import com.masi.logistics.domain.ItemAssetDepreciationDetail;
import com.masi.logistics.service.dto.InventoriesStorageDTO;
import com.masi.logistics.service.dto.ItemAssetDepreciationDetailDTO;
import com.masi.logistics.service.dto.ItemDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ItemAssetDepreciationDetail} and its DTO {@link ItemAssetDepreciationDetailDTO}.
 */
@Mapper(componentModel = "spring")
public interface ItemAssetDepreciationDetailMapper extends EntityMapper<ItemAssetDepreciationDetailDTO, ItemAssetDepreciationDetail> {

    @Mapping(source = "inventoriesStorage", target = "inventoriesStorage", qualifiedByName = "inventoriesStoragesId")
    ItemAssetDepreciationDetailDTO toDto(ItemAssetDepreciationDetail itemAssetDepreciationDetail);

    @Named("inventoriesStoragesId")
    @Mapping(target = "item", source = "item", qualifiedByName = "itemId")
    @BeanMapping(ignoreByDefault = false)
    InventoriesStorageDTO toDtoInventoriesStoragesId(InventoriesStorage inventoriesStorage);

    @Named("itemId")
    @BeanMapping(ignoreByDefault = false)
    ItemDTO toDtoItemId(Item item);
}
