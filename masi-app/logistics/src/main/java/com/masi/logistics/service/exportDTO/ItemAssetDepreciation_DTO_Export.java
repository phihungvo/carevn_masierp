package com.masi.logistics.service.exportDTO;

import com.carevn.masi.utils.CSV.CSVColumn;
import com.masi.logistics.domain.ItemAssetDepreciation;
import com.masi.logistics.service.dto.ItemAssetDepreciationDTO;
import lombok.Data;

import java.time.format.DateTimeFormatter;

@Data
public class ItemAssetDepreciation_DTO_Export {
    @CSVColumn(name = "Kỳ phân bố")
    private String code;

    @CSVColumn(name = "Ngày tính phân bố")
    private String dateOfDepreciation;

    @CSVColumn(name = "Người tính")
    private String employee;

    @CSVColumn(name = "Diễn giải")
    private String note;

    @CSVColumn(name = "Trạng thái")
    private String status;

    public ItemAssetDepreciation_DTO_Export(ItemAssetDepreciationDTO itemAssetDepreciationDTO) {
        this.code = itemAssetDepreciationDTO.getCode() != null ? itemAssetDepreciationDTO.getCode() : "";
        this.dateOfDepreciation = itemAssetDepreciationDTO.getDepreciationDate() != null
                ? itemAssetDepreciationDTO.getDepreciationDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                : "";
        this.employee = itemAssetDepreciationDTO.getEmployee() != null
                ? itemAssetDepreciationDTO.getEmployee().getEmployeeCode() + " - " + itemAssetDepreciationDTO.getEmployee().getFullName()
                : "";
        this.note = itemAssetDepreciationDTO.getDescription() != null ? itemAssetDepreciationDTO.getDescription() : "";
        this.status = itemAssetDepreciationDTO.getStatus() != null ? itemAssetDepreciationDTO.getStatus().getVietnameseName() : "";
    }


}
