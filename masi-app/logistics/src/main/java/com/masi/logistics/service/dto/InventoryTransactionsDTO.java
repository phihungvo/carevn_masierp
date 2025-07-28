package com.masi.logistics.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.masi.logistics.domain.enumeration.InventoryTransactionStatus;
import com.masi.logistics.domain.enumeration.TransactionType;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.UUID;

/**
 * A DTO for the {@link com.masi.logistics.domain.InventoryTransactions} entity.
 */
@Data
@SuppressWarnings("common-java:DuplicatedBlocks")
public class InventoryTransactionsDTO implements Serializable {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID id;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String code;

    private InventoryTransactionStatus status;

    private ZonedDateTime transactionDate;

    private ZonedDateTime invoiceDate;

    private UUID employeeId;

    private UUID receiverId;

    private UUID driverId;

    private String deliveryLocation;

    private BigDecimal totalPrice;

    private Integer vehicleNumber;

    private Integer trailerNumber;

    private TransactionType transactionType;

    private UUID warehouseId;

    private UUID supplierId;

    private UUID customerId;

    private UUID orderId;

    private UUID manufactureId;

    private String notes;

    private String purpose;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ZonedDateTime createAt;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String createBy;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ZonedDateTime updateAt;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String updateBy;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ZonedDateTime deleteAt;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String deleteBy;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String company;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String department;

    private Collection<InventoryTransactionDetailsDTO> inventoryTransactionDetails;

    private Collection<InventoryCheckDTO> inventoryChecks;

    //setter auto count price from inventoryTransactionDetails:
    public void setInventoryTransactionDetails(Collection<InventoryTransactionDetailsDTO> inventoryTransactionDetails) {
        this.inventoryTransactionDetails = inventoryTransactionDetails;
        setTotalPrice(inventoryTransactionDetails);
    }

    private void setTotalPrice(Collection<InventoryTransactionDetailsDTO> inventoryTransactionDetails) {
        if (inventoryTransactionDetails != null) {
            BigDecimal totalPrice = BigDecimal.ZERO;
            for (InventoryTransactionDetailsDTO inventoryTransactionDetail : inventoryTransactionDetails) {
                totalPrice = totalPrice.add(inventoryTransactionDetail.getUnitPrice().multiply(BigDecimal.valueOf(inventoryTransactionDetail.getQuantity())));
            }
            this.totalPrice = totalPrice;
        }
        else
        {
            this.totalPrice = BigDecimal.ZERO;
        }
    }

}
