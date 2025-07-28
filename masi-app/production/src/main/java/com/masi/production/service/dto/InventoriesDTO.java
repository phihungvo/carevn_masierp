package com.masi.production.service.dto;

import com.carevn.masi.dto.EmployeeDTO;
import com.carevn.masi.utils.PgJsonObjectDeserializer;
import com.carevn.masi.utils.PgJsonObjectSerializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.r2dbc.postgresql.codec.Json;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.UUID;

/**
 * A DTO for the {@link com.masi.logistics.domain} entity.
 */
@Data
@SuppressWarnings("common-java:DuplicatedBlocks")
public class InventoriesDTO implements Serializable {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID id;

    private String code;

    private UUID receiverUserId;

    private LocalDate deliveryDate;

    private UUID inventoriesTypeId;

    private LocalDate dateCreate;

    private UUID customerId;

    private UUID customerRecipientId;

    private UUID invoiceId;

    private Boolean isInvoice;

    private String address;

    private String note;

    private UUID incomingWarehouseId;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private WarehouseDTO incomingWarehouse; // Add Warehouse

    private UUID outgoingWarehouseId;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private WarehouseDTO outgoingWarehouse; // Add Warehouse

    private BigDecimal purchasePrice;

    private BigDecimal salePrice;

    private BigDecimal totalAmount;

    private UUID inputDepartmentId;

    private UUID employeeId;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private EmployeeDTO employee;

    private UUID orderId;

    private UUID manufacturingOrderId;

    private UUID contractId;

    private UUID supplierRequestId;

    private String taxCode;

    private String series;

    private String currencyCodeRate;

    private BigDecimal exchangeRate;

    private Boolean isEmptiness;

    private UUID emptinessId;

    private Collection<InventoriesDetailDTO> inventoriesDetails;

    private UUID purchaseContractId;

    private UUID productionId;

    private UUID paymentRequestId;

    private Boolean isReview;

    private BigDecimal TotalQuantity;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private EmployeeDTO createdByEmployee;

    @JsonSerialize(using = PgJsonObjectSerializer.class)
    @JsonDeserialize(using = PgJsonObjectDeserializer.class)
    private Json file;

    @JsonSerialize(using = PgJsonObjectSerializer.class)
    @JsonDeserialize(using = PgJsonObjectDeserializer.class)
    private Json attribute;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Boolean isDeleted;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ZonedDateTime createdAt;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String createdBy;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ZonedDateTime updatedAt;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String updatedBy;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ZonedDateTime deletedAt;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String deletedBy;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String company;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String department;

    private String receivedWorkspace;

    private WarehouseGroupType warehouseGroupType;

    public String getAttributeValue(String key) {
        try {
            if (attribute != null) {
                String jsonString = new String(attribute.asArray(), StandardCharsets.UTF_8);
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(jsonString);
                return jsonNode.path(key).asText();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ""; // or a default value
    }
    public enum WarehouseGroupType {
        WAREHOUSE_COMMERCE_EXPORT,
        WAREHOUSE_DEPRECIATION_EXPORT,
        WAREHOUSE_DEPRECIATION_IMPORT,
        WAREHOUSE_COMMERCE_IMPORT // nhap kho binh thuong
    }

}
