package com.masi.production.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.masi.production.domain.enumeration.WeightUnit;
import com.masi.production.service.dto.AdditiveMaterialChecklistDTO;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A AdditiveMaterialChecklist.
 */
@Table("additive_material_checklist")
@JsonIgnoreProperties(value = { "new" })
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AdditiveMaterialChecklist implements Serializable, Persistable<UUID> {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "must not be null")
    @Id
    @Column("id")
    private UUID id;

    @NotNull(message = "must not be null")
    @Column("check_date")
    private LocalDate checkDate;

    @NotNull(message = "must not be null")
    @Column("check_time")
    private ZonedDateTime checkTime;

    @NotNull(message = "must not be null")
    @Column("weight_number")
    private String weightNumber;

    @NotNull(message = "must not be null")
    @Column("check_impurity")
    private Boolean checkImpurity;

    @Column("impurity_note")
    private String impurityNote;

    @NotNull(message = "must not be null")
    @Column("weight_material")
    private String weightMaterial;

    @Column("weight_material_unit")
    private WeightUnit weightMaterialUnit;

    @NotNull(message = "must not be null")
    @Column("bicabonat_lot_number")
    private String bicabonatLotNumber;

    @NotNull(message = "must not be null")
    @Column("bicacbonat_weight")
    private Float bicacbonatWeight;

    @Column("bicacbonat_weight_unit")
    private WeightUnit bicacbonatWeightUnit;

    @NotNull(message = "must not be null")
    @Column("receiver_id")
    private UUID receiverId;

    @Column("note")
    private String note;

    @NotNull(message = "must not be null")
    @Column("is_active")
    private Boolean isActive;

    @Column("created_at")
    private ZonedDateTime createdAt;

    @Column("last_updated")
    private ZonedDateTime lastUpdated;

    @Column("work_item_id")
    private UUID workItemId;

    @Transient
    private boolean isPersisted;

    @Transient
    @JsonIgnoreProperties(
        value = {
            "receiveMaterialChecklists",
            "additiveMaterialChecklists",
            "machineOperationChecklists",
            "steamingProcessChecklists",
            "metalDetectionChecklists",
            "mixingReportChecklists",
            "workOrder",
        },
        allowSetters = true
    )
    private WorkItem workItem;



    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public AdditiveMaterialChecklist id(UUID id) {
        this.setId(id);
        return this;
    }

    public WeightUnit getWeightMaterialUnit() {
        return weightMaterialUnit;
    }

    public void setWeightMaterialUnit(WeightUnit weightMaterialUnit) {
        this.weightMaterialUnit = weightMaterialUnit;
    }

    public WeightUnit getBicacbonatWeightUnit() {
        return bicacbonatWeightUnit;
    }

    public void setBicacbonatWeightUnit(WeightUnit bicacbonatWeightUnit) {
        this.bicacbonatWeightUnit = bicacbonatWeightUnit;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public LocalDate getCheckDate() {
        return this.checkDate;
    }

    public AdditiveMaterialChecklist checkDate(LocalDate checkDate) {
        this.setCheckDate(checkDate);
        return this;
    }

    public void setCheckDate(LocalDate checkDate) {
        this.checkDate = checkDate;
    }

    public ZonedDateTime getCheckTime() {
        return this.checkTime;
    }

    public AdditiveMaterialChecklist checkTime(ZonedDateTime checkTime) {
        this.setCheckTime(checkTime);
        return this;
    }

    public void setCheckTime(ZonedDateTime checkTime) {
        this.checkTime = checkTime;
    }

    public String getWeightNumber() {
        return this.weightNumber;
    }

    public AdditiveMaterialChecklist weightNumber(String weightNumber) {
        this.setWeightNumber(weightNumber);
        return this;
    }

    public void setWeightNumber(String weightNumber) {
        this.weightNumber = weightNumber;
    }

    public Boolean getCheckImpurity() {
        return this.checkImpurity;
    }

    public AdditiveMaterialChecklist checkImpurity(Boolean checkImpurity) {
        this.setCheckImpurity(checkImpurity);
        return this;
    }

    public void setCheckImpurity(Boolean checkImpurity) {
        this.checkImpurity = checkImpurity;
    }

    public String getImpurityNote() {
        return this.impurityNote;
    }

    public AdditiveMaterialChecklist impurityNote(String impurityNote) {
        this.setImpurityNote(impurityNote);
        return this;
    }

    public void setImpurityNote(String impurityNote) {
        this.impurityNote = impurityNote;
    }

    public String getWeightMaterial() {
        return this.weightMaterial;
    }

    public AdditiveMaterialChecklist weightMaterial(String weightMaterial) {
        this.setWeightMaterial(weightMaterial);
        return this;
    }

    public void setWeightMaterial(String weightMaterial) {
        this.weightMaterial = weightMaterial;
    }

    public String getBicabonatLotNumber() {
        return this.bicabonatLotNumber;
    }

    public AdditiveMaterialChecklist bicabonatLotNumber(String bicabonatLotNumber) {
        this.setBicabonatLotNumber(bicabonatLotNumber);
        return this;
    }

    public void setBicabonatLotNumber(String bicabonatLotNumber) {
        this.bicabonatLotNumber = bicabonatLotNumber;
    }

    public Float getBicacbonatWeight() {
        return this.bicacbonatWeight;
    }

    public AdditiveMaterialChecklist bicacbonatWeight(Float bicacbonatWeight) {
        this.setBicacbonatWeight(bicacbonatWeight);
        return this;
    }

    public void setBicacbonatWeight(Float bicacbonatWeight) {
        this.bicacbonatWeight = bicacbonatWeight;
    }

    public UUID getReceiverId() {
        return this.receiverId;
    }

    public AdditiveMaterialChecklist receiverId(UUID receiverId) {
        this.setReceiverId(receiverId);
        return this;
    }

    public void setReceiverId(UUID receiverId) {
        this.receiverId = receiverId;
    }

    public String getNote() {
        return this.note;
    }

    public AdditiveMaterialChecklist note(String note) {
        this.setNote(note);
        return this;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public AdditiveMaterialChecklist isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public ZonedDateTime getCreatedAt() {
        return this.createdAt;
    }

    public AdditiveMaterialChecklist createdAt(ZonedDateTime createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public ZonedDateTime getLastUpdated() {
        return this.lastUpdated;
    }

    public AdditiveMaterialChecklist lastUpdated(ZonedDateTime lastUpdated) {
        this.setLastUpdated(lastUpdated);
        return this;
    }

    public void setLastUpdated(ZonedDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public UUID getWorkItemId() {
        return this.workItemId;
    }

    public void setWorkItemId(UUID workItemId) {
        this.workItemId = workItemId;
    }

    @Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public AdditiveMaterialChecklist setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    public WorkItem getWorkItem() {
        return this.workItem;
    }

    public void setWorkItem(WorkItem workItem) {
        this.workItem = workItem;
        this.workItemId = workItem != null ? workItem.getId() : null;
    }

    public AdditiveMaterialChecklist workItem(WorkItem workItem) {
        this.setWorkItem(workItem);
        return this;
    }



    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AdditiveMaterialChecklist)) {
            return false;
        }
        return getId() != null && getId().equals(((AdditiveMaterialChecklist) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AdditiveMaterialChecklist{" +
            "id=" + getId() +
            ", checkDate='" + getCheckDate() + "'" +
            ", checkTime='" + getCheckTime() + "'" +
            ", weightNumber='" + getWeightNumber() + "'" +
            ", checkImpurity='" + getCheckImpurity() + "'" +
            ", impurityNote='" + getImpurityNote() + "'" +
            ", weightMaterial='" + getWeightMaterial() + "'" +
            ", bicabonatLotNumber='" + getBicabonatLotNumber() + "'" +
            ", bicacbonatWeight=" + getBicacbonatWeight() +
            ", receiverId='" + getReceiverId() + "'" +
            ", note='" + getNote() + "'" +
            ", isActive='" + getIsActive() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", lastUpdated='" + getLastUpdated() + "'" +
            ", workItemId='" + getWorkItemId() + "'" +
            "}";
    }

    public AdditiveMaterialChecklistDTO toDto() {
        AdditiveMaterialChecklistDTO dto = new AdditiveMaterialChecklistDTO();
        dto.setId(id);
        dto.setCheckDate(checkDate);
        if (Objects.nonNull(checkDate)) {
            dto.setZonedCheckDate(checkDate.atStartOfDay(ZoneOffset.UTC));
        }
        dto.setCheckTime(checkTime);
        dto.setWeightNumber(weightNumber);
        dto.setCheckImpurity(checkImpurity);
        dto.setImpurityNote(impurityNote);
        dto.setWeightMaterial(weightMaterial);
        dto.setBicabonatLotNumber(bicabonatLotNumber);
        dto.setBicacbonatWeight(bicacbonatWeight);
        dto.setReceiverId(receiverId);
        dto.setNote(note);
        dto.setIsActive(isActive);
        dto.setCreatedAt(createdAt);
        dto.setLastUpdated(lastUpdated);
        dto.setWorkItemId(workItemId);
        // set unit
        dto.setWeightMaterialUnit(weightMaterialUnit==null?WeightUnit.KILOGRAM:weightMaterialUnit);
        dto.setBicacbonatWeightUnit(bicacbonatWeightUnit==null?WeightUnit.KILOGRAM:bicacbonatWeightUnit);
        return dto;
    }
}
