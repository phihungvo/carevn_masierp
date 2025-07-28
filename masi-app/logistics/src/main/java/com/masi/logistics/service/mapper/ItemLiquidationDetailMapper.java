package com.masi.logistics.service.mapper;

import com.masi.logistics.domain.*;
import com.masi.logistics.service.dto.*;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ItemLiquidationDetail} and its DTO {@link ItemLiquidationDetailDTO}.
 */
@Mapper(componentModel = "spring")
public interface ItemLiquidationDetailMapper extends EntityMapper<ItemLiquidationDetailDTO, ItemLiquidationDetail> {

    @Mapping(target = "inventoriesStorage", source = "inventoriesStorage", qualifiedByName = "inventoriesStorageId")
    ItemLiquidationDetailDTO toDto(ItemLiquidationDetail itemLiquidationDetail);

    @Named("inventoriesStorageId")
    @BeanMapping(ignoreByDefault = false)
    @Mapping(target = "item", source = "item",  qualifiedByName = "itemId")
    InventoriesStorageDTO toDtoInventoriesId(InventoriesStorage inventoriesStorage);

    @Named("itemId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "code", source = "code")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "supplierId", source = "supplierId")
    @Mapping(target = "supplier", source = "supplier", qualifiedByName = "supplierId")
    ItemDTO toDtoItemId(Item item);

    @Named("supplierId")
    @BeanMapping(ignoreByDefault = false)
    SuppliersDTO toDtoSupplierId(Suppliers supplier);

}
