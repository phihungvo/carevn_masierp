package com.masi.logistics.service.exportDTO;

import com.carevn.masi.utils.CSV.CSVColumn;
import com.masi.logistics.domain.InventoriesStorage;
import com.masi.logistics.service.dto.InventoriesStorageDTO;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class Inventories_storage_DTO_Export {
    @CSVColumn(name = "Mã tài sản")
    private String inventoryCode;

    @CSVColumn(name = "Mã từ kho")
    private String warehouseName;

    @CSVColumn(name = "Diễn giải")
    private String note;

    @CSVColumn(name = "Phân nhóm")
    private String itemCategory;

    @CSVColumn(name = "MST")
    private String taxCode; ;

    @CSVColumn(name = "Lý do nhập")
    private String reasonImport;

    @CSVColumn(name = "Nguyên giá")
    private BigDecimal price;

    @CSVColumn(name = "Gía trị còn lại")
    private BigDecimal priceCurrency;

    @CSVColumn(name = "Số lượng còn lại")
    private BigDecimal quantityCurrent;

    @CSVColumn(name = "Trạng thái")
    private String status;

    public Inventories_storage_DTO_Export(InventoriesStorageDTO inventoriesStorageDTO) {
        this.inventoryCode = inventoriesStorageDTO.getCode() != null ? inventoriesStorageDTO.getCode() : "";
        this.warehouseName = inventoriesStorageDTO.getWarehouse() != null && inventoriesStorageDTO.getWarehouse().getName() != null
                ? inventoriesStorageDTO.getWarehouse().getCode() + " - " + inventoriesStorageDTO.getWarehouse().getName()
                : "";
        this.note = inventoriesStorageDTO.getNotes() != null ? inventoriesStorageDTO.getNotes() : "";
        this.itemCategory = inventoriesStorageDTO.getItem() != null && inventoriesStorageDTO.getItem().getItemCategory() != null
                ? inventoriesStorageDTO.getItem().getItemCategory().getCode()
                : "";
        this.taxCode = inventoriesStorageDTO.getItem() != null ? inventoriesStorageDTO.getItem().getSupplier().getTaxCode() : "";
        this.reasonImport = inventoriesStorageDTO.getItemInfo() != null ? inventoriesStorageDTO.getItemInfo().getReasonForRemoval() : "";
        this.price = inventoriesStorageDTO.getPrice() != null ? inventoriesStorageDTO.getPrice() : BigDecimal.ZERO;
        this.priceCurrency = inventoriesStorageDTO.getRemainingPrice() != null ? inventoriesStorageDTO.getRemainingPrice() : BigDecimal.ZERO;
        this.quantityCurrent = inventoriesStorageDTO.getQuantity() != null ? inventoriesStorageDTO.getQuantity() : BigDecimal.ZERO;
        this.status = inventoriesStorageDTO.getStatus() != null ? inventoriesStorageDTO.getStatus().getVietnameseName() : "";
    }
}
