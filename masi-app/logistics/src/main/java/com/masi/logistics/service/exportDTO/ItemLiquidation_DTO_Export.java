package com.masi.logistics.service.exportDTO;

import com.carevn.masi.utils.CSV.CSVColumn;
import com.masi.logistics.service.dto.ItemAssetDepreciationDTO;
import com.masi.logistics.service.dto.ItemLiquidationDTO;
import lombok.*;

import java.time.format.DateTimeFormatter;

@Data
public class ItemLiquidation_DTO_Export {
    @CSVColumn(name = "Số tham chiếu")
    private String code;

    @CSVColumn(name = "Ngày thanh lý")
    private String dateOfDepreciation;

    @CSVColumn(name = "Lý do thanh lý")
    private String employee;

    @CSVColumn(name = "Diễn giải")
    private String note;

    @CSVColumn(name = "Trạng thái")
    private String status;


    public ItemLiquidation_DTO_Export(ItemLiquidationDTO itemAssetDepreciationDTO) {
        this.code = itemAssetDepreciationDTO.getCode() != null ? itemAssetDepreciationDTO.getCode() : "";
        this.dateOfDepreciation = itemAssetDepreciationDTO.getLiquidationDate() != null
                ? itemAssetDepreciationDTO.getLiquidationDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                : "";
        this.employee = itemAssetDepreciationDTO.getReason() != null
                ? itemAssetDepreciationDTO.getReason().getVietnameseValue()
                : "";
        this.note = itemAssetDepreciationDTO.getDescription() != null ? itemAssetDepreciationDTO.getDescription() : "";
        this.status = itemAssetDepreciationDTO.getStatus() != null ? itemAssetDepreciationDTO.getStatus().getVietnameseName() : "";
    }
}
