package com.masi.logistics.service.exportDTO;

import com.carevn.masi.utils.CSV.CSVColumn;
import com.masi.logistics.domain.enumeration.StatusEntity;
import com.masi.logistics.service.dto.InventoriesCheckDTO;
import com.masi.logistics.service.dto.InventoriesDTO;
import lombok.Data;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.format.DateTimeFormatter;

@Data
public class inventories_DTO_Export {
    @CSVColumn(name = "Mã kiểm kê")
    private String inventoryCode;

    @CSVColumn(name = "Ngày kiểm")
    private String inventoryDate;

    @CSVColumn(name = "Kho")
    private String wareHouseName;

    @CSVColumn(name = "Số lượng chênh lệch")
    private BigDecimal inventoryValue;

    @CSVColumn(name = "Ghi chú")
    private String inventoryNote;

    @CSVColumn(name = "Trạng thái")
    private String status;

    public inventories_DTO_Export(InventoriesCheckDTO inventoriesCheckDTO) {
        this.inventoryCode = inventoriesCheckDTO.getCode() != null ? inventoriesCheckDTO.getCode() : "";

        // Format ngày tháng theo "dd/MM/yyyy"
        this.inventoryDate = inventoriesCheckDTO.getCheckDate() != null
                ? inventoriesCheckDTO.getCheckDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                : "";

        this.wareHouseName = inventoriesCheckDTO.getWarehouse().getName() != null
                ? inventoriesCheckDTO.getWarehouse().getCode() + " - " + inventoriesCheckDTO.getWarehouse().getName()
                : "";

        this.inventoryValue = inventoriesCheckDTO.getAmountOfDifference() != null
                ? inventoriesCheckDTO.getAmountOfDifference().setScale(0, RoundingMode.FLOOR)
                : BigDecimal.ZERO;

        this.inventoryNote = inventoriesCheckDTO.getNote() != null ? inventoriesCheckDTO.getNote() : "";

        this.status = inventoriesCheckDTO.getStatus() != null
                ? inventoriesCheckDTO.getStatus().getVietnameseName()
                : "";
    }

}
