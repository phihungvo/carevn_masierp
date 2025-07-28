package com.masi.logistics.service.mapper;

import com.masi.logistics.domain.*;
import com.masi.logistics.service.dto.*;

import java.util.Objects;
import java.util.UUID;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Item} and its DTO {@link ItemDTO}.
 */
@Mapper(componentModel = "spring")
public interface ItemMapper extends EntityMapper<ItemDTO, Item> {
    @Mapping(target = "itemCategory", source = "itemCategory", qualifiedByName = "itemCategoryId")
    @Mapping(target = "uom", source = "uom", qualifiedByName = "uomId")
    @Mapping(target = "supplier", source = "supplier", qualifiedByName = "supplierId")
    @Mapping(target = "revenueGroup", source = "revenueGroup", qualifiedByName = "revenueGroupTable")  // Add mapping for revenueGroup
    @Mapping(target = "itemTypes", source = "itemTypes", qualifiedByName = "itemTypeId")  // Add mapping for revenueGroup
    @Mapping(target = "itemTypeId", source = "itemTypeId")
    @Mapping(target = "itemType", source = "itemType")
    ItemDTO toDto(Item s);

    @Named("itemCategoryId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "code", source = "code")
    ItemCategoryDTO toDtoItemCategoryId(ItemCategory itemCategory);

    @Named("revenueGroupTable")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "code", source = "code")
    ItemCategoryDTO toDtoRevenueGroupTable(ItemCategory itemCategory);

    @Named("uomId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
//    @Mapping(target = "code", source = "code")
    @Mapping(target = "name", source = "name")
    UomDTO toDtoUomId(Uom uom);

    @Named("supplierId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    SuppliersDTO toDtoSupplierId(Suppliers suppliers);

    @Named("itemTypeId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "code", source = "code")
    ItemTypeDTO toDtoItemTypeId(ItemType itemType);

    default String map(UUID value) {
        return Objects.toString(value, null);
    }
}
