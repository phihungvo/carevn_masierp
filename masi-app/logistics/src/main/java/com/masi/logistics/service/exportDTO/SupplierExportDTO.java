package com.masi.logistics.service.exportDTO;

import com.carevn.masi.utils.CSV.CSVColumn;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SupplierExportDTO {

    @CSVColumn(name = "Mã NCC")
    private String code;

    @CSVColumn(name = "Tên NCC")
    private String name;

    @CSVColumn(name = "Địa chỉ")
    private String address;

    @CSVColumn(name = "MST")
    private String taxCode;

    @CSVColumn(name = "Người đại diện")
    private String fullName;

    @CSVColumn(name = "Tel")
    private String tel;

    @CSVColumn(name = "Email")
    private String email;

    @CSVColumn(name = "Hạn thanh toán")
    private String paymentTerm;

    @CSVColumn(name = "Trạng thái")
    private String status;


}
