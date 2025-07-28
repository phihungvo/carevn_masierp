package com.masi.production.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.masi.production.service.dto.MetalDetectionChecklistDTO;

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
 * A MetalDetectionChecklist.
 */
@Table("metal_detection_checklist")
@JsonIgnoreProperties(value = { "new" })
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MetalDetectionChecklist implements Serializable, Persistable<UUID> {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "must not be null")
    @Id
    @Column("id")
    private UUID id;

    @NotNull(message = "must not be null")
    @Column("check_date")
    private LocalDate checkDate;

    @NotNull(message = "must not be null")
    @Column("fin_product_no")
    private String finProductNo;

    @NotNull(message = "must not be null")
    @Column("magnet_begin")
    private Boolean magnetBegin;

    @Column("magnet_begin_note")
    private String magnetBeginNote;

    @NotNull(message = "must not be null")
    @Column("magnet_end")
    private Boolean magnetEnd;

    @Column("magnet_end_note")
    private String magnetEndNote;

    @NotNull(message = "must not be null")
    @Column("screen_4_begin")
    private Boolean screen4Begin;

    @Column("screen_4_begin_note")
    private String screen4BeginNote;

    @NotNull(message = "must not be null")
    @Column("screen_4_end")
    private Boolean screen4End;

    @Column("screen_4_end_note")
    private String screen4EndNote;

    @NotNull(message = "must not be null")
    @Column("screen_3_begin")
    private Boolean screen3Begin;

    @Column("screen_3_begin_note")
    private String screen3BeginNote;

    @NotNull(message = "must not be null")
    @Column("screen_3_end")
    private Boolean screen3End;

    @Column("screen_3_end_note")
    private String screen3EndNote;

    @NotNull(message = "must not be null")
    @Column("checked_by")
    private UUID checkedBy;

    @NotNull(message = "must not be null")
    @Column("audited_by")
    private UUID auditedBy;

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

    public MetalDetectionChecklist id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public LocalDate getCheckDate() {
        return this.checkDate;
    }

    public MetalDetectionChecklist checkDate(LocalDate checkDate) {
        this.setCheckDate(checkDate);
        return this;
    }

    public void setCheckDate(LocalDate checkDate) {
        this.checkDate = checkDate;
    }

    public String getFinProductNo() {
        return this.finProductNo;
    }

    public MetalDetectionChecklist finProductNo(String finProductNo) {
        this.setFinProductNo(finProductNo);
        return this;
    }

    public void setFinProductNo(String finProductNo) {
        this.finProductNo = finProductNo;
    }

    public Boolean getMagnetBegin() {
        return this.magnetBegin;
    }

    public MetalDetectionChecklist magnetBegin(Boolean magnetBegin) {
        this.setMagnetBegin(magnetBegin);
        return this;
    }

    public void setMagnetBegin(Boolean magnetBegin) {
        this.magnetBegin = magnetBegin;
    }

    public String getMagnetBeginNote() {
        return this.magnetBeginNote;
    }

    public MetalDetectionChecklist magnetBeginNote(String magnetBeginNote) {
        this.setMagnetBeginNote(magnetBeginNote);
        return this;
    }

    public void setMagnetBeginNote(String magnetBeginNote) {
        this.magnetBeginNote = magnetBeginNote;
    }

    public Boolean getMagnetEnd() {
        return this.magnetEnd;
    }

    public MetalDetectionChecklist magnetEnd(Boolean magnetEnd) {
        this.setMagnetEnd(magnetEnd);
        return this;
    }

    public void setMagnetEnd(Boolean magnetEnd) {
        this.magnetEnd = magnetEnd;
    }

    public String getMagnetEndNote() {
        return this.magnetEndNote;
    }

    public MetalDetectionChecklist magnetEndNote(String magnetEndNote) {
        this.setMagnetEndNote(magnetEndNote);
        return this;
    }

    public void setMagnetEndNote(String magnetEndNote) {
        this.magnetEndNote = magnetEndNote;
    }

    public Boolean getScreen4Begin() {
        return this.screen4Begin;
    }

    public MetalDetectionChecklist screen4Begin(Boolean screen4Begin) {
        this.setScreen4Begin(screen4Begin);
        return this;
    }

    public void setScreen4Begin(Boolean screen4Begin) {
        this.screen4Begin = screen4Begin;
    }

    public String getScreen4BeginNote() {
        return this.screen4BeginNote;
    }

    public MetalDetectionChecklist screen4BeginNote(String screen4BeginNote) {
        this.setScreen4BeginNote(screen4BeginNote);
        return this;
    }

    public void setScreen4BeginNote(String screen4BeginNote) {
        this.screen4BeginNote = screen4BeginNote;
    }

    public Boolean getScreen4End() {
        return this.screen4End;
    }

    public MetalDetectionChecklist screen4End(Boolean screen4End) {
        this.setScreen4End(screen4End);
        return this;
    }

    public void setScreen4End(Boolean screen4End) {
        this.screen4End = screen4End;
    }

    public String getScreen4EndNote() {
        return this.screen4EndNote;
    }

    public MetalDetectionChecklist screen4EndNote(String screen4EndNote) {
        this.setScreen4EndNote(screen4EndNote);
        return this;
    }

    public void setScreen4EndNote(String screen4EndNote) {
        this.screen4EndNote = screen4EndNote;
    }

    public Boolean getScreen3Begin() {
        return this.screen3Begin;
    }

    public MetalDetectionChecklist screen3Begin(Boolean screen3Begin) {
        this.setScreen3Begin(screen3Begin);
        return this;
    }

    public void setScreen3Begin(Boolean screen3Begin) {
        this.screen3Begin = screen3Begin;
    }

    public String getScreen3BeginNote() {
        return this.screen3BeginNote;
    }

    public MetalDetectionChecklist screen3BeginNote(String screen3BeginNote) {
        this.setScreen3BeginNote(screen3BeginNote);
        return this;
    }

    public void setScreen3BeginNote(String screen3BeginNote) {
        this.screen3BeginNote = screen3BeginNote;
    }

    public Boolean getScreen3End() {
        return this.screen3End;
    }

    public MetalDetectionChecklist screen3End(Boolean screen3End) {
        this.setScreen3End(screen3End);
        return this;
    }

    public void setScreen3End(Boolean screen3End) {
        this.screen3End = screen3End;
    }

    public String getScreen3EndNote() {
        return this.screen3EndNote;
    }

    public MetalDetectionChecklist screen3EndNote(String screen3EndNote) {
        this.setScreen3EndNote(screen3EndNote);
        return this;
    }

    public void setScreen3EndNote(String screen3EndNote) {
        this.screen3EndNote = screen3EndNote;
    }

    public UUID getCheckedBy() {
        return this.checkedBy;
    }

    public MetalDetectionChecklist checkedBy(UUID checkedBy) {
        this.setCheckedBy(checkedBy);
        return this;
    }

    public void setCheckedBy(UUID checkedBy) {
        this.checkedBy = checkedBy;
    }

    public UUID getAuditedBy() {
        return this.auditedBy;
    }

    public MetalDetectionChecklist auditedBy(UUID auditedBy) {
        this.setAuditedBy(auditedBy);
        return this;
    }

    public void setAuditedBy(UUID auditedBy) {
        this.auditedBy = auditedBy;
    }

    public String getNote() {
        return this.note;
    }

    public MetalDetectionChecklist note(String note) {
        this.setNote(note);
        return this;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public MetalDetectionChecklist isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public ZonedDateTime getCreatedAt() {
        return this.createdAt;
    }

    public MetalDetectionChecklist createdAt(ZonedDateTime createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public ZonedDateTime getLastUpdated() {
        return this.lastUpdated;
    }

    public MetalDetectionChecklist lastUpdated(ZonedDateTime lastUpdated) {
        this.setLastUpdated(lastUpdated);
        return this;
    }

    public UUID getWorkItemId() {
        return workItemId;
    }

    public void setWorkItemId(UUID workItemId) {
        this.workItemId = workItemId;
    }

    public void setLastUpdated(ZonedDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    @Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public MetalDetectionChecklist setIsPersisted() {
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

    public MetalDetectionChecklist workItem(WorkItem workItem) {
        this.setWorkItem(workItem);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MetalDetectionChecklist)) {
            return false;
        }
        return getId() != null && getId().equals(((MetalDetectionChecklist) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MetalDetectionChecklist{" +
            "id=" + getId() +
            ", checkDate='" + getCheckDate() + "'" +
            ", finProductNo='" + getFinProductNo() + "'" +
            ", magnetBegin='" + getMagnetBegin() + "'" +
            ", magnetBeginNote='" + getMagnetBeginNote() + "'" +
            ", magnetEnd='" + getMagnetEnd() + "'" +
            ", magnetEndNote='" + getMagnetEndNote() + "'" +
            ", screen4Begin='" + getScreen4Begin() + "'" +
            ", screen4BeginNote='" + getScreen4BeginNote() + "'" +
            ", screen4End='" + getScreen4End() + "'" +
            ", screen4EndNote='" + getScreen4EndNote() + "'" +
            ", screen3Begin='" + getScreen3Begin() + "'" +
            ", screen3BeginNote='" + getScreen3BeginNote() + "'" +
            ", screen3End='" + getScreen3End() + "'" +
            ", screen3EndNote='" + getScreen3EndNote() + "'" +
            ", checkedBy='" + getCheckedBy() + "'" +
            ", auditedBy='" + getAuditedBy() + "'" +
            ", note='" + getNote() + "'" +
            ", isActive='" + getIsActive() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", lastUpdated='" + getLastUpdated() + "'" +
            ", workItemId='" + getWorkItemId() + "'" +
            "}";
    }

    public MetalDetectionChecklistDTO toDto() {
        MetalDetectionChecklistDTO dto = new MetalDetectionChecklistDTO();
        dto.setId(this.id);
        dto.setCheckDate(this.checkDate);
        if (Objects.nonNull(this.checkDate)) {
            dto.setZonedDateCheck(this.checkDate.atStartOfDay().atZone(ZoneOffset.UTC));
        }
        dto.setFinProductNo(this.finProductNo);
        dto.setMagnetBegin(this.magnetBegin);
        dto.setMagnetBeginNote(this.magnetBeginNote);
        dto.setMagnetEnd(this.magnetEnd);
        dto.setMagnetEndNote(this.magnetEndNote);
        dto.setScreen4Begin(this.screen4Begin);
        dto.setScreen4BeginNote(this.screen4BeginNote);
        dto.setScreen4End(this.screen4End);
        dto.setScreen4EndNote(this.screen4EndNote);
        dto.setScreen3Begin(this.screen3Begin);
        dto.setScreen3BeginNote(this.screen3BeginNote);
        dto.setScreen3End(this.screen3End);
        dto.setScreen3EndNote(this.screen3EndNote);
        dto.setCheckedBy(this.checkedBy);
        dto.setAuditedBy(this.auditedBy);
        dto.setNote(this.note);
        dto.setIsActive(this.isActive);
        dto.setCreatedAt(this.createdAt);
        dto.setLastUpdated(this.lastUpdated);
        dto.setWorkItemId(this.workItemId);
        return dto;
    }
}
