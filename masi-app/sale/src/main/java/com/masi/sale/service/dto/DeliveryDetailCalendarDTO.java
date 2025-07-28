package com.masi.sale.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.relational.core.mapping.Column;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class DeliveryDetailCalendarDTO {
    @Column("id")
    private UUID id;

    @Column("item_id")
    private UUID itemId;

    @Column("logistic_item")
    private UUID logisticItem;

    private String itemCode;

    @Column("item_name")
    private String itemName;

    @Column("order_id")
    private UUID orderId;

    @Column("order_code")
    private String orderCode;

    @Column("contract_id")
    private UUID contractId;

    @Column("expected_quantity")
    private BigDecimal expectedQuantity;

    @Column("contract_quantity")
    private BigDecimal contractQuantity;

    @Column("received")
    private BigDecimal received;

    @Column("remain")
    private BigDecimal remain;

    @Column("planing_import")
    private BigDecimal planningImport;

    @Column("outstanding_quantity")
    private BigDecimal outstandingQuantity;

    @Column("contract_name")
    private String contractName;

    @Column("created_at")
    private ZonedDateTime createdAt;

    @Column("delivery_date")
    private ZonedDateTime deliveryDate;

    @Column("address")
    private String address;

    @Column("note")
    private String note;

    @Column("actual_delivery_date")
    private LocalDate actualDeliveryDate;

    @Column("customer_id")
    private UUID customerId;

    @Column("customer_code")
    private String customerCode;

    @Column("customer_name")
    private String customerName;

    @Column("actual_quantity")
    private BigDecimal actualQuantity;
}
