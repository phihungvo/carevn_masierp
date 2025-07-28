package com.masi.production.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.masi.production.domain.enumeration.WeightUnit;
import com.masi.production.service.dto.MixingReportChecklistDTO;

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
 * A MixingReportChecklist.
 */
@Table("mixing_report_checklist")
@JsonIgnoreProperties(value = { "new" })
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MixingReportChecklist implements Serializable, Persistable<UUID> {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private UUID id;

    @Column("check_date")
    private LocalDate checkDate;

    @Column("fin_product_1_no")
    private String finProduct1No;

    @Column("fin_product_1_weight")
    private Float finProduct1Weight;

    @Column("fin_product_1_weight_unit")
    private WeightUnit finProduct1WeightUnit;

    @Column("fin_product_2_no")
    private String finProduct2No;

    @Column("fin_product_2_weight")
    private Float finProduct2Weight;

    @Column("fin_product_2_weight_unit")
    private WeightUnit finProduct2WeightUnit;

    @Column("bht_no")
    private String bhtNo;

    @Column("bht_weight")
    private Float bhtWeight;

    @Column("bht_weight_unit")
    private WeightUnit bhtWeightUnit;

    @Column("bht_weight_prd")
    private Float bhtWeightPrd;

    @Column("bht_weight_prd_unit")
    private WeightUnit bhtWeightPrdUnit;

    @Column("weight_prd_no")
    private String weightPrdNo;


    @Column("check_impurity")
    private Boolean checkImpurity;

    @Column("check_impurity_note")
    private String checkImpurityNote;

    @Column("check_smell")
    private Boolean checkSmell;

    @Column("check_smell_note")
    private String checkSmellNote;

    @Column("check_color")
    private Boolean checkColor;

    @Column("check_color_note")
    private String checkColorNote;

    @Column("check_employee_id")
    private String checkEmployeeId;

    @Column("note")
    private String note;

    @Column("is_active")
    private Boolean isActive;

    @Column("created_at")
    private ZonedDateTime createdAt;

    @Column("last_updated")
    private ZonedDateTime lastUpdated;

    @Column("moisture")
    private Float moisture;

    @Column("tvn")
    private Float tvn;

    @Column("ash")
    private Float ash;

    @Column("protein")
    private Float protein;

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


    public WeightUnit getFinProduct1WeightUnit() {
        return finProduct1WeightUnit;
    }

    public void setFinProduct1WeightUnit(WeightUnit finProduct1WeightUnit) {
        this.finProduct1WeightUnit = finProduct1WeightUnit;
    }

    public WeightUnit getFinProduct2WeightUnit() {
        return finProduct2WeightUnit;
    }

    public void setFinProduct2WeightUnit(WeightUnit finProduct2WeightUnit) {
        this.finProduct2WeightUnit = finProduct2WeightUnit;
    }

    public WeightUnit getBhtWeightUnit() {
        return bhtWeightUnit;
    }

    public void setBhtWeightUnit(WeightUnit bhtWeightUnit) {
        this.bhtWeightUnit = bhtWeightUnit;
    }

    public WeightUnit getBhtWeightPrdUnit() {
        return bhtWeightPrdUnit;
    }

    public void setBhtWeightPrdUnit(WeightUnit bhtWeightPrdUnit) {
        this.bhtWeightPrdUnit = bhtWeightPrdUnit;
    }

    public UUID getId() {
        return this.id;
    }

    public MixingReportChecklist id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public LocalDate getCheckDate() {
        return this.checkDate;
    }

    public MixingReportChecklist dateCheck(LocalDate dateCheck) {
        this.setCheckDate(dateCheck);
        return this;
    }

    public void setCheckDate(LocalDate checkDate) {
        this.checkDate = checkDate;
    }

    public String getFinProduct1No() {
        return this.finProduct1No;
    }

    public MixingReportChecklist finProduct1No(String finProduct1No) {
        this.setFinProduct1No(finProduct1No);
        return this;
    }

    public void setFinProduct1No(String finProduct1No) {
        this.finProduct1No = finProduct1No;
    }

    public Float getFinProduct1Weight() {
        return this.finProduct1Weight;
    }

    public MixingReportChecklist finProduct1Weight(Float finProduct1Weight) {
        this.setFinProduct1Weight(finProduct1Weight);
        return this;
    }

    public void setFinProduct1Weight(Float finProduct1Weight) {
        this.finProduct1Weight = finProduct1Weight;
    }

    public String getFinProduct2No() {
        return this.finProduct2No;
    }

    public MixingReportChecklist finProduct2No(String finProduct2No) {
        this.setFinProduct2No(finProduct2No);
        return this;
    }

    public void setFinProduct2No(String finProduct2No) {
        this.finProduct2No = finProduct2No;
    }

    public Float getFinProduct2Weight() {
        return this.finProduct2Weight;
    }

    public MixingReportChecklist finProduct2Weight(Float finProduct2Weight) {
        this.setFinProduct2Weight(finProduct2Weight);
        return this;
    }

    public void setFinProduct2Weight(Float finProduct2Weight) {
        this.finProduct2Weight = finProduct2Weight;
    }

    public String getBhtNo() {
        return this.bhtNo;
    }

    public MixingReportChecklist bhtNo(String bhtNo) {
        this.setBhtNo(bhtNo);
        return this;
    }

    public void setBhtNo(String bhtNo) {
        this.bhtNo = bhtNo;
    }

    public Float getBhtWeight() {
        return this.bhtWeight;
    }

    public MixingReportChecklist bhtWeight(Float bhtWeight) {
        this.setBhtWeight(bhtWeight);
        return this;
    }

    public void setBhtWeight(Float bhtWeight) {
        this.bhtWeight = bhtWeight;
    }

    public Float getBhtWeightPrd() {
        return this.bhtWeightPrd;
    }

    public MixingReportChecklist bhtWeightPrd(Float bhtWeightPrd) {
        this.setBhtWeightPrd(bhtWeightPrd);
        return this;
    }

    public void setBhtWeightPrd(Float bhtWeightPrd) {
        this.bhtWeightPrd = bhtWeightPrd;
    }

    public String getWeightPrdNo() {
        return this.weightPrdNo;
    }

    public MixingReportChecklist weightPrdNo(String weightPrdNo) {
        this.setWeightPrdNo(weightPrdNo);
        return this;
    }

    public void setWeightPrdNo(String weightPrdNo) {
        this.weightPrdNo = weightPrdNo;
    }

    public Boolean getCheckImpurity() {
        return this.checkImpurity;
    }

    public MixingReportChecklist checkImpurity(Boolean checkImpurity) {
        this.setCheckImpurity(checkImpurity);
        return this;
    }

    public void setCheckImpurity(Boolean checkImpurity) {
        this.checkImpurity = checkImpurity;
    }

    public String getCheckImpurityNote() {
        return this.checkImpurityNote;
    }

    public MixingReportChecklist checkImpurityNote(String checkImpurityNote) {
        this.setCheckImpurityNote(checkImpurityNote);
        return this;
    }

    public void setCheckImpurityNote(String checkImpurityNote) {
        this.checkImpurityNote = checkImpurityNote;
    }

    public Boolean getCheckSmell() {
        return this.checkSmell;
    }

    public MixingReportChecklist checkSmell(Boolean checkSmell) {
        this.setCheckSmell(checkSmell);
        return this;
    }

    public void setCheckSmell(Boolean checkSmell) {
        this.checkSmell = checkSmell;
    }

    public String getCheckSmellNote() {
        return this.checkSmellNote;
    }

    public MixingReportChecklist checkSmellNote(String checkSmellNote) {
        this.setCheckSmellNote(checkSmellNote);
        return this;
    }

    public void setCheckSmellNote(String checkSmellNote) {
        this.checkSmellNote = checkSmellNote;
    }

    public Boolean getCheckColor() {
        return this.checkColor;
    }

    public MixingReportChecklist checkColor(Boolean checkColor) {
        this.setCheckColor(checkColor);
        return this;
    }

    public void setCheckColor(Boolean checkColor) {
        this.checkColor = checkColor;
    }

    public String getCheckColorNote() {
        return this.checkColorNote;
    }

    public MixingReportChecklist checkColorNote(String checkColorNote) {
        this.setCheckColorNote(checkColorNote);
        return this;
    }

    public void setCheckColorNote(String checkColorNote) {
        this.checkColorNote = checkColorNote;
    }

    public String getCheckEmployeeId() {
        return this.checkEmployeeId;
    }

    public MixingReportChecklist checkEmployeeId(String checkEmployeeId) {
        this.setCheckEmployeeId(checkEmployeeId);
        return this;
    }

    public void setCheckEmployeeId(String checkEmployeeId) {
        this.checkEmployeeId = checkEmployeeId;
    }

    public String getNote() {
        return this.note;
    }

    public MixingReportChecklist note(String note) {
        this.setNote(note);
        return this;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public MixingReportChecklist isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public ZonedDateTime getCreatedAt() {
        return this.createdAt;
    }

    public MixingReportChecklist createdAt(ZonedDateTime createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public ZonedDateTime getLastUpdated() {
        return this.lastUpdated;
    }

    public MixingReportChecklist lastUpdated(ZonedDateTime lastUpdated) {
        this.setLastUpdated(lastUpdated);
        return this;
    }

    public void setLastUpdated(ZonedDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public Float getMoisture() {
        return this.moisture;
    }

    public MixingReportChecklist moisture(Float moisture) {
        this.setMoisture(moisture);
        return this;
    }

    public void setMoisture(Float moisture) {
        this.moisture = moisture;
    }

    public Float getTvn() {
        return this.tvn;
    }

    public MixingReportChecklist tvn(Float tvn) {
        this.setTvn(tvn);
        return this;
    }

    public void setTvn(Float tvn) {
        this.tvn = tvn;
    }

    public Float getAsh() {
        return this.ash;
    }

    public MixingReportChecklist ash(Float ash) {
        this.setAsh(ash);
        return this;
    }

    public void setAsh(Float ash) {
        this.ash = ash;
    }

    public Float getProtein() {
        return this.protein;
    }

    public MixingReportChecklist protein(Float protein) {
        this.setProtein(protein);
        return this;
    }

    public void setProtein(Float protein) {
        this.protein = protein;
    }

    public UUID getWorkItemId() {
        return workItemId;
    }

    public void setWorkItemId(UUID workItemId) {
        this.workItemId = workItemId;
    }


    @Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public MixingReportChecklist setIsPersisted() {
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

    public MixingReportChecklist workItem(WorkItem workItem) {
        this.setWorkItem(workItem);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MixingReportChecklist)) {
            return false;
        }
        return getId() != null && getId().equals(((MixingReportChecklist) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MixingReportChecklist{" +
            "id=" + getId() +
            ", dateCheck='" + getCheckDate() + "'" +
            ", finProduct1No='" + getFinProduct1No() + "'" +
            ", finProduct1Weight=" + getFinProduct1Weight() +
            ", finProduct2No='" + getFinProduct2No() + "'" +
            ", finProduct2Weight=" + getFinProduct2Weight() +
            ", bhtNo='" + getBhtNo() + "'" +
            ", bhtWeight=" + getBhtWeight() +
            ", bhtWeightPrd=" + getBhtWeightPrd() +
            ", weightPrdNo='" + getWeightPrdNo() + "'" +
            ", checkImpurity='" + getCheckImpurity() + "'" +
            ", checkImpurityNote='" + getCheckImpurityNote() + "'" +
            ", checkSmell='" + getCheckSmell() + "'" +
            ", checkSmellNote='" + getCheckSmellNote() + "'" +
            ", checkColor='" + getCheckColor() + "'" +
            ", checkColorNote='" + getCheckColorNote() + "'" +
            ", checkEmployeeId='" + getCheckEmployeeId() + "'" +
            ", note='" + getNote() + "'" +
            ", isActive='" + getIsActive() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", lastUpdated='" + getLastUpdated() + "'" +
            ", moisture=" + getMoisture() +
            ", tvn=" + getTvn() +
            ", ash=" + getAsh() +
            ", protein=" + getProtein() +
            ", workItemId='" + getWorkItemId() + "'" +
            "}";
    }

    public MixingReportChecklistDTO toDto() {
        MixingReportChecklistDTO dto = new MixingReportChecklistDTO();
        dto.setId(this.id);
        dto.setCheckDate(this.checkDate);
        if (Objects.nonNull(this.checkDate)) dto.setZonedCheckDate(this.checkDate.atStartOfDay().atZone(ZoneOffset.UTC));
        dto.setFinProduct1No(this.finProduct1No);
        dto.setFinProduct1Weight(this.finProduct1Weight);
        dto.setFinProduct2No(this.finProduct2No);
        dto.setFinProduct2Weight(this.finProduct2Weight);
        dto.setBhtNo(this.bhtNo);
        dto.setBhtWeight(this.bhtWeight);
        dto.setBhtWeightPrd(this.bhtWeightPrd);
        dto.setWeightPrdNo(this.weightPrdNo);
        dto.setCheckImpurity(this.checkImpurity);
        dto.setCheckImpurityNote(this.checkImpurityNote);
        dto.setCheckSmell(this.checkSmell);
        dto.setCheckSmellNote(this.checkSmellNote);
        dto.setCheckColor(this.checkColor);
        dto.setCheckColorNote(this.checkColorNote);
        dto.setCheckEmployeeId(this.checkEmployeeId);
        dto.setNote(this.note);
        dto.setIsActive(this.isActive);
        dto.setCreatedAt(this.createdAt);
        dto.setLastUpdated(this.lastUpdated);
        dto.setMoisture(this.moisture);
        dto.setTvn(this.tvn);
        dto.setAsh(this.ash);
        dto.setProtein(this.protein);
        dto.setWorkItemId(this.workItemId);
        // set uint
        dto.setFinProduct1WeightUnit(this.finProduct1WeightUnit==null? WeightUnit.KILOGRAM: this.finProduct1WeightUnit);
        dto.setFinProduct2WeightUnit(this.finProduct2WeightUnit==null? WeightUnit.KILOGRAM: this.finProduct2WeightUnit);
        dto.setBhtWeightUnit(this.bhtWeightUnit==null? WeightUnit.KILOGRAM: this.bhtWeightUnit);
        dto.setBhtWeightPrdUnit(this.bhtWeightPrdUnit==null? WeightUnit.KILOGRAM: this.bhtWeightPrdUnit);

        return dto;
    }
}
