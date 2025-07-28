package com.masi.logistics.service.exportDTO;

import com.carevn.masi.utils.CSV.CSVColumn;
import com.masi.logistics.domain.enumeration.StatusEntity;
import com.masi.logistics.service.dto.InventoriesTypeDTO;
import com.masi.logistics.service.dto.SupplierContractDTO;
import com.masi.logistics.service.dto.SuppliersDTO;
import com.masi.logistics.service.dto.WarehouseDTO;
import lombok.Data;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Data
public class invertories_CE_DTO_Export {
    @CSVColumn(name = "Mã Phiếu")
    private String code;

    @CSVColumn(name = "Ngày xuất")
    private String deliveryDate;

    @CSVColumn(name = "Kho")
    private String incomingWarehouse;

    @CSVColumn(name = "Khách hàng")
    private String customer;

    @CSVColumn(name = "Người nhận")
    private String receiver;

    @CSVColumn(name = "Địa chỉ")
    private String address;

    @CSVColumn(name = "Ghi chú")
    private String note;

    @CSVColumn(name = "Tổng tiền")
    private BigDecimal totalAmount;

    @CSVColumn(name = "Trạng thái")
    private String status;

    public invertories_CE_DTO_Export(String code, LocalDate deliveryDate, InventoriesTypeDTO inventoriesType, SuppliersDTO customer,
                                     WarehouseDTO incomingWarehouse, BigDecimal totalQuantity, BigDecimal totalAmount,
                                     SupplierContractDTO purchaseContract, StatusEntity status, String receiver, String address, String note) {
        this.code = code != null ? code : "";

        this.deliveryDate = deliveryDate != null
                ? deliveryDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                : "";
//        this.inventoriesType = inventoriesType != null ? inventoriesType.getCode() + " - " + incomingWarehouse.getName() : "";

        this.customer = customer != null ? customer.getCode() + " - " + customer.getName() : "";
        this.incomingWarehouse = incomingWarehouse != null ? incomingWarehouse.getCode() + " - " + incomingWarehouse.getName() : "";
        //this.totalQuantity = totalQuantity != null ? totalQuantity : BigDecimal.ZERO;

        this.totalAmount = totalAmount != null
                ? totalAmount.setScale(0, RoundingMode.FLOOR)
                : BigDecimal.ZERO;

//        if (purchaseContract != null) {
//            String contractCode = Optional.ofNullable(purchaseContract.getContractCode()).orElse("");
//            String contractName = Optional.ofNullable(purchaseContract.getContractName()).orElse("");
//            this.purchaseContract = contractCode.isEmpty() || contractName.isEmpty()
//                    ? contractCode + contractName
//                    : contractCode + " - " + contractName;
//        } else {
//            this.purchaseContract = "";
//        }

        this.receiver = receiver != null ? receiver : "";
        this.address = address != null ? address : "";
        this.note = note != null ? note : "";


        this.status = status != null ? status.getVietnameseName() : "";
    }

}
