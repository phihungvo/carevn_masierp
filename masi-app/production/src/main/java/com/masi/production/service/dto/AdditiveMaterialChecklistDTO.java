package com.masi.production.service.dto;

import com.masi.production.domain.ReleaseWarehouse;
import com.masi.production.domain.enumeration.WeightUnit;
import jakarta.validation.constraints.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.masi.production.domain.AdditiveMaterialChecklist;
import com.masi.production.domain.enumeration.ChecklistType;
import lombok.*;

/**
 * A DTO for the {@link com.masi.production.domain.AdditiveMaterialChecklist} entity.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AdditiveMaterialChecklistDTO extends BaseCheckListDto implements Serializable {


    @NotNull(message = "must not be null")
    private LocalDate checkDate = LocalDate.now();

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ZonedDateTime zonedCheckDate;

    @NotNull(message = "must not be null")
    private ZonedDateTime checkTime = ZonedDateTime.now();

    @NotNull(message = "must not be null")
    private String weightNumber = "";

    @NotNull(message = "must not be null")
    private Boolean checkImpurity = false;

    private String impurityNote;

    @NotNull(message = "must not be null")
    private String weightMaterial = "";

    @NotNull(message = "must not be null")
    private String bicabonatLotNumber = "";

    @NotNull(message = "must not be null")
    private Float bicacbonatWeight = 0f;

    @NotNull(message = "must not be null")
    private UUID receiverId = UUID.randomUUID();

    private String note;

    private WeightUnit weightMaterialUnit;

    private WeightUnit bicacbonatWeightUnit;

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public ZonedDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(ZonedDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public WorkItemDTO getWorkItem() {
        return workItem;
    }

    public void setWorkItem(WorkItemDTO workItem) {
        this.workItem = workItem;
    }

    public UUID getWorkItemId() {
        return workItemId;
    }

    public void setWorkItemId(UUID workItemId) {
        this.workItemId = workItemId;
    }

    public Collection<ReleaseWarehouseDTO> releaseWarehouseDTO;

    public List<ReleaseWarehouse> toReleaseWarehouseEntity() {
        return releaseWarehouseDTO.stream().map(ReleaseWarehouseDTO::toEntity).collect(Collectors.toList());
    }

    public AdditiveMaterialChecklistDTO() {
        this.type = ChecklistType.ADDITIVE_MATERIAL_CHECKLIST.getName();
    }

    public AdditiveMaterialChecklist toEntity() {
        AdditiveMaterialChecklist entity = new AdditiveMaterialChecklist();
        entity.setId(this.id);
        entity.setCheckDate(this.checkDate);
        entity.setCheckTime(this.checkTime);
        entity.setWeightNumber(this.weightNumber);
        entity.setCheckImpurity(this.checkImpurity);
        entity.setImpurityNote(this.impurityNote);
        entity.setWeightMaterial(this.weightMaterial);
        entity.setBicabonatLotNumber(this.bicabonatLotNumber);
        entity.setBicacbonatWeight(this.bicacbonatWeight);
        entity.setReceiverId(this.receiverId);
        entity.setNote(this.note);
        entity.setIsActive(this.isActive);
        entity.setCreatedAt(this.createdAt);
        entity.setLastUpdated(this.lastUpdated);
        entity.setWorkItemId(this.workItemId);
        // set unit
        entity.setIsActive(true);
        entity.setCreatedAt(ZonedDateTime.now());
        entity.setLastUpdated(ZonedDateTime.now());
        entity.setWeightMaterialUnit(this.weightMaterialUnit);
        entity.setBicacbonatWeightUnit(this.bicacbonatWeightUnit);

        return entity;
    }

    public void applyUpdate(AdditiveMaterialChecklist entity) {
        if (entity == null) {
            return;
        }
        entity.setCheckDate(this.checkDate);
        entity.setCheckTime(this.checkTime);
        entity.setWeightNumber(this.weightNumber);
        entity.setCheckImpurity(this.checkImpurity);
        entity.setImpurityNote(this.impurityNote);
        entity.setWeightMaterial(this.weightMaterial);
        entity.setBicabonatLotNumber(this.bicabonatLotNumber);
        entity.setBicacbonatWeight(this.bicacbonatWeight);
        entity.setReceiverId(this.receiverId);
        entity.setNote(this.note);
        entity.setWorkItemId(this.workItemId);
        entity.setIsPersisted();
        // set unit
        entity.setWeightMaterialUnit(this.weightMaterialUnit);
        entity.setBicacbonatWeightUnit(this.bicacbonatWeightUnit);
    }


}
