package com.masi.logistics.service.mapper;

import com.masi.logistics.domain.*;
import com.masi.logistics.service.dto.*;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link InventoriesStorage} and its DTO {@link InventoriesStorageDTO}.
 */
@Mapper(componentModel = "spring")
public interface InventoriesStorageMapper extends EntityMapper<InventoriesStorageDTO, InventoriesStorage> {

    @Mapping(target = "inventoriesDetail", source = "inventoriesDetail", qualifiedByName = "inventoriesDetailId")
    @Mapping(target = "item", source = "item", qualifiedByName = "itemId")
    @Mapping(target = "warehouse", source = "warehouse", qualifiedByName = "warehouseId")
    @Mapping(target = "supplier", source = "supplier", qualifiedByName = "supplierId")
    InventoriesStorageDTO toDto(InventoriesStorage inventoriesStorage);


    @Named("inventoriesDetailId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "code", source = "code")
    InventoriesDetailDTO toDtoInventoriesDetailId(InventoriesDetail inventoriesDetail);


    @Named("itemId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "code", source = "code")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "itemCategory", source = "itemCategory", qualifiedByName = "itemCategoryId")
    @Mapping(target = "supplierId", source = "supplierId")
    @Mapping(target = "uom", source = "uom", qualifiedByName = "uomId")
    @Mapping(target = "percentProtein" , source = "percentProtein")
    @Mapping(target = "supplier", source = "supplier", qualifiedByName = "supplierId")
    ItemDTO toDtoItemId(Item item);


    @Named("itemCategoryId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "code", source = "code")
    @Mapping(target = "name", source = "name")
    ItemCategoryDTO toDtoItemCategoryId(ItemCategory itemCategory);

    @Named("warehouseId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "code", source = "code")
    WarehouseDTO toDtoWarehouseId(Warehouse warehouse);

    @Named("uomId")
    @BeanMapping(ignoreByDefault = false)
    UomDTO toDtoUomId(Uom uom);

    @Named("supplierId")
    @BeanMapping(ignoreByDefault = false)
    SuppliersDTO toDtoSupplierId(Suppliers supplier);

}
