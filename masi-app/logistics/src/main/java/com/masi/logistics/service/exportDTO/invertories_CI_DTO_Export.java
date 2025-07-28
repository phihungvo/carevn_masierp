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
public class invertories_CI_DTO_Export {
    @CSVColumn(name = "Mã Phiếu")
    private String code;

    @CSVColumn(name = "Ngày Nhập Kho")
    private String deliveryDate;

    @CSVColumn(name = "Loại Kho")
    private String inventoriesType;

    @CSVColumn(name = "NCC")
    private String customer;

    @CSVColumn(name = "Kho nhập")
    private String incomingWarehouse;

    @CSVColumn(name = "Tổng số lượng")
    private BigDecimal totalQuantity;

    @CSVColumn(name = "Tổng tiền")
    private BigDecimal totalAmount;

    @CSVColumn(name = "Hợp Đồng")
    private String purchaseContract;

    @CSVColumn(name = "Trạng thái")
    private String status;


    public invertories_CI_DTO_Export(String code, LocalDate deliveryDate, InventoriesTypeDTO inventoriesType, SuppliersDTO customer,
                                     WarehouseDTO incomingWarehouse, BigDecimal purchasePrice, BigDecimal totalAmount,
                                     SupplierContractDTO purchaseContract, StatusEntity status, BigDecimal totalQuantity) {
        this.code = code != null ? code : "";

        this.deliveryDate = deliveryDate != null
                ? deliveryDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                : "";
        this.inventoriesType = inventoriesType != null ? inventoriesType.getCode() + " - " + incomingWarehouse.getName() : "";

        this.customer = customer != null ? customer.getCode() + " - " + customer.getName() : "";
        this.incomingWarehouse = incomingWarehouse != null ? incomingWarehouse.getCode() + " - " + incomingWarehouse.getName() : "";
//        this.purchasePrice = purchasePrice != null ? purchasePrice : BigDecimal.ZERO;

        this.totalAmount = totalAmount != null
                ? totalAmount.setScale(0, RoundingMode.FLOOR)
                : BigDecimal.ZERO;

        if (purchaseContract != null) {
            String contractCode = Optional.ofNullable(purchaseContract.getContractCode()).orElse("");
            String contractName = Optional.ofNullable(purchaseContract.getContractName()).orElse("");
            this.purchaseContract = contractCode.isEmpty() || contractName.isEmpty()
                    ? contractCode + contractName
                    : contractCode + " - " + contractName;
        } else {
            this.purchaseContract = "";
        }


        this.status = status != null ? status.getVietnameseName() : "";

        this.totalQuantity = totalQuantity != null ? totalQuantity : BigDecimal.ZERO;

    }

}
