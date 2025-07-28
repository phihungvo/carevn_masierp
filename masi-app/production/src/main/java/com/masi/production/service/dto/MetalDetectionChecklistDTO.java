package com.masi.production.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.masi.production.domain.MetalDetectionChecklist;
import com.masi.production.domain.enumeration.ChecklistType;
import lombok.*;

/**
 * A DTO for the {@link com.masi.production.domain.MetalDetectionChecklist}
 * entity.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MetalDetectionChecklistDTO extends BaseCheckListDto implements Serializable {

    @NotNull(message = "must not be null")
    private LocalDate checkDate=LocalDate.now();
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ZonedDateTime zonedDateCheck;

    @NotNull(message = "must not be null")
    private String finProductNo="";

    @NotNull(message = "must not be null")
    private Boolean magnetBegin = false;

    private String magnetBeginNote;

    @NotNull(message = "must not be null")
    private Boolean magnetEnd = false;

    private String magnetEndNote;

    @NotNull(message = "must not be null")
    private Boolean screen4Begin = false;

    private String screen4BeginNote;

    @NotNull(message = "must not be null")
    private Boolean screen4End = false;

    private String screen4EndNote;

    @NotNull(message = "must not be null")
    private Boolean screen3Begin = false;

    private String screen3BeginNote;

    @NotNull(message = "must not be null")
    private Boolean screen3End = false;

    private String screen3EndNote;

    @NotNull(message = "must not be null")
    private UUID checkedBy = UUID.randomUUID();

    @NotNull(message = "must not be null")
    private UUID auditedBy = UUID.randomUUID();

    private String note;

    public UUID getWorkItemId() {
        return workItemId;
    }

    public void setWorkItemId(UUID workItemId) {
        this.workItemId = workItemId;
    }

    public MetalDetectionChecklist toEntity() {
        MetalDetectionChecklist entity = new MetalDetectionChecklist();
        entity.setId(this.id);
        entity.setCheckDate(this.checkDate);
        entity.setFinProductNo(this.finProductNo);
        entity.setMagnetBegin(this.magnetBegin);
        entity.setMagnetBeginNote(this.magnetBeginNote);
        entity.setMagnetEnd(this.magnetEnd);
        entity.setMagnetEndNote(this.magnetEndNote);
        entity.setScreen4Begin(this.screen4Begin);
        entity.setScreen4BeginNote(this.screen4BeginNote);
        entity.setScreen4End(this.screen4End);
        entity.setScreen4EndNote(this.screen4EndNote);
        entity.setScreen3Begin(this.screen3Begin);
        entity.setScreen3BeginNote(this.screen3BeginNote);
        entity.setScreen3End(this.screen3End);
        entity.setScreen3EndNote(this.screen3EndNote);
        entity.setCheckedBy(this.checkedBy);
        entity.setAuditedBy(this.auditedBy);
        entity.setNote(this.note);
        entity.setWorkItemId(this.workItemId);
        entity.setIsActive(true);
        entity.setCreatedAt(ZonedDateTime.now());
        entity.setLastUpdated(ZonedDateTime.now());
        return entity;
    }

    public void applyUpdate(MetalDetectionChecklist entity) {
        if (entity == null) {
            return;
        }
        entity.setCheckDate(this.checkDate);
        entity.setFinProductNo(this.finProductNo);
        entity.setMagnetBegin(this.magnetBegin);
        entity.setMagnetBeginNote(this.magnetBeginNote);
        entity.setMagnetEnd(this.magnetEnd);
        entity.setMagnetEndNote(this.magnetEndNote);
        entity.setScreen4Begin(this.screen4Begin);
        entity.setScreen4BeginNote(this.screen4BeginNote);
        entity.setScreen4End(this.screen4End);
        entity.setScreen4EndNote(this.screen4EndNote);
        entity.setScreen3Begin(this.screen3Begin);
        entity.setScreen3BeginNote(this.screen3BeginNote);
        entity.setScreen3End(this.screen3End);
        entity.setScreen3EndNote(this.screen3EndNote);
        entity.setCheckedBy(this.checkedBy);
        entity.setAuditedBy(this.auditedBy);
        entity.setNote(this.note);
        entity.setWorkItemId(this.workItemId);
    }

    public MetalDetectionChecklistDTO() {
        this.type = ChecklistType.METAL_DETECTION_CHECKLIST.getName();
    }
}
