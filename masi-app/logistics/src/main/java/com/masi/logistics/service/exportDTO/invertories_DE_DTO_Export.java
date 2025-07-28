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
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Data
public class invertories_DE_DTO_Export {
    @CSVColumn(name = "Mã Phiếu")
    private String code;

    @CSVColumn(name = "Ngày xuất")
    private String deliveryDate;

    @CSVColumn(name = "Kho")
    private String incomingWarehouse;

    @CSVColumn(name = "Nơi nhận")
    private String receiverAddress;

    @CSVColumn(name = "Số lượng")
    private BigDecimal totalQuantity;

    @CSVColumn(name = "Thành tiền")
    private BigDecimal totalAmount;

    @CSVColumn(name = "Ghi chú")
    private String note;

    @CSVColumn(name = "Trạng thái")
    private String status;

    private ZonedDateTime createdAt;

    public invertories_DE_DTO_Export(String code,
                                     LocalDate deliveryDate,
                                     WarehouseDTO incomingWarehouse,
                                     BigDecimal purchasePrice,
                                     BigDecimal totalAmount,
                                     StatusEntity status,
                                     String receiverAddress,
                                     String note,
                                     ZonedDateTime createdAt) {
        this.code = code != null ? code : "";

        this.deliveryDate = deliveryDate != null
                ? deliveryDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                : "";
//        this.inventoriesType = inventoriesType != null ? inventoriesType.getCode() + " - " + incomingWarehouse.getName() : "";
//
//        this.customer = customer != null ? customer.getCode() + " - " + customer.getName() : "";
        this.incomingWarehouse = incomingWarehouse != null ? incomingWarehouse.getCode() + " - " + incomingWarehouse.getName() : "";
        this.totalQuantity = purchasePrice != null ? purchasePrice : BigDecimal.ZERO;

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


        this.status = status != null ? status.getVietnameseName() : "";
        this.receiverAddress = receiverAddress != null ? receiverAddress : "";
        this.note = note != null ? note : "";
    }

}
