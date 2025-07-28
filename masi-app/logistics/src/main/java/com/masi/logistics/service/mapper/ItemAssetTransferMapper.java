package com.masi.logistics.service.mapper;

import com.masi.logistics.domain.*;
import com.masi.logistics.service.dto.*;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ItemAssetTransfer} and its DTO {@link ItemAssetTransferDTO}.
 */
@Mapper(componentModel = "spring")
public interface ItemAssetTransferMapper extends EntityMapper<ItemAssetTransferDTO, ItemAssetTransfer> {

    @Mapping(target = "transactionType", source = "transactionType", qualifiedByName = "transactionTypeId")
    @Mapping(target = "itemCategory", source = "itemCategory", qualifiedByName = "itemCategoryId")
    @Mapping(target = "inventoriesStorage", source = "inventoriesStorage", qualifiedByName = "inventoriesStorageId")
    ItemAssetTransferDTO toDto(ItemAssetTransfer itemAssetTransfer);

    @Named("transactionTypeId")
    @BeanMapping(ignoreByDefault = false)
    TransactionTypeDTO toTransactionTypeDTO(TransactionType transactionType);

    @Named("itemCategoryId")
    @BeanMapping(ignoreByDefault = false)
    ItemCategoryDTO toItemCategoryDTO(ItemCategory itemCategory);

    @Named("inventoriesStorageId")
    @BeanMapping(ignoreByDefault = false)
    @Mapping(target = "item", source = "item",  qualifiedByName = "itemId")
    InventoriesStorageDTO toInventoriesStorageDTO(InventoriesStorage inventoriesStorage);

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
