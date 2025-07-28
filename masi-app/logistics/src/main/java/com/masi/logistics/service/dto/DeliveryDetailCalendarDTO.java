package com.masi.logistics.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.relational.core.mapping.Column;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * A DTO for the {@link com.masi.logistics.domain.DeliveryDetail} entity.
 */
@Data
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DeliveryDetailCalendarDTO implements Serializable {

    @Column("id")
    private UUID id;

    @Column("item_id")
    private UUID itemId;

    @Column("item_code")
    private String itemCode;

    @Column("item_name")
    private String itemName;

    @Column("supplier_id")
    private UUID supplierId;

    @Column("supplier_code")
    private String supplierCode;

    @Column("supplier_name")
    private String supplierName;

    @Column("contract_code")
    private String contractCode;

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

    @Column("delivery_date")
    private LocalDate deliveryDate;

    @Column("expected_quantity")
    private BigDecimal expectedQuantity;

    @Column("actual_quantity")
    private BigDecimal actualQuantity;

    @Column("difference_quantity")
    private BigDecimal differenceQuantity;

    @Column("created_at")
    private ZonedDateTime createdAt;

    @Column("actual_delivery_date")
    private LocalDate actualDeliveryDate;

    @Column("address")
    private String address;

    @Column("note")
    private String note;

    public DeliveryDetailCalendarDTO(UUID itemId, LocalDate deliveryDate, BigDecimal expectedQuantity) {
        this.itemId = itemId;
        this.deliveryDate = deliveryDate;
        this.expectedQuantity = expectedQuantity;
    }
}
