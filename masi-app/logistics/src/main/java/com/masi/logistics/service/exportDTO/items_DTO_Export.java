package com.masi.logistics.service.exportDTO;

import com.carevn.masi.utils.CSV.CSVColumn;
import com.masi.logistics.service.dto.ItemDTO;
import lombok.Data;

import java.text.DecimalFormat;

@Data
public class items_DTO_Export {

    @CSVColumn(name = "Mã hàng hóa")
    private String code;

    @CSVColumn(name = "Tên hàng hóa")
    private String name;

    @CSVColumn(name = "Nhóm")
    private String groupItem;

    @CSVColumn(name = "Loại")
    private String typeItem;

    @CSVColumn(name = "ĐVT")
    private String uom;

    @CSVColumn(name = "Đơn giá")
    private String unitPrice;

    @CSVColumn(name = "Trạng thái")
    private String status;

    public items_DTO_Export(ItemDTO itemDTO) {
        this.code = itemDTO != null && itemDTO.getCode() != null ? itemDTO.getCode() : "";
        this.name = itemDTO != null && itemDTO.getName() != null ? itemDTO.getName() : "";
        this.typeItem = itemDTO != null && itemDTO.getItemTypes() != null && itemDTO.getItemTypes().getName() != null
                ? itemDTO.getItemTypes().getName() : "";
        this.groupItem = itemDTO != null && itemDTO.getItemCategory() != null && itemDTO.getItemCategory().getName() != null
                ? itemDTO.getItemCategory().getName() : "";
        this.uom = itemDTO != null && itemDTO.getUom() != null && itemDTO.getUom().getName() != null
                ? itemDTO.getUom().getName() : "";

        DecimalFormat decimalFormat = new DecimalFormat("#.#");
        this.unitPrice = itemDTO != null && itemDTO.getUnitPrice() != null
                ? decimalFormat.format(itemDTO.getUnitPrice())
                : "0";

        // Kiểm tra trạng thái
        this.status = itemDTO != null && itemDTO.getIsActive() != null
                ? itemDTO.getIsActive() ? "Hoạt động" : "Ngừng hoạt động" : "Không xác định";
    }
}
