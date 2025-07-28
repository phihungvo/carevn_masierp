package com.masi.production.service.dto;

import com.carevn.masi.utils.PgJsonObjectDeserializer;
import com.carevn.masi.utils.PgJsonObjectSerializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.masi.production.domain.ManufactureOrder;
import com.masi.production.domain.enumeration.ManufactureOrderType;
import com.masi.production.domain.enumeration.MoStatus;
import com.masi.production.domain.enumeration.StatusEntity;
import io.r2dbc.postgresql.codec.Json;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.data.relational.core.mapping.Column;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 * A DTO for the {@link com.masi.production.domain.ManufactureOrder} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
@Data
public class ManufactureOrderDTO implements Serializable {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID id;

    private String code;

    private String name;

    private LocalDate fromDate;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ZonedDateTime zonedFromDate;

    private String manufactureOrderType;

    private LocalDate toDate;

    private String typeProtein;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ZonedDateTime zonedToDate;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private StatusEntity status;

    private UUID orderId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Boolean isActive;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ZonedDateTime createdAt;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ZonedDateTime lastUpdated;

    private Collection<UUID> listIdContractMaterial;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(allOf = {WorkOrderDTO.class})
    private Collection<WorkOrderDTO> workOrders;

    private Collection<ReleaseWarehouseDTO> releaseWarehouseDTOS;

    private UUID productionStandardId;

    private UUID qualityCheckSampleId;

    private String productionStatus;

    private Float productionQuantity;
    private UUID productionRoutingId;
    private UUID inventoryId;

    private UUID materialId;

    private String percentProtein;

    @JsonSerialize(using = PgJsonObjectSerializer.class)
    @JsonDeserialize(using = PgJsonObjectDeserializer.class)
    private Json attributes;

    private UUID productMaintainId;

    private UUID productPackageId;

    private UUID orderItemId;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ManufactureOrderDTO manufactureOrderDTO)) {
            return false;
        }

        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, manufactureOrderDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    public ManufactureOrder applyUpdateTo(ManufactureOrder manufactureOrder) {
        manufactureOrder.setName(this.name);
        manufactureOrder.setCode(this.code);
        manufactureOrder.setFromDate(this.fromDate);
        manufactureOrder.setToDate(this.toDate);
        manufactureOrder.setOrderId(this.orderId);
        manufactureOrder.setProductionStandardId(this.productionStandardId);
        manufactureOrder.setQualityCheckSampleId(this.qualityCheckSampleId);
        manufactureOrder.setPercentProtein(this.percentProtein);
        manufactureOrder.setMaterialId(this.materialId);
        manufactureOrder.setProductionQuantity(this.productionQuantity);

        // manufactureOrder.setTypeProtein(this.typeProtein);
        return manufactureOrder;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ManufactureOrderDTO{" +
                "id='" + getId() + "'" +
                ", name='" + getName() + "'" +
                ", fromDate='" + getFromDate() + "'" +
                ", toDate='" + getToDate() + "'" +
                ", status='" + getStatus() + "'" +
                ", orderId='" + getOrderId() + "'" +
                ", isActive='" + getIsActive() + "'" +
                ", createdAt='" + getCreatedAt() + "'" +
                ", lastUpdated='" + getLastUpdated() + "'" +
                "}";
    }

    public ManufactureOrder toEntity() {
        ManufactureOrder entity = new ManufactureOrder();
        entity.setId(this.getId());
        entity.setName(this.getName());
        entity.setCode(this.getCode());
        entity.setFromDate(this.getFromDate());
        entity.setToDate(this.getToDate());
        entity.setManufactureOrderType(this.getManufactureOrderType());
        entity.setStatus(this.getStatus());
        entity.setOrderId(this.getOrderId());
        entity.setIsActive(this.getIsActive());
        entity.setTypeProtein(this.getTypeProtein());
        entity.setCreatedAt(this.getCreatedAt());
        entity.setLastUpdated(this.getLastUpdated());
        entity.setQualityCheckSampleId(this.getQualityCheckSampleId());
        entity.setProductionStatus(this.getProductionStatus());
        entity.setProductionStandardId(this.getProductionStandardId());
        entity.setProductionQuantity(this.getProductionQuantity());
        entity.setMaterialId(this.getMaterialId());
        entity.setPercentProtein(this.getPercentProtein());
        return entity;
    }
}
