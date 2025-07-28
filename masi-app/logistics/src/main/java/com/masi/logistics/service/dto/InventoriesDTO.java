package com.masi.logistics.service.dto;

import com.carevn.masi.dto.EmployeeDTO;
import com.carevn.masi.utils.CSV.CSVColumn;
import com.carevn.masi.utils.PgJsonObjectDeserializer;
import com.carevn.masi.utils.PgJsonObjectSerializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.masi.logistics.domain.IncomingInvoice;
import com.masi.logistics.domain.Inventories;
import com.masi.logistics.domain.SupplierContract;
import com.masi.logistics.domain.WarehouseType;
import com.masi.logistics.domain.enumeration.StatusEntity;
import com.masi.logistics.domain.enumeration.WarehouseGroupType;
import io.r2dbc.postgresql.codec.Json;
import jakarta.validation.constraints.*;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link com.masi.logistics.domain.Inventories} entity.
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
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private InventoriesTypeDTO inventoriesType; // Add InventoriesTypeDTO

    private LocalDate dateCreate;

    private UUID customerId;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private SuppliersDTO customer; // Add SuppliersDTO

    private UUID customerRecipientId;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private SuppliersDTO customerRecipient; // Add SuppliersDTO

    private UUID invoiceId;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private IncomingInvoice invoice; // Add InvoiceSupplies
    private List<IncomingInvoice> invoices;

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
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<OrderSaleDTO> orders;


    private UUID contractId;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<ContractSaleDTO> contracts;

    private UUID supplierRequestId;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private SuppliesRequestDTO supplierRequest; // Add SuppliesRequestDTO

    private String taxCode;

    private String series;

    private String currencyCodeRate;

    private BigDecimal exchangeRate;

    private Boolean isEmptiness;

    private UUID emptinessId;

    private Collection<InventoriesDetailDTO> inventoriesDetails;

    private UUID purchaseContractId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private SupplierContractDTO purchaseContract; // Add ContactDTO

    private UUID productionId;

    private WarehouseType warehouseType;

    private WarehouseGroupType warehouseGroupType;

    private UUID paymentRequestId;
    private PaymentRequestDTO paymentRequest;

    private Boolean isReview;

    private BigDecimal TotalQuantity;

    private Collection<RequestApprovalDTO> requestApprovals;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private EmployeeDTO createdByEmployee;

    @JsonSerialize(using = PgJsonObjectSerializer.class)
    @JsonDeserialize(using = PgJsonObjectDeserializer.class)
    private Json file;

    @JsonSerialize(using = PgJsonObjectSerializer.class)
    @JsonDeserialize(using = PgJsonObjectDeserializer.class)
    private Json attribute;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private StatusEntity status;

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

    private String companyImport;

    private Boolean isOrderManu;
    private Boolean isProductStanding;

    public Inventories toEntity() {
        Inventories inventories = new Inventories();
        inventories.setId(this.id);  // Nếu cần set ID, tùy thuộc vào context.
        inventories.setCode(this.code);
        inventories.setReceiverUserId(this.receiverUserId);
        inventories.setDeliveryDate(this.deliveryDate);
        inventories.setInventoriesTypeId(this.inventoriesTypeId);
        inventories.setDateCreate(this.dateCreate);
        inventories.setCustomerId(this.customerId);
        inventories.setCustomerRecipientId(this.customerRecipientId);
        inventories.setInvoiceId(this.invoiceId);
//        inventories.setIsInvoice(this.isInvoice);
        inventories.setAddress(this.address);
        inventories.setNote(this.note);
        inventories.setPurchasePrice(this.purchasePrice);
        inventories.setSalePrice(this.salePrice);
        inventories.setTotalAmount(this.totalAmount);
        inventories.setInputDepartmentId(this.inputDepartmentId);
        inventories.setIncomingWarehouseId(this.incomingWarehouseId);
        inventories.setOutgoingWarehouseId(this.outgoingWarehouseId);
        inventories.setEmployeeId(this.employeeId);
        inventories.setOrderId(this.orderId);
        inventories.setSupplierRequestId(this.supplierRequestId);
        inventories.setTaxCode(this.taxCode);
        inventories.setSeries(this.series);
        inventories.setCurrencyCodeRate(this.currencyCodeRate);
        inventories.setExchangeRate(this.exchangeRate);
        inventories.setIsEmptiness(this.isEmptiness);
        inventories.setEmptinessId(this.emptinessId);
//        inventories.setInventoriesDetails(this.inventoriesDetails);
        inventories.setPurchaseContractId(this.purchaseContractId);
        inventories.setProductionId(this.productionId);
//        inventories.setWarehouseType(this.warehouseType);
        inventories.setIsReview(this.isReview);
        inventories.setFile(this.file);
        inventories.setAttribute(this.attribute);
        inventories.setStatus(this.status);
        inventories.setIsDeleted(this.isDeleted);
        return inventories;
    }

//    {
//        "shipper": "nguyễn văn a",
//        "receiverName": "trần thị phương vy 1",
//        "receiverPhone": "0858840839",
//        "shipperAddress": "dương quảng hàm "
//    }

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


}
