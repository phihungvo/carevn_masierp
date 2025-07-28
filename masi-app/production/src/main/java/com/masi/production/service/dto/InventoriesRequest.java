package com.masi.production.service.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class InventoriesRequest {
    private String code;
    private UUID incomingWarehouseId;
    private UUID inventoriesTypeId;
    private ZonedDateTime dateCreate;
    private String createdByName;
    private UUID employeeId;
    private String workspaceName;
    private Boolean isNoReview;

    private ZonedDateTime createdAt;
    private UUID customerId;
    private String shipper;
    private String shipperPhone;
    private List<InventoryDetail> inventoriesDetails;
    private UUID productionId;
    private BigDecimal totalAmount;
    private Float totalQuantity;
    private String status;
    private String warehouseGroupType;
    private Boolean isReview;
    private String companyImport;
    private Boolean isOrderManu;
    private Boolean isProductStanding;
    @Data
    public static class InventoryDetail {
        private String code;
        private UUID itemId;
        private Float quantity;
        private BigDecimal price;
        private String note;
        private BigDecimal totalPrice;
        private Float ProteinPercentageApply;
        private Boolean isDefaultItem;

    }


}
