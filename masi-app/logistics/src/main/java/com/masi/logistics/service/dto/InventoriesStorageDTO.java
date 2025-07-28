package com.masi.logistics.service.dto;

import com.carevn.masi.utils.PgJsonObjectDeserializer;
import com.carevn.masi.utils.PgJsonObjectSerializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.masi.logistics.domain.InventoriesStorage;
import com.masi.logistics.domain.enumeration.ItemStatus;
import com.masi.logistics.domain.enumeration.ItemType;
import io.r2dbc.postgresql.codec.Json;
import jakarta.persistence.Lob;
import lombok.*;
import org.springframework.data.relational.core.mapping.Column;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.UUID;

/**
 * A DTO for the {@link com.masi.logistics.domain.InventoriesStorage} entity.
 */
@Data
@SuppressWarnings("common-java:DuplicatedBlocks")
public class InventoriesStorageDTO implements Serializable {

    private UUID id;

    private String code;

    private UUID itemId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ItemDTO item;

    private UUID inventoriesDetailId;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private InventoriesDetailDTO inventoriesDetail;

    private ZonedDateTime importDate;

    private ZonedDateTime exportDate;

    private String depreciation;

    private ZonedDateTime expiryDate;

    private BigDecimal totalQuantity;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String itemCode;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String itemName;

    private ItemType itemType;

    private BigDecimal quantity;

    private ItemStatus status;

    private BigDecimal price;

    private BigDecimal remainingPrice;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private BigDecimal accumulated;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private BigDecimal depreciationRate;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private BigDecimal depreciationValue;

    private String uomName;

    private String uomId;
    @Lob
    private String notes;

    @JsonSerialize(using = PgJsonObjectSerializer.class)
    @JsonDeserialize(using = PgJsonObjectDeserializer.class)
    private Json attribute;

    private ItemInfoDTO itemInfo;

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

    private String department;

    private UUID warehouseId;
    private WarehouseDTO warehouse;

    private Collection<ItemAssetTransferDTO> itemAssetTransfers;

    private Collection<ItemAssetDepreciationDetailDTO> itemAssetDepreciationDetails;

    @JsonSerialize(using = PgJsonObjectSerializer.class)
    @JsonDeserialize(using = PgJsonObjectDeserializer.class)
    private Json assetLogs;

    private UUID supplierId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private SuppliersDTO supplier;

    private Boolean isOrderManu;

    public ItemAssetDepreciationDetailDTO toItemAssetDepreciationDTO() {
        ItemAssetDepreciationDetailDTO itemAssetDepreciationDTO = new ItemAssetDepreciationDetailDTO();
        itemAssetDepreciationDTO.setId(UUID.randomUUID());
        itemAssetDepreciationDTO.setInventoriesStorageId(this.id);
        itemAssetDepreciationDTO.setNote(this.notes);
        itemAssetDepreciationDTO.setCostInformation("");
        itemAssetDepreciationDTO.setAmortizedCostInformation("");
        itemAssetDepreciationDTO.setAmortizationAmount(depreciationValue);
        itemAssetDepreciationDTO.setAmortizationRate(depreciationRate);
        itemAssetDepreciationDTO.setAccumulatedAmortizationAmount(accumulated);
        itemAssetDepreciationDTO.setRecipe("DEFAULT");

        return itemAssetDepreciationDTO;
    }

    public static String addTransaction(String json, InventoriesStorage.Transaction transaction) throws Exception {
        // Khởi tạo ObjectMapper
        ObjectMapper mapper = new ObjectMapper();

        // Parse JSON string thành JsonNode
        JsonNode rootNode = mapper.readTree(json);

        // Kiểm tra sự tồn tại của trường "transactions"
        if (!rootNode.has("transactions") || !rootNode.get("transactions").isArray()) {
            rootNode = mapper.createObjectNode();
            ((ObjectNode) rootNode).putArray("transactions");
        }

        // Lấy mảng transactions
        ArrayNode transactions = (ArrayNode) rootNode.get("transactions");


        // Tạo đối tượng transaction mới
        ObjectNode newTransaction = mapper.createObjectNode();
        newTransaction.put("code", transaction.code());
        newTransaction.put("date", String.valueOf(transaction.date()));
        newTransaction.put("type", transaction.type().name());
        newTransaction.put("costInfo", transaction.costInfo());
        newTransaction.put("quantity", transaction.quantity());
        newTransaction.put("basePrice", transaction.basePrice());
        newTransaction.put("deprecation", transaction.deprecation());
        newTransaction.put("description", transaction.description());
        newTransaction.put("monthDeprecation", transaction.monthDeprecation());

        // Thêm đối tượng mới vào mảng transactions
        transactions.add(newTransaction);

        // Chuyển đổi JsonNode trở lại JSON string
        return mapper.writeValueAsString(rootNode);
    }
}
