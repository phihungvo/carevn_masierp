package com.masi.logistics.service.dto;

import com.masi.logistics.domain.ItemAssetDepreciationDetail;
import lombok.Data;

import java.util.List;

@Data
public class ArisesDTO {
    private InventoriesStorageDTO inventoriesStorage;
    private List<AssetTransferDetailsDTO> assetTransferDetails;
    private List<ItemAssetDepreciationDetailDTO> itemAssetDepreciationDetail;
    private ItemLiquidationDetailDTO itemLiquidationDetail;
}
