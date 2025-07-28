package com.masi.logistics.service.mapper;

import com.masi.logistics.domain.*;
import com.masi.logistics.service.dto.*;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AssetTransferDetails} and its DTO {@link AssetTransferDetailsDTO}.
 */
@Mapper(componentModel = "spring")
public interface AssetTransferDetailsMapper extends EntityMapper<AssetTransferDetailsDTO, AssetTransferDetails> {

    @Mapping(source = "inventoriesStorage", target = "inventoriesStorage", qualifiedByName = "inventoriesStoragesId")
    @Mapping(source = "itemAssetTransfer", target = "itemAssetTransfer", qualifiedByName = "itemAssetTransferId")
    AssetTransferDetailsDTO toDto(AssetTransferDetails assetTransferDetails);

    @Named("inventoriesStoragesId")
    @BeanMapping(ignoreByDefault = false)
    InventoriesStorageDTO toDtoInventoriesStoragesId(InventoriesStorage inventoriesStorage);

    @Named("itemAssetTransferId")
    @BeanMapping(ignoreByDefault = false)
    ItemAssetTransferDTO toDtoItemAssetTransferId(ItemAssetTransfer itemAssetTransfer);

    @Named("itemId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "id", target = "id")
    @Mapping(source = "code", target = "code")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "supplierId", target = "supplierId")
    @Mapping(source = "supplier", target = "supplier", qualifiedByName = "supplierId")
    ItemDTO toDtoItemId(Item item);

    @Named("supplierId")
    @BeanMapping(ignoreByDefault = false)
    SuppliersDTO toDtoSupplierId(Suppliers supplier);
}
