package com.masi.logistics.service.dto;

import com.carevn.masi.utils.PgJsonObjectDeserializer;
import com.carevn.masi.utils.PgJsonObjectSerializer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.masi.logistics.domain.Item;
import com.masi.logistics.domain.enumeration.ItemType;
import io.r2dbc.postgresql.codec.Json;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.data.relational.core.mapping.Column;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.util.UUID;

/**
 * A DTO for the {@link com.masi.logistics.domain.Item} entity.
 */
@Data
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ItemDTO implements Serializable {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID id;

    private String code;

    private String name;

    private UUID uomId;

    private UomDTO uom;

    @JsonSerialize(using = PgJsonObjectSerializer.class)
    @JsonDeserialize(using = PgJsonObjectDeserializer.class)
    private Json attribute;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String company;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String department;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Boolean isDeleted;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String createdBy;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ZonedDateTime createdDate;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String updatedBy;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ZonedDateTime updatedAt;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String deletedBy;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ZonedDateTime deletedAt;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(allOf = {ItemCategoryDTO.class})
    private ItemCategoryDTO itemCategory;

    private UUID itemCategoryId;

    private UUID itemTypeId;
    private ItemTypeDTO itemTypes;

    private Float percentProtein ;

    private String notes;

    private Float vatRate;

    private UUID vatId;

    private Float unitPrice;

    private UUID revenueGroupId;
    private ItemCategoryDTO revenueGroup;

    private UUID supplierId;

    private SuppliersDTO supplier;

    private ItemType itemType;

    private Boolean isActive;

    private Boolean isSeparation;

    public Float getPercentProtein() {
        try {
            if (attribute != null) {
                // Chuyển Json.JsonByteArrayInput sang chuỗi JSON
                String jsonString = new String(attribute.asArray(), StandardCharsets.UTF_8);

                // Sử dụng ObjectMapper để phân tích chuỗi JSON
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(jsonString);

                // Truy cập vào "material.percent_protein"
                JsonNode materialNode = jsonNode.path("material");
                if (materialNode.has("percent_protein")) {
                    return (float) materialNode.get("percent_protein").asDouble();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return percentProtein;  // Giá trị mặc định nếu không tìm thấy
    }

    // Setter - Đặt giá trị cho key "material.percent_protein"
    public void setPercentProtein(Float percentProtein) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            ObjectNode rootNode;

            // Nếu attribute đã có giá trị, cập nhật nó
            if (attribute != null) {
                String jsonString = new String(attribute.asArray(), StandardCharsets.UTF_8);
                rootNode = (ObjectNode) objectMapper.readTree(jsonString);
            } else {
                // Nếu attribute chưa có giá trị, tạo một ObjectNode mới
                rootNode = objectMapper.createObjectNode();
            }

            // Kiểm tra và tạo node "material" nếu cần
            ObjectNode materialNode;
            if (rootNode.has("material")) {
                materialNode = (ObjectNode) rootNode.get("material");
            } else {
                materialNode = rootNode.putObject("material");
            }

            // Đặt giá trị mới cho "material.percent_protein"
            materialNode.put("percent_protein", percentProtein);

            // Cập nhật lại thuộc tính attribute với JSON mới
            this.attribute = Json.of(rootNode.toString().getBytes(StandardCharsets.UTF_8));
            this.percentProtein = percentProtein;  // Cập nhật giá trị proteinPercent
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // kiểm tra attribute có key là percent_protein không
    public boolean hasProteinPercent(Json attribute) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(attribute.asString());
            return jsonNode.has("percent_protein");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public Item toEntity(){
        Item item = new Item();
        item.setId(this.id);
        item.setCode(this.code);
        item.setName(this.name);
        item.setUomId(this.uomId);
        item.setAttribute(this.attribute);
        item.setCompany(this.company);
        item.setDepartment(this.department);
        item.setIsDeleted(this.isDeleted);
        item.setCreatedBy(this.createdBy);
        item.setCreatedDate(this.createdDate);
        item.setUpdatedBy(this.updatedBy);
        item.setUpdatedAt(this.updatedAt);
        item.setDeletedBy(this.deletedBy);
        item.setDeletedAt(this.deletedAt);
        item.setItemCategoryId(this.itemCategoryId);
        item.setPercentProtein(this.percentProtein);
        item.setNotes(this.notes);
        item.setVatRate(this.vatRate);
        item.setUnitPrice(this.unitPrice);
        item.setRevenueGroupId(this.revenueGroupId);
        item.setSupplierId(this.supplierId);
        item.setItemType(this.itemType);
        return item;
    }
}
