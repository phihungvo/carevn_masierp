package com.masi.logistics.service.exportDTO;

import com.carevn.masi.utils.CSV.CSVColumn;
import com.masi.logistics.domain.enumeration.StatusEntity;
import com.masi.logistics.service.dto.ItemAssetDepreciationDTO;
import com.masi.logistics.service.dto.ItemAssetTransferDTO;
import lombok.Data;

import java.time.format.DateTimeFormatter;

@Data
public class ItemAssetTransferExportDTO {
    @CSVColumn(name = "Mã điều chuyển")
    private String code;

    @CSVColumn(name = "Ngày xuất")
    private String transferDate;

    @CSVColumn(name = "Loại phát sinh")
    private String transactionType;

    @CSVColumn(name = "Diễn giải")
    private String note;

    @CSVColumn(name = "Trạng thái")
    private String status;

    public ItemAssetTransferExportDTO(ItemAssetTransferDTO itemAssetTransferDTO) {
        this.code = itemAssetTransferDTO.getCode() != null ? itemAssetTransferDTO.getCode() : "";
        this.transferDate = itemAssetTransferDTO.getTransferDate() != null
                ? itemAssetTransferDTO.getTransferDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                : "";
        this.transactionType = itemAssetTransferDTO.getTransactionType() != null ? itemAssetTransferDTO.getTransactionType().getCode() + " - " + itemAssetTransferDTO.getTransactionType().getName() : "";
        this.note = itemAssetTransferDTO.getDescription() != null ? itemAssetTransferDTO.getDescription() : "";
        if (itemAssetTransferDTO.getStatus() != null) {
            if (itemAssetTransferDTO.getStatus().equals(StatusEntity.COMPLETED))
                this.status = "Hoàn tất";
            else if (itemAssetTransferDTO.getStatus().equals(StatusEntity.NEW))
                this.status = "Mới";
            else if (itemAssetTransferDTO.getStatus().equals(StatusEntity.CANCELLED))
                this.status = "Hủy";
            else
                this.status = "";
        }
    }
}
