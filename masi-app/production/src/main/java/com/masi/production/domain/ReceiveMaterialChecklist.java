package com.masi.production.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.masi.production.domain.enumeration.MaterialType;
import com.masi.production.service.dto.ReceiveMaterialChecklistDTO;

import jakarta.validation.constraints.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A ReceiveMaterialChecklist.
 */
@Data
@Table("receive_material_checklist")
@JsonIgnoreProperties(value = { "new" })
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ReceiveMaterialChecklist implements Serializable, Persistable<UUID> {

    @Serial
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
    @Column("transport_condition")
    private Boolean transportCondition;

    @Column("transport_note")
    private String transportNote;

    @NotNull(message = "must not be null")
    @Column("check_status")
    private Boolean checkStatus;

    @Column("status_note")
    private String statusNote;

    @NotNull(message = "must not be null")
    @Column("check_smell")
    private Boolean checkSmell;

    @Column("smell_note")
    private String smellNote;

    @NotNull(message = "must not be null")
    @Column("check_impurity")
    private Boolean checkImpurity;

    @Column("impurity_note")
    private String impurityNote;

    @NotNull(message = "must not be null")
    @Column("check_poison")
    private Boolean checkPoison;

    @Column("poison_note")
    private String poisonNote;

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
    @Column("material_type")
    private MaterialType materialType;

    @Column("weight")
    private String weight;

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

    public ReceiveMaterialChecklist id(UUID id) {
        this.setId(id);
        return this;
    }

    public ReceiveMaterialChecklist checkDate(LocalDate checkDate) {
        this.setCheckDate(checkDate);
        return this;
    }

    public ReceiveMaterialChecklist checkTime(ZonedDateTime checkTime) {
        this.setCheckTime(checkTime);
        return this;
    }

    public ReceiveMaterialChecklist weightNumber(String weightNumber) {
        this.setWeightNumber(weightNumber);
        return this;
    }

    public ReceiveMaterialChecklist transportCondition(Boolean transportCondition) {
        this.setTransportCondition(transportCondition);
        return this;
    }

    public ReceiveMaterialChecklist transportNote(String transportNote) {
        this.setTransportNote(transportNote);
        return this;
    }

    public ReceiveMaterialChecklist checkStatus(Boolean checkStatus) {
        this.setCheckStatus(checkStatus);
        return this;
    }

    public ReceiveMaterialChecklist statusNote(String statusNote) {
        this.setStatusNote(statusNote);
        return this;
    }

    public ReceiveMaterialChecklist checkSmell(Boolean checkSmell) {
        this.setCheckSmell(checkSmell);
        return this;
    }

    public ReceiveMaterialChecklist smellNote(String smellNote) {
        this.setSmellNote(smellNote);
        return this;
    }

    public ReceiveMaterialChecklist checkImpurity(Boolean checkImpurity) {
        this.setCheckImpurity(checkImpurity);
        return this;
    }

    public ReceiveMaterialChecklist impurityNote(String impurityNote) {
        this.setImpurityNote(impurityNote);
        return this;
    }

    public ReceiveMaterialChecklist checkPoison(Boolean checkPoison) {
        this.setCheckPoison(checkPoison);
        return this;
    }

    public ReceiveMaterialChecklist poisonNote(String poisonNote) {
        this.setPoisonNote(poisonNote);
        return this;
    }

    public ReceiveMaterialChecklist receiverId(UUID receiverId) {
        this.setReceiverId(receiverId);
        return this;
    }

    public ReceiveMaterialChecklist note(String note) {
        this.setNote(note);
        return this;
    }

    public ReceiveMaterialChecklist isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public ReceiveMaterialChecklist createdAt(ZonedDateTime createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public ReceiveMaterialChecklist lastUpdated(ZonedDateTime lastUpdated) {
        this.setLastUpdated(lastUpdated);
        return this;
    }

    @Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public ReceiveMaterialChecklist setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    public ReceiveMaterialChecklist workItem(WorkItem workItem) {
        this.setWorkItem(workItem);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    public ReceiveMaterialChecklistDTO toDto() {
        ReceiveMaterialChecklistDTO dto = new ReceiveMaterialChecklistDTO();
        dto.setId(this.id);
        dto.setCheckDate(this.checkDate);
        if (Objects.nonNull(this.checkDate)) {
            dto.setZonedCheckDate(this.checkDate.atStartOfDay().atZone(ZoneOffset.UTC));
        }
        dto.setCheckTime(this.checkTime);
        dto.setWeightNumber(this.weightNumber);
        dto.setTransportCondition(this.transportCondition);
        dto.setTransportNote(this.transportNote);
        dto.setCheckStatus(this.checkStatus);
        dto.setStatusNote(this.statusNote);
        dto.setCheckSmell(this.checkSmell);
        dto.setSmellNote(this.smellNote);
        dto.setCheckImpurity(this.checkImpurity);
        dto.setImpurityNote(this.impurityNote);
        dto.setCheckPoison(this.checkPoison);
        dto.setPoisonNote(this.poisonNote);
        dto.setReceiverId(this.receiverId);
        dto.setNote(this.note);
        dto.setIsActive(this.isActive);
        dto.setCreatedAt(this.createdAt);
        dto.setLastUpdated(this.lastUpdated);
        dto.setWorkItemId(this.workItemId);
        dto.setMaterialType(this.materialType);
        dto.setWeight(this.weight);
        return dto;
    }}
